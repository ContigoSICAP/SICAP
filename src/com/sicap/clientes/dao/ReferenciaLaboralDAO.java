package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.ReferenciaLaboralVO;


public class ReferenciaLaboralDAO extends DAOMaster{

	//SELECT RL_NUMCLIENTE, RL_NUMSOLICITUD, RL_ESTATUS, RL_FECHA_ELABORACION, RL_NOMBRE_EMPRESA, RL_NOMBRE_INFORMANTE, RL_CARGO, RL_GIRO_EMPRESA, RL_RFC, RL_TELEFONO1, RL_TELEFONO2, RL_INICIO_OPERACIONES, RL_NUMERO_EMPLEADOS, RL_PRINCIPALES_PRODS_SERV, RL_FECHA_ALTA_HACIENDA, RL_PUESTO, RL_DESCRIPCION_PUESTO, RL_INICIO_EMPLEO, RL_RECOMENDACION_CREDITO, RL_PERSPECTIVAS_MESES, RL_PERSPECTIVAS_ANIOS, RL_JORNADA, RL_HORARIO, RL_DIAS_DESCANSO, RL_SALARIO_FIJO, RL_INCENTIVOS, RL_SEGURO_MEDICO, RL_TIPO_SEGURO, RL_FECHA_CAPTURA FROM D_REFERENCIAS_LABORALES
	public ReferenciaLaboralVO getReferencia(int idCliente, int idSolicitud) throws ClientesException{
		ReferenciaLaboralVO  referencia = null;
		Connection cn = null;
		String query =	"SELECT RL_NUMCLIENTE, RL_NUMSOLICITUD, RL_ESTATUS, RL_FECHA_ELABORACION, RL_NOMBRE_EMPRESA, "+
						"RL_NOMBRE_INFORMANTE, RL_CARGO, RL_GIRO_EMPRESA, RL_RFC, RL_DIRECCION_VERDADERA, RL_TELEFONO1, RL_TELEFONO2, "+
						"RL_INICIO_OPERACIONES, RL_NUMERO_EMPLEADOS, RL_PRINCIPALES_PRODS_SERV, RL_FECHA_ALTA_HACIENDA, "+
						"RL_PUESTO, RL_DESCRIPCION_PUESTO, RL_INICIO_EMPLEO, RL_RECOMENDACION_CREDITO, "+
						"RL_PERSPECTIVAS_MESES, RL_PERSPECTIVAS_ANIOS, RL_JORNADA, RL_HORARIO, RL_DIAS_DESCANSO, "+
						"RL_SALARIO_FIJO, RL_INCENTIVOS, RL_SEGURO_MEDICO, RL_TIPO_SEGURO, RL_FECHA_CAPTURA FROM "+
						"D_REFERENCIAS_LABORALES WHERE RL_NUMCLIENTE = ? AND RL_NUMSOLICITUD = ?";
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(1, idCliente);
			ps.setInt(2, idSolicitud);
			//Logger.debug("Ejecutando = "+query);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				referencia =  new ReferenciaLaboralVO();
				referencia.idCliente = idCliente;
				referencia.idSolicitud = idSolicitud;
				referencia.estatus = rs.getInt("RL_ESTATUS");
				referencia.fechaElaboracion = rs.getDate("RL_FECHA_ELABORACION");
				referencia.nombreEmpresa = rs.getString("RL_NOMBRE_EMPRESA");
				referencia.nombreInformante = rs.getString("RL_NOMBRE_INFORMANTE");
				referencia.cargo = rs.getString("RL_CARGO");
				referencia.giroEmpresa = rs.getString("RL_GIRO_EMPRESA");
				referencia.rfc = rs.getString("RL_RFC");
				referencia.direccionVerdadera = rs.getInt("RL_DIRECCION_VERDADERA");
				referencia.telefono1 = rs.getString("RL_TELEFONO1");
				referencia.telefono2 = rs.getString("RL_TELEFONO2");
				referencia.inicioOperaciones = rs.getString("RL_INICIO_OPERACIONES");
				referencia.numeroEmpleados = rs.getInt("RL_NUMERO_EMPLEADOS");
				referencia.principalesProdsServs = rs.getString("RL_PRINCIPALES_PRODS_SERV");
				referencia.fechaAltaHacienda = rs.getDate("RL_FECHA_ALTA_HACIENDA");
				referencia.puesto = rs.getString("RL_PUESTO");
				referencia.descPuesto = rs.getString("RL_DESCRIPCION_PUESTO");
				referencia.inicioEmpleo = rs.getString("RL_INICIO_EMPLEO");
				referencia.reconmendacionCredito = rs.getInt("RL_RECOMENDACION_CREDITO");
				referencia.perspectivasMeses = rs.getInt("RL_PERSPECTIVAS_MESES");
				referencia.perspectivasAnios = rs.getInt("RL_PERSPECTIVAS_ANIOS");
				referencia.jornada = rs.getInt("RL_JORNADA");
				referencia.horario = rs.getString("RL_HORARIO");
				referencia.diasDescanso = rs.getString("RL_DIAS_DESCANSO");
				referencia.salarioFijo = rs.getDouble("RL_SALARIO_FIJO");
				referencia.incentivos = rs.getDouble("RL_INCENTIVOS");
				referencia.seguroMedico = rs.getInt("RL_SEGURO_MEDICO");
				referencia.tipoSeguro = rs.getString("RL_TIPO_SEGURO");
				referencia.fechaCaptura = rs.getTimestamp("RL_FECHA_CAPTURA");
			}
		}catch(SQLException sqle) {
			Logger.debug("SQLException en getReferencia : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getReferencia : "+e.getMessage());
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
		return referencia;
	}

	public int addReferencia(int idCliente, int idSolicitud, ReferenciaLaboralVO referencia) throws ClientesException{
		return addReferencia(null, idCliente, idSolicitud, referencia);
	}

