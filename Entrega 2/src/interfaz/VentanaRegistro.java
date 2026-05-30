package interfaz;

import Modelo.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;


public class VentanaRegistro extends JFrame {

    private JTextField     txtNombre, txtApellido, txtCorreo, txtLogin;
    private JPasswordField txtContrasena;

    private List<Persona>      usuarios;
    private List<JuegoMesa>    todosLosJuegos;
    private InventarioPrestamo inventarioPrestamo;
    private InventarioVenta    inventarioVenta;
    private List<TurnoSemanal> turnos;
    private List<Venta> ventas;
    private List<Prestamo> prestamos;
    private List<ItemMenu> itemsMenu;
    private List<Torneo> torneosDisponibles;

    public VentanaRegistro(List<Persona> usuarios,
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

        setTitle("Registro");
        setSize(380, 320);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 5, 6, 5);

        JLabel titulo = new JLabel("Crear cuenta");
        titulo.setFont(new Font("Arial", Font.BOLD, 15));

        txtNombre     = new JTextField();
        txtApellido   = new JTextField();
        txtCorreo     = new JTextField();
        txtLogin      = new JTextField();
        txtContrasena = new JPasswordField();

        JButton btnRegistrar = new JButton("Registrarse");
        JButton btnVolver    = new JButton("Volver");

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titulo, gbc);
        gbc.gridwidth = 1;

        agregarFila(panel, gbc, 1, "Nombre:",     txtNombre);
        agregarFila(panel, gbc, 2, "Apellido:",   txtApellido);
        agregarFila(panel, gbc, 3, "Correo:",     txtCorreo);
        agregarFila(panel, gbc, 4, "Login:",      txtLogin);
        agregarFila(panel, gbc, 5, "Contraseña:", txtContrasena);

        gbc.gridx = 0; gbc.gridy = 6;
        panel.add(btnVolver, gbc);
        gbc.gridx = 1;
        panel.add(btnRegistrar, gbc);

        btnRegistrar.addActionListener(e -> registrar());
        btnVolver.addActionListener(e -> {
            new VentanaBienvenida(usuarios, todosLosJuegos,
                inventarioPrestamo, inventarioVenta, turnos,ventas,prestamos,itemsMenu, torneosDisponibles).setVisible(true);
            dispose();
        });

        add(panel);
    }

    private void agregarFila(JPanel panel, GridBagConstraints gbc,
                              int fila, String etiqueta, JComponent campo) {
        gbc.gridx = 0; gbc.gridy = fila; gbc.weightx = 0.35;
        panel.add(new JLabel(etiqueta), gbc);
        gbc.gridx = 1; gbc.weightx = 0.65;
        panel.add(campo, gbc);
    }

    private void registrar() {
        String nombre     = txtNombre.getText().trim();
        String apellido   = txtApellido.getText().trim();
        String correo     = txtCorreo.getText().trim();
        String login      = txtLogin.getText().trim();
        String contrasena = new String(txtContrasena.getPassword()).trim();

        if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty()
                || login.isEmpty() || contrasena.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.",
                "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        for (Persona p : usuarios) {
            if (p.getLogin().equals(login)) {
                JOptionPane.showMessageDialog(this, "Ese login ya está en uso.",
                    "Error", JOptionPane.WARNING_MESSAGE);
                return;
            }
        }

        usuarios.add(new Cliente(nombre, apellido, correo, contrasena, login, 0));

        JOptionPane.showMessageDialog(this, "Cuenta creada. Inicia sesión.",
            "Registro exitoso", JOptionPane.INFORMATION_MESSAGE);

        new VentanaLogin(usuarios, todosLosJuegos,
            inventarioPrestamo, inventarioVenta, turnos,ventas,prestamos,itemsMenu, torneosDisponibles).setVisible(true);
        dispose();
    }
}

