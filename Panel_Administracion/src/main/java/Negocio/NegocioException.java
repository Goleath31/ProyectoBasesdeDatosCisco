/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Negocio;

/**
 * Excepción personalizada para errores de la capa de negocio.
 */
public class NegocioException extends Exception {

    /**
     * Crea una excepción con un mensaje descriptivo.
     * @param mensaje Descripción del error.
     */
    public NegocioException(String mensaje) {
        super(mensaje);
    }

    /**
     * Crea una excepción con un mensaje y la causa original.
     * @param mensaje Descripción del error.
     * @param causa La excepción original capturada (PersistenciaException).
     */
    public NegocioException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}