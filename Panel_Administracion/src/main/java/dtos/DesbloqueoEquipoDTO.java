/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtos;

/**
 * Contiene la información necesaria para realizar el desbloqueo de una computadora.
 * * @author golea
 */
public class DesbloqueoEquipoDTO {
    private int idComputadora;
    private String contrasenaMaestra;

    /** Constructor por defecto. */
    public DesbloqueoEquipoDTO() {}

    /** @return ID de la computadora a desbloquear. */
    public int getIdComputadora() { return idComputadora; }

    /** @param idComputadora ID de la computadora. */
    public void setIdComputadora(int idComputadora) { this.idComputadora = idComputadora; }

    /** @return Contraseña maestra para validación de desbloqueo. */
    public String getContrasenaMaestra() { return contrasenaMaestra; }

    /** @param contrasenaMaestra Contraseña a establecer. */
    public void setContrasenaMaestra(String contrasenaMaestra) { this.contrasenaMaestra = contrasenaMaestra; }
}
