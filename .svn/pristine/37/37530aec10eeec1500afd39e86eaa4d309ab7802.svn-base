			package com.sicap.clientes.commands;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.ReporteVisitaGrupalVO;


public class CommandConsultaInformeVisita implements Command{


	private String siguiente;


	public CommandConsultaInformeVisita(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		//Notification notificaciones[] = new Notification[1];
		GrupoVO grupo = new GrupoVO();
		ReporteVisitaGrupalVO reporteVisita = new ReporteVisitaGrupalVO();
		HttpSession session = request.getSession();
		CicloGrupalVO ciclo = new CicloGrupalVO();
		grupo = (GrupoVO)session.getAttribute("GRUPO");
		try{
			int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
			int idAlerta = HTMLHelper.getParameterInt(request, "idAlerta");
			int numeroAlerta = HTMLHelper.getParameterInt(request, "numeroAlerta");
			int tipoOperacion = HTMLHelper.getParameterInt(request, "tipoOperacionCliente");
			ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
			
			if( ciclo.eventosDePago[idAlerta].reporteVisita != null && ciclo.eventosDePago[idAlerta].reporteVisita.idAlerta==numeroAlerta )
				reporteVisita = ciclo.eventosDePago[idAlerta].reporteVisita;

		request.setAttribute("IDALERTA", idAlerta);	
		request.setAttribute("OPERACION", tipoOperacion);	
		request.setAttribute("REPORTE_VISITA_GRUPAL", reporteVisita);		
		request.setAttribute("CICLO_EVENTOS_PAGO", ciclo);
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
