package com.sicap.clientes.commands;

import com.sicap.clientes.dao.CobradorDAO;
import com.sicap.clientes.dao.EjecutivoCreditoDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import static com.sicap.clientes.helpers.PagosReferenciadosHelper.myLogger;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.CobradorVO;
import com.sicap.clientes.vo.EjecutivoCreditoVO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author LDAVILA
 */
public class CommandSeleccionarCobrador implements Command {

    private String siguiente;

    public CommandSeleccionarCobrador(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {
        Notification notificaciones[] = new Notification[1];
        int idSucursal = 0;
        CobradorDAO cobradorDao = new CobradorDAO();
        Connection conn = null;

        try {

            idSucursal = HTMLHelper.getParameterInt(request, "idSucursal");
            conn = ConnectionManager.getMySQLConnection();

            if (idSucursal > 0) {
                //if(idEjecutivo!=0) {
                Enumeration parametros = request.getParameterNames();  //obtiene todos los parámetros

                while (parametros.hasMoreElements()) {
                    String nombreParametro = (String) parametros.nextElement();
                    String[] valores = request.getParameterValues(nombreParametro);
                    /*
                     if(valores.length == 1) {
                     String valorParametro = valores[0]; 					
                     }
                     */
                    //el contenido de esos valores
                    for (int i = 0; i < valores.length; i++) {
                        if (nombreParametro.equals("opcion")) {	//busca por las checkboxes que se llaman así
                            int idCobradorModificar = Integer.parseInt(valores[i]);
                            CobradorVO cobrador = new CobradorVO();
                            cobrador = cobradorDao.getCobrador(conn,idCobradorModificar);
                            request.setAttribute("COBRADOR", cobrador);
                        }
                    }
                }
                //}
                //else {
                //	notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se ha seleccionado a ning&uacute;n ejecutivo");
                //	request.setAttribute("NOTIFICACIONES",notificaciones);
                //} //fin else
            } //fin if
            else {
                notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se ha seleccionado una sucursal");
                request.setAttribute("NOTIFICACIONES", notificaciones);
            }	//fin else
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
            } catch (SQLException se2) {
                myLogger.error("Problema de conexion");
                myLogger.error(se2.getMessage());
                throw new CommandException(se2.getMessage());
            }
        }
        return siguiente;
    }
}
