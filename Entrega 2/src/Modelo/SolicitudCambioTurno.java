package Modelo;

import java.time.LocalDate;

public class SolicitudCambioTurno {

    private String tipo;
    private String estado;
    private LocalDate fechaDeSolicitud;
    private String motivo;
    private Empleado solicitante;
    private Empleado empleadoDestino;

    public SolicitudCambioTurno(String tipo, String motivo, Empleado solicitante, Empleado empleadoDestino) {
        this.tipo = tipo;
        this.motivo = motivo;
        this.solicitante = solicitante;
        this.empleadoDestino = empleadoDestino;
        this.fechaDeSolicitud = LocalDate.now();
        this.estado = "pendiente";
    }

    public String getTipo() {
        return tipo;
    }

    public String getEstado() {
        return estado;
    }

    public LocalDate getFechaDeSolicitud() {
        return fechaDeSolicitud;
    }

    public String getMotivo() {
        return motivo;
    }

    public Empleado getSolicitante() {
        return solicitante;
    }

    public Empleado getEmpleadoDestino() {
        return empleadoDestino;
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