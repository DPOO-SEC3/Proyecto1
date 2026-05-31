package consola;

import Modelo.*;

import java.util.ArrayList;
import java.util.List;

public class ConsolaVentaCafeteria extends ConsolaBasica {

    public VentaCafeteria iniciarVenta(Cliente cliente, Mesa mesa, List<ItemMenu> menuDisponible) {

        System.out.println("===== PEDIDO DE CAFETERÍA =====");

        if (menuDisponible == null || menuDisponible.isEmpty()) {
            System.out.println("[INFO] No hay ítems en el menú disponibles en este momento.");
            return null;
        }

        List<ItemMenu> itemsPedido = new ArrayList<>();

        int opcion;
        do {
            opcion = super.mostrarMenu("PEDIDO DE CAFETERÍA", new String[]{
                    "Agregar bebida al pedido",
                    "Agregar pastelería al pedido",
                    "Ver pedido actual",
                    "Finalizar pedido",
                    "Cancelar"
            });

            switch (opcion) {
                case 1:
                    agregarBebida(itemsPedido, menuDisponible, mesa);
                    break;
                case 2:
                    agregarPasteleria(itemsPedido, menuDisponible, mesa);
                    break;
                case 3:
                    mostrarResumenPedido(itemsPedido);
                    break;
                case 4:
                    break;
                case 5:
                    System.out.println("[INFO] Pedido cancelado.");
                    return null;
            }
        } while (opcion != 4);

        if (itemsPedido.isEmpty()) {
            System.out.println("[INFO] No se agregaron ítems. Pedido cancelado.");
            return null;
        }

        mostrarResumenPedido(itemsPedido);

        double propina = 0;
        boolean agregarPropina = super.pedirConfirmacionAlUsuario("¿Desea agregar propina?");
        if (agregarPropina) {
            propina = super.pedirDoubleAlUsuario("Ingrese el monto de la propina");
        }

        String codigoDescuento = "";
        boolean tieneDescuento = super.pedirConfirmacionAlUsuario("¿Tiene un código de descuento?");
        if (tieneDescuento) {
            codigoDescuento = super.pedirCadenaAlUsuario("Ingrese el código de descuento");
        }

        boolean confirmar = super.pedirConfirmacionAlUsuario("¿Confirmar pedido?");
        if (!confirmar) {
            System.out.println("[INFO] Pedido cancelado.");
            return null;
        }

        VentaCafeteria venta = cliente.comprarCafeteria(itemsPedido, codigoDescuento, propina);
        mesa.registrarVentaCafeteria(venta);

        System.out.println("========== RESUMEN DE VENTA ==========");
        System.out.println("Ítems pedidos:");
        for (ItemMenu item : venta.getItems()) {
            System.out.println("- " + item.getNombre() + ": $" + item.getPrecioBase());
        }
        double subtotal = venta.calcularSubtotal();
        System.out.println("Subtotal: $" + subtotal);
        System.out.println("Impuesto al consumo (8%): $" + venta.getImpuestoConsumo());
        System.out.println("Propina: $" + venta.getPropina());
        System.out.println("TOTAL: $" + venta.calcularTotal());
        System.out.println("Puntos de fidelidad generados: " + venta.getPuntosFidelidadRedimibles());

        return venta;
    }

    private void agregarBebida(List<ItemMenu> pedido, List<ItemMenu> menu, Mesa mesa) {
        List<Bebida> bebidas = new ArrayList<>();
        for (ItemMenu item : menu) {
            if (item instanceof Bebida) bebidas.add((Bebida) item);
        }

        if (bebidas.isEmpty()) {
            System.out.println("[INFO] No hay bebidas disponibles en el menú.");
            return;
        }

        System.out.println("Bebidas disponibles:");
        for (int i = 0; i < bebidas.size(); i++) {
            Bebida b = bebidas.get(i);
            System.out.println((i + 1) + ". " + b.getNombre() + " - $" + b.getPrecioBase() + " - " + b.getTemperatura() + " - " + (b.isEsAlcoholica() ? "Alcohólica" : "Sin alcohol"));
        }

        int indice = super.pedirEnteroAlUsuario("Seleccione el número de la bebida (0 para cancelar)");
        if (indice == 0) return;
        if (indice < 1 || indice > bebidas.size()) {
            System.out.println("[ERROR] Opción no válida.");
            return;
        }

        Bebida seleccionada = bebidas.get(indice - 1);

        if (seleccionada.isEsAlcoholica() && mesa.isHayMenoresDeEdad()) {
            System.out.println("[RESTRICCIÓN] No se puede servir bebida alcohólica: hay menores de edad en la mesa.");
            return;
        }

        try {
            double temp = Double.parseDouble(seleccionada.getTemperatura());
            if (temp > 40 && mesa.tieneJuegoAccion()) {
                System.out.println("[RESTRICCIÓN] No se puede servir bebida caliente: hay un juego de acción activo en la mesa.");
                return;
            }
        } catch (NumberFormatException e) {
            if (seleccionada.getTemperatura().equalsIgnoreCase("caliente") && mesa.tieneJuegoAccion()) {
                System.out.println("[RESTRICCIÓN] No se puede servir bebida caliente: hay un juego de acción activo en la mesa.");
                return;
            }
        }

        pedido.add(seleccionada);
        System.out.println("[OK] " + seleccionada.getNombre() + " agregada al pedido.");
    }

    private void agregarPasteleria(List<ItemMenu> pedido, List<ItemMenu> menu, Mesa mesa) {
        List<Pasteleria> pasteles = new ArrayList<>();
        for (ItemMenu item : menu) {
            if (item instanceof Pasteleria) pasteles.add((Pasteleria) item);
        }

        if (pasteles.isEmpty()) {
            System.out.println("[INFO] No hay pastelería disponible en el menú.");
            return;
        }

        System.out.println("Pastelería disponible:");
        for (int i = 0; i < pasteles.size(); i++) {
            Pasteleria p = pasteles.get(i);
            System.out.println((i + 1) + ". " + p.getNombre() + " - $" + p.getPrecioBase());
            if (!p.getListaAlergenos().isEmpty()) {
                System.out.println("   Alérgenos: " + String.join(", ", p.getListaAlergenos()));
            }
        }

        int indice = super.pedirEnteroAlUsuario("Seleccione el número del producto (0 para cancelar)");
        if (indice == 0) return;
        if (indice < 1 || indice > pasteles.size()) {
            System.out.println("[ERROR] Opción no válida.");
            return;
        }

        Pasteleria seleccionada = pasteles.get(indice - 1);

        if (!seleccionada.getListaAlergenos().isEmpty()) {
            System.out.println("[AVISO] Este producto contiene los siguientes alérgenos: " + String.join(", ", seleccionada.getListaAlergenos()));
            boolean continuar = super.pedirConfirmacionAlUsuario("¿Desea agregarlo de todas formas?");
            if (!continuar) return;
        }

        pedido.add(seleccionada);
        System.out.println("[OK] " + seleccionada.getNombre() + " agregado al pedido.");
    }

    private void mostrarResumenPedido(List<ItemMenu> items) {
        if (items.isEmpty()) {
            System.out.println("[INFO] El pedido está vacío.");
            return;
        }
        System.out.println("Pedido actual:");
        double subtotal = 0;
        for (ItemMenu item : items) {
            System.out.println("- " + item.getNombre() + ": $" + item.getPrecioBase());
            subtotal += item.getPrecioBase();
        }
        System.out.println("Subtotal: $" + subtotal);
        System.out.println("Impuesto al consumo (8%): $" + subtotal * 0.08);
    }
}