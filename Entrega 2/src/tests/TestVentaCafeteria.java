package tests;

import Modelo.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestsVentaCafeteria {

    private Cliente cliente;
    private ItemMenu bebida;
    private ItemMenu pastel;

    @BeforeEach
    void setUp() {
        cliente = new Cliente("Ana", "Garcia", "ana@mail.com", "1234", "ana", 0);

        bebida = new Bebida(false, "20", "Agua", "Fría", 3000);

        List<String> alergenos = new ArrayList<>();
        alergenos.add("gluten");
        pastel = new Pasteleria("Torta", "Torta de chocolate", 10000, alergenos);
    }

    @Test
    void testCalcularSubtotal() {
        VentaCafeteria venta = new VentaCafeteria(LocalDateTime.now(), "", cliente, 0);
        venta.getItems().add(bebida);
        venta.getItems().add(pastel);

        assertEquals(13000, venta.calcularSubtotal());
    }

    @Test
    void testCalcularTotal() {
        VentaCafeteria venta = new VentaCafeteria(LocalDateTime.now(), "", cliente, 0);
        venta.getItems().add(bebida);
        venta.calcularSubtotal();

        assertEquals(3000 + 3000 * 0.08, venta.calcularTotal());
    }

    @Test
    void testCalcularTotalConPropina() {
        VentaCafeteria venta = new VentaCafeteria(LocalDateTime.now(), "", cliente, 2000);
        venta.getItems().add(bebida);
        venta.calcularSubtotal();

        assertEquals(3000 + 3000 * 0.08 + 2000, venta.calcularTotal());
    }

    @Test
    void testGetImpuestoConsumo() {
        VentaCafeteria venta = new VentaCafeteria(LocalDateTime.now(), "", cliente, 0);
        venta.getItems().add(bebida);
        venta.calcularSubtotal();

        assertEquals(3000 * 0.08, venta.getImpuestoConsumo());
    }

    @Test
    void testSetPropina() {
        VentaCafeteria venta = new VentaCafeteria(LocalDateTime.now(), "", cliente, 0);
        venta.setPropina(5000);

        assertEquals(5000, venta.getPropina());
    }

    @Test
    void testGetItems() {
        VentaCafeteria venta = new VentaCafeteria(LocalDateTime.now(), "", cliente, 0);
        venta.getItems().add(bebida);
        venta.getItems().add(pastel);

        assertTrue(venta.getItems().contains(bebida));
        assertTrue(venta.getItems().contains(pastel));
    }
}