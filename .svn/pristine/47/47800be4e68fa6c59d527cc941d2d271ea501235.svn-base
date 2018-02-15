package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.EventosPagosGrupalDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.GrupoVO;
import java.util.Vector;


public class CommandIgnorarAlertasFuturas implements Command{


	private String siguiente;


	public CommandIgnorarAlertasFuturas(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		Vector<Notification> notificaciones = new Vector<Notification>();
		HttpSession session = request.getSession();
		GrupoVO grupo = new GrupoVO();
		CicloGrupalVO ciclo = new CicloGrupalVO();
		BitacoraUtil bitutil = null;
		Object bitacoraTemporal= null;
		int motivoIgnorarFuturasAlertas=0;
		String comentarios = null;
		
		try{
			int idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
			int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
			
			motivoIgnorarFuturasAlertas = HTMLHelper.getParameterInt(request, "motivoIgnorarAlertas");
			comentarios = HTMLHelper.getParameterString(request, "comentarioIgnorarFuturasAlertas");
			
			if ( idGrupo!=0 && idCiclo!=0 ){
				grupo = (GrupoVO)session.getAttribute("GRUPO");
				bitutil = new BitacoraUtil(grupo.idGrupo, request.getRemoteUser(), "CommandIgnorarAlertasFuturas");
				bitacoraTemporal = "idGrupo=["+idGrupo+"],idCiclo=["+idCiclo+"],motivoIgnorarFuturasAlertas=["+motivoIgnorarFuturasAlertas+"],comentarios=["+comentarios+"]";
				
				if( grupo.ciclos!=null && grupo.ciclos.length>0 ){
					ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
					if( ciclo!=null && ciclo.idGrupo!=0 ){
						ciclo.estatusAlertasPago = 2;
						new EventosPagosGrupalDAO().desactivaFuturasAletas(ciclo);
						GrupoUtil.setCiclo(grupo, ciclo, idCiclo);
						notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron guardados correctamente"));
					}else{
						notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No se encontro ciclo"));
					}
				}else{
					notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No se encontro grupo"));
				}
			}else{
				notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No se ignoraran futuras alertas devido a un problema"));
			}
			bitutil.registraEvento(bitacoraTemporal);
			request.setAttribute("NOTIFICACIONES", notificaciones);
			
			session.setAttribute("GRUPO", grupo);

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
