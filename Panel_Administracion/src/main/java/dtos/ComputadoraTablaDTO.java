/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtos;

/**
 *
 * @author golea
 */
public class ComputadoraTablaDTO {
    private int idComputadora;
    private int numeroMaquina;
    private String direccionIp;
    private String estatus;

    public ComputadoraTablaDTO() {
    }
    
    public ComputadoraTablaDTO(int idComputadora, int numeroMaquina, String direccionIp, String estatus) {
    this.idComputadora = idComputadora;
    this.numeroMaquina = numeroMaquina;
    this.direccionIp = direccionIp;
    this.estatus = estatus;
}

    public int getIdComputadora() {
        return idComputadora;
    }

    public void setIdComputadora(int idComputadora) {
        this.idComputadora = idComputadora;
    }

    public int getNumeroMaquina() {
        return numeroMaquina;
    }

    public void setNumeroMaquina(int numeroMaquina) {
        this.numeroMaquina = numeroMaquina;
    }

    public String getDireccionIp() {
        return direccionIp;
    }

    public void setDireccionIp(String direccionIp) {
        this.direccionIp = direccionIp;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

}
