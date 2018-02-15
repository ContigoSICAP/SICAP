package com.sicap.clientes.dao.cartera;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sicap.clientes.dao.DAOMaster;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.cartera.TransaccionesContabVO;
import java.util.List;
import org.apache.log4j.Logger;

public class TransaccionesContabDAO extends DAOMaster {

    private static Logger myLogger = Logger.getLogger(TransaccionesContabDAO.class);

    public TransaccionesContabVO[] getElementos(String status) throws ClientesException {
        TransaccionesContabVO transac = null;
        Connection cn = null;
        ArrayList<TransaccionesContabVO> array = new ArrayList<TransaccionesContabVO>();
        TransaccionesContabVO elementos[] = null;

        String query = "SELECT TC_NUMERO_TRANSACCION, TC_SECUENCIAL, TC_TIPO_TRANSACCION, TC_NUMCLIENTE, TC_NUMCREDITO, TC_FECHA_PROCESO, TC_FECHA_VALOR, TC_NUMPRODUCTO,"
                + "TC_CENTRO_COSTOS, TC_CODIGO_RUBRO, TC_STATUS_RUBRO, TC_MONTO, TC_STATUS, TC_FONDEADOR, TC_NO_CUENTA, TC_TIPO_MOVIMIENTO, TC_CENTRO_COSTOS_DEST FROM D_TRANSACCIONES_CONTABILIDAD WHERE TC_STATUS = ? ";
        try {
            cn = getCWConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, status);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + status + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                transac = new TransaccionesContabVO();
                transac.numTransaccion = rs.getInt("TC_NUMERO_TRANSACCION");
                transac.secuencial = rs.getInt("TC_SECUENCIAL");
                transac.tipoTransaccion = rs.getString("TC_TIPO_TRANSACCION");
                transac.numCliente = rs.getInt("TC_NUMCLIENTE");
                transac.numCredito = rs.getInt("TC_NUMCREDITO");
                transac.fechaProceso = rs.getDate("TC_FECHA_PROCESO");
                transac.fechaValor = rs.getDate("TC_FECHA_VALOR");
                transac.numProducto = rs.getInt("TC_NUMPRODUCTO");
                transac.centroCostos = rs.getInt("TC_CENTRO_COSTOS");
                transac.centroCostosDest = rs.getInt("TC_CENTRO_COSTOS_DEST");
                transac.rubro = rs.getString("TC_CODIGO_RUBRO");
                transac.statusRubro = rs.getString("TC_STATUS_RUBRO");
                transac.monto = rs.getDouble("TC_MONTO");
                transac.status = rs.getString("TC_STATUS");
                transac.numCuenta = rs.getString("TC_NO_CUENTA");
                transac.tipoMovimiento = rs.getString("TC_TIPO_MOVIMIENTO");
                transac.fondeador = rs.getInt("TC_FONDEADOR");
                array.add(transac);
            }
            if (array.size() > 0) {
                elementos = new TransaccionesContabVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (TransaccionesContabVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getElementos", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getElementos", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }

        return elementos;
    }

