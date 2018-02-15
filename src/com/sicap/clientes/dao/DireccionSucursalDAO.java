package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.DireccionSucursalVO;

public class DireccionSucursalDAO extends DAOMaster{

	public DireccionSucursalVO getDireccion(int idSucursal) throws ClientesException{

		String query = "SELECT DS_CALLE, DS_COLONIA, DS_CP, DS_ESTADO, DS_CIUDAD FROM C_DIRECCIONES_SUCURSALES " +
					   "WHERE DS_NUMSUCURSAL = ? ";
        DireccionSucursalVO resp = null;
		Connection cn = null;
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(1, idSucursal);
			Logger.debug("Ejecutando = "+query+idSucursal);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				resp = new DireccionSucursalVO();
				resp.idSucursal = idSucursal;
				resp.calle = rs.getString("DS_CALLE");
				resp.colonia = rs.getString("DS_COLONIA");
				resp.cp = rs.getString("DS_CP");
				resp.estado = rs.getString("DS_ESTADO");
				resp.ciudad = rs.getString("DS_CIUDAD");
				Logger.debug("Direccio encontrada : "+resp.toString());
			}

		}catch(SQLException sqle) {
			Logger.debug("SQLException en getDireccionSucursal : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getDireccionSucursal : "+e.getMessage());
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
	    return resp;

	}

}