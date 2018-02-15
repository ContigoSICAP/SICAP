package com.sicap.clientes.dao;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.InformacionCrediticiaVO;
import com.sicap.clientes.vo.ScoreFicoVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class ScoreCirculoCreditoDAO extends DAOMaster {

    private static Logger myLogger = Logger.getLogger(ScoreCirculoCreditoDAO.class);
    private Connection con = null;
    private PreparedStatement pstm = null;
    private ResultSet res = null;
    private String query = "";

    public void setScore(ScoreFicoVO score) throws ClientesException {

        query = "INSERT INTO d_score_fico(sf_numcliente,sf_numsolicitud,sf_nombre,sf_codigo,sf_valor,sf_razon1,sf_razon2,sf_razon3,sf_razon4,sf_fechacaptura) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        try {
            con = getConnection();
            pstm = con.prepareStatement(query);
            pstm.setInt(1, score.getIdCliente());
            pstm.setInt(2, score.getIdSolicitud());
            pstm.setString(3, score.getNombre());
            pstm.setInt(4, score.getCodigo());
            pstm.setInt(5, score.getValor());
            pstm.setString(6, score.getRazon1());
            pstm.setString(7, score.getRazon2());
            pstm.setString(8, score.getRazon3());
            pstm.setString(9, score.getRazon4());
            pstm.setDate(10, score.getFechaCaptura());
            myLogger.debug("Ejecutando = " + query);
            pstm.executeUpdate();
        } catch (SQLException se) {
            myLogger.error("setScore",se);
            throw new ClientesException(se.getMessage());
        } catch (Exception e) {
            myLogger.error("setScore",e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException se) {
                se.printStackTrace();
                throw new ClientesDBException(se.getMessage());
            }
        }
    }
    
    public int deleteScore(InformacionCrediticiaVO infoCredito) throws ClientesException {

        int res = 0;
        query = "DELETE FROM d_score_fico WHERE sf_numcliente=? AND sf_numsolicitud=?;";
        try {
            con = getConnection();
            pstm = con.prepareStatement(query);
            pstm.setInt(1, infoCredito.idCliente);
            pstm.setInt(2, infoCredito.idSolicitud);
            myLogger.debug("Ejecutando= "+pstm);
            pstm.executeUpdate();
        } catch (SQLException se) {
            myLogger.error("deleteScore",se);
            throw new ClientesException(se.getMessage());
        } catch (Exception e) {
            myLogger.error("deleteScore",e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null)
                    con.close();
                if (pstm != null)
                    pstm.close();
            } catch (SQLException se) {
                se.printStackTrace();
                throw new ClientesDBException(se.getMessage());
            }
        }
        return res;
    }

    public int getValorScore(int idCliente) throws ClientesException {

        int valor = -1;
        query = "SELECT MAX(sf_valor) AS valor FROM d_score_fico WHERE sf_numcliente=?;";
        try {
            con = getConnection();
            pstm = con.prepareStatement(query);
            pstm.setInt(1, idCliente);
            res = pstm.executeQuery();
            while (res.next()) {
                valor = res.getInt("valor");
            }
            myLogger.debug("Ejecutando = " + query);
        } catch (SQLException se) {
            myLogger.error("getValorScore",se);
            throw new ClientesException(se.getMessage());
        } catch (Exception e) {
            myLogger.error("getValorScore",e);
            throw new ClientesException(e.getMessage());
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException se) {
                myLogger.error("getValorScore",se);
                throw new ClientesDBException(se.getMessage());
            }
        }
        return valor;
    }

}
