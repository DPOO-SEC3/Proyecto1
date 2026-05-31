package Modelo;

public class Bebida extends ItemMenu {
	private boolean esAlcoholica;
	private String temperatura;
	
	public Bebida(boolean esAlcoholica, String temperatura, String nombre, String descripcion, double precioBase)
	{
		super(nombre, descripcion, precioBase);
		this.esAlcoholica=esAlcoholica;
		this.temperatura=temperatura;
	}

	public boolean isEsAlcoholica() {
		return esAlcoholica;
	}

	public String getTemperatura() {
		return temperatura;
	}
	
	public boolean esCaliente()
	{
		double temperatura= Double.parseDouble(getTemperatura());
		if (temperatura>40)
		{
			return true;
		}
		return false;
	}
	public boolean aptaParaMesa(Mesa mesa)
	{
		if (esAlcoholica && mesa.isHayMenoresDeEdad())
		{
			return false;
		}
		if (esCaliente() && mesa.tieneJuegoAccion())
		{
			return false;
		}
		return true;
	}

}
