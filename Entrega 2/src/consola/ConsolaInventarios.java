package consola;
import java.util.List;

import Modelo.*;
public class ConsolaInventarios extends ConsolaBasica {
	
	public void iniciar(InventarioVenta inventarioVenta, InventarioPrestamo inventarioPrestamo, List<JuegoMesa> juegosDisponibles) {
		System.out.println("Bienvenido a la gestión de inventarios. Aquí podrás administrar el inventario de venta y préstamo de juegos de mesa.");
		mostrarMenuInventarios(inventarioVenta, inventarioPrestamo, juegosDisponibles);
	}
	
	public void mostrarMenuInventarios(InventarioVenta inventarioVenta, InventarioPrestamo inventarioPrestamo, List<JuegoMesa> juegosDisponibles) {
		int opcionElegida;
		do {
			String[] opciones = {"Gestionar Inventario de Venta", "Gestionar Inventario de Préstamo", "Volver al menú principal"};
			opcionElegida = super.mostrarMenu("GESTIÓN DE INVENTARIOS", opciones);
			switch (opcionElegida) {
				case 1:
					gestionarInventarioVenta(inventarioVenta);
					break;
				case 2:
					gestionarInventarioPrestamo(inventarioPrestamo, juegosDisponibles);
					break;
				case 3:
					System.out.println("Volviendo al menú principal...");
					break;
				default:
					System.out.println("Opción no válida. Por favor, elija una opción del menú.");
			}
		} while (opcionElegida != 3);
	}
	
	public void gestionarInventarioVenta(InventarioVenta inventarioVenta) {
		int opcionElegida;
		do {
			String[] opciones = {"Agregar juego al inventario de venta", "Eliminar juego del inventario de venta", "Volver al menú anterior"};
			opcionElegida = super.mostrarMenu("GESTIÓN DE INVENTARIO DE VENTA", opciones);
			switch (opcionElegida) {
				case 1:
					agregarJuegoVenta(inventarioVenta);
					break;
				case 2:
					eliminarJuegoVenta(inventarioVenta);
					break;
				case 3:
					System.out.println("Volviendo al menú anterior...");
					break;
				default:
					System.out.println("Opción no válida. Por favor, elija una opción del menú.");
			}
		} while (opcionElegida != 3);
	}
	public void gestionarInventarioPrestamo(InventarioPrestamo inventarioPrestamo, List<JuegoMesa> juegosDisponibles) {
		 int opcionElegida;
		do {
			String[] opciones = {"Agregar ejemplar al inventario de préstamo", "Eliminar ejemplar del inventario de préstamo", "Marcar ejemplar como desaparecido", "Volver al menú anterior"};
			opcionElegida = super.mostrarMenu("GESTIÓN DE INVENTARIO DE PRÉSTAMO", opciones);
			switch (opcionElegida) {
				case 1:
					String nombreJuego = super.pedirCadenaAlUsuario("Ingrese el nombre del juego para el cual desea agregar ejemplares al inventario de préstamo:");
					JuegoMesa juegoSeleccionado = null;
					for (JuegoMesa juego : juegosDisponibles) {
						if (juego.getNombre().equalsIgnoreCase(nombreJuego)) {
							juegoSeleccionado = juego;
							mostrarDetallesJuego(juegoSeleccionado);
							mostrarEjemplaresJuego(juegoSeleccionado);
							break;
						}
					}
					if (juegoSeleccionado != null) {
						agregarEjemplarPrestamo(inventarioPrestamo, juegoSeleccionado);
					} else {
						System.out.println("No se seleccionó ningún juego. Volviendo al menú anterior...");
					}
					break;
				case 2:
					eliminarJuegoPrestamo(inventarioPrestamo);
					break;
				case 3:
					marcarEjemplarDesaparecidoPorID(inventarioPrestamo);
					break;
				case 4:
					System.out.println("Volviendo al menú anterior...");
					break;
				default:
					System.out.println("Opción no válida. Por favor, elija una opción del menú.");
			}
		} while (opcionElegida != 4);
	}
	
