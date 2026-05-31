package Persistencia;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import Modelo.*;

public class PersistenciaTorneos {

    public void guardarTorneos(List<Torneo> torneos, String ruta) throws Exception {
        JSONArray array = new JSONArray();

        for (Torneo t : torneos) {
            JSONObject obj = new JSONObject();

            obj.put("nombre", t.getNombre());
            obj.put("numeroParticipantes", t.getNumeroParticipantes());
            obj.put("juego", t.getJuego().getNombre());
            obj.put("diaSemana", t.getDiaSemana());
            obj.put("tipo", t.getTipo());

            JSONArray participantes = new JSONArray();
            for (Persona p : t.getParticipantes()) {
                participantes.put(p.getLogin());
            }

            obj.put("participantes", participantes);

            if (t.getGanador() != null) {
                obj.put("ganador", t.getGanador().getLogin());
            } else {
                obj.put("ganador", JSONObject.NULL);
            }

            array.put(obj);
        }

        JSONObject raiz = new JSONObject();
        raiz.put("torneos", array);

        Files.write(Paths.get(ruta), raiz.toString(4).getBytes());
    }

    public List<Torneo> cargarTorneos(String ruta, List<JuegoMesa> juegos, List<Persona> usuarios) throws Exception {
        List<Torneo> torneos = new ArrayList<>();

        if (!Files.exists(Paths.get(ruta))) {
            return torneos;
        }

        String contenido = new String(Files.readAllBytes(Paths.get(ruta)));
        JSONObject raiz = new JSONObject(contenido);
        JSONArray array = raiz.getJSONArray("torneos");

        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);

            String nombre = obj.getString("nombre");
            int numeroParticipantes = obj.getInt("numeroParticipantes");
            String nombreJuego = obj.getString("juego");
            String diaSemana = obj.getString("diaSemana");
            String tipo = obj.getString("tipo");

            JuegoMesa juego = buscarJuego(nombreJuego, juegos);

            if (juego != null) {
                Torneo torneo = new Torneo(nombre, numeroParticipantes, juego, diaSemana, tipo);

                JSONArray participantes = obj.getJSONArray("participantes");
                for (int j = 0; j < participantes.length(); j++) {
                    Persona p = buscarPersona(participantes.getString(j), usuarios);
                    if (p != null) {
                        torneo.agregarParticipante(p);
                    }
                }

                if (!obj.isNull("ganador")) {
                    Persona ganador = buscarPersona(obj.getString("ganador"), usuarios);
                    torneo.setGanador(ganador);
                }

                torneos.add(torneo);
            }
        }

        return torneos;
    }

    private JuegoMesa buscarJuego(String nombre, List<JuegoMesa> juegos) {
        for (JuegoMesa j : juegos) {
            if (j.getNombre().equalsIgnoreCase(nombre)) {
                return j;
            }
        }
        return null;
    }

    private Persona buscarPersona(String login, List<Persona> usuarios) {
        for (Persona p : usuarios) {
            if (p.getLogin().equalsIgnoreCase(login)) {
                return p;
            }
        }
        return null;
    }
}