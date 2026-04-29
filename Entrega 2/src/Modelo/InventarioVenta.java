package Modelo;

import excepciones.CapacidadMaximaSuperadaException;
import excepciones.JuegoNoExistenteException;

import java.util.ArrayList;
import java.util.List;

public class InventarioVenta {

    private int capacidadMaxima;
    private List<JuegoMesa> juegos;

    public InventarioVenta(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
        this.juegos = new ArrayList<>();
    }

    public int getCapacidadMaxima() {
        return capacidadMaxima;
    }

    public List<JuegoMesa> getJuegos() {
        return juegos;
    }

    public JuegoMesa buscarJuego(String nombre) throws JuegoNoExistenteException {
        for (JuegoMesa j : juegos) {
            if (j.getNombre().equalsIgnoreCase(nombre)) {
                return j;
            }
        }
        throw new JuegoNoExistenteException(nombre);
    }

    public boolean hayStock(JuegoMesa juego) {
        return juegos.contains(juego);
    }

    public void agregarJuego(JuegoMesa juego) throws CapacidadMaximaSuperadaException {
        if (juegos.size() >= capacidadMaxima) {
            throw new CapacidadMaximaSuperadaException("InventarioVenta",capacidadMaxima);
        }
        juegos.add(juego);
    }
    public void removerJuego(JuegoMesa juego) {
        juegos.remove(juego);
    }
}