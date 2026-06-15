/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

import entidad.AlumnoEntidad;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author rafaelgb
 */
public class AlumnoDAO implements IAlumnoDAO{

   private final IConexionBD conexion;

    public AlumnoDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

    @Override
    public boolean validarContrasena(String idAlumno, String contrasena) throws PersistenciaException {
        String sentenciaSQL = "SELECT contrasena FROM Alumno WHERE id = ?;";

        try (Connection conn = conexion.crearConexion();
                PreparedStatement comando = conn.prepareStatement(sentenciaSQL)) {

            comando.setString(1, idAlumno);

            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return resultado.getString("contrasena").equals(contrasena);
                }
                return false;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al validar la contraseña del alumno: " + e.getMessage(), e);
        }
    }
}
