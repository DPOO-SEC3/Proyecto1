package consola;

import Modelo.*;
import java.util.List;

public class ConsolaSolicitudes extends ConsolaBasica {

    public void iniciar(Administrador admin) {
        System.out.println("Bienvenido a la gestión de solicitudes de cambio de turno.");
        mostrarMenuSolicitudes(admin);
    }

    public void mostrarMenuSolicitudes(Administrador admin) {
        int opcionElegida;

        do {
            String[] opciones = {
                "Ver solicitudes",
                "Aprobar solicitud",
                "Rechazar solicitud",
                "Volver"
            };

            opcionElegida = super.mostrarMenu("GESTIÓN DE SOLICITUDES", opciones);

            switch (opcionElegida) {
                case 1:
                    verSolicitudes(admin);
                    break;
                case 2:
                    aprobarSolicitud(admin);
                    break;
                case 3:
                    rechazarSolicitud(admin);
                    break;
                case 4:
                    System.out.println("Volviendo...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }

        } while (opcionElegida != 4);
    }

    private void verSolicitudes(Administrador admin) {
        List<SolicitudCambioTurno> solicitudes = admin.getSolicitudesCambioPendientes();

        for (int i = 0; i < solicitudes.size(); i++) {
            SolicitudCambioTurno s = solicitudes.get(i);
            System.out.println(i + ". Tipo: " + s.getTipo() +
                               " | Estado: " + s.getEstado());
        }
    }

    private void aprobarSolicitud(Administrador admin) {
        List<SolicitudCambioTurno> solicitudes = admin.getSolicitudesCambioPendientes();

        int index = pedirEnteroAlUsuario("Índice de la solicitud a aprobar");

        if (index < solicitudes.size()) {
            admin.aprobarSolicitudCambio(solicitudes.get(index));
            System.out.println("Solicitud aprobada.");
        }
    }

    private void rechazarSolicitud(Administrador admin) {
        List<SolicitudCambioTurno> solicitudes = admin.getSolicitudesCambioPendientes();

        int index = pedirEnteroAlUsuario("Índice de la solicitud a rechazar");

        if (index < solicitudes.size()) {
            admin.rechazarSolicitudCambio(solicitudes.get(index));
            System.out.println("Solicitud rechazada.");
        }
    }
}