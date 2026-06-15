/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Persistencia;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Define el contrato para la gestión de la conexión con la base de datos.
 * Proporciona una abstracción para que las clases DAO no dependan de una
 * implementación concreta.
 *
 * * @author golea
 */
public interface IConexionBD {

    /**
     * Crea y devuelve una nueva conexión a la base de datos.
     *
     * * @return Un objeto {@link java.sql.Connection} activo.
     * @throws java.sql.SQLException Si ocurre un error al intentar establecer
     * la conexión.
     */
    public Connection crearConexion() throws SQLException;
}
