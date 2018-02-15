package com.sicap.clientes.dao;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.ContabilidadCarteraVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class ContabilidadCarteraDAO extends DAOMaster{
    
    private Connection con = null;
    private PreparedStatement pstm = null;
    private ResultSet res = null;
    private String query = "";
    
    public ContabilidadCarteraVO getControlContable(int numCliente, int numSolicitud) throws ClientesException{
        
        ContabilidadCarteraVO contableVO = new ContabilidadCarteraVO();
        query = "SELECT SUM(cc_traspcapitalavigente) AS traspCapitalVigente,SUM(cc_interesvendiario) AS sumIntVencidoDiario,SUM(cc_ivainteresvendiario)AS sumIvaIntVencidoDiario,SUM(cc_traspcapitalavencido) AS traspCapitalVencido,"
                +"MAX(cc_numamortizacion) AS numPago FROM d_contabilidad_cartera WHERE cc_numcliente=? AND cc_numsolicitud=? AND cc_estatus=1;";
        try {
            con = getConnection();
            pstm = con.prepareStatement(query);
            pstm.setInt(1, numCliente);
            pstm.setInt(2, numSolicitud);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros[ "+numCliente+", "+numSolicitud+" ]");
            res = pstm.executeQuery();
            while (res.next()) {                
                contableVO.setTraspasoCapitalVigente(res.getDouble("traspCapitalVigente"));
                contableVO.setCancelacionInteresVencido(res.getDouble("sumIntVencidoDiario"));
                contableVO.setCancelacionIvaInteresVencido(res.getDouble("sumIvaIntVencidoDiario"));
                contableVO.setTraspasoCapitalVencido(res.getDouble("traspCapitalVencido"));
                contableVO.setNumPago(res.getInt("numPago"));
            }
        } catch (SQLException se) {
            Logger.debug("Excepcion en getControlContable : "+se.getMessage());
            se.printStackTrace();
            throw new ClientesException(se.getMessage());
        } catch (Exception e){
            Logger.debug("SQLException en getControlContable : "+e.getMessage());
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
        return contableVO;
    }
    
    public ContabilidadCarteraVO getDevengadoDiario(int numCliente, int numSolicitud, int numAmortizacion) throws ClientesException{
        
        ContabilidadCarteraVO contableVO = new ContabilidadCarteraVO();
        query = "SELECT COUNT(cc_numamortizacion) AS numero,MAX(cc_interesvigdiario) AS interes,MAX(cc_ivainteresvigdiario) AS iva FROM d_contabilidad_cartera WHERE cc_numcliente=? AND cc_numsolicitud=? AND cc_numamortizacion=?;";
        try {
            con = getConnection();
            pstm = con.prepareStatement(query);
            pstm.setInt(1, numCliente);
            pstm.setInt(2, numSolicitud);
            pstm.setInt(3, numAmortizacion);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros[ "+numCliente+", "+numSolicitud+", "+numAmortizacion+" ]");
            System.out.println("ps "+pstm);
            res = pstm.executeQuery();
            while (res.next()) {
                contableVO.setInteresDiario(res.getDouble("interes"));
                contableVO.setIvaInteresDiario(res.getDouble("iva"));
                contableVO.setNumPago(res.getInt("numero"));
            }
        } catch (SQLException se) {
            Logger.debug("Excepcion en getDevengadoDiario : "+se.getMessage());
            se.printStackTrace();
            throw new ClientesException(se.getMessage());
        } catch (Exception e){
            Logger.debug("SQLException en getDevengadoDiario : "+e.getMessage());
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
        return contableVO;
    }
    
    public ContabilidadCarteraVO getDevengadoDiarioAnterior(int numCredito, int numPago) throws ClientesException{
        
        ContabilidadCarteraVO contableVO = new ContabilidadCarteraVO();
        query = "SELECT ta_interes,ta_iva_interes FROM d_tabla_amortizacion WHERE ta_numcredito=? AND ta_numpago=?;";
        try {
            con = getCWConnection();
            pstm = con.prepareStatement(query);
            pstm.setInt(1, numCredito);
            pstm.setInt(2, numPago);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros[ "+numCredito+", "+numPago+" ]");
            res = pstm.executeQuery();
            while (res.next()) {
                contableVO.setInteresDiario(res.getDouble("ta_interes"));
                contableVO.setIvaInteresDiario(res.getDouble("ta_iva_interes"));
            }
        } catch (SQLException se) {
            Logger.debug("Excepcion en getDevengadoDiarioAnterior : "+se.getMessage());
            se.printStackTrace();
            throw new ClientesException(se.getMessage());
        } catch (Exception e){
            Logger.debug("SQLException en getDevengadoDiarioAnterior : "+e.getMessage());
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
        return contableVO;
    }
    
    public ContabilidadCarteraVO getIntereses(int numCliente, int numSolicitud) throws ClientesException{
        
        ContabilidadCarteraVO contableVO = new ContabilidadCarteraVO();
        query = "SELECT SUM(cc_montointeres) AS interes,SUM(cc_montoivainteres) AS ivaInteres,SUM(cc_interesvigdiario) AS interesDev,SUM(cc_ivainteresvigdiario) AS ivaInteresDev "
                +"FROM d_contabilidad_cartera WHERE cc_numcliente=? AND cc_numsolicitud=?;";
        try {
            con = getConnection();
            pstm = con.prepareStatement(query);
            pstm.setInt(1, numCliente);
            pstm.setInt(2, numSolicitud);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros[ "+numCliente+", "+numSolicitud+" ]");
            res = pstm.executeQuery();
            while (res.next()) {
                contableVO.setTotalInteres(res.getDouble("interes"));
                contableVO.setTotalIvaInteres(res.getDouble("ivaInteres"));
                contableVO.setInteresPagado(res.getDouble("interesDev"));
                contableVO.setIvaInteresPagado(res.getDouble("ivaInteresDev"));
            }
        } catch (SQLException se) {
            Logger.debug("Excepcion en getIntereses : "+se.getMessage());
            se.printStackTrace();
            throw new ClientesException(se.getMessage());
        } catch (Exception e){
            Logger.debug("SQLException en getIntereses : "+e.getMessage());
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
        return contableVO;
    }
    
    public void updateEstatusContable(int numCliente, int numSolicitud) throws ClientesException{
        
        ContabilidadCarteraVO contableVO = new ContabilidadCarteraVO();
        query = "UPDATE d_contabilidad_cartera SET cc_estatus=2 WHERE cc_numcliente=? AND cc_numsolicitud=? AND cc_estatus=1;";
        try {
            con = getConnection();
            pstm = con.prepareStatement(query);
            pstm.setInt(1, numCliente);
            pstm.setInt(2, numSolicitud);
            Logger.debug("Ejecutando = "+query);
            Logger.debug("Parametros[ "+numCliente+", "+numSolicitud+" ]");
            pstm.execute();
        } catch (SQLException se) {
            Logger.debug("Excepcion en updateEstatusContable : "+se.getMessage());
            se.printStackTrace();
            throw new ClientesException(se.getMessage());
        } catch (Exception e){
            Logger.debug("SQLException en updateEstatusContable : "+e.getMessage());
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
    
    public void insertRegistroContable(ContabilidadCarteraVO registroConta) throws ClientesException{
        
        ContabilidadCarteraVO contableVO = new ContabilidadCarteraVO();
        query = "INSERT INTO d_contabilidad_cartera VALUES(0,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        try {
            con = getConnection();
            pstm = con.prepareStatement(query);
            pstm.setInt(1, registroConta.getNumSucursal());
            pstm.setDate(2, Convertidor.toSqlDate(registroConta.getFechaMov()));
            pstm.setInt(3, registroConta.getNumCliente());
            pstm.setInt(4, registroConta.getNumSolicitud());
            pstm.setDouble(5, registroConta.getGarantia());
            pstm.setDouble(6, registroConta.getGarantiaSaldoFavor());
            pstm.setDouble(7, registroConta.getDesembolso());
            pstm.setDouble(8, registroConta.getTotalInteres());
            pstm.setDouble(9, registroConta.getInteresVigenteDiario());
            pstm.setDouble(10, registroConta.getIvaInteresVigenteDiario());
            pstm.setDouble(11, registroConta.getTraspasoCapitalVigente());
            pstm.setDouble(12, registroConta.getTraspasoInteresVigente());
            pstm.setDouble(13, registroConta.getTraspasoIvaInteresVigente());
            pstm.setDouble(14, registroConta.getInteresVencidoDiario());
            pstm.setDouble(15, registroConta.getIvaInteresVencidoDiario());
            pstm.setDouble(16, registroConta.getSaldoBucket());
            pstm.setDouble(17, registroConta.getMovimientoMultaVigente());
            pstm.setDouble(18, registroConta.getMovimientoIvaMultaVigente());
            pstm.setDouble(19, registroConta.getMovimientoInteresVigente());
            pstm.setDouble(20, registroConta.getMovimientoIvaInteresVigente());
            pstm.setDouble(21, registroConta.getMovimientoCapitalVigente());
            pstm.setDouble(22, registroConta.getMovimientoMultaVencido());
            pstm.setDouble(23, registroConta.getMovimientoIvaMultaVencido());
            pstm.setDouble(24, registroConta.getMovimientoInteresVencido());
            pstm.setDouble(25, registroConta.getMovimientoIvaInteresVencido());
            pstm.setDouble(26, registroConta.getMovimientoCapitalVencido());
            pstm.setDouble(27, registroConta.getTraspasoCapitalVencido());
            pstm.setDouble(28, registroConta.getTraspasoInteresVencido());
            pstm.setDouble(29, registroConta.getTraspasoIvaInteresVencido());
            pstm.setDouble(30, registroConta.getCancelacionInteresVencido());
            pstm.setDouble(31, registroConta.getCancelacionIvaInteresVencido());
            pstm.setDouble(32, 1);
            pstm.setDouble(33, registroConta.getIngresoBancos());
            pstm.setDouble(34, registroConta.getCondonacionMulta());
            pstm.setDouble(35, registroConta.getMulta());
            pstm.setDouble(36, registroConta.getIvaMulta());
            pstm.setDouble(37, registroConta.getNumPago());
            pstm.setDouble(38, registroConta.getTotalIvaInteres());
            pstm.setDouble(39, registroConta.getSaldoFavor());
            Logger.debug("Ejecutando = "+query);
            //Logger.debug("Parametros[ "+numCliente+", "+numSolicitud+" ]");
            pstm.execute();
        } catch (SQLException se) {
            Logger.debug("Excepcion en insertRegistroContable : "+se.getMessage());
            se.printStackTrace();
            throw new ClientesException(se.getMessage());
        } catch (Exception e){
            Logger.debug("SQLException en insertRegistroContable : "+e.getMessage());
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
    
    public boolean findRegistroContable(int numCliente, int numSolicitud, java.sql.Date fechaCierre) throws ClientesException{
        
        boolean existe = false;
        query = "SELECT cc_idcontabilidad FROM d_contabilidad_cartera WHERE cc_numcliente=? AND cc_numsolicitud=? AND cc_fechamov=?;";
        try {
            con = getConnection();
            pstm = con.prepareStatement(query);
            pstm.setInt(1, numCliente);
            pstm.setInt(2, numSolicitud);
            pstm.setDate(3, fechaCierre);
            Logger.debug("Ejecutando = "+query);
            //Logger.debug("Parametros[ "+numCliente+", "+numSolicitud+" ]");
            res = pstm.executeQuery();
            if (res.next())
                existe = true;
        } catch (SQLException se) {
            Logger.debug("Excepcion en findRegistroContable : "+se.getMessage());
            se.printStackTrace();
            throw new ClientesException(se.getMessage());
        } catch (Exception e){
            Logger.debug("SQLException en findRegistroContable : "+e.getMessage());
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
        return existe;
    }
}
