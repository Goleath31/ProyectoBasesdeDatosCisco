/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidad;

/**
 *
 * @author golea
 */
public class CarreraEntidad {
    private int idCarrera;
    private String nombre;
    private int tiempoDiarioLimite;

    public CarreraEntidad() {
    }

    public int getIdCarrera() {
        return idCarrera;
    }

    public void setIdCarrera(int idCarrera) {
        this.idCarrera = idCarrera;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTiempoDiarioLimite() {
        return tiempoDiarioLimite;
    }

    public void setTiempoDiarioLimite(int tiempoDiarioLimite) {
        this.tiempoDiarioLimite = tiempoDiarioLimite;
    }
}
