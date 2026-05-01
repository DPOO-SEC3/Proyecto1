package Modelo;

import java.util.List;

public class Torneo {
	
	private String nombre;
	private int numeroParticipantes;
	private JuegoMesa juego;
	private List<Persona> participantes;
	private String diaSemana;
	
	
	public Torneo(String nombre, int numeroParticipantes, JuegoMesa juego, String diaSemana) {
		this.nombre = nombre;
		this.numeroParticipantes = numeroParticipantes;
		this.juego = juego;
		this.participantes= null;
		this.diaSemana = diaSemana;
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
		this.participantes = participantes;
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
}
