package com.sicap.clientes.commands;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.ClienteDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.RFCUtil;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.SolicitudVO;


public class CommandRegistraRFCCliente implements Command{


	private String siguiente;


	public CommandRegistraRFCCliente(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		
		int idCliente = 0;
		ClienteVO existeRFC = null;
		HttpSession session = request.getSession();
		ClienteVO cliente = new ClienteVO();
		SolicitudVO solicitud = new SolicitudVO();
		ClienteDAO dao = new ClienteDAO();
		SolicitudDAO solicituddao = new SolicitudDAO();
		Notification notificaciones[] = new Notification[1];
		String regreso = "/nuevoCliente.jsp";
		String nombre = request.getParameter("nombre").trim().toUpperCase();
		String aPaterno = request.getParameter("aPaterno").trim().toUpperCase();
		String aMaterno = request.getParameter("aMaterno").trim().toUpperCase();
		String fechaNacimiento = request.getParameter("fechaNacimiento");
		String RFC = "";
		
		try{
			if (nombre!=null && aPaterno!=null && aMaterno!=null && fechaNacimiento!=null){
				synchronized(this){
					//cliente.rfc = request.getParameter("rfc").toUpperCase();
					RFC = RFCUtil.obtenRFC(nombre.trim().toUpperCase(), aPaterno.trim().toUpperCase(), aMaterno.trim().toUpperCase(), Convertidor.formatDateCirculo(fechaNacimiento));
					existeRFC = dao.getClienteRFC(RFC);
					// Logger.debug("Existe RFC : "+existeRFC.nombreCompleto);
					  if( existeRFC==null ){
					    cliente.rfc = RFC;
					    cliente.nombre = nombre;
                                            if(aPaterno.equals("X"))
                                                aPaterno = "";
                                            if(aMaterno.equals("X"))
                                                aMaterno = "";
					    cliente.aPaterno = aPaterno;
					    cliente.aMaterno = aMaterno;
					    cliente.nombreCompleto = cliente.nombre+" "+cliente.aPaterno+" "+cliente.aMaterno;
					    cliente.fechaNacimiento = Convertidor.toSqlDate(HTMLHelper.getParameterDate(request, "fechaNacimiento"));
					    cliente.entidadNacimiento = HTMLHelper.getParameterInt(request, "entidadNacimiento");
					    cliente.idSucursal = HTMLHelper.getParameterInt(request, "sucursal");
					    //solicitud.tipoOperacion = HTMLHelper.getParameterInt(request, "operacion");
					    cliente.estatus = ClientesConstants.ESTATUS_CAPTURADO;
					    cliente.fechaCaptura = new Timestamp(System.currentTimeMillis()); 
					    idCliente = dao.addCliente(cliente);
					    BitacoraUtil bitutil = new BitacoraUtil(idCliente, request.getRemoteUser(), "CommandRegistraRFCCliente");
					    bitutil.registraEvento(cliente);
				        }
					  
				}
				if ( idCliente!=0 ){
					cliente.idCliente = idCliente;
					solicitud.idCliente = idCliente;
					solicitud.tipoOperacion = HTMLHelper.getParameterInt(request, "operacion");
					solicitud.fechaCaptura = new Timestamp(System.currentTimeMillis());
					solicitud.estatus = ClientesConstants.ESTATUS_CAPTURADO;
					int idSolicitud = solicituddao.addSolicitud(idCliente, solicitud);
					BitacoraUtil bitutil = new BitacoraUtil(idCliente, request.getRemoteUser(), "CommandRegistraRFCCliente");
				    bitutil.registraEvento(solicitud);
				    bitutil.registraCambioEstatus(solicitud, request);
					cliente.solicitudes = solicituddao.getSolicitudes(idCliente);
					session.setAttribute("CLIENTE",cliente);
					request.setAttribute("ID_SOLICITUD",new Integer(idSolicitud));
					notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"El RFC se registr&oacute; correctamente");
					request.setAttribute("NOTIFICACIONES",notificaciones);
				}else{
					notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE,"El RFC ya se encuentra registrado");
					request.setAttribute("NOTIFICACIONES",notificaciones);
					request.setAttribute("RFC_ENCONTRADO",existeRFC);
				}
			}
		}catch(ClientesDBException dbe){
			throw new CommandException(dbe.getMessage());
		}
		catch(Exception e){
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		if ( existeRFC!=null )
			return regreso;
		return siguiente;

	}


}
