package consola;
 
import Modelo.*;
import Persistencia.*;
import excepciones.*;
 
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
 

// Este Archivo fue generado con ayuda de Claude, utilizando el analisis realizado por el grupo y el contexto de la aplicación, esto se hizo asi, pues aun no tenemos del todo claro como implemen
/**
 * Consola principal de la aplicación DulcesnDados.
 * Hereda de ConsolaBasica para usar sus métodos de entrada/salida.
 *
 * Flujo de ejecución:
 *   1. Cargar datos persistidos
 *   2. Login del usuario
 *   3. Menú según rol (Cliente / Empleado / Administrador)
 *   4. Guardar datos al salir
 */
public class ConsolaCafeteria extends ConsolaBasica {
 
    // ── Rutas de archivos ─────────────────────────────────────────────────────
    private static final String RUTA_USUARIOS = "datos/usuarios.json";
    private static final String RUTA_TURNOS   = "datos/turnos.json";
 
    // ── Estado global ─────────────────────────────────────────────────────────
    private List<Persona>      usuarios           = new ArrayList<>();
    private List<JuegoMesa>    catalogoJuegos     = new ArrayList<>();
    private InventarioPrestamo inventarioPrestamo;
    private InventarioVenta    inventarioVenta;
    private List<TurnoSemanal> turnos             = new ArrayList<>();
    private List<Mesa>         mesas              = new ArrayList<>();
 
    // ── Persistencia ──────────────────────────────────────────────────────────
    private final PersistenciaJuegos   persistenciaJuegos   = new PersistenciaJuegos();
    private final PersistenciaUsuarios persistenciaUsuarios = new PersistenciaUsuarios();
    private final PersistenciaTurnos   persistenciaTurnos   = new PersistenciaTurnos();
 
    // ─────────────────────────────────────────────────────────────────────────
    //  MAIN
    // ─────────────────────────────────────────────────────────────────────────
 
    public static void main(String[] args) {
        ConsolaCafeteria app = new ConsolaCafeteria();
        app.ejecutar();
    }
 
    // ─────────────────────────────────────────────────────────────────────────
    //  CICLO PRINCIPAL
    // ─────────────────────────────────────────────────────────────────────────
 
    public void ejecutar() {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║     DULCES N' DADOS — SISTEMA        ║");
        System.out.println("╚══════════════════════════════════════╝");
 
        cargarDatos();
 
        boolean salir = false;
        while (!salir) {
            Persona usuarioActual = menuLogin();
            if (usuarioActual == null) {
                salir = pedirConfirmacionAlUsuario("\n¿Desea salir de la aplicación?");
            } else {
                ejecutarSegunRol(usuarioActual);
            }
        }
 
        guardarDatos();
        System.out.println("\nHasta pronto. ¡Que disfrute los juegos!");
    }
 
    /**
     * Determina el tipo de usuario logueado y redirige al menú correspondiente.
     */
    private void ejecutarSegunRol(Persona usuario) {
        if (usuario instanceof Administrador) {
            menuAdministrador((Administrador) usuario);
        } else if (usuario instanceof Mesero) {
            menuMesero((Mesero) usuario);
        } else if (usuario instanceof Cocinero) {
            menuCocinero((Cocinero) usuario);
        } else if (usuario instanceof Cliente) {
            menuCliente((Cliente) usuario);
        }
    }
 
    // ═══════════════════════════════════════════════════════════════════════════
    //  MENÚ DE LOGIN
    // ═══════════════════════════════════════════════════════════════════════════
 
    /**
     * Solicita login y contraseña. Retorna la Persona autenticada o null si falla.
     */
    private Persona menuLogin() {
        System.out.println("\n─────────────────────────────────────────");
        String login     = pedirCadenaAlUsuario("Login");
        String contrasena = pedirCadenaAlUsuario("Contraseña");
 
        for (Persona p : usuarios) {
            if (p.getLogin().equalsIgnoreCase(login) && p.verificarContrasena(contrasena)) {
                System.out.println("Bienvenido/a, " + p.getNombre() + " ("
                        + p.getClass().getSimpleName() + ")");
                return p;
            }
        }
        System.out.println("Login o contraseña incorrectos. Intente de nuevo.");
        return null;
    }
 
    // ═══════════════════════════════════════════════════════════════════════════
    //  MENÚ CLIENTE
    // ═══════════════════════════════════════════════════════════════════════════
 
    private void menuCliente(Cliente cliente) {
        boolean volver = false;
        String[] opciones = {
            "Consultar catálogo de juegos",
            "Ocupar mesa",
            "Solicitar préstamo de juego",
            "Devolver juego",
            "Comprar juego",
            "Comprar en cafetería",
            "Ver mis puntos de fidelidad",
            "Gestionar juegos favoritos",
            "Cerrar sesión"
        };
        while (!volver) {
            int opcion = mostrarMenu("MENÚ CLIENTE — " + cliente.getNombre(), opciones);
            switch (opcion) {
                case 1: consultarCatalogo();                  break;
                case 2: ocuparMesa(cliente);                  break;
                case 3: solicitarPrestamo(cliente);           break;
                case 4: devolverJuego(cliente);               break;
                case 5: comprarJuego(cliente);                break;
                case 6: comprarCafeteria(cliente);            break;
                case 7: verPuntosFidelidad(cliente);          break;
                case 8: gestionarFavoritos(cliente);          break;
                case 9: volver = true;                        break;
            }
        }
    }
 
    // ═══════════════════════════════════════════════════════════════════════════
    //  MENÚ MESERO
    // ═══════════════════════════════════════════════════════════════════════════
 
