/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.dao;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.ControlPagosVO;
import com.sicap.clientes.vo.GrupoVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Alex
 */
public class ControlPagosDAO extends DAOMaster{
    
    public ArrayList<ControlPagosVO> getControlPagos(GrupoVO grupo) throws ClientesException, SQLException{
        
        ArrayList<ControlPagosVO> arrConPagos = new ArrayList<ControlPagosVO>();
        ControlPagosVO pago = null;
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        
        String query = "SELECT cp_numgrupo, cp_numcredito, cp_numciclo, cp_numpago, cp_fecha_calendario, cp_fecha_real, cp_fecha_pago, cp_diferencia_fechas, "+
                "cp_monto_pagar, cp_monto_pago, cp_diferencia_monto, cp_ahorro_sem, cp_saldo_ahorro "+
                "FROM CLIENTES.d_control_pagos WHERE cp_numgrupo=? AND cp_numcredito=?";
        
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, grupo.idGrupo);
            ps.setInt(2, grupo.idGrupoIBS);
            
            res = ps.executeQuery();
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros ["+grupo.idGrupo+", "+grupo.idGrupoIBS+"]");
            
            while (res.next()) {                
                arrConPagos.add(new ControlPagosVO(res.getInt("cp_numpago"), res.getDate("cp_fecha_calendario"), res.getDate("cp_fecha_real"), res.getDate("cp_fecha_pago"), 
                        res.getInt("cp_diferencia_fechas"), res.getDouble("cp_monto_pagar"), res.getDouble("cp_monto_pago"), res.getDouble("cp_diferencia_monto"), 
                        res.getDouble("cp_ahorro_sem"), res.getDouble("cp_saldo_ahorro")));
            }
            
        } catch(SQLException sqle) {
            Logger.debug("SQLException en getSucursal : "+sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesDBException(sqle.getMessage());
        } catch(Exception e) {
            Logger.debug("Excepcion en getSucursal : "+e.getMessage());
            e.printStackTrace();
        }
        con.close();
        return arrConPagos;
    }
    
}
