package Modelo;

import excepciones.JuegoNoDisponibleException;
import excepciones.JuegoNoAptoParaMesaException;

import java.util.ArrayList;
import java.util.List;

public class JuegoMesa {

    private String nombre;
    private int anioPublicacion;
    private String empresaFabricante;
    private String categoria;
    private int minimoJugadores;
    private int maximoJugadores;
    private String restriccionEdad;
    private boolean esDificil;
    private double precioDeVenta;
    private List<EjemplarJuego> ejemplares;

    public JuegoMesa(String nombre, int anioPublicacion, String empresaFabricante,String categoria, int minimoJugadores, int maximoJugadores,String restriccionEdad, boolean esDificil, double precioDeVenta) {
		this.nombre = nombre;
		this.anioPublicacion = anioPublicacion;
        this.empresaFabricante = empresaFabricante;
        this.categoria = categoria;
        this.minimoJugadores = minimoJugadores;
        this.maximoJugadores = maximoJugadores;
        this.restriccionEdad = restriccionEdad;
        this.esDificil = esDificil;
        this.precioDeVenta = precioDeVenta;
        this.ejemplares = new ArrayList<>();
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

    public String getRestriccionEdad() {
        return restriccionEdad;
    }

    public boolean esDificil() {
        return esDificil;
    }

    public double getPrecioDeVenta() {
        return precioDeVenta;
    }

    public List<EjemplarJuego> getEjemplares() {
        return ejemplares;
    }
    public boolean hayEjemplarDisponible() {
        for (EjemplarJuego e : ejemplares) {
            if (e.isDisponible()) {
                return true;
            }
        }
        return false;
    }

    public EjemplarJuego getEjemplarDisponible() throws JuegoNoDisponibleException {
        for (EjemplarJuego e : ejemplares) {
            if (e.isDisponible()) {
                return e;
            }
        }
        throw new JuegoNoDisponibleException(nombre);
    }
    public void esAptoParaMesa(int numPersonas, boolean hayNinos, boolean hayMenoresEdad)
            throws JuegoNoAptoParaMesaException {
        if (restriccionEdad.equalsIgnoreCase("exclusivo adultos") && hayMenoresEdad) {
            throw new JuegoNoAptoParaMesaException(nombre,"el juego es exclusivo para adultos y hay menores de edad en la mesa.");
        }
        if (restriccionEdad.equalsIgnoreCase("no apto menores de 5") && hayNinos) {
            throw new JuegoNoAptoParaMesaException(nombre,"el juego no es apto para menores de 5 años y hay niños en la mesa.");
        }

        if (numPersonas < minimoJugadores) {
            throw new JuegoNoAptoParaMesaException(nombre,"la mesa tiene " + numPersonas + " persona(s) pero el juego necesita minimo " + minimoJugadores + ".");
        }
        if (numPersonas > maximoJugadores) {
            throw new JuegoNoAptoParaMesaException(nombre,"la mesa tiene " + numPersonas + " persona(s) pero el juego soporta hasta " + maximoJugadores + ".");
        }
    }

    public void setPrecioDeVenta(double precio) {
        this.precioDeVenta = precio;
    }

    public void setEsDificil(boolean esDificil) {
        this.esDificil = esDificil;
    }

    public void agregarEjemplar(EjemplarJuego ejemplar) {
        ejemplares.add(ejemplar);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JuegoMesa)) return false;
        JuegoMesa otro = (JuegoMesa) o;
        return this.nombre.equalsIgnoreCase(otro.nombre);
    }

    @Override
    public int hashCode() {
        return nombre.toLowerCase().hashCode();
    }

}