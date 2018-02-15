/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.commands;

import com.sicap.clientes.dao.CarteraCedidaDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.SaldosHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.SaldoIBSVO;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.cartera.CreditoCartVO;
import java.sql.Savepoint;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Alex
 */
public class CommandGuardaCarteraCedida implements Command {

    private String siguiente;

    public CommandGuardaCarteraCedida(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        Notification mensaje[] = new Notification[1];
        boolean error = false;
        mensaje[0] = new Notification(ClientesConstants.ERROR_LEVEL, "Error en Migracion");
        BitacoraUtil bitacora = null;
        boolean shError = false;
        ArrayList<SaldoIBSVO> arrSaldos = new ArrayList<SaldoIBSVO>();
        List lisNumCliente = new ArrayList();
        CarteraCedidaDAO creditoDAO = new CarteraCedidaDAO();
        SaldoIBSVO saldo = new SaldoIBSVO();
        CreditoCartVO credito = new CreditoCartVO();
        Connection con = null;
        Savepoint save = null;
        int numDireccion = 0;
        int i = 0;
        String strClientes = "";
        try {
            con = ConnectionManager.getMySQLConnection();
            con.setAutoCommit(false);
            save = con.setSavepoint("BackSaldo");
            arrSaldos = new SaldosHelper().getAplicaCarteraCedidaHelper(request);
            int numDespacho = HTMLHelper.getParameterInt(request, "idDespacho");
            for (SaldoIBSVO cartCedida : arrSaldos) {
                strClientes = "";
                lisNumCliente = creditoDAO.getListaClientes(cartCedida, con);
                for (Iterator it = lisNumCliente.iterator(); it.hasNext();) {
                    Object cliente = it.next();
                    if(creditoDAO.getCliente((Integer) cliente)!=0)
                        strClientes += cliente+",";
                    i++;
                }
                if(!strClientes.isEmpty())
                    strClientes = strClientes.substring(0, strClientes.length()-1);                
                bitacora = new BitacoraUtil(cartCedida.getIdClienteSICAP(), request.getRemoteUser(), "CommandGuardaCarteraCedida");
                numDireccion = creditoDAO.getDireccionReunion(cartCedida.getCredito(), 2);
                if(numDireccion == 0){
                    numDireccion = creditoDAO.getDireccionReunion(cartCedida.getCredito(), 1);
                    if(!creditoDAO.insertReferencia(cartCedida, con, save))
                        error = true;
                    if(!error){
                        if(!creditoDAO.insertDireccion(numDireccion, con, save))
                            error = true;
                    }
                    if(!error){
                        if(!creditoDAO.getBuscaGrupo(cartCedida)){
                            if(!creditoDAO.insertGrupo(cartCedida, con, save))
                                error = true;
                        }
                    }
                }
                if(!error){
                    if(!creditoDAO.updateDespachoSaldo(cartCedida, numDespacho, con, save))
                        error = true;
                }
                if(!error){
                    if(!creditoDAO.insertSaldoCedido(cartCedida, con, save))
                        error = true;
                }
                if(!error){
                    if(!creditoDAO.insertCreditoCedido(cartCedida, con, save))
                        error = true;
                }
                if(!error){
                    if(!creditoDAO.insertAmortClieCedido(cartCedida, con, save))
                        error = true;
                }
                if(!error){
                    if(!creditoDAO.insertAmortCartCedido(cartCedida, con, save))
                    error = true;
                }
                if(!error){
                    if(!creditoDAO.insertCiclosGrupalesCedido(cartCedida, con, save))
                        error = true;
                }
                if(!error){
                    if(!creditoDAO.insertClientesCedido(cartCedida, con, save, strClientes))
                        error = true;
                }
                if(!error){
                    if(!creditoDAO.insertCliDireccionCedido(cartCedida, strClientes, con, save))
                        error = true;
                }
                if(!error){
                    if(!creditoDAO.insertCliTelefonoCedido(cartCedida, strClientes, con, save))
                        error = true;
                }
                if(!error){
                    if(!creditoDAO.insertSolicitudesCedido(cartCedida, con, save))
                        error = true;
                }
                if(!error){
                    if(!creditoDAO.insertIntegrantesCedido(cartCedida, con, save))
                        error = true;
                }
                if(!error){
                    if(!creditoDAO.insertDeciComiteCedido(cartCedida, con, save))
                        error = true;
                }
                if(!error){
                    saldo = creditoDAO.getSaldo(cartCedida);
                    if(!creditoDAO.insertPagosCedido(cartCedida, con, save))
                        error = true;
                    if(!error){
                        if(!creditoDAO.insertPagosLiquida(cartCedida, con, save))
                            error = true;
                    }
                    if(!error){
                        if(!creditoDAO.updateSaldoTabla(cartCedida, con, save))
                        error = true;
                    }
                    if(!error){
                        if(!creditoDAO.updateSaldoCedido(cartCedida, saldo, con, save))
                            error = true;
                    }
                    if(!error){
                        if(!creditoDAO.updateCreditoCedido(cartCedida, saldo, con, save))
                            error = true;
                    }
                }
                bitacora.registraEventoString("grupo="+cartCedida.getIdClienteSICAP()+", ciclo="+cartCedida.getIdSolicitudSICAP()+", despacho="+numDespacho);
            }
            con.commit();
            if(error)
                mensaje[0] = new Notification(ClientesConstants.ERROR_LEVEL, "ERROR DENTRO DE LA MIGRACION");
            else
                mensaje[0] = new Notification(ClientesConstants.INFO_LEVEL, "Cartera Cedida Migrada");
            con.close();
        } catch (Exception e) {
            try {
                con.rollback(save);
                e.printStackTrace();
            } catch (SQLException se1) {
                Logger.debug("Problema dentro de Rollback");
                Logger.debug(se1.getMessage());
            }
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException se2) {
                Logger.debug("Problema de conexion");
            }
        }
        request.setAttribute("NOTIFICACIONES", mensaje);
        return siguiente;
    }
}
