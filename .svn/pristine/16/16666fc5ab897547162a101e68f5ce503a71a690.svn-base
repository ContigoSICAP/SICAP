package com.sicap.clientes.helpers;


import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.ConyugeVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.LimiteCreditoVO;
import com.sicap.clientes.vo.TelefonoVO;


public class ClienteHelper{


	public static String displayNotifications(Notification[] notificaciones){

		String notificacion = "";
		Notification not = null;


		for ( int c=0 ; notificaciones!=null && c<notificaciones.length ; c++ ){
			not = (Notification)notificaciones[c];
			if ( not.type==ClientesConstants.ERROR_TYPE ){
				notificacion += "<b><font color='"+ClientesConstants.ERROR_COLOR+"'>"+not.text+"</font><b><br><br>";
			}
			else{
				notificacion += "<b><font color='"+ClientesConstants.INFO_COLOR+"'>"+not.text+"</font></b><br><br>";
			}
		}
		return notificacion;

	}


	//Recibe el cliente de la session como parametro, agrega la información capturada en el formulario
	//retornando el objeto actualizado
	public static ClienteVO getVO (ClienteVO cliente, HttpServletRequest request) throws Exception{
		
		if ( request.getParameter("idCliente")!=null ) cliente.idCliente = HTMLHelper.getParameterInt(request, "idCliente");
		if ( request.getParameter("rfc")!=null ) cliente.rfc = request.getParameter("rfc");
		if ( request.getParameter("fechaNacimiento")!=null ) cliente.fechaNacimiento = Convertidor.toSqlDate(HTMLHelper.getParameterDate(request, "fechaNacimiento"));		
		//cliente.fechaNacimiento = new java.sql.Date(HTMLHelper.getParameterDate(request, "fechaNacimiento").getTime());
		if ( request.getParameter("entidadNacimiento")!=null ) cliente.entidadNacimiento = HTMLHelper.getParameterInt(request, "entidadNacimiento");
		if ( request.getParameter("nombre")!=null ) cliente.nombre = request.getParameter("nombre").toUpperCase().trim();
		if ( request.getParameter("aPaterno")!=null ) cliente.aPaterno = request.getParameter("aPaterno").toUpperCase().trim();
		if ( request.getParameter("aMaterno")!=null )cliente.aMaterno = request.getParameter("aMaterno").toUpperCase().trim();
		if ( request.getParameter("sexo")!=null )cliente.sexo = HTMLHelper.getParameterInt(request, "sexo");
		if ( request.getParameter("nacionalidad")!=null )cliente.nacionalidad = HTMLHelper.getParameterInt(request, "nacionalidad");
		if ( request.getParameter("tipoIdentificacion")!=null )cliente.tipoIdentificacion = HTMLHelper.getParameterInt(request, "tipoIdentificacion");
		if ( request.getParameter("numeroIdentificacion")!=null )cliente.numeroIdentificacion = request.getParameter("numeroIdentificacion");
		if ( request.getParameter("estadoCivil")!=null )cliente.estadoCivil = HTMLHelper.getParameterInt(request, "estadoCivil");
		if ( request.getParameter("correoElectronico")!=null )cliente.correoElectronico = request.getParameter("correoElectronico");
		if ( request.getParameter("dependientesEconomicos")!=null )cliente.dependientesEconomicos = HTMLHelper.getParameterInt(request, "dependientesEconomicos");
		if ( request.getParameter("idGrupo")!=null )cliente.idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
		if ( request.getParameter("nivelEstudios")!=null )cliente.nivelEstudios = HTMLHelper.getParameterInt(request, "nivelEstudios");
                if ( request.getParameter("LenguaIndigena")!=null )cliente.LenguaIndigena = HTMLHelper.getParameterInt(request, "LenguaIndigena");
                if ( request.getParameter("Discapacidad")!=null )cliente.Discapacidad = HTMLHelper.getParameterInt(request, "Discapacidad");
                if ( request.getParameter("UsodeInternet")!=null )cliente.UsodeInternet = HTMLHelper.getParameterInt(request, "UsodeInternet");
                if ( request.getParameter("RedesSociales")!=null )cliente.RedesSociales = HTMLHelper.getParameterInt(request, "RedesSociales");
		if ( request.getParameter("curp")!=null )cliente.curp = request.getParameter("curp").toUpperCase();
		if ( request.getParameter("idSucursal")!=null )cliente.idSucursal = HTMLHelper.getParameterInt(request, "idSucursal");
		
		return cliente;

	}


