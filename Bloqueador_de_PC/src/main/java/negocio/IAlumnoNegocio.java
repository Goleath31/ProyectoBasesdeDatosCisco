/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package negocio;

/**
 *
 * @author rafaelgb
 */
public interface IAlumnoNegocio {
    
        boolean validarContrasena(String idAlumno, String contrasena) throws NegocioException;
}
