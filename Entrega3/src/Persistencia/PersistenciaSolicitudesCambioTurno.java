package Persistencia;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import Modelo.*;

public class PersistenciaSolicitudesCambioTurno {

    public void guardarSolicitudes(List<Persona> usuarios, String ruta) throws Exception {
        JSONArray array = new JSONArray();

        for (Persona p : usuarios) {
            if (p instanceof Empleado) {
                Empleado empleado = (Empleado) p;

                for (SolicitudCambioTurno s : empleado.getSolicitudesCambio()) {
                    JSONObject obj = new JSONObject();

                    obj.put("tipo", s.getTipo());
                    obj.put("estado", s.getEstado());
                    obj.put("fecha", s.getFechaDeSolicitud().toString());
                    obj.put("motivo", s.getMotivo());
                    obj.put("solicitanteLogin", s.getSolicitante().getLogin());

                    if (s.getEmpleadoDestino() != null) {
                        obj.put("empleadoDestinoLogin", s.getEmpleadoDestino().getLogin());
                    } else {
                        obj.put("empleadoDestinoLogin", JSONObject.NULL);
                    }

                    array.put(obj);
                }
            }
        }

        JSONObject raiz = new JSONObject();
        raiz.put("solicitudes", array);

        Files.write(Paths.get(ruta), raiz.toString(4).getBytes());
    }

    public void cargarSolicitudes(String ruta, List<Persona> usuarios) throws Exception {
        if (!Files.exists(Paths.get(ruta))) {
            return;
        }

        String contenido = new String(Files.readAllBytes(Paths.get(ruta)));
        JSONObject raiz = new JSONObject(contenido);
        JSONArray array = raiz.getJSONArray("solicitudes");

        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);

            String tipo = obj.getString("tipo");
            String estado = obj.getString("estado");
            String motivo = obj.getString("motivo");
            String solicitanteLogin = obj.getString("solicitanteLogin");

            Empleado solicitante = buscarEmpleado(solicitanteLogin, usuarios);

            Empleado empleadoDestino = null;
            if (!obj.isNull("empleadoDestinoLogin")) {
                empleadoDestino = buscarEmpleado(obj.getString("empleadoDestinoLogin"), usuarios);
            }

            if (solicitante != null) {
                SolicitudCambioTurno solicitud =
                    new SolicitudCambioTurno(tipo, motivo, solicitante, empleadoDestino);

                if (estado.equalsIgnoreCase("aprobada")) {
                    solicitud.aprobar();
                } else if (estado.equalsIgnoreCase("rechazada")) {
                    solicitud.rechazar();
                }

                solicitante.getSolicitudesCambio().add(solicitud);
            }
        }
    }

    private Empleado buscarEmpleado(String login, List<Persona> usuarios) {
        for (Persona p : usuarios) {
            if (p instanceof Empleado && p.getLogin().equals(login)) {
                return (Empleado) p;
            }
        }
        return null;
    }
}