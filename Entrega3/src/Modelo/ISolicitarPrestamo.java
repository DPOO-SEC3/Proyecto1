package Modelo;

public interface ISolicitarPrestamo {
	
	Prestamo solicitarPrestamo(InventarioPrestamo inventarioPrestamo,EjemplarJuego ejemplar, Mesa mesa);
	
	void devolverJuego(Prestamo prestamo);
	
	Prestamo buscarPrestamoActivo(String nombreJuego);

}
