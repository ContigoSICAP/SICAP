package com.sicap.clientes.dao;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.vo.PaynetVO;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import javax.naming.NamingException;
import org.apache.log4j.Logger;

public class PaynetDAO extends DAOMaster{
    
    private static Logger myLogger = Logger.getLogger(PaynetDAO.class);
    
    public ArrayList<PaynetVO> getTransAutorizadasPaynet(Date fecha) throws ClientesDBException{
        
        
        ArrayList<PaynetVO> arrTrans = new ArrayList<PaynetVO>();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet res = null;
        String query = "SELECT bt_idtran,bt_fechaingreso,bt_fechaingpaynet,bt_referenciapaynet,bt_monto,bt_coinciliacion "
                +"FROM d_bitacora_tranwspaynet WHERE DATE(bt_fechaingreso)=? AND bt_coinciliacion=1;";
        try {
            con = getConnection();
            pstm = con.prepareStatement(query);
            pstm.setDate(1, fecha);
            myLogger.debug("pstm "+pstm);
            res = pstm.executeQuery();
            while (res.next()) {                
                arrTrans.add(new PaynetVO(res.getInt("bt_idtran"), res.getString("bt_referenciapaynet"), res.getDouble("bt_monto"), res.getDate("bt_fechaingpaynet"), res.getDate("bt_fechaingreso"), res.getInt("bt_coinciliacion")));
            }
        } catch (SQLException e) {
            myLogger.error("Erro en getTransAutorizadasPaynet", e);
        } catch (Exception e) {
            myLogger.error("Erro en getTransAutorizadasPaynet", e);
        } finally{
            try {
                if (con != null)
                    con.close();
                if (pstm != null)
                    pstm.close();
                if (res != null)
                    res.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return arrTrans;
    }
    
    public boolean updatePagoPaynet(PaynetVO pago, Connection con) throws ClientesDBException{
        
        boolean listo = true;
        PreparedStatement pstm = null;
        String query = "UPDATE d_bitacora_tranwspaynet SET bt_coinciliacion=?,bt_numpago=? WHERE bt_idtran=? AND bt_referenciapaynet=?;";
        try {
            pstm = con.prepareStatement(query);
            pstm.setInt(1, pago.getEstatus());
            pstm.setInt(2, pago.getNumPago());
            pstm.setInt(3, pago.getIdPago());
            pstm.setString(4, pago.getReferenciaPay());
            myLogger.debug("pstm "+pstm);
            pstm.executeUpdate();
        } catch (SQLException e) {
            myLogger.error("Erro en getTransaccionesPaynet", e);
            return listo = false;
        } finally{
            try {
                if (pstm != null)
                    pstm.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return listo;
    }
    
    public ArrayList<PaynetVO> getTransPaynet(String fechaIni, String fechaFin, int estatus) throws ClientesDBException{
        
        ArrayList<PaynetVO> arrTrans = new ArrayList<PaynetVO>();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet res = null;
        String query = "SELECT bt_idtran,bt_fechaingreso,bt_fechaingpaynet,bt_referenciapaynet,bt_monto,bt_coinciliacion "
                +"FROM d_bitacora_tranwspaynet WHERE DATE(bt_fechaingpaynet) BETWEEN ? AND ? AND bt_coinciliacion=?;";
        try {
            con = getConnection();
            pstm = con.prepareStatement(query);
            pstm.setString(1, fechaIni);
            pstm.setString(2, fechaFin);
            pstm.setInt(3, estatus);
            myLogger.debug("pstm "+pstm);
            res = pstm.executeQuery();
            while (res.next()) {                
                arrTrans.add(new PaynetVO(res.getInt("bt_idtran"), res.getString("bt_referenciapaynet"), res.getDouble("bt_monto"), res.getDate("bt_fechaingpaynet"), res.getDate("bt_fechaingreso"), res.getInt("bt_coinciliacion")));
            }
        } catch (SQLException e) {
            myLogger.error("Erro en getTransaccionesPaynet", e);
        } catch (Exception e) {
            myLogger.error("Erro en getTransaccionesPaynet", e);
        } finally{
            try {
                if (con != null)
                    con.close();
                if (pstm != null)
                    pstm.close();
                if (res != null)
                    res.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return arrTrans;
    }
    
    public PaynetVO getTransPaynet(PaynetVO paynet) throws ClientesDBException{
        
        PaynetVO pagoPaynet = new PaynetVO();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet res = null;
        String query = "SELECT bt_fechaingpaynet,bt_coinciliacion,bt_numpago FROM d_bitacora_tranwspaynet WHERE bt_idtran=? AND bt_referenciapaynet=? AND bt_monto=?;";
        try {
            con = getConnection();
            pstm = con.prepareStatement(query);
            pstm.setInt(1, paynet.getIdPago());
            pstm.setString(2, paynet.getReferenciaPay());
            pstm.setDouble(3, paynet.getMonto());
            myLogger.debug("pstm "+pstm);
            res = pstm.executeQuery();
            if (res.next()) {
                pagoPaynet.setFechaRegistro(res.getTimestamp("bt_fechaingpaynet"));
                pagoPaynet.setEstatus(res.getInt("bt_coinciliacion"));
                pagoPaynet.setNumPago(res.getInt("bt_numpago"));
            }
        } catch (SQLException e) {
            myLogger.error("Erro en getTransPaynet", e);
        } catch (Exception e) {
            myLogger.error("Erro en getTransPaynet", e);
        } finally{
            try {
                if (con != null)
                    con.close();
                if (pstm != null)
                    pstm.close();
                if (res != null)
                    res.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return pagoPaynet;
    }
    
    public void setEstatus(PaynetVO pago) throws ClientesDBException{
        
        PreparedStatement pstm = null;
        Connection con =  null;
        String query = "UPDATE d_bitacora_tranwspaynet SET bt_coinciliacion=? WHERE bt_idtran=? AND bt_referenciapaynet=?;";
        try {
            con = getConnection();
            pstm = con.prepareStatement(query);
            pstm.setInt(1, pago.getEstatus());
            pstm.setInt(2, pago.getIdPago());
            pstm.setString(3, pago.getReferenciaPay());
            myLogger.debug("pstm "+pstm);
            pstm.executeUpdate();
        } catch (SQLException e) {
            myLogger.error("Erro en setEstatus", e);
        } catch (NamingException ex) {
            myLogger.error("Erro en setEstatus", ex);
        } finally{
            try {
                if (con != null)
                    con.close();
                if (pstm != null)
                    pstm.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
    }
}
