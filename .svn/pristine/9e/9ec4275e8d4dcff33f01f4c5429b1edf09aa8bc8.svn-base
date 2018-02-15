package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.CreditoViviendaVO;


public class CreditoViviendaDAO extends DAOMaster{


	public CreditoViviendaVO getCreditoVivienda(int idCliente, int idSolicitud) throws ClientesException{
		CreditoViviendaVO creditoVivienda = null;
		Connection cn = null;
		String query =	"SELECT CV_NUMCLIENTE, CV_NUMSOLICITUD, CV_TIPO_CREDITO, CV_COFINANCIADO, "+
						"CV_TIPO_TASA, CV_VALOR_SOLUCION, CV_DERECHOS, CV_GASTOS_OPERACION, "+
						"CV_VALOR_AVALUO, CV_IMPUESTOS, CV_AHORRO, CV_SUBCUENTA_TITULAR, CV_SUBCUENTA_CONYUGE, "+
						"CV_NUMERO_CREDITO, CV_CLABE_BANCARIA FROM D_CREDITO_VIVIENDA "+
						"WHERE CV_NUMCLIENTE = ? AND CV_NUMSOLICITUD = ?";
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(1, idCliente);
			ps.setInt(2, idSolicitud);
			//Logger.debug("Ejecutando = "+query);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				creditoVivienda =  new CreditoViviendaVO();
				creditoVivienda.idCliente  = idCliente;
				creditoVivienda.idSolicitud  = idSolicitud;
				creditoVivienda.tipoCredito = rs.getInt("CV_TIPO_CREDITO");
				creditoVivienda.cofinanciado = rs.getInt("CV_COFINANCIADO");
				creditoVivienda.tipoTasa = rs.getInt("CV_TIPO_TASA");
				creditoVivienda.valorSolucion = rs.getDouble("CV_VALOR_SOLUCION");
				creditoVivienda.derechos = rs.getDouble("CV_DERECHOS");
				creditoVivienda.gastosOperacion = rs.getDouble("CV_GASTOS_OPERACION");
				creditoVivienda.valorAvaluo = rs.getDouble("CV_VALOR_AVALUO");
				creditoVivienda.impuestos = rs.getDouble("CV_IMPUESTOS");
				creditoVivienda.ahorro = rs.getDouble("CV_AHORRO");
				creditoVivienda.subcuentaTitular = rs.getDouble("CV_SUBCUENTA_TITULAR");
				creditoVivienda.subcuentaConyuge = rs.getDouble("CV_SUBCUENTA_CONYUGE");
				creditoVivienda.numCredito = rs.getString("CV_NUMERO_CREDITO");
				creditoVivienda.CLABEBancaria = rs.getString("CV_CLABE_BANCARIA");
			}
		}catch(SQLException sqle) {
			Logger.debug("SQLException en getCreditoVivienda : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getCreditoVivienda : "+e.getMessage());
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
		return creditoVivienda;
	}


	public int addCreditoVivienda (int idCliente, int idSolicitud, CreditoViviendaVO creditoVivienda) throws ClientesException{

		String query =	"INSERT INTO D_CREDITO_VIVIENDA (CV_NUMCLIENTE, CV_NUMSOLICITUD, CV_TIPO_CREDITO, CV_COFINANCIADO, "+
						"CV_TIPO_TASA, CV_VALOR_SOLUCION, CV_DERECHOS, CV_GASTOS_OPERACION, "+
						"CV_VALOR_AVALUO, CV_IMPUESTOS, CV_AHORRO, CV_SUBCUENTA_TITULAR, CV_SUBCUENTA_CONYUGE, "+
						"CV_NUMERO_CREDITO, CV_CLABE_BANCARIA) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		Connection cn = null;
		int param = 1;
		int  res = 0;
		try{
			PreparedStatement ps =null;
			cn = getConnection();
			ps = cn.prepareStatement(query); 
			ps.setInt(param++, idCliente);
			ps.setInt(param++, idSolicitud);
			ps.setInt(param++,creditoVivienda.tipoCredito);
			ps.setInt(param++,creditoVivienda.cofinanciado);
			ps.setInt(param++,creditoVivienda.tipoTasa);
			ps.setDouble(param++,creditoVivienda.valorSolucion);
			ps.setDouble(param++,creditoVivienda.derechos);
			ps.setDouble(param++,creditoVivienda.gastosOperacion);
			ps.setDouble(param++,creditoVivienda.valorAvaluo);
			ps.setDouble(param++,creditoVivienda.impuestos);
			ps.setDouble(param++,creditoVivienda.ahorro);
			ps.setDouble(param++,creditoVivienda.subcuentaTitular);
			ps.setDouble(param++,creditoVivienda.subcuentaConyuge);
			ps.setString(param++,creditoVivienda.numCredito);
			ps.setString(param++,creditoVivienda.CLABEBancaria);
			Logger.debug("Ejecutando = "+query);
			res = ps.executeUpdate();
			Logger.debug("Resultado = "+res);
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en addCreditoVivienda : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en addCreditoVivienda : "+e.getMessage());
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


	public int updateCreditoVivienda (int idCliente, int idSolicitud, CreditoViviendaVO creditoVivienda) throws ClientesException{

		String query =	"UPDATE D_CREDITO_VIVIENDA SET CV_TIPO_CREDITO = ?, CV_COFINANCIADO = ?, "+
						"CV_TIPO_TASA = ?, CV_VALOR_SOLUCION = ?, CV_DERECHOS = ?, CV_GASTOS_OPERACION = ?, "+
						"CV_VALOR_AVALUO = ?, CV_IMPUESTOS = ?, CV_AHORRO = ?, CV_SUBCUENTA_TITULAR = ?, CV_SUBCUENTA_CONYUGE = ?, "+
						"CV_NUMERO_CREDITO = ?, CV_CLABE_BANCARIA = ? WHERE CV_NUMCLIENTE = ? AND CV_NUMSOLICITUD = ?";
		Connection cn = null;
		int  res = 0;
		int  param = 1;
		try{
			PreparedStatement ps =null;
			cn = getConnection();
			ps = cn.prepareStatement(query); 
			ps.setInt(param++,creditoVivienda.tipoCredito);
			ps.setInt(param++,creditoVivienda.cofinanciado);
			ps.setInt(param++,creditoVivienda.tipoTasa);
			ps.setDouble(param++,creditoVivienda.valorSolucion);
			ps.setDouble(param++,creditoVivienda.derechos);
			ps.setDouble(param++,creditoVivienda.gastosOperacion);
			ps.setDouble(param++,creditoVivienda.valorAvaluo);
			ps.setDouble(param++,creditoVivienda.impuestos);
			ps.setDouble(param++,creditoVivienda.ahorro);
			ps.setDouble(param++,creditoVivienda.subcuentaTitular);
			ps.setDouble(param++,creditoVivienda.subcuentaConyuge);
			ps.setString(param++,creditoVivienda.numCredito);
			ps.setString(param++,creditoVivienda.CLABEBancaria);
			ps.setInt(param++, idCliente);
			ps.setInt(param++, idSolicitud);
			Logger.debug("Ejecutando = "+query);
			//Logger.debug("CreditoVivienda = "+creditoVivienda.toString());
			res = ps.executeUpdate();
			Logger.debug("Resultado = "+res);
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en updateCreditoVivienda : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en updateCreditoVivienda : "+e.getMessage());
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

}
