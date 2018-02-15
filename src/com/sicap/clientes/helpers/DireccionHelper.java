package com.sicap.clientes.helpers;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.vo.DireccionGenericaVO;
import com.sicap.clientes.vo.DireccionVO;


public class DireccionHelper{


	//Recibe el cliente de la session como parametro, agrega la informaci�n capturada en el formulario
	//retornando el objeto actualizado
	public static DireccionVO getVO (DireccionVO direccion, HttpServletRequest request) throws Exception{
		if ( direccion==null )
			direccion = new DireccionVO();
		if ( request.getParameter("idCliente")!=null ) direccion.idCliente = HTMLHelper.getParameterInt(request, "idCliente");
		if ( request.getParameter("idSolicitud")!=null ) direccion.idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
		if ( request.getParameter("idObligado")!=null ) direccion.indiceTabla = HTMLHelper.getParameterInt(request, "idObligado");
		//direccion.idDireccion = HTMLHelper.getParameterInt(request, "idDireccion");
		if ( request.getParameter("idColonia")!=null ) direccion.idColonia = HTMLHelper.getParameterInt(request, "idColonia");
		if ( request.getParameter("asentamientoCP")!=null ) direccion.asentamiento_cp = HTMLHelper.getParameterString(request, "asentamientoCP");
		if ( request.getParameter("estado")!=null ) direccion.estado = HTMLHelper.getParameterString(request, "estado");
		if ( request.getParameter("municipio")!=null ) direccion.municipio = HTMLHelper.getParameterString(request, "municipio");
		if ( request.getParameter("colonia")!=null ) direccion.colonia = HTMLHelper.getParameterString(request, "colonia");
		if ( request.getParameter("cp")!=null ) direccion.cp = HTMLHelper.getParameterString(request, "cp");
		if ( request.getParameter("calle")!=null ) direccion.calle = HTMLHelper.getParameterString(request, "calle");
		if ( request.getParameter("numeroExterior")!=null ) direccion.numeroExterior = HTMLHelper.getParameterString(request, "numeroExterior");
		if ( request.getParameter("numeroInterior")!=null ) direccion.numeroInterior = HTMLHelper.getParameterString(request, "numeroInterior");
		if ( request.getParameter("numeroInterior")!=null ) direccion.situacionVivienda = HTMLHelper.getParameterInt(request, "situacionVivienda");
		if ( request.getParameter("antiguedadDomicilio")!=null ) direccion.antDomicilio = HTMLHelper.getParameterString(request, "antiguedadDomicilio");
		if ( request.getParameter("idLocalidad")!=null ) direccion.idLocalidad = HTMLHelper.getParameterInt(request, "idLocalidad");
		if ( request.getParameter("localidad")!=null ) direccion.localidad = HTMLHelper.getParameterString(request, "localidad");
                if ( request.getParameter("TipoAsentameinto")!=null ) direccion.tipoAsentamiento = HTMLHelper.getParameterInt(request, "TipoAsentameinto");
                if ( request.getParameter("TipoVialidad")!=null ) direccion.tipoVialidad = HTMLHelper.getParameterInt(request, "TipoVialidad");
                //^o^
		return direccion;
		
	}
	
	public static DireccionVO getOtrasDirecciones (DireccionVO direccion, HttpServletRequest request, int idDireccion) throws Exception{
		if(idDireccion == 2){
			if ( direccion==null )
				direccion = new DireccionVO();
			if ( request.getParameter("idCliente")!=null ) direccion.idCliente = HTMLHelper.getParameterInt(request, "idCliente");
			if ( request.getParameter("idSolicitud")!=null ) direccion.idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
			if ( request.getParameter("idColonia")!=null ) direccion.idColonia = HTMLHelper.getParameterInt(request, "idColonia");
			if ( request.getParameter("estado")!=null ) direccion.estado = HTMLHelper.getParameterString(request, "estado");
			if ( request.getParameter("municipio")!=null ) direccion.municipio = HTMLHelper.getParameterString(request, "municipio");
			if ( request.getParameter("colonia")!=null ) direccion.colonia = HTMLHelper.getParameterString(request, "colonia");
			if ( request.getParameter("cp")!=null ) direccion.cp = HTMLHelper.getParameterString(request, "cp");
			if ( request.getParameter("calle")!=null ) direccion.calle = HTMLHelper.getParameterString(request, "calle");
			if ( request.getParameter("numeroExterior")!=null ) direccion.numeroExterior = HTMLHelper.getParameterString(request, "numeroExterior");
			if ( request.getParameter("numeroInterior")!=null ) direccion.numeroInterior = HTMLHelper.getParameterString(request, "numeroInterior");
			if ( request.getParameter("situacionVivienda")!=null ) direccion.idDireccion = HTMLHelper.getParameterInt(request, "situacionVivienda");
		}else if(idDireccion == 3){
			if ( direccion==null )
				direccion = new DireccionVO();
			if ( request.getParameter("idCliente")!=null ) direccion.idCliente = HTMLHelper.getParameterInt(request, "idCliente");
			if ( request.getParameter("idSolicitud")!=null ) direccion.idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
			if ( request.getParameter("idColoniaTerceraDir")!=null ) direccion.idColonia = HTMLHelper.getParameterInt(request, "idColoniaTerceraDir");
			if ( request.getParameter("estadoTerceraDir")!=null ) direccion.estado = HTMLHelper.getParameterString(request, "estadoTerceraDir");
			if ( request.getParameter("municipioTerceraDir")!=null ) direccion.municipio = HTMLHelper.getParameterString(request, "municipioTerceraDir");
			if ( request.getParameter("coloniaTerceraDir")!=null ) direccion.colonia = HTMLHelper.getParameterString(request, "coloniaTerceraDir");
			if ( request.getParameter("cpTerceraDir")!=null ) direccion.cp = HTMLHelper.getParameterString(request, "cpTerceraDir");
			if ( request.getParameter("calleTerceraDir")!=null ) direccion.calle = HTMLHelper.getParameterString(request, "calleTerceraDir");
			if ( request.getParameter("numeroExteriorTerceraDir")!=null ) direccion.numeroExterior = HTMLHelper.getParameterString(request, "numeroExteriorTerceraDir");
			if ( request.getParameter("numeroInteriorTerceraDir")!=null ) direccion.numeroInterior = HTMLHelper.getParameterString(request, "numeroInteriorTerceraDir");
			if ( request.getParameter("situacionViviendaTerceraDir")!=null ) direccion.idDireccion = HTMLHelper.getParameterInt(request, "situacionViviendaTerceraDir");
		}
		return direccion;
	}

