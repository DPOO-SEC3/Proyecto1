package Modelo;

import java.time.LocalDate;

public class SugerenciaPlatillo {

    private String descripcion;
    private String estado;
    private LocalDate fechaSugerencia;
    private Empleado empleado;

    public SugerenciaPlatillo(String descripcion, Empleado empleado) {
        this.descripcion = descripcion;
        this.empleado = empleado;
        this.fechaSugerencia = LocalDate.now();
        this.estado = "pendiente";
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public LocalDate getFechaSugerencia() {
        return fechaSugerencia;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public boolean estaPendiente() {
        return estado.equalsIgnoreCase("pendiente");
    }

    public void aprobar() {
        this.estado = "aprobada";
    }

    public void rechazar() {
        this.estado = "rechazada";
    }
}