    private void menuMesero(Mesero mesero) {
        boolean volver = false;
        String[] opciones = {
            "Ver mi turno",
            "Solicitar cambio de turno",
            "Ver mesas que atiendo",
            "Registrar juego que conozco",
            "Sugerir nuevo platillo",
            "Comprar juego (descuento empleado)",
            "Pedir en cafetería (descuento empleado)",
            "Solicitar préstamo de juego",
            "Cerrar sesión"
        };
        while (!volver) {
            int opcion = mostrarMenu("MENÚ MESERO — " + mesero.getNombre(), opciones);
            switch (opcion) {
                case 1: verTurno(mesero);                             break;
                case 2: solicitarCambioTurno(mesero);                 break;
                case 3: verMesasAtendidas(mesero);                    break;
                case 4: registrarJuegoConocido(mesero);               break;
                case 5: sugerirPlatillo(mesero);                      break;
                case 6: comprarJuegoEmpleado(mesero);                 break;
                case 7: comprarCafeteriaEmpleado(mesero);             break;
                case 8: solicitarPrestamoEmpleado(mesero);            break;
                case 9: volver = true;                                break;
            }
        }
    }
 
    // ═══════════════════════════════════════════════════════════════════════════
    //  MENÚ COCINERO
    // ═══════════════════════════════════════════════════════════════════════════
 
    private void menuCocinero(Cocinero cocinero) {
        boolean volver = false;
        String[] opciones = {
            "Ver mi turno",
            "Solicitar cambio de turno",
            "Sugerir nuevo platillo",
            "Comprar juego (descuento empleado)",
            "Pedir en cafetería (descuento empleado)",
            "Cerrar sesión"
        };
        while (!volver) {
            int opcion = mostrarMenu("MENÚ COCINERO — " + cocinero.getNombre(), opciones);
            switch (opcion) {
                case 1: verTurno(cocinero);                   break;
                case 2: solicitarCambioTurno(cocinero);       break;
                case 3: sugerirPlatillo(cocinero);            break;
                case 4: comprarJuegoEmpleado(cocinero);       break;
                case 5: comprarCafeteriaEmpleado(cocinero);   break;
                case 6: volver = true;                        break;
            }
        }
    }
 
    // ═══════════════════════════════════════════════════════════════════════════
    //  MENÚ ADMINISTRADOR
    // ═══════════════════════════════════════════════════════════════════════════
 
    private void menuAdministrador(Administrador admin) {
        boolean volver = false;
        String[] opciones = {
            "Ver inventario de préstamo",
            "Ver inventario de venta",
            "Agregar ejemplar al inventario de préstamo",
            "Agregar juego al inventario de venta",
            "Mover juego de venta a préstamo",
            "Reparar ejemplar",
            "Marcar ejemplar como desaparecido",
            "Gestionar menú del café",
            "Ver solicitudes de cambio de turno",
            "Aprobar o rechazar solicitud de turno",
            "Ver y modificar turnos de empleados",
            "Consultar historial de préstamos",
            "Consultar reporte de ventas",
            "Cerrar sesión"
        };
        while (!volver) {
            int opcion = mostrarMenu("MENÚ ADMINISTRADOR", opciones);
            switch (opcion) {
                case 1:  verInventarioPrestamo();                   break;
                case 2:  verInventarioVenta();                      break;
                case 3:  agregarEjemplarAPrestamo(admin);           break;
                case 4:  agregarJuegoAVenta(admin);                 break;
                case 5:  moverJuegoAPrestamoAdmin(admin);           break;
                case 6:  repararEjemplar(admin);                    break;
                case 7:  marcarDesaparecido(admin);                 break;
                case 8:  gestionarMenu(admin);                      break;
                case 9:  verSolicitudesCambio(admin);               break;
                case 10: aprobarRechazarSolicitud(admin);           break;
                case 11: modificarTurnos(admin);                    break;
                case 12: verHistorialPrestamos();                   break;
                case 13: verReporteVentas(admin);                   break;
                case 14: volver = true;                             break;
            }
        }
    }
 
    // ═══════════════════════════════════════════════════════════════════════════
    //  FUNCIONALIDADES — CLIENTE
    // ═══════════════════════════════════════════════════════════════════════════
 
    private void consultarCatalogo() {
        System.out.println("\n── Catálogo de juegos ──");
        for (JuegoMesa j : catalogoJuegos) {
            System.out.println("  • " + j.getNombre()
                + " | Categoría: "    + j.getCategoria()
                + " | Jugadores: "    + j.getMinimoJugadores() + "-" + j.getMaximoJugadores()
                + " | Restricción: "  + j.getRestriccionEdad()
                + " | Difícil: "      + j.esDificil()
                + " | Disponible: "   + j.hayEjemplarDisponible());
        }
    }
 
    private void ocuparMesa(Cliente cliente) {
        if (cliente.getMesa() != null) {
            System.out.println("Ya tiene una mesa asignada (mesa " + cliente.getMesa().getNumeroMesa() + ").");
            return;
        }
        System.out.println("\n── Mesas disponibles ──");
        List<Mesa> disponibles = new ArrayList<>();
        for (Mesa m : mesas) {
            if (!m.estaOcupada()) {
                System.out.println("  Mesa " + m.getNumeroMesa() + " (capacidad: " + m.getMaximaDePersonas() + ")");
                disponibles.add(m);
            }
        }
        if (disponibles.isEmpty()) {
            System.out.println("No hay mesas disponibles en este momento.");
            return;
        }
        int numMesa       = pedirEnteroAlUsuario("Número de mesa deseada");
        int numPersonas   = pedirEnteroAlUsuario("¿Cuántas personas van en la mesa?");
        boolean hayNinos  = pedirConfirmacionAlUsuario("¿Hay niños menores de 5 años?");
        boolean hayMenores= pedirConfirmacionAlUsuario("¿Hay menores de 18 años?");
 
        Mesa mesaElegida = buscarMesa(numMesa);
        if (mesaElegida == null || mesaElegida.estaOcupada()) {
            System.out.println("Mesa no disponible.");
            return;
        }
        try {
            cliente.ocuparMesa(mesaElegida, numPersonas, hayNinos, hayMenores);
            System.out.println("Mesa " + numMesa + " asignada correctamente.");
        } catch (MesaNoDisponibleException | CapacidadMaximaSuperadaException e) {
            System.out.println("No se pudo ocupar la mesa: " + e.getMessage());
        }
    }
 