	public static TelefonoVO getTelefono(TelefonoVO[] telefonos, int tipoTelefono){
		TelefonoVO telefono = new TelefonoVO();
		for( int i=0 ; telefonos!=null && i<telefonos.length ; i++ ){
			if ( telefonos[i].tipoTelefono==tipoTelefono )
				telefono = telefonos[i];
		}
		return telefono;
	}



	public static TelefonoVO getTelefonoVO(TelefonoVO telefono, int tipoTelefono, HttpServletRequest request) throws Exception{
		telefono.idCliente = HTMLHelper.getParameterInt(request, "idCliente");
		telefono.idDireccion = 1;
		telefono.tipoTelefono = tipoTelefono;
		switch (tipoTelefono){
		case ClientesConstants.TELEFONO_PRINCIPAL:
			telefono.numeroTelefono = request.getParameter("telefonoPrincipal");
			break;
		case ClientesConstants.TELEFONO_RECADOS:
			telefono.numeroTelefono = request.getParameter("telefonoRecados");
                        telefono.nomContacto = request.getParameter("nomContacto");
			break;
		case ClientesConstants.TELEFONO_CELULAR:
			telefono.numeroTelefono = request.getParameter("telefonoCelular");
		}

		return telefono;
	}



	public static ConyugeVO getConyugeVO (ConyugeVO conyuge, HttpServletRequest request) throws Exception{
		
		conyuge.idCliente = HTMLHelper.getParameterInt(request, "idCliente");
		if ( HTMLHelper.getParameterDate(request, "fechaEvaluacion")!=null )
			conyuge.fechaEvaluacion = new java.sql.Date(HTMLHelper.getParameterDate(request, "fechaEvaluacion").getTime());
		conyuge.nombre = request.getParameter("nombre");
		conyuge.aPaterno = request.getParameter("aPaterno");
		conyuge.aMaterno = request.getParameter("aMaterno");
		conyuge.direccionDomicilio = request.getParameter("direccionDomicilio");
		conyuge.telefonoDomicilio = request.getParameter("telefonoDomicilio");
		conyuge.direccionTrabajo = request.getParameter("direccionTrabajo");
		conyuge.telefonoTrabajo = request.getParameter("telefonoTrabajo");
		conyuge.telefonoCelular = request.getParameter("telefonoCelular");
		conyuge.curp = request.getParameter("curp");
		conyuge.rfc = request.getParameter("rfc");
		conyuge.fechaNacimiento = Convertidor.stringToSqlDate(request.getParameter("fechaNacimiento"));
		conyuge.sexo = HTMLHelper.getParameterInt(request, "sexoconyuge");
		conyuge.formaIngreso = HTMLHelper.getParameterInt(request, "formaingreso");
		conyuge.tipoSector = HTMLHelper.getParameterInt(request, "tiposector");
		conyuge.dependencia = HTMLHelper.getParameterInt(request, "dependencia");
		conyuge.sueldoMensual = HTMLHelper.getParameterDouble(request, "sueldoconyuge");
		
		return conyuge;
		
	}
	
	
	public static ConyugeVO getConyugeVOMantenimiento(ConyugeVO conyuge, HttpServletRequest request) throws Exception{
		HttpSession session = request.getSession();
		ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
		if(conyuge == null)
			conyuge = new ConyugeVO();
		conyuge.idCliente = cliente.idCliente;
		if ( HTMLHelper.getParameterDate(request, "fechaEvaluacion")!=null )
			conyuge.fechaEvaluacion = new java.sql.Date(HTMLHelper.getParameterDate(request, "fechaEvaluacion").getTime());
		conyuge.nombre = request.getParameter("nombreConyuge");
		conyuge.aPaterno = request.getParameter("aPaternoConyuge");
		conyuge.aMaterno = request.getParameter("aMaternoConyuge");
		conyuge.direccionDomicilio = request.getParameter("direccionDomicilioConyuge");
		conyuge.telefonoDomicilio = request.getParameter("telefonoDomicilioConyuge");
		conyuge.direccionTrabajo = request.getParameter("direccionTrabajoConyuge");
		conyuge.telefonoTrabajo = request.getParameter("telefonoTrabajoConyuge");
		conyuge.telefonoCelular = request.getParameter("telefonoCelularConyuge");
		conyuge.curp = request.getParameter("curpConyuge");
		conyuge.rfc = request.getParameter("rfcConyuge");
		conyuge.fechaNacimiento = Convertidor.stringToSqlDate(request.getParameter("fechaNacimientoConyuge"));
		conyuge.sexo = HTMLHelper.getParameterInt(request, "sexoconyuge");
		conyuge.formaIngreso = HTMLHelper.getParameterInt(request, "formaingresoConyuge");
		conyuge.tipoSector = HTMLHelper.getParameterInt(request, "tiposectorConyuge");
		conyuge.dependencia = HTMLHelper.getParameterInt(request, "dependenciaConyuge");
		conyuge.sueldoMensual = HTMLHelper.getParameterDouble(request, "sueldoconyuge");
		return conyuge;
		
	}
	

