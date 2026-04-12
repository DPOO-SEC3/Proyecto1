package Modelo;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class VentaCafeteria extends Venta {
	private List<ItemMenu> items;
	private Mesa mesa;
	private double impuestoConsumo;
	private double propina;
	
	public VentaCafeteria(LocalDateTime fechaHora, double descuentoAplicado,
			double puntosFidelidadGenerados, Persona comprador, Mesa mesa, double propina)
	{
		super(fechaHora, descuentoAplicado, puntosFidelidadGenerados,comprador);
		this.impuestoConsumo=0.08;
		this.propina=propina;
		this.items=new ArrayList<>();
		this.mesa=mesa;
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
