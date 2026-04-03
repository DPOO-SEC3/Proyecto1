package Modelo;

import java.util.List;

public class Cliente extends Persona {

    private double puntosFidelidadAcumulados;

    public Cliente(String nombre, String apellido, String correoElectronico, String contrasena, String login, double puntosFidelidadAcumulados) {
        super(nombre, apellido, correoElectronico, contrasena, login);
        this.puntosFidelidadAcumulados = puntosFidelidadAcumulados;
    }

    public double getPuntosFidelidad() {
        return puntosFidelidadAcumulados;
    }

    public Mesa getMesa() {
        return null;
    }

    public List<Prestamo> getPrestamos() {
        return null;
    }

    public List<Venta> getVentas() {
        return null;
    }

    public List<JuegoMesa> getJuegosFavoritos() {
        return null;
    }

    public void ocuparMesa(Mesa mesa, int numPersonas, boolean hayNinos, boolean hayMenores) {

    }

    public void liberarMesa() {

    }

    public Prestamo solicitarPrestamo(EjemplarJuego ejemplar) {
        return null;
    }

    public void devolverJuego(Prestamo prestamo) {

    }

    public VentaJuego comprarJuego(JuegoMesa juego, String codigoDescuento, double puntosUsados) {
        return null;
    }

    public VentaCafeteria comprarCafeteria(List<ItemMenu> items, double propina, double puntosUsados) {
        return null;
    }

    public void agregarFavorito(JuegoMesa juego) {

    }

    public void eliminarFavorito(JuegoMesa juego) {

    }

    private void acumularPuntos(double puntos) {
        this.puntosFidelidadAcumulados += puntos;
    }

    private boolean descontarPuntos(double puntos) {
        if (puntos <= puntosFidelidadAcumulados) {
            puntosFidelidadAcumulados -= puntos;
            return true;
        }
        return false;
    }
}