package modelo;

public class SugerenciaPlatillo {
	private String descripcion;
	private String estado;
	private LocalDate fechaSugerencia;
	
	public SugerenciaPlatillo (String descripcion, String estado, LocalDate fechaSugerencia)
	{
		this.descripcion=descripcion;
		this.estado=estado;
		this.fechaSugerencia=fechaSugerencia;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public String getEstado() {
		return estado;
	}

	public LocalDate getFechaSugerencia() {
		return fechaSugerencia;
	}
	public empleado getEmpleado()
	{
		return empleado;
	}
	public boolean estaPendiente()
	{
		return estaPendiente;
	}
	private void aprobar()
	{
		this.estado=true;
		return estado;
	}
	public void rechazar()
	{
		this.estado=false;
		return estado;
	}
}
