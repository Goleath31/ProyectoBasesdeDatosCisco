/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidad;

import java.time.LocalDate;

/**
 *
 * @author golea
 */
public class BloqueoEntidad {
    private int idBloqueo;
    private LocalDate fechaBloqueo;
    private String motivo;
    private AlumnoEntidad alumno;

    public BloqueoEntidad() {
    }

    public int getIdBloqueo() {
        return idBloqueo;
    }

    public void setIdBloqueo(int idBloqueo) {
        this.idBloqueo = idBloqueo;
    }

    public LocalDate getFechaBloqueo() {
        return fechaBloqueo;
    }

    public void setFechaBloqueo(LocalDate fechaBloqueo) {
        this.fechaBloqueo = fechaBloqueo;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public AlumnoEntidad getAlumno() {
        return alumno;
    }

    public void setAlumno(AlumnoEntidad alumno) {
        this.alumno = alumno;
    }
}
