package com.sicap.clientes.dao.cartera;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;

import com.sicap.clientes.dao.DAOMaster;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.PagoVO;
import com.sicap.clientes.vo.cartera.PagoManualVO;
import com.sicap.clientes.vo.cartera.PagosConcentradoraVO;
import org.apache.log4j.Logger;

public class PagoManualDAO extends DAOMaster {

    private static Logger myLogger = Logger.getLogger(PagoManualDAO.class);

    public PagoManualVO[] getPagoManual(int idCliente, int idSolicitud, String referencia) throws ClientesException {
        ArrayList<PagoManualVO> arrayPago = new ArrayList<PagoManualVO>();
        PagoManualVO[] pagos = null;
        Connection conn = null;
        String query = "SELECT CLIENTES.NUM_CLIENTE, PAGOS_MANUALES.* FROM PAGOS_MANUALES, CLIENTES WHERE CLIENTES.NUM_CLIENTE = ? AND CLIENTES.CICLO = ? AND CLIENTES.NO_REFERENCIA = ? AND PAGOS_MANUALES.ID_CTE = CLIENTES.ID_CTE ORDER BY PAGOS_MANUALES.ID_REG";
        int param = 1;
        try {
            conn = getCWConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(param++, idCliente);
            ps.setInt(param++, idSolicitud);
            ps.setString(param++, referencia);
            myLogger.debug("Ejecutando : " + query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PagoManualVO pago = new PagoManualVO();
                pago.numCliete = rs.getInt("NUM_CLIENTE");
                pago.idPago = rs.getInt("Id_Pago");
                pago.idCliete = rs.getInt("Id_Cte");
                pago.cliente = rs.getString("Cliente");
                pago.referencia = rs.getString("Referencia");
                pago.ciclo = rs.getString("Ciclo");
                pago.idReg = rs.getInt("ID_REG");
                pago.fechaEvento = rs.getDate("Fecha_Evento");
                pago.evento = rs.getString("Evento");
                pago.monto = rs.getDouble("Monto");
                pago.fechaIncial = rs.getDate("Fecha_Inicial");
                pago.fechaFinal = rs.getDate("Fecha_Final");
                pago.dias = rs.getInt("Dias");
                pago.multa = rs.getDouble("MULTA");
                pago.cMulta = rs.getDouble("C_MULTA");
                pago.ivaMulta = rs.getDouble("IVA_MULTA");
                pago.cIvaMulta = rs.getDouble("C_IVA_MULTA");
                pago.ivaIntMoratorio = rs.getDouble("IVA_INT_MORATORIO");
                pago.cIvaIntMoratorio = rs.getDouble("C_IVA_INT_MORATORIO");
                pago.intMoratorio = rs.getDouble("INT_MORATORIO");
                pago.cIntMoratorio = rs.getDouble("C_INT_MORATORIO");
                pago.ivaIntVigente = rs.getDouble("IVA_INTERES_VIG");
                pago.cIvaIntVigente = rs.getDouble("C_IVA_INTERES_VIG");
                pago.intVigente = rs.getDouble("INTERES_VIG");
                pago.cIntVigente = rs.getDouble("C_INTERES_VIG");
                pago.capVigente = rs.getDouble("CAPITAL_VIG");
                pago.cCapVigente = rs.getDouble("C_CAPITAL_VIG");
                pago.intMoratorioVencido = rs.getDouble("INT_MORATORIO_VDO");
                pago.cIntMoratorioVencido = rs.getDouble("C_INT_MORATORIO_VDO");
                pago.intVencido = rs.getDouble("INTERES_VDO");
                pago.cIntVencido = rs.getDouble("C_INTERES_VDO");
                pago.capVencido = rs.getDouble("CAPITAL_VDO");
                pago.cCapVencido = rs.getDouble("C_CAPITAL_VDO");
                pago.totPagar = rs.getDouble("TOT_PAGAR");
                pago.saldo = rs.getDouble("SALDO");
                pago.refLoc = rs.getString("Ref_Loc");
                pago.stsLoc = rs.getString("Sts_Loc");
                pago.rmte = rs.getDouble("RMTE");
                arrayPago.add(pago);
            }
            if (arrayPago.size() > 0) {
                pagos = new PagoManualVO[arrayPago.size()];
                for (int i = 0; i < pagos.length; i++) {
                    pagos[i] = arrayPago.get(i);
                }
            }
        } catch (SQLException exc) {
            myLogger.error("getPagoManual", exc);
            throw new ClientesException(exc.getMessage());
        } catch (NamingException exc) {
            myLogger.error("getPagoManual", exc);
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getPagoManual", exc);
                throw new ClientesException(exc.getMessage());
            }
        }
        return pagos;
    }

    public PagosConcentradoraVO[] getPagoConcentradora(int idCliente, int idSolicitud, String referencia) throws ClientesException {
        ArrayList<PagosConcentradoraVO> arrayPago = new ArrayList<PagosConcentradoraVO>();
        PagosConcentradoraVO[] pagos = null;
        Connection conn = null;
        String query = "SELECT CLIENTES.NUM_CLIENTE, PAGOS_CONCENTRADORA.FECHA, PAGOS_CONCENTRADORA.REFERENCIA, PAGOS_CONCENTRADORA.RETIRO, PAGOS_CONCENTRADORA.DEPÓSITO, PAGOS_CONCENTRADORA.SALDO, PAGOS_CONCENTRADORA.STS_REF, PAGOS_CONCENTRADORA.BANCO, PAGOS_CONCENTRADORA.FECHA_REP, PAGOS_CONCENTRADORA.ID_CTE, PAGOS_CONCENTRADORA.PM, PAGOS_CONCENTRADORA.PR, BANCOS.BANCO FROM PAGOS_CONCENTRADORA, CLIENTES, BANCOS WHERE CLIENTES.NUM_CLIENTE = ? AND CLIENTES.CICLO = ? AND CLIENTES.NO_REFERENCIA = ? AND PAGOS_CONCENTRADORA.ID_CTE = CLIENTES.ID_CTE AND PAGOS_CONCENTRADORA.BANCO = BANCOS.ID_BANCO ORDER BY FECHA;";
        int param = 1;
        try {
            conn = getCWConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(param++, idCliente);
            ps.setInt(param++, idSolicitud);
            ps.setString(param++, referencia);
            myLogger.debug("Ejecutando : " + query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PagosConcentradoraVO pago = new PagosConcentradoraVO();
                pago.numCte = rs.getInt(1);
                pago.fecha = rs.getTimestamp(2);
                pago.referencia = rs.getString(3);
                pago.retiro = rs.getDouble(4);
                pago.deposito = rs.getDouble(5);
                pago.saldo = rs.getDouble(6);
                pago.stsRef = rs.getString(7);
                pago.banco = rs.getInt(8);
                pago.fechaRep = rs.getString(9);
                pago.idCte = rs.getInt(10);
                pago.pm = rs.getBoolean(11);
                pago.pr = rs.getBoolean(12);
                pago.descripcionBanco = rs.getString(13);
                arrayPago.add(pago);
            }
            if (arrayPago.size() > 0) {
                pagos = new PagosConcentradoraVO[arrayPago.size()];
                for (int i = 0; i < pagos.length; i++) {
                    pagos[i] = arrayPago.get(i);
                }
            }
        } catch (SQLException exc) {
            myLogger.error("getPagoConcentradora", exc);
            throw new ClientesException(exc.getMessage());
        } catch (NamingException exc) {
            myLogger.error("getPagoConcentradora", exc);
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getPagoConcentradora", exc);
                throw new ClientesException(exc.getMessage());
            }
        }
        return pagos;
    }

    public PagosConcentradoraVO[] getPagosPendientes(String referencia) throws ClientesException {
        ArrayList<PagosConcentradoraVO> arrayPago = new ArrayList<PagosConcentradoraVO>();
        PagosConcentradoraVO[] pagos = null;
        Connection conn = null;
        String query = "SELECT  ID, FECHA, DEPÓSITO, BANCO FROM PAGOS_CONCENTRADORA "
                + "WHERE NO_REFERENCIA = ? AND STS_REF = 'NA';";
        int param = 1;
        try {
            conn = getCWConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setString(param++, referencia);
            myLogger.debug("Ejecutando : " + query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PagosConcentradoraVO pago = new PagosConcentradoraVO();
                pago.idCte = rs.getInt(1);
                pago.fecha = rs.getTimestamp(2);
                pago.deposito = rs.getDouble(3);
                pago.banco = rs.getInt(4);
                arrayPago.add(pago);
            }
            if (arrayPago.size() > 0) {
                pagos = new PagosConcentradoraVO[arrayPago.size()];
                for (int i = 0; i < pagos.length; i++) {
                    pagos[i] = arrayPago.get(i);
                }
            }
        } catch (SQLException exc) {
            myLogger.error("getPagosPendientes", exc);
            throw new ClientesException(exc.getMessage());
        } catch (NamingException exc) {
            myLogger.error("getPagosPendientes", exc);
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getPagosPendientes", exc);
                throw new ClientesException(exc.getMessage());
            }
        }
        return pagos;
    }

    public void insertPago(PagoVO pago) throws ClientesException {
        int param = 1;
        String query = "INSERT INTO PAGOS_CONCENTRADORA"
                + "(FECHA, REFERENCIA, DESCRIPCIÓN, RETIRO, DEPÓSITO, SALDO,STS_REF,BANCO, FECHA_REP, ID_CTE, PM, PR, FECHA_TRX, PC)"
                + "VALUES(? , ?, ? , NULL, ?, NULL, 'Aplicado', ?, NULL, (SELECT ID_CTE FROM CLIENTES WHERE NO_REFERENCIA = ?), 0, 0,CURRENT_TIMESTAMP, 0)";
        Connection cn = null;
        try {
            cn = getCWConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setDate(param++, pago.fechaPago);
            ps.setString(param++, pago.referencia);
            ps.setString(param++, pago.referencia);
            ps.setDouble(param++, pago.monto);
            ps.setInt(param++, pago.bancoReferencia);
            ps.setString(param++, pago.referencia);
            myLogger.debug("Ejecutando : " + query);
            ps.executeUpdate();
        } catch (SQLException exc) {
            myLogger.error("insertPago", exc);
            throw new ClientesException(exc.getMessage());
        } catch (NamingException exc) {
            myLogger.error("insertPago", exc);
            throw new ClientesException(exc.getMessage());
        } finally {
            if (cn != null) {
                try {
                    cn.close();
                } catch (SQLException exc) {
                    myLogger.error("insertPago", exc);
                    throw new ClientesDBException(exc.getMessage());
                }
            }
        }
    }

    public boolean insertPago(PagoVO pago, Connection con) throws ClientesException {
        
        int param = 1;
        String query = "INSERT INTO PAGOS_CONCENTRADORA"
                + "(FECHA, REFERENCIA, DESCRIPCIÓN, RETIRO, DEPÓSITO, SALDO,STS_REF,BANCO, FECHA_REP, ID_CTE, PM, PR, FECHA_TRX, PC)"
                + "VALUES(? , ?, ? , NULL, ?, NULL, 'Aplicado', ?, NULL, (SELECT ID_CTE FROM CLIENTES WHERE NO_REFERENCIA = ?), 0, 0,CURRENT_TIMESTAMP, 0)";
        boolean listo = true;
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(query);
            ps.setDate(param++, pago.fechaPago);
            ps.setString(param++, pago.referencia);
            ps.setString(param++, pago.referencia);
            ps.setDouble(param++, pago.monto);
            ps.setInt(param++, pago.bancoReferencia);
            ps.setString(param++, pago.referencia);
            myLogger.debug("Ejecutando "+ps);
            ps.executeUpdate();
        } catch (SQLException exc) {
            myLogger.error("insertPago", exc);
            return listo = false;
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException exc) {
                myLogger.error("insertPago", exc);
                throw new ClientesDBException(exc.getMessage());
            }
        }
        return listo;
    }
     public void insertPagoLayout(PagoVO pago, Connection con) throws ClientesException {        
        int param = 1;
        String query = "INSERT INTO PAGOS_CONCENTRADORA"
                + "(FECHA, REFERENCIA, DESCRIPCIÓN, RETIRO, DEPÓSITO, SALDO,STS_REF,BANCO, FECHA_REP, ID_CTE, PM, PR, FECHA_TRX, PC)"
                + "VALUES(? , ?, ? , NULL, ?, NULL, 'Aplicado', ?, NULL, (SELECT ID_CTE FROM CLIENTES WHERE NO_REFERENCIA = ?), 0, 0,CURRENT_TIMESTAMP, 0)";        
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(query);
            ps.setDate(param++, pago.fechaPago);
            ps.setString(param++, pago.referencia);
            ps.setString(param++, pago.referencia);
            ps.setDouble(param++, pago.monto);
            ps.setInt(param++, pago.bancoReferencia);
            ps.setString(param++, pago.referencia);
            myLogger.debug("Ejecutando "+ps);
            ps.executeUpdate();
        } catch (SQLException exc) {
            myLogger.error("insertPago", exc);
            throw new ClientesException(exc.getMessage());
        } 
    }

    public int updatePagoConcentradora(int ID, String referencia) throws ClientesException {

        String query = "UPDATE PAGOS_CONCENTRADORA SET STS_REF = 'Aplicado' "
                + "WHERE ID = ? AND REFERENCIA = ? ";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            cn = getCWConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(param++, ID);
            ps.setString(param++, referencia);
            res = ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("updatePagoConcentradora", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updatePagoConcentradora", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException sqle) {
                myLogger.error("updatePagoConcentradora", sqle);
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return res;
    }

}
