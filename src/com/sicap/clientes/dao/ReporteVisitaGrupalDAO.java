package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.ReporteVisitaGrupalVO;

public class ReporteVisitaGrupalDAO extends DAOMaster{


	public ReporteVisitaGrupalVO getReporteVisitaGrupal(int numeroAlerta) throws ClientesException{

		String query = " SELECT * FROM D_REPORTE_VISITA_GRUPAL A WHERE RV_ID_ALERTA = ? ";
		
		ReporteVisitaGrupalVO temporal = null;
		Connection cn = null;
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(1, numeroAlerta);
			Logger.debug("Ejecutando = "+query);
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()){
				temporal = new ReporteVisitaGrupalVO();
				temporal.idAlerta = rs.getInt("RV_ID_ALERTA");
				temporal.usuario= rs.getString("RV_USUARIO");
				temporal.fechaCaptura = rs.getTimestamp("RV_FECHA_CAPTURA");
				temporal.problemasGrupo = rs.getInt("RV_PROBLEMAS_GRUPO");
				temporal.problemasAsesor = rs.getInt("RV_PROBLEMAS_ASESOR");
				temporal.problemasNegocio = rs.getInt("RV_PROBLEMAS_NEGOCIO");
				temporal.problemasPersonales = rs.getInt("RV_PROBLEMAS_PERSONALES");
				temporal.problemasOtros = rs.getInt("RV_PROBLEMAS_OTROS");
				temporal.propuestaSolucion = rs.getInt("RV_PROPUESTA_SOLUCION");
				temporal.comentarios = rs.getString("RV_COMENTARIOS");
				temporal.integrantesVisitados = rs.getString("RV_INTEGRANTES_VISITADOS");
				Logger.debug("REPORTE VISITA: "+temporal.toString());
			}

		}catch(SQLException sqle) {
			Logger.debug("SQLException en ReporteVisitaGrupalDAO : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en ReporteVisitaGrupalDAO : "+e.getMessage());
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
	    return temporal;
	}
	
	
	public int addReporteVisitaGrupal(ReporteVisitaGrupalVO reporteVisita) throws ClientesException{

		String query =	"INSERT INTO D_REPORTE_VISITA_GRUPAL "+
						"VALUES (?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?, ?, ?)";

		Connection cn = null;
		PreparedStatement ps = null;
		int param = 1;
		int res = 0;
		try{
			cn = getConnection();
			ps = cn.prepareStatement(query); 

			ps.setInt(param++, reporteVisita.idAlerta);
			ps.setString(param++, reporteVisita.usuario);
			ps.setInt(param++, reporteVisita.problemasGrupo);
			ps.setInt(param++, reporteVisita.problemasAsesor);
			ps.setInt(param++, reporteVisita.problemasNegocio);
			ps.setInt(param++, reporteVisita.problemasPersonales);
			ps.setInt(param++, reporteVisita.problemasOtros);
			ps.setInt(param++, reporteVisita.propuestaSolucion);
			ps.setString(param++, reporteVisita.integrantesVisitados);
			ps.setString(param++, reporteVisita.comentarios);
			
			Logger.debug("Ejecutando = "+query);
			res = ps.executeUpdate();
			Logger.debug("Resultado = "+res);
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en addReporteVisitaGrupal : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en addReporteVisitaGrupal : "+e.getMessage());
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