package modelo;

import java.util.ArrayList;
import java.util.List;

public class Pasteleria extends ItemMenu {
	private List<String> listaAlergenos;
	
	public Pasteleria(String nombre, String descripcion, double precioBase, List<String> listaAlergenos) {
	    super(nombre, descripcion, precioBase);
	    this.listaAlergenos = new ArrayList<>(listaAlergenos);
	}
	public List<String> getListaAlergenos()
	{
		return listaAlergenos;
	}
	public boolean tieneAlergeno(String alergeno)
	{
		return(listaAlergenos.contains(alergeno));
	}
	public void agregarAlergeno(String alergeno)
	{
		listaAlergenos.add(alergeno);
	}
	public void removerAlergeno(String alergeno)
	{
		listaAlergenos.remove(alergeno);
	}
}
