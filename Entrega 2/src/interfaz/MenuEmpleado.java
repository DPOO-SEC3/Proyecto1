package interfaz;

import Modelo.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Menú principal para usuarios de tipo Empleado (Mesero o Cocinero).
 */
public class MenuEmpleado extends JFrame {

    private Empleado           empleado;
    private List<TurnoSemanal> turnos;
    private List<Venta> ventas;
    private List<Prestamo> prestamos;
    private List<ItemMenu> itemsMenu;

    public MenuEmpleado(Empleado empleado, List<TurnoSemanal> turnos,
            List<Venta> ventas, List<Prestamo> prestamos, List<ItemMenu> itemsMenu) {
        this.empleado = empleado;
        this.turnos   = turnos;
        this.ventas   = ventas;
        this.prestamos = prestamos;
        this.itemsMenu = itemsMenu;

        String rol = empleado instanceof Mesero ? "Mesero" : "Cocinero";
        setTitle("Menú " + rol + " – " + empleado.getNombre());
        setSize(320, 280);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel titulo = new JLabel("Hola, " + empleado.getNombre() + " (" + rol + ")");
        titulo.setFont(new Font("Arial", Font.BOLD, 14));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        panel.add(titulo);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));

        agregarBoton(panel, "Ver mis turnos",              e -> verMisTurnos());
        agregarBoton(panel, "Solicitar cambio de turno",   e -> JOptionPane.showMessageDialog(this, "Próximamente."));

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

    private void verMisTurnos() {
        StringBuilder sb = new StringBuilder();
        for (TurnoSemanal t : turnos) {
            if (t.getEmpleado().equals(empleado)) {
                sb.append(String.format("• %s: %s – %s%n",
                    t.getDiaSemana(), t.getHoraInicio(), t.getHoraFin()));
            }
        }
        String texto = sb.length() > 0 ? sb.toString() : "No tienes turnos asignados.";
        JOptionPane.showMessageDialog(this, texto, "Mis turnos", JOptionPane.PLAIN_MESSAGE);
    }
}
