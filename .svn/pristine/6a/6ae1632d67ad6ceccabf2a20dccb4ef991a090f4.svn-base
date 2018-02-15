package com.sicap.clientes.exceptions;

import java.util.TreeSet;



public class ValidationException extends ClientesException {
    private String error;
    private TreeSet campos_error;
    private TreeSet campos_cargados;
    
    /** Creates a new instance of ValidationException */
    public ValidationException() {
    
    }
    
    public ValidationException(String error) {
        super(error);
        this.error = error;
    }
    
    public ValidationException(String error, TreeSet campos_errores, 
    TreeSet campos_cargados) {
        super(error);
        this.error = error;
        this.campos_cargados = campos_cargados;
        this.campos_error = campos_errores;
    }
    
    public String toString( ){
        return error;
    }
    
    /** Getter for property campos_cargados.
     * @return Value of property campos_cargados.
     *
     */
    public java.util.TreeSet getCampos_cargados() {
        return campos_cargados;
    }
    
    /** Setter for property campos_cargados.
     * @param campos_cargados New value of property campos_cargados.
     *
     */
    public void setCampos_cargados(java.util.TreeSet campos_cargados) {
        this.campos_cargados = campos_cargados;
    }
    
    /** Getter for property campos_error.
     * @return Value of property campos_error.
     *
     */
    public java.util.TreeSet getCampos_error() {
        return campos_error;
    }
    
    /** Setter for property campos_error.
     * @param campos_error New value of property campos_error.
     *
     */
    public void setCampos_error(java.util.TreeSet campos_error) {
        this.campos_error = campos_error;
    }
    
    /** Getter for property error.
     * @return Value of property error.
     *
     */
    public java.lang.String getError() {
        return error;
    }
    
    /** Setter for property error.
     * @param error New value of property error.
     *
     */
    public void setError(java.lang.String error) {
        this.error = error;
    }
    
}
