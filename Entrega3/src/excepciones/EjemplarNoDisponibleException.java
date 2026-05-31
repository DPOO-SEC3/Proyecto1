package excepciones;

// Se lanza cuando se intenta prestar un EjemplarJuego específico que está actualmente prestado o marcado como desaparecido.
public class EjemplarNoDisponibleException extends Exception {

    private String idEjemplar;
    private String estadoActual;

    public EjemplarNoDisponibleException(String idEjemplar, String estadoActual) {
        super("El ejemplar '" + idEjemplar + "' no está disponible. Estado actual: " + estadoActual);
        this.idEjemplar = idEjemplar;
        this.estadoActual = estadoActual;
    }

    public String getIdEjemplar() {
        return idEjemplar;
    }

    public String getEstadoActual() {
        return estadoActual;
    }
}
