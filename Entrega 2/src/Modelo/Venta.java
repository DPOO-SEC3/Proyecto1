package Modelo;
import java.time.LocalDateTime;
import java.util.HashMap;



public abstract class Venta {
	
	private LocalDateTime fechaHora;
	protected double subtotal;
	protected Double total;
	private double descuentoAplicado;
	private double puntosFidelidadGenerados;
	private Persona comprador;
	public Venta(LocalDateTime fechaHora, String codigoDescuento, Persona comprador) {
	
		this.fechaHora = fechaHora;
		this.subtotal = 0;
		this.total = 0.0;
		this.comprador = comprador;
		calcularPuntosFidelidadGenerados();
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
	public void calcularPuntosFidelidadGenerados() {
		this.puntosFidelidadGenerados = total.intValue() * 0.01;
	}
	public int getPuntosFidelidadRedimibles() {
		return (int) puntosFidelidadGenerados;
	}
	
	public double calcularDescuento(String codigoDescuento, HashMap<String, Double> codigosDescuento) {
		if (codigosDescuento.containsKey(codigoDescuento)) {
			this.descuentoAplicado = codigosDescuento.get(codigoDescuento);
			return codigosDescuento.get(codigoDescuento);
		}
		return 0;
	}
	

	protected abstract double calcularSubtotal();
	protected abstract double calcularTotal();
}
	
