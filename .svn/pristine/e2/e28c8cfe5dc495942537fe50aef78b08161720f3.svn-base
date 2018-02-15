/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mantenimiento;

import com.sicap.clientes.dao.DAOMaster;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.PagoVO;
import com.sicap.clientes.vo.cartera.CreditoCartVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 *
 * @author Alex
 */
public class AjusteCierreDAO extends DAOMaster {

    private Connection conn = null;
    private Statement stm = null;
    private PreparedStatement pstm = null;
    private ResultSet res = null;
    private String query = "";
    private static Logger myLogger = Logger.getLogger(AjusteCierreDAO.class);

    public CreditoCartVO compruebaCiclo(int idGrupo, int numCiclo) throws ClientesDBException {
        CreditoCartVO credito = new CreditoCartVO();
        try {
            conn = getCWConnection();
            query = "SELECT * FROM d_credito WHERE cr_num_cliente=? and cr_num_solicitud=?";
            pstm = conn.prepareStatement(query);
            pstm.setInt(1, idGrupo);
            pstm.setInt(2, numCiclo);
            myLogger.debug("Ejecutando: " + query);
            myLogger.debug("Parametros: [" + idGrupo + "," + numCiclo + "]");
            res = pstm.executeQuery();
            while (res.next()) {
                credito.setNumCliente(res.getInt("CR_NUM_CLIENTE"));
                credito.setNumSolicitud(res.getInt("CR_NUM_SOLICITUD"));
                credito.setNumCredito(res.getInt("CR_NUM_CREDITO"));
                credito.setReferencia(res.getString("CR_REFERENCIA"));
                credito.setMontoCredito(res.getDouble("CR_MONTO_CREDITO"));
                credito.setMontoDesembolsado(res.getDouble("CR_MONTO_DESEMBOLSADO"));
                credito.setMontoCuenta(res.getDouble("CR_MONTO_CUENTA"));
                credito.setStatus(res.getInt("CR_STATUS"));
                credito.setMontoCuentaCongelada(res.getDouble("CR_MONTO_CUENTA_CONGELADA"));
                credito.setFechaDesembolso(res.getDate("CR_FECHA_DESEMBOLSO"));
            }
            conn.close();
        } catch (SQLException sqle) {
            myLogger.error("compruebaCiclo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("pagos", e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return credito;
    }

    public ArrayList pagos(CreditoCartVO credito) throws ClientesException {
        ArrayList pago = new ArrayList();
        //PagoVO pagos = null;
        try {
            conn = getConnection();
            query = "SELECT * FROM d_pagos_cartera WHERE pc_referencia=?";
            pstm = conn.prepareStatement(query);
            pstm.setString(1, credito.getReferencia());
            res = pstm.executeQuery();
            myLogger.debug("Ejecutando: " + query);
            myLogger.debug("Parametros: [" + credito.getReferencia() + "]");
            while (res.next()) {
                //pago.add(new PagoVO(res.getDouble("pc_monto"), res.getDate("pc_fecha_pago"), res.getInt("pc_enviado")));
                pago.add(new PagoVO(res.getDouble("pc_monto"), res.getDate("pc_fecha_pago"), 0, res.getInt("pc_banco_referencia")));
            }
            conn.close();
        } catch (SQLException sqle) {
            myLogger.error("pagos", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("pagos", e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return pago;
    }

    public ArrayList tablaAmortizacion(CreditoCartVO credito) throws ClientesException {
        ArrayList tabla = new ArrayList();
        //TablaAmortVO amortizacion = null;
        try {
            conn = getConnection();
            //query = "SELECT * FROM d_tabla_amortizacion WHERE TA_NUMCLIENTE=? and TA_NUMCREDITO=?";
            query = "SELECT * FROM d_tabla_amortizacion WHERE TA_ID_CLIENTE=? AND TA_ID_SOLICITUD=? AND TA_NO_PAGO!=0";
            pstm = conn.prepareStatement(query);
            pstm.setInt(1, credito.getNumCliente());
            pstm.setInt(2, credito.getNumSolicitud());
            myLogger.debug("Ejecutando: " + query);
            myLogger.debug("Parametros: [" + credito.getNumCliente() + "," + credito.getNumSolicitud() + "]");
            res = pstm.executeQuery();
            while (res.next()) {
                /*tabla.add(new TablaAmortVO(res.getInt("TA_NO_PAGO"), res.getDate("TA_FECHA_PAGO"), res.getDouble("TA_ABONO_CAPITAL"), 
                 res.getDouble("TA_SALDO_CAPITAL"), 0, res.getDouble("TA_INTERES"),res.getDouble("TA_IVA_INTERES"), 0, 0, 
                 0, 0, 0, 0, res.getDouble("TA_MONTO_PAGAR"), 0, 0, "N"));*/
            }
            conn.close();
        } catch (SQLException sqle) {
            myLogger.error("tablaAmortizacion", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("tablaAmortizacion", e);
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return tabla;
    }
}
