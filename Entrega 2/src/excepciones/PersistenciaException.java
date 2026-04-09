package excepciones;

//Se lanza cuando ocurre un error al leer o escribir los archivos de persistencia del sistema.

public class PersistenciaException extends Exception {

    private String archivo;

    public PersistenciaException(String archivo, String detalle) {
        super("Error de persistencia en el archivo '" + archivo + "': " + detalle);
        this.archivo = archivo;
    }

    public PersistenciaException(String archivo, Throwable causa) {
        super("Error de persistencia en el archivo '" + archivo + "'.", causa);
        this.archivo = archivo;
    }

    public String getArchivo() {
        return archivo;
    }
}
