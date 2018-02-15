package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.MetasEjecutivosVO;


public class MetasEjecutivosDAO extends DAOMaster{
    
    private static org.apache.log4j.Logger myLogger = org.apache.log4j.Logger.getLogger(MetasEjecutivosDAO.class);

	public int addMeta(MetasEjecutivosVO meta) throws ClientesException{

		String query =	"INSERT INTO D_METAS_EJECUTIVOS (ME_NUMEJECUTIVO, ME_MESANO, ME_META, ME_INTEGRANTES, ME_INTEGRANTES_TOTAL, ME_CLIENTES_NUEVOS) VALUES (?, ?, ?, ?, ?, ?)";
		int param = 1;
		int res = 0;
		PreparedStatement ps = null;
		Connection cn = null;

		try{
			cn = getConnection();
			ps = cn.prepareStatement(query); 

			ps.setInt(param++, meta.idEjecutivo);
			ps.setInt(param++, meta.mesAño);
			ps.setInt(param++, meta.meta);
			ps.setInt(param++, meta.integrantes);
			ps.setInt(param++, meta.integrantesTotal);
			ps.setInt(param++, meta.clientesNuevos);
			res = ps.executeUpdate();
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en addMeta : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en addMeta : "+e.getMessage());
			e.printStackTrace();
			throw new ClientesException(e.getMessage());
		}
		finally {
			try {
				if ( cn!=null ) cn.close();
			}
			catch(SQLException sqle) {
				throw new ClientesDBException(sqle.getMessage());
			}
		}
		
		return res;
	}
	
	public MetasEjecutivosVO getMeta(int idEjecutivo, int mesAño) throws ClientesException{

		String query = "SELECT * FROM D_METAS_EJECUTIVOS WHERE ME_NUMEJECUTIVO = ? AND ME_MESANO = ?";
		MetasEjecutivosVO meta = null;
		Connection cn = null;
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(1, idEjecutivo);
			ps.setInt(2, mesAño);

			ResultSet rs = ps.executeQuery();
			if (rs.next()){
				meta =  new MetasEjecutivosVO();
				meta.idEjecutivo = idEjecutivo;
				meta.mesAño = mesAño;
				meta.meta = rs.getInt("ME_META");
				meta.integrantes = rs.getInt("ME_INTEGRANTES");
				meta.integrantesTotal = rs.getInt("ME_INTEGRANTES_TOTAL");
				meta.clientesNuevos = rs.getInt("ME_CLIENTES_NUEVOS");
			}
		}catch(SQLException sqle) {
			Logger.debug("SQLException en getMeta : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getMeta : "+e.getMessage());
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
	    return meta;

	}
        
        public void eliminaMeta(int idAsesor) throws ClientesException{
            String query = "DELETE FROM d_metas_ejecutivos WHERE me_numejecutivo = ?";
            Connection cn = null;
            PreparedStatement ps = null;
            try {
                cn = getConnection();
                ps = cn.prepareStatement(query);
                ps.setInt(1, idAsesor);
                myLogger.debug("Ejecutando: "+ ps.toString());
                ps.executeUpdate();
            } catch (SQLException sqle) {
                myLogger.error("eliminaMeta", sqle);
                throw new ClientesDBException(sqle.getMessage());
            } catch(Exception e) {
                myLogger.error("eliminaMeta", e);
                throw new ClientesException(e.getMessage());
            } finally {
                try {
                    if(cn != null){
                    cn.close();
                    }
                    if (ps != null){
                        ps.close();
                    }
                } catch (SQLException sqle){
                    throw new ClientesDBException(sqle.getMessage());
                }
            }
        }

}