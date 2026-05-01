package consola;

import java.util.List;
import Modelo.*;

public class ConsolaCliente extends ConsolaBasica {
	
	private int mostrarMenuCliente() {
		String[] opciones = {"Ocupar Mesa","Pedir/Devoler Juego Prestado","Comprar Juego","Comprar item cafeteria","Gestionar Juegos favoritos","Desocupar Mesa","Salir"};
		int opcionElegida= super.mostrarMenu("--------MENU DE CLIENTE--------", opciones);
		return opcionElegida;
	}
	public void iniciar(InventarioVenta inventarioVenta, InventarioPrestamo inventarioPrestamo, List<JuegoMesa> juegosDisponibles, Cliente cliente, List<Mesa> mesas) {
		int opcionElegida;
		do {
			opcionElegida = mostrarMenuCliente();
			switch (opcionElegida) {
				case 1:
					 OcuparMesa(cliente, mesas);
					break;
				case 2:
					try {
						ConsolaPrestamos consolaPrestamos = new ConsolaPrestamos();
						consolaPrestamos.mostrarMenuPrestamos(inventarioPrestamo, cliente.getMesa(), cliente);
					} catch (Exception e) {
						System.out.println("Error al solicitar préstamo: " + e.getMessage());
					}
					break;
				case 3:
					System.out.println("Funcionalidad de comprar juego aún no implementada.");
					break;
				case 4:
					System.out.println("Funcionalidad de comprar item cafetería aún no implementada.");
					break;
				case 5:
					ConsolaJuegosFavoritos consolaJuegosFavoritos = new ConsolaJuegosFavoritos();
					consolaJuegosFavoritos.mostrarMenuJuegosFavoritos(cliente, juegosDisponibles);
					break;
				case 6:
					DesocuparMesa(cliente);
					break;
				case 7:
					System.out.println("Saliendo del menú de cliente. ¡Hasta luego!");
					break;
				default:
					System.out.println("Opción no válida. Por favor, elija una opción del menú.");
			}
		} while (opcionElegida != 7);
	}
	
	
	public void DesocuparMesa(Cliente cliente) {
		cliente.liberarMesa();
	}
	
	public void OcuparMesa(Cliente cliente, List<Mesa> listaMesas) {
		
		int numeroPersonas = super.pedirEnteroAlUsuario("Ingrese el numero de personas que ocuparan la mesa: ");
		
		for(Mesa mesa:listaMesas) {
			if(!mesa.estaOcupada()) {
				 if(mesa.getMaximaDePersonas() >= numeroPersonas) {
					 cliente.ocuparMesa(mesa, numeroPersonas, super.pedirBooleanoAlUsuario("¿Hay niños? (true/false): "), super.pedirBooleanoAlUsuario("¿Hay menores de edad? (true/false): "));
					 System.out.println("Mesa " + mesa.getNumeroMesa() + " ocupada exitosamente.");
					 break;
				 } else {
					 System.out.println("La mesa " + mesa.getNumeroMesa() + " no tiene capacidad suficiente para " + numeroPersonas + " personas.");
				 }
				
			} else {
				continue;
			}
		}
			
	}

}
