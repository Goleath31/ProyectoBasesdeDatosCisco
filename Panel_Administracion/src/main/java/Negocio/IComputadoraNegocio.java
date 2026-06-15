/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package Negocio;

import dtos.BloqueoEquipoDTO;
import dtos.ComputadoraTablaDTO;
import dtos.DesbloqueoEquipoDTO;
import java.util.List;

/**
 *
 * @author golea
 */
public interface IComputadoraNegocio {

    List<ComputadoraTablaDTO> obtenerComputadorasPorLaboratorio(int idLaboratorio) throws NegocioException;

    boolean bloquearEquipoPorNumero(BloqueoEquipoDTO dto) throws NegocioException;

    boolean desbloquearEquipoConContrasena(DesbloqueoEquipoDTO dto) throws NegocioException;

    int obtenerContadorEquiposEnUso(int idLaboratorio) throws NegocioException;

    ComputadoraTablaDTO obtenerIdLaboratorioPorIP(String ip) throws NegocioException;

    int obtenerTotalEquiposLaboratorio(int idLaboratorio) throws NegocioException;

    String calcularPorcentajeOcupacion(int idLaboratorio) throws NegocioException;

    List<ComputadoraTablaDTO> filtrarComputadorasPorLaboratorio(int idLaboratorio, String criterio, String estatus, int pagina, int tamanoPagina) throws NegocioException;

    int calcularTotalPaginas(int idLaboratorio, String criterio, String estatus, int tamanoPagina) throws NegocioException;
    
    boolean desbloquearEquipoPorNumero(int numeroMaquina, int idLaboratorio) throws NegocioException;
}
