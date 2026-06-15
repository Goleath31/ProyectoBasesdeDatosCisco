/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dtos;

/**
 * Representa los datos requeridos para bloquear un equipo de cómputo
 * específico.
 *
 * * @author golea
 */
public class BloqueoEquipoDTO {

    private int numeroMaquina;
    private int idLaboratorioActual;

    /**
     * Constructor por defecto.
     */
    public BloqueoEquipoDTO() {
    }

    /**
     * @return El número identificador de la máquina.
     */
    public int getNumeroMaquina() {
        return numeroMaquina;
    }

    /**
     * @param numeroMaquina Número de máquina a establecer.
     */
    public void setNumeroMaquina(int numeroMaquina) {
        this.numeroMaquina = numeroMaquina;
    }

    /**
     * @return El identificador del laboratorio donde se encuentra el equipo.
     */
    public int getIdLaboratorioActual() {
        return idLaboratorioActual;
    }

    /**
     * @param idLaboratorioActual ID del laboratorio a establecer.
     */
    public void setIdLaboratorioActual(int idLaboratorioActual) {
        this.idLaboratorioActual = idLaboratorioActual;
    }
}
