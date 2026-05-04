package consola;

import consola.ConsolaVentas;
import consola.ConsolaPrestamos;
import consola.ConsolaTorneos;

import java.util.List;

import Modelo.*;

public class ConsolaEmpleado extends ConsolaBasica {

    public int mostrarMenuEmpleado() {
        String[] opciones = {
            "Consultar turnos semanales",
            "Solicitar cambio de turno",
            "Comprar Juego",
            "Pedir juego prestado",
            "Sugerencia platillos",
            "Torneos",
            "Salir"
        };
        return super.mostrarMenu("MENU EMPLEADO", opciones);
    }

    private void gestionarTurnos(Empleado empleado) {
        TurnoSemanal turno = empleado.getTurnoSemanal();

        if (turno == null) {
            System.out.println("No tienes turno asignado.");
        } else {
            System.out.println("Turno asignado:");
            System.out.println("Día: " + turno.getDiaSemana());
            System.out.println("Inicio: " + turno.getHoraInicio());
            System.out.println("Fin: " + turno.getHoraFin());
        }
    }

    private void gestionarSolicitudCambio(Empleado empleado, List<Empleado> empleados) {
        String tipo = pedirCadenaAlUsuario("Tipo de solicitud");
        String motivo = pedirCadenaAlUsuario("Motivo");

        Empleado otro = null;

        boolean intercambio = pedirConfirmacionAlUsuario("¿Desea intercambiar con otro empleado?");
        if (intercambio) {

            System.out.println("Seleccione empleado:");
            for (int i = 0; i < empleados.size(); i++) {
                Empleado e = empleados.get(i);
                if (!e.equals(empleado)) {
                    System.out.println(i + ". " + e.getNombre());
                }
            }

            int indice = pedirEnteroAlUsuario("Índice del empleado");

            if (indice >= 0 && indice < empleados.size()) {
                otro = empleados.get(indice);
            }
        }

        SolicitudCambioTurno solicitud = empleado.solicitarCambioTurno(tipo, motivo, otro);

        System.out.println("Solicitud creada correctamente.");
        System.out.println("Estado: " + solicitud.getEstado());
    }

    public <T extends Empleado> void iniciarEmpleado(
            InventarioVenta inventarioVenta,
            InventarioPrestamo inventarioPrestamo,
            Mesa mesa,
            T user,
            List<Torneo> torneosDisponibles,
            List<Empleado> empleados,
            List<ItemMenu> itemsMenu) {

        int opcion;

        do {
            opcion = mostrarMenuEmpleado();

            switch (opcion) {

                case 1:
                    gestionarTurnos(user);
                    break;

                case 2:
                    gestionarSolicitudCambio(user, empleados);
                    break;

                case 3:
                    ConsolaVentas consolaVentas = new ConsolaVentas();
                    consolaVentas.iniciar(inventarioVenta, user,mesa,itemsMenu);
                    break;

                case 4:
                    ConsolaPrestamos consolaPrestamos = new ConsolaPrestamos();
                    consolaPrestamos.iniciar(inventarioPrestamo, mesa, user);
                    break;

                case 5:
                    System.out.println("Falta implementar consola para Sugerencia platillos");
                    break;

                case 6:
                    ConsolaTorneos consolaTorneos = new ConsolaTorneos();
                    consolaTorneos.iniciar(torneosDisponibles, user);
                    break;

                case 7:
                    System.out.println("Salir");
                    break;

                default:
                    System.out.println("Opción no válida.");
            }

        } while (opcion != 7);
    }
}