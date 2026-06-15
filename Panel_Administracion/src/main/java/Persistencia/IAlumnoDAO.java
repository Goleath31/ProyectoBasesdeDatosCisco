/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Persistencia;

import dtos.AlumnoBloqueadoTablaDTO;
import dtos.BloqueoAlumnoDTO;
import java.util.List;

/**
 *
 * @author golea
 */
public interface IAlumnoDAO {

    boolean registrarBloqueoAlumno(BloqueoAlumnoDTO bloqueoDTO) throws PersistenciaException;

    int contarAlumnosBloqueados(int idLaboratorio, String criterio) throws PersistenciaException;

    public List<AlumnoBloqueadoTablaDTO> obtenerAlumnosBloqueadosGlobal(String criterio, int pagina, int registros) throws PersistenciaException;
}
