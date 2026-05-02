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
 
/**
 * Punto de entrada de la aplicación, por ahora solo inicializa todas las clases y persistencias en el orden correcto, junto con ejemplos para probar funcionalidades basicas, no hay consolas ni tests robustos implementados para esta entrega.
 */
public class ConsolaCafeteria extends ConsolaBasica {
 
    private static final String RUTA_USUARIOS = "datos/usuarios.json";
    private static final String RUTA_TURNOS   = "datos/turnos.json";
 
    // Estado de la aplicación
    private List<JuegoMesa>    todosLosJuegos;
    private InventarioPrestamo inventarioPrestamo;
    private InventarioVenta    inventarioVenta;
    private Administrador      administrador;
    private List<Persona>      usuarios;
    private List<TurnoSemanal> turnos;
    private List<Mesa> mesasDisponibles = new ArrayList<Mesa>();
 
    // Persistencias
    private PersistenciaJuegos   persistenciaJuegos;
    private PersistenciaUsuarios persistenciaUsuarios;
    private PersistenciaTurnos   persistenciaTurnos;
 
    public ConsolaCafeteria() {
        persistenciaJuegos   = new PersistenciaJuegos();
        persistenciaUsuarios = new PersistenciaUsuarios();
        persistenciaTurnos   = new PersistenciaTurnos();
    }
 
    public static void main(String[] args) {
        new ConsolaCafeteria().iniciar();
    }
    private void iniciar() {
    	System.out.println("=== DulcesnDados - Inicializando sistema ===\n");
		cargarJuegosEInventarios();
		cargarUsuarios();
		cargarTurnos();
		inicializarMesas(10);
		int inicio =super.mostrarMenu("BIENVENIDO A DULCESNDADOS ", new String[] {"Ya tengo una cuenta (Iniciar sesión)", "Soy Nuevo ( Registrarse )" , "Salir"});
		do {
			switch (inicio) {
			case 1:
				iniciarSesion();
				break;
			case 2:
				registrarUsuario();
				break;
			case 3:
				System.out.println("Saliendo del sistema. ¡Hasta luego!");
				return;
			default:
				System.out.println("Opción no válida. Por favor, elija una opción del menú.");
			}
		} while (inicio != 3);
	}
    
    private void registrarUsuario() {
    	
    	String nombre = super.pedirCadenaAlUsuario("Ingrese su nombre:");
    	String apellido = super.pedirCadenaAlUsuario("Ingrese su apellido:");
    	String email = super.pedirCadenaAlUsuario("Ingrese su correo electrónico:");
    	String contrasena = super.pedirCadenaAlUsuario("Ingrese su contraseña:");
    	String login = super.pedirCadenaAlUsuario("Ingrese su nombre de usuario:");
    	
    	Cliente cliente = new Cliente(nombre,apellido,email,contrasena,login,0.0);
    	
    	usuarios.add(cliente);
    	System.out.println("Usuario registrado exitosamente. Ahora puedes iniciar sesión con tu nuevo usuario.");
    	
    }
	private void iniciarSesion() {
		do {
		String login = super.pedirCadenaAlUsuario("Ingrese su nombre de usuario para iniciar sesión");
		String constrasena = super.pedirCadenaAlUsuario("Ingrese su contraseña para iniciar sesión");
		int indexUsuarioEncontrado= -1;
		if(usuarios.isEmpty()) {
			System.out.println("[WARN] No hay usuarios registrados. Por favor, contacte al administrador.");
			return;
		}else {
			for(int i=0; i<usuarios.size(); i++) {
				if(usuarios.get(i).getLogin().equalsIgnoreCase(login) && usuarios.get(i).getContrasena().equalsIgnoreCase(constrasena)) {
					System.out.println("Bienvenido, " + usuarios.get(i).getNombre() + " " + usuarios.get(i).getApellido() + "!");
					indexUsuarioEncontrado = i;
					break;
				}
			}
			if(indexUsuarioEncontrado == -1) {
				System.out.println("[ERROR] Usuario o contraseña incorrectos. Por favor, intente nuevamente.");
				return;
			}else {
				if(usuarios.get(indexUsuarioEncontrado) instanceof Cliente) {
					System.out.println("Has iniciado sesión como Cliente.");
					ConsolaCliente consolaCliente = new ConsolaCliente();
					consolaCliente.iniciar(inventarioVenta,inventarioPrestamo,todosLosJuegos,(Cliente) usuarios.get(indexUsuarioEncontrado),mesasDisponibles);
					}
				else if(usuarios.get(indexUsuarioEncontrado) instanceof Mesero) {
					System.out.println("Has iniciado sesión como Mesero.");
				}
				else if(usuarios.get(indexUsuarioEncontrado) instanceof Cocinero) {
					System.out.println("Has iniciado sesión como Cocinero.");
				}
				else if(usuarios.get(indexUsuarioEncontrado) instanceof Administrador) {
					System.out.println("Has iniciado sesión como Administrador.");
					ConsolaAdministrador consolaAdmin = new ConsolaAdministrador();
					consolaAdmin.iniciar((Administrador)usuarios.get(indexUsuarioEncontrado),inventarioVenta,inventarioPrestamo,todosLosJuegos);
				}
			}
		}
		
		} while (true);
		
    }
    private void inicializarMesas(int numeroMesas) { 
    	for (int i = 1; i <= numeroMesas; i++) {
    		int valorRandomCapacidadMesa= (int) (Math.random() * 5) + 3;
			Mesa mesa = new Mesa(i, valorRandomCapacidadMesa);
			mesasDisponibles.add(mesa);
		}
    }
 
