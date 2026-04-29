package Modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Cliente extends Persona implements ISolicitarPrestamo {

    private double puntosFidelidadAcumulados;
    private Mesa mesa;
    private List<Prestamo> prestamos = new ArrayList<>();
    private List<Venta> ventas = new ArrayList<>();
    private List<JuegoMesa> favoritos = new ArrayList<>();

    public Cliente(String nombre, String apellido, String correoElectronico, String contrasena, String login, double puntosFidelidadAcumulados) {
        super(nombre, apellido, correoElectronico, contrasena, login);
        this.puntosFidelidadAcumulados = puntosFidelidadAcumulados;
    }

    public double getPuntosFidelidad() {
        return puntosFidelidadAcumulados;
    }

    public Mesa getMesa() {
        return mesa;
    }

    public List<Prestamo> getPrestamos() {
        return prestamos;
    }

    public List<Venta> getVentas() {
        return ventas;
    }

    public List<JuegoMesa> getJuegosFavoritos() {
        return favoritos;
    }

    public void ocuparMesa(Mesa mesa, int numPersonas, boolean hayNinos, boolean hayMenores) {
        this.mesa = mesa;
    }

    public void liberarMesa() {
        this.mesa = null;
    }
    

   @Override
    public Prestamo solicitarPrestamo(InventarioPrestamo inventarioPrestamo,EjemplarJuego ejemplar, Mesa mesa) {
        Prestamo p = new Prestamo(inventarioPrestamo,ejemplar,mesa);
        prestamos.add(p);
        inventarioPrestamo.removerEjemplar(ejemplar);
        return p;
    }
    
    

    public void devolverJuego(Prestamo prestamo) {
    
        try {
        	InventarioPrestamo inventario = prestamo.getInventarioPrestamo();
        	inventario.agregarEjemplar(prestamo.getEjemplar());
        	prestamo.setFechaHoraFinal();
        	prestamo.setEstado("Devuelto");
        } catch (Exception e) {
			System.out.println("Error al devolver el juego: " + e.getMessage());
        }
        
    }

    public VentaJuego comprarJuego(JuegoMesa juego, String codigoDescuento, double puntosUsados) {
        VentaJuego venta = new VentaJuego(LocalDateTime.now(), 0, 0, this);
        venta.getJuegos().add(juego);
        ventas.add(venta);
        return venta;
    }

    public VentaCafeteria comprarCafeteria(List<ItemMenu> items, double propina, double puntosUsados) {
        VentaCafeteria venta = new VentaCafeteria(LocalDateTime.now(), 0, 0, this, mesa, propina);
        venta.getItems().addAll(items);
        ventas.add(venta);
        return venta;
    }

    public void agregarFavorito(JuegoMesa juego) {
        if (!favoritos.contains(juego)) {
            favoritos.add(juego);
        }
    }
    
    public Prestamo buscarPrestamoActivo(String nombreEjemplar) {
    	for (Prestamo prestamo : prestamos) {
			if (prestamo.estaActivo() && prestamo.getEjemplar().getJuegoMesa().getNombre().equals(nombreEjemplar)) {
				return prestamo;
			}
		}
		return null; 
    }

    public void eliminarFavorito(JuegoMesa juego) {
        favoritos.remove(juego);
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