package com.sicap.clientes.dao.inffinix;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;

import com.sicap.clientes.dao.DAOMaster;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.inffinix.CodigoCartaVO;

public class CodigoCartaDAO extends DAOMaster{
	
	public CodigoCartaVO[] getCodigosCarta(){
		CodigoCartaVO[] codVO = null;
		ArrayList<CodigoCartaVO> array = new ArrayList<CodigoCartaVO>();
		Connection con = null;
		String query = "SELECT * FROM C_CODIGOS_CARTA;";
		try{
			con = getConnection();
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				CodigoCartaVO cod = new CodigoCartaVO();
				cod.codigoCarta = rs.getString("cc_codigo");
				cod.descripcion = rs.getString("cc_descripcion");
				array.add(cod);
			}
			codVO = new CodigoCartaVO[array.size()];
			for(int i = 0; i < codVO.length; i++){
				codVO[i] = (CodigoCartaVO)array.get(i);
			}
		}
		catch(NamingException exc){
			Logger.debug("NamingException en getCodigosCarta::" + exc);
		}
		catch(SQLException exc){
			Logger.debug("SQLException en getCodigosCarta::" + exc);
		}
		finally{
			if(con!=null){
				try{
					con.close();
				}
				catch(SQLException exc){
					Logger.debug("SQLException en getCodigosCarta::" + exc);
				}
			}
		}
		return codVO;
	}
}
