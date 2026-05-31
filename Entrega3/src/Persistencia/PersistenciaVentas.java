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
 * Persiste el historial completo de ventas (VentaJuego y VentaCafeteria).
 *
 * Estrategia de referencias:
 *   - VentaJuego  guarda los nombres de los juegos vendidos (no índices,
 *     porque al vender el juego sale del inventario).
 *   - VentaCafeteria guarda cada ItemMenu embebido con su tipo y campos.
 *   - El comprador se guarda por login y se resuelve contra la lista de usuarios.
 */
public class PersistenciaVentas {

    private static final String RUTA = "datos/ventas.json";

    // GUARDAR
    public void guardarVentas(List<Venta> ventas) throws PersistenciaException {
        JSONArray array = new JSONArray();
        for (Venta v : ventas) {
            if (v instanceof VentaJuego) {
                array.put(ventaJuegoAJson((VentaJuego) v));
            } else if (v instanceof VentaCafeteria) {
                array.put(ventaCafeteriaAJson((VentaCafeteria) v));
            }
        }
        escribir(RUTA, array.toString(2));
    }

    // CARGAR
    public List<Venta> cargarVentas(List<Persona> usuarios,
                                    List<JuegoMesa> todosLosJuegos) throws PersistenciaException {
        List<Venta> lista = new ArrayList<>();
        if (!Files.exists(Paths.get(RUTA))) return lista;

        String contenido = leer(RUTA);
        JSONArray array = new JSONArray(contenido);

        for (int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            String tipo = obj.getString("tipo");
            Persona comprador = buscarPersona(obj.getString("loginComprador"), usuarios);

            if (tipo.equals("VentaJuego")) {
                lista.add(jsonAVentaJuego(obj, comprador, todosLosJuegos));
            } else if (tipo.equals("VentaCafeteria")) {
                lista.add(jsonAVentaCafeteria(obj, comprador));
            }
        }
        return lista;
    }

    // SERIALIZACIÓN(VentaJuego)
    private JSONObject ventaJuegoAJson(VentaJuego v) {
        JSONObject obj = new JSONObject();
        obj.put("tipo",           "VentaJuego");
        obj.put("fechaHora",      v.getFechaHora().toString());
        obj.put("loginComprador", v.getComprador() != null ? v.getComprador().getLogin() : "");
        obj.put("subtotal",       v.getSubtotal());
        obj.put("descuento",      v.getDescuentoAplicado());

        JSONArray juegosArray = new JSONArray();
        for (JuegoMesa j : v.getJuegos()) {
            juegosArray.put(j.getNombre());
        }
        obj.put("juegos", juegosArray);
        return obj;
    }

    private VentaJuego jsonAVentaJuego(JSONObject obj, Persona comprador,
                                        List<JuegoMesa> todosLosJuegos) {
        LocalDateTime fecha = LocalDateTime.parse(obj.getString("fechaHora"));

        // Reconstruir lista de juegos por nombre
        JSONArray juegosArray = obj.getJSONArray("juegos");
        List<JuegoMesa> juegos = new ArrayList<>();
        for (int i = 0; i < juegosArray.length(); i++) {
            String nombre = juegosArray.getString(i);
            for (JuegoMesa j : todosLosJuegos) {
                if (j.getNombre().equalsIgnoreCase(nombre)) {
                    juegos.add(j);
                    break;
                }
            }
        }

        VentaJuego venta = new VentaJuego(juegos, fecha, "", comprador, null);
        venta.calcularSubtotal();
        venta.calcularTotal();
        return venta;
    }


    // SERIALIZACIÓN(VentaCafeteria)
    private JSONObject ventaCafeteriaAJson(VentaCafeteria v) {
        JSONObject obj = new JSONObject();
        obj.put("tipo",           "VentaCafeteria");
        obj.put("fechaHora",      v.getFechaHora().toString());
        obj.put("loginComprador", v.getComprador() != null ? v.getComprador().getLogin() : "");
        obj.put("propina",        v.getPropina());
        obj.put("subtotal",       v.getSubtotal());

        JSONArray itemsArray = new JSONArray();
        for (ItemMenu item : v.getItems()) {
            itemsArray.put(itemMenuAJson(item));
        }
        obj.put("items", itemsArray);
        return obj;
    }

    private VentaCafeteria jsonAVentaCafeteria(JSONObject obj, Persona comprador) {
        LocalDateTime fecha  = LocalDateTime.parse(obj.getString("fechaHora"));
        double propina       = obj.getDouble("propina");

        VentaCafeteria venta = new VentaCafeteria(fecha, "", comprador, propina);

        JSONArray itemsArray = obj.getJSONArray("items");
        for (int i = 0; i < itemsArray.length(); i++) {
            venta.getItems().add(jsonAItemMenu(itemsArray.getJSONObject(i)));
        }
        venta.calcularSubtotal();
        venta.calcularTotal();
        return venta;
    }


    // SERIALIZACIÓN (Bebida / Pasteleria)
    private JSONObject itemMenuAJson(ItemMenu item) {
        JSONObject obj = new JSONObject();
        obj.put("nombre",      item.getNombre());
        obj.put("descripcion", item.getDescripcion());
        obj.put("precioBase",  item.getPrecioBase());

        if (item instanceof Bebida) {
            Bebida b = (Bebida) item;
            obj.put("tipo",         "Bebida");
            obj.put("esAlcoholica", b.isEsAlcoholica());
            obj.put("temperatura",  b.getTemperatura());
        } else if (item instanceof Pasteleria) {
            Pasteleria p = (Pasteleria) item;
            obj.put("tipo", "Pasteleria");
            JSONArray alergenos = new JSONArray();
            for (String a : p.getListaAlergenos()) alergenos.put(a);
            obj.put("alergenos", alergenos);
        }
        return obj;
    }

    private ItemMenu jsonAItemMenu(JSONObject obj) {
        String tipo       = obj.getString("tipo");
        String nombre     = obj.getString("nombre");
        String descripcion = obj.getString("descripcion");
        double precio     = obj.getDouble("precioBase");

        if (tipo.equals("Bebida")) {
            return new Bebida(
                obj.getBoolean("esAlcoholica"),
                obj.getString("temperatura"),
                nombre, descripcion, precio
            );
        } else {
            List<String> alergenos = new ArrayList<>();
            JSONArray arr = obj.getJSONArray("alergenos");
            for (int i = 0; i < arr.length(); i++) alergenos.add(arr.getString(i));
            return new Pasteleria(nombre, descripcion, precio, alergenos);
        }
    }

    // AUXILIARES
    private Persona buscarPersona(String login, List<Persona> usuarios) {
        for (Persona p : usuarios) {
            if (p.getLogin().equals(login)) return p;
        }
        return null;
    }

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