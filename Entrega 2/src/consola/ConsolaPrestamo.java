package consola;

import Modelo.*;

import java.util.List;

public class ConsolaPrestamo extends ConsolaBasica {

    public void iniciar(Cliente cliente, InventarioPrestamo inventarioPrestamo, Mesa mesa) {

        System.out.println("===== PRÉSTAMO DE JUEGOS =====");

        int opcion;
        do {
            opcion = super.mostrarMenu("PRÉSTAMOS", new String[]{
                    "Solicitar préstamo de un juego",
                    "Devolver un juego prestado",
                    "Ver mis préstamos activos",
                    "Volver"
            });

            switch (opcion) {
                case 1:
                    solicitarPrestamo(cliente, inventarioPrestamo, mesa);
                    break;
                case 2:
                    devolverJuego(cliente);
                    break;
                case 3:
                    mostrarPrestamosActivos(cliente);
                    break;
                case 4:
                    break;
            }
        } while (opcion != 4);
    }

    private void solicitarPrestamo(Cliente cliente, InventarioPrestamo inventarioPrestamo, Mesa mesa) {

        if (mesa == null) {
            System.out.println("[ERROR] Debe tener una mesa asignada para solicitar un préstamo.");
            return;
        }

        List<EjemplarJuego> disponibles = inventarioPrestamo.getEjemplares();
        if (disponibles.isEmpty()) {
            System.out.println("[INFO] No hay ejemplares disponibles para préstamo en este momento.");
            return;
        }

        System.out.println("Ejemplares disponibles para préstamo:");
        int contador = 1;
        for (EjemplarJuego ejemplar : disponibles) {
            if (ejemplar.isDisponible()) {
                System.out.println(contador + ". " + ejemplar.getJuegoMesa().getNombre() + " - Estado: " + ejemplar.getEstado());
                contador++;
            }
        }

        if (contador == 1) {
            System.out.println("[INFO] No hay ejemplares disponibles en este momento.");
            return;
        }

        int indice = super.pedirEnteroAlUsuario("Seleccione el número del ejemplar que desea (0 para cancelar)");
        if (indice == 0) return;

        EjemplarJuego seleccionado = null;
        int count = 1;
        for (EjemplarJuego ejemplar : disponibles) {
            if (ejemplar.isDisponible()) {
                if (count == indice) {
                    seleccionado = ejemplar;
                    break;
                }
                count++;
            }
        }

        if (seleccionado == null) {
            System.out.println("[ERROR] Opción no válida.");
            return;
        }

        boolean confirmar = super.pedirConfirmacionAlUsuario("¿Confirmar préstamo de " + seleccionado.getJuegoMesa().getNombre() + "?");
        if (!confirmar) return;

        Prestamo prestamo = cliente.solicitarPrestamo(inventarioPrestamo, seleccionado, mesa);
        mesa.registrarPrestamo(prestamo);

        System.out.println("[OK] Préstamo registrado exitosamente.");
        System.out.println("Juego: " + seleccionado.getJuegoMesa().getNombre());
        System.out.println("Inicio: " + prestamo.getFechaHoraInicio());
    }

    private void devolverJuego(Cliente cliente) {

        List<Prestamo> prestamos = cliente.getPrestamos();

        System.out.println("Sus préstamos activos:");
        int contador = 1;
        for (Prestamo prestamo : prestamos) {
            if (prestamo.estaActivo()) {
                System.out.println(contador + ". " + prestamo.getEjemplar().getJuegoMesa().getNombre() + " - Inicio: " + prestamo.getFechaHoraInicio());
                contador++;
            }
        }

        if (contador == 1) {
            System.out.println("[INFO] No tiene préstamos activos para devolver.");
            return;
        }

        int indice = super.pedirEnteroAlUsuario("Seleccione el número del préstamo a devolver (0 para cancelar)");
        if (indice == 0) return;

        Prestamo aDevolver = null;
        int count = 1;
        for (Prestamo prestamo : prestamos) {
            if (prestamo.estaActivo()) {
                if (count == indice) {
                    aDevolver = prestamo;
                    break;
                }
                count++;
            }
        }

        if (aDevolver == null) {
            System.out.println("[ERROR] Opción no válida.");
            return;
        }

        boolean confirmar = super.pedirConfirmacionAlUsuario("¿Confirmar devolución de " + aDevolver.getEjemplar().getJuegoMesa().getNombre() + "?");
        if (!confirmar) return;

        cliente.devolverJuego(aDevolver);
        System.out.println("[OK] Juego devuelto correctamente. ¡Gracias!");
    }

    private void mostrarPrestamosActivos(Cliente cliente) {
        List<Prestamo> prestamos = cliente.getPrestamos();
        boolean hayActivos = false;

        System.out.println("Mis préstamos activos:");
        for (Prestamo prestamo : prestamos) {
            if (prestamo.estaActivo()) {
                System.out.println("- " + prestamo.getEjemplar().getJuegoMesa().getNombre() + " - Inicio: " + prestamo.getFechaHoraInicio() + " - Estado: " + prestamo.getEstado());
                hayActivos = true;
            }
        }

        if (!hayActivos) {
            System.out.println("[INFO] No tiene préstamos activos en este momento.");
        }
    }
}