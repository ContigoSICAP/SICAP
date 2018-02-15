package com.sicap.clientes.commands;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.GrupoDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.GrupoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.GrupoVO;
import java.util.Vector;


public class CommandGuardaGrupo implements Command{


	private String siguiente;


	public CommandGuardaGrupo(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		Vector<Notification> notificaciones = new Vector<Notification>();
		HttpSession session = request.getSession();
		GrupoVO grupo = new GrupoVO();
		GrupoDAO grupodao = new GrupoDAO();
		BitacoraUtil bitutil = new BitacoraUtil(grupo.idGrupo, request.getRemoteUser(), "CommandGuardaGrupo");
		try{
			int idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
			if ( idGrupo==0 ){
				grupo = new GrupoVO();
				grupo = GrupoHelper.getGrupoVO(grupo, request);
                                if(!CatalogoHelper.esSucursaldeIxaya(grupo.sucursal)){
                                    if( grupo.idOperacion==ClientesConstants.GRUPAL )
                                            grupo.calificacion = "AA";
                                    else if( grupo.idOperacion==ClientesConstants.REESTRUCTURA_GRUPAL )
                                            grupo.calificacion = "RG";
                                    grupo.estatus = ClientesConstants.ESTATUS_CAPTURADO;
                                    grupo.fechaCaptura = new Timestamp(System.currentTimeMillis());
                                    if ( !grupodao.exists(grupo) ){
                                            grupodao.addGrupo(grupo);
                                            //bitutil = new BitacoraUtil(grupo.idGrupo, request.getRemoteUser(), "CommandGuardaGrupo");
                                            notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron guardados correctamente"));
                                    }else{
                                        notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "El nombre del grupo ya existe"));
                                    }
                                }else{
                                    notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "El movimiento se debe de realizar en el sistema JUCAVI"));
                                }
			}else{
				GrupoVO grupoAntes = (GrupoVO)session.getAttribute("GRUPO");
				grupo = grupoAntes;
				grupo.calificacionAnterior = grupoAntes.calificacion;
				grupo = GrupoHelper.getGrupoVO(grupo, request);
				//bitutil = new BitacoraUtil(grupo.idGrupo, request.getRemoteUser(), "CommandGuardaGrupo");
				grupodao.updateGrupo(grupo);
				notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron actualizados correctamente"));
			}
                        bitutil.registraEventoString("Grupo= "+grupo.nombre+" idSucursal= "+grupo.sucursal+" "+notificaciones.get(0).text);
			request.setAttribute("NOTIFICACIONES", notificaciones);
			//Actualiza el objeto cliente
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
