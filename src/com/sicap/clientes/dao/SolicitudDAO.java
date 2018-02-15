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
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.SolicitudVO;
import org.apache.log4j.Logger;

public class SolicitudDAO extends DAOMaster {

    private static Logger myLogger = Logger.getLogger(SolicitudDAO.class);

    //Obtiene las solicitudes para un ejecutivo excepto aquellas que estón rechazadas
    public SolicitudVO[] getSolicitudesByEjecutivo(int idEjecutivo) throws ClientesException {

        String query = "SELECT * FROM D_SOLICITUDES WHERE SO_NUMEJECUTIVO = ? AND SO_ESTATUS <> ?";
        Connection cn = null;
        SolicitudVO solicitud = null;
        ArrayList<SolicitudVO> array = new ArrayList<SolicitudVO>();
        SolicitudVO elementos[] = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idEjecutivo);
            ps.setInt(2, ClientesConstants.SOLICITUD_RECHAZADA);  //Listar todos menos los rechazados
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                solicitud = new SolicitudVO();
                solicitud.idCliente = rs.getInt("SO_NUMCLIENTE");
                solicitud.idSolicitud = rs.getInt("SO_NUMSOLICITUD");
                solicitud.idCreditoIBS = rs.getInt("SO_NUMCREDITO_IBS");
                solicitud.idCuentaIBS = rs.getInt("SO_NUMCUENTA_IBS");
                solicitud.estatus = rs.getInt("SO_ESTATUS");
                solicitud.cveSolicitud = rs.getString("SO_CVESOLICITUD");
                solicitud.idSucursal = rs.getInt("SO_NUMSUCURSAL");
                solicitud.tipoOperacion = rs.getInt("SO_NUMOPERACION");
                solicitud.fechaFirma = rs.getDate("SO_FECHA_FIRMA");
                solicitud.fechaCaptura = rs.getTimestamp("SO_FECHA_CAPTURA");
                solicitud.medio = rs.getInt("SO_NUMMEDIO");
                solicitud.idEjecutivo = rs.getInt("SO_NUMEJECUTIVO");
                solicitud.fuente = rs.getInt("SO_FUENTE");
                solicitud.montoSolicitado = rs.getDouble("SO_MONTO_SOLICITADO");
                solicitud.plazoSolicitado = rs.getInt("SO_PLAZO_SOLICITADO");
                solicitud.frecuenciaPagoSolicitada = rs.getInt("SO_FRECPAGO_SOLICITADA");
                solicitud.destinoCredito = rs.getInt("SO_DESTINO_CREDITO");
                solicitud.montoPropuesto = rs.getDouble("SO_MONTO_PROPUESTO");
                solicitud.plazoPropuesto = rs.getInt("SO_PLAZO_PROPUESTO");
                solicitud.frecuenciaPagoPropuesta = rs.getInt("SO_FRECPAGO_PROPUESTA");
                solicitud.cuota = rs.getDouble("SO_CUOTA");
                solicitud.desembolsado = rs.getInt("SO_DESEMBOLSADO");
                solicitud.fechaDesembolso = rs.getDate("SO_FECHA_DESEMBOLSO");
                solicitud.contrato = rs.getString("SO_NO_CONTRATO");
                solicitud.tasaCalculada = rs.getDouble("SO_TASA_CALCULADA");
                solicitud.numrepresentante = rs.getInt("SO_NUMREPRESENTANTE");
                solicitud.rolHogar = rs.getInt("SO_NUMROLHOGAR");
                solicitud.subproducto = rs.getInt("so_subproducto");
                solicitud.otroCredito = rs.getInt("so_otrocredito");
                solicitud.mejorIngreso = rs.getInt("so_mejoringreso");
                solicitud.idProgProspera = rs.getString("so_idprogprospera");
                solicitud.documentacionCompleta = rs.getInt("so_domentacion_completa");
                array.add(solicitud);
                //Logger.debug("Solicitud encontrada : "+solicitud.toString());
            }

        } catch (SQLException sqle) {
            myLogger.error("getSolicitudesByEjecutivo", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSolicitudesByEjecutivo", e);
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
        elementos = new SolicitudVO[array.size()];
        for (int i = 0; i < elementos.length; i++) {
            elementos[i] = (SolicitudVO) array.get(i);
        }
        return elementos;

    }

    public SolicitudVO getSolicitud(int idCliente, int numSolicitud) throws ClientesException {

        String query = "SELECT * FROM D_SOLICITUDES WHERE SO_NUMCLIENTE = ? AND SO_NUMSOLICITUD = ? ";
        Connection cn = null;
        SolicitudVO solicitud = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.setInt(2, numSolicitud);
//			Logger.debug("Ejecutando : "+query);
//			Logger.debug("Para cliente : "+idCliente);
//			Logger.debug("Para Solicitud: "+numSolicitud);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                solicitud = new SolicitudVO();
                solicitud.idCliente = idCliente;
                solicitud.idSolicitud = rs.getInt("SO_NUMSOLICITUD");
                solicitud.idCreditoIBS = rs.getInt("SO_NUMCREDITO_IBS");
                solicitud.idCuentaIBS = rs.getInt("SO_NUMCUENTA_IBS");
                solicitud.estatus = rs.getInt("SO_ESTATUS");
                solicitud.cveSolicitud = rs.getString("SO_CVESOLICITUD");
                solicitud.idSucursal = rs.getInt("SO_NUMSUCURSAL");
                solicitud.tipoOperacion = rs.getInt("SO_NUMOPERACION");
                solicitud.reestructura = rs.getInt("SO_REESTRUCTURA");
                solicitud.solicitudReestructura = rs.getInt("SO_SOLICITUD_REESTRUCTURA");
                solicitud.fechaFirma = rs.getDate("SO_FECHA_FIRMA");
                solicitud.fechaCaptura = rs.getTimestamp("SO_FECHA_CAPTURA");
                solicitud.medio = rs.getInt("SO_NUMMEDIO");
                solicitud.idEjecutivo = rs.getInt("SO_NUMEJECUTIVO");
                solicitud.fuente = rs.getInt("SO_FUENTE");
                solicitud.montoSolicitado = rs.getDouble("SO_MONTO_SOLICITADO");
                solicitud.plazoSolicitado = rs.getInt("SO_PLAZO_SOLICITADO");
                solicitud.frecuenciaPagoSolicitada = rs.getInt("SO_FRECPAGO_SOLICITADA");
                solicitud.destinoCredito = rs.getInt("SO_DESTINO_CREDITO");
                solicitud.montoPropuesto = rs.getDouble("SO_MONTO_PROPUESTO");
                solicitud.plazoPropuesto = rs.getInt("SO_PLAZO_PROPUESTO");
                solicitud.frecuenciaPagoPropuesta = rs.getInt("SO_FRECPAGO_PROPUESTA");
                solicitud.cuota = rs.getDouble("SO_CUOTA");
                solicitud.desembolsado = rs.getInt("SO_DESEMBOLSADO");
                solicitud.fechaDesembolso = rs.getDate("SO_FECHA_DESEMBOLSO");
                solicitud.contrato = rs.getString("SO_NO_CONTRATO");
                solicitud.tasaCalculada = rs.getDouble("SO_TASA_CALCULADA");
                solicitud.numCheque = rs.getString("SO_NUMCHEQUE");
                solicitud.numrepresentante = rs.getInt("SO_NUMREPRESENTANTE");
                solicitud.idGrupo = rs.getInt("SO_NUMGRUPO");
                solicitud.rolHogar = rs.getInt("SO_NUMROLHOGAR");
                solicitud.subproducto = rs.getInt("so_subproducto");
                solicitud.otroCredito = rs.getInt("so_otrocredito");
                solicitud.mejorIngreso = rs.getInt("so_mejoringreso");
                solicitud.idProgProspera = rs.getString("so_idprogprospera");
                solicitud.documentacionCompleta = rs.getInt("so_domentacion_completa");
//				Logger.debug("Solicitud encontrada : "+solicitud.toString());
            }

        } catch (SQLException sqle) {
            myLogger.error("getSolicitud", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSolicitud", e);
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
        return solicitud;

    }

    public SolicitudVO[] getSolicitudes(int idCliente) throws ClientesException {

        String query = "SELECT * FROM D_SOLICITUDES WHERE SO_NUMCLIENTE = ? ";
        Connection cn = null;
        SolicitudVO solicitud = null;
        ArrayList<SolicitudVO> array = new ArrayList<SolicitudVO>();
        SolicitudVO elementos[] = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            myLogger.debug("Ejecutando : " + query);
            myLogger.debug("Para cliente : " + idCliente);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                solicitud = new SolicitudVO();
                solicitud.idCliente = idCliente;
                solicitud.idSolicitud = rs.getInt("SO_NUMSOLICITUD");
                solicitud.idCreditoIBS = rs.getInt("SO_NUMCREDITO_IBS");
                solicitud.idCuentaIBS = rs.getInt("SO_NUMCUENTA_IBS");
                solicitud.estatus = rs.getInt("SO_ESTATUS");
                solicitud.cveSolicitud = rs.getString("SO_CVESOLICITUD");
                solicitud.idSucursal = rs.getInt("SO_NUMSUCURSAL");
                solicitud.tipoOperacion = rs.getInt("SO_NUMOPERACION");
                solicitud.reestructura = rs.getInt("SO_REESTRUCTURA");
                solicitud.solicitudReestructura = rs.getInt("SO_SOLICITUD_REESTRUCTURA");
                solicitud.fechaFirma = rs.getDate("SO_FECHA_FIRMA");
                solicitud.fechaCaptura = rs.getTimestamp("SO_FECHA_CAPTURA");
                solicitud.medio = rs.getInt("SO_NUMMEDIO");
                solicitud.idEjecutivo = rs.getInt("SO_NUMEJECUTIVO");
                solicitud.fuente = rs.getInt("SO_FUENTE");
                solicitud.montoSolicitado = rs.getDouble("SO_MONTO_SOLICITADO");
                solicitud.plazoSolicitado = rs.getInt("SO_PLAZO_SOLICITADO");
                solicitud.frecuenciaPagoSolicitada = rs.getInt("SO_FRECPAGO_SOLICITADA");
                solicitud.destinoCredito = rs.getInt("SO_DESTINO_CREDITO");
                solicitud.montoPropuesto = rs.getDouble("SO_MONTO_PROPUESTO");
                solicitud.plazoPropuesto = rs.getInt("SO_PLAZO_PROPUESTO");
                solicitud.frecuenciaPagoPropuesta = rs.getInt("SO_FRECPAGO_PROPUESTA");
                solicitud.cuota = rs.getDouble("SO_CUOTA");
                solicitud.desembolsado = rs.getInt("SO_DESEMBOLSADO");
                solicitud.fechaDesembolso = rs.getDate("SO_FECHA_DESEMBOLSO");
                solicitud.contrato = rs.getString("SO_NO_CONTRATO");
                solicitud.tasaCalculada = rs.getDouble("SO_TASA_CALCULADA");
                solicitud.numCheque = rs.getString("SO_NUMCHEQUE");
                solicitud.CAT = rs.getString("SO_CAT");
                solicitud.comentarios = rs.getString("SO_COMENTARIOS");
                solicitud.numrepresentante = rs.getInt("SO_NUMREPRESENTANTE");
                solicitud.consultaCC = rs.getInt("so_consulcc");
                solicitud.rolHogar = rs.getInt("SO_NUMROLHOGAR");
                solicitud.subproducto = rs.getInt("so_subproducto");
                solicitud.otroCredito = rs.getInt("so_otrocredito");
                solicitud.mejorIngreso = rs.getInt("so_mejoringreso");
                solicitud.idProgProspera = rs.getString("so_idprogprospera");
                solicitud.documentacionCompleta = rs.getInt("so_domentacion_completa");
                array.add(solicitud);
                myLogger.debug("Solicitud encontrada : " + solicitud.toString());
            }
        } catch (SQLException sqle) {
            myLogger.error("getSolicitudes", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSolicitudes", e);
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
        elementos = new SolicitudVO[array.size()];
        for (int i = 0; i < elementos.length; i++) {
            elementos[i] = (SolicitudVO) array.get(i);
        }
        return elementos;

    }

    public int addSolicitud(int idCliente, SolicitudVO solicitud) throws ClientesException {

        return addSolicitud(null, idCliente, solicitud);
    }

    public int addSolicitud(Connection conn, int idCliente, SolicitudVO solicitud) throws ClientesException {

        String query = "INSERT INTO D_SOLICITUDES (SO_NUMCLIENTE, SO_NUMSOLICITUD, SO_ESTATUS, SO_CVESOLICITUD, "
                + "SO_NUMSUCURSAL, SO_NUMOPERACION,SO_REESTRUCTURA,SO_SOLICITUD_REESTRUCTURA, SO_FECHA_FIRMA, SO_FECHA_CAPTURA, SO_NUMMEDIO, "
                + "SO_NUMEJECUTIVO, SO_FUENTE, SO_MONTO_SOLICITADO, SO_PLAZO_SOLICITADO, "
                + "SO_FRECPAGO_SOLICITADA, SO_DESTINO_CREDITO, SO_MONTO_PROPUESTO, SO_PLAZO_PROPUESTO, "
                + "SO_FRECPAGO_PROPUESTA, SO_CUOTA, SO_DESEMBOLSADO, SO_FECHA_DESEMBOLSO, SO_NO_CONTRATO, SO_NUMCHEQUE, SO_NUMREPRESENTANTE, "
                + "SO_COMENTARIOS,SO_ORIGENMIGRACION, SO_NUMROLHOGAR, so_subproducto,so_otrocredito,so_mejoringreso,so_idprogprospera, so_domentacion_completa) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,"
                + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        int next = getNext(idCliente);
        solicitud.idSolicitud = next;
        try {
            if (conn == null) {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            } else {
                ps = conn.prepareStatement(query);
            }
			//cn = getConnection();
            //PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(param++, idCliente);
            ps.setInt(param++, solicitud.idSolicitud);
            ps.setInt(param++, solicitud.estatus);
            ps.setString(param++, solicitud.cveSolicitud);
            ps.setInt(param++, solicitud.idSucursal);
            ps.setInt(param++, solicitud.tipoOperacion);
            ps.setInt(param++, solicitud.reestructura);
            ps.setInt(param++, solicitud.solicitudReestructura);
            ps.setDate(param++, solicitud.fechaFirma);
            ps.setTimestamp(param++, solicitud.fechaCaptura);
            ps.setInt(param++, solicitud.medio);
            ps.setInt(param++, solicitud.idEjecutivo);
            ps.setInt(param++, solicitud.fuente);
            ps.setDouble(param++, solicitud.montoSolicitado);
            ps.setInt(param++, solicitud.plazoSolicitado);
            ps.setInt(param++, solicitud.frecuenciaPagoSolicitada);
            ps.setInt(param++, solicitud.destinoCredito);
            ps.setDouble(param++, solicitud.montoPropuesto);
            ps.setInt(param++, solicitud.plazoPropuesto);
            ps.setInt(param++, solicitud.frecuenciaPagoPropuesta);
            ps.setDouble(param++, solicitud.cuota);
            ps.setInt(param++, solicitud.desembolsado);
            ps.setDate(param++, solicitud.fechaDesembolso);
            ps.setString(param++, solicitud.contrato);
            ps.setString(param++, solicitud.numCheque);
            ps.setInt(param++, solicitud.numrepresentante);
            ps.setString(param++, solicitud.comentarios);
            ps.setInt(param++, solicitud.origenMigracion);
            ps.setInt(param++, solicitud.rolHogar);
            ps.setInt(param++,solicitud.subproducto);
            ps.setInt(param++,solicitud.otroCredito);
            ps.setInt(param++,solicitud.mejorIngreso);
            ps.setString(param++,solicitud.idProgProspera);
            if(solicitud.idSolicitud == 1){
                solicitud.documentacionCompleta=1;
            }
            ps.setInt(param++,solicitud.documentacionCompleta);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Solicitud = " + solicitud.toString());
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("addSolicitud", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addSolicitud", e);
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
        return next;
    }

    public int updateSolicitud(int idCliente, SolicitudVO solicitud) throws ClientesException {
        return updateSolicitud(null, idCliente, solicitud);
    }

    public int updateSolicitud(Connection conn, int idCliente, SolicitudVO solicitud) throws ClientesException {

        int res = 0;
        String query = "UPDATE D_SOLICITUDES SET SO_NUMCREDITO_IBS = ?, SO_NUMCUENTA_IBS = ?, SO_ESTATUS=?, SO_CVESOLICITUD=?, SO_NUMSUCURSAL=?, "
                + "SO_NUMOPERACION=?, SO_REESTRUCTURA=?, SO_FECHA_FIRMA=?, SO_NUMMEDIO=?, "
                + "SO_NUMEJECUTIVO=?, SO_FUENTE=?, SO_MONTO_SOLICITADO=?, SO_PLAZO_SOLICITADO=?, "
                + "SO_FRECPAGO_SOLICITADA=?, SO_DESTINO_CREDITO=?, SO_MONTO_PROPUESTO=?, "
                + "SO_PLAZO_PROPUESTO=?, SO_FRECPAGO_PROPUESTA=?, SO_CUOTA=?, SO_DESEMBOLSADO=?, SO_FECHA_DESEMBOLSO=?, "
                + "SO_NO_CONTRATO=?, SO_TASA_CALCULADA=?, SO_NUMCHEQUE=?, SO_CAT=?, SO_NUMREPRESENTANTE=?, SO_COMENTARIOS=?, SO_NUMGRUPO=?, so_consulcc=?, SO_NUMROLHOGAR = ?, "
                + "so_subproducto=?, so_otrocredito=?, so_mejoringreso=?, so_idprogprospera=? , so_domentacion_completa =?"
                + " WHERE SO_NUMCLIENTE = ? AND SO_NUMSOLICITUD = ?";
        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        try {
            if (conn == null) {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            } else {
                ps = conn.prepareStatement(query);
            }
			//cn = getConnection();
            //PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(param++, solicitud.idCreditoIBS);
            ps.setInt(param++, solicitud.idCuentaIBS);
            ps.setInt(param++, solicitud.estatus);
            ps.setString(param++, solicitud.cveSolicitud);
            ps.setInt(param++, solicitud.idSucursal);
            ps.setInt(param++, solicitud.tipoOperacion);
            ps.setInt(param++, solicitud.reestructura);
            ps.setDate(param++, solicitud.fechaFirma);
            ps.setInt(param++, solicitud.medio);
            ps.setInt(param++, solicitud.idEjecutivo);
            ps.setInt(param++, solicitud.fuente);
            ps.setDouble(param++, solicitud.montoSolicitado);
            ps.setInt(param++, solicitud.plazoSolicitado);
            ps.setInt(param++, solicitud.frecuenciaPagoSolicitada);
            ps.setInt(param++, solicitud.destinoCredito);
            ps.setDouble(param++, solicitud.montoPropuesto);
            ps.setInt(param++, solicitud.plazoPropuesto);
            ps.setInt(param++, solicitud.frecuenciaPagoPropuesta);
            ps.setDouble(param++, solicitud.cuota);
            ps.setInt(param++, solicitud.desembolsado);
            ps.setDate(param++, solicitud.fechaDesembolso);
            ps.setString(param++, solicitud.contrato);
            ps.setDouble(param++, solicitud.tasaCalculada);
            ps.setString(param++, solicitud.numCheque);
            ps.setString(param++, solicitud.CAT);
            ps.setInt(param++, solicitud.numrepresentante);
            ps.setString(param++, solicitud.comentarios);
            ps.setInt(param++, solicitud.idGrupo);
            ps.setInt(param++, solicitud.consultaCC);
            ps.setInt(param++, solicitud.rolHogar);
            ps.setInt(param++, solicitud.subproducto);
            ps.setInt(param++, solicitud.otroCredito);
            ps.setInt(param++, solicitud.mejorIngreso);
            ps.setString(param++, solicitud.idProgProspera);
            ps.setInt(param++, solicitud.documentacionCompleta);
            ps.setInt(param++, idCliente);
            ps.setInt(param++, solicitud.idSolicitud);
            myLogger.debug("Ejecutando = " + ps);
            myLogger.debug("Solicitud = " + solicitud.toString());
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("updateSolicitud", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateSolicitud", e);
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
    public int updateLimpiaSolicitud(Connection conn, int idCliente, int idSolicitud, SolicitudVO solicitudVO) throws ClientesException {

        int res = 0;
        String query = "update clientes_cec.d_solicitudes set  so_numcredito_ibs = ? , so_numcuenta_ibs = ?," +
                       " so_fecha_firma = ? , so_nummedio = ?, so_numejecutivo = ?, so_fuente = ?, so_monto_solicitado = ?," +
                       " so_plazo_solicitado = ?, so_frecpago_solicitada = ?, so_destino_credito = ?, so_desembolsado = ?, so_fecha_desembolso = ?," +
                       " so_tasa_calculada = ?, so_numcheque = ?, so_numgrupo = ?, so_numrolhogar = ?, so_otrocredito = ?, so_mejoringreso = ?," +
                       " so_idprogprospera = ?,so_domentacion_completa = ?, so_estatus = ?" +
                       " where so_numcliente = ?" +
                       " and so_numsolicitud = ?;";
        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        try {
            if (conn == null) {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            } else {
                ps = conn.prepareStatement(query);
            }
            ps.setNull(param++, java.sql.Types.NULL);
            ps.setNull(param++, java.sql.Types.NULL);
            ps.setNull(param++, java.sql.Types.NULL);
            ps.setInt(param++, solicitudVO.medio);
            ps.setInt(param++, solicitudVO.idEjecutivo);
            ps.setInt(param++, solicitudVO.fuente);
            ps.setDouble(param++, solicitudVO.montoSolicitado);
            ps.setInt(param++, solicitudVO.plazoPropuesto);
            ps.setInt(param++, solicitudVO.frecuenciaPagoSolicitada);
            ps.setInt(param++, solicitudVO.destinoCredito);
            ps.setInt(param++, solicitudVO.desembolsado);
            ps.setNull(param++, java.sql.Types.NULL);
            ps.setNull(param++, java.sql.Types.NULL);
            ps.setNull(param++, java.sql.Types.NULL);
            ps.setInt(param++, solicitudVO.idGrupo);
            ps.setInt(param++, solicitudVO.rolHogar);
            ps.setInt(param++, solicitudVO.otroCredito);
            ps.setInt(param++, solicitudVO.mejorIngreso);
            ps.setString(param++, solicitudVO.idProgProspera);
            ps.setInt(param++, solicitudVO.documentacionCompleta);
            ps.setInt(param++, solicitudVO.estatus);
            ps.setInt(param++, idCliente);
            ps.setInt(param++, idSolicitud);
            
            myLogger.debug("Ejecutando = " + ps);
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("updateLimpiaSolicitud", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateLimpiaSolicitud", e);
            throw new ClientesException(e.getMessage());
        }
        return res;
    }

    public int updateSolicitudCredito(SolicitudVO solicitud) throws ClientesException {

        String query = "UPDATE D_SOLICITUDES SET SO_NUMCREDITO_IBS = ?, SO_NUMCUENTA_IBS = ?, SO_TASA_CALCULADA = ? "
                + "WHERE SO_NUMCLIENTE = ? AND SO_NUMSOLICITUD = ?";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(param++, solicitud.idCreditoIBS);
            ps.setInt(param++, solicitud.idCuentaIBS);
            ps.setDouble(param++, solicitud.tasaCalculada);
            ps.setInt(param++, solicitud.idCliente);
            ps.setInt(param++, solicitud.idSolicitud);
            res = ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("updateSolicitudCredito", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateSolicitudCredito", e);
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

    public int updateChequeSolicitud(Connection conn, int idCliente, int idSolicitud, String chequeNuevo) throws ClientesException {

        int res = 0;
        String query = "UPDATE D_SOLICITUDES SET SO_NUMCHEQUE = ? WHERE SO_NUMCLIENTE = ? AND SO_NUMSOLICITUD = ?";
        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        try {
            if (conn == null) {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            } else {
                ps = conn.prepareStatement(query);
            }
			//cn = getConnection();
            //PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(param++, chequeNuevo);
            ps.setInt(param++, idCliente);
            ps.setInt(param++, idSolicitud);
            myLogger.debug("Ejecutando = " + query);
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("updateChequeSolicitud", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateChequeSolicitud", e);
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

    public void updateGrupoSolicitud(int idGrupo, int idCliente, int idSolicitud) throws ClientesException {

        String query = "UPDATE d_solicitudes SET so_numgrupo = ? WHERE so_numcliente = ? AND so_numsolicitud = ?";
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, idGrupo);
            ps.setInt(2, idCliente);
            ps.setInt(3, idSolicitud);
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("updateGrupoSolicitud", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateGrupoSolicitud", e);
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

    public void updateSolicitudCiclo(int desembolsado, int idEquipo, int idCiclo) throws ClientesException {

        String query = "UPDATE d_integrantes_ciclo "
                + "LEFT JOIN d_solicitudes ON(ic_numcliente=so_numcliente AND ic_numsolicitud=so_numsolicitud) "
                + "SET so_desembolsado=? WHERE ic_numgrupo=? AND ic_numciclo=? ";
        Connection cn = null;
        PreparedStatement ps = null;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, desembolsado);
            ps.setInt(2, idEquipo);
            ps.setInt(3, idCiclo);
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

    public String getCheque(int idCliente, int idSolicitud) throws ClientesException {

        String query = "SELECT SO_NUMCHEQUE FROM D_SOLICITUDES WHERE SO_NUMCLIENTE = ? AND SO_NUMSOLICITUD = ?";
        Connection cn = null;
        String numCheque = "";

        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.setInt(2, idSolicitud);
            myLogger.debug("Ejecutando : " + query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                numCheque = rs.getString("SO_NUMCHEQUE");
                myLogger.debug("Cheque encontrado");
            }

        } catch (SQLException sqle) {
            myLogger.error("getCheque", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCheque", e);
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
        return numCheque;

    }

    public int getNext(int idCliente) throws ClientesException {

        String query = "SELECT COALESCE(MAX(SO_NUMSOLICITUD),0)+1 AS NEXT FROM D_SOLICITUDES WHERE SO_NUMCLIENTE = ?";
        Connection cn = null;
        int next = 1;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
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

    public boolean getEstatusSolicitud(int idCliente) throws ClientesException {

        String query = "SELECT so_desembolsado FROM d_solicitudes WHERE so_numcliente = ?";
        Connection cn = null;
        boolean respuesta = true;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();
            myLogger.debug("Ejecutando = " + query);
            while (rs.next()) {
                if (rs.getInt("so_desembolsado") != 2 && rs.getInt("so_desembolsado") != 3) {
                    respuesta = false;
                    break;
                }
            }

        } catch (SQLException sqle) {
            myLogger.error("getEstatusSolicitud", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getEstatusSolicitud", e);
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

    public SolicitudVO[] getSolicitudBySucursalEstatusFechaCaptura(int sucursal, int estatus, Date fechaInicio, Date fechaFin) throws ClientesException {
        Connection cn = null;
        SolicitudVO solicitud = null;
        ArrayList<SolicitudVO> array = new ArrayList<SolicitudVO>();
        SolicitudVO elementos[] = null;
        String query = "SELECT SO_NUMCLIENTE, SO_NUMSOLICITUD, SO_NUMCREDITO_IBS, SO_NUMCUENTA_IBS, SO_ESTATUS, SO_CVESOLICITUD, SO_NUMSUCURSAL, SO_NUMOPERACION, SO_FECHA_FIRMA, SO_FECHA_CAPTURA,";
        query += " SO_NUMMEDIO, SO_NUMEJECUTIVO, SO_FUENTE, SO_MONTO_SOLICITADO, SO_PLAZO_SOLICITADO, SO_FRECPAGO_SOLICITADA, SO_DESTINO_CREDITO,";
        query += " SO_MONTO_PROPUESTO, SO_PLAZO_PROPUESTO, SO_FRECPAGO_PROPUESTA, SO_CUOTA, SO_DESEMBOLSADO,";
        query += " SO_FECHA_DESEMBOLSO, SO_NO_CONTRATO, SO_TASA_CALCULADA, SO_NUMCHEQUE, SO_NUMREPRESENTANTE, SO_COMENTARIOS ";
        query += " FROM D_SOLICITUDES, D_CLIENTES";
        query += " WHERE EN_NUMCLIENTE = SO_NUMCLIENTE ";
        query += " AND EN_NUMSUCURSAL = ? ";
        if (estatus != 0) {
            query += " AND SO_ESTATUS = ? ";
        }
        if (fechaInicio != null && fechaFin != null) {
            query += " AND  DATE(SO_FECHA_CAPTURA) BETWEEN ? AND ? ";
        }
        query += "GROUP BY SO_NUMCLIENTE ORDER BY SO_NUMCLIENTE";
//		Logger.debug("Ejecutando::" + query);
        try {
            int param = 1;
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(param++, sucursal);
            if (estatus != 0) {
                ps.setInt(param++, estatus);
            }
            if (fechaInicio != null && fechaFin != null) {
                ps.setDate(param++, fechaInicio);
                ps.setDate(param++, fechaFin);
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                solicitud = new SolicitudVO();
                solicitud.idCliente = rs.getInt("SO_NUMCLIENTE");
                solicitud.idSolicitud = rs.getInt("SO_NUMSOLICITUD");
                solicitud.idCreditoIBS = rs.getInt("SO_NUMCREDITO_IBS");
                solicitud.idCuentaIBS = rs.getInt("SO_NUMCUENTA_IBS");
                solicitud.estatus = rs.getInt("SO_ESTATUS");
                solicitud.cveSolicitud = rs.getString("SO_CVESOLICITUD");
                solicitud.idSucursal = rs.getInt("SO_NUMSUCURSAL");
                solicitud.tipoOperacion = rs.getInt("SO_NUMOPERACION");
                solicitud.fechaFirma = rs.getDate("SO_FECHA_FIRMA");
                solicitud.fechaCaptura = rs.getTimestamp("SO_FECHA_CAPTURA");
                solicitud.medio = rs.getInt("SO_NUMMEDIO");
                solicitud.idEjecutivo = rs.getInt("SO_NUMEJECUTIVO");
                solicitud.fuente = rs.getInt("SO_FUENTE");
                solicitud.montoSolicitado = rs.getDouble("SO_MONTO_SOLICITADO");
                solicitud.plazoSolicitado = rs.getInt("SO_PLAZO_SOLICITADO");
                solicitud.frecuenciaPagoSolicitada = rs.getInt("SO_FRECPAGO_SOLICITADA");
                solicitud.destinoCredito = rs.getInt("SO_DESTINO_CREDITO");
                solicitud.montoPropuesto = rs.getDouble("SO_MONTO_PROPUESTO");
                solicitud.plazoPropuesto = rs.getInt("SO_PLAZO_PROPUESTO");
                solicitud.frecuenciaPagoPropuesta = rs.getInt("SO_FRECPAGO_PROPUESTA");
                solicitud.cuota = rs.getDouble("SO_CUOTA");
                solicitud.desembolsado = rs.getInt("SO_DESEMBOLSADO");
                solicitud.fechaDesembolso = rs.getDate("SO_FECHA_DESEMBOLSO");
                solicitud.contrato = rs.getString("SO_NO_CONTRATO");
                solicitud.tasaCalculada = rs.getDouble("SO_TASA_CALCULADA");
                solicitud.numCheque = rs.getString("SO_NUMCHEQUE");
                solicitud.numrepresentante = rs.getInt("SO_NUMREPRESENTANTE");
                solicitud.comentarios = rs.getString("SO_COMENTARIOS");
                array.add(solicitud);
            }
            elementos = new SolicitudVO[array.size()];
            for (int i = 0; i < elementos.length; i++) {
                elementos[i] = (SolicitudVO) array.get(i);
            }
        } catch (SQLException sqle) {
            myLogger.error("getSolicitudBySucursalEstatusFechaCaptura", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSolicitudBySucursalEstatusFechaCaptura", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (cn != null) {
                    cn.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getSolicitudBySucursalEstatusFechaCaptura", exc);
            }
        }
        return elementos;
    }

    public boolean solicitudEnviada(int idCliente, int idSolicitud) throws ClientesException {
        int param = 1;
        boolean esEnvio = false;
        String query = "SELECT * FROM D_BITACORA_SYNCRONET WHERE BS_NUMCLIENTE = ? "
                + "AND BS_NUMSOLICITUD = ? "
                + "AND BS_TIPO_ENVIO = 'CTE'";
        Connection con = null;
        try {
            con = getConnection();
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(param++, idCliente);
            ps.setInt(param++, idSolicitud);
//			Logger.debug("Ejecutando:" + query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                esEnvio = true;
            }
        } catch (SQLException sqle) {
            myLogger.error("solicitudEnviada", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("solicitudEnviada", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                myLogger.error("solicitudEnviada", exc);
            }
        }
        return esEnvio;
    }

    public SolicitudVO[] getSolicitudesForIBS(Date fecha) throws ClientesException {

        String query = "SELECT * FROM D_SOLICITUDES WHERE SO_FECHA_DESEMBOLSO = ? AND SO_NUMOPERACION IN ( 1, 2 ) ";
        //23669
        Connection cn = null;
        SolicitudVO solicitud = null;
        ArrayList<SolicitudVO> array = new ArrayList<SolicitudVO>();
        SolicitudVO elementos[] = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setDate(1, fecha);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                solicitud = new SolicitudVO();
                solicitud.idCliente = rs.getInt("SO_NUMCLIENTE");
                solicitud.idSolicitud = rs.getInt("SO_NUMSOLICITUD");
                solicitud.idCreditoIBS = rs.getInt("SO_NUMCREDITO_IBS");
                solicitud.idCuentaIBS = rs.getInt("SO_NUMCUENTA_IBS");
                solicitud.estatus = rs.getInt("SO_ESTATUS");
                solicitud.cveSolicitud = rs.getString("SO_CVESOLICITUD");
                solicitud.idSucursal = rs.getInt("SO_NUMSUCURSAL");
                solicitud.tipoOperacion = rs.getInt("SO_NUMOPERACION");
                solicitud.reestructura = rs.getInt("SO_REESTRUCTURA");
                solicitud.solicitudReestructura = rs.getInt("SO_SOLICITUD_REESTRUCTURA");
                solicitud.fechaFirma = rs.getDate("SO_FECHA_FIRMA");
                solicitud.fechaCaptura = rs.getTimestamp("SO_FECHA_CAPTURA");
                solicitud.medio = rs.getInt("SO_NUMMEDIO");
                solicitud.idEjecutivo = rs.getInt("SO_NUMEJECUTIVO");
                solicitud.fuente = rs.getInt("SO_FUENTE");
                solicitud.montoSolicitado = rs.getDouble("SO_MONTO_SOLICITADO");
                solicitud.plazoSolicitado = rs.getInt("SO_PLAZO_SOLICITADO");
                solicitud.frecuenciaPagoSolicitada = rs.getInt("SO_FRECPAGO_SOLICITADA");
                solicitud.destinoCredito = rs.getInt("SO_DESTINO_CREDITO");
                solicitud.montoPropuesto = rs.getDouble("SO_MONTO_PROPUESTO");
                solicitud.plazoPropuesto = rs.getInt("SO_PLAZO_PROPUESTO");
                solicitud.frecuenciaPagoPropuesta = rs.getInt("SO_FRECPAGO_PROPUESTA");
                solicitud.cuota = rs.getDouble("SO_CUOTA");
                solicitud.desembolsado = rs.getInt("SO_DESEMBOLSADO");
                solicitud.fechaDesembolso = fecha;
                solicitud.contrato = rs.getString("SO_NO_CONTRATO");
                solicitud.tasaCalculada = rs.getDouble("SO_TASA_CALCULADA");
                solicitud.numCheque = rs.getString("SO_NUMCHEQUE");
                solicitud.CAT = rs.getString("SO_CAT");
                solicitud.comentarios = rs.getString("SO_COMENTARIOS");
                solicitud.numrepresentante = rs.getInt("SO_NUMREPRESENTANTE");
                solicitud.rolHogar = rs.getInt("SO_NUMROLHOGAR");
                solicitud.subproducto = rs.getInt("so_subproducto");
                solicitud.otroCredito = rs.getInt("so_otrocredito");
                solicitud.mejorIngreso = rs.getInt("so_mejoringreso");
                solicitud.idProgProspera = rs.getString("so_idprogprospera");
                array.add(solicitud);
                //Logger.debug("Solicitud encontrada : "+solicitud.toString());
            }
        } catch (SQLException sqle) {
            myLogger.error("getSolicitudesForIBS", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSolicitudesForIBS", e);
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
        elementos = new SolicitudVO[array.size()];
        for (int i = 0; i < elementos.length; i++) {
            elementos[i] = (SolicitudVO) array.get(i);
        }
        return elementos;

    }

    public int updateSolicitudDesembolsada(int numCliente, int numSolicitud) throws ClientesException {

        String query = "UPDATE D_SOLICITUDES SET SO_DESEMBOLSADO = 1 "
                + "WHERE SO_NUMCLIENTE = ? AND SO_NUMSOLICITUD = ?";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(param++, numCliente);
            ps.setInt(param++, numSolicitud);
            myLogger.debug("Ejecutando = " + query);
            res = ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("updateSolicitudDesembolsada", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateSolicitudDesembolsada", e);
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

    public int cancelarSolicitud(int numCliente, int numSolicitud) throws ClientesException {

        String query = "UPDATE D_SOLICITUDES SET SO_DESEMBOLSADO = 3, SO_ESTATUS = 3 "
                + "WHERE SO_NUMCLIENTE = ? AND SO_NUMSOLICITUD = ?";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(param++, numCliente);
            ps.setInt(param++, numSolicitud);
            myLogger.debug("Ejecutando = " + query);
            res = ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("cancelarSolicitud", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("cancelarSolicitud", e);
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
    
    public int getNumEquipo(int idCliente, int idSolicitud) throws ClientesException {

        String query = "SELECT so_numgrupo FROM d_solicitudes WHERE so_numcliente = ? AND so_numsolicitud = ?";
        Connection cn = null;
        int numEquipo = 0;

        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.setInt(2, idSolicitud);
            myLogger.debug("Ejecutando : " + query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                numEquipo = rs.getInt("so_numgrupo");
                myLogger.debug("Numero de equipo encontrado");
            }

        } catch (SQLException sqle) {
            myLogger.error("getNumEquipo", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getNumEquipo", e);
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
        return numEquipo;

    }

    public SolicitudVO getSolicitudSaldo(int numCliente) throws ClientesException, SQLException {

        SolicitudVO solicitud = null;
        String query = "SELECT ic_numcliente, ic_numsolicitud, ci_numgrupo, ci_numciclo, gr_nombre, ib_estatus "
                + "FROM d_integrantes_ciclo LEFT JOIN d_saldos ON (ic_numgrupo=ib_numclientesicap AND ic_numciclo=ib_numsolicitudsicap) "
                + "LEFT JOIN d_ciclos_grupales ON (ic_numgrupo=ci_numgrupo AND ic_numciclo=ci_numciclo) "
                + "LEFT JOIN d_grupos ON (ci_numgrupo=gr_numgrupo) "
                + "WHERE (ic_numcliente=? AND ib_estatus IS NULL) OR (ic_numcliente=? AND ib_estatus!=3) LIMIT 1";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet res = null;
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numCliente);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros [" + numCliente + "]");
            res = ps.executeQuery();
            if (res.next()) {
                solicitud = new SolicitudVO(res.getInt("ic_numcliente"), res.getInt("ic_numsolicitud"),
                        new SaldoIBSVO(res.getInt("ci_numgrupo"), res.getInt("ci_numciclo"), res.getString("gr_nombre"), res.getInt("ib_estatus")));
            }
        } catch (SQLException sqle) {
            con.close();
            myLogger.error("getSolicitudSaldo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSolicitudSaldo", e);
            throw new ClientesException(e.getMessage());
        }
        con.close();
        return solicitud;
    }
    
     public boolean getGrupoIC(int idGrupo) throws ClientesException {

        String query = "SELECT count(so_numcliente) cantidad" +
                        " FROM  d_solicitudes" +
                        " WHERE so_subproducto = 1" +
                        " AND so_numgrupo = ?" +
                        " AND so_estatus = 2" +
                        " AND so_desembolsado != 3" +
                        " AND so_numoperacion = 3";
        Connection cn = null;
        int CantidadIC = 0;
        boolean grupoIC = false;

        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idGrupo);            
            myLogger.debug("Ejecutando : " + query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                CantidadIC = rs.getInt("cantidad");
                myLogger.debug("Cheque encontrado");
            }
            if (CantidadIC>0)
                grupoIC = true;

        } catch (SQLException sqle) {
            myLogger.error("getCheque", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getCheque", e);
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
        return grupoIC ;

    }
    public int updateDocumentacionCompletaSolicitud(Connection conn, int idCliente, int idSolicitud, int DomentacionCompleta, Date fechaFirma) 
            throws ClientesException {
        int res = 0;
        String query = "UPDATE D_SOLICITUDES SET so_domentacion_completa = ?, so_fecha_firma = ? WHERE SO_NUMCLIENTE = ? AND SO_NUMSOLICITUD = ?";
        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        try {
            if (conn == null) {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            } else {
                ps = conn.prepareStatement(query);
            }
			//cn = getConnection();
            //PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(param++, DomentacionCompleta);
            ps.setInt(param++, idCliente);
            ps.setInt(param++, idSolicitud);
            ps.setDate(param++, fechaFirma);
            myLogger.debug("Ejecutando = " + query);
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("updateDocumentacionCompletaSolicitud", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateDocumentacionCompletaSolicitud", e);
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
    
         
     //----------- 050417 ---------------------
     
     public int updateSolicitudCapturaRapida(int numCliente, int numSolicitud) throws ClientesException {

        String query = "UPDATE D_SOLICITUDES SET SO_ESTATUS = 7 "
                + "WHERE SO_NUMCLIENTE = ? AND SO_NUMSOLICITUD = ?";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(param++, numCliente);
            ps.setInt(param++, numSolicitud);
            myLogger.debug("Ejecutando = " + query);
            res = ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("updateSolicitudCapturaRapida", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateSolicitudCapturaRapida", e);
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
     
     //--------- 050417 -------------
     
     //--------- 070417 ----------------
     public int updateSolicitudCancelada(int numCliente, int numSolicitud) throws ClientesException {

        String query = "UPDATE D_SOLICITUDES SET SO_ESTATUS = 3 "
                + "WHERE SO_NUMCLIENTE = ? AND SO_NUMSOLICITUD = ?";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(param++, numCliente);
            ps.setInt(param++, numSolicitud);
            myLogger.debug("Ejecutando = " + query);
            res = ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("updateSolicitudCapturaRapida", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateSolicitudCapturaRapida", e);
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
     
    public int updateSolicitudRenovacion(int numCliente, int numSolicitud) throws ClientesException {

        String query = "UPDATE D_SOLICITUDES SET SO_ESTATUS = 1 "
                + "WHERE SO_NUMCLIENTE = ? AND SO_NUMSOLICITUD = ?";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(param++, numCliente);
            ps.setInt(param++, numSolicitud);
            myLogger.debug("Ejecutando = " + query);
            res = ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("updateSolicitudRenovacion", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateSolicitudRenovacion", e);
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


 /**
      * JECB 01/10/2017
      * Método que obtiene el total de solicitudes desembolsadas para un cliente no mayores a 1 año de antiguedad
      * @param numCliente identificador del cliente
      * @param statusSolicitud estatus que referencia desembolso
      * @return total de solicitudes del cliente en desembolso
      * @throws ClientesException 
      */
    public int getTotalSolicitudesDesembolsadas(int numCliente, int statusSolicitud) throws ClientesException {

        String query = "SELECT count(*) total_solicitudes " +
                "FROM D_SOLICITUDES WHERE SO_NUMCLIENTE = ? and so_desembolsado=? " +
                "and so_fecha_desembolso >= DATE_SUB(NOW(), INTERVAL 4 year)" ;
        
        Connection cn = null;
        int totalSolicitudes = 0;
        

        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, numCliente);    
            ps.setInt(2, statusSolicitud);
            myLogger.debug("Ejecutando : " + query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                totalSolicitudes = rs.getInt("total_solicitudes");
                myLogger.debug("Cheque encontrado");
            }
        } catch (SQLException sqle) {
            myLogger.error("getTotalSolicitudesPorStatus", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getTotalSolicitudesPorStatus", e);
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
        return totalSolicitudes;

    }
}
