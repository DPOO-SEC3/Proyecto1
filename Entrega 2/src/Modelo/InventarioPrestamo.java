package Modelo;

import excepciones.CapacidadMaximaSuperadaException;
import excepciones.JuegoNoDisponibleException;

import java.util.ArrayList;
import java.util.List;

public class InventarioPrestamo {

    private int capacidadMaxima;
    private List<EjemplarJuego> ejemplares;

    public InventarioPrestamo(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
        this.ejemplares = new ArrayList<>();
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public List<EjemplarJuego> getEjemplares() {
        return ejemplares;
    }
    
    public EjemplarJuego buscarEjemplarPorID(String ID) throws JuegoNoDisponibleException {
		for (EjemplarJuego e : ejemplares) {
			if (e.getID().equalsIgnoreCase(ID)) {
				return e;
			}
		}
		throw new JuegoNoDisponibleException("ID: " + ID);
	}

    public EjemplarJuego buscarEjemplarDisponible(String nombreJuego) throws JuegoNoDisponibleException {
        for (EjemplarJuego e : ejemplares) {
            if (e.getJuegoMesa().getNombre().equals(nombreJuego) && e.isDisponible()) {
                return e;
            }
        }
        throw new JuegoNoDisponibleException(nombreJuego);
    }
    public List<EjemplarJuego> getEjemplaresPorJuego(JuegoMesa juego) {
        List<EjemplarJuego> resultado = new ArrayList<>();
        for (EjemplarJuego e : ejemplares) {
            if (e.getJuegoMesa().equals(juego)) {
                resultado.add(e);
            }
        }
        return resultado;
    }

    public void agregarEjemplar(EjemplarJuego ejemplar) throws CapacidadMaximaSuperadaException {
        if (ejemplares.size() >= capacidadMaxima) {
            throw new CapacidadMaximaSuperadaException("InventarioPrestamo", capacidadMaxima);
        }
        ejemplares.add(ejemplar);
    }

    public void removerEjemplar(EjemplarJuego ejemplar) {
        ejemplares.remove(ejemplar);
    }
}