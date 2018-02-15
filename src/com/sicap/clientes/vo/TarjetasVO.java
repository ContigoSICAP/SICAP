package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Timestamp;

public class TarjetasVO implements Serializable {
    
    public int carga;
    public Timestamp fechaCaptura;
    public String usuario;
    public int banco;
    public String loteBanco;
    public String loteSicap;
    public int totTarjetas;
    public String tarjeta;
    public int esBaja;
    public int asignada;
    public Timestamp fechaBaja;
    public Timestamp fechaAsignacion;
    public int idCliente;
    public int envio;
    public Timestamp fechaEnvio;
    public String archivo;
    public ClienteVO clienteVO;
    public SucursalVO sucursalVO;
    public int idSolicitud;
    public int idSucursal;
    public String nombre;
    public double monto;
    public int estatus;
    public Timestamp fechaCancelacion;
    public String grupo;
    public String descEstatus;

    public TarjetasVO() {
    }
    
    public TarjetasVO(String tarjeta) {
        this.tarjeta = tarjeta;
    }

    public TarjetasVO(Timestamp fechaCaptura, String usuario, int banco, String loteBanco, String loteSicap, int totTarjetas, String archivo) {
        this.fechaCaptura = fechaCaptura;
        this.usuario = usuario;
        this.banco = banco;
        this.loteBanco = loteBanco;
        this.loteSicap = loteSicap;
        this.totTarjetas = totTarjetas;
        this.archivo = archivo;
    }

    public TarjetasVO(String tarjeta, ClienteVO clienteVO, SucursalVO sucursalVO) {
        this.tarjeta = tarjeta;
        this.clienteVO = clienteVO;
        this.sucursalVO = sucursalVO;
    }

    public TarjetasVO(int carga, Timestamp fechaCaptura, String usuario, int banco, String tarjeta, int idCliente, int envio, Timestamp fechaEnvio, String archivo, int idSolicitud, SucursalVO sucursalVO, String nombre, double monto, int estatus, String grupo, String descEstatus) {
        this.carga = carga;
        this.fechaCaptura = fechaCaptura;
        this.usuario = usuario;
        this.banco = banco;
        this.tarjeta = tarjeta;
        this.idCliente = idCliente;
        this.envio = envio;
        this.fechaEnvio = fechaEnvio;
        this.archivo = archivo;
        this.idSolicitud = idSolicitud;
        this.sucursalVO = sucursalVO;
        this.nombre = nombre;
        this.monto = monto;
        this.estatus = estatus;
        this.grupo = grupo;
        this.descEstatus = descEstatus;
    }
    
    public int getCarga() {
        return carga;
    }

    public void setCarga(int carga) {
        this.carga = carga;
    }

    public Timestamp getFechaCaptura() {
        return fechaCaptura;
    }

    public void setFechaCaptura(Timestamp fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public int getBanco() {
        return banco;
    }

    public void setBanco(int banco) {
        this.banco = banco;
    }

    public String getLoteBanco() {
        return loteBanco;
    }

    public void setLoteBanco(String loteBanco) {
        this.loteBanco = loteBanco;
    }

    public String getLoteSicap() {
        return loteSicap;
    }

    public void setLoteSicap(String loteSicap) {
        this.loteSicap = loteSicap;
    }

    public int getTotTarjetas() {
        return totTarjetas;
    }

    public void setTotTarjetas(int totTarjetas) {
        this.totTarjetas = totTarjetas;
    }

    public String getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(String tarjeta) {
        this.tarjeta = tarjeta;
    }

    public int getEsBaja() {
        return esBaja;
    }

    public void setEsBaja(int esBaja) {
        this.esBaja = esBaja;
    }

    public int getAsignada() {
        return asignada;
    }

    public void setAsignada(int asignada) {
        this.asignada = asignada;
    }

    public Timestamp getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Timestamp fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public Timestamp getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(Timestamp fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getEnvio() {
        return envio;
    }

    public void setEnvio(int envio) {
        this.envio = envio;
    }

    public Timestamp getFechaEnvio() {
        return fechaEnvio;
    }

    public void setFechaEnvio(Timestamp fechaEnvio) {
        this.fechaEnvio = fechaEnvio;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    public ClienteVO getClienteVO() {
        return clienteVO;
    }

    public void setClienteVO(ClienteVO clienteVO) {
        this.clienteVO = clienteVO;
    }

    public SucursalVO getSucursalVO() {
        return sucursalVO;
    }

    public void setSucursalVO(SucursalVO sucursalVO) {
        this.sucursalVO = sucursalVO;
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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public Timestamp getFechaCancelacion() {
        return fechaCancelacion;
    }

    public void setFechaCancelacion(Timestamp fechaCancelacion) {
        this.fechaCancelacion = fechaCancelacion;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getDescEstatus() {
        return descEstatus;
    }

    public void setDescEstatus(String descEstatus) {
        this.descEstatus = descEstatus;
    }
    
}
