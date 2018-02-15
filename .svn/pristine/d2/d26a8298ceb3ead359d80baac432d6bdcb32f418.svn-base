package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class OrdenDePagoVO implements Serializable, Cloneable {

    int idOrdenPago;
    int idCliente;
    int idSolicitud;
    int idOperacion;
    int idSucursal;
    String nombre;
    String numero;
    double monto;
    String referencia;
    String nombreArchivo;

    String usuario;
    Timestamp fechaCaptura;
    Timestamp fechaEnvio;
    Timestamp fechaCancelacion;
    int idBanco;
    int estatus;

    //Descripciones
    String nomSucursal;
    String producto;
    String descEstatus;
    String grupo;
    Date fecha;
    String nombres;
    String apaterno;
    String amaterno;
    int idOficTelecom;
    String nomOficTelecom;
    String identificacion;
    int numEnvio;

    public OrdenDePagoVO() {
        idOrdenPago = 0;
        idCliente = 0;
        idSolicitud = 0;
        idSucursal = 0;
        idOperacion = 0;
        nombre = null;
        monto = 0.0;
        referencia = null;
        usuario = null;
        fechaCaptura = null;
        fechaEnvio = null;
        fechaCancelacion = null;
        idBanco = 0;
        estatus = 0;
        nomSucursal = null;
        producto = null;
        descEstatus = null;
    }

    public String toString() {
        String respuesta = null;
        respuesta = "idOrdenPago[" + idOrdenPago + "],";
        respuesta = "idCliente[" + idCliente + "],";
        respuesta += "idSolicitud[" + idSolicitud + "],";
        respuesta += "idOperacion[" + idOperacion + "],";
        respuesta += "nombre[" + nombre + "],";
        respuesta += "monto[" + monto + "],";
        respuesta += "referencia[" + referencia + "],";
        respuesta += "usuario[" + usuario + "],";
        respuesta += "fechaCaptura[" + fechaCaptura + "],";
        respuesta += "fechaEnvio[" + fechaEnvio + "],";
        respuesta += "fechaCancelacion[" + fechaCancelacion + "],";
        respuesta += "idBanco[" + idBanco + "],";
        respuesta += "estatus[" + estatus + "]";
        respuesta += "nombreArchivo[" + nombreArchivo + "]";
        return respuesta;
    }

    public OrdenDePagoVO(int idOrdenPago, int idCliente, int idSolicitud, String nombre, double monto, int estatus, String nomSucursal, String grupo, int idSucursal, String nombres, String apaterno, String amaterno, int idOficTelecom, String nomOficTelecom, String identificacion, String referencia) {
        this.idOrdenPago = idOrdenPago;
        this.idCliente = idCliente;
        this.idSolicitud = idSolicitud;
        this.nombre = nombre;
        this.monto = monto;
        this.estatus = estatus;
        this.nomSucursal = nomSucursal;
        this.grupo = grupo;
        this.idSucursal = idSucursal;
        this.nombres = nombres;
        this.apaterno = apaterno;
        this.amaterno = amaterno;
        this.idOficTelecom = idOficTelecom;
        this.nomOficTelecom = nomOficTelecom;
        this.identificacion = identificacion;
        this.referencia = referencia;
    }
        public OrdenDePagoVO(int idOrdenPago, int idCliente, int idSolicitud, String nombre, double monto, int estatus, String nomSucursal, String grupo, int idSucursal, String nombres, String apaterno, String amaterno, int idOficTelecom, String nomOficTelecom, String identificacion, String referencia,Timestamp fechaEnvio) {
        this.idOrdenPago = idOrdenPago;
        this.idCliente = idCliente;
        this.idSolicitud = idSolicitud;
        this.nombre = nombre;
        this.monto = monto;
        this.estatus = estatus;
        this.nomSucursal = nomSucursal;
        this.grupo = grupo;
        this.idSucursal = idSucursal;
        this.nombres = nombres;
        this.apaterno = apaterno;
        this.amaterno = amaterno;
        this.idOficTelecom = idOficTelecom;
        this.nomOficTelecom = nomOficTelecom;
        this.identificacion = identificacion;
        this.referencia = referencia;
        this.fechaEnvio = fechaEnvio;
    }

    public OrdenDePagoVO(int idOrdenPago, int idCliente, int idSolicitud, int idSucursal, String nombre, double monto, String referencia, String usuario, Timestamp fechaEnvio, int estatus, String nomSucursal, String grupo, String nombres, String apaterno, String amaterno, int idOficTelecom, String nomOficTelecom, String identificacion) {
        this.idOrdenPago = idOrdenPago;
        this.idCliente = idCliente;
        this.idSolicitud = idSolicitud;
        this.idSucursal = idSucursal;
        this.nombre = nombre;
        this.monto = monto;
        this.referencia = referencia;
        this.usuario = usuario;
        this.fechaEnvio = fechaEnvio;
        this.estatus = estatus;
        this.nomSucursal = nomSucursal;
        this.grupo = grupo;
        this.nombres = nombres;
        this.apaterno = apaterno;
        this.amaterno = amaterno;
        this.idOficTelecom = idOficTelecom;
        this.nomOficTelecom = nomOficTelecom;
        this.identificacion = identificacion;
    }

    public OrdenDePagoVO(int idOrdenPago, int idCliente, int idSolicitud, int idOperacion, int idSucursal, String nombre, double monto, String referencia, String nombreArchivo, String usuario, Timestamp fechaCaptura, Timestamp fechaEnvio, int idBanco, int estatus, String nomSucursal, String producto, String descEstatus, String grupo, int numEnvio) {
        this.idOrdenPago = idOrdenPago;
        this.idCliente = idCliente;
        this.idSolicitud = idSolicitud;
        this.idOperacion = idOperacion;
        this.idSucursal = idSucursal;
        this.nombre = nombre;
        this.monto = monto;
        this.referencia = referencia;
        this.nombreArchivo = nombreArchivo;
        this.usuario = usuario;
        this.fechaCaptura = fechaCaptura;
        this.fechaEnvio = fechaEnvio;
        this.idBanco = idBanco;
        this.estatus = estatus;
        this.nomSucursal = nomSucursal;
        this.producto = producto;
        this.descEstatus = descEstatus;
        this.grupo = grupo;
        this.numEnvio = numEnvio;
    }

    public OrdenDePagoVO(int idCliente, int idSolicitud, String nombre, double monto) {
        this.idCliente = idCliente;
        this.idSolicitud = idSolicitud;
        this.nombre = nombre;
        this.monto = monto;
    }
    
    public OrdenDePagoVO(String referencia, int idCliente, int idSolicitud, int idSucursal, String nombre, double monto, int idBanco, int estatus) {
        this.referencia = referencia;
        this.idCliente = idCliente;
        this.idSolicitud = idSolicitud;
        this.idSucursal = idSucursal;
        this.nombre = nombre;
        this.monto = monto;
        this.idBanco = idBanco;
        this.estatus = estatus;
    }
    public OrdenDePagoVO(String referencia, int idCliente, int idSolicitud, int idSucursal, String nombre, double monto, int idBanco, int estatus, Timestamp fechaEnvio) {
        this.referencia = referencia;
        this.idCliente = idCliente;
        this.idSolicitud = idSolicitud;
        this.idSucursal = idSucursal;
        this.nombre = nombre;
        this.monto = monto;
        this.idBanco = idBanco;
        this.estatus = estatus;
        this.fechaEnvio = fechaEnvio;
    }

    public int getIdOrdenPago() {
        return idOrdenPago;
    }

    public void setIdOrdenPago(int idOrdenPago) {
        this.idOrdenPago = idOrdenPago;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(int idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public int getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(int idSucursal) {
        this.idSucursal = idSucursal;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public Timestamp getFechaCaptura() {
        return fechaCaptura;
    }

    public void setFechaCaptura(Timestamp fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }

    public Timestamp getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Timestamp fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public int getIdBanco() {
        return idBanco;
    }

    public void setIdBanco(int idBanco) {
        this.idBanco = idBanco;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getDescEstatus() {
        return descEstatus;
    }

    public void setDescEstatus(String descEstatus) {
        this.descEstatus = descEstatus;
    }

    public String getNomSucursal() {
        return nomSucursal;
    }

    public void setNomSucursal(String nomSucursal) {
        this.nomSucursal = nomSucursal;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public int getIdOperacion() {
        return idOperacion;
    }

    public void setIdOperacion(int idOperacion) {
        this.idOperacion = idOperacion;
    }

    public Timestamp getFechaCancelacion() {
        return fechaCancelacion;
    }

    public void setFechaCancelacion(Timestamp fechaCancelacion) {
        this.fechaCancelacion = fechaCancelacion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApaterno() {
        return apaterno;
    }

    public void setApaterno(String apaterno) {
        this.apaterno = apaterno;
    }

    public String getAmaterno() {
        return amaterno;
    }

    public void setAmaterno(String amaterno) {
        this.amaterno = amaterno;
    }

    public int getIdOficTelecom() {
        return idOficTelecom;
    }

    public void setIdOficTelecom(int idOficTelecom) {
        this.idOficTelecom = idOficTelecom;
    }

    public String getNomOficTelecom() {
        return nomOficTelecom;
    }

    public void setNomOficTelecom(String nomOficTelecom) {
        this.nomOficTelecom = nomOficTelecom;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public int getNumEnvio() {
        return numEnvio;
    }

    public void setNumEnvio(int numEnvio) {
        this.numEnvio = numEnvio;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone(); 
    }

    
}
