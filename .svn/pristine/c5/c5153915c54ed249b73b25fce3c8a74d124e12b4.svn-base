
package com.sicap.clientes.dao;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FormatUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.LineaCreditoVO;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

/**
 * Clase para la administracion de lineas de credito
 * @author nmejia
 */
public class LineaCreditoDAO extends DAOMaster 
{
    private static final Logger myLogger = Logger.getLogger(LineaCreditoDAO.class);
    
    /**
     * Metodo para dar de alta una linea de credito
     * @param lineaCredito
     * @return <ul>
     *          <li>1: se insertaron datos en la tabla lineaCredito</li>
     *          <li>0: no se insertaron datos en la tabla lineaCredito</li>
     *          </ul>
     */
    public int altaLineaCredito(LineaCreditoVO lineaCredito, Connection cn, boolean cerrarConexion) throws ClientesException 
    {
        myLogger.debug("Entra altaLineaCredito()");
         
        int res = 0;
        String query = "INSERT INTO d_lineas_credito(lc_numfondeador, lc_nombre, lc_monto, lc_fecha_inicio, lc_fecha_fin, lc_tasa, lc_pre_selec_cartera, lc_estatus)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try 
        {
            cn = getConnection();
            PreparedStatement ps = null;
            if (cn != null) 
            {
                ps = cn.prepareStatement(query);
            } 
            ps.setInt(1, lineaCredito.getIdFondeador());
            ps.setString(2, lineaCredito.getNombreLineaCredito());
            ps.setInt(3, lineaCredito.getMontoLineaCredito());
            Date fechaInicio=Convertidor.toSqlDate(lineaCredito.getFechaVigenciaInicio());
            ps.setDate(4, fechaInicio);
            Date fechaFin=Convertidor.toSqlDate(lineaCredito.getFechaVigenciaFin());
            ps.setDate(5, fechaFin);
            
            ps.setString(6, lineaCredito.getTasa());
            ps.setInt(7, lineaCredito.getPreSeleccionCartera());
            ps.setInt(8, lineaCredito.getEstatus());
            myLogger.debug("****Obteniendo Datos***");
            myLogger.debug("Nombre Fondeador: "+lineaCredito.getIdFondeador());
            myLogger.debug("Nombre Linea Credito: "+lineaCredito.getNombreLineaCredito());
            myLogger.debug("Monto Linea Credito: "+lineaCredito.getMontoLineaCredito());
            myLogger.debug("Fecha Inicio: "+lineaCredito.getFechaVigenciaInicio());
            myLogger.debug("Fecha Fin: "+lineaCredito.getFechaVigenciaFin());
            myLogger.debug("Tasa: "+lineaCredito.getTasa());
            myLogger.debug("PreSeleccionCartera: "+lineaCredito.getPreSeleccionCartera());
            myLogger.debug("Estatus: "+lineaCredito.getEstatus());
           
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Linea Credito = " + lineaCredito.getNombreLineaCredito());
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } 
        catch (SQLException sqle) 
        {
            myLogger.error("altaLineaCredito()", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) 
        {
            myLogger.error("altaLineaCredito()", e);
            throw new ClientesException(e.getMessage());
        } 
        finally 
        {
            try 
            {
                if (cn != null && cerrarConexion) 
                {
                    cn.close();
                    myLogger.debug("Se ha cerrado correctamente la conexion altaLineaCredito()");
                }
            } 
            catch (SQLException sqle) 
            {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return res;
    }
    
    public int fondeadorTieneParametroSaldo(int idFondeador) throws ClientesException 
    {
        String query = "select count(*) as saldoFondeador from c_parametros_fondeadores where pf_fondeador = ? and pf_cve='SALDO_FONDEADOR'";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        int tieneParametro = 0;
        try 
        {
            con = getConnection();
            ps = con.prepareStatement(query);

            ps.setInt(1, idFondeador);

            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros ["+ idFondeador+"]");

            res = ps.executeQuery();
            while (res.next()) 
            {
               tieneParametro = res.getInt("saldoFondeador");
            }

        } 
        catch (SQLException sqle) 
        {
            myLogger.error("fondeadorTieneParametroSaldo()", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } 
        catch (Exception e) 
        {
            myLogger.error("fondeadorTieneParametroSaldo()", e);
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
        return tieneParametro;
    }
    
    public int altaParamSaldoFondeador(int idFondeador, int monto, Connection cn, boolean cerrarConexion) throws ClientesException 
    {
        myLogger.debug("Entra altaParamSaldoFondeador()");
        int res = 0;
        String query = "INSERT INTO c_parametros_fondeadores(pf_cve, pf_valor, pf_fondeador)"
                + "VALUES (?, ?, ?)";
        try 
        {
            cn = getConnection();
            PreparedStatement ps = null;
            if (cn != null) 
            {
                ps = cn.prepareStatement(query);
            } 
            ps.setString(1, "SALDO_FONDEADOR");
            ps.setInt(2, monto);
            ps.setInt(3, idFondeador);

            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } 
        catch (SQLException sqle) 
        {
            myLogger.error("altaParamSaldoFondeador()", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) 
        {
            myLogger.error("altaParamSaldoFondeador()", e);
            throw new ClientesException(e.getMessage());
        } 
        finally 
        {
            try 
            {
                if (cn != null && cerrarConexion) 
                {
                    cn.close();
                    myLogger.debug("Se ha cerrado correctamente la conexion altaParamSaldoFondeador()");
                }
            } 
            catch (SQLException sqle) 
            {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return res;
    }
    
    //obtener las lineas de credito activas
    public ArrayList<LineaCreditoVO> getNombreLineasCredito(int estatus) throws ClientesException 
    {
        String query = "SELECT lc.lc_numlinea, lc.lc_nombre FROM d_lineas_credito lc WHERE lc.lc_estatus=?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        ArrayList<LineaCreditoVO> lineasCredito = new ArrayList<LineaCreditoVO>();
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
                lineasCredito.add(new LineaCreditoVO(res.getInt("lc.lc_numlinea"), res.getString("lc.lc_nombre")));
            }

        } 
        catch (SQLException sqle) 
        {
            myLogger.error("getNombreLineasCredito", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } 
        catch (Exception e) 
        {
            myLogger.error("getNombreLineasCredito", e);
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
        return lineasCredito;
    }
    //obtenemos nombre de la linea de credito para msj correoElectronico
    public String getNombreLineaCredito(int numPago) throws ClientesDBException, ClientesException
    {
        String query = "select lc.lc_nombre from d_lineas_credito lc, d_amortizaciones_pagares am, d_pagares pa where pa.pa_numpagare = am.ap_numpagare and lc.lc_numlinea = pa.pa_numlinea and am.ap_numpago = ?;";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        String nombreLinea = null;
        try 
        {
            con = getConnection();
            ps = con.prepareStatement(query);

            ps.setInt(1, numPago);

            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros ["+ numPago+"]");

            res = ps.executeQuery();
            while (res.next()) 
            {
               nombreLinea = res.getString("lc.lc_nombre");
            }

        } 
        catch (SQLException sqle) 
        {
            myLogger.error("getNombreLineaCredito()", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } 
        catch (Exception e) 
        {
            myLogger.error("getNombreLineaCredito()", e);
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
        return nombreLinea;
    }
    public LineaCreditoVO obtenerSaldoFechVigencia(int idLineaCredito) throws ClientesDBException, ClientesException
    {
        String query = "SELECT pf.pf_valor, lc.lc_fecha_fin, lc.lc_numfondeador FROM c_parametros_fondeadores pf, d_lineas_credito lc, d_pagares pa WHERE pf.pf_cve = 'SALDO_FONDEADOR' AND pf.pf_valor = 0 AND pf.pf_fondeador = lc.lc_numfondeador AND pa.pa_numlinea =?;";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        LineaCreditoVO lineaCredito = null;
        try 
        {
            con = getConnection();
            ps = con.prepareStatement(query);

            ps.setInt(1, idLineaCredito);

            myLogger.info("Ejecutando:" +ps.toString());

            res = ps.executeQuery();
            while (res.next()) 
            {
                lineaCredito = new LineaCreditoVO(res.getInt("pf.pf_valor"), res.getDate("lc.lc_fecha_fin"), res.getInt("lc.lc_numfondeador"));
            }

        } 
        catch (SQLException sqle) 
        {
            myLogger.error("desactivarLineaCredito()", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } 
        catch (Exception e) 
        {
            myLogger.error("desactivarLineaCredito()", e);
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
        return lineaCredito;
    }
    public int desactivarLineaCredito(int numLineaCred) throws ClientesException 
    {

        String query = "UPDATE d_lineas_credito SET d_lineas_credito.lc_estatus = ? WHERE d_lineas_credito.lc_numlinea = ?";

        Connection con = null;
        int res = 0;
        try 
        {
            PreparedStatement ps = null;
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, ClientesConstants.LC_INACTIVA);
            ps.setInt(2, numLineaCred);

            myLogger.info("Ejecutando = " +ps.toString());
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } 
        catch (SQLException sqle) 
        {
            myLogger.error("desactivarLineaCredito()", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } 
        catch (Exception e) 
        {
            myLogger.error("desactivarLineaCredito()", e);
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
        return res;
    }
    //obtenemos fecha vigencia de linea credito
    public LineaCreditoVO getFechaVigenciaMontoLineaCred(int numLineaCredito) throws ClientesDBException, ClientesException
    {
        String query = "select lc.lc_fecha_fin, lc.lc_monto from d_lineas_credito lc where lc.lc_numlinea = ?;";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        LineaCreditoVO lineaCredito = null;
        try 
        {
            con = getConnection();
            ps = con.prepareStatement(query);

            ps.setInt(1, numLineaCredito);

            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros ["+ numLineaCredito+"]");

            res = ps.executeQuery();
            while (res.next()) 
            {
               lineaCredito = new LineaCreditoVO(res.getDate("lc.lc_fecha_fin"), res.getInt("lc.lc_monto"));
            }

        } 
        catch (SQLException sqle) 
        {
            myLogger.error("getFechaVigencia()", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } 
        catch (Exception e) 
        {
            myLogger.error("getFechaVigencia()", e);
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
        return lineaCredito;
    }
    //obtenemos la suma de pagares existentes asociados a una linea de credito
    public int getSumPagaresAsociadosLineaCred(int numLineaCred) throws ClientesDBException, ClientesException
    {
        String query = "select sum(pa_monto) as montoTotal from d_pagares where pa_numlinea = ?;";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        int resultado = 0;
        try 
        {
            con = getConnection();
            ps = con.prepareStatement(query);

            ps.setInt(1, numLineaCred);

            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros ["+ numLineaCred+"]");

            res = ps.executeQuery();
            while (res.next()) 
            {
               resultado = res.getInt("montoTotal");
            }

        } 
        catch (SQLException sqle) 
        {
            myLogger.error("getNombreLineaCredito()", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } 
        catch (Exception e) 
        {
            myLogger.error("getNombreLineaCredito()", e);
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
        return resultado;
    }
    
    public LineaCreditoVO obtenerLineaCredito(int idLineaCredito) throws ClientesDBException, ClientesException
    {
        String query = "select lc.lc_nombre, lc.lc_tasa, lc.lc_numlinea, lc.lc_numfondeador, lc.lc_monto, lc.lc_fecha_inicio, lc.lc_fecha_fin, lc.lc_estatus, lc.lc_pre_selec_cartera"
                + " from d_lineas_credito lc where lc.lc_numlinea = ?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        LineaCreditoVO lcVO = new LineaCreditoVO();
        try 
        {
            con = getConnection();
            ps = con.prepareStatement(query);

            ps.setInt(1, idLineaCredito);

            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros ["+ idLineaCredito+"]");

            res = ps.executeQuery();
            while (res.next()) 
            {
               lcVO.setIdLineaCredito(res.getInt("lc.lc_numlinea"));
               lcVO.setIdFondeador(res.getInt("lc.lc_numfondeador"));
               lcVO.setTasa(res.getString("lc.lc_tasa"));
               lcVO.setNombreLineaCredito(res.getString("lc.lc_nombre"));
               lcVO.setMontoLineaCredito(res.getInt("lc.lc_monto"));
               lcVO.setFechaVigenciaInicio(res.getDate("lc.lc_fecha_inicio"));
               lcVO.setFechaVigenciaFin(res.getDate("lc.lc_fecha_fin"));
               lcVO.setEstatus(res.getInt("lc.lc_estatus"));
               lcVO.setPreSeleccionCartera(res.getInt("lc.lc_pre_selec_cartera"));
            }

        } 
        catch (SQLException sqle) 
        {
            myLogger.error("getNombreLineaCredito()", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } 
        catch (Exception e) 
        {
            myLogger.error("getNombreLineaCredito()", e);
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
        return lcVO;
    }
    
    public List<Integer> obtenerLineasCreditoConPagaresCompletos() throws ClientesDBException, ClientesException
    {
        String query = "select lc.lc_numlinea, lc.lc_monto, p.sumPagares from  d_lineas_credito lc " +
            "left join (select pa.pa_numlinea, sum(pa.pa_monto) as sumPagares from d_pagares pa group by pa.pa_numlinea) p on p.pa_numlinea=lc.lc_numlinea " +
            "where lc.lc_monto=p.sumPagares";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        List<Integer> lineasSinLiquidar = new ArrayList<Integer>();
        try 
        {
            con = getConnection();
            ps = con.prepareStatement(query);

            myLogger.debug("Ejecutando = " + query);

            res = ps.executeQuery();
            while (res.next()) 
            {
              lineasSinLiquidar.add(res.getInt("lc.lc_numlinea"));
            }

        } 
        catch (SQLException sqle) 
        {
            myLogger.error("obtenerLineasCreditoSinLiquidar()", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } 
        catch (Exception e) 
        {
            myLogger.error("obtenerLineasCreditoSinLiquidar()", e);
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
        return lineasSinLiquidar;
    }
}
