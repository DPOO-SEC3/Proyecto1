package Modelo;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalTime;

public class Administrador extends Persona {
	
	private InventarioPrestamo inventarioPrestamo = new InventarioPrestamo(100);
	private InventarioVenta inventarioVenta = new InventarioVenta(100);

	private List<SugerenciaPlatillo> sugerencias = new ArrayList<>();
	private List<SolicitudCambioTurno> solicitudes = new ArrayList<>();
	private List<TurnoSemanal> turnos = new ArrayList<>();
	private List<Venta> ventas = new ArrayList<>();
	private List<Prestamo> prestamos = new ArrayList<>();
	private List<ItemMenu> itemsMenu = new ArrayList<>();

    public Administrador(String nombre, String apellido, String correoElectronico, String contrasena, String login) {
        super(nombre, apellido, correoElectronico, contrasena, login);
    }

    public InventarioPrestamo getInventarioPrestamo() {
        return inventarioPrestamo;
    }

    public InventarioVenta getInventarioVenta() {
        return inventarioVenta;
    }

    public List<SugerenciaPlatillo> getSugerenciasPendientes() {
        return sugerencias;
    }

    public List<SolicitudCambioTurno> getSolicitudesCambioPendientes() {
        return solicitudes;
    }

    public List<TurnoSemanal> getTurnos() {
        return turnos;
    }

    public List<Venta> consultarVentas(LocalDate desde, LocalDate hasta) {
        List<Venta> resultado = new ArrayList<>();
        for (Venta v : ventas) {
            LocalDate fecha = v.getFechaHora().toLocalDate();
            if ((fecha.isEqual(desde) || fecha.isAfter(desde)) &&
                (fecha.isEqual(hasta) || fecha.isBefore(hasta))) {
                resultado.add(v);
            }
        }
        return resultado;
    }

    public List<Prestamo> consultarHistorialPrestamos() {
        return prestamos;
    }

    public List<ItemMenu> getItemsMenu() {
        return itemsMenu;
    }

    public void agregarEjemplarAPrestamo(EjemplarJuego ejemplar) {
        inventarioPrestamo.getEjemplares().add(ejemplar);
    }

    public void agregarJuegoAVenta(JuegoMesa juego) {
        inventarioVenta.getJuegos().add(juego);
    }

    public void moverDeVentaAPrestamo(JuegoMesa juego) {
        inventarioVenta.getJuegos().remove(juego);
    }

    public void repararEjemplar(EjemplarJuego ejemplar) {
        ejemplar.setEstado("Reparado");
        ejemplar.setDisponible(true);
    }

    public void marcarEjemplarDesaparecido(EjemplarJuego ejemplar) {
        ejemplar.marcarDesaparecido();
    }

    public void agregarItemMenu(ItemMenu item) {
        itemsMenu.add(item);
    }

    public void aprobarSugerencia(SugerenciaPlatillo sugerencia) {
        sugerencias.remove(sugerencia);
    }

    public void rechazarSugerencia(SugerenciaPlatillo sugerencia) {
        sugerencias.remove(sugerencia);
    }

    public TurnoSemanal crearTurnoSemanal(Empleado empleado, String dia, LocalTime inicio, LocalTime fin) {
        TurnoSemanal turno = new TurnoSemanal(dia, inicio, fin, empleado);
        turnos.add(turno);
        return turno;
    }

    public void modificarTurnoSemanal(TurnoSemanal turno, String dia, LocalTime inicio, LocalTime fin) {
        turno.setDiaSemana(dia);
        turno.setHoraInicio(inicio);
        turno.setHoraFin(fin);
    }

    public void aprobarSolicitudCambio(SolicitudCambioTurno solicitud) {
        solicitud.aprobar();
        solicitudes.remove(solicitud); 
    }

    public void rechazarSolicitudCambio(SolicitudCambioTurno solicitud) {
        solicitud.rechazar(); 
        solicitudes.remove(solicitud);
    }

    public String generarReporteVentas(LocalDate desde, LocalDate hasta, String granularidad) {
        List<Venta> lista = consultarVentas(desde, hasta);
        double total = 0;

        for (Venta v : lista) {
            total += v.getSubtotal();
        }

        return "Ventas desde " + desde + " hasta " + hasta + ": " + total;
    }
    
    public Torneo crearTorneo(String nombre, int numeroParticipantes, JuegoMesa juego, String diaSemana, String tipo) {
		Torneo torneo = new Torneo(nombre, numeroParticipantes, juego, diaSemana, tipo);
		return torneo;
	}
}