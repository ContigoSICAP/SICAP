package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.RegistroPagosSegurosVO;

public class RegistroPagosSegurosDAO extends DAOMaster {
	
public RegistroPagosSegurosVO[] getRegistroPagosSeguros(int numCliente, int solicitud) throws ClientesException{
		
		String query = "SELECT * FROM D_REGISTRO_PAGOS_SEGUROS WHERE so_numcliente = ? AND so_numsolicitud = ? ";
			
		ArrayList<RegistroPagosSegurosVO> listaPagos = new ArrayList<RegistroPagosSegurosVO>();
		int param = 1;
		RegistroPagosSegurosVO[] arrayPagos = null;
		Connection cn = null;
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(param++, numCliente);
			ps.setInt(param++, solicitud);
			Logger.debug("query::" + ps.toString());
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				RegistroPagosSegurosVO registropagoseguro = new RegistroPagosSegurosVO();
				registropagoseguro.idcliente = rs.getInt("re_numcliente");
				registropagoseguro.numsolicitud = rs.getInt("re_numsolicitud");
				registropagoseguro.referencia = rs.getString("re_referencia");
				registropagoseguro.montoCubierto = rs.getDouble("re_monto_cubierto");
				registropagoseguro.montoporCubrir = rs.getDouble("re_monto_cubrir");
				registropagoseguro.fechaPago = rs.getDate("re_fecha_pago");
				registropagoseguro.fechaHora = rs.getTimestamp("re_fecha_hora");
				listaPagos.add(registropagoseguro);
			}
		}
		catch(SQLException exc){
			Logger.debug("SQLException en RegistroPagosSegurosDAO.getRegistroPagosSeguros:" + exc);
			throw new ClientesException(exc.getMessage());
		}
		catch(NamingException exc){
			Logger.debug("NamingException en RegistroPagosSegurosDAO.getRegistroPagosSeguros:" + exc);
			throw new ClientesException(exc.getMessage());
		}
		finally{
			try{
				if(cn!= null)
					cn.close();
			}
			catch(SQLException exc){
				throw new ClientesDBException(exc.getMessage());
			}
		}
		arrayPagos = new RegistroPagosSegurosVO[listaPagos.size()];
		for(int i = 0; i < arrayPagos.length; i++)
			arrayPagos[i] = listaPagos.get(i);
		
		return arrayPagos;
	}


     public void insertRegistroPagoSeguro(RegistroPagosSegurosVO pago) throws ClientesException{
	      int param = 1;
	      String query = "INSERT INTO D_REGISTRO_PAGOS_SEGUROS(re_numcliente, re_numsolicitud, re_referencia, re_monto_cubierto," +
	      		" re_monto_cubrir, re_fecha_pago, re_fecha_hora,re_banco_referencia, re_id_contabilidad)";
		     query += "VALUES(?,?,?,?,?,?,CURRENT_TIMESTAMP,?,?)";
	      Connection cn = null;
	   try{
		  cn = getConnection();
		  PreparedStatement ps = cn.prepareStatement(query);
		  ps.setInt(param++, pago.idcliente);
		  ps.setInt(param++, pago.numsolicitud);
		  ps.setString(param++, pago.referencia);
		  ps.setDouble(param++, pago.montoCubierto);
		  ps.setDouble(param++, pago.montoporCubrir);
		  ps.setDate(param++, pago.fechaPago);
		  ps.setInt(param++, pago.bancoReferencia);
		  ps.setInt(param++, pago.idContabilidad);
		  ps.executeUpdate();
	   }
	   catch(SQLException exc){
		  Logger.debug("SQLException en RegistroPagosSegurosDAO.insertRegistroPagoSeguro():" + exc);
		  throw new ClientesException(exc.getMessage());
	   }
	   catch(NamingException exc){
		   Logger.debug("NamingException en RegistroPagosSegurosDAO.insertRegistroPagoSeguro():" + exc);
		   throw new ClientesException(exc.getMessage());
	   }
	   finally{
		   if(cn!=null){
			   try{
				cn.close();
			    }
			   catch(SQLException exc){
				 throw new ClientesDBException(exc.getMessage());
			 } 
		  }
	 }
}

	

}
