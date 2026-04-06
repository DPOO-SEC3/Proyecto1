package Modelo;

import java.time.LocalTime;

public class TurnoSemanal {

    private String diaSemana;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private Empleado empleado;

    public TurnoSemanal(String diaSemana, LocalTime horaInicio, LocalTime horaFin, Empleado empleado) {
        this.diaSemana = diaSemana;
        this.horaInicio = horaInicio;
        this.horaFin = horaFin;
        this.empleado = empleado;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public boolean estaActivo() {
    	return true;
    }

    public boolean solapaCon(TurnoSemanal otro) {
        if (!this.diaSemana.equals(otro.diaSemana)) {
            return false;
        }

        return !(this.horaFin.isBefore(otro.horaInicio) || this.horaInicio.isAfter(otro.horaFin));
    }

    public void setDiaSemana(String dia) {
        this.diaSemana = dia;
    }

    public void setHoraInicio(LocalTime hora) {
        this.horaInicio = hora;
    }

    public void setHoraFin(LocalTime hora) {
        this.horaFin = hora;
    }
}
