package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.NegocioVO;


public class NegocioDAO extends DAOMaster{

 

	public NegocioVO getNegocio(int idCliente, int idSolicitud) throws ClientesException{
		NegocioVO  negocio = null;
		Connection cn = null;
		String query =	"SELECT NE_NUMCLIENTE, NE_NUMSOLICITUD, NE_ESTATUS, NE_RAZON_SOCIAL, NE_RFC, NE_ACTIVIDAD, NE_SECTOR, "+
						"NE_TELEFONO, NE_TELEFONO_CEL, NE_FECHA_VISITA, NE_TIEMPO_EXPERIENCIA, NE_EMPLEADOS, NE_VENTAS_CONTADO, NE_VENTAS_CREDITO, "+
						"NE_COMPRAS_CONTADO, NE_COMPRAS_CREDITO, NE_SITUACION_LOCAL, NE_ENTORNO_NEGOCIO, NE_REGISTROS_CONTABLES, "+
						"NE_AUTORIZACIONES_NEGOCIO, NE_FECHA_CAPTURA FROM D_NEGOCIOS WHERE NE_NUMCLIENTE = ? AND NE_NUMSOLICITUD = ?";
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(1, idCliente);
			ps.setInt(2, idSolicitud);
			//Logger.debug("Ejecutando = "+query);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				negocio =  new NegocioVO();
				negocio.idCliente = idCliente;
				negocio.idSolicitud = idSolicitud;
				negocio.estatus = rs.getInt("NE_ESTATUS");
				negocio.razonSocial = rs.getString("NE_RAZON_SOCIAL");
				negocio.rfc = rs.getString("NE_RFC");
				negocio.activiad = rs.getInt("NE_ACTIVIDAD");
				negocio.sector = rs.getInt("NE_SECTOR");
				negocio.telefono = rs.getString("NE_TELEFONO");
				negocio.telefonoCelular = rs.getString("NE_TELEFONO_CEL");
				negocio.fechaVisita = rs.getDate("NE_FECHA_VISITA");
				negocio.tiempoExperiencia = rs.getString("NE_TIEMPO_EXPERIENCIA");
				negocio.empleados = rs.getInt("NE_EMPLEADOS");
				negocio.ventasContado = rs.getInt("NE_VENTAS_CONTADO");
				negocio.ventasCredito = rs.getInt("NE_VENTAS_CREDITO");
				negocio.comprasContado = rs.getInt("NE_COMPRAS_CONTADO");
				negocio.comprasCredito = rs.getInt("NE_COMPRAS_CREDITO");
				negocio.situacionLocal = rs.getInt("NE_SITUACION_LOCAL");
				negocio.entornoNegocio = rs.getInt("NE_ENTORNO_NEGOCIO");
				negocio.registrosContables = rs.getInt("NE_REGISTROS_CONTABLES");
				negocio.autorizacionesNegocio = rs.getInt("NE_AUTORIZACIONES_NEGOCIO");
				negocio.fechaCaptura = rs.getTimestamp("NE_FECHA_CAPTURA");
			}
		}catch(SQLException sqle) {
			Logger.debug("SQLException en getNegocio : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getNegocio : "+e.getMessage());
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
		return negocio;
	}




	public int addNegocio(Connection conn, int idCliente, int idSolicitud, NegocioVO negocio) throws ClientesException{

		String query =	"INSERT INTO D_NEGOCIOS (NE_NUMCLIENTE, NE_NUMSOLICITUD, NE_ESTATUS, NE_RAZON_SOCIAL, "+
						"NE_RFC, NE_ACTIVIDAD, NE_SECTOR, NE_TELEFONO, NE_TELEFONO_CEL, NE_FECHA_VISITA, NE_TIEMPO_EXPERIENCIA, NE_EMPLEADOS, "+
						"NE_VENTAS_CONTADO, NE_VENTAS_CREDITO, NE_COMPRAS_CONTADO, NE_COMPRAS_CREDITO, "+
						"NE_SITUACION_LOCAL, NE_ENTORNO_NEGOCIO, NE_REGISTROS_CONTABLES, NE_AUTORIZACIONES_NEGOCIO, "+
						"NE_FECHA_CAPTURA) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		Connection cn = null;
		PreparedStatement ps = null;
		int param = 1;
		int  res = 0;
		try{
			if( conn==null ){
				cn = getConnection();
				ps = cn.prepareStatement(query); 
			}
			else{
				ps = conn.prepareStatement(query);
			}
			ps.setInt(param++, idCliente);
			ps.setInt(param++, idSolicitud);
			ps.setInt(param++, negocio.estatus);
			ps.setString(param++, negocio.razonSocial);
			ps.setString(param++, negocio.rfc);
			ps.setInt(param++, negocio.activiad);
			ps.setInt(param++, negocio.sector);
			ps.setString(param++, negocio.telefono);
			ps.setString(param++, negocio.telefonoCelular);
			ps.setDate(param++, negocio.fechaVisita);
			ps.setString(param++, negocio.tiempoExperiencia);
			ps.setInt(param++, negocio.empleados);
			ps.setInt(param++, negocio.ventasContado);
			ps.setInt(param++, negocio.ventasCredito);
			ps.setInt(param++, negocio.comprasContado);
			ps.setInt(param++, negocio.comprasCredito);
			ps.setInt(param++, negocio.situacionLocal);
			ps.setInt(param++, negocio.entornoNegocio);
			ps.setInt(param++, negocio.registrosContables);
			ps.setInt(param++, negocio.autorizacionesNegocio);
			ps.setTimestamp(param++, negocio.fechaCaptura);
			Logger.debug("Ejecutando = "+query);
			//Logger.debug("Negocio= "+negocio.toString());
			res = ps.executeUpdate();
			Logger.debug("Resultado = "+res);
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en addNegocio : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en addNegocio : "+e.getMessage());
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



//UPDATE D_NEGOCIOS SET NE_NUMCLIENTE = ?, NE_NUMSOLICITUD = ?, NE_ESTATUS = ?, NE_RAZON_SOCIAL = ?, NE_RFC = ?, NE_ACTIVIDAD = ?, NE_SECTOR = ?, NE_FECHA_VISITA = ?, NE_TIEMPO_EXPERIENCIA = ?, NE_EMPLEADOS = ?, NE_VENTAS_CONTADO = ?, NE_VENTAS_CREDITO = ?, NE_COMPRAS_CONTADO = ?, NE_COMPRAS_CREDITO = ?, NE_SITUACION_LOCAL = ?, NE_ENTORNO_NEGOCIO = ?, NE_REGISTROS_CONTABLES = ?, NE_AUTORIZACIONES_NEGOCIO = ? WHERE NE_NUMCLIENTE = ? AND NE_NUMSOLICITUD = ?

	public int updateNegocio (Connection conn, int idCliente, int idSolicitud, NegocioVO negocio) throws ClientesException{

		String query =	"UPDATE D_NEGOCIOS SET NE_ESTATUS = ?, NE_RAZON_SOCIAL = ?, NE_RFC = ?, NE_ACTIVIDAD = ?, "+
						"NE_SECTOR = ?, NE_TELEFONO = ?, NE_TELEFONO_CEL = ?, NE_FECHA_VISITA = ?, NE_TIEMPO_EXPERIENCIA = ?, NE_EMPLEADOS = ?, "+
						"NE_VENTAS_CONTADO = ?, NE_VENTAS_CREDITO = ?, NE_COMPRAS_CONTADO = ?, NE_COMPRAS_CREDITO = ?, "+
						"NE_SITUACION_LOCAL = ?, NE_ENTORNO_NEGOCIO = ?, NE_REGISTROS_CONTABLES = ?, "+
						"NE_AUTORIZACIONES_NEGOCIO = ? WHERE NE_NUMCLIENTE = ? AND NE_NUMSOLICITUD = ?";
		
		Connection cn = null;
		PreparedStatement ps = null;
		int  res = 0;
		int  param = 1;
		try{
			if( conn==null ){
				cn = getConnection();
				ps = cn.prepareStatement(query); 
			}
			else{
				ps = conn.prepareStatement(query);
			}
			ps.setInt(param++, negocio.estatus);
			ps.setString(param++, negocio.razonSocial);
			ps.setString(param++, negocio.rfc);
			ps.setInt(param++, negocio.activiad);
			ps.setInt(param++, negocio.sector);
			ps.setString(param++, negocio.telefono);
			ps.setString(param++, negocio.telefonoCelular);
			ps.setDate(param++, negocio.fechaVisita);
			ps.setString(param++, negocio.tiempoExperiencia);
			ps.setInt(param++, negocio.empleados);
			ps.setInt(param++, negocio.ventasContado);
			ps.setInt(param++, negocio.ventasCredito);
			ps.setInt(param++, negocio.comprasContado);
			ps.setInt(param++, negocio.comprasCredito);
			ps.setInt(param++, negocio.situacionLocal);
			ps.setInt(param++, negocio.entornoNegocio);
			ps.setInt(param++, negocio.registrosContables);
			ps.setInt(param++, negocio.autorizacionesNegocio);
			ps.setInt(param++, idCliente);
			ps.setInt(param++, idSolicitud);
			Logger.debug("Ejecutando = "+query);
			//Logger.debug("Negocio = "+negocio.toString());
			res = ps.executeUpdate();
			Logger.debug("Resultado = "+res);
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en updateNegocio : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en updateNegocio : "+e.getMessage());
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