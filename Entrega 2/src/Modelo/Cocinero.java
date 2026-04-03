package Modelo;

import java.util.List;

public class Cocinero extends Empleado {

    public Cocinero(String nombre, String apellido, String correoElectronico, String contrasena, String login) {
        super(nombre, apellido, correoElectronico, contrasena, login);
    }

    @Override
    public TurnoSemanal getTurnoSemanal() {
        return null;
    }

    @Override
    public boolean estaEnTurno() {
        return false;
    }

    @Override
    public List<SolicitudCambioTurno> getSolicitudesCambio() {
        return null;
    }

    @Override
    public List<SugerenciaPlatillo> getSugerencias() {
        return null;
    }

    @Override
    public List<Prestamo> getPrestamos() {
        return null;
    }

    @Override
    public List<Venta> getVentas() {
        return null;
    }

    @Override
    public List<JuegoMesa> getJuegosFavoritos() {
        return null;
    }

    @Override
    public SolicitudCambioTurno solicitarCambioTurno(String tipo, String motivo, Empleado otro) {
        return null;
    }

    @Override
    public SugerenciaPlatillo sugerirPlatillo(String descripcion) {
        return null;
    }

    @Override
    public Prestamo solicitarPrestamo(EjemplarJuego ejemplar) {
        return null;
    }

    @Override
    public void devolverJuego(Prestamo prestamo) {

    }

    @Override
    public VentaJuego comprarJuego(JuegoMesa juego, double descuento, double puntosUsados) {
        return null;
    }

    @Override
    public VentaCafeteria comprarCafeteria(List<ItemMenu> items, double propina, double puntosUsados) {
        return null;
    }

    @Override
    public void agregarFavorito(JuegoMesa juego) {

    }

    @Override
    public void eliminarFavorito(JuegoMesa juego) {

    }
}