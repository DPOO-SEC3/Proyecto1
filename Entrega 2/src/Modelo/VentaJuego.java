package Modelo;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;



public class VentaJuego extends Venta {
	private double IVA;
	private List<JuegoMesa> juegos;
	
	public VentaJuego(LocalDateTime fechaHora, double descuentoAplicado, double puntosFidelidadGenerados, Persona comprador)
	{
		super (fechaHora, descuentoAplicado, puntosFidelidadGenerados, comprador);
		this.juegos=new ArrayList<>();
		this.IVA=0.19;
	}
	public List<JuegoMesa> getJuegos()
	{
		return juegos;
	}
	public double getIVA(double subtotal)
	{
		return (subtotal*IVA);
	}
	protected double calcularSubtotal()
	{
		this.subtotal=0;
		for (JuegoMesa juego: juegos)
		{
			this.subtotal=subtotal+juego.getPrecioDeVenta();
		}
		return subtotal;
	}
	protected double calcularTotal()
	{
		this.total=subtotal+(this.getIVA(subtotal));
		return total;
	}
}
