package com.sicap.clientes.dao;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.vo.PagoPagareVO;
import com.sicap.clientes.vo.ParametroVO;
import com.sicap.clientes.vo.SaldoFondeadorVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.naming.NamingException;
import org.apache.log4j.Logger;

/**
 *
 * @author avillanueva
 */
public class SaldoFondeadorDAO extends DAOMaster{
    
    private static Logger myLogger = Logger.getLogger(PaynetDAO.class);
    private Connection con = null;
    private PreparedStatement pstm = null;
    private ResultSet res = null;
    private String query = "";
    
    public double montoTentativoDispersion(int numFondeador) throws ClientesDBException, NamingException{
        
        double monto = 0.00;
        query = "SELECT SUM(ci_monto_con_comision) as monto FROM d_ciclos_grupales WHERE ci_fondeador=? AND ci_fecha_dispersion=? AND ci_numcredito_ibs=0 AND ci_estatus=10;";
        try {
            con = getConnection();
            pstm = con.prepareStatement(query);
            pstm.setInt(1, numFondeador);
            pstm.setDate(2, Convertidor.toSqlDate(new Date()));
            myLogger.debug("pstm "+pstm);
            res = pstm.executeQuery();
            while(res.next())
                monto = res.getDouble("monto");
        } catch (SQLException e) {
            myLogger.error("Erro en montoTentativoDispersion()", e);
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
        return monto;
    }
    
    public double montoDispersion(int numFondeador) throws ClientesDBException, NamingException{
        
        double monto = 0.00;
        query = "SELECT SUM(sm_monto) as monto FROM d_saldo_mov_fondeadores WHERE sm_tipomov='DES' AND sm_fondeador=? AND DATE(sm_fechahoramov)=?;";
        try {
            con = getCWConnection();
            pstm = con.prepareStatement(query);
            pstm.setInt(1, numFondeador);
            pstm.setDate(2, Convertidor.toSqlDate(new Date()));
            myLogger.debug("pstm "+pstm);
            res = pstm.executeQuery();
            while(res.next())
                monto = res.getDouble("monto");
        } catch (SQLException e) {
            myLogger.error("Erro en montoDispersion()", e);
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
        return monto;
    }
    
    public double montoCancelacion(int numFondeador) throws ClientesDBException, NamingException{
        
        double monto = 0.00;
        query = "SELECT SUM(sm_monto) as monto FROM d_saldo_mov_fondeadores WHERE sm_tipomov IN ('CAN_DES','DEV') AND sm_fondeador=? AND DATE(sm_fechahoramov)=?;";
        try {
            con = getCWConnection();
            pstm = con.prepareStatement(query);
            pstm.setInt(1, numFondeador);
            pstm.setDate(2, Convertidor.toSqlDate(new Date()));
            myLogger.debug("pstm "+pstm);
            res = pstm.executeQuery();
            while(res.next())
                monto = res.getDouble("monto");
        } catch (SQLException e) {
            myLogger.error("Erro en montoCancelacion()", e);
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
        return monto;
    }
    
    public double montoCobranza(int numFondeador) throws ClientesDBException, NamingException{
        
        double monto = 0.00;
        query = "SELECT SUM(tr_monto) as monto FROM d_transacciones WHERE tr_tipo_transaccion='REG' AND tr_codigo_rubro='EFE' AND tr_fondeador=? AND tr_fecha_proceso=?;";
        try {
            con = getCWConnection();
            pstm = con.prepareStatement(query);
            pstm.setInt(1, numFondeador);
            pstm.setDate(2, Convertidor.toSqlDate(new Date()));
            myLogger.debug("pstm "+pstm);
            res = pstm.executeQuery();
            while(res.next())
                monto = res.getDouble("monto");
        } catch (SQLException e) {
            myLogger.error("Erro en montoCobranza()", e);
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
        return monto;
    }
    
    public double montoSaldo(int tipoSaldo, int numFondeador) throws ClientesDBException, NamingException{
        
        double monto = -1;
        query = "SELECT sm_saldo as monto FROM d_saldo_mov_fondeadores WHERE DATE(sm_fechahoramov)=? AND sm_fondeador=? ORDER BY sm_numeromov DESC LIMIT 1;";
        try {
            Calendar calendar = Calendar.getInstance();
            if(tipoSaldo == 1)
                calendar.add(Calendar.DATE, -1);
            else if(tipoSaldo == 3)
                query = "SELECT sm_saldo as monto FROM d_saldo_mov_fondeadores WHERE DATE(sm_fechahoramov)<? AND sm_fondeador=? ORDER BY sm_numeromov DESC LIMIT 1;";
            Date fecha = calendar.getTime();
            con = getCWConnection();
            pstm = con.prepareStatement(query);
            pstm.setDate(1, Convertidor.toSqlDate(fecha));
            pstm.setInt(2, numFondeador);
            myLogger.debug("pstm "+pstm);
            res = pstm.executeQuery();
            while(res.next())
                monto = res.getDouble("monto");
        } catch (SQLException e) {
            myLogger.error("Erro en montoSaldo()", e);
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
        return monto;
    }
    
    public int insertMovimiento(SaldoFondeadorVO fondeoVO) throws ClientesDBException, NamingException{
        
        int inserto = 0;
        query = "INSERT INTO d_saldo_mov_fondeadores(sm_numero_transaccion, sm_numcliente, sm_numcredito, sm_tipomov, sm_monto, sm_origen, sm_fondeador, sm_saldo) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        try {
            con = getCWConnection();
            pstm = con.prepareStatement(query);
            pstm.setInt(1, fondeoVO.getNumTransaccion());
            pstm.setInt(2, fondeoVO.getNumCliente());
            pstm.setInt(3, fondeoVO.getNumCredito());
            pstm.setString(4, fondeoVO.getTipoMov());
            pstm.setDouble(5, fondeoVO.getMonto());
            pstm.setInt(6, fondeoVO.getOrigen());
            pstm.setInt(7, fondeoVO.getNumFondeador());
            pstm.setDouble(8, fondeoVO.getSaldoFinal());
            myLogger.debug("pstm "+pstm);
            inserto = pstm.executeUpdate();
        } catch (SQLException e) {
            myLogger.error("Erro en insertMovimiento()", e);
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
        return inserto;
    }
       public int insertMovimiento(SaldoFondeadorVO fondeoVO,Connection clieCon) throws ClientesDBException, NamingException{
        
        int inserto = 0;
        query = "INSERT INTO cartera_cec.d_saldo_mov_fondeadores(sm_numero_transaccion, sm_numcliente, sm_numcredito, sm_tipomov, sm_monto, sm_origen, sm_fondeador, sm_saldo) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
        try {            
            pstm = clieCon.prepareStatement(query);
            pstm.setInt(1, fondeoVO.getNumTransaccion());
            pstm.setInt(2, fondeoVO.getNumCliente());
            pstm.setInt(3, fondeoVO.getNumCredito());
            pstm.setString(4, fondeoVO.getTipoMov());
            pstm.setDouble(5, fondeoVO.getMonto());
            pstm.setInt(6, fondeoVO.getOrigen());
            pstm.setInt(7, fondeoVO.getNumFondeador());
            pstm.setDouble(8, fondeoVO.getSaldoFinal());
            myLogger.debug("Executando: "+ pstm);
            inserto = pstm.executeUpdate();
        } catch (SQLException e) {
            myLogger.error("Error en insertMovimiento(): ", e);
        }
        return inserto;
    }
    public ArrayList<SaldoFondeadorVO> getListaCobranza(int numFondeador) throws ClientesDBException, NamingException{
        
        ArrayList<SaldoFondeadorVO> arrLista = new ArrayList<SaldoFondeadorVO>();
        query = "SELECT sm_fechahoramov,sm_monto FROM d_saldo_mov_fondeadores WHERE sm_fondeador=? AND sm_tipomov='COB' ORDER BY sm_fechahoramov DESC LIMIT 10;";
        try {
            con = getCWConnection();
            pstm = con.prepareStatement(query);
            pstm.setInt(1, numFondeador);
            myLogger.debug("pstm "+pstm);
            res = pstm.executeQuery();
            while(res.next())
                arrLista.add(new SaldoFondeadorVO(res.getDouble("sm_monto"), res.getDate("sm_fechahoramov")));
        } catch (SQLException e) {
            myLogger.error("Error en getCobranza()", e);
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
        return arrLista;
    }
     /**
     * Obtiene el saldo del fondeador de la tabla c_parametros_fondeadores
     *
     * @param idFondeador 
     * @return Saldo Fondeador
     * @throws ClientesException
     */
    public String getSaldoFondeadorParam(int idFondeador) throws ClientesException {
        String saldoFondeador = "";
        PagoPagareVO pago = null;
        Connection cn = null;
        String query = "SELECT pf.pf_valor FROM c_parametros_fondeadores pf, c_fondeadores cf WHERE pf.pf_cve = 'SALDO_FONDEADOR'  AND cf.fo_numfondeador = pf.pf_fondeador AND cf.fo_numfondeador = ?";
        try 
        {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idFondeador);
            myLogger.debug("Ejecutando = " + ps);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) 
            {
                String saldoParam = rs.getString("pf.pf_valor");
                myLogger.debug("Saldo Fondeador: " + saldoParam);
                saldoFondeador = saldoParam;
            }
        } catch (SQLException sqle) {
            myLogger.error("SQLException en getSaldoFondeadorParam(): ", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("Excepcion en getSaldoFondeadorParam(): ", e);
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
        return saldoFondeador;
    }
    //metodo actualizar Saldo Fondeador cuando se aplica pago a pagare
    public  int actualizaSaldoFondeadorPagare(SaldoFondeadorVO saldoFondeadorVo, double saldoFondeadorParam) throws ClientesDBException, SQLException, NamingException {
        int respuesta = 0;
        Connection cn = null;
        SaldoFondeadorDAO saldoFondDAO = new SaldoFondeadorDAO();
        NumberFormat num = new DecimalFormat("########0.##");

        try 
        {
            cn = getConnection();
            cn.setAutoCommit(false);
            synchronized (this) 
            {
                double pagoCapital = saldoFondeadorVo.getMonto();
                myLogger.debug("Saldo inicial: " + pagoCapital);
                if (saldoFondeadorVo.tipoMov.equals(ClientesConstants.PAGO_FONDEADOR)) 
                {
                    if (saldoFondeadorParam > saldoFondeadorVo.getMonto()) 
                    {
                        myLogger.debug("Entra a disminucion de Saldo Fondeador");
                        saldoFondeadorVo.saldoFinal = saldoFondeadorParam - saldoFondeadorVo.monto;
                        myLogger.debug("Saldo Actual Fondeador: " + num.format(saldoFondeadorVo.saldoFinal));
                        CatalogoHelper.updateParametro("SALDO_FONDEADOR_" + saldoFondeadorVo.numFondeador, num.format(saldoFondeadorVo.saldoFinal), cn);
                        saldoFondDAO.insertMovimiento(saldoFondeadorVo, cn);
                        respuesta = 1;
                    } 
                    else if (saldoFondeadorVo.numFondeador == ClientesConstants.ID_FONDEADOR_CREDITO_REAL) 
                    {
                        saldoFondeadorVo.saldoFinal = 0;
                        saldoFondDAO.insertMovimiento(saldoFondeadorVo, cn);
                        respuesta = 1;
                    } 
                    else 
                    { //saldo insuficiente
                        myLogger.debug("Saldo de fondeador insufiente");
                        respuesta = 2;
                    }
                } 
            }
            cn.commit();
        } 
        catch (Exception e) 
        {
            myLogger.info("Entra al catch");
            myLogger.error("actualizaSaldoFondeadorPagare: ", e);
            try 
            {
                cn.rollback();
            } 
            catch (Exception ce) 
            {
                myLogger.info("Error al realizar el Commit");
                throw new ClientesDBException(ce.getMessage());
            }
            throw new ClientesDBException(e.getMessage());
        } 
        finally 
        {
            myLogger.info("Enviando respuesta");
            try 
            {
                cn.close();
            } 
            catch (Exception ce) 
            {
                myLogger.info("Error al realizar el Commit");
                throw new ClientesDBException(ce.getMessage());
            }
        }
        return respuesta;
    }
}

