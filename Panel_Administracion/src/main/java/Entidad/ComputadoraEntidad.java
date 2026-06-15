/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidad;

/**
 *
 * @author golea
 */
public class ComputadoraEntidad {
    private int idComputadora;
    private int numeroMaquina;
    private String direccionIp;
    private String estatus = "Disponible";
    private String tipo; 
    private LaboratorioEntidad laboratorio;

    public ComputadoraEntidad() {
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LaboratorioEntidad getLaboratorio() {
        return laboratorio;
    }

    public void setLaboratorio(LaboratorioEntidad laboratorio) {
        this.laboratorio = laboratorio;
    }
}
