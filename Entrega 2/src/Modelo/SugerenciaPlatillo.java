package Modelo;
import java.time.LocalDate;

public class SugerenciaPlatillo {
	private String descripcion;
	private String estado;
	private LocalDate fechaSugerencia;
	private Empleado empleado;
	private boolean estaPendiente;
	
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
	public Empleado getEmpleado()
	{
		return empleado;
	}
	public boolean estaPendiente()
	{
		return estaPendiente;
	}
	private void aprobar(){
		this.estado="aprobada";
	}
	public void rechazar()
	{
		this.estado="aprobada";
	}
}
