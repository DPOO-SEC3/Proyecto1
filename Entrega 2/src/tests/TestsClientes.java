package tests;

import Modelo.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestsClientes {

    private Cliente cliente;
    private Mesa mesa;
    private JuegoMesa juego;
    private EjemplarJuego ejemplar;
    private InventarioPrestamo inventarioPrestamo;
    private InventarioVenta inventarioVenta;

    @BeforeEach
    void setUp() {
        cliente = new Cliente("Ana", "Garcia", "ana@mail.com", "1234", "ana", 0);
        mesa = new Mesa(1, 4);
        mesa.ocupar(2, false, false, cliente);

        juego = new JuegoMesa("Catan", 2000, "Kosmos", "Tablero", 3, 4, "sin restriccion", false, 100000);
        ejemplar = new EjemplarJuego("Nuevo", juego);
        juego.agregarEjemplar(ejemplar);

        inventarioPrestamo = new InventarioPrestamo(10);
        inventarioVenta = new InventarioVenta(10);
        try {
            inventarioPrestamo.agregarEjemplar(ejemplar);
            inventarioVenta.agregarJuego(juego);
        } catch (Exception e) {
            fail("No debería lanzar excepción en setUp: " + e.getMessage());
        }
    }

    @Test
    void testSolicitarPrestamo() {
        Prestamo prestamo = cliente.solicitarPrestamo(inventarioPrestamo, ejemplar, mesa);
        assertTrue(cliente.getPrestamos().contains(prestamo));
    }

    @Test
    void testSolicitarPrestamoRemoveEjemplarDelInventario() {
        cliente.solicitarPrestamo(inventarioPrestamo, ejemplar, mesa);
        assertFalse(inventarioPrestamo.getEjemplares().contains(ejemplar));
    }

    @Test
    void testDevolverJuego() {
        Prestamo prestamo = cliente.solicitarPrestamo(inventarioPrestamo, ejemplar, mesa);
        cliente.devolverJuego(prestamo);
        assertEquals("Devuelto", prestamo.getEstado());
    }

    @Test
    void testComprarJuegos() {
        List<JuegoMesa> juegos = new ArrayList<>();
        juegos.add(juego);

        VentaJuego venta = cliente.comprarJuegos(juegos, "", inventarioVenta);
        assertTrue(cliente.getVentas().contains(venta));
    }

    @Test
    void testComprarJuegosRemoveDelInventario() {
        List<JuegoMesa> juegos = new ArrayList<>();
        juegos.add(juego);

        cliente.comprarJuegos(juegos, "", inventarioVenta);
        assertFalse(inventarioVenta.getJuegos().contains(juego));
    }

    @Test
    void testComprarCafeteria() {
        List<ItemMenu> items = new ArrayList<>();
        items.add(new Bebida(false, "20", "Agua", "Fría", 3000));

        VentaCafeteria venta = cliente.comprarCafeteria(items, "", 0);
        assertTrue(cliente.getVentas().contains(venta));
    }

    @Test
    void testAgregarFavorito() {
        cliente.agregarFavorito(juego);
        assertTrue(cliente.getJuegosFavoritos().contains(juego));
    }

    @Test
    void testAgregarFavoritoDuplicado() {
        cliente.agregarFavorito(juego);
        cliente.agregarFavorito(juego);
        assertEquals(1, cliente.getJuegosFavoritos().size());
    }

    @Test
    void testEliminarFavorito() {
        cliente.agregarFavorito(juego);
        cliente.eliminarFavorito(juego);
        assertFalse(cliente.getJuegosFavoritos().contains(juego));
    }

    @Test
    void testOcuparMesa() {
        Cliente otroCliente = new Cliente("Luis", "Perez", "luis@mail.com", "1234", "luis", 0);
        Mesa otraMesa = new Mesa(2, 4);
        otroCliente.ocuparMesa(otraMesa, 3, false, false);
        assertEquals(otraMesa, otroCliente.getMesa());
    }
}
