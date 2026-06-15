/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import dtos.BloqueoEquipoDTO;
import dtos.ComputadoraTablaDTO;
import dtos.DesbloqueoEquipoDTO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de acceso a datos para la entidad Computadora. Proporciona
 * métodos para gestión de estados, filtrado y desbloqueo de equipos.
 *
 * * @author golea
 */
public class ComputadoraDAO implements IComputadoraDAO {

    private IConexionBD conexion;

    /**
     * Constructor que inyecta la dependencia de conexión.
     *
     * @param conexion Objeto que implementa {@link IConexionBD}.
     */
    public ComputadoraDAO(IConexionBD conexion) {
        this.conexion = conexion;
    }

    /**
     * Consulta todas las computadoras asociadas a un laboratorio específico.
     *
     * * @param idLaboratorio ID del laboratorio a consultar.
     * @return Lista de objetos {@link ComputadoraTablaDTO}.
     * @throws PersistenciaException Si la consulta falla.
     */
    @Override
    public List<ComputadoraTablaDTO> obtenerComputadorasPorLaboratorio(int idLaboratorio) throws PersistenciaException {
        String sentenciaSQL = "SELECT id_computadora, numero_maquina, direccion_ip, estatus FROM Computadora WHERE id_laboratorio = ?;";
        List<ComputadoraTablaDTO> lista = new ArrayList<>();

        try (Connection conn = this.conexion.crearConexion(); PreparedStatement comando = conn.prepareStatement(sentenciaSQL)) {

            comando.setInt(1, idLaboratorio);

            try (ResultSet resultado = comando.executeQuery()) {
                while (resultado.next()) {
                    ComputadoraTablaDTO dto = new ComputadoraTablaDTO();
                    dto.setIdComputadora(resultado.getInt("id_computadora"));
                    dto.setNumeroMaquina(resultado.getInt("numero_maquina"));
                    dto.setDireccionIp(resultado.getString("direccion_ip"));
                    dto.setEstatus(resultado.getString("estatus"));
                    lista.add(dto);
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new PersistenciaException("Error al consultar las computadoras del laboratorio: " + e.getMessage());
        }
    }

    /**
     * Cambia el estatus de una computadora a 'Bloqueado'.
     *
     * * @param registro DTO con el número de máquina y el ID del laboratorio.
     * @return true si la operación fue exitosa, false si no se encontró el
     * equipo.
     * @throws PersistenciaException Si hay errores de transacción.
     */
    @Override
    public boolean bloquearEquipoPorNumero(BloqueoEquipoDTO registro) throws PersistenciaException {
        String sentenciaSQL = "UPDATE Computadora SET estatus = 'Bloqueado' WHERE numero_maquina = ? AND id_laboratorio = ?;";

        try (Connection conn = this.conexion.crearConexion(); PreparedStatement comando = conn.prepareStatement(sentenciaSQL)) {

            conn.setAutoCommit(false);
            try {
                comando.setInt(1, registro.getNumeroMaquina());
                comando.setInt(2, registro.getIdLaboratorioActual());

                int filasAfectadas = comando.executeUpdate();
                conn.commit();

                return filasAfectadas > 0;
            } catch (SQLException e) {
                conn.rollback();
                throw new PersistenciaException("Error en la transacción al bloquear el equipo: " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error de conexión al intentar bloquear el equipo: " + e.getMessage());
        }
    }

    /**
     * Valida una contraseña maestra contra el laboratorio asociado antes de
     * desbloquear el equipo. Utiliza una transacción para asegurar la
     * integridad de la validación.
     *
     * * @param registro DTO con el ID de la computadora y la contraseña
     * ingresada.
     * @return true si la contraseña es correcta y el equipo se desbloqueó.
     * @throws PersistenciaException En caso de fallo en la base de datos.
     */
    @Override
    public boolean desbloquearEquipoConContrasena(DesbloqueoEquipoDTO registro) throws PersistenciaException {
        // Primero verificamos que la contraseña ingresada pertenezca a la contraseña_maestra de la tabla Laboratorio asociada
        String consultaSQL = "SELECT L.contrasena_maestra FROM Computadora C "
                + "JOIN Laboratorio L ON C.id_laboratorio = L.id_laboratorio "
                + "WHERE C.id_computadora = ?;";

        String actualizacionSQL = "UPDATE Computadora SET estatus = 'Disponible' WHERE id_computadora = ?;";

        try (Connection conn = this.conexion.crearConexion()) {
            conn.setAutoCommit(false);
            try {
                // Step 1: Validar Contraseña Maestra del Centro
                try (PreparedStatement comandoConsulta = conn.prepareStatement(consultaSQL)) {
                    comandoConsulta.setInt(1, registro.getIdComputadora());
                    try (ResultSet rs = comandoConsulta.executeQuery()) {
                        if (rs.next()) {
                            String passMaestraDB = rs.getString("contrasena_maestra");
                            if (!passMaestraDB.equals(registro.getContrasenaMaestra())) {
                                conn.rollback();
                                return false; // Contraseña inválida
                            }
                        } else {
                            conn.rollback();
                            return false;
                        }
                    }
                }

                try (PreparedStatement comandoActualizar = conn.prepareStatement(actualizacionSQL)) {
                    comandoActualizar.setInt(1, registro.getIdComputadora());
                    comandoActualizar.executeUpdate();
                }

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw new PersistenciaException("Error en la transacción al desbloquear el equipo: " + e.getMessage());
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Error de conexión al intentar desbloquear el equipo: " + e.getMessage());
        }
    }

    /**
     * Obtiene el número total de computadoras que tienen el estatus 'En uso'.
     *
     * @param idLaboratorio ID del laboratorio.
     * @return Cantidad de equipos en uso.
     * @throws PersistenciaException Si ocurre un error.
     */
    @Override
    public int obtenerContadorEquiposEnUso(int idLaboratorio) throws PersistenciaException {
        String sentenciaSQL = "SELECT COUNT(*) FROM Computadora WHERE id_laboratorio = ? AND estatus = 'En uso';";

        try (Connection conn = this.conexion.crearConexion(); PreparedStatement comando = conn.prepareStatement(sentenciaSQL)) {

            comando.setInt(1, idLaboratorio);

            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return resultado.getInt(1);
                }
            }
            return 0;
        } catch (SQLException e) {
            throw new PersistenciaException("Error al contar los equipos en uso: " + e.getMessage());
        }
    }

    /**
     * Busca información de un laboratorio a partir de la dirección IP de una
     * computadora.
     *
     * * @param ip Dirección IP del equipo.
     * @return DTO con la información del laboratorio asociado o null si no se
     * encuentra.
     * @throws PersistenciaException Si la búsqueda falla.
     */
    @Override
    public ComputadoraTablaDTO obtenerIdLaboratorioPorIP(String ip) throws PersistenciaException {
        String sentenciaSQL = "SELECT l.id_laboratorio, l.nombre, l.contrasena_maestra "
                + "FROM Laboratorio l "
                + "JOIN Computadora c ON l.id_laboratorio = c.id_laboratorio "
                + "WHERE c.direccion_ip = ? LIMIT 1;";

        try (Connection conn = this.conexion.crearConexion(); PreparedStatement comando = conn.prepareStatement(sentenciaSQL)) {

            comando.setString(1, ip);

            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    ComputadoraTablaDTO labInfo = new ComputadoraTablaDTO();
                    labInfo.setIdComputadora(resultado.getInt("id_laboratorio")); // Guardamos temporalmente el ID aquí
                    labInfo.setDireccionIp(resultado.getString("contrasena_maestra")); // Guardamos la contraseña temporalmente aquí
                    labInfo.setEstatus(resultado.getString("nombre"));          // Guardamos temporalmente el Nombre aquí
                    return labInfo;
                }
            }
            return null;
        } catch (SQLException e) {
            throw new PersistenciaException("Error al buscar el laboratorio por la IP del equipo: " + e.getMessage());
        }
    }

    /**
     * Filtra computadoras por laboratorio, criterio de texto y estatus
     * utilizando paginación.
     *
     * * @param idLaboratorio ID del laboratorio.
     * @param criterio Filtro de búsqueda (IP o número de máquina).
     * @param estatus Estatus a filtrar ('Disponible', 'Bloqueado', etc.).
     * @param pagina Página actual.
     * @param tamanoPagina Registros por página.
     * @return Lista paginada de {@link ComputadoraTablaDTO}.
     * @throws PersistenciaException Si ocurre un error SQL.
     */
    @Override
    public int obtenerTotalEquiposLaboratorio(int idLaboratorio) throws PersistenciaException {
        String sentenciaSQL = "SELECT COUNT(*) FROM Computadora WHERE id_laboratorio = ?;";
        try (Connection conn = this.conexion.crearConexion(); PreparedStatement comando = conn.prepareStatement(sentenciaSQL)) {
            comando.setInt(1, idLaboratorio);
            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return resultado.getInt(1);
                }
            }
            return 0;
        } catch (SQLException e) {
            throw new PersistenciaException("Error al contar los equipos totales: " + e.getMessage());
        }
    }

    /**
     * Filtra computadoras por laboratorio, criterio de texto y estatus
     * utilizando paginación.
     *
     * * @param idLaboratorio ID del laboratorio.
     * @param criterio Filtro de búsqueda (IP o número de máquina).
     * @param estatus Estatus a filtrar ('Disponible', 'Bloqueado', etc.).
     * @param pagina Página actual.
     * @param tamanoPagina Registros por página.
     * @return Lista paginada de {@link ComputadoraTablaDTO}.
     * @throws PersistenciaException Si ocurre un error SQL.
     */
    @Override
    public List<ComputadoraTablaDTO> filtrarComputadorasPorLaboratorio(int idLaboratorio, String criterio, String estatus, int pagina, int tamanoPagina) throws PersistenciaException {
        List<ComputadoraTablaDTO> lista = new ArrayList<>();

        StringBuilder sentenciaSQL = new StringBuilder(
                "SELECT id_computadora, numero_maquina, direccion_ip, estatus "
                + "FROM Computadora "
                + "WHERE id_laboratorio = ? "
        );

        // Filtro por texto (IP o Número)
        boolean tieneCriterio = (criterio != null && !criterio.trim().isEmpty());
        if (tieneCriterio) {
            sentenciaSQL.append("AND (numero_maquina = ? OR direccion_ip LIKE ?) ");
        }

        // REGLA: Solo filtra si es diferente de vacío, nulo o "Todos". Si es "Todos", entran todos los estatus.
        boolean tieneEstatus = (estatus != null && !estatus.trim().isEmpty() && !estatus.equalsIgnoreCase("Todos"));
        if (tieneEstatus) {
            sentenciaSQL.append("AND estatus = ? ");
        }

        // Paginación limpia en SQL
        sentenciaSQL.append("LIMIT ? OFFSET ?;");

        try (Connection conn = this.conexion.crearConexion(); PreparedStatement comando = conn.prepareStatement(sentenciaSQL.toString())) {

            int indiceParametro = 1;
            comando.setInt(indiceParametro++, idLaboratorio);

            if (tieneCriterio) {
                int numeroMaquina = -1;
                try {
                    numeroMaquina = Integer.parseInt(criterio.trim());
                } catch (NumberFormatException e) {
                }
                comando.setInt(indiceParametro++, numeroMaquina);
                comando.setString(indiceParametro++, "%" + criterio.trim() + "%");
            }

            if (tieneEstatus) {
                comando.setString(indiceParametro++, estatus.trim());
            }

            int offset = (pagina - 1) * tamanoPagina;
            comando.setInt(indiceParametro++, tamanoPagina);
            comando.setInt(indiceParametro++, offset);

            try (ResultSet resultado = comando.executeQuery()) {
                while (resultado.next()) {
                    ComputadoraTablaDTO dto = new ComputadoraTablaDTO();
                    dto.setIdComputadora(resultado.getInt("id_computadora"));
                    dto.setNumeroMaquina(resultado.getInt("numero_maquina"));
                    dto.setDireccionIp(resultado.getString("direccion_ip"));
                    dto.setEstatus(resultado.getString("estatus"));
                    lista.add(dto);
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new PersistenciaException("Error al paginar los equipos: " + e.getMessage());
        }
    }

    /**
     * Calcula el total de registros de computadoras para fines de paginación.
     *
     * * @return Total de registros que coinciden con los filtros aplicados.
     * @throws PersistenciaException Si falla el conteo.
     */
    @Override
    public int contarComputadorasFiltradas(int idLaboratorio, String criterio, String estatus) throws PersistenciaException {
        StringBuilder sentenciaSQL = new StringBuilder(
                "SELECT COUNT(*) FROM Computadora WHERE id_laboratorio = ? "
        );

        boolean tieneCriterio = (criterio != null && !criterio.trim().isEmpty());
        if (tieneCriterio) {
            sentenciaSQL.append("AND (numero_maquina = ? OR direccion_ip LIKE ?) ");
        }

        boolean tieneEstatus = (estatus != null && !estatus.trim().isEmpty() && !estatus.equalsIgnoreCase("Todos"));
        if (tieneEstatus) {
            sentenciaSQL.append("AND estatus = ? ");
        }

        try (Connection conn = this.conexion.crearConexion(); PreparedStatement comando = conn.prepareStatement(sentenciaSQL.toString())) {

            int indiceParametro = 1;
            comando.setInt(indiceParametro++, idLaboratorio);

            if (tieneCriterio) {
                int numeroMaquina = -1;
                try {
                    numeroMaquina = Integer.parseInt(criterio.trim());
                } catch (NumberFormatException e) {
                }
                comando.setInt(indiceParametro++, numeroMaquina);
                comando.setString(indiceParametro++, "%" + criterio.trim() + "%");
            }

            if (tieneEstatus) {
                comando.setString(indiceParametro++, estatus.trim());
            }

            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return resultado.getInt(1);
                }
            }
            return 0;
        } catch (SQLException e) {
            throw new PersistenciaException("Error al contar los registros de paginación: " + e.getMessage());
        }
    }

    /**
     * Desbloquea un equipo directamente por su número de máquina.
     *
     * * @param numeroMaquina Número de máquina a desbloquear.
     * @param idLaboratorio ID del laboratorio.
     * @return true si la operación afectó al registro.
     * @throws PersistenciaException Si falla la actualización.
     */
    @Override
    public boolean desbloquearEquipoPorNumero(int numeroMaquina, int idLaboratorio) throws PersistenciaException {
        // Cambiamos el estatus a 'Disponible' que es el estado inicial normal de tus equipos
        String sentenciaSQL = "UPDATE Computadora SET estatus = 'Disponible' WHERE numero_maquina = ? AND id_laboratorio = ?;";

        try (Connection conn = this.conexion.crearConexion(); PreparedStatement comando = conn.prepareStatement(sentenciaSQL)) {

            comando.setInt(1, numeroMaquina);
            comando.setInt(2, idLaboratorio);

            int filasAfectadas = comando.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            throw new PersistenciaException("Error al desbloquear el equipo en la base de datos: " + e.getMessage());
        }
    }
}
