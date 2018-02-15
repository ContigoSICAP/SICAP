package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.EjecutivoCreditoDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.EjecutivoCreditoVO;


public class CommandGuardaEjecutivos implements Command{


	private String siguiente;


	public CommandGuardaEjecutivos(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {
		Notification notificaciones[] = new Notification[1];
		
		try{
			//int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
			int sucursal = HTMLHelper.getParameterInt(request, "idSucursal");
			String nombre = HTMLHelper.getParameterString(request, "nombre");
			String aPaterno = HTMLHelper.getParameterString(request, "aPaterno");
			String aMaterno = HTMLHelper.getParameterString(request, "aMaterno");
                        int tipo = HTMLHelper.getParameterInt(request, "tipoEjecutivo");
                        int upline = HTMLHelper.getParameterInt(request, "upline");
                        BitacoraUtil bitutil = new BitacoraUtil(sucursal, request.getRemoteUser(), "CommandGuardaEjecutivos");
                        int idEjecutivo = 0;
                        			
			EjecutivoCreditoDAO ejecutivodao = new EjecutivoCreditoDAO();
			EjecutivoCreditoVO ejecutivo = new EjecutivoCreditoVO();
                        int tipoEjecutivoUpline = ejecutivodao.tipoEjecutivo(upline);			
			ejecutivo.idSucursal = sucursal;
			ejecutivo.nombre = nombre;
			ejecutivo.aPaterno = aPaterno;
			ejecutivo.aMaterno = aMaterno;
			ejecutivo.estatus = "A";	//se asume que al registrarse se da de alta
                        ejecutivo.tipoEjecutivo = tipo;
                        ejecutivo.usuario = request.getRemoteUser();
                        if (ejecutivo.tipoEjecutivo==1||ejecutivo.tipoEjecutivo>tipoEjecutivoUpline){
                            ejecutivo.upline = upline;
                            idEjecutivo = ejecutivodao.addEjecutivo(ejecutivo);
                            notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE,"Los datos fueron guardados correctamente. ID Colaborador "+idEjecutivo);
                            bitutil.registraEventoString("Sucursal= "+sucursal+" Nombre= "+nombre+" "+aPaterno+" "+aMaterno+" Tipo= "+tipo+" Upline= "+upline+" "+notificaciones[0].text);
                        }
                        else {
                            notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No es posible guardar la información. Debe seleccionar un jefe inmediato de nivel más alto");
                            request.setAttribute("NOTIFICACIONES",notificaciones);
                        }
                            request.setAttribute("NOTIFICACIONES",notificaciones);
                            request.setAttribute("EJECUTIVOS", ejecutivo);
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