    private void solicitarPrestamo(Cliente cliente) {
        if (cliente.getMesa() == null) {
            System.out.println("Debe ocupar una mesa antes de solicitar un préstamo.");
            return;
        }
        if (cliente.getMesa().getPrestamos().size() >= 2) {
            System.out.println("Ya tiene el máximo de 2 préstamos activos.");
            return;
        }
        consultarCatalogo();
        String nombreJuego = pedirCadenaAlUsuario("\nNombre del juego que desea pedir prestado");
        JuegoMesa juego = buscarJuegoEnCatalogo(nombreJuego);
        if (juego == null) {
            System.out.println("Juego no encontrado en el catálogo.");
            return;
        }
        try {
            juego.esAptoParaMesa(
                cliente.getMesa().getNumeroDePersonas(),
                cliente.getMesa().isHayNinos(),
                cliente.getMesa().isHayMenoresDeEdad()
            );
            EjemplarJuego ejemplar = inventarioPrestamo.buscarEjemplarDisponible(juego);
 
            if (juego.esDificil()) {
                boolean hayMeseroCapacitado = false;
                for (Persona p : usuarios) {
                    if (p instanceof Mesero && ((Mesero) p).conoceJuego(juego)) {
                        hayMeseroCapacitado = true;
                        break;
                    }
                }
                if (!hayMeseroCapacitado) {
                    System.out.println("⚠ Advertencia: no hay mesero disponible para enseñar este juego. ¿Desea continuar?");
                    if (!pedirConfirmacionAlUsuario("¿Confirma el préstamo de todas formas?")) return;
                } else {
                    System.out.println("✓ Hay un mesero capacitado para enseñarle este juego.");
                }
            }
 
            Prestamo prestamo = cliente.solicitarPrestamo(ejemplar, cliente.getMesa());
            System.out.println("Préstamo de '" + juego.getNombre() + "' registrado. Estado: " + prestamo.getEstado());
 
        } catch (JuegoNoAptoParaMesaException e) {
            System.out.println("El juego no es apto para su mesa: " + e.getMessage());
        } catch (JuegoNoDisponibleException e) {
            System.out.println("No hay ejemplares disponibles: " + e.getMessage());
        } catch (LimitePrestamosAlcanzadoException e) {
            System.out.println("Límite de préstamos alcanzado: " + e.getMessage());
        }
    }
 
    private void devolverJuego(Cliente cliente) {
        List<Prestamo> activos = new ArrayList<>();
        for (Prestamo p : cliente.getPrestamos()) {
            if (p.estaActivo()) activos.add(p);
        }
        if (activos.isEmpty()) {
            System.out.println("No tiene préstamos activos para devolver.");
            return;
        }
        System.out.println("\n── Préstamos activos ──");
        for (int i = 0; i < activos.size(); i++) {
            System.out.println("  " + (i + 1) + ". " + activos.get(i).getEjemplar().getJuegoMesa().getNombre()
                + " (desde " + activos.get(i).getFechaHoraInicio() + ")");
        }
        int seleccion = pedirEnteroAlUsuario("¿Cuál desea devolver? (número)");
        if (seleccion < 1 || seleccion > activos.size()) {
            System.out.println("Opción inválida.");
            return;
        }
        try {
            cliente.devolverJuego(activos.get(seleccion - 1));
            System.out.println("Juego devuelto correctamente.");
        } catch (PrestamoNoActivoException e) {
            System.out.println("Error al devolver: " + e.getMessage());
        }
    }
 
    private void comprarJuego(Cliente cliente) {
        System.out.println("\n── Juegos en venta ──");
        for (JuegoMesa j : inventarioVenta.getJuegos()) {
            System.out.println("  • " + j.getNombre() + " | $" + j.getPrecioDeVenta());
        }
        String nombreJuego = pedirCadenaAlUsuario("\nNombre del juego que desea comprar");
        try {
            JuegoMesa juego = inventarioVenta.buscarJuego(nombreJuego);
            String codigo   = pedirCadenaAlUsuario("Código de descuento (Enter para omitir)");
            if (codigo.trim().isEmpty()) codigo = null;
 
            double puntosDisponibles = cliente.getPuntosFidelidad();
            double puntosAUsar = 0;
            if (puntosDisponibles > 0) {
                System.out.println("Tiene " + puntosDisponibles + " puntos disponibles (1 punto = $1 de descuento).");
                if (pedirConfirmacionAlUsuario("¿Desea usar puntos de fidelidad?")) {
                    puntosAUsar = pedirNumeroAlUsuario("¿Cuántos puntos desea usar? (máx: " + puntosDisponibles + ")");
                }
            }
 
            VentaJuego venta = cliente.comprarJuego(juego, codigo, puntosAUsar, inventarioVenta);
            System.out.println("\n── Resumen de compra ──");
            System.out.println("  Subtotal:          $" + venta.getSubtotal());
            System.out.println("  Descuento:         $" + venta.getDescuentoAplicado());
            System.out.println("  IVA (19%):         $" + (venta.getTotalConImpuestos() - venta.getSubtotal() + venta.getDescuentoAplicado()));
            System.out.println("  Total:             $" + venta.getTotalConImpuestos());
            System.out.println("  Puntos generados:   " + venta.getPuntosFidelidadGenerados());
            System.out.println("  Puntos acumulados:  " + cliente.getPuntosFidelidad());
 
        } catch (JuegoNoExistenteException e) {
            System.out.println("Juego no encontrado en venta: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error en la compra: " + e.getMessage());
        }
    }
 
