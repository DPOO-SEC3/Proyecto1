package excepciones;

// Se lanza cuando se intenta prestar o vender un JuegoMesadel que no hay ningún ejemplar disponible en este momento.
public class JuegoNoDisponibleException extends Exception {

    private String nombreJuego;

    public JuegoNoDisponibleException(String nombreJuego) {
        super("El juego '" + nombreJuego + "' no tiene ejemplares disponibles en este momento.");
        this.nombreJuego = nombreJuego;
    }

    public String getNombreJuego() {
        return nombreJuego;
    }
}
