package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.DireccionVO;
import org.apache.log4j.Logger;

public class DireccionDAO extends DAOMaster {
    
    static private Logger myLogger = Logger.getLogger(DireccionDAO.class);

    public DireccionVO[] getDirecciones(int idCliente) throws ClientesException {

        String query = "SELECT DI_NUMCLIENTE, DI_NUMSOLICITUD, DI_TABLA, DI_INDICE_TABLA, DI_NUMDIRECCION, ES_NOMBRE, ES_NUMESTADO, "
                + "MU_NUMMUNICIPIO, MU_NOMBRE, DI_NUMCOLONIA, DI_NUMLOCALIDAD, CO_NOMBRE_COLONIA, DI_CIUDAD, DI_CALLE,di_tipoAsentamiento,di_tipoVialidad, DI_NUMERO_EXT, DI_NUMERO_INT, CO_CP, CO_ASENTAMIENTO_CP, co_asentamiento_cp, "
                + "DI_SITUACION_VIVIENDA, DI_ANT_DOMICILIO FROM D_DIRECCIONES, C_COLONIAS, C_MUNICIPIOS, "
                + "C_ESTADOS WHERE DI_NUMCLIENTE = ? AND DI_NUMSOLICITUD = 0 AND DI_INDICE_TABLA = 1 AND "
                + "DI_NUMCOLONIA = CO_NUMCOLONIA AND CO_NUMMUNICIPIO = MU_NUMMUNICIPIO AND "
                + "MU_NUMESTADO = ES_NUMESTADO AND CO_NUMESTADO = ES_NUMESTADO";
        ArrayList<DireccionVO> array = new ArrayList<DireccionVO>();
        DireccionVO temporal = null;
        DireccionVO elementos[] = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            //myLogger.debug("Ejecutando = "+ps);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new DireccionVO();
                temporal.idCliente = idCliente;
                temporal.idSolicitud = rs.getInt("DI_NUMSOLICITUD");
                temporal.tabla = rs.getString("DI_TABLA");
                temporal.indiceTabla = rs.getInt("DI_INDICE_TABLA");
                temporal.idDireccion = rs.getInt("DI_NUMDIRECCION");
                temporal.estado = rs.getString("ES_NOMBRE");
                temporal.numestado = rs.getInt("ES_NUMESTADO");
                temporal.numMunicipio = rs.getInt("MU_NUMMUNICIPIO");
                temporal.municipio = rs.getString("MU_NOMBRE");
                temporal.idColonia = rs.getInt("DI_NUMCOLONIA");
                temporal.idLocalidad = rs.getInt("DI_NUMLOCALIDAD");
                temporal.colonia = rs.getString("CO_NOMBRE_COLONIA");
                temporal.ciudad = rs.getString("DI_CIUDAD");
                temporal.calle = rs.getString("DI_CALLE");
                temporal.tipoAsentamiento= rs.getInt("di_tipoAsentamiento");
                temporal.tipoVialidad= rs.getInt("di_tipoVialidad");
                temporal.numeroExterior = rs.getString("DI_NUMERO_EXT");
                temporal.numeroInterior = rs.getString("DI_NUMERO_INT");
                temporal.idColoniaSepomex = rs.getString("co_asentamiento_cp");
                temporal.cp = rs.getString("CO_CP");
                temporal.asentamiento_cp = rs.getString("CO_ASENTAMIENTO_CP");
                temporal.situacionVivienda = rs.getInt("DI_SITUACION_VIVIENDA");
                temporal.antDomicilio = rs.getString("DI_ANT_DOMICILIO");
                array.add(temporal);
//				myLogger.debug("Direccio encontrada : "+temporal.toString());
            }
            if (array.size() > 0) {
                elementos = new DireccionVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (DireccionVO) array.get(i);
                }
            }
        } catch (SQLException sqle) {
            myLogger.error("getDirecciones", sqle);
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
        return elementos;

    }

    public DireccionVO getDireccion(int idCliente, int idSolicitud, String tabla, int indiceTabla) throws ClientesException {

        String query = "SELECT DI_NUMCLIENTE, DI_NUMSOLICITUD, DI_TABLA, DI_INDICE_TABLA, DI_NUMDIRECCION, ES_NOMBRE, ES_NUMESTADO,"
                + "MU_NUMMUNICIPIO, MU_NOMBRE, DI_NUMCOLONIA, CO_NOMBRE_COLONIA, DI_CIUDAD, DI_CALLE, DI_NUMERO_EXT, DI_NUMERO_INT, CO_CP, CO_ASENTAMIENTO_CP, "
                + "DI_SITUACION_VIVIENDA, DI_ANT_DOMICILIO FROM D_DIRECCIONES, C_COLONIAS, C_MUNICIPIOS, "
                + "C_ESTADOS WHERE DI_NUMCLIENTE = ? AND DI_NUMSOLICITUD = ? AND DI_TABLA = ? AND DI_INDICE_TABLA = ? AND "
                + "DI_NUMCOLONIA = CO_NUMCOLONIA AND CO_NUMMUNICIPIO = MU_NUMMUNICIPIO AND "
                + "MU_NUMESTADO = ES_NUMESTADO AND CO_NUMESTADO = ES_NUMESTADO";
        DireccionVO temporal = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.setInt(2, idSolicitud);
            ps.setString(3, tabla);
            ps.setInt(4, indiceTabla);
            //myLogger.debug("Ejecutando = "+query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                temporal = new DireccionVO();
                temporal.idCliente = idCliente;
                temporal.idSolicitud = rs.getInt("DI_NUMSOLICITUD");
                temporal.tabla = rs.getString("DI_TABLA");
                temporal.indiceTabla = rs.getInt("DI_INDICE_TABLA");
                temporal.idDireccion = rs.getInt("DI_NUMDIRECCION");
                temporal.estado = rs.getString("ES_NOMBRE");
                temporal.numestado = rs.getInt("ES_NUMESTADO");
                temporal.numMunicipio = rs.getInt("MU_NUMMUNICIPIO");
                temporal.municipio = rs.getString("MU_NOMBRE");
                temporal.idColonia = rs.getInt("DI_NUMCOLONIA");
                temporal.colonia = rs.getString("CO_NOMBRE_COLONIA");
                temporal.ciudad = rs.getString("DI_CIUDAD");
                temporal.calle = rs.getString("DI_CALLE");
                temporal.numeroExterior = rs.getString("DI_NUMERO_EXT");
                temporal.numeroInterior = rs.getString("DI_NUMERO_INT");
                temporal.cp = rs.getString("CO_CP");
                temporal.asentamiento_cp = rs.getString("CO_ASENTAMIENTO_CP");
                temporal.situacionVivienda = rs.getInt("DI_SITUACION_VIVIENDA");
                temporal.antDomicilio = rs.getString("DI_ANT_DOMICILIO");
//				myLogger.debug("Direccio encontrada : "+temporal.toString());
            }
        } catch (SQLException sqle) {
            myLogger.error("getDireccion", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getDireccion", e);
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

    public DireccionVO getDireccionLocalidad(int idCliente, int idSolicitud, String tabla, int indiceTabla) throws ClientesException {

        String query = "SELECT DI_NUMCLIENTE, DI_NUMSOLICITUD, DI_TABLA, DI_INDICE_TABLA, DI_NUMDIRECCION, ES_NOMBRE, ES_NUMESTADO, "
                + "MU_NOMBRE, DI_NUMCOLONIA, DI_NUMLOCALIDAD, LO_DESCRIPCION, CO_NOMBRE_COLONIA, DI_CIUDAD, DI_CALLE, DI_NUMERO_EXT, "
                + "DI_NUMERO_INT, CO_CP, CO_ASENTAMIENTO_CP, DI_SITUACION_VIVIENDA, DI_ANT_DOMICILIO "
                + "FROM D_DIRECCIONES, C_COLONIAS, C_MUNICIPIOS, C_ESTADOS, C_LOCALIDADES "
                + "WHERE DI_NUMCLIENTE = ? AND DI_NUMSOLICITUD = ? AND DI_TABLA = ? AND DI_INDICE_TABLA = ? AND "
                + "DI_NUMCOLONIA = CO_NUMCOLONIA AND CO_NUMMUNICIPIO = MU_NUMMUNICIPIO AND "
                + "MU_NUMESTADO = ES_NUMESTADO AND CO_NUMESTADO = ES_NUMESTADO AND "
                + "MU_NUMESTADO = LO_NUMESTADO AND MU_NUMMUNICIPIO = LO_NUMMUNICIPIO AND "
                + "LO_NUMLOCALIDAD = DI_NUMLOCALIDAD;";
        DireccionVO temporal = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.setInt(2, idSolicitud);
            ps.setString(3, tabla);
            ps.setInt(4, indiceTabla);
//			myLogger.debug("Ejecutando = "+query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                temporal = new DireccionVO();
                temporal.idCliente = idCliente;
                temporal.idSolicitud = rs.getInt("DI_NUMSOLICITUD");
                temporal.tabla = rs.getString("DI_TABLA");
                temporal.indiceTabla = rs.getInt("DI_INDICE_TABLA");
                temporal.idDireccion = rs.getInt("DI_NUMDIRECCION");
                temporal.estado = rs.getString("ES_NOMBRE");
                temporal.numestado = rs.getInt("ES_NUMESTADO");
                temporal.municipio = rs.getString("MU_NOMBRE");
                temporal.idColonia = rs.getInt("DI_NUMCOLONIA");
                temporal.idLocalidad = rs.getInt("DI_NUMLOCALIDAD");
                temporal.localidad = rs.getString("LO_DESCRIPCION");
                temporal.colonia = rs.getString("CO_NOMBRE_COLONIA");
                temporal.ciudad = rs.getString("DI_CIUDAD");
                temporal.calle = rs.getString("DI_CALLE");
                temporal.numeroExterior = rs.getString("DI_NUMERO_EXT");
                temporal.numeroInterior = rs.getString("DI_NUMERO_INT");
                temporal.cp = rs.getString("CO_CP");
                temporal.asentamiento_cp = rs.getString("CO_ASENTAMIENTO_CP");
                temporal.situacionVivienda = rs.getInt("DI_SITUACION_VIVIENDA");
                temporal.antDomicilio = rs.getString("DI_ANT_DOMICILIO");
//				myLogger.debug("Direccio encontrada : "+temporal.toString());
            }
        } catch (SQLException sqle) {
            myLogger.error("getDireccionLocalidad", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getDireccionLocalidad", e);
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

    public int addDireccion(Connection conn, int idCliente, DireccionVO direccion) throws ClientesException {
        return addDireccion(conn, idCliente, 0, "d_clientes", 1, direccion);
    }

    public int addDireccion(Connection conn, int idCliente, int idSolicitud, String tabla, int indiceTabla, DireccionVO direccion) throws ClientesException {

        String query = "INSERT INTO D_DIRECCIONES (DI_NUMCLIENTE, DI_NUMSOLICITUD, DI_TABLA, DI_INDICE_TABLA, "
                + "DI_NUMDIRECCION, DI_NUMCOLONIA, DI_CIUDAD, DI_CALLE, DI_NUMERO_EXT, DI_NUMERO_INT, DI_SITUACION_VIVIENDA, "
                + "DI_ANT_DOMICILIO, DI_NUMLOCALIDAD, di_tipoAsentamiento,di_tipoVialidad ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        int next = 0;
        if (direccion.idDireccion == 0) {
            next = getNext(idCliente, idSolicitud, tabla, indiceTabla);
            direccion.idDireccion = next;
        }
        try {
            if (conn == null) {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            } else {
                ps = conn.prepareStatement(query);
            }

            ps.setInt(param++, idCliente);
            ps.setInt(param++, idSolicitud);
            ps.setString(param++, tabla);
            ps.setInt(param++, indiceTabla);
            ps.setInt(param++, direccion.idDireccion);
            ps.setInt(param++, direccion.idColonia);
            ps.setString(param++, direccion.ciudad);
            ps.setString(param++, direccion.calle);
            ps.setString(param++, direccion.numeroExterior);
            ps.setString(param++, direccion.numeroInterior);
            ps.setInt(param++, direccion.situacionVivienda);
            ps.setString(param++, direccion.antDomicilio);
            ps.setInt(param++, direccion.idLocalidad);
            ps.setInt(param++, direccion.tipoAsentamiento);
            ps.setInt(param++, direccion.tipoVialidad);
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
        return next;
    }

    public int updateDireccion(Connection conn, int idCliente, DireccionVO direccion) throws ClientesException {
        return updateDireccion(conn, idCliente, 0, "d_clientes", 1, direccion);
    }

    public int updateDireccion(Connection conn, int idCliente, int idSolicitud, String tabla, int indiceTabla, DireccionVO direccion) throws ClientesException {

        int res = 0;
        String query = "UPDATE D_DIRECCIONES SET DI_NUMCOLONIA=?, DI_CIUDAD=?, DI_CALLE=?, DI_NUMERO_EXT=?, DI_NUMERO_INT=?, "
                + "DI_SITUACION_VIVIENDA=?, DI_ANT_DOMICILIO=?, DI_NUMLOCALIDAD=? , di_tipoAsentamiento= ?, di_tipoVialidad=? WHERE DI_NUMCLIENTE = ? AND "
                + "DI_NUMSOLICITUD = ? AND DI_TABLA = ? AND DI_INDICE_TABLA = ? AND DI_NUMDIRECCION = ?";
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
            ps.setInt(param++, direccion.situacionVivienda);
            ps.setString(param++, direccion.antDomicilio);
            ps.setInt(param++, direccion.idLocalidad);
            ps.setInt(param++, direccion.tipoAsentamiento);
            ps.setInt(param++, direccion.tipoVialidad);
            ps.setInt(param++, idCliente);
            ps.setInt(param++, idSolicitud);
            ps.setString(param++, tabla);
            ps.setInt(param++, indiceTabla);
            ps.setInt(param++, direccion.idDireccion);
            myLogger.debug("Ejecutando = " + ps);
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

    public int updateDireccionesAdicionales(Connection conn, int idCliente, DireccionVO direccion, int numDireccion) throws ClientesException {
        return updateDireccionesAdicionales(conn, idCliente, 0, "d_clientes", 1, direccion, numDireccion);
    }

    public int updateDireccionesAdicionales(Connection conn, int idCliente, int idSolicitud, String tabla, int indiceTabla, DireccionVO direccion, int numDireccion) throws ClientesException {

        int res = 0;
        String query = "UPDATE D_DIRECCIONES SET DI_NUMDIRECCION = ?, DI_NUMCOLONIA=?, DI_CIUDAD=?, DI_CALLE=?, DI_NUMERO_EXT=?, DI_NUMERO_INT=?, "
                + "DI_SITUACION_VIVIENDA=?, DI_ANT_DOMICILIO=?, DI_NUMLOCALIDAD=? WHERE DI_NUMCLIENTE = ? AND "
                + "DI_NUMSOLICITUD = ? AND DI_TABLA = ? AND DI_INDICE_TABLA = ? AND DI_NUMDIRECCION = ? ";
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
            ps.setInt(param++, direccion.idDireccion);
            ps.setInt(param++, direccion.idColonia);
            ps.setString(param++, direccion.ciudad);
            ps.setString(param++, direccion.calle);
            ps.setString(param++, direccion.numeroExterior);
            ps.setString(param++, direccion.numeroInterior);
            ps.setInt(param++, direccion.situacionVivienda);
            ps.setString(param++, direccion.antDomicilio);
            ps.setInt(param++, direccion.idLocalidad);
            ps.setInt(param++, idCliente);
            ps.setInt(param++, idSolicitud);
            ps.setString(param++, tabla);
            ps.setInt(param++, indiceTabla);
            ps.setInt(param++, numDireccion);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Direccion = " + direccion.toString());
            res = ps.executeUpdate();
            myLogger.debug("Resultado = " + res);
        } catch (SQLException sqle) {
            myLogger.error("updateDireccionesAdicionales", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateDireccionesAdicionales", e);
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

    public int getNext(int idCliente, int idSolicitud, String tabla, int indiceTabla) throws ClientesException {

        String query = "SELECT COALESCE(MAX(DI_NUMDIRECCION),0)+1 AS NEXT FROM D_DIRECCIONES WHERE "
                + "DI_NUMCLIENTE = ? AND DI_NUMSOLICITUD = ? AND DI_TABLA = ? AND DI_INDICE_TABLA = ?";
        Connection cn = null;
        int next = 1;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.setInt(2, idSolicitud);
            ps.setString(3, tabla);
            ps.setInt(4, indiceTabla);
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

    public int countDirecciones(int idCliente) throws ClientesException {

        String query = "SELECT COUNT(*) FROM D_DIRECCIONES WHERE DI_NUMCLIENTE=? AND "
                + "DI_TABLA='d_clientes' AND DI_INDICE_TABLA=1 AND DI_NUMSOLICITUD=0";
        Connection cn = null;
        int next = 1;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();
            myLogger.debug("Ejecutando = " + query);
            if (rs.next()) {
                next = rs.getInt("COUNT(*)");
            }
            myLogger.debug("Resultado = " + next);
        } catch (SQLException sqle) {
            myLogger.error("countDirecciones", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("countDirecciones", e);
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
    
    public int getLocalidadFommur(int numEstado, int numMunicipio, int numLocalidad) throws ClientesException {

        String query = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int acepta = 0;
        try {
            con = getConnection();
            query = "SELECT lo_aceptafommur FROM c_localidades WHERE lo_numestado=? AND lo_nummunicipio=? AND lo_numlocalidad=?;";
            ps = con.prepareStatement(query);
            ps.setInt(1, numEstado);
            ps.setInt(2, numMunicipio);
            ps.setInt(3, numLocalidad);
            rs = ps.executeQuery();
            myLogger.debug("Ejecutando = " + ps);
            if (rs.next()) {
                acepta = rs.getInt("lo_aceptafommur");
            }
        } catch (SQLException sqle) {
            myLogger.error("getLocalidadFommur", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getLocalidadFommur", e);
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
        return acepta;
    }
    public int getMunicipioFommur2(int numEstado, int numMunicipio) throws ClientesException {//Busca los municipios aceptados para el segundo fondeo

        String query = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int acepta = 0;
        try {
            con = getConnection();
            query = "Select mu_aceptafommur" +
                    " from c_municipios" +
                    " where mu_numestado =?" +
                    " and mu_nummunicipio = ?";
            ps = con.prepareStatement(query);
            ps.setInt(1, numEstado);
            ps.setInt(2, numMunicipio);
            rs = ps.executeQuery();
            myLogger.debug("Ejecutando = " + ps);
            if (rs.next()) {
                acepta = rs.getInt("mu_aceptafommur");
            }
        } catch (SQLException sqle) {
            myLogger.error("getLocalidadFommur", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getLocalidadFommur", e);
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
        return acepta;
    }
    
    public int validaLocalidadFommur(int numcolonia, int numLocalidad) throws ClientesException {

        String query = "";
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int acepta = 0;
        try {
            con = getConnection();
            query = "Select lo_aceptafommur from c_localidades, c_colonias " +
                    " where lo_numestado = co_numestado" +
                    " and lo_nummunicipio =co_nummunicipio" +
                    " and lo_numlocalidad = ?" +
                    " and co_numcolonia = ?";
            ps = con.prepareStatement(query);
            ps.setInt(1, numLocalidad);
            ps.setInt(2, numcolonia);
            rs = ps.executeQuery();
            myLogger.debug("Ejecutando = " + ps);
            if (rs.next()) {
                acepta = rs.getInt("lo_aceptafommur");
            }
        } catch (SQLException sqle) {
            myLogger.error("getLocalidadFommur", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getLocalidadFommur", e);
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
        return acepta;
    }
     public int deleteDirEmpleo(Connection conn,int idCliente, int idSolicitud) throws ClientesException {

        int res = 0;
        String query = "DELETE FROM d_direcciones" +
                       " where di_numcliente = ?" +
                       " and di_numsolicitud = ?" +
                       " and di_tabla = \'d_empleos\'";
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
            myLogger.error("deleteDirEmpleo", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("deleteDirEmpleo", e);
            throw new ClientesException(e.getMessage());
        } 
        return res;
    }

}
