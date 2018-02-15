
package com.sicap.clientes.dao;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.vo.AmortizacionPagareVO;
import com.sicap.clientes.vo.PagareVO;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.log4j.Logger;

/**
    * Clase para la administracion de amortizaciones de pagares
    * @author nmejia
    */
public class AmortPagareDAO extends DAOMaster
{
    private static final Logger myLogger = Logger.getLogger(AmortPagareDAO.class);
    
    /**
     * Metodo para dar de alta la amortizacion de un pagare
     * @param amortizaciones
     * @return <ul>
     *          <li>1: se insertaron datos en la tabla amortizacion</li>
     *          <li>0: no se insertaron datos en la tabla amortizacion</li>
     *          </ul>
     * @throws com.sicap.clientes.exceptions.ClientesException
     */
    public int altaAmortPagare(List<AmortizacionPagareVO> amortizaciones, Connection cn) throws ClientesException 
    {
        myLogger.debug("Entra altaAmortPagare()");
        
        int res = 0;
        String query = "INSERT INTO d_amortizaciones_pagares(ap_numpagare, ap_numpago, ap_fecha_pago, ap_capital, ap_interes, ap_iva, ap_estatus) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        boolean conexionAbierta = true;
        try 
        {
            if(cn == null){
                cn = getConnection();
                conexionAbierta=false;
            }
            
            myLogger.debug("Ejecutando = " + query);
            myLogger.info("Numero Amortizaciones a guardar: "+amortizaciones.size());
            for(AmortizacionPagareVO ap : amortizaciones){
                PreparedStatement ps = cn.prepareStatement(query);

                ps.setInt(1, ap.getIdPagare());
                ps.setInt(2, ap.getNumPago());
                Date fechaPago=Convertidor.toSqlDate(ap.getFechaPago());
                ps.setDate(3, fechaPago);
                ps.setDouble(4, ap.getCapital());
                ps.setString(5, ap.getInteres());
                ps.setDouble(6, ap.getIva());
                ps.setDouble(7, ClientesConstants.ACTIVO);
                
                myLogger.debug("Amortizacion = " + ap.getNumPago());
                res = ps.executeUpdate();
                myLogger.debug("Resultado = " + res);
            }

        } 
        catch (SQLException sqle) 
        {
            myLogger.error("altaAmortPagare()", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } 
        catch (Exception e) 
        {
            myLogger.error("altaAmortPagare()", e);
            throw new ClientesException(e.getMessage());
        } 
        finally 
        {
            try 
            {
                if (cn != null && !conexionAbierta) 
                {
                    cn.close();
                    myLogger.debug("Se ha cerrado correctamente la conexion altaAmortPagare()");
                }
            } 
            catch (SQLException sqle) 
            {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return res;
    }
    //obtener las amortizaciones activas
    public ArrayList<AmortizacionPagareVO> getAmortActivas(int estatus, List<java.util.Date> fechas) throws ClientesException 
    {
        String query = "select am.ap_numpagare, p.pa_nombre, lc.lc_nombre, am.ap_numpago, am.ap_fecha_pago, am.ap_capital, am.ap_interes, am.ap_iva "
                + "from d_amortizaciones_pagares am inner join d_pagares p on am.ap_numpagare = p.pa_numpagare inner join d_lineas_credito lc on p.pa_numlinea = lc.lc_numlinea "
                + "where am.ap_estatus = ?"; 
        
        if(!fechas.isEmpty()){
            query = query+" and (";
        }
        for(int i = 0;  i < fechas.size() ; i++){
            query = query+" am.ap_fecha_pago = ?";
            if(i<(fechas.size()-1)){
                query = query+" or";
            }
        }
        if(!fechas.isEmpty()){
            query = query+")";
        }
        
        SimpleDateFormat formatFec = new SimpleDateFormat("yyyy-MM-dd");
        
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        ArrayList<AmortizacionPagareVO> datos = new ArrayList<AmortizacionPagareVO>();
        try 
        {
            con = getConnection();
            ps = con.prepareStatement(query);

            ps.setInt(1, estatus);
            
            int i = 2;
            
            for(java.util.Date fec : fechas){
                ps.setString(i, formatFec.format(fec));
                i++;
                myLogger.info("Fechas ingresadas "+i+": "+formatFec.format(fec));
            }

            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros ["+ estatus+"]");

            res = ps.executeQuery();
            while (res.next()) 
            {
               AmortizacionPagareVO ap = new AmortizacionPagareVO();
               ap.setIdPagare(res.getInt("ap_numpagare"));
               ap.setNombrePagare(res.getString("pa_nombre"));
               ap.setNombreLineaCredito(res.getString("lc_nombre"));
               ap.setNumPago(res.getInt("ap_numpago"));
               ap.setFechaPago(res.getDate("ap_fecha_pago"));
               ap.setCapital(res.getDouble("ap_capital"));
               ap.setInteres(res.getString("ap_interes"));
               ap.setIva(res.getDouble("ap_iva"));
               datos.add(ap);
            }

        } 
        catch (SQLException sqle) 
        {
            myLogger.error("getAmortActivas()", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } 
        catch (Exception e) 
        {
            myLogger.error("getAmortActivas()", e);
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
        return datos;
    }
    
    public List<Integer> getPagaresConAmortizaciones() throws ClientesException 
    {
        String query = "select distinct ap_numpagare "
                + "from d_amortizaciones_pagares"; 

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        List<Integer> idsPagares = new ArrayList<Integer>();
        try 
        {
            con = getConnection();
            ps = con.prepareStatement(query);
            
            myLogger.debug("Ejecutando = " + query);

            res = ps.executeQuery();
            
            while (res.next()) 
            {
               idsPagares.add(res.getInt("ap_numpagare"));
            }
            myLogger.debug("Pagares con amortizacion: "+idsPagares.size());
        } 
        catch (SQLException sqle) 
        {
            myLogger.error("getPagaresConAmortizaciones()", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } 
        catch (Exception e) 
        {
            myLogger.error("getPagaresConAmortizaciones()", e);
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
        return idsPagares;
    }
    
}
