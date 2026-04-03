package modelo;

import java.util.List;

public abstract class Empleado extends Persona {

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

    public abstract Prestamo solicitarPrestamo(EjemplarJuego ejemplar);

    public abstract void devolverJuego(Prestamo prestamo);

    public abstract VentaJuego comprarJuego(JuegoMesa juego, double descuento, double puntosUsados);

    public abstract VentaCafeteria comprarCafeteria(List<ItemMenu> items, double propina, double puntosUsados);

    public abstract void agregarFavorito(JuegoMesa juego);

    public abstract void eliminarFavorito(JuegoMesa juego);
}