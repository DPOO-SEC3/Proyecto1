package interfaz;

import Modelo.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Ventana de ventas compartida por Cliente y Empleado.
 * Dos pestañas: Comprar Juegos y Comprar Cafetería.
 */
public class VentanaVentas extends JFrame {

    private Persona          comprador;
    private InventarioVenta  inventarioVenta;
    private List<ItemMenu>   itemsMenu;
    private List<Venta>      historialVentas; // lista global del sistema

    public VentanaVentas(Persona comprador,
                         InventarioVenta inventarioVenta,
                         List<ItemMenu> itemsMenu,
                         List<Venta> historialVentas) {
        this.comprador       = comprador;
        this.inventarioVenta = inventarioVenta;
        this.itemsMenu       = itemsMenu;
        this.historialVentas = historialVentas;

        setTitle("Ventas – " + comprador.getNombre());
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Comprar juegos",     panelJuegos());
        tabs.addTab("Comprar cafetería",  panelCafeteria());
        add(tabs);
    }

    // ================================================================
    // PESTAÑA 1 — COMPRAR JUEGOS
    // ================================================================
    private JPanel panelJuegos() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Lista de juegos disponibles con checkbox
        DefaultListModel<JuegoMesa> modelo = new DefaultListModel<>();
        for (JuegoMesa j : inventarioVenta.getJuegos()) modelo.addElement(j);

        JList<JuegoMesa> listaJuegos = new JList<>(modelo);
        listaJuegos.setCellRenderer(new JuegoRenderer());
        listaJuegos.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scroll = new JScrollPane(listaJuegos);
        scroll.setBorder(new TitledBorder("Juegos disponibles (Ctrl+clic para seleccionar varios)"));

        // Panel inferior — descuento y resumen
        JPanel inferior = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtDescuento = new JTextField(10);
        JLabel lblResumen = new JLabel("Selecciona juegos para ver el total.");
        lblResumen.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JButton btnComprar = new JButton("Confirmar compra");

