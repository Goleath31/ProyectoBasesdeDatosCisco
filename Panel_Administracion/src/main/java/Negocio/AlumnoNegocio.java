/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Persistencia.IAlumnoDAO;
import Persistencia.PersistenciaException;
import dtos.AlumnoBloqueadoTablaDTO;
import dtos.AlumnoDTO;
import dtos.BloqueoAlumnoDTO;
import java.util.List;

/**
 * Implementación de la lógica de negocio para la gestión de alumnos bloqueados.
 */
public class AlumnoNegocio implements IAlumnoNegocio {

    private final IAlumnoDAO alumnoDAO;

    /**
     * Inyecta la dependencia del DAO de alumnos.
     *
     * @param alumnoDAO Implementación de acceso a datos.
     */
    public AlumnoNegocio(IAlumnoDAO alumnoDAO) {
        this.alumnoDAO = alumnoDAO;
    }

    @Override
    public boolean bloquearAlumno(BloqueoAlumnoDTO bloqueoDTO) throws NegocioException {
        try {
            // Validamos que el ID sea numérico antes de intentar cualquier operación
            Integer.parseInt(bloqueoDTO.getIdAlumno());

            // Aquí puedes agregar la llamada a existeAlumno(bloqueoDTO.getIdAlumno())
            return alumnoDAO.registrarBloqueoAlumno(bloqueoDTO);
        } catch (NumberFormatException e) {
            throw new NegocioException("El formato del ID es inválido.");
        } catch (PersistenciaException e) {
            throw new NegocioException("Error de base de datos.", e);
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
            if (totalRegistros == 0) {
                return 1;
            }
            return (int) Math.ceil((double) totalRegistros / registrosPorPagina);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error de negocio al calcular la paginación de bloqueos.", e);
        }
    }

    public int obtenerTotalBloqueados(int idLaboratorio, String criterio) throws NegocioException {
        try {
            return alumnoDAO.contarAlumnosBloqueados(idLaboratorio, criterio);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al obtener el total de bloqueados", e);
        }
    }

    @Override
    public void eliminarBloqueo(int idAlumno) throws NegocioException {
        try {
            alumnoDAO.eliminarBloqueoAlumno(idAlumno);
        } catch (PersistenciaException e) {
            throw new NegocioException("No se pudo quitar el bloqueo: " + e.getMessage());
        }
    }

    @Override
    public AlumnoDTO obtenerAlumnoPorId(int idAlumno) throws NegocioException {
        try {
            return alumnoDAO.obtenerAlumnoPorId(idAlumno);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al consultar alumno.", e);
        }
    }
}
