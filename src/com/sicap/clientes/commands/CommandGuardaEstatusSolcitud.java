package com.sicap.clientes.commands;

import com.sicap.clientes.exceptions.CommandException;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.SolicitudVO;
import java.util.Vector;
import javax.servlet.http.HttpSession;

/**
 *
 * @author LDAVILA
 */
public class CommandGuardaEstatusSolcitud implements Command {
    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandGuardaDecisionComite.class);
    
    public CommandGuardaEstatusSolcitud(String Siguiente){
        this.siguiente = Siguiente;
    }
     public String execute(HttpServletRequest request) throws CommandException {
         Notification notificaciones[] = new Notification[1];
         HttpSession session = request.getSession();
         SolicitudDAO solicituddao = new SolicitudDAO();
         try{
             int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
             ClienteVO cliente = (ClienteVO) session.getAttribute("CLIENTE");
             BitacoraUtil bitutil = new BitacoraUtil(cliente.idCliente, request.getRemoteUser(), "CommandGuardaEstatusSolcitud");
             int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
             if(cliente.solicitudes[indiceSolicitud].estatus==ClientesConstants.SOLICITUD_NUEVA){
                 cliente.solicitudes[indiceSolicitud].estatus=ClientesConstants.SOLICITUD_EN_ANALISIS;
                 notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "El cliente fue enviado a Mesa de control con el estatus de Analisis"); 
             }else if(cliente.solicitudes[indiceSolicitud].estatus==ClientesConstants.SOLICITUD_PENDIENTE){
                 cliente.solicitudes[indiceSolicitud].estatus=ClientesConstants.SOLICITUD_REVALORACION;
                 notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "El cliente fue enviado a Mesa de control con el estatus de Revaloracion");
             }
             solicituddao.updateSolicitud(cliente.idCliente, cliente.solicitudes[indiceSolicitud]);
             bitutil.registraCambioEstatus(cliente.solicitudes[indiceSolicitud], request);
             request.setAttribute("NOTIFICACIONES", notificaciones);
             session.setAttribute("CLIENTE", cliente);
         }catch (Exception e) {
            myLogger.error(e);
            throw new CommandException(e.getMessage());
        }
         return siguiente;
     }
}
