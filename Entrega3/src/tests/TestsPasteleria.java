package tests;

import Modelo.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestsPasteleria {

    private Pasteleria pastel;

    @BeforeEach
    void setUp() {
        List<String> alergenos = new ArrayList<>();
        alergenos.add("gluten");
        alergenos.add("leche");
        pastel = new Pasteleria("Torta", "Torta de chocolate", 10000, alergenos);
    }

    @Test
    void testTieneAlergenoExistente() {
        assertTrue(pastel.tieneAlergeno("gluten"));
    }

    @Test
    void testTieneAlergenoNoExistente() {
        assertFalse(pastel.tieneAlergeno("maní"));
    }

    @Test
    void testAgregarAlergeno() {
        pastel.agregarAlergeno("huevo");
        assertTrue(pastel.tieneAlergeno("huevo"));
    }

    @Test
    void testRemoverAlergeno() {
        pastel.removerAlergeno("gluten");
        assertFalse(pastel.tieneAlergeno("gluten"));
    }

    @Test
    void testGetListaAlergenos() {
        assertTrue(pastel.getListaAlergenos().contains("gluten"));
        assertTrue(pastel.getListaAlergenos().contains("leche"));
    }

    @Test
    void testPasteleriaVaciaDeAlergenos() {
        List<String> sinAlergenos = new ArrayList<>();
        Pasteleria sinRestricciones = new Pasteleria("Galleta", "Galleta de vainilla", 2000, sinAlergenos);
        assertTrue(sinRestricciones.getListaAlergenos().isEmpty());
    }
}