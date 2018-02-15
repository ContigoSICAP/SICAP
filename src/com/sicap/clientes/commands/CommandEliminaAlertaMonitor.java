package com.sicap.clientes.commands;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.ArchivoAsociadoDAO;
import com.sicap.clientes.dao.EventosPagosGrupalDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.ArchivosAsociadosHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.EventosDePagoVO;
import com.sicap.clientes.vo.GrupoVO;


public class CommandEliminaAlertaMonitor implements Command{


	private String siguiente;


	public CommandEliminaAlertaMonitor(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {
		
		Connection conn  = null;
		GrupoVO grupo = new GrupoVO();
		HttpSession session = request.getSession();
		CicloGrupalVO ciclo = new CicloGrupalVO();
		Notification notificaciones[] = new Notification[1];
		int idCiclo = 0;
		int identificadorAlerta = 0;
		int resultado = 0;
		int motivoEliminacion = 0;
		String comentarios = null;
		boolean eliminoArchivo = false;
		
		try{

			conn = ConnectionManager.getMySQLConnection();
			conn.setAutoCommit(false);
			
			idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
			identificadorAlerta = HTMLHelper.getParameterInt(request, "numeroAlerta");
			motivoEliminacion = HTMLHelper.getParameterInt(request, "motivoEliminar"+identificadorAlerta);
			comentarios = HTMLHelper.getParameterString(request, "comentarioEliminacion");
			
			grupo = (GrupoVO)session.getAttribute("GRUPO");

			if(idCiclo!=0)
				ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
		
			BitacoraUtil bitutil = new BitacoraUtil(grupo.idGrupo, request.getRemoteUser(), "CommandEliminaAlertaMonitor");
			EventosDePagoVO[] alertasTeporal = new EventosDePagoVO[ciclo.eventosDePago.length-1];
			
			int teporal =0;
			int idArreglo = -1;
			for(int i=0; i<ciclo.eventosDePago.length; i++){
				if(ciclo.eventosDePago[i].identificador!=identificadorAlerta){
					alertasTeporal[teporal] = ciclo.eventosDePago[i];
					teporal ++;
				}else
					idArreglo = i;
			}
			
			resultado = new EventosPagosGrupalDAO().eliminaAlertaGrupal(conn, identificadorAlerta);
			
			if( ciclo.archivosAsociados!=null ){
				String ruta = ArchivosAsociadosHelper.getRutaArchivo(ciclo.idGrupo, ciclo.idCiclo, "G");
				for(int i=0; i<ciclo.archivosAsociados.length; i++){
					if(ciclo.archivosAsociados[i].consecutivo == identificadorAlerta){
						File archivo = new File(ruta+ciclo.archivosAsociados[i].nombre);
						eliminoArchivo = archivo.delete();
						new ArchivoAsociadoDAO().deleteArchivoAsociado(conn, grupo.idGrupo, ciclo.idCiclo, ClientesConstants.ARCHIVO_TIPO_REPORTE_VISITA_GRUPAL, identificadorAlerta);
					}			
				}
			}else{
				eliminoArchivo = true;
			}

			
			if ( ciclo.eventosDePago!=null && ciclo.eventosDePago.length>0){
				request.setAttribute("NOMBRE", grupo.nombre);
			}else{
				notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se encontraron datos");				
			}
			
			if(resultado==1 ){//&& eliminoArchivo){
				ciclo.eventosDePago[idArreglo].motivoElminacion = motivoEliminacion;
				ciclo.eventosDePago[idArreglo].comentarioEliminacion = comentarios;
				bitutil.registraEvento(ciclo.eventosDePago[idArreglo]);
				ciclo.eventosDePago =alertasTeporal;
				GrupoUtil.setCiclo(grupo, ciclo, idCiclo);
				conn.commit();
				notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Alerta eliminada correctamente");
			}else{
				conn.rollback();
				notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Ocurrio un error alerta no eliminada");
			}
			
			request.setAttribute("NOTIFICACIONES",notificaciones);
			
			request.setAttribute("CICLO_EVENTOS_PAGO", ciclo);
			session.setAttribute("GRUPO", grupo);
		}catch(ClientesDBException dbe){
			throw new CommandException(dbe.getMessage());
		}
		catch(Exception e){
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}finally {
			try {
				if ( conn!=null ) conn.close();
			}
			catch(SQLException sqle) {
				throw new CommandException(sqle.getMessage());
			}
		}
		return siguiente;

	}


}
