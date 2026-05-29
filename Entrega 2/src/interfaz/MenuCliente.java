package interfaz;

import Modelo.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Menú principal para usuarios de tipo Cliente.
 */
public class MenuCliente extends JFrame {

    private Cliente            cliente;
    private List<JuegoMesa>    todosLosJuegos;
    private InventarioPrestamo inventarioPrestamo;
    private InventarioVenta    inventarioVenta;
    private List<ItemMenu> itemsMenu;
    private List<Venta> ventas;
    private List<Prestamo> prestamos;
    public MenuCliente(Cliente cliente,
                       List<JuegoMesa> todosLosJuegos,
                       InventarioPrestamo inventarioPrestamo,
                       InventarioVenta inventarioVenta,
                       List<Venta> ventas, List<Prestamo> prestamos, List<ItemMenu> itemsMenu) {
        this.cliente           = cliente;
        this.todosLosJuegos    = todosLosJuegos;
        this.inventarioPrestamo = inventarioPrestamo;
        this.inventarioVenta   = inventarioVenta;
        this.itemsMenu         = itemsMenu;
        this.ventas            = ventas;
        this.prestamos         = prestamos;

        setTitle("Menú Cliente – " + cliente.getNombre());
        setSize(320, 380);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel titulo = new JLabel("Hola, " + cliente.getNombre());
        titulo.setFont(new Font("Arial", Font.BOLD, 15));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel puntos = new JLabel("Puntos Fidelidad: " + (int) cliente.getPuntosFidelidad());
        puntos.setFont(new Font("Arial", Font.PLAIN, 12));
        puntos.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titulo);
        panel.add(Box.createRigidArea(new Dimension(0, 4)));
        panel.add(puntos);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));

        agregarBoton(panel, "Ver catálogo de juegos",   e -> verCatalogo());
        agregarBoton(panel, "Pedir prestamo/devolver juego",        e -> abrirMenuPrestamos());
        agregarBoton(panel, "Comprar juego/ cafeteria",             e -> abrirMenuCompras());

        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        agregarBoton(panel, "Cerrar sesión", e -> cerrarSesion());

        add(panel);
    }

    private void agregarBoton(JPanel panel, String texto, java.awt.event.ActionListener accion) {
        JButton btn = new JButton(texto);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(220, 32));
        btn.addActionListener(accion);
        panel.add(btn);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
    }

    private void verCatalogo() {
        if (todosLosJuegos.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No hay juegos registrados.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        for (JuegoMesa j : todosLosJuegos) {
            sb.append(String.format("• %s (%s) – $%.0f%n",
                j.getNombre(), j.getCategoria(), j.getPrecioDeVenta()));
        }
        JTextArea area = new JTextArea(sb.toString());
        area.setEditable(false);
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(300, 200));
        JOptionPane.showMessageDialog(this, scroll, "Catálogo de juegos",
            JOptionPane.PLAIN_MESSAGE);
    }
    private void abrirMenuCompras() {
    	new VentanaVentas(cliente, inventarioVenta, itemsMenu, ventas).setVisible(true);  	
    }
    private void abrirMenuPrestamos() {
    	new VentanaPrestamos(cliente, inventarioPrestamo, todosLosJuegos, prestamos).setVisible(true);
    }

    private void cerrarSesion() {
        JOptionPane.showMessageDialog(this, "Sesión cerrada.");
        System.exit(0);
    }
}

