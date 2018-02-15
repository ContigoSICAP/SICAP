package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.SecurityUtil;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.SucursalVO;
import com.sicap.clientes.vo.UsuarioVO;
import com.sicap.clientes.vo.ColoniaVO;
import org.apache.log4j.Logger;

public class ColoniaDAO extends DAOMaster {

    private static Logger myLogger = Logger.getLogger(ColoniaDAO.class);

    public ColoniaVO getColonia(int numColonia) throws ClientesException {

        String query = "SELECT  CO_NUMCOLONIA, CO_NUMESTADO, CO_NUMMUNICIPIO, +"
                + "CO_CP, CO_NOMBRE_COLONIA, CO_ASENTAMIENTO_CP FROM C_COLONIAS where CO_NUMCOLONIA = ? ";
        ColoniaVO temporal = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, numColonia);
            //Logger.debug("Ejecutando = "+query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new ColoniaVO();
                //	temporal.idColonia = numColonia;
                temporal.idColonia = rs.getInt("CO_NUMCOLONIA");
                temporal.idEstado = rs.getInt("CO_NUMESTADO");
                temporal.idMunicipio = rs.getInt("CO_NUMMUNICIPIO");
                temporal.cp = rs.getInt("CO_CP");
                temporal.nombreColonia = rs.getString("CO_NOMBRE_COLONIA");
                temporal.asentamiento = rs.getString("CO_ASENTAMIENTO_CP");

            }
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
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return temporal;
    }

    public int getIdColonia(String cp, String codAsentamiento) throws ClientesException {

        String query = "SELECT * FROM C_COLONIAS WHERE CO_CP = ? AND CO_ASENTAMIENTO_CP = ?";
        ColoniaVO temporal = new ColoniaVO();
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setString(1, cp);
            ps.setString(2, codAsentamiento);
            myLogger.debug("Ejecutando = " + query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal.idColonia = rs.getInt("CO_NUMCOLONIA");
            }
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
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return temporal.idColonia;
    }

}
