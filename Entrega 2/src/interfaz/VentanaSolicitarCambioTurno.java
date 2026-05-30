package interfaz;

import Modelo.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VentanaSolicitarCambioTurno extends JFrame {
	
	private static final long serialVersionUID = 1L;

    private Empleado empleado;
    private List<Persona> usuarios;

    private JTextField txtTipo;
    private JTextArea txtMotivo;
    private JComboBox<Empleado> comboEmpleadoDestino;

    public VentanaSolicitarCambioTurno(Empleado empleado, List<Persona> usuarios) {
        this.empleado = empleado;
        this.usuarios = usuarios;

        setTitle("Solicitar cambio de turno");
        setSize(420, 330);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JPanel formulario = new JPanel(new GridLayout(4, 1, 8, 8));

        txtTipo = new JTextField();
        txtMotivo = new JTextArea(4, 20);
        txtMotivo.setLineWrap(true);
        txtMotivo.setWrapStyleWord(true);

        comboEmpleadoDestino = new JComboBox<>();
        comboEmpleadoDestino.addItem(null);

        for (Persona p : usuarios) {
            if (p instanceof Empleado && p != empleado) {
                comboEmpleadoDestino.addItem((Empleado) p);
            }
        }

        formulario.add(crearCampo("Tipo de solicitud:", txtTipo));
        formulario.add(crearCampo("Motivo:", new JScrollPane(txtMotivo)));
        formulario.add(crearCampo("Empleado destino (opcional):", comboEmpleadoDestino));

        JButton btnCrear = new JButton("Crear solicitud");
        btnCrear.addActionListener(e -> crearSolicitud());

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());

        JPanel botones = new JPanel();
        botones.add(btnCerrar);
        botones.add(btnCrear);

        panel.add(new JLabel("Nueva solicitud de cambio de turno"), BorderLayout.NORTH);
        panel.add(formulario, BorderLayout.CENTER);
        panel.add(botones, BorderLayout.SOUTH);

        add(panel);
    }

    private JPanel crearCampo(String etiqueta, Component componente) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(new JLabel(etiqueta), BorderLayout.NORTH);
        panel.add(componente, BorderLayout.CENTER);
        return panel;
    }

    private void crearSolicitud() {
        String tipo = txtTipo.getText().trim();
        String motivo = txtMotivo.getText().trim();
        Empleado destino = (Empleado) comboEmpleadoDestino.getSelectedItem();

        if (tipo.isEmpty() || motivo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar tipo y motivo.");
            return;
        }

        empleado.solicitarCambioTurno(tipo, motivo, destino);

        JOptionPane.showMessageDialog(this, "Solicitud creada correctamente.");
        dispose();
    }
}