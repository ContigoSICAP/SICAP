package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.IncidenciaSegurosVO;



/**
 * Módulo: Gestión Seguros: Clase entidad de IncidenciaSeguroDAO
 * @author jahtechnologies
 *
 */

public class IncidenciaSegurosDAO extends DAOMaster {
	public IncidenciaSegurosVO getIncidencias(Date fechaMovimiento) throws ClientesException{
		IncidenciaSegurosVO incidencias = null;
		Connection cn = null;
		//ArrayList array = new ArrayList();

		String query =	"SELECT IS_FECHA_MOVIMIENTO, IS_REFERENCIA, IS_ID_CLIENTE, IS_TIPO_INCIDENCIA, IS_OBSERVACIONES " + 
						" FROM D_INCIDENCIAS_SEGUROS WHERE IS_FECHA_MOVIMIENTO = ?";
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setDate(1, fechaMovimiento);
			Logger.debug("Ejecutando = "+query);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				incidencias =  new IncidenciaSegurosVO();
				incidencias.fechaMovimiento = fechaMovimiento;
				incidencias.referencia = rs.getString("IS_REFERENCIA");
				incidencias.idCliente = rs.getInt("IS_ID_CLIENTE");
				incidencias.tipoIncidencia = rs.getInt("IS_TIPO_INCIDENCIA");
				incidencias.observaciones = rs.getString("IS_OBSERVACIONES");
													
				//array.add(incidencias);
				Logger.debug("Incidencias Seguro encontrado : "+incidencias.toString());
			}
		}catch(SQLException sqle) {
			Logger.debug("SQLException en getSeguros : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getSeguros : "+e.getMessage());
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
		
		return incidencias;
	}

	public int addIncidencias(IncidenciaSegurosVO incidencias) throws ClientesException{

		String query =	"INSERT INTO D_INCIDENCIAS_SEGUROS (IS_FECHA_MOVIMIENTO, IS_REFERENCIA, IS_ID_CLIENTE, IS_TIPO_INCIDENCIA, IS_OBSERVACIONES,IS_FECHA_HORA,IS_BANCO_REFERENCIA)"+
						"VALUES (?, ?, ?, ?, ?,CURRENT_TIMESTAMP,?)";

		Connection cn = null;
		int param = 1;
		int  res = 0;
		try{
			PreparedStatement ps =null;
			cn = getConnection();
			ps = cn.prepareStatement(query); 
			ps.setDate(param++, incidencias.fechaMovimiento);
			ps.setString(param++, incidencias.referencia);
			ps.setInt(param++, incidencias.idCliente);
			ps.setInt(param++, incidencias.tipoIncidencia);
			ps.setString(param++, incidencias.observaciones);
			ps.setInt(param++, incidencias.bancoReferencia);
									
			Logger.debug("Ejecutando = "+query);
			res = ps.executeUpdate();
			Logger.debug("Resultado = "+res);
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en addIncidencias : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en addIncidencias : "+e.getMessage());
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

	public int updateIncidencias (IncidenciaSegurosVO incidencias) throws ClientesException{
		
		String query =	"UPDATE D_INCIDENCIAS_SEGUROS SET IS_REFERENCIA = ?, IS_ID_CLIENTE = ?, IS_TIPO_INCIDENCIA = ?, IS_OBSERVACIONES = ? " +
						"WHERE IS_FECHA_MOVIMIENTO = ?";
		
		Connection cn = null;
		int  res = 0;
		int  param = 1;
		try{
			PreparedStatement ps =null;
			cn = getConnection();
			ps = cn.prepareStatement(query); 
			
			ps.setString(param++, incidencias.referencia);
			ps.setInt(param++, incidencias.idCliente);
			ps.setInt(param++, incidencias.tipoIncidencia);
			ps.setString(param++, incidencias.observaciones);
			ps.setDate(param++, incidencias.fechaMovimiento);
										
			Logger.debug("Ejecutando = "+query);
			
			res = ps.executeUpdate();
			Logger.debug("Resultado = "+res);
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en updateIncidencias : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en updateIncidencias : "+e.getMessage());
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
