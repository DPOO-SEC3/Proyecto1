package Persistencia;

import Modelo.*;
import excepciones.PersistenciaException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Persiste el historial completo de préstamos.
 *
 * Estrategia de referencias:
 *   - El ejemplar se referencia por su ID único (ej. "Catan-1").
 *   - El inventario de préstamo se pasa al cargar para resolver la referencia.
 *   - La mesa no se persiste (es un estado de sesión, no histórico).
 */
public class PersistenciaPrestamos {

    private static final String RUTA = "datos/prestamos.json";

    // GUARDAR
    public void guardarPrestamos(List<Prestamo> prestamos) throws PersistenciaException {
        JSONArray array = new JSONArray();
        for (Prestamo p : prestamos) {
            array.put(prestamoAJson(p));
        }
        escribir(RUTA, array.toString(2));
    }

    // CARGAR
    public List<Prestamo> cargarPrestamos(InventarioPrestamo inventarioPrestamo)
            throws PersistenciaException {
        List<Prestamo> lista = new ArrayList<>();
        if (!Files.exists(Paths.get(RUTA))) return lista;

        String contenido = leer(RUTA);
        JSONArray array = new JSONArray(contenido);

        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            Prestamo prestamo = jsonAPrestamo(obj, inventarioPrestamo);
            if (prestamo != null) lista.add(prestamo);
        }
        return lista;
    }

    // SERIALIZACIÓN

    private JSONObject prestamoAJson(Prestamo p) {
        JSONObject obj = new JSONObject();

        obj.put("idEjemplar",       p.getEjemplar().getID());
        obj.put("fechaHoraInicio",  p.getFechaHoraInicio().toString());
        obj.put("estado",           p.getEstado() != null ? p.getEstado() : "activo");

        if (p.getFechaHoraDevolucion() != null) {
            obj.put("fechaHoraDevolucion", p.getFechaHoraDevolucion().toString());
        } else {
            obj.put("fechaHoraDevolucion", JSONObject.NULL);
        }

        return obj;
    }

    private Prestamo jsonAPrestamo(JSONObject obj, InventarioPrestamo inventarioPrestamo) {
        String idEjemplar = obj.getString("idEjemplar");

        EjemplarJuego ejemplar = null;
        for (EjemplarJuego e : inventarioPrestamo.getEjemplares()) {
            if (e.getID().equals(idEjemplar)) {
                ejemplar = e;
                break;
            }
        }

        if (ejemplar == null) {
            System.out.println("[WARN] Préstamo omitido: ejemplar " + idEjemplar + " no encontrado.");
            return null;
        }

        Prestamo prestamo = new Prestamo(inventarioPrestamo, ejemplar, null);


        prestamo.setFechaHoraInicioManual(LocalDateTime.parse(obj.getString("fechaHoraInicio")));

        if (!obj.isNull("fechaHoraDevolucion")) {
            prestamo.setFechaHoraDevolucionManual(
                LocalDateTime.parse(obj.getString("fechaHoraDevolucion")));
        }

        prestamo.setEstado(obj.getString("estado"));
        return prestamo;
    }

    // AUXILIARES
    private void escribir(String ruta, String contenido) throws PersistenciaException {
        try {
            Files.createDirectories(Paths.get("datos"));
            FileWriter fw = new FileWriter(ruta);
            fw.write(contenido);
            fw.close();
        } catch (IOException e) {
            throw new PersistenciaException(ruta, e);
        }
    }

    private String leer(String ruta) throws PersistenciaException {
        try {
            return new String(Files.readAllBytes(Paths.get(ruta)));
        } catch (IOException e) {
            throw new PersistenciaException(ruta, e);
        }
    }
}