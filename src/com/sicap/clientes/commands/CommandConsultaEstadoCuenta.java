package com.sicap.clientes.commands;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.SaldoT24DAO;
import com.sicap.clientes.dao.PagoDAO;
import com.sicap.clientes.dao.cartera.PagoManualDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.IBSHelper;
import com.sicap.clientes.helpers.ibs.BucketHelperIBS;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.SolicitudVO;


public class CommandConsultaEstadoCuenta implements Command{
	
	private String siguiente;
	
	public CommandConsultaEstadoCuenta(String siguiente){
		this.siguiente = siguiente;
	}

	public String execute(HttpServletRequest request) throws CommandException{
		try{
			HttpSession session = request.getSession();
			ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
			Vector<Notification> notificaciones = new Vector<Notification>();

			int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
			int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
			SolicitudVO solicitud = cliente.solicitudes[indiceSolicitud];
			if( solicitud!=null && (solicitud.saldo==null || solicitud.pagosConcentradora==null ) ){
				if ( solicitud.idCreditoIBS != 0 && solicitud.idCuentaIBS != 0 ){
					Logger.debug("entra a la primera opcion");
					solicitud.saldo = new SaldoIBSDAO().getSaldo(cliente.idCliente, solicitud.idSolicitud, solicitud.referencia);
// Se retira la consulta a pagos IBS					
//					solicitud.pagosIBS = new BucketHelperIBS().consultaEstadoBucket(solicitud.idCreditoIBS, notificaciones);
					solicitud.pagos = new PagoDAO().getFechasPagosCliente(solicitud.referencia);
				}
				else  
					solicitud.saldo = IBSHelper.getSaldosT24ToIBS(new SaldoT24DAO().getSaldosT24ByNumClienteCiclo(cliente.idCliente, idSolicitud, solicitud.referencia));
				solicitud.pagosConcentradora = new PagoManualDAO().getPagoConcentradora(cliente.idCliente, idSolicitud, solicitud.referencia);
				
			}
//			else{
//				notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE,"No se tiene información del cliente.");
//				request.setAttribute("NOTIFICACIONES", notificaciones);
//			}
			cliente.solicitudes[indiceSolicitud] = solicitud;
			session.setAttribute("CLIENTE", cliente);
			request.setAttribute("indiceSolicitud", new Integer(indiceSolicitud));
		}catch(Exception e){
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		return siguiente;
	}
}
