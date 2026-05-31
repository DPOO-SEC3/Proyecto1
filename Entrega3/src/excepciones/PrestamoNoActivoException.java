package excepciones;

//Se lanza cuando se intenta devolver o modificar un préstamo que ya fue devuelto o que está marcado como desaparecido.

public class PrestamoNoActivoException extends Exception {

    private String estadoPrestamo;

    public PrestamoNoActivoException(String estadoPrestamo) {
        super("El préstamo no está activo. Estado actual: " + estadoPrestamo);
        this.estadoPrestamo = estadoPrestamo;
    }

    public String getEstadoPrestamo() {
        return estadoPrestamo;
    }
}
