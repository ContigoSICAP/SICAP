package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.ReferenciaComercialVO;


public class ReferenciaComercialDAO extends DAOMaster {

	
	public ReferenciaComercialVO[] getReferenciasComerciales(int idCliente, int idSolicitud) throws ClientesException{

		String query =	"SELECT RC_IDCLIENTE, RC_IDSOLICITUD, RC_IDREFERENCIA, RC_NOMBRE, RC_TELEFONO, RC_HORARIOLLAMADA, "+
						"RC_TIEMPOCONOCIMIENTO, RC_CONOCIMIENTOOCUPACION, RC_RELACION, RC_TIPOPRODUCTO, RC_FRECUENCIASURTIDO, "+
						"RC_CANTIDADSURTIDO, RC_STATUSVENTAS, RC_TIPOPAGO, RC_DIASPAGO, RC_CALIDADPAGO, RC_RAZONATRASO, "+
						"RC_CANTIDADPERSONAL, RC_RECOMENDACIONCREDITO, RC_DESCRIPCIONCLIENTE, RC_DISPONIBILIDADRESPALDO, "+
						"RC_CALIFICACIONCLIENTE, RC_FECHAREALIZACIONCONSULTA, RC_FECHACAPTURA FROM D_REFERENCIA_COMERCIAL "+
						"WHERE RC_IDCLIENTE = ? AND RC_IDSOLICITUD = ?";
		ArrayList<ReferenciaComercialVO> array = new ArrayList<ReferenciaComercialVO>();
        ReferenciaComercialVO temporal = null;
        ReferenciaComercialVO elementos[] = null;
		Connection cn = null;
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(1, idCliente);
			ps.setInt(2, idSolicitud);
			//Logger.debug("Ejecutando : "+query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				temporal = new ReferenciaComercialVO();
				temporal.idCliente=rs.getInt("RC_IDCLIENTE");
				temporal.idSolicitud=rs.getInt("RC_IDSOLICITUD");
				temporal.idReferencia=rs.getInt("RC_IDREFERENCIA");
				temporal.nombre=rs.getString("RC_NOMBRE");
				temporal.telefono=rs.getString("RC_TELEFONO");
				temporal.horarioLlamada=rs.getString("RC_HORARIOLLAMADA");
				temporal.tiempoConocimiento=rs.getString("RC_TIEMPOCONOCIMIENTO");
				temporal.conocimientoOcupacion=rs.getString("RC_CONOCIMIENTOOCUPACION");
				temporal.relacion=rs.getString("RC_RELACION");
				temporal.tipoProducto=rs.getString("RC_TIPOPRODUCTO");
				temporal.frecuenciaSurtido=rs.getString("RC_FRECUENCIASURTIDO");
				temporal.cantidadSurtido=rs.getString("RC_CANTIDADSURTIDO");
				temporal.statusVentas=rs.getString("RC_STATUSVENTAS");
				temporal.tipoPago=rs.getString("RC_TIPOPAGO");
				temporal.diasPago=rs.getString("RC_DIASPAGO");
				temporal.calidadPago=rs.getString("RC_CALIDADPAGO");
				temporal.razonAtraso=rs.getString("RC_RAZONATRASO");
				temporal.cantidadPersonal=rs.getString("RC_CANTIDADPERSONAL");
				temporal.recomendacionCredito=rs.getString("RC_RECOMENDACIONCREDITO");
				temporal.descripcionCliente=rs.getString("RC_DESCRIPCIONCLIENTE");
				temporal.disponibilidadRespaldo=rs.getString("RC_DISPONIBILIDADRESPALDO");
				temporal.calificacionCliente=rs.getInt("RC_CALIFICACIONCLIENTE");
				temporal.fechaRealizacionConsulta=rs.getDate("RC_FECHAREALIZACIONCONSULTA");
				temporal.fechaCaptura=rs.getTimestamp("RC_FECHACAPTURA");
				Logger.debug("Referencia encontrada : "+temporal.toString());
				array.add(temporal);
			}
		}catch(SQLException sqle) {
			Logger.debug("SQLException en getReferenciasComerciales : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getReferenciasComerciales : "+e.getMessage());
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
		elementos = new ReferenciaComercialVO[array.size()];
	    for(int i=0;i<elementos.length; i++) elementos[i] = (ReferenciaComercialVO)array.get(i);
	    return elementos;

	}
	
	public ReferenciaComercialVO getReferenciaComercial(int idCliente, int idSolicitud, int idReferencia) throws ClientesException {
		ReferenciaComercialVO referenciaComercial = null;
		Connection cn = null;
		try {
			cn = getConnection();
			String query = "SELECT RC_IDCLIENTE,RC_IDSOLICITUD,RC_NOMBRE,RC_TELEFONO," +
							"RC_HORARIOLLAMADA,RC_RELACION,RC_TIEMPOCONOCIMIENTO,RC_CONOCIMIENTOOCUPACION,RC_TIPOPRODUCTO," +
							"RC_FRECUENCIASURTIDO,RC_CANTIDADSURTIDO,RC_STATUSVENTAS,RC_TIPOPAGO,RC_CALIDADPAGO,RC_RAZONATRASO," +
							"RC_CANTIDADPERSONAL,RC_RECOMENDACIONCREDITO,RC_DESCRIPCIONCLIENTE,RC_DISPONIBILIDADRESPALDO," +
							"RC_CALIFICACIONCLIENTE,RC_FECHAREALIZACIONCONSULTA,RC_FECHACAPTURA FROM D_REFERENCIA_COMERCIAL" +
							" WHERE RC_IDCLIENTE=? AND RC_IDSOLICITUD=? AND RC_IDREFERENCIA=? ";
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(1, idCliente);
			ps.setInt(2, idSolicitud);
			ps.setInt(2, idReferencia);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				referenciaComercial.idCliente=rs.getInt("RC_idCliente");
				referenciaComercial.idSolicitud=rs.getInt("RC_idSolicitud");
				referenciaComercial.nombre=rs.getString("RC_nombre");
				referenciaComercial.telefono=rs.getString("RC_telefono");
				referenciaComercial.horarioLlamada=rs.getString("RC_horarioLlamada");
				referenciaComercial.relacion=rs.getString("RC_relacion");
				referenciaComercial.tiempoConocimiento=rs.getString("RC_tiempoConocimiento");
				referenciaComercial.conocimientoOcupacion=rs.getString("RC_conocimientoOcupacion");
				referenciaComercial.tipoProducto=rs.getString("RC_tipoProducto");
				referenciaComercial.frecuenciaSurtido=rs.getString("RC_frecuenciaSurtido");
				referenciaComercial.cantidadSurtido=rs.getString("RC_cantidadSurtido");
				referenciaComercial.statusVentas=rs.getString("RC_statusVentas");
				referenciaComercial.tipoPago=rs.getString("RC_tipoPago");
				referenciaComercial.calidadPago=rs.getString("RC_calidadPago");
				referenciaComercial.razonAtraso=rs.getString("RC_razonAtraso");
				referenciaComercial.cantidadPersonal=rs.getString("RC_cantidadPersonal");
				referenciaComercial.recomendacionCredito=rs.getString("RC_recomendacionCredito");
				referenciaComercial.descripcionCliente=rs.getString("RC_descripcionCliente");
				referenciaComercial.disponibilidadRespaldo=rs.getString("RC_disponibilidadRespaldo");
				referenciaComercial.calificacionCliente=rs.getInt("RC_calificacionCliente");
				referenciaComercial.fechaRealizacionConsulta=rs.getDate("RC_fechaRealizacionConsulta");
				referenciaComercial.fechaCaptura=rs.getTimestamp("RC_fechaCaptura");
			}
		} catch (SQLException sqle) {
			Logger.debug("SQLException en getReferenciaComercial : "
					+ sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		} catch (Exception e) {
			Logger.debug("Excepcion en getReferenciaComercial : " + e.getMessage());
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
		return referenciaComercial;
	}


//INSERT INTO D_REFERENCIA_COMERCIAL (RC_IDCLIENTE, RC_IDSOLICITUD, RC_IDREFERENCIA, RC_NOMBRE, RC_TELEFONO, RC_HORARIOLLAMADA, RC_TIEMPOCONOCIMIENTO, RC_CONOCIMIENTOOCUPACION, RC_RELACION, RC_TIPOPRODUCTO, RC_FRECUENCIASURTIDO, RC_CANTIDADSURTIDO, RC_STATUSVENTAS, RC_TIPOPAGO, RC_DIASPAGO, RC_CALIDADPAGO, RC_RAZONATRASO, RC_CANTIDADPERSONAL, RC_RECOMENDACIONCREDITO, RC_DESCRIPCIONCLIENTE, RC_DISPONIBILIDADRESPALDO, RC_CALIFICACIONCLIENTE, RC_FECHAREALIZACIONCONSULTA, RC_FECHACAPTURA) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)

	public int addReferenciaComercial(int idCliente, int idSolicitud, ReferenciaComercialVO referenciaComercial) throws ClientesException {
		return addReferenciaComercial(null, idCliente, idSolicitud, referenciaComercial);
	}
	
	public int addReferenciaComercial(Connection conn, int idCliente, int idSolicitud, ReferenciaComercialVO referenciaComercial) throws ClientesException {
		int res = 0;
		String query =	"INSERT INTO D_REFERENCIA_COMERCIAL (RC_IDCLIENTE, RC_IDSOLICITUD, RC_IDREFERENCIA, RC_NOMBRE, "+
						"RC_TELEFONO, RC_HORARIOLLAMADA, RC_TIEMPOCONOCIMIENTO, RC_CONOCIMIENTOOCUPACION, RC_RELACION, "+
						"RC_TIPOPRODUCTO, RC_FRECUENCIASURTIDO, RC_CANTIDADSURTIDO, RC_STATUSVENTAS, RC_TIPOPAGO, "+
						"RC_DIASPAGO, RC_CALIDADPAGO, RC_RAZONATRASO, RC_CANTIDADPERSONAL, RC_RECOMENDACIONCREDITO, "+
						"RC_DESCRIPCIONCLIENTE, RC_DISPONIBILIDADRESPALDO, RC_CALIFICACIONCLIENTE, RC_FECHAREALIZACIONCONSULTA, "+
						"RC_FECHACAPTURA) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		int param = 1;
		Connection cn = null;
		int next = 0;
		try {
			PreparedStatement ps = null;
			if( conn==null ){
				next = getNext(idCliente, idSolicitud);
				cn = getConnection();
				ps = cn.prepareStatement(query); 
			}
			else{
				next = getNext(conn, idCliente, idSolicitud);
				ps = conn.prepareStatement(query);
			}
			ps.setInt(param++, idCliente);
			ps.setInt(param++, idSolicitud);
			ps.setInt(param++, next);
			ps.setString(param++,referenciaComercial.nombre);
			ps.setString(param++,referenciaComercial.telefono);
			ps.setString(param++,referenciaComercial.horarioLlamada);
			ps.setString(param++,referenciaComercial.tiempoConocimiento);
			ps.setString(param++,referenciaComercial.conocimientoOcupacion);
			ps.setString(param++,referenciaComercial.relacion);
			ps.setString(param++,referenciaComercial.tipoProducto);
			ps.setString(param++,referenciaComercial.frecuenciaSurtido);
			ps.setString(param++,referenciaComercial.cantidadSurtido);
			ps.setString(param++,referenciaComercial.statusVentas);
			ps.setString(param++,referenciaComercial.tipoPago);
			ps.setString(param++,referenciaComercial.diasPago);
			ps.setString(param++,referenciaComercial.calidadPago);
			ps.setString(param++,referenciaComercial.razonAtraso);
			ps.setString(param++,referenciaComercial.cantidadPersonal);
			ps.setString(param++,referenciaComercial.recomendacionCredito);
			ps.setString(param++,referenciaComercial.descripcionCliente);
			ps.setString(param++,referenciaComercial.disponibilidadRespaldo);
			ps.setInt(param++,referenciaComercial.calificacionCliente);
			ps.setDate(param++,referenciaComercial.fechaRealizacionConsulta);
			ps.setTimestamp(param++,referenciaComercial.fechaCaptura);
			Logger.debug("Ejecutando = "+query);
			Logger.debug("Referencia = "+referenciaComercial.toString());
			res = ps.executeUpdate();
			Logger.debug("Resultado = "+res);
		} catch (SQLException sqle) {
			Logger.debug("SQLException en addReferenciaComercial : " + sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		} catch (Exception e) {
			Logger.debug("Excepcion en addReferenciaComercial : " + e.getMessage());
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
		return next;

	}


	//UPDATE D_REFERENCIA_COMERCIAL SET RC_NOMBRE = ?, RC_TELEFONO = ?, RC_HORARIOLLAMADA = ?, RC_TIEMPOCONOCIMIENTO = ?, RC_CONOCIMIENTOOCUPACION = ?, RC_RELACION = ?, RC_TIPOPRODUCTO = ?, RC_FRECUENCIASURTIDO = ?, RC_CANTIDADSURTIDO = ?, RC_STATUSVENTAS = ?, RC_TIPOPAGO = ?, RC_DIASPAGO = ?, RC_CALIDADPAGO = ?, RC_RAZONATRASO = ?, RC_CANTIDADPERSONAL = ?, RC_RECOMENDACIONCREDITO = ?, RC_DESCRIPCIONCLIENTE = ?, RC_DISPONIBILIDADRESPALDO = ?, RC_CALIFICACIONCLIENTE = ?, RC_FECHAREALIZACIONCONSULTA = ?, RC_FECHACAPTURA FROM D_REFERENCIA_COMERCIAL WHERE RC_IDCLIENTE = ? AND RC_IDSOLICITUD = ? AND RC_IDREFERENCIA = ?

	public int updateReferenciaComercial(int idCliente, int idSolicitud, int idReferencia, ReferenciaComercialVO referenciaComercial) throws ClientesException {
		int res = 0;
		String query =	"UPDATE D_REFERENCIA_COMERCIAL SET RC_NOMBRE = ?, RC_TELEFONO = ?, RC_HORARIOLLAMADA = ?, RC_TIEMPOCONOCIMIENTO = ?, "+
						"RC_CONOCIMIENTOOCUPACION = ?, RC_RELACION = ?, RC_TIPOPRODUCTO = ?, RC_FRECUENCIASURTIDO = ?, RC_CANTIDADSURTIDO = ?, "+
						"RC_STATUSVENTAS = ?, RC_TIPOPAGO = ?, RC_DIASPAGO = ?, RC_CALIDADPAGO = ?, RC_RAZONATRASO = ?, RC_CANTIDADPERSONAL = ?, "+
						"RC_RECOMENDACIONCREDITO = ?, RC_DESCRIPCIONCLIENTE = ?, RC_DISPONIBILIDADRESPALDO = ?, RC_CALIFICACIONCLIENTE = ?, "+
						"RC_FECHAREALIZACIONCONSULTA = ? WHERE RC_IDCLIENTE = ? AND RC_IDSOLICITUD = ? AND RC_IDREFERENCIA = ?";
		int param = 1;
		Connection cn = null;
		try {
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setString(param++,referenciaComercial.nombre);
			ps.setString(param++,referenciaComercial.telefono);
			ps.setString(param++,referenciaComercial.horarioLlamada);
			ps.setString(param++,referenciaComercial.tiempoConocimiento);
			ps.setString(param++,referenciaComercial.conocimientoOcupacion);
			ps.setString(param++,referenciaComercial.relacion);
			ps.setString(param++,referenciaComercial.tipoProducto);
			ps.setString(param++,referenciaComercial.frecuenciaSurtido);
			ps.setString(param++,referenciaComercial.cantidadSurtido);
			ps.setString(param++,referenciaComercial.statusVentas);
			ps.setString(param++,referenciaComercial.tipoPago);
			ps.setString(param++,referenciaComercial.diasPago);
			ps.setString(param++,referenciaComercial.calidadPago);
			ps.setString(param++,referenciaComercial.razonAtraso);
			ps.setString(param++,referenciaComercial.cantidadPersonal);
			ps.setString(param++,referenciaComercial.recomendacionCredito);
			ps.setString(param++,referenciaComercial.descripcionCliente);
			ps.setString(param++,referenciaComercial.disponibilidadRespaldo);
			ps.setInt(param++,referenciaComercial.calificacionCliente);
			ps.setDate(param++,referenciaComercial.fechaRealizacionConsulta);
			ps.setInt(param++, idCliente);
			ps.setInt(param++, idSolicitud);
			ps.setInt(param++, idReferencia);
			Logger.debug("Ejecutando = "+query);
			Logger.debug("Referencia = "+referenciaComercial.toString());
			res = ps.executeUpdate();
			Logger.debug("Resultado = "+res);
		} catch (SQLException sqle) {
			Logger.debug("SQLException en updateReferenciaComercial : " + sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		} catch (Exception e) {
			Logger.debug("Excepcion en updateReferenciaComercial : " + e.getMessage());
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


	public int getNext(int idCliente, int idSolicitud) throws ClientesException{
		return getNext(null, idCliente, idSolicitud);
	}

	public int getNext(Connection conn, int idCliente, int idSolicitud) throws ClientesException{

		String query = "SELECT COALESCE(MAX(RC_IDREFERENCIA),0)+1 AS NEXT FROM D_REFERENCIA_COMERCIAL WHERE RC_IDCLIENTE = ? AND RC_IDSOLICITUD = ?";
		Connection cn = null;
		int next = 1;
		try{
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
