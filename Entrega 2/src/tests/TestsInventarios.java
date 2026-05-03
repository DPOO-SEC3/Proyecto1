package tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import Modelo.*;
import excepciones.CapacidadMaximaSuperadaException;
import excepciones.JuegoNoDisponibleException;
import excepciones.JuegoNoExistenteException;
import excepciones.JuegoNoAptoParaMesaException;

@DisplayName("Pruebas de Inventarios y Juegos")
class TestsInventarios {

    private Administrador administrador;
    private InventarioPrestamo inventarioPrestamo;
    private InventarioVenta inventarioVenta;
    private JuegoMesa juego1, juego2, juego3;
    private EjemplarJuego ejemplar1, ejemplar2;
    private Mesa mesa1;
    private Cliente cliente;

    @BeforeEach
    void setUp() {
        // Inicializar administrador
        administrador = new Administrador("Juan", "Pérez", "juan@example.com", "password123", "jperez");

        // Obtener inventarios
        inventarioPrestamo = administrador.getInventarioPrestamo();
        inventarioVenta = administrador.getInventarioVenta();

        // Crear juegos de prueba
        juego1 = new JuegoMesa("Ajedrez", 2020, "ChessBox", "Estrategia", 2, 2, "mayor de 8", false, 50000);
        juego2 = new JuegoMesa("Monopoly", 2019, "Hasbro", "Economía", 2, 6, "mayor de 8", false, 80000);
        juego3 = new JuegoMesa("Action Game", 2021, "GameStudio", "Accion", 1, 4, "general", true, 100000);

        // Crear ejemplares
        ejemplar1 = new EjemplarJuego("disponible", juego1);
        ejemplar2 = new EjemplarJuego("disponible", juego2);

        juego1.agregarEjemplar(ejemplar1);
        juego2.agregarEjemplar(ejemplar2);

        // Crear mesa
        mesa1 = new Mesa(1, 6);
        cliente = new Cliente("Carlos", "López", "carlos@example.com", "pass456", "clopez",0);
    }

    // PRUEBAS DE AGREGAR JUEGOS/EJEMPLARES

    @Test
    @DisplayName("Agregar ejemplar de juego al inventario de préstamo")
    void testAgregarEjemplarAInventarioPrestamo() {
        assertTrue(inventarioPrestamo.getEjemplares().isEmpty(), "El inventario debe estar vacío inicialmente");

        administrador.agregarEjemplarAPrestamo(ejemplar1);

        assertFalse(inventarioPrestamo.getEjemplares().isEmpty(), "El inventario no debe estar vacío");
        assertTrue(inventarioPrestamo.getEjemplares().contains(ejemplar1), "El ejemplar debe estar en el inventario");
        assertEquals(1, inventarioPrestamo.getEjemplares().size(), "Debe haber exactamente 1 ejemplar");
    }

    @Test
    @DisplayName("Agregar juego al inventario de venta")
    void testAgregarJuegoAInventarioVenta() {
        assertTrue(inventarioVenta.getJuegos().isEmpty(), "El inventario debe estar vacío inicialmente");

        administrador.agregarJuegoAVenta(juego1);

        assertFalse(inventarioVenta.getJuegos().isEmpty(), "El inventario no debe estar vacío");
        assertTrue(inventarioVenta.getJuegos().contains(juego1), "El juego debe estar en el inventario");
        assertEquals(1, inventarioVenta.getJuegos().size(), "Debe haber exactamente 1 juego");
    }

    @Test
    @DisplayName("Agregar múltiples ejemplares al inventario de préstamo")
    void testAgregarMultiplesEjemplares() {
        EjemplarJuego ejemplar3 = new EjemplarJuego("disponible", juego1);
        juego1.agregarEjemplar(ejemplar3);

        administrador.agregarEjemplarAPrestamo(ejemplar1);
        administrador.agregarEjemplarAPrestamo(ejemplar3);

        assertEquals(2, inventarioPrestamo.getEjemplares().size(), "Debe haber 2 ejemplares");
        assertTrue(inventarioPrestamo.getEjemplares().contains(ejemplar1));
        assertTrue(inventarioPrestamo.getEjemplares().contains(ejemplar3));
    }

