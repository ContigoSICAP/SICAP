package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.GrupoVO;


public class CommandConsultaInformeCobranzaGrupal implements Command{


	private String siguiente;


	public CommandConsultaInformeCobranzaGrupal(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {
		
		GrupoVO grupo = new GrupoVO();
		HttpSession session = request.getSession();
		CicloGrupalVO ciclo = new CicloGrupalVO();
		Notification notificaciones[] = new Notification[1];
		int idCiclo = 0;
		int idAlerta = -1;
		int numeroAlerta = 0;
		try{
			idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
			idAlerta = HTMLHelper.getParameterInt(request, "idAlerta");
			numeroAlerta = HTMLHelper.getParameterInt(request, "numeroAlerta");

			
			if ( idAlerta!=-1 && idCiclo!=0 ){
				grupo = (GrupoVO)session.getAttribute("GRUPO");
				ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
	
				if ( ciclo.eventosDePago!=null && ciclo.eventosDePago.length>0 ){
					request.setAttribute("NOMBRE", grupo.nombre);
				}else{
					notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se encontraron datos");
					request.setAttribute("NOTIFICACIONES",notificaciones);
				}
			}else{
				notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se encontraron datos");
				request.setAttribute("NOTIFICACIONES",notificaciones);
			}
			
			request.setAttribute("IDALERTA", idAlerta);
			request.setAttribute("CICLO_EVENTOS_PAGO", ciclo);
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
