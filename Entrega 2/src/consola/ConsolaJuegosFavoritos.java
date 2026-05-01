package consola;
import java.util.List;

import Modelo.*;
public class ConsolaJuegosFavoritos extends ConsolaBasica{
	
	public<T extends Persona & IJuegosFavoritos> void mostrarMenuJuegosFavoritos(T usuario, List<JuegoMesa> juegosDisponibles) {
		String[] opciones = {"Agregar juego favorito", "Eliminar juego favorito", "Ver juegos favoritos", "Volver al menú principal"};
		int opcion= super.mostrarMenu("GESTION DE JUEGOS FAVORITOS",opciones);
		do {
			switch (opcion) {
				case 1:
					agregarJuegoFavorito(usuario, juegosDisponibles);
					break;
				case 2:
					eliminarJuegoFavorito(usuario);
					break;
				case 3:
					verJuegosFavoritos(usuario);
					break;
				case 4:
					System.out.println("Volviendo al menú principal...");
					break;
				default:
					System.out.println("Opción no válida. Por favor, elija una opción del menú.");
			}
		} while (opcion != 5);
		
	}
	
	public<T extends Persona & IJuegosFavoritos> void agregarJuegoFavorito(T usuario, List<JuegoMesa> juegosDisponibles) {
		
		System.out.println("Agregar juego favorito");
		String nombreJuego = super.pedirCadenaAlUsuario("Ingrese el nombre del juego que desea agregar a favoritos:");
		
		for(JuegoMesa juego: juegosDisponibles) {
			if(juego.getNombre().equalsIgnoreCase(nombreJuego)) {
				usuario.agregarFavorito(juego);
				System.out.println("Juego agregado a favoritos: " + juego.getNombre());
				return;
			}
		}
	
	}
	
	public <T extends Persona & IJuegosFavoritos> void eliminarJuegoFavorito(T usuario) {
		
		System.out.println("Eliminar juego favorito");
		String nombreJuego = super.pedirCadenaAlUsuario("Ingrese el nombre del juego que desea eliminar de favoritos:");
		
		for(JuegoMesa juego: usuario.getJuegosFavoritos()) {
			if(juego.getNombre().equalsIgnoreCase(nombreJuego)) {
				usuario.eliminarFavorito(juego);
				System.out.println("Juego eliminado de favoritos: " + juego.getNombre());
				return;
			}
		}
	
	}
	
	public <T extends Persona & IJuegosFavoritos> void verJuegosFavoritos(T usuario) {
		System.out.println("Juegos favoritos:");
		for(JuegoMesa juego: usuario.getJuegosFavoritos()) {
			System.out.println("- " + juego.getNombre());
		}
	}
	

}
