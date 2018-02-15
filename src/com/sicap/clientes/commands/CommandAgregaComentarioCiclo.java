package com.sicap.clientes.commands;

import com.sicap.clientes.dao.BitacoraCicloDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.BitacoraCicloVO;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class CommandAgregaComentarioCiclo implements Command{
    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandAgregaComentarioCiclo.class);

    public CommandAgregaComentarioCiclo(String siguiente) {
        this.siguiente = siguiente;
    }
    
    public String execute(HttpServletRequest request) throws CommandException{
        Vector<Notification> notificaciones = new Vector<Notification>();
        BitacoraCicloVO bitacora = new BitacoraCicloVO();
        BitacoraCicloDAO bitacoraDao = new BitacoraCicloDAO();
        myLogger.debug("Variables inicializadas");
        try {
            int idGrupo= HTMLHelper.getParameterInt(request, "idGrupo");
            int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
            bitacora.setIdEquipo(idGrupo);
            bitacora.setIdCiclo(idCiclo);
            bitacora.setEstatus(HTMLHelper.getParameterInt(request, "estatus"));
            bitacora.setIdComentario(bitacoraDao.getNumComentario(idGrupo, idCiclo)+1);
            bitacora.setComentario(HTMLHelper.getParameterString(request, "comentario"));
            bitacora.setUsuarioComentario(request.getRemoteUser());
            bitacora.setUsuarioAsignado(bitacoraDao.getUsuarioAsignado(idGrupo, idCiclo));
            myLogger.info("Insertando registro en bitácora: "+ bitacora.toString());
            bitacoraDao.insertaBitacoraCiclo(null, bitacora);
            notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Se agrega el comentario correctamente"));
            request.setAttribute("NOTIFICACIONES", notificaciones);
        } catch(ClientesDBException dbe){
            myLogger.error("CommandAgregaComentarioCiclo", dbe);
            throw new CommandException(dbe.getMessage());
        } catch(Exception e){
            e.printStackTrace();
            myLogger.error("CommandAgregaComentarioCiclo", e);
            throw new CommandException(e.getMessage());
        }
        return siguiente;
    }
    
    
}
