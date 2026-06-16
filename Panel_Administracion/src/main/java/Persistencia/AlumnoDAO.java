/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import dtos.AlumnoBloqueadoTablaDTO;
import dtos.BloqueoAlumnoDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de acceso a datos para la entidad Alumno. Gestiona el registro
 * y consulta de bloqueos de alumnos en el sistema.
 *
 * * @author golea
 */
public class AlumnoDAO implements IAlumnoDAO {

    private final IConexionBD conexion;

    /**
     * Constructor que inyecta la dependencia de conexión.
     *
     * @param conexion Objeto que implementa {@link IConexionBD}.
     */
    public AlumnoDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

    /**
     * Actualiza el estatus del alumno a 'Bloqueado' e inserta un registro en la
     * tabla Bloqueo de forma transaccional.
     *
     * @param bloqueoDTO DTO con la información necesaria (idAlumno y motivo).
     * @return true si ambos procesos se ejecutaron exitosamente.
     * @throws PersistenciaException Si ocurre un error en la base de datos.
     */
    @Override
    public boolean registrarBloqueoAlumno(BloqueoAlumnoDTO dto) throws PersistenciaException {
        String sql = "INSERT INTO Bloqueo (id_alumno, motivo, fecha_bloqueo) VALUES (?, ?, ?)";
        try (Connection conn = this.conexion.crearConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dto.getIdAlumno()); // Enviamos el String "0000024006"
            ps.setString(2, dto.getMotivo());
            ps.setDate(3, dto.getFecha());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new PersistenciaException("Error al registrar: " + e.getMessage());
        }
    }

    /**
     * Obtiene una lista paginada de alumnos bloqueados realizando un JOIN con
     * la tabla de Bloqueos.
     *
     * * @param criterio Filtro de búsqueda por nombre de alumno o motivo.
     * @param pagina Número de página actual para la paginación.
     * @param registros Cantidad de registros por página.
     * @return Lista de {@link AlumnoBloqueadoTablaDTO}.
     * @throws PersistenciaException Si ocurre un error durante la consulta.
     */
    @Override
    public List<AlumnoBloqueadoTablaDTO> obtenerAlumnosBloqueadosGlobal(String criterio, int pagina, int registros) throws PersistenciaException {
        List<AlumnoBloqueadoTablaDTO> lista = new ArrayList<>();

        // Tu consulta con JOIN
        String sql = "SELECT b.id_bloqueo, b.fecha_bloqueo, b.motivo, b.id_alumno, a.nombre AS nombre_alumno "
                + "FROM Bloqueo b "
                + "JOIN Alumno a ON b.id_alumno = a.id "
                + "WHERE a.nombre LIKE ? OR b.motivo LIKE ? "
                + "LIMIT ?, ?;";

        String buscarPattern = "%" + (criterio == null ? "" : criterio) + "%";

        try (Connection conn = this.conexion.crearConexion(); PreparedStatement comando = conn.prepareStatement(sql)) {

            comando.setString(1, buscarPattern);
            comando.setString(2, buscarPattern);
            comando.setInt(3, (pagina - 1) * registros); // Cálculo para el OFFSET
            comando.setInt(4, registros);

            try (ResultSet rs = comando.executeQuery()) {
                while (rs.next()) {
                    lista.add(new AlumnoBloqueadoTablaDTO(
                            rs.getInt("id_alumno"),
                            rs.getString("nombre_alumno"),
                            rs.getString("motivo"),
                            rs.getString("fecha_bloqueo")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al consultar bloqueos con JOIN: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Cuenta el total de alumnos bloqueados que coinciden con un criterio.
     *
     * * @param idLaboratorio Identificador del laboratorio (filtro adicional).
     * @param criterio Filtro de texto para la búsqueda.
     * @return Total de registros encontrados.
     * @throws PersistenciaException Si ocurre un error en la base de datos.
     */
    @Override
    public int contarAlumnosBloqueados(int idLaboratorio, String criterio) throws PersistenciaException {

        String sql = "SELECT COUNT(*) FROM Bloqueo b "
                + "JOIN Alumno a ON b.id_alumno = a.id "
                + "WHERE a.nombre LIKE ? OR b.motivo LIKE ?;";

        String buscarPattern = "%" + (criterio == null ? "" : criterio) + "%";

        try (Connection conn = this.conexion.crearConexion(); PreparedStatement comando = conn.prepareStatement(sql)) {
            comando.setString(1, buscarPattern);
            comando.setString(2, buscarPattern);

            try (ResultSet rs = comando.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al contar: " + e.getMessage());
        }
        return 0;
    }

    public boolean existeAlumno(int idAlumno) throws PersistenciaException {
        String sql = "SELECT COUNT(*) FROM Alumno WHERE id = ?";
        try (Connection conn = this.conexion.crearConexion(); PreparedStatement comando = conn.prepareStatement(sql)) {

            comando.setInt(1, idAlumno);
            try (ResultSet rs = comando.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error al verificar existencia del alumno: " + e.getMessage());
        }
        return false;
    }

}
