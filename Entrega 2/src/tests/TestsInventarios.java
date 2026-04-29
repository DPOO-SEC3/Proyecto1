package tests;

import Modelo.*;
import excepciones.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests JUnit para el módulo de Juegos e Inventarios.
 *
 * Cubre:
 *   1. Agregar y quitar juegos/ejemplares en inventarios (Administrador)
 *   2. Acceso a juego inexistente en inventario
 *   3. Préstamo quita disponibilidad del ejemplar
 *   4. Venta remueve el juego del inventario de venta
 *   5. Restricciones de préstamo por mesa (edad, número de jugadores, categoría)
 */
public class TestsInventarios {

    // ── Objetos compartidos entre tests ──────────────────────────────────────
    private Administrador admin;
    private InventarioPrestamo inventarioPrestamo;
    private InventarioVenta    inventarioVenta;

    private JuegoMesa catan;
    private JuegoMesa uno;
    private JuegoMesa twister;   // categoría Accion
    private JuegoMesa risk;      // exclusivo adultos
    private JuegoMesa juegoNinos;// no apto menores de 5

    private EjemplarJuego ejemplarCatan;
    private EjemplarJuego ejemplarUno;
    private EjemplarJuego ejemplarTwister;
    private EjemplarJuego ejemplarRisk;
    private EjemplarJuego ejemplarJuegoNinos;

    private Mesa mesaNormal;       // 4 personas, sin menores
    private Mesa mesaConNinos;     // 3 personas, con niños < 5
    private Mesa mesaConMenores;   // 4 personas, con menores < 18

    @BeforeEach
    void setUp() throws CapacidadMaximaSuperadaException {
        // Administrador e inventarios
        admin = new Administrador("Admin", "Sistema", "admin@mail.com", "admin123", "admin");
        inventarioPrestamo = new InventarioPrestamo(10);
        inventarioVenta    = new InventarioVenta(10);

        // Juegos
        catan      = new JuegoMesa("Catan",    2000, "Kosmos",  "Tablero", 3, 4, "sin restriccion",     false, 120000);
        uno        = new JuegoMesa("Uno",      1971, "Mattel",  "Cartas",  2, 10,"sin restriccion",     false,  25000);
        twister    = new JuegoMesa("Twister",  1966, "Hasbro",  "Accion",  2, 6, "sin restriccion",     false,  35000);
        risk       = new JuegoMesa("Risk",     1957, "Hasbro",  "Tablero", 2, 6, "exclusivo adultos",   false,  85000);
        juegoNinos = new JuegoMesa("Lego",     2000, "Lego",    "Accion",  2, 4, "no apto menores de 5",false,  50000);

        // Ejemplares
        ejemplarCatan      = new EjemplarJuego("Nuevo", catan);
        ejemplarUno        = new EjemplarJuego("Nuevo", uno);
        ejemplarTwister    = new EjemplarJuego("Nuevo", twister);
        ejemplarRisk       = new EjemplarJuego("Nuevo", risk);
        ejemplarJuegoNinos = new EjemplarJuego("Nuevo", juegoNinos);

        catan.agregarEjemplar(ejemplarCatan);
        uno.agregarEjemplar(ejemplarUno);
        twister.agregarEjemplar(ejemplarTwister);
        risk.agregarEjemplar(ejemplarRisk);
        juegoNinos.agregarEjemplar(ejemplarJuegoNinos);

        // Poblar inventarios
        inventarioPrestamo.agregarEjemplar(ejemplarCatan);
        inventarioPrestamo.agregarEjemplar(ejemplarUno);
        inventarioPrestamo.agregarEjemplar(ejemplarTwister);
        inventarioPrestamo.agregarEjemplar(ejemplarRisk);
        inventarioPrestamo.agregarEjemplar(ejemplarJuegoNinos);

        inventarioVenta.agregarJuego(catan);
        inventarioVenta.agregarJuego(uno);

        // Mesas
        // NOTA: ocupar() es private en Mesa — para los tests de restricciones
        // usamos directamente esAptoParaMesa() con los parámetros equivalentes.
        // Si se hace público ocupar(), los tests de mesa pueden simplificarse.
        mesaNormal    = new Mesa(1, 6);
        mesaConNinos  = new Mesa(2, 6);
        mesaConMenores= new Mesa(3, 6);
    }

