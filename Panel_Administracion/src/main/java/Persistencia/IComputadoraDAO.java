/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Persistencia;

import dtos.BloqueoEquipoDTO;
import dtos.ComputadoraTablaDTO;
import dtos.DesbloqueoEquipoDTO;
import java.util.List;

/**
 *
 * @author golea
 */
public interface IComputadoraDAO {

    List<ComputadoraTablaDTO> obtenerComputadorasPorLaboratorio(int idLaboratorio) throws PersistenciaException;

    boolean bloquearEquipoPorNumero(BloqueoEquipoDTO registro) throws PersistenciaException;

    boolean desbloquearEquipoConContrasena(DesbloqueoEquipoDTO registro) throws PersistenciaException;

    int obtenerContadorEquiposEnUso(int idLaboratorio) throws PersistenciaException;

    ComputadoraTablaDTO obtenerIdLaboratorioPorIP(String ip) throws PersistenciaException;

    int obtenerTotalEquiposLaboratorio(int idLaboratorio) throws PersistenciaException;

    List<ComputadoraTablaDTO> filtrarComputadorasPorLaboratorio(int idLaboratorio, String criterio, String estatus, int pagina, int tamanoPagina) throws PersistenciaException;

    int contarComputadorasFiltradas(int idLaboratorio, String criterio, String estatus) throws PersistenciaException;
    
    boolean desbloquearEquipoPorNumero(int numeroMaquina, int idLaboratorio) throws PersistenciaException;
}
