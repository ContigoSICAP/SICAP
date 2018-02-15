package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Timestamp;

public class ReporteVO implements Serializable{
	
	public int idCliente;
	public String rfc;
	public String nombre;
	public int idSolicitud;
	public int idProducto;
	public String producto;
	public int idEstatusSolicitud;
	public String descEstatusSolicitud;
	public int idEstatusDesembolso;
	public String descEstatusDesembolso;
	public int idMotivoRechazo;
	public String descMotivoRechazo;
        public int idSucursal;
	public String nombreSucursal;
	public int dictamen;
	public String usuarioSucursal;
	public String usuarioMesaControl;
        public String nombreEjecutivo;
        public int numGrupo;
        public String nombreGrupo;
	public Timestamp fechaModificacion;
	public Date fechaCaptura;
        public Date fechaFirma;
        public Date fechaVencimiento;
	public Date fechaCertificado;
        public String origen;
        public String ejecutivo;
        public String comentario;
        public Timestamp fechaHora;
        public int calificacion;
        public int calificacionCC = 0;
        public int calificacionMesa = 0;
        public int aceptaRegular = 0;
	
	public ReporteVO(){
		idCliente = 0;
		rfc = null;
		nombre = null;
		idSolicitud = 0;
		idProducto = 0;
		producto = null;
                numGrupo = 0;
                nombreGrupo = "";
		idEstatusSolicitud = 0;
		descEstatusSolicitud = null;
		idEstatusDesembolso = 0;
		descEstatusDesembolso = null;
		idMotivoRechazo = 0;
		descMotivoRechazo = null;
		nombreSucursal = null;
                idSucursal = 0;
		dictamen = 0;
		fechaCertificado = null;
		usuarioSucursal = null;
		usuarioMesaControl = null;
		fechaModificacion = null;
                fechaCaptura = null;
                fechaFirma = null;
                fechaVencimiento = null;
                origen = null;
                comentario = "";
                fechaHora = null;
                calificacion = 0;
                calificacionCC = 0;
                calificacionMesa = 0;
	}
	
	public String toString(){
		String respuesta = null;
		respuesta = "idCliente=[" + idCliente + "],";
		respuesta += "rfc=[" + rfc + "],";
		respuesta += "nombre=[" + nombre + "],";
		respuesta += "idSolicitud=[" + idSolicitud + "],";
		respuesta += "idProducto=[" + idProducto + "],";
		respuesta += "producto=[" + producto + "],";
		respuesta += "idEstatusSolicitud=[" + idEstatusSolicitud + "],";
		respuesta += "descEstatusSolicitud=[" + descEstatusSolicitud + "],";
		respuesta += "idEstatusDesembolso=[" + idEstatusDesembolso + "],";
		respuesta += "descEstatusDesembolso=[" + descEstatusDesembolso + "],";
		respuesta += "idMotivoRechazo=[" + idMotivoRechazo + "],";
		respuesta += "descmotivoRechazo=[" + descMotivoRechazo + "],";
		respuesta += "fechaCertificado=[" + fechaCertificado + "],";
		respuesta += "nombreSucursal=[" + nombreSucursal + "],";
		respuesta += "usuarioSucursal=[" + usuarioSucursal + "],";
		respuesta += "usuarioMesaControl=[" + usuarioMesaControl + "],";
		respuesta += "fechaModificacion=[" + fechaModificacion + "],";
		respuesta += "fechaCaptura=[" + fechaCaptura + "],";
                respuesta += "fechaFirma=[" + fechaFirma + "],";
                respuesta += "fechaVencimiento=[" + fechaVencimiento + "],";
		respuesta += "dictamen=[" + dictamen + "],";
                respuesta += "origen=[" + origen + "],";
                respuesta += "comentario=[" + comentario + "],";
                respuesta += "fechaHora=[" + fechaHora + "],";
                respuesta += "calificacion=[" + calificacion + "]";
                respuesta += "calificacionCC=[" + calificacionCC + "]";
                respuesta += "calificacionMesa=[" + calificacionMesa + "]";
		respuesta += "idSucursal=[" + idSucursal + "]";
		return respuesta;
	}

}
