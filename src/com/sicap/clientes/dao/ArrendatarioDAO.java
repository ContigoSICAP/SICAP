package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.ArrendatarioVO;


public class ArrendatarioDAO extends DAOMaster{


	public ArrendatarioVO getArrendatario(int idCliente, int idSolicitud, int tipoArrendatario ) throws ClientesException{
		ArrendatarioVO  arrendatario = null;
		Connection cn = null;
		String query =	"SELECT AR_ESTATUS, AR_NOMBRE, AR_TELEFONO, AR_HORARIO_LLAMADA , AR_TIEMPO_CONOCIMIENTO , "+
						"AR_CONOCIMIENTO_OCUPACION, AR_CONOCIMIENTO_VIVIENDA, AR_RELACION, AR_INMUEBLE_RENTA, "+
						"AR_TIEMPO_RENTA, AR_EXISTENCIA_CONTRATO, AR_DURACION_CONTRATO, AR_PUNTUALIDAD_PAGO, AR_CONDUCTA_ATRASO, "+
						"AR_PLAN_RENTA_FUTURA, AR_RECOMENDACION_CREDITO, AR_DESCRIPCION_CLIENTE, AR_DISPONIBILIDAD_RESPALDO, "+
						"AR_CALIFICACION_CLIENTE, AR_FECHA_CONSULTA, AR_FECHA_CAPTURA, AR_DIRECCION FROM D_ARRENDATARIOS "+
						"WHERE AR_NUMCLIENTE = ? AND AR_NUMSOLICITUD = ? AND AR_TIPO = ?";
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(1, idCliente);
			ps.setInt(2, idSolicitud);
			ps.setInt(3, tipoArrendatario);
			//Logger.debug("Ejecutando = "+query);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				arrendatario=  new ArrendatarioVO();
				arrendatario.idCliente  = idCliente;
				arrendatario.idSolicitud  = idSolicitud;
				arrendatario.idTipo = tipoArrendatario;
				arrendatario.estatus = rs.getInt("AR_ESTATUS");
				arrendatario.nombre = rs.getString("AR_NOMBRE");
				arrendatario.telefono = rs.getString("AR_TELEFONO");
				arrendatario.horarioLlamada = rs.getString("AR_HORARIO_LLAMADA");
				arrendatario.tiempoConocimiento = rs.getString("AR_TIEMPO_CONOCIMIENTO");
				arrendatario.conocimientoOcupacion = rs.getString("AR_CONOCIMIENTO_OCUPACION");
				arrendatario.conocimientoVivienda = rs.getString("AR_CONOCIMIENTO_VIVIENDA");
				arrendatario.relacion = rs.getString("AR_RELACION");
				arrendatario.inmuebleRenta = rs.getString("AR_INMUEBLE_RENTA");
				arrendatario.tiempoRenta = rs.getString("AR_TIEMPO_RENTA");
				arrendatario.existenciaContrato = rs.getString("AR_EXISTENCIA_CONTRATO");
				arrendatario.duracionContrato = rs.getString("AR_DURACION_CONTRATO");
				arrendatario.puntualidadPago = rs.getString("AR_PUNTUALIDAD_PAGO");
				arrendatario.conductaAtraso = rs.getString("AR_CONDUCTA_ATRASO");
				arrendatario.planRentaFutura = rs.getString("AR_PLAN_RENTA_FUTURA");
				arrendatario.reconmendacionCredito = rs.getString("AR_RECOMENDACION_CREDITO");
				arrendatario.descripcionCliente = rs.getString("AR_DESCRIPCION_CLIENTE");
				arrendatario.disponibilidadRespaldo = rs.getString("AR_DISPONIBILIDAD_RESPALDO");
				arrendatario.calificacionCliente = rs.getInt("AR_CALIFICACION_CLIENTE");
				arrendatario.fechaRealizacionConsulta = rs.getDate("AR_FECHA_CONSULTA");
				arrendatario.fechaCaptura = rs.getTimestamp("AR_FECHA_CAPTURA");
				arrendatario.direccion = rs.getString("AR_DIRECCION");
			}
		}catch(SQLException sqle) {
			Logger.debug("SQLException en getArrendatario : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getArrendatario : "+e.getMessage());
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
		return arrendatario;
	}


	public int adddArrendatario (int idCliente, int idSolicitud, ArrendatarioVO arrendatario) throws ClientesException{
		return adddArrendatario(null, idCliente, idSolicitud, arrendatario);
	}

	public int adddArrendatario (Connection conn, int idCliente, int idSolicitud, ArrendatarioVO arrendatario) throws ClientesException{

		String query =	"INSERT INTO D_ARRENDATARIOS (AR_NUMCLIENTE, AR_NUMSOLICITUD, AR_TIPO, AR_ESTATUS, AR_NOMBRE, "+
						"AR_TELEFONO, AR_HORARIO_LLAMADA , AR_TIEMPO_CONOCIMIENTO , AR_CONOCIMIENTO_OCUPACION, "+
						"AR_CONOCIMIENTO_VIVIENDA, AR_RELACION, AR_INMUEBLE_RENTA, AR_TIEMPO_RENTA, AR_EXISTENCIA_CONTRATO, "+
						"AR_DURACION_CONTRATO, AR_PUNTUALIDAD_PAGO, AR_CONDUCTA_ATRASO, AR_PLAN_RENTA_FUTURA, "+
						"AR_RECOMENDACION_CREDITO, AR_DESCRIPCION_CLIENTE, AR_DISPONIBILIDAD_RESPALDO, AR_CALIFICACION_CLIENTE, "+
						"AR_FECHA_CONSULTA, AR_FECHA_CAPTURA, AR_DIRECCION) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		Connection cn = null;
		PreparedStatement ps =null;
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
			ps.setInt(param++, arrendatario.idTipo);
			ps.setInt(param++, arrendatario.estatus);
			ps.setString(param++, arrendatario.nombre);
			ps.setString(param++, arrendatario.telefono);
			ps.setString(param++, arrendatario.horarioLlamada);
			ps.setString(param++, arrendatario.tiempoConocimiento);
			ps.setString(param++, arrendatario.conocimientoOcupacion);
			ps.setString(param++, arrendatario.conocimientoVivienda);
			ps.setString(param++, arrendatario.relacion);
			ps.setString(param++, arrendatario.inmuebleRenta);
			ps.setString(param++, arrendatario.tiempoRenta);
			ps.setString(param++, arrendatario.existenciaContrato);
			ps.setString(param++, arrendatario.duracionContrato);
			ps.setString(param++, arrendatario.puntualidadPago);
			ps.setString(param++, arrendatario.conductaAtraso);
			ps.setString(param++, arrendatario.planRentaFutura);
			ps.setString(param++, arrendatario.reconmendacionCredito);
			ps.setString(param++, arrendatario.descripcionCliente);
			ps.setString(param++, arrendatario.disponibilidadRespaldo);
			ps.setInt(param++, arrendatario.calificacionCliente);
			ps.setDate(param++, arrendatario.fechaRealizacionConsulta);			
			ps.setTimestamp(param++, arrendatario.fechaCaptura);
			ps.setString(param++, arrendatario.direccion);
			Logger.debug("Ejecutando = "+query);
			Logger.debug("Arrendatario= "+arrendatario.toString());
			res = ps.executeUpdate();
			Logger.debug("Resultado = "+res);
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en adddArrendatario : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en adddArrendatario : "+e.getMessage());
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



	public int updateArrendatario (int idCliente, int idSolicitud, ArrendatarioVO arrendatario) throws ClientesException{

		String query =	"UPDATE D_ARRENDATARIOS SET AR_ESTATUS = ?, AR_NOMBRE = ?, AR_TELEFONO = ?, AR_HORARIO_LLAMADA = ?, "+
						"AR_TIEMPO_CONOCIMIENTO = ?, AR_CONOCIMIENTO_OCUPACION = ?, AR_CONOCIMIENTO_VIVIENDA = ?, "+
						"AR_RELACION = ?, AR_INMUEBLE_RENTA = ?, AR_TIEMPO_RENTA = ?, AR_EXISTENCIA_CONTRATO = ?, "+
						"AR_DURACION_CONTRATO = ?, AR_PUNTUALIDAD_PAGO = ?, AR_CONDUCTA_ATRASO = ?, AR_PLAN_RENTA_FUTURA = ?, "+
						"AR_RECOMENDACION_CREDITO = ?, AR_DESCRIPCION_CLIENTE = ?, AR_DISPONIBILIDAD_RESPALDO = ?, "+
						"AR_CALIFICACION_CLIENTE = ?, AR_FECHA_CONSULTA = ?, AR_DIRECCION = ? WHERE AR_NUMCLIENTE = ? AND AR_NUMSOLICITUD = ? AND AR_TIPO = ?";
		
		Connection cn = null;
		int  res = 0;
		int  param = 1;
		try{
			PreparedStatement ps =null;
			cn = getConnection();
			ps = cn.prepareStatement(query); 
			
			ps.setInt(param++, arrendatario.estatus);
			ps.setString(param++, arrendatario.nombre);
			ps.setString(param++, arrendatario.telefono);
			ps.setString(param++, arrendatario.horarioLlamada);
			ps.setString(param++, arrendatario.tiempoConocimiento);
			ps.setString(param++, arrendatario.conocimientoOcupacion);
			ps.setString(param++, arrendatario.conocimientoVivienda);
			ps.setString(param++, arrendatario.relacion);
			ps.setString(param++, arrendatario.inmuebleRenta);
			ps.setString(param++, arrendatario.tiempoRenta);
			ps.setString(param++, arrendatario.existenciaContrato);
			ps.setString(param++, arrendatario.duracionContrato);
			ps.setString(param++, arrendatario.puntualidadPago);
			ps.setString(param++, arrendatario.conductaAtraso);
			ps.setString(param++, arrendatario.planRentaFutura);
			ps.setString(param++, arrendatario.reconmendacionCredito);
			ps.setString(param++, arrendatario.descripcionCliente);
			ps.setString(param++, arrendatario.disponibilidadRespaldo);
			ps.setInt(param++, arrendatario.calificacionCliente);
			ps.setDate(param++, arrendatario.fechaRealizacionConsulta);			
			ps.setString(param++, arrendatario.direccion);
			
			ps.setInt(param++, idCliente);
			ps.setInt(param++, idSolicitud);
			ps.setInt(param++, arrendatario.idTipo);
			Logger.debug("Ejecutando = "+query);
			Logger.debug("Arrendatario = "+arrendatario.toString());
			res = ps.executeUpdate();
			Logger.debug("Resultado = "+res);
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en updateArrendatario : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en updateArrendatario : "+e.getMessage());
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




