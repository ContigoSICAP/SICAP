package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.CobradorDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import static com.sicap.clientes.helpers.PagosReferenciadosHelper.myLogger;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.CobradorVO;
import java.sql.Connection;
import java.sql.SQLException;

public class CommandModificaCobradores implements Command {

    private String siguiente;

    public CommandModificaCobradores(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        Notification notificaciones[] = new Notification[2];
        Connection conn = null;
        int actualizar = 0;
        int resp = 0;
        CobradorDAO cobradorDao = new CobradorDAO();
        
        try {
            conn = ConnectionManager.getMySQLConnection();
            conn.setAutoCommit(false);

            int idCobrador = HTMLHelper.getParameterInt(request, "idCobrador");
            int idSucursal = HTMLHelper.getParameterInt(request, "idSucursal");
            String nombre = HTMLHelper.getParameterString(request, "nombre");
            String aPaterno = HTMLHelper.getParameterString(request, "aPaterno");
            String aMaterno = HTMLHelper.getParameterString(request, "aMaterno");
            int estatus = HTMLHelper.getParameterInt(request, "estatus");

            BitacoraUtil bitutil = new BitacoraUtil(idCobrador, request.getRemoteUser(), "CommandModificaCobrador");

            if (idSucursal > 0) {

                CobradorVO cobrador = new CobradorVO();
                cobrador.setIdCobrador(idCobrador);
                cobrador.setIdSucursal(idSucursal);
                cobrador.setNombre(nombre);
                cobrador.setaPaterno(aPaterno);
                cobrador.setaMaterno(aMaterno);
                cobrador.setEstatus(estatus);
                cobrador.setUsuario(request.getRemoteUser());

                if (cobrador.getEstatus() == 1) {
                    CobradorVO cobradorAnte = new CobradorVO();
                    cobradorAnte = cobradorDao.getCobradorAnt(conn, cobrador);
                    if (cobradorAnte.getIdCobrador() != cobrador.getIdCobrador()) {
                        cobradorAnte.setEstatus(2);
                        resp = cobradorDao.updateCobrador(conn, cobradorAnte);
                        bitutil.registraEventoString("Se Cambio a estatus inactivo: " + cobradorAnte.toString()); //Registra en bitacora el cambio
                        notificaciones[1] = new Notification(ClientesConstants.ERROR_TYPE, "El Cobrador: " + cobradorAnte.getNombre() + " " + cobradorAnte.getaPaterno() + " " + cobradorAnte.getaMaterno() + " paso a Inactivo");
                    }
                    else{
                        notificaciones[1] = new Notification(ClientesConstants.INFO_TYPE, "");
                    }
                }
                else {
                    notificaciones[1] = new Notification(ClientesConstants.INFO_TYPE, "");
                }
                actualizar = cobradorDao.updateCobrador(conn, cobrador);
                bitutil.registraEventoString(cobrador.toString());
                conn.commit();
                notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron guardados correctamente");
                
                request.setAttribute("COBRADOR", cobrador);
                request.setAttribute("ACTUALIZACION_COBRADOR", actualizar);
                request.setAttribute("NOTIFICACIONES", notificaciones);

            } //fin if
            else {
                notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se ha seleccionado una sucursal");
                request.setAttribute("NOTIFICACIONES", notificaciones);
            }	//fin else            
        } catch (ClientesDBException dbe) {
            try {
                conn.rollback();
            } catch (SQLException se1) {
                myLogger.error("Problema dentro de Rollback ModificacionCobradores");
                myLogger.error(se1.getMessage());
            }
            myLogger.error("execute"+dbe.getMessage());
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException se2) {
                myLogger.error("Problema dentro de Rollback ModificacionCobradores");
                myLogger.error(se2.getMessage());
            }
            myLogger.error("execute "+e.getMessage());
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
