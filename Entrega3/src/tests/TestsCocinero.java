package tests;

import Modelo.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestsCocinero {

    private Cocinero cocinero;
    private JuegoMesa juego;
    private EjemplarJuego ejemplar;
    private InventarioPrestamo inventarioPrestamo;
    private InventarioVenta inventarioVenta;
    private Mesa mesa;

    @BeforeEach
    void setUp() throws Exception {
        cocinero = new Cocinero("Ana", "Lopez", "ana@mail.com", "123", "cocinero");

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
    void testSugerirPlatillo() {
        SugerenciaPlatillo s = cocinero.sugerirPlatillo("Nueva hamburguesa");

        assertNotNull(s);
        assertTrue(cocinero.getSugerencias().contains(s));
    }

    @Test
    void testSolicitarCambioTurno() {
        Cocinero otro = new Cocinero("Luis", "Perez", "l@mail.com", "123", "otro");

        SolicitudCambioTurno solicitud = cocinero.solicitarCambioTurno("Cambio", "Motivo", otro);

        assertNotNull(solicitud);
        assertTrue(cocinero.getSolicitudesCambio().contains(solicitud));
    }

    @Test
    void testSolicitarPrestamo() {
        Prestamo p = cocinero.solicitarPrestamo(inventarioPrestamo, ejemplar, mesa);

        assertNotNull(p);
        assertTrue(cocinero.getPrestamos().contains(p));
    }

    @Test
    void testDevolverJuego() {
        Prestamo p = cocinero.solicitarPrestamo(inventarioPrestamo, ejemplar, mesa);

        cocinero.devolverJuego(p);

        assertFalse(cocinero.getPrestamos().contains(p));
    }

    @Test
    void testComprarJuego() {
        List<JuegoMesa> juegos = new ArrayList<>();
        juegos.add(juego);

        VentaJuego venta = cocinero.comprarJuegos(juegos, "", inventarioVenta);

        assertNotNull(venta);
        assertTrue(cocinero.getVentas().contains(venta));
        assertFalse(inventarioVenta.getJuegos().contains(juego));
    }

    @Test
    void testComprarCafeteria() {
        List<ItemMenu> items = new ArrayList<>();
        items.add(new Bebida(false, "30", "Jugo", "Frío", 4000));

        VentaCafeteria venta = cocinero.comprarCafeteria(items, "", 1000);

        assertNotNull(venta);
        assertTrue(cocinero.getVentas().contains(venta));
    }

    @Test
    void testFavoritos() {
        cocinero.agregarFavorito(juego);
        assertTrue(cocinero.getJuegosFavoritos().contains(juego));

        cocinero.eliminarFavorito(juego);
        assertFalse(cocinero.getJuegosFavoritos().contains(juego));
    }

    @Test
    void testBuscarPrestamoActivo() {
        Prestamo p = cocinero.solicitarPrestamo(inventarioPrestamo, ejemplar, mesa);

        Prestamo encontrado = cocinero.buscarPrestamoActivo("Catan");

        assertEquals(p, encontrado);
    }
}