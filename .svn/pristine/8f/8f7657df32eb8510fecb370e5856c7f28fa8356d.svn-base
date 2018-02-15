			package com.sicap.clientes.commands;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.ReporteVisitaGrupalDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.GrupoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.ReporteVisitaGrupalVO;


public class CommandGuardaInformeVisitaGrupal implements Command{


	private String siguiente;


	public CommandGuardaInformeVisitaGrupal(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		Notification notificaciones[] = new Notification[1];
		GrupoVO grupo = new GrupoVO();
		ReporteVisitaGrupalVO reporteVisita = new ReporteVisitaGrupalVO();
		HttpSession session = request.getSession();
		CicloGrupalVO ciclo = new CicloGrupalVO();
		grupo = (GrupoVO)session.getAttribute("GRUPO");
		BitacoraUtil bitutil = new BitacoraUtil(grupo.idGrupo, request.getRemoteUser(), "CommandGuardaInformeVisitaGrupal");
		try{
			int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
			int idAlerta = HTMLHelper.getParameterInt(request, "identificadorAlerta");
			ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
			
			//obtengo datos del cuestionario
			GrupoHelper.getReporteVisitaGrupal(reporteVisita, request, ciclo);
			
			
			//persisto en DB
			new ReporteVisitaGrupalDAO().addReporteVisitaGrupal(reporteVisita);
			
			GrupoUtil.procesaInformeVisita(ciclo, reporteVisita, idAlerta);
			
			//Actualizo el ciclo en el arreglo de grupo ciclos
			GrupoUtil.setCiclo(grupo, ciclo, idCiclo);
			
			bitutil.registraEvento(reporteVisita);
			notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Se guardo informe de visita.");
			request.setAttribute("NOTIFICACIONES",notificaciones);
			
			request.setAttribute("IDALERTA", idAlerta);		
			request.setAttribute("CICLO_EVENTOS_PAGO", ciclo);
			session.setAttribute("GRUPO", grupo);	
			//Actualiza el cliente en sesion
			//session.setAttribute("CLIENTE", cliente);
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
