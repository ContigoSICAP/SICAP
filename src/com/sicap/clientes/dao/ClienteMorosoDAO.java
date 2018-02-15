package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.ClienteMorosoVO;

public class ClienteMorosoDAO extends DAOMaster{

	public ClienteMorosoVO getClienteMoroso(String referencia) throws ClientesException{

		String query = "SELECT CM_REFERENCIA, CM_NUMOPERACION, CM_RFC, CM_NUMDIAS_VENCIDOS " +
					   "FROM D_CLIENTES_MOROSOS WHERE CM_REFERENCIA = ?";

		Connection cn = null;
		ClienteMorosoVO cliente = null;

		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setString(1, referencia);
			Logger.debug("Ejecutando : "+query + " Referencia: " + referencia);
			ResultSet rs = ps.executeQuery();
			if( rs.next() ){
				cliente = new ClienteMorosoVO();
				cliente.referencia = rs.getString("CM_REFERENCIA");
				cliente.numOperacion = rs.getInt("CM_NUMOPERACION");
				cliente.rfc = rs.getString("CM_RFC");
				cliente.numDiasVencidos = rs.getInt("CM_NUMDIAS_VENCIDOS");
			}

		}catch(SQLException sqle) {
			Logger.debug("SQLException en getClienteMoroso : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getClienteMoroso : "+e.getMessage());
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
	    return cliente;

	}
	
}