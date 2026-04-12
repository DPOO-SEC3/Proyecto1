package Persistencia;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import Modelo.*;

public class PersistenciaUsuarios {

    // GUARDAR USUARIOS
    public void guardarUsuarios(List<Persona> usuarios, String ruta) throws Exception {

        JSONArray array = new JSONArray();

        for (Persona p : usuarios) {
            JSONObject obj = new JSONObject();

            // Datos comunes
            obj.put("nombre", p.getNombre());
            obj.put("apellido", p.getApellido());
            obj.put("correo", p.getCorreoElectronico());
            obj.put("contrasena", p.getContrasena());
            obj.put("login", p.getLogin());

            // Tipo
            if (p instanceof Cliente) {
                obj.put("tipo", "Cliente");
                obj.put("puntos", ((Cliente) p).getPuntosFidelidad());

            } else if (p instanceof Administrador) {
                obj.put("tipo", "Administrador");

            } else if (p instanceof Mesero) {
                obj.put("tipo", "Mesero");

            } else if (p instanceof Cocinero) {
                obj.put("tipo", "Cocinero");

            } else if (p instanceof Empleado) {
                obj.put("tipo", "Empleado"); // fallback
            }

            array.put(obj);
        }

        JSONObject raiz = new JSONObject();
        raiz.put("usuarios", array);

        Files.write(Paths.get(ruta), raiz.toString(4).getBytes());
    }

    // CARGAR USUARIOS
    public List<Persona> cargarUsuarios(String ruta) throws Exception {

        List<Persona> lista = new ArrayList<>();

        String contenido = new String(Files.readAllBytes(Paths.get(ruta)));
        JSONObject raiz = new JSONObject(contenido);
        JSONArray array = raiz.getJSONArray("usuarios");

        for (int i = 0; i < array.length(); i++) {

            JSONObject obj = array.getJSONObject(i);

            String tipo = obj.getString("tipo");
            String nombre = obj.getString("nombre");
            String apellido = obj.getString("apellido");
            String correo = obj.getString("correo");
            String contrasena = obj.getString("contrasena");
            String login = obj.getString("login");

            Persona p = null;

            if (tipo.equals("Cliente")) {
                double puntos = obj.getDouble("puntos");
                p = new Cliente(nombre, apellido, correo, contrasena, login, puntos);

            } else if (tipo.equals("Administrador")) {
                p = new Administrador(nombre, apellido, correo, contrasena, login);

            } else if (tipo.equals("Mesero")) {
                p = new Mesero(nombre, apellido, correo, contrasena, login);

            } else if (tipo.equals("Cocinero")) {
                p = new Cocinero(nombre, apellido, correo, contrasena, login);

            } else {
                throw new Exception("Tipo de usuario desconocido: " + tipo);
            }

            lista.add(p);
        }

        return lista;
    }
}
