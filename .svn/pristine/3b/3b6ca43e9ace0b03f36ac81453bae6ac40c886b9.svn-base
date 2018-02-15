/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.commands;

import com.sicap.clientes.dao.bsc.MigracionInfoDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Alex
 */
public class CommandMigracionSemanal implements Command {
    
    private String siguiente;

    public CommandMigracionSemanal(String siguiente) {
        this.siguiente = siguiente;
    }
    
    public String execute(HttpServletRequest request) throws CommandException {
        
        Notification mensaje[] = new Notification[1];
        MigracionInfoDAO migraDAO = new MigracionInfoDAO();
        try {
            migraDAO.MigracionDatosBSC();
            mensaje[0] = new Notification(ClientesConstants.INFO_LEVEL, "DATOS MIGRADOS A BSC");
            request.setAttribute("NOTIFICACIONES", mensaje);
            
        } catch (Exception e) {
            mensaje[0] = new Notification(ClientesConstants.ERROR_LEVEL, "MIGRACION FALLIDA");
        }
        request.setAttribute("NOTIFICACIONES", mensaje);
        
        return siguiente;
        
    }
    
}
