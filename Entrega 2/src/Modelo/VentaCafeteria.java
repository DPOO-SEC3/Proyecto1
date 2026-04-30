package Modelo;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class VentaCafeteria extends Venta {
	private List<ItemMenu> items;
	private Mesa mesa;
	private double impuestoConsumo;
	private double propina;
	private Persona comprador;
	
	public VentaCafeteria(LocalDateTime fechaHora, String codigoDescuento, Persona comprador, double propina)
	{
		super(fechaHora, codigoDescuento,comprador);
		this.impuestoConsumo=0.08;
		this.propina=propina;
		this.items=new ArrayList<>();
		this.comprador=comprador;
	}
	
	public Persona getPersona()
	{
		return comprador;
	}
	public List<ItemMenu> getItems()
	{
		return items;
	}
	public double getImpuestoConsumo()
	{
		return (subtotal*impuestoConsumo);
	}
	public double getPropina()
	{
		return propina;
	}
	public Mesa getMesa()
	{
		return mesa;
	}
	public void setPropina(double propina)
	{
		this.propina=propina;
	}
	protected double calcularSubtotal()
	{
		this.subtotal=0;
		for (ItemMenu comida: items)
		{
			this.subtotal=subtotal+comida.getPrecioBase();
		}
		return subtotal;
	}
	protected double calcularTotal()
	{
		this.total=subtotal+getImpuestoConsumo()+propina;
		return total;
	}
}
