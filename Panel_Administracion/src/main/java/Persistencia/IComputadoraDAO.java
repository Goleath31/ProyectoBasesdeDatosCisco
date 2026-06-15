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
 * Define las operaciones de persistencia para la gestión de equipos
 * (computadoras).
 *
 * * @author golea
 */
public interface IComputadoraDAO {

    /**
     * Obtiene todas las computadoras vinculadas a un laboratorio específico.
     *
     * @param idLaboratorio ID del laboratorio.
     * @return Lista de {@link ComputadoraTablaDTO}.
     */
    List<ComputadoraTablaDTO> obtenerComputadorasPorLaboratorio(int idLaboratorio) throws PersistenciaException;

    /**
     * Bloquea un equipo específico dentro de un laboratorio dado su número de
     * máquina.
     *
     * @param registro Datos del equipo y laboratorio.
     */
    boolean bloquearEquipoPorNumero(BloqueoEquipoDTO registro) throws PersistenciaException;

    /**
     * Desbloquea un equipo validando una contraseña maestra asociada al
     * laboratorio.
     *
     * @param registro Datos necesarios para la validación y el desbloqueo.
     */
    boolean desbloquearEquipoConContrasena(DesbloqueoEquipoDTO registro) throws PersistenciaException;

    /**
     * Cuenta cuántos equipos están actualmente en estatus 'En uso' en un
     * laboratorio.
     */
    int obtenerContadorEquiposEnUso(int idLaboratorio) throws PersistenciaException;

    /**
     * Recupera información del laboratorio (y su contraseña) basada en la IP de
     * un equipo.
     */
    ComputadoraTablaDTO obtenerIdLaboratorioPorIP(String ip) throws PersistenciaException;

    /**
     * Obtiene el total de equipos registrados en un laboratorio.
     */
    int obtenerTotalEquiposLaboratorio(int idLaboratorio) throws PersistenciaException;

    /**
     * Filtra equipos aplicando criterios de búsqueda, estatus y paginación.
     */
    List<ComputadoraTablaDTO> filtrarComputadorasPorLaboratorio(int idLaboratorio, String criterio, String estatus, int pagina, int tamanoPagina) throws PersistenciaException;

    /**
     * Cuenta equipos que coinciden con filtros específicos para soportar la
     * paginación.
     */
    int contarComputadorasFiltradas(int idLaboratorio, String criterio, String estatus) throws PersistenciaException;

    /**
     * Desbloquea un equipo directamente usando su número de máquina.
     */
    boolean desbloquearEquipoPorNumero(int numeroMaquina, int idLaboratorio) throws PersistenciaException;
}
