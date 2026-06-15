/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package negocio;

/**
 *
 * @author rafaelgb
 */
public class ReservaNegocio {
    private final IReservaDAO reservaDAO;

    public ReservaNegocio(IReservaDAO reservaDAO) {
        this.reservaDAO = reservaDAO;
    }

    @Override
    public ReservaActivaDTO obtenerReservaActivaPorComputadora(int idComputadora) throws NegocioException {
        try {
            return reservaDAO.obtenerReservaActivaPorComputadora(idComputadora);
        } catch (PersistenciaException e) {
            throw new NegocioException("No se pudo verificar el apartado del equipo.", e);
        }
    }

    @Override
    public void liberarReserva(int idReserva, int idComputadora) throws NegocioException {
        try {
            reservaDAO.liberarReserva(idReserva, idComputadora);
        } catch (PersistenciaException e) {
            throw new NegocioException("No se pudo liberar el equipo.", e);
        }
    }
}
