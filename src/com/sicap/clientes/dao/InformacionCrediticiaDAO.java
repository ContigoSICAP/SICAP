package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.InformacionCrediticiaVO;
import java.sql.Date;
import org.apache.log4j.Logger;

public class InformacionCrediticiaDAO extends DAOMaster {

    private static Logger logger = Logger.getLogger(InformacionCrediticiaDAO.class);

    public InformacionCrediticiaVO getInfoCrediticia(int idCliente, int idSolicitud, int idObligado, int idSociedad) throws ClientesException {

        String query = "SELECT IC_NUMSOCIEDAD, IC_TIPO_RESPUESTA, IC_RESPUESTA_CONSULTA, IC_FECHA_CONSULTA, IC_IDPROVIDER, IC_USUARIO_CONSULTA FROM D_INFORMACION_CREDITICIA "
                + "WHERE IC_NUMCLIENTE = ? AND IC_NUMSOLICITUD = ? AND IC_NUMOBLIGADO = ? AND IC_TIPO_RESPUESTA = 1 AND IC_NUMSOCIEDAD = ?";

        InformacionCrediticiaVO infoCrediticia = null;
        Connection cn = null;

        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.setInt(2, idSolicitud);
            ps.setInt(3, idObligado);
            ps.setInt(4, idSociedad);
            //logger.debug("Ejecutando = "+query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                infoCrediticia = new InformacionCrediticiaVO();
                infoCrediticia.idCliente = idCliente;
                infoCrediticia.idSolicitud = idSolicitud;
                infoCrediticia.idObligado = idObligado;
                infoCrediticia.idSociedad = idSociedad;
                infoCrediticia.idTipoRespuesta = rs.getInt("IC_TIPO_RESPUESTA");
                infoCrediticia.respuesta = rs.getString("IC_RESPUESTA_CONSULTA");
                infoCrediticia.fechaConsulta = rs.getDate("IC_FECHA_CONSULTA");
                infoCrediticia.idProvider = rs.getInt("IC_IDPROVIDER");
                infoCrediticia.usuarioConsulta = rs.getString("IC_USUARIO_CONSULTA");
                //logger.debug("Info de crédito encontrada: " + infoCrediticia.toString());
            }

        } catch (SQLException sqle) {
            logger.error("getInfoCrediticia",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            logger.error("getInfoCrediticia",e);
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
        return infoCrediticia;

    }

    public InformacionCrediticiaVO getLastInfoCrediticia(int idCliente) throws ClientesException {

        String query = "SELECT IC_NUMSOLICITUD, IC_TIPO_RESPUESTA, IC_RESPUESTA_CONSULTA, IC_FECHA_CONSULTA, IC_IDPROVIDER, IC_USUARIO_CONSULTA FROM D_INFORMACION_CREDITICIA "
                + "WHERE IC_NUMCLIENTE = ? AND IC_NUMOBLIGADO=0 AND IC_NUMSOCIEDAD=2 ORDER BY IC_NUMSOLICITUD DESC LIMIT 1";

        InformacionCrediticiaVO infoCrediticia = null;
        Connection cn = null;

        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);

            logger.debug("Ejecutando = " + ps);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                infoCrediticia = new InformacionCrediticiaVO();
                infoCrediticia.idCliente = idCliente;
                infoCrediticia.idSolicitud = rs.getInt("IC_NUMSOLICITUD");;
                infoCrediticia.idObligado = 0;
                infoCrediticia.idSociedad = 2;
                infoCrediticia.idTipoRespuesta = rs.getInt("IC_TIPO_RESPUESTA");
                infoCrediticia.respuesta = rs.getString("IC_RESPUESTA_CONSULTA");
                infoCrediticia.fechaConsulta = rs.getDate("IC_FECHA_CONSULTA");
                infoCrediticia.idProvider = rs.getInt("IC_IDPROVIDER");
                infoCrediticia.usuarioConsulta = rs.getString("IC_USUARIO_CONSULTA");
                logger.debug("Info de crédito encontrada: " + infoCrediticia.toString());
            }

        } catch (SQLException sqle) {
            logger.error("getLastInfoCrediticia",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            logger.error("getLastInfoCrediticia",e);
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
        return infoCrediticia;

    }

    public int addInfoCrediticia(InformacionCrediticiaVO infoCredito) throws ClientesException {

        String query = "INSERT INTO D_INFORMACION_CREDITICIA (IC_NUMCLIENTE, IC_NUMSOLICITUD, IC_NUMOBLIGADO, IC_NUMSOCIEDAD, IC_TIPO_RESPUESTA, "
                + "IC_RESPUESTA_CONSULTA, IC_FECHA_CONSULTA, IC_IDPROVIDER,IC_USUARIO_CONSULTA) VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?,?)";

        Connection cn = null;
        PreparedStatement ps = null;

        int param = 1;
        int res = 0;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(param++, infoCredito.idCliente);
            ps.setInt(param++, infoCredito.idSolicitud);
            ps.setInt(param++, infoCredito.idObligado);
            ps.setInt(param++, infoCredito.idSociedad);
            ps.setInt(param++, infoCredito.idTipoRespuesta);
            ps.setString(param++, infoCredito.respuesta);
            ps.setInt(param++, infoCredito.idProvider);
            ps.setString(param++, infoCredito.usuarioConsulta);
            //logger.debug("Ejecutando = " + query);
            logger.debug("Ejecutando = "+ ps);
            res = ps.executeUpdate();
            logger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            logger.error("addInfoCrediticia",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            logger.error("addInfoCrediticia",e);
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

    public int delInfoCrediticia(InformacionCrediticiaVO infoCredito) throws ClientesException {

        String query = "DELETE FROM D_INFORMACION_CREDITICIA WHERE IC_NUMCLIENTE = ? AND IC_NUMSOLICITUD = ? AND IC_NUMOBLIGADO = ? AND IC_NUMSOCIEDAD = ?";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(param++, infoCredito.idCliente);
            ps.setInt(param++, infoCredito.idSolicitud);
            ps.setInt(param++, infoCredito.idObligado);
            ps.setInt(param++, infoCredito.idSociedad);

            logger.debug("Ejecutando = " + query);
            res = ps.executeUpdate();
            logger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            logger.error("delInfoCrediticia",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            logger.error("delInfoCrediticia",e);
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
    public int delInfoCrediticia(Connection conn, int idCliente, int idSolicitud) throws ClientesException {

        int res = 0;
        String query = "DELETE FROM D_INFORMACION_CREDITICIA WHERE IC_NUMCLIENTE = ? AND IC_NUMSOLICITUD = ?";
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

            logger.debug("Ejecutando = " + ps);
            res = ps.executeUpdate();
            logger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            logger.error("delInfoCrediticia", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            logger.error("delInfoCrediticia", e);
            throw new ClientesException(e.getMessage());
        }finally {
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

    public int updateInfoCrediticia(InformacionCrediticiaVO infoCredito) throws ClientesException {

        String query = "UPDATE D_INFORMACION_CREDITICIA SET IC_RESPUESTA_CONSULTA = ?, IC_FECHA_CONSULTA = CURRENT_TIMESTAMP, IC_IDPROVIDER = ?, IC_USUARIO_CONSULTA = ? "
                + "WHERE IC_NUMCLIENTE = ? AND IC_NUMSOLICITUD = ? AND IC_NUMOBLIGADO = ? AND IC_NUMSOCIEDAD = ? AND IC_TIPO_RESPUESTA = ?";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setString(param++, infoCredito.respuesta);
            ps.setInt(param++, infoCredito.idProvider);
            ps.setString(param++, infoCredito.usuarioConsulta);
            ps.setInt(param++, infoCredito.idCliente);
            ps.setInt(param++, infoCredito.idSolicitud);
            ps.setInt(param++, infoCredito.idObligado);
            ps.setInt(param++, infoCredito.idSociedad);
            ps.setInt(param++, infoCredito.idTipoRespuesta);

            logger.debug("Ejecutando = " + query);
            res = ps.executeUpdate();
            logger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            logger.error("updateInfoCrediticia",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            logger.error("updateInfoCrediticia",e);
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


    public Date getMaxFechaConsulta(int idCliente) throws ClientesException {

        String query = "SELECT MAX(ic_fecha_consulta) AS maxfecha FROM d_informacion_crediticia WHERE ic_numcliente=?;";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Date fecha = new Date(System.currentTimeMillis());
        try {
            con = getConnection();
            ps = con.prepareStatement(query);
            ps.setInt(1, idCliente);
            rs = ps.executeQuery();
            while (rs.next()) {
                fecha = rs.getDate("maxfecha");
            }
        } catch (SQLException sqle) {
            logger.error("getMaxFechaConsulta",sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            logger.error("getMaxFechaConsulta",e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null)
                    con.close();
                if (ps != null)
                    ps.close();
                if (rs != null)
                    rs.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return fecha;

    }
}