//INSERT INTO D_REFERENCIAS_LABORALES (RL_NUMCLIENTE, RL_NUMSOLICITUD, RL_ESTATUS, RL_FECHA_ELABORACION, RL_NOMBRE_EMPRESA, RL_NOMBRE_INFORMANTE, RL_CARGO, RL_GIRO_EMPRESA, RL_RFC, RL_TELEFONO1, RL_TELEFONO2, RL_INICIO_OPERACIONES, RL_NUMERO_EMPLEADOS, RL_PRINCIPALES_PRODS_SERV, RL_FECHA_ALTA_HACIENDA, RL_PUESTO, RL_DESCRIPCION_PUESTO, RL_INICIO_EMPLEO, RL_RECOMENDACION_CREDITO, RL_PERSPECTIVAS_MESES, RL_PERSPECTIVAS_ANIOS, RL_JORNADA, RL_HORARIO, RL_DIAS_DESCANSO, RL_SALARIO_FIJO, RL_INCENTIVOS, RL_SEGURO_MEDICO, RL_TIPO_SEGURO, RL_FECHA_CAPTURA) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
	public int addReferencia(Connection conn, int idCliente, int idSolicitud, ReferenciaLaboralVO referencia) throws ClientesException{

		String query =	"INSERT INTO D_REFERENCIAS_LABORALES (RL_NUMCLIENTE, RL_NUMSOLICITUD, RL_ESTATUS, RL_FECHA_ELABORACION, "+
						"RL_NOMBRE_EMPRESA, RL_NOMBRE_INFORMANTE, RL_CARGO, RL_GIRO_EMPRESA, RL_RFC, RL_DIRECCION_VERDADERA, RL_TELEFONO1, RL_TELEFONO2, "+
						"RL_INICIO_OPERACIONES, RL_NUMERO_EMPLEADOS, RL_PRINCIPALES_PRODS_SERV, RL_FECHA_ALTA_HACIENDA, RL_PUESTO, "+
						"RL_DESCRIPCION_PUESTO, RL_INICIO_EMPLEO, RL_RECOMENDACION_CREDITO, RL_PERSPECTIVAS_MESES, "+
						"RL_PERSPECTIVAS_ANIOS, RL_JORNADA, RL_HORARIO, RL_DIAS_DESCANSO, RL_SALARIO_FIJO, RL_INCENTIVOS, "+
						"RL_SEGURO_MEDICO, RL_TIPO_SEGURO, RL_FECHA_CAPTURA) "+
						"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		Connection cn = null;
		int param = 1;
		int  res = 0;
		try{
			PreparedStatement ps =null;
			if( conn==null ){
				cn = getConnection();
				ps = cn.prepareStatement(query); 
			}
			else{
				ps = conn.prepareStatement(query);
			}
			
			ps.setInt(param++, idCliente );
			ps.setInt(param++, idSolicitud );
			ps.setInt(param++, referencia.estatus );
			ps.setDate(param++, referencia.fechaElaboracion );
			ps.setString(param++, referencia.nombreEmpresa );
			ps.setString(param++, referencia.nombreInformante );
			ps.setString(param++, referencia.cargo );
			ps.setString(param++, referencia.giroEmpresa );
			ps.setString(param++, referencia.rfc );
			ps.setInt(param++, referencia.direccionVerdadera );
			ps.setString(param++, referencia.telefono1 );
			ps.setString(param++, referencia.telefono2 );
			ps.setString(param++, referencia.inicioOperaciones );
			ps.setInt(param++, referencia.numeroEmpleados );
			ps.setString(param++, referencia.principalesProdsServs );
			ps.setDate(param++, referencia.fechaAltaHacienda );
			ps.setString(param++, referencia.puesto );
			ps.setString(param++, referencia.descPuesto );
			ps.setString(param++, referencia.inicioEmpleo );
			ps.setInt(param++, referencia.reconmendacionCredito );
			ps.setInt(param++, referencia.perspectivasMeses );
			ps.setInt(param++, referencia.perspectivasAnios );
			ps.setInt(param++, referencia.jornada );
			ps.setString(param++, referencia.horario );
			ps.setString(param++, referencia.diasDescanso );
			ps.setDouble(param++, referencia.salarioFijo );
			ps.setDouble(param++, referencia.incentivos );
			ps.setInt(param++, referencia.seguroMedico );
			ps.setString(param++, referencia.tipoSeguro );
			ps.setTimestamp(param++, referencia.fechaCaptura );
			Logger.debug("Ejecutando = "+query);
			Logger.debug("Referencia = "+referencia.toString());
			res = ps.executeUpdate();
			Logger.debug("Resultado = "+res);
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en addReferencia : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en addReferencia : "+e.getMessage());
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


//UPDATE D_REFERENCIAS_LABORALES RL_ESTATUS = ?, RL_FECHA_ELABORACION = ?, RL_NOMBRE_EMPRESA = ?, RL_NOMBRE_INFORMANTE = ?, RL_CARGO = ?, RL_GIRO_EMPRESA = ?, RL_RFC = ?, RL_TELEFONO1 = ?, RL_TELEFONO2 = ?, RL_INICIO_OPERACIONES = ?, RL_NUMERO_EMPLEADOS = ?, RL_PRINCIPALES_PRODS_SERV = ?, RL_FECHA_ALTA_HACIENDA = ?, RL_PUESTO = ?, RL_DESCRIPCION_PUESTO = ?, RL_INICIO_EMPLEO = ?, RL_RECOMENDACION_CREDITO = ?, RL_PERSPECTIVAS_MESES = ?, RL_PERSPECTIVAS_ANIOS = ?, RL_JORNADA = ?, RL_HORARIO = ?, RL_DIAS_DESCANSO = ?, RL_SALARIO_FIJO = ?, RL_INCENTIVOS = ?, RL_SEGURO_MEDICO = ?, RL_TIPO_SEGURO = ?  WHERE RL_NUMCLIENTE = ? AND RL_NUMSOLICITUD = ? 
	public int updateReferencia (int idCliente, int idSolicitud, ReferenciaLaboralVO referencia) throws ClientesException{

		String query =	"UPDATE D_REFERENCIAS_LABORALES SET RL_ESTATUS = ?, RL_FECHA_ELABORACION = ?, RL_NOMBRE_EMPRESA = ?, "+
						"RL_NOMBRE_INFORMANTE = ?, RL_CARGO = ?, RL_GIRO_EMPRESA = ?, RL_RFC = ?, RL_DIRECCION_VERDADERA = ?, RL_TELEFONO1 = ?, "+
						"RL_TELEFONO2 = ?, RL_INICIO_OPERACIONES = ?, RL_NUMERO_EMPLEADOS = ?, RL_PRINCIPALES_PRODS_SERV = ?, "+
						"RL_FECHA_ALTA_HACIENDA = ?, RL_PUESTO = ?, RL_DESCRIPCION_PUESTO = ?, RL_INICIO_EMPLEO = ?, "+
						"RL_RECOMENDACION_CREDITO = ?, RL_PERSPECTIVAS_MESES = ?, RL_PERSPECTIVAS_ANIOS = ?, RL_JORNADA = ?, "+
						"RL_HORARIO = ?, RL_DIAS_DESCANSO = ?, RL_SALARIO_FIJO = ?, RL_INCENTIVOS = ?, RL_SEGURO_MEDICO = ?, "+
						"RL_TIPO_SEGURO = ? WHERE RL_NUMCLIENTE = ? AND RL_NUMSOLICITUD = ?";
		
		Connection cn = null;
		int  res = 0;
		int  param = 1;
		try{
			PreparedStatement ps =null;
			cn = getConnection();
			ps = cn.prepareStatement(query);
			ps.setInt(param++, referencia.estatus );
			ps.setDate(param++, referencia.fechaElaboracion );
			ps.setString(param++, referencia.nombreEmpresa );
			ps.setString(param++, referencia.nombreInformante );
			ps.setString(param++, referencia.cargo );
			ps.setString(param++, referencia.giroEmpresa );
			ps.setString(param++, referencia.rfc );
			ps.setInt(param++, referencia.direccionVerdadera );
			ps.setString(param++, referencia.telefono1 );
			ps.setString(param++, referencia.telefono2 );
			ps.setString(param++, referencia.inicioOperaciones );
			ps.setInt(param++, referencia.numeroEmpleados );
			ps.setString(param++, referencia.principalesProdsServs );
			ps.setDate(param++, referencia.fechaAltaHacienda );
			ps.setString(param++, referencia.puesto );
			ps.setString(param++, referencia.descPuesto );
			ps.setString(param++, referencia.inicioEmpleo );
			ps.setInt(param++, referencia.reconmendacionCredito );
			ps.setInt(param++, referencia.perspectivasMeses );
			ps.setInt(param++, referencia.perspectivasAnios );
			ps.setInt(param++, referencia.jornada );
			ps.setString(param++, referencia.horario );
			ps.setString(param++, referencia.diasDescanso );
			ps.setDouble(param++, referencia.salarioFijo );
			ps.setDouble(param++, referencia.incentivos );
			ps.setInt(param++, referencia.seguroMedico );
			ps.setString(param++, referencia.tipoSeguro );
			ps.setInt(param++, idCliente);
			ps.setInt(param++, idSolicitud);
			Logger.debug("Ejecutando = "+query);
			Logger.debug("Referencia = "+referencia.toString());
			res = ps.executeUpdate();
			Logger.debug("Resultado = "+res);
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en updateReferencia : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en updateReferencia : "+e.getMessage());
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
