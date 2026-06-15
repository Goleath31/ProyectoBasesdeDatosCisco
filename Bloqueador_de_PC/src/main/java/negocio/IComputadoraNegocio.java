/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package negocio;

/**
 *
 * @author rafaelgb
 */
public interface IComputadoraNegocio {
    UbicacionEquipoDTO obtenerUbicacionPorIP(String direccionIp) throws NegocioException;

    void marcarEquipoEnUso(int idComputadora) throws NegocioException;
}
