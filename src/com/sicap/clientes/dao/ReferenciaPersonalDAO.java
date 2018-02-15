package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.ReferenciaPersonalVO;


public class ReferenciaPersonalDAO extends DAOMaster {

	public ReferenciaPersonalVO getReferenciaPersonal( int idReferencia,int idCliente, int idSolicitud ) throws ClientesException {
		ReferenciaPersonalVO referenciaPersonal = null;
		Connection cn = null;
		try {
			cn = getConnection();
			String query = 	"SELECT RC_IDREFERENCIA,RC_IDCLIENTE,RC_IDSOLICITUD,RC_NOMBRE,RC_TELEFONO,"
							+ "RC_HORARIOLLAMADA,RC_RELACION,RC_TIEMPOCONOCIMIENTO,RC_CONOCIMIENTOOCUPACION,"
							+ "RC_VISITANEGOCIO,RC_DONDEVENDE,RC_TIEMPOOPERACION,RC_STATUSVENTAS,RC_CONQUIENVIVE,"
							+ "RC_CONOCIMIENTOVIVIENDA,RC_IMPEDIMENTOPAGO,RC_PRESTAMODINERO,RC_RECOMENDACIONCREDITO,"
							+ "RC_PRESTRIADINERO,RC_DISPONIBILIDADRESPALDO,RC_DESCRIPCIONCLIENTE,RC_CALIFICACIONCLIENTE,"
							+ "RC_FECHAREALIZACIONCONSULTA,RC_RECOMENDACIONCREDITO,RC_FECHACAPTURA,RC_DIRECCION FROM D_REFERENCIA_PERSONAL WHERE RC_IDCLIENTE=? AND RC_IDSOLICITUD=? AND RC_IDREFERENCIA=?";

			PreparedStatement ps = cn.prepareStatement(query);
			
			ps.setInt(1, idSolicitud);
			ps.setInt(2, idCliente);
			ps.setInt(3, idReferencia);
			
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				referenciaPersonal  = new ReferenciaPersonalVO();
				referenciaPersonal.idCliente = rs.getInt("RC_idCliente");
				referenciaPersonal.idSolicitud = rs.getInt("RC_idSolicitud");
				referenciaPersonal.nombre = rs.getString("RC_nombre");
				referenciaPersonal.telefono = rs.getString("RC_telefono");
				referenciaPersonal.horarioLlamada = rs.getString("RC_horarioLlamada");
				referenciaPersonal.relacion = rs.getString("RC_relacion");
				referenciaPersonal.tiempoConocimiento = rs.getString("RC_tiempoConocimiento");
				referenciaPersonal.conocimientoOcupacion = rs.getString("RC_conocimientoOcupacion");
				referenciaPersonal.visitaNegocio = rs.getString("RC_visitaNegocio");
				referenciaPersonal.dondeVende = rs.getString("RC_dondeVende");
				referenciaPersonal.tiempoOperacion = rs.getString("RC_tiempoOperacion");
				referenciaPersonal.statusVentas = rs.getString("RC_statusVentas");
				referenciaPersonal.conQuienVive = rs.getString("RC_conQuienVive");
				referenciaPersonal.conocimientoVivienda = rs.getString("RC_conocimientoVivienda");
				referenciaPersonal.impedimentoPago = rs.getString("RC_impedimentoPago");
				referenciaPersonal.prestamoDinero = rs.getString("RC_prestamoDinero");
				referenciaPersonal.recomendacionCredito = rs.getString("RC_recomendacionCredito");
				referenciaPersonal.prestriaDinero = rs.getString("RC_prestriaDinero");
				referenciaPersonal.disponibilidadRespaldo = rs.getString("RC_disponibilidadRespaldo");
				referenciaPersonal.descripcionCliente = rs.getString("RC_descripcionCliente");
				referenciaPersonal.calificacionCliente = rs.getInt("RC_calificacionCliente");
				referenciaPersonal.fechaRealizacionConsulta = rs.getDate("RC_fechaRealizacionConsulta");
				referenciaPersonal.recomendacionCredito = rs.getString("RC_recomendacionCredito");
				referenciaPersonal.fechaCaptura = rs.getTimestamp("RC_fechaCaptura");
				referenciaPersonal.direccion = rs.getString("RC_DIRECCION");
			}

		} catch (SQLException sqle) {
			Logger.debug("SQLException en getReferenciaPersonal : "
					+ sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		} catch (Exception e) {
			Logger.debug("Excepcion en getReferenciaPersonal : " + e.getMessage());
			e.printStackTrace();
			throw new ClientesException(e.getMessage());
		} finally {
			try {
				if (cn != null)
					cn.close();
			} catch (SQLException e) {
				throw new ClientesDBException(e.getMessage());
			}
		}

		return referenciaPersonal;

	}

	

