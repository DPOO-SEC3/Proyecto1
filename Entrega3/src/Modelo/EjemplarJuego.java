package Modelo;

public class EjemplarJuego {
	
    private String estado;
    private String ID;
    private int numeroDeVecesPrestado;
    private boolean disponible;
    private JuegoMesa juegoMesa;
	private boolean desaparecido;

    public EjemplarJuego(String estado, JuegoMesa juegoMesa) {
        this.estado = estado;
        this.juegoMesa = juegoMesa;
        this.ID= setID();
        this.numeroDeVecesPrestado = 0;
        this.disponible = true;
		this.desaparecido = false;
    }
    
    public String getID() {
		return ID;
	}
    
    public String setID() {
    	JuegoMesa juego= this.juegoMesa;
    	if (juego.getEjemplares().isEmpty()) {
			String iD = juego.getNombre() + "-1";
			return iD;
		} else {
			String iD = juego.getNombre() + "-" + (Integer.parseInt(juego.getEjemplares().get(juego.getEjemplares().size()-1).getID().split("-")[1]) + 1);
			return iD;
		}	
    }

    public String getEstado() {
        return estado;
    }
    public int getNumeroDeVecesPrestado() {
        return numeroDeVecesPrestado;
    }
    public boolean isDisponible() {
        return disponible && !desaparecido;
    }
    public JuegoMesa getJuegoMesa() {
        return juegoMesa;
    }

    public boolean isDesaparecido() {
        return desaparecido;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public void setNumeroDeVecesPrestado(int numeroDeVecesPrestado) {
        this.numeroDeVecesPrestado = numeroDeVecesPrestado;
    }

    public void incrementarVecesPrestado() {
        this.numeroDeVecesPrestado++;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void marcarDesaparecido() {
        this.desaparecido = true;
        this.disponible = false;
    }
}