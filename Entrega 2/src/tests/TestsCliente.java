package tests;

import Modelo.*;
import excepciones.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestsCliente {

    private Cliente cliente;
    private InventarioPrestamo inventarioPrestamo;
    private InventarioVenta inventarioVenta;

    private JuegoMesa juego;
    private EjemplarJuego ejemplar;
    private Mesa mesa;    

    @BeforeEach
    void setUp() throws Exception {
        cliente = new Cliente("Juan", "Perez", "jp@mail.com", "123", "juan", 0);

        inventarioPrestamo = new InventarioPrestamo(10);
        inventarioVenta = new InventarioVenta(10);

        juego = new JuegoMesa("Catan", 2000, "X", "Tablero", 3, 4, "sin restriccion", false, 100);
        ejemplar = new EjemplarJuego("Nuevo", juego);

        juego.agregarEjemplar(ejemplar);
        inventarioPrestamo.agregarEjemplar(ejemplar);

        inventarioVenta.agregarJuego(juego);

        mesa = new Mesa(1, 4);
    }

    @Test
    void testOcuparMesa() {
        cliente.ocuparMesa(mesa, 3, false, false);

        assertEquals(mesa, cliente.getMesa());
        assertTrue(mesa.estaOcupada());
    }

    @Test
    void testLiberarMesa() {
        cliente.ocuparMesa(mesa, 3, false, false);
        cliente.liberarMesa();

        assertNull(cliente.getMesa());
    }

    @Test
    void testSolicitarPrestamo() {
        cliente.ocuparMesa(mesa, 3, false, false);

        Prestamo p = cliente.solicitarPrestamo(inventarioPrestamo, ejemplar, mesa);

        assertNotNull(p);
        assertTrue(cliente.getPrestamos().contains(p));
        assertFalse(inventarioPrestamo.getEjemplares().contains(ejemplar));
    }

    @Test
    void testDevolverJuego() throws Exception {
        cliente.ocuparMesa(mesa, 3, false, false);

        Prestamo p = cliente.solicitarPrestamo(inventarioPrestamo, ejemplar, mesa);

        cliente.devolverJuego(p);

        assertTrue(inventarioPrestamo.getEjemplares().contains(ejemplar));
        assertEquals("Devuelto", p.getEstado());
    }

    @Test
    void testComprarJuego() {
        List<JuegoMesa> juegos = new ArrayList<>();
        juegos.add(juego);

        VentaJuego venta = cliente.comprarJuegos(juegos, "", inventarioVenta);

        assertNotNull(venta);
        assertTrue(cliente.getVentas().contains(venta));
        assertFalse(inventarioVenta.getJuegos().contains(juego));
    }

    @Test
    void testComprarCafeteria() {
        List<ItemMenu> items = new ArrayList<>();
        items.add(new Bebida(false, "55", "Café", "Bebida caliente", 5000));

        VentaCafeteria venta = cliente.comprarCafeteria(items, "", 1000);

        assertNotNull(venta);
        assertTrue(cliente.getVentas().contains(venta));
    }

    @Test
    void testAgregarFavorito() {
        cliente.agregarFavorito(juego);

        assertTrue(cliente.getJuegosFavoritos().contains(juego));
    }

    @Test
    void testEliminarFavorito() {
        cliente.agregarFavorito(juego);
        cliente.eliminarFavorito(juego);

        assertFalse(cliente.getJuegosFavoritos().contains(juego));
    }

    @Test
    void testBuscarPrestamoActivo() {
        cliente.ocuparMesa(mesa, 3, false, false);

        Prestamo p = cliente.solicitarPrestamo(inventarioPrestamo, ejemplar, mesa);

        Prestamo encontrado = cliente.buscarPrestamoActivo("Catan");

        assertEquals(p, encontrado);
    }
}