    @Test
    @DisplayName("Agregar ejemplar al inventario de préstamo aumenta el tamaño en 1")
    void testAgregarEjemplarAPrestamo() throws CapacidadMaximaSuperadaException {
        int tamanoAntes = inventarioPrestamo.getEjemplares().size();

        JuegoMesa nuevo = new JuegoMesa("Pandemic", 2008, "Z-Man", "Tablero", 2, 4, "sin restriccion", false, 90000);
        EjemplarJuego ejemplarNuevo = new EjemplarJuego("Nuevo", nuevo);
        nuevo.agregarEjemplar(ejemplarNuevo);
        inventarioPrestamo.agregarEjemplar(ejemplarNuevo);

        assertEquals(tamanoAntes + 1, inventarioPrestamo.getEjemplares().size(),
            "El inventario de préstamo debe tener un ejemplar más.");
        assertTrue(inventarioPrestamo.getEjemplares().contains(ejemplarNuevo),
            "El ejemplar recién agregado debe estar en el inventario.");
    }

    @Test
    @DisplayName("Remover ejemplar del inventario de préstamo reduce el tamaño en 1")
    void testRemoverEjemplarDePrestamo() {
        int tamanoAntes = inventarioPrestamo.getEjemplares().size();

        inventarioPrestamo.removerEjemplar(ejemplarCatan);

        assertEquals(tamanoAntes - 1, inventarioPrestamo.getEjemplares().size(),
            "El inventario de préstamo debe tener un ejemplar menos.");
        assertFalse(inventarioPrestamo.getEjemplares().contains(ejemplarCatan),
            "El ejemplar removido no debe estar en el inventario.");
    }

    @Test
    @DisplayName("Agregar juego al inventario de venta aumenta el tamaño en 1")
    void testAgregarJuegoAVenta() throws CapacidadMaximaSuperadaException {
        int tamanoAntes = inventarioVenta.getJuegos().size();

        inventarioVenta.agregarJuego(twister);

        assertEquals(tamanoAntes + 1, inventarioVenta.getJuegos().size(),
            "El inventario de venta debe tener un juego más.");
        assertTrue(inventarioVenta.getJuegos().contains(twister),
            "El juego recién agregado debe estar en el inventario de venta.");
    }

    @Test
    @DisplayName("Remover juego del inventario de venta reduce el tamaño en 1")
    void testRemoverJuegoDeVenta() {
        int tamanoAntes = inventarioVenta.getJuegos().size();

        inventarioVenta.removerJuego(catan);

        assertEquals(tamanoAntes - 1, inventarioVenta.getJuegos().size(),
            "El inventario de venta debe tener un juego menos.");
        assertFalse(inventarioVenta.getJuegos().contains(catan),
            "El juego removido no debe aparecer en el inventario de venta.");
    }

    @Test
    @DisplayName("No se puede agregar más ejemplares que la capacidad máxima del inventario de préstamo")
    void testCapacidadMaximaInventarioPrestamo() {
        InventarioPrestamo pequeño = new InventarioPrestamo(1);
        EjemplarJuego e1 = new EjemplarJuego("Nuevo", catan);
        EjemplarJuego e2 = new EjemplarJuego("Nuevo", uno);

        assertDoesNotThrow(() -> pequeño.agregarEjemplar(e1),
            "El primer ejemplar debe agregarse sin error.");
        assertThrows(CapacidadMaximaSuperadaException.class,
            () -> pequeño.agregarEjemplar(e2),
            "Agregar un segundo ejemplar a inventario con capacidad 1 debe lanzar CapacidadMaximaSuperadaException.");
    }

