package tests;

import Modelo.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestsMesero {

    private Mesero mesero;
    private JuegoMesa juego;
    private EjemplarJuego ejemplar;
    private InventarioPrestamo inventarioPrestamo;
    private InventarioVenta inventarioVenta;
    private Mesa mesa;

    @BeforeEach
    void setUp() throws Exception {
        mesero = new Mesero("Carlos", "Gomez", "c@mail.com", "123", "mesero");

        inventarioPrestamo = new InventarioPrestamo(10);
        inventarioVenta = new InventarioVenta(10);

        juego = new JuegoMesa("Uno", 2000, "X", "Cartas", 2, 6, "sin restriccion", false, 50);
        ejemplar = new EjemplarJuego("Nuevo", juego);

        juego.agregarEjemplar(ejemplar);
        inventarioPrestamo.agregarEjemplar(ejemplar);
        inventarioVenta.agregarJuego(juego);

        mesa = new Mesa(1, 4);
    }

    @Test
    void testConoceJuego() {
        mesero.agregarJuegoConocido(juego);

        assertTrue(mesero.conoceJuego(juego));
    }

    @Test
    void testAtenderMesa() {
        mesero.atenderMesa(mesa);

        assertTrue(mesero.getMesasAtendidas().contains(mesa));
    }

    @Test
    void testDesatenderMesa() {
        mesero.atenderMesa(mesa);
        mesero.desatenderMesa(mesa);

        assertFalse(mesero.getMesasAtendidas().contains(mesa));
    }

    @Test
    void testSolicitarPrestamo() {
        Prestamo p = mesero.solicitarPrestamo(inventarioPrestamo, ejemplar, mesa);

        assertNotNull(p);
        assertTrue(mesero.getPrestamos().contains(p));
    }

    @Test
    void testDevolverJuego() {
        Prestamo p = mesero.solicitarPrestamo(inventarioPrestamo, ejemplar, mesa);

        mesero.devolverJuego(p);

        assertFalse(mesero.getPrestamos().contains(p));
    }

    @Test
    void testComprarJuego() {
        List<JuegoMesa> juegos = new ArrayList<>();
        juegos.add(juego);

        VentaJuego venta = mesero.comprarJuegos(juegos, "", inventarioVenta);

        assertNotNull(venta);
        assertTrue(mesero.getVentas().contains(venta));
    }

    @Test
    void testComprarCafeteria() {
        List<ItemMenu> items = new ArrayList<>();
        items.add(new Bebida(false, "25", "Té", "Caliente", 3000));

        VentaCafeteria venta = mesero.comprarCafeteria(items, "", 500);

        assertNotNull(venta);
        assertTrue(mesero.getVentas().contains(venta));
    }

    @Test
    void testFavoritos() {
        mesero.agregarFavorito(juego);
        assertTrue(mesero.getJuegosFavoritos().contains(juego));

        mesero.eliminarFavorito(juego);
        assertFalse(mesero.getJuegosFavoritos().contains(juego));
    }

    @Test
    void testBuscarPrestamoActivo() {
        Prestamo p = mesero.solicitarPrestamo(inventarioPrestamo, ejemplar, mesa);

        Prestamo encontrado = mesero.buscarPrestamoActivo("Uno");

        assertEquals(p, encontrado);
    }
}