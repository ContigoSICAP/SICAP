package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.EjecutivoCreditoDAO;
import com.sicap.clientes.dao.GrupoDAO;
import com.sicap.clientes.dao.RepresentantesDAO;
import com.sicap.clientes.dao.cartera.CreditoCartDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.EjecutivoCreditoVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.RepresentantesVO;
import com.sicap.clientes.vo.cartera.CreditoCartVO;


public class CommandModificarEjecutivo implements Command{

	private String siguiente;

	public CommandModificarEjecutivo(String siguiente) {
		this.siguiente = siguiente;
	}

	public String execute(HttpServletRequest request) throws CommandException {

		Notification notificaciones[] = new Notification[1];
		int idSucursal = 0;
		int idEjecutivo = 0;
		String nombre = null;
		String aPaterno = null;
		String aMaterno = null;		
		String estatus = null;
                int tipoEjecutivo = 0;
                int upline = 0;
                int actualizar = 0;
		try{
			idSucursal = HTMLHelper.getParameterInt(request, "idSucursal");
			idEjecutivo = HTMLHelper.getParameterInt(request, "idEjecutivo");
			nombre = HTMLHelper.getParameterString(request,"nombre");
			aPaterno = HTMLHelper.getParameterString(request,"aPaterno");
			aMaterno = HTMLHelper.getParameterString(request,"aMaterno");
			estatus = HTMLHelper.getParameterString(request, "estatus");
                        tipoEjecutivo = HTMLHelper.getParameterInt(request, "tipoEjecutivo");
                        upline = HTMLHelper.getParameterInt(request, "upline");
                        BitacoraUtil bitutil = new BitacoraUtil(idEjecutivo, request.getRemoteUser(), "CommandModificarEjecutivo");
                        if(idSucursal>0) {
				if(idEjecutivo>0) {
					EjecutivoCreditoVO ejecutivo = new EjecutivoCreditoVO();
					EjecutivoCreditoDAO ejecutivodao = new EjecutivoCreditoDAO();
					ejecutivo = ejecutivodao.getEjecutivo(idEjecutivo);
                                        int tipoEjecutivoUpline = ejecutivodao.tipoEjecutivo(upline);
                                        //ejecutivo.idSucursal =idSucursal;
					ejecutivo.nombre =nombre;
					ejecutivo.aPaterno =aPaterno;
					ejecutivo.aMaterno=aMaterno;
					ejecutivo.estatus=estatus;
                                        ejecutivo.tipoEjecutivo=tipoEjecutivo;
                                        if( HTMLHelper.getIdAltaStatusEjecutivo(estatus).equals("A")) 
						ejecutivo.estatus = "A";
					else
						ejecutivo.estatus = "B";
					if ((!ejecutivodao.tieneCartera(idEjecutivo))||((ejecutivo.estatus.equals("A"))&&(idSucursal==ejecutivo.idSucursal))){
                                            if (ejecutivo.tipoEjecutivo==1||ejecutivo.tipoEjecutivo>tipoEjecutivoUpline){
                                                ejecutivo.idSucursal =idSucursal;
                                                ejecutivo.upline=upline;
                                                actualizar = ejecutivodao.updateEjecutivo(ejecutivo);
                                                notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Se actualizó la información del ejecutivo correctamente");
                                                bitutil.registraEventoString("idEjecutivo= "+idEjecutivo+" Nombre= "+nombre+" "+aPaterno+" "+aMaterno+" Sucursal= "+idSucursal+" Tipo= "+tipoEjecutivo+" Upline= "+upline+" "+notificaciones[0].text);
						request.setAttribute("NOTIFICACIONES",notificaciones);
                                            }
                                            else {
						notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No es posible actualizar la información. El jefe inmediato debe ser de nivel más alto");
						request.setAttribute("NOTIFICACIONES",notificaciones);
                                            }
					}
                                        else {
						notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No es posible actualizar la información. El ejecutivo tiene equipos activos");
						request.setAttribute("NOTIFICACIONES",notificaciones);
                                        }
                                        request.setAttribute("EJECUTIVO",ejecutivo);
                                        request.setAttribute("ACTUALIZACION_EJECUTIVO", actualizar);
                                        
				}  //fin if
				else {
					notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se ha seleccionado a ning&uacute;n ejecutivo");
					request.setAttribute("NOTIFICACIONES",notificaciones);
				}	//fin else
			}  //fin if
			else {
				notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se ha seleccionado una sucursal");
				request.setAttribute("NOTIFICACIONES",notificaciones);
			}	//fin else
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
