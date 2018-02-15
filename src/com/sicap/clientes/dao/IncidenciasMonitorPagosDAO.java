package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.IncidenciasMonitorPagosVO;

public class IncidenciasMonitorPagosDAO extends DAOMaster{

	
	public int addIncidenciaMonitorPagos(IncidenciasMonitorPagosVO incidencia) throws ClientesException{

		String query =	"INSERT INTO d_incidencias_monitor_pagos " +
						"(im_numalerta, im_supervisor, im_gerente, im_gestor, im_fecha_alerta, im_numatraso, " +
						"im_numpago, im_estatus, im_fecha_registrada ) "+
						"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

		Connection cn = null;
		PreparedStatement ps = null;
		int param = 1;
		int res = 0;
		try{
			cn = getConnection();
			ps = cn.prepareStatement(query); 

			ps.setInt(param++, incidencia.numAlerta);
			ps.setInt(param++, incidencia.visitaSupervisor);
			ps.setInt(param++, incidencia.visitaGerente);
			ps.setInt(param++, incidencia.visitaGestor);
			ps.setDate(param++, incidencia.fechaAlerta);
			ps.setInt(param++, incidencia.numAtraso);
			ps.setInt(param++, incidencia.numPago);
			ps.setInt(param++, incidencia.estatus);
			ps.setDate(param++, incidencia.fechaRegistrada);

			//Logger.debug("Ejecutando = "+query);
			res = ps.executeUpdate();
			//Logger.debug("Resultado = "+res);
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en addIncidenciaMonitorPagos : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en addIncidenciaMonitorPagos : "+e.getMessage());
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

	
	public IncidenciasMonitorPagosVO[] getIncidenciasPorId(int idAlerta) throws ClientesException{
		IncidenciasMonitorPagosVO incidencia = null;
		Connection cn = null;
		ArrayList<IncidenciasMonitorPagosVO> array = new ArrayList<IncidenciasMonitorPagosVO>();
		IncidenciasMonitorPagosVO elementos[] = null;

		String query =	"SELECT * FROM d_incidencias_monitor_pagos WHERE IM_NUMALERTA = ?";
		
		try{
		cn = getConnection();
		PreparedStatement ps = cn.prepareStatement(query);
		ps.setInt(1, idAlerta);
		Logger.debug("Ejecutando = "+query);
		ResultSet rs = ps.executeQuery();
		while(rs.next()){
			incidencia =  new IncidenciasMonitorPagosVO();
			incidencia.estatus = rs.getInt("IM_ESTATUS");
			incidencia.fechaAlerta = rs.getDate("IM_FECHA_ALERTA");
			incidencia.fechaRegistrada = rs.getDate("IM_FECHA_REGISTRADA");
			incidencia.numAtraso = rs.getInt("IM_NUMATRASO");
			incidencia.numPago = rs.getInt("IM_NUMPAGO");
			incidencia.visitaSupervisor = rs.getInt("IM_SUPERVISOR");
			incidencia.visitaGerente = rs.getInt("IM_GERENTE");
			incidencia.visitaGestor = rs.getInt("IM_GESTOR");
			array.add(incidencia);
			Logger.debug("Incidencia encontrada : "+incidencia.toString());
		}
		}catch(SQLException sqle) {
			Logger.debug("SQLException en getIncidenciasPorId : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getIncidenciasPorId : "+e.getMessage());
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
		elementos = new IncidenciasMonitorPagosVO[array.size()];
	    for(int i=0;i<elementos.length; i++) 
	    	elementos[i] = (IncidenciasMonitorPagosVO)array.get(i);
		return elementos;
	}
	
}