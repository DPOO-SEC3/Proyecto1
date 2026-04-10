package Persistencia;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import Modelo.*;

public class PersistenciaTurnos {

    // GUARDAR TURNOS
    public void guardarTurnos(List<TurnoSemanal> turnos, String ruta) throws Exception {

        JSONArray array = new JSONArray();

        for (TurnoSemanal t : turnos) {
            JSONObject obj = new JSONObject();

            obj.put("dia", t.getDiaSemana());
            obj.put("horaInicio", t.getHoraInicio().toString());
            obj.put("horaFin", t.getHoraFin().toString());

            if (t.getEmpleado() == null) {
                throw new Exception("Turno sin empleado asignado");
            }

            obj.put("empleadoLogin", t.getEmpleado().getLogin());

            array.put(obj);
        }

        JSONObject raiz = new JSONObject();
        raiz.put("turnos", array);

        Files.write(Paths.get(ruta), raiz.toString(4).getBytes());
    }

    // CARGAR TURNOS
    public List<TurnoSemanal> cargarTurnos(String ruta, List<Persona> usuarios) throws Exception {

        List<TurnoSemanal> lista = new ArrayList<>();

        String contenido = new String(Files.readAllBytes(Paths.get(ruta)));
        JSONObject raiz = new JSONObject(contenido);
        JSONArray array = raiz.getJSONArray("turnos");

        for (int i = 0; i < array.length(); i++) {

            JSONObject obj = array.getJSONObject(i);

            String dia = obj.getString("dia");
            LocalTime inicio = LocalTime.parse(obj.getString("horaInicio"));
            LocalTime fin = LocalTime.parse(obj.getString("horaFin"));
            String login = obj.getString("empleadoLogin");

            Empleado empleado = buscarEmpleado(login, usuarios);

            if (empleado == null) {
                throw new Exception("No se encontró el empleado con login: " + login);
            }

            TurnoSemanal turno = new TurnoSemanal(dia, inicio, fin, empleado);
            lista.add(turno);
        }

        return lista;
    }

    // MÉTODO AUXILIAR
    private Empleado buscarEmpleado(String login, List<Persona> usuarios) {

        for (Persona p : usuarios) {
            if (p instanceof Empleado && p.getLogin().equals(login)) {
                return (Empleado) p;
            }
        }

        return null;
    }
}