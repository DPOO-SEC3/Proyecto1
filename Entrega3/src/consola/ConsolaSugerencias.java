package consola;

import Modelo.*;

import java.util.List;

public class ConsolaSugerencias extends ConsolaBasica {

    public void iniciarEmpleado(Empleado empleado, List<SugerenciaPlatillo> listaSugerencias) {

        System.out.println("===== SUGERENCIAS DE PLATILLOS =====");

        int opcion;
        do {
            opcion = super.mostrarMenu("SUGERENCIAS", new String[]{
                    "Crear nueva sugerencia",
                    "Ver mis sugerencias",
                    "Volver"
            });

            switch (opcion) {
                case 1:
                    crearSugerencia(empleado, listaSugerencias);
                    break;
                case 2:
                    verSugerenciasEmpleado(empleado, listaSugerencias);
                    break;
                case 3:
                    break;
            }
        } while (opcion != 3);
    }

    public void iniciarAdministrador(List<SugerenciaPlatillo> listaSugerencias) {

        System.out.println("===== GESTIÓN DE SUGERENCIAS =====");

        int opcion;
        do {
            opcion = super.mostrarMenu("GESTIÓN DE SUGERENCIAS", new String[]{
                    "Ver todas las sugerencias",
                    "Aprobar una sugerencia",
                    "Rechazar una sugerencia",
                    "Volver"
            });

            switch (opcion) {
                case 1:
                    mostrarTodasLasSugerencias(listaSugerencias);
                    break;
                case 2:
                    gestionarSugerencia(listaSugerencias, true);
                    break;
                case 3:
                    gestionarSugerencia(listaSugerencias, false);
                    break;
                case 4:
                    break;
            }
        } while (opcion != 4);
    }

    private void crearSugerencia(Empleado empleado, List<SugerenciaPlatillo> listaSugerencias) {
        String descripcion = super.pedirCadenaAlUsuario("Describa el platillo que desea sugerir");

        if (descripcion == null || descripcion.trim().isEmpty()) {
            System.out.println("[ERROR] La descripción no puede estar vacía.");
            return;
        }

        SugerenciaPlatillo sugerencia = new SugerenciaPlatillo(descripcion.trim(), empleado);
        listaSugerencias.add(sugerencia);

        System.out.println("[OK] Sugerencia registrada exitosamente.");
        System.out.println("Descripción: " + sugerencia.getDescripcion());
        System.out.println("Fecha: " + sugerencia.getFechaSugerencia());
        System.out.println("Estado: " + sugerencia.getEstado());
    }

    private void verSugerenciasEmpleado(Empleado empleado, List<SugerenciaPlatillo> listaSugerencias) {
        boolean hayAlguna = false;

        System.out.println("Mis sugerencias:");
        for (SugerenciaPlatillo s : listaSugerencias) {
            if (s.getEmpleado().equals(empleado)) {
                System.out.println("- " + s.getDescripcion() + " - Estado: " + s.getEstado() + " - Fecha: " + s.getFechaSugerencia());
                hayAlguna = true;
            }
        }

        if (!hayAlguna) {
            System.out.println("[INFO] No ha realizado ninguna sugerencia todavía.");
        }
    }

    private void mostrarTodasLasSugerencias(List<SugerenciaPlatillo> listaSugerencias) {
        if (listaSugerencias.isEmpty()) {
            System.out.println("[INFO] No hay sugerencias registradas.");
            return;
        }

        System.out.println("Todas las sugerencias:");
        for (SugerenciaPlatillo s : listaSugerencias) {
            System.out.println("- " + s.getDescripcion() + " - Estado: " + s.getEstado() + " - Fecha: " + s.getFechaSugerencia());
        }
    }

    private void gestionarSugerencia(List<SugerenciaPlatillo> listaSugerencias, boolean aprobar) {
        System.out.println("Sugerencias pendientes:");
        int contador = 1;
        for (SugerenciaPlatillo s : listaSugerencias) {
            if (s.estaPendiente()) {
                System.out.println(contador + ". " + s.getDescripcion() + " - Fecha: " + s.getFechaSugerencia());
                contador++;
            }
        }

        if (contador == 1) {
            System.out.println("[INFO] No hay sugerencias pendientes.");
            return;
        }

        int indice = super.pedirEnteroAlUsuario((aprobar ? "¿Cuál desea aprobar?" : "¿Cuál desea rechazar?") + " (0 para cancelar)");
        if (indice == 0) return;

        SugerenciaPlatillo seleccionada = null;
        int count = 1;
        for (SugerenciaPlatillo s : listaSugerencias) {
            if (s.estaPendiente()) {
                if (count == indice) {
                    seleccionada = s;
                    break;
                }
                count++;
            }
        }

        if (seleccionada == null) {
            System.out.println("[ERROR] Opción no válida.");
            return;
        }

        if (aprobar) {
            seleccionada.aprobar();
            System.out.println("[OK] Sugerencia aprobada: " + seleccionada.getDescripcion());
        } else {
            seleccionada.rechazar();
            System.out.println("[OK] Sugerencia rechazada: " + seleccionada.getDescripcion());
        }
    }
}