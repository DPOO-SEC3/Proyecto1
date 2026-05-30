package interfaz;

import Modelo.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VentanaTorneos extends JFrame {

    private static final long serialVersionUID = 1L;

    private Persona usuario;
    private List<Torneo> torneosDisponibles;
    private List<TurnoSemanal> turnos;

    private DefaultListModel<Torneo> modeloLista;
    private JList<Torneo> listaTorneos;

    public VentanaTorneos(Persona usuario, List<Torneo> torneosDisponibles, List<TurnoSemanal> turnos) {
        this.usuario = usuario;
        this.torneosDisponibles = torneosDisponibles;
        this.turnos = turnos;

        setTitle("Torneos disponibles");
        setSize(520, 380);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        modeloLista = new DefaultListModel<>();
        listaTorneos = new JList<>(modeloLista);
        cargarTorneos();

        JScrollPane scroll = new JScrollPane(listaTorneos);

        JButton btnInscribir = new JButton("Inscribirme");
        btnInscribir.addActionListener(e -> inscribirse());

        JButton btnRetirar = new JButton("Retirarme");
        btnRetirar.addActionListener(e -> retirarse());

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());

        JPanel botones = new JPanel();
        botones.add(btnInscribir);
        botones.add(btnRetirar);
        botones.add(btnCerrar);

        add(new JLabel("Seleccione un torneo", SwingConstants.CENTER), BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
    }

    private void cargarTorneos() {
        modeloLista.clear();

        for (Torneo t : torneosDisponibles) {
            modeloLista.addElement(t);
        }
    }

    private void inscribirse() {
        Torneo torneo = listaTorneos.getSelectedValue();

        if (torneo == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un torneo.");
            return;
        }

        if (usuario instanceof Empleado && empleadoEstaEnTurnoEseDia((Empleado) usuario, torneo.getDiaSemana())) {
            JOptionPane.showMessageDialog(this, "No puede inscribirse porque está en turno ese día.");
            return;
        }

        if (torneo.getParticipantes().contains(usuario)) {
            JOptionPane.showMessageDialog(this, "Ya estás inscrito en este torneo.");
            return;
        }

        torneo.agregarParticipante(usuario);
        cargarTorneos();

        JOptionPane.showMessageDialog(this, "Inscripción realizada correctamente.");
    }

    private void retirarse() {
        Torneo torneo = listaTorneos.getSelectedValue();

        if (torneo == null) {
            JOptionPane.showMessageDialog(this, "Seleccione un torneo.");
            return;
        }

        torneo.retirarParticipante(usuario);
        cargarTorneos();

        JOptionPane.showMessageDialog(this, "Retiro realizado correctamente.");
    }

    private boolean empleadoEstaEnTurnoEseDia(Empleado empleado, String diaTorneo) {
        for (TurnoSemanal t : turnos) {
            if (t.getEmpleado().equals(empleado)
                    && t.getDiaSemana().equalsIgnoreCase(diaTorneo)) {
                return true;
            }
        }
        return false;
    }
}