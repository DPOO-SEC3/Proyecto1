package consola;

import java.util.List;
import Modelo.*;

public class ConsolaCliente extends ConsolaBasica {
	
	private int mostrarMenuCliente() {
		String[] opciones = {"Ocupar Mesa","Pedir Juego Prestado","Devolver Juego","Comprar Juego","Comprar item cafeteria","Gestionar Juegos favoritos","Salir"};
		int opcionElegida= super.mostrarMenu("--------MENU DE CLIENTE--------", opciones);
		return opcionElegida;
	}
	public void iniciar(InventarioVenta inventarioVenta, InventarioPrestamo inventarioPrestamo, List<JuegoMesa> juegosDisponibles) {
		int opcionElegida;
		do {
			opcionElegida = mostrarMenuCliente();
			switch (opcionElegida) {
				case 1:
					System.out.println("Funcionalidad de ocupar mesa aún no implementada.");
					break;
				case 2:
					System.out.println("Funcionalidad de pedir juego prestado aún no implementada.");
					break;
				case 3:
					System.out.println("Funcionalidad de devolver juego aún no implementada.");
					break;
				case 4:
					System.out.println("Funcionalidad de comprar juego aún no implementada.");
					break;
				case 5:
					System.out.println("Funcionalidad de comprar item cafetería aún no implementada.");
					break;
				case 6:
					System.out.println("Funcionalidad de gestionar juegos favoritos aún no implementada.");
					break;
				case 7:
					System.out.println("Saliendo del menú de cliente. ¡Hasta luego!");
					break;
				default:
					System.out.println("Opción no válida. Por favor, elija una opción del menú.");
			}
		} while (opcionElegida != 8);
	}

}
