package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.LoteChequesVO;

public class LoteChequesDAO extends DAOMaster{

	public LoteChequesVO[] getLotesCheques(int idSucursal) throws ClientesException{

		String query = "SELECT LC_NUMLOTE, LC_NUMSUCURSAL, LC_NUMBANCO, LC_NUMCHEQUE_INI, LC_NUMCHEQUE_FIN, LC_FECHA_ALTA, LC_FECHA_BAJA " +
					   "FROM D_LOTES_CHEQUES WHERE LC_NUMSUCURSAL = ? AND LC_FECHA_BAJA IS NULL ORDER BY LC_NUMLOTE";

		Connection cn = null;
		ArrayList<LoteChequesVO> array = new ArrayList<LoteChequesVO>();
		LoteChequesVO temporal = null;
		LoteChequesVO elementos[] = null;
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(1, idSucursal);
			Logger.debug("Ejecutando : "+query);
			ResultSet rs = ps.executeQuery();
			while( rs.next() ){
				temporal = new LoteChequesVO();
				temporal.idLote = rs.getInt("LC_NUMLOTE");
				temporal.idSucursal = rs.getInt("LC_NUMSUCURSAL");
				temporal.idBanco = rs.getInt("LC_NUMBANCO");
				temporal.numChequeIni = rs.getInt("LC_NUMCHEQUE_INI");
				temporal.numChequeFin = rs.getInt("LC_NUMCHEQUE_FIN");
				temporal.fechaAlta = rs.getDate("LC_FECHA_ALTA");
				temporal.fechaBaja = rs.getDate("LC_FECHA_BAJA");
				array.add(temporal);
			}
			if ( array.size()>0 ){
				elementos = new LoteChequesVO[array.size()];
			    for(int i=0;i<elementos.length; i++) elementos[i] = (LoteChequesVO)array.get(i);
			}
		}catch(SQLException sqle) {
			Logger.debug("SQLException en getLotesCheques : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getLotesCheques : "+e.getMessage());
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


	public int addLote(LoteChequesVO lote) throws ClientesException{

		String query =	"INSERT INTO D_LOTES_CHEQUES (LC_NUMLOTE, LC_NUMSUCURSAL, LC_NUMBANCO, LC_NUMCHEQUE_INI, " +
						"LC_NUMCHEQUE_FIN, LC_FECHA_ALTA) VALUES (?, ?, ?, ?, ?, CURRENT_DATE)";
		int param = 1;
//		int res = 0;
		PreparedStatement ps = null;
		Connection cn = null;
		int next = getNext();
		try{
			cn = getConnection();
			ps = cn.prepareStatement(query); 

			ps.setInt(param++, next);
			ps.setInt(param++, lote.idSucursal);
			ps.setInt(param++, lote.idBanco);
			ps.setInt(param++, lote.numChequeIni);
			ps.setInt(param++, lote.numChequeFin);			
			Logger.debug("Ejecutando = "+query);
			ps.executeUpdate();
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en addLote : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en addLote : "+e.getMessage());
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