package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.CatalogoMapeoVO;



public class CatalogoMapeoDAO extends DAOMaster{



	public CatalogoMapeoVO[] getCatalogoMapeo(int idCatalogo) throws ClientesException{

		String query = "SELECT MA_CVECATALOGO, MA_NOMCATALOGO, MA_CODIGO, MA_CODIGO_SIC FROM C_MAPEOS WHERE MA_CVECATALOGO = ?";
		ArrayList<CatalogoMapeoVO> array = new ArrayList<CatalogoMapeoVO>();
        CatalogoMapeoVO temporal = null;
        CatalogoMapeoVO elementos[] = null;
		Connection cn = null;
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(1, idCatalogo);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				temporal = new CatalogoMapeoVO();
				temporal.idCatalogo = rs.getInt("MA_CVECATALOGO");
				temporal.nombreCatalogo = rs.getString("MA_NOMCATALOGO");
				temporal.codigo = rs.getString("MA_CODIGO");
				temporal.codigoSyncronet = rs.getString("MA_CODIGO_SIC");
				array.add(temporal);
			}
		}catch(SQLException sqle) {
			Logger.debug("SQLException en getMapeoCatalogo : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getMapeoCatalogo : "+e.getMessage());
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
	elementos = new CatalogoMapeoVO[array.size()];
    for(int i=0;i<elementos.length; i++) elementos[i] = (CatalogoMapeoVO)array.get(i);
    return elementos;

	}




}