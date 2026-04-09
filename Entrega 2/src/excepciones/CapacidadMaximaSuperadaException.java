package excepciones;

// Se lanza cuando se intenta agregar un elemento a un inventario o al café y se ha alcanzado la capacidad máxima permitida.
public class CapacidadMaximaSuperadaException extends Exception {

    private int capacidadMaxima;
    private String contexto;

    public CapacidadMaximaSuperadaException(String contexto, int capacidadMaxima) {
        super("Se ha superado la capacidad máxima de " + capacidadMaxima + " en: " + contexto);
        this.capacidadMaxima = capacidadMaxima;
        this.contexto = contexto;
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public String getContexto() {
        return contexto;
    }
}
