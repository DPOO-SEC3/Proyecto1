package modelo;
import java.time.LocalDateTime;



public abstract class Venta {
	
	private LocalDateTime fechaHora;
	protected double subtotal;
	protected double total;
	private double descuentoAplicado;
	private double puntosFidelidadGenerados;
	private Persona comprador;
	public Venta(LocalDateTime fechaHora, double descuentoAplicado, double puntosFidelidadGenerados) {
	
		this.fechaHora = fechaHora;
		this.descuentoAplicado = descuentoAplicado;
		this.puntosFidelidadGenerados = puntosFidelidadGenerados;
		this.subtotal = 0;
		this.total = 0;
		this.comprador = comprador;
	}
	public LocalDateTime getFechaHora() {
		return fechaHora;
	}
	public double getSubtotal() {
		return subtotal;
	}
	public double getDescuentoAplicado() {
		return descuentoAplicado;
	}
	public double getPuntosFidelidadGenerados() {
		return puntosFidelidadGenerados;
	}
	public Persona getComprador()
	{
		return comprador;
	}
	

	protected abstract double calcularSubtotal();
	protected abstract double calcularTotal();
}
	
