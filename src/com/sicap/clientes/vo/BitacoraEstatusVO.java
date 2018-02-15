package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Timestamp;

public class BitacoraEstatusVO implements Serializable{
	
	public int idCliente;
	public int idSolicitud;
	public int estatus;
        public String estatusDescripcion;
	public String usuario;
	public int esAnalisisCredito;
	public Timestamp fechaHora;
        public int numComentario;
        public String comentario;
	
	public BitacoraEstatusVO(){
		idCliente = 0;
		idSolicitud = 0;
		estatus = 0;
		usuario = null;
		esAnalisisCredito = 0;
		fechaHora = null;
                numComentario = 0;
                comentario ="";
                estatusDescripcion="";
	}
	
	public String toString(){
		String respuesta = null;
		respuesta = "idCliente[" + idCliente + "],";
		respuesta += "idSolicitud[" + idSolicitud + "],";
		respuesta += "estatus[" + estatus + "],";
                respuesta += "DescripcionEstatus" +estatusDescripcion+"],";
		respuesta += "usuario[" + usuario + "],";
		respuesta += "esAnalisisCredito[" + esAnalisisCredito + "],";
		respuesta += "fechaHora[" + fechaHora + "],";
                respuesta += "numComentario["+numComentario+"],";
                respuesta += "Comentario["+comentario+"]";
		return respuesta;
	}
}
