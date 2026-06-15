/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package persistencia;

import entidad.AlumnoEntidad;

/**
 *
 * @author rafaelgb
 */
public interface IAlumnoDAO {
    
    boolean validarContrasena(String idAlumno, String contrasena) throws PersistenciaException;
    
}
