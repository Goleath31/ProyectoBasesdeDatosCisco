/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package persistencia;

/**
 *
 * @author rafaelgb
 */
public class ComputadoraDAO {
    
     private final IConexionBD conexion;

    public ComputadoraDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

    @Override
    public UbicacionEquipoDTO obtenerUbicacionPorIP(String direccionIp) throws PersistenciaException {
        String sentenciaSQL = "SELECT c.id_computadora, c.numero_maquina, c.estatus, "
                + "l.id_laboratorio, l.nombre "
                + "FROM Computadora c "
                + "JOIN Laboratorio l ON c.id_laboratorio = l.id_laboratorio "
                + "WHERE c.direccion_ip = ? LIMIT 1;";

        try (Connection conn = conexion.crearConexion();
                PreparedStatement comando = conn.prepareStatement(sentenciaSQL)) {

            comando.setString(1, direccionIp);

            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return new UbicacionEquipoDTO(
                            resultado.getInt("id_computadora"),
                            resultado.getInt("numero_maquina"),
                            direccionIp,
                            resultado.getString("estatus"),
                            resultado.getInt("id_laboratorio"),
                            resultado.getString("nombre"));
                }
                return null;
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al obtener la ubicación del equipo: " + e.getMessage(), e);
        }
    }

    @Override
    public void actualizarEstatus(int idComputadora, String estatus) throws PersistenciaException {
        String sentenciaSQL = "UPDATE Computadora SET estatus = ? WHERE id_computadora = ?;";

        try (Connection conn = conexion.crearConexion();
                PreparedStatement comando = conn.prepareStatement(sentenciaSQL)) {

            comando.setString(1, estatus);
            comando.setInt(2, idComputadora);
            comando.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Error al actualizar el estatus del equipo: " + e.getMessage(), e);
        }
    }
}
