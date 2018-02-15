
package com.sicap.clientes.commands;

import com.sicap.clientes.dao.FondeadorDAO;
import com.sicap.clientes.dao.LineaCreditoDAO;
import com.sicap.clientes.dao.PagareDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.FondeadorVO;
import com.sicap.clientes.vo.LineaCreditoVO;
import com.sicap.clientes.vo.PagareVO;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;


public class CommandConsultaLineasCredito implements Command
{
    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandConsultaLineasCredito.class);

    public CommandConsultaLineasCredito(String siguiente) {
        this.siguiente = siguiente;
    }
    
    public String execute(HttpServletRequest request) throws CommandException {
        
        Notification notificaciones[] = new Notification[1];
        try{
            
                myLogger.debug("Entra a crear Fondeador()");
                //obtenemos los datos de la pantalla altaLineaCredito
                int idLineaCredito = HTMLHelper.getParameterInt(request, "idLineaCredito");
                PagareDAO pagareDAO = new PagareDAO();
                List<PagareVO> pagares = pagareDAO.getPagaresLineaCredito(idLineaCredito,-1);
                
                if(!pagares.isEmpty()){
                    notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Pagares obtenidos correctamente");
                }else{
                    notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se encontraron pagares para la Línea de Crédito");
                } 

            request.setAttribute("PAGARES", pagares);
            request.setAttribute("NOTIFICACIONES", notificaciones);
        } 
        catch (Exception e) 
        {
            myLogger.error("execute", e);
            throw new CommandException(e.getMessage());
        }
        return siguiente;
    }
}
