package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.ChequeVO;


public class ChequesDAO extends DAOMaster{


	public int getNumCheque(Connection conn, int numLote) throws ClientesException{

		String query = "SELECT MIN(CH_NUMCHEQUE) FROM D_CHEQUES WHERE CH_NUMLOTE = ? AND CH_ESTATUS = 0";

		Connection cn = null;
		PreparedStatement ps = null;
		int numeroCheque = 0;
		try{
			if ( conn!=null )
				ps = conn.prepareStatement(query);
			else{
				cn = getConnection();
				ps = cn.prepareStatement(query);
			}
			ps.setInt(1, numLote);
			Logger.debug("Ejecutando = "+query);
			ResultSet rs = ps.executeQuery();
			while( rs.next() ){
				numeroCheque = rs.getInt(1);			
			}
		}catch(SQLException sqle) {
			Logger.debug("SQLException en getNumCheque : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getNumCheque : "+e.getMessage());
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
	    return numeroCheque;

	}

	public int getCountCheques(int numLote) throws ClientesException{

		String query = "SELECT COUNT(*) FROM D_CHEQUES WHERE CH_NUMLOTE = ? AND CH_ESTATUS = 0";

		Connection cn = null;
		int numeroCheques = 0;
		
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(1, numLote);
			Logger.debug("Ejecutando : "+query);
			ResultSet rs = ps.executeQuery();
			while( rs.next() ){
				numeroCheques = rs.getInt(1);			
			}

		}catch(SQLException sqle) {
			Logger.debug("SQLException en getNumCheque : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getNumCheque : "+e.getMessage());
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
	    return numeroCheques;

	}	
	

	public int addCheque(Connection conn, ChequeVO cheque) throws ClientesException{

		String query =	"INSERT INTO D_CHEQUES (CH_NUMCHEQUE, CH_NUMLOTE) VALUES (?, ?)";
		int param = 1;
		int res = 0;
		PreparedStatement ps = null;
		Connection cn = null;

		try{
			if ( conn!=null )
				ps = conn.prepareStatement(query);
			else{
				cn = getConnection();
				ps = cn.prepareStatement(query);
			}
			ps.setInt(param++, cheque.numCheque);
			ps.setInt(param++, cheque.numLote);
			Logger.debug("Ejecutando = "+query);
			res = ps.executeUpdate();
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en addCheque : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en addCheque : "+e.getMessage());
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


	public int updateLoteCheques(int idLote) throws ClientesException{

		int res = 0;
		String query =	"UPDATE D_LOTES_CHEQUES SET LC_FECHA_BAJA = CURRENT_DATE WHERE LC_NUMLOTE = ?";
		int param = 1;
		PreparedStatement ps = null;
		Connection cn = null;
		try{
			cn = getConnection();
			ps = cn.prepareStatement(query); 
			ps.setInt(param++, idLote);
			Logger.debug("Ejecutando = "+query);
			res = ps.executeUpdate();
			Logger.debug("Resultado = "+res);
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en updateLoteCheques : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en updateLoteCheques : "+e.getMessage());
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

	
	public int updateCheque(Connection conn, ChequeVO cheque) throws ClientesException{

		int res = 0;
		String query =	"UPDATE D_CHEQUES SET CH_NUMCLIENTE = ?, CH_NUMSOLICITUD = ?, CH_NUMGRUPO = ?, CH_NUMCICLO = ?, CH_ESTATUS = ?, " +
						"CH_FECHA_ASIGNACION = ?, CH_FECHA_CANCELACION = ?, CH_TIPO_CANCELACION = ?, CH_COMENTARIOS = ? " +
						"WHERE CH_NUMCHEQUE = ? AND CH_NUMLOTE = ?";
		Connection cn = null;
		PreparedStatement ps = null;
		int param = 1;
		try{
			if( conn==null ){
				cn = getConnection();
				ps = cn.prepareStatement(query); 
			}
			else{
				ps = conn.prepareStatement(query);
			}
			ps.setInt(param++, cheque.numCliente);
			ps.setInt(param++, cheque.numSolicitud);
			ps.setInt(param++, cheque.numGrupo);
			ps.setInt(param++, cheque.numCiclo);
			ps.setInt(param++, cheque.estatus);
			ps.setDate(param++, cheque.fechaAsignacion);
			ps.setDate(param++, cheque.fechaCancelacion);
			ps.setInt(param++, cheque.tipoCancelacion);
			ps.setString(param++, cheque.comentarios);
			ps.setInt(param++, cheque.numCheque);
			ps.setInt(param++, cheque.numLote);
			Logger.debug("Ejecutando = "+query);
			Logger.debug("Cheque = "+cheque.toString());
			res = ps.executeUpdate();
			Logger.debug("Resultado = "+res);
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en updateCheque : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en updateCheque : "+e.getMessage());
			e.printStackTrace();
			throw new ClientesException(e.getMessage());
		}

		return res;
	}

	
	public ChequeVO getCheque(int numCheque, int idCliente, int idSucursal) throws ClientesException{

		String query =	"SELECT CH_NUMLOTE, CH_NUMCLIENTE, CH_NUMSOLICITUD, CH_NUMGRUPO, CH_NUMCICLO, CH_ESTATUS, CH_FECHA_ASIGNACION, " +
						"CH_FECHA_CANCELACION FROM D_CHEQUES, D_LOTES_CHEQUES WHERE CH_NUMCHEQUE = ? AND CH_NUMCLIENTE = ? AND " +
						"LC_NUMSUCURSAL = ? AND CH_FECHA_CANCELACION IS NULL AND LC_NUMLOTE = CH_NUMLOTE";

		Connection cn = null;
		ChequeVO cheque = null;
		
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(1, numCheque);
			ps.setInt(2, idCliente);
			ps.setInt(3, idSucursal);
			Logger.debug("Ejecutando : "+query);
			ResultSet rs = ps.executeQuery();
			while( rs.next() ){
				cheque = new ChequeVO();
				cheque.numCheque = numCheque;
				cheque.numCliente = idCliente;
				cheque.numLote = rs.getInt("CH_NUMLOTE");
				cheque.numSolicitud = rs.getInt("CH_NUMSOLICITUD");
				cheque.numGrupo = rs.getInt("CH_NUMGRUPO");
				cheque.numCiclo = rs.getInt("CH_NUMCICLO");
				cheque.estatus = rs.getInt("CH_ESTATUS");
				cheque.fechaAsignacion = rs.getDate("CH_FECHA_ASIGNACION");
				cheque.fechaCancelacion = rs.getDate("CH_FECHA_CANCELACION");
			}
		}catch(SQLException sqle) {
			Logger.debug("SQLException en getCheque : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getCheque : "+e.getMessage());
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

		return cheque;
	}
	
	

	public int getNext() throws ClientesException{

		String query = "SELECT COALESCE(MAX(LC_NUMLOTE),0)+1 AS NEXT FROM D_LOTES_CHEQUES";
		Connection cn = null;
		int next = 1;
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
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