    private void comprarCafeteria(Cliente cliente) {
        if (cliente.getMesa() == null) {
            System.out.println("Debe ocupar una mesa antes de pedir en cafetería.");
            return;
        }
        List<ItemMenu> itemsSeleccionados = seleccionarItemsMenu(cliente.getMesa());
        if (itemsSeleccionados.isEmpty()) return;
 
        double propinaSugerida = 0.10;
        System.out.println("Propina sugerida: 10%");
        double propina = propinaSugerida;
        if (pedirConfirmacionAlUsuario("¿Desea cambiar el porcentaje de propina?")) {
            propina = pedirNumeroAlUsuario("Ingrese el porcentaje de propina (ej: 0.15 para 15%)");
        }
 
        double puntosAUsar = 0;
        if (cliente.getPuntosFidelidad() > 0) {
            System.out.println("Tiene " + cliente.getPuntosFidelidad() + " puntos disponibles.");
            if (pedirConfirmacionAlUsuario("¿Desea usar puntos de fidelidad?")) {
                puntosAUsar = pedirNumeroAlUsuario("¿Cuántos puntos desea usar?");
            }
        }
 
        try {
            VentaCafeteria venta = cliente.comprarCafeteria(itemsSeleccionados, propina, puntosAUsar, cliente.getMesa());
            System.out.println("\n── Resumen del pedido ──");
            System.out.println("  Subtotal:               $" + venta.getSubtotal());
            System.out.println("  Propina:                $" + venta.getPropina());
            System.out.println("  Impuesto consumo (8%):  $" + (venta.getTotalConImpuestos() - venta.getSubtotal() - venta.getPropina()));
            System.out.println("  Total:                  $" + venta.getTotalConImpuestos());
            System.out.println("  Puntos generados:        " + venta.getPuntosFidelidadGenerados());
        } catch (Exception e) {
            System.out.println("Error en el pedido: " + e.getMessage());
        }
    }
 
    private List<ItemMenu> seleccionarItemsMenu(Mesa mesa) {
        List<ItemMenu> seleccionados = new ArrayList<>();
        boolean agregarMas = true;
        while (agregarMas) {
            System.out.println("\n── Menú del café ──");
            List<ItemMenu> menuCompleto = obtenerMenuCompleto();
            for (int i = 0; i < menuCompleto.size(); i++) {
                ItemMenu item = menuCompleto.get(i);
                String extra = "";
                if (item instanceof Pasteleria) {
                    List<String> alergenos = ((Pasteleria) item).getListaAlergenos();
                    if (!alergenos.isEmpty()) extra = " ⚠ Alérgenos: " + alergenos;
                }
                if (item instanceof Bebida) {
                    Bebida b = (Bebida) item;
                    extra = " [" + (b.isEsAlcoholica() ? "Alcohólica" : "Sin alcohol") + ", " + b.getTemperatura() + "]";
                }
                System.out.println("  " + (i + 1) + ". " + item.getNombre() + " $" + item.getPrecioBase() + extra);
            }
            int seleccion = pedirEnteroAlUsuario("Seleccione un ítem (número)");
            if (seleccion < 1 || seleccion > menuCompleto.size()) {
                System.out.println("Opción inválida.");
            } else {
                ItemMenu item = menuCompleto.get(seleccion - 1);
                // Validar bebida antes de agregar
                if (item instanceof Bebida) {
                    try {
                        ((Bebida) item).aptoParaMesa(mesa);
                        seleccionados.add(item);
                        System.out.println("'" + item.getNombre() + "' agregado al pedido.");
                    } catch (Exception e) {
                        System.out.println("No se puede agregar este ítem: " + e.getMessage());
                    }
                } else {
                    seleccionados.add(item);
                    System.out.println("'" + item.getNombre() + "' agregado al pedido.");
                }
            }
            agregarMas = pedirConfirmacionAlUsuario("¿Desea agregar otro ítem?");
        }
        return seleccionados;
    }
 
    private void verPuntosFidelidad(Cliente cliente) {
        System.out.println("\nPuntos de fidelidad acumulados: " + cliente.getPuntosFidelidad());
        System.out.println("(Cada punto equivale a $1 de descuento en su próxima compra)");
    }
 
    private void gestionarFavoritos(Cliente cliente) {
        String[] opciones = { "Ver mis favoritos", "Agregar favorito", "Eliminar favorito", "Volver" };
        boolean volver = false;
        while (!volver) {
            int opcion = mostrarMenu("JUEGOS FAVORITOS", opciones);
            switch (opcion) {
                case 1:
                    List<JuegoMesa> favs = cliente.getJuegosFavoritos();
                    if (favs.isEmpty()) System.out.println("No tiene juegos favoritos.");
                    else favs.forEach(j -> System.out.println("  • " + j.getNombre()));
                    break;
                case 2:
                    consultarCatalogo();
                    String nombre = pedirCadenaAlUsuario("Nombre del juego a agregar");
                    JuegoMesa juego = buscarJuegoEnCatalogo(nombre);
                    if (juego != null) { cliente.agregarFavorito(juego); System.out.println("Agregado."); }
                    else System.out.println("Juego no encontrado.");
                    break;
                case 3:
                    List<JuegoMesa> favs2 = cliente.getJuegosFavoritos();
                    if (favs2.isEmpty()) { System.out.println("No tiene favoritos."); break; }
                    favs2.forEach(j -> System.out.println("  • " + j.getNombre()));
                    String nombre2 = pedirCadenaAlUsuario("Nombre del juego a eliminar");
                    JuegoMesa juego2 = buscarJuegoEnFavoritos(cliente, nombre2);
                    if (juego2 != null) { cliente.eliminarFavorito(juego2); System.out.println("Eliminado."); }
                    else System.out.println("No está en sus favoritos.");
                    break;
                case 4: volver = true; break;
            }
        }
    }
 
