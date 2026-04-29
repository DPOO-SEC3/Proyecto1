package consola;
import Modelo.*;
import Persistencia.PersistenciaJuegos;
import Persistencia.PersistenciaTurnos;
import Persistencia.PersistenciaUsuarios;
import excepciones.CapacidadMaximaSuperadaException;
import excepciones.PersistenciaException;
 
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import consola.ConsolaInventarios;
public class ConsolaAdministrador extends ConsolaBasica {
	
	private int mostrarMenuAdministrador() {
		String[] opciones = {"Gestionar Inventarios", "Gestionar Sugerencias", "Gestionar Solicitudes de Cambio de Turno", "Consultar Ventas", "Consultar Historial de Préstamos", "Gestionar Turnos Semanales", "Gestionar Items del Menú","Salir"};
		int opcionElegida= super.mostrarMenu("--------MENU DE ADMINISTRADOR--------", opciones);
		return opcionElegida;
	}
	private void gestionarInventarios(InventarioVenta inventarioVenta, InventarioPrestamo inventarioPrestamo, List<JuegoMesa> juegosDisponibles) {
		ConsolaInventarios consolaInventarios = new ConsolaInventarios();
		consolaInventarios.iniciar( inventarioVenta, inventarioPrestamo, juegosDisponibles);
	}
	public void iniciar(InventarioVenta inventarioVenta, InventarioPrestamo inventarioPrestamo, List<JuegoMesa> juegosDisponibles) {
		int opcionElegida;
		do {
			opcionElegida = mostrarMenuAdministrador();
			switch (opcionElegida) {
				case 1:
					gestionarInventarios(inventarioVenta, inventarioPrestamo,juegosDisponibles);
					break;
				case 2:
					System.out.println("Funcionalidad de gestión de sugerencias aún no implementada.");
					break;
				case 3:
					System.out.println("Funcionalidad de gestión de solicitudes de cambio de turno aún no implementada.");
					break;
				case 4:
					System.out.println("Funcionalidad de consulta de ventas aún no implementada.");
					break;
				case 5:
					System.out.println("Funcionalidad de consulta de historial de préstamos aún no implementada.");
					break;
				case 6:
					System.out.println("Funcionalidad de gestión de turnos semanales aún no implementada.");
					break;
				case 7:
					System.out.println("Funcionalidad de gestión de items del menú aún no implementada.");
					break;
				case 8:
					System.out.println("Saliendo del menú de administrador. ¡Hasta luego!");
					break;
				default:
					System.out.println("Opción no válida. Por favor, elija una opción del menú.");
			}
		} while (opcionElegida != 8);
	}
	
}
