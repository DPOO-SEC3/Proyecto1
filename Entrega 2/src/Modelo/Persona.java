package Modelo;

public abstract class Persona {

    private String nombre;
    private String apellido;
    private String correoElectronico;
    private String contrasena;
    private String login;

    public Persona(String nombre, String apellido, String correoElectronico, String contrasena, String login) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.correoElectronico = correoElectronico;
        this.contrasena = contrasena;
        this.login = login;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getCorreoElectronico() {
        return correoElectronico;
    }

    public String getLogin() {
        return login;
    }
    
    public String getContrasena() {
        return contrasena;
    }

    public boolean verificarContrasena(String contrasena) {
        return this.contrasena.equals(contrasena);
    }
}