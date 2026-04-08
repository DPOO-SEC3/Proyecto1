package Modelo;

import java.util.List;

public class JuegoMesa {
	private String nombre;
	private int anioPublicacion;
	private String empresaFabricante;
	private String categoria;
	private int minimoJugadores;
	private int maximoJugadores;
	private String restriccionesEdad;
	private boolean esDificil;
	private double precioDeVenta;
	private List<EjemplarJuego> ejemplares;
	
	public JuegoMesa(String nombre, int anioPublicacion, String empresaFabricante, String categoria,
			int minimoJugadores, int maximoJugadores, String restriccionesEdad, boolean esDificil,
			double precioDeVenta) {
		this.nombre = nombre;
		this.anioPublicacion = anioPublicacion;
		this.empresaFabricante = empresaFabricante;
		this.categoria = categoria;
		this.minimoJugadores = minimoJugadores;
		this.maximoJugadores = maximoJugadores;
		this.restriccionesEdad = restriccionesEdad;
		this.esDificil = esDificil;
		this.precioDeVenta = precioDeVenta;
	}
	public String getNombre() {
		return nombre;
	}
	public int getAnioPublicacion() {
		return anioPublicacion;
	}
	public String getEmpresaFabricante() {
		return empresaFabricante;
	}
	public String getCategoria() {
		return categoria;
	}
	public int getMinimoJugadores() {
		return minimoJugadores;
	}
	public int getMaximoJugadores() {
		return maximoJugadores;
	}
	public String getRestriccionesEdad() {
		return restriccionesEdad;
	}
	public boolean esDificil() {
		return esDificil;
	}
	public double getPrecioDeVenta() {
		return precioDeVenta;
	}
	public List<EjemplarJuego> getListaEjemplares(){
		return ejemplares;
	}
	
	
	

}
