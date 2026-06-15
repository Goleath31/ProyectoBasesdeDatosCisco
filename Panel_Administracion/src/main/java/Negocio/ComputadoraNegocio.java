/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

import Persistencia.IComputadoraDAO;
import Persistencia.PersistenciaException;
import dtos.BloqueoEquipoDTO;
import dtos.ComputadoraTablaDTO;
import dtos.DesbloqueoEquipoDTO;
import java.util.List;

/**
 *
 * @author golea
 */
public class ComputadoraNegocio implements IComputadoraNegocio {

    private final IComputadoraDAO computadoraDAO;

    /**
     * Constructor que recibe el DAO por inyección de dependencias.
     *
     * @param computadoraDAO el DAO de persistencia de cómputo
     */
    public ComputadoraNegocio(IComputadoraDAO computadoraDAO) {
        this.computadoraDAO = computadoraDAO;
    }

    @Override
    public List<ComputadoraTablaDTO> obtenerComputadorasPorLaboratorio(int idLaboratorio) throws NegocioException {
        // Validación del parámetro de entrada
        if (idLaboratorio <= 0) {
            throw new NegocioException("El identificador del laboratorio es inválido para la consulta.");
        }

        try {
            return computadoraDAO.obtenerComputadorasPorLaboratorio(idLaboratorio);
        } catch (PersistenciaException e) {
            // Se encapsula el error de persistencia en una excepción de negocio legible
            throw new NegocioException("Error al procesar la lista de equipos en la capa de negocio.", e);
        }
    }

    @Override
    public boolean bloquearEquipoPorNumero(BloqueoEquipoDTO dto) throws NegocioException {
        // Validaciones estrictas de reglas de negocio en base a lo capturado en pantalla
        if (dto == null) {
            throw new NegocioException("Los datos de bloqueo no pueden ser nulos.");
        }
        if (dto.getNumeroMaquina() <= 0) {
            throw new NegocioException("El número de máquina debe ser un valor entero positivo mayor a cero.");
        }
        if (dto.getIdLaboratorioActual() <= 0) {
            throw new NegocioException("No se ha detectado un entorno de laboratorio activo para procesar el bloqueo.");
        }

        try {
            boolean exito = computadoraDAO.bloquearEquipoPorNumero(dto);
            if (!exito) {
                throw new NegocioException("No se pudo aplicar el bloqueo. Verifique que la máquina número "
                        + dto.getNumeroMaquina() + " exista en este centro de cómputo.");
            }
            return true;
        } catch (PersistenciaException e) {
            throw new NegocioException("Error crítico de datos al intentar bloquear la computadora corporativa.", e);
        }
    }

    @Override
    public boolean desbloquearEquipoConContrasena(DesbloqueoEquipoDTO dto) throws NegocioException {
        // Validaciones del DTO que usará Confirmacion_Desbloqueo_Equipo
        if (dto == null) {
            throw new NegocioException("La información para realizar el desbloqueo está incompleta.");
        }
        if (dto.getIdComputadora() <= 0) {
            throw new NegocioException("No se ha seleccionado un equipo físico válido para remover la restricción.");
        }
        if (dto.getContrasenaMaestra() == null || dto.getContrasenaMaestra().trim().isEmpty()) {
            throw new NegocioException("La contraseña maestra es obligatoria para autorizar la liberación del equipo.");
        }

        try {
            boolean credencialValida = computadoraDAO.desbloquearEquipoConContrasena(dto);
            if (!credencialValida) {
                throw new NegocioException("La contraseña maestra ingresada es incorrecta. Acción de desbloqueo rechazada.");
            }
            return true;
        } catch (PersistenciaException e) {
            throw new NegocioException("Error de comunicación con el almacén de datos al procesar el desbloqueo.", e);
        }
    }

