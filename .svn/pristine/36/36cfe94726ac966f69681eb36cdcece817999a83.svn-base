package com.sicap.clientes.commands;

import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class CommandCierreDiaHora implements Command {

    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandCierreDiaHora.class);

    public CommandCierreDiaHora(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {
        
        try {
            String horaEspera = HTMLHelper.getParameterString(request, "horaEspera");
            new CatalogoDAO().updateParametro("HORA_CIERRE_MES", horaEspera);
            myLogger.debug("Actualiza tiempo de espera para iniciar cierre a "+horaEspera);
        } catch (Exception e) {
            myLogger.error("Error en CommandCierreDiaHora", e);
        }
        return siguiente;
    }
}
