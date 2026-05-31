package tests;

import Persistencia.PersistenciaUsuarios;
import Modelo.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestsPersistenciaUsuarios {

    @Test
    @DisplayName("Guardar y cargar usuarios mantiene la cantidad correcta")
    void testGuardarYCargarUsuariosCantidad() throws Exception {

        PersistenciaUsuarios persistencia = new PersistenciaUsuarios();

        List<Persona> usuarios = new ArrayList<>();

        usuarios.add(new Cliente("Juan", "Perez", "juan@mail.com", "123", "juan", 100));
        usuarios.add(new Administrador("Ana", "Lopez", "ana@mail.com", "123", "ana"));
        usuarios.add(new Mesero("Luis", "Gomez", "luis@mail.com", "123", "luis"));

        String ruta = "test_usuarios.json";

        persistencia.guardarUsuarios(usuarios, ruta);

        List<Persona> cargados = persistencia.cargarUsuarios(ruta);

        assertEquals(usuarios.size(), cargados.size(),
                "La cantidad de usuarios cargados debe ser igual a la guardada.");
    }

    @Test
    @DisplayName("Cargar usuarios mantiene los tipos correctos")
    void testTiposUsuarios() throws Exception {

        PersistenciaUsuarios persistencia = new PersistenciaUsuarios();

        List<Persona> usuarios = new ArrayList<>();

        usuarios.add(new Cliente("Juan", "Perez", "juan@mail.com", "123", "juan", 50));
        usuarios.add(new Administrador("Admin", "Sys", "admin@mail.com", "123", "admin"));

        String ruta = "test_tipos.json";

        persistencia.guardarUsuarios(usuarios, ruta);

        List<Persona> cargados = persistencia.cargarUsuarios(ruta);

        assertTrue(cargados.get(0) instanceof Cliente,
                "El primer usuario debe ser Cliente.");

        assertTrue(cargados.get(1) instanceof Administrador,
                "El segundo usuario debe ser Administrador.");
    }

    @Test
    @DisplayName("Cliente conserva puntos de fidelidad después de cargar")
    void testPuntosCliente() throws Exception {

        PersistenciaUsuarios persistencia = new PersistenciaUsuarios();

        List<Persona> usuarios = new ArrayList<>();

        usuarios.add(new Cliente("Carlos", "Diaz", "carlos@mail.com", "123", "carlos", 200));

        String ruta = "test_puntos.json";

        persistencia.guardarUsuarios(usuarios, ruta);

        List<Persona> cargados = persistencia.cargarUsuarios(ruta);

        Cliente cliente = (Cliente) cargados.get(0);

        assertEquals(200, cliente.getPuntosFidelidad(),
                "Los puntos de fidelidad deben mantenerse después de cargar.");
    }

    @Test
    @DisplayName("Cargar archivo inexistente lanza excepción")
    void testArchivoInexistente() {

        PersistenciaUsuarios persistencia = new PersistenciaUsuarios();

        assertThrows(Exception.class,
                () -> persistencia.cargarUsuarios("no_existe.json"),
                "Cargar un archivo inexistente debe lanzar excepción.");
    }
}