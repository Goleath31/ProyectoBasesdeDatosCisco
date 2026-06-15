/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtos;

/**
 *
 * @author golea
 */
public class BloqueoEquipoDTO {
    private int numeroMaquina;
    private int idLaboratorioActual;

    public BloqueoEquipoDTO() {
    }

    public int getNumeroMaquina() {
        return numeroMaquina;
    }

    public void setNumeroMaquina(int numeroMaquina) {
        this.numeroMaquina = numeroMaquina;
    }

    public int getIdLaboratorioActual() {
        return idLaboratorioActual;
    }

    public void setIdLaboratorioActual(int idLaboratorioActual) {
        this.idLaboratorioActual = idLaboratorioActual;
    }
}
