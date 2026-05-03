package Modelo;

import java.util.List;

public class Torneo {
	
	private String nombre;
	private int numeroParticipantes;
	private JuegoMesa juego;
	private List<Persona> participantes;
	private String diaSemana;
	private String tipo;
	private Persona ganador;
	
	
	public Torneo(String nombre, int numeroParticipantes, JuegoMesa juego, String diaSemana,String tipo ) {
		this.nombre = nombre;
		this.numeroParticipantes = numeroParticipantes;
		this.juego = juego;
		this.participantes= null;
		this.diaSemana = diaSemana;
		this.tipo = tipo;
	}
	
	public Persona getGanador() {
		return ganador;
	}
	public void setGanador(Persona ganador) {
		this.ganador = ganador;
	}
	
	public String getTipo() {
		return tipo;
	}
	public String getDiaSemana() {
		return diaSemana;
	}
	public void setDiaSemana(String diaSemana) {
		this.diaSemana = diaSemana;
	}
	public String getNombre() {
		return nombre;
	}
	public int getNumeroParticipantes() {
		return numeroParticipantes;
	}
	public JuegoMesa getJuego() {
		return juego;
	}
	public List<Persona> getParticipantes() {
		return participantes;
	}
	public void setParticipantes(List<Persona> participantes) {
		for (Persona p : participantes) {
			if (this.participantes.size() < this.numeroParticipantes) {
				this.participantes.add(p);
			} else {
				System.out.println("No se pueden agregar más participantes. El torneo ya está lleno.");
				break;
			}
		}
	}
	public void agregarParticipante(Persona participante) {
		if (participantes.size() < numeroParticipantes) {
			participantes.add(participante);
		} else {
			System.out.println("No se pueden agregar más participantes. El torneo ya está lleno.");
		}
	}
	public void retirarParticipante(Persona participante) {
		participantes.remove(participante);
	}
	
	public int calcularTarifaInscpcion() {
		if(this.tipo.equalsIgnoreCase("Amistoso")) {
			return 0;
		} else {
			return 10;
		}
	}
}
