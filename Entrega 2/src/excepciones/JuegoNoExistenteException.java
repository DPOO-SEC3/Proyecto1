package excepciones;

//Se lanza cuando se busca un JuegoMesa que no existeen el inventario o catálogo del café.
public class JuegoNoExistenteException extends Exception {

    private String nombreJuego;

    public JuegoNoExistenteException(String nombreJuego) {
        super("El juego '" + nombreJuego + "' no existe en el catálogo.");
        this.nombreJuego = nombreJuego;
    }

    public String getNombreJuego() {
        return nombreJuego;
    }
}
