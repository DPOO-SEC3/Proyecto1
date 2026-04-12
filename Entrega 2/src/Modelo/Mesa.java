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
			if(prestamo.getEjemplar().getJuegoMesa().getCategoria()=="Accion")
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
	private void ocupar(int numeroDePersonas, boolean hayNiños, boolean hayMenoresDeEdad, Cliente cliente)
	{
		if (estaOcupada())
		{
			liberar();
		}
		this.numeroDePersonas=numeroDePersonas;
		this.hayNiños=hayNiños;
		this.hayMenoresDeEdad=hayMenoresDeEdad;
		this.cliente=cliente;
	}
	
	private void liberar()
	{
		if (ventasCafeterias==null && prestamos==null)
		{
			this.numeroDePersonas=0;
			this.hayMenoresDeEdad=false;
			this.hayNiños=false;
		}
	}
	
	private void registrarPrestamo(Prestamo prestamo)
	{
		prestamos.add(prestamo);
	}
	
	private void registrarVentaCafeteria(VentaCafeteria venta)
	{
		ventasCafeterias.add(venta);
	}
	
}