        public static DireccionVO getVODireccionAnterior (DireccionVO direccion, HttpServletRequest request) throws Exception{
		if ( direccion==null )
			direccion = new DireccionVO();
		if ( request.getParameter("idCliente")!=null ) direccion.idCliente = HTMLHelper.getParameterInt(request, "idCliente");
		if ( request.getParameter("idSolicitud")!=null ) direccion.idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
		if ( request.getParameter("idObligado")!=null ) direccion.indiceTabla = HTMLHelper.getParameterInt(request, "idObligado");
		//direccion.idDireccion = HTMLHelper.getParameterInt(request, "idDireccion");
		if ( request.getParameter("idColoniaAnterior")!=null ) direccion.idColonia = HTMLHelper.getParameterInt(request, "idColoniaAnterior");
		if ( request.getParameter("asentamientoCPAnterior")!=null ) direccion.asentamiento_cp = HTMLHelper.getParameterString(request, "asentamientoCPAnterior");
		if ( request.getParameter("estadoAnterior")!=null ) direccion.estado = HTMLHelper.getParameterString(request, "estadoAnterior");
		if ( request.getParameter("municipioAnterior")!=null ) direccion.municipio = HTMLHelper.getParameterString(request, "municipioAnterior");
		if ( request.getParameter("coloniaAnterior")!=null ) direccion.colonia = HTMLHelper.getParameterString(request, "coloniaAnterior");
		if ( request.getParameter("cpAnterior")!=null ) direccion.cp = HTMLHelper.getParameterString(request, "cpAnterior");
		if ( request.getParameter("calleAnterior")!=null ) direccion.calle = HTMLHelper.getParameterString(request, "calleAnterior");
		if ( request.getParameter("numeroExteriorAnterior")!=null ) direccion.numeroExterior = HTMLHelper.getParameterString(request, "numeroExteriorAnterior");
		if ( request.getParameter("numeroInteriorAnterior")!=null ) direccion.numeroInterior = HTMLHelper.getParameterString(request, "numeroInteriorAnterior");
		if ( request.getParameter("idLocalidadAnterior")!=null ) direccion.idLocalidad = HTMLHelper.getParameterInt(request, "idLocalidadAnterior");
		if ( request.getParameter("TipoAsentameintoAnterior")!=null ) direccion.tipoAsentamiento = HTMLHelper.getParameterInt(request, "TipoAsentameintoAnterior");
                if ( request.getParameter("TipoVialidadAnterior")!=null ) direccion.tipoVialidad = HTMLHelper.getParameterInt(request, "TipoVialidadAnterior");
                
		return direccion;
		
	}
	
	public static DireccionGenericaVO getDireccionGenericaVO (DireccionGenericaVO direccion, HttpServletRequest request) throws Exception{
		if ( direccion==null )
			direccion = new DireccionGenericaVO();
		//direccion.idDireccion = HTMLHelper.getParameterInt(request, "idDireccion");
		direccion.idColonia = HTMLHelper.getParameterInt(request, "idColonia");
		direccion.asentamiento_cp = HTMLHelper.getParameterString(request, "asentamientoCP");
		direccion.estado = HTMLHelper.getParameterString(request, "estado");
		direccion.municipio = HTMLHelper.getParameterString(request, "municipio");
		direccion.colonia = HTMLHelper.getParameterString(request, "colonia");
		direccion.cp = HTMLHelper.getParameterString(request, "cp");
		direccion.calle = HTMLHelper.getParameterString(request, "calle");
		direccion.numeroExterior = HTMLHelper.getParameterString(request, "numeroExterior");
		direccion.numeroInterior = HTMLHelper.getParameterString(request, "numeroInterior");
		return direccion;
		
	}
	
	
}