    // ═══════════════════════════════════════════════════════════════════════════
    //  FUNCIONALIDADES — EMPLEADO (compartidas por Mesero y Cocinero)
    // ═══════════════════════════════════════════════════════════════════════════
 
    private void verTurno(Empleado empleado) {
        TurnoSemanal turno = empleado.getTurnoSemanal();
        if (turno == null) { System.out.println("No tiene turno asignado."); return; }
        System.out.println("\nSu turno: " + turno.getDiaSemana()
            + " de " + turno.getHoraInicio() + " a " + turno.getHoraFin());
    }
 
    private void solicitarCambioTurno(Empleado empleado) {
        String[] tiposOpciones = { "Cambio general", "Intercambio con otro empleado" };
        int tipo = mostrarMenu("TIPO DE SOLICITUD", tiposOpciones);
        String motivo = pedirCadenaAlUsuario("Motivo de la solicitud");
 
        Empleado otroEmpleado = null;
        if (tipo == 2) {
            String loginOtro = pedirCadenaAlUsuario("Login del empleado con quien desea intercambiar");
            Persona otro = buscarUsuarioPorLogin(loginOtro);
            if (otro instanceof Empleado) otroEmpleado = (Empleado) otro;
            else { System.out.println("Empleado no encontrado."); return; }
        }
 
        try {
            String tipoStr = tipo == 1 ? "cambio general" : "intercambio";
            SolicitudCambioTurno solicitud = empleado.solicitarCambioTurno(tipoStr, motivo, otroEmpleado);
            System.out.println("Solicitud enviada al administrador. Estado: " + solicitud.getEstado());
        } catch (Exception e) {
            System.out.println("No se pudo crear la solicitud: " + e.getMessage());
        }
    }
 
    private void sugerirPlatillo(Empleado empleado) {
        String descripcion = pedirCadenaAlUsuario("Descripción del platillo que sugiere");
        SugerenciaPlatillo sugerencia = empleado.sugerirPlatillo(descripcion);
        System.out.println("Sugerencia enviada al administrador. Estado: " + sugerencia.getEstado());
    }
 
