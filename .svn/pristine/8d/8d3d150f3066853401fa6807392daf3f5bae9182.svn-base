			package com.sicap.clientes.commands;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.ReporteCobranzaGrupalDAO;
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
import com.sicap.clientes.vo.ReporteCobranzaGrupalVO;


public class CommandGuardaInformeCobranza implements Command{


	private String siguiente;


	public CommandGuardaInformeCobranza(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		Notification notificaciones[] = new Notification[1];
		GrupoVO grupo = new GrupoVO();
		ReporteCobranzaGrupalVO reporteCobranza = new ReporteCobranzaGrupalVO();
		HttpSession session = request.getSession();
		CicloGrupalVO ciclo = new CicloGrupalVO();
		grupo = (GrupoVO)session.getAttribute("GRUPO");
		BitacoraUtil bitutil = new BitacoraUtil(grupo.idGrupo, request.getRemoteUser(), "CommandGuardaReporteCobranza");
		try{
			int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
			int idAlerta = HTMLHelper.getParameterInt(request, "identificadorAlerta");
			ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
			
			//obtengo datos del cuestionario
			GrupoHelper.getCuestionarioCobranza(reporteCobranza, request);
			//Agrego identificador de alerta
			reporteCobranza.idAlerta = ciclo.eventosDePago[idAlerta].identificador;
			//persisto en DB
			new ReporteCobranzaGrupalDAO().addReporteCobranza(reporteCobranza);
			
			GrupoUtil.procesaReporteCobranza(ciclo, reporteCobranza, idAlerta);
			
			//Actualizo el ciclo en el arreglo de grupo ciclos
			GrupoUtil.setCiclo(grupo, ciclo, idCiclo);
			
			bitutil.registraEvento(reporteCobranza);
			notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Se guardo informe con exito para el integrante: "+reporteCobranza.nombreCliente);
			request.setAttribute("NOTIFICACIONES",notificaciones);
			
			request.setAttribute("IDALERTA", idAlerta);	
			request.setAttribute("REPORTE_COBRANZA", reporteCobranza);		
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
