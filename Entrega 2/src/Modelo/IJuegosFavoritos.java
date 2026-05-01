package Modelo;

import java.util.List;

public interface IJuegosFavoritos {
	
	public abstract void agregarFavorito(JuegoMesa juego);

    public abstract void eliminarFavorito(JuegoMesa juego);
    
    public abstract List<JuegoMesa> getJuegosFavoritos();

}
