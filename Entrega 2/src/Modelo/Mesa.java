package Modelo;
import java.util.List;
import java.util.ArrayList;



public class Mesa {
	
	private int numeroMesa;
	private int numeroDePersonas;
	private int maximaDePersonas;
	private boolean hayNiños;
	private boolean hayMenoresDeEdad;
	private List<Prestamo> prestamos;
	private List<VentaCafeteria> ventasCafeterias;
	private Cliente cliente;
	
	public Mesa(int numeroMesa, int maximaDePersonas) {
		this.numeroMesa = numeroMesa;
		this.maximaDePersonas = maximaDePersonas;
		this.prestamos = new ArrayList<>();
		this.ventasCafeterias = new ArrayList<>();
		this.cliente = cliente;
	}
	public void setNumeroPersonas(int numeroDePersonas)
	{
		this.numeroDePersonas=numeroDePersonas;
	}
	public void setHayNiños(boolean hayNiños)
	{
		this.hayNiños=hayNiños;
	}
	public void setHayMenoresDeEdad(boolean hayMenoresDeEdad)
	{
		this.hayMenoresDeEdad=hayMenoresDeEdad;
	}
	public void setClientes(Cliente cliente)
	{
		this.cliente=cliente;
	}

	public int getNumeroMesa() {
		return numeroMesa;
	}
	
	public int getNumeroDePersonas()
	{
		return numeroDePersonas;
	}

	public int getMaximaDePersonas() {
		return maximaDePersonas;
	}

	public boolean isHayNiños() {
		return hayNiños;
	}

	public boolean isHayMenoresDeEdad() {
		return hayMenoresDeEdad;
	}
	public List<Prestamo> getPrestamos()
	{
		return prestamos;
	}
	
	public List<VentaCafeteria> getVentasCafeterias()
	{
		return ventasCafeterias;
	}
	public boolean estaOcupada()
	{
		if (getNumeroDePersonas()>0)
		{
			return true;
		}
		return false;
	}
	public boolean tieneBebidaCaliente() {
	    for (VentaCafeteria venta : ventasCafeterias) {
	        for (ItemMenu item : venta.getItems()) {
	            if (item instanceof Bebida && ((Bebida) item).esCaliente()) {
	                return true;
	            }
	        }
	    }
	    return false;
	}
	
	public boolean tieneJuegoAccion()
	{
		for (Prestamo prestamo: prestamos)
		{
			if(prestamo.getEjemplar().getJuegoMesa().getCategoria().equals("Accion"))
			{
				return true;
			}
		}
		return false;
	}
	public Cliente getCliente()
	{
		return cliente;
	}
	public void ocupar(int numeroDePersonas, boolean hayNiños, boolean hayMenoresDeEdad, Cliente clientes)
	{
		if (estaOcupada())
		{
			liberar();
		}
		setNumeroPersonas(numeroDePersonas);
		setHayNiños(hayNiños);
		setHayMenoresDeEdad(hayMenoresDeEdad);
		setClientes(cliente);
	}
	
	public void liberar()
	{
		if (ventasCafeterias==null && prestamos==null)
		{
			this.numeroDePersonas=0;
			this.hayMenoresDeEdad=false;
			this.hayNiños=false;
			this.cliente = null;
		}
	}
	
	public void registrarPrestamo(Prestamo prestamo)
	{
		prestamos.add(prestamo);
	}
	
	public void registrarVentaCafeteria(VentaCafeteria venta)
	{
		ventasCafeterias.add(venta);
	}
	
}
