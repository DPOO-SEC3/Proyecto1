package interfaz;

import Modelo.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Ventana inicial de la aplicación.
 * Recibe el estado completo del sistema para pasarlo a las ventanas siguientes.
 */
public class VentanaBienvenida extends JFrame {

    private List<Persona>      usuarios;
    private List<JuegoMesa>    todosLosJuegos;
    private InventarioPrestamo inventarioPrestamo;
    private InventarioVenta    inventarioVenta;
    private List<TurnoSemanal> turnos;
    private List<Venta> ventas;
    private List<Prestamo> prestamos;
    private List<ItemMenu> itemsMenu;
    private List<Torneo> torneosDisponibles;

    public VentanaBienvenida(List<Persona> usuarios,
                              List<JuegoMesa> todosLosJuegos,
                              InventarioPrestamo inventarioPrestamo,
                              InventarioVenta inventarioVenta,
                              List<TurnoSemanal> turnos, List<Venta> ventas, List<Prestamo> prestamos, List<ItemMenu> itemsMenu, List<Torneo> torneosDisponibles) {
        this.usuarios          = usuarios;
        this.todosLosJuegos    = todosLosJuegos;
        this.inventarioPrestamo = inventarioPrestamo;
        this.inventarioVenta   = inventarioVenta;
        this.turnos            = turnos;
        this.ventas            = ventas;
        this.prestamos         = prestamos;
        this.itemsMenu         = itemsMenu;
        this.torneosDisponibles = torneosDisponibles;

        setTitle("DulcesnDados");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        JLabel titulo = new JLabel("Bienvenido a DulcesnDados");
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        titulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitulo = new JLabel("¿Cómo desea continuar?");
        subtitulo.setFont(new Font("Arial", Font.PLAIN, 12));
        subtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnRegistrarse = new JButton("Registrarse");
        btnRegistrarse.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnRegistrarse.setMaximumSize(new Dimension(200, 35));

        JButton btnLogin = new JButton("Iniciar sesión");
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnLogin.setMaximumSize(new Dimension(200, 35));

        btnRegistrarse.addActionListener(e -> {
            new VentanaRegistro(usuarios, todosLosJuegos,
                inventarioPrestamo, inventarioVenta, turnos,ventas,prestamos,itemsMenu, torneosDisponibles).setVisible(true);
            dispose();
        });

        btnLogin.addActionListener(e -> {
            new VentanaLogin(usuarios, todosLosJuegos,
                inventarioPrestamo, inventarioVenta, turnos,ventas,prestamos,itemsMenu, torneosDisponibles).setVisible(true);
            dispose();
        });

        panel.add(titulo);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        panel.add(subtitulo);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));
        panel.add(btnRegistrarse);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(btnLogin);

        add(panel);
    }
}
