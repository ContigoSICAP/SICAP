package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.CifraControlVO;

public class CifraControlDAO extends DAOMaster {

    private static org.apache.log4j.Logger myLogger = org.apache.log4j.Logger.getLogger(CifraControlDAO.class);

    public int addCifraControl(CifraControlVO cifraControl) throws ClientesException {

        String query = "INSERT INTO D_CIFRAS_CONTROL (CC_FECHA,CC_BANCO_REFERENCIA,CC_REGISTROS_INTRODUCIDOS,CC_REGISTROS_PROCESADOS,"
                + "CC_REGISTROS_NOPROCESADOS, CC_MONTOS_INTRODUCIDOS, CC_MONTOS_PROCESADOS, CC_MONTOS_NOPROCESADOS,"
                + "CC_MONTOS_CREDITO,CC_MONTOS_SEGUROS,CC_MONTOS_EXCEDENTES, CC_SEGUROS, cc_numcuenta,cc_cantidadRegistros, cc_NombreArchivo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        ResultSet res = null;
        int key = 0;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setDate(param++, cifraControl.fechaMov);
            ps.setInt(param++, cifraControl.bancoReferencia);
            ps.setInt(param++, cifraControl.registrosIntroducidos);
            ps.setInt(param++, cifraControl.registrosProcesados);
            ps.setInt(param++, cifraControl.registrosNoProcesados);
            ps.setDouble(param++, cifraControl.montosIntroducidos);
            ps.setDouble(param++, cifraControl.montosProcesados);
            ps.setDouble(param++, cifraControl.montosNoProcesados);
            ps.setDouble(param++, cifraControl.montosAplicadosaCredito);
            ps.setDouble(param++, cifraControl.montosAplicadosaSeguros);
            ps.setDouble(param++, cifraControl.montosdeExcedentes);
            ps.setInt(param++, cifraControl.seguros);
            ps.setInt(param++, cifraControl.numCuenta);
            ps.setInt(param++, cifraControl.cantTotRegistros);
            ps.setString(param++, cifraControl.nombreArchivo);
            myLogger.debug("Ejecutando = " + ps);
            ps.executeUpdate();
            res = ps.getGeneratedKeys();
            if (res.next()) {
                key = res.getInt(1);
            }
        } catch (SQLException sqle) {
            myLogger.error("SQLException en addCifraControl : " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesException(sqle.getMessage());

        } catch (Exception e) {
            myLogger.error("Excepcion en addCifraControl : " + e.getMessage());
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
                if (res != null) {
                    res.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return key;
    }
    
    public int verificaCifraControl(CifraControlVO cifraControl) throws ClientesException {

        String query = "Select Count(cc_NombreArchivo) as Result " +
                       " from D_CIFRAS_CONTROL" +
                       " where cc_fecha = ?" +
                       " and cc_cantidadRegistros = ?" +
                       " and cc_banco_referencia = ?" +
                       " and cc_NombreArchivo = ? ;";
        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        ResultSet res = null;
        int key = 0;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setDate(param++, cifraControl.fechaMov);
            ps.setInt(param++, cifraControl.cantTotRegistros);
            ps.setInt(param++, cifraControl.bancoReferencia);
            ps.setString(param++, cifraControl.nombreArchivo);
            myLogger.debug("Ejecutando = " + ps);
            res = ps.executeQuery();
            if (res.next()) {
                key = res.getInt("Result");
            }
        } catch (SQLException sqle) {
            myLogger.error("SQLException en addCifraControl : " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesException(sqle.getMessage());

        } catch (Exception e) {
            myLogger.error("Excepcion en addCifraControl : " + e.getMessage());
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
                if (res != null) {
                    res.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return key;
    }

    public int addCifraControl(CifraControlVO cifraControl, Connection con) throws ClientesException {

        String query = "INSERT INTO D_CIFRAS_CONTROL (CC_FECHA,CC_BANCO_REFERENCIA,CC_REGISTROS_INTRODUCIDOS,CC_REGISTROS_PROCESADOS,"
                + "CC_REGISTROS_NOPROCESADOS, CC_MONTOS_INTRODUCIDOS, CC_MONTOS_PROCESADOS, CC_MONTOS_NOPROCESADOS,"
                + "CC_MONTOS_CREDITO,CC_MONTOS_SEGUROS,CC_MONTOS_EXCEDENTES, CC_SEGUROS, cc_numcuenta) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        ResultSet res = null;
        int param = 1, numInsert = 0;
        try {
            ps = con.prepareStatement(query);
            ps.setDate(param++, cifraControl.fechaMov);
            ps.setInt(param++, cifraControl.bancoReferencia);
            ps.setInt(param++, cifraControl.registrosIntroducidos);
            ps.setInt(param++, cifraControl.registrosProcesados);
            ps.setInt(param++, cifraControl.registrosNoProcesados);
            ps.setDouble(param++, cifraControl.montosIntroducidos);
            ps.setDouble(param++, cifraControl.montosProcesados);
            ps.setDouble(param++, cifraControl.montosNoProcesados);
            ps.setDouble(param++, cifraControl.montosAplicadosaCredito);
            ps.setDouble(param++, cifraControl.montosAplicadosaSeguros);
            ps.setDouble(param++, cifraControl.montosdeExcedentes);
            ps.setInt(param++, cifraControl.seguros);
            ps.setInt(param++, cifraControl.numCuenta);
            myLogger.debug("Ejecutando " + ps);
            ps.executeUpdate();
            res = ps.getGeneratedKeys();
            if (res.next()) {
                numInsert = res.getInt(1);
            }
        } catch (SQLException sqle) {
            myLogger.error("SQLException en addCifraControl :", sqle);
        } catch (Exception e) {
            myLogger.error("Excepcion en addCifraControl :", e);
        }
        return numInsert;
    }

    public void updateCifraControlLayout(CifraControlVO cifraControl, Connection con) throws ClientesException {

        String query = "UPDATE D_CIFRAS_CONTROL SET CC_FECHA = ?,CC_BANCO_REFERENCIA = ?, CC_REGISTROS_INTRODUCIDOS = ?, "
                + "CC_REGISTROS_PROCESADOS = ?, CC_REGISTROS_NOPROCESADOS = ?, CC_MONTOS_INTRODUCIDOS = ?, "
                + "CC_MONTOS_PROCESADOS = ?, CC_MONTOS_NOPROCESADOS = ?, CC_MONTOS_CREDITO = ?, CC_MONTOS_SEGUROS = ?, "
                + "CC_MONTOS_EXCEDENTES = ?, CC_SEGUROS = ?, CC_SITUACION_PAGO = ? WHERE CC_ID_CIFRA_CONTROL = ? ";

        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            ps = con.prepareStatement(query);
            ps.setDate(param++, cifraControl.fechaMov);
            ps.setInt(param++, cifraControl.bancoReferencia);
            ps.setInt(param++, cifraControl.registrosIntroducidos);
            ps.setInt(param++, cifraControl.registrosProcesados);
            ps.setInt(param++, cifraControl.registrosNoProcesados);
            ps.setDouble(param++, cifraControl.montosIntroducidos);
            ps.setDouble(param++, cifraControl.montosProcesados);
            ps.setDouble(param++, cifraControl.montosNoProcesados);
            ps.setDouble(param++, cifraControl.montosAplicadosaCredito);
            ps.setDouble(param++, cifraControl.montosAplicadosaSeguros);
            ps.setDouble(param++, cifraControl.montosdeExcedentes);
            ps.setInt(param++, cifraControl.seguros);
            ps.setInt(param++, cifraControl.situacionPago);
            ps.setInt(param++, cifraControl.idCifraControl);
            myLogger.debug("Ejecutando = " + ps);
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);

        } catch (SQLException sqle) {
            myLogger.error("SQLException en updateCifraControl : " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesDBException(sqle.getMessage());

        } catch (Exception e) {
            myLogger.error("Excepcion en updateCifraControl : " + e.getMessage());
            e.printStackTrace();
            throw new ClientesException(e.getMessage());

        }

    }

    public void updateCifraControl(CifraControlVO cifraControl) throws ClientesException {

        String query = "UPDATE D_CIFRAS_CONTROL SET CC_FECHA = ?,CC_BANCO_REFERENCIA = ?, CC_REGISTROS_INTRODUCIDOS = ?, "
                + "CC_REGISTROS_PROCESADOS = ?, CC_REGISTROS_NOPROCESADOS = ?, CC_MONTOS_INTRODUCIDOS = ?, "
                + "CC_MONTOS_PROCESADOS = ?, CC_MONTOS_NOPROCESADOS = ?, CC_MONTOS_CREDITO = ?, CC_MONTOS_SEGUROS = ?, "
                + "CC_MONTOS_EXCEDENTES = ?, CC_SEGUROS = ?, CC_SITUACION_PAGO = ? WHERE CC_ID_CIFRA_CONTROL = ? ";
        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setDate(param++, cifraControl.fechaMov);
            ps.setInt(param++, cifraControl.bancoReferencia);
            ps.setInt(param++, cifraControl.registrosIntroducidos);
            ps.setInt(param++, cifraControl.registrosProcesados);
            ps.setInt(param++, cifraControl.registrosNoProcesados);
            ps.setDouble(param++, cifraControl.montosIntroducidos);
            ps.setDouble(param++, cifraControl.montosProcesados);
            ps.setDouble(param++, cifraControl.montosNoProcesados);
            ps.setDouble(param++, cifraControl.montosAplicadosaCredito);
            ps.setDouble(param++, cifraControl.montosAplicadosaSeguros);
            ps.setDouble(param++, cifraControl.montosdeExcedentes);
            ps.setInt(param++, cifraControl.seguros);
            ps.setInt(param++, cifraControl.situacionPago);
            ps.setInt(param++, cifraControl.idCifraControl);
            myLogger.debug("Ejecutando = " + ps);
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);

        } catch (SQLException sqle) {
            myLogger.error("SQLException en updateCifraControl : " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesDBException(sqle.getMessage());

        } catch (Exception e) {
            myLogger.error("Excepcion en updateCifraControl : " + e.getMessage());
            e.printStackTrace();
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

    public boolean updateCifraControl(CifraControlVO cifraControl, Connection con) throws ClientesException {

        PreparedStatement ps = null;
        boolean listo = true;
        int param = 1;
        String query = "UPDATE D_CIFRAS_CONTROL SET CC_FECHA = ?,CC_BANCO_REFERENCIA = ?, CC_REGISTROS_INTRODUCIDOS = ?, "
                + "CC_REGISTROS_PROCESADOS = ?, CC_REGISTROS_NOPROCESADOS = ?, CC_MONTOS_INTRODUCIDOS = ?, "
                + "CC_MONTOS_PROCESADOS = ?, CC_MONTOS_NOPROCESADOS = ?, CC_MONTOS_CREDITO = ?, CC_MONTOS_SEGUROS = ?, "
                + "CC_MONTOS_EXCEDENTES = ?, CC_SEGUROS = ?, CC_SITUACION_PAGO = ? WHERE CC_ID_CIFRA_CONTROL = ? ";
        try {
            ps = con.prepareStatement(query);
            ps.setDate(param++, cifraControl.fechaMov);
            ps.setInt(param++, cifraControl.bancoReferencia);
            ps.setInt(param++, cifraControl.registrosIntroducidos);
            ps.setInt(param++, cifraControl.registrosProcesados);
            ps.setInt(param++, cifraControl.registrosNoProcesados);
            ps.setDouble(param++, cifraControl.montosIntroducidos);
            ps.setDouble(param++, cifraControl.montosProcesados);
            ps.setDouble(param++, cifraControl.montosNoProcesados);
            ps.setDouble(param++, cifraControl.montosAplicadosaCredito);
            ps.setDouble(param++, cifraControl.montosAplicadosaSeguros);
            ps.setDouble(param++, cifraControl.montosdeExcedentes);
            ps.setInt(param++, cifraControl.seguros);
            ps.setInt(param++, cifraControl.situacionPago);
            ps.setInt(param++, cifraControl.idCifraControl);
            myLogger.debug("Ejecutando " + ps);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("Error en updateCifraControl SQLException ", sqle);
            return listo = false;
        }
        return listo;
    }

    public int getIdCifraControl() throws ClientesException {

        String query = "SELECT COALESCE(MAX(CC_ID_CIFRA_CONTROL),0) AS NEXT FROM D_CIFRAS_CONTROL";
        Connection cn = null;
        int next = 1;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            Logger.debug("Ejecutando = " + query);
            if (rs.next()) {
                next = rs.getInt("NEXT");
            }
        } catch (SQLException sqle) {
            Logger.debug("SQLException en getIdCifraControl : " + sqle.getMessage());
            sqle.printStackTrace();
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            Logger.debug("Excepcion en getIdCifraControl : " + e.getMessage());
            e.printStackTrace();
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

}