        gbc.gridx = 0; gbc.gridy = 0;
        inferior.add(new JLabel("Código de descuento:"), gbc);
        gbc.gridx = 1;
        inferior.add(txtDescuento, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2;
        inferior.add(lblResumen, gbc);

        gbc.gridy = 2;
        inferior.add(btnComprar, gbc);

        // Actualizar resumen al cambiar selección
        listaJuegos.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                lblResumen.setText(calcularResumenJuegos(listaJuegos.getSelectedValuesList()));
            }
        });

        btnComprar.addActionListener(e -> {
            List<JuegoMesa> seleccionados = listaJuegos.getSelectedValuesList();
            if (seleccionados.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Selecciona al menos un juego.");
                return;
            }
            confirmarCompraJuegos(new ArrayList<>(seleccionados),
                txtDescuento.getText().trim(), modelo, listaJuegos);
        });

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(inferior, BorderLayout.SOUTH);
        return panel;
    }

    private String calcularResumenJuegos(List<JuegoMesa> juegos) {
        if (juegos.isEmpty()) return "Selecciona juegos para ver el total.";
        double subtotal = juegos.stream().mapToDouble(JuegoMesa::getPrecioDeVenta).sum();
        double iva      = subtotal * 0.19;
        return String.format("Subtotal: $%.0f  |  IVA 19%%: $%.0f  |  TOTAL: $%.0f",
            subtotal, iva, subtotal + iva);
    }

    private void confirmarCompraJuegos(List<JuegoMesa> juegos, String descuento,
                                        DefaultListModel<JuegoMesa> modelo,
                                        JList<JuegoMesa> lista) {
        double subtotal = juegos.stream().mapToDouble(JuegoMesa::getPrecioDeVenta).sum();
        double iva      = subtotal * 0.19;
        double total    = subtotal + iva;

        int confirmar = JOptionPane.showConfirmDialog(this,
            String.format("Total a pagar: $%.0f%n¿Confirmar compra?", total),
            "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirmar != JOptionPane.YES_OPTION) return;

        VentaJuego venta = comprador instanceof Cliente
            ? ((Cliente) comprador).comprarJuegos(juegos, descuento, inventarioVenta)
            : ((Empleado) comprador).comprarJuegos(juegos, descuento, inventarioVenta);

        venta.calcularSubtotal();
        venta.calcularTotal();
        historialVentas.add(venta);

        // Quitar de la lista visual
        for (JuegoMesa j : juegos) modelo.removeElement(j);
        lista.clearSelection();

        JOptionPane.showMessageDialog(this,
            String.format("Compra registrada.%nTotal: $%.0f  |  Puntos ganados: %.0f",
                total, total * 0.01));
    }

    // ================================================================
    // PESTAÑA 2 — COMPRAR CAFETERÍA
    // ================================================================
    private JPanel panelCafeteria() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Lista del menú
        DefaultListModel<ItemMenu> modeloMenu = new DefaultListModel<>();
        for (ItemMenu item : itemsMenu) modeloMenu.addElement(item);

        JList<ItemMenu> listaMenu = new JList<>(modeloMenu);
        listaMenu.setCellRenderer(new ItemMenuRenderer());
        listaMenu.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane scroll = new JScrollPane(listaMenu);
        scroll.setBorder(new TitledBorder("Menú (Ctrl+clic para seleccionar varios)"));

        // Panel inferior
        JPanel inferior = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JTextField txtDescuento = new JTextField(10);
        JTextField txtPropina   = new JTextField("0", 8);
        JLabel lblResumen = new JLabel("Selecciona items para ver el total.");
        lblResumen.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JButton btnComprar = new JButton("Confirmar pedido");

        gbc.gridx = 0; gbc.gridy = 0;
        inferior.add(new JLabel("Código de descuento:"), gbc);
        gbc.gridx = 1; inferior.add(txtDescuento, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        inferior.add(new JLabel("Propina ($):"), gbc);
        gbc.gridx = 1; inferior.add(txtPropina, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        inferior.add(lblResumen, gbc);

        gbc.gridy = 3;
        inferior.add(btnComprar, gbc);

        listaMenu.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                lblResumen.setText(calcularResumenCafeteria(
                    listaMenu.getSelectedValuesList(), parsePropina(txtPropina)));
            }
        });
        txtPropina.addActionListener(e ->
            lblResumen.setText(calcularResumenCafeteria(
                listaMenu.getSelectedValuesList(), parsePropina(txtPropina))));

        btnComprar.addActionListener(e -> {
            List<ItemMenu> seleccionados = listaMenu.getSelectedValuesList();
            if (seleccionados.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Selecciona al menos un item.");
                return;
            }
            // Advertir alérgenos de pastelería
            StringBuilder alerta = new StringBuilder();
            for (ItemMenu item : seleccionados) {
                if (item instanceof Pasteleria) {
                    List<String> alergenos = ((Pasteleria) item).getListaAlergenos();
                    if (!alergenos.isEmpty()) {
                        alerta.append("• ").append(item.getNombre())
                              .append(" contiene: ").append(alergenos).append("\n");
                    }
                }
            }
            if (alerta.length() > 0) {
                JOptionPane.showMessageDialog(this,
                    "⚠ Alérgenos detectados:\n" + alerta,
                    "Advertencia de alérgenos", JOptionPane.WARNING_MESSAGE);
            }
            confirmarCompraCafeteria(new ArrayList<>(seleccionados),
                txtDescuento.getText().trim(), parsePropina(txtPropina));
        });

        panel.add(scroll, BorderLayout.CENTER);
        panel.add(inferior, BorderLayout.SOUTH);
        return panel;
    }

    private double parsePropina(JTextField campo) {
        try { return Double.parseDouble(campo.getText().trim()); }
        catch (NumberFormatException e) { return 0; }
    }

    private String calcularResumenCafeteria(List<ItemMenu> items, double propina) {
        if (items.isEmpty()) return "Selecciona items para ver el total.";
        double subtotal  = items.stream().mapToDouble(ItemMenu::getPrecioBase).sum();
        double impuesto  = subtotal * 0.08;
        double total     = subtotal + impuesto + propina;
        return String.format("Subtotal: $%.0f  |  Imp.8%%: $%.0f  |  Propina: $%.0f  |  TOTAL: $%.0f",
            subtotal, impuesto, propina, total);
    }

    private void confirmarCompraCafeteria(List<ItemMenu> items, String descuento, double propina) {
        double subtotal = items.stream().mapToDouble(ItemMenu::getPrecioBase).sum();
        double impuesto = subtotal * 0.08;
        double total    = subtotal + impuesto + propina;

        int confirmar = JOptionPane.showConfirmDialog(this,
            String.format("Total a pagar: $%.0f%n¿Confirmar pedido?", total),
            "Confirmar", JOptionPane.YES_NO_OPTION);

        if (confirmar != JOptionPane.YES_OPTION) return;

        VentaCafeteria venta = comprador instanceof Cliente
            ? ((Cliente) comprador).comprarCafeteria(items, descuento, propina)
            : ((Empleado) comprador).comprarCafeteria(items, descuento, propina);

        venta.calcularSubtotal();
        venta.calcularTotal();
        historialVentas.add(venta);

        JOptionPane.showMessageDialog(this,
            String.format("Pedido registrado.%nTotal: $%.0f  |  Puntos ganados: %.0f",
                total, total * 0.01));
    }

    // ================================================================
    // RENDERERS
    // ================================================================
    private static class JuegoRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof JuegoMesa) {
                JuegoMesa j = (JuegoMesa) value;
                setText(String.format("%-22s | %-8s | $%.0f",
                    j.getNombre(), j.getCategoria(), j.getPrecioDeVenta()));
            }
            return this;
        }
    }

    private static class ItemMenuRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Bebida) {
                Bebida b = (Bebida) value;
                setText(String.format("🍵 %-20s | %-8s | %s | $%.0f",
                    b.getNombre(), b.getTemperatura(),
                    b.isEsAlcoholica() ? "Alcohólica" : "Sin alcohol",
                    b.getPrecioBase()));
            } else if (value instanceof Pasteleria) {
                Pasteleria p = (Pasteleria) value;
                String alerg = p.getListaAlergenos().isEmpty() ? "" : " ⚠";
                setText(String.format("🍰 %-20s%s | $%.0f",
                    p.getNombre(), alerg, p.getPrecioBase()));
            }
            return this;
        }
    }
    
    
}