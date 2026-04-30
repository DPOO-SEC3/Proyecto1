package tests;

import Modelo.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestsAdministrador {

    private Administrador admin;
    private JuegoMesa juego;
    private EjemplarJuego ejemplar;

    @BeforeEach
    void setUp() {
        admin = new Administrador("Admin", "Test", "admin@mail.com", "123", "admin");

        juego = new JuegoMesa("Catan", 2000, "X", "Tablero", 3, 4, "sin restriccion", false, 100);
        ejemplar = new EjemplarJuego("Nuevo", juego);

        juego.agregarEjemplar(ejemplar);
    }

    @Test
    void testAgregarJuegoAVenta() {
        admin.agregarJuegoAVenta(juego);

        assertTrue(admin.getInventarioVenta().getJuegos().contains(juego));
    }

    @Test
    void testAgregarEjemplarAPrestamo() {
        admin.agregarEjemplarAPrestamo(ejemplar);

        assertTrue(admin.getInventarioPrestamo().getEjemplares().contains(ejemplar));
    }

    @Test
    void testMoverDeVentaAPrestamo() {
        admin.agregarJuegoAVenta(juego);
        admin.moverDeVentaAPrestamo(juego);

        assertFalse(admin.getInventarioVenta().getJuegos().contains(juego));
    }

    @Test
    void testRepararEjemplar() {
        ejemplar.setDisponible(false);
        ejemplar.setEstado("dañado");

        admin.repararEjemplar(ejemplar);

        assertTrue(ejemplar.isDisponible());
        assertEquals("disponible", ejemplar.getEstado());
    }

    @Test
    void testMarcarEjemplarDesaparecido() {
        admin.marcarEjemplarDesaparecido(ejemplar);

        assertFalse(ejemplar.isDisponible());
        assertEquals("desaparecido", ejemplar.getEstado());
    }

    @Test
    void testAgregarItemMenu() {
        ItemMenu item = new Bebida(false, "20", "Agua", "Fría", 3000);

        admin.agregarItemMenu(item);

        assertTrue(admin.getItemsMenu().contains(item));
    }

    @Test
    void testCrearTurnoSemanal() {
        Empleado empleado = new Mesero("Juan", "Perez", "jp@mail.com", "123", "mesero");

        TurnoSemanal turno = admin.crearTurnoSemanal(empleado, "Lunes", LocalTime.of(8, 0), LocalTime.of(16, 0));

        assertTrue(admin.getTurnos().contains(turno));
    }

    @Test
    void testModificarTurnoSemanal() {
        Empleado empleado = new Mesero("Juan", "Perez", "jp@mail.com", "123", "mesero");

        TurnoSemanal turno = admin.crearTurnoSemanal(empleado, "Lunes", LocalTime.of(8, 0), LocalTime.of(16, 0));

        admin.modificarTurnoSemanal(turno, "Martes", LocalTime.of(9, 0), LocalTime.of(17, 0));

        assertEquals("Martes", turno.getDiaSemana());
        assertEquals(LocalTime.of(9, 0), turno.getHoraInicio());
    }

    @Test
    void testConsultarVentasVacio() {
        List<Venta> ventas = admin.consultarVentas(LocalDate.now().minusDays(1), LocalDate.now());

        assertTrue(ventas.isEmpty());
    }

    @Test
    void testGenerarReporteVentas() {
        String reporte = admin.generarReporteVentas(LocalDate.now().minusDays(1), LocalDate.now(), "diario");

        assertNotNull(reporte);
        assertTrue(reporte.contains("Ventas"));
    }
}