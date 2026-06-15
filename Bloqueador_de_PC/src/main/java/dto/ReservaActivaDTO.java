/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author rafaelgb
 */
public class ReservaActivaDTO {
    private int idReserva;
    private String idAlumno;
    private String nombreAlumno;

    public ReservaActivaDTO() {
    }

    public ReservaActivaDTO(int idReserva, String idAlumno, String nombreAlumno) {
        this.idReserva = idReserva;
        this.idAlumno = idAlumno;
        this.nombreAlumno = nombreAlumno;
    }

    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public String getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(String idAlumno) {
        this.idAlumno = idAlumno;
    }

    public String getNombreAlumno() {
        return nombreAlumno;
    }

    public void setNombreAlumno(String nombreAlumno) {
        this.nombreAlumno = nombreAlumno;
    }
}
