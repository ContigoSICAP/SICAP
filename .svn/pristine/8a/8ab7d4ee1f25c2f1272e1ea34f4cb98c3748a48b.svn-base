package com.sicap.clientes.dao.cartera;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sicap.clientes.dao.DAOMaster;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.cartera.TransaccionesVO;
import org.apache.log4j.Logger;

public class TransaccionesDAO extends DAOMaster {

    private static Logger myLogger = Logger.getLogger(TransaccionesDAO.class);

    public TransaccionesVO[] getElementos(String status) throws ClientesException {
        TransaccionesVO transac = null;
        Connection cn = null;
        ArrayList<TransaccionesVO> array = new ArrayList<TransaccionesVO>();
        TransaccionesVO elementos[] = null;

        String query = "SELECT TR_NUM_TRANSACCION, TR_SECUENCIAL, TR_TIPO_TRANSACCION, TR_NUMCLIENTE, TR_NUMCREDITO, TR_FECHA_PROCESO, TR_FECHA_VALOR, TR_NUMPRODUCTO,"
                + "TR_CENTRO_COSTOS, TR_CODIGO_RUBRO, TR_STATUS_RUBRO, TR_MONTO, TR_STATUS FROM D_TRANSACCIONES WHERE TR_STATUS = ? ";
        try {
            cn = getCWConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, status);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + status + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                transac = new TransaccionesVO();
                transac.numTransaccion = rs.getInt("TR_NUM_TRANSACCION");
                transac.secuencial = rs.getInt("TR_SECUENCIAL");
                transac.tipoTransaccion = rs.getString("TR_TIPO_TRANSACCION");
                transac.numCliente = rs.getInt("TR_NUMCLIENTE");
                transac.numCredito = rs.getInt("TR_NUMCREDITO");
                transac.fechaProceso = rs.getDate("TR_FECHA_PROCESO");
                transac.fechaValor = rs.getDate("TR_FECHA_VALOR");
                transac.numProducto = rs.getInt("TR_NUMPRODUCTO");
                transac.centroCostos = rs.getInt("TR_CENTRO_COSTOS");
                transac.rubro = rs.getString("TR_CODIGO_RUBRO");
                transac.statusRubro = rs.getString("TR_STATUS_RUBRO");
                transac.monto = rs.getDouble("TR_MONTO");
                transac.status = rs.getString("TR_STATUS");
                array.add(transac);
            }
            if (array.size() > 0) {
                elementos = new TransaccionesVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (TransaccionesVO) array.get(i);
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

    public TransaccionesVO[] getElementosIng() throws ClientesException {
        TransaccionesVO transac = null;
        Connection cn = null;
        ArrayList<TransaccionesVO> array = new ArrayList<TransaccionesVO>();
        TransaccionesVO elementos[] = null;
        String query = "SELECT TR_NUMERO_TRANSACCION, TR_SECUENCIAL, TR_TIPO_TRANSACCION, TR_NUMCLIENTE, TR_NUMCREDITO, TR_FECHA_PROCESO, TR_FECHA_VALOR, TR_NUMPRODUCTO,"
                + "TR_CENTRO_COSTOS, TR_CODIGO_RUBRO, TR_STATUS_RUBRO, TR_MONTO, TR_STATUS, TR_ORIGEN FROM D_TRANSACCIONES WHERE TR_STATUS = 'ING' ORDER BY TR_TIPO_TRANSACCION, TR_CENTRO_COSTOS, TR_NUMPRODUCTO,"
                + "TR_NUMERO_TRANSACCION, TR_SECUENCIAL";
        try {
            cn = getCWConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            myLogger.debug("Ejecutando = " + query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                transac = new TransaccionesVO();
                transac.numTransaccion = rs.getInt("TR_NUMERO_TRANSACCION");
                transac.secuencial = rs.getInt("TR_SECUENCIAL");
                transac.tipoTransaccion = rs.getString("TR_TIPO_TRANSACCION");
                transac.numCliente = rs.getInt("TR_NUMCLIENTE");
                transac.numCredito = rs.getInt("TR_NUMCREDITO");
                transac.fechaProceso = rs.getDate("TR_FECHA_PROCESO");
                transac.fechaValor = rs.getDate("TR_FECHA_VALOR");
                transac.numProducto = rs.getInt("TR_NUMPRODUCTO");
                transac.centroCostos = rs.getInt("TR_CENTRO_COSTOS");
                transac.rubro = rs.getString("TR_CODIGO_RUBRO");
                transac.statusRubro = rs.getString("TR_STATUS_RUBRO");
                transac.monto = rs.getDouble("TR_MONTO");
                transac.status = rs.getString("TR_STATUS");
                transac.origen = rs.getInt("TR_ORIGEN");
                array.add(transac);
            }
            if (array.size() > 0) {
                elementos = new TransaccionesVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (TransaccionesVO) array.get(i);
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
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }

        return elementos;
    }

    public TransaccionesVO[] getElementosIng(String fecha_inicio, String fecha_fin) throws ClientesException {
        TransaccionesVO transac = null;
        Connection cn = null;
        ArrayList<TransaccionesVO> array = new ArrayList<TransaccionesVO>();
        TransaccionesVO elementos[] = null;
        String query = "SELECT TR_NUMERO_TRANSACCION, TR_SECUENCIAL, TR_TIPO_TRANSACCION, TR_NUMCLIENTE, TR_NUMCREDITO, TR_FECHA_PROCESO, TR_FECHA_VALOR, TR_NUMPRODUCTO,"
                + "TR_CENTRO_COSTOS, TR_CODIGO_RUBRO, TR_STATUS_RUBRO, TR_MONTO, TR_STATUS, TR_ORIGEN, tr_fondeador FROM D_TRANSACCIONES WHERE TR_STATUS = 'ING' AND TR_FECHA_PROCESO BETWEEN ?  AND ? "
                + "ORDER BY TR_TIPO_TRANSACCION, tr_fondeador, TR_CENTRO_COSTOS, TR_NUMPRODUCTO,"
                + "TR_NUMERO_TRANSACCION, TR_SECUENCIAL";
        try {
            cn = getCWConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, fecha_inicio);
            ps.setString(2, fecha_fin);
            myLogger.debug("Ejecutando = " + ps);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                transac = new TransaccionesVO();
                transac.numTransaccion = rs.getInt("TR_NUMERO_TRANSACCION");
                transac.secuencial = rs.getInt("TR_SECUENCIAL");
                transac.tipoTransaccion = rs.getString("TR_TIPO_TRANSACCION");
                transac.numCliente = rs.getInt("TR_NUMCLIENTE");
                transac.numCredito = rs.getInt("TR_NUMCREDITO");
                transac.fechaProceso = rs.getDate("TR_FECHA_PROCESO");
                transac.fechaValor = rs.getDate("TR_FECHA_VALOR");
                transac.numProducto = rs.getInt("TR_NUMPRODUCTO");
                transac.centroCostos = rs.getInt("TR_CENTRO_COSTOS");
                transac.rubro = rs.getString("TR_CODIGO_RUBRO");
                transac.statusRubro = rs.getString("TR_STATUS_RUBRO");
                transac.monto = rs.getDouble("TR_MONTO");
                transac.status = rs.getString("TR_STATUS");
                transac.origen = rs.getInt("TR_ORIGEN");
                transac.fondeador = rs.getInt("tr_fondeador");
                array.add(transac);
            }
            if (array.size() > 0) {
                elementos = new TransaccionesVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (TransaccionesVO) array.get(i);
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
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }

        return elementos;
    }

    public boolean getTransaccionCont(int numCliente, int numCredito) throws ClientesException {
        boolean contabilizado = false;
        Connection cn = null;
        ArrayList<TransaccionesVO> array = new ArrayList<TransaccionesVO>();
        String query = "SELECT * FROM d_transacciones WHERE tr_numcliente=? AND tr_numcredito=? AND tr_status !='ING'";
        try {
            cn = getCWConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numCredito);
            ResultSet rs = ps.executeQuery();
            myLogger.debug("Ejecutando = " + ps.toString());
            while (rs.next()) {
                contabilizado = true;
            }
        } catch (SQLException sqle) {
            myLogger.error("getTransaccionCont", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getTransaccionCont", e);
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

        return contabilizado;
    }

    public int addTransaccion(TransaccionesVO transac) throws ClientesException {

        String query = "INSERT INTO D_TRANSACCIONES(TR_NUMERO_TRANSACCION, TR_SECUENCIAL, TR_TIPO_TRANSACCION, TR_NUMCLIENTE, TR_NUMCREDITO, TR_FECHA_PROCESO, "
                + "TR_FECHA_VALOR, TR_NUMPRODUCTO, TR_CENTRO_COSTOS, TR_CODIGO_RUBRO, TR_STATUS_RUBRO, TR_MONTO, TR_STATUS, TR_ORIGEN, tr_fondeador) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                + "?, ?, ?, ?, ?) ";

        Connection cn = null;
        ResultSet res = null;
        int param = 1;
        int insert = 0;
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
            ps.setInt(param++, transac.origen);
            ps.setInt(param++, transac.fondeador);
            myLogger.debug("Ejecutando = "+ps);
            ps.executeUpdate();
            myLogger.debug("Ejecutado");
            res = ps.getGeneratedKeys();
            myLogger.debug("Recupero llave");
            if (res.next())
                insert = res.getInt(1);
            myLogger.debug("Llave generada: "+insert);
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
                myLogger.error("addTransaccion", sqle);
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return insert;
    }

    
    
    public boolean addTransaccion(TransaccionesVO transac, Connection con) throws ClientesException {

        String query = "INSERT INTO D_TRANSACCIONES(TR_NUMERO_TRANSACCION, TR_SECUENCIAL, TR_TIPO_TRANSACCION, TR_NUMCLIENTE, TR_NUMCREDITO, TR_FECHA_PROCESO, "
                + "TR_FECHA_VALOR, TR_NUMPRODUCTO, TR_CENTRO_COSTOS, TR_CODIGO_RUBRO, TR_STATUS_RUBRO, TR_MONTO, TR_STATUS, TR_ORIGEN, tr_fondeador) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                + "?, ?, ?, ?, ?) ";
        int param = 1;
        PreparedStatement ps = null;
        boolean listo = true;
        try {
            ps = con.prepareStatement(query);
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
            ps.setInt(param++, transac.origen);
            ps.setInt(param++, transac.fondeador);
            myLogger.debug("Ejecutando "+ps);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("addTransaccion", sqle);
            return listo = false;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return listo;
    }

    public int updateStatusTransaccion(TransaccionesVO transac) throws ClientesException {

        String query = "UPDATE D_TRANSACCIONES SET TR_STATUS = ? "
                + "WHERE TR_NUMERO_TRANSACCION = ? ";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            cn = getCWConnection();
            ps = cn.prepareStatement(query);
            ps.setString(param++, transac.status);
            ps.setInt(param++, transac.numTransaccion);
            res = ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("updateStatusTransaccion", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateStatusTransaccion", e);
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

    public int getMaxTransaccion() throws ClientesException {

        String query = "SELECT MAX(TR_NUMERO_TRANSACCION) FROM D_TRANSACCIONES ";

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
            myLogger.error("getMaxTransaccion", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getMaxTransaccion", e);
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
    
    public int getMaxTransaccion(Connection con) throws ClientesException {
        
        String query = "SELECT MAX(TR_NUMERO_TRANSACCION) FROM D_TRANSACCIONES ";
        int numTransaccion = 0;
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(query);
            myLogger.debug("Ejecutando : " + query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                numTransaccion = rs.getInt(1);
            }
        } catch (SQLException sqle) {
            myLogger.error("getMaxTransaccion", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getMaxTransaccion", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return numTransaccion;
    }

    public void eliminaTransaccionesCiclo(int idEquipo, int idCiclo) throws ClientesDBException {
        String query = "UPDATE d_transacciones SET tr_status='DEL' WHERE tr_numcliente=? AND tr_numcredito=? AND tr_status='ING'";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getCWConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, idEquipo);
            ps.setInt(2, idCiclo);
            myLogger.debug(ps.toString());
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("eliminaTransaccionesCiclo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("eliminaTransaccionesCiclo", e);
            throw new ClientesDBException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
    }
         public void updateTransaccionesCiclo(int idEquipo, int idCiclo,int numtransaccion, Connection con) throws ClientesDBException {
        String query = "update d_transacciones" +
                       " set  TR_NUMCLIENTE= ?, tr_numcredito= ?" +
                       " where tr_numero_transaccion = ?;";
        
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(query);
            ps.setInt(1, idEquipo);
            ps.setInt(2, idCiclo);
            ps.setInt(3,numtransaccion);
            myLogger.debug(ps.toString());
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("eliminaTransaccionesCiclo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("eliminaTransaccionesCiclo", e);
            throw new ClientesDBException(e.getMessage());
        } 
    }

    public ArrayList<TransaccionesVO> getTransaccionesCuaIng(int idEquipo, int idCiclo) throws ClientesDBException, ClientesException {
        String query = "SELECT * FROM d_transacciones WHERE tr_numcliente=? AND tr_numcredito=? AND (tr_status='CUA' OR tr_status='ING')";
        ArrayList<TransaccionesVO> transacciones = new ArrayList<TransaccionesVO>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getCWConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, idEquipo);
            ps.setInt(2, idCiclo);
            rs = ps.executeQuery();
            myLogger.debug(ps.toString());
            while (rs.next()) {
                TransaccionesVO transaccion = new TransaccionesVO();
                transaccion.numTransaccion = rs.getInt("tr_numero_transaccion");
                transaccion.secuencial = rs.getInt("tr_secuencial");
                transaccion.tipoTransaccion = rs.getString("tr_tipo_transaccion");
                transaccion.numCliente = rs.getInt("tr_numcliente");
                transaccion.numCredito = rs.getInt("tr_numcredito");
                transaccion.fechaProceso = rs.getDate("tr_fecha_proceso");
                transaccion.fechaValor = rs.getDate("tr_fecha_valor");
                transaccion.numProducto = rs.getInt("tr_numproducto");
                transaccion.centroCostos = rs.getInt("tr_centro_costos");
                transaccion.rubro = rs.getString("tr_codigo_rubro");
                transaccion.statusRubro = rs.getString("tr_status_rubro");
                transaccion.monto = rs.getDouble("tr_monto");
                transaccion.status = rs.getString("tr_status");
                transaccion.origen = rs.getInt("tr_origen");
                transaccion.fondeador = rs.getInt("tr_fondeador");
                transacciones.add(transaccion);
            }
        } catch (SQLException sqle) {
            myLogger.error("getTransaccionesCua", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getTransaccionesCua", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return transacciones;
    }
    
    public ArrayList<TransaccionesVO> getTransacciones(int idEquipo, int idCiclo) throws ClientesDBException, ClientesException {
        
        String query = "SELECT * FROM d_transacciones WHERE tr_numcliente=? AND tr_numcredito=?;";
        ArrayList<TransaccionesVO> transacciones = new ArrayList<TransaccionesVO>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getCWConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, idEquipo);
            ps.setInt(2, idCiclo);
            rs = ps.executeQuery();
            myLogger.debug(ps.toString());
            while (rs.next()) {
                TransaccionesVO transaccion = new TransaccionesVO();
                transaccion.numTransaccion = rs.getInt("tr_numero_transaccion");
                transaccion.secuencial = rs.getInt("tr_secuencial");
                transaccion.tipoTransaccion = rs.getString("tr_tipo_transaccion");
                transaccion.numCliente = rs.getInt("tr_numcliente");
                transaccion.numCredito = rs.getInt("tr_numcredito");
                transaccion.fechaProceso = rs.getDate("tr_fecha_proceso");
                transaccion.fechaValor = rs.getDate("tr_fecha_valor");
                transaccion.numProducto = rs.getInt("tr_numproducto");
                transaccion.centroCostos = rs.getInt("tr_centro_costos");
                transaccion.rubro = rs.getString("tr_codigo_rubro");
                transaccion.statusRubro = rs.getString("tr_status_rubro");
                transaccion.monto = rs.getDouble("tr_monto");
                transaccion.status = rs.getString("tr_status");
                transaccion.origen = rs.getInt("tr_origen");
                transaccion.fondeador = rs.getInt("tr_fondeador");
                transacciones.add(transaccion);
            }
        } catch (SQLException sqle) {
            myLogger.error("getTransacciones", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getTransacciones", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return transacciones;
    }

    public void insertaTransaccionContraria(TransaccionesVO transaccion) throws ClientesDBException, ClientesException {
        String query = "INSERT INTO d_transacciones (tr_numero_transaccion, tr_secuencial, tr_tipo_transaccion, tr_numcliente, tr_numcredito, tr_fecha_proceso, tr_fecha_valor,"
                + "tr_numproducto, tr_centro_costos, tr_codigo_rubro, tr_status_rubro, tr_monto, tr_status, tr_origen, tr_fondeador) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getCWConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, transaccion.numTransaccion);
            ps.setInt(2, transaccion.secuencial);
            ps.setString(3, transaccion.tipoTransaccion);
            ps.setInt(4, transaccion.numCliente);
            ps.setInt(5, transaccion.numCredito);
            ps.setDate(6, transaccion.fechaProceso);
            ps.setDate(7, transaccion.fechaValor);
            ps.setInt(8, transaccion.numProducto);
            ps.setInt(9, transaccion.centroCostos);
            ps.setString(10, transaccion.rubro);
            ps.setString(11, transaccion.statusRubro);
            ps.setDouble(12, transaccion.monto);
            ps.setString(13, transaccion.status);
            ps.setInt(14, transaccion.origen);
            ps.setInt(15, transaccion.fondeador);
            myLogger.debug("Ejecutando:" + ps.toString());
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("insertaTransaccionContraria", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("insertaTransaccionContraria", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
    }
    public int addTransaccion(Connection con, TransaccionesVO transac) throws ClientesException{
    
        return addTransaccion(con, transac,true);
    }
    
    public int addTransaccion(Connection con, TransaccionesVO transac,boolean cierraConexion) throws ClientesException {

        String query = "INSERT INTO D_TRANSACCIONES(TR_NUMERO_TRANSACCION, TR_SECUENCIAL, TR_TIPO_TRANSACCION, TR_NUMCLIENTE, TR_NUMCREDITO, TR_FECHA_PROCESO, "
                + "TR_FECHA_VALOR, TR_NUMPRODUCTO, TR_CENTRO_COSTOS, TR_CODIGO_RUBRO, TR_STATUS_RUBRO, TR_MONTO, TR_STATUS, TR_ORIGEN, tr_fondeador) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
                + "?, ?, ?, ?, ?) ";

        ResultSet res = null;
        PreparedStatement ps = null;
        int param = 1;
        int insert = 0;
        try {
            ps = con.prepareStatement(query);
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
            ps.setInt(param++, transac.origen);
            ps.setInt(param++, transac.fondeador);
            myLogger.debug("Ejecutando = "+ps);
            ps.executeUpdate();
            myLogger.debug("Ejecutado");
            res = ps.getGeneratedKeys();
            myLogger.debug("Recupero llave");
            if (res.next())
                insert = res.getInt(1);
            myLogger.debug("Llave generada: "+insert);
        } catch (SQLException sqle) {
            myLogger.error("addTransaccion", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addTransaccion", e);
            throw new ClientesException(e.getMessage());
        } finally {
            if(cierraConexion){
                try {
                    if (ps != null)
                        ps.close();
                    if (res != null)
                        res.close();
                } catch (SQLException sqle) {
                    myLogger.error("addTransaccion", sqle);
                    throw new ClientesDBException(sqle.getMessage());
                }
            }
            
        }
        return insert;
    }
}
