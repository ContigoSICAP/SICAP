package com.sicap.clientes.dao;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.CatalogoVO;
import com.sicap.clientes.vo.LocalidadVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import org.apache.log4j.Logger;

/**
 *
 * @author LDAVILA
 */
public class LocalidadDAO extends DAOMaster {

    private static Logger myLogger = Logger.getLogger(CatalogoDAO.class);

    public LocalidadVO getLocalidad(int idColonia, int idLocalidad) throws ClientesException {

        String query = "SELECT * FROM C_LOCALIDADES LO, C_COLONIAS CO WHERE LO.LO_NUMESTADO = CO.CO_NUMESTADO "
                + "AND LO.LO_NUMMUNICIPIO=CO.CO_NUMMUNICIPIO AND LO.LO_NUMLOCALIDAD=? AND CO.CO_NUMCOLONIA=?";

        LocalidadVO temporal = null;
        Connection cn = null;
        try {
            cn = getConnection();
            PreparedStatement ps = cn.prepareStatement(query);
            ps.setInt(1, idLocalidad);
            ps.setInt(2, idColonia);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                temporal = new LocalidadVO();
                temporal.setNumLocalidad( idLocalidad);
                temporal.setNombreLocalidad(rs.getString("LO_DESCRIPCION"));
                temporal.setAmbito(rs.getString("LO_AMBITO"));                
            }
        } catch (SQLException sqle) {
            myLogger.error("getLocalidad", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("getLocalidad", e);
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
}
