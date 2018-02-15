package com.sicap.clientes.commands;

import com.sicap.clientes.dao.CobradorDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import static com.sicap.clientes.helpers.PagosReferenciadosHelper.myLogger;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.CobradorVO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

public class CommandListaCobradores implements Command{

    private String siguiente;

    public CommandListaCobradores(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        //TreeMap catEjecutivos = new TreeMap();
        ArrayList<CobradorVO> listaCobrador = null;
        CobradorDAO cobradorDao = new CobradorDAO();
        Connection conn = null;
        Notification notificaciones[] = new Notification[1];
        int idSucursal = 0;
        try {
            conn = ConnectionManager.getMySQLConnection();
            idSucursal = HTMLHelper.getParameterInt(request, "idSucursal");
            if (idSucursal > 0) {
                listaCobrador = cobradorDao.getCobradoresSucursal(conn,idSucursal);
                if (listaCobrador != null && listaCobrador.size() > 0) {
                    request.setAttribute("COBRADORES", listaCobrador);
                    Logger.debug("Cobradores encontrados");

                } //fin if
                else {
                    notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se encontraron Cobradores para la sucursal seleccionada");
                    request.setAttribute("NOTIFICACIONES", notificaciones);
                }	//fin else
            } //fin if
            else {
                notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se ha seleccionado una sucursal");
                request.setAttribute("NOTIFICACIONES", notificaciones);
            }  //fin else
        } catch (ClientesDBException dbe) {
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            myLogger.error(e.getMessage());
            throw new CommandException(e.getMessage());
        }finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException se2) {
                myLogger.error("Problema de conexion");
                myLogger.error(se2.getMessage());
                throw new CommandException(se2.getMessage());
            }
        }
        return siguiente;
    }
}


