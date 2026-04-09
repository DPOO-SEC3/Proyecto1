package Persistencia;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import Modelo.EjemplarJuego;
import Modelo.InventarioPrestamo;
import Modelo.InventarioVenta;
import Modelo.JuegoMesa;

public class PersistenciaJuegos {

    public void guardarJuegos(List<JuegoMesa> juegos, String ruta) throws Exception {
        JSONObject raiz = new JSONObject();
        JSONArray arrayJuegos = new JSONArray();

        for (JuegoMesa juego : juegos) {
            JSONObject jJson = new JSONObject();
            jJson.put("nombre", juego.getNombre());
            jJson.put("categoria", juego.getCategoria());
            jJson.put("precioDeVenta", juego.getPrecioDeVenta());
            jJson.put("anioPublicacion",juego.getAnioPublicacion());
            jJson.put("empresaFabricante",juego.getEmpresaFabricante());
            jJson.put("minimoJugadores",juego.getMinimoJugadores());
            jJson.put("maximoJugadores",juego.getMaximoJugadores());
            jJson.put("restriccionesEdad",juego.getRestriccionesEdad());
            jJson.put("esDificil",juego.esDificil());
            jJson.put("ejemplares",juego.getListaEjemplares());

            JSONArray arrayEjemplares = new JSONArray();
            for (EjemplarJuego ej : juego.getListaEjemplares()) {
                JSONObject eJson = new JSONObject();
                eJson.put("nombre", ej.getNombre()); // Usamos el nombre como ID
                eJson.put("estado", ej.getEstado());
                eJson.put("disponible", ej.isDisponible());
                arrayEjemplares.put(eJson);
            }
            jJson.put("ejemplares", arrayEjemplares);
            arrayJuegos.put(jJson);
        }
        raiz.put("catalogo", arrayJuegos);
        Files.writeString(Paths.get(ruta), raiz.toString(4));
    }
    public List<JuegoMesa> cargarJuegos(String ruta) throws Exception {
        List<JuegoMesa> listaCargada = new ArrayList<>();
        String contenido = Files.readString(Paths.get(ruta));
        JSONObject raiz = new JSONObject(contenido);
        JSONArray arrayJuegos = raiz.getJSONArray("catalogo");

        for (int i = 0; i < arrayJuegos.length(); i++) {
            JSONObject jJson = arrayJuegos.getJSONObject(i);
            
            // Construir JuegoMesa
            JuegoMesa juego = new JuegoMesa(
                jJson.getString("nombre"),
                jJson.getString("categoria"),
                jJson.getDouble("precioDeVenta"),
                jJson.getString("categoria"),

            );

            // Construir sus Ejemplares
            JSONArray arrayEjemplares = jJson.getJSONArray("ejemplares");
            for (int k = 0; k < arrayEjemplares.length(); k++) {
                JSONObject eJson = arrayEjemplares.getJSONObject(k);
                EjemplarJuego ej = new EjemplarJuego(
                    eJson.getString("idEjemplar"), 
                    juego // Relación bidireccional
                );
                ej.setEstado(eJson.getString("estado"));
                ej.setDisponible(eJson.getBoolean("disponible"));
                juego.getListaEjemplares().add(ej);
            }
            listaCargada.add(juego);
        }
        return listaCargada;
    }
    
}
