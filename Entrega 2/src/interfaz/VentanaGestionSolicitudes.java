package interfaz;

import Modelo.*;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class VentanaGestionSolicitudes extends JFrame {
	
	private static final long serialVersionUID = 1L;

    private Administrador admin;
    private List<Persona> usuarios;

    private DefaultListModel<SolicitudCambioTurno> modeloLista;
    private JList<SolicitudCambioTurno> listaSolicitudes;

    public VentanaGestionSolicitudes(Administrador admin, List<Persona> usuarios) {
        this.admin = admin;
        this.usuarios = usuarios;

        setTitle("Gestión de solicitudes de cambio");
        setSize(520, 380);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        modeloLista = new DefaultListModel<>();
        listaSolicitudes = new JList<>(modeloLista);
        listaSolicitudes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cargarSolicitudes();

        JScrollPane scroll = new JScrollPane(listaSolicitudes);

        JButton btnAprobar = new JButton("Aprobar");
        btnAprobar.addActionListener(e -> aprobarSolicitud());

        JButton btnRechazar = new JButton("Rechazar");
        btnRechazar.addActionListener(e -> rechazarSolicitud());

        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(e -> dispose());

        JPanel botones = new JPanel();
        botones.add(btnAprobar);
        botones.add(btnRechazar);
        botones.add(btnCerrar);

        add(new JLabel("Solicitudes pendientes", SwingConstants.CENTER), BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(botones, BorderLayout.SOUTH);
    }

    private void cargarSolicitudes() {
        modeloLista.clear();

        for (SolicitudCambioTurno s : obtenerSolicitudesPendientes()) {
            modeloLista.addElement(s);
        }
    }

    private List<SolicitudCambioTurno> obtenerSolicitudesPendientes() {
        List<SolicitudCambioTurno> solicitudes = new ArrayList<>();

        for (Persona p : usuarios) {
            if (p instanceof Empleado) {
                Empleado e = (Empleado) p;

                for (SolicitudCambioTurno s : e.getSolicitudesCambio()) {
                    if (s.estaPendiente()) {
                        solicitudes.add(s);
                    }
                }
            }
        }

        return solicitudes;
    }

    private void aprobarSolicitud() {
        SolicitudCambioTurno solicitud = listaSolicitudes.getSelectedValue();

        if (solicitud == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una solicitud.");
            return;
        }

        solicitud.aprobar();
        cargarSolicitudes();

        JOptionPane.showMessageDialog(this, "Solicitud aprobada.");
    }

    private void rechazarSolicitud() {
        SolicitudCambioTurno solicitud = listaSolicitudes.getSelectedValue();

        if (solicitud == null) {
            JOptionPane.showMessageDialog(this, "Seleccione una solicitud.");
            return;
        }

        solicitud.rechazar();
        cargarSolicitudes();

        JOptionPane.showMessageDialog(this, "Solicitud rechazada.");
    }
}