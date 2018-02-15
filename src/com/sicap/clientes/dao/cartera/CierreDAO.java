package com.sicap.clientes.dao.cartera;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;

import com.sicap.clientes.dao.DAOMaster;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.cartera.CierreVO;

public class CierreDAO extends DAOMaster{
	
	
	public CierreVO[] getCierre() throws ClientesException{
		ArrayList<CierreVO> arrayCierre = new ArrayList<CierreVO>();
		CierreVO[] cierres = null;
		Connection conn = null;
		String query = "SELECT CI_NUMCIERRE, CI_FECHA_INICIO, CI_FECHA_FIN, CI_STATUS FROM D_CIERRE WHERE FECHA_FIN = (SELECT MAX(FECHA_FIN) FROM D_CIERRE);";
		int param = 1;
		try{
			conn = getCWConnection();
			PreparedStatement ps = conn.prepareStatement(query);
			Logger.debug("Ejecutando : " + query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				CierreVO cierre = new CierreVO();
				cierre.numCierre = rs.getInt(1);
				cierre.fechaInicio = rs.getDate(2);
				cierre.fechaFin = rs.getDate(3);
				cierre.status = rs.getString(4);
				arrayCierre.add(cierre);
			}
			if(arrayCierre.size()>0){
				cierres = new CierreVO[arrayCierre.size()];
				for(int i = 0; i < cierres.length; i++)
					cierres[i] = arrayCierre.get(i);
			}
		}catch(SQLException exc){
			exc.printStackTrace();
			throw new ClientesException(exc.getMessage());
		}catch(NamingException exc){
			exc.printStackTrace();
			throw new ClientesException(exc.getMessage());
		}finally{
			try{
				if(conn!=null){
					conn.close();
				}
			}catch(SQLException exc){
				exc.printStackTrace();
				throw new ClientesException(exc.getMessage()); 
			}
		}
		return cierres;
	}


	public void insertCierre(CierreVO cierre) throws ClientesException{
		int param = 1;
		String query =	"INSERT INTO D_CIERRE" +
						"(CI_FECHA_INICIO, CI_FECHA_FIN, CI_STATUS )"+
						"VALUES(? , ?, ? )";
		Connection cn = null;
		try{
			cn = getCWConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setDate(param++, cierre.fechaInicio);
			ps.setDate(param++, cierre.fechaFin);
			ps.setString(param++, cierre.status);
			Logger.debug("Ejecutando : "+query);
			ps.executeUpdate();
		}
		catch(SQLException exc){
			Logger.debug("SQLException en CierreDAO.insertCirre()::" + exc);
			exc.printStackTrace();
			throw new ClientesException(exc.getMessage());
		}
		catch(NamingException exc){
			Logger.debug("NamingException en CierreDAO.insertCierre()::" + exc);
			exc.printStackTrace();
			throw new ClientesException(exc.getMessage());
		}
		finally{
			if(cn!=null){
				try{
					cn.close();
				}
				catch(SQLException exc){
					Logger.debug("SQLException en CierreDAO.insertCierre()::" + exc);
					exc.printStackTrace();
					throw new ClientesDBException(exc.getMessage());
				}
			}
		}
	}


}