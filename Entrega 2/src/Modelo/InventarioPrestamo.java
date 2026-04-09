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

    public EjemplarJuego buscarEjemplarDisponible(JuegoMesa juego) throws JuegoNoDisponibleException {
        for (EjemplarJuego e : ejemplares) {
            if (e.getJuegoMesa().equals(juego) && e.isDisponible()) {
                return e;
            }
        }
        throw new JuegoNoDisponibleException(juego.getNombre());
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