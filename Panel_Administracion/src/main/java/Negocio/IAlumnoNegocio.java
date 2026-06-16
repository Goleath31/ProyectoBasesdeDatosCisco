/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Negocio;

import dtos.AlumnoBloqueadoTablaDTO;
import dtos.BloqueoAlumnoDTO;
import java.util.List;

/**
 * Define las operaciones de negocio relacionadas con el bloqueo y gestión de
 * alumnos.
 */
public interface IAlumnoNegocio {

    /**
     * Registra el bloqueo de un alumno tras validar sus datos.
     *
     * @param bloqueoDTO Datos del alumno a bloquear.
     * @return true si el bloqueo fue exitoso.
     * @throws NegocioException Si la validación falla o hay error en
     * persistencia.
     */
    boolean bloquearAlumno(BloqueoAlumnoDTO bloqueoDTO) throws NegocioException;

    /**
     * Obtiene una lista paginada de alumnos bloqueados según un criterio de
     * búsqueda.
     *
     * @param criterio Filtro de búsqueda (nombre, matrícula, etc.).
     * @param pagina Número de página actual.
     * @param registros Cantidad de registros por página.
     * @return Lista de alumnos bloqueados.
     * @throws NegocioException Si ocurre un error al consultar los datos.
     */
    List<AlumnoBloqueadoTablaDTO> obtenerAlumnosBloqueadosPaginados(String criterio, int pagina, int registros) throws NegocioException;

    /**
     * Calcula el total de páginas necesarias para la paginación de alumnos
     * bloqueados.
     *
     * @param idLaboratorio Identificador del laboratorio.
     * @param criterio Filtro aplicado.
     * @param registrosPorPagina Cantidad de registros definidos por página.
     * @return Total de páginas calculadas.
     * @throws NegocioException Si hay error en el cálculo.
     */
    int calcularTotalPaginas(int idLaboratorio, String criterio, int registrosPorPagina) throws NegocioException;
    int obtenerTotalBloqueados(int idLaboratorio, String criterio) throws NegocioException;
}
