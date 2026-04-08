package Modelo;
import java.util.List;

public class InventarioPrestamo {
	
	private int CapacidadMaxima;
	private List<JuegoMesa> juegos;
	
	public InventarioPrestamo(int capacidadMaxima) {
		this.CapacidadMaxima = capacidadMaxima;
	}
	public int getCapacidadMaxima() {
		return CapacidadMaxima;
	}
	public List<JuegoMesa> getJuegos() {
		return juegos;
	}
	private void agregarJuego(JuegoMesa juego) {
		if (juegos.size() < CapacidadMaxima) {
			juegos.add(juego);
		} else {
			System.out.println("No se puede agregar el juego. Capacidad máxima alcanzada.");
		}
	}
	private void eliminarJuego(JuegoMesa juego) {
		juegos.remove(juego);
	}
	

}
