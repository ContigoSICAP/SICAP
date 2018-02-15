package com.sicap.clientes.dao.cartera;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sicap.clientes.dao.DAOMaster;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.cartera.CondonacionesVO;

public class CondonacionesDAO extends DAOMaster{



	public CondonacionesVO[] getElementos(int numCliente, int numCredito, int numDividendo, String rubro) throws ClientesException{
		CondonacionesVO condo = null;
		Connection cn = null;
		ArrayList<CondonacionesVO> array = new ArrayList<CondonacionesVO>();
		CondonacionesVO elementos[] = null;

		String query =	"SELECT CO_NUMCLIENTE, CO_NUMCREDITO, CO_DIVIDENDO, CO_MONTO, CO_RUBRO, CO_COMENTARIO, CO_FECHA, CO_USUARIO "+
						"FROM D_CONDONACIONES WHERE CO_NUMCLIENTE = ? AND CO_NUMCREDITO = ? AND CO_DIVIDENDO = ? AND CO_RUBRO = ? ";
		try{
			cn = getCWConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(1, numCliente);
			ps.setInt(2, numCredito);
			ps.setInt(3, numDividendo);
			ps.setString(4, rubro);
			Logger.debug("Ejecutando = "+query);
			Logger.debug("Parametros = ["+numCliente+","+numCredito+","+numDividendo+","+rubro+"]");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				condo =  new CondonacionesVO();
				condo.numCliente = numCliente;
				condo.numCredito = numCredito;
				condo.numDividendo = numDividendo;
				condo.rubro = rubro;
				condo.monto = rs.getDouble("CO_MONTO");
				condo.comentario = rs.getString("CO_COMENTARIO");
				condo.fecha = rs.getDate("CO_FECHA");
				condo.usuario = rs.getString("CO_USUARIO");
				array.add(condo);
			}
			if ( array.size()>0 ){
				elementos = new CondonacionesVO[array.size()];
			    for(int i=0;i<elementos.length; i++) elementos[i] = (CondonacionesVO)array.get(i);
			}
		}catch(SQLException sqle) {
			Logger.debug("SQLException en getCondonaciones : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getCondonaciones : "+e.getMessage());
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
		
		return elementos;
	}
	
	
	public int addCondonacion(CondonacionesVO condo) throws ClientesException{

		String query =	"INSERT INTO D_CONDONACIONES (CO_NUMCLIENTE, CO_NUMCREDITO, CO_DIVIDENDO, CO_MONTO, CO_RUBRO , "+
						"CO_COMENTARIO, CO_FECHA,CO_USUARIO) " +
						"VALUES (?, ?, ?, ?, ?, ?, ?, ? )";

		Connection cn = null;
		int param = 1;
		int  res = 0;
		try{
			PreparedStatement ps =null;
			cn = getCWConnection();
			ps = cn.prepareStatement(query); 
			ps.setInt(param++, condo.numCliente);
			ps.setInt(param++, condo.numCredito);
			ps.setInt(param++, condo.numDividendo);
			ps.setDouble(param++, condo.monto);
			ps.setString(param++, condo.rubro);
			ps.setString(param++, condo.comentario);
			ps.setDate(param++, condo.fecha);
			ps.setString(param++, condo.usuario);
			//Logger.debug("Ejecutando = "+query);
			//Logger.debug("Para = "+tabla);
			res = ps.executeUpdate();
			//Logger.debug("Resultado = "+res);
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en addCondonacion : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en addCondonacion : "+e.getMessage());
			e.printStackTrace();
			throw new ClientesException(e.getMessage());
		}
		finally {
			try {
				if ( cn!=null ) cn.close();
			}
			catch(SQLException sqle) {
				throw new ClientesDBException(sqle.getMessage());
			}
		}
		return res;
	}



	public void delCondonacion(int numCliente, int numCredito, String rubro, int numDividendo) throws ClientesException{

		String query =	"DELETE FROM D_CONDONACIONES WHERE CO_NUMCLIENTE = ? AND CO_NUMCREDITO = ? AND CO_RUBRO = ? AND CO_DIVIDENDO = ?";

		Connection cn = null;

		try{
			PreparedStatement ps =null;
			cn = getCWConnection();
			ps = cn.prepareStatement(query); 
			ps.setInt(1, numCliente);
			ps.setInt(2, numCredito);
			ps.setString(3, rubro);
			ps.setInt(4, numDividendo);

			//Logger.debug("Ejecutando = "+query);
			//Logger.debug("Parametros = ["+idCliente+","+idSolicitud+","+idDisposicion+","+idAmortizacion+"]");
			ps.execute();
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en delCondonacion : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en delCondonacion : "+e.getMessage());
			e.printStackTrace();
			throw new ClientesException(e.getMessage());
		}
		finally {
			try {
				if ( cn!=null ) cn.close();
			}
			catch(SQLException sqle) {
				throw new ClientesDBException(sqle.getMessage());
			}
		}
	}

	
}
