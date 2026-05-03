package consola;
import consola.ConsolaVentas;
import consola.ConsolaPrestamos;
import consola.ConsolaTorneos;

import java.util.List;

import Modelo.*;
public class ConsolaEmpleado  extends ConsolaBasica{
	
	public int mostrarMenuEmpleado()
	{
		String[] opciones = {"Consultar turnos semanales", "Solicitar cambio de turno", "Comprar Juego", "Pedir juego prestado", "Sugerencia platillos","Torneos","Salir"};
		int opcion= super.mostrarMenu("MENU EMPLEADO", opciones);
		return opcion;
	}

	public< T extends Empleado> void iniciarEmpleado(InventarioVenta inventariVenta, InventarioPrestamo inventariPrestamo, Mesa mesa, T user, List<Torneo> torneosDisponibles) {
		int opcion;
		do {
			opcion = mostrarMenuEmpleado();
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
					consolaPrestamos.iniciar(inventariPrestamo,mesa,user);
					break;
				case 5:
					System.out.println("Falta implementar consola para Sugerencia platillos");
					 break;
				case 6:
					ConsolaTorneos consolaTorneos = new ConsolaTorneos();
					consolaTorneos.iniciar(torneosDisponibles, user);
				case 7:
					System.out.println("Salir");
					break;
				default:
					System.out.println("Opción no válida. Por favor, intente de nuevo.");
			}
		} while (opcion != 7);
	}

}
