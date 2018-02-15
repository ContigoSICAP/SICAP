package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class GrupoVO implements Serializable {

    public int idGrupo;
    public int idGrupoIBS;
    public String rfc;
    public int sucursal;
    public int estatus;
    public String nombre;
    public Date fechaFormacion;
    public CicloGrupalVO[] ciclos;
    public String calificacion;
    public String calificacionAnterior;
    public String nivelRestructura;
    public int idGrupoOriginal;
    public int idCicloOriginal;
    public Timestamp fechaCaptura;
    public int idOperacion;
    public EventosDePagoVO monitorPagos;
    public int tipoSucursal;
    public String refGrupo;
    public Double garantia ;
    public int cuotaAtraso;
    public int otraFinaciera;

    public GrupoVO() {
        idGrupo = 0;
        idGrupoIBS = 0;
        rfc = null;
        sucursal = 0;
        estatus = 0;
        nombre = null;
        fechaFormacion = null;
        ciclos = null;
        fechaCaptura = null;
        idOperacion = 0;
        calificacion = "";
        calificacionAnterior = null;
        nivelRestructura = null;
        idGrupoOriginal = 0;
        idCicloOriginal = 0;
        monitorPagos = new EventosDePagoVO();
        tipoSucursal = 0;
        otraFinaciera = 0;
    }
    //PROPIEDAD DE LA SOLICITUD

    public String toString() {
        String respuesta = null;
        respuesta = "idGrupo=[" + idGrupo + "],";
        respuesta += "idGrupoIBS=[" + idGrupoIBS + "],";
        respuesta += "rfc=[" + rfc + "],";
        respuesta += "sucursal=[" + sucursal + "],";
        respuesta += "estatus=[" + estatus + "],";
        respuesta += "nombre=[" + nombre + "],";
        respuesta += "fechaFormacion=[" + fechaFormacion + "],";
        respuesta += "fechaCaptura=[" + fechaCaptura + "],";
        respuesta += "idOperacion=[" + idOperacion + "],";
        respuesta += "calificacion=[" + calificacion + "]";
        respuesta += "calificacionAnterior=[" + calificacionAnterior + "],";
        respuesta += "nivelRestructura=[" + nivelRestructura + "],";
        respuesta += "idGrupoOriginal=[" + idGrupoOriginal + "],";
        respuesta += "idCicloOriginal=[" + idCicloOriginal + "],";
        respuesta += "tipoSucursal=[" + tipoSucursal + "],";
        respuesta += "otraFinaciera=["+otraFinaciera+"]";

        return respuesta;
    }

    public GrupoVO(int sucursal, String nombre, Date fechaFormacion, String refGrupo) {
        this.sucursal = sucursal;
        this.nombre = nombre;
        this.fechaFormacion = fechaFormacion;
        this.refGrupo = refGrupo;
    }

    public GrupoVO(int sucursal, String nombre) {
        this.sucursal = sucursal;
        this.nombre = nombre;
    }

    public String getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(String calificacion) {
        this.calificacion = calificacion;
    }

    public String getCalificacionAnterior() {
        return calificacionAnterior;
    }

    public void setCalificacionAnterior(String calificacionAnterior) {
        this.calificacionAnterior = calificacionAnterior;
    }

    public CicloGrupalVO[] getCiclos() {
        return ciclos;
    }

    public void setCiclos(CicloGrupalVO[] ciclos) {
        this.ciclos = ciclos;
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

    public Date getFechaFormacion() {
        return fechaFormacion;
    }

    public void setFechaFormacion(Date fechaFormacion) {
        this.fechaFormacion = fechaFormacion;
    }

    public int getIdCicloOriginal() {
        return idCicloOriginal;
    }

    public void setIdCicloOriginal(int idCicloOriginal) {
        this.idCicloOriginal = idCicloOriginal;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    public int getIdGrupoIBS() {
        return idGrupoIBS;
    }

    public void setIdGrupoIBS(int idGrupoIBS) {
        this.idGrupoIBS = idGrupoIBS;
    }

    public int getIdGrupoOriginal() {
        return idGrupoOriginal;
    }

    public void setIdGrupoOriginal(int idGrupoOriginal) {
        this.idGrupoOriginal = idGrupoOriginal;
    }

    public int getIdOperacion() {
        return idOperacion;
    }

    public void setIdOperacion(int idOperacion) {
        this.idOperacion = idOperacion;
    }

    public EventosDePagoVO getMonitorPagos() {
        return monitorPagos;
    }

    public void setMonitorPagos(EventosDePagoVO monitorPagos) {
        this.monitorPagos = monitorPagos;
    }

    public String getNivelRestructura() {
        return nivelRestructura;
    }

    public void setNivelRestructura(String nivelRestructura) {
        this.nivelRestructura = nivelRestructura;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRefGrupo() {
        return refGrupo;
    }

    public void setRefGrupo(String refGrupo) {
        this.refGrupo = refGrupo;
    }

    public String getRfc() {
        return rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public int getSucursal() {
        return sucursal;
    }

    public void setSucursal(int sucursal) {
        this.sucursal = sucursal;
    }

    public int getTipoSucursal() {
        return tipoSucursal;
    }

    public void setTipoSucursal(int tipoSucursal) {
        this.tipoSucursal = tipoSucursal;
    }
    public Double getGarantia() {
        return garantia;
    }

    public void setGarantia(Double garantia) {
        this.garantia = garantia;
    }

    public int getCuotaAtraso() {
        return cuotaAtraso;
    }

    public void setCuotaAtraso(int cuotaAtraso) {
        this.cuotaAtraso = cuotaAtraso;
    }

    public int getOtraFinaciera() {
        return otraFinaciera;
    }

    public void setOtraFinaciera(int otraFinaciera) {
        this.otraFinaciera = otraFinaciera;
    }
    
    
}