	public static LimiteCreditoVO getLimiteVO (LimiteCreditoVO limite, HttpServletRequest request) throws Exception{
		
		limite.tasa = HTMLHelper.getParameterInt(request, "tasa");
		limite.plazo = HTMLHelper.getParameterInt(request, "plazoAutorizado");
		limite.comision = HTMLHelper.getParameterInt(request, "comision");
		limite.monto = HTMLHelper.getParameterDouble(request, "montoAutorizado");
		
		limite.garantia = HTMLHelper.getCheckBox(request, "garantia");
		limite.comentarios = request.getParameter("comentarios");
		
		limite.usuario = request.getRemoteUser();
		
		return limite;
		
	}
	
	public static String getNombreCompleto(ClienteVO cliente){
		String nombreCompleto = null;
		if ( cliente.nombre==null && cliente.aPaterno==null && cliente.aMaterno==null )
			nombreCompleto = "";
		else{
			nombreCompleto = HTMLHelper.displayField(cliente.nombre)+" ";
			nombreCompleto += HTMLHelper.displayField(cliente.aPaterno)+" ";
			nombreCompleto += HTMLHelper.displayField(cliente.aMaterno);
			nombreCompleto = nombreCompleto.trim();
		}
		return nombreCompleto;
	}



	public static GrupoVO getGrupoVO (GrupoVO grupo, HttpServletRequest request) throws Exception{
		
		grupo.idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
		grupo.fechaFormacion = Convertidor.toSqlDate(HTMLHelper.getParameterDate(request, "fechaFormacion"));
		grupo.nombre = request.getParameter("nombre").toUpperCase().trim();
		
		return grupo;
		
	}

	public static String formatNombre(String nombre){
		
		StringTokenizer st = new StringTokenizer(nombre);
		String nombreFormatted = "";
		String element = "";
		while (st.hasMoreTokens()){
			element = st.nextToken();
			element = element.trim().toUpperCase();
			nombreFormatted = nombreFormatted + element + " ";
		}

		nombreFormatted = nombreFormatted.trim();

		return nombreFormatted;
	}
	

}