package Modelo;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;
import Modelo.Venta;



public class VentaJuego extends Venta {
	private double IVA;
	private List<JuegoMesa> juegos;
	private InventarioVenta inventarioVentaJuego;
	private Persona comprador;
	
	public VentaJuego(List<JuegoMesa> juegos,LocalDateTime fechaHora, String codigoDescuento, Persona comprador, InventarioVenta inventarioVentaJuego)
	{
		super (fechaHora, codigoDescuento, comprador);
		this.juegos=juegos;
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
	public double calcularSubtotal()
	{
		this.subtotal=0;
		for (JuegoMesa juego: juegos)
		{
			this.subtotal=subtotal+juego.getPrecioDeVenta();
		}
		return subtotal;
	}
	public double calcularTotal()
	{
		this.total=subtotal+(this.getIVA(subtotal));
		return total;
	}
}
