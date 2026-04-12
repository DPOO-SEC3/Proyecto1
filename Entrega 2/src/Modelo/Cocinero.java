package Modelo;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class Cocinero extends Empleado {

    private TurnoSemanal turno;
    private List<SolicitudCambioTurno> solicitudes;
    private List<SugerenciaPlatillo> sugerencias;
    private List<Prestamo> prestamos;
    private List<Venta> ventas;
    private List<JuegoMesa> favoritos;

    public Cocinero(String nombre, String apellido, String correoElectronico, String contrasena, String login) {
        super(nombre, apellido, correoElectronico, contrasena, login);
        this.solicitudes = new ArrayList<>();
        this.sugerencias = new ArrayList<>();
        this.prestamos = new ArrayList<>();
        this.ventas = new ArrayList<>();
        this.favoritos = new ArrayList<>();
    }

    @Override
    public TurnoSemanal getTurnoSemanal() {
        return turno;
    }

    @Override
    public boolean estaEnTurno() {
        return turno != null && turno.estaActivo();
    }

    @Override
    public List<SolicitudCambioTurno> getSolicitudesCambio() {
        return solicitudes;
    }

    @Override
    public List<SugerenciaPlatillo> getSugerencias() {
        return sugerencias;
    }

    @Override
    public List<Prestamo> getPrestamos() {
        return prestamos;
    }

    @Override
    public List<Venta> getVentas() {
        return ventas;
    }

    @Override
    public List<JuegoMesa> getJuegosFavoritos() {
        return favoritos;
    }

    @Override
    public SolicitudCambioTurno solicitarCambioTurno(String tipo, String motivo, Empleado otro) {
        SolicitudCambioTurno solicitud = new SolicitudCambioTurno(tipo, motivo, this, otro);
        solicitudes.add(solicitud);
        return solicitud;
    }

    @Override
    public SugerenciaPlatillo sugerirPlatillo(String descripcion) {
    	SugerenciaPlatillo s = new SugerenciaPlatillo(descripcion, this);
        sugerencias.add(s);
        return s;
    }

    @Override
    public Prestamo solicitarPrestamo(EjemplarJuego ejemplar) {
        Prestamo p = new Prestamo(LocalDateTime.now(), null, "activo");
        prestamos.add(p);
        return p;
    }

    @Override
    public void devolverJuego(Prestamo prestamo) {
        prestamos.remove(prestamo);
    }

    @Override
    public VentaJuego comprarJuego(JuegoMesa juego, double descuento, double puntosUsados) {
        VentaJuego v = new VentaJuego(LocalDateTime.now(), descuento, 0, this);
        v.getJuegos().add(juego);
        ventas.add(v);
        return v;
    }

    @Override
    public VentaCafeteria comprarCafeteria(List<ItemMenu> items, double propina, double puntosUsados) {
        VentaCafeteria v = new VentaCafeteria(LocalDateTime.now(), 0, 0, this, null, propina);
        v.getItems().addAll(items);
        ventas.add(v);
        return v;
    }

    @Override
    public void agregarFavorito(JuegoMesa juego) {
        favoritos.add(juego);
    }

    @Override
    public void eliminarFavorito(JuegoMesa juego) {
        favoritos.remove(juego);
    }
}