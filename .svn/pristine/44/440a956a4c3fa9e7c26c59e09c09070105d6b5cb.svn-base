package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.ReporteCobranzaGrupalVO;

public class ReporteCobranzaGrupalDAO extends DAOMaster{


	public ReporteCobranzaGrupalVO[] getReportesCobranza(int idAlerta) throws ClientesException{

		String query = " SELECT * FROM D_REPORTE_COBRANZA A WHERE RC_ID_ALERTA = ? ";
		
		ArrayList<ReporteCobranzaGrupalVO> array = new ArrayList<ReporteCobranzaGrupalVO>();
		ReporteCobranzaGrupalVO temporal = null;
		ReporteCobranzaGrupalVO elementos[] = null;
		Connection cn = null;
		
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(1, idAlerta);
			Logger.debug("Ejecutando = "+query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				temporal = new ReporteCobranzaGrupalVO();
				temporal.idAlerta = rs.getInt("RC_ID_ALERTA");
				temporal.numCliente = rs.getInt("rc_numero_cliente");
				temporal.nombreCliente = rs.getString("rc_nombre_cliente");
				temporal.usuario= rs.getString("rc_usuario");
				temporal.fechaCaptura = rs.getTimestamp("rc_fecha_captura");
				temporal.motivoNoContacto = rs.getInt("rc_motivo_no_contacto");
				temporal.realizaPagos = rs.getBoolean("rc_realiza_pagos");
				temporal.receptorPagos = rs.getInt("rc_receptor_pagos");
				temporal.receptorPagosOtro = rs.getString("rc_receptor_pagos_otro");
				temporal.numerofaltas = rs.getInt("rc_numero_faltas");
				temporal.asesorVisitaSemanal = rs.getBoolean("rc_asesor_visita_semanal");
				temporal.asesorPuntual = rs.getBoolean("rc_asesor_puntual");
				temporal.asesorProductivo = rs.getBoolean("rc_asesor_productivo");
				temporal.asesorRespeta = rs.getBoolean("rc_asesor_respeta");
				temporal.asesorRecibePagos = rs.getBoolean("rc_asesor_recibe_pagos");
				temporal.comentarios = rs.getString("rc_comentarios");
				array.add(temporal);
				Logger.debug("REPORTE : "+temporal.toString());
			}
			if ( array.size()>0 ){
				elementos = new ReporteCobranzaGrupalVO[array.size()];
			    for(int i=0;i<elementos.length; i++) elementos[i] = (ReporteCobranzaGrupalVO)array.get(i);
			}
		}catch(SQLException sqle) {
			Logger.debug("SQLException en getReportesCobranza : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getReportesCobranza : "+e.getMessage());
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
	
	
	public int addReporteCobranza(ReporteCobranzaGrupalVO reporteCobranza) throws ClientesException{

		String query =	"INSERT INTO D_REPORTE_COBRANZA "+
						"VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		Connection cn = null;
		PreparedStatement ps = null;
		int param = 1;
		int res = 0;
		try{
			cn = getConnection();
			ps = cn.prepareStatement(query); 

			ps.setInt(param++, reporteCobranza.idAlerta);
			ps.setInt(param++, reporteCobranza.numCliente);
			ps.setString(param++, reporteCobranza.nombreCliente);
			ps.setString(param++, reporteCobranza.usuario);
			
			ps.setInt(param++, reporteCobranza.motivoNoContacto);
			ps.setBoolean(param++, reporteCobranza.realizaPagos);
			ps.setInt(param++, reporteCobranza.receptorPagos);
			ps.setString(param++, reporteCobranza.receptorPagosOtro);
			ps.setInt(param++, reporteCobranza.numerofaltas);
			ps.setBoolean(param++, reporteCobranza.asesorVisitaSemanal);
			ps.setBoolean(param++, reporteCobranza.asesorPuntual);
			ps.setBoolean(param++, reporteCobranza.asesorProductivo);
			ps.setBoolean(param++, reporteCobranza.asesorRespeta);
			ps.setBoolean(param++, reporteCobranza.asesorRecibePagos);
			ps.setString(param++, reporteCobranza.comentarios);

			Logger.debug("Ejecutando = "+query);
			res = ps.executeUpdate();
			Logger.debug("Resultado = "+res);
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en ReporteCobranzaGrupalVO : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en ReporteCobranzaGrupalVO : "+e.getMessage());
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