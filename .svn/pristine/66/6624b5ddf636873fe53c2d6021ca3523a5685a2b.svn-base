package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.SolicitudVO;
import java.sql.Savepoint;
import org.apache.log4j.Logger;

public class CicloGrupalDAO extends DAOMaster {

    private static Logger myLogger = Logger.getLogger(CicloGrupalDAO.class);

    public CicloGrupalVO[] getCiclos(int idGrupo) throws ClientesException {

        String query = "SELECT A.*, B.IB_ESTATUS FROM D_CICLOS_GRUPALES A LEFT JOIN D_SALDOS B ON( IB_NUMCLIENTESICAP=CI_NUMGRUPO AND IB_NUMSOLICITUDSICAP=CI_NUMCICLO AND IB_PRODUCTO=3 AND IB_ESTATUS!=4) WHERE CI_NUMGRUPO = ? ORDER BY 2";
        ArrayList<CicloGrupalVO> array = new ArrayList<CicloGrupalVO>();
        CicloGrupalVO temporal = null;
        CicloGrupalVO elementos[] = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idGrupo);
            myLogger.debug("Ejecutando = " + query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new CicloGrupalVO();
                temporal.idGrupo = rs.getInt("CI_NUMGRUPO");
                temporal.idCiclo = rs.getInt("CI_NUMCICLO");
                temporal.idCreditoIBS = rs.getInt("CI_NUMCREDITO_IBS");
                temporal.idCuentaIBS = rs.getInt("CI_NUMCUENTA_IBS");
                temporal.estatus = rs.getInt("CI_ESTATUS");
                temporal.desembolsado = rs.getInt("CI_DESEMBOLSADO");
                temporal.idTipoCiclo = rs.getInt("CI_TIPO_CICLO");
                temporal.idDireccionReunion = rs.getInt("CI_NUMDIRECCION_REUNION");
                temporal.idDireccionAlterna = rs.getInt("CI_NUMDIRECCION_ALTERNA");
                temporal.diaReunion = rs.getInt("CI_DIA_REUNION");
                temporal.horaReunion = rs.getInt("CI_HORA_REUNION");
                temporal.asesor = rs.getInt("CI_EJECUTIVO");
                temporal.coordinador = rs.getInt("CI_COORDINADOR");
                temporal.multaRetraso = rs.getDouble("CI_MULTA_RETRASO");
                temporal.multaFalta = rs.getDouble("CI_MULTA_FALTA");
                temporal.fechaCaptura = rs.getTimestamp("CI_FECHA_CAPTURA");
                temporal.monto = rs.getDouble("CI_MONTO");
                temporal.tasa = rs.getInt("CI_TASA");
                temporal.tasaCalculada = rs.getInt("CI_TASA_CALCULADA");
                temporal.montoConComision = rs.getDouble("CI_MONTO_CON_COMISION");
                temporal.montoRefinanciado = rs.getDouble("CI_MONTO_REFINANCIADO");
                temporal.estatusT24 = rs.getInt("IB_ESTATUS");
                temporal.plazo = rs.getInt("ci_plazo");
                temporal.fechaDispersion = rs.getDate("ci_fecha_dispersion");
                temporal.bancoDispersion = rs.getInt("ci_banco_dispersion");
                temporal.aperturador = rs.getInt("ci_aperturador");
                temporal.fondeador = rs.getInt("ci_fondeador");
                temporal.estatusIC = rs.getInt("ci_estatusIC");
                temporal.estatusIC2 = rs.getInt("ci_estatusIC_2");
                temporal.aceptaAdicional = rs.getInt("ci_semadi"); 
                array.add(temporal);
                myLogger.debug("Ciclo encontrado : " + temporal.toString());
            }
            if (array.size() > 0) {
                elementos = new CicloGrupalVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (CicloGrupalVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getCiclos", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCiclos", e);
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
        return elementos;

    }

