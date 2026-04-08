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
    private static final String NOMBRE="nombre";
    private static final String ANIOPUBLICACION="anioPublicacion";
    private static final String EMPRESAFABRICANTE="empresaFabricante";
    private static final String CATEGORIA="categoria";
    private static final String MINJUGADORES="minimoJugadores";
    private static final String MAXJUGADORES="maximoJugadores";
    private static final String RESTRICCIONEDAD="restriccionEdad";
    private static final String ESDIFICL="esDificil";
    private static final String PRECIOVENTA="precioDeVenta";
    private static final String EJEMPLARES="ejemplares";
    // 1. GUARDAR JUEGOS Y EJEMPLARES
    public void guardarJuegos(List<JuegoMesa> juegos, String ruta) throws Exception {
        JSONObject raiz = new JSONObject();
        JSONArray arrayJuegos = new JSONArray();

        for (JuegoMesa juego : juegos) {
            JSONObject jJson = new JSONObject();
            jJson.put("nombre", juego.getNombre());
            jJson.put("categoria", juego.getCategoria());
            jJson.put("precioDeVenta", juego.getPrecioDeVenta());
            jJson.put()
            // ... agregar los demás atributos de JuegoMesa ...

            JSONArray arrayEjemplares = new JSONArray();
            for (EjemplarJuego ej : juego.getListaEjemplares()) {
                JSONObject eJson = new JSONObject();
                eJson.put("idEjemplar", ej.getNombre()); // Usamos el nombre como ID
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

    // 2. CARGAR JUEGOS Y EJEMPLARES
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
                jJson.getDouble("precioDeVenta")
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

    // 3. GUARDAR INVENTARIOS (Solo IDs)
    public void guardarInventarios(InventarioPrestamo ip, InventarioVenta iv, String ruta) throws Exception {
        JSONObject raiz = new JSONObject();

        // IDs de Juegos en Venta
        JSONArray ventaIds = new JSONArray();
        for (JuegoMesa j : iv.getJuegos()) {
            ventaIds.put(j.getNombre());
        }

        // IDs de Ejemplares en Préstamo
        JSONArray prestamoIds = new JSONArray();
        for (EjemplarJuego ej : ip.getEjemplares()) {
            prestamoIds.put(ej.getNombre()); 
        }

        raiz.put("nombresJuegosVenta", ventaIds);
        raiz.put("idsEjemplaresPrestamo", prestamoIds);
        
        Files.writeString(Paths.get(ruta), raiz.toString(4));
    }

    // 4. CARGAR INVENTARIOS (Reconstrucción de referencias)
    public void cargarInventarios(String ruta, List<JuegoMesa> juegosCargados, InventarioPrestamo ip, InventarioVenta iv) throws Exception {
        String contenido = Files.readString(Paths.get(ruta));
        JSONObject raiz = new JSONObject(contenido);

        // Reconstruir Venta
        JSONArray ventaIds = raiz.getJSONArray("nombresJuegosVenta");
        for (int i = 0; i < ventaIds.length(); i++) {
            String nombreBuscado = ventaIds.getString(i);
            for (JuegoMesa j : juegosCargados) {
                if (j.getNombre().equals(nombreBuscado)) {
                    iv.getJuegos().add(j);
                    break;
                }
            }
        }

        // Reconstruir Préstamo
        JSONArray prestamoIds = raiz.getJSONArray("idsEjemplaresPrestamo");
        for (int i = 0; i < prestamoIds.length(); i++) {
            String idBuscado = prestamoIds.getString(i);
            // Buscamos el ejemplar dentro de todos los juegos
            for (JuegoMesa j : juegosCargados) {
                for (EjemplarJuego ej : j.getListaEjemplares()) {
                    if (ej.getNombre().equals(idBuscado)) {
                        ip.getEjemplares().add(ej);
                        break;
                    }
                }
            }
        }
}
