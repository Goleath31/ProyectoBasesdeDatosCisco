/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtos;

/**
 *
 * @author golea
 */
public class BloqueoAlumnoDTO {

    private int idAlumno;
    private String matricula;
    private String motivo;

    public BloqueoAlumnoDTO() {
    }

    public BloqueoAlumnoDTO(int idAlumno, String matricula, String motivo) {
        this.idAlumno = idAlumno;
        this.matricula = matricula;
        this.motivo = motivo;
    }

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
