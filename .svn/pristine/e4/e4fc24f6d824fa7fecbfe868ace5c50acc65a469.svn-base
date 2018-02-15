package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.ViviendaVO;


public class ViviendaDAO extends DAOMaster{

//SELECT VI_NUMCLIENTE, VI_NUMSOLICITUD, VI_ESTATUS, VI_TIPO_VIVIENDA, VI_IMPORTE, VI_NIVEL_VIVIENDA, VI_ZONA, VI_PISOS, VI_CUARTOS, VI_FACHADA, VI_TECHO, VI_TIEMPO_RESIDENCIA, VI_FECHA_CAPTURA FROM D_VIVIENDAS 

	public ViviendaVO getVivienda(int idCliente, int idSolicitud) throws ClientesException{
		ViviendaVO vivienda = null;
		Connection cn = null;
		String query =	"SELECT VI_NUMCLIENTE, VI_NUMSOLICITUD, VI_ESTATUS, VI_TIPO_VIVIENDA, "+
						"VI_IMPORTE, VI_NIVEL_VIVIENDA, VI_ZONA, VI_PISOS, VI_CUARTOS, VI_FACHADA, "+
						"VI_TECHO, VI_TIEMPO_RESIDENCIA, VI_FECHA_CAPTURA FROM D_VIVIENDAS "+
						"WHERE VI_NUMCLIENTE = ? AND VI_NUMSOLICITUD = ?";
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(1, idCliente);
			ps.setInt(2, idSolicitud);
			//Logger.debug("Ejecutando = "+query);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				vivienda =  new ViviendaVO();
				vivienda.idCliente = idCliente;
				vivienda.idSolicitud = idSolicitud;
				vivienda.estatus = rs.getInt("VI_ESTATUS");
				vivienda.tipoVivienda = rs.getInt("VI_TIPO_VIVIENDA");
				vivienda.impAlquilerHipoteca = rs.getDouble("VI_IMPORTE");
				vivienda.nivelVivienda = rs.getInt("VI_NIVEL_VIVIENDA");
				vivienda.zona = rs.getInt("VI_ZONA");
				vivienda.pisos = rs.getInt("VI_PISOS");
				vivienda.cuartos = rs.getInt("VI_CUARTOS");
				vivienda.fachada = rs.getInt("VI_FACHADA");
				vivienda.techo = rs.getInt("VI_TECHO");
				vivienda.tiempoResidencia = rs.getInt("VI_TIEMPO_RESIDENCIA");
				vivienda.fechaCaptura = rs.getTimestamp("VI_FECHA_CAPTURA");
			}
		}catch(SQLException sqle) {
			Logger.debug("SQLException en getVivienda : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getVivienda : "+e.getMessage());
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
		return vivienda;
	}

//INSERT INTO D_VIVIENDAS (VI_NUMCLIENTE, VI_NUMSOLICITUD, VI_ESTATUS, VI_TIPO_VIVIENDA, VI_IMPORTE, VI_NIVEL_VIVIENDA, VI_ZONA, VI_PISOS, VI_CUARTOS, VI_FACHADA, VI_TECHO, VI_TIEMPO_RESIDENCIA, VI_FECHA_CAPTURA) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)

	public int addVivienda(int idCliente, int idSolicitud, ViviendaVO vivienda) throws ClientesException{

		String query =	"INSERT INTO D_VIVIENDAS (VI_NUMCLIENTE, VI_NUMSOLICITUD, VI_ESTATUS, VI_TIPO_VIVIENDA, "+
						"VI_IMPORTE, VI_NIVEL_VIVIENDA, VI_ZONA, VI_PISOS, VI_CUARTOS, VI_FACHADA, VI_TECHO, "+
						"VI_TIEMPO_RESIDENCIA, VI_FECHA_CAPTURA) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		Connection cn = null;
		PreparedStatement ps = null;
		int param = 1;
		int  res = 0;
		try{
			cn = getConnection();
			ps = cn.prepareStatement(query); 
			ps.setInt(param++, idCliente);
			ps.setInt(param++, idSolicitud);
			ps.setInt(param++, vivienda.estatus);
			ps.setInt(param++, vivienda.tipoVivienda);
			ps.setDouble(param++, vivienda.impAlquilerHipoteca);
			ps.setInt(param++, vivienda.nivelVivienda);
			ps.setInt(param++, vivienda.zona);
			ps.setInt(param++, vivienda.pisos);
			ps.setInt(param++, vivienda.cuartos);
			ps.setInt(param++, vivienda.fachada);
			ps.setInt(param++, vivienda.techo);
			ps.setInt(param++, vivienda.tiempoResidencia);
			ps.setTimestamp(param++, vivienda.fechaCaptura);
			Logger.debug("Ejecutando = "+query);
			Logger.debug("Vivienda = "+vivienda.toString());
			res = ps.executeUpdate();
			Logger.debug("Resultado = "+res);
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en addVivienda : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en addVivienda : "+e.getMessage());
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



//UPDATE D_VIVIENDAS VI_ESTATUS = ?, VI_TIPO_VIVIENDA = ?, VI_IMPORTE = ?, VI_NIVEL_VIVIENDA = ?, VI_ZONA = ?, VI_PISOS = ?, VI_CUARTOS = ?, VI_FACHADA = ?, VI_TECHO = ?, VI_TIEMPO_RESIDENCIA = ? WHERE VI_NUMCLIENTE = ? AND VI_NUMSOLICITUD = ? 

	public int updateVivienda (int idCliente, int idSolicitud, ViviendaVO vivienda) throws ClientesException{

		String query =	"UPDATE D_VIVIENDAS SET VI_ESTATUS = ?, VI_TIPO_VIVIENDA = ?, VI_IMPORTE = ?, "+
						"VI_NIVEL_VIVIENDA = ?, VI_ZONA = ?, VI_PISOS = ?, VI_CUARTOS = ?, VI_FACHADA = ?, "+
						"VI_TECHO = ?, VI_TIEMPO_RESIDENCIA = ? WHERE VI_NUMCLIENTE = ? AND VI_NUMSOLICITUD = ?";
		
		Connection cn = null;
		PreparedStatement ps = null;
		int  res = 0;
		int  param = 1;
		try{
			cn = getConnection();
			ps = cn.prepareStatement(query); 
			ps.setInt(param++, vivienda.estatus);
			ps.setInt(param++, vivienda.tipoVivienda);
			ps.setDouble(param++, vivienda.impAlquilerHipoteca);
			ps.setInt(param++, vivienda.nivelVivienda);
			ps.setInt(param++, vivienda.zona);
			ps.setInt(param++, vivienda.pisos);
			ps.setInt(param++, vivienda.cuartos);
			ps.setInt(param++, vivienda.fachada);
			ps.setInt(param++, vivienda.techo);
			ps.setInt(param++, vivienda.tiempoResidencia);
			ps.setInt(param++, idCliente);
			ps.setInt(param++, idSolicitud);
			Logger.debug("Ejecutando = "+query);
			Logger.debug("Vivienda = "+vivienda.toString());
			res = ps.executeUpdate();
			Logger.debug("Resultado = "+res);
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en updateVivienda : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en updateVivienda : "+e.getMessage());
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