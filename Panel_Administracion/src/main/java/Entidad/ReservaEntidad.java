/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidad;

import java.time.LocalDateTime;

/**
 *
 * @author golea
 */
public class ReservaEntidad {
    private int idReserva;
    private AlumnoEntidad alumno;
    private ComputadoraEntidad computadora;
    private LocalDateTime horaInicio;
    private LocalDateTime horaFin;
    private int tiempoRestante;

    public ReservaEntidad() {
    }

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public AlumnoEntidad getAlumno() {
        return alumno;
    }

    public void setAlumno(AlumnoEntidad alumno) {
        this.alumno = alumno;
    }

    public ComputadoraEntidad getComputadora() {
        return computadora;
    }

    public void setComputadora(ComputadoraEntidad computadora) {
        this.computadora = computadora;
    }

    public LocalDateTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalDateTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalDateTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalDateTime horaFin) {
        this.horaFin = horaFin;
    }

    public int getTiempoRestante() {
        return tiempoRestante;
    }

    public void setTiempoRestante(int tiempoRestante) {
        this.tiempoRestante = tiempoRestante;
    }
}
