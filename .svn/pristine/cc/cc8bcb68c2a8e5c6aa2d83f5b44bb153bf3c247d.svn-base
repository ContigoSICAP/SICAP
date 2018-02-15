/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.commands;

import com.sicap.clientes.dao.BitacoraCicloDAO;
import com.sicap.clientes.dao.BitacoraEstatusDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.BitacoraCicloVO;
import com.sicap.clientes.vo.BitacoraEstatusVO;
import com.sicap.clientes.vo.ClienteVO;
import java.util.ArrayList;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

public class CommandHistorialComentariosCliente implements Command {

    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandHistorialComentariosCliente.class);

    public CommandHistorialComentariosCliente(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {
        
        HttpSession session = request.getSession();
        //ClienteVO cliente = (ClienteVO) session.getAttribute("CLIENTE");
        Vector<Notification> notificaciones = new Vector<Notification>();
        ArrayList<BitacoraEstatusVO> registrosCiclo = new ArrayList<BitacoraEstatusVO>();
        BitacoraEstatusDAO bitacoraDao = new BitacoraEstatusDAO();
        myLogger.debug("Variables inicializadas");
        try {
            
            int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
            int idCliente =  HTMLHelper.getParameterInt(request, "idCliente");
            
            myLogger.debug("Obteniendo historial de equipo");
            registrosCiclo = bitacoraDao.getHistorialEquipo(idCliente, idSolicitud);
            if (registrosCiclo.isEmpty()) {
                myLogger.debug("No hay historial");
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No se encontraron registros"));
            }
            request.setAttribute("REGISTROS", registrosCiclo);
            request.setAttribute("NOTIFICACIONES", notificaciones);
        } catch (ClientesDBException dbe) {
            myLogger.error("execute", dbe);
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            myLogger.error("execute", e);
            throw new CommandException(e.getMessage());
        }
        return siguiente;
    }

}
