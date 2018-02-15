package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.ChequesDAO;
import com.sicap.clientes.dao.LoteChequesDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.ChequerasHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.ChequeVO;
import com.sicap.clientes.vo.LoteChequesVO;



public class CommandGuardaChequera implements Command{


	private String siguiente;


	public CommandGuardaChequera(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		Notification notificaciones[] = new Notification[1];
		LoteChequesVO lote = new LoteChequesVO();
		LoteChequesDAO loteDAO = new LoteChequesDAO();
		ChequeVO cheque = new ChequeVO();
		ChequesDAO chequeDAO = new ChequesDAO();
		
		try{

			BitacoraUtil bitutil = new BitacoraUtil(0, request.getRemoteUser(), "CommandGuardaLoteCheques");
			lote = ChequerasHelper.getVO(lote, request);
			int numLote = loteDAO.addLote(lote);
			for ( int i=lote.numChequeIni; i <= lote.numChequeFin; i++){
				cheque = new ChequeVO();
				cheque.numLote = numLote;
				cheque.numCheque = i;
				chequeDAO.addCheque(null, cheque);
			}
			notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Los datos fueron guardados correctamente");
			bitutil.registraEvento(lote);
			request.setAttribute("NOTIFICACIONES",notificaciones);

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
