package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.BitacoraBuroCirculoVO;


public class BitacoraBuroCirculoDAO extends DAOMaster{

	public int add(BitacoraBuroCirculoVO bitacora) throws ClientesException{

		String query =	"INSERT INTO D_BITACORA_BUROCIRCULO (BB_NUMCUENTA, BB_REFERENCIA, BB_MOP, BB_DIAS_MORA, BB_SALDO_ACTUAL, BB_SALDO_VENCIDO, BB_ESTATUS, BB_FECHA_ENVIO, BB_CUENTA_ENVIADA ) " +
						"VALUES (?, ?, ?, ?, ?, ?, ?, CURRENT_DATE, ?)";

		Connection cn = null;
		int param = 1;
		int  res = 0;
		try{
			PreparedStatement ps =null;
			cn = getConnection();
			ps = cn.prepareStatement(query); 
			ps.setInt(param++, bitacora.numCuenta);
			ps.setString(param++, bitacora.referencia);
			ps.setString(param++, bitacora.mop);
			ps.setInt(param++, bitacora.numDiasMora);
			ps.setDouble(param++, bitacora.saldoActual);
			ps.setDouble(param++, bitacora.saldoVencido);
			ps.setInt(param++, bitacora.estatus);
			ps.setInt(param++, bitacora.cuentaEnviada);

			//Logger.debug("Ejecutando = "+query);
			res = ps.executeUpdate();
			//Logger.debug("Resultado = "+res);
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en addBitacoraBuroCirculo : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en addBitacoraBuroCirculo : "+e.getMessage());
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

	public BitacoraBuroCirculoVO getBitacora(String referencia) throws ClientesException{

		String query =	"SELECT BB_NUMCUENTA, BB_REFERENCIA, BB_MOP, BB_DIAS_MORA, BB_SALDO_ACTUAL, BB_SALDO_VENCIDO, BB_ESTATUS, " +
						"BB_FECHA_ENVIO, BB_CUENTA_ENVIADA FROM D_BITACORA_BUROCIRCULO WHERE BB_REFERENCIA = ?";

		Connection cn = null;
		BitacoraBuroCirculoVO bitBuro = null;
		
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setString(1, referencia);
			//Logger.debug("Ejecutando : "+query);
			ResultSet rs = ps.executeQuery();
			while( rs.next() ){
				bitBuro = new BitacoraBuroCirculoVO();
				bitBuro.numCuenta = rs.getInt("BB_NUMCUENTA");
				bitBuro.referencia = rs.getString("BB_REFERENCIA");
				bitBuro.mop = rs.getString("BB_MOP");
				bitBuro.numDiasMora = rs.getInt("BB_DIAS_MORA");
				bitBuro.saldoActual = rs.getDouble("BB_SALDO_ACTUAL");
				bitBuro.saldoVencido = rs.getDouble("BB_SALDO_VENCIDO");
				bitBuro.estatus = rs.getInt("BB_ESTATUS");
				bitBuro.fechaEnvio = rs.getDate("BB_FECHA_ENVIO");
			}
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en getBitacoraBuroCirculo : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getBitacoraBuroCirculo : "+e.getMessage());
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
		return bitBuro;
	}

}