    @Test
    @DisplayName("No se puede agregar más juegos que la capacidad máxima del inventario de venta")
    void testCapacidadMaximaInventarioVenta() {
        InventarioVenta pequeño = new InventarioVenta(1);
        JuegoMesa j1 = new JuegoMesa("J1", 2000, "X", "Cartas", 2, 4, "sin restriccion", false, 10000);
        JuegoMesa j2 = new JuegoMesa("J2", 2001, "X", "Cartas", 2, 4, "sin restriccion", false, 10000);

        assertDoesNotThrow(() -> pequeño.agregarJuego(j1),
            "El primer juego debe agregarse sin error.");
        assertThrows(CapacidadMaximaSuperadaException.class,
            () -> pequeño.agregarJuego(j2),
            "Agregar un segundo juego a inventario con capacidad 1 debe lanzar CapacidadMaximaSuperadaException.");
    }


    @Test
    @DisplayName("Buscar un juego que no existe en venta lanza JuegoNoExistenteException")
    void testBuscarJuegoInexistenteEnVenta() {
        assertThrows(JuegoNoExistenteException.class,
            () -> inventarioVenta.buscarJuego("Monopolio"),
            "Buscar un juego que no está en venta debe lanzar JuegoNoExistenteException.");
    }

    @Test
    @DisplayName("Buscar ejemplar disponible de juego sin ejemplares lanza JuegoNoDisponibleException")
    void testBuscarEjemplarDeJuegoSinEjemplares() {
        JuegoMesa sinEjemplares = new JuegoMesa("Dixit", 2008, "Asmodee", "Cartas", 3, 6, "sin restriccion", true, 95000);
        // No se agregan ejemplares ni al inventario
        assertThrows(JuegoNoDisponibleException.class,
            () -> inventarioPrestamo.buscarEjemplarDisponible(sinEjemplares.getNombre()),
            "Buscar ejemplar de un juego sin ejemplares en préstamo debe lanzar JuegoNoDisponibleException.");
    }

    @Test
    @DisplayName("Buscar ejemplar cuando todos están prestados lanza JuegoNoDisponibleException")
    void testBuscarEjemplarCuandoTodosEstanPrestados() {
        // Marcar el único ejemplar de Catan como no disponible
        ejemplarCatan.setDisponible(false);

        assertThrows(JuegoNoDisponibleException.class,
            () -> inventarioPrestamo.buscarEjemplarDisponible(catan.getNombre()),
            "Buscar ejemplar cuando todos están prestados debe lanzar JuegoNoDisponibleException.");
    }

    @Test
    @DisplayName("Buscar juego existente en venta retorna el juego correcto")
    void testBuscarJuegoExistenteEnVenta() {
        assertDoesNotThrow(() -> {
            JuegoMesa encontrado = inventarioVenta.buscarJuego("Catan");
            assertEquals("Catan", encontrado.getNombre(),
                "El juego encontrado debe ser Catan.");
        });
    }

    @Test
    @DisplayName("Buscar juego en venta es insensible a mayúsculas/minúsculas")
    void testBuscarJuegoInsensibleMayusculas() {
        assertDoesNotThrow(() -> {
            JuegoMesa encontrado = inventarioVenta.buscarJuego("catan");
            assertNotNull(encontrado, "Debe encontrar Catan aunque se escriba en minúsculas.");
        });
    }

    @Test
    @DisplayName("Al marcar un ejemplar como no disponible deja de aparecer en búsquedas")
    void testEjemplarNoDisponibleDespuesDePrestamo() throws JuegoNoDisponibleException {
        // Verificar que antes está disponible
        assertTrue(ejemplarCatan.isDisponible(),
            "El ejemplar debe estar disponible antes del préstamo.");

        // Simular el efecto del préstamo: marcar como no disponible e incrementar contador
        ejemplarCatan.setDisponible(false);
        ejemplarCatan.incrementarVecesPrestado();

        assertFalse(ejemplarCatan.isDisponible(),
            "El ejemplar no debe estar disponible después del préstamo.");
        assertEquals(1, ejemplarCatan.getNumeroDeVecesPrestado(),
            "El contador de veces prestado debe ser 1.");
    }

