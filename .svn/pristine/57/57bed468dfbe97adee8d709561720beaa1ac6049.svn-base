package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.EventosDePagoVO;
import org.apache.log4j.Logger;

public class EventosPagosGrupalDAO extends DAOMaster {

    private static Logger myLogger = Logger.getLogger(EventosPagosGrupalDAO.class);

    public EventosDePagoVO[] getEventosPagos(int idGrupo) throws ClientesException {

        String query = " SELECT * FROM D_MONITOR_PAGOS_GRUPAL "
                + " LEFT JOIN D_ARCHIVOS_ASOCIADOS ON(AS_NUMCLIENTE=MO_NUMGRUPO AND AS_TIPO=13) "
                + " WHERE MO_NUMGRUPO = ? ";

        ArrayList<EventosDePagoVO> array = new ArrayList<EventosDePagoVO>();
        EventosDePagoVO temporal = null;
        EventosDePagoVO elementos[] = null;
        Connection cn = null;

        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idGrupo);
            myLogger.debug("Ejecutando = " + query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new EventosDePagoVO();
                temporal.identificador = rs.getInt("MO_ID");
                temporal.numGrupo = rs.getInt("MO_NUMGRUPO");
                temporal.numCiclo = rs.getInt("MO_NUMCICLO");
                temporal.numPago = rs.getInt("MO_NUMPAGO");
                temporal.numAtrasos = rs.getInt("MO_NUMERO_ATRASOS");
                temporal.enMora = rs.getString("MO_EN_MORA");
                temporal.estatusVisitaSupervisor = rs.getInt("MO_ESTATUS_VISITA_SUPERVISOR");
                temporal.fechaAlertaSupervisor = rs.getDate("MO_FECHA_ALERTA_SUPERVISOR");
                temporal.estatusVisitaGerente = rs.getInt("MO_ESTATUS_VISITA_GERENTE");
                temporal.fechaAlertaGerente = rs.getDate("MO_FECHA_ALERTA_GERENTE");
                temporal.estatusVisitaGestor = rs.getInt("MO_ESTATUS_VISITA_GESTOR");
                temporal.fechaAlertaGestor = rs.getDate("MO_FECHA_ALERTA_GESTOR");
                temporal.estatusReporteCobranza = rs.getInt("MO_ESTATUS_REPORTE_COBRANZA");
                temporal.fechaReporteCobranza = rs.getDate("MO_FECHA_REPORTE_COBRANZA");
                array.add(temporal);
                //Logger.debug("GRUPO MONITOR : "+temporal.toString());
            }
            if (array.size() > 0) {
                elementos = new EventosDePagoVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (EventosDePagoVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getEventosPagos", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getEventosPagos", e);
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

    public EventosDePagoVO[] getEventosPagos(int idGrupo, int idCiclo) throws ClientesException {

        String query = " SELECT * FROM D_MONITOR_PAGOS_GRUPAL WHERE MO_NUMGRUPO = ? AND MO_NUMCICLO=? ";

        ArrayList<EventosDePagoVO> array = new ArrayList<EventosDePagoVO>();
        EventosDePagoVO temporal = null;
        EventosDePagoVO elementos[] = null;
        Connection cn = null;

        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idGrupo);
            ps.setInt(2, idCiclo);
            myLogger.debug("Ejecutando = " + query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new EventosDePagoVO();
                temporal.identificador = rs.getInt("MO_ID");
                temporal.numGrupo = rs.getInt("MO_NUMGRUPO");
                temporal.numCiclo = rs.getInt("MO_NUMCICLO");
                temporal.numPago = rs.getInt("MO_NUMPAGO");
                temporal.numAtrasos = rs.getInt("MO_NUMERO_ATRASOS");
                temporal.enMora = rs.getString("MO_EN_MORA");
                temporal.estatusVisitaSupervisor = rs.getInt("MO_ESTATUS_VISITA_SUPERVISOR");
                temporal.fechaAlertaSupervisor = rs.getDate("MO_FECHA_ALERTA_SUPERVISOR");
                temporal.estatusVisitaGerente = rs.getInt("MO_ESTATUS_VISITA_GERENTE");
                temporal.fechaAlertaGerente = rs.getDate("MO_FECHA_ALERTA_GERENTE");
                temporal.estatusVisitaGestor = rs.getInt("MO_ESTATUS_VISITA_GESTOR");
                temporal.fechaAlertaGestor = rs.getDate("MO_FECHA_ALERTA_GESTOR");
                temporal.estatusReporteCobranza = rs.getInt("MO_ESTATUS_REPORTE_COBRANZA");
                temporal.fechaReporteCobranza = rs.getDate("MO_FECHA_REPORTE_COBRANZA");
                array.add(temporal);
                //Logger.debug("GRUPO MONITOR : "+temporal.toString());
            }
            if (array.size() > 0) {
                elementos = new EventosDePagoVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (EventosDePagoVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getEventosPagos", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getEventosPagos", e);
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

    public boolean existeEventoMonitor(int idGrupo, int idCiclo, int numPago) throws ClientesException {

        String query = " SELECT * FROM D_MONITOR_PAGOS_GRUPAL WHERE MO_NUMGRUPO = ? AND MO_NUMCICLO=? AND MO_NUMPAGO = ?";

        ArrayList<EventosDePagoVO> array = new ArrayList<EventosDePagoVO>();
        EventosDePagoVO temporal = null;
        EventosDePagoVO elementos[] = null;
        boolean existe = false;
        Connection cn = null;

        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idGrupo);
            ps.setInt(2, idCiclo);
            ps.setInt(3, numPago);
            myLogger.debug("Ejecutando = " + query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new EventosDePagoVO();
                temporal.identificador = rs.getInt("MO_ID");
                temporal.numGrupo = rs.getInt("MO_NUMGRUPO");
                temporal.numCiclo = rs.getInt("MO_NUMCICLO");
                temporal.numPago = rs.getInt("MO_NUMPAGO");
                temporal.numAtrasos = rs.getInt("MO_NUMERO_ATRASOS");
                temporal.enMora = rs.getString("MO_EN_MORA");
                temporal.estatusVisitaSupervisor = rs.getInt("MO_ESTATUS_VISITA_SUPERVISOR");
                temporal.fechaAlertaSupervisor = rs.getDate("MO_FECHA_ALERTA_SUPERVISOR");
                temporal.estatusVisitaGerente = rs.getInt("MO_ESTATUS_VISITA_GERENTE");
                temporal.fechaAlertaGerente = rs.getDate("MO_FECHA_ALERTA_GERENTE");
                temporal.estatusVisitaGestor = rs.getInt("MO_ESTATUS_VISITA_GESTOR");
                temporal.fechaAlertaGestor = rs.getDate("MO_FECHA_ALERTA_GESTOR");
                temporal.estatusReporteCobranza = rs.getInt("MO_ESTATUS_REPORTE_COBRANZA");
                temporal.fechaReporteCobranza = rs.getDate("MO_FECHA_REPORTE_COBRANZA");
                array.add(temporal);
                //Logger.debug("GRUPO MONITOR : "+temporal.toString());
            }
            if (array.size() > 0) {
                existe = true;
                elementos = new EventosDePagoVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (EventosDePagoVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("existeEventoMonitor", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("existeEventoMonitor", e);
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
        return existe;
    }

    public EventosDePagoVO[] getVencimientosDelDia() throws ClientesException {

        String query = "SELECT "
                + "CI_NUMGRUPO, CI_NUMCICLO, TA_NO_PAGO, CI_NUMCREDITO_IBS "
                + "FROM "
                + "D_CICLOS_GRUPALES, "
                + "D_TABLA_AMORTIZACION "
                + "WHERE "
                + // -- CICLO -- 
                "CI_ESTATUS=1 "
                + "AND CI_DESEMBOLSADO=3 "
                + "AND CI_ESTATUS_REVISION_MONITOR=1 "
                + // -- TABLA AMORTIZACION --
                "AND TA_ID_CLIENTE=CI_NUMGRUPO "
                + "AND TA_ID_SOLICITUD=CI_NUMCICLO "
                + "AND TA_NO_PAGO!=0 "
                + "AND TA_TPO_AMORTIZACION = 1 "
                + "AND TA_FECHA_PAGO=ADDDATE(CURDATE(),-3); ";

        ArrayList<EventosDePagoVO> array = new ArrayList<EventosDePagoVO>();
        EventosDePagoVO temporal = null;
        EventosDePagoVO elementos[] = null;
        Connection cn = null;

        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            myLogger.debug("Ejecutando = " + query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new EventosDePagoVO();
                temporal.numGrupo = rs.getInt("CI_NUMGRUPO");
                temporal.numCiclo = rs.getInt("CI_NUMCICLO");
                temporal.numPago = rs.getInt("TA_NO_PAGO");
                if (rs.getInt("CI_NUMCREDITO_IBS") > 0) {
                    temporal.esIBS = true;
                }
                array.add(temporal);
                //Logger.debug("MONITOR DE PAGOS : "+temporal.toString());
            }
            if (array.size() > 0) {
                elementos = new EventosDePagoVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (EventosDePagoVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getVencimientosDelDia", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getVencimientosDelDia", e);
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

    public EventosDePagoVO getSaldosT24(int numGrupo, int numCiclo) throws ClientesException {

        String query = "SELECT "
                + "CI_NUMGRUPO, CI_NUMCICLO, ST_SITUACION_ACTUAL_CREDITO, ST_DIAS_VENCIDOS, ST_TOTAL_EXIGIBLE "
                + "FROM "
                + "D_SALDOS_T24, "
                + "D_CICLOS_GRUPALES "
                + "WHERE "
                + // -- CICLO -- 
                "CI_ESTATUS=1 "
                + "AND CI_NUMGRUPO = ? "
                + "AND CI_NUMCICLO = ? "
                + "AND CI_DESEMBOLSADO=3 "
                + "AND CI_ESTATUS_REVISION_MONITOR=1 "
                + // -- SALDOS --
                "AND ST_NUMOPERACION IN(3,5) "
                + "AND ST_NUMCLIENTE=CI_NUMGRUPO "
                + "AND ST_CICLO=CI_NUMCICLO "
                + "AND ST_SITUACION_ACTUAL_CREDITO IN(1,2) "
                + "GROUP BY CI_NUMGRUPO,CI_NUMCICLO ";

        EventosDePagoVO temporal = null;
        Connection cn = null;

        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, numGrupo);
            ps.setInt(2, numCiclo);
            //Logger.debug("Ejecutando = "+query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new EventosDePagoVO();
                temporal.numGrupo = rs.getInt("CI_NUMGRUPO");
                temporal.numCiclo = rs.getInt("CI_NUMCICLO");
                temporal.situacionActualCredito = rs.getInt("ST_SITUACION_ACTUAL_CREDITO");
                temporal.diasVencidos = rs.getInt("ST_DIAS_VENCIDOS");
                temporal.saldoTotalVencido = rs.getDouble("ST_TOTAL_EXIGIBLE");
                //Logger.debug("MONITOR DE PAGOS : "+temporal.toString());
            }

        } catch (SQLException sqle) {
            myLogger.error("getSaldosT24", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSaldosT24", e);
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

    public EventosDePagoVO getSaldos(int numGrupo, int numCiclo) throws ClientesException {

        String query = "SELECT "
                + "CI_NUMGRUPO, CI_NUMCICLO, IB_ESTATUS, IB_NUM_DIAS_MORA, IB_TOTAL_VENCIDO "
                + "FROM "
                + "D_SALDOS, "
                + "D_CICLOS_GRUPALES "
                + "WHERE "
                + // -- CICLO -- 
                "CI_ESTATUS=1 "
                + "AND CI_NUMGRUPO = ? "
                + "AND CI_NUMCICLO = ? "
                + "AND CI_DESEMBOLSADO=3 "
                + "AND CI_ESTATUS_REVISION_MONITOR=1 "
                + // -- SALDOS --
                "AND IB_PRODUCTO IN(3,5) "
                + "AND IB_NUMCLIENTESICAP=CI_NUMGRUPO "
                + "AND IB_NUMSOLICITUDSICAP=CI_NUMCICLO "
                + "AND IB_ESTATUS IN(1,2) "
                + "GROUP BY CI_NUMGRUPO,CI_NUMCICLO ";

        EventosDePagoVO temporal = null;
        Connection cn = null;

        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, numGrupo);
            ps.setInt(2, numCiclo);
            //Logger.debug("Ejecutando = "+query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new EventosDePagoVO();
                temporal.numGrupo = rs.getInt("CI_NUMGRUPO");
                temporal.numCiclo = rs.getInt("CI_NUMCICLO");
                temporal.situacionActualCredito = rs.getInt("IB_ESTATUS");
                temporal.diasVencidos = rs.getInt("IB_NUM_DIAS_MORA");
                temporal.saldoTotalVencido = rs.getDouble("IB_TOTAL_VENCIDO");
                //Logger.debug("MONITOR DE PAGOS : "+temporal.toString());
            }

        } catch (SQLException sqle) {
            myLogger.error("getSaldos", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getSaldos", e);
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

    public EventosDePagoVO[] getMonitoreoGeneral() throws ClientesException {

        String query = "SELECT * FROM D_MONITOR_PAGOS_GRUPAL WHERE MO_ESTATUS_VISITA_SUPERVISOR!=2 AND MO_ESTATUS_VISITA_GERENTE!=2";

        ArrayList<EventosDePagoVO> array = new ArrayList<EventosDePagoVO>();
        EventosDePagoVO temporal = null;
        EventosDePagoVO elementos[] = null;
        Connection cn = null;

        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            //Logger.debug("Ejecutando = "+query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new EventosDePagoVO();
                temporal.numGrupo = rs.getInt("MO_NUMGRUPO");
                temporal.numCiclo = rs.getInt("MO_NUMCICLO");
                temporal.numPago = rs.getInt("MO_NUMPAGO");
                temporal.numAtrasos = rs.getInt("MO_NUMERO_ATRASOS");
                temporal.estatusVisitaSupervisor = rs.getInt("MO_ESTATUS_VISITA_SUPERVISOR");
                temporal.estatusVisitaGerente = rs.getInt("MO_ESTATUS_VISITA_GERENTE");
                temporal.estatusVisitaGestor = rs.getInt("MO_ESTATUS_VISITA_GESTOR");
                temporal.enMora = rs.getString("MO_EN_MORA");
                temporal.estatusReporteCobranza = rs.getInt("MO_ESTATUS_REPORTE_COBRANZA");
                temporal.identificador = rs.getInt("MO_ID");
                temporal.fechaAlertaSupervisor = rs.getDate("MO_FECHA_ALERTA_SUPERVISOR");
                temporal.fechaAlertaGerente = rs.getDate("MO_FECHA_ALERTA_GERENTE");
                temporal.fechaAlertaGestor = rs.getDate("MO_FECHA_ALERTA_GESTOR");
                temporal.fechaReporteCobranza = rs.getDate("MO_FECHA_REPORTE_COBRANZA");
                array.add(temporal);
                //Logger.debug("MONITOR DE PAGOS : "+temporal.toString());
            }
            if (array.size() > 0) {
                elementos = new EventosDePagoVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (EventosDePagoVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getMonitoreoGeneral", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getMonitoreoGeneral", e);
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

    public int addMonitor(EventosDePagoVO registroMonitor) throws ClientesException {

        String query = "INSERT INTO D_MONITOR_PAGOS_GRUPAL "
                + "(MO_NUMGRUPO, MO_NUMCICLO, MO_NUMPAGO, MO_NUMERO_ATRASOS, MO_ESTATUS_VISITA_SUPERVISOR, "
                + "MO_ESTATUS_VISITA_GERENTE, MO_ESTATUS_VISITA_GESTOR, MO_EN_MORA, MO_FECHA_ALERTA_SUPERVISOR, MO_FECHA_ALERTA_GERENTE, MO_FECHA_ALERTA_GESTOR, MO_ESTATUS_REPORTE_COBRANZA,"
                + "MO_FECHA_REPORTE_COBRANZA, MO_FECHA_MODIFICACION ) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);

            ps.setInt(param++, registroMonitor.numGrupo);
            ps.setInt(param++, registroMonitor.numCiclo);
            ps.setInt(param++, registroMonitor.numPago);
            ps.setInt(param++, registroMonitor.numAtrasos);
            ps.setInt(param++, registroMonitor.estatusVisitaSupervisor);
            ps.setInt(param++, registroMonitor.estatusVisitaGerente);
            ps.setInt(param++, registroMonitor.estatusVisitaGestor);
            ps.setString(param++, registroMonitor.enMora);
            ps.setDate(param++, registroMonitor.fechaAlertaSupervisor);
            ps.setDate(param++, registroMonitor.fechaAlertaGerente);
            ps.setDate(param++, registroMonitor.fechaAlertaGestor);
            ps.setInt(param++, registroMonitor.estatusReporteCobranza);
            ps.setDate(param++, registroMonitor.fechaReporteCobranza);

            //Logger.debug("Ejecutando = "+query);
            res = ps.executeUpdate();
            //Logger.debug("Resultado = "+res);
        } catch (SQLException sqle) {
            myLogger.error("addMonitor", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addMonitor", e);
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

    public int updateMonitor(EventosDePagoVO registroMonitor) throws ClientesException {

        String query = "UPDATE D_MONITOR_PAGOS_GRUPAL SET MO_NUMPAGO=?, MO_NUMERO_ATRASOS=?, MO_ESTATUS_VISITA_SUPERVISOR=?, "
                + "MO_ESTATUS_VISITA_GERENTE=?, MO_ESTATUS_VISITA_GESTOR = ?, MO_ESTATUS_REPORTE_COBRANZA=?, MO_EN_MORA=?, MO_FECHA_ALERTA_SUPERVISOR=?, "
                + "MO_FECHA_ALERTA_GERENTE=?, MO_FECHA_ALERTA_GESTOR = ?, MO_FECHA_REPORTE_COBRANZA=? WHERE MO_ID=? ";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);

            ps.setInt(param++, registroMonitor.numPago);
            ps.setInt(param++, registroMonitor.numAtrasos);
            ps.setInt(param++, registroMonitor.estatusVisitaSupervisor);
            ps.setInt(param++, registroMonitor.estatusVisitaGerente);
            ps.setInt(param++, registroMonitor.estatusVisitaGestor);
            ps.setInt(param++, registroMonitor.estatusReporteCobranza);
            ps.setString(param++, registroMonitor.enMora);
            ps.setDate(param++, registroMonitor.fechaAlertaSupervisor);
            ps.setDate(param++, registroMonitor.fechaAlertaGerente);
            ps.setDate(param++, registroMonitor.fechaAlertaGestor);
            ps.setDate(param++, registroMonitor.fechaReporteCobranza);

            ps.setInt(param++, registroMonitor.identificador);

			//Logger.debug("Ejecutando = "+query);
            //Logger.debug("Monitor= "+registroMonitor.toString());
            res = ps.executeUpdate();
            //Logger.debug("Resultado = "+res);
        } catch (SQLException sqle) {
            myLogger.error("updateMonitor", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateMonitor", e);
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

    public Date getFechaUltimoPago(String referencia) throws ClientesException {

        String query = "SELECT MAX(PC_FECHA_PAGO) AS FECHA FROM d_pagos_cartera where pc_referencia = ?";

        Connection cn = null;
        Date fechaUltimoPago = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, referencia);
            myLogger.debug("Ejecutando = " + query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                fechaUltimoPago = rs.getDate("FECHA");
            }
        } catch (SQLException sqle) {
            myLogger.error("getFechaUltimoPago", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getFechaUltimoPago", e);
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
        return fechaUltimoPago;
    }

    public int eliminaAlertaGrupal(Connection conn, int numeroAlerta) throws ClientesException {

        String query = "DELETE FROM D_MONITOR_PAGOS_GRUPAL WHERE  MO_ID= ?";

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

            ps.setInt(param++, numeroAlerta);

            myLogger.debug("Ejecutando = " + query);
            res = ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("eliminaAlertaGrupal", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("eliminaAlertaGrupal", e);
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

    public int desactivaFuturasAletas(CicloGrupalVO ciclo) throws ClientesException {

        String query = "UPDATE D_CICLOS_GRUPALES SET CI_ESTATUS_REVISION_MONITOR = ? WHERE CI_NUMGRUPO = ? AND CI_NUMCICLO = ?";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);

            ps.setInt(param++, ciclo.estatusAlertasPago);
            ps.setInt(param++, ciclo.idGrupo);
            ps.setInt(param++, ciclo.idCiclo);
            res = ps.executeUpdate();

        } catch (SQLException sqle) {
            myLogger.error("desactivaFuturasAletas", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("desactivaFuturasAletas", e);
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
}
