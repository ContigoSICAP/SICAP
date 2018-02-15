
package com.sicap.clientes.dao;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.vo.PagoPagareVO;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
    * Clase para la administracion de pagos de pagarés
    * 
*/
public class PagoPagareDAO extends DAOMaster
{
    private static final Logger myLogger = Logger.getLogger(PagoPagareDAO.class);
    
    
    /**
     * Metodo para dar de alta de pago a pagaré correspondiente a un fondeador
     * @param datosPago
     * @return <ul>
     *          <li>1: se insertaron datos en la tabla pagos_manuales</li>
     *          <li>0: no se insertaron datos en la tabla pagos_manuales</li>
     *          </ul>
     * @throws com.sicap.clientes.exceptions.ClientesException
     */
    public int altaPagoPagare(PagoPagareVO datosPago) throws ClientesException 
    {
        myLogger.debug("Entra altaPagoPagare()");
        Connection cn = null;
        int res = 0;
        String query = "INSERT INTO d_pagos_manuales(pm_numpagare, pm_fecha_pago, pm_monto, pm_estatus, pm_ref_bancaria, pm_username) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
    
        try 
        {
            cn = getConnection();
            PreparedStatement ps = null;
            if (cn != null) 
            {
                ps = cn.prepareStatement(query);
            } 
            else 
            {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            }
            //int numAmort = getNumAmort(datosPago.getNumPago(),datosPago.getNumPagare());
            ps.setInt(1, datosPago.getNumPagare());
            Date fechaPago=Convertidor.toSqlDate(datosPago.getFechaPago());
            ps.setDate(2, fechaPago);
            ps.setDouble(3, datosPago.getMonto());
            ps.setInt(4, datosPago.getEstatus());
            ps.setString(5, datosPago.getRefBancaria());
            ps.setString(6, datosPago.getUsername());
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Pago Pägaré = " + datosPago.getNumPago());
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } 
        catch (SQLException sqle) 
        {
            myLogger.error("altaPagoPagare()", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } 
        catch (Exception e) 
        {
            myLogger.error("altaPagoPagare()", e);
            throw new ClientesException(e.getMessage());
        } 
        finally 
        {
            try 
            {
                if (cn != null) 
                {
                    cn.close();
                    myLogger.debug("Se ha cerrado correctamente la conexion altaPagoPagare()");
                }
            } 
            catch (SQLException sqle) 
            {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return res;
    }
    //obtener los pagos activos
    public ArrayList<PagoPagareVO> getPagosActivos(int estatus) throws ClientesException 
    {
        String query = "select pm.pm_numpago, pm.pm_ref_bancaria from d_pagos_manuales pm where pm.pm_estatus =?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        ArrayList<PagoPagareVO> pagos = new ArrayList<PagoPagareVO>();
        try 
        {
            con = getConnection();
            ps = con.prepareStatement(query);

            ps.setInt(1, estatus);

            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros ["+ estatus+"]");

            res = ps.executeQuery();
            
            while (res.next()) 
            {
                pagos.add(new PagoPagareVO(res.getInt("pm.pm_numpago"), res.getString("pm.pm_ref_bancaria")));
            }

        } 
        catch (SQLException sqle) 
        {
            myLogger.error("getPagosActivos()", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } 
        catch (Exception e) 
        {
            myLogger.error("getPagosActivos()", e);
            throw new ClientesException(e.getMessage());
        } 
        finally 
        {
            try 
            {
                if (con != null) {
                    con.close();
                }
            } 
            catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return pagos;
    }
    
     
    //metodo insertar pago pagare V2.0
    public boolean insertarPagoPagare(PagoPagareVO datosPago, Connection cn) {
        String query = "INSERT INTO d_pagos_manuales (pm_numpagare, pm_fecha_pago, pm_monto, pm_estatus, pm_ref_bancaria, pm_username)"
                + "values (?,?,?,?,?,?)";
        try {
            if(cn == null){
                cn = getConnection();
            }

            PreparedStatement pst = cn.prepareStatement(query);

            pst.setInt(1, datosPago.getNumPagare());
            Date fechaPago=Convertidor.toSqlDate(datosPago.getFechaPago());
            pst.setDate(2, fechaPago);
            pst.setDouble(3, datosPago.getMonto());
            pst.setInt(4, datosPago.getEstatus());
            pst.setString(5, datosPago.getRefBancaria());
            pst.setString(6, datosPago.getUsername());
            
            int n = pst.executeUpdate();

            if (n != 0) 
                return true;
            else
                return false;
        } 
        catch (Exception e) {
           myLogger.error("SQLException en insertarPagoPagare(): ", e);
            return false;
        }
    }
    /**
     * Obtiene el numero de amortizacion para un numero de pago y numero de pagare especifico
     *
     * @param numPago Numero de pago
     * @param numPagare Numero de pagare
     * @return Numero de amortizacion
     * @throws ClientesException
     */
    public int getNumAmort(int numPago, int numPagare) throws ClientesException {
        int resultado = 0;
        Connection cn = null;
        String query = "SELECT d.pm_numamort FROM d_pagos_manuales d where d.pm_numpago=? AND d.pm_numpagare=?";
        try {
            int param = 1;
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(param++, numPago);
            ps.setInt(param++, numPagare);
            myLogger.debug("Ejecutando = " + ps);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                resultado = rs.getInt("d.pm_numamort");
                myLogger.debug("NumAmort: " + resultado);
            }
        } catch (SQLException sqle) {
            myLogger.error("SQLException en getNumAmort(): ", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("Excepcion en getNumAmort(): ", e);
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
        return resultado;
    }
    
    public double obtenerSumaPagosManuales(int numPagare) throws ClientesException {
        double resultado = 0;
        Connection cn = null;
        String query = "SELECT sum(pm_monto) as SumaPagos FROM d_pagos_manuales d where d.pm_numpagare=?";
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, numPagare);
            myLogger.debug("Ejecutando = " + ps);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                resultado = rs.getDouble("SumaPagos");
                myLogger.debug("SumaPagos: " + resultado);
            }
        } catch (SQLException sqle) {
            myLogger.error("SQLException en obtenerSumaPagosManuales(): ", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("Excepcion en obtenerSumaPagosManuales(): ", e);
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
        return resultado;
    }
    
    public int actualizarPagoTotalPagare(int numPagare, double restanteTotalPagare, Connection cn) throws ClientesException 
    {
        myLogger.debug("Entra actualizarPagoTotalPagare()");
        int res = 0;
        String query = "UPDATE d_pagares set pa_totalPagar = ?, pa_estatus = ? where pa_numpagare = ?";
        boolean conexionAbierta = true;
        try 
        {
            PreparedStatement ps = null;
            if (cn != null) 
            {
                ps = cn.prepareStatement(query);
            } 
            else 
            {
                cn = getConnection();
                ps = cn.prepareStatement(query);
                conexionAbierta=false;
            }
            //int numAmort = getNumAmort(datosPago.getNumPago(),datosPago.getNumPagare());
            ps.setDouble(1, restanteTotalPagare);
            if(restanteTotalPagare==0){
                ps.setInt(2, ClientesConstants.PAGARE_PAGADO);
            }else{
                ps.setInt(2, ClientesConstants.PAGARE_VIGENTE);
            }
            ps.setInt(3, numPagare);

            myLogger.debug("Ejecutando = " + query);
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } 
        catch (SQLException sqle) 
        {
            myLogger.error("actualizarPagoTotalPagare()", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } 
        catch (Exception e) 
        {
            myLogger.error("actualizarPagoTotalPagare()", e);
            throw new ClientesException(e.getMessage());
        } 
        finally 
        {
            try 
            {
                if (cn != null && !conexionAbierta) 
                {
                    cn.close();
                    myLogger.debug("Se ha cerrado correctamente la conexion actualizarPagoTotalPagare()");
                }
            } 
            catch (SQLException sqle) 
            {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return res;
    }
}
