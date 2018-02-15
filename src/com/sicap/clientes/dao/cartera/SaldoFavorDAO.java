/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.dao.cartera;

import com.sicap.clientes.dao.DAOMaster;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.PagoVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Alex
 */
public class SaldoFavorDAO extends DAOMaster {
    
    private Connection conn = null;
    private Statement stm = null;
    private PreparedStatement pstm = null;
    private ResultSet res = null;
    private String query = "";
    private int resInt = 0;
    
    public Integer aplicaSaldo(int idGrupo, int numCiclo)  throws ClientesDBException{
        int aplicado = 0;
        try {
            conn = getCWConnection();
            query = "UPDATE d_credito set CR_APLICA_SALDO=1 WHERE CR_NUM_CLIENTE=? AND CR_NUM_SOLICITUD=?";
            pstm = conn.prepareStatement(query);
            pstm.setInt(1, idGrupo);
            pstm.setInt(2, numCiclo);
            Logger.debug("Ejecutando: "+query);
            Logger.debug("Parametros: ["+idGrupo+","+numCiclo+"]");
            resInt = pstm.executeUpdate();
            if(resInt != 0)
                aplicado = 1;
        }catch(SQLException sqle) {
            Logger.debug("ERROR: Aplicacion de Uso de Saldo a Favor en Base: "+sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
        } finally {
            try {
                if ( conn!=null ) conn.close();
            } catch(SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return aplicado;
    }
    
    public Integer aplicaUsoSaldo(int idGrupo, int numCiclo)  throws ClientesDBException{
        int autorizado = 0;
        try {
            conn = getCWConnection();
            query = "SELECT CR_APLICA_SALDO FROM d_credito WHERE CR_NUM_CLIENTE=? AND CR_NUM_SOLICITUD=? AND CR_APLICA_SALDO=1 AND CR_MONTO_CUENTA>0";
            pstm = conn.prepareStatement(query);
            pstm.setInt(1, idGrupo);
            pstm.setInt(2, numCiclo);
            Logger.debug("Ejecutando: "+query);
            Logger.debug("Parametros: ["+idGrupo+","+numCiclo+"]");
            res = pstm.executeQuery();
            while(res.next()){
                autorizado = 1;
            }
        }catch(SQLException sqle) {
            Logger.debug("ERROR: Aplicacion de Uso de Saldo a Favor en Base: "+sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
        } finally {
            try {
                if ( conn!=null ) conn.close();
            } catch(SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return autorizado;
    }
    
    public double montoSaldoFavor(String referencia, Date fecha)  throws ClientesDBException{
        
        double monto = 0;
        try {
            conn = getConnection();
            query = "SELECT SUM(pc_monto) AS monto FROM d_pagos_cartera WHERE pc_referencia=? AND pc_fecha_pago<=? AND pc_banco_referencia=8;";
            pstm = conn.prepareStatement(query);
            pstm.setString(1, referencia);
            pstm.setDate(2, Convertidor.toSqlDate(fecha));
            Logger.debug("Ejecutando: "+query);
            Logger.debug("Parametros: ["+referencia+"]");
            res = pstm.executeQuery();
            while(res.next()){
                monto = res.getDouble("monto");
            }
        }catch(SQLException sqle) {
            Logger.debug("ERROR: montoSaldoFavor "+sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
        } finally {
            try {
                if ( conn!=null ) conn.close();
            } catch(SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return monto;
    }
    
    public ArrayList<PagoVO> montoPagoBancos(String referencia, Date fecha)  throws ClientesDBException{
        
        ArrayList<PagoVO> arr = new ArrayList<PagoVO>();
        try {
            conn = getConnection();
            query = "SELECT SUM(pc_monto) AS monto,pc_banco_referencia FROM d_pagos_cartera WHERE pc_referencia=? AND pc_fecha_pago=? GROUP BY pc_banco_referencia;";
            pstm = conn.prepareStatement(query);
            pstm.setString(1, referencia);
            pstm.setDate(2, Convertidor.toSqlDate(fecha));
            Logger.debug("Ejecutando: "+query);
            Logger.debug("Parametros: ["+referencia+", "+fecha+"]");
            res = pstm.executeQuery();
            while(res.next()){
                arr.add(new PagoVO(res.getDouble("monto"), res.getInt("pc_banco_referencia")));
            }
        }catch(SQLException sqle) {
            Logger.debug("ERROR: montoSaldoFavor "+sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
        } finally {
            try {
                if ( conn!=null ) conn.close();
            } catch(SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return arr;
    }
}