    // PRUEBAS DE QUITAR JUEGOS/EJEMPLARES 

    @Test
    @DisplayName("Quitar juego de inventario de venta")
    void testQuitarJuegoDeInventarioVenta() {
        administrador.agregarJuegoAVenta(juego1);
        assertTrue(inventarioVenta.getJuegos().contains(juego1));

        administrador.moverDeVentaAPrestamo(juego1);

        assertFalse(inventarioVenta.getJuegos().contains(juego1), "El juego debe removerse del inventario");
        assertTrue(inventarioVenta.getJuegos().isEmpty());
    }

    @Test
    @DisplayName("Quitar ejemplar de inventario de préstamo")
    void testQuitarEjemplarDeInventarioPrestamo() {
        administrador.agregarEjemplarAPrestamo(ejemplar1);
        assertEquals(1, inventarioPrestamo.getEjemplares().size());

        inventarioPrestamo.removerEjemplar(ejemplar1);

        assertEquals(0, inventarioPrestamo.getEjemplares().size(), "El ejemplar debe removerse");
        assertFalse(inventarioPrestamo.getEjemplares().contains(ejemplar1));
    }

    // PRUEBAS DE ACCESO A JUEGOS NO DISPONIBLES 

    @Test
    @DisplayName("No permitir acceso a juego que no está en inventario de venta")
    void testAccesoJuegoNoExistenteEnVenta() {
        assertThrows(JuegoNoExistenteException.class, () -> {
            inventarioVenta.buscarJuego("Juego Inexistente");
        }, "Debe lanzar excepción cuando busca un juego que no existe");
    }

    @Test
    @DisplayName("No permitir acceso a juego que no está en inventario de préstamo")
    void testAccesoEjemplarNoDisponibleEnPrestamo() {
        assertThrows(JuegoNoDisponibleException.class, () -> {
            inventarioPrestamo.buscarEjemplarDisponible("Ajedrez");
        }, "Debe lanzar excepción cuando busca un ejemplar que no existe");
    }

    @Test
    @DisplayName("Buscar juego existente en inventario de venta")
    void testBuscarJuegoExistenteEnVenta() {
        administrador.agregarJuegoAVenta(juego1);

        JuegoMesa juegoBuscado;
		try {
			juegoBuscado = inventarioVenta.buscarJuego("Ajedrez");
			assertNotNull(juegoBuscado, "El juego debe encontrarse");
	        assertEquals("Ajedrez", juegoBuscado.getNombre());
		} catch (JuegoNoExistenteException e) {
			e.printStackTrace();
		}

    }

    @Test
    @DisplayName("Buscar ejemplar disponible en inventario de préstamo")
    void testBuscarEjemplarDisponibleEnPrestamo() {
        administrador.agregarEjemplarAPrestamo(ejemplar1);

        EjemplarJuego ejemplarBuscado;
		try {
			ejemplarBuscado = inventarioPrestamo.buscarEjemplarDisponible("Ajedrez");
			assertNotNull(ejemplarBuscado, "El ejemplar debe encontrarse");
	        assertEquals(ejemplar1.getID(), ejemplarBuscado.getID());
		} catch (JuegoNoDisponibleException e) {
			e.printStackTrace();
		}
    }

    //  PRUEBAS DE PRÉSTAMO Y CAMBIO DE ESTADO

    @Test
    @DisplayName("Al hacer un préstamo, el ejemplar se quita del inventario")
    void testPrestamoEliminaDelInventario() {
        administrador.agregarEjemplarAPrestamo(ejemplar1);
        assertEquals(1, inventarioPrestamo.getEjemplares().size());

        mesa1.setNumeroPersonas(2);
        mesa1.setClientes(cliente);

        Prestamo prestamo = new Prestamo(inventarioPrestamo, ejemplar1, mesa1);
        mesa1.registrarPrestamo(prestamo);

        assertFalse(ejemplar1.isDisponible(), "El ejemplar debe marcarse como no disponible");
    }

    @Test
    @DisplayName("Reparar un ejemplar lo marca como disponible")
    void testRepararEjemplarDisponible() {
        ejemplar1.setEstado("dañado");

        administrador.repararEjemplar(ejemplar1);

        assertEquals("Reparado",ejemplar1.getEstado());
    }

