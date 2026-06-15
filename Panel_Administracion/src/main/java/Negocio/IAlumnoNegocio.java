/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Negocio;

import dtos.AlumnoBloqueadoTablaDTO;
import dtos.BloqueoAlumnoDTO;
import java.util.List;

/**
 *
 * @author golea
 */
public interface IAlumnoNegocio {

    boolean bloquearAlumno(BloqueoAlumnoDTO bloqueoDTO) throws NegocioException;

    public List<AlumnoBloqueadoTablaDTO> obtenerAlumnosBloqueadosPaginados(String criterio, int pagina, int registros) throws NegocioException;

    int calcularTotalPaginas(int idLaboratorio, String criterio, int registrosPorPagina) throws NegocioException;
}
