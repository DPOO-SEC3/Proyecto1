package interfaz;

import Modelo.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class VentanaCrearTorneo extends JFrame {

    private static final long serialVersionUID = 1L;

    private Administrador admin;
    private List<Torneo> torneosDisponibles;
    private List<JuegoMesa> juegos;

    private JTextField txtNombre;
    private JTextField txtParticipantes;
    private JTextField txtDia;
    private JComboBox<JuegoMesa> comboJuegos;
    private JComboBox<String> comboTipo;

    public VentanaCrearTorneo(Administrador admin, List<Torneo> torneosDisponibles, List<JuegoMesa> juegos) {
        this.admin = admin;
        this.torneosDisponibles = torneosDisponibles;
        this.juegos = juegos;

        setTitle("Crear torneo");
        setSize(420, 360);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel panel = new JPanel(new GridLayout(6, 2, 8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        txtNombre = new JTextField();
        txtParticipantes = new JTextField();
        txtDia = new JTextField();

        comboJuegos = new JComboBox<>();
        for (JuegoMesa juego : juegos) {
            comboJuegos.addItem(juego);
        }

        comboTipo = new JComboBox<>(new String[] {"Amistoso", "Competitivo"});

        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);

        panel.add(new JLabel("Número participantes:"));
        panel.add(txtParticipantes);

        panel.add(new JLabel("Juego:"));
        panel.add(comboJuegos);

        panel.add(new JLabel("Día semana:"));
        panel.add(txtDia);

        panel.add(new JLabel("Tipo:"));
        panel.add(comboTipo);

        JButton btnCrear = new JButton("Crear");
        JButton btnCerrar = new JButton("Cerrar");

        btnCrear.addActionListener(e -> crearTorneo());
        btnCerrar.addActionListener(e -> dispose());

        panel.add(btnCerrar);
        panel.add(btnCrear);

        add(panel);
    }

    private void crearTorneo() {
        try {
            String nombre = txtNombre.getText().trim();
            int participantes = Integer.parseInt(txtParticipantes.getText().trim());
            JuegoMesa juego = (JuegoMesa) comboJuegos.getSelectedItem();
            String dia = txtDia.getText().trim();
            String tipo = (String) comboTipo.getSelectedItem();

            if (nombre.isEmpty() || dia.isEmpty() || juego == null) {
                JOptionPane.showMessageDialog(this, "Debe llenar todos los campos.");
                return;
            }

            Torneo torneo = admin.crearTorneo(nombre, participantes, juego, dia, tipo);
            torneosDisponibles.add(torneo);

            JOptionPane.showMessageDialog(this, "Torneo creado correctamente.");
            dispose();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al crear torneo: " + e.getMessage());
        }
    }
}