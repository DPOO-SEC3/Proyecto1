package consola;

import Modelo.*;

import java.util.ArrayList;
import java.util.List;

public class ConsolaVentaJuego extends ConsolaBasica {

    public VentaJuego iniciarVenta(Cliente cliente, InventarioVenta inventarioVenta) {

        System.out.println("===== COMPRA DE JUEGOS =====");

        if (inventarioVenta.getJuegos().isEmpty()) {
            System.out.println("[INFO] No hay juegos disponibles para la venta en este momento.");
            return null;
        }

        List<JuegoMesa> juegoSeleccionados = new ArrayList<>();

        int opcion;
        do {
            opcion = super.mostrarMenu("COMPRA DE JUEGOS", new String[]{
                    "Ver juegos disponibles y agregar al carrito",
                    "Ver carrito actual",
                    "Finalizar compra",
                    "Cancelar"
            });

            switch (opcion) {
                case 1:
                    agregarJuegoAlCarrito(juegoSeleccionados, inventarioVenta);
                    break;
                case 2:
                    mostrarCarrito(juegoSeleccionados);
                    break;
                case 3:
                    break;
                case 4:
                    System.out.println("[INFO] Compra cancelada.");
                    return null;
            }
        } while (opcion != 3);

        if (juegoSeleccionados.isEmpty()) {
            System.out.println("[INFO] No seleccionó ningún juego. Compra cancelada.");
            return null;
        }

        mostrarCarrito(juegoSeleccionados);

        String codigoDescuento = "";
        boolean tieneDescuento = super.pedirConfirmacionAlUsuario("¿Tiene un código de descuento?");
        if (tieneDescuento) {
            codigoDescuento = super.pedirCadenaAlUsuario("Ingrese el código de descuento");
        }

        boolean confirmar = super.pedirConfirmacionAlUsuario("¿Confirmar compra?");
        if (!confirmar) {
            System.out.println("[INFO] Compra cancelada.");
            return null;
        }

        VentaJuego venta = cliente.comprarJuegos(juegoSeleccionados, codigoDescuento, inventarioVenta);

        System.out.println("========== RESUMEN DE COMPRA ==========");
        System.out.println("Juegos comprados:");
        for (JuegoMesa j : venta.getJuegos()) {
            System.out.println("- " + j.getNombre() + ": $" + j.getPrecioDeVenta());
        }
        double subtotal = venta.calcularSubtotal();
        System.out.println("Subtotal: $" + subtotal);
        System.out.println("IVA (19%): $" + venta.getIVA(subtotal));
        System.out.println("TOTAL: $" + venta.calcularTotal());
        System.out.println("Puntos de fidelidad generados: " + venta.getPuntosFidelidadRedimibles());

        return venta;
    }

    private void agregarJuegoAlCarrito(List<JuegoMesa> carrito, InventarioVenta inventarioVenta) {
        List<JuegoMesa> disponibles = inventarioVenta.getJuegos();

        System.out.println("Juegos disponibles para venta:");
        for (int i = 0; i < disponibles.size(); i++) {
            JuegoMesa j = disponibles.get(i);
            System.out.println((i + 1) + ". " + j.getNombre() + " - $" + j.getPrecioDeVenta() + " - " + j.getMinimoJugadores() + " a " + j.getMaximoJugadores() + " jugadores");
        }

        int indice = super.pedirEnteroAlUsuario("Seleccione el número del juego (0 para cancelar)");
        if (indice == 0) return;
        if (indice < 1 || indice > disponibles.size()) {
            System.out.println("[ERROR] Opción no válida.");
            return;
        }

        JuegoMesa seleccionado = disponibles.get(indice - 1);

        if (carrito.contains(seleccionado)) {
            System.out.println("[AVISO] Ese juego ya está en el carrito.");
            return;
        }

        carrito.add(seleccionado);
        System.out.println("[OK] " + seleccionado.getNombre() + " agregado al carrito.");
    }

    private void mostrarCarrito(List<JuegoMesa> carrito) {
        if (carrito.isEmpty()) {
            System.out.println("[INFO] El carrito está vacío.");
            return;
        }
        System.out.println("Carrito actual:");
        double subtotal = 0;
        for (JuegoMesa j : carrito) {
            System.out.println("- " + j.getNombre() + ": $" + j.getPrecioDeVenta());
            subtotal += j.getPrecioDeVenta();
        }
        System.out.println("Subtotal: $" + subtotal);
        System.out.println("IVA (19%): $" + subtotal * 0.19);
        System.out.println("Total estimado: $" + (subtotal + subtotal * 0.19));
    }
}