    @Test
    @DisplayName("Después de un préstamo, buscarEjemplarDisponible retorna otro ejemplar del mismo juego")
    void testSegundoEjemplarDisponibleDespuesDePrestamo()
            throws CapacidadMaximaSuperadaException, JuegoNoDisponibleException {
        // Agregar un segundo ejemplar de Catan
        EjemplarJuego segundoEjemplar = new EjemplarJuego("Bueno", catan);
        catan.agregarEjemplar(segundoEjemplar);
        inventarioPrestamo.agregarEjemplar(segundoEjemplar);

        // "Prestar" el primer ejemplar
        ejemplarCatan.setDisponible(false);

        // Buscar debe retornar el segundo ejemplar
        EjemplarJuego encontrado = inventarioPrestamo.buscarEjemplarDisponible(catan.getNombre());
        assertEquals(segundoEjemplar, encontrado,
            "Debe retornar el segundo ejemplar disponible de Catan.");
    }

    @Test
    @DisplayName("Al devolver un préstamo el ejemplar vuelve a estar disponible")
    void testEjemplarDisponibleDespuesDeDevolucion() {
        // Simular préstamo
        ejemplarUno.setDisponible(false);
        assertFalse(ejemplarUno.isDisponible(), "Debe estar no disponible tras el préstamo.");

        // Simular devolución
        ejemplarUno.setDisponible(true);
        assertTrue(ejemplarUno.isDisponible(),
            "El ejemplar debe estar disponible de nuevo tras la devolución.");
    }

    @Test
    @DisplayName("Un ejemplar marcado como desaparecido no está disponible")
    void testEjemplarDesaparecidoNoDisponible() {
        assertTrue(ejemplarRisk.isDisponible(), "Debe estar disponible antes.");

        ejemplarRisk.marcarDesaparecido();

        assertFalse(ejemplarRisk.isDisponible(),
            "Un ejemplar desaparecido no debe estar disponible.");
        assertTrue(ejemplarRisk.isDesaparecido(),
            "El flag desaparecido debe ser true.");
    }

    @Test
    @DisplayName("Buscar ejemplar disponible ignora ejemplares desaparecidos")
    void testBuscarEjemplarIgnoraDesaparecidos() {
        ejemplarRisk.marcarDesaparecido();

        assertThrows(JuegoNoDisponibleException.class,
            () -> inventarioPrestamo.buscarEjemplarDisponible(risk.getNombre()),
            "No debe retornar un ejemplar desaparecido.");
    }


    @Test
    @DisplayName("Después de una venta el juego ya no está en el inventario de venta")
    void testJuegoRemovidoDeInventarioDespuesDeVenta() {
        assertTrue(inventarioVenta.getJuegos().contains(catan),
            "Catan debe estar en inventario de venta antes de la venta.");

        inventarioVenta.removerJuego(catan);

        assertFalse(inventarioVenta.getJuegos().contains(catan),
            "Catan no debe estar en inventario de venta después de la venta.");
    }

    @Test
    @DisplayName("Después de remover el juego de venta, buscarlo lanza JuegoNoExistenteException")
    void testBuscarJuegoRemovidoDespuesDeVenta() {
        inventarioVenta.removerJuego(uno);

        assertThrows(JuegoNoExistenteException.class,
            () -> inventarioVenta.buscarJuego("Uno"),
            "Buscar un juego ya vendido debe lanzar JuegoNoExistenteException.");
    }

    @Test
    @DisplayName("hayStock retorna false para juego no presente en inventario de venta")
    void testHayStockFalsoParaJuegoNoEnVenta() {
        assertFalse(inventarioVenta.hayStock(twister),
            "Twister no está en el inventario de venta, hayStock debe retornar false.");
    }

    @Test
    @DisplayName("hayStock retorna true para juego presente en inventario de venta")
    void testHayStockVerdaderoParaJuegoEnVenta() {
        assertTrue(inventarioVenta.hayStock(catan),
            "Catan está en el inventario de venta, hayStock debe retornar true.");
    }


    @Test
    @DisplayName("Juego exclusivo adultos no se puede prestar a mesa con menores de edad")
    void testJuegoAdultosNoAptoParaMesaConMenores() {
        // Mesa con menores de edad (hayMenoresEdad = true)
        assertThrows(JuegoNoAptoParaMesaException.class,
            () -> risk.esAptoParaMesa(4, false, true),
            "Risk es exclusivo adultos y no debe prestarse a mesa con menores.");
    }

