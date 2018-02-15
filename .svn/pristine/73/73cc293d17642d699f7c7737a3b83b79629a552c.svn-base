package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.sicap.clientes.vo.ReferenciaSHFVO;

public class ReferenciaSHFDAO extends DAOMaster {
	
	public List getReferenciasSHF(){
		List<ReferenciaSHFVO> lista = new ArrayList<ReferenciaSHFVO>();
		Connection conn = null;
		ReferenciaSHFVO referenciaSHF = null;
		
		try{
			conn = getConnection();
			
			String query = "SELECT * "+
			                 "FROM referenciasSHF " +
			                "WHERE sh_estatus = 'V'";
			
			PreparedStatement ps = conn.prepareStatement(query);
			
			ResultSet rs = ps.executeQuery();
			
			while( rs.next() ){
				referenciaSHF = new ReferenciaSHFVO();
				
				referenciaSHF.setReferencia( rs.getString("sh_referencia") );
				referenciaSHF.setUltimoPago( rs.getInt("sh_ultimo_pago") );
				referenciaSHF.setFechaLiquidacion( rs.getDate("sh_fecha_liquidacion") );
				referenciaSHF.setEstatus( rs.getString("sh_estatus") );
				
				lista.add( referenciaSHF );
			}
			
			
		}catch(Exception e){ e.printStackTrace();}
		finally {
			try {
				if ( conn!=null ) conn.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return lista;
	}
	
	public void updateReferenciaSHF( ReferenciaSHFVO referenciaShf ){
		
		Connection conn = null;
		try{
			conn = getConnection();
			
			String query = "UPDATE referenciasSHF "+
						      "SET sh_ultimo_pago       = ?, "+
						          "sh_fecha_liquidacion = ?, "+
						          "sh_estatus           = ?  "+
						    "WHERE sh_referencia        = ?";
			
			PreparedStatement ps = conn.prepareStatement(query);
			
			ps.setInt   (1, referenciaShf.getUltimoPago());
			ps.setDate  (2, referenciaShf.getFechaLiquidacion());
			ps.setString(3, referenciaShf.getEstatus());
			ps.setString(4, referenciaShf.getReferencia());
			
			ps.executeUpdate();
			
		}catch(Exception e){ e.printStackTrace();}
		finally {
			try {
				if ( conn!=null ) conn.close();
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
	}

}
