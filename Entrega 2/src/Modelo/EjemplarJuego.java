package Modelo;

public class EjemplarJuego {
	private String nombre;
	private String estado;
	private int numeroDeVecesPrestado;
	private boolean disponible;
	private JuegoMesa juegoMesa;
	private boolean ddesaparecido;
	
	public EjemplarJuego(String nombre,String estado,) {
		this.nombre= nombre;
		this.estado = estado;
		this.numeroDeVecesPrestado = 0;
		this.disponible = true;
	}

	public String getNombre(){
		return nombre;
	}
	public String getEstado() {
		return estado;
	}
	public int getNumeroDeVecesPrestado() {
		return numeroDeVecesPrestado;
	}
	public boolean isDisponible() {
		return disponible;
	}
	public JuegoMesa getJuegoMesa() {
		return this.juegoMesa;
	}
	private void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}
	private void incrementarVecesPrestado() {
		this.numeroDeVecesPrestado++;
	}
	private void setEstado(String estado) {
		this.estado = estado;
	}
	
}
