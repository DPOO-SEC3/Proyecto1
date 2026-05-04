package tests;

import Modelo.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestsBebida {

    private Mesa mesaConMenores;
    private Mesa mesaSinMenores;
    private Mesa mesaConJuegoAccion;

    @BeforeEach
    void setUp() {
        Cliente cliente = new Cliente("Ana", "Garcia", "ana@mail.com", "1234", "ana", 0);

        mesaConMenores = new Mesa(1, 4);
        mesaConMenores.ocupar(4, false, true, cliente);

        mesaSinMenores = new Mesa(2, 4);
        mesaSinMenores.ocupar(2, false, false, cliente);

        JuegoMesa juegoAccion = new JuegoMesa("Twister", 1966, "Hasbro", "Accion", 2, 6, "sin restriccion", false, 45000);
        EjemplarJuego ejemplar = new EjemplarJuego("Nuevo", juegoAccion);
        juegoAccion.agregarEjemplar(ejemplar);

        mesaConJuegoAccion = new Mesa(3, 4);
        mesaConJuegoAccion.ocupar(2, false, false, cliente);

        InventarioPrestamo inventario = new InventarioPrestamo(10);
        try {
            inventario.agregarEjemplar(ejemplar);
        } catch (Exception e) {
            fail("No debería lanzar excepción en setUp: " + e.getMessage());
        }
        Prestamo prestamo = new Prestamo(inventario, ejemplar, mesaConJuegoAccion);
        mesaConJuegoAccion.registrarPrestamo(prestamo);
    }

    @Test
    void testEsCalienteTemperaturaAlta() {
        Bebida bebida = new Bebida(false, "60", "Café", "Caliente", 5000);
        assertTrue(bebida.esCaliente());
    }

    @Test
    void testEsCalienteTemperaturaBaja() {
        Bebida bebida = new Bebida(false, "20", "Agua", "Fría", 3000);
        assertFalse(bebida.esCaliente());
    }

    @Test
    void testAptaParaMesaAlcoholicaConMenores() {
        Bebida bebida = new Bebida(true, "20", "Cerveza", "Fría", 8000);
        assertFalse(bebida.aptaParaMesa(mesaConMenores));
    }

    @Test
    void testAptaParaMesaAlcoholicaSinMenores() {
        Bebida bebida = new Bebida(true, "20", "Cerveza", "Fría", 8000);
        assertTrue(bebida.aptaParaMesa(mesaSinMenores));
    }

    @Test
    void testAptaParaMesaCalienteConJuegoAccion() {
        Bebida bebida = new Bebida(false, "60", "Café", "Caliente", 5000);
        assertFalse(bebida.aptaParaMesa(mesaConJuegoAccion));
    }

    @Test
    void testAptaParaMesaCalienteSinJuegoAccion() {
        Bebida bebida = new Bebida(false, "60", "Café", "Caliente", 5000);
        assertTrue(bebida.aptaParaMesa(mesaSinMenores));
    }

    @Test
    void testAptaParaMesaNormalSinRestricciones() {
        Bebida bebida = new Bebida(false, "20", "Agua", "Fría", 3000);
        assertTrue(bebida.aptaParaMesa(mesaConMenores));
    }
}