    @Test
    @DisplayName("Marcar ejemplar como desaparecido")
    void testMarcarEjemplarDesaparecido() {
        assertTrue(ejemplar1.isDisponible());

        administrador.marcarEjemplarDesaparecido(ejemplar1);

        assertFalse(ejemplar1.isDisponible(), "El ejemplar no debe estar disponible");
        assertTrue(ejemplar1.isDesaparecido());
    }

    // PRUEBAS DE VALIDACIÓN DE JUEGOS POR MESA 

    @Test
    @DisplayName("No prestar juego exclusivo para adultos a mesa con menores")
    void testNoPrestarJuegoExclusivoAdultosAMesaConMenores() {
        JuegoMesa juegoAdultosOnly = new JuegoMesa("Poker", 2020, "CardGames", "Juego de Azar", 2, 6, "exclusivo adultos", false, 120000);
        
        mesa1.setNumeroPersonas(2);
        mesa1.setHayMenoresDeEdad(true);

        assertThrows(JuegoNoAptoParaMesaException.class, () -> {
            juegoAdultosOnly.esAptoParaMesa(mesa1.getNumeroDePersonas(), mesa1.isHayNiños(), mesa1.isHayMenoresDeEdad());
        }, "No debe permitir prestar juego para adultos a mesa con menores");
    }

    @Test
    @DisplayName("No prestar juego no apto para menores de 5 años a mesa con niños")
    void testNoPrestarJuegoNoAptoNinosAMesaConNinos() {
        JuegoMesa juegoMayores5 = new JuegoMesa("Building Blocks", 2020, "ToyBlock", "Construcción", 1, 3, "no apto menores de 5", false, 50000);
        
        mesa1.setNumeroPersonas(2);
        mesa1.setHayNiños(true);

        assertThrows(JuegoNoAptoParaMesaException.class, () -> {
            juegoMayores5.esAptoParaMesa(mesa1.getNumeroDePersonas(), mesa1.isHayNiños(), mesa1.isHayMenoresDeEdad());
        }, "No debe permitir prestar juego no apto para menores de 5 a mesa con niños");
    }

    @Test
    @DisplayName("No prestar juego si mesa tiene menos jugadores que el mínimo requerido")
    void testNoPrestarJuegoMenosDeLosMinimosRequeridos() {
        JuegoMesa juegoMinimo4 = new JuegoMesa("Dungeons", 2020, "Fantasy", "RPG", 4, 6, "general", false, 150000);
        
        mesa1.setNumeroPersonas(2); // Menos de 4

        assertThrows(JuegoNoAptoParaMesaException.class, () -> {
            juegoMinimo4.esAptoParaMesa(mesa1.getNumeroDePersonas(), mesa1.isHayNiños(), mesa1.isHayMenoresDeEdad());
        }, "No debe permitir prestar juego si hay menos jugadores que el mínimo");
    }

    @Test
    @DisplayName("No prestar juego si mesa tiene más jugadores que el máximo permitido")
    void testNoPrestarJuegoMasDelMaximoPermitido() {
        JuegoMesa juegoMaximo2 = new JuegoMesa("Chess", 2020, "Classic", "Estrategia", 2, 2, "general", false, 80000);
        
        mesa1.setNumeroPersonas(4); // Más de 2

        assertThrows(JuegoNoAptoParaMesaException.class, () -> {
            juegoMaximo2.esAptoParaMesa(mesa1.getNumeroDePersonas(), mesa1.isHayNiños(), mesa1.isHayMenoresDeEdad());
        }, "No debe permitir prestar juego si hay más jugadores que el máximo");
    }

    @Test
    @DisplayName("Permitir préstamo de juego apto para la mesa")
    void testPrestarJuegoAptoParaMesa() {
        JuegoMesa juegoApto = new JuegoMesa("Cards", 2020, "CardGame", "Cartas", 2, 4, "general", false, 60000);
        
        mesa1.setNumeroPersonas(3);
        mesa1.setHayNiños(false);
        mesa1.setHayMenoresDeEdad(false);

        // Debe ejecutarse sin lanzar excepción
        assertDoesNotThrow(() -> {
            juegoApto.esAptoParaMesa(mesa1.getNumeroDePersonas(), mesa1.isHayNiños(), mesa1.isHayMenoresDeEdad());
        }, "Debe permitir prestar juego apto para la mesa");
    }

