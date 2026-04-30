package consola;
import java.util.ArrayList;
import java.util.List;

import Modelo.*;
public class ConsolaVentas extends ConsolaBasica{
	
	
	public int mostrarMenuVentas() {
		return super.mostrarMenu("VENTAS", new String[] {"Comprar Juego", "Comprar item cafeteria", "Volver al menu principal" });
	}
	
	public void iniciar() {
		System.out.println("Bienvenido a la gestión de ventas. Aquí podrás realizar compras de juegos de mesa y artículos de cafetería.");
		int opcionElegida;
		do {
			opcionElegida = mostrarMenuVentas();
			switch (opcionElegida) {
				case 1:
					System.out.println("Funcionalidad de comprar juego aún no implementada.");
					break;
				case 2:
					System.out.println("Funcionalidad de comprar item cafetería aún no implementada.");
					break;
				case 3:
					System.out.println("Volviendo al menú principal...");
					break;
				default:
					System.out.println("Opción no válida. Por favor, elija una opción del menú.");
			}
		} while (opcionElegida != 3);
	}
	
	public<T extends Persona & IComprar> void comprarJuego(InventarioVenta inventarioVenta, T usuario) {
		
		System.out.println("Compra de un juego");
		
		try {
			int cantidad = super.pedirEnteroAlUsuario("Ingrese la cantidad de unidades que desea comprar(máximo 4):");
			if(cantidad < 1 || cantidad > 4) {
				throw new IllegalArgumentException("La cantidad debe ser entre 1 y 4.");
			} else {
				String codigoDescuento = super.pedirCadenaAlUsuario("Ingrese el código de descuento (si tiene uno, de lo contrario deje en blanco):");
				List<JuegoMesa> juegosSeleccionados = new ArrayList<JuegoMesa>();
				for(int i=0; i<cantidad; i++) {
					
					String nombreJuego = super.pedirCadenaAlUsuario("Ingrese el nombre del juego que desea comprar:");
					JuegoMesa juego= inventarioVenta.buscarJuego(nombreJuego);
					juegosSeleccionados.add(juego);
				}	
				usuario.comprarJuegos(juegosSeleccionados,codigoDescuento,inventarioVenta);
			}
		} catch (Exception e) {
			System.out.println("Error al comprar el juego: " + e.getMessage());
			return;
		}
		
	}

}
