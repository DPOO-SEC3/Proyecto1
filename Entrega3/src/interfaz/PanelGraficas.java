package interfaz;
 
import Modelo.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
 
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
 
/**
 * Ventana de gráficas para el Administrador.
 * Contiene tres pestañas:
 *   1. Pastel  – distribución copias préstamo vs venta de un juego
 *   2. Barras  – ventas cafetería vs juegos en 5 días (sin impuestos)
 *   3. Líneas  – número de préstamos por día durante una semana
 */
public class PanelGraficas extends JFrame {
 
    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("d/M/yy");
 
    private Administrador      admin;
    private List<JuegoMesa>    todosLosJuegos;
    private InventarioPrestamo inventarioPrestamo;
    private InventarioVenta    inventarioVenta;
    private List<Venta>        ventas;
    private List<Prestamo>     prestamos;
 
    public PanelGraficas(Administrador admin,
                         List<JuegoMesa> todosLosJuegos,
                         InventarioPrestamo inventarioPrestamo,
                         InventarioVenta inventarioVenta,
                         List<Venta> ventas,
                         List<Prestamo> prestamos) {
        this.admin             = admin;
        this.todosLosJuegos    = todosLosJuegos;
        this.inventarioPrestamo = inventarioPrestamo;
        this.inventarioVenta   = inventarioVenta;
        this.ventas            = ventas;
        this.prestamos         = prestamos;
 
        setTitle("Gráficas – DulcesnDados");
        setSize(860, 560);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
 
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Disponibilidad por juego", panelPastel());
        tabs.addTab("Ventas por día",            panelBarras());
        tabs.addTab("Préstamos por semana",      panelLineas());
 
        add(tabs);
    }
 
    // ---------------------------------------------------------------
    // 1. GRÁFICA DE PASTEL
    // ---------------------------------------------------------------
    private JPanel panelPastel() {
        JPanel contenedor = new JPanel(new BorderLayout(8, 8));
        contenedor.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
 
        // Selector de juego
        JPanel controles = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controles.add(new JLabel("Juego:"));
        JComboBox<String> combo = new JComboBox<>();
        for (JuegoMesa j : todosLosJuegos) combo.addItem(j.getNombre());
        controles.add(combo);
 
        // Panel de gráfica (se reemplaza al cambiar selección)
        JPanel[] wrapper = { new JPanel(new BorderLayout()) };
        wrapper[0].add(crearPastel((String) combo.getSelectedItem()), BorderLayout.CENTER);
 
        combo.addActionListener(e -> {
            wrapper[0].removeAll();
            wrapper[0].add(crearPastel((String) combo.getSelectedItem()), BorderLayout.CENTER);
            wrapper[0].revalidate();
            wrapper[0].repaint();
        });
 
        contenedor.add(controles, BorderLayout.NORTH);
        contenedor.add(wrapper[0], BorderLayout.CENTER);
        return contenedor;
    }
 
    private ChartPanel crearPastel(String nombreJuego) {
        DefaultPieDataset dataset = new DefaultPieDataset();
 
        if (nombreJuego != null) {
            // Copias en préstamo
            long enPrestamo = inventarioPrestamo.getEjemplares().stream()
                .filter(e -> e.getJuegoMesa().getNombre().equalsIgnoreCase(nombreJuego))
                .count();
 
            // Copias en venta (el juego aparece una vez por cada unidad en la lista)
            long enVenta = inventarioVenta.getJuegos().stream()
                .filter(j -> j.getNombre().equalsIgnoreCase(nombreJuego))
                .count();
 
            dataset.setValue("Copias Préstamo",  enPrestamo);
            dataset.setValue("Copias Inventario", enVenta);
        }
 
        JFreeChart chart = ChartFactory.createPieChart(
            "Disponibilidad – " + nombreJuego,
            dataset, true, true, false
        );
        return new ChartPanel(chart);
    }
 
    // ---------------------------------------------------------------
    // 2. GRÁFICA DE BARRAS
    // ---------------------------------------------------------------
    private JPanel panelBarras() {
        JPanel contenedor = new JPanel(new BorderLayout(8, 8));
        contenedor.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
 
        // Selectores de fecha inicio
        JPanel controles = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controles.add(new JLabel("Fecha inicio (YYYY-MM-DD):"));
        JTextField txtFecha = new JTextField(LocalDate.now().minusDays(4).toString(), 12);
        controles.add(txtFecha);
        JButton btnActualizar = new JButton("Actualizar");
        controles.add(btnActualizar);
 
        JPanel[] wrapper = { new JPanel(new BorderLayout()) };
        LocalDate[] inicio = { LocalDate.now().minusDays(4) };
        wrapper[0].add(crearBarras(inicio[0]), BorderLayout.CENTER);
 
        btnActualizar.addActionListener(e -> {
            try {
                inicio[0] = LocalDate.parse(txtFecha.getText().trim());
                wrapper[0].removeAll();
                wrapper[0].add(crearBarras(inicio[0]), BorderLayout.CENTER);
                wrapper[0].revalidate();
                wrapper[0].repaint();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Formato de fecha inválido. Use YYYY-MM-DD.", "Error",
                    JOptionPane.WARNING_MESSAGE);
            }
        });
 
