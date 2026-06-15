/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase encargada de gestionar la conexión con la base de datos MySQL.
 * Implementa la interfaz {@link IConexionBD}.
 *
 * * @author golea
 */
public class ConexionBD implements IConexionBD {

    final String SERVER = "localhost";
    final String BASE_DATOS = "itson_laboratorios";
    private final String CADENA_CONEXION = "jdbc:mysql://" + SERVER + "/" + BASE_DATOS;
    final String USUARIO = "root";
    final String CONTRASEÑA = "goleath9090";

    /**
     * Establece una nueva conexión con la base de datos configurada.
     *
     * * @return Un objeto {@link java.sql.Connection} activo.
     * @throws java.sql.SQLException Si ocurre un error al intentar conectar con
     * el servidor.
     */
    @Override
    public Connection crearConexion() throws SQLException {
        Connection conexion = DriverManager.getConnection(CADENA_CONEXION, USUARIO, CONTRASEÑA);
        return conexion;
    }
}
