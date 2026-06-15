/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidad;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author golea
 */
public class SoftwareInstaladoEntidad {
    private ComputadoraEntidad computadora;
    private String software;

    public SoftwareInstaladoEntidad() {
    }

    public ComputadoraEntidad getComputadora() {
        return computadora;
    }

    public void setComputadora(ComputadoraEntidad computadora) {
        this.computadora = computadora;
    }

    public String getSoftware() {
        return software;
    }

    public void setSoftware(String software) {
        this.software = software;
    }
}

class SoftwareInstaladoId implements Serializable {
    private int computadora;
    private String software;

    public SoftwareInstaladoId() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SoftwareInstaladoId that = (SoftwareInstaladoId) o;
        return computadora == that.computadora && Objects.equals(software, that.software);
    }

    @Override
    public int hashCode() {
        return Objects.hash(computadora, software);
    }
}
