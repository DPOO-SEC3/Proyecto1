package Modelo;
import java.util.List;

public class InventarioPrestamo {
	
	private int CapacidadMaxima;
	private List<EjemplarJuego> ejemplares;
	
	public InventarioPrestamo(int capacidadMaxima) {
		this.CapacidadMaxima = capacidadMaxima;
	}
	public int getCapacidadMaxima() {
		return CapacidadMaxima;
	}
	public List<EjemplarJuego> getEjemplares() {
		return ejemplares;
	}
	protected void agregarEjemplar(EjemplarJuego ejemplar) {
		if (ejemplares.size() < CapacidadMaxima) {
			ejemplares.add(ejemplar);
		} else {
			System.out.println("No se puede agregar el juego. Capacidad máxima alcanzada.");
		}
	}
	protected void eliminarEjemplar(EjemplarJuego ejemplar) {
		ejemplares.remove(ejemplar);
	}
	

}
