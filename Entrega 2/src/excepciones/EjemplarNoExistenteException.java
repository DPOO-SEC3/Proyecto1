package excepciones;

// Se lanza cuando se busca un EjemplarJuego con un identificadorque no corresponde a ningún ejemplar registrado en el sistema.
public class EjemplarNoExistenteException extends Exception {

    private String idEjemplar;

    public EjemplarNoExistenteException(String idEjemplar) {
        super("El ejemplar con id '" + idEjemplar + "' no existe en el sistema.");
        this.idEjemplar = idEjemplar;
    }

    public String getIdEjemplar() {
        return idEjemplar;
    }
}
