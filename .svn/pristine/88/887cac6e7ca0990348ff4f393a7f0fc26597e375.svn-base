package com.sicap.clientes.dao.inffinix;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.inffinix.SolicitudInfinix;


public class SolicitudInffinixDAO extends SolicitudDAO{


	public SolicitudInfinix[] getSolicitudes(int idCliente) throws ClientesException{

		String query = 	"SELECT D_SOLICITUDES.*, TMP_REFERENCIAS.* FROM D_SOLICITUDES, D_SALDOS_T24, TMP_REFERENCIAS " +
						"WHERE SO_NUMCLIENTE = ? AND SO_NUMCLIENTE = ST_NUMCLIENTE AND SO_NUMSOLICITUD " +
						"= ST_CICLO AND REFERENCIA = ST_REFERENCIA AND APARICIONES = 1 AND ST_NUMOPERACION NOT IN (3, 5)";
		Connection cn = null;
		SolicitudInfinix solicitud = null;
		ArrayList<SolicitudInfinix> array = new ArrayList<SolicitudInfinix>();
        SolicitudInfinix elementos[] = null;
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(1,idCliente);
			Logger.debug("Ejecutando : "+query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				solicitud = new SolicitudInfinix();
				solicitud.idCliente = idCliente;
				solicitud.idSolicitud = rs.getInt("SO_NUMSOLICITUD");
				solicitud.estatus = rs.getInt("SO_ESTATUS");
				solicitud.cveSolicitud = rs.getString("SO_CVESOLICITUD");
				solicitud.idSucursal = rs.getInt("SO_NUMSUCURSAL");
				solicitud.tipoOperacion = rs.getInt("SO_NUMOPERACION");
				solicitud.fechaFirma = rs.getDate("SO_FECHA_FIRMA");
				solicitud.fechaCaptura = rs.getTimestamp("SO_FECHA_CAPTURA");
				solicitud.medio = rs.getInt("SO_NUMMEDIO");
				solicitud.idEjecutivo = rs.getInt("SO_NUMEJECUTIVO");
				solicitud.fuente = rs.getInt("SO_FUENTE");
				solicitud.montoSolicitado = rs.getDouble("SO_MONTO_SOLICITADO");
				solicitud.plazoSolicitado = rs.getInt("SO_PLAZO_SOLICITADO");
				solicitud.frecuenciaPagoSolicitada = rs.getInt("SO_FRECPAGO_SOLICITADA");
				solicitud.destinoCredito = rs.getInt("SO_DESTINO_CREDITO");
				solicitud.montoPropuesto = rs.getDouble("SO_MONTO_PROPUESTO");
				solicitud.plazoPropuesto = rs.getInt("SO_PLAZO_PROPUESTO");
				solicitud.frecuenciaPagoPropuesta = rs.getInt("SO_FRECPAGO_PROPUESTA");
				solicitud.cuota = rs.getDouble("SO_CUOTA");
				solicitud.contrato = rs.getString("SO_NO_CONTRATO");
				solicitud.referencia = rs.getString("REFERENCIA"); 
				array.add(solicitud);
				//Logger.debug("Solicitud encontrada : "+solicitud.toString());
			}
			
		}catch(SQLException sqle) {
			Logger.debug("SQLException en getSolicitud : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getSolicitud : "+e.getMessage());
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
		elementos = new SolicitudInfinix[array.size()];
	    for(int i=0;i<elementos.length; i++) elementos[i] = array.get(i);
	    return elementos;

	}
	
	public SolicitudInfinix getSolicitudesByNumClienteSolicitud(int idCliente, int idSolicitud) throws ClientesException{
		String query = 	"SELECT D_SOLICITUDES.*, TMP_REFERENCIAS.* FROM D_SOLICITUDES, D_SALDOS_T24, TMP_REFERENCIAS " +
						"WHERE SO_NUMCLIENTE = ? AND SO_NUMSOLICITUD = ? AND SO_NUMCLIENTE = ST_NUMCLIENTE AND SO_NUMSOLICITUD " +
						"= ST_CICLO AND REFERENCIA = ST_REFERENCIA AND APARICIONES = 1 AND ST_NUMOPERACION NOT IN (3, 5)";
		Connection cn = null;
		SolicitudInfinix solicitud = null;
		int param = 1;
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(param++,idCliente);
			ps.setInt(param++,idSolicitud);
			Logger.debug("Ejecutando : "+query);
			Logger.debug("Para cliente : "+idCliente);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				solicitud = new SolicitudInfinix();
				solicitud.idCliente = idCliente;
				solicitud.idSolicitud = rs.getInt("SO_NUMSOLICITUD");
				solicitud.estatus = rs.getInt("SO_ESTATUS");
				solicitud.cveSolicitud = rs.getString("SO_CVESOLICITUD");
				solicitud.idSucursal = rs.getInt("SO_NUMSUCURSAL");
				solicitud.tipoOperacion = rs.getInt("SO_NUMOPERACION");
				solicitud.fechaFirma = rs.getDate("SO_FECHA_FIRMA");
				solicitud.fechaCaptura = rs.getTimestamp("SO_FECHA_CAPTURA");
				solicitud.medio = rs.getInt("SO_NUMMEDIO");
				solicitud.idEjecutivo = rs.getInt("SO_NUMEJECUTIVO");
				solicitud.fuente = rs.getInt("SO_FUENTE");
				solicitud.montoSolicitado = rs.getDouble("SO_MONTO_SOLICITADO");
				solicitud.plazoSolicitado = rs.getInt("SO_PLAZO_SOLICITADO");
				solicitud.frecuenciaPagoSolicitada = rs.getInt("SO_FRECPAGO_SOLICITADA");
				solicitud.destinoCredito = rs.getInt("SO_DESTINO_CREDITO");
				solicitud.montoPropuesto = rs.getDouble("SO_MONTO_PROPUESTO");
				solicitud.plazoPropuesto = rs.getInt("SO_PLAZO_PROPUESTO");
				solicitud.frecuenciaPagoPropuesta = rs.getInt("SO_FRECPAGO_PROPUESTA");
				solicitud.cuota = rs.getDouble("SO_CUOTA");
				solicitud.contrato = rs.getString("SO_NO_CONTRATO");
				solicitud.referencia = rs.getString("REFERENCIA"); 
				Logger.debug("Solicitud encontrada : "+solicitud.toString());
			}
		}catch(SQLException sqle) {
			Logger.debug("SQLException en getSolicitud : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getSolicitud : "+e.getMessage());
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
		return solicitud;
	}
}