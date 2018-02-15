package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.ReferenciaCrediticiaVO;

public class ReferenciaCrediticiaDAO extends DAOMaster {

	public ReferenciaCrediticiaVO getReferenciaCrediticia( int idCliente, int idSolicitud, int idReferencia ) throws ClientesException {
		ReferenciaCrediticiaVO referenciaPersonal = null;
		Connection cn = null;
		try {
			cn = getConnection();
			String query = 	"SELECT RC_NUMCLIENTE, RC_NUMSOLICITUD, RC_NUMREFERENCIA, RC_INSTITUCION, RC_NUMCREDITO, RC_PLAZO, RC_SALDO, "+
							"RC_FRECUENCIA_PAGO, RC_FECHA_CAPTURA FROM D_REFERENCIAS_CREDITICIAS WHERE RC_NUMCLIENTE = ? AND RC_NUMSOLICITUD = ? AND RC_NUMREFERENCIA = ?";
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(1, idSolicitud);
			ps.setInt(2, idCliente);
			ps.setInt(3, idReferencia);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				referenciaPersonal  = new ReferenciaCrediticiaVO();
				referenciaPersonal.idCliente = rs.getInt("RC_NUMCLIENTE");
				referenciaPersonal.idSolicitud = rs.getInt("RC_NUMSOLICITUD");
				referenciaPersonal.idReferencia = rs.getInt("RC_NUMREFERENCIA");
				referenciaPersonal.institucion = rs.getString("RC_INSTITUCION");
				referenciaPersonal.numCredito = rs.getString("RC_NUMCREDITO");
				referenciaPersonal.plazo = rs.getString("RC_PLAZO");
				referenciaPersonal.saldo = rs.getDouble("RC_SALDO");
				referenciaPersonal.frecuenciaPago = rs.getString("RC_FRECUENCIA_PAGO");
				referenciaPersonal.fechaCaptura = rs.getTimestamp("RC_FECHA_CAPTURA");
			}
		} catch (SQLException sqle) {
			Logger.debug("SQLException en getReferenciaCrediticia : "
					+ sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		} catch (Exception e) {
			Logger.debug("Excepcion en getReferenciaCrediticia : " + e.getMessage());
			e.printStackTrace();
			throw new ClientesException(e.getMessage());
		} finally {
			try {
				if (cn != null)
					cn.close();
			} catch (SQLException e) {
				throw new ClientesDBException(e.getMessage());
			}
		}

		return referenciaPersonal;

	}

	