    public TransaccionesContabVO[] getElementosIng(String ejercicio) throws ClientesException {
        TransaccionesContabVO transac = null;
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<TransaccionesContabVO> array = new ArrayList<TransaccionesContabVO>();
        TransaccionesContabVO elementos[] = null;

        String query = "SELECT TC_NUMERO_TRANSACCION, TC_SECUENCIAL, TC_TIPO_TRANSACCION, TC_NUMPRODUCTO, TC_CENTRO_COSTOS, TC_CENTRO_COSTOS_DEST, TC_FONDEADOR, TC_NO_CUENTA, TC_TIPO_MOVIMIENTO, SUM(TC_MONTO) FROM D_TRANSACCIONES_CONTABILIDAD "
                + " WHERE TC_STATUS = 'ING' AND LEFT(tc_fecha_proceso,7)=? GROUP BY TC_TIPO_TRANSACCION, TC_NUMPRODUCTO, TC_CENTRO_COSTOS, TC_NO_CUENTA, TC_TIPO_MOVIMIENTO, TC_FONDEADOR"
                + " ORDER BY TC_TIPO_TRANSACCION, TC_NUMPRODUCTO, TC_FONDEADOR";
        try {
            cn = getCWConnection();
            ps = cn.prepareStatement(query);
            ps.setString(1, ejercicio);
            myLogger.debug("Ejecutando = " + ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                transac = new TransaccionesContabVO();
                transac.numTransaccion = rs.getInt("TC_NUMERO_TRANSACCION");
                transac.secuencial = rs.getInt("TC_SECUENCIAL");
                transac.tipoTransaccion = rs.getString("TC_TIPO_TRANSACCION");
                transac.numProducto = rs.getInt("TC_NUMPRODUCTO");
                transac.centroCostos = rs.getInt("TC_CENTRO_COSTOS");
                transac.centroCostosDest = rs.getInt("TC_CENTRO_COSTOS_DEST");
                transac.fondeador = rs.getInt("TC_FONDEADOR");
                transac.numCuenta = rs.getString("TC_NO_CUENTA");
                transac.tipoMovimiento = rs.getString("TC_TIPO_MOVIMIENTO");
                transac.monto = rs.getDouble("SUM(TC_MONTO)");
                array.add(transac);
            }
            if (array.size() > 0) {
                elementos = new TransaccionesContabVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (TransaccionesContabVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getElementosIng", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getElementosIng", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null)
                    cn.close();
                if (ps != null)
                    ps.close();
                if (rs != null)
                    rs.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }

        return elementos;
    }

    public int addTransaccion(TransaccionesContabVO transac) throws ClientesException {

        String query = "INSERT INTO D_TRANSACCIONES_CONTABILIDAD (TC_NUMERO_TRANSACCION, TC_SECUENCIAL, TC_TIPO_TRANSACCION, TC_NUMCLIENTE, TC_NUMCREDITO, TC_FECHA_PROCESO, "
                + "TC_FECHA_VALOR, TC_NUMPRODUCTO, TC_CENTRO_COSTOS, TC_CODIGO_RUBRO, TC_STATUS_RUBRO, TC_MONTO, TC_STATUS, "
                + "TC_NO_CUENTA, TC_TIPO_MOVIMIENTO, TC_FONDEADOR, TC_CENTRO_COSTOS_DEST, tc_no_cuenta_sap) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                + "?, ?, ?, ?, ?, ?, ?, ?) ";

        Connection cn = null;
        int param = 1;
        int res = 0;
        try {
            PreparedStatement ps = null;
            cn = getCWConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(param++, transac.numTransaccion);
            ps.setInt(param++, transac.secuencial);
            ps.setString(param++, transac.tipoTransaccion);
            ps.setInt(param++, transac.numCliente);
            ps.setInt(param++, transac.numCredito);
            ps.setDate(param++, transac.fechaProceso);
            ps.setDate(param++, transac.fechaValor);
            ps.setInt(param++, transac.numProducto);
            ps.setInt(param++, transac.centroCostos);
            ps.setString(param++, transac.rubro);
            ps.setString(param++, transac.statusRubro);
            ps.setDouble(param++, transac.monto);
            ps.setString(param++, transac.status);
            ps.setString(param++, transac.numCuenta);
            ps.setString(param++, transac.tipoMovimiento);
            ps.setInt(param++, transac.fondeador);
            ps.setInt(param++, transac.centroCostosDest);
            ps.setString(param++, transac.numCuentaSap);
            myLogger.debug("Ejecutando = " + ps);
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("addTransaccion", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addTransaccion", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return res;
    }

    public int updateStatusTransaccion(Connection cn, TransaccionesContabVO transac) throws ClientesException {

        String query = "UPDATE D_TRANSACCIONES_CONTABILIDAD SET TC_STATUS = ? "
                + "WHERE TC_TIPO_TRANSACCION = ? AND TC_NUMPRODUCTO = ? AND TC_CENTRO_COSTOS = ? AND "
                + "TC_STATUS = 'ING' AND TC_NO_CUENTA = ? AND TC_TIPO_MOVIMIENTO = ?";
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            ps = cn.prepareStatement(query);
            ps.setString(param++, transac.status);
            ps.setString(param++, transac.tipoTransaccion);
            ps.setInt(param++, transac.numProducto);
            ps.setInt(param++, transac.centroCostos);
            ps.setString(param++, transac.numCuenta);
            ps.setString(param++, transac.tipoMovimiento);
            myLogger.debug("Ejecutando = " + ps);
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("updateStatusTransaccion", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateStatusTransaccion", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return res;
    }

    public int getMaxTransaccion() throws ClientesException {

        String query = "SELECT MAX(TC_NUMERO_TRANSACCION) FROM D_TRANSACCIONES_CONTABLES ";

        Connection cn = null;
        int numTransaccion = 0;

        try {
            cn = getCWConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            myLogger.debug("Ejecutando : " + query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                numTransaccion = rs.getInt(1);
            }

        } catch (SQLException sqle) {
            myLogger.error("getElementos", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getElementos", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return numTransaccion;

    }
    
    public ArrayList<TransaccionesContabVO> getFechasTransaccion() throws ClientesException {

        ArrayList<TransaccionesContabVO> arrFechas = new ArrayList<TransaccionesContabVO>();
        String query = "SELECT MAX(tc_fecha_proceso) FROM D_TRANSACCIONES_CONTABILIDAD WHERE tc_status='ING' GROUP BY LEFT(tc_fecha_proceso,7);";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        TransaccionesContabVO transaccion = null;
        try {
            con = getCWConnection();
            ps = con.prepareStatement(query);
            myLogger.debug("Ejecutando : " + ps);
            res = ps.executeQuery();
            while (res.next()) {
                transaccion = new TransaccionesContabVO();
                transaccion.setFechaProceso(res.getDate("MAX(tc_fecha_proceso)"));
                arrFechas.add(transaccion);
            }
        } catch (SQLException sqle) {
            myLogger.error("getElementos", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getElementos", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null)
                    con.close();
                if (ps != null)
                    ps.close();
                if (res != null)
                    res.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return arrFechas;
    }
    
    public String getNumTransacciones(String ejercicio, String cuentas, boolean banco) throws ClientesException {

        String lstTransacciones = "";
        String query = "SELECT tc_numero_transaccion FROM D_TRANSACCIONES_CONTABILIDAD WHERE tc_status='ENV' AND tc_status_sap='ING' AND LEFT(tc_fecha_proceso,7)=? ";
        if(banco)
            query += "AND tc_no_cuenta_sap IN ("+cuentas+") ";
        else
            query += "AND tc_no_cuenta_sap NOT IN ("+cuentas+") ";
        query += "GROUP BY tc_numero_transaccion;";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            con = getCWConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, ejercicio);
            myLogger.debug("Ejecutando : " + ps);
            res = ps.executeQuery();
            while (res.next()) {
                lstTransacciones += res.getInt("tc_numero_transaccion")+",";
            }
            if(!lstTransacciones.equals(""))
                lstTransacciones = lstTransacciones.substring(0, lstTransacciones.length()-1);
        } catch (SQLException sqle) {
            myLogger.error("getNumTransacciones", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getNumTransacciones", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null)
                    con.close();
                if (ps != null)
                    ps.close();
                if (res != null)
                    res.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return lstTransacciones;
    }
    
    public TransaccionesContabVO[] getElementosIngSAP(String ejercicio, String transacciones) throws ClientesException {
        TransaccionesContabVO transac = null;
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<TransaccionesContabVO> array = new ArrayList<TransaccionesContabVO>();
        TransaccionesContabVO elementos[] = null;
        String query = "SELECT tc_numero_transaccion, tc_secuencial, tc_tipo_transaccion, tc_numproducto, tc_centro_costos, tc_centro_costos_dest, tc_fondeador, tc_no_cuenta_sap, tc_tipo_movimiento, SUM(tc_monto) "+
                "FROM D_TRANSACCIONES_CONTABILIDAD WHERE tc_numero_transaccion IN ("+transacciones+") "+
                "GROUP BY tc_tipo_transaccion, tc_numproducto, tc_centro_costos, tc_no_cuenta_sap, tc_tipo_movimiento, tc_fondeador "+
                "ORDER BY tc_tipo_transaccion, tc_numproducto, tc_fondeador;";
        try {
            cn = getCWConnection();
            ps = cn.prepareStatement(query);
            myLogger.debug("Ejecutando = " + ps);
            rs = ps.executeQuery();
            while (rs.next()) {
                transac = new TransaccionesContabVO();
                transac.numTransaccion = rs.getInt("tc_numero_transaccion");
                transac.secuencial = rs.getInt("tc_secuencial");
                transac.tipoTransaccion = rs.getString("tc_tipo_transaccion");
                transac.numProducto = rs.getInt("tc_numproducto");
                transac.centroCostos = rs.getInt("tc_centro_costos");
                transac.centroCostosDest = rs.getInt("tc_centro_costos_dest");
                transac.fondeador = rs.getInt("tc_fondeador");
                transac.numCuentaSap = rs.getString("tc_no_cuenta_sap");
                transac.tipoMovimiento = rs.getString("tc_tipo_movimiento");
                transac.monto = rs.getDouble("SUM(tc_monto)");
                array.add(transac);
            }
            if (array.size() > 0) {
                elementos = new TransaccionesContabVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (TransaccionesContabVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getElementosIng", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getElementosIng", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null)
                    cn.close();
                if (ps != null)
                    ps.close();
                if (rs != null)
                    rs.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return elementos;
    }
    
    public int updateStatusTransaccionSAP(Connection con, TransaccionesContabVO transac) throws ClientesException {

        String query = "UPDATE D_TRANSACCIONES_CONTABILIDAD SET tc_status_sap=? "
                + "WHERE tc_tipo_transaccion=? AND tc_numproducto=? AND tc_centro_costos=? AND tc_status_sap='ING' AND tc_no_cuenta_sap=? AND tc_tipo_movimiento=?";
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            ps = con.prepareStatement(query);
            ps.setString(param++, transac.statusSAP);
            ps.setString(param++, transac.tipoTransaccion);
            ps.setInt(param++, transac.numProducto);
            ps.setInt(param++, transac.centroCostos);
            ps.setString(param++, transac.numCuentaSap);
            ps.setString(param++, transac.tipoMovimiento);
            myLogger.debug("Ejecutando = " + ps);
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("updateStatusTransaccionSAP", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateStatusTransaccionSAP", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return res;
    }

}
