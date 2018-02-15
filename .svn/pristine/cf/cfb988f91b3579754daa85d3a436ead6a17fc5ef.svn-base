package com.sicap.clientes.dao.inffinix;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;

import com.sicap.clientes.dao.DAOMaster;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.inffinix.CodigoAccionVO;

public class CodigoAccionDAO extends DAOMaster {

	public CodigoAccionVO[] getCodigosAccion(){
		String query = "SELECT * FROM C_CODIGOS_ACCION;";
		Connection con = null;
		ArrayList<CodigoAccionVO> array = new ArrayList<CodigoAccionVO>();
		CodigoAccionVO[] codAccionVO = null;
		
		try{
			con = getConnection();
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs  = ps.executeQuery();
			while(rs.next()){
				CodigoAccionVO codVO = new CodigoAccionVO();
				codVO.codigoAccion = rs.getString("ca_codigo_accion");
				codVO.descripcion = rs.getString("ca_descripcion");
				array.add(codVO);
			}
			codAccionVO = new CodigoAccionVO[array.size()];
			for(int i = 0; i < codAccionVO.length; i++){
				codAccionVO[i] = (CodigoAccionVO) array.get(i);
			}
		}
		catch(SQLException exc){
			Logger.debug("SQLException en getCodigosAccion::" + exc);
		}
		catch(NamingException exc){
			Logger.debug("NamingException en getCodigosAccion::" + exc);
		}
		finally{
			if(con!=null){
				try{
					con.close();
				}
				catch(SQLException exc){
					Logger.debug("SQLException en getCodigosAccion::" + exc);
				}
			}
		}
		return codAccionVO;
	}
	
	
	public CodigoAccionVO[] getCodigosAccion(String codigoAccion){
		String query = "SELECT * FROM C_CODIGOS_ACCION WHERE CA_CODIGO_ACCION = ?;";
		Connection con = null;
		ArrayList<CodigoAccionVO> array = new ArrayList<CodigoAccionVO>();
		CodigoAccionVO[] codAccionVO = null;
		int param = 1;
		
		try{
			con = getConnection();
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(param++, codigoAccion);
			ResultSet rs  = ps.executeQuery();
			while(rs.next()){
				CodigoAccionVO codVO = new CodigoAccionVO();
				codVO.codigoAccion = rs.getString("ca_codigo_accion");
				codVO.descripcion = rs.getString("ca_descripcion");
				array.add(codVO);
			}
			codAccionVO = new CodigoAccionVO[array.size()];
			for(int i = 0; i < codAccionVO.length; i++){
				codAccionVO[i] = (CodigoAccionVO) array.get(i);
			}
		}
		catch(SQLException exc){
			Logger.debug("SQLException en getCodigosAccion::" + exc);
		}
		catch(NamingException exc){
			Logger.debug("NamingException en getCodigosAccion::" + exc);
		}
		finally{
			if(con!=null){
				try{
					con.close();
				}
				catch(SQLException exc){
					Logger.debug("SQLException en getCodigosAccion::" + exc);
				}
			}
		}
		return codAccionVO;
	}
	
	
}
