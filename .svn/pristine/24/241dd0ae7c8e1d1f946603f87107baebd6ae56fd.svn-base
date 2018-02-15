package com.sicap.clientes.commands;

import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.vo.UsuarioVO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

public class CommandIdentificaPersonal implements Command{
    
    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandIdentificaPersonal.class);

	public CommandIdentificaPersonal(String siguiente) {
		this.siguiente = siguiente;
	}
        
        public String execute(HttpServletRequest request) throws CommandException {
            
            UsuarioVO usuario = null;
            HttpServletRequest req = (HttpServletRequest) request;
            HttpSession session = req.getSession();
            try {
                usuario = (UsuarioVO)session.getAttribute("USUARIO");
                if(usuario.idSucursal == 33)
                    siguiente = "/consultaGrupos.jsp";
                else
                    siguiente = "/buscaClientePorRFC.jsp";
            } catch (Exception e) {
                myLogger.error("Error dentro de CommandIdentificaPersonal", e);
            }
            return siguiente;
        }
}
