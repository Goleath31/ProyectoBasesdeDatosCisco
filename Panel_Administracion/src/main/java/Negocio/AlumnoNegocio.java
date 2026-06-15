/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Persistencia.IAlumnoDAO;
import Persistencia.PersistenciaException;
import dtos.AlumnoBloqueadoTablaDTO;
import dtos.BloqueoAlumnoDTO;
import java.util.List;
/**
 * Implementación de la lógica de negocio para la gestión de alumnos bloqueados.
 */
public class AlumnoNegocio implements IAlumnoNegocio {

    private final IAlumnoDAO alumnoDAO;

    /**
     * Inyecta la dependencia del DAO de alumnos.
     * @param alumnoDAO Implementación de acceso a datos.
     */
    public AlumnoNegocio(IAlumnoDAO alumnoDAO) {
        this.alumnoDAO = alumnoDAO;
    }

    @Override
    public boolean bloquearAlumno(BloqueoAlumnoDTO bloqueoDTO) throws NegocioException {
        // Valida que los campos obligatorios no sean nulos o vacíos
        if (bloqueoDTO.getMatricula() == null || bloqueoDTO.getMatricula().trim().isEmpty()) {
            throw new NegocioException("La matrícula del alumno es obligatoria.");
        }
        if (bloqueoDTO.getMotivo() == null || bloqueoDTO.getMotivo().trim().isEmpty()) {
            throw new NegocioException("Debe especificar un motivo para el bloqueo.");
        }

        try {
            return alumnoDAO.registrarBloqueoAlumno(bloqueoDTO);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error en las reglas de negocio al bloquear alumno.", e);
        }
    }

    @Override
    public List<AlumnoBloqueadoTablaDTO> obtenerAlumnosBloqueadosPaginados(String criterio, int pagina, int registros) throws NegocioException {
        try {
            return alumnoDAO.obtenerAlumnosBloqueadosGlobal(criterio, pagina, registros);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al cargar la lista global: " + e.getMessage());
        }
    }

    @Override
    public int calcularTotalPaginas(int idLaboratorio, String criterio, int registrosPorPagina) throws NegocioException {
        if (idLaboratorio <= 0 || registrosPorPagina <= 0) {
            return 1;
        }
        try {
            int totalRegistros = alumnoDAO.contarAlumnosBloqueados(idLaboratorio, criterio);
            if (totalRegistros == 0) return 1;
            return (int) Math.ceil((double) totalRegistros / registrosPorPagina);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error de negocio al calcular la paginación de bloqueos.", e);
        }
    }
}
