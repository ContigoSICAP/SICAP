package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.LimiteCreditoVO;

public class LimiteCreditoDAO extends DAOMaster{


	public LimiteCreditoVO getLimite (int numCliente, int solicitud) throws ClientesException{
		String query = "SELECT * FROM D_LIMITES_CREDITO WHERE LI_NUMCLIENTE = ? AND LI_NUMSOLICITUD = ?";
		Connection cn = null;
		LimiteCreditoVO limite = null;
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(1,numCliente);
			ps.setInt(2,solicitud);
			Logger.debug("Ejecutando : "+query);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				limite = new LimiteCreditoVO();
				limite.idCliente = numCliente;
				limite.idSolicitud = solicitud;
				limite.tasa = rs.getInt("LI_TASA");
				limite.plazo = rs.getInt("LI_PLAZO");
				limite.comision = rs.getInt("LI_COMISION");
				limite.monto = rs.getDouble("LI_MONTO");
				
				limite.garantia = rs.getBoolean("LI_GARANTIA");
				limite.comentarios = rs.getString("LI_COMENTARIOS");
				
				
				limite.fechaCaptura = rs.getTimestamp("LI_FECHA_CAPTURA");
				limite.usuario = rs.getString("LI_USUARIO");
				Logger.debug("Limite encontrado : "+limite.toString());
			}
		}catch(SQLException sqle) {
			Logger.debug("SQLException en getCliente : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getCliente : "+e.getMessage());
			e.printStackTrace();
			throw new ClientesException(e.getMessage());
		}
		finally {
			try {
				if ( cn!=null ) cn.close();
			}catch(SQLException e) {
			}
		}
		return limite;
	}


	public int addLimite(LimiteCreditoVO limite) throws ClientesException{

		String query =	"INSERT INTO D_LIMITES_CREDITO (LI_NUMCLIENTE, LI_NUMSOLICITUD, LI_TASA, LI_PLAZO, "+
						"LI_COMISION, LI_MONTO, LI_GARANTIA, LI_COMENTARIOS, LI_FECHA_CAPTURA, LI_USUARIO) VALUES (?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?);";
		int param = 1;
		PreparedStatement ps = null;
		Connection cn = null;
		int res = 0;
		try{
			cn = getConnection();
			ps = cn.prepareStatement(query); 
			ps.setInt(param++, limite.idCliente);
			ps.setInt(param++, limite.idSolicitud);
			ps.setInt(param++, limite.tasa);
			ps.setInt(param++, limite.plazo);
			ps.setInt(param++, limite.comision);
			ps.setDouble(param++, limite.monto);
			
			ps.setBoolean(param++, limite.garantia);
			ps.setString(param++, limite.comentarios);
			
			ps.setString(param++, limite.usuario);
			Logger.debug("Ejecutando = "+query);
			Logger.debug("Para = "+limite.toString());
			ps.executeUpdate();
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en addLimite : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en addLimite : "+e.getMessage());
			e.printStackTrace();
			throw new ClientesException(e.getMessage());
		}
		finally {
			try {
				if ( cn!=null ) cn.close();
			}
			catch(SQLException sqle) {
			}
		}
		return res;
	}
	
	public int updateLimite(LimiteCreditoVO limite) throws ClientesException{

		int res = 0;
		String query =	"UPDATE D_LIMITES_CREDITO SET LI_TASA = ?, LI_PLAZO = ?, LI_COMISION = ?, " +
						"LI_MONTO = ?, LI_GARANTIA = ?, LI_COMENTARIOS = ?, LI_USUARIO = ? WHERE LI_NUMCLIENTE = ? AND LI_NUMSOLICITUD = ?";
		Connection cn = null;
		try{
			PreparedStatement ps =null;
			cn = getConnection();
			ps =cn.prepareStatement(query);
			int param = 1; 
			ps.setInt(param++, limite.tasa);
			ps.setInt(param++, limite.plazo);
			ps.setInt(param++, limite.comision);
			ps.setDouble(param++, limite.monto);
			
			ps.setBoolean(param++, limite.garantia);
			ps.setString(param++, limite.comentarios);
			
			ps.setString(param++, limite.usuario);
			ps.setInt(param++, limite.idCliente);
			ps.setInt(param++, limite.idSolicitud);
			Logger.debug("Ejecutando = "+query);
			res = ps.executeUpdate();
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en updateLimite : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en updateLimite : "+e.getMessage());
			e.printStackTrace();
			throw new ClientesException(e.getMessage());
		}
		finally {
			try {
				if ( cn!=null ) cn.close();
			}
			catch(SQLException sqle) {
			}
		}
		return res;
	}

}
