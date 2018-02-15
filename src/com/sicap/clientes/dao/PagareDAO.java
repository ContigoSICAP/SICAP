
package com.sicap.clientes.dao;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.vo.PagareVO;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.naming.NamingException;
import org.apache.log4j.Logger;

/**
    * Clase para la administracion de pagares
    * @author nmejia
    */
public class PagareDAO extends DAOMaster
{
    private static final Logger myLogger = Logger.getLogger(PagareDAO.class);
    
    
    /**
     * Metodo para dar de alta un pagare asociado a una linea de credito
     * @param datosPagare
     * @return <ul>
     *          <li>1: se insertaron datos en la tabla pagares</li>
     *          <li>0: no se insertaron datos en la tabla pagares</li>
     *          </ul>
     * @throws com.sicap.clientes.exceptions.ClientesException
     */
    public int altaPagare(PagareVO datosPagare) throws ClientesException 
    {
        myLogger.debug("Entra altaPagare()");
        Connection cn = null;
        int res = 0;
        String query = "INSERT INTO d_pagares(pa_numlinea, pa_nombre, pa_monto, pa_fechainicio, pa_fechafin, pa_tasa, pa_estatus) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
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
            ps.setInt(1, datosPagare.getNumLineaCredito());
            ps.setString(2, datosPagare.getNombrePagare());
            ps.setInt(3, datosPagare.getMontoPagare());
            Date fechaInicio=Convertidor.toSqlDate(datosPagare.getFechaInicio());
            ps.setDate(4, fechaInicio);
            Date fechaFin=Convertidor.toSqlDate(datosPagare.getFechaFin());
            ps.setDate(5, fechaFin);
            ps.setString(6, datosPagare.getTasa());
            ps.setInt(7, ClientesConstants.ACTIVO);
           
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Pagaré = " + datosPagare.getNombrePagare());
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } 
        catch (SQLException sqle) 
        {
            myLogger.error("altaPagare()", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } 
        catch (Exception e) 
        {
            myLogger.error("altaPagare()", e);
            throw new ClientesException(e.getMessage());
        } 
        finally 
        {
            try 
            {
                if (cn != null) 
                {
                    cn.close();
                    myLogger.debug("Se ha cerrado correctamente la conexion altaPagare()");
                }
            } 
            catch (SQLException sqle) 
            {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return res;
    }
    //obtener pagares asociados a una linea de credito
    public ArrayList<PagareVO> getPagaresLineaCredito(int idLineaCredito, int estatus) throws ClientesException 
    {
        //String query = "select pa.pa_numpagare, pa.pa_nombre from d_pagares pa, d_lineas_credito lc, d_amortizaciones_pagares am where lc.lc_numlinea = pa.pa_numlinea and pa.pa_numpagare = am.ap_numpagare;";
        String query="select pa_numpagare, pa_nombre, lc_nombre, pa_monto, pa_fechainicio, pa_fechafin, pa_estatus"
                + " from d_pagares, d_lineas_credito "
                + " where pa_numlinea = lc_numlinea ";
        if(estatus>=0){
            query=query+" and lc_estatus = "+estatus;
        }
        if(idLineaCredito>0){
            query=query+" and lc_numlinea = "+idLineaCredito;
        }
                
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        ArrayList<PagareVO> pagares = new ArrayList<PagareVO>();
        try 
        {
            con = getConnection();
            ps = con.prepareStatement(query);

            myLogger.debug("Ejecutando = " + query);

            res = ps.executeQuery();
            while (res.next()) 
            {
                PagareVO pag = new PagareVO(res.getInt("pa_numpagare"), res.getString("pa_nombre"));
                pag.setNombreLineaCredito(res.getString("lc_nombre"));
                pag.setFechaInicio(res.getDate("pa_fechainicio"));
                pag.setFechaFin(res.getDate("pa_fechafin"));
                pag.setMonto(res.getDouble("pa_monto"));
                pag.setEstatus(res.getInt("pa_estatus"));
                pagares.add(pag);
            }

        } 
        catch (SQLException sqle) 
        {
            myLogger.error("getPagaresLineaCredito()", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } 
        catch (Exception e) 
        {
            myLogger.error("getPagaresLineaCredito()", e);
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
        return pagares;
    }
    //obtenemos nombre del pagare para msj correoElectronico
    public String getNombrePagare(int numPagare, int numPago) throws ClientesDBException, ClientesException
    {
        
        //String query = "select p.pa_nombre from d_amortizaciones_pagares am, d_pagares p where am.ap_numpagare = p.pa_numpagare and am.ap_numpago = ?;";
        String query = "select p.pa_nombre from d_amortizaciones_pagares am, d_pagares p, d_lineas_credito lc where lc.lc_numlinea=p.pa_numlinea and p.pa_numpagare=am.ap_numpagare and am.ap_numpagare = ? and am.ap_numpago = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        String nombrePagare = null;
        try 
        {
            con = getConnection();
            ps = con.prepareStatement(query);

            ps.setInt(1, numPagare);
            ps.setInt(2, numPago);

            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros ["+ numPagare+"]");

            res = ps.executeQuery();
            while (res.next()) 
            {
               nombrePagare = res.getString("p.pa_nombre");
            }

        } 
        catch (SQLException sqle) 
        {
            myLogger.error("getNombrePagare()", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } 
        catch (Exception e) 
        {
            myLogger.error("getNombrePagare()", e);
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
        return nombrePagare;
    }
    //obtenemos la tasa de interes
    public String getTasaInteres(int numPagare) throws ClientesDBException, ClientesException
    {
        
        String query = "select p.pa_tasa from d_amortizaciones_pagares am, d_pagares p, d_lineas_credito lc where lc.lc_numlinea = p.pa_numlinea and p.pa_numpagare=am.ap_numpagare and am.ap_numpagare =? limit 1;";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        String tasa = "";
        try 
        {
            con = getConnection();
            ps = con.prepareStatement(query);

            ps.setInt(1, numPagare);

            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros ["+ numPagare+"]");

            res = ps.executeQuery();
            while (res.next()) 
            {
               tasa = res.getString("p.pa_tasa");
            }

        } 
        catch (SQLException sqle) 
        {
            myLogger.error("getTasaPagare()", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } 
        catch (Exception e) 
        {
            myLogger.error("getTasaPagare()", e);
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
        return tasa;
    }
    /**
     * Este método obtiene los pagares activos para aplicar pago a pagare de fondeador
     * @return El arreglo de los pagares
     */
    public PagareVO[] getPagaresActivos() {
        PagareVO[] pagares = null;
        String query = "select p.pa_numpagare, p.pa_nombre, cf.fo_nombre, p.pa_monto, p.pa_fechafin from d_pagares p, d_lineas_credito lc, c_fondeadores cf where cf.fo_numfondeador =  lc.lc_numfondeador and lc.lc_numlinea = p.pa_numlinea and p.pa_estatus = 1;";
        Connection cn = null;
        ArrayList<PagareVO> arrayPagares = new ArrayList<PagareVO>();
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PagareVO pagare = new PagareVO();
                pagare.setNumPagare(rs.getInt("p.pa_numpagare"));
                pagare.setNombrePagare(rs.getString("p.pa_nombre"));
                pagare.setNombreFondeador(rs.getString("cf.fo_nombre"));
                pagare.setMontoPagare(rs.getInt("p.pa_monto"));
                pagare.setFechaFin(rs.getDate("p.pa_fechafin"));
                arrayPagares.add(pagare);
            }
            pagares = new PagareVO[arrayPagares.size()];
            for (int i = 0; i < pagares.length; i++) {
                pagares[i] = arrayPagares.get(i);
            }
        } catch (SQLException exc) {
            myLogger.error("getPagaresActivos", exc);
        } catch (NamingException exc) {
            myLogger.error("getPagaresActivos", exc);
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getPagaresActivos", exc);
            }
        }
        return pagares;
    }
    
    public PagareVO obtenerPagare(int idPagare) {
        PagareVO pagare = null;
        String query = "select p.pa_numpagare, p.pa_nombre, p.pa_monto, p.pa_fechafin, p.pa_fechainicio, p.pa_tasa, p.pa_totalPagar from d_pagares p where p.pa_numpagare = ?";
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idPagare);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                pagare = new PagareVO();
                pagare.setNumPagare(rs.getInt("p.pa_numpagare"));
                pagare.setNombrePagare(rs.getString("p.pa_nombre"));
                pagare.setMontoPagare(rs.getInt("p.pa_monto"));
                pagare.setFechaFin(rs.getDate("p.pa_fechafin"));
                pagare.setFechaInicio(rs.getDate("p.pa_fechainicio"));
                pagare.setTasa(rs.getString("p.pa_tasa"));
                pagare.setTotalPagar(rs.getDouble("pa_totalPagar"));
            }

        } catch (SQLException exc) {
            myLogger.error("obtenerPagare", exc);
        } catch (NamingException exc) {
            myLogger.error("obtenerPagare", exc);
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("obtenerPagare", exc);
            }
        }
        return pagare;
    }
    
    /**
     * Este método obtiene los pagares activos para aplicar pago a pagare de fondeador
     * ademas tengan un saldo mayor a cero
     * @return El arreglo de los pagares
     */
    public ArrayList<PagareVO> getPagaresActivosArr() {
        
        String query = "select cf.fo_numfondeador, p.pa_numpagare, p.pa_nombre, cf.fo_nombre, p.pa_monto, p.pa_fechafin from c_parametros_fondeadores pf, c_fondeadores cf, d_lineas_credito lc, d_pagares p where p.pa_numlinea = lc.lc_numlinea and cf.fo_numfondeador = lc.lc_numfondeador and lc_numfondeador = pf.pf_fondeador and cf.fo_numfondeador = pf.pf_fondeador and pf.pf_cve = 'SALDO_FONDEADOR' and pf.pf_valor IS NOT NULL  and pf.pf_valor > 0 and p.pa_estatus != 2";
        Connection cn = null;
        ArrayList<PagareVO> pagares = new ArrayList<PagareVO>();
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) 
            {
                pagares.add(new PagareVO(rs.getInt("cf.fo_numfondeador"),rs.getInt("p.pa_numpagare"), rs.getString("p.pa_nombre"),rs.getString("cf.fo_nombre"),rs.getInt("p.pa_monto"),rs.getDate("p.pa_fechafin")));
            }
        } catch (SQLException exc) {
            myLogger.error("getPagaresActivosArr", exc);
        } catch (NamingException exc) {
            myLogger.error("getPagaresActivosArr", exc);
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getPagaresActivosArr", exc);
            }
        }
        return pagares;
    }
    
    /**
     * Obtiene el idFondeador de acuerdo al registro seleccionado
     *
     * @param numPagare Numero de pago
     * @param nombreFondeador Numero de pagare
     * @return IdFondeador
     * @throws ClientesException
     */
    public int getIdFondeador(int numPagare, String nombreFondeador) throws ClientesException {
        int resultado = 0;
        Connection cn = null;
        String query = "select cf.fo_numfondeador from c_fondeadores cf, d_pagares p,d_lineas_credito lc where lc.lc_numlinea=p.pa_numlinea and p.pa_numpagare = ? and cf.fo_nombre = ?;";
        try {
            int param = 1;
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, numPagare);
            ps.setString(2, nombreFondeador);
            myLogger.debug("Ejecutando = " + ps);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                resultado = rs.getInt("cf.fo_numfondeador");
                myLogger.debug("NumFondeador: " + resultado);
            }
        } catch (SQLException sqle) {
            myLogger.error("SQLException en getIdFondeador(): ", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("Excepcion en getIdFondeador(): ", e);
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
    //metodo actualizar monto del pagare cuando es efectuado el pago
    public PagareVO actualizarMontoPagare(double monto, double pagoCapital, int numPagare) throws ClientesException 
    {
        Connection cn = null;
        double montoActual = monto - pagoCapital;
        String query = "UPDATE d_pagares SET pa_monto = ? WHERE pa_numpagare = ?;";
        PagareVO pagare = null;
        try {
            cn = getConnection();            
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setDouble(1, montoActual);
            ps.setInt(2, numPagare);
            myLogger.debug("Executando: "+ ps);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("actualizarMontoPagare", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("actualizarMontoPagare", e);
            throw new ClientesException(e.getMessage());
        } 
        return pagare;
    }

}
