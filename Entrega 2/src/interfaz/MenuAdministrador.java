package interfaz;

import Modelo.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class MenuAdministrador extends JFrame {

    private Administrador      admin;
    private List<Persona>      usuarios;
    private List<JuegoMesa>    todosLosJuegos;
    private InventarioPrestamo inventarioPrestamo;
    private InventarioVenta    inventarioVenta;
    private List<TurnoSemanal> turnos;
    private List<Venta> ventas;
    private List<Prestamo> prestamos;
    private List<ItemMenu> itemsMenu;

    public MenuAdministrador(Administrador admin,
                              List<Persona> usuarios,
                              List<JuegoMesa> todosLosJuegos,
                              InventarioPrestamo inventarioPrestamo,
                              InventarioVenta inventarioVenta,
                              List<TurnoSemanal> turnos,
                              List<Venta> ventas, List<Prestamo> prestamos, List<ItemMenu> itemsMenu) {
        this.admin             = admin;
        this.usuarios          = usuarios;
        this.todosLosJuegos    = todosLosJuegos;
        this.inventarioPrestamo = inventarioPrestamo;
        this.inventarioVenta   = inventarioVenta;
        this.turnos            = turnos;
        this.ventas            = ventas;
        this.prestamos         = prestamos;
        this.itemsMenu         = itemsMenu;

        setTitle("Menú Administrador – " + admin.getNombre());
        setSize(320, 380);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel titulo = new JLabel("Panel de administración");
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titulo);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));

        agregarBoton(panel, "Ver inventario de préstamo",  e -> verInventarioPrestamo());
        agregarBoton(panel, "Ver inventario de venta",     e -> verInventarioVenta());
        agregarBoton(panel, "Ver todos los turnos",        e -> verTodosTurnos());
        agregarBoton(panel, "Gestionar solicitudes",       e -> JOptionPane.showMessageDialog(this, "Próximamente."));
        agregarBoton(panel, "Ver usuarios",                e -> verUsuarios());
        agregarBoton(panel, "Ver graficos del negocio", e -> abrirPanelGraficas());

        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        agregarBoton(panel, "Cerrar sesión", e -> {
            JOptionPane.showMessageDialog(this, "Sesión cerrada.");
            System.exit(0);
        });

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
    
    private void abrirPanelGraficas() {
    	PanelGraficas panel = new PanelGraficas(admin,todosLosJuegos,inventarioPrestamo,inventarioVenta,ventas,prestamos);
    }

    private void verInventarioPrestamo() {
        StringBuilder sb = new StringBuilder();
        for (EjemplarJuego e : inventarioPrestamo.getEjemplares()) {
            sb.append(String.format("• %-18s | %-12s | Prestado: %dx%n",
                e.getJuegoMesa().getNombre(),
                e.isDisponible() ? "Disponible" : "En préstamo",
                e.getNumeroDeVecesPrestado()));
        }
        mostrarTexto("Inventario de préstamo",
            sb.length() > 0 ? sb.toString() : "Vacío.");
    }

    private void verInventarioVenta() {
        StringBuilder sb = new StringBuilder();
        for (JuegoMesa j : inventarioVenta.getJuegos()) {
            sb.append(String.format("• %-18s | $%.0f%n",
                j.getNombre(), j.getPrecioDeVenta()));
        }
        mostrarTexto("Inventario de venta",
            sb.length() > 0 ? sb.toString() : "Vacío.");
    }

    private void verTodosTurnos() {
        StringBuilder sb = new StringBuilder();
        for (TurnoSemanal t : turnos) {
            sb.append(String.format("• %-10s | %s - %s | %s (%s)%n",
                t.getDiaSemana(), t.getHoraInicio(), t.getHoraFin(),
                t.getEmpleado().getNombre(),
                t.getEmpleado().getClass().getSimpleName()));
        }
        mostrarTexto("Turnos", sb.length() > 0 ? sb.toString() : "Sin turnos.");
    }

    private void verUsuarios() {
        StringBuilder sb = new StringBuilder();
        for (Persona p : usuarios) {
            sb.append(String.format("• %-12s | %-10s | %s%n",
                p.getLogin(),
                p.getClass().getSimpleName(),
                p.getNombre() + " " + p.getApellido()));
        }
        mostrarTexto("Usuarios", sb.toString());
    }

    private void mostrarTexto(String titulo, String contenido) {
        JTextArea area = new JTextArea(contenido);
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(area);
        scroll.setPreferredSize(new Dimension(380, 200));
        JOptionPane.showMessageDialog(this, scroll, titulo, JOptionPane.PLAIN_MESSAGE);
    }
}

