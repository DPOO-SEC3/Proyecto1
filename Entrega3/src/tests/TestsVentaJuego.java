package tests;

import Modelo.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestsVentaJuego {

    private Cliente cliente;
    private JuegoMesa juego1;
    private JuegoMesa juego2;
    private InventarioVenta inventarioVenta;

    @BeforeEach
    void setUp() {
        cliente = new Cliente("Ana", "Garcia", "ana@mail.com", "1234", "ana", 0);

        juego1 = new JuegoMesa("Catan", 2000, "Kosmos", "Tablero", 3, 4, "sin restriccion", false, 100000);
        juego2 = new JuegoMesa("Uno", 1971, "Mattel", "Cartas", 2, 10, "sin restriccion", false, 25000);

        inventarioVenta = new InventarioVenta(10);
        try {
            inventarioVenta.agregarJuego(juego1);
            inventarioVenta.agregarJuego(juego2);
        } catch (Exception e) {
            fail("No debería lanzar excepción en setUp: " + e.getMessage());
        }
    }

    @Test
    void testCalcularSubtotal() {
        List<JuegoMesa> juegos = new ArrayList<>();
        juegos.add(juego1);
        juegos.add(juego2);

        VentaJuego venta = new VentaJuego(juegos, LocalDateTime.now(), "", cliente, inventarioVenta);

        assertEquals(125000, venta.calcularSubtotal());
    }

    @Test
    void testCalcularTotal() {
        List<JuegoMesa> juegos = new ArrayList<>();
        juegos.add(juego1);

        VentaJuego venta = new VentaJuego(juegos, LocalDateTime.now(), "", cliente, inventarioVenta);
        venta.calcularSubtotal();

        assertEquals(100000 + 100000 * 0.19, venta.calcularTotal());
    }

    @Test
    void testGetIVA() {
        List<JuegoMesa> juegos = new ArrayList<>();
        juegos.add(juego1);

        VentaJuego venta = new VentaJuego(juegos, LocalDateTime.now(), "", cliente, inventarioVenta);
        venta.calcularSubtotal();

        assertEquals(100000 * 0.19, venta.getIVA(venta.calcularSubtotal()));
    }

    @Test
    void testSubtotalUnJuego() {
        List<JuegoMesa> juegos = new ArrayList<>();
        juegos.add(juego1);

        VentaJuego venta = new VentaJuego(juegos, LocalDateTime.now(), "", cliente, inventarioVenta);

        assertEquals(100000, venta.calcularSubtotal());
    }

    @Test
    void testGetJuegos() {
        List<JuegoMesa> juegos = new ArrayList<>();
        juegos.add(juego1);
        juegos.add(juego2);

        VentaJuego venta = new VentaJuego(juegos, LocalDateTime.now(), "", cliente, inventarioVenta);

        assertTrue(venta.getJuegos().contains(juego1));
        assertTrue(venta.getJuegos().contains(juego2));
    }
}