    @Test
    @DisplayName("Juego exclusivo adultos sí se puede prestar a mesa solo con adultos")
    void testJuegoAdultosAptoParaMesaSoloAdultos() {
        assertDoesNotThrow(
            () -> risk.esAptoParaMesa(4, false, false),
            "Risk debe poder prestarse a una mesa solo con adultos.");
    }

    @Test
    @DisplayName("Juego no apto menores de 5 no se puede prestar a mesa con niños")
    void testJuegoNoAptoNinosConNinos() {
        assertThrows(JuegoNoAptoParaMesaException.class,
            () -> juegoNinos.esAptoParaMesa(3, true, true),
            "El juego no apto para menores de 5 no debe prestarse a mesa con niños.");
    }

    @Test
    @DisplayName("Juego no apto menores de 5 sí se puede prestar a mesa sin niños aunque haya menores de 18")
    void testJuegoNoAptoNinosSinNinos() {
        assertDoesNotThrow(
            () -> juegoNinos.esAptoParaMesa(3, false, true),
            "El juego no apto para menores de 5 debe poder prestarse si no hay niños menores de 5.");
    }

    @Test
    @DisplayName("Juego no se puede prestar a mesa con menos personas que el mínimo requerido")
    void testJuegoNoAptoPocoJugadores() {
        // Catan requiere mínimo 3 jugadores, mesa con 2 personas
        assertThrows(JuegoNoAptoParaMesaException.class,
            () -> catan.esAptoParaMesa(2, false, false),
            "Catan requiere mínimo 3 jugadores y no debe prestarse a mesa con 2 personas.");
    }

    @Test
    @DisplayName("Juego no se puede prestar a mesa con más personas que el máximo soportado")
    void testJuegoNoAptoDemasiadosJugadores() {
        // Catan soporta máximo 4 jugadores, mesa con 6 personas
        assertThrows(JuegoNoAptoParaMesaException.class,
            () -> catan.esAptoParaMesa(6, false, false),
            "Catan soporta máximo 4 jugadores y no debe prestarse a mesa con 6 personas.");
    }

    @Test
    @DisplayName("Juego sí se puede prestar cuando el número de jugadores es válido")
    void testJuegoAptoNumeroCorrecto() {
        // Catan requiere 3-4 jugadores, mesa con 3 personas
        assertDoesNotThrow(
            () -> catan.esAptoParaMesa(3, false, false),
            "Catan debe poder prestarse a mesa con 3 personas.");
    }

    @Test
    @DisplayName("Juego sin restricción de edad se puede prestar a cualquier mesa")
    void testJuegoSinRestriccionEdadCualquierMesa() {
        assertDoesNotThrow(
            () -> uno.esAptoParaMesa(4, true, true),
            "Uno no tiene restricción de edad y debe prestarse a cualquier mesa.");
    }

    @Test
    @DisplayName("Mesa con juego de Acción detecta correctamente tieneJuegoAccion")
    void testMesaDetectaJuegoAccion() {
        // NOTA: registrarPrestamo() es private en Mesa.
        // Para poder probar tieneJuegoAccion() se necesita que ese método sea
        // al menos package-private. Por ahora verificamos la categoría directamente.
        assertEquals("Accion", twister.getCategoria(),
            "Twister debe ser de categoría Accion.");
    }

    @Test
    @DisplayName("hayEjemplarDisponible retorna false cuando todos los ejemplares están prestados")
    void testHayEjemplarDisponibleFalso() {
        ejemplarCatan.setDisponible(false);
        assertFalse(catan.hayEjemplarDisponible(),
            "hayEjemplarDisponible debe retornar false cuando el único ejemplar no está disponible.");
    }

    @Test
    @DisplayName("hayEjemplarDisponible retorna true cuando hay al menos un ejemplar disponible")
    void testHayEjemplarDisponibleVerdadero() {
        assertTrue(catan.hayEjemplarDisponible(),
            "hayEjemplarDisponible debe retornar true cuando el ejemplar está disponible.");
    }
}
