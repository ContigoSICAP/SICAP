package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.ChequesDAO;
import com.sicap.clientes.dao.ClienteDAO;
import com.sicap.clientes.dao.DecisionComiteDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.ChequeVO;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.DecisionComiteVO;
import com.sicap.clientes.vo.SolicitudVO;

public class CommandConsultaCheques implements Command{


	private String siguiente;


	public CommandConsultaCheques(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		int idSucursal = 0;
		int numCheque = 0;
		int idCliente = 0;
		ChequeVO cheque = null;
		ChequesDAO chequeDAO = new ChequesDAO();
		ClienteVO cliente = new ClienteVO();
		DecisionComiteVO decision = new DecisionComiteVO();
		ClienteDAO clienteDAO = new ClienteDAO();
		SolicitudVO[] solicitudes = null;
		DecisionComiteDAO decisionDAO = new DecisionComiteDAO();
		Notification notificaciones[] = new Notification[1];		
		
		try{
			numCheque = HTMLHelper.getParameterInt(request, "numcheque");
			idSucursal = HTMLHelper.getParameterInt(request, "sucursal");
			idCliente = HTMLHelper.getParameterInt(request, "idCliente");
			cheque = chequeDAO.getCheque(numCheque, idCliente, idSucursal);

			if ( cheque!=null ){
				if ( cheque.fechaAsignacion != null ){
					cliente = clienteDAO.getCliente(cheque.numCliente);
					solicitudes = new SolicitudDAO().getSolicitudes(cliente.idCliente);
					cliente.solicitudes = solicitudes;
					decision = decisionDAO.getDecisionComite(cheque.numCliente, cheque.numSolicitud);
					request.setAttribute("CHEQUE", cheque);
					request.setAttribute("CLIENTE", cliente);
					request.setAttribute("DECISION", decision);
				}else{
					request.setAttribute("CHEQUE", cheque);
					request.setAttribute("CLIENTE", cliente);
					request.setAttribute("DECISION", decision);
				}
			}
			else{
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"No se encontró la información del cheque consultado");
				request.setAttribute("NOTIFICACIONES",notificaciones);
			}
		}catch(ClientesDBException dbe){
			throw new CommandException(dbe.getMessage());
		}
		catch(Exception e){
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		return siguiente;
	}


}
