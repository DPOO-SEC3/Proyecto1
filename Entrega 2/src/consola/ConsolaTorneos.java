package consola;
import java.util.ArrayList;
import java.util.List;

import Modelo.*;

public class ConsolaTorneos extends ConsolaBasica{
	
	public int mostrarMenuTorneos()
 	{
 		String[] opciones = {"Ver torneos disponibles","Inscribirse a torneo", "Retirarse de torneo", "Salir"};
 		int opcion= super.mostrarMenu("MENU TORNEOS", opciones);
 		return opcion;
 	}
	
	public <T extends Persona & IInscripcionTorneo> void iniciar(List<Torneo> torneosDisponibles, T user) {
		
		 
	}
	
	public void mostrarTorneosDisponibles(List<Torneo> torneosDisponibles) {
	    System.out.println("Torneos disponibles:");
	    for (Torneo torneo : torneosDisponibles) {
	        System.out.println("- " + torneo.getNombre() + " (" + torneo.getJuego().getNombre() + ")");
	    }
	}
	
	public<T extends Persona & IInscripcionTorneo> void inscribirseTorneo(List<Torneo> torneosDisponibles, T user) {
	    	
		String nombreTorneo =super.pedirCadenaAlUsuario("Ingrese el nombre del torneo al que desea inscribirse: ");
		int cantidadInscritos =super.pedirEnteroAlUsuario("Ingrese el numero de participantes que desea inscriir (Sin contarse a usted mismo):");
		List<Persona> participantes = new ArrayList<>();
		for (int i = 0; i < cantidadInscritos; i++) {
			String nombre = super.pedirCadenaAlUsuario("Ingrese su nombre:");
	    	String apellido = super.pedirCadenaAlUsuario("Ingrese su apellido:");
	    	String email = super.pedirCadenaAlUsuario("Ingrese su correo electrónico:");
	    	String contrasena = super.pedirCadenaAlUsuario("Ingrese su contraseña:");
	    	String login = super.pedirCadenaAlUsuario("Ingrese su nombre de usuario:");
	    	participantes.add(new Cliente(nombre, apellido, email, contrasena, login,0));
	    	System.out.println("Usuario registrado exitosamente. continue con el siguiente.");
		} 
		participantes.add(user);
		user.inscribirTorneo(torneosDisponibles, nombreTorneo,participantes);
	}
	
	public<T extends Persona & IInscripcionTorneo> void retirarseTorneo(T user) {
		String nombreTorneo =super.pedirCadenaAlUsuario("Ingrese el nombre del torneo del que desea retirarse: ");
		int cantidadInscritos =super.pedirEnteroAlUsuario("Ingrese el numero de participantes que desea retirar (Sin contarse a usted mismo):");
		System.out.println("Ingrese los datos de los participantes que desea retirar ( deben ser los mismos con los que los ingresó):");
		List<Persona> participantes = new ArrayList<>();
		for (int i = 0; i < cantidadInscritos; i++) {
			String nombre = super.pedirCadenaAlUsuario("Ingrese su nombre:");
	    	String apellido = super.pedirCadenaAlUsuario("Ingrese su apellido:");
	    	String email = super.pedirCadenaAlUsuario("Ingrese su correo electrónico:");
	    	String contrasena = super.pedirCadenaAlUsuario("Ingrese su contraseña:");
	    	String login = super.pedirCadenaAlUsuario("Ingrese su nombre de usuario:");
	    	participantes.add(new Cliente(nombre, apellido, email, contrasena, login,0));
	    	System.out.println("Usuario registrado exitosamente. continue con el siguiente.");
		} 
		participantes.add(user);
		
		user.retirarTorneo(nombreTorneo, participantes);
	}
	
	public int mostrarMenuAdminTorneos() {
		String[] opciones = {"Crear torneo", "Eliminar torneo", "Volver al menú principal"};
		int opcion = super.mostrarMenu("MENU ADMINISTRADOR - TORNEOS", opciones);
		return opcion;
	}
	public void iniciarAdmin(List<Torneo> torneosDisponibles, Administrador admin, List<JuegoMesa> juegosDisponibles) {
		int opcionElegida;
		do {
			opcionElegida = mostrarMenuAdminTorneos();
			switch (opcionElegida) {
				case 1:
					System.out.println("Funcionalidad de crear torneo aún no implementada.");
					break;
				case 2:
					System.out.println("Funcionalidad de eliminar torneo aún no implementada.");
					break;
				case 3:
					System.out.println("Volviendo al menú principal...");
					break;
				default:
					System.out.println("Opción no válida. Por favor, elija una opción del menú.");
			}
		} while (opcionElegida != 3);
	}
	
	public void crearTorneo(List<Torneo> torneosDisponibles, List<JuegoMesa> juegosDisponibles) {
		String nombreTorneo = super.pedirCadenaAlUsuario("Ingrese el nombre del torneo:");
		String nombreJuego = super.pedirCadenaAlUsuario("Ingrese el nombre del juego para el torneo:");
		int cantidadParticipantes = super.pedirEnteroAlUsuario("Ingrese la cantidad de participantes para el torneo:");
		String diaSemana= super.pedirCadenaAlUsuario("Ingrese el día de la semana para el torneo:");
		String tipoTorneo = super.pedirCadenaAlUsuario("Ingrese el tipo de torneo (amistoso, competitivo):");
		JuegoMesa juegoSeleccionado = null;
		for (JuegoMesa juego : juegosDisponibles) {
			if (juego.getNombre().equalsIgnoreCase(nombreJuego)) {
				juegoSeleccionado = juego;
				break;
			}
		}
		if (juegoSeleccionado == null) {
			System.out.println("Juego no encontrado. No se pudo crear el torneo.");
			return;
		}
		Torneo nuevoTorneo = new Torneo(nombreTorneo, cantidadParticipantes,juegoSeleccionado, diaSemana, tipoTorneo);
		torneosDisponibles.add(nuevoTorneo);
		System.out.println("Torneo '" + nombreTorneo + "' creado exitosamente con el juego '" + nombreJuego + "'.");
		
	}
	
	public void eliminarTorneo(List<Torneo> torneosDisponibles) {
		String nombreTorneo = super.pedirCadenaAlUsuario("Ingrese el nombre del torneo que desea eliminar:");
		Torneo torneoAEliminar = null;
		for (Torneo torneo : torneosDisponibles) {
			if (torneo.getNombre().equalsIgnoreCase(nombreTorneo)) {
				torneoAEliminar = torneo;
				break;
			}
		}
		if (torneoAEliminar != null) {
			torneosDisponibles.remove(torneoAEliminar);
			System.out.println("Torneo '" + nombreTorneo + "' eliminado exitosamente.");
		} else {
			System.out.println("Torneo no encontrado. No se pudo eliminar.");
		}
	}
	
	
	

}
