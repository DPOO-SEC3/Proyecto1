package Persistencia;

import Modelo.EjemplarJuego;
import Modelo.InventarioPrestamo;
import Modelo.InventarioVenta;
import Modelo.JuegoMesa;
import excepciones.CapacidadMaximaSuperadaException;
import excepciones.PersistenciaException;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


public class PersistenciaJuegos {

    private static final String ARCHIVO_JUEGOS       = "datos/juegos.json";
    private static final String ARCHIVO_INVENTARIOS  = "datos/inventarios.json";

    public void guardarJuegos(List<JuegoMesa> juegos) throws PersistenciaException {
        JSONArray arregloJuegos = new JSONArray();

        for (JuegoMesa juego : juegos) {
            arregloJuegos.put(juegoAJson(juego));
        }

        escribirArchivo(ARCHIVO_JUEGOS, arregloJuegos.toString(2));
    }


    public void guardarInventarios(InventarioPrestamo inventarioPrestamo,
                                   InventarioVenta inventarioVenta,
                                   List<JuegoMesa> todosLosJuegos) throws PersistenciaException {
        JSONObject raiz = new JSONObject();

        JSONObject jsonPrestamo = new JSONObject();
        jsonPrestamo.put("capacidadMaxima", inventarioPrestamo.getCapacidadMaxima());

        JSONArray idsEjemplares = new JSONArray();
        for (EjemplarJuego ejemplar : inventarioPrestamo.getEjemplares()) {
            JSONObject refEjemplar = new JSONObject();
            JuegoMesa juegoPadre = ejemplar.getJuegoMesa();
            int indiceJuego = todosLosJuegos.indexOf(juegoPadre);
            int indiceEjemplar = juegoPadre.getEjemplares().indexOf(ejemplar);
            refEjemplar.put("indiceJuego", indiceJuego);
            refEjemplar.put("indiceEjemplar", indiceEjemplar);
            idsEjemplares.put(refEjemplar);
        }
        jsonPrestamo.put("ejemplares", idsEjemplares);
        raiz.put("inventarioPrestamo", jsonPrestamo);

        JSONObject jsonVenta = new JSONObject();
        jsonVenta.put("capacidadMaxima", inventarioVenta.getCapacidadMaxima());

        JSONArray indicesJuegosVenta = new JSONArray();
        for (JuegoMesa juego : inventarioVenta.getJuegos()) {
            indicesJuegosVenta.put(todosLosJuegos.indexOf(juego));
        }
        jsonVenta.put("juegos", indicesJuegosVenta);
        raiz.put("inventarioVenta", jsonVenta);

        escribirArchivo(ARCHIVO_INVENTARIOS, raiz.toString(2));
    }


    public List<JuegoMesa> cargarJuegos() throws PersistenciaException {
        List<JuegoMesa> juegos = new ArrayList<>();

        if (!Files.exists(Paths.get(ARCHIVO_JUEGOS))) {
            return juegos;
        }

        String contenido = leerArchivo(ARCHIVO_JUEGOS);
        JSONArray arregloJuegos = new JSONArray(contenido);

        for (int i = 0; i < arregloJuegos.length(); i++) {
            JSONObject jsonJuego = arregloJuegos.getJSONObject(i);
            JuegoMesa juego = jsonAJuego(jsonJuego);
            juegos.add(juego);
        }

        return juegos;
    }


    public InventarioVenta cargarInventarioVenta(List<JuegoMesa> todosLosJuegos) throws PersistenciaException {

        if (!Files.exists(Paths.get(ARCHIVO_INVENTARIOS))) {
            return new InventarioVenta(100);
        }

        String contenido = leerArchivo(ARCHIVO_INVENTARIOS);
        JSONObject raiz = new JSONObject(contenido);

        JSONObject jsonVenta = raiz.getJSONObject("inventarioVenta");
        int capVenta = jsonVenta.getInt("capacidadMaxima");
        InventarioVenta inventarioVenta = new InventarioVenta(capVenta);

        JSONArray indicesVenta = jsonVenta.getJSONArray("juegos");
        for (int i = 0; i < indicesVenta.length(); i++) {
            int indiceJuego = indicesVenta.getInt(i);
            if (indiceJuego >= 0 && indiceJuego < todosLosJuegos.size()) {
                try {
                    inventarioVenta.agregarJuego(todosLosJuegos.get(indiceJuego));
                } catch (CapacidadMaximaSuperadaException e) {
                    throw new PersistenciaException(ARCHIVO_INVENTARIOS,
                        "Capacidad de inventario de venta superada al cargar: " + e.getMessage());
                }
            }
        }

        return inventarioVenta;
    }

