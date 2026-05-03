package Modelo;

import java.util.List;

public interface IInscripcionTorneo {
	
	public Torneo inscribirTorneo(List<Torneo> torneosDisponibles,String NombreTorneo ,List<Persona> participantes);
	public void retirarTorneo(String NombreTorneo ,List<Persona> participantes);

}
