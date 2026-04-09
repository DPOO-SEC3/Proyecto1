package excepciones;

// Se lanza cuando se intenta prestar un juego a una mesa pero el juego no es apto por restricción de edad o por no soportar el número de jugadores de la mesa.
public class JuegoNoAptoParaMesaException extends Exception {

    private String nombreJuego;
    private String motivo;

    public JuegoNoAptoParaMesaException(String nombreJuego, String motivo) {
        super("El juego '" + nombreJuego + "' no es apto para esta mesa. Motivo: " + motivo);
        this.nombreJuego = nombreJuego;
        this.motivo = motivo;
    }

    public String getNombreJuego() {
        return nombreJuego;
    }

    public String getMotivo() {
        return motivo;
    }
}
