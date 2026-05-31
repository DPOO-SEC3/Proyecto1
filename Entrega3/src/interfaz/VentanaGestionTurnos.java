package interfaz;

import Modelo.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalTime;
import java.util.List;

public class VentanaGestionTurnos extends JFrame {
	
	private static final long serialVersionUID = 1L;

    private Administrador admin;
    private List<Persona> usuarios;
    private List<TurnoSemanal> turnos;

    private DefaultListModel<TurnoSemanal> modeloLista;
    private JList<TurnoSemanal> listaTurnos;

    private JComboBox<Empleado> comboEmpleado;
    private JTextField txtDia;
    private JTextField txtInicio;
    private JTextField txtFin;

    public VentanaGestionTurnos(Administrador admin, List<Persona> usuarios, List<TurnoSemanal> turnos) {
        this.admin = admin;
        this.usuarios = usuarios;
        this.turnos = turnos;

        setTitle("Gestión de turnos");
        setSize(620, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        modeloLista = new DefaultListModel<>();
        listaTurnos = new JList<>(modeloLista);
        cargarTurnos();

        JScrollPane scroll = new JScrollPane(listaTurnos);

        JPanel formulario = crearFormulario();

        JButton btnCrear = new JButton("Crear turno");
        btnCrear.addActionListener(e -> crearTurno());

        JButton btnModificar = new JButton("Modificar seleccionado");
        btnModificar.addActionListener(e -> modificarTurno());

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());

        JPanel botones = new JPanel();
        botones.add(btnCrear);
        botones.add(btnModificar);
        botones.add(btnCerrar);

        add(new JLabel("Gestión de turnos semanales", SwingConstants.CENTER), BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(formulario, BorderLayout.WEST);
        add(botones, BorderLayout.SOUTH);
    }

    private JPanel crearFormulario() {
        JPanel panel = new JPanel(new GridLayout(8, 1, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        comboEmpleado = new JComboBox<>();
        for (Persona p : usuarios) {
            if (p instanceof Empleado) {
                comboEmpleado.addItem((Empleado) p);
            }
        }

        txtDia = new JTextField();
        txtInicio = new JTextField("08:00");
        txtFin = new JTextField("16:00");

        panel.add(new JLabel("Empleado:"));
        panel.add(comboEmpleado);

        panel.add(new JLabel("Día:"));
        panel.add(txtDia);

        panel.add(new JLabel("Hora inicio (HH:mm):"));
        panel.add(txtInicio);

        panel.add(new JLabel("Hora fin (HH:mm):"));
        panel.add(txtFin);

        return panel;
    }

    private void cargarTurnos() {
        modeloLista.clear();

        for (TurnoSemanal t : turnos) {
            modeloLista.addElement(t);
        }
    }

    private void crearTurno() {
        try {
            Empleado empleado = (Empleado) comboEmpleado.getSelectedItem();
            String dia = txtDia.getText().trim();
            LocalTime inicio = LocalTime.parse(txtInicio.getText().trim());
            LocalTime fin = LocalTime.parse(txtFin.getText().trim());

            if (empleado == null || dia.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe seleccionar empleado y escribir día.");
                return;
            }

            TurnoSemanal nuevo = new TurnoSemanal(dia, inicio, fin, empleado);
            turnos.add(nuevo);

            cargarTurnos();
            JOptionPane.showMessageDialog(this, "Turno creado correctamente.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al crear turno. Revise el formato de hora HH:mm.");
        }
    }

    private void modificarTurno() {
        TurnoSemanal turno = listaTurnos.getSelectedValue();

        if (turno == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un turno.");
            return;
        }

        try {
            String dia = txtDia.getText().trim();
            LocalTime inicio = LocalTime.parse(txtInicio.getText().trim());
            LocalTime fin = LocalTime.parse(txtFin.getText().trim());

            if (dia.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe escribir un día.");
                return;
            }

            admin.modificarTurnoSemanal(turno, dia, inicio, fin);

            cargarTurnos();
            JOptionPane.showMessageDialog(this, "Turno modificado correctamente.");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al modificar turno. Revise el formato de hora HH:mm.");
        }
    }
}