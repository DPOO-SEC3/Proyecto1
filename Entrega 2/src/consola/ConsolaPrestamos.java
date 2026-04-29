package consola;

import Modelo.*;
public class ConsolaPrestamos extends ConsolaBasica {
	
	
	public void iniciar(InventarioPrestamo inventarioPrestamo, Mesa mesa, Persona usuario) {
		System.out.println("Bienvenido a la gestión de préstamos. Aquí podrás ver todo lo relacionado a el proceso de préstamo de juegos de mesa.");
		mostrarMenuPrestamos(inventarioPrestamo, mesa, usuario);
	}
	
	public void mostrarMenuPrestamos(InventarioPrestamo inventarioPrestamo, Mesa mesa, Persona usuario) {
		int opcionElegida;
		do {
			String[] opciones = {"Solicitar nuevo préstamo", "Devolver juego", "Ver catalogo prestamos", "Volver al menú principal"};
			opcionElegida = super.mostrarMenu("GESTIÓN DE PRÉSTAMOS", opciones);
			switch (opcionElegida) {
				case 1:
					System.out.println("Funcionalidad de registrar nuevo préstamo aún no implementada.");
					break;
				case 2:
					System.out.println("Funcionalidad de registrar devolución de juego aún no implementada.");
					break;
				case 3:
					super.mostrarCatalogoInventarioPrestamo(inventarioPrestamo);
					break;
				case 4:
					System.out.println("Volviendo al menú principal...");
					break;
				default:
					System.out.println("Opción no válida. Por favor, elija una opción del menú.");
			}
		} while (opcionElegida != 4);
		
	}
	
	public<T extends Persona & ISolicitarPrestamo> void solicitarPrestamo(InventarioPrestamo inventarioPrestamo, Mesa mesa, T usuario) {
		System.out.println("Solicitud de un prestamo");
		try {
			String nombreEjemplar= super.pedirCadenaAlUsuario("Ingrese el nombre del juego que desea solicitar en préstamo:");
			EjemplarJuego ejemplar = inventarioPrestamo.buscarEjemplarDisponible(nombreEjemplar);
			Prestamo prestamo = usuario.solicitarPrestamo(inventarioPrestamo, ejemplar,mesa);
			mesa.registrarPrestamo(prestamo);
		} catch (Exception e) {
			System.out.println("No se encontró un ejemplar disponible para el juego solicitado. Por favor, intente con otro juego o revise el inventario de préstamos.");
			return;
		}
	}
	
	public<T extends Persona & ISolicitarPrestamo> void devolverJuego(T usuario) {
		System.out.println("Devolución de un juego prestado");
		try {
			String nombreEjemplar= super.pedirCadenaAlUsuario("Ingrese el nombre del juego que desea devolver:");
			Prestamo prestamo = usuario.buscarPrestamoActivo(nombreEjemplar);
			usuario.devolverJuego(prestamo);
		} catch (Exception e) {
			System.out.println("No se encontró un ejemplar prestado con el nombre ingresado. Por favor, revise el nombre del juego o consulte su historial de préstamos.");
			return;
		}
	}
	
}
