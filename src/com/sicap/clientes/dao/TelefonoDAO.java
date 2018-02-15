package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.TelefonoVO;
import org.apache.log4j.Logger;

public class TelefonoDAO extends DAOMaster {

    private static Logger myLogger = Logger.getLogger(TelefonoDAO.class);

    public TelefonoVO[] getTelefonos(int idCliente, int idDireccion) throws ClientesException {

        String query = "SELECT TE_NUMCLIENTE, TE_NUMDIRECCION, TE_NUMTELEFONO, TE_TIPOTELEFONO, TE_TELEFONO, te_nomcontacto FROM D_TELEFONOS "
                + "WHERE TE_NUMCLIENTE = ? AND TE_NUMDIRECCION = ?";
        ArrayList<TelefonoVO> array = new ArrayList<TelefonoVO>();
        TelefonoVO temporal = null;
        TelefonoVO elementos[] = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.setInt(2, idDireccion);
            myLogger.debug("Ejecutando = " + query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new TelefonoVO();
                temporal.idCliente = idCliente;
                temporal.idCliente = idDireccion;
                temporal.idTelefono = rs.getInt("TE_NUMTELEFONO");
                temporal.tipoTelefono = rs.getInt("TE_TIPOTELEFONO");
                temporal.numeroTelefono = rs.getString("TE_TELEFONO");
                temporal.nomContacto = rs.getString("te_nomcontacto");
                array.add(temporal);
            }
            if (array.size() > 0) {
                elementos = new TelefonoVO[array.size()];
                for (int i = 0; i < elementos.length; i++) {
                    elementos[i] = (TelefonoVO) array.get(i);
                }
            }
            else
                myLogger.warn("No encontro telefonos para cliente "+idCliente+ ", direccion " + idDireccion);
        } catch (SQLException sqle) {
            myLogger.error("getTelefonos", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getTelefonos", e);
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

    /*	
     public TelefonoVO getTelefono(int idCliente, int idSolicitud, int idObligado) throws ClientesException{

     String query = "SELECT DI_NUMCLIENTE, DI_NUMSOLICITUD, DI_NUMOBLIGADO, DI_NUMDIRECCION, ES_NOMBRE, "+
     "MU_NOMBRE, DI_NUMCOLONIA, CO_NOMBRE_COLONIA, DI_CIUDAD, DI_CALLE, DI_NUMERO, CO_CP, "+
     "DI_SITUACION_VIVIENDA, DI_ANT_DOMICILIO FROM D_DIRECCIONES, C_COLONIAS, C_MUNICIPIOS, "+
     "C_ESTADOS WHERE DI_NUMCLIENTE = ? AND DI_NUMSOLICITUD = ? AND DI_NUMOBLIGADO = ? AND "+
     "DI_NUMCOLONIA = CO_NUMCOLONIA AND CO_NUMMUNICIPIO = MU_NUMMUNICIPIO AND "+
     "MU_NUMESTADO = ES_NUMESTADO AND CO_NUMESTADO = ES_NUMESTADO";
     TelefonoVO temporal = null;
     Connection cn = null;
     try{
     cn = getConnection();
     PreparedStatement ps = cn.prepareStatement(query);
     ps.setInt(1, idCliente);
     ps.setInt(2, idSolicitud);
     ps.setInt(3, idObligado);
     Logger.debug("Ejecutando = "+query);
     ResultSet rs = ps.executeQuery();
     while(rs.next()){
     temporal = new TelefonoVO();
     temporal.idCliente = idCliente;
     temporal.idSolicitud = rs.getInt("DI_NUMSOLICITUD");
     temporal.idObligado = rs.getInt("DI_NUMOBLIGADO");
     temporal.idDireccion= rs.getInt("DI_NUMDIRECCION");
     temporal.estado = rs.getString("ES_NOMBRE");
     temporal.municipio = rs.getString("MU_NOMBRE");
     temporal.idColonia = rs.getInt("DI_NUMCOLONIA");
     temporal.colonia = rs.getString("CO_NOMBRE_COLONIA");
     temporal.ciudad = rs.getString("DI_CIUDAD");
     temporal.calle = rs.getString("DI_CALLE");
     temporal.numero = rs.getString("DI_NUMERO");
     temporal.cp = rs.getString("CO_CP");
     temporal.situacionVivienda = rs.getInt("DI_SITUACION_VIVIENDA");
     temporal.antDomicilio = rs.getString("DI_ANT_DOMICILIO");
     }
     }catch(SQLException sqle) {
     Logger.debug("SQLException en getTelefonos : "+sqle.getMessage());
     sqle.printStackTrace();
     throw new ClientesDBException(sqle.getMessage());
     }
     catch(Exception e) {
     Logger.debug("Excepcion en getTelefonos : "+e.getMessage());
     e.printStackTrace();
     throw new ClientesException(e.getMessage());
     }
     finally {
     try {
     if ( cn!=null ) cn.close();
     }catch(SQLException e) {
     throw new ClientesDBException(e.getMessage());
     }
     }
     return temporal;

     }
     */
    public int addTelefono(Connection conn, int idCliente, int idDireccion, TelefonoVO telefono) throws ClientesException {

        String query = "INSERT INTO D_TELEFONOS (TE_NUMCLIENTE, TE_NUMDIRECCION, TE_NUMTELEFONO, TE_TIPOTELEFONO, TE_TELEFONO, te_nomcontacto) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        Connection cn = null;
        PreparedStatement ps = null;
        int param = 1;
        int res = 0;
        int next = getNext(idCliente, idDireccion);
        telefono.idTelefono = next;
        try {
            if (conn == null) {
                cn = getConnection();
                ps = cn.prepareStatement(query);
            } else {
                ps = conn.prepareStatement(query);
            }
            ps.setInt(param++, idCliente);
            ps.setInt(param++, idDireccion);
            ps.setInt(param++, telefono.idTelefono);
            ps.setInt(param++, telefono.tipoTelefono);
            ps.setString(param++, telefono.numeroTelefono);
            ps.setString(param++, telefono.nomContacto);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Para : " + telefono.toString());
            res = ps.executeUpdate();
            myLogger.debug("Resultado : " + res);
        } catch (SQLException sqle) {
            myLogger.error("addTelefono", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("addTelefono", e);
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

    public int updateTelefono(Connection conn, int idCliente, int idDireccion, TelefonoVO telefono) throws ClientesException {

        int res = 0;
        String query = "UPDATE D_TELEFONOS SET TE_TELEFONO = ? WHERE TE_NUMCLIENTE = ? AND TE_NUMDIRECCION = ? AND TE_TIPOTELEFONO = ?";
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
            ps.setString(param++, telefono.numeroTelefono);
            ps.setInt(param++, idCliente);
            ps.setInt(param++, idDireccion);
            ps.setInt(param++, telefono.tipoTelefono);
            myLogger.debug("Ejecutando = " + query);
            myLogger.debug("Telefono = " + telefono.toString());
            res = ps.executeUpdate();
            myLogger.debug("Resultado : " + res);
        } catch (SQLException sqle) {
            myLogger.error("updateTelefono", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("updateTelefono", e);
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

    public int getNext(int idCliente, int idDireccion) throws ClientesException {

        String query = "SELECT COALESCE(MAX(TE_NUMTELEFONO),0)+1 AS NEXT FROM D_TELEFONOS WHERE "
                + "TE_NUMCLIENTE = ? AND TE_NUMDIRECCION = ?";
        Connection cn = null;
        int next = 1;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idCliente);
            ps.setInt(2, idDireccion);
            ResultSet rs = ps.executeQuery();
            myLogger.debug("Ejecutando = " + query);
            if (rs.next()) {
                next = rs.getInt("NEXT");
            }
            myLogger.debug("Resultado : " + next);
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
