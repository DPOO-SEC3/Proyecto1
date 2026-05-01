package consola;

import Modelo.*;
import java.time.LocalTime;
import java.util.List;

public class ConsolaTurnos extends ConsolaBasica {

    public void iniciar(Administrador admin) {
        System.out.println("Bienvenido a la gestión de turnos.");
        mostrarMenuTurnos(admin);
    }

    public void mostrarMenuTurnos(Administrador admin) {
        int opcionElegida;

        do {
            String[] opciones = {
                "Ver turnos",
                "Crear turno",
                "Modificar turno",
                "Volver"
            };

            opcionElegida = super.mostrarMenu("GESTIÓN DE TURNOS", opciones);

            switch (opcionElegida) {
                case 1:
                    verTurnos(admin);
                    break;
                case 2:
                    crearTurno(admin);
                    break;
                case 3:
                    modificarTurno(admin);
                    break;
                case 4:
                    System.out.println("Volviendo...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }

        } while (opcionElegida != 4);
    }

    private void verTurnos(Administrador admin) {
        List<TurnoSemanal> turnos = admin.getTurnos();

        for (int i = 0; i < turnos.size(); i++) {
            TurnoSemanal t = turnos.get(i);
            System.out.println(i + ". " + t.getDiaSemana() + " "
                    + t.getHoraInicio() + " - " + t.getHoraFin());
        }
    }

    private void crearTurno(Administrador admin) {
        String dia = pedirCadenaAlUsuario("Día");
        int hInicio = pedirEnteroAlUsuario("Hora inicio");
        int hFin = pedirEnteroAlUsuario("Hora fin");

        admin.crearTurnoSemanal(
                null,
                dia,
                LocalTime.of(hInicio, 0),
                LocalTime.of(hFin, 0)
        );

        System.out.println("Turno creado.");
    }

    private void modificarTurno(Administrador admin) {
        List<TurnoSemanal> turnos = admin.getTurnos();

        int index = pedirEnteroAlUsuario("Índice del turno");

        if (index < turnos.size()) {
            String dia = pedirCadenaAlUsuario("Nuevo día");
            int hInicio = pedirEnteroAlUsuario("Nueva hora inicio");
            int hFin = pedirEnteroAlUsuario("Nueva hora fin");

            admin.modificarTurnoSemanal(
                    turnos.get(index),
                    dia,
                    LocalTime.of(hInicio, 0),
                    LocalTime.of(hFin, 0)
            );

            System.out.println("Turno modificado.");
        }
    }
}