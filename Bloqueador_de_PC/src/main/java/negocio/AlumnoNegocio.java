/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio;

import persistencia.IAlumnoDAO;

/**
 *
 * @author rafaelgb
 */
public class AlumnoNegocio {
    private final IAlumnoDAO alumnoDAO;

    public AlumnoNegocio(IAlumnoDAO alumnoDAO) {
        this.alumnoDAO = alumnoDAO;
    }

    @Override
    public boolean validarContrasena(String idAlumno, String contrasena) throws NegocioException {
        if (contrasena == null || contrasena.isBlank()) {
            throw new NegocioException("Debe ingresar una contraseña.");
        }

        try {
            return alumnoDAO.validarContrasena(idAlumno, contrasena);
        } catch (PersistenciaException e) {
            throw new NegocioException("No se pudo validar la contraseña del alumno.", e);
        }
    }
}
