package interfaz;

import Modelo.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;

/**
 * Ventana de préstamos compartida por Cliente y Empleado.
 * Dos pestañas: Solicitar préstamo y Devolver juego.
 *
 * Nota: los Empleados no necesitan mesa para pedir prestado.
 * Los Clientes deben tener una mesa asignada.
 */
public class VentanaPrestamos extends JFrame {

    private Persona            usuario;
    private InventarioPrestamo inventarioPrestamo;
    private List<JuegoMesa>    todosLosJuegos;
    private List<Prestamo>     historialPrestamos; // lista global del sistema

    public VentanaPrestamos(Persona usuario,
                             InventarioPrestamo inventarioPrestamo,
                             List<JuegoMesa> todosLosJuegos,
                             List<Prestamo> historialPrestamos) {
        this.usuario            = usuario;
        this.inventarioPrestamo = inventarioPrestamo;
        this.todosLosJuegos     = todosLosJuegos;
        this.historialPrestamos = historialPrestamos;

        setTitle("Préstamos – " + usuario.getNombre());
        setSize(580, 480);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Solicitar préstamo", panelSolicitar());
        tabs.addTab("Devolver juego",     panelDevolver());
        add(tabs);
    }

    // ================================================================
    // PESTAÑA 1 — SOLICITAR PRÉSTAMO
    // ================================================================
    private JPanel panelSolicitar() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Info de mesa (solo relevante para Cliente)
        JPanel panelMesa = construirPanelMesa();

        // Lista de juegos del catálogo
        DefaultListModel<JuegoMesa> modeloJuegos = new DefaultListModel<>();
        for (JuegoMesa j : todosLosJuegos) modeloJuegos.addElement(j);

        JList<JuegoMesa> listaJuegos = new JList<>(modeloJuegos);
        listaJuegos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaJuegos.setCellRenderer(new JuegoDisponibilidadRenderer(inventarioPrestamo));
        JScrollPane scroll = new JScrollPane(listaJuegos);
        scroll.setBorder(new TitledBorder("Catálogo de juegos (verde = disponible)"));

        JButton btnSolicitar = new JButton("Solicitar préstamo");

        btnSolicitar.addActionListener(e -> {
            JuegoMesa juego = listaJuegos.getSelectedValue();
            if (juego == null) {
                JOptionPane.showMessageDialog(this, "Selecciona un juego.");
                return;
            }
            solicitarPrestamo(juego, listaJuegos);
        });

        panel.add(panelMesa,  BorderLayout.NORTH);
        panel.add(scroll,     BorderLayout.CENTER);
        panel.add(btnSolicitar, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel construirPanelMesa() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        p.setBorder(new TitledBorder("Información de la mesa"));

        if (usuario instanceof Cliente) {
            Cliente cliente = (Cliente) usuario;
            Mesa mesa = cliente.getMesa();
            if (mesa != null) {
                p.add(new JLabel("Mesa #" + mesa.getNumeroMesa()));
                p.add(new JLabel("| Personas: " + mesa.getNumeroDePersonas()));
                p.add(new JLabel("| Niños: " + (mesa.isHayNiños() ? "Sí" : "No")));
                p.add(new JLabel("| Menores de edad: " + (mesa.isHayMenoresDeEdad() ? "Sí" : "No")));
            } else {
                p.add(new JLabel("⚠ No tienes mesa asignada. Los préstamos requieren mesa."));
            }
        } else {
            p.add(new JLabel("Empleado – no requiere mesa para pedir prestado."));
        }
        return p;
    }