    // Inicialización en orden
//    private void iniciar() {
//        System.out.println("=== DulcesnDados - Inicializando sistema ===\n");
//
//        // 1. Juegos e inventarios
//        cargarJuegosEInventarios();
//
//        // 2. Usuarios
//        cargarUsuarios();
//
//        // 3. Turnos
//        cargarTurnos();
//
//        // Si no había datos previos
//        if (todosLosJuegos.isEmpty() && usuarios.isEmpty()) {
//            System.out.println("[INFO] Sin datos previos. Cargando datos de ejemplo...");
//            cargarDatosEjemplo();
//            guardarTodo();
//        }
//
//        // Buscar usuarios por tipo
//        Cliente clienteEjem = null;
//        Mesero meseroEjem = null;
//        Cocinero cocineroEjem = null;
//        Administrador adminEjem = null;
//
//        for (Persona p : usuarios) {
//            if (p instanceof Cliente && clienteEjem == null) {
//                clienteEjem = (Cliente) p;
//            } else if (p instanceof Mesero && meseroEjem == null) {
//                meseroEjem = (Mesero) p;
//            } else if (p instanceof Cocinero && cocineroEjem == null) {
//                cocineroEjem = (Cocinero) p;
//            } else if (p instanceof Administrador && adminEjem == null) {
//                adminEjem = (Administrador) p;
//            }
//        }
//
//        // Validación para evitar errores
//        if (clienteEjem == null || meseroEjem == null || cocineroEjem == null || adminEjem == null) {
//            System.out.println("[WARN] No hay suficientes usuarios para pruebas");
//            return;
//        }
//
//        // -----------------------------
//        // Pruebas
//        // -----------------------------
//        Mesa mesaEjemplo1 = new Mesa(1, 4);
//        Mesa mesaEjemplo2 = new Mesa(2, 4);
//        Mesa mesaEjemplo3 = new Mesa(3, 4);
//
//        clienteEjem.ocuparMesa(mesaEjemplo1, 4, true, true);
//        clienteEjem.ocuparMesa(mesaEjemplo2, 1, false, false);
//        clienteEjem.ocuparMesa(mesaEjemplo3, 2, false, true);
//
//        if (!inventarioPrestamo.getEjemplares().isEmpty()) {
//            clienteEjem.solicitarPrestamo(inventarioPrestamo.getEjemplares().get(0));
//        }
//        clienteEjem.comprarJuego(todosLosJuegos.get(0), "", 0);
//
//        clienteEjem.comprarJuego(todosLosJuegos.get(0), "", 0);
//        clienteEjem.comprarJuego(todosLosJuegos.get(1), "", 0);
//
//        List<ItemMenu> items = new ArrayList<>();
//        ItemMenu item1 = new Bebida(false, "caliente", "Cafe", "cafe para tomar", 5000);
//        List<String> lista = new ArrayList<>(Arrays.asList("gluten", "leche", "arandanos"));
//        ItemMenu item2 = new Pasteleria("Torta", "torta de chocolate", 10000, lista);
//
//        items.add(item1);
//        items.add(item2);
//
//        clienteEjem.comprarCafeteria(items, 2000, 0);
//
//        guardarTodo();
//
//        System.out.println("\n=== Sistema listo ===");
//        System.out.println("Juegos:       " + todosLosJuegos.size());
//        System.out.println("Usuarios:     " + usuarios.size());
//        System.out.println("Turnos:       " + turnos.size());
//        System.out.println("Inv.prestamo: " + inventarioPrestamo.getEjemplares().size() + " ejemplares");
//        System.out.println("Inv.venta:    " + inventarioVenta.getJuegos().size() + " juegos");
//    }

    // Carga
    private void cargarJuegosEInventarios() {
        try {
            todosLosJuegos     = persistenciaJuegos.cargarJuegos();
            inventarioPrestamo = persistenciaJuegos.cargarInventarioPrestamo(todosLosJuegos);
            inventarioVenta    = persistenciaJuegos.cargarInventarioVenta(todosLosJuegos);
            System.out.println("[OK] Juegos e inventarios cargados.");
        } catch (PersistenciaException e) {
            System.out.println("[WARN] No se pudieron cargar juegos: " + e.getMessage());
            todosLosJuegos     = new ArrayList<>();
            inventarioPrestamo = new InventarioPrestamo(100);
            inventarioVenta    = new InventarioVenta(100);
        }
    }
 
