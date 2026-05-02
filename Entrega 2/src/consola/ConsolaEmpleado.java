package consola;
import consola.ConsolaVentas;
import consola.ConsolaPrestamos;
import Modelo.*;
public class ConsolaEmpleado  extends ConsolaBasica{
	
	public int mostrarMenuMesero()
	{
		String[] opciones = {"Consultar turnos semanales", "Solicitar cambio de turno", "Comprar Juego", "Pedir juego prestado","Salir"};
		int opcion= super.mostrarMenu("MENU EMPLEADO", opciones);
		return opcion;
	}

	public void iniciarMesero(InventarioVenta inventariVenta, InventarioPrestamo inventariPrestamo, Mesa mesa, Mesero mesero) {
		int opcion;
		do {
			opcion = mostrarMenuMesero();
			switch (opcion) {
				case 1:
					System.out.println("Consultar turnos semanales");
					break;
				case 2:
					System.out.println("Solicitar cambio de turno");
					break;
				case 3:
					System.out.println("Comprar Juego/Item cafetería");
					ConsolaVentas consolaVentas = new ConsolaVentas();
					consolaVentas.iniciar();
					break;
				case 4:
					System.out.println("Pedir juego prestado");
					ConsolaPrestamos consolaPrestamos = new ConsolaPrestamos();
					consolaPrestamos.iniciar(inventariPrestamo,mesa,mesero);
					break;
				case 5:
					System.out.println("Salir");
					break;
				default:
					System.out.println("Opción no válida. Por favor, intente de nuevo.");
			}
		} while (opcion != 5);
	}
	

}
