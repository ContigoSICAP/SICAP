package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.DisposicionVO;




public class DisposicionDAO extends DAOMaster{




	public DisposicionVO[] getDisposiciones(int idCliente, int idSolicitud) throws ClientesException{

		String query = "SELECT DP_NUMCLIENTE, DP_NUMSOLICITUD, DP_NUMDISPOSICION, DP_MONTO, DP_FECHA_CAPTURA "+
						"FROM D_DISPOSICIONES WHERE DP_NUMCLIENTE = ? AND DP_NUMSOLICITUD = ?";
		ArrayList<DisposicionVO> array = new ArrayList<DisposicionVO>();
        DisposicionVO temporal = null;
        DisposicionVO elementos[] = null;
		Connection cn = null;
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(1, idCliente);
			ps.setInt(2, idSolicitud);
			Logger.debug("Ejecutando : "+query);
			Logger.debug("Para ["+idCliente+","+idSolicitud+"]");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				temporal = new DisposicionVO();
				temporal.idCliente = idCliente;
				temporal.idSolicitud = idSolicitud;
				temporal.idDisposicion = rs.getInt("DP_NUMDISPOSICION");
				temporal.monto = rs.getDouble("DP_MONTO");
				temporal.fechaCaptura = rs.getTimestamp("DP_FECHA_CAPTURA");
				Logger.debug("Disposicion encontrada : "+temporal.toString());
				array.add(temporal);
			}
			if ( array.size()>0 ){
				elementos = new DisposicionVO[array.size()];
			    for(int i=0;i<elementos.length; i++) elementos[i] = (DisposicionVO)array.get(i);
			}
		}catch(SQLException sqle) {
			Logger.debug("SQLException en getDisposiciones : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getDisposiciones : "+e.getMessage());
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



	public DisposicionVO getDisposicion(int idCliente, int idSolicitud, int idDisposicion) throws ClientesException{

		String query = "SELECT DP_NUMCLIENTE, DP_NUMSOLICITUD, DP_NUMDISPOSICION, DP_MONTO, DP_FECHA_CAPTURA FROM D_DISPOSICIONES WHERE DP_NUMCLIENTE = ? AND DP_NUMSOLICITUD = ? AND DP_NUMDISPOSICION = ?";
		Connection cn = null;
		DisposicionVO disposicion = null;
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(1,idCliente);
			Logger.debug("Ejecutando : "+query);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				disposicion.idCliente = idCliente;
				disposicion.idSolicitud = idSolicitud;
				disposicion.idDisposicion = rs.getInt("DP_NUMDISPOSICION");
				disposicion.monto = rs.getDouble("DP_MONTO");
				disposicion.fechaCaptura = rs.getTimestamp("DP_FECHA_CAPTURA");
				Logger.debug("Disposicion encontrada : "+disposicion.toString());
			}
		}catch(SQLException sqle) {
			Logger.debug("SQLException en getDisposicion : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getDisposicion : "+e.getMessage());
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
		return disposicion;

	}



	public int addDisposicion(int idCliente, int idSolicitud, DisposicionVO disposicion) throws ClientesException{

		String query =	"INSERT INTO D_DISPOSICIONES (DP_NUMCLIENTE, DP_NUMSOLICITUD, DP_NUMDISPOSICION, DP_MONTO, DP_FECHA_CAPTURA) VALUES (?, ?, ?, ?, ?)";

		Connection cn = null;
		PreparedStatement ps = null;
		int param = 1;
		int next = getNext(idCliente, idSolicitud);
		disposicion.idDisposicion = next;
		disposicion.fechaCaptura = new Timestamp(System.currentTimeMillis());
		try{
			cn = getConnection();
			ps = cn.prepareStatement(query);
			ps.setInt(param++, idCliente);
			ps.setInt(param++, idSolicitud);
			ps.setInt(param++, disposicion.idDisposicion);
			ps.setDouble(param++, disposicion.monto);
			ps.setTimestamp(param++, disposicion.fechaCaptura);
			Logger.debug("Ejecutando = "+query);
			Logger.debug("Disposicion = "+disposicion);
			ps.executeUpdate();
			
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en addDisposicion : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en addDisposicion : "+e.getMessage());
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
		return next;
	}



	public int updateDisposicion(int idCliente, int idSolicitud, int idDisposicion, DisposicionVO disposicion) throws ClientesException{

		int res = 0;
		String query =	"UPDATE D_DISPOSICIONES SET DP_MONTO = ? WHERE DP_NUMCLIENTE = ? AND DP_NUMSOLICITUD = ? AND DP_NUMDISPOSICION = ?";
		Connection cn = null;
		PreparedStatement ps = null;
		int param = 1;
		try{
			cn = getConnection();
			ps = cn.prepareStatement(query); 
			ps.setDouble(param++, disposicion.monto);
			ps.setInt(param++, idCliente);
			ps.setInt(param++, idSolicitud);
			ps.setInt(param++, idDisposicion);
			Logger.debug("Ejecutando = "+query);
			Logger.debug("Disposicion = "+disposicion.toString());
			res = ps.executeUpdate();
			Logger.debug("Resultado = "+res);
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en updateDisposicion : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en updateDisposicion : "+e.getMessage());
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



	public int getNext(int idCliente, int idSolicitud) throws ClientesException{

		String query = "SELECT COALESCE(MAX(DP_NUMDISPOSICION),0)+1 AS NEXT FROM D_DISPOSICIONES WHERE DP_NUMCLIENTE = ? AND DP_NUMSOLICITUD = ?";
		Connection cn = null;
		int next = 1;
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(1, idCliente);
			ps.setInt(2, idSolicitud);
			ResultSet rs = ps.executeQuery();
			Logger.debug("Ejecutando = "+query);
			if( rs.next() ){
				next = rs.getInt("NEXT");
			}
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