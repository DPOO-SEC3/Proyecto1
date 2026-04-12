package Modelo;

public abstract class ItemMenu {
	
	private String nombre;
	private String descripcion;
	private double precioBase;
	
	public ItemMenu(String nombre, String descripcion, double precioBase) {
		super();
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precioBase = precioBase;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public void setPrecioBase(double precioBase) {
		this.precioBase = precioBase;
	}

	public String getNombre() {
		return nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public double getPrecioBase() {
		return precioBase;
	}
	
}
