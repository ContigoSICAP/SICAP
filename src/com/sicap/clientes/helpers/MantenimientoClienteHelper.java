package com.sicap.clientes.helpers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.DireccionDAO;
import com.sicap.clientes.dao.TelefonoDAO;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.ConyugeVO;
import com.sicap.clientes.vo.DireccionVO;
import com.sicap.clientes.vo.TelefonoVO;

public class MantenimientoClienteHelper {

	public static void asignaValoresCliente(HttpServletRequest request){
		
		HttpSession session = request.getSession();
		ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
		cliente.idCliente = Integer.parseInt(request.getParameter("idCliente"));
		cliente.rfc = request.getParameter("rfc");
		cliente.nombre = request.getParameter("nombre");
		cliente.aPaterno = request.getParameter("aPaterno");
		cliente.aMaterno = request.getParameter("aMaterno");
		cliente.nombreCompleto = cliente.nombre + " " + cliente.aPaterno + " " + cliente.aMaterno; 
		cliente.idGrupo = Integer.parseInt(request.getParameter("idGrupo"));
		cliente.dependientesEconomicos = Integer.parseInt(request.getParameter("idDepEconomicos"));
		cliente.nivelEstudios = Integer.parseInt(request.getParameter("idNivelEst"));
		cliente.tipoIdentificacion = Integer.parseInt(request.getParameter("idIdentificacion"));
		cliente.numeroIdentificacion = request.getParameter("numeroIdentificacion");
		cliente.estadoCivil = Integer.parseInt(request.getParameter("estadoCivil"));
		cliente.correoElectronico = request.getParameter("correoElectronico");
		Logger.debug("Cliente:::" + cliente.toString());
	}
	
	public static void asignaValoresConyuge(HttpServletRequest request){
		
		HttpSession session = request.getSession();
		ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
		ConyugeVO conyuge = cliente.conyuge;
		conyuge.nombre = request.getParameter("nombreConyuge");
		conyuge.aPaterno = request.getParameter("aPaternoConyuge");
		conyuge.aMaterno = request.getParameter("aMaternoConyuge");
		conyuge.direccionDomicilio = request.getParameter("dirDomicilio");
		conyuge.telefonoDomicilio = request.getParameter("telDomicilio");
		conyuge.telefonoCelular = request.getParameter("telCelular");
		conyuge.direccionTrabajo = request.getParameter("dirTrabajo");
		conyuge.telefonoTrabajo = request.getParameter("telTrabajo");
		conyuge.sueldoMensual = Double.parseDouble(request.getParameter("sueldoMensual"));
		conyuge.formaIngreso = Integer.parseInt(request.getParameter("idIngreso"));
		conyuge.tipoSector = Integer.parseInt(request.getParameter("idSector"));
		conyuge.dependencia = Integer.parseInt(request.getParameter("idDependencia"));
		
		Logger.debug("Conyuge:::" + conyuge.toString());
	}
	
	public static void asignaDireccionCliente(HttpServletRequest request)throws ClientesException{
		try{
			HttpSession session = request.getSession();
			ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
			
			DireccionVO[] direccion = cliente.direcciones;
			if(direccion == null)
				direccion = new DireccionDAO().getDirecciones(cliente.idCliente);
			direccion[0] = new DireccionVO();
			direccion[0].cp = request.getParameter("cp");
			direccion[0].estado = request.getParameter("estado");
			direccion[0].municipio = request.getParameter("municipio");
			direccion[0].colonia = request.getParameter("colonia");
			direccion[0].localidad = request.getParameter("localidad");
			direccion[0].calle = request.getParameter("calle");
			direccion[0].numeroExterior = request.getParameter("numeroExterior");
			direccion[0].numeroInterior = request.getParameter("numeroInterior");
			
			Logger.debug("Direccion:::" + direccion[0].toString());	
		}catch(ClientesException exc){
			exc.printStackTrace();
			throw new ClientesException(exc.getMessage());
		}
	}
	
	public static void asignaTelefonosCliente(HttpServletRequest request)throws ClientesException{
		
		try{
			HttpSession session = request.getSession();
			ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
			
			DireccionVO direccion = cliente.direcciones[0];
			if(direccion.telefonos!=null){
				TelefonoVO telefonoPrincipal = direccion.telefonos[0];
				TelefonoVO telefonoRecados   = direccion.telefonos[1];
				TelefonoVO telefonoCelular   = direccion.telefonos[2];
				
				telefonoPrincipal.numeroTelefono = request.getParameter("telefonoPrincipal");
				telefonoRecados.numeroTelefono   = request.getParameter("telefonoRecados");
				telefonoCelular.numeroTelefono   = request.getParameter("telefonoCelular");
			}else{
				direccion.telefonos = new TelefonoDAO().getTelefonos(cliente.idCliente, direccion.idDireccion);
				TelefonoVO telefonoPrincipal = new TelefonoVO();
				TelefonoVO telefonoRecados   = new TelefonoVO();
				TelefonoVO telefonoCelular   = new TelefonoVO();
				
				telefonoPrincipal.idCliente = cliente.idCliente;
				telefonoPrincipal.idDireccion = direccion.idDireccion;
				telefonoPrincipal.idTelefono = 1;
				telefonoPrincipal.tipoTelefono = 1;
				telefonoPrincipal.numeroTelefono = request.getParameter("telefonoPrincipal");
				
				telefonoRecados.idCliente = cliente.idCliente;
				telefonoRecados.idDireccion = direccion.idDireccion;
				telefonoRecados.idTelefono = 1;
				telefonoRecados.tipoTelefono = 2;
				telefonoRecados.numeroTelefono   = request.getParameter("telefonoRecados");
				
				telefonoCelular.idCliente = cliente.idCliente;
				telefonoCelular.idDireccion = direccion.idDireccion;
				telefonoCelular.idTelefono = 1;
				telefonoCelular.tipoTelefono = 3;
				telefonoCelular.numeroTelefono   = request.getParameter("telefonoCelular");
				
				TelefonoVO[] telefonos = {telefonoPrincipal, telefonoRecados, telefonoCelular};
				
				direccion.telefonos = telefonos;	
			}
		}catch(ClientesException exc){
			exc.printStackTrace();
			throw new ClientesException(exc.getMessage());
		}
	}
}