    private void cargarUsuarios() {
        try {
            usuarios = persistenciaUsuarios.cargarUsuarios(RUTA_USUARIOS);
            System.out.println("[OK] Usuarios cargados.");
        } catch (Exception e) {
            System.out.println("[WARN] No se pudieron cargar usuarios: " + e.getMessage());
            usuarios = new ArrayList<>();
        }
    }
 
    private void cargarTurnos() {
        try {
            turnos = persistenciaTurnos.cargarTurnos(RUTA_TURNOS, usuarios);
            System.out.println("[OK] Turnos cargados.");
        } catch (Exception e) {
            System.out.println("[WARN] No se pudieron cargar turnos: " + e.getMessage());
            turnos = new ArrayList<>();
        }
    }

    // Guardado
    private void guardarTodo() {
        try {
            persistenciaJuegos.guardarJuegos(todosLosJuegos);
            persistenciaJuegos.guardarInventarios(inventarioPrestamo, inventarioVenta, todosLosJuegos);
            System.out.println("[OK] Juegos e inventarios guardados.");
        } catch (PersistenciaException e) {
            System.out.println("[ERROR] Al guardar juegos: " + e.getMessage());
        }
        try {
            persistenciaUsuarios.guardarUsuarios(usuarios, RUTA_USUARIOS);
            System.out.println("[OK] Usuarios guardados.");
        } catch (Exception e) {
            System.out.println("[ERROR] Al guardar usuarios: " + e.getMessage());
        }
        try {
            List<TurnoSemanal> validos = new ArrayList<>();
            for (TurnoSemanal t : turnos)
                if (t.getEmpleado() != null) validos.add(t);
            if (!validos.isEmpty()) {
                persistenciaTurnos.guardarTurnos(validos, RUTA_TURNOS);
                System.out.println("[OK] Turnos guardados.");
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Al guardar turnos: " + e.getMessage());
        }
    }
 
    // Datos de ejemplo (solo se usan si no hay archivos previos)
    private void cargarDatosEjemplo() {
        // Juegos
        JuegoMesa catan = new JuegoMesa("Catan", 1995, "Kosmos", "Tablero",
            3, 4, "apto para todos", true, 120000.0);
        catan.agregarEjemplar(new EjemplarJuego("Nuevo", catan));
        catan.agregarEjemplar(new EjemplarJuego("Bueno", catan));
        todosLosJuegos.add(catan);
 
        JuegoMesa uno = new JuegoMesa("Uno", 1971, "Mattel", "Cartas",
            2, 10, "apto para todos", false, 25000.0);
        uno.agregarEjemplar(new EjemplarJuego("Nuevo", uno));
        todosLosJuegos.add(uno);
 
        JuegoMesa twister = new JuegoMesa("Twister", 1966, "Hasbro", "Accion",
            2, 6, "exclusivo adultos", false, 45000.0);
        twister.agregarEjemplar(new EjemplarJuego("Bueno", twister));
        todosLosJuegos.add(twister);
 
        // Inventarios
        try {
            for (EjemplarJuego e : catan.getEjemplares())   inventarioPrestamo.agregarEjemplar(e);
            for (EjemplarJuego e : uno.getEjemplares())     inventarioPrestamo.agregarEjemplar(e);
            for (EjemplarJuego e : twister.getEjemplares()) inventarioPrestamo.agregarEjemplar(e);
            inventarioVenta.agregarJuego(uno);
            inventarioVenta.agregarJuego(twister);
        } catch (CapacidadMaximaSuperadaException e) {
            System.out.println("[ERROR] " + e.getMessage());
        }
 
        // Usuarios
        usuarios.add(new Cliente("Ana", "Garcia", "ana@mail.com", "1234", "ana", 0));
        Mesero mesero     = new Mesero("Luis", "Perez", "luis@mail.com", "1234", "luis");
        Mesero mesero2     = new Mesero("Luisa", "Peralta", "luisa@mail.com", "1234", "luisa");
        Cocinero cocinero = new Cocinero("Marta", "Lopez", "marta@mail.com", "1234", "marta");
        usuarios.add(mesero);
        usuarios.add(mesero2);
        usuarios.add(cocinero);
        usuarios.add(new Administrador("Carlos", "Diaz", "carlos@mail.com", "admin", "admin"));
 
        // Turnos
        turnos.add(new TurnoSemanal("Lunes", LocalTime.of(8, 0), LocalTime.of(16, 0), mesero));
        turnos.add(new TurnoSemanal("Lunes", LocalTime.of(8, 0), LocalTime.of(16, 0), cocinero));
    }
}