	public void eliminarJuegoVenta(InventarioVenta inventarioVenta) {
		try {
			String nombreJuego = pedirCadenaAlUsuario("Ingrese el nombre del juego que desea eliminar del inventario de venta:");
			JuegoMesa juego =inventarioVenta.buscarJuego(nombreJuego);
			if (super.pedirConfirmacionAlUsuario("¿Está seguro de que desea eliminar el juego: " + juego.getNombre() + "?")) {
				inventarioVenta.removerJuego(juego);
				System.out.println("Juego eliminado exitosamente: " + juego.getNombre());
			} else {
				System.out.println("Eliminación cancelada para el juego: " + juego.getNombre());
			}
		} catch (Exception e) {
			System.out.println("Error al buscar el juego en el inventario de venta: " + e.getMessage());
			return;
		}
	}
	public void eliminarJuegoPrestamo(InventarioPrestamo inventarioPrestamo) {
		try {
			String idJuego = super.pedirCadenaAlUsuario("Ingrese el ID del ejemplar del juego que desea eliminar del inventario de préstamo:");
			EjemplarJuego ejemplar= inventarioPrestamo.buscarEjemplarPorID(idJuego);
			if (super.pedirConfirmacionAlUsuario("¿Está seguro de que desea eliminar el ejemplar del juego: " + ejemplar.getJuegoMesa().getNombre()+ "?")) {
				inventarioPrestamo.removerEjemplar(ejemplar);
				System.out.println("Juego eliminado exitosamente: " + ejemplar.getJuegoMesa().getNombre());
			} else {
				System.out.println("Eliminación cancelada para el juego: " + ejemplar.getJuegoMesa().getNombre());
			}	
		} catch (Exception e) {
			System.out.println("Error al buscar el ejemplar del juego en el inventario de préstamo: " + e.getMessage());
			return;
			}
	}
	public void agregarJuegoVenta(InventarioVenta inventarioVenta) {
		JuegoMesa juego = crearJuego();
		try {
			inventarioVenta.agregarJuego(juego);
			System.out.println("Juego agregado exitosamente al inventario de venta: " + juego.getNombre());
		} catch (Exception e) {
			System.out.println("Error al agregar el juego al inventario de venta: " + e.getMessage());
		}
	}
	public void agregarEjemplarPrestamo(InventarioPrestamo inventarioPrestamo, JuegoMesa juego) {
		int cantidad = super.pedirEnteroAlUsuario("Ingrese la cantidad de ejemplares a añadir al inventario de préstamo para el juego: " + juego.getNombre());
		for (int i = 0; i < cantidad; i++) {
			EjemplarJuego nuevoEjemplar = new EjemplarJuego("Nuevo",juego);
			try {
				inventarioPrestamo.agregarEjemplar(nuevoEjemplar);
				juego.agregarEjemplar(nuevoEjemplar);
				System.out.println("Ejemplar nuevo añadido exitosamente al inventario de préstamo para el juego: " + juego.getNombre());
			} catch (Exception e) {
				System.out.println("Error al agregar el ejemplar al inventario de préstamo: " + e.getMessage());
				break;
			}
		}
	}
	public JuegoMesa crearJuego() {
		 String nombre = super.pedirCadenaAlUsuario("Ingrese el nombre del juego:");
		 int anioPublicacion = super.pedirEnteroAlUsuario("Ingrese el año de publicación del juego:");
		 String empresaFabricante = super.pedirCadenaAlUsuario("Ingrese la empresa fabricante del juego:");
		 String categoria = super.pedirCadenaAlUsuario("Ingrese la categoría del juego:");
		 int minimoJugadores = super.pedirEnteroAlUsuario("Ingrese el número mínimo de jugadores:");
		 int maximoJugadores = super.pedirEnteroAlUsuario("Ingrese el número máximo de jugadores:");
		 String restriccionEdad = super.pedirCadenaAlUsuario("Ingrese la restricción de edad para el juego:");
		 boolean esDificil = super.pedirBooleanoAlUsuario("¿El juego es difícil? (true/false):");
		 double precioDeVenta = super.pedirDoubleAlUsuario("Ingrese el precio de venta del juego:");
		 JuegoMesa nuevoJuego = new JuegoMesa(nombre, anioPublicacion, empresaFabricante, categoria, minimoJugadores, maximoJugadores, restriccionEdad, esDificil, precioDeVenta);
		 System.out.println("Juego creado exitosamente: " + nombre);
		 return nuevoJuego;
	}
	
	public void mostrarDetallesJuego(JuegoMesa juego) {
		System.out.println("Detalles del juego:");
		System.out.println("Nombre: " + juego.getNombre());
		System.out.println("Año de publicación: " + juego.getAnioPublicacion());
		System.out.println("Empresa fabricante: " + juego.getEmpresaFabricante());
		System.out.println("Categoría: " + juego.getCategoria());
		System.out.println("Número mínimo de jugadores: " + juego.getMinimoJugadores());
		System.out.println("Número máximo de jugadores: " + juego.getMaximoJugadores());
		System.out.println("Restricción de edad: " + juego.getRestriccionEdad());
		System.out.println("¿Es difícil? " + (juego.esDificil() ? "Sí" : "No"));
		System.out.println("Precio de venta: $" + juego.getPrecioDeVenta());
	}
	
	public void marcarEjemplarDesaparecidoPorID(InventarioPrestamo inventarioPrestamo) {
		String idEjemplar = super.pedirCadenaAlUsuario("Ingrese el ID del ejemplar del juego que desea marcar como desaparecido:");
		try {
			EjemplarJuego ejemplar = inventarioPrestamo.buscarEjemplarPorID(idEjemplar);
			if (super.pedirConfirmacionAlUsuario("¿Está seguro de que desea marcar el ejemplar del juego: " + ejemplar.getJuegoMesa().getNombre() + " como desaparecido?")) {
				ejemplar.marcarDesaparecido();
				System.out.println("Ejemplar marcado como desaparecido exitosamente: " + ejemplar.getJuegoMesa().getNombre());
			} else {
				System.out.println("Marcado como desaparecido cancelado para el ejemplar del juego: " + ejemplar.getJuegoMesa().getNombre());
			}
		} catch (Exception e) {
			System.out.println("Error al buscar el ejemplar del juego en el inventario de préstamo: " + e.getMessage());
			return;
		}
	}
		
	public void mostrarEjemplaresJuego(JuegoMesa juego) {
		System.out.println("Ejemplares del juego: " + juego.getNombre());
		for (EjemplarJuego ejemplar : juego.getEjemplares()) {
			System.out.println("- Estado: " + ejemplar.getEstado() + ", Disponible: " + (ejemplar.isDisponible() ? "Sí" : "No") + ", Veces prestado: " + ejemplar.getNumeroDeVecesPrestado() + ", Desaparecido: " + (ejemplar.isDesaparecido() ? "Sí" : "No"));
		}
	}
	
	
	


	

}
