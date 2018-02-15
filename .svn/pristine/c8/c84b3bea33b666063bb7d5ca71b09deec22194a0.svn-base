/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.dao;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.PagoVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Alex
 */
public class CarteraTCIDAO extends DAOMaster{
    
    private Connection con = null;
    private PreparedStatement pstm = null;
    private ResultSet res = null;
    private String query = "";
    
    public boolean verificaEstatusCredito(String referencia) throws ClientesException{
        
        boolean cedido = false;
        query = "SELECT ib_estatus FROM d_saldos WHERE ib_referencia=?";
        try {
            con = getConnection();
            pstm = con.prepareStatement(query);
            pstm.setString(1, referencia);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros[ "+referencia+" ]");
            res = pstm.executeQuery();
            while (res.next()) {                
                if(res.getInt("ib_estatus") == 7)
                    cedido = true;
            }
        } catch (SQLException se) {
            Logger.debug("Excepcion en verificaEstatusCredito : "+se.getMessage());
            se.printStackTrace();
            throw new ClientesException(se.getMessage());
        } catch (Exception e){
            Logger.debug("SQLException en verificaEstatusCredito : "+e.getMessage());
            e.printStackTrace();throw new ClientesException(e.getMessage());
        } finally{
            try {
                if(con!=null)
                    con.close();
            } catch (SQLException se) {
                se.printStackTrace();
                throw new ClientesDBException(se.getMessage());
            }
        }
        return cedido;
    }
    
    public boolean verificaEstatusCedido(String referencia) throws ClientesException{
        
        boolean liquidado = false;
        query = "SELECT ib_estatus FROM d_saldos WHERE ib_referencia=?";
        try {
            //con = getTCIConnection();
            pstm = con.prepareStatement(query);
            pstm.setString(1, referencia);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros[ "+referencia+" ]");
            res = pstm.executeQuery();
            while(res.next()){
                if(res.getInt("ib_estatus") == 3)
                    liquidado = true;
            }
        } catch (SQLException se) {
            Logger.debug("Excepcion en verificaEstatusCedido : "+se.getMessage());
            se.printStackTrace();
            throw new ClientesException(se.getMessage());
        } catch (Exception e){
            Logger.debug("SQLException en verificaEstatusCedido : "+e.getMessage());
            e.printStackTrace();throw new ClientesException(e.getMessage());
        } finally{
            try {
                if(con!=null)
                    con.close();
            } catch (SQLException se) {
                se.printStackTrace();
                throw new ClientesDBException(se.getMessage());
            }
        }
        return liquidado;
    }
    
    public boolean verificaPagoDuplicado(String referencia, double monto, java.sql.Date fechaPago, int bancoRefe) throws ClientesException{
        
        boolean duplicado = false;
        query = "SELECT pc_monto FROM d_pagos_cartera WHERE pc_referencia=? AND pc_monto=? AND pc_fecha_pago=? AND pc_banco_referencia=?";
        try {
            //con = getTCIConnection();
            pstm = con.prepareStatement(query);
            pstm.setString(1, referencia);
            pstm.setDouble(2, monto);
            pstm.setDate(3, fechaPago);
            pstm.setInt(4, bancoRefe);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros[ "+referencia+", "+monto+","+fechaPago+", "+bancoRefe+" ]");
            res = pstm.executeQuery();
            if(res.next())
                duplicado = true;
        } catch (SQLException se) {
            Logger.debug("Excepcion en verificaPagoDuplicado : "+se.getMessage());
            se.printStackTrace();
            throw new ClientesException(se.getMessage());
        } catch (Exception e){
            Logger.debug("SQLException en verificaPagoDuplicado : "+e.getMessage());
            e.printStackTrace();throw new ClientesException(e.getMessage());
        } finally{
            try {
                if(con!=null)
                    con.close();
            } catch (SQLException se) {
                se.printStackTrace();
                throw new ClientesDBException(se.getMessage());
            }
        }
        return duplicado;
    }
    
    public void insertPagoTCI(PagoVO pago) throws ClientesException{
        
        query = "INSERT INTO d_pagos_cartera VALUES (?,?,?,?,?,?,?,0,0,?,0)";
        try {
            con = getTCIConnection();
            pstm = con.prepareStatement(query);
            pstm.setString(1, pago.getReferencia());
            pstm.setDouble(2, pago.getMonto());
            pstm.setDate(3, pago.getFechaPago());
            pstm.setTimestamp(4, pago.getFechaHora());
            pstm.setInt(5, pago.getBancoReferencia());
            pstm.setString(6, pago.getSucursal());
            pstm.setInt(7, pago.getStatus());
            pstm.setInt(8, pago.getEnviado());
            Logger.debug("Ejecutando "+query);
            Logger.debug("Patametos[ "+pago.getReferencia()+" ]");
            pstm.execute();
        } catch (SQLException se) {
            Logger.debug("Excepcion en insertPagoTCI : "+se.getMessage());
            se.printStackTrace();
            throw new ClientesException(se.getMessage());
        } catch (Exception e){
            Logger.debug("SQLException en insertPagoTCI : "+e.getMessage());
            e.printStackTrace();throw new ClientesException(e.getMessage());
        } finally{
            try {
                if(con!=null)
                    con.close();
            } catch (SQLException se) {
                se.printStackTrace();
                throw new ClientesDBException(se.getMessage());
            }
        }
    }
    
    public void insertIncidenciaTCI(PagoVO pago, int tipo, String nombreTipo) throws ClientesException{
        
        query = "INSERT INTO d_incidencias_pagos_ref(ip_fecha_mov,ip_tipo_incidencia,ip_observaciones,ip_fecha_hora,ip_referencia,ip_banco_referencia,ip_reprocesar,ip_id_contabilidad,ip_monto) VALUES (?,?,?,?,?,?,1,0,?)";
        try {
            //con = getTCIConnection();
            pstm = con.prepareStatement(query);
            pstm.setDate(1, pago.getFechaPago());
            pstm.setInt(2, tipo);
            pstm.setString(3, nombreTipo);
            pstm.setTimestamp(4, pago.getFechaHora());
            pstm.setString(5, pago.getReferencia());
            pstm.setInt(6, pago.getBancoReferencia());
            pstm.setDouble(7, pago.getMonto());
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros["+pago.getReferencia()+"]");
            pstm.execute();
        } catch (SQLException se) {
            Logger.debug("Excepcion en insertIncidenciaTCI : "+se.getMessage());
            se.printStackTrace();
            throw new ClientesException(se.getMessage());
        } catch (Exception e){
            Logger.debug("SQLException en insertIncidenciaTCI : "+e.getMessage());
            e.printStackTrace();throw new ClientesException(e.getMessage());
        } finally{
            try {
                if(con!=null)
                    con.close();
            } catch (SQLException se) {
                se.printStackTrace();
                throw new ClientesDBException(se.getMessage());
            }
        }
    }
    
}
