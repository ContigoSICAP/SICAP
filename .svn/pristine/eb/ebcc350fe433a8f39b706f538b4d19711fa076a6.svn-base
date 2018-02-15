package com.sicap.clientes.commands;

import com.sicap.clientes.dao.GrupoDAO;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import com.sicap.clientes.dao.OrdenDePagoDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.OrdenDePagoVO;
import java.util.ArrayList;

public class CommandConsultaOrdenDePagoGrupal implements Command{

	private String siguiente;

	public CommandConsultaOrdenDePagoGrupal(String siguiente) {
		this.siguiente = siguiente;
	}

	public String execute(HttpServletRequest request) throws CommandException {
		Vector<Notification> notificaciones = new Vector<Notification>();
		
		try{
			int numGrupo = HTMLHelper.getParameterInt(request, "numGrupo");
			int numCiclo = HTMLHelper.getParameterInt(request, "numCiclo");
                        GrupoDAO grupoDao = new GrupoDAO();
			OrdenDePagoDAO opagodao = new OrdenDePagoDAO();
			ArrayList<OrdenDePagoVO> ordenesDePagoEnviadas =  new ArrayList<OrdenDePagoVO>();
                        String nombreGrupo = grupoDao.getNombreGrupo(numGrupo);
                        ordenesDePagoEnviadas = opagodao.getOrdenesPagoEnviadas(numGrupo, numCiclo);
			if( ordenesDePagoEnviadas.isEmpty() ){
                            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE,"No se encontraron Órdenes de Pago enviadas a banco para el equipo "+numGrupo+" ciclo "+numCiclo));
			}                        
                        request.setAttribute("NOTIFICACIONES",notificaciones);
			request.setAttribute("ORDENESDEPAGO", ordenesDePagoEnviadas);
                        request.setAttribute("NOMBREGRUPO", nombreGrupo);
                        request.setAttribute("NUMEROGRUPO", numGrupo);
                        request.setAttribute("NUMEROCICLO", numCiclo);
		}
                catch(ClientesDBException dbe){
			throw new CommandException(dbe.getMessage());
		}
		catch(Exception e){
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}		
		return siguiente;
	}


}