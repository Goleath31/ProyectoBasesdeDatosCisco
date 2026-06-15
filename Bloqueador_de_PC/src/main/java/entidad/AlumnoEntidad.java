/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entidad;

/**
 *
 * @author rafaelgb
 */
public class AlumnoEntidad {
    private int id;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String estatusInscripcion;
    private String contrasena;
    private int idCarrera;

    public AlumnoEntidad(int id, String nombre, String apellidoPaterno, String apellidoMaterno, String estatusInscripcion, String contrasena, int idCarrera) {
        this.id = id;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.estatusInscripcion = estatusInscripcion;
        this.contrasena = contrasena;
        this.idCarrera = idCarrera;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getEstatusInscripcion() {
        return estatusInscripcion;
    }

    public void setEstatusInscripcion(String estatusInscripcion) {
        this.estatusInscripcion = estatusInscripcion;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public int getIdCarrera() {
        return idCarrera;
    }

    public void setIdCarrera(int idCarrera) {
        this.idCarrera = idCarrera;
    }

    @Override
    public String toString() {
        return "AlumnoEntidad{" + "id=" + id + ", nombre=" + nombre + ", apellidoPaterno=" + apellidoPaterno + ", apellidoMaterno=" + apellidoMaterno + ", estatusInscripcion=" + estatusInscripcion + ", contrasena=" + contrasena + ", idCarrera=" + idCarrera + '}';
    }
    
    
    
}
