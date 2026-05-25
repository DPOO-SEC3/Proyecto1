package interfaz;

import Modelo.*;
import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * Ventana de inicio de sesión.
 * Redirige automáticamente al menú correspondiente según el rol del usuario.
 */
public class VentanaLogin extends JFrame {

    private JTextField    txtLogin;
    private JPasswordField txtContrasena;

    private List<Persona>      usuarios;
    private List<JuegoMesa>    todosLosJuegos;
    private InventarioPrestamo inventarioPrestamo;
    private InventarioVenta    inventarioVenta;
    private List<TurnoSemanal> turnos;
    private List<Venta> ventas;
    private List<Prestamo> prestamos;

    public VentanaLogin(List<Persona> usuarios,
                        List<JuegoMesa> todosLosJuegos,
                        InventarioPrestamo inventarioPrestamo,
                        InventarioVenta inventarioVenta,
                        List<TurnoSemanal> turnos) {
        this.usuarios          = usuarios;
        this.todosLosJuegos    = todosLosJuegos;
        this.inventarioPrestamo = inventarioPrestamo;
        this.inventarioVenta   = inventarioVenta;
        this.turnos            = turnos;

        setTitle("Iniciar sesión");
        setSize(350, 230);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(7, 5, 7, 5);

        JLabel titulo = new JLabel("Iniciar sesión");
        titulo.setFont(new Font("Arial", Font.BOLD, 15));

        txtLogin      = new JTextField();
        txtContrasena = new JPasswordField();

        JButton btnEntrar = new JButton("Entrar");
        JButton btnVolver = new JButton("Volver");

        // Fila 0 – título
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        panel.add(titulo, gbc);
        gbc.gridwidth = 1;

        // Fila 1 – login
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        panel.add(new JLabel("Login:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        panel.add(txtLogin, gbc);

        // Fila 2 – contraseña
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        panel.add(new JLabel("Contraseña:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        panel.add(txtContrasena, gbc);

        // Fila 3 – botones
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.5;
        panel.add(btnVolver, gbc);
        gbc.gridx = 1;
        panel.add(btnEntrar, gbc);

        // Permitir login con Enter
        getRootPane().setDefaultButton(btnEntrar);

        btnEntrar.addActionListener(e -> intentarLogin());
        btnVolver.addActionListener(e -> {
            new VentanaBienvenida(usuarios, todosLosJuegos,
                inventarioPrestamo, inventarioVenta, turnos).setVisible(true);
            dispose();
        });

        add(panel);
    }

    private void intentarLogin() {
        String login      = txtLogin.getText().trim();
        String contrasena = new String(txtContrasena.getPassword()).trim();

        for (Persona p : usuarios) {
            if (p.getLogin().equals(login) && p.getContrasena().equals(contrasena)) {
                abrirMenuSegunRol(p);
                dispose();
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Login o contraseña incorrectos.",
            "Error", JOptionPane.WARNING_MESSAGE);
        txtContrasena.setText("");
    }

    private void abrirMenuSegunRol(Persona usuario) {
        if (usuario instanceof Administrador) {
            new MenuAdministrador((Administrador) usuario, usuarios,
                todosLosJuegos, inventarioPrestamo, inventarioVenta, turnos)
                .setVisible(true);

        } else if (usuario instanceof Empleado) {
            new MenuEmpleado((Empleado) usuario, turnos).setVisible(true);

        } else if (usuario instanceof Cliente) {
            new MenuCliente((Cliente) usuario, todosLosJuegos,
                inventarioPrestamo, inventarioVenta).setVisible(true);
        }
    }
}