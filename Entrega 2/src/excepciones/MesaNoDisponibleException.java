package excepciones;

//Se lanza cuando se intenta ocupar una mesa que ya está ocupada por otro cliente, o cuando el café ha alcanzadosu capacidad máxima de personas.

public class MesaNoDisponibleException extends Exception {

    private int numeroMesa;

    public MesaNoDisponibleException(int numeroMesa) {
        super("La mesa " + numeroMesa + " no está disponible en este momento.");
        this.numeroMesa = numeroMesa;
    }

    public String getMessage() {
        return super.getMessage();
    }

    public int getNumeroMesa() {
        return numeroMesa;
    }
}
