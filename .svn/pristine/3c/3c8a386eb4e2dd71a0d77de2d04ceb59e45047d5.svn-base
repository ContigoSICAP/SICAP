package com.sicap.clientes.commands;

import com.sicap.clientes.dao.DAOMaster;
import com.sicap.clientes.dao.PagoReferenciadoDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.PagoVO;
import javax.servlet.http.HttpServletRequest;

public class CommandEliminaIncidencia extends DAOMaster implements Command{
    private String siguiente;
    public CommandEliminaIncidencia(String siguiente){
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {
        Notification notificaciones[] = new Notification[1];
        PagoReferenciadoDAO incidenciaDao = new PagoReferenciadoDAO();
        try {
            BitacoraUtil bitacora = new BitacoraUtil(0, request.getRemoteUser(), "CommandEliminaIncidencia");
            String [] IdIncidenciasParaReprocesar = request.getParameterValues("Incidencias");
            PagoVO[] datosIncidencia =  new PagoVO[IdIncidenciasParaReprocesar.length];
            for(int i=0;i<IdIncidenciasParaReprocesar.length;i++){
                datosIncidencia[i] = incidenciaDao.getPagoAReprocesar(IdIncidenciasParaReprocesar[i]);
                datosIncidencia[i].usuarioReproceso = request.getRemoteUser().toString();
		datosIncidencia[i].comentarios = request.getParameter(IdIncidenciasParaReprocesar[i]+"comentarios");
                incidenciaDao.eliminaIncidencia(datosIncidencia[i]);
                bitacora.registraEvento(datosIncidencia[i]);
            }
            notificaciones[0] = new Notification(ClientesConstants.INFO_LEVEL, "Se eliminaron las incidencias correctamente");
            request.setAttribute("NOTIFICACIONES",notificaciones);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new CommandException(e.getMessage());
        }
        finally {
        }
        return siguiente;
    }    
}