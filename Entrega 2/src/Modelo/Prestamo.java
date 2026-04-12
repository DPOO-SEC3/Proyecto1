package Modelo;
import java.time.LocalDateTime;

public class Prestamo {
	private LocalDateTime fechaHoraInicio;
	private LocalDateTime fechaHoraDevolucion;
	private String estado;
	private EjemplarJuego ejemplar;
	private Mesa mesa;

	public Prestamo(LocalDateTime fechaHoraInicio, LocalDateTime fechaHoraDevolucion, String estado) {
		
		this.fechaHoraInicio = fechaHoraInicio;
		this.fechaHoraDevolucion = fechaHoraDevolucion;
		this.estado = estado;
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
	
	private void devolver()
	{
		LocalDateTime ahora=LocalDateTime.now();
		this.fechaHoraDevolucion=ahora;	
	}
	private void marcarDesaparecido()
	{
		this.estado="desaparecido";
	}
}

