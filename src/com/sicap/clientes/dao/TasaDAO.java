package com.sicap.clientes.dao;

import com.sicap.clientes.exceptions.ClientesDBException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

/**
 *
 * @author avillanueva
 */
public class TasaDAO extends DAOMaster{
    
    static private Logger myLogger = Logger.getLogger(TasaDAO.class);
    
        public boolean getTasaAutorizadaFommmur(int idTasa) throws ClientesDBException{
        
        boolean aplicaTasa = false;
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet res = null;
        String query = "SELECT tg_tasa_fommur FROM c_tasas_grupal WHERE tg_numtasa=?;";
        try {
            con = getConnection();
            pstm = con.prepareStatement(query);
            pstm.setInt(1, idTasa);
            myLogger.debug("pstm "+pstm);
            res = pstm.executeQuery();
            if (res.next()) {
                aplicaTasa = res.getBoolean("tg_tasa_fommur");
            }
        } catch (SQLException e) {
            myLogger.error("Erro en getTasaAutorizadaFommmur", e);
        } catch (Exception e) {
            myLogger.error("Erro en getTasaAutorizadaFommmur", e);
        } finally{
            try {
                if (con != null)
                    con.close();
                if (pstm != null)
                    pstm.close();
                if (res != null)
                    res.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return aplicaTasa;
    }
}