	public ReferenciaPersonalVO[] getReferenciaPersonales(int idCliente, int idSolicitud) throws ClientesException{

		String query = 	"SELECT RC_IDREFERENCIA,RC_IDCLIENTE,RC_IDSOLICITUD,RC_NOMBRE,RC_TELEFONO,"
			+ "RC_HORARIOLLAMADA,RC_RELACION,RC_TIEMPOCONOCIMIENTO,RC_CONOCIMIENTOOCUPACION,"
			+ "RC_VISITANEGOCIO,RC_DONDEVENDE,RC_TIEMPOOPERACION,RC_STATUSVENTAS,RC_CONQUIENVIVE,"
			+ "RC_CONOCIMIENTOVIVIENDA,RC_IMPEDIMENTOPAGO,RC_PRESTAMODINERO,RC_RECOMENDACIONCREDITO,"
			+ "RC_PRESTRIADINERO,RC_DISPONIBILIDADRESPALDO,RC_DESCRIPCIONCLIENTE,RC_CALIFICACIONCLIENTE,"
			+ "RC_FECHAREALIZACIONCONSULTA,RC_RECOMENDACIONCREDITO,RC_FECHACAPTURA,RC_DIRECCION FROM D_REFERENCIA_PERSONAL WHERE RC_IDCLIENTE=? AND RC_IDSOLICITUD=? ";
		ArrayList<ReferenciaPersonalVO> array = new ArrayList<ReferenciaPersonalVO>();
		ReferenciaPersonalVO temporal = null;
		ReferenciaPersonalVO elementos[] = null;
		Connection cn = null;
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(1, idCliente);
			ps.setInt(2, idSolicitud);
			//Logger.debug("Ejecutando : "+query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				temporal = new ReferenciaPersonalVO();
				temporal.idReferencia = rs.getInt("RC_IDREFERENCIA");
				temporal.idCliente = rs.getInt("RC_idCliente");
				temporal.idSolicitud = rs.getInt("RC_idSolicitud");
				temporal.nombre = rs.getString("RC_nombre");
				temporal.telefono = rs.getString("RC_telefono");
				temporal.horarioLlamada = rs.getString("RC_horarioLlamada");
				temporal.relacion = rs.getString("RC_relacion");
				temporal.tiempoConocimiento = rs.getString("RC_tiempoConocimiento");
				temporal.conocimientoOcupacion = rs.getString("RC_conocimientoOcupacion");
				temporal.visitaNegocio = rs.getString("RC_visitaNegocio");
				temporal.dondeVende = rs.getString("RC_dondeVende");
				temporal.tiempoOperacion = rs.getString("RC_tiempoOperacion");
				temporal.statusVentas = rs.getString("RC_statusVentas");
				temporal.conQuienVive = rs.getString("RC_conQuienVive");
				temporal.conocimientoVivienda = rs.getString("RC_conocimientoVivienda");
				temporal.impedimentoPago = rs.getString("RC_impedimentoPago");
				temporal.prestamoDinero = rs.getString("RC_prestamoDinero");
				temporal.recomendacionCredito = rs.getString("RC_recomendacionCredito");
				temporal.prestriaDinero = rs.getString("RC_prestriaDinero");
				temporal.disponibilidadRespaldo = rs.getString("RC_disponibilidadRespaldo");
				temporal.descripcionCliente = rs.getString("RC_descripcionCliente");
				temporal.calificacionCliente = rs.getInt("RC_calificacionCliente");
				temporal.fechaRealizacionConsulta = rs.getDate("RC_fechaRealizacionConsulta");
				temporal.recomendacionCredito = rs.getString("RC_recomendacionCredito");
				temporal.fechaCaptura = rs.getTimestamp("RC_fechaCaptura");
				temporal.direccion = rs.getString("RC_DIRECCION");
				//Logger.debug("Referencia Personal encontrada : "+temporal.toString());
				array.add(temporal);
			}
		}catch(SQLException sqle) {
			Logger.debug("SQLException en getReferenciaPersonales : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getReferenciaPersonales : "+e.getMessage());
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
		elementos = new ReferenciaPersonalVO[array.size()];
	    for(int i=0;i<elementos.length; i++) elementos[i] = (ReferenciaPersonalVO)array.get(i);
	    return elementos;

	}

	
	public int addReferenciaPersonal(int idCliente, int idSolicitud,ReferenciaPersonalVO referenciaPersonal) throws ClientesException {
		return addReferenciaPersonal(null, idCliente, idSolicitud, referenciaPersonal);
	}
	

	public int addReferenciaPersonal(Connection conn, int idCliente, int idSolicitud,ReferenciaPersonalVO referenciaPersonal) throws ClientesException {
		int res = 0;
		String query =	"INSERT INTO D_REFERENCIA_PERSONAL  (RC_IDREFERENCIA,RC_IDCLIENTE,RC_IDSOLICITUD"
						+ ",RC_NOMBRE,RC_TELEFONO,RC_HORARIOLLAMADA,RC_RELACION,RC_TIEMPOCONOCIMIENTO,"
						+ "RC_CONOCIMIENTOOCUPACION,RC_VISITANEGOCIO,RC_DONDEVENDE,RC_TIEMPOOPERACION,RC_STATUSVENTAS,"
						+ "RC_CONQUIENVIVE,RC_CONOCIMIENTOVIVIENDA,RC_IMPEDIMENTOPAGO,RC_PRESTAMODINERO,RC_RECOMENDACIONCREDITO,"
						+ "RC_PRESTRIADINERO,RC_DISPONIBILIDADRESPALDO,RC_DESCRIPCIONCLIENTE,RC_CALIFICACIONCLIENTE,"
						+ "RC_FECHAREALIZACIONCONSULTA,RC_FECHACAPTURA,RC_DIRECCION) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		int param = 1;
		 int next = 0;
		Connection cn = null;
		PreparedStatement ps =null;
		try {
			
			if( conn==null ){
				next = getNext(idCliente, idSolicitud);
				cn = getConnection();
				ps = cn.prepareStatement(query); 
			}
			else{
				next = getNext(conn, idCliente, idSolicitud);
				ps = conn.prepareStatement(query);
			}
			ps.setInt(param++, next);
			ps.setInt(param++, idCliente);
			ps.setInt(param++, idSolicitud);
			ps.setString(param++, referenciaPersonal.nombre);
			ps.setString(param++, referenciaPersonal.telefono);
			ps.setString(param++, referenciaPersonal.horarioLlamada);
			ps.setString(param++, referenciaPersonal.relacion);
			ps.setString(param++, referenciaPersonal.tiempoConocimiento);
			ps.setString(param++, referenciaPersonal.conocimientoOcupacion);
			ps.setString(param++, referenciaPersonal.visitaNegocio);
			ps.setString(param++, referenciaPersonal.dondeVende);
			ps.setString(param++, referenciaPersonal.tiempoOperacion);
			ps.setString(param++, referenciaPersonal.statusVentas);
			ps.setString(param++, referenciaPersonal.conQuienVive);
			ps.setString(param++, referenciaPersonal.conocimientoVivienda);
			ps.setString(param++, referenciaPersonal.impedimentoPago);
			ps.setString(param++, referenciaPersonal.prestamoDinero);
			ps.setString(param++, referenciaPersonal.recomendacionCredito);
			ps.setString(param++, referenciaPersonal.prestriaDinero);
			ps.setString(param++, referenciaPersonal.disponibilidadRespaldo);
			ps.setString(param++, referenciaPersonal.descripcionCliente);
			ps.setInt(param++, referenciaPersonal.calificacionCliente);
			ps.setDate(param++, referenciaPersonal.fechaRealizacionConsulta);
			ps.setTimestamp(param++, referenciaPersonal.fechaCaptura);
			ps.setString(param++, referenciaPersonal.direccion);
	
			Logger.debug("Ejecutando = "+query);
			Logger.debug("Referencia = "+referenciaPersonal.toString());
			res = ps.executeUpdate();
			Logger.debug("Resultado = "+res);
		} catch (SQLException sqle) {
			Logger.debug("SQLException en addReferenciaPersonal : " + sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		} catch (Exception e) {
			Logger.debug("Excepcion en addReferenciaPersonal : " + e.getMessage());
			e.printStackTrace();
			throw new ClientesException(e.getMessage());
		} finally {
			try {
				if (cn != null)
					cn.close();
			} catch (SQLException sqle) {
				throw new ClientesDBException(sqle.getMessage());
			}
		}

		return res;

	}



	public int updateReferenciaPersonal(int idCliente, int idSolicitud,int idReferencia, ReferenciaPersonalVO referenciaPersonal) throws ClientesException {
		int res = 0;
		String query =	"UPDATE D_REFERENCIA_PERSONAL  SET RC_NOMBRE=?,RC_TELEFONO=?,RC_HORARIOLLAMADA=?,RC_RELACION=?,RC_TIEMPOCONOCIMIENTO=?," +
						"RC_CONOCIMIENTOOCUPACION=?,RC_VISITANEGOCIO=?,RC_DONDEVENDE=?,RC_TIEMPOOPERACION=?,RC_STATUSVENTAS=?," +
						"RC_CONQUIENVIVE=?,RC_CONOCIMIENTOVIVIENDA=?,RC_IMPEDIMENTOPAGO=?,RC_PRESTAMODINERO=?,RC_RECOMENDACIONCREDITO=?," +
						"RC_PRESTRIADINERO=?,RC_DISPONIBILIDADRESPALDO=?,RC_DESCRIPCIONCLIENTE=?,RC_CALIFICACIONCLIENTE=?," +
						"RC_FECHAREALIZACIONCONSULTA=?,RC_DIRECCION=? WHERE RC_IDCLIENTE=? AND RC_IDSOLICITUD=? AND RC_IDREFERENCIA=?";
		int param = 1;
		Connection cn = null;
		try {
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setString(param++, referenciaPersonal.nombre);
			ps.setString(param++, referenciaPersonal.telefono);
			ps.setString(param++, referenciaPersonal.horarioLlamada);
			ps.setString(param++, referenciaPersonal.relacion);
			ps.setString(param++, referenciaPersonal.tiempoConocimiento);
			ps.setString(param++, referenciaPersonal.conocimientoOcupacion);
			ps.setString(param++, referenciaPersonal.visitaNegocio);
			ps.setString(param++, referenciaPersonal.dondeVende);
			ps.setString(param++, referenciaPersonal.tiempoOperacion);
			ps.setString(param++, referenciaPersonal.statusVentas);
			ps.setString(param++, referenciaPersonal.conQuienVive);
			ps.setString(param++, referenciaPersonal.conocimientoVivienda);
			ps.setString(param++, referenciaPersonal.impedimentoPago);
			ps.setString(param++, referenciaPersonal.prestamoDinero);
			ps.setString(param++, referenciaPersonal.recomendacionCredito);
			ps.setString(param++, referenciaPersonal.prestriaDinero);
			ps.setString(param++, referenciaPersonal.disponibilidadRespaldo);
			ps.setString(param++, referenciaPersonal.descripcionCliente);
			ps.setInt(param++, referenciaPersonal.calificacionCliente);
			ps.setDate(param++, referenciaPersonal.fechaRealizacionConsulta);
			ps.setString(param++, referenciaPersonal.direccion);
			ps.setInt(param++, idCliente);
			ps.setInt(param++, idSolicitud);
			ps.setInt(param++, idReferencia);
			Logger.debug("Ejecutando = "+query);
			Logger.debug("Referencia = "+referenciaPersonal.toString());
			res = ps.executeUpdate();
			Logger.debug("Resultado = "+res);
		} catch (SQLException sqle) {
			Logger.debug("SQLException en updateReferenciaPersonal : " + sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		} catch (Exception e) {
			Logger.debug("Excepcion en updateReferenciaPersonal : " + e.getMessage());
			e.printStackTrace();
			throw new ClientesException(e.getMessage());
		} finally {
			try {
				if (cn != null)
					cn.close();
			} catch (SQLException sqle) {
				throw new ClientesDBException(sqle.getMessage());
			}
		}

		return res;

	}


	public int getNext(int idCliente, int idSolicitud ) throws ClientesException{
		return getNext(null, idCliente, idSolicitud );
	}

	public int getNext(Connection conn, int idCliente, int idSolicitud ) throws ClientesException{

		String query = "SELECT COALESCE(MAX(RC_IDREFERENCIA),0)+1 AS NEXT FROM D_REFERENCIA_PERSONAL WHERE RC_IDCLIENTE = ? AND RC_IDSOLICITUD = ?";
		Connection cn = null;
		int next = 1;
		try{
			cn = null;
			PreparedStatement ps = null;
			if( conn==null ){
				cn = getConnection();
				ps = cn.prepareStatement(query); 
			}
			else{
				ps = conn.prepareStatement(query);
			}
			
			ps.setInt(1, idCliente);
			ps.setInt(2, idSolicitud);
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
