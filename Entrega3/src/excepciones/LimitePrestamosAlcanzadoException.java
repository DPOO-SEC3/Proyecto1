package excepciones;

//Se lanza cuando una mesa ya tiene el máximo de préstamosactivos permitidos (2) y se intenta solicitar uno más.

public class LimitePrestamosAlcanzadoException extends Exception {

    private int numeroMesa;

    public LimitePrestamosAlcanzadoException(int numeroMesa) {
        super("La mesa " + numeroMesa + " ya tiene el máximo de 2 préstamos activos permitidos.");
        this.numeroMesa = numeroMesa;
    }

    public int getNumeroMesa() {
        return numeroMesa;
    }
}
