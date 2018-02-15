package com.sicap.clientes.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.ExpedienteVO;


public class ExpedienteDAO extends DAOMaster {
	
	public ExpedienteVO getExpediente(int idCliente, int idSolicitud) throws ClientesException{
		
		ExpedienteVO expediente = null;
		Connection cn = null;
		String query =	"SELECT EX_NUMCLIENTE, EX_NUMSOLICITUD, EX_SOLICITUDCREDITO, "+
		"EX_IDTITULAR, EX_IDSOLIDARIO, EX_IDAVAL, EX_COMPDOMICILIO, EX_AUTORIZASIC, EX_CONSULTATITULAR,  "+
		"EX_CONSULTASOLIDARIO, EX_CONSULTAAVALES, EX_CONSULTACIRCTITULAR, EX_CONSULTACIRCSOLIDARIO, EX_CONSULTACIRCAVALES, "+
		"EX_CONSULTAINTERTITULAR, EX_CONSULTAINTERSOLIDARIO, EX_CONSULTAINTERAVALES, EX_FORMATOEVALUACION, "+
		"EX_PERFILOPERACIONES, EX_FORMATOREF, EX_FORMATOCREDITO, EX_BITACORACOBRANZA, EX_TABLAAMORT, EX_PAGARE, "+
		"EX_CONTRATOCREDITO, EX_FACTGARANTIA, EX_FORMATOSEGURO, EX_FACTURABIENGARANTIA," +
		"EX_REGLAMENTOINTERNO, EX_ACTAFORMACIONGRUPO, EX_ANEXOBGRUPAL FROM D_EXPEDIENTES WHERE EX_NUMCLIENTE= ? " +
		"AND EX_NUMSOLICITUD= ? ";

		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(1, idCliente);
			ps.setInt(2, idSolicitud);
			//Logger.debug("Ejecutando = "+query);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				expediente = new ExpedienteVO();
				expediente.idCliente = idCliente;
				expediente.idSolicitud = idSolicitud;
				expediente.solicitudcredito = rs.getString("EX_SOLICITUDCREDITO");
				expediente.idtitular = rs.getString("EX_IDTITULAR");
				expediente.idsolidario = rs.getString("EX_IDSOLIDARIO");
				expediente.idaval = rs.getString("EX_IDAVAL");
				expediente.compdomicilio = rs.getString("EX_COMPDOMICILIO");
				expediente.autorizasic = rs.getString("EX_AUTORIZASIC");
				expediente.consultatitular = rs.getString("EX_CONSULTATITULAR");
				expediente.consultasolidario = rs.getString("EX_CONSULTASOLIDARIO");
				expediente.consultaavales = rs.getString("EX_CONSULTAAVALES");
				expediente.consultacirctitular = rs.getString("EX_CONSULTACIRCTITULAR");
				expediente.consultacircsolidario = rs.getString("EX_CONSULTACIRCSOLIDARIO");
				expediente.consultacircavales = rs.getString("EX_CONSULTACIRCAVALES");
				expediente.consultaintertitular = rs.getString("EX_CONSULTAINTERTITULAR");
				expediente.consultaintersolidario = rs.getString("EX_CONSULTAINTERSOLIDARIO");
				expediente.consultainteravales = rs.getString("EX_CONSULTAINTERAVALES");
				expediente.formatoevaluacion = rs.getString("EX_FORMATOEVALUACION");
				expediente.perfiloperaciones = rs.getString("EX_PERFILOPERACIONES");
				expediente.formatoref = rs.getString("EX_FORMATOREF");
				expediente.formatocredito = rs.getString("EX_FORMATOCREDITO");
				expediente.bitacoracobranza = rs.getString("EX_BITACORACOBRANZA");
				expediente.tablaamort = rs.getString("EX_TABLAAMORT");
				expediente.pagare = rs.getString("EX_PAGARE");
				expediente.contratocredito = rs.getString("EX_CONTRATOCREDITO"); 
				expediente.factgarantia = rs.getString("EX_FACTGARANTIA");
				expediente.formatoseguro = rs.getString("EX_FORMATOSEGURO");
				expediente.facturabiengarantia = rs.getString("EX_FACTURABIENGARANTIA");
				expediente.reglamentointerno = rs.getString("EX_REGLAMENTOINTERNO");
				expediente.actaformaciongrupo = rs.getString("EX_ACTAFORMACIONGRUPO");
				expediente.anexobgrupal = rs.getString("EX_ANEXOBGRUPAL");
			}
			
		}catch(SQLException sqle) {
				Logger.debug("SQLException en getExpediente : "+sqle.getMessage());
				sqle.printStackTrace();
				throw new ClientesException(sqle.getMessage());
			}
			catch(Exception e) {
				Logger.debug("Excepcion en getExpediente : "+e.getMessage());
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
			
		    return expediente;
		}
	
	
	
	
	public int deleteExpediente (int idCliente, int idSolicitud) throws ClientesException{
		
		Logger.debug("IDCLIENTE ES "+idCliente);
		Connection cn = null;
		int  res = 0;
		String deletequery = "DELETE FROM D_EXPEDIENTES WHERE EX_NUMCLIENTE= ? AND EX_NUMSOLICITUD= ? ";
		
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(deletequery);
			ps.setInt(1, idCliente);
		    ps.setInt(2, idSolicitud);
			Logger.debug("Ejecutando delete = "+deletequery);
			ps.executeUpdate();
		}catch(SQLException sqle) {
				Logger.debug("SQLException en getExpediente : "+sqle.getMessage());
				sqle.printStackTrace();
				throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getExpediente : "+e.getMessage());
			e.printStackTrace();
			throw new ClientesException(e.getMessage());
		}finally {
			try {
				if ( cn!=null ) cn.close();
			}catch(SQLException e) {
				throw new ClientesDBException(e.getMessage());
			}
		}
		return res;
      }
		
	public int addExpediente (int idCliente, int idSolicitud, ExpedienteVO expediente) throws ClientesException{
		
		String query =	"INSERT INTO D_EXPEDIENTES (EX_NUMCLIENTE, EX_NUMSOLICITUD, EX_SOLICITUDCREDITO, "+
		"EX_IDTITULAR, EX_IDSOLIDARIO, EX_IDAVAL, EX_COMPDOMICILIO, EX_AUTORIZASIC, EX_CONSULTATITULAR,  "+
		"EX_CONSULTASOLIDARIO, EX_CONSULTAAVALES, EX_CONSULTACIRCTITULAR, EX_CONSULTACIRCSOLIDARIO, EX_CONSULTACIRCAVALES, "+
		"EX_CONSULTAINTERTITULAR, EX_CONSULTAINTERSOLIDARIO, EX_CONSULTAINTERAVALES, EX_FORMATOEVALUACION, "+
		"EX_PERFILOPERACIONES, EX_FORMATOREF, EX_FORMATOCREDITO, EX_BITACORACOBRANZA, EX_TABLAAMORT, EX_PAGARE, "+
		"EX_CONTRATOCREDITO, EX_FACTGARANTIA, EX_FORMATOSEGURO, EX_FACTURABIENGARANTIA, EX_REGLAMENTOINTERNO, " +
		"EX_ACTAFORMACIONGRUPO, EX_ANEXOBGRUPAL) VALUES (?, ?, ?, ?, ?, ?, ?, " +
		"? ,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		Connection cn = null;
		int param = 1;
		int  res = 0;
	
		try{
			PreparedStatement ps =null;
			cn = getConnection();
			ps = cn.prepareStatement(query); 
			ps.setInt(param++, idCliente);
			ps.setInt(param++, idSolicitud);
			ps.setString(param++, expediente.solicitudcredito);
			ps.setString(param++, expediente.idtitular);
			ps.setString(param++, expediente.idsolidario);
			ps.setString(param++, expediente.idaval);
			ps.setString(param++, expediente.compdomicilio);
			ps.setString(param++, expediente.autorizasic);
			ps.setString(param++, expediente.consultatitular);
			ps.setString(param++, expediente.consultasolidario);
			ps.setString(param++, expediente.consultaavales);
			ps.setString(param++, expediente.consultacirctitular);
			ps.setString(param++, expediente.consultacircsolidario);
			ps.setString(param++, expediente.consultacircavales);
			ps.setString(param++, expediente.consultaintertitular);
			ps.setString(param++, expediente.consultaintersolidario);
			ps.setString(param++, expediente.consultainteravales);
			ps.setString(param++, expediente.formatoevaluacion);
			ps.setString(param++, expediente.perfiloperaciones);
			ps.setString(param++, expediente.formatoref);
			ps.setString(param++, expediente.formatocredito);
			ps.setString(param++, expediente.bitacoracobranza);
			ps.setString(param++, expediente.tablaamort);
			ps.setString(param++, expediente.pagare);
			ps.setString(param++, expediente.contratocredito);
			ps.setString(param++, expediente.factgarantia);
			ps.setString(param++, expediente.formatoseguro);
			ps.setString(param++, expediente.facturabiengarantia);
			ps.setString(param++, expediente.reglamentointerno);
			ps.setString(param++, expediente.actaformaciongrupo);
			ps.setString(param++, expediente.anexobgrupal);
			
			res = ps.executeUpdate();
		
		}catch(SQLException sqle) {
			Logger.debug("SQLException en addExpediente : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en addExpediente : "+e.getMessage());
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