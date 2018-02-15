package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.PagoReferenciadoVO;
import com.sicap.clientes.vo.PagoVO;
import org.apache.log4j.Logger;

public class PagoReferenciadoDAO extends DAOMaster {

    private static Logger myLogger = Logger.getLogger(PagoReferenciadoDAO.class);

    public boolean existeReferencia(String d_rg_referencia_anterior) throws ClientesException {
        int cantReg = 0;
        Connection cn = null;
        String query = "SELECT COUNT(*) FROM D_REFERENCIAS_GENERALES WHERE RG_REFERENCIA = ?";
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            myLogger.debug("Referencia anterior::" + d_rg_referencia_anterior);
            ps.setString(1, d_rg_referencia_anterior);
            myLogger.debug("Ejecutando = " + query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                cantReg = rs.getInt(1);
                myLogger.debug("Registros encontrados ::" + cantReg);
            }
        } catch (SQLException sqle) {
            myLogger.error("existeReferencia", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("existeReferencia", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException e) {
                myLogger.error("existeReferencia", e);
                throw new ClientesDBException(e.getMessage());
            }
        }
        if (cantReg == 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean existeReferenciaenTablaAlterna(String d_rg_referencia_anterior) throws ClientesException {
        int cantReg = 0;
        Connection cn = null;
        String query = "SELECT COUNT(*) FROM TEMP_EQUIVALENCIAS, D_REFERENCIAS_GENERALES WHERE REFERENCIA=RG_REFERENCIA"
                + " AND REFERENCIA_ALTERNA = ?";

        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            myLogger.debug("Referencia anterior::" + d_rg_referencia_anterior);
            ps.setString(1, d_rg_referencia_anterior);
            myLogger.debug("Ejecutando = " + query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                cantReg = rs.getInt(1);
                myLogger.debug("Registros encontrados ::" + cantReg);
            }
        } catch (SQLException sqle) {
            myLogger.error("existeReferenciaenTablaAlterna", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("existeReferenciaenTablaAlterna", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException e) {
                myLogger.error("existeReferenciaenTablaAlterna", e);
                throw new ClientesDBException(e.getMessage());
            }
        }
        if (cantReg == 0) {
            return false;
        } else {
            return true;
        }
    }

    public boolean esPagoT24(String referencia) throws ClientesException {

        Connection cn = null;
        boolean resultado = false;
        String query = "SELECT 1 FROM D_REFERENCIAS_GENERALES WHERE RG_REFERENCIA = ? AND (SUBSTR(RG_CONTRATO,1,2) = 'LD' OR SUBSTR(RG_CONTRATO,1,2) = '00')";
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, referencia);
            myLogger.debug("Ejecutando = " + query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                resultado = true;
            }
        } catch (SQLException sqle) {
            myLogger.error("esPagoT24", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("esPagoT24", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException e) {
                myLogger.error("esPagoT24", e);
                throw new ClientesDBException(e.getMessage());
            }
        }
        return resultado;
    }

    public int addIncidencia(PagoVO pago, int tipoIncidencia, String observaciones) throws ClientesException {
        String query = "INSERT INTO D_INCIDENCIAS_PAGOS_REF(IP_REFERENCIA, IP_FECHA_MOV, IP_TIPO_INCIDENCIA, IP_OBSERVACIONES,IP_BANCO_REFERENCIA,IP_REPROCESAR,IP_ID_CONTABILIDAD,IP_MONTO, IP_FECHA_HORA, ip_numcuenta)"
                + " VALUES (?, ?, ?, ?,?,?,?,?,CURRENT_TIMESTAMP,?)";

        Connection cn = null;
        int param = 1;
        int res = 0;
        try {
            PreparedStatement ps = null;
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setString(param++, pago.referencia);
            ps.setDate(param++, pago.fechaPago);
            ps.setInt(param++, tipoIncidencia);
            ps.setString(param++, observaciones);
            ps.setInt(param++, pago.bancoReferencia);
            ps.setInt(param++, 1);
            ps.setInt(param++, pago.idContable);
            ps.setDouble(param++, pago.monto);
            ps.setInt(param++, pago.numCuenta);
            myLogger.debug("Ejecutando = " + ps);
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("addIncidencia", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addIncidencia", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException sqle) {
                myLogger.error("addIncidencia", sqle);
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return res;
    }

    public int addIncidencia(PagoVO pago, int tipoIncidencia, String observaciones, Connection con) throws ClientesException {

        String query = "INSERT INTO D_INCIDENCIAS_PAGOS_REF(IP_REFERENCIA, IP_FECHA_MOV, IP_TIPO_INCIDENCIA, IP_OBSERVACIONES,IP_BANCO_REFERENCIA,IP_REPROCESAR,IP_ID_CONTABILIDAD,IP_MONTO, IP_FECHA_HORA, ip_numcuenta)"
                + " VALUES (?, ?, ?, ?,?,?,?,?,CURRENT_TIMESTAMP,?)";
        int param = 1;
        int numInci = 0;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            ps = con.prepareStatement(query);
            ps.setString(param++, pago.referencia);
            ps.setDate(param++, pago.fechaPago);
            ps.setInt(param++, tipoIncidencia);
            ps.setString(param++, observaciones);
            ps.setInt(param++, pago.bancoReferencia);
            ps.setInt(param++, 1);
            ps.setInt(param++, pago.idContable);
            ps.setDouble(param++, pago.monto);
            ps.setInt(param++, pago.numCuenta);
            myLogger.debug("Ejecutando " + ps);
            ps.executeUpdate();
            res = ps.getGeneratedKeys();
            if (res.next()) {
                numInci = res.getInt(1);
            }
        } catch (SQLException sqle) {
            myLogger.error("addIncidencia", sqle);
        } catch (Exception e) {
            myLogger.error("addIncidencia", e);
        }
        return numInci;
    }

    public int addIncidencia(PagoReferenciadoVO pagosVO, int tipoIncidencia, String observaciones) throws ClientesException {
        String query = "INSERT INTO D_INCIDENCIAS_PAGOS_REF(IP_REFERENCIA, IP_FECHA_MOV, IP_TIPO_INCIDENCIA, IP_OBSERVACIONES"
                + ") VALUES (?, ?, ?, ?)";

        Connection cn = null;
        int param = 1;
        int res = 0;
        try {
            PreparedStatement ps = null;
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setString(param++, pagosVO.referencia);
            ps.setDate(param++, pagosVO.fecha_movimiento);
            ps.setInt(param++, tipoIncidencia);
            ps.setString(param++, observaciones);

            myLogger.debug("Ejecutando = " + query);
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("addIncidencia", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addIncidencia", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException sqle) {
                myLogger.error("addIncidencia", sqle);
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return res;
    }

    public int addReferenciaAnterior(PagoReferenciadoVO pagosVO) throws ClientesException {
        String query = "INSERT INTO D_REFERENCIAS_ANTERIORES(RA_FECHA_MOV, RA_NOMBRE, RA_REFERENCIA, RA_MONTO"
                + ") VALUES (?, ?, ?, ?)";

        Connection cn = null;
        int param = 1;
        int res = 0;
        try {
            PreparedStatement ps = null;
            cn = getConnection();
            ps = cn.prepareStatement(query);

            ps.setDate(param++, pagosVO.fecha_movimiento);
            ps.setString(param++, pagosVO.nombre);
            ps.setString(param++, pagosVO.referencia);
            ps.setDouble(param++, pagosVO.saldo);

            myLogger.debug("Ejecutando = " + query);
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("addReferenciaAnterior", sqle);
            this.addIncidencia(pagosVO, 2, "Referencia duplicada");
            //throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addReferenciaAnterior", e);
            this.addIncidencia(pagosVO, 2, "Referencia duplicada");
            //throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException sqle) {
                myLogger.error("addReferenciaAnterior", sqle);
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return res;
    }

    public String getContrato(String referenciaAnt) throws ClientesException {
        String referenciaSyncronet = null;
        Connection cn = null;
        String query = "SELECT RG_CONTRATO FROM D_REFERENCIAS_GENERALES WHERE RG_REFERENCIA = ?";
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, referenciaAnt);
            myLogger.debug("Ejecutando = " + query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                referenciaSyncronet = rs.getString(1);
            }
        } catch (SQLException sqle) {
            myLogger.error("getContrato", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getContrato", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException e) {
                myLogger.error("getContrato", e);
                throw new ClientesDBException(e.getMessage());
            }
        }
        return referenciaSyncronet;
    }

    public PagoVO getPagoAReprocesar(String idIncidenciaReprocesar) throws ClientesException {

        // reprocesar = 1:  los posibles a reprocesar
        // reprocesar = 3:  los que se van a reprocesar
        String query = "SELECT * FROM D_INCIDENCIAS_PAGOS_REF WHERE IP_ID_INCIDENCIA=?";
        Connection cn = null;
        PagoVO temporal = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, idIncidenciaReprocesar);
            myLogger.debug("Ejecutando : " + query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                temporal = new PagoVO();
                temporal.idIncidencia = rs.getInt("IP_ID_INCIDENCIA");
                temporal.fechaPago = rs.getDate("IP_FECHA_MOV");
                temporal.tipo = rs.getString("IP_TIPO_INCIDENCIA");
                temporal.observaciones = rs.getString("IP_OBSERVACIONES");
                temporal.fechaHora = rs.getTimestamp("IP_FECHA_HORA");
                temporal.referencia = rs.getString("IP_REFERENCIA");
                temporal.nuevareferencia = rs.getString("IP_NUEVA_REFERENCIA");
                temporal.bancoReferencia = rs.getInt("IP_BANCO_REFERENCIA");
                temporal.reprocesar = rs.getInt("IP_REPROCESAR");
                temporal.usuarioReproceso = rs.getString("IP_USUARIO_REPROCESO");
                temporal.comentarios = rs.getString("IP_COMENTARIOS");
                temporal.idContable = rs.getInt("IP_ID_CONTABILIDAD");
                temporal.monto = rs.getDouble("IP_MONTO");
                temporal.numCuenta = rs.getInt("ip_numcuenta");
                //array.add(temporal);
            }
            //if ( array.size()>0 ){
            //elementos = new PagoVO[array.size()];
            // for(int i=0;i<elementos.length; i++) elementos[i] = (PagoVO)array.get(i);
            //}
        } catch (SQLException sqle) {
            myLogger.error("getPagoAReprocesar", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getPagoAReprocesar", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException e) {
                myLogger.error("getPagoAReprocesar", e);
                throw new ClientesDBException(e.getMessage());
            }
        }
        return temporal;

    }

    public PagoVO[] getPagoAReprocesarReferencia(String numeroReferencia) throws ClientesException {

        // reprocesar = 1:  los posibles a reprocesar
        // reprocesar = 3:  los que se van a reprocesar
        String query = "SELECT * FROM D_INCIDENCIAS_PAGOS_REF WHERE IP_REFERENCIA=?";
        Connection cn = null;
        ArrayList<PagoVO> array = new ArrayList<PagoVO>();
        PagoVO temporal = null;
        PagoVO elementos[] = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, numeroReferencia);
            myLogger.debug("Ejecutando : " + query);
            ResultSet rs = ps.executeQuery();
            myLogger.debug("Parametros : " + numeroReferencia);
            while (rs.next()) {
                temporal = new PagoVO();
                temporal.idIncidencia = rs.getInt("IP_ID_INCIDENCIA");
                temporal.fechaPago = rs.getDate("IP_FECHA_MOV");
                temporal.tipo = rs.getString("IP_TIPO_INCIDENCIA");
                temporal.observaciones = rs.getString("IP_OBSERVACIONES");
                temporal.fechaHora = rs.getTimestamp("IP_FECHA_HORA");
                temporal.referencia = rs.getString("IP_REFERENCIA");
                temporal.nuevareferencia = rs.getString("IP_NUEVA_REFERENCIA");
                temporal.bancoReferencia = rs.getInt("IP_BANCO_REFERENCIA");
                temporal.reprocesar = rs.getInt("IP_REPROCESAR");
                temporal.usuarioReproceso = rs.getString("IP_USUARIO_REPROCESO");
                temporal.comentarios = rs.getString("IP_COMENTARIOS");
                temporal.idContable = rs.getInt("IP_ID_CONTABILIDAD");
                temporal.monto = rs.getDouble("IP_MONTO");
                temporal.numCuenta = rs.getInt("ip_numcuenta");
                array.add(temporal);
            }
            if (array.size() > 0) {
                elementos = new PagoVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (PagoVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getPagoAReprocesarReferencia", sqle);
            throw new ClientesDBException(sqle.getMessage());

        } catch (Exception e) {
            myLogger.error("getPagoAReprocesarReferencia", e);
            throw new ClientesException(e.getMessage());

        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException e) {
                myLogger.error("getPagoAReprocesarReferencia", e);
                throw new ClientesDBException(e.getMessage());
            }
        }
        return elementos;

    }

