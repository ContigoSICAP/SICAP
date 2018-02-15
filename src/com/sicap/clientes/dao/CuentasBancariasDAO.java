/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.dao;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import com.sicap.clientes.vo.CuentaBancariaVO;

/**
 *
 * @author avillanueva
 */
public class CuentasBancariasDAO extends DAOMaster {

    private static Logger myLogger = Logger.getLogger(AjusteCreditoDAO.class);
    private Connection con = null;
    private Statement st = null;
    private PreparedStatement ps = null;
    private ResultSet res = null;
    private String query = "";

    public int getNumCuentaBancaria(int banco, String cuenta, String tipo) throws ClientesDBException, ClientesException {

        int num = 0;
        try {
            query = "SELECT cb_idcuenta FROM c_cuentas_bancarias WHERE cb_banco=? AND cb_convenio=? AND cb_tipocuenta=?;";
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, banco);
            ps.setString(2, cuenta);
            ps.setString(3, tipo);
            myLogger.debug("Ejecutando "+ps.toString());
            res = ps.executeQuery();
            if(res.next())
                num = res.getInt("cb_idcuenta");
        } catch (SQLException sqle) {
            myLogger.error("getNumCuentaBancaria", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getNumCuentaBancaria", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null)
                    con.close();
                if (ps != null)
                    ps.close();
                if (res != null)
                    res.close();
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return num;
    }
    public CuentaBancariaVO getCuentaBancaria(int  idCuenta) throws ClientesDBException, ClientesException {

        CuentaBancariaVO cuentaBancaria = new CuentaBancariaVO();
        try {
            query = "SELECT * FROM c_cuentas_bancarias WHERE cb_idcuenta = ?;";
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, idCuenta);            
            myLogger.debug("Ejecutando "+ps.toString());
            res = ps.executeQuery();
            if(res.next()){
                cuentaBancaria.setIdCuenta(res.getInt("cb_idcuenta"));
                cuentaBancaria.setIdBanco(res.getInt("cb_banco"));
                cuentaBancaria.setNumCuenta(res.getString("cb_cuenta"));
                cuentaBancaria.setConvenio(res.getString("cb_convenio"));
                cuentaBancaria.setTipoCuenta(res.getString("cb_tipocuenta"));
                cuentaBancaria.setEstatus(res.getInt("cb_estatus"));
                cuentaBancaria.setNombreCuenta(res.getString("cb_nombrecuenta"));
                cuentaBancaria.setComision(res.getFloat("cb_valorcomision"));      
            }
        } catch (SQLException sqle) {
            myLogger.error("getNumCuentaBancaria", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getNumCuentaBancaria", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null)
                    con.close();
                if (ps != null)
                    ps.close();
                if (res != null)
                    res.close();
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return cuentaBancaria;
    }
}
