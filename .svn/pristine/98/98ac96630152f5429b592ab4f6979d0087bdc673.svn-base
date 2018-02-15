package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.BitacoraVO;
import org.apache.log4j.Logger;

public class BitacoraDAO extends DAOMaster {

    private static Logger myLogger = Logger.getLogger(BitacoraDAO.class);
    
    public int add(BitacoraVO bitacora) throws ClientesException {

        String query = "INSERT INTO D_BITACORA (BI_NUMCLIENTE, BI_USUARIO, BI_COMANDO, BI_OBJETO, BI_FECHA_HORA) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";

        Connection cn = null;
        int param = 1;
        int res = 0;
        try {
            PreparedStatement ps = null;
            cn = getConnection();
            ps = cn.prepareStatement(query);
            ps.setInt(param++, bitacora.idCliente);
            ps.setString(param++, bitacora.usuario);
            ps.setString(param++, bitacora.comando);
            ps.setString(param++, bitacora.objeto.toString());
            myLogger.debug("Ejecutando = "+query);
            res = ps.executeUpdate();
            //myLogger.debug("Resultado = "+res);
        } catch (SQLException sqle) {
            myLogger.error("add", sqle);
            throw new ClientesDBException(sqle.getMessage());
        } catch (Exception e) {
            myLogger.error("add", e);
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