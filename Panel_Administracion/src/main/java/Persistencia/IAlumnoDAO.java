/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import dtos.AlumnoBloqueadoTablaDTO;
import dtos.AlumnoDTO;
import dtos.BloqueoAlumnoDTO;
import java.util.List;

/**
 * Define las operaciones de persistencia permitidas para la entidad Alumno.
 *
 * * @author golea
 */
public interface IAlumnoDAO {

    /**
     * Registra el bloqueo de un alumno en el sistema.
     *
     * * @param bloqueoDTO Datos necesarios para registrar el bloqueo (id y
     * motivo).
     * @return true si el registro fue exitoso, false en caso contrario.
     * @throws PersistenciaException Si ocurre un error en la capa de
     * persistencia.
     */
    boolean registrarBloqueoAlumno(BloqueoAlumnoDTO bloqueoDTO) throws PersistenciaException;

    /**
     * Cuenta la cantidad de alumnos bloqueados que coinciden con un criterio de
     * búsqueda.
     *
     * * @param idLaboratorio Identificador del laboratorio para filtrar.
     * @param criterio Texto para buscar en nombres o motivos de bloqueo.
     * @return El total de registros encontrados.
     * @throws PersistenciaException Si ocurre un error durante la ejecución de
     * la consulta.
     */
    int contarAlumnosBloqueados(int idLaboratorio, String criterio) throws PersistenciaException;

    /**
     * Obtiene una lista paginada de alumnos bloqueados.
     *
     * * @param criterio Texto de búsqueda.
     * @param pagina Número de página deseada.
     * @param registros Cantidad de registros por página.
     * @return Una lista de objetos {@link AlumnoBloqueadoTablaDTO}.
     * @throws PersistenciaException Si la consulta falla.
     */
    public List<AlumnoBloqueadoTablaDTO> obtenerAlumnosBloqueadosGlobal(String criterio, int pagina, int registros) throws PersistenciaException;

    public boolean existeAlumno(int idAlumno) throws PersistenciaException;

    public boolean eliminarBloqueoAlumno(int idAlumno) throws PersistenciaException;

    public AlumnoDTO obtenerAlumnoPorId(int idAlumno) throws PersistenciaException;

}
