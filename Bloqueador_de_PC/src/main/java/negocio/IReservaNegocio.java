/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package negocio;

import dto.ReservaActivaDTO;

/**
 *
 * @author rafaelgb
 */
public interface IReservaNegocio {
    ReservaActivaDTO obtenerReservaActivaPorComputadora(int idComputadora) throws NegocioException;

    void liberarReserva(int idReserva, int idComputadora) throws NegocioException;
}
