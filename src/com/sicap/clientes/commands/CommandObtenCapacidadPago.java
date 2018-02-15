package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.ScoringDAO;
import com.sicap.clientes.dao.RepresentantesDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.SolicitudHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.ScoringUtil;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.ScoringVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.RepresentantesVO;


public class CommandObtenCapacidadPago implements Command{


	private String siguiente;


	public CommandObtenCapacidadPago(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		Notification notificaciones[] = new Notification[1];
		HttpSession session = request.getSession();
		ScoringDAO scoringdao = new ScoringDAO();
		ScoringVO score = new ScoringVO();
		SolicitudVO solicitud = new SolicitudVO();
		ScoringUtil su = null;
		RepresentantesVO  convenio    = new RepresentantesVO();
		RepresentantesDAO convenioDAO = new RepresentantesDAO();
		try{
			int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
			ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
			int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
			solicitud = cliente.solicitudes[indiceSolicitud];
			score = SolicitudHelper.getCapacidadPago(score, request);
			BitacoraUtil bitutil = new BitacoraUtil(cliente.idCliente, request.getRemoteUser(), "CommandObtenScore");
			convenio = convenioDAO.getRepresentante(cliente.solicitudes[indiceSolicitud].numrepresentante);
			su = new ScoringUtil(solicitud.tipoOperacion);

			su.getDictamenDescuento(score, convenio.factor);
			if ( cliente.solicitudes[indiceSolicitud].scoring==null )
				scoringdao.addScoring(score);
			else
				scoringdao.updateScoring(score);

			bitutil.registraEvento( score.toString() );
			
			request.setAttribute("NOTIFICACIONES",notificaciones);

			//Actualiza el cliente en sesion
			//session.setAttribute("CLIENTE", cliente);
			cliente.solicitudes[indiceSolicitud].scoring = score;
			//request.setAttribute("SCORE", score);

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