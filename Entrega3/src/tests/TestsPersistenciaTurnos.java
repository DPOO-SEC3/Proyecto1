package tests;

import Persistencia.PersistenciaTurnos;
import Modelo.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestsPersistenciaTurnos {

    @Test
    @DisplayName("Guardar y cargar turnos mantiene la cantidad correcta")
    void testGuardarYCargarTurnosCantidad() throws Exception {

        PersistenciaTurnos persistencia = new PersistenciaTurnos();

        List<Persona> usuarios = new ArrayList<>();
        Empleado mesero = new Mesero("Luis", "Gomez", "luis@mail.com", "123", "luis");
        usuarios.add(mesero);

        List<TurnoSemanal> turnos = new ArrayList<>();
        turnos.add(new TurnoSemanal("Lunes", LocalTime.of(8, 0), LocalTime.of(16, 0), mesero));

        String ruta = "test_turnos.json";

        persistencia.guardarTurnos(turnos, ruta);

        List<TurnoSemanal> cargados = persistencia.cargarTurnos(ruta, usuarios);

        assertEquals(turnos.size(), cargados.size(),
                "La cantidad de turnos cargados debe ser igual a la guardada.");
    }

    @Test
    @DisplayName("Turno cargado conserva día y horas")
    void testDatosTurno() throws Exception {

        PersistenciaTurnos persistencia = new PersistenciaTurnos();

        List<Persona> usuarios = new ArrayList<>();
        Empleado cocinero = new Cocinero("Ana", "Lopez", "ana@mail.com", "123", "ana");
        usuarios.add(cocinero);

        List<TurnoSemanal> turnos = new ArrayList<>();
        turnos.add(new TurnoSemanal("Martes", LocalTime.of(10, 0), LocalTime.of(18, 0), cocinero));

        String ruta = "test_turnos_datos.json";

        persistencia.guardarTurnos(turnos, ruta);

        List<TurnoSemanal> cargados = persistencia.cargarTurnos(ruta, usuarios);

        TurnoSemanal turno = cargados.get(0);

        assertEquals("Martes", turno.getDiaSemana());
        assertEquals(LocalTime.of(10, 0), turno.getHoraInicio());
        assertEquals(LocalTime.of(18, 0), turno.getHoraFin());
    }

    @Test
    @DisplayName("Turno cargado mantiene el empleado correcto")
    void testEmpleadoAsignado() throws Exception {

        PersistenciaTurnos persistencia = new PersistenciaTurnos();

        List<Persona> usuarios = new ArrayList<>();
        Empleado mesero = new Mesero("Carlos", "Diaz", "carlos@mail.com", "123", "carlos");
        usuarios.add(mesero);

        List<TurnoSemanal> turnos = new ArrayList<>();
        turnos.add(new TurnoSemanal("Miercoles", LocalTime.of(9, 0), LocalTime.of(17, 0), mesero));

        String ruta = "test_turnos_empleado.json";

        persistencia.guardarTurnos(turnos, ruta);

        List<TurnoSemanal> cargados = persistencia.cargarTurnos(ruta, usuarios);

        TurnoSemanal turno = cargados.get(0);

        assertEquals("carlos", turno.getEmpleado().getLogin(),
                "El empleado del turno debe coincidir con el guardado.");
    }

    @Test
    @DisplayName("Guardar turno sin empleado lanza excepción")
    void testTurnoSinEmpleado() {

        PersistenciaTurnos persistencia = new PersistenciaTurnos();

        List<TurnoSemanal> turnos = new ArrayList<>();
        turnos.add(new TurnoSemanal("Jueves", LocalTime.of(8, 0), LocalTime.of(16, 0), null));

        assertThrows(Exception.class,
                () -> persistencia.guardarTurnos(turnos, "test_error.json"),
                "No se debe poder guardar un turno sin empleado.");
    }

    @Test
    @DisplayName("Cargar turno con empleado inexistente lanza excepción")
    void testEmpleadoNoExiste() throws Exception {

        PersistenciaTurnos persistencia = new PersistenciaTurnos();

        List<Persona> usuarios = new ArrayList<>();

        List<TurnoSemanal> turnos = new ArrayList<>();
        Empleado mesero = new Mesero("Luis", "Gomez", "luis@mail.com", "123", "luis");
        turnos.add(new TurnoSemanal("Viernes", LocalTime.of(8, 0), LocalTime.of(16, 0), mesero));

        String ruta = "test_turnos_error.json";

        persistencia.guardarTurnos(turnos, ruta);

        assertThrows(Exception.class,
                () -> persistencia.cargarTurnos(ruta, usuarios),
                "Debe lanzar excepción si el empleado no existe al cargar.");
    }
}