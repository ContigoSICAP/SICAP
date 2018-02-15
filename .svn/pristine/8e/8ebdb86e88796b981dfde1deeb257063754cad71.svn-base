package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.DecisionComiteVO;
import java.util.ArrayList;
import org.apache.log4j.Logger;

public class DecisionComiteDAO extends DAOMaster {

    private static Logger myLogger = Logger.getLogger(DecisionComiteDAO.class);

    public DecisionComiteVO getDecisionComite(int idCliente, int idSolicitud) throws ClientesException {
        DecisionComiteVO decisionComite = null;
        Connection cn = null;
        String query = "SELECT * FROM  D_DECISION_COMITE WHERE DE_NUMCLIENTE = ? AND DE_NUMSOLICITUD = ?";
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.setInt(2, idSolicitud);
            myLogger.debug("Ejecutando = " + query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                decisionComite = new DecisionComiteVO();
                decisionComite.idCliente = idCliente;
                decisionComite.idSolicitud = idSolicitud;
                decisionComite.fechaRealizacion = rs.getDate("DE_FECHAREALIZACION");
                decisionComite.fechaCaptura = rs.getTimestamp("DE_FECHACAPTURA");
                decisionComite.decisionComite = rs.getInt("DE_DECISION_COMITE");
                decisionComite.causaRechazo = rs.getInt("DE_CAUSARECHAZO");
                decisionComite.motivoRechazoCliente = rs.getInt("DE_MOTIVORECHAZOCLIENTE");
                decisionComite.detalleMotivoRechazoCliente = rs.getString("DE_DETALLEMOTIVORECHAZOCLIENTE");
                decisionComite.montoAutorizado = rs.getDouble("DE_MONTOAUTORIZADO");
                decisionComite.plazoAutorizado = rs.getInt("DE_PLAZOAUTORIZADO");
                decisionComite.comision = rs.getInt("DE_COMISION");
                decisionComite.montoRefinanciado = rs.getDouble("DE_MONTOREFINANCIADO");
                decisionComite.tasa = rs.getInt("DE_TASA");
                decisionComite.frecuenciaPago = rs.getInt("DE_FRECUENCIAPAGO");
                decisionComite.motivoCondicionamiento = rs.getInt("DE_MOTIVOCONDICIONAMIENTO");
                decisionComite.comentariosComite = rs.getString("DE_COMENTARIOSCOMITE");
                decisionComite.multa = rs.getDouble("DE_MULTA");
                decisionComite.montoConSeguro = rs.getDouble("DE_MONTOCONSEGURO");
            }
        } catch (SQLException sqle) {
            myLogger.error("getDecisionComite", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getDecisionComite", e);
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
        return decisionComite;
    }

    public ArrayList<DecisionComiteVO> getDecisionesComite(int idCliente) throws ClientesException {
        String query = "SELECT * FROM  D_DECISION_COMITE WHERE DE_NUMCLIENTE = ?";

        ArrayList<DecisionComiteVO> registros = new ArrayList<DecisionComiteVO>();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            myLogger.debug("Ejecutando: " + ps.toString());
            rs = ps.executeQuery();
            while (rs.next()) {
                DecisionComiteVO decisionComite = new DecisionComiteVO();
                decisionComite = new DecisionComiteVO();
                decisionComite.idCliente = idCliente;
                decisionComite.fechaRealizacion = rs.getDate("DE_FECHAREALIZACION");
                decisionComite.fechaCaptura = rs.getTimestamp("DE_FECHACAPTURA");
                decisionComite.decisionComite = rs.getInt("DE_DECISION_COMITE");
                decisionComite.causaRechazo = rs.getInt("DE_CAUSARECHAZO");
                decisionComite.motivoRechazoCliente = rs.getInt("DE_MOTIVORECHAZOCLIENTE");
                decisionComite.detalleMotivoRechazoCliente = rs.getString("DE_DETALLEMOTIVORECHAZOCLIENTE");
                decisionComite.montoAutorizado = rs.getDouble("DE_MONTOAUTORIZADO");
                decisionComite.plazoAutorizado = rs.getInt("DE_PLAZOAUTORIZADO");
                decisionComite.comision = rs.getInt("DE_COMISION");
                decisionComite.montoRefinanciado = rs.getDouble("DE_MONTOREFINANCIADO");
                decisionComite.tasa = rs.getInt("DE_TASA");
                decisionComite.frecuenciaPago = rs.getInt("DE_FRECUENCIAPAGO");
                decisionComite.motivoCondicionamiento = rs.getInt("DE_MOTIVOCONDICIONAMIENTO");
                decisionComite.comentariosComite = rs.getString("DE_COMENTARIOSCOMITE");
                decisionComite.multa = rs.getDouble("DE_MULTA");
                registros.add(decisionComite);
            }
        } catch (SQLException sqle) {
            myLogger.error("getHistorialEquipo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getHistorialEquipo", e);
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
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
        return registros;
    }

    public int addDecisionComite(DecisionComiteVO decisionComite) throws ClientesException {
        return addDecisionComite(null, decisionComite);
    }

    public int addDecisionComite(Connection conn, DecisionComiteVO decisionComite) throws ClientesException {

        String query = "INSERT INTO d_decision_comite (de_numcliente, de_numsolicitud,de_fecharealizacion,"
                + "de_fechacaptura, de_decision_comite,de_causarechazo, de_motivorechazocliente,de_detallemotivorechazocliente,"
                + "de_montoautorizado,de_montorefinanciado,de_plazoautorizado,de_comision,de_tasa,de_frecuenciapago,de_motivocondicionamiento"
                + ",de_comentarioscomite,de_multa ) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        Connection cn = null;
        int res = 0;
        PreparedStatement ps = null;
        try {
            if (conn != null) {
                ps = conn.prepareStatement(query);
            } else {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            }
            ps.setInt(1, decisionComite.idCliente);
            ps.setInt(2, decisionComite.idSolicitud);
            ps.setDate(3, decisionComite.fechaRealizacion);
            ps.setTimestamp(4, decisionComite.fechaCaptura);
            ps.setInt(5, decisionComite.decisionComite);
            ps.setInt(6, decisionComite.causaRechazo);
            ps.setInt(7, decisionComite.motivoRechazoCliente);
            ps.setString(8, decisionComite.detalleMotivoRechazoCliente);
            ps.setDouble(9, decisionComite.montoAutorizado);
            ps.setDouble(10, decisionComite.montoRefinanciado);
            ps.setInt(11, decisionComite.plazoAutorizado);
            ps.setInt(12, decisionComite.comision);
            ps.setInt(13, decisionComite.tasa);
            ps.setInt(14, decisionComite.frecuenciaPago);
            ps.setInt(15, decisionComite.motivoCondicionamiento);
            ps.setString(16, decisionComite.comentariosComite);
            ps.setDouble(17, decisionComite.multa);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Decision Comite = " + decisionComite.toString());
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("addDecisionComite", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addDecisionComite", e);
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

    public int updateDecisionComite(DecisionComiteVO decisionComite) throws ClientesException {
        return updateDecisionComite(null, decisionComite);
    }

    public int updateDecisionComite(Connection conn, DecisionComiteVO decisionComite) throws ClientesException {

        String query = "UPDATE D_DECISION_COMITE SET DE_FECHAREALIZACION=?,DE_DECISION_COMITE=?, "
                + "DE_CAUSARECHAZO=?, DE_MOTIVORECHAZOCLIENTE=?,DE_DETALLEMOTIVORECHAZOCLIENTE=?,"
                + "DE_MONTOAUTORIZADO=?,DE_MONTOREFINANCIADO=?,DE_PLAZOAUTORIZADO=?,DE_COMISION=?,DE_TASA=?,DE_FRECUENCIAPAGO=?,"
                + "DE_MOTIVOCONDICIONAMIENTO=?, DE_COMENTARIOSCOMITE=?, DE_MULTA=? WHERE DE_NUMCLIENTE = ?  AND DE_NUMSOLICITUD = ? ";

        Connection cn = null;
        int res = 0;
        PreparedStatement ps = null;
        try {
            if (conn != null) {
                ps = conn.prepareStatement(query);
            } else {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            }

            ps.setDate(1, decisionComite.fechaRealizacion);
            //ps.setTimestamp(2,decisionComite.fechaCaptura);
            ps.setInt(2, decisionComite.decisionComite);
            ps.setInt(3, decisionComite.causaRechazo);
            ps.setInt(4, decisionComite.motivoRechazoCliente);
            ps.setString(5, decisionComite.detalleMotivoRechazoCliente);
            ps.setDouble(6, decisionComite.montoAutorizado);
            ps.setDouble(7, decisionComite.montoRefinanciado);
            ps.setInt(8, decisionComite.plazoAutorizado);
            ps.setInt(9, decisionComite.comision);
            ps.setInt(10, decisionComite.tasa);
            ps.setInt(11, decisionComite.frecuenciaPago);
            ps.setInt(12, decisionComite.motivoCondicionamiento);
            ps.setString(13, decisionComite.comentariosComite);
            ps.setDouble(14, decisionComite.multa);
            ps.setInt(15, decisionComite.idCliente);
            ps.setInt(16, decisionComite.idSolicitud);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Decision Comite = " + decisionComite.toString());
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("updateDecisionComite", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateDecisionComite", e);
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

    public int updateMontoComisionTasa(Connection conn, DecisionComiteVO decisionComite) throws ClientesException {

        String query = "UPDATE D_DECISION_COMITE SET DE_MONTOAUTORIZADO=?,DE_MONTOREFINANCIADO=?,DE_COMISION=?,DE_TASA=?, DE_PLAZOAUTORIZADO=?"
                + " WHERE DE_NUMCLIENTE = ?  AND DE_NUMSOLICITUD = ? ";

        Connection cn = null;
        int res = 0;
        PreparedStatement ps = null;
        try {
            if (conn != null) {
                ps = conn.prepareStatement(query);
            } else {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            }
            ps.setDouble(1, decisionComite.montoAutorizado);
            ps.setDouble(2, decisionComite.montoRefinanciado);
            ps.setInt(3, decisionComite.comision);
            ps.setInt(4, decisionComite.tasa);
            ps.setInt(5, decisionComite.plazoAutorizado);
            ps.setInt(6, decisionComite.idCliente);
            ps.setInt(7, decisionComite.idSolicitud);
            myLogger.debug("Ejecutando = " + query);
//			myLogger.debug("Actualiza monto conmision tasa ::::: Decision Comite = "+decisionComite.toString());
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("updateMontoComisionTasaPlazo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateMontoComisionTasaPlazo", e);
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
    
    public int updateMontos(Connection conn, int idCliente, int idSolicitud, double montoAutorizado) throws ClientesException {

        String query = "UPDATE D_DECISION_COMITE SET DE_MONTOAUTORIZADO=? WHERE DE_NUMCLIENTE = ?  AND DE_NUMSOLICITUD = ? ";

        Connection cn = null;
        int res = 0;
        PreparedStatement ps = null;
        try {
            if (conn != null) {
                ps = conn.prepareStatement(query);
            } else {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            }
            ps.setDouble(1, montoAutorizado);
            ps.setInt(2, idCliente);
            ps.setInt(3, idSolicitud);
            myLogger.debug("Ejecutando = " + query);
//			myLogger.debug("Actualiza monto conmision tasa ::::: Decision Comite = "+decisionComite.toString());
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("updateMontos", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateMontos", e);
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

    public DecisionComiteVO getDecisionComiteCierre(int idCliente, int idSolicitud) throws ClientesException {

        DecisionComiteVO decisionComite = null;
        Connection cn = null;
        ResultSet res = null;
        String query = "SELECT de_plazoautorizado,de_tasa,de_frecuenciapago FROM d_decision_comite where de_numcliente= ? AND de_numsolicitud= ?";

        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.setInt(2, idSolicitud);
            myLogger.debug("Ejecutando = " + query);
            res = ps.executeQuery();
            while (res.next()) {
                decisionComite = new DecisionComiteVO(res.getInt("de_plazoautorizado"), res.getInt("de_tasa"), res.getInt("de_frecuenciapago"));
            }
        } catch (SQLException sqle) {
            myLogger.error("getDecisionComiteCierre", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getDecisionComiteCierre", e);
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
        return decisionComite;
    }

    public int deleteDecisionComite(Connection conn, int idCliente, int idSolicitud) throws ClientesException {

        int res = 0;
        String query = "DELETE FROM d_decision_comite where de_numcliente = ? and de_numsolicitud = ?;";
        int param = 1;
        PreparedStatement ps = null;
        Connection cn = null;
        try {
            if (conn == null) {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            } else {
                ps = conn.prepareStatement(query);
            }
            ps.setInt(param++, idCliente);
            ps.setInt(param++, idSolicitud);

            myLogger.debug("Ejecutando = " + query);
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("deleteDecisionComite", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("deleteDecisionComite", e);
            throw new ClientesException(e.getMessage());
        } 
        return res;
    }
    
    /**
     * Actualiza el monto con seguro.
     * 
     * @param con conexion
     * @param montoConSeguro monto con seguro
     * @param idCliente identificador del cliente/
     * @param idSolicitud identificador de la solicitud
     * 
     * @throws ClientesException 
     */
    public void updateMontoConSeguro(Connection con, double montoConSeguro, int idCliente, int idSolicitud) throws ClientesException {

        StringBuilder query = new StringBuilder();
        query.append("UPDATE d_decision_comite ");
        query.append("SET    de_montoconseguro = ?  ");
        query.append("WHERE  de_numcliente = ?  ");
        query.append("AND    de_numsolicitud = ?  ");
        
        PreparedStatement ps = null;
        try {
            ps = con.prepareStatement(query.toString());
            ps.setDouble(1, montoConSeguro);
            ps.setInt(2, idCliente);
            ps.setInt(3, idSolicitud);
            myLogger.debug("Ejecutando = " + query.toString());
            myLogger.debug("Parametros [" + montoConSeguro + ", " + idCliente + ", " + idSolicitud + "]");
            ps.executeUpdate();
        } catch (SQLException sqle) {
            myLogger.error("SQLException", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("Exception", e);
            throw new ClientesException(e.getMessage());
        }
    }
    
    /**
     * Obtenemos el monto autorizado, esto para comparar si en algunos
     * flujos el monto cambio del autorizado previamente.
     * 
     * @param idCliente identificador del cliente
     * @param idSolicitud identificador de la solicitud
     * @return monto autorizado
     * @throws ClientesException 
     */
    public double getMontoAutorizado(int idCliente, int idSolicitud) throws ClientesException {
        double montoAutorizado = 0;
        Connection cn = null;
        String query = "SELECT DE_MONTOAUTORIZADO FROM  D_DECISION_COMITE WHERE DE_NUMCLIENTE = ? AND DE_NUMSOLICITUD = ?";
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.setInt(2, idSolicitud);
            myLogger.debug("Ejecutando = " + query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                montoAutorizado = rs.getDouble("DE_MONTOAUTORIZADO");
            }
        } catch (SQLException sqle) {
            myLogger.error("getMontoAutorizado", sqle);
            throw new ClientesException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getMontoAutorizado", e);
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
        return montoAutorizado;
    }
}
