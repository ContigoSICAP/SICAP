package com.sicap.clientes.dao.cartera;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.sicap.clientes.dao.DAOMaster;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.vo.cartera.EventosVO;
import org.apache.log4j.Logger;

public class EventoDAO extends DAOMaster {

    private static Logger myLogger = Logger.getLogger(EventoDAO.class);

    public EventosVO[] getElementos(int numCliente, int numCredito) throws ClientesException {
        EventosVO eventos = null;
        Connection cn = null;
        ArrayList<EventosVO> array = new ArrayList<EventosVO>();
        EventosVO elementos[] = null;

        String query = "SELECT ev_numcliente,ev_numcredito,ev_tipo_evento,ev_numdividendo,ev_fecha_ini,ev_fecha_fin,ev_numdias,ev_monto,ev_status,ev_usuario,ev_saldo "
                + "FROM d_eventos WHERE ev_numcliente=? AND ev_numcredito=? AND TIME(ev_fecha_ini)!=0 ORDER BY ev_fecha_ini,ev_tipo_evento";
        try {
            cn = getCWConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numCredito);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + numCliente + "," + numCredito + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                eventos = new EventosVO();
                eventos.numCliente = numCliente;
                eventos.numCredito = numCredito;
                eventos.tipoEvento = rs.getString("ev_tipo_evento");
                eventos.numDividendo = rs.getInt("ev_numdividendo");
                /*eventos.fechaIni = rs.getDate("EV_FECHA_INI");
                eventos.fechaFin = rs.getDate("EV_FECHA_FIN");*/
                eventos.fechaIni = rs.getTimestamp("ev_fecha_ini");
                eventos.fechaFin = rs.getTimestamp("ev_fecha_fin");
                eventos.numDias = rs.getInt("ev_numdias");
                eventos.monto = rs.getDouble("ev_monto");
                eventos.status = rs.getInt("ev_status");
                eventos.usuario = rs.getString("ev_usuario");
                eventos.saldo = rs.getDouble("ev_saldo");
                array.add(eventos);
            }
            if (array.size() > 0) {
                elementos = new EventosVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (EventosVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getEventos", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getEventos", e);
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

    public EventosVO[] getElementosTipo(int numCliente, int numCredito, String tipoEvento) throws ClientesException {
        EventosVO eventos = null;
        Connection cn = null;
        ArrayList<EventosVO> array = new ArrayList<EventosVO>();
        EventosVO elementos[] = null;

        String query = "SELECT EV_NUMCLIENTE, EV_NUMCREDITO, EV_TIPO_EVENTO, EV_NUMDIVIDENDO, EV_FECHA_INI, EV_FECHA_FIN, EV_NUMDIAS, EV_MONTO,"
                + "EV_STATUS, EV_USUARIO, EV_SALDO FROM D_EVENTOS WHERE EV_NUMCLIENTE = ? AND EV_NUMCREDITO = ? AND EV_TIPO_EVENTO = ? ";
        try {
            cn = getCWConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numCredito);
            ps.setString(3, tipoEvento);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + numCliente + "," + numCredito + "," + tipoEvento + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                eventos = new EventosVO();
                eventos.numCliente = numCliente;
                eventos.numCredito = numCredito;
                eventos.tipoEvento = tipoEvento;
                eventos.numDividendo = rs.getInt("EV_NUMDIVIDENDO");
                /*eventos.fechaIni = rs.getDate("EV_FECHA_INI");
                eventos.fechaFin = rs.getDate("EV_FECHA_FIN");*/
                eventos.fechaIni = rs.getTimestamp("EV_FECHA_INI");
                eventos.fechaFin = rs.getTimestamp("EV_FECHA_FIN");
                eventos.numDias = rs.getInt("EV_NUMDIAS");
                eventos.monto = rs.getDouble("EV_MONTO");
                eventos.status = rs.getInt("EV_STATUS");
                eventos.usuario = rs.getString("EV_USUARIO");
                eventos.saldo = rs.getDouble("EV_SALDO");
                array.add(eventos);
            }
            if (array.size() > 0) {
                elementos = new EventosVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (EventosVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getElementosTipo", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getElementosTipo", e);
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

    public EventosVO getDividendoTipo(int numCliente, int numCredito, int numDiv, String tipoEvento) throws ClientesException {
        EventosVO eventos = null;
        Connection cn = null;
        ArrayList<EventosVO> array = new ArrayList<EventosVO>();

        String query = "SELECT EV_NUMCLIENTE, EV_NUMCREDITO, EV_TIPO_EVENTO, EV_NUMDIVIDENDO, EV_FECHA_INI, EV_FECHA_FIN, EV_NUMDIAS, EV_MONTO,"
                + "EV_STATUS, EV_USUARIO, EV_SALDO FROM D_EVENTOS WHERE EV_NUMCLIENTE = ? AND EV_NUMCREDITO = ? AND EV_NUMDIVIDENDO = ? AND EV_TIPO_EVENTO = ? ";
        try {
            cn = getCWConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numCredito);
            ps.setInt(3, numDiv);
            ps.setString(4, tipoEvento);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + numCliente + "," + numCredito + "," + tipoEvento + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                eventos = new EventosVO();
                eventos.numCliente = numCliente;
                eventos.numCredito = numCredito;
                eventos.tipoEvento = tipoEvento;
                eventos.numDividendo = rs.getInt("EV_NUMDIVIDENDO");
                /*eventos.fechaIni = rs.getDate("EV_FECHA_INI");
                eventos.fechaFin = rs.getDate("EV_FECHA_FIN");*/
                eventos.fechaIni = rs.getTimestamp("EV_FECHA_INI");
                eventos.fechaFin = rs.getTimestamp("EV_FECHA_FIN");
                eventos.numDias = rs.getInt("EV_NUMDIAS");
                eventos.monto = rs.getDouble("EV_MONTO");
                eventos.status = rs.getInt("EV_STATUS");
                eventos.usuario = rs.getString("EV_USUARIO");
                eventos.saldo = rs.getDouble("EV_SALDO");
                array.add(eventos);
            }
        } catch (SQLException sqle) {
            myLogger.error("getDividendoTipo", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getDividendoTipo", e);
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

        return eventos;
    }

    public EventosVO getLastEventoTipo(int numCliente, int numCredito, String tipoEvento) throws ClientesException {
        EventosVO eventos = null;
        Connection cn = null;
        ArrayList<EventosVO> array = new ArrayList<EventosVO>();

        String query = "SELECT EV_NUMDIVIDENDO, EV_NUMCLIENTE, EV_NUMCREDITO, EV_TIPO_EVENTO, EV_FECHA_INI, MAX(EV_FECHA_FIN) AS EV_FECHA_FIN, "
                + "EV_NUMDIAS, EV_MONTO, EV_STATUS, EV_USUARIO, EV_SALDO FROM D_EVENTOS WHERE EV_NUMCLIENTE = ? AND EV_NUMCREDITO = ? AND EV_TIPO_EVENTO = ? "
                + "GROUP BY EV_NUMDIVIDENDO, EV_NUMCLIENTE, EV_NUMCREDITO, EV_TIPO_EVENTO, EV_FECHA_INI, EV_NUMDIAS, EV_MONTO, EV_STATUS, EV_USUARIO, EV_SALDO";
        try {
            cn = getCWConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numCredito);
            ps.setString(3, tipoEvento);
            myLogger.debug("Ejecutando::: = " + query);
            myLogger.debug("Parametros = [" + numCliente + "," + numCredito + "," + tipoEvento + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                eventos = new EventosVO();
                eventos.numCliente = numCliente;
                eventos.numCredito = numCredito;
                eventos.tipoEvento = tipoEvento;
                eventos.numDividendo = rs.getInt("EV_NUMDIVIDENDO");
                /*eventos.fechaIni = rs.getDate("EV_FECHA_INI");
                eventos.fechaFin = rs.getDate("EV_FECHA_FIN");*/
                eventos.fechaIni = rs.getTimestamp("EV_FECHA_INI");
                eventos.fechaFin = rs.getTimestamp("EV_FECHA_FIN");
                eventos.numDias = rs.getInt("EV_NUMDIAS");
                eventos.monto = rs.getDouble("EV_MONTO");
                eventos.status = rs.getInt("EV_STATUS");
                eventos.usuario = rs.getString("EV_USUARIO");
                eventos.saldo = rs.getDouble("EV_SALDO");
                array.add(eventos);
            }
        } catch (SQLException sqle) {
            myLogger.error("getLastEventoTipo", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getLastEventoTipo", e);
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

        return eventos;
    }

    public EventosVO getLastEventoTipo(int numCliente, int numCredito, int numPago, String tipoEvento, Date fechaMov) throws ClientesException {
        EventosVO eventos = null;
        Connection cn = null;
        ArrayList<EventosVO> array = new ArrayList<EventosVO>();

        String query = "SELECT EV_NUMDIVIDENDO, EV_NUMCLIENTE, EV_NUMCREDITO, EV_TIPO_EVENTO, EV_FECHA_INI, EV_FECHA_FIN, EV_NUMDIAS, EV_MONTO,"
                + " EV_STATUS, EV_USUARIO, EV_SALDO FROM D_EVENTOS WHERE EV_NUMCLIENTE = ? AND EV_NUMCREDITO = ?  AND EV_NUMDIVIDENDO = ?"
                + " AND EV_TIPO_EVENTO = ? AND EV_FECHA_FIN = ?";
        try {
            cn = getCWConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numCredito);
            ps.setInt(3, numPago);
            ps.setString(4, tipoEvento);
            ps.setDate(5, Convertidor.toSqlDate(fechaMov));
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + numCliente + "," + numCredito + "," + numPago + "," + tipoEvento + "," + fechaMov.toString() + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                eventos = new EventosVO();
                eventos.numCliente = numCliente;
                eventos.numCredito = numCredito;
                eventos.tipoEvento = tipoEvento;
                eventos.numDividendo = rs.getInt("EV_NUMDIVIDENDO");
                /*eventos.fechaIni = rs.getDate("EV_FECHA_INI");
                eventos.fechaFin = rs.getDate("EV_FECHA_FIN");*/
                eventos.fechaIni = rs.getTimestamp("EV_FECHA_INI");
                eventos.fechaFin = rs.getTimestamp("EV_FECHA_FIN");
                eventos.numDias = rs.getInt("EV_NUMDIAS");
                eventos.monto = rs.getDouble("EV_MONTO");
                eventos.status = rs.getInt("EV_STATUS");
                eventos.usuario = rs.getString("EV_USUARIO");
                eventos.saldo = rs.getDouble("EV_SALDO");
                array.add(eventos);
            }
        } catch (SQLException sqle) {
            myLogger.error("getLastEventoTipo", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getLastEventoTipo", e);
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

        return eventos;
    }

    public EventosVO getLastEventoTipo(int numCliente, int numCredito, int numPago, String tipoEvento) throws ClientesException {
        EventosVO eventos = null;
        Connection cn = null;
        ArrayList<EventosVO> array = new ArrayList<EventosVO>();

        String query = "SELECT EV_NUMDIVIDENDO, EV_NUMCLIENTE, EV_NUMCREDITO, EV_TIPO_EVENTO, EV_FECHA_INI, EV_FECHA_FIN, EV_NUMDIAS, EV_MONTO,"
                + " EV_STATUS, EV_USUARIO, EV_SALDO FROM D_EVENTOS WHERE EV_NUMCLIENTE = ? AND EV_NUMCREDITO = ?  AND EV_NUMDIVIDENDO = ?"
                + " AND EV_TIPO_EVENTO = ?";
        try {
            cn = getCWConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numCredito);
            ps.setInt(3, numPago);
            ps.setString(4, tipoEvento);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Parametros = [" + numCliente + "," + numCredito + "," + numPago + "," + tipoEvento + "]");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                eventos = new EventosVO();
                eventos.numCliente = numCliente;
                eventos.numCredito = numCredito;
                eventos.tipoEvento = tipoEvento;
                eventos.numDividendo = rs.getInt("EV_NUMDIVIDENDO");
                /*eventos.fechaIni = rs.getDate("EV_FECHA_INI");
                eventos.fechaFin = rs.getDate("EV_FECHA_FIN");*/
                eventos.fechaIni = rs.getTimestamp("EV_FECHA_INI");
                eventos.fechaFin = rs.getTimestamp("EV_FECHA_FIN");
                eventos.numDias = rs.getInt("EV_NUMDIAS");
                eventos.monto = rs.getDouble("EV_MONTO");
                eventos.status = rs.getInt("EV_STATUS");
                eventos.usuario = rs.getString("EV_USUARIO");
                eventos.saldo = rs.getDouble("EV_SALDO");
                array.add(eventos);
            }
        } catch (SQLException sqle) {
            myLogger.error("getLastEventoTipo", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getLastEventoTipo", e);
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

        return eventos;
    }

    public int addEvento(EventosVO evento) throws ClientesException {

        String query = "INSERT INTO D_EVENTOS (EV_NUMCLIENTE, EV_NUMCREDITO, EV_TIPO_EVENTO, EV_NUMDIVIDENDO, EV_FECHA_INI,"
                + "EV_FECHA_FIN, EV_NUMDIAS, EV_MONTO, EV_STATUS, EV_USUARIO, EV_SALDO) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection cn = null;
        int param = 1;
        int res = 0;
        try {
            PreparedStatement ps = null;
            cn = getCWConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(param++, evento.numCliente);
            ps.setInt(param++, evento.numCredito);
            ps.setString(param++, evento.tipoEvento);
            ps.setInt(param++, evento.numDividendo);
            /*ps.setDate(param++, evento.fechaIni);
            ps.setDate(param++, evento.fechaFin);*/
            ps.setTimestamp(param++, evento.fechaIni);
            ps.setTimestamp(param++, evento.fechaFin);
            ps.setInt(param++, evento.numDias);
            ps.setDouble(param++, evento.monto);
            ps.setInt(param++, evento.status);
            ps.setString(param++, evento.usuario);
            ps.setDouble(param++, evento.saldo);
            myLogger.debug("Ejecutando = " + ps);
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("addEvento", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addEvento", e);
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

    public int addEvento(EventosVO evento, Connection con) throws ClientesException {
        String query = "INSERT INTO D_EVENTOS (EV_NUMCLIENTE, EV_NUMCREDITO, EV_TIPO_EVENTO, EV_NUMDIVIDENDO, EV_FECHA_INI,"
                + "EV_FECHA_FIN, EV_NUMDIAS, EV_MONTO, EV_STATUS, EV_USUARIO, EV_SALDO) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int param = 1;
        int res = 0;
        try {
            PreparedStatement ps = null;
            ps = con.prepareStatement(query);
            ps.setInt(param++, evento.numCliente);
            ps.setInt(param++, evento.numCredito);
            ps.setString(param++, evento.tipoEvento);
            ps.setInt(param++, evento.numDividendo);
            /*ps.setDate(param++, evento.fechaIni);
            ps.setDate(param++, evento.fechaFin);*/
            ps.setTimestamp(param++, evento.fechaIni);
            ps.setTimestamp(param++, evento.fechaFin);
            ps.setInt(param++, evento.numDias);
            ps.setDouble(param++, evento.monto);
            ps.setInt(param++, evento.status);
            ps.setString(param++, evento.usuario);
            ps.setDouble(param++, evento.saldo);
            myLogger.debug("Ejecutando = " + ps);
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("addEvento", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addEvento", e);
            throw new ClientesException(e.getMessage());
        }
        return res;
    }

    public void delEvento(int numCliente, int numCredito, String tipoEvento, int numDividendo) throws ClientesException {

        String query = "DELETE FROM D_EVENTOS WHERE EV_NUMCLIENTE = ? AND EV_NUMCREDITO = ? AND EV_TIPO_EVENTO = ? AND TA_NUMDIVIDENDO = ?";

        Connection cn = null;

        try {
            PreparedStatement ps = null;
            cn = getCWConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, numCliente);
            ps.setInt(2, numCredito);
            ps.setString(3, tipoEvento);
            ps.setInt(4, numDividendo);

            //myLogger.debug("Ejecutando = "+query);
            //myLogger.debug("Parametros = ["+idCliente+","+idSolicitud+","+idDisposicion+","+idAmortizacion+"]");
            ps.execute();
        } catch (SQLException sqle) {
            myLogger.error("delEvento", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("delEvento", e);
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

    public int updateEvento(EventosVO evento) throws ClientesException {

        String query = "UPDATE D_EVENTOS SET EV_FECHA_FIN = ?, EV_NUMDIAS = ? , EV_MONTO = ?, EV_STATUS = ? "
                + "WHERE EV_NUMCLIENTE = ? AND EV_NUMCREDITO = ? AND EV_TIPO_EVENTO = ? AND EV_NUMDIVIDENDO = ? ";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            cn = getCWConnection();
            ps = cn.prepareStatement(query);
            //ps.setDate(param++, evento.fechaFin);
            ps.setTimestamp(param++, evento.fechaFin);
            ps.setInt(param++, evento.numDias);
            ps.setDouble(param++, evento.monto);
            ps.setInt(param++, evento.status);
            res = ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("updateEvento", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateEvento", e);
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

    public void respaldaEventoCiclo(int idEquipo, int idCredito) throws ClientesDBException, ClientesException {
        String query = "insert into d_eventos_del (EV_NUMCLIENTE,EV_NUMCREDITO,EV_TIPO_EVENTO,EV_NUMDIVIDENDO,EV_FECHA_INI,EV_FECHA_FIN,EV_NUMDIAS,EV_MONTO,EV_STATUS,EV_USUARIO,EV_SALDO) "
                + "(SELECT EV_NUMCLIENTE,EV_NUMCREDITO,EV_TIPO_EVENTO,EV_NUMDIVIDENDO,EV_FECHA_INI,EV_FECHA_FIN,EV_NUMDIAS,EV_MONTO,EV_STATUS,EV_USUARIO,EV_SALDO"
                + " FROM d_eventos WHERE ev_numcliente=? AND ev_numcredito=?)";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            con = getCWConnection();
            
            ps = con.prepareStatement(query);
            ps.setInt(1, idEquipo);
            ps.setInt(2, idCredito);
            myLogger.debug("Ejecutando: " + ps.toString());
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("eliminaEventoCiclo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("eliminaEventoCiclo", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
    }
    
    public void eliminaEventoCiclo(int idEquipo, int idCredito, boolean conexionODS) throws ClientesDBException, ClientesException {
        String query = "DELETE FROM d_eventos WHERE ev_numcliente=? AND ev_numcredito=?";
        Connection con = null;
        PreparedStatement ps = null;
        try {
            if(conexionODS){
                con = getCODSConnection();
            }else{
                con = getCWConnection();
            }
            
            ps = con.prepareStatement(query);
            ps.setInt(1, idEquipo);
            ps.setInt(2, idCredito);
            myLogger.debug("Ejecutando: " + ps.toString());
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("eliminaEventoCiclo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("eliminaEventoCiclo", e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
    }
}
