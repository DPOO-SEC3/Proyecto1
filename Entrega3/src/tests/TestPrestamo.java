package tests;

import Modelo.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestPrestamo {

    private Cliente cliente;
    private Mesa mesa;
    private JuegoMesa juego;
    private EjemplarJuego ejemplar;
    private InventarioPrestamo inventarioPrestamo;

    @BeforeEach
    void setUp() {
        cliente = new Cliente("Ana", "Garcia", "ana@mail.com", "1234", "ana", 0);
        mesa = new Mesa(1, 4);
        mesa.ocupar(2, false, false, cliente);

        juego = new JuegoMesa("Catan", 2000, "Kosmos", "Tablero", 3, 4, "sin restriccion", false, 100000);
        ejemplar = new EjemplarJuego("Nuevo", juego);
        juego.agregarEjemplar(ejemplar);

        inventarioPrestamo = new InventarioPrestamo(10);
        try {
            inventarioPrestamo.agregarEjemplar(ejemplar);
        } catch (Exception e) {
            fail("No debería lanzar excepción en setUp: " + e.getMessage());
        }
    }

    @Test
    void testPrestamoSeCreaConFechaInicio() {
        Prestamo prestamo = new Prestamo(inventarioPrestamo, ejemplar, mesa);
        assertNotNull(prestamo.getFechaHoraInicio());
    }

    @Test
    void testPrestamoTieneMesa() {
        Prestamo prestamo = new Prestamo(inventarioPrestamo, ejemplar, mesa);
        assertEquals(mesa, prestamo.getMesa());
    }

    @Test
    void testPrestamoTieneEjemplar() {
        Prestamo prestamo = new Prestamo(inventarioPrestamo, ejemplar, mesa);
        assertEquals(ejemplar, prestamo.getEjemplar());
    }

    @Test
    void testSetEstado() {
        Prestamo prestamo = new Prestamo(inventarioPrestamo, ejemplar, mesa);
        prestamo.setEstado("Devuelto");
        assertEquals("Devuelto", prestamo.getEstado());
    }

    @Test
    void testDevolverJuego() {
        Prestamo prestamo = cliente.solicitarPrestamo(inventarioPrestamo, ejemplar, mesa);
        cliente.devolverJuego(prestamo);
        assertEquals("Devuelto", prestamo.getEstado());
    }

    @Test
    void testEjemplarNoDisponibleDespuesDePrestamo() {
        cliente.solicitarPrestamo(inventarioPrestamo, ejemplar, mesa);
        assertFalse(inventarioPrestamo.getEjemplares().contains(ejemplar));
    }

    @Test
    void testEjemplarDisponibleDespuesDeDevolucion() {
        Prestamo prestamo = cliente.solicitarPrestamo(inventarioPrestamo, ejemplar, mesa);
        cliente.devolverJuego(prestamo);
        assertTrue(inventarioPrestamo.getEjemplares().contains(ejemplar));
    }
}