    // PRUEBAS DE CAPACIDAD DE INVENTARIOS 

    @Test
    @DisplayName("No exceder capacidad máxima del inventario de préstamo")
    void testNoExcederCapacidadInventarioPrestamo() {
        InventarioPrestamo inventarioLimitado = new InventarioPrestamo(2);

        EjemplarJuego ej1 = new EjemplarJuego("disponible", juego1);
        EjemplarJuego ej2 = new EjemplarJuego("disponible", juego2);
        EjemplarJuego ej3 = new EjemplarJuego("disponible", juego3);

        juego1.agregarEjemplar(ej1);
        juego2.agregarEjemplar(ej2);
        juego3.agregarEjemplar(ej3);

        assertDoesNotThrow(() -> inventarioLimitado.agregarEjemplar(ej1));
        assertDoesNotThrow(() -> inventarioLimitado.agregarEjemplar(ej2));

        assertThrows(CapacidadMaximaSuperadaException.class, () -> {
            inventarioLimitado.agregarEjemplar(ej3);
        }, "No debe permitir agregar más ejemplares que la capacidad máxima");
    }

    @Test
    @DisplayName("No exceder capacidad máxima del inventario de venta")
    void testNoExcederCapacidadInventarioVenta() {
        InventarioVenta inventarioLimitado = new InventarioVenta(2);

        JuegoMesa j1 = new JuegoMesa("Game1", 2020, "Studio1", "Acción", 1, 2, "general", false, 50000);
        JuegoMesa j2 = new JuegoMesa("Game2", 2020, "Studio2", "Estrategia", 1, 2, "general", false, 60000);
        JuegoMesa j3 = new JuegoMesa("Game3", 2020, "Studio3", "Puzzle", 1, 2, "general", false, 70000);

        assertDoesNotThrow(() -> inventarioLimitado.agregarJuego(j1));
        assertDoesNotThrow(() -> inventarioLimitado.agregarJuego(j2));

        assertThrows(CapacidadMaximaSuperadaException.class, () -> {
            inventarioLimitado.agregarJuego(j3);
        }, "No debe permitir agregar más juegos que la capacidad máxima");
    }

    // PRUEBAS DE IDENTIFICACIÓN DE EJEMPLARES 

    @Test
    @DisplayName("Buscar ejemplar por ID")
    void testBuscarEjemplarPorID() {
        administrador.agregarEjemplarAPrestamo(ejemplar1);
        String idEjemplar = ejemplar1.getID();

        EjemplarJuego ejemplarEncontrado;
		try {
			ejemplarEncontrado = inventarioPrestamo.buscarEjemplarPorID(idEjemplar);
			assertNotNull(ejemplarEncontrado, "El ejemplar debe encontrarse por ID");
	        assertEquals(idEjemplar, ejemplarEncontrado.getID());
		} catch (JuegoNoDisponibleException e) {
			
			e.printStackTrace();
		}

    }

    @Test
    @DisplayName("Lanzar excepción al buscar ID de ejemplar inexistente")
    void testBuscarEjemplarPorIDInexistente() {
        assertThrows(JuegoNoDisponibleException.class, () -> {
            inventarioPrestamo.buscarEjemplarPorID("INEXISTENTE-1");
        }, "Debe lanzar excepción cuando el ID no existe");
    }


    @Test
    @DisplayName("Incrementar contador de veces prestado")
    void testIncrementarContadorVecesPrestado() {
        assertEquals(0, ejemplar1.getNumeroDeVecesPrestado(), "Inicialmente debe ser 0");

        ejemplar1.incrementarVecesPrestado();
        assertEquals(1, ejemplar1.getNumeroDeVecesPrestado());

        ejemplar1.incrementarVecesPrestado();
        assertEquals(2, ejemplar1.getNumeroDeVecesPrestado());
    }
}
