package com.sicap.clientes.dao.inffinix;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;

import com.sicap.clientes.dao.DAOMaster;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.inffinix.CodigoResultadoVO;


public class CodigoResultadoDAO extends DAOMaster{
	
	public CodigoResultadoVO[] getCodigosResultadoByCodigoAccion(String codigoAccion){
		String query = "SELECT * FROM C_CODIGOS_RESULTADOS WHERE CR_CODIGO_RESULTADO IN";
		query += "(SELECT AR_CODIGO_RESULTADO FROM C_RELACION_ACCION_RESULTADO WHERE AR_CODIGO_ACCION = ?);";
		Logger.debug("Query::" + query);
		CodigoResultadoVO[] codVO = null;
		ArrayList<CodigoResultadoVO> array = new ArrayList<CodigoResultadoVO>();
		int param = 1;
		
		Connection con = null;
		try{
			con = getConnection();
			PreparedStatement ps = con.prepareStatement(query);
			ps.setString(param++, codigoAccion);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				CodigoResultadoVO resVO = new CodigoResultadoVO();
				resVO.codigoResultado = rs.getString("cr_codigo_resultado");
				resVO.descripcion = rs.getString("cr_descripcion");
				array.add(resVO);
			}
			codVO = new CodigoResultadoVO[array.size()];
			for(int i = 0; i < codVO.length; i++){
				codVO[i] = (CodigoResultadoVO)array.get(i);
			}
		}
		catch(SQLException exc){
			Logger.debug("SQLException en getCodigosResultadoByCodigoAccion::" + exc);
		}
		catch(NamingException exc){
			Logger.debug("NamingException en getCodigosResultadoByCodigoAccion::" + exc);
		}
		finally{
			if(con!=null){
				try{
					con.close();
				}
				catch(SQLException exc){
					Logger.debug("SQLException en getCodigosResultadoByCodigoAccion::" + exc);
				}
			}
		}
		return codVO;
	}

}
