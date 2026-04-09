package Modelo;
import java.util.List;

public class InventarioVenta {
	private int CapacidadMaxima;
	private List<JuegoMesa> juegos;
	public InventarioVenta(int capacidadMaxima) {
		this.CapacidadMaxima = capacidadMaxima;
	}
	public int getCapacidadMaxima() {
		return CapacidadMaxima;
	}
	public List<JuegoMesa> getJuegos() {
		return juegos;
	}
	protected void agregarJuego(JuegoMesa juego) {
		if (juegos.size() < CapacidadMaxima) {
			juegos.add(juego);
		} else {
			System.out.println("No se puede agregar el juego. Capacidad máxima alcanzada.");
		}
	}
	protected void eliminarJuego(JuegoMesa juego) {
		juegos.remove(juego);
	}

}
