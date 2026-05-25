package Modelo;
import java.time.LocalDateTime;

public class Prestamo {
	private LocalDateTime fechaHoraInicio;
	private LocalDateTime fechaHoraDevolucion;
	private String estado;
	private EjemplarJuego ejemplar;
	private Mesa mesa;
	private InventarioPrestamo inventarioPrestamo;

	public Prestamo(InventarioPrestamo inventarioPrestamo,EjemplarJuego ejemplar, Mesa mesa) {
		
		setFechaHoraInicio();
		this.ejemplar = ejemplar;
		this.mesa = mesa;
		this.inventarioPrestamo=inventarioPrestamo;
	}
	public InventarioPrestamo getInventarioPrestamo()
	{
		return inventarioPrestamo;
	}
	public void setFechaHoraInicio() {
		fechaHoraInicio=LocalDateTime.now();
	}
	public void setFechaHoraFinal() {
		fechaHoraDevolucion=LocalDateTime.now();
	}

	public LocalDateTime getFechaHoraInicio() {
		return fechaHoraInicio;
	}

	public LocalDateTime getFechaHoraDevolucion() {
		return fechaHoraDevolucion;
	}

	public String getEstado() {
		return estado;
	}
	public EjemplarJuego getEjemplar()
	{
		return ejemplar;
	}
	public Mesa getMesa()
	{
		return mesa;
	}
	public boolean estaActivo()
	{
		LocalDateTime ahora=LocalDateTime.now();
		if (fechaHoraInicio.equals(ahora) || fechaHoraInicio.isBefore(fechaHoraDevolucion))
		{
			return true;
		}
		return false;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	private void devolver()
	{
		this.setEstado("terminado");
		setFechaHoraFinal();
	}
	private void marcarDesaparecido()
	{
		this.estado="desaparecido";
		this.ejemplar.marcarDesaparecido();
		
	}
	public void setFechaHoraInicioManual(LocalDateTime localDateTime) {
		fechaHoraInicio = localDateTime;
	}
	public void setFechaHoraDevolucionManual(LocalDateTime localDateTime) {
		fechaHoraDevolucion = localDateTime;
	}
}

