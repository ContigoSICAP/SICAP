package com.sicap.clientes.commands;

import com.sicap.clientes.dao.BitacoraCicloDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.BitacoraCicloVO;
import java.util.ArrayList;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class CommandBuscaHistorialComentarios implements Command{
    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandBuscaHistorialComentarios.class);

    public CommandBuscaHistorialComentarios(String siguiente) {
        this.siguiente = siguiente;
    }
    
    public String execute(HttpServletRequest request) throws CommandException {
        Vector<Notification> notificaciones = new Vector<Notification>();
        ArrayList<BitacoraCicloVO> registrosCiclo = new ArrayList<BitacoraCicloVO>();
        BitacoraCicloDAO bitacoraDao = new BitacoraCicloDAO();
        myLogger.debug("Variables inicializadas");
        try{
            int numEquipo = HTMLHelper.getParameterInt(request, "idGrupo");
            int numCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
            myLogger.debug("Obteniendo historial de equipo");
            registrosCiclo = bitacoraDao.getHistorialEquipo(numEquipo,numCiclo);
            if(registrosCiclo.isEmpty()){
                myLogger.debug("No hay historial");
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No se encontraron registros"));
            }
            request.setAttribute("REGISTROS", registrosCiclo);
            request.setAttribute("NOTIFICACIONES", notificaciones);
        }catch(ClientesDBException dbe){
            myLogger.error("execute", dbe);
            throw new CommandException(dbe.getMessage());
        } catch(Exception e){
            myLogger.error("execute", e);
            throw new CommandException(e.getMessage());
        }
        return siguiente;
    }    
}
