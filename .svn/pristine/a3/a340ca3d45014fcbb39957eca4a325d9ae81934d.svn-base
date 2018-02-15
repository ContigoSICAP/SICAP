package com.sicap.clientes.commands;

import com.sicap.clientes.dao.CobradorDAO;
import com.sicap.clientes.dao.EjecutivoCreditoDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import javax.servlet.http.HttpServletRequest;
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
import org.apache.log4j.Logger;

public class CommandGuardaCobradores implements Command {

    private String siguiente;
    private static Logger myLogger = Logger.getLogger(EjecutivoCreditoDAO.class);

    public CommandGuardaCobradores(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        Notification notificaciones[] = new Notification[2];
        Connection conn = null;
        int iExisteActivo = 0;
        int resp = 0;
        CobradorDAO Cobradordao = new CobradorDAO();

        try {
            //Se genera la conexion a la Base de Datos
            conn = ConnectionManager.getMySQLConnection();
            conn.setAutoCommit(false);
            //Se traen los datos del request
            int sucursal = HTMLHelper.getParameterInt(request, "idSucursal");
            String nombre = HTMLHelper.getParameterString(request, "nombre");
            String aPaterno = HTMLHelper.getParameterString(request, "aPaterno");
            String aMaterno = HTMLHelper.getParameterString(request, "aMaterno");
            int estatus = HTMLHelper.getParameterInt(request, "estatus");
            BitacoraUtil bitutil = new BitacoraUtil(sucursal, request.getRemoteUser(), "CommandGuardaCobradores");

            //Se pasan los datos del request al objeto
            CobradorVO cobrador = new CobradorVO();
            cobrador.setIdSucursal(sucursal);
            cobrador.setNombre(nombre);
            cobrador.setaPaterno(aPaterno);
            cobrador.setaMaterno(aMaterno);
            cobrador.setEstatus(estatus);
            cobrador.setUsuario(request.getRemoteUser());
            if (!Cobradordao.existeCobrador(conn, cobrador)) {  // Se Comprueba si existe un cobrador con el nombre enviado en el request para esa sucursal                 
                iExisteActivo = Cobradordao.cobradorActivo(conn, cobrador);// cuenta cuantos cobradores activos existen en esa sucursal
                if (iExisteActivo >= 1 && cobrador.getEstatus() == 1) { //Si hay cobradores activos los pasa a inactivos 
                    CobradorVO cobradorAnte = new CobradorVO();
                    cobradorAnte = Cobradordao.getCobradorAnt(conn, cobrador);                    
                    cobradorAnte.setEstatus(2);
                    resp = Cobradordao.updateCobrador(conn, cobradorAnte);
                    bitutil.registraEventoString("Se Cambio a estatus inactivo: " + cobradorAnte.toString()); //Registra en bitacora el cambio
                    notificaciones[1] = new Notification(ClientesConstants.ERROR_TYPE, "El Cobrador: " + cobradorAnte.getNombre() + " " + cobradorAnte.getaPaterno() + " " + cobradorAnte.getaMaterno() + " paso a Inactivo");
                }
                else if (iExisteActivo == 0 && cobrador.getEstatus() == 2){
                    notificaciones[1] = new Notification(ClientesConstants.ERROR_TYPE, "La Susucrsal no tiene un Cobrador Activo");                    
                }
                else {
                    notificaciones[1] = new Notification(ClientesConstants.ERROR_TYPE, "");                
                }
                resp = Cobradordao.addCobrador(conn, cobrador);
                bitutil.registraEventoString(cobrador.toString());
                conn.commit();
                notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Los datos fueron guardados correctamente");
            }else{
                notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Ya existe un cobrador: ");
                notificaciones[1] = new Notification(ClientesConstants.ERROR_TYPE, cobrador.getNombre() + " " + cobrador.getaPaterno() + " " + cobrador.getaMaterno() + ", en esa sucursal");
            }
            request.setAttribute("NOTIFICACIONES", notificaciones);
            //request.setAttribute("COBRADOR", cobrador);
        }catch (ClientesDBException dbe) {
            try {
                conn.rollback();
            } catch (SQLException se1) {
                myLogger.error("Problema dentro de Rollback Cobradores");
                myLogger.error(se1.getMessage());
            }
            myLogger.error("execute"+dbe.getMessage());
            throw new CommandException(dbe.getMessage());
        }catch (Exception e) {
            try {
                conn.rollback();
            } catch (SQLException se1) {
                myLogger.error("Problema dentro de Rollback Cobradores");
                myLogger.error(se1.getMessage());
                throw new CommandException(se1.getMessage());
            }
            myLogger.error(e.getMessage());
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