    public CicloGrupalVO getCiclo(int idGrupo) throws ClientesException {

        String query = "SELECT * FROM D_CICLOS_GRUPALES WHERE CI_NUMGRUPO = ? AND CI_ESTATUS = 1";
        CicloGrupalVO temporal = null;
        Connection cn = null;
        
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idGrupo);
            //ps.setInt(2, status);
            myLogger.debug("Ejecutando = " + query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                temporal = new CicloGrupalVO();
                temporal.idGrupo = rs.getInt("CI_NUMGRUPO");
                temporal.idCiclo = rs.getInt("CI_NUMCICLO");
                temporal.idCreditoIBS = rs.getInt("CI_NUMCREDITO_IBS");
                temporal.idCuentaIBS = rs.getInt("CI_NUMCUENTA_IBS");
                temporal.estatus = rs.getInt("CI_ESTATUS");
                temporal.desembolsado = rs.getInt("CI_DESEMBOLSADO");
                temporal.idTipoCiclo = rs.getInt("CI_TIPO_CICLO");
                temporal.idDireccionReunion = rs.getInt("CI_NUMDIRECCION_REUNION");
                temporal.idDireccionAlterna = rs.getInt("CI_NUMDIRECCION_ALTERNA");
                temporal.diaReunion = rs.getInt("CI_DIA_REUNION");
                temporal.horaReunion = rs.getInt("CI_HORA_REUNION");
                temporal.asesor = rs.getInt("CI_EJECUTIVO");
                temporal.coordinador = rs.getInt("CI_COORDINADOR");
                temporal.multaRetraso = rs.getDouble("CI_MULTA_RETRASO");
                temporal.multaFalta = rs.getDouble("CI_MULTA_FALTA");
                temporal.fechaCaptura = rs.getTimestamp("CI_FECHA_CAPTURA");
                temporal.tasa = rs.getInt("CI_TASA");
                temporal.tasaCalculada = rs.getInt("CI_TASA_CALCULADA");
                temporal.monto = rs.getDouble("CI_MONTO");
                temporal.montoConComision = rs.getDouble("CI_MONTO_CON_COMISION");
                temporal.montoRefinanciado = rs.getDouble("CI_MONTO_REFINANCIADO");
                temporal.estatusIC= rs.getInt("ci_estatusIC");
                temporal.estatusIC2= rs.getInt("ci_estatusIC_2");
                temporal.aceptaAdicional = rs.getInt("ci_semadi");
                myLogger.debug("Ciclo encontrado : " + temporal.toString());
            }
        } catch (SQLException sqle) {
            myLogger.error("getCiclo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCiclo", e);
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
        return temporal;

    }
    
        public ArrayList<CicloGrupalVO> getCicloInterCiclo(int idEquipo, Date fechaInicio, Date fechaFin, int idEstatus, int idSucursal) throws ClientesException {

        String query = "Select d_ciclos_grupales.*, gr_nombre, ta_fecha_pago, fo_nombre" +
                        " from d_ciclos_grupales, d_grupos, d_tabla_amortizacion, c_fondeadores" +
                        " where ci_numgrupo = gr_numgrupo" +
                        " and ci_numgrupo = ta_id_cliente" +
                        " and ci_numciclo = ta_id_solicitud" +
                        " and fo_numfondeador = ci_fondeador" +
                        " and ta_no_pago = 4" +
                        " and ta_tpo_amortizacion = 1" +
                        " and ci_estatusIC = ?" ;
                       
        ArrayList<CicloGrupalVO> arrEquipos = new ArrayList<CicloGrupalVO>();
        CicloGrupalVO temporal = null;
        Connection cn = null;
         int parametro=1;
        try {
            if (fechaInicio!=null&&fechaFin!=null){
                query += " and ta_fecha_pago between ? and ?";
            }
            if (idSucursal!=0){
                query += " and ci_coordinador = ?";
            }
            if (idEquipo!=0){
                query += " and ci_numgrupo = ? ";            
            }
            
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(parametro++, idEstatus);
            if (fechaInicio!=null&&fechaFin!=null){
                ps.setDate(parametro++, fechaInicio);
                ps.setDate(parametro++, fechaFin);
            }
            if (idSucursal!=0){
                ps.setInt(parametro++, idSucursal);
            }
            if (idEquipo!=0){
                ps.setInt(parametro++, idEquipo);            
            }
            
            //ps.setInt(2, status);
            myLogger.debug("Ejecutando = " + query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {                
                temporal = new CicloGrupalVO();
                temporal.idGrupo = rs.getInt("CI_NUMGRUPO");
                temporal.idCiclo = rs.getInt("CI_NUMCICLO");
                temporal.idCreditoIBS = rs.getInt("CI_NUMCREDITO_IBS");
                temporal.idCuentaIBS = rs.getInt("CI_NUMCUENTA_IBS");
                temporal.estatus = rs.getInt("CI_ESTATUS");
                temporal.desembolsado = rs.getInt("CI_DESEMBOLSADO");
                temporal.idTipoCiclo = rs.getInt("CI_TIPO_CICLO");
                temporal.idDireccionReunion = rs.getInt("CI_NUMDIRECCION_REUNION");
                temporal.idDireccionAlterna = rs.getInt("CI_NUMDIRECCION_ALTERNA");
                temporal.diaReunion = rs.getInt("CI_DIA_REUNION");
                temporal.horaReunion = rs.getInt("CI_HORA_REUNION");
                temporal.asesor = rs.getInt("CI_EJECUTIVO");
                temporal.coordinador = rs.getInt("CI_COORDINADOR");
                temporal.multaRetraso = rs.getDouble("CI_MULTA_RETRASO");
                temporal.multaFalta = rs.getDouble("CI_MULTA_FALTA");
                temporal.fechaCaptura = rs.getTimestamp("CI_FECHA_CAPTURA");
                temporal.tasa = rs.getInt("CI_TASA");
                temporal.tasaCalculada = rs.getInt("CI_TASA_CALCULADA");
                temporal.monto = rs.getDouble("CI_MONTO");
                temporal.montoConComision = rs.getDouble("CI_MONTO_CON_COMISION");
                temporal.montoRefinanciado = rs.getDouble("CI_MONTO_REFINANCIADO");
                temporal.estatusIC= rs.getInt("ci_estatusIC");
                temporal.estatusIC2= rs.getInt("ci_estatusIC_2");
                temporal.aceptaAdicional = rs.getInt("ci_semadi");
                temporal.nombreEquipo = rs.getString("gr_nombre");
                temporal.fechaDispersion = rs.getDate("ta_fecha_pago");
                temporal.fondeador = rs.getInt("ci_fondeador");
                arrEquipos.add(temporal);
                myLogger.debug("Ciclo encontrado : " + temporal.toString());
            }
        } catch (SQLException sqle) {
            myLogger.error("getCiclo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCiclo", e);
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
        return arrEquipos;

    }

    public CicloGrupalVO getCiclo(int idGrupo, int idCiclo) throws ClientesException {

        String query = "SELECT * FROM D_CICLOS_GRUPALES WHERE CI_NUMGRUPO = ? AND CI_NUMCICLO = ?";
        CicloGrupalVO temporal = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idGrupo);
            ps.setInt(2, idCiclo);
            myLogger.debug("Ejecutando = " + query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                temporal = new CicloGrupalVO();
                temporal.idGrupo = rs.getInt("CI_NUMGRUPO");
                temporal.idCiclo = rs.getInt("CI_NUMCICLO");
                temporal.idCreditoIBS = rs.getInt("CI_NUMCREDITO_IBS");
                temporal.idCuentaIBS = rs.getInt("CI_NUMCUENTA_IBS");
                temporal.estatus = rs.getInt("CI_ESTATUS");
                temporal.desembolsado = rs.getInt("CI_DESEMBOLSADO");
                temporal.idTipoCiclo = rs.getInt("CI_TIPO_CICLO");
                temporal.idDireccionReunion = rs.getInt("CI_NUMDIRECCION_REUNION");
                temporal.idDireccionAlterna = rs.getInt("CI_NUMDIRECCION_ALTERNA");
                temporal.diaReunion = rs.getInt("CI_DIA_REUNION");
                temporal.horaReunion = rs.getInt("CI_HORA_REUNION");
                temporal.asesor = rs.getInt("CI_EJECUTIVO");
                temporal.coordinador = rs.getInt("CI_COORDINADOR");
                temporal.multaRetraso = rs.getDouble("CI_MULTA_RETRASO");
                temporal.multaFalta = rs.getDouble("CI_MULTA_FALTA");
                temporal.fechaCaptura = rs.getTimestamp("CI_FECHA_CAPTURA");
                temporal.tasa = rs.getInt("CI_TASA");
                temporal.tasaCalculada = rs.getInt("CI_TASA_CALCULADA");
                temporal.monto = rs.getDouble("CI_MONTO");
                temporal.montoConComision = rs.getDouble("CI_MONTO_CON_COMISION");
                temporal.montoRefinanciado = rs.getDouble("CI_MONTO_REFINANCIADO");
                temporal.estatusAlertasPago = rs.getInt("CI_ESTATUS_REVISION_MONITOR");
                temporal.seguro = rs.getInt("ci_conseguro");
                temporal.plazo = rs.getInt("ci_plazo");
                temporal.fechaDispersion = rs.getDate("ci_fecha_dispersion");
                temporal.bancoDispersion = rs.getInt("ci_banco_dispersion");
                temporal.aperturador = rs.getInt("ci_aperturador");
                temporal.garantia = rs.getInt("ci_porgarantia");
                temporal.fondeador = rs.getInt("ci_fondeador");
                temporal.estatusIC = rs.getInt("ci_estatusIC");
                temporal.estatusIC2= rs.getInt("ci_estatusIC_2");
                temporal.seguroCompleto = rs.getInt("ci_consegurocompleto");
                temporal.aceptaAdicional = rs.getInt("ci_semadi");                myLogger.debug("Ciclo encontrado : " + temporal.toString());
            }
        } catch (SQLException sqle) {
            myLogger.error("getCiclo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCiclo", e);
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
        return temporal;

    }

    public CicloGrupalVO getCicloApertura(int idGrupo, int idCiclo) throws ClientesException {

        String query = "SELECT * FROM D_CICLOS_GRUPALES WHERE CI_NUMGRUPO = ? AND CI_NUMCICLO = ?";
        CicloGrupalVO ciclo = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idGrupo);
            ps.setInt(2, idCiclo);
            myLogger.debug("Ejecutando = " + query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ciclo = new CicloGrupalVO();
                ciclo.idGrupo = rs.getInt("CI_NUMGRUPO");
                ciclo.idCiclo = rs.getInt("CI_NUMCICLO");
                ciclo.idCreditoIBS = rs.getInt("CI_NUMCREDITO_IBS");
                ciclo.idCuentaIBS = rs.getInt("CI_NUMCUENTA_IBS");
                ciclo.estatus = rs.getInt("CI_ESTATUS");
                ciclo.desembolsado = rs.getInt("CI_DESEMBOLSADO");
                ciclo.idTipoCiclo = rs.getInt("CI_TIPO_CICLO");
                ciclo.idDireccionReunion = rs.getInt("CI_NUMDIRECCION_REUNION");
                ciclo.idDireccionAlterna = rs.getInt("CI_NUMDIRECCION_ALTERNA");
                ciclo.diaReunion = rs.getInt("CI_DIA_REUNION");
                ciclo.horaReunion = rs.getInt("CI_HORA_REUNION");
                ciclo.asesor = rs.getInt("CI_EJECUTIVO");
                ciclo.coordinador = rs.getInt("CI_COORDINADOR");
                ciclo.multaRetraso = rs.getDouble("CI_MULTA_RETRASO");
                ciclo.multaFalta = rs.getDouble("CI_MULTA_FALTA");
                ciclo.fechaCaptura = rs.getTimestamp("CI_FECHA_CAPTURA");
                ciclo.tasa = rs.getInt("CI_TASA");
                ciclo.tasaCalculada = rs.getInt("CI_TASA_CALCULADA");
                ciclo.monto = rs.getDouble("CI_MONTO");
                ciclo.montoConComision = rs.getDouble("CI_MONTO_CON_COMISION");
                ciclo.montoRefinanciado = rs.getDouble("CI_MONTO_REFINANCIADO");
                ciclo.estatusAlertasPago = rs.getInt("CI_ESTATUS_REVISION_MONITOR");
                ciclo.seguro = rs.getInt("ci_conseguro");
                ciclo.plazo = rs.getInt("ci_plazo");
                ciclo.fechaDispersion = rs.getDate("ci_fecha_dispersion");
                ciclo.bancoDispersion = rs.getInt("ci_banco_dispersion");
                ciclo.aperturador = rs.getInt("ci_aperturador");
                ciclo.garantia = rs.getInt("ci_porgarantia");
                ciclo.estatusIC = rs.getInt("ci_estatusIC");
                ciclo.estatusIC2= rs.getInt("ci_estatusIC_2");
                ciclo.aceptaAdicional = rs.getInt("ci_semadi");
                myLogger.debug("Ciclo encontrado : " + ciclo.toString());
            }
        } catch (SQLException sqle) {
            myLogger.error("getCicloApertura", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCicloApertura", e);
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
        return ciclo;

    }

    public CicloGrupalVO getCicloEliminacion(int idGrupo, int idCiclo) throws ClientesException {

        String query = "SELECT d_ciclos_grupales.*, gr_nombre, su_nombre FROM d_ciclos_grupales LEFT JOIN d_grupos "
                + "ON(ci_numgrupo=gr_numgrupo) LEFT JOIN c_sucursales "
                + "ON(gr_numsucursal=su_numsucursal) "
                + "WHERE ci_numgrupo=? AND ci_numciclo=?;";
        CicloGrupalVO ciclo = null;
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, idGrupo);
            ps.setInt(2, idCiclo);
            myLogger.debug("Ejecutando: " + ps.toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                ciclo = new CicloGrupalVO();
                ciclo.nombreEquipo = rs.getString("gr_nombre");
                ciclo.nombreSucursal = rs.getString("su_nombre");
                ciclo.idGrupo = rs.getInt("CI_NUMGRUPO");
                ciclo.idCiclo = rs.getInt("CI_NUMCICLO");
                ciclo.idCreditoIBS = rs.getInt("CI_NUMCREDITO_IBS");
                ciclo.idCuentaIBS = rs.getInt("CI_NUMCUENTA_IBS");
                ciclo.estatus = rs.getInt("CI_ESTATUS");
                ciclo.desembolsado = rs.getInt("CI_DESEMBOLSADO");
                ciclo.idTipoCiclo = rs.getInt("CI_TIPO_CICLO");
                ciclo.idDireccionReunion = rs.getInt("CI_NUMDIRECCION_REUNION");
                ciclo.idDireccionAlterna = rs.getInt("CI_NUMDIRECCION_ALTERNA");
                ciclo.diaReunion = rs.getInt("CI_DIA_REUNION");
                ciclo.horaReunion = rs.getInt("CI_HORA_REUNION");
                ciclo.asesor = rs.getInt("CI_EJECUTIVO");
                ciclo.coordinador = rs.getInt("CI_COORDINADOR");
                ciclo.multaRetraso = rs.getDouble("CI_MULTA_RETRASO");
                ciclo.multaFalta = rs.getDouble("CI_MULTA_FALTA");
                ciclo.fechaCaptura = rs.getTimestamp("CI_FECHA_CAPTURA");
                ciclo.tasa = rs.getInt("CI_TASA");
                ciclo.tasaCalculada = rs.getInt("CI_TASA_CALCULADA");
                ciclo.monto = rs.getDouble("CI_MONTO");
                ciclo.montoConComision = rs.getDouble("CI_MONTO_CON_COMISION");
                ciclo.montoRefinanciado = rs.getDouble("CI_MONTO_REFINANCIADO");
                ciclo.estatusAlertasPago = rs.getInt("CI_ESTATUS_REVISION_MONITOR");
                ciclo.seguro = rs.getInt("ci_conseguro");
                ciclo.plazo = rs.getInt("ci_plazo");
                ciclo.fechaDispersion = rs.getDate("ci_fecha_dispersion");
                ciclo.bancoDispersion = rs.getInt("ci_banco_dispersion");
                ciclo.aperturador = rs.getInt("ci_aperturador");
                ciclo.garantia = rs.getInt("ci_porgarantia");
                ciclo.fondeador = rs.getInt("ci_fondeador");
                ciclo.estatusIC = rs.getInt("ci_estatusIC");
                ciclo.estatusIC2= rs.getInt("ci_estatusIC_2");
                ciclo.aceptaAdicional = rs.getInt("ci_semadi");
                //Logger.debug("Ciclo encontrado : " + ciclo.toString());
            }
        } catch (SQLException sqle) {
            myLogger.error("SQLException", sqle);
            sqle.printStackTrace();
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("Exception", e);
            e.printStackTrace();
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                myLogger.error("SQLException", e);
                throw new ClientesDBException(e.getMessage());
            }
        }
        return ciclo;
    }

    public int addCicloGrupal(Connection conn, CicloGrupalVO ciclo) throws ClientesException {

        String query = "INSERT INTO D_CICLOS_GRUPALES (CI_NUMGRUPO, CI_NUMCICLO, CI_TIPO_CICLO, CI_ESTATUS, CI_NUMDIRECCION_REUNION, CI_NUMDIRECCION_ALTERNA, "
                + "CI_DIA_REUNION, CI_HORA_REUNION, CI_EJECUTIVO, CI_COORDINADOR, CI_MULTA_RETRASO, "
                + "CI_MULTA_FALTA, CI_TASA, CI_FECHA_CAPTURA, CI_MONTO, CI_MONTO_CON_COMISION, CI_MONTO_REFINANCIADO, CI_FECHA_DISPERSION, CI_BANCO_DISPERSION, ci_estatusIC, ci_estatusIC_2) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,  ?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?)";

        Connection cn = null;
        PreparedStatement ps = null;
        ciclo.idCiclo = getNext(ciclo.idGrupo);
        int param = 1;
        int res = 0;
        try {
            if (conn == null) {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            } else {
                ps = conn.prepareStatement(query);
            }
            ps.setInt(param++, ciclo.idGrupo);
            ps.setInt(param++, ciclo.idCiclo);
            ps.setInt(param++, ciclo.idTipoCiclo);
            ps.setInt(param++, ciclo.estatus);
            ps.setInt(param++, ciclo.idDireccionReunion);
            ps.setInt(param++, ciclo.idDireccionAlterna);
            ps.setInt(param++, ciclo.diaReunion);
            ps.setInt(param++, ciclo.horaReunion);
            ps.setInt(param++, ciclo.asesor);
            ps.setInt(param++, ciclo.coordinador);
            ps.setDouble(param++, ciclo.multaRetraso);
            ps.setDouble(param++, ciclo.multaFalta);
            ps.setInt(param++, ciclo.tasa);
            ps.setDouble(param++, ciclo.monto);
            ps.setDouble(param++, ciclo.montoConComision);
            ps.setDouble(param++, ciclo.montoRefinanciado);
            ps.setDate(param++, ciclo.fechaDispersion);
            ps.setInt(param++, ciclo.bancoDispersion);
            ps.setInt(param++, ciclo.estatusIC);
            ps.setInt(param++, ciclo.estatusIC2);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Ciclo= " + ciclo.toString());
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
            res = ciclo.idCiclo;
        } catch (SQLException sqle) {
            myLogger.error("addCicloGrupal", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addCicloGrupal", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return res;
    }

    public int addCicloApertura(CicloGrupalVO ciclo) throws ClientesException {

        String query = "INSERT INTO d_ciclos_grupales(ci_numgrupo, ci_numciclo, ci_tipo_ciclo, ci_estatus, ci_numdireccion_reunion, ci_numdireccion_alterna, ci_dia_reunion, ci_hora_reunion,"
                +" ci_ejecutivo, ci_coordinador, ci_multa_retraso, ci_multa_falta, ci_tasa, ci_fecha_captura, ci_monto, ci_monto_con_comision, ci_monto_refinanciado, ci_fecha_dispersion,"
                +" ci_banco_dispersion, ci_aperturador, ci_porgarantia) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,  ?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?, ?)";

        Connection cn = null;
        PreparedStatement ps = null;
        ciclo.idCiclo = getNext(ciclo.idGrupo);
        int param = 1;
        int res = 0;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(param++, ciclo.idGrupo);
            ps.setInt(param++, ciclo.idCiclo);
            ps.setInt(param++, ciclo.idTipoCiclo);
            ps.setInt(param++, ciclo.estatus);
            ps.setInt(param++, ciclo.idDireccionReunion);
            ps.setInt(param++, ciclo.idDireccionAlterna);
            ps.setInt(param++, ciclo.diaReunion);
            ps.setInt(param++, ciclo.horaReunion);
            ps.setInt(param++, ciclo.asesor);
            ps.setInt(param++, ciclo.coordinador);
            ps.setDouble(param++, ciclo.multaRetraso);
            ps.setDouble(param++, ciclo.multaFalta);
            ps.setInt(param++, ciclo.tasa);
            ps.setDouble(param++, ciclo.monto);
            ps.setDouble(param++, ciclo.montoConComision);
            ps.setDouble(param++, ciclo.montoRefinanciado);
            ps.setDate(param++, ciclo.fechaDispersion);
            ps.setInt(param++, ciclo.bancoDispersion);
            ps.setInt(param++, ciclo.aperturador);
            ps.setInt(param++, ciclo.garantia);
            myLogger.debug("Ejecutando = " + ps);
            //myLogger.debug("Ciclo= " + ciclo.toString());
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
            res = ciclo.idCiclo;
        } catch (SQLException sqle) {
            myLogger.error("addCicloApertura", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addCicloApertura", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return res;
    }

    public int updateCiclo(CicloGrupalVO ciclo) throws ClientesException {
        return updateCiclo(null, ciclo);
    }

    public int updateCiclo(Connection conn, CicloGrupalVO ciclo) throws ClientesException {
        return updateCiclo(conn, ciclo, false);
    }

    public int updateCiclo(Connection conn, CicloGrupalVO ciclo,boolean cierraConexion) throws ClientesException {

        String query = "UPDATE D_CICLOS_GRUPALES SET CI_ESTATUS = ?, CI_DESEMBOLSADO = ?, CI_TASA = ?, CI_DIA_REUNION = ?, CI_HORA_REUNION = ?, "
                + "CI_EJECUTIVO = ?, CI_COORDINADOR = ?, CI_MULTA_RETRASO = ?, CI_MULTA_FALTA = ?, CI_MONTO = ?, CI_TASA = ?, CI_MONTO_CON_COMISION = ?, "
                + "CI_MONTO_REFINANCIADO = ?, CI_FECHA_CAPTURA = CI_FECHA_CAPTURA, CI_NUMCREDITO_IBS = ?, CI_NUMCUENTA_IBS = ?, CI_PLAZO = ?, ci_fecha_dispersion = ?, "
                + "ci_banco_dispersion = ?, ci_fondeador = ?, ci_estatusIC = ?, ci_estatusIC_2 = ? "
                + "WHERE CI_NUMGRUPO = ? AND CI_NUMCICLO = ?";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            if (conn == null) {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            } else {
                ps = conn.prepareStatement(query);
            }
            ps.setInt(param++, ciclo.estatus);
            ps.setInt(param++, ciclo.desembolsado);
            ps.setInt(param++, ciclo.tasa);
            ps.setInt(param++, ciclo.diaReunion);
            ps.setInt(param++, ciclo.horaReunion);
            ps.setInt(param++, ciclo.asesor);
            ps.setInt(param++, ciclo.coordinador);
            ps.setDouble(param++, ciclo.multaRetraso);
            ps.setDouble(param++, ciclo.multaFalta);
            ps.setDouble(param++, ciclo.monto);
            ps.setInt(param++, ciclo.tasa);
            ps.setDouble(param++, ciclo.montoConComision);
            ps.setDouble(param++, ciclo.montoRefinanciado);
            ps.setInt(param++, ciclo.idCreditoIBS);
            ps.setInt(param++, ciclo.idCuentaIBS);
            ps.setInt(param++, ciclo.plazo);
            ps.setDate(param++, ciclo.fechaDispersion);
            ps.setInt(param++, ciclo.bancoDispersion);
            ps.setInt(param++, ciclo.fondeador);
            ps.setInt(param++, ciclo.estatusIC);
            ps.setInt(param++, ciclo.estatusIC2);
            ps.setInt(param++, ciclo.idGrupo);
            ps.setInt(param++, ciclo.idCiclo);
            myLogger.debug("Ejecutando = " + ps);
            myLogger.debug("Grupo = " + ciclo.toString());
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("updateCiclo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateCiclo", e);
            throw new ClientesException(e.getMessage());
        } finally {
            if(cierraConexion){
            
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
                
        }
        }
        return res;
    }
    
        public int updateCiclo(Connection conn, int numGrupo, int numCiclo, int semAdi) throws ClientesException {

        String query = "UPDATE D_CICLOS_GRUPALES SET ci_semadi = ? WHERE CI_NUMGRUPO = ? AND CI_NUMCICLO = ?";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            if (conn == null) {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            } else {
                ps = conn.prepareStatement(query);
            }
            ps.setInt(param++, semAdi);
            ps.setInt(param++, numGrupo);
            ps.setInt(param++, numCiclo);
            myLogger.debug("Ejecutando = " + ps);
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("updateCiclo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateCiclo", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return res;
    }


    public int updateCicloApertura(CicloGrupalVO ciclo) throws ClientesException {

        String query = "UPDATE d_ciclos_grupales SET ci_desembolsado = ?, ci_tasa = ?, ci_dia_reunion = ?, ci_hora_reunion = ?, ci_ejecutivo = ?, ci_coordinador = ?, "
                + "ci_multa_retraso = ?, ci_multa_falta = ?, ci_monto = ?, ci_tasa = ?, ci_monto_con_comision = ?, ci_monto_refinanciado = ?, ci_numcredito_ibs = ?, "
                + "ci_numcuenta_ibs = ?, ci_plazo = ?, ci_estatus = ?, ci_fecha_dispersion = ?, ci_banco_dispersion = ?, ci_aperturador = ?, ci_porgarantia = ?, ci_fondeador = ?, "
                + "ci_estatusIC = ?, ci_estatusIC_2 = ? "
                + "WHERE ci_numgrupo = ? AND ci_numciclo = ?";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            //if (conn == null) {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            /*} else {
             ps = conn.prepareStatement(query);
             }*/
            ps.setInt(param++, ciclo.desembolsado);
            ps.setInt(param++, ciclo.tasa);
            ps.setInt(param++, ciclo.diaReunion);
            ps.setInt(param++, ciclo.horaReunion);
            ps.setInt(param++, ciclo.asesor);
            ps.setInt(param++, ciclo.coordinador);
            ps.setDouble(param++, ciclo.multaRetraso);
            ps.setDouble(param++, ciclo.multaFalta);
            ps.setDouble(param++, ciclo.monto);
            ps.setInt(param++, ciclo.tasa);
            ps.setDouble(param++, ciclo.montoConComision);
            ps.setDouble(param++, ciclo.montoRefinanciado);
            ps.setInt(param++, ciclo.idCreditoIBS);
            ps.setInt(param++, ciclo.idCuentaIBS);
            ps.setInt(param++, ciclo.plazo);
            ps.setInt(param++, ciclo.estatus);
            ps.setDate(param++, ciclo.fechaDispersion);
            ps.setInt(param++, ciclo.bancoDispersion);
            ps.setInt(param++, ciclo.aperturador);
            ps.setInt(param++, ciclo.garantia);
            ps.setInt(param++, ciclo.fondeador);
            ps.setInt(param++, ciclo.estatusIC);
            ps.setInt(param++, ciclo.estatusIC2);
            ps.setInt(param++, ciclo.idGrupo);
            ps.setInt(param++, ciclo.idCiclo);
            myLogger.debug("Ejecutando = " + ps);
            //myLogger.debug("Grupo = " + ciclo.toString());
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("updateCicloApertura", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateCicloApertura", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return res;
    }

    public int updateCicloCredito(CicloGrupalVO ciclo) throws ClientesException {

        String query = "UPDATE D_CICLOS_GRUPALES SET CI_NUMCREDITO_IBS = ?, CI_NUMCUENTA_IBS = ?, CI_TASA_CALCULADA = ? "
                + "WHERE CI_NUMGRUPO = ? AND CI_NUMCICLO = ?";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(param++, ciclo.idCreditoIBS);
            ps.setInt(param++, ciclo.idCuentaIBS);
            ps.setDouble(param++, ciclo.tasaCalculada);
            ps.setInt(param++, ciclo.idGrupo);
            ps.setInt(param++, ciclo.idCiclo);
            myLogger.debug("Ejecutando = " + query);
            res = ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("updateCicloCredito", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateCicloCredito", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return res;
    }

    public int getNext(int idGrupo) throws ClientesException {

        String query = "SELECT COALESCE(MAX(CI_NUMCICLO),0)+1 AS NEXT FROM D_CICLOS_GRUPALES WHERE CI_NUMGRUPO = ?";
        Connection cn = null;
        int next = 1;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idGrupo);
            ResultSet rs = ps.executeQuery();
            myLogger.debug("Ejecutando = " + query);
            if (rs.next()) {
                next = rs.getInt("NEXT");
            }
            myLogger.debug("Resultado = " + next);
        } catch (SQLException sqle) {
            myLogger.error("getNext", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getNext", e);
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
        return next;
    }

    public int getEstatusCiclo(int idGrupo, int idCiclo) throws ClientesException {

        String query = "SELECT ci_estatus FROM d_ciclos_grupales WHERE ci_numgrupo = ? AND ci_numciclo = ?";
        Connection cn = null;
        int estatus = 0;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idGrupo);
            ps.setInt(2, idCiclo);
            ResultSet rs = ps.executeQuery();
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + idGrupo + ", " + idCiclo + "]");
            if (rs.next()) {
                estatus = rs.getInt("ci_estatus");
            }
        } catch (SQLException sqle) {
            myLogger.error("getEstatusCiclo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getEstatusCiclo", e);
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
        return estatus;
    }

    public boolean grupoEnviado(int idCliente, int idSolicitud) throws ClientesException {
        Connection cn = null;
        String query = "SELECT * FROM D_INTEGRANTES_CICLO, D_CICLOS_GRUPALES LEFT JOIN D_BITACORA_SYNCRONET "
                + "ON (BS_NUMCLIENTE = CI_NUMGRUPO AND BS_NUMSOLICITUD = CI_NUMCICLO) WHERE "
                + "CI_NUMGRUPO = IC_NUMGRUPO AND CI_NUMCICLO = IC_NUMCICLO AND IC_NUMCLIENTE = ? AND "
                + "IC_NUMSOLICITUD = ? AND ( BS_TIPO_ENVIO = 'GPO' OR CI_DESEMBOLSADO = 3)";
        boolean esEnvio = false;
        int param = 1;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(param++, idCliente);
            ps.setInt(param++, idSolicitud);
            myLogger.debug("Ejecutando:" + query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                esEnvio = true;
            }
        } catch (SQLException sqle) {
            myLogger.error("grupoEnviado", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("grupoEnviado", e);
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
        return esEnvio;
    }

    public boolean existeCiclo(int idGrupo, int idCiclo) throws ClientesException {
        Connection cn = null;
        String query = "SELECT * FROM d_ciclos_grupales WHERE ci_numgrupo = ? AND ci_numciclo = ?";
        boolean respuesta = false;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idGrupo);
            ps.setInt(2, idCiclo);
            myLogger.debug("Ejecutando:" + query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                respuesta = true;
            }
        } catch (SQLException sqle) {
            myLogger.error("existeCiclo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("existeCiclo", e);
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
        return respuesta;
    }

    public CicloGrupalVO getCiclosGrupales(int numGrupo, int numCiclo)
            throws ClientesException {
        CicloGrupalVO cicloGrupal = null;
        Connection cn = null;

        String query = "SELECT *"
                + " FROM d_ciclos_grupales WHERE ci_numgrupo=? AND ci_numciclo=?";
        try {
            int param = 1;
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            myLogger.debug("Ejecutando = " + query);
            ps.setInt(param++, numGrupo);
            ps.setInt(param++, numCiclo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                cicloGrupal = new CicloGrupalVO();
                cicloGrupal.idGrupo = rs.getInt("ci_numgrupo");
                cicloGrupal.idCiclo = rs.getInt("ci_numciclo");

                cicloGrupal.estatus = rs.getInt("ci_estatus");
                cicloGrupal.desembolsado = rs.getInt("ci_desembolsado");
                cicloGrupal.diaReunion = rs.getInt("ci_dia_reunion");
                cicloGrupal.horaReunion = rs.getInt("ci_hora_reunion");
                cicloGrupal.asesor = rs.getInt("ci_ejecutivo");
                cicloGrupal.tasa = rs.getInt("ci_tasa");
                cicloGrupal.tasaCalculada = rs.getInt("CI_TASA_CALCULADA");
                cicloGrupal.coordinador = rs.getInt("ci_coordinador");
                cicloGrupal.multaRetraso = rs.getDouble("ci_multa_retraso");
                cicloGrupal.multaFalta = rs.getDouble("ci_multa_falta");
                cicloGrupal.fechaCaptura = rs.getTimestamp("ci_fecha_captura");
                cicloGrupal.idDireccionReunion = rs.getInt("ci_numdireccion_reunion");
                cicloGrupal.idDireccionAlterna = rs.getInt("ci_numdireccion_alterna");
                myLogger.debug("Incidencia encontrada : "
                        + cicloGrupal.toString());
            }
        } catch (SQLException sqle) {
            myLogger.error("getCiclosGrupales", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCiclosGrupales", e);
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
        return cicloGrupal;
    }

    /**
     * Obtiene el numero de ciclo actual de un grupo JAHTECH
     *
     * @param numGrupo Numero de grupo
     * @return Numero de ciclo
     * @throws ClientesException
     */
    public int getCicloActual(int numGrupo) throws ClientesException {
        int resultado = 0;
        Connection cn = null;

        String query = "SELECT MAX(CI_NUMCICLO) AS NUMCICLO FROM D_CICLOS_GRUPALES WHERE CI_NUMGRUPO=?";
        try {
            int param = 1;
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            myLogger.debug("Ejecutando = " + query);
            ps.setInt(param++, numGrupo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                resultado = rs.getInt("numciclo");
            }
        } catch (SQLException sqle) {
            myLogger.error("getCicloActual", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCicloActual", e);
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

    public CicloGrupalVO[] getCiclosForIBS(Date fecha) throws ClientesException {

        String query = "SELECT DISTINCT( IC_NUMGRUPO ), IC_NUMCICLO  FROM D_SOLICITUDES "
                + "LEFT JOIN D_INTEGRANTES_CICLO ON (SO_NUMCLIENTE=IC_NUMCLIENTE AND SO_NUMSOLICITUD=IC_NUMSOLICITUD) "
                + "WHERE SO_NUMOPERACION = 3 AND SO_FECHA_DESEMBOLSO = ? AND SO_DESEMBOLSADO = 2 GROUP BY 1,2";

        ArrayList<CicloGrupalVO> array = new ArrayList<CicloGrupalVO>();
        CicloGrupalVO elementos[] = null;
        CicloGrupalVO temporal = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setDate(1, fecha);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new CicloGrupalVO();
                temporal.idGrupo = rs.getInt(1);
                temporal.idCiclo = rs.getInt(2);
                array.add(temporal);
            }

            if (array.size() > 0) {
                elementos = new CicloGrupalVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (CicloGrupalVO) array.get(i);
                }
            }

        } catch (SQLException sqle) {
            myLogger.error("getCiclosForIBS", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCiclosForIBS", e);
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

        return elementos;

    }

    public int deleteCicloCredito(CicloGrupalVO ciclo) throws ClientesException {

        String query = "DELETE FROM D_CICLOS_GRUPALES WHERE CI_NUMGRUPO = ? AND CI_NUMCICLO = ?";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(param++, ciclo.idGrupo);
            ps.setInt(param++, ciclo.idCiclo);
            myLogger.debug("Ejecutando = " + query);
            res = ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("deleteCicloCredito", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("deleteCicloCredito", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return res;
    }

    public void respaldaCicloGrupal(int numEquipo, int numCiclo) throws ClientesException {

        String query = "insert into d_ciclos_grupales_del "
                + "(ci_numgrupo,ci_numciclo,ci_tipo_ciclo,ci_numcredito_ibs,ci_numcuenta_ibs,ci_estatus,ci_dia_reunion,ci_desembolsado,ci_hora_reunion,ci_ejecutivo,ci_coordinador,ci_multa_retraso,ci_multa_falta,ci_tasa,ci_fecha_captura,ci_numdireccion_reunion,ci_numdireccion_alterna,ci_contrato,ci_monto,ci_monto_con_comision,ci_monto_refinanciado,ci_tasa_calculada,ci_estatus_revision_monitor,ci_conseguro,ci_plazo,ci_id_migracion,ci_fecha_dispersion,ci_banco_dispersion,ci_origenmigracion,ci_porgarantia,ci_aperturador,ci_fondeador,ci_estatusIC,ci_estatusIC_2) "
                + "(select ci_numgrupo,ci_numciclo,ci_tipo_ciclo,ci_numcredito_ibs,ci_numcuenta_ibs,ci_estatus,ci_dia_reunion,ci_desembolsado,ci_hora_reunion,ci_ejecutivo,ci_coordinador,ci_multa_retraso,ci_multa_falta,ci_tasa,ci_fecha_captura,ci_numdireccion_reunion,ci_numdireccion_alterna,ci_contrato,ci_monto,ci_monto_con_comision,ci_monto_refinanciado,ci_tasa_calculada,ci_estatus_revision_monitor,ci_conseguro,ci_plazo,ci_id_migracion,ci_fecha_dispersion,ci_banco_dispersion,ci_origenmigracion,ci_porgarantia,ci_aperturador,ci_fondeador,ci_estatusIC,ci_estatusIC_2"
                + " FROM d_ciclos_grupales WHERE CI_NUMGRUPO = ? AND CI_NUMCICLO = ?)";
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = getConnection();
            
            ps = cn.prepareStatement(query);
            ps.setInt(1, numEquipo);
            ps.setInt(2, numCiclo);
            myLogger.debug("Ejecutando: " + ps.toString());
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("SQLException", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("Exception", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException sqle) {
                myLogger.error("SQLException", sqle);
                throw new ClientesDBException(sqle.getMessage());
            }
        }
    }

    
    public void deleteCicloGrupal(int numEquipo, int numCiclo, boolean conexionODS) throws ClientesException {

        String query = "DELETE FROM d_ciclos_grupales WHERE CI_NUMGRUPO = ? AND CI_NUMCICLO = ?";
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            if(conexionODS){
                cn = getODSConnection();
            }else{
                cn = getConnection();
            }
            ps = cn.prepareStatement(query);
            ps.setInt(1, numEquipo);
            ps.setInt(2, numCiclo);
            myLogger.debug("Ejecutando: " + ps.toString());
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("SQLException", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("Exception", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException sqle) {
                myLogger.error("SQLException", sqle);
                throw new ClientesDBException(sqle.getMessage());
            }
        }
    }
    
    public void bajaLogicaCicloGrupal(int numEquipo, int numCiclo) {

        String query = "update d_ciclos_grupales set ci_estatus = ? WHERE CI_NUMGRUPO = ? AND CI_NUMCICLO = ?";
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = getODSConnection();

            ps = cn.prepareStatement(query);
            ps.setInt(1, 0);
            ps.setInt(2, numEquipo);
            ps.setInt(3, numCiclo);
            myLogger.debug("Ejecutando: " + ps.toString());
            int result = ps.executeUpdate();
            myLogger.debug("Registros afectados: " + result);
        } catch (SQLException sqle) {
            myLogger.error("Error de conexión ODS...");
            myLogger.error("SQLException", sqle);
        } catch (Exception e) {
            myLogger.error("Error de al realizar baja logica ciclo grupales ODS");
            myLogger.error("Exception", e);
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException sqle) {
                myLogger.error("Error de conexión ODS...");
                myLogger.error("SQLException", sqle);
            }
        }
    }

    public double tasaCicloCredito(SaldoIBSVO saldo) throws ClientesException {

        String query = "SELECT ci_tasa FROM d_ciclos_grupales WHERE ci_numgrupo= ? AND ci_numciclo= ?";

        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        int param = 1;
        double tasa = 0;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(param++, saldo.getIdClienteSICAP());
            ps.setInt(param++, saldo.getIdSolicitudSICAP());
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + saldo.getIdClienteSICAP() + ", " + saldo.getIdSolicitudSICAP() + "]");
            res = ps.executeQuery();
            while (res.next()) {
                tasa = res.getDouble("ci_tasa");
            }

        } catch (SQLException sqle) {
            myLogger.error("tasaCicloCredito", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("tasaCicloCredito", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return tasa;
    }

    public void updateSeguroCiclo(int seguro, int idGrupo, int idCiclo) throws ClientesException {

        String query = "UPDATE d_ciclos_grupales SET ci_conseguro=? WHERE ci_numgrupo=? AND ci_numciclo=?";

        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, seguro);
            ps.setInt(2, idGrupo);
            ps.setInt(3, idCiclo);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + idGrupo + ", " + idCiclo + ", " + seguro + "]");
            ps.executeUpdate();

        } catch (SQLException sqle) {
            myLogger.error("updateSeguroCiclo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateSeguroCiclo", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
    }

    public void updateEstatusCiclo(int idGrupo, int idCiclo, int estatus) throws ClientesException {

        String query = "UPDATE d_ciclos_grupales SET ci_estatus=? WHERE ci_numgrupo=? AND ci_numciclo=?";

        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, estatus);
            ps.setInt(2, idGrupo);
            ps.setInt(3, idCiclo);
            myLogger.debug("Ejecutando = " + ps.toString());
            ps.executeUpdate();
            
        } catch (SQLException sqle) {
            myLogger.error("updateEstatusCiclo",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateEstatusCiclo",e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
    }
    
    public void updateEstatusCicloIC(Connection con, int idGrupo, int idCiclo, int estatus, int semDisp) throws ClientesException {

        String query = "";
        if(semDisp == ClientesConstants.DISPERSION_SEMANA_2)
            query = "UPDATE d_ciclos_grupales SET ci_estatusIC=? WHERE ci_numgrupo=? AND ci_numciclo=?";
        else if(semDisp == ClientesConstants.DISPERSION_SEMANA_4)
            query = "UPDATE d_ciclos_grupales SET ci_estatusIC_2=? WHERE ci_numgrupo=? AND ci_numciclo=?";
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            if (con == null){
                cn = getConnection();
                ps = cn.prepareStatement(query);
            } else {
                ps = con.prepareStatement(query);
            }
            ps.setInt(1, estatus);
            ps.setInt(2, idGrupo);
            ps.setInt(3, idCiclo);
            myLogger.debug("Ejecutando = " + ps.toString());
            ps.executeUpdate();
            
        } catch (SQLException sqle) {
            myLogger.error("updateEstatusCiclo",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateEstatusCiclo",e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null)
                    cn.close();
                if (ps != null)
                    ps.close();
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
    }
    
    public ArrayList<CicloGrupalVO> getEquiposEjecutivos(int idEjecutivo) throws ClientesException {

        ArrayList<CicloGrupalVO> arrEquipos = new ArrayList<CicloGrupalVO>();
        String query = "SELECT ci_numgrupo,ci_numciclo,ib_nombreCliente,ib_nombresucursal,ib_fecha_desembolso,ib_fecha_vencimiento,ci_ejecutivo,CONCAT(ej_nombre,' ',ej_apaterno,' ',ej_amaterno) AS ejecutivo "
                + "FROM d_ciclos_grupales INNER JOIN d_saldos ON (ci_numgrupo=ib_numclientesicap AND ci_numciclo=ib_numsolicitudsicap) "
                + "INNER JOIN c_ejecutivos ON (ci_ejecutivo=ej_numejecutivo) WHERE ci_ejecutivo=? AND ib_estatus IN (1,2,4);";
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, idEjecutivo);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + idEjecutivo + "]");
            res = ps.executeQuery();
            while (res.next()) {
                arrEquipos.add(new CicloGrupalVO(res.getInt("ci_numgrupo"), res.getInt("ci_numciclo"), res.getInt("ci_ejecutivo"), res.getDate("ib_fecha_desembolso"), res.getString("ib_nombreCliente"), res.getString("ejecutivo"), res.getString("ib_nombresucursal"), res.getDate("ib_fecha_vencimiento")));
            }
        } catch (SQLException sqle) {
            myLogger.error("getEquiposEjecutivos", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getEquiposEjecutivos", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return arrEquipos;
    }

    public boolean setEjecutivo(CicloGrupalVO grupo, int idEjecutivo, Connection con) throws SQLException {

        boolean listo = true;
        String query = "";
        PreparedStatement pstm = null;
        try {
            query = "UPDATE d_ciclos_grupales SET ci_ejecutivo= ? WHERE ci_numgrupo=? AND ci_numciclo=?";
            pstm = con.prepareStatement(query);
            pstm.setInt(1, idEjecutivo);
            pstm.setInt(2, grupo.getIdGrupo());
            pstm.setInt(3, grupo.getIdCiclo());
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + idEjecutivo + ", " + grupo.getIdGrupo() + ", " + grupo.getIdCiclo() + "]");
            pstm.executeUpdate();
        } catch (SQLException e) {
            myLogger.error("setEjecutivo", e);
            con.rollback();
            return listo = false;
        }
        return listo;
    }

    public ArrayList<CicloGrupalVO> getCiclosFechaDispercion(Date fechaInicio, Date fechaFin, int sucursal) throws ClientesException {

        ArrayList<CicloGrupalVO> arrEquipos = new ArrayList<CicloGrupalVO>();
        int param = 0;
        String query = "SELECT ci_numgrupo,ci_numciclo FROM d_ciclos_grupales INNER JOIN d_grupos ON (gr_numgrupo=ci_numgrupo) WHERE ci_desembolsado>0 AND ";
        if (fechaFin != null) {
            query += "ci_fecha_dispersion BETWEEN ? AND ? ";
        } else {
            query += "ci_fecha_dispersion=? ";
        }
        query += (sucursal > 0 ? "AND gr_numsucursal=?;" : ";");
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setDate(++param, fechaInicio);
            if (fechaFin != null) {
                ps.setDate(++param, fechaFin);
            }
            if (sucursal > 0) {
                ps.setInt(++param, sucursal);
            }
            myLogger.debug("Ejecutando = " + ps);
            res = ps.executeQuery();
            while (res.next()) {
                arrEquipos.add(new CicloGrupalVO(res.getInt("ci_numgrupo"), res.getInt("ci_numciclo")));
            }
        } catch (SQLException sqle) {
            myLogger.error("getCiclosFechaDispercion", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCiclosFechaDispercion", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return arrEquipos;
    }

    public void getCicloSolicitud(SolicitudVO solicitud) throws ClientesException {

        String query = "select ci_numgrupo, ci_numciclo, ib_estatus, ib_nombreCliente, ib_fecha_desembolso, ib_num_cuotas_trancurridas, ib_num_cuotas from "
                + "d_integrantes_ciclo, d_ciclos_grupales, d_saldos  "
                + "where ic_numcliente = ? and ic_numsolicitud = ? "
                + "and ic_numgrupo = ci_numgrupo and ic_numciclo = ci_numciclo "
                + "and ci_numgrupo = ib_numClienteSICAP and ci_numciclo = ib_numSolicitudSICAP";
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, solicitud.idCliente);
            ps.setInt(2, solicitud.idSolicitud);
            myLogger.debug("Ejecutando = " + query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                solicitud.idGrupo = rs.getInt("CI_NUMGRUPO");
                solicitud.nombreGrupo = rs.getString("ib_nombreCliente");
                solicitud.idCiclo = rs.getInt("CI_NUMCICLO");
                solicitud.estatusCiclo = rs.getInt("ib_estatus");
                solicitud.fechaDesembolso = rs.getDate("ib_fecha_desembolso");
                solicitud.numCuotasTrans = rs.getInt("ib_num_cuotas_trancurridas");
                solicitud.numCuotas = rs.getInt("ib_num_cuotas");
            }
        } catch (SQLException sqle) {
            myLogger.error("getCicloSolicitud", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCicloSolicitud", e);
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

    }
    
   public void updateSeguroCompletoCiclo(CicloGrupalVO ciclo) throws ClientesException {
   
        String query = "UPDATE d_ciclos_grupales SET ci_consegurocompleto=? WHERE ci_numgrupo=? AND ci_numciclo=?";

        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, ciclo.seguroCompleto);
            ps.setInt(2, ciclo.idGrupo);
            ps.setInt(3, ciclo.idCiclo);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + ciclo.idGrupo + ", " + ciclo.idCiclo + ", " + ciclo.seguroCompleto + "]");
            ps.executeUpdate();

        } catch (SQLException sqle) {
            myLogger.error("updateSeguroCompletoCiclo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateSeguroCompletoCiclo", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
}
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
    }

}
