/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtos;

import java.sql.Date;

/**
 * Contiene los datos necesarios para procesar el bloqueo de un alumno.
 *
 * * @author golea
 */
public class BloqueoAlumnoDTO {

    private String idAlumno;
    private String motivo;
    private Date Fecha;

    /**
     * Constructor por defecto.
     */
    public BloqueoAlumnoDTO() {
    }

    /**
     * Constructor para bloqueo completo.
     *
     *
     * /**
     * Constructor simplificado.
     *
     * @param idAlumno ID del alumno.
     * @param motivo Razón del bloqueo.
     */
    public BloqueoAlumnoDTO(String idAlumno, String motivo) {
        this.idAlumno = String.format("%010d", Integer.parseInt(idAlumno));
        this.motivo = motivo;
        this.Fecha = new java.sql.Date(System.currentTimeMillis());
    }

    public String getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(String idAlumno) {
        this.idAlumno = idAlumno;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Date getFecha() {
        return Fecha;
    }

    public void setFecha(Date Fecha) {
        this.Fecha = Fecha;
    }

    
    
    
}
