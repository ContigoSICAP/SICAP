package com.sicap.clientes.commands;

import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.cartera.CreditoCartDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.SaldoIBSVO;

public class CommandConsultaSaldoFavor implements Command{

	private String siguiente;

	public CommandConsultaSaldoFavor(String siguiente) {
		this.siguiente = siguiente;
	}

	public String execute(HttpServletRequest request) throws CommandException {
		Vector<Notification> notificaciones = new Vector<Notification>();

		try{
                    int numEquipo = HTMLHelper.getParameterInt(request, "numEquipo");
                    int numCiclo = HTMLHelper.getParameterInt(request, "numCiclo");
                    SaldoIBSVO saldoFavor = new SaldoIBSVO();
                    SaldoIBSDAO saldoDAO = new SaldoIBSDAO();
                    CreditoCartDAO creditoDAO = new CreditoCartDAO();
                    saldoFavor=saldoDAO.getSaldoFavor(numEquipo, numCiclo);
                    if( saldoFavor == null || saldoFavor.getSaldoBucket()!=creditoDAO.getMontoCuenta(numEquipo, numCiclo)){
                        notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE,"No Se Encuentra Saldo a Favor Con Los Datos Proporcionados"));
                        saldoFavor = null;
                    }
                    
                    request.setAttribute("NOTIFICACIONES",notificaciones);
                    request.setAttribute("SALDOAFAVOR", saldoFavor);
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