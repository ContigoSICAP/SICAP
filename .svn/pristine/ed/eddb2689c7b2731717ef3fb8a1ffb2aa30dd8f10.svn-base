package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.SellFinanceVO;

public class SellFinanceDAO extends DAOMaster{


	public SellFinanceVO getSellFinance (int numCliente, int solicitud) throws ClientesException{
		String query = "SELECT * FROM D_SELL_FINANCE WHERE SF_NUMCLIENTE = ? AND SF_NUMSOLICITUD = ?";
		Connection cn = null;
		SellFinanceVO sellFinance = null;
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(1,numCliente);
			ps.setInt(2,solicitud);
			Logger.debug("Ejecutando : "+query);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				sellFinance = new SellFinanceVO();
				sellFinance.idCliente = numCliente;
				sellFinance.idSolicitud = solicitud;
				sellFinance.numeroFactura= rs.getString("SF_NUMFACTURA");
				sellFinance.idMarca= rs.getInt("SF_IDMARCA");
				sellFinance.idProducto = rs.getInt("SF_IDPRODUCTO");
				sellFinance.idPlan= rs.getInt("SF_NUMPLAN");
				sellFinance.fechaCaptura = rs.getTimestamp("SF_FECHA_CAPTURA");
				
				Logger.debug("Sell Finance encontrado : "+sellFinance.toString());
			}
		}catch(SQLException sqle) {
			Logger.debug("SQLException en getSellFinance : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getSellFinance : "+e.getMessage());
			e.printStackTrace();
			throw new ClientesException(e.getMessage());
		}
		finally {
			try {
				if ( cn!=null ) cn.close();
			}catch(SQLException e) {
			}
		}
		return sellFinance;
	}


	public int addSellFinance(SellFinanceVO sellFinance) throws ClientesException{

		String query =	"INSERT INTO D_SELL_FINANCE (SF_NUMCLIENTE, SF_NUMSOLICITUD, SF_NUMFACTURA, SF_IDMARCA, SF_IDPRODUCTO, SF_NUMPLAN, "+
						"SF_FECHA_CAPTURA) VALUES (?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP);";
		int param = 1;
		PreparedStatement ps = null;
		Connection cn = null;
		int res = 0;
		try{
			cn = getConnection();
			ps = cn.prepareStatement(query); 
			ps.setInt(param++, sellFinance.idCliente);
			ps.setInt(param++, sellFinance.idSolicitud);
			ps.setString(param++, sellFinance.numeroFactura);
			ps.setInt(param++, sellFinance.idMarca);
			ps.setInt(param++, sellFinance.idProducto );
			ps.setInt(param++, sellFinance.idPlan );
			
			Logger.debug("Ejecutando = "+query);
			Logger.debug("Para = "+sellFinance.toString());
			ps.executeUpdate();
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en add_sellFinance : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en add_sellFinance : "+e.getMessage());
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
	
	public int updateSellFinance(SellFinanceVO sellFinance) throws ClientesException{

		int res = 0;
		String query =	"UPDATE D_SELL_FINANCE SET SF_IDMARCA = ?, SF_IDPRODUCTO = ?, SF_NUMPLAN = ?, SF_NUMFACTURA = ?" +
						" WHERE SF_NUMCLIENTE = ? AND SF_NUMSOLICITUD = ?";
		Connection cn = null;
		try{
			PreparedStatement ps =null;
			cn = getConnection();
			ps =cn.prepareStatement(query);
			int param = 1; 
			ps.setInt(param++, sellFinance.idMarca);
			ps.setInt(param++, sellFinance.idProducto );
			ps.setInt(param++, sellFinance.idPlan );
			ps.setString(param++, sellFinance.numeroFactura );
			ps.setInt(param++, sellFinance.idCliente);
			ps.setInt(param++, sellFinance.idSolicitud);
			Logger.debug("Ejecutando = "+query);
			res = ps.executeUpdate();
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en update_sellFinance : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en update_sellFinance : "+e.getMessage());
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
	
	public int updateNumFactura(SellFinanceVO sellFinance) throws ClientesException{

		String query =	"UPDATE D_SELL_FINANCE SET SF_NUMFACTURA = ? WHERE SF_NUMCLIENTE = ? AND SF_NUMSOLICITUD = ?";
						
		int param = 1;
		PreparedStatement ps = null;
		Connection cn = null;
		int res = 0;
		try{
			cn = getConnection();
			ps = cn.prepareStatement(query); 
			ps.setString(param++, sellFinance.numeroFactura);
			ps.setInt(param++, sellFinance.idMarca);
			ps.setInt(param++, sellFinance.idProducto );
			
			Logger.debug("Ejecutando = "+query);
			Logger.debug("Para = "+sellFinance.toString());
			ps.executeUpdate();
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en add_sellFinance : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en add_sellFinance : "+e.getMessage());
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
