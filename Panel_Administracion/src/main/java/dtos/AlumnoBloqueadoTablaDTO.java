/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtos;

/**
 * Representa la información de un alumno bloqueado para ser visualizada en una
 * tabla.
 *
 * * @author golea
 */
public class AlumnoBloqueadoTablaDTO {

    private int idAlumno;
    private String nombre;
    private String motivo;
    private String fechaBloqueo;

    /**
     * Constructor por defecto.
     */
    public AlumnoBloqueadoTablaDTO() {
    }

    /**
     * Constructor con parámetros.
     *
     * @param idAlumno Identificador único del alumno.
     * @param nombre Nombre completo del alumno.
     * @param motivo Razón por la cual el alumno fue bloqueado.
     * @param fechaBloqueo Fecha y hora en formato String del bloqueo.
     */
    public AlumnoBloqueadoTablaDTO(int idAlumno, String nombre, String motivo, String fechaBloqueo) {
        this.idAlumno = idAlumno;
        this.nombre = nombre;
        this.motivo = motivo;
        this.fechaBloqueo = fechaBloqueo;
    }

    public int getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(int idAlumno) {
        this.idAlumno = idAlumno;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getFechaBloqueo() {
        return fechaBloqueo;
    }

    public void setFechaBloqueo(String fechaBloqueo) {
        this.fechaBloqueo = fechaBloqueo;
    }
}