    public InventarioPrestamo cargarInventarioPrestamo(List<JuegoMesa> todosLosJuegos) throws PersistenciaException {

        if (!Files.exists(Paths.get(ARCHIVO_INVENTARIOS))) {
            return new InventarioPrestamo(100);
        }

        String contenido = leerArchivo(ARCHIVO_INVENTARIOS);
        JSONObject raiz = new JSONObject(contenido);

        JSONObject jsonPrestamo = raiz.getJSONObject("inventarioPrestamo");
        int capPrestamo = jsonPrestamo.getInt("capacidadMaxima");
        InventarioPrestamo inventarioPrestamo = new InventarioPrestamo(capPrestamo);

        JSONArray idsEjemplares = jsonPrestamo.getJSONArray("ejemplares");
        for (int i = 0; i < idsEjemplares.length(); i++) {
            JSONObject ref = idsEjemplares.getJSONObject(i);
            int indiceJuego    = ref.getInt("indiceJuego");
            int indiceEjemplar = ref.getInt("indiceEjemplar");

            if (indiceJuego >= 0 && indiceJuego < todosLosJuegos.size()) {
                JuegoMesa juego = todosLosJuegos.get(indiceJuego);
                if (indiceEjemplar >= 0 && indiceEjemplar < juego.getEjemplares().size()) {
                    try {
                        inventarioPrestamo.agregarEjemplar(juego.getEjemplares().get(indiceEjemplar));
                    } catch (CapacidadMaximaSuperadaException e) {
                        throw new PersistenciaException(ARCHIVO_INVENTARIOS,
                            "Capacidad de inventario de préstamo superada al cargar: " + e.getMessage());
                    }
                }
            }
        }
        return inventarioPrestamo;
    }

    private JSONObject juegoAJson(JuegoMesa juego) {
        JSONObject obj = new JSONObject();
        obj.put("nombre",            juego.getNombre());
        obj.put("anioPublicacion",   juego.getAnioPublicacion());
        obj.put("empresaFabricante", juego.getEmpresaFabricante());
        obj.put("categoria",         juego.getCategoria());
        obj.put("minimoJugadores",   juego.getMinimoJugadores());
        obj.put("maximoJugadores",   juego.getMaximoJugadores());
        obj.put("restriccionEdad",   juego.getRestriccionEdad());
        obj.put("esDificil",         juego.esDificil());
        obj.put("precioDeVenta",     juego.getPrecioDeVenta());

        JSONArray arregloEjemplares = new JSONArray();
        for (EjemplarJuego ejemplar : juego.getEjemplares()) {
            arregloEjemplares.put(ejemplarAJson(ejemplar));
        }
        obj.put("ejemplares", arregloEjemplares);

        return obj;
    }

    private JSONObject ejemplarAJson(EjemplarJuego ejemplar) {
        JSONObject obj = new JSONObject();
        obj.put("estado",                ejemplar.getEstado());
        obj.put("disponible",            ejemplar.isDisponible());
        obj.put("desaparecido",          ejemplar.isDesaparecido());
        obj.put("numeroDeVecesPrestado", ejemplar.getNumeroDeVecesPrestado());
        return obj;
    }

    private JuegoMesa jsonAJuego(JSONObject obj) {
        JuegoMesa juego = new JuegoMesa(
            obj.getString("nombre"),
            obj.getInt("anioPublicacion"),
            obj.getString("empresaFabricante"),
            obj.getString("categoria"),
            obj.getInt("minimoJugadores"),
            obj.getInt("maximoJugadores"),
            obj.getString("restriccionEdad"),
            obj.getBoolean("esDificil"),
            obj.getDouble("precioDeVenta")
        );

        JSONArray arregloEjemplares = obj.getJSONArray("ejemplares");
        for (int i = 0; i < arregloEjemplares.length(); i++) {
            JSONObject jsonEjemplar = arregloEjemplares.getJSONObject(i);
            EjemplarJuego ejemplar = new EjemplarJuego(
                jsonEjemplar.getString("estado"),
                juego
            );

            // Restaurar disponibilidad y veces prestado directamente, sin loops costosos
            ejemplar.setDisponible(jsonEjemplar.getBoolean("disponible"));
            ejemplar.setNumeroDeVecesPrestado(jsonEjemplar.getInt("numeroDeVecesPrestado"));

            // Restaurar estado desaparecido si aplica
            if (jsonEjemplar.optBoolean("desaparecido", false)) {
                ejemplar.marcarDesaparecido();
            }

            juego.agregarEjemplar(ejemplar);
        }

        return juego;
    }


    private void escribirArchivo(String ruta, String contenido) throws PersistenciaException {
        try {
            Files.createDirectories(Paths.get("datos"));
            FileWriter writer = new FileWriter(ruta);
            writer.write(contenido);
            writer.close();
        } catch (IOException e) {
            throw new PersistenciaException(ruta, e);
        }
    }


    private String leerArchivo(String ruta) throws PersistenciaException {
        try {
            return new String(Files.readAllBytes(Paths.get(ruta)));
        } catch (IOException e) {
            throw new PersistenciaException(ruta, e);
        }
    }
}