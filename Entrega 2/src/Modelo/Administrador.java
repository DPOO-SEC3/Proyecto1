package Modelo;

import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;

public class Administrador extends Persona {

    public Administrador(String nombre, String apellido, String correoElectronico, String contrasena, String login) {
        super(nombre, apellido, correoElectronico, contrasena, login);
    }

    public InventarioPrestamo getInventarioPrestamo() {
        return null;
    }

    public InventarioVenta getInventarioVenta() {
        return null;
    }

    public List<SugerenciaPlatillo> getSugerenciasPendientes() {
        return null;
    }

    public List<SolicitudCambioTurno> getSolicitudesCambioPendientes() {
        return null;
    }

    public List<TurnoSemanal> getTurnos() {
        return null;
    }

    public List<Venta> consultarVentas(LocalDate desde, LocalDate hasta) {
        return null;
    }

    public List<Prestamo> consultarHistorialPrestamos() {
        return null;
    }

    public List<ItemMenu> getItemsMenu() {
        return null;
    }

    public void agregarEjemplarAPrestamo(EjemplarJuego ejemplar) {

    }

    public void agregarJuegoAVenta(JuegoMesa juego) {

    }

    public void moverDeVentaAPrestamo(JuegoMesa juego) {

    }

    public void repararEjemplar(EjemplarJuego ejemplar) {

    }

    public void marcarEjemplarDesaparecido(EjemplarJuego ejemplar) {

    }

    public void agregarItemMenu(ItemMenu item) {

    }

    public void aprobarSugerencia(SugerenciaPlatillo sugerencia) {

    }

    public void rechazarSugerencia(SugerenciaPlatillo sugerencia) {

    }

    public TurnoSemanal crearTurnoSemanal(Empleado empleado, String dia, LocalTime inicio, LocalTime fin) {
        return null;
    }

    public void modificarTurnoSemanal(TurnoSemanal turno, String dia, LocalTime inicio, LocalTime fin) {

    }

    public void aprobarSolicitudCambio(SolicitudCambioTurno solicitud) {

    }

    public void rechazarSolicitudCambio(SolicitudCambioTurno solicitud) {

    }

    public String generarReporteVentas(LocalDate desde, LocalDate hasta, String granularidad) {
        return null;
    }
}