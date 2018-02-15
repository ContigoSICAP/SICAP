package com.sicap.clientes.dao;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.vo.FondeadorVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.naming.NamingException;
import org.apache.log4j.Logger;

/**
 *
 * @author ERojano
 */
public class FondeadorDAO extends DAOMaster{
    
    private static final Logger myLogger = Logger.getLogger(FondeadorDAO.class);
    private Connection con = null;
    private PreparedStatement pstm = null;
    private ResultSet res = null;
    private String query = "";
    
    public int insertFondeador(FondeadorVO fondeoVO) throws ClientesDBException, NamingException{
        
        int inserto = 0;
        query = "INSERT INTO c_fondeadores(fo_nombre, fo_conPreseleccion, fo_status, fo_prioridad, fo_isrevolvente) VALUES (?, ?, ?, ?, ?);";
        try {
            con = getConnection();
            pstm = con.prepareStatement(query);
            pstm.setString(1, fondeoVO.getNombre());
            pstm.setInt(2, fondeoVO.getPreSeleccionCartera());
            pstm.setInt(3, fondeoVO.getEstatus());
            pstm.setInt(4, fondeoVO.getPrioridad());
            pstm.setInt(5, fondeoVO.getRevolvente());
            myLogger.debug("pstm "+pstm);
            inserto = pstm.executeUpdate();
        } catch (SQLException e) {
            myLogger.error("Erro al crear fondeador", e);
        } finally{
            try {
                if (con != null)
                    con.close();
                if (pstm != null)
                    pstm.close();
            } catch (SQLException e) {
                throw new ClientesDBException(e.getMessage());
            }
        }
        return inserto;
    }
      
    public int obtenerUltimaPrioridad() throws ClientesDBException, NamingException{
        
        int ultimaPrioridad = 0;
        query = "SELECT max(fo_prioridad) as ultimaPrioridad FROM c_fondeadores";
        try {
            con = getConnection();
            pstm = con.prepareStatement(query);
            res = pstm.executeQuery();
            while(res.next())
                ultimaPrioridad = res.getInt("ultimaPrioridad");
        } catch (SQLException e) {
            myLogger.error("Erro al obtener la ultima prioridad()", e);
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
        return ultimaPrioridad;
    }
    
    public FondeadorVO obtenerFondeador(int idFondeador) throws ClientesDBException, NamingException{
        
        FondeadorVO fond = null;
        query = "SELECT * FROM c_fondeadores WHERE fo_numfondeador = ?";
        try {
            con = getConnection();
            pstm = con.prepareStatement(query);
            pstm.setInt(1, idFondeador);
            res = pstm.executeQuery();
            while(res.next()){
                fond = new FondeadorVO(res.getString("fo_nombre"), res.getInt("fo_conPreseleccion"), res.getInt("fo_status"), res.getInt("fo_prioridad"), res.getInt("fo_isrevolvente"));
                fond.setNumFondeador(res.getInt("fo_numfondeador"));
            }
        } catch (SQLException e) {
            myLogger.error("Erro al obtener fondeador()", e);
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
        return fond;
    }
    
        public List<FondeadorVO> obtenerFondeadores() throws ClientesDBException, NamingException{
        
        List<FondeadorVO> fondeadores = new ArrayList<FondeadorVO>();
        query = "SELECT * FROM c_fondeadores order by fo_prioridad";
        try {
            con = getConnection();
            pstm = con.prepareStatement(query);
            res = pstm.executeQuery();
            while(res.next()){
                FondeadorVO fond = new FondeadorVO(res.getString("fo_nombre"), res.getInt("fo_conPreseleccion"), res.getInt("fo_status"), res.getInt("fo_prioridad"), res.getInt("fo_isrevolvente"));
                fond.setNumFondeador(res.getInt("fo_numfondeador"));
                fondeadores.add(fond);
            }
        } catch (SQLException e) {
            myLogger.error("Erro al obtener fondeador()", e);
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
        return fondeadores;
    }
}