    private void comprarJuegoEmpleado(Empleado empleado) {
        if (empleado.estaEnTurno()) {
            System.out.println("No puede comprar juegos mientras está en turno laboral.");
            return;
        }
        System.out.println("\n── Juegos en venta (descuento empleado 20%) ──");
        for (JuegoMesa j : inventarioVenta.getJuegos()) {
            System.out.println("  • " + j.getNombre() + " | Precio: $" + j.getPrecioDeVenta()
                + " | Con descuento: $" + (j.getPrecioDeVenta() * 0.80));
        }
        String nombreJuego = pedirCadenaAlUsuario("Nombre del juego que desea comprar");
        try {
            JuegoMesa juego = inventarioVenta.buscarJuego(nombreJuego);
            double puntosAUsar = 0;
            VentaJuego venta = empleado.comprarJuego(juego, 0.20, puntosAUsar, inventarioVenta);
            System.out.println("Compra registrada. Total con descuento 20%: $" + venta.getTotalConImpuestos());
        } catch (JuegoNoExistenteException e) {
            System.out.println("Juego no encontrado: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
 
    private void comprarCafeteriaEmpleado(Empleado empleado) {
        List<ItemMenu> items = seleccionarItemsMenuSinMesa();
        if (items.isEmpty()) return;
        double propina = 0;
        try {
            VentaCafeteria venta = empleado.comprarCafeteria(items, propina, 0);
            System.out.println("Pedido registrado con descuento empleado. Total: $" + venta.getTotalConImpuestos());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
 
    private void solicitarPrestamoEmpleado(Empleado empleado) {
        if (empleado.estaEnTurno()) {
            System.out.println("No puede pedir juegos prestados mientras está en turno.");
            return;
        }
        consultarCatalogo();
        String nombreJuego = pedirCadenaAlUsuario("Nombre del juego que desea pedir prestado");
        JuegoMesa juego = buscarJuegoEnCatalogo(nombreJuego);
        if (juego == null) { System.out.println("Juego no encontrado."); return; }
        try {
            EjemplarJuego ejemplar = inventarioPrestamo.buscarEjemplarDisponible(juego);
            Prestamo prestamo = empleado.solicitarPrestamo(ejemplar);
            System.out.println("Préstamo registrado. Estado: " + prestamo.getEstado());
        } catch (JuegoNoDisponibleException e) {
            System.out.println("No hay ejemplares disponibles: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
 
    private void verMesasAtendidas(Mesero mesero) {
        List<Mesa> mesasAtendidas = mesero.getMesasAtendidas();
        if (mesasAtendidas.isEmpty()) { System.out.println("No está atendiendo mesas actualmente."); return; }
        mesasAtendidas.forEach(m -> System.out.println("  Mesa " + m.getNumeroMesa()
            + " | Personas: " + m.getNumeroDePersonas()
            + " | Préstamos activos: " + m.getPrestamos().size()));
    }
 
    private void registrarJuegoConocido(Mesero mesero) {
        consultarCatalogo();
        String nombre = pedirCadenaAlUsuario("Nombre del juego difícil que conoce");
        JuegoMesa juego = buscarJuegoEnCatalogo(nombre);
        if (juego == null) { System.out.println("Juego no encontrado."); return; }
        if (!juego.esDificil()) { System.out.println("Este juego no está marcado como difícil."); return; }
        mesero.agregarJuegoConocido(juego);
        System.out.println("Juego registrado como conocido.");
    }
 
    // ═══════════════════════════════════════════════════════════════════════════
    //  FUNCIONALIDADES — ADMINISTRADOR
    // ═══════════════════════════════════════════════════════════════════════════
 
    private void verInventarioPrestamo() {
        System.out.println("\n── Inventario de Préstamo ("
            + inventarioPrestamo.getEjemplares().size() + " / "
            + inventarioPrestamo.getCapacidadMaxima() + ") ──");
        for (EjemplarJuego e : inventarioPrestamo.getEjemplares()) {
            System.out.println("  • " + e.getJuegoMesa().getNombre()
                + " | Estado: "          + e.getEstado()
                + " | Disponible: "       + e.isDisponible()
                + " | Veces prestado: "   + e.getNumeroDeVecesPrestado());
        }
    }
 
    private void verInventarioVenta() {
        System.out.println("\n── Inventario de Venta ("
            + inventarioVenta.getJuegos().size() + " / "
            + inventarioVenta.getCapacidadMaxima() + ") ──");
        for (JuegoMesa j : inventarioVenta.getJuegos()) {
            System.out.println("  • " + j.getNombre() + " | $" + j.getPrecioDeVenta());
        }
    }
 
    private void agregarEjemplarAPrestamo(Administrador admin) {
        consultarCatalogo();
        String nombreJuego = pedirCadenaAlUsuario("Nombre del juego al que pertenece el ejemplar");
        JuegoMesa juego = buscarJuegoEnCatalogo(nombreJuego);
        if (juego == null) { System.out.println("Juego no encontrado."); return; }
        String estado = pedirCadenaAlUsuario("Estado del ejemplar (Nuevo / Bueno / Falta una pieza)");
        EjemplarJuego nuevo = new EjemplarJuego(estado, juego);
        try {
            admin.agregarEjemplarAPrestamo(nuevo, inventarioPrestamo);
            juego.agregarEjemplar(nuevo);
            System.out.println("Ejemplar agregado al inventario de préstamo.");
        } catch (CapacidadMaximaSuperadaException e) {
            System.out.println("No se pudo agregar: " + e.getMessage());
        }
    }
 
    private void agregarJuegoAVenta(Administrador admin) {
        String nombre    = pedirCadenaAlUsuario("Nombre del juego");
        int anio         = pedirEnteroAlUsuario("Año de publicación");
        String empresa   = pedirCadenaAlUsuario("Empresa fabricante");
        String categoria = pedirCadenaAlUsuario("Categoría (Cartas / Tablero / Accion)");
        int minJug       = pedirEnteroAlUsuario("Mínimo de jugadores");
        int maxJug       = pedirEnteroAlUsuario("Máximo de jugadores");
        String restEdad  = pedirCadenaAlUsuario("Restricción de edad (sin restriccion / exclusivo adultos / no apto menores de 5)");
        boolean dificil  = pedirConfirmacionAlUsuario("¿Es un juego difícil?");
        double precio    = pedirNumeroAlUsuario("Precio de venta");
 
        JuegoMesa nuevo = new JuegoMesa(nombre, anio, empresa, categoria, minJug, maxJug, restEdad, dificil, precio);
        try {
            admin.agregarJuegoAVenta(nuevo, inventarioVenta);
            catalogoJuegos.add(nuevo);
            System.out.println("Juego '" + nombre + "' agregado al inventario de venta.");
        } catch (CapacidadMaximaSuperadaException e) {
            System.out.println("No se pudo agregar: " + e.getMessage());
        }
    }
 
    private void moverJuegoAPrestamoAdmin(Administrador admin) {
        verInventarioVenta();
        String nombre = pedirCadenaAlUsuario("Nombre del juego a mover a préstamo");
        try {
            JuegoMesa juego = inventarioVenta.buscarJuego(nombre);
            String estado   = pedirCadenaAlUsuario("Estado del ejemplar a crear (Nuevo / Bueno)");
            admin.moverDeVentaAPrestamo(juego, inventarioVenta, inventarioPrestamo, estado);
            System.out.println("Juego movido al inventario de préstamo.");
        } catch (JuegoNoExistenteException | CapacidadMaximaSuperadaException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
 
    private void repararEjemplar(Administrador admin) {
        verInventarioPrestamo();
        String nombre = pedirCadenaAlUsuario("Nombre del juego cuyo ejemplar desea reparar");
        JuegoMesa juego = buscarJuegoEnCatalogo(nombre);
        if (juego == null) { System.out.println("Juego no encontrado."); return; }
 
        List<EjemplarJuego> dañados = new ArrayList<>();
        for (EjemplarJuego e : inventarioPrestamo.getEjemplares()) {
            if (e.getJuegoMesa().equals(juego) && !e.isDisponible() && !e.getEstado().equalsIgnoreCase("Desaparecido")) {
                dañados.add(e);
            }
        }
        if (dañados.isEmpty()) { System.out.println("No hay ejemplares dañados de este juego."); return; }
 
        for (int i = 0; i < dañados.size(); i++) {
            System.out.println("  " + (i+1) + ". Estado: " + dañados.get(i).getEstado());
        }
        int sel = pedirEnteroAlUsuario("¿Cuál desea reparar?");
        if (sel < 1 || sel > dañados.size()) { System.out.println("Opción inválida."); return; }
 
        try {
            admin.repararEjemplar(dañados.get(sel - 1), inventarioVenta, inventarioPrestamo);
            System.out.println("Ejemplar reparado correctamente.");
        } catch (Exception e) {
            System.out.println("No se pudo reparar: " + e.getMessage());
        }
    }
 
    private void marcarDesaparecido(Administrador admin) {
        verInventarioPrestamo();
        String nombre = pedirCadenaAlUsuario("Nombre del juego a marcar como desaparecido");
        JuegoMesa juego = buscarJuegoEnCatalogo(nombre);
        if (juego == null) { System.out.println("Juego no encontrado."); return; }
 
        List<EjemplarJuego> ejemplaresJuego = inventarioPrestamo.getEjemplaresPorJuego(juego);
        if (ejemplaresJuego.isEmpty()) { System.out.println("No hay ejemplares de este juego en préstamo."); return; }
 
        for (int i = 0; i < ejemplaresJuego.size(); i++) {
            System.out.println("  " + (i+1) + ". Estado: " + ejemplaresJuego.get(i).getEstado()
                + " | Disponible: " + ejemplaresJuego.get(i).isDisponible());
        }
        int sel = pedirEnteroAlUsuario("¿Cuál desea marcar como desaparecido?");
        if (sel < 1 || sel > ejemplaresJuego.size()) { System.out.println("Opción inválida."); return; }
 
        admin.marcarEjemplarDesaparecido(ejemplaresJuego.get(sel - 1));
        System.out.println("Ejemplar marcado como desaparecido.");
    }
 
    private void gestionarMenu(Administrador admin) {
        String[] opciones = { "Ver menú actual", "Agregar bebida", "Agregar pastelería",
                              "Ver sugerencias pendientes", "Volver" };
        boolean volver = false;
        while (!volver) {
            int opcion = mostrarMenu("GESTIÓN DEL MENÚ", opciones);
            switch (opcion) {
                case 1:
                    admin.getItemsMenu().forEach(i -> System.out.println("  • " + i.getNombre() + " $" + i.getPrecioBase()));
                    break;
                case 2:
                    String nombreB  = pedirCadenaAlUsuario("Nombre de la bebida");
                    String descB    = pedirCadenaAlUsuario("Descripción");
                    double precioB  = pedirNumeroAlUsuario("Precio base");
                    boolean alcohol = pedirConfirmacionAlUsuario("¿Es alcohólica?");
                    String temp     = pedirCadenaAlUsuario("Temperatura (caliente / fría)");
                    Bebida bebida   = new Bebida(nombreB, descB, precioB, alcohol, temp);
                    admin.agregarItemMenu(bebida);
                    System.out.println("Bebida agregada al menú.");
                    break;
                case 3:
                    String nombreP   = pedirCadenaAlUsuario("Nombre del producto");
                    String descP     = pedirCadenaAlUsuario("Descripción");
                    double precioP   = pedirNumeroAlUsuario("Precio base");
                    String alergenosStr = pedirCadenaAlUsuario("Alérgenos separados por coma (o Enter para ninguno)");
                    List<String> alergenos = new ArrayList<>();
                    if (!alergenosStr.trim().isEmpty()) {
                        for (String a : alergenosStr.split(",")) alergenos.add(a.trim());
                    }
                    Pasteleria pasteleria = new Pasteleria(nombreP, descP, precioP, alergenos);
                    admin.agregarItemMenu(pasteleria);
                    System.out.println("Producto de pastelería agregado al menú.");
                    break;
                case 4:
                    List<SugerenciaPlatillo> pendientes = admin.getSugerenciasPendientes();
                    if (pendientes.isEmpty()) { System.out.println("No hay sugerencias pendientes."); break; }
                    pendientes.forEach(s -> System.out.println("  • [" + s.getEmpleado().getNombre() + "]: " + s.getDescripcion()));
                    break;
                case 5: volver = true; break;
            }
        }
    }
 
    private void verSolicitudesCambio(Administrador admin) {
        List<SolicitudCambioTurno> pendientes = admin.getSolicitudesCambioPendientes();
        if (pendientes.isEmpty()) { System.out.println("No hay solicitudes pendientes."); return; }
        System.out.println("\n── Solicitudes pendientes ──");
        for (int i = 0; i < pendientes.size(); i++) {
            SolicitudCambioTurno s = pendientes.get(i);
            System.out.println("  " + (i+1) + ". " + s.getSolicitante().getNombre()
                + " | Tipo: " + s.getTipo()
                + " | Motivo: " + s.getMotivo()
                + " | Fecha: " + s.getFechaDeSolicitud());
        }
    }
 
    private void aprobarRechazarSolicitud(Administrador admin) {
        List<SolicitudCambioTurno> pendientes = admin.getSolicitudesCambioPendientes();
        if (pendientes.isEmpty()) { System.out.println("No hay solicitudes pendientes."); return; }
        verSolicitudesCambio(admin);
        int sel = pedirEnteroAlUsuario("Número de solicitud a gestionar");
        if (sel < 1 || sel > pendientes.size()) { System.out.println("Opción inválida."); return; }
        SolicitudCambioTurno solicitud = pendientes.get(sel - 1);
        boolean aprobar = pedirConfirmacionAlUsuario("¿Desea aprobar esta solicitud? (No = rechazar)");
        try {
            if (aprobar) {
                admin.aprobarSolicitudCambio(solicitud, turnos);
                System.out.println("Solicitud aprobada.");
            } else {
                admin.rechazarSolicitudCambio(solicitud);
                System.out.println("Solicitud rechazada.");
            }
        } catch (Exception e) {
            System.out.println("Error al procesar solicitud: " + e.getMessage());
        }
    }
 
    private void modificarTurnos(Administrador admin) {
        System.out.println("\n── Turnos actuales ──");
        for (int i = 0; i < turnos.size(); i++) {
            TurnoSemanal t = turnos.get(i);
            System.out.println("  " + (i+1) + ". " + t.getEmpleado().getNombre()
                + " | " + t.getDiaSemana()
                + " | " + t.getHoraInicio() + " - " + t.getHoraFin());
        }
        int sel = pedirEnteroAlUsuario("Número del turno a modificar");
        if (sel < 1 || sel > turnos.size()) { System.out.println("Opción inválida."); return; }
        TurnoSemanal turno = turnos.get(sel - 1);
        String dia    = pedirCadenaAlUsuario("Nuevo día (ej: Lunes)");
        String inicio = pedirCadenaAlUsuario("Nueva hora inicio (HH:MM)");
        String fin    = pedirCadenaAlUsuario("Nueva hora fin (HH:MM)");
        try {
            admin.modificarTurnoSemanal(turno, dia, LocalTime.parse(inicio), LocalTime.parse(fin));
            System.out.println("Turno modificado correctamente.");
        } catch (Exception e) {
            System.out.println("Error al modificar turno: " + e.getMessage());
        }
    }
 
    private void verHistorialPrestamos() {
        System.out.println("\n── Historial de préstamos ──");
        for (EjemplarJuego e : inventarioPrestamo.getEjemplares()) {
            System.out.println("  • " + e.getJuegoMesa().getNombre()
                + " | Estado: " + e.getEstado()
                + " | Veces prestado: " + e.getNumeroDeVecesPrestado()
                + " | Disponible: " + e.isDisponible());
        }
    }
 
    private void verReporteVentas(Administrador admin) {
        String[] granularidades = { "Diario", "Semanal", "Mensual", "Todas las ventas" };
        int opcion = mostrarMenu("GRANULARIDAD DEL REPORTE", granularidades);
        String granularidad = granularidades[opcion - 1].toLowerCase();
        String reporte = admin.generarReporteVentas(null, null, granularidad);
        System.out.println("\n── Reporte de ventas ──");
        System.out.println(reporte);
    }
 
    // ═══════════════════════════════════════════════════════════════════════════
    //  CARGA Y GUARDADO
    // ═══════════════════════════════════════════════════════════════════════════
 
    private void cargarDatos() {
        System.out.println("\nCargando datos...");
        try {
            usuarios           = persistenciaUsuarios.cargarUsuarios(RUTA_USUARIOS);
            catalogoJuegos     = persistenciaJuegos.cargarJuegos();
            inventarioPrestamo = persistenciaJuegos.cargarInventarioPrestamo(catalogoJuegos);
            inventarioVenta    = persistenciaJuegos.cargarInventarioVenta(catalogoJuegos);
            turnos             = persistenciaTurnos.cargarTurnos(RUTA_TURNOS, usuarios);
            System.out.println("Datos cargados: " + usuarios.size() + " usuarios, "
                + catalogoJuegos.size() + " juegos, " + turnos.size() + " turnos.");
        } catch (Exception e) {
            System.out.println("Primera ejecución o datos no encontrados. Iniciando con sistema vacío.");
            if (inventarioPrestamo == null) inventarioPrestamo = new InventarioPrestamo(100);
            if (inventarioVenta    == null) inventarioVenta    = new InventarioVenta(100);
        }
        for (int i = 1; i <= 10; i++) mesas.add(new Mesa(i, 6));
    }
 
    private void guardarDatos() {
        System.out.println("\nGuardando datos...");
        try {
            persistenciaUsuarios.guardarUsuarios(usuarios, RUTA_USUARIOS);
            persistenciaJuegos.guardarJuegos(catalogoJuegos);
            persistenciaJuegos.guardarInventarios(inventarioPrestamo, inventarioVenta, catalogoJuegos);
            List<TurnoSemanal> conEmpleado = new ArrayList<>();
            for (TurnoSemanal t : turnos) { if (t.getEmpleado() != null) conEmpleado.add(t); }
            if (!conEmpleado.isEmpty()) persistenciaTurnos.guardarTurnos(conEmpleado, RUTA_TURNOS);
            System.out.println("Datos guardados correctamente.");
        } catch (Exception e) {
            System.out.println("Error al guardar: " + e.getMessage());
        }
    }
 
    // ═══════════════════════════════════════════════════════════════════════════
    //  UTILIDADES PRIVADAS
    // ═══════════════════════════════════════════════════════════════════════════
 
    private Mesa buscarMesa(int numero) {
        for (Mesa m : mesas) { if (m.getNumeroMesa() == numero) return m; }
        return null;
    }
 
    private JuegoMesa buscarJuegoEnCatalogo(String nombre) {
        for (JuegoMesa j : catalogoJuegos) { if (j.getNombre().equalsIgnoreCase(nombre)) return j; }
        return null;
    }
 
    private Persona buscarUsuarioPorLogin(String login) {
        for (Persona p : usuarios) { if (p.getLogin().equalsIgnoreCase(login)) return p; }
        return null;
    }
 
    private JuegoMesa buscarJuegoEnFavoritos(Cliente cliente, String nombre) {
        for (JuegoMesa j : cliente.getJuegosFavoritos()) { if (j.getNombre().equalsIgnoreCase(nombre)) return j; }
        return null;
    }
 
    private List<ItemMenu> obtenerMenuCompleto() {
        // Retorna todos los ítems del menú registrados por el administrador
        for (Persona p : usuarios) {
            if (p instanceof Administrador) return ((Administrador) p).getItemsMenu();
        }
        return new ArrayList<>();
    }
 
    private List<ItemMenu> seleccionarItemsMenuSinMesa() {
        List<ItemMenu> seleccionados = new ArrayList<>();
        boolean agregarMas = true;
        while (agregarMas) {
            List<ItemMenu> menu = obtenerMenuCompleto();
            if (menu.isEmpty()) { System.out.println("El menú está vacío."); return seleccionados; }
            for (int i = 0; i < menu.size(); i++) {
                System.out.println("  " + (i+1) + ". " + menu.get(i).getNombre() + " $" + menu.get(i).getPrecioBase());
            }
            int sel = pedirEnteroAlUsuario("Seleccione un ítem");
            if (sel >= 1 && sel <= menu.size()) {
                seleccionados.add(menu.get(sel - 1));
                System.out.println("Agregado: " + menu.get(sel - 1).getNombre());
            }
            agregarMas = pedirConfirmacionAlUsuario("¿Agregar otro ítem?");
        }
        return seleccionados;
    }
}
