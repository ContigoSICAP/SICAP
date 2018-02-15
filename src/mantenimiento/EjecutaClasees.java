/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mantenimiento;

import com.sicap.clientes.commands.Command;
import com.sicap.clientes.exceptions.CommandException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Alex
 */
public class EjecutaClasees implements Command{
    
    private String siguiente;
    
    public EjecutaClasees(String siguiente) {
        this.siguiente = siguiente;
    }
    
    public String execute(HttpServletRequest request) throws CommandException {
        
        new AjustaTipoCliente().updateTipoCliente();
        
        return siguiente;
    }
}
