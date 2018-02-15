package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Timestamp;

public class ReporteVisitaGrupalVO  implements Serializable{

	public int idAlerta;
	public String usuario;
	public Timestamp fechaCaptura;
	public int problemasGrupo;
	public int problemasAsesor;
	public int problemasNegocio;
	public int problemasPersonales;
	public int problemasOtros;
	public int propuestaSolucion;
	public String comentarios;
	public String integrantesVisitados;
	

	
	public ReporteVisitaGrupalVO (){
		idAlerta = 0;
		usuario = null;
		fechaCaptura = null;
		problemasGrupo = 0;
		problemasAsesor = 0;
		problemasNegocio = 0;
		problemasPersonales = 0;
		problemasOtros = 0;
		propuestaSolucion = 0;
		comentarios = null;
		integrantesVisitados = null;
	}


	public String toString(){
		String respuesta = null;
		respuesta = "idAlerta=["+idAlerta+"],";
		respuesta += "usuario=["+usuario+"],";
		respuesta += "fechaCaptura=["+fechaCaptura+"],";
		respuesta += "problemasGrupo=["+problemasGrupo+"],";
		respuesta += "problemasAsesor=["+problemasAsesor+"],";
		respuesta += "problemasNegocio=["+problemasNegocio+"],";
		respuesta += "problemasPersonales=["+problemasPersonales+"],";
		respuesta += "problemasOtros=["+problemasOtros+"],";
		respuesta += "propuestaSolucion=["+propuestaSolucion+"],";
		respuesta += "comentarios=["+comentarios+"],";
		respuesta += "integrantesAsistentes=["+integrantesVisitados+"],";
		return respuesta;
	}

}
