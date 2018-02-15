package com.sicap.clientes.commands;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.OrdenDePagoDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.OrdenesDePagoHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.OrdenDePagoVO;

public class CommandGeneraOrdenDePago implements Command {

    private String siguiente;

    public CommandGeneraOrdenDePago(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {
        Notification notificaciones[] = new Notification[1];
        OrdenDePagoDAO oPagoDao = new OrdenDePagoDAO();
        List<OrdenDePagoVO> list = new ArrayList<OrdenDePagoVO>();
        OrdenDePagoVO oPago = null;
        List file = null;
        Connection conn = null;
        Date utilDate = new Date();
        int banco = 0;
        int numEnvio = 0;
        String filename = null;
        boolean procesaOrden = false;
        int estatus = 0;


        try {
            int totalOrdenes = HTMLHelper.getParameterInt(request, "totalOrdenes");
            int idOrden = 0;
            boolean isChecked = false;
            int idOrdenCancel = 0;
            int idOrdenModificada = 0;

            conn = ConnectionManager.getMySQLConnection();
            conn.setAutoCommit(false);

            for (int i = 0; i < totalOrdenes; i++) {
                idOrden = HTMLHelper.getParameterInt(request, "idOrdenPago" + i);
                isChecked = HTMLHelper.getCheckBox(request, "ordenpago" + i);
                idOrdenCancel = HTMLHelper.getParameterInt(request, "idOrdenPagoCancelada" + i);
                idOrdenModificada = HTMLHelper.getParameterInt(request, "idOrdenModificada" + i);

                procesaOrden = false;

                if ((idOrden > 0 && isChecked)) {
                    procesaOrden = true;
                    estatus = ClientesConstants.OP_DISPERSADA;
                } else if (idOrdenCancel > 0) {
                    procesaOrden = true;
                    estatus = ClientesConstants.OP_CANCELADA;
                    idOrden = idOrdenCancel;
                } else if (idOrdenModificada > 0) {
                    procesaOrden = true;
                    estatus = ClientesConstants.OP_ACTUALIZA_NOMBRE;
                    idOrden = idOrdenModificada;
                }
                
                if (procesaOrden) {
                    oPago = oPagoDao.getOrdenDePago(conn, idOrden);

                    oPago.setEstatus(estatus);
                    oPago.setUsuario(request.getRemoteUser());
                    oPago.setFechaEnvio(Convertidor.toSqlTimeStamp(utilDate));
                    banco = oPago.getIdBanco();

                    list.add(oPago);
                }

            }
            /**
             * Generando Buffer el cual contiene la información que se va a escribir en el Archivo 
             */
            switch (banco) {
                case 2: //Banorte
                    Logger.debug("Procesando Layout Banorte");
                    OrdenesDePagoHelper oPagoHelp = new OrdenesDePagoHelper(list);
                    file = oPagoHelp.creaOrdenDePagoBanorte();
                    numEnvio = oPagoHelp.envio;
                    filename = oPagoHelp.getFileName();
                    break;
                case 3: //Bancomer
                    Logger.debug("Procesando Layout Bancomer");
                    OrdenesDePagoHelper oPagoHelpBancomer = new OrdenesDePagoHelper(list, banco);
                    file = oPagoHelpBancomer.creaOrdenDePagoBancomer();
                    numEnvio = oPagoHelpBancomer.envio;
                    filename = oPagoHelpBancomer.getFileName("OPN");
                    break;
                case 12: //Banamex
                    Logger.debug("Procesando Layout Banamex");
                    OrdenesDePagoHelper oPagoHelpBanamex = new OrdenesDePagoHelper(list, banco);
                    file = oPagoHelpBanamex.creaOrdenDePagoBanamex();
                    numEnvio = oPagoHelpBanamex.envio;
                    filename = oPagoHelpBanamex.getFileName("OBanamex");
                    break;
                case 16: //Scotia
                    Logger.debug("Procesando Layout Scotia");
                    OrdenesDePagoHelper oPagoHelpScotia = new OrdenesDePagoHelper(list, banco);
                    file = oPagoHelpScotia.creaOrdenDePagoScotia();
                    numEnvio = oPagoHelpScotia.envio;
                    filename = oPagoHelpScotia.getFileName("ODPScotia");
                    break;
                default:
                    notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No Existe Definido un Layout para el tipo de Banco Solicitado");
                    break;
            }

            /**
             * Proceso que Actualiza las Solicitudes que se generaron en el Archivo con estatus de Dispersada
             * si todas fuerón Actualizadas de Manera Correcta se realiza el Commit y se envia el Archivo a Pantalla. 
             */
            if (oPagoDao.updateStatusFromList(conn, list, numEnvio, "CommandGeneraOrdenDePago")) {
                conn.commit();
            }

            request.setAttribute("NOTIFICACIONES", notificaciones);
            request.setAttribute("FILE", file);
            request.setAttribute("FILENAME", filename);

        } catch (ClientesDBException dbe) {
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommandException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                throw new CommandException(sqle.getMessage());
            }
        }

        return siguiente;
    }
}