	public ReferenciaCrediticiaVO[] getReferenciasCrediticias(int idCliente, int idSolicitud) throws ClientesException{

		String query = 	"SELECT RC_NUMCLIENTE, RC_NUMSOLICITUD, RC_NUMREFERENCIA, RC_INSTITUCION, RC_NUMCREDITO, RC_PLAZO, RC_SALDO, "+
						"RC_FRECUENCIA_PAGO, RC_FECHA_CAPTURA FROM D_REFERENCIAS_CREDITICIAS WHERE RC_NUMCLIENTE = ? AND RC_NUMSOLICITUD = ?";
		ArrayList<ReferenciaCrediticiaVO> array = new ArrayList<ReferenciaCrediticiaVO>();
		ReferenciaCrediticiaVO temporal = null;
		ReferenciaCrediticiaVO elementos[] = null;
		Connection cn = null;
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(1, idCliente);
			ps.setInt(2, idSolicitud);
			Logger.debug("Ejecutando : "+query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				temporal = new ReferenciaCrediticiaVO();
				temporal.idCliente = rs.getInt("RC_NUMCLIENTE");
				temporal.idSolicitud = rs.getInt("RC_NUMSOLICITUD");
				temporal.idReferencia = rs.getInt("RC_NUMREFERENCIA");
				temporal.institucion = rs.getString("RC_INSTITUCION");
				temporal.numCredito = rs.getString("RC_NUMCREDITO");
				temporal.plazo = rs.getString("RC_PLAZO");
				temporal.saldo = rs.getDouble("RC_SALDO");
				temporal.frecuenciaPago = rs.getString("RC_FRECUENCIA_PAGO");
				temporal.fechaCaptura = rs.getTimestamp("RC_FECHA_CAPTURA");
				//Logger.debug("Referencia Personal encontrada : "+temporal.toString());
				array.add(temporal);
			}
			if ( array.size()>0 ){
				elementos = new ReferenciaCrediticiaVO[array.size()];
			    for(int i=0;i<elementos.length; i++) elementos[i] = (ReferenciaCrediticiaVO)array.get(i);
			}
		}catch(SQLException sqle) {
			Logger.debug("SQLException en getReferenciasCrediticias : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getReferenciasCrediticias : "+e.getMessage());
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

	public int addReferenciaCrediticia(int idCliente, int idSolicitud, ReferenciaCrediticiaVO referenciaCrediticia) throws ClientesException{
		return addReferenciaCrediticia(null, idCliente, idSolicitud, referenciaCrediticia);
	}
	

	public int addReferenciaCrediticia(Connection conn, int idCliente, int idSolicitud, ReferenciaCrediticiaVO referenciaCrediticia) throws ClientesException {
		int res = 0;
		String query =	"INSERT INTO D_REFERENCIAS_CREDITICIAS (RC_NUMCLIENTE, RC_NUMSOLICITUD, RC_NUMREFERENCIA, RC_INSTITUCION, "+
						"RC_NUMCREDITO, RC_PLAZO, RC_SALDO, RC_FRECUENCIA_PAGO, RC_FECHA_CAPTURA) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		int param = 1;
		
		Connection cn = null;
		PreparedStatement ps = null;
		try {
			if( conn==null ){
				referenciaCrediticia.idReferencia = getNext(idCliente, idSolicitud);
				cn = getConnection();
				ps = cn.prepareStatement(query); 
			}
			else{
				referenciaCrediticia.idReferencia = getNext(conn, idCliente, idSolicitud);
				ps = conn.prepareStatement(query);
			}
			
			ps.setInt(param++, idCliente);
			ps.setInt(param++, idSolicitud);
			ps.setInt(param++, referenciaCrediticia.idReferencia);
			ps.setString(param++, referenciaCrediticia.institucion);
			ps.setString(param++, referenciaCrediticia.numCredito);
			ps.setString(param++, referenciaCrediticia.plazo);
			ps.setDouble(param++, referenciaCrediticia.saldo);
			ps.setString(param++, referenciaCrediticia.frecuenciaPago);
			ps.setTimestamp(param++, referenciaCrediticia.fechaCaptura);
			Logger.debug("Ejecutando = "+query);
			Logger.debug("Referencia = "+referenciaCrediticia.toString());
			res = ps.executeUpdate();
			Logger.debug("Resultado = "+res);
		} catch (SQLException sqle) {
			Logger.debug("SQLException en addReferenciaCrediticia : " + sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		} catch (Exception e) {
			Logger.debug("Excepcion en addReferenciaCrediticia : " + e.getMessage());
			e.printStackTrace();
			throw new ClientesException(e.getMessage());
		} finally {
			try {
				if (cn != null)
					cn.close();
			} catch (SQLException sqle) {
				throw new ClientesDBException(sqle.getMessage());
			}
		}

		return res;

	}


//UPDATE D_REFERENCIAS_CREDITICIAS SET RC_INSTITUCION = ?, RC_NUMCREDITO = ?, RC_PLAZO = ?, RC_SALDO = ?, RC_FRECUENCIA_PAGO = ?, RC_FECHA_CAPTURA = ? WHERE RC_NUMCLIENTE = ? AND RC_NUMSOLICITUD = ? AND RC_NUMREFERENCIA = ? 
	public int updateReferenciaCrediticia(int idCliente, int idSolicitud,int idReferencia, ReferenciaCrediticiaVO referenciaCrediticia) throws ClientesException {
		int res = 0;
		String query =	"UPDATE D_REFERENCIAS_CREDITICIAS SET RC_INSTITUCION = ?, RC_NUMCREDITO = ?, RC_PLAZO = ?, RC_SALDO = ?, "+
						"RC_FRECUENCIA_PAGO = ?, RC_FECHA_CAPTURA = ? WHERE RC_NUMCLIENTE = ? AND RC_NUMSOLICITUD = ? AND RC_NUMREFERENCIA = ?";
		int param = 1;
		Connection cn = null;
		try {
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setString(param++, referenciaCrediticia.institucion);
			ps.setString(param++, referenciaCrediticia.numCredito);
			ps.setString(param++, referenciaCrediticia.plazo);
			ps.setDouble(param++, referenciaCrediticia.saldo);
			ps.setString(param++, referenciaCrediticia.frecuenciaPago);
			ps.setTimestamp(param++, referenciaCrediticia.fechaCaptura);
			ps.setInt(param++, referenciaCrediticia.idCliente);
			ps.setInt(param++, referenciaCrediticia.idSolicitud);
			ps.setInt(param++, referenciaCrediticia.idReferencia);
			Logger.debug("Ejecutando = "+query);
			Logger.debug("Referencia = "+referenciaCrediticia.toString());
			res = ps.executeUpdate();
			Logger.debug("Resultado = "+res);
		} catch (SQLException sqle) {
			Logger.debug("SQLException en updateReferenciaCrediticia : " + sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		} catch (Exception e) {
			Logger.debug("Excepcion en updateReferenciaCrediticia : " + e.getMessage());
			e.printStackTrace();
			throw new ClientesException(e.getMessage());
		} finally {
			try {
				if (cn != null)
					cn.close();
			} catch (SQLException sqle) {
				throw new ClientesDBException(sqle.getMessage());
			}
		}

		return res;

	}


	public int getNext(int idCliente, int idSolicitud ) throws ClientesException{
		return getNext(null, idCliente, idSolicitud ) ;
	}

	public int getNext(Connection conn, int idCliente, int idSolicitud ) throws ClientesException{

		String query = "SELECT COALESCE(MAX(RC_NUMREFERENCIA),0)+1 AS NEXT FROM D_REFERENCIAS_CREDITICIAS WHERE RC_NUMCLIENTE = ? AND RC_NUMSOLICITUD = ?";
		Connection cn = null;
		int next = 1;
		try{
			PreparedStatement ps = null;
			if( conn==null ){
				cn = getConnection();
				ps = cn.prepareStatement(query); 
			}
			else{
				ps = conn.prepareStatement(query);
			}
			
			ps.setInt(1, idCliente);
			ps.setInt(2, idSolicitud);
			ResultSet rs = ps.executeQuery();
			Logger.debug("Ejecutando = "+query);
			if( rs.next() ){
				next = rs.getInt("NEXT");
			}
			Logger.debug("Resultado = "+next);
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en getNext : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getNext : "+e.getMessage());
			e.printStackTrace();
			throw new ClientesException(e.getMessage());
		}
		finally {
			try {
				if ( cn!=null )
					cn.close();
			}catch(SQLException e) {
				throw new ClientesDBException(e.getMessage());
			}
		}
		return next;
	}


}
