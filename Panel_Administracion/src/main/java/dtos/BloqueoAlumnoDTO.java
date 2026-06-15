/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtos;

/**
 * Contiene los datos necesarios para procesar el bloqueo de un alumno.
 *
 * * @author golea
 */
public class BloqueoAlumnoDTO {

    private int idAlumno;
    private String matricula;
    private String motivo;

    /**
     * Constructor por defecto.
     */
    public BloqueoAlumnoDTO() {
    }

    /**
     * Constructor para bloqueo completo.
     *
     * @param idAlumno ID del alumno.
     * @param matricula Matrícula institucional.
     * @param motivo Razón del bloqueo.
     */
    public BloqueoAlumnoDTO(int idAlumno, String matricula, String motivo) {
        this.idAlumno = idAlumno;
        this.matricula = matricula;
        this.motivo = motivo;
    }

    /**
     * Constructor simplificado.
     *
     * @param idAlumno ID del alumno.
     * @param motivo Razón del bloqueo.
     */
    public BloqueoAlumnoDTO(int idAlumno, String motivo) {
        this.idAlumno = idAlumno;
        this.motivo = motivo;
    }

    public int getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(int idAlumno) {
        this.idAlumno = idAlumno;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