    @Override
    public int obtenerContadorEquiposEnUso(int idLaboratorio) throws NegocioException {
        if (idLaboratorio <= 0) {
            return 0;
        }
        try {
            return computadoraDAO.obtenerContadorEquiposEnUso(idLaboratorio);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al calcular las métricas de uso de los equipos en tiempo real.", e);
        }
    }

    @Override
    public ComputadoraTablaDTO obtenerIdLaboratorioPorIP(String ip) throws NegocioException {
        if (ip == null || ip.trim().isEmpty()) {
            throw new NegocioException("La dirección IP obtenida de la estación de cómputo no es válida.");
        }
        try {
            return computadoraDAO.obtenerIdLaboratorioPorIP(ip);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error en el servicio al procesar la autenticación de red del equipo.", e);
        }
    }

    @Override
    public int obtenerTotalEquiposLaboratorio(int idLaboratorio) throws NegocioException {
        if (idLaboratorio <= 0) {
            return 0;
        }
        try {
            return computadoraDAO.obtenerTotalEquiposLaboratorio(idLaboratorio);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al obtener las métricas de equipos totales.", e);
        }
    }

    @Override
    public String calcularPorcentajeOcupacion(int idLaboratorio) throws NegocioException {
        if (idLaboratorio <= 0) {
            return "0%";
        }
        try {
            int enUso = computadoraDAO.obtenerContadorEquiposEnUso(idLaboratorio);
            int totales = computadoraDAO.obtenerTotalEquiposLaboratorio(idLaboratorio);

            if (totales == 0) {
                return "0%";
            }

            double porcentaje = ((double) enUso * 100) / totales;

            return String.format("%.1f%%", porcentaje);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al calcular el porcentaje de ocupación.", e);
        }
    }

    @Override
    public List<ComputadoraTablaDTO> filtrarComputadorasPorLaboratorio(int idLaboratorio, String criterio, String estatus, int pagina, int tamanoPagina) throws NegocioException {
        if (idLaboratorio <= 0) {
            throw new NegocioException("El laboratorio no es válido.");
        }
        if (pagina < 1) {
            pagina = 1;
        }
        try {
            String critLimpio = (criterio != null) ? criterio.trim() : "";
            String estLimpio = (estatus != null) ? estatus.trim() : "";
            return computadoraDAO.filtrarComputadorasPorLaboratorio(idLaboratorio, critLimpio, estLimpio, pagina, tamanoPagina);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al procesar la lista paginada.", e);
        }
    }

    @Override
    public int calcularTotalPaginas(int idLaboratorio, String criterio, String estatus, int tamanoPagina) throws NegocioException {
        if (idLaboratorio <= 0) {
            return 1;
        }
        try {
            String critLimpio = (criterio != null) ? criterio.trim() : "";
            String estLimpio = (estatus != null) ? estatus.trim() : "";

            int totalRegistros = computadoraDAO.contarComputadorasFiltradas(idLaboratorio, critLimpio, estLimpio);

            if (totalRegistros == 0) {
                return 1;
            }
            return (int) Math.ceil((double) totalRegistros / tamanoPagina);
        } catch (PersistenciaException e) {
            throw new NegocioException("Error al calcular la paginación.", e);
        }
    }

    @Override
    public boolean desbloquearEquipoPorNumero(int numeroMaquina, int idLaboratorio) throws NegocioException {
        if (numeroMaquina <= 0) {
            throw new NegocioException("El número de máquina no es válido.");
        }
        if (idLaboratorio <= 0) {
            throw new NegocioException("El laboratorio no es válido.");
        }
        try {
            // Ejecutamos directamente el cambio en el DAO
            boolean exito = computadoraDAO.desbloquearEquipoPorNumero(numeroMaquina, idLaboratorio);
            if (!exito) {
                throw new NegocioException("No se encontró el equipo o no pertenece a este laboratorio.");
            }
            return exito;
        } catch (PersistenciaException e) {
            throw new NegocioException("Error de negocio al intentar desbloquear el equipo.", e);
        }
    }
}
