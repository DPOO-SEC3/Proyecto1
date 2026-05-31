package Modelo;

import java.util.List;

public abstract class Empleado extends Persona implements ISolicitarPrestamo,IComprar, IJuegosFavoritos, IInscripcionTorneo {

    public Empleado(String nombre, String apellido, String correoElectronico, String contrasena, String login) {
        super(nombre, apellido, correoElectronico, contrasena, login);
    }

    public abstract TurnoSemanal getTurnoSemanal();

    public abstract boolean estaEnTurno();

    public abstract List<SolicitudCambioTurno> getSolicitudesCambio();

    public abstract List<SugerenciaPlatillo> getSugerencias();

    public abstract List<Prestamo> getPrestamos();

    public abstract List<Venta> getVentas();

    public abstract List<JuegoMesa> getJuegosFavoritos();

    public abstract SolicitudCambioTurno solicitarCambioTurno(String tipo, String motivo, Empleado otro);

    public abstract SugerenciaPlatillo sugerirPlatillo(String descripcion);

    public abstract Prestamo solicitarPrestamo(InventarioPrestamo inventarioPrestamo,EjemplarJuego ejemplar, Mesa mesa);

    public abstract void agregarFavorito(JuegoMesa juego);

    public abstract void eliminarFavorito(JuegoMesa juego);
    
    public abstract Prestamo buscarPrestamoActivo(String nombreJuego);
}