/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio;

/**
 *
 * @author rafaelgb
 */
public class ComputadoraNegocio implements IComputadoraNegocio{
    private final IComputadoraDAO computadoraDAO;

    public ComputadoraNegocio(IComputadoraDAO computadoraDAO) {
        this.computadoraDAO = computadoraDAO;
    }

    @Override
    public UbicacionEquipoDTO obtenerUbicacionPorIP(String direccionIp) throws NegocioException {
        if (direccionIp == null || direccionIp.isBlank()) {
            throw new NegocioException("La dirección IP del equipo no es válida.");
        }

        try {
            return computadoraDAO.obtenerUbicacionPorIP(direccionIp);
        } catch (PersistenciaException e) {
            throw new NegocioException("No se pudo obtener la ubicación del equipo.", e);
        }
    }

    @Override
    public void marcarEquipoEnUso(int idComputadora) throws NegocioException {
        try {
            computadoraDAO.actualizarEstatus(idComputadora, "En uso");
        } catch (PersistenciaException e) {
            throw new NegocioException("No se pudo actualizar el estatus del equipo.", e);
        }
    }
}
