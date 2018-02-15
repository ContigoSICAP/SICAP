/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.commands;

import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.ControlPagosHelper;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.ControlPagosVO;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Alex
 */
public class CommandControlPagos implements Command {

    private String siguiente;

    public CommandControlPagos(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        Notification notificaciones[] = new Notification[1];
        HttpSession session = request.getSession();
        
        try {
            ArrayList<ControlPagosVO> arrPagos = ControlPagosHelper.getControlPagos(request);
            for (ControlPagosVO contPago : arrPagos) {
                System.out.println(contPago.getNumPago()+" "+contPago.getFechaReunion()+" "+contPago.getFechaReal()+" "+contPago.getFechaPago()+" "+
                        contPago.difDiasFechas+" "+contPago.getMontoFichas()+" "+contPago.getMontoPagoCajas());
            }
        } catch (Exception e) {
            Logger.debug("Error: CommandControlPagos");
            Logger.debug(e.toString());
        }
        
        return siguiente;
    }
}