        contenedor.add(controles, BorderLayout.NORTH);
        contenedor.add(wrapper[0], BorderLayout.CENTER);
        return contenedor;
    }
 
    private ChartPanel crearBarras(LocalDate inicio) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        LocalDate fin = inicio.plusDays(4); // 5 días inclusive
 
        for (LocalDate dia = inicio; !dia.isAfter(fin); dia = dia.plusDays(1)) {
            String etiqueta = dia.format(FMT);
            final LocalDate diaFinal = dia;
 
            // Ventas cafetería sin impuesto al consumo (8%)
            double cafeteria = ventas.stream()
                .filter(v -> v instanceof VentaCafeteria)
                .filter(v -> v.getFechaHora().toLocalDate().equals(diaFinal))
                .mapToDouble(v -> {
                    VentaCafeteria vc = (VentaCafeteria) v;
                    double sub = vc.getSubtotal();
                    return sub; // subtotal ya no incluye impuesto
                })
                .sum();
 
            // Ventas juegos sin IVA (19%)
            double juegos = ventas.stream()
                .filter(v -> v instanceof VentaJuego)
                .filter(v -> v.getFechaHora().toLocalDate().equals(diaFinal))
                .mapToDouble(v -> v.getSubtotal())
                .sum();
 
            dataset.addValue(cafeteria, "Cafetería", etiqueta);
            dataset.addValue(juegos,    "Juegos",    etiqueta);
        }
 
        JFreeChart chart = ChartFactory.createBarChart(
            "Ventas por período (" + fin.format(FMT) + ")",
            "Categorías", "Valor en Pesos",
            dataset, PlotOrientation.VERTICAL,
            true, true, false
        );
        return new ChartPanel(chart);
    }
 
    // ---------------------------------------------------------------
    // 3. GRÁFICA DE LÍNEAS
    // ---------------------------------------------------------------
    private JPanel panelLineas() {
        JPanel contenedor = new JPanel(new BorderLayout(8, 8));
        contenedor.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
 
        // Selector de fecha inicio de semana
        JPanel controles = new JPanel(new FlowLayout(FlowLayout.LEFT));
        controles.add(new JLabel("Inicio de semana (YYYY-MM-DD):"));
        JTextField txtFecha = new JTextField(LocalDate.now().minusDays(6).toString(), 12);
        controles.add(txtFecha);
        JButton btnActualizar = new JButton("Actualizar");
        controles.add(btnActualizar);
 
        JPanel[] wrapper = { new JPanel(new BorderLayout()) };
        LocalDate[] inicio = { LocalDate.now().minusDays(6) };
        wrapper[0].add(crearLineas(inicio[0]), BorderLayout.CENTER);
 
        btnActualizar.addActionListener(e -> {
            try {
                inicio[0] = LocalDate.parse(txtFecha.getText().trim());
                wrapper[0].removeAll();
                wrapper[0].add(crearLineas(inicio[0]), BorderLayout.CENTER);
                wrapper[0].revalidate();
                wrapper[0].repaint();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                    "Formato de fecha inválido. Use YYYY-MM-DD.", "Error",
                    JOptionPane.WARNING_MESSAGE);
            }
        });
 
        contenedor.add(controles, BorderLayout.NORTH);
        contenedor.add(wrapper[0], BorderLayout.CENTER);
        return contenedor;
    }
 
    private ChartPanel crearLineas(LocalDate inicioSemana) {
        String[] dias = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};
        XYSeries series = new XYSeries("Reservas");
 
        for (int i = 0; i < 7; i++) {
            final LocalDate dia = inicioSemana.plusDays(i);
            long cantidad = prestamos.stream()
                .filter(p -> p.getFechaHoraInicio().toLocalDate().equals(dia))
                .count();
            series.add(i, cantidad);
        }
 
        XYSeriesCollection dataset = new XYSeriesCollection(series);
 
        JFreeChart chart = ChartFactory.createXYLineChart(
            "Reservas Semana " + inicioSemana.format(FMT),
            "Día de la Semana", "Número Reservas",
            dataset, PlotOrientation.VERTICAL,
            true, true, false
        );
 
        // Reemplazar números del eje X por nombres de días
        org.jfree.chart.axis.SymbolAxis ejeX =
            new org.jfree.chart.axis.SymbolAxis("Día de la Semana", dias);
        ((org.jfree.chart.plot.XYPlot) chart.getPlot()).setDomainAxis(ejeX);
 
        return new ChartPanel(chart);
    }
}