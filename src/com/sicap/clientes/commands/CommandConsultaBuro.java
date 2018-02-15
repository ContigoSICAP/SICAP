package com.sicap.clientes.commands;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.InformacionCrediticiaDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.ParserStringHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SocketBuro;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.InformacionCrediticiaVO;

public class CommandConsultaBuro implements Command{


	private String siguiente;
	
	public CommandConsultaBuro(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {
		try{
			Calendar cal = Calendar.getInstance();
			Calendar.getInstance();
			Date fechaHoy = cal.getTime();

			String tipoConsulta = request.getParameter("buro");
			HttpSession session = request.getSession();
			Notification notificaciones[] = new Notification[1];
			ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
			InformacionCrediticiaVO infoCredito = new InformacionCrediticiaVO();
			int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
			int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
			String INTL = "";
			String persona = (String)request.getParameter("persona");
			int idObligado = 0;
			idObligado = HTMLHelper.getParameterInt(request, "idObligado");
			request.setAttribute("persona", persona);
			request.setAttribute("idSolicitud", idSolicitud);
			request.setAttribute("idObligado", idObligado);
			
			//Si existe la consulta en BD no llama al servicio de consulta de Buró
			if (tipoConsulta.equalsIgnoreCase("true")){
				//Actualiza la información
				if(persona.equals("cliente"))
					INTL = cliente.solicitudes[indiceSolicitud].infoCreditoBuro.respuesta;
				if(persona.equals("obligado"))
					INTL = cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado - 1].infoCreditoBuro.respuesta;				
			}else{
				//Si no se encontró el registro en BD, forma la cadena de consulta para mandarla a buró de Crédito
				String user = CatalogoHelper.getParametro("USERBURO");
				String pass = CatalogoHelper.getParametro("PASSBURO");
				
				String cadenaConsulta = ParserStringHelper.makeString(cliente, user, pass, idSolicitud, persona, idObligado);
				//Abre socket y envía la cadena de consulta
				INTL = SocketBuro.getStringBuro(cadenaConsulta);
				
				String codErr = INTL.substring(0, 33);
				if(codErr.contains("ERRR")){
					notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE,"Ocurrió un error en la consulta a Buró de crédito, favor de comunicar al administrador del sistema " + codErr);
					request.setAttribute("NOTIFICACIONES",notificaciones);
					infoCredito = null;
				}else{
					if (INTL.substring(47, 48).equals("1")){
						//Almacena la cadena de respuesta de consulta a buró
						InformacionCrediticiaDAO infoCreditoDAO = new InformacionCrediticiaDAO();
						if(persona.equals("cliente")){
							if(cliente.solicitudes[indiceSolicitud].infoCreditoBuro == null){
								infoCredito = new InformacionCrediticiaVO();
								infoCredito.idCliente = cliente.idCliente;
								infoCredito.idSolicitud = idSolicitud;
								infoCredito.idObligado = 0;
								infoCredito.idSociedad = ClientesConstants.SOCIEDAD_BURO;
								infoCredito.idTipoRespuesta = 1;
								infoCredito.respuesta = INTL;
								infoCredito.fechaConsulta = Convertidor.toSqlDate(fechaHoy);
								infoCredito.idProvider = 0;
								infoCreditoDAO.addInfoCrediticia(infoCredito);
								cliente.solicitudes[indiceSolicitud].infoCreditoBuro = infoCredito;
								BitacoraUtil bitutil = new BitacoraUtil(cliente.idCliente, request.getRemoteUser(), "CommandConsultaBuro");
								bitutil.registraEvento("Insertó buró de credito");
							}else{
								infoCredito = new InformacionCrediticiaVO();
								infoCredito = cliente.solicitudes[indiceSolicitud].infoCreditoBuro;
								infoCredito.respuesta = INTL;
								infoCredito.fechaConsulta = Convertidor.toSqlDate(fechaHoy);
								infoCreditoDAO.updateInfoCrediticia(infoCredito);
								cliente.solicitudes[indiceSolicitud].infoCreditoBuro = infoCredito;
								BitacoraUtil bitutil = new BitacoraUtil(cliente.idCliente, request.getRemoteUser(), "CommandConsultaBuro");
								bitutil.registraEvento("Actualizó buró de credito");
							}
						}else if(persona.equals("obligado")){
							if(cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado-1].infoCreditoBuro == null){
								infoCredito = new InformacionCrediticiaVO();
								infoCredito.idCliente = cliente.idCliente;
								infoCredito.idSolicitud = idSolicitud;
								infoCredito.idObligado = idObligado;
								infoCredito.idSociedad = ClientesConstants.SOCIEDAD_BURO;
								infoCredito.idTipoRespuesta = 1;
								infoCredito.respuesta = INTL;
								infoCredito.fechaConsulta = Convertidor.toSqlDate(fechaHoy);
								infoCredito.idProvider = 0;
								infoCreditoDAO.addInfoCrediticia(infoCredito);
								cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado-1].infoCreditoBuro = infoCredito;
								BitacoraUtil bitutil = new BitacoraUtil(cliente.idCliente, request.getRemoteUser(), "CommandConsultaBuro");
								bitutil.registraEvento("Insertó buró de credito obligado");
							}else{
								infoCredito = new InformacionCrediticiaVO();
								infoCredito = cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado-1].infoCreditoBuro;
								infoCredito.respuesta = INTL;
								infoCredito.fechaConsulta = Convertidor.toSqlDate(fechaHoy);
								infoCreditoDAO.updateInfoCrediticia(infoCredito);
								cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado-1].infoCreditoBuro = infoCredito;
								BitacoraUtil bitutil = new BitacoraUtil(cliente.idCliente, request.getRemoteUser(), "CommandConsultaBuro");
								bitutil.registraEvento("Actualizó buró de credito obligado");
							}
						}
					}else{
						cliente.solicitudes[indiceSolicitud].infoCreditoBuro = null;
						if ( persona.equals("obligado") )
							cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado-1].infoCreditoBuro = null;
						}
			}
		}
		session.setAttribute("CLIENTE", cliente);
			
		}catch(Exception e){
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		return siguiente;

	}
}