    private void solicitarPrestamo(JuegoMesa juego, JList<JuegoMesa> lista) {
        // Validar aptitud para mesa si es Cliente
        Mesa mesa = null;
        if (usuario instanceof Cliente) {
            mesa = ((Cliente) usuario).getMesa();
            if (mesa == null) {
                JOptionPane.showMessageDialog(this,
                    "Necesitas una mesa asignada para pedir un juego prestado.",
                    "Sin mesa", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                juego.esAptoParaMesa(
                    mesa.getNumeroDePersonas(),
                    mesa.isHayNiños(),
                    mesa.isHayMenoresDeEdad()
                );
            } catch (excepciones.JuegoNoAptoParaMesaException ex) {
                JOptionPane.showMessageDialog(this,
                    "Juego no apto para tu mesa:\n" + ex.getMessage(),
                    "Rechazado", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        // Buscar ejemplar disponible
        EjemplarJuego ejemplar;
        try {
            ejemplar = inventarioPrestamo.buscarEjemplarDisponible(juego.getNombre());
        } catch (excepciones.JuegoNoDisponibleException ex) {
            JOptionPane.showMessageDialog(this,
                "No hay ejemplares disponibles de " + juego.getNombre() + ".",
                "No disponible", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Advertencia si es difícil
        if (juego.esDificil()) {
            JOptionPane.showMessageDialog(this,
                "⚠ Este juego es difícil. Puede que no haya mesero capacitado para explicarlo.",
                "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }

        // Registrar préstamo
        Prestamo prestamo = usuario instanceof Cliente
            ? ((Cliente) usuario).solicitarPrestamo(inventarioPrestamo, ejemplar, mesa)
            : ((Empleado) usuario).solicitarPrestamo(inventarioPrestamo, ejemplar, mesa);

        ejemplar.setDisponible(false);
        ejemplar.incrementarVecesPrestado();

        if (mesa != null) mesa.registrarPrestamo(prestamo);
        historialPrestamos.add(prestamo);

        // Refrescar color de disponibilidad en la lista
        lista.repaint();

        JOptionPane.showMessageDialog(this,
            "Préstamo registrado: " + ejemplar.getID() + " – " + juego.getNombre());
    }

    // ================================================================
    // PESTAÑA 2 — DEVOLVER JUEGO
    // ================================================================
    private JPanel panelDevolver() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Lista de préstamos activos del usuario
        DefaultListModel<Prestamo> modeloPrestamos = new DefaultListModel<>();
        cargarPrestamosActivos(modeloPrestamos);

        JList<Prestamo> listaPrestamos = new JList<>(modeloPrestamos);
        listaPrestamos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        listaPrestamos.setCellRenderer(new PrestamoRenderer());
        JScrollPane scroll = new JScrollPane(listaPrestamos);
        scroll.setBorder(new TitledBorder("Tus préstamos activos"));

        JButton btnDevolver   = new JButton("Devolver juego");
        JButton btnRefrescar  = new JButton("Refrescar lista");

        JPanel botones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        botones.add(btnRefrescar);
        botones.add(btnDevolver);

        btnRefrescar.addActionListener(e -> {
            modeloPrestamos.clear();
            cargarPrestamosActivos(modeloPrestamos);
        });

        btnDevolver.addActionListener(e -> {
            Prestamo prestamo = listaPrestamos.getSelectedValue();
            if (prestamo == null) {
                JOptionPane.showMessageDialog(this, "Selecciona un préstamo.");
                return;
            }
            devolverJuego(prestamo, modeloPrestamos);
        });

        panel.add(scroll,  BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);
        return panel;
    }

    private void cargarPrestamosActivos(DefaultListModel<Prestamo> modelo) {
        List<Prestamo> prestamos = usuario instanceof Cliente
            ? ((Cliente) usuario).getPrestamos()
            : ((Empleado) usuario).getPrestamos();

        for (Prestamo p : prestamos) {
            if (p.estaActivo()) modelo.addElement(p);
        }

        if (modelo.isEmpty()) {
            // Mostrar mensaje vacío a través del modelo no es directo;
            // se maneja en el renderer
        }
    }

    private void devolverJuego(Prestamo prestamo, DefaultListModel<Prestamo> modelo) {
        String nombre = prestamo.getEjemplar().getJuegoMesa().getNombre();

        int confirmar = JOptionPane.showConfirmDialog(this,
            "¿Devolver " + nombre + "?", "Confirmar devolución",
            JOptionPane.YES_NO_OPTION);

        if (confirmar != JOptionPane.YES_OPTION) return;

        if (usuario instanceof Cliente) {
            ((Cliente) usuario).devolverJuego(prestamo);
        } else {
            ((Empleado) usuario).devolverJuego(prestamo);
        }

        modelo.removeElement(prestamo);
        JOptionPane.showMessageDialog(this, "Juego devuelto: " + nombre);
    }

    // ================================================================
    // RENDERERS
    // ================================================================
    private static class JuegoDisponibilidadRenderer extends DefaultListCellRenderer {
        private final InventarioPrestamo inventario;

        JuegoDisponibilidadRenderer(InventarioPrestamo inventario) {
            this.inventario = inventario;
        }

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof JuegoMesa) {
                JuegoMesa j = (JuegoMesa) value;
                boolean disponible = inventario.getEjemplares().stream()
                    .anyMatch(e -> e.getJuegoMesa().equals(j) && e.isDisponible());

                setText(String.format("%-22s | %-8s | %d-%d jug. | %s | %s",
                    j.getNombre(), j.getCategoria(),
                    j.getMinimoJugadores(), j.getMaximoJugadores(),
                    j.esDificil() ? "DIFÍCIL" : "Normal",
                    disponible ? "✔ Disponible" : "✘ No disponible"));

                if (!isSelected) {
                    setForeground(disponible ? new Color(0, 120, 0) : Color.GRAY);
                }
            }
            return this;
        }
    }

    private static class PrestamoRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
            super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            if (value instanceof Prestamo) {
                Prestamo p = (Prestamo) value;
                setText(String.format("%s  |  ID: %s  |  Desde: %s",
                    p.getEjemplar().getJuegoMesa().getNombre(),
                    p.getEjemplar().getID(),
                    p.getFechaHoraInicio().toLocalDate()));
            }
            return this;
        }
    }
}