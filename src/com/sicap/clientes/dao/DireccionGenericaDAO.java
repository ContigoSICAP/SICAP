package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.DireccionGenericaVO;
import org.apache.log4j.Logger;

public class DireccionGenericaDAO extends DAOMaster {

    private static Logger myLogger = Logger.getLogger(DireccionGenericaDAO.class);

    public DireccionGenericaVO getDireccion(int idDireccion) throws ClientesException {

        String query = "SELECT DG_NUMDIRECCION, ES_NUMESTADO, ES_NOMBRE, MU_NUMMUNICIPIO, MU_NOMBRE, DG_NUMCOLONIA, CO_NOMBRE_COLONIA, "
                + "DG_CIUDAD, DG_CALLE, DG_NUMERO_EXT, DG_NUMERO_INT, CO_CP, CO_ASENTAMIENTO_CP, DG_FECHA_CAPTURA "
                + "FROM D_DIRECCIONES_GENERICAS, C_COLONIAS, C_MUNICIPIOS, C_ESTADOS WHERE DG_NUMDIRECCION = ? "
                + "AND DG_NUMCOLONIA = CO_NUMCOLONIA AND CO_NUMMUNICIPIO = MU_NUMMUNICIPIO AND "
                + "MU_NUMESTADO = ES_NUMESTADO AND CO_NUMESTADO = ES_NUMESTADO";
        DireccionGenericaVO temporal = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idDireccion);
//			myLogger.debug("Ejecutando = "+query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new DireccionGenericaVO();
                temporal.idDireccion = rs.getInt("DG_NUMDIRECCION");
                temporal.idEstado = rs.getInt("ES_NUMESTADO");
                temporal.estado = rs.getString("ES_NOMBRE");
                temporal.idMunicipio = rs.getInt("MU_NUMMUNICIPIO");
                temporal.municipio = rs.getString("MU_NOMBRE");
                temporal.idColonia = rs.getInt("DG_NUMCOLONIA");
                temporal.colonia = rs.getString("CO_NOMBRE_COLONIA");
                temporal.ciudad = rs.getString("DG_CIUDAD");
                temporal.calle = rs.getString("DG_CALLE");
                temporal.numeroExterior = rs.getString("DG_NUMERO_EXT");
                temporal.numeroInterior = rs.getString("DG_NUMERO_INT");
                temporal.cp = rs.getString("CO_CP");
                temporal.asentamiento_cp = rs.getString("CO_ASENTAMIENTO_CP");
                temporal.fechaCaptura = rs.getTimestamp("DG_FECHA_CAPTURA");
                myLogger.debug("Direccion encontrada : " + temporal.toString());
            }
        } catch (SQLException sqle) {
            myLogger.error("getDirecciones", sqle);
            sqle.printStackTrace();
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getDirecciones", e);
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

    public int addDireccion(Connection conn, DireccionGenericaVO direccion) throws ClientesException {

        String query = "INSERT INTO D_DIRECCIONES_GENERICAS (DG_NUMDIRECCION, DG_NUMCOLONIA, DG_CIUDAD, DG_CALLE, DG_NUMERO_EXT, "
                + "DG_NUMERO_INT, DG_FECHA_CAPTURA) VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        direccion.idDireccion = getNext();
        try {
            if (conn == null) {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            } else {
                ps = conn.prepareStatement(query);
            }

            ps.setInt(param++, direccion.idDireccion);
            ps.setInt(param++, direccion.idColonia);
            ps.setString(param++, direccion.ciudad);
            ps.setString(param++, direccion.calle);
            ps.setString(param++, direccion.numeroExterior);
            ps.setString(param++, direccion.numeroInterior);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("direccion = " + direccion);
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("addDireccion", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addDireccion", e);
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
        return direccion.idDireccion;
    }

    public int addDireccionApertura(DireccionGenericaVO direccion) throws ClientesException {

        String query = "INSERT INTO D_DIRECCIONES_GENERICAS (DG_NUMDIRECCION, DG_NUMCOLONIA, DG_CIUDAD, DG_CALLE, DG_NUMERO_EXT, "
                + "DG_NUMERO_INT, DG_FECHA_CAPTURA) VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP)";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        direccion.idDireccion = getNext();
        try {
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(param++, direccion.idDireccion);
            ps.setInt(param++, direccion.idColonia);
            ps.setString(param++, direccion.ciudad);
            ps.setString(param++, direccion.calle);
            ps.setString(param++, direccion.numeroExterior);
            ps.setString(param++, direccion.numeroInterior);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("direccion = " + direccion);
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("addDireccionApertura", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addDireccionApertura", e);
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
        return direccion.idDireccion;
    }

    public int updateDireccion(Connection conn, DireccionGenericaVO direccion) throws ClientesException {

        int res = 0;
        String query = "UPDATE D_DIRECCIONES_GENERICAS SET DG_NUMCOLONIA=?, DG_CIUDAD=?, DG_CALLE=?, DG_NUMERO_EXT=?, DG_NUMERO_INT=? WHERE DG_NUMDIRECCION = ?";
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
            ps.setInt(param++, direccion.idColonia);
            ps.setString(param++, direccion.ciudad);
            ps.setString(param++, direccion.calle);
            ps.setString(param++, direccion.numeroExterior);
            ps.setString(param++, direccion.numeroInterior);
            ps.setInt(param++, direccion.idDireccion);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Direccion = " + direccion.toString());
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("updateDireccion", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateDireccion", e);
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

    public int updateDireccionApertura(DireccionGenericaVO direccion) throws ClientesException {

        int res = 0;
        String query = "UPDATE D_DIRECCIONES_GENERICAS SET DG_NUMCOLONIA=?, DG_CIUDAD=?, DG_CALLE=?, DG_NUMERO_EXT=?, DG_NUMERO_INT=? WHERE DG_NUMDIRECCION = ?";
        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        try {
            //if( conn==null ){
            cn = getConnection();
            ps = cn.prepareStatement(query);
            /*}
             else{
             ps = conn.prepareStatement(query);
             }*/
            ps.setInt(param++, direccion.idColonia);
            ps.setString(param++, direccion.ciudad);
            ps.setString(param++, direccion.calle);
            ps.setString(param++, direccion.numeroExterior);
            ps.setString(param++, direccion.numeroInterior);
            ps.setInt(param++, direccion.idDireccion);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Direccion = " + direccion.toString());
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("updateDireccionApertura", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateDireccionApertura", e);
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

    public int getNext() throws ClientesException {

        String query = "SELECT COALESCE(MAX(DG_NUMDIRECCION),0)+1 AS NEXT FROM D_DIRECCIONES_GENERICAS";
        Connection cn = null;
        int next = 1;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
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

}
