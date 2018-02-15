package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.ScoringVO;


public class ScoringDAO extends DAOMaster{


	public ScoringVO getScoring(int idCliente, int idSolicitud) throws ClientesException{
		ScoringVO  scoring = null;
		Connection cn = null;
		String query =	"SELECT * FROM D_SCORING WHERE SC_NUMCLIENTE = ? AND SC_NUMSOLICITUD = ?";
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(1, idCliente);
			ps.setInt(2, idSolicitud);
//			Logger.debug("Ejecutando = "+query);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				scoring =  new ScoringVO();
				scoring.idCliente = rs.getInt("sc_numcliente");
				scoring.idSolicitud = rs.getInt("sc_numsolicitud");
				scoring.estatus = rs.getInt("sc_estatus");
				scoring.calificacionSIC = rs.getInt("sc_calif_sic");
				scoring.tipoCuenta = rs.getInt("sc_tipo_cuenta");
				scoring.antCuenta = rs.getInt("sc_ant_cuenta");
				scoring.numBuquedas = rs.getInt("sc_busquedas_cuenta");
				scoring.genero = rs.getInt("sc_genero");
				scoring.edad = rs.getInt("sc_edad");
				scoring.estadoCivil = rs.getInt("sc_estado_civil");
				scoring.antLaboral = rs.getInt("sc_ant_laboral");
				scoring.tipoContrato = rs.getInt("sc_tipo_contrato");
				scoring.situacionVivienda = rs.getInt("sc_situacion_vivienda");
				scoring.alquilerHipoteca = rs.getDouble("sc_alquiler_hipot");
				scoring.tiempoResidencia = rs.getInt("sc_tiempo_resid");
				scoring.dependientesEconomicos = rs.getInt("sc_dependientes");
				scoring.ingresosNomina = rs.getDouble("sc_ingresos_nom");
				scoring.otrosNoComprobables = rs.getDouble("sc_otros_no_comp");
				scoring.otrosIngresos = rs.getDouble("sc_otros_ingresos");
				scoring.rentaVivienda = rs.getDouble("sc_renta_viv");
				scoring.pagoDeuda = rs.getDouble("sc_pago_deuda");
				scoring.otrosGastos = rs.getDouble("sc_otros_gastos");
				scoring.disponibleMensual = rs.getDouble("sc_disponible_mensual");
				scoring.cuotaSobreDisponible = rs.getDouble("sc_cuota_sobre_disp");
				scoring.cuotaSobreIngresoBruto = rs.getDouble("sc_cuota_sobre_ingreso");
				scoring.nivelVivienda = rs.getInt("sc_nivel_viv");
				scoring.calificacionZona = rs.getInt("sc_calif_zona");
				scoring.pisosVivienda = rs.getInt("sc_pisos_viv");
				scoring.habitacionesVivienda = rs.getInt("sc_habit_vivienda");
				scoring.caracteristicasFachada = rs.getInt("sc_fachada");
				scoring.caracteristicasTecho = rs.getInt("sc_techo");
				scoring.arraigoEmpresa = rs.getInt("sc_arraigo_empresa");
				scoring.numeroEmpleados = rs.getInt("sc_numero_empl");
				scoring.jornada = rs.getInt("sc_jornada");
				scoring.plazoContrato = rs.getInt("sc_plazo_contrato");
				scoring.referencia1 = rs.getInt("sc_referencia1");
				scoring.referencia2 = rs.getInt("sc_referencia2");
				scoring.referenciaLaboral = rs.getInt("sc_referencia_laboral");
				scoring.referenciaArrendador = rs.getInt("sc_referencia_arrendador");
				scoring.destinoCredito = rs.getInt("sc_destino_credito");
				scoring.monto = rs.getDouble("sc_monto");
				scoring.plazo = rs.getInt("sc_plazo");
				scoring.montoConComision = rs.getDouble("sc_monto_comision");
				scoring.cuota = rs.getDouble("sc_cuota");
				scoring.tasa = rs.getInt("sc_tasa");
				scoring.comision = rs.getInt("sc_comision");
				scoring.totalCostofinanciero = rs.getDouble("sc_total_costo_fin");
				scoring.coberturaPago = rs.getInt("sc_cobertura_pago");
				scoring.tramo = rs.getInt("sc_tramo");
				scoring.puntuacion = rs.getDouble("sc_puntuacion");
				scoring.dictamenFinal = rs.getInt("sc_dictamen");
				scoring.periodicidad = rs.getInt("sc_periodicidad");
				scoring.fechaCaptura = rs.getTimestamp("sc_fecha_captura");
			}
		}catch(SQLException sqle) {
			Logger.debug("SQLException en getScoring : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en getScoring : "+e.getMessage());
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
		return scoring;
	}

	public int addScoring(ScoringVO scoring) throws ClientesException{
		return addScoring(null, scoring);
	}

	public int addScoring(Connection conn, ScoringVO scoring) throws ClientesException{

		String query =	"INSERT INTO D_SCORING (SC_NUMCLIENTE, SC_NUMSOLICITUD, SC_ESTATUS, SC_CALIF_SIC, SC_TIPO_CUENTA, "+
						"SC_ANT_CUENTA, SC_BUSQUEDAS_CUENTA, SC_GENERO, SC_EDAD, SC_ESTADO_CIVIL, SC_ANT_LABORAL, "+
						"SC_TIPO_CONTRATO, SC_SITUACION_VIVIENDA, SC_ALQUILER_HIPOT, SC_TIEMPO_RESID, SC_DEPENDIENTES, "+
						"SC_INGRESOS_NOM, SC_OTROS_NO_COMP, SC_OTROS_INGRESOS, SC_RENTA_VIV, SC_PAGO_DEUDA, SC_OTROS_GASTOS, "+
						"SC_DISPONIBLE_MENSUAL, SC_CUOTA_SOBRE_DISP, SC_CUOTA_SOBRE_INGRESO, SC_NIVEL_VIV, SC_CALIF_ZONA, "+
						"SC_PISOS_VIV, SC_HABIT_VIVIENDA, SC_FACHADA, SC_TECHO, SC_ARRAIGO_EMPRESA, SC_NUMERO_EMPL, "+
						"SC_JORNADA, SC_PLAZO_CONTRATO, SC_REFERENCIA1, SC_REFERENCIA2, SC_REFERENCIA_LABORAL, "+
						"SC_REFERENCIA_ARRENDADOR, SC_DESTINO_CREDITO, SC_MONTO, SC_PLAZO, SC_MONTO_COMISION, SC_CUOTA, "+
						"SC_TASA, SC_COMISION, SC_TOTAL_COSTO_FIN, SC_COBERTURA_PAGO, SC_TRAMO, SC_PUNTUACION, SC_DICTAMEN, SC_FECHA_CAPTURA, SC_PERIODICIDAD) VALUES (?, ?, ?, ?, ?, ?,"+
						" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "+
						"?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP,?)";

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
			
			ps.setInt(param++, scoring.idCliente);
			ps.setInt(param++, scoring.idSolicitud);
			ps.setInt(param++, scoring.estatus);
			ps.setInt(param++, scoring.calificacionSIC);
			ps.setInt(param++, scoring.tipoCuenta);
			ps.setInt(param++, scoring.antCuenta);
			ps.setInt(param++, scoring.numBuquedas);
			ps.setInt(param++, scoring.genero);
			ps.setInt(param++, scoring.edad);
			ps.setInt(param++, scoring.estadoCivil);
			ps.setInt(param++, scoring.antLaboral);
			ps.setInt(param++, scoring.tipoContrato);
			ps.setInt(param++, scoring.situacionVivienda);
			ps.setDouble(param++, scoring.alquilerHipoteca);
			ps.setInt(param++, scoring.tiempoResidencia);
			ps.setInt(param++, scoring.dependientesEconomicos);
			ps.setDouble(param++, scoring.ingresosNomina);
			ps.setDouble(param++, scoring.otrosNoComprobables);
			ps.setDouble(param++, scoring.otrosIngresos);
			ps.setDouble(param++, scoring.rentaVivienda);
			ps.setDouble(param++, scoring.pagoDeuda);
			ps.setDouble(param++, scoring.otrosGastos);
			ps.setDouble(param++, scoring.disponibleMensual);
			ps.setDouble(param++, scoring.cuotaSobreDisponible);
			ps.setDouble(param++, scoring.cuotaSobreIngresoBruto);
			ps.setInt(param++, scoring.nivelVivienda);
			ps.setInt(param++, scoring.calificacionZona);
			ps.setInt(param++, scoring.pisosVivienda);
			ps.setInt(param++, scoring.habitacionesVivienda);
			ps.setInt(param++, scoring.caracteristicasFachada);
			ps.setInt(param++, scoring.caracteristicasTecho);
			ps.setInt(param++, scoring.arraigoEmpresa);
			ps.setInt(param++, scoring.numeroEmpleados);
			ps.setInt(param++, scoring.jornada);
			ps.setInt(param++, scoring.plazoContrato);
			ps.setInt(param++, scoring.referencia1);
			ps.setInt(param++, scoring.referencia2);
			ps.setInt(param++, scoring.referenciaLaboral);
			ps.setInt(param++, scoring.referenciaArrendador);
			ps.setInt(param++, scoring.destinoCredito);
			ps.setDouble(param++, scoring.monto);
			ps.setInt(param++, scoring.plazo);
			ps.setDouble(param++, scoring.montoConComision);
			ps.setDouble(param++, scoring.cuota);
			ps.setInt(param++, scoring.tasa);
			ps.setInt(param++, scoring.comision);
			ps.setDouble(param++, scoring.totalCostofinanciero);
			ps.setInt(param++, scoring.coberturaPago);
			ps.setInt(param++, scoring.tramo);
			ps.setDouble(param++, scoring.puntuacion);
			ps.setInt(param++, scoring.dictamenFinal);
			ps.setInt(param++, scoring.periodicidad);
			Logger.debug("Ejecutando = "+query);
			Logger.debug("Scoring= "+scoring.toString());
			res = ps.executeUpdate();
			Logger.debug("Resultado = "+res);
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en addScoring : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en addScoring : "+e.getMessage());
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

 

	public int updateScoring (ScoringVO scoring) throws ClientesException{

		String query =	"UPDATE D_SCORING SET SC_ESTATUS = ?, SC_CALIF_SIC = ?, SC_TIPO_CUENTA = ?, "+
						"SC_ANT_CUENTA = ?, SC_BUSQUEDAS_CUENTA = ?, SC_GENERO = ?, SC_EDAD = ?, "+
						"SC_ESTADO_CIVIL = ?, SC_ANT_LABORAL = ?, SC_TIPO_CONTRATO = ?, SC_SITUACION_VIVIENDA = ?, "+
						"SC_ALQUILER_HIPOT = ?, SC_TIEMPO_RESID = ?, SC_DEPENDIENTES = ?, SC_INGRESOS_NOM = ?, "+
						"SC_OTROS_NO_COMP = ?, SC_OTROS_INGRESOS = ?, SC_RENTA_VIV = ?, SC_PAGO_DEUDA = ?, "+
						"SC_OTROS_GASTOS = ?, SC_DISPONIBLE_MENSUAL = ?, SC_CUOTA_SOBRE_DISP = ?, SC_CUOTA_SOBRE_INGRESO = ?, "+
						"SC_NIVEL_VIV = ?, SC_CALIF_ZONA = ?, SC_PISOS_VIV = ?, SC_HABIT_VIVIENDA = ?, SC_FACHADA = ?, "+
						"SC_TECHO = ?, SC_ARRAIGO_EMPRESA = ?, SC_NUMERO_EMPL = ?, SC_JORNADA = ?, SC_PLAZO_CONTRATO = ?, "+
						"SC_REFERENCIA1 = ?, SC_REFERENCIA2 = ?, SC_REFERENCIA_LABORAL = ?, SC_REFERENCIA_ARRENDADOR = ?, "+
						"SC_DESTINO_CREDITO = ?, SC_MONTO = ?, SC_PLAZO = ?, SC_MONTO_COMISION = ?, SC_CUOTA = ?, "+
						"SC_TASA = ?, SC_COMISION = ?, SC_TOTAL_COSTO_FIN = ?, SC_COBERTURA_PAGO = ?, SC_TRAMO = ?, SC_PUNTUACION = ?, SC_DICTAMEN = ?, SC_PERIODICIDAD = ? WHERE SC_NUMCLIENTE = ? AND SC_NUMSOLICITUD = ?";
		
		Connection cn = null;
		int  res = 0;
		int  param = 1;
		try{
			PreparedStatement ps =null;
			cn = getConnection();
			ps = cn.prepareStatement(query); 
			ps.setInt(param++, scoring.estatus);
			ps.setInt(param++, scoring.calificacionSIC);
			ps.setInt(param++, scoring.tipoCuenta);
			ps.setInt(param++, scoring.antCuenta);
			ps.setInt(param++, scoring.numBuquedas);
			ps.setInt(param++, scoring.genero);
			ps.setInt(param++, scoring.edad);
			ps.setInt(param++, scoring.estadoCivil);
			ps.setInt(param++, scoring.antLaboral);
			ps.setInt(param++, scoring.tipoContrato);
			ps.setInt(param++, scoring.situacionVivienda);
			ps.setDouble(param++, scoring.alquilerHipoteca);
			ps.setInt(param++, scoring.tiempoResidencia);
			ps.setInt(param++, scoring.dependientesEconomicos);
			ps.setDouble(param++, scoring.ingresosNomina);
			ps.setDouble(param++, scoring.otrosNoComprobables);
			ps.setDouble(param++, scoring.otrosIngresos);
			ps.setDouble(param++, scoring.rentaVivienda);
			ps.setDouble(param++, scoring.pagoDeuda);
			ps.setDouble(param++, scoring.otrosGastos);
			ps.setDouble(param++, scoring.disponibleMensual);
			ps.setDouble(param++, scoring.cuotaSobreDisponible);
			ps.setDouble(param++, scoring.cuotaSobreIngresoBruto);
			ps.setInt(param++, scoring.nivelVivienda);
			ps.setInt(param++, scoring.calificacionZona);
			ps.setInt(param++, scoring.pisosVivienda);
			ps.setInt(param++, scoring.habitacionesVivienda);
			ps.setInt(param++, scoring.caracteristicasFachada);
			ps.setInt(param++, scoring.caracteristicasTecho);
			ps.setInt(param++, scoring.arraigoEmpresa);
			ps.setInt(param++, scoring.numeroEmpleados);
			ps.setInt(param++, scoring.jornada);
			ps.setInt(param++, scoring.plazoContrato);
			ps.setInt(param++, scoring.referencia1);
			ps.setInt(param++, scoring.referencia2);
			ps.setInt(param++, scoring.referenciaLaboral);
			ps.setInt(param++, scoring.referenciaArrendador);
			ps.setInt(param++, scoring.destinoCredito);
			ps.setDouble(param++, scoring.monto);
			ps.setInt(param++, scoring.plazo);
			ps.setDouble(param++, scoring.montoConComision);
			ps.setDouble(param++, scoring.cuota);
			ps.setInt(param++, scoring.tasa);
			ps.setInt(param++, scoring.comision);
			ps.setDouble(param++, scoring.totalCostofinanciero);
			ps.setInt(param++, scoring.coberturaPago);
			ps.setInt(param++, scoring.tramo);
			ps.setDouble(param++, scoring.puntuacion);
			ps.setInt(param++, scoring.dictamenFinal);
			ps.setInt(param++, scoring.periodicidad);
			ps.setInt(param++, scoring.idCliente);
			ps.setInt(param++, scoring.idSolicitud);
			Logger.debug("Ejecutando = "+query);
			Logger.debug("Scoring = "+scoring.toString());
			res = ps.executeUpdate();
			Logger.debug("Resultado = "+res);
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en updateScoring : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en updateScoring : "+e.getMessage());
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