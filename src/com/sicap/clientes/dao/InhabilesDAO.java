package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;


public class InhabilesDAO extends DAOMaster{


	public Date[] getInhabiles(String year) throws ClientesException{

		String query = "SELECT IN_FECHA FROM C_INHABILES";
		ArrayList<Date> array = new ArrayList<Date>();
		Date elementos[] = null;
		Connection cn = null;
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			//ps.setString(1, year);
			ResultSet rs = ps.executeQuery(query);
			Date temporal = null;
			
			while(rs.next()){
				temporal = new Date();
				temporal = rs.getDate("IN_FECHA");
				//System.out.println("FECHA: " + temporal);
				array.add(temporal);
			}
		}catch(SQLException sqle) {
			Logger.debug("SQLException en getDiasInhabiles : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getDiasInhabiles : "+e.getMessage());
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
		elementos = new Date[array.size()];
	    for(int i=0;i<elementos.length; i++) elementos[i] = (Date)array.get(i);
	    
	    return elementos;

	}

}