    public PagoVO[] getPagosAReprocesar(int reprocesar, String fecha) throws ClientesException {

        // reprocesar = 1:  los posibles a reprocesar
        String query = "SELECT * FROM D_INCIDENCIAS_PAGOS_REF WHERE IP_REPROCESAR=? AND IP_FECHA_MOV=? ";
        Connection cn = null;
        ArrayList<PagoVO> array = new ArrayList<PagoVO>();
        PagoVO temporal = null;
        PagoVO elementos[] = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, reprocesar);
            ps.setString(2, fecha);
            myLogger.debug("Ejecutando : " + query);
            ResultSet rs = ps.executeQuery();
            myLogger.debug("Parametros : " + reprocesar + "  : " + fecha);
            while (rs.next()) {
                temporal = new PagoVO();
                temporal.idIncidencia = rs.getInt("IP_ID_INCIDENCIA");
                temporal.fechaPago = rs.getDate("IP_FECHA_MOV");
                temporal.tipo = rs.getString("IP_TIPO_INCIDENCIA");
                temporal.observaciones = rs.getString("IP_OBSERVACIONES");
                temporal.fechaHora = rs.getTimestamp("IP_FECHA_HORA");
                temporal.referencia = rs.getString("IP_REFERENCIA");
                temporal.nuevareferencia = rs.getString("IP_NUEVA_REFERENCIA");
                temporal.bancoReferencia = rs.getInt("IP_BANCO_REFERENCIA");
                temporal.reprocesar = rs.getInt("IP_REPROCESAR");
                temporal.usuarioReproceso = rs.getString("IP_USUARIO_REPROCESO");
                temporal.comentarios = rs.getString("IP_COMENTARIOS");
                temporal.monto = rs.getDouble("IP_MONTO");
                array.add(temporal);
            }
            if (array.size() > 0) {
                elementos = new PagoVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (PagoVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getPagosAReprocesar", sqle);
            throw new ClientesException(sqle.getMessage());

        } catch (Exception e) {
            myLogger.error("getPagosAReprocesar", e);
            throw new ClientesException(e.getMessage());

        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException e) {
                myLogger.error("getPagosAReprocesar", e);
                throw new ClientesDBException(e.getMessage());
            }
        }
        return elementos;

    }

    public PagoVO getPagoAReprocesar(PagoVO pago) throws ClientesException {

        String query = "SELECT * FROM D_PAGOS WHERE PA_REFERENCIA=? AND PA_FECHA_PAGO=?";
        Connection cn = null;
        //ArrayList<PagoVO> array = new ArrayList<PagoVO>();
        PagoVO temporal = null;
        //PagoVO elementos[] = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, pago.referencia);
            ps.setDate(2, pago.fechaPago);
            myLogger.debug("Ejecutando : " + query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                temporal = new PagoVO();
                temporal.referencia = rs.getString("PA_REFERENCIA");
                temporal.monto = rs.getDouble("PA_MONTO");
                temporal.fechaPago = rs.getDate("PA_FECHA_PAGO");
                temporal.fechaHora = rs.getTimestamp("PA_FECHA_HORA");
                temporal.tipo = rs.getString("PA_TIPO");
                temporal.enviado = rs.getInt("PA_ENVIADO");
                temporal.status = rs.getInt("PA_STATUS");
                temporal.bancoReferencia = rs.getInt("PA_BANCO_REFERENCIA");

            }

        } catch (SQLException sqle) {
            myLogger.error("getPagoAReprocesar", sqle);
            throw new ClientesException(sqle.getMessage());

        } catch (Exception e) {
            myLogger.error("getPagoAReprocesar", e);
            throw new ClientesException(e.getMessage());

        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException e) {
                myLogger.error("getPagoAReprocesar", e);
                throw new ClientesDBException(e.getMessage());
            }
        }
        return temporal;

    }

    public PagoVO getReferenciaAlterna(PagoVO pago) throws ClientesException {

        String query = "SELECT * FROM TEMP_EQUIVALENCIAS WHERE REFERENCIA_ALTERNA = ? ";
        Connection cn = null;
        //ArrayList<PagoVO> array = new ArrayList<PagoVO>();
        PagoVO temporal = null;
        //PagoVO elementos[] = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, pago.referencia);
            myLogger.debug("Ejecutando : " + query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                temporal = new PagoVO();
                temporal.numCliente = rs.getInt("NUMCLIENTE");
                temporal.nombre = rs.getString("NOMBRE");
                temporal.referencia = rs.getString("REFERENCIA");
                temporal.nuevareferencia = rs.getString("REFERENCIA_ALTERNA");
            }

        } catch (SQLException sqle) {
            myLogger.error("getReferenciaAlterna", sqle);
            throw new ClientesException(sqle.getMessage());

        } catch (Exception e) {
            myLogger.error("getReferenciaAlterna", e);
            throw new ClientesException(e.getMessage());

        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException e) {
                myLogger.error("getReferenciaAlterna", e);
                throw new ClientesDBException(e.getMessage());
            }
        }
        return temporal;

    }

    public int updateIncidencias(PagoVO pagos) throws ClientesException {
        int param = 1, res = 0;
        String query = "UPDATE D_INCIDENCIAS_PAGOS_REF SET IP_FECHA_MOV = ?, IP_TIPO_INCIDENCIA = ?, "
                + "IP_OBSERVACIONES = ?, IP_NUEVA_REFERENCIA = ?, "
                + "IP_BANCO_REFERENCIA = ? , IP_REPROCESAR = ? , IP_USUARIO_REPROCESO = ?, IP_COMENTARIOS = ?, IP_ID_CONTABILIDAD_SALIDA = ? "
                + "WHERE IP_ID_INCIDENCIA = ? ";

        myLogger.debug("Query a ejecutar:" + query);
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setDate(param++, pagos.fechaPago);
            ps.setString(param++, pagos.tipo);
            ps.setString(param++, pagos.observaciones);
            ps.setString(param++, pagos.nuevareferencia);
            ps.setInt(param++, pagos.bancoReferencia);
            ps.setInt(param++, pagos.reprocesar);
            ps.setString(param++, pagos.usuarioReproceso.toString());
            ps.setString(param++, pagos.comentarios);
            ps.setInt(param++, pagos.idContablePolizaSalidaIncidencia);
            //ps.setInt(param++, pagos.idContable);
            ps.setInt(param++, pagos.idIncidencia);

            res = ps.executeUpdate();
            myLogger.debug("Parámetros: " + pagos.toString());
            myLogger.debug("Resultado es: " + res);
        } catch (SQLException exc) {
            myLogger.error("updateIncidencias", exc);
            throw new ClientesDBException(exc.getMessage());
        } catch (NamingException exc) {
            myLogger.error("updateIncidencias", exc);
            throw new ClientesException(exc.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("updateIncidencias", exc);
                throw new ClientesDBException(exc.getMessage());
            }
        }

        return res;
    }

    public PagoVO[] getPagosAReprocesarCont(int reprocesar, String fecha) throws ClientesException {

        // reprocesar = 1:  los posibles a reprocesar
        /*String query = "SELECT IP_ID_INCIDENCIA,IP_FECHA_MOV AS FECHA_MOV,IP_TIPO_INCIDENCIA,IP_OBSERVACIONES,"
                + "MAX(IP_FECHA_HORA) AS IP_FECHA_HORA,IP_NUEVA_REFERENCIA,IP_REFERENCIA,IP_NUEVA_REFERENCIA,IP_BANCO_REFERENCIA,IP_REPROCESAR,"
                + "IP_USUARIO_REPROCESO,IP_COMENTARIOS,IP_MONTO,COUNT(IP_REFERENCIA) AS REPETIDOS "
                + "FROM D_INCIDENCIAS_PAGOS_REF "
                + "WHERE IP_REPROCESAR= ? AND IP_FECHA_MOV= ? "
                + "GROUP BY IP_REFERENCIA,IP_MONTO,IP_FECHA_MOV ORDER BY IP_REFERENCIA,IP_MONTO,IP_FECHA_MOV";*/
        String query = "SELECT * "
                + "FROM D_INCIDENCIAS_PAGOS_REF "
                + "WHERE IP_REPROCESAR= ? AND IP_FECHA_MOV between ?  and ?";

        Connection cn = null;
        ArrayList<PagoVO> array = new ArrayList<PagoVO>();
        PagoVO temporal = null;
        PagoVO elementos[] = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, reprocesar);
            ps.setString(2, fecha);
            myLogger.debug("Ejecutando : " + query);
            ResultSet rs = ps.executeQuery();
            myLogger.debug("Parametros : " + reprocesar + "  : " + fecha);
            while (rs.next()) {
                temporal = new PagoVO();
                temporal.idIncidencia = rs.getInt("IP_ID_INCIDENCIA");
                temporal.fechaPago = rs.getDate("IP_FECHA_MOV");
                temporal.tipo = rs.getString("IP_TIPO_INCIDENCIA");
                temporal.observaciones = rs.getString("IP_OBSERVACIONES");
                temporal.fechaHora = rs.getTimestamp("IP_FECHA_HORA");
                temporal.referencia = rs.getString("IP_REFERENCIA");
                temporal.nuevareferencia = rs.getString("IP_NUEVA_REFERENCIA");
                temporal.bancoReferencia = rs.getInt("IP_BANCO_REFERENCIA");
                temporal.reprocesar = rs.getInt("IP_REPROCESAR");
                temporal.usuarioReproceso = rs.getString("IP_USUARIO_REPROCESO");
                temporal.comentarios = rs.getString("IP_COMENTARIOS");
                temporal.monto = rs.getDouble("IP_MONTO");
                temporal.numTransaccion = rs.getInt("ip_num_transaccion");
                //temporal.numRepetido = rs.getInt("REPETIDOS");
                array.add(temporal);
            }
            if (array.size() > 0) {
                elementos = new PagoVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (PagoVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getPagosAReprocesarCont", sqle);
            throw new ClientesDBException(sqle.getMessage());

        } catch (Exception e) {
            myLogger.error("getPagosAReprocesarCont", e);
            throw new ClientesException(e.getMessage());

        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException e) {
                myLogger.error("getPagosAReprocesarCont", e);
                throw new ClientesDBException(e.getMessage());
            }
        }
        return elementos;

    }

        public PagoVO[] getPagosAReprocesarCont(int reprocesar, String fecha, String fechaFin) throws ClientesException {

        // reprocesar = 1:  los posibles a reprocesar
        /*String query = "SELECT IP_ID_INCIDENCIA,IP_FECHA_MOV AS FECHA_MOV,IP_TIPO_INCIDENCIA,IP_OBSERVACIONES,"
                + "MAX(IP_FECHA_HORA) AS IP_FECHA_HORA,IP_NUEVA_REFERENCIA,IP_REFERENCIA,IP_NUEVA_REFERENCIA,IP_BANCO_REFERENCIA,IP_REPROCESAR,"
                + "IP_USUARIO_REPROCESO,IP_COMENTARIOS,IP_MONTO,COUNT(IP_REFERENCIA) AS REPETIDOS "
                + "FROM D_INCIDENCIAS_PAGOS_REF "
                + "WHERE IP_REPROCESAR= ? AND IP_FECHA_MOV between ?  and ?"
                + "GROUP BY IP_REFERENCIA,IP_MONTO,IP_FECHA_MOV,IP_BANCO_REFERENCIA ORDER BY IP_REFERENCIA,IP_MONTO,IP_FECHA_MOV";*/
        String query = "SELECT * "
                + "FROM D_INCIDENCIAS_PAGOS_REF "
                + "WHERE IP_REPROCESAR= ? AND IP_FECHA_MOV between ?  and ?";
        Connection cn = null;
        ArrayList<PagoVO> array = new ArrayList<PagoVO>();
        PagoVO temporal = null;
        PagoVO elementos[] = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, reprocesar);
            ps.setString(2, fecha);
            ps.setString(3, fechaFin);
            myLogger.debug("Ejecutando : " + query);
            ResultSet rs = ps.executeQuery();
            myLogger.debug("Parametros : " + reprocesar + "  : " + fecha);
            while (rs.next()) {
                temporal = new PagoVO();
                temporal.idIncidencia = rs.getInt("IP_ID_INCIDENCIA");
                temporal.fechaPago = rs.getDate("IP_FECHA_MOV");
                temporal.tipo = rs.getString("IP_TIPO_INCIDENCIA");
                temporal.observaciones = rs.getString("IP_OBSERVACIONES");
                temporal.fechaHora = rs.getTimestamp("IP_FECHA_HORA");
                temporal.referencia = rs.getString("IP_REFERENCIA");
                temporal.nuevareferencia = rs.getString("IP_NUEVA_REFERENCIA");
                temporal.bancoReferencia = rs.getInt("IP_BANCO_REFERENCIA");
                temporal.reprocesar = rs.getInt("IP_REPROCESAR");
                temporal.usuarioReproceso = rs.getString("IP_USUARIO_REPROCESO");
                temporal.comentarios = rs.getString("IP_COMENTARIOS");
                temporal.monto = rs.getDouble("IP_MONTO");
                temporal.numTransaccion = rs.getInt("ip_num_transaccion");
                //temporal.numRepetido = rs.getInt("REPETIDOS");
                array.add(temporal);
            }
            if (array.size() > 0) {
                elementos = new PagoVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (PagoVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getPagosAReprocesarCont", sqle);
            throw new ClientesDBException(sqle.getMessage());

        } catch (Exception e) {
            myLogger.error("getPagosAReprocesarCont", e);
            throw new ClientesException(e.getMessage());

        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException e) {
                myLogger.error("getPagosAReprocesarCont", e);
                throw new ClientesDBException(e.getMessage());
            }
        }
        return elementos;

    }

    public ArrayList<PagoVO> getPagosReferencia(String referencia) throws ClientesException {
        String query = "SELECT * FROM d_pagos_cartera WHERE pc_referencia=?";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<PagoVO> pagos = new ArrayList<PagoVO>();
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setString(1, referencia);
            myLogger.debug("Ejecutando: " + ps.toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                PagoVO pago = new PagoVO(rs.getString("pc_referencia"), rs.getDouble("pc_monto"), rs.getDate("pc_fecha_pago"), rs.getTimestamp("pc_fecha_hora"),
                        rs.getInt("pc_enviado"), rs.getInt("pc_status"), rs.getInt("pc_banco_referencia"), rs.getString("pc_sucursal"), rs.getInt("pc_numcuenta"));
                pagos.add(pago);
            }
        } catch (SQLException sqle) {
            myLogger.error("getPagosReferencia", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getPagosReferencia", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return pagos;
    }

    public void eliminaIncidencia(PagoVO referencia) throws ClientesException {
        String query = "UPDATE d_incidencias_pagos_ref SET ip_reprocesar = 3, ip_comentarios=?, ip_usuario_reproceso=? WHERE ip_id_incidencia = ?";
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, referencia.comentarios);
            ps.setString(2, referencia.usuarioReproceso);
            ps.setInt(3, referencia.idIncidencia);
            myLogger.debug("Ejecutando : " + query);
            ps.executeUpdate();

        } catch (SQLException sqle) {
            myLogger.error("eliminaIncidencia", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("eliminaIncidencia", e);
            throw new ClientesException(e.getMessage());
        } finally {
        }
        try {
            if (cn != null) {
                cn.close();
            }
        } catch (SQLException sqle) {
            throw new ClientesDBException(sqle.getMessage());
        }
    }

    public void eliminaIncidenciaCiclo(String referencia) throws ClientesException {
        String query = "DELETE FROM d_incidencias_pagos_ref WHERE ip_referencia=?;";
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setString(1, referencia);
            myLogger.debug("Ejecutando : " + ps.toString());
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("eliminaIncidenciaCiclo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("eliminaIncidenciaCiclo", e);
            throw new ClientesException(e.getMessage());
        } finally {
        }
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

    public void insertaIncidenciasCiclo(ArrayList<PagoVO> incidencias) throws ClientesException {
        String query = "INSERT INTO d_incidencias_pagos_ref (ip_fecha_mov, ip_tipo_incidencia, ip_observaciones, ip_fecha_hora, ip_referencia, ip_nueva_referencia, "
                + "ip_banco_referencia, ip_reprocesar, ip_usuario_reproceso, ip_comentarios, ip_monto, ip_numcuenta) "
                + "VALUES(?,?,?,CURRENT_TIMESTAMP,?,?,?,?,?,?,?,?)";
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            for (PagoVO incidencia : incidencias) {
                ps.setDate(1, incidencia.getFechaPago());
                ps.setString(2, incidencia.getTipo());
                ps.setString(3, incidencia.getObservaciones());
                ps.setString(4, incidencia.getReferencia());
                ps.setString(5, incidencia.getNuevareferencia());
                ps.setInt(6, incidencia.getBancoReferencia());
                ps.setInt(7, incidencia.getReprocesar());
                ps.setString(8, incidencia.getUsuarioReproceso());
                ps.setString(9, incidencia.getComentarios());
                ps.setDouble(10, incidencia.getMonto());
                ps.setInt(11, incidencia.getNumCuenta());
                myLogger.debug("Ejecutando : " + ps.toString());
                ps.executeUpdate();
            }
        } catch (SQLException sqle) {
            myLogger.error("insertaIncidenciasCiclo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("insertaIncidenciasCiclo", e);
            throw new ClientesException(e.getMessage());
        } finally {
        }
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

    public int getIncidencias(PagoVO pago) throws ClientesException {

        Connection con = null;
        ResultSet res = null;
        PreparedStatement ps = null;
        int resultado = 0;
        String query = "SELECT * FROM d_incidencias_pagos_ref WHERE ip_id_incidencia=? AND ip_referencia=? AND ip_monto=? AND ip_fecha_mov=? AND ip_banco_referencia=?";
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, pago.getNumRegistro());
            ps.setString(2, pago.getReferencia());
            ps.setDouble(3, pago.getMonto());
            ps.setDate(4, pago.getFechaPago());
            ps.setInt(5, pago.getBancoReferencia());
            myLogger.debug("Ejecutando " + ps);
            res = ps.executeQuery();
            if (res.next()) {
                resultado = 1;
            }
        } catch (SQLException sqle) {
            myLogger.error("getIncidencias", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getIncidencias", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (res != null) {
                    res.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return resultado;
    }
}
