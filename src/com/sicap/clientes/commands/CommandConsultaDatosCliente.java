package com.sicap.clientes.commands;

import com.sicap.clientes.dao.ClienteDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.ClienteVO;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;

public class CommandConsultaDatosCliente implements Command{
    
    private String siguiente;
    
    public CommandConsultaDatosCliente(String siguiente){
        this.siguiente=siguiente;
    }
    
    public String execute(HttpServletRequest request) throws CommandException {
        Vector<Notification> notificaciones = new Vector<Notification>();
        
        try{
            int numCliente = HTMLHelper.getParameterInt(request, "numCliente");
            ClienteDAO clienteDAO = new ClienteDAO();
            ClienteVO cliente = new ClienteVO();
            cliente = clienteDAO.getCliente(numCliente);
            if(cliente==null){
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE,"No se encontraron clientes"));
            }
            request.setAttribute("NOTIFICACIONES", notificaciones);
            request.setAttribute("CLIENTE", cliente);            
        }
        catch(ClientesDBException dbe){
            throw new CommandException(dbe.getMessage());
        }
        catch(Exception e){
            throw new CommandException(e.getMessage());
        }
        return siguiente;
    }    
}
