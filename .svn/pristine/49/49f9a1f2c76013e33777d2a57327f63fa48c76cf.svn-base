package com.sicap.clientes.commands;

import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import com.sicap.clientes.dao.OrdenDePagoDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.OrdenDePagoVO;
import java.util.ArrayList;
import org.apache.log4j.Logger;

public class CommandCancelaOrdenesDePagoGrupal implements Command {

    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandCancelaOrdenesDePagoGrupal.class);

    public CommandCancelaOrdenesDePagoGrupal(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {
        Vector<Notification> notificaciones = new Vector<Notification>();

        try {
            int numGrupo = HTMLHelper.getParameterInt(request, "numGrupo");
            int numCiclo = HTMLHelper.getParameterInt(request, "numCiclo");
            OrdenDePagoDAO opagoDAO = new OrdenDePagoDAO();
            ArrayList<OrdenDePagoVO> ordenesDePagoEnviadas = new ArrayList<OrdenDePagoVO>();
            ordenesDePagoEnviadas = opagoDAO.getOrdenesPagoEnviadas(numGrupo, numCiclo);
            BitacoraUtil bitacora = new BitacoraUtil(numGrupo, request.getRemoteUser(), "CommandCancelaOrdenesDePagoGrupal");
            if (!opagoDAO.cancelaOrdenesEnviadas(numGrupo, numCiclo)) {
                notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE, "No es posible cancelar las órdenes de pago para el equipo " + numGrupo + " ciclo " + numCiclo));
            } else {
                for (OrdenDePagoVO ordenPago : ordenesDePagoEnviadas) {
                    bitacora.registraEvento(ordenPago);
                }
                notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Se cancelaron las órdenes de pago correctamente"));
            }
            request.setAttribute("NOTIFICACIONES", notificaciones);
        } catch (ClientesDBException dbe) {
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            myLogger.error("execute", e);
            throw new CommandException(e.getMessage());
        }
        return siguiente;
    }
}
