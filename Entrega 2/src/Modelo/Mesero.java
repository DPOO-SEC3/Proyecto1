package Modelo;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class Mesero extends Empleado {

    private List<JuegoMesa> juegosConocidos;
    private List<Mesa> mesasAtendidas;
    private TurnoSemanal turno;
    private List<SolicitudCambioTurno> solicitudes;
    private List<SugerenciaPlatillo> sugerencias;
    private List<Prestamo> prestamos;
    private List<Venta> ventas;
    private List<JuegoMesa> favoritos;

    public Mesero(String nombre, String apellido, String correoElectronico, String contrasena, String login) {
        super(nombre, apellido, correoElectronico, contrasena, login);
        this.juegosConocidos = new ArrayList<>();
        this.mesasAtendidas = new ArrayList<>();
        this.solicitudes = new ArrayList<>();
        this.sugerencias = new ArrayList<>();
        this.prestamos = new ArrayList<>();
        this.ventas = new ArrayList<>();
        this.favoritos = new ArrayList<>();
    }

    public List<JuegoMesa> getJuegosConocidos() {
        return juegosConocidos;
    }

    public boolean conoceJuego(JuegoMesa juego) {
        return juegosConocidos.contains(juego);
    }

    public List<Mesa> getMesasAtendidas() {
        return mesasAtendidas;
    }

    public void atenderMesa(Mesa mesa) {
        mesasAtendidas.add(mesa);
    }

    public void desatenderMesa(Mesa mesa) {
        mesasAtendidas.remove(mesa);
    }

    public void agregarJuegoConocido(JuegoMesa juego) {
        juegosConocidos.add(juego);
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
    public Prestamo solicitarPrestamo(InventarioPrestamo inventarioPrestamo,EjemplarJuego ejemplar,Mesa mesa) {
        Prestamo p = new Prestamo(inventarioPrestamo,ejemplar,mesa);
        prestamos.add(p);
        return p;
    }

    @Override
    public void devolverJuego(Prestamo prestamo) {
        prestamos.remove(prestamo);
    }

    @Override 
    public VentaJuego comprarJuegos(List<JuegoMesa> juegos, String codigoDescuento, InventarioVenta inventarioVenta) {
         VentaJuego venta = new VentaJuego(juegos,LocalDateTime.now(),codigoDescuento, this,inventarioVenta);
         ventas.add(venta);
         for(JuegoMesa juego : juegos) {
 			 inventarioVenta.removerJuego(juego);
 		 }
         return venta;
     }

     @Override
     public VentaCafeteria comprarCafeteria(List<ItemMenu> items,String codigoDescuento, double propina) {
         VentaCafeteria venta = new VentaCafeteria(LocalDateTime.now(),codigoDescuento, this, propina);
         venta.getItems().addAll(items);
         ventas.add(venta);
         return venta;
     }

    @Override
    public void agregarFavorito(JuegoMesa juego) {
        favoritos.add(juego);
    }

    @Override
    public void eliminarFavorito(JuegoMesa juego) {
        favoritos.remove(juego);
    }
    @Override 
    public Prestamo buscarPrestamoActivo(String nombreEjemplar) {
    	for (Prestamo prestamo : prestamos) {
			if (prestamo.estaActivo() && prestamo.getEjemplar().getJuegoMesa().getNombre().equals(nombreEjemplar)) {
				return prestamo;
			}
		}
		return null; 
    }
}