package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.NamingException;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.SaldoT24VO;

public class SaldoT24DAO extends DAOMaster{
	
	public void updateSucursal(String nomSucursal, int numSucursal, String rfc){
		Connection cn = null;
		String query = "UPDATE D_SALDOS_T24 SET ST_NOMBRE_SUCURSAL = ?, ST_NUMSUCURSAL = ? WHERE ST_RFC = ?";
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			int param = 1;
			ps.setString(param++, nomSucursal);
			ps.setInt(param++, numSucursal);
			ps.setString(param++, rfc);
			ps.executeUpdate();
		}
		catch(SQLException exc){
			System.out.println("SQLException en saldoT24DAO.UpdateSucursal():" + exc);
			exc.printStackTrace();
		}
		catch(NamingException exc){
			System.out.println("NamingException en saldoT24DAO.UpdateSucursal():" + exc);
			exc.printStackTrace();
		}
		finally{
			if(cn != null){
				try{
					cn.close();
				}
				catch(SQLException exc1){
					Logger.debug("SQLException en saldoT24DAO.UpdaetSucursal():" + exc1);
				}
			}
		}
	}
	
	
	public void updateDireccionTelefono(String telefono, String direccion1, String direccion2, String rfc){
		Connection cn = null;
		String query = "UPDATE D_SALDOS_T24 SET ST_DIRECCION1 = ?, ST_DIRECCION2 = ?, ST_TELEFONO = ? WHERE ST_RFC = ?";
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			int param = 1;
			ps.setString(param++, direccion1);
			ps.setString(param++, direccion2);
			ps.setString(param++, telefono);
			ps.setString(param++, rfc);
			ps.executeUpdate();
		}
		catch(SQLException exc){
			Logger.debug("SQLException en saldoT24DAO.updateDireccionTelefono():" + exc);
			exc.printStackTrace();
		}
		catch(NamingException exc){
			Logger.debug("NamingException en saldoT24DAO.updateDireccionTelefono():" + exc);
			exc.printStackTrace();
		}
		finally{
			if(cn != null){
				try{
					cn.close();
				}
				catch(SQLException exc1){
					Logger.debug("SQLException en saldoT24DAO.updateDireccionTelefono():" + exc1);
					exc1.printStackTrace();
				}
			}
		}
	}

	public SaldoT24VO[] getSaldosT24(){
		Connection cn = null;
		ArrayList<SaldoT24VO> arrayListSaldos = new ArrayList<SaldoT24VO>();
		SaldoT24VO[] arraySaldo = null;
		String query = "SELECT * FROM D_SALDOS_T24 ORDER BY ST_NUMCLIENTE";
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			Logger.debug("Ejecutando : " + query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				SaldoT24VO saldo = new SaldoT24VO();
				saldo.capitalVencido = roundNum(rs.getDouble("st_capital_vencido"));
				saldo.comision = roundNum(rs.getDouble("st_comision"));
				saldo.ciclo = rs.getInt("st_ciclo");
				saldo.desembolso = roundNum(rs.getDouble("st_desembolso"));
				saldo.destinoCredito = rs.getInt("st_destino_credito");
				saldo.diasVencidos = rs.getInt("st_dias_vencidos");
				saldo.direccion1 = rs.getString("st_direccion1");
				saldo.direccion2 = rs.getString("st_direccion2");
				saldo.fechaDisposicion = rs.getDate("st_fecha_disposicion");
				saldo.fechaEntradaCartVencida = rs.getDate("st_fecha_entrada_a_cart_vencida");
				saldo.fechaGeneracion = rs.getDate("st_fecha_generacion");
				saldo.fechaIncumplimiento = rs.getDate("st_fecha_incumplimiento");
				saldo.fechaInf = rs.getDate("st_fecha_inf");
				saldo.fechaVencimiento = rs.getDate("st_fecha_vencimiento");
				saldo.feeVencidos = roundNum(rs.getDouble("st_fee_vencidos"));
				saldo.formaAmortizacion = rs.getInt("st_forma_amortizacion");
				saldo.formaPagoIntereses = rs.getInt("st_forma_pago_intereses");
				saldo.frecuenciaAmortizacion = rs.getString("st_frecuencia_amortizacion");
				saldo.interesesDevNoCobrados = roundNum(rs.getDouble("st_intereses_dev_no_cobrados"));
				saldo.interesesVencidos = roundNum(rs.getDouble("st_intereses_vencidos"));
				saldo.interesMoratorio = roundNum(rs.getDouble("st_interes_moratorio"));
				saldo.ivaComision = roundNum(rs.getDouble("st_iva_comision"));
				saldo.ivaMulta = roundNum(rs.getDouble("st_iva_multa"));
				saldo.moneda = rs.getInt("st_moneda");
				saldo.montoAmortizacion = roundNum(rs.getDouble("st_monto_amortizacion"));
				saldo.montoInteresesPorCobrar = roundNum(rs.getDouble("st_monto_intereses_por_cobrar"));
				saldo.montoLineaCredito = roundNum(rs.getDouble("st_monto_linea_credito"));
				saldo.multa = roundNum(rs.getDouble("st_multa"));
				saldo.nombre = rs.getString("st_nombre");
				saldo.nombreSucursal = rs.getString("st_nombre_sucursal");
				saldo.numCliente = rs.getInt("st_numcliente");
				saldo.numCuotas = rs.getInt("st_numero_cuotas");
				saldo.numCuotasRestantes = rs.getInt("st_numero_cuotas_restantes");
				saldo.numeroPagosVencidos = rs.getInt("st_numero_pagos_vencidos");
				saldo.numSucursal = rs.getInt("st_numsucursal");
				saldo.responsabilidadTotal = roundNum(rs.getDouble("st_responsabilidad_total"));
				saldo.rfc = rs.getString("st_rfc");
				saldo.saldoInsoluto = roundNum(rs.getDouble("st_saldo_insoluto"));
				saldo.situacionActualCredito = rs.getInt("st_situacion_actual_credito");
				saldo.tasaBruta = roundNum(rs.getDouble("st_tasa_bruta"));
				saldo.tasaReferencia = roundNum(rs.getDouble("st_tasa_referencia"));
				saldo.telefono = rs.getString("st_telefono");
				saldo.ivaMoratorio = roundNum(rs.getDouble("st_iva_moratorio"));
				saldo.totalVencido = roundNum(rs.getDouble("st_total_vencido"));
				saldo.totalExigible = roundNum(rs.getDouble("st_total_exigible"));
				saldo.capitalPagado = roundNum(rs.getDouble("st_capital_pagado"));
				saldo.interesPagado = roundNum(rs.getDouble("st_interes_pagado"));
				saldo.totalPagado = roundNum(rs.getDouble("st_total_pagado"));
				saldo.ejecutivo = rs.getString("st_ejecutivo");
				saldo.diasTotales = rs.getInt("st_dias_totales");
				saldo.interesFuturo = roundNum(rs.getDouble("st_interes_futuro"));
				saldo.moratorioPagado = roundNum(rs.getDouble("st_moratorio_pagado"));
				saldo.montoLiquidacionAnticipada = roundNum(rs.getDouble("st_monto_liquidacion_anticipada"));
				saldo.fechaProximoVencimiento = rs.getDate("st_fecha_proximo_vencimiento");
				saldo.interesAlDia = roundNum(rs.getDouble("st_interes_al_dia"));
				saldo.diasTranscurridos = rs.getInt("st_dias_transcurridos");
				saldo.totalMulta = roundNum(rs.getDouble("st_total_multa"));
				saldo.totalMoratorios = roundNum(rs.getDouble("st_total_moratorios"));
				saldo.fechaSigAmortizacion = rs.getDate("st_fecha_sig_amort");
				saldo.fuenteFondeo = rs.getInt("st_fuente_fondeo");
				saldo.numOperacion = rs.getInt("st_numoperacion");
				saldo.tipoGtiaReal = rs.getInt("st_tipo_garantia_real");
				saldo.tipoRelacion = rs.getInt("st_tipo_relacion");
				saldo.tipoIndustria = rs.getInt("st_tipo_industria");
				saldo.montoAprobado = roundNum(rs.getDouble("st_monto_aprobado"));
				saldo.noCuenta = rs.getString("st_referencia");
				saldo.nombreProducto = rs.getString("st_nombre_producto");
				arrayListSaldos.add(saldo);
			}
		}
		catch(SQLException exc){
			System.out.println("SQLException en saldoT24DAO.getSaldosT24():" + exc);
			exc.printStackTrace();
		}
		catch(NamingException exc){
			System.out.println("NamingException en saldoT24DAO.getSaldosT24():" + exc);
			exc.printStackTrace();
		}
		catch(Exception exc){
			System.out.println("Esception caugth:" + exc);
			exc.printStackTrace();
		}
		finally{
			if(cn != null){
				try{
					cn.close();
				}
				catch(SQLException exc1){
					System.out.println("SQLException en saldoT24DAO.getSaldosT24():" + exc1);
					exc1.printStackTrace();
				}
			}
		}
		arraySaldo = new SaldoT24VO[ arrayListSaldos.size()];
		for(int i = 0; i < arraySaldo.length; i++){
			arraySaldo[i] = arrayListSaldos.get(i);
		}
		return arraySaldo;
	}
	
	
	
	public SaldoT24VO getSaldosT24ByNumClienteCiclo(int numCliente, int ciclo, String referencia){

		Connection cn = null;
		int param = 1;
		SaldoT24VO saldo = null;
		String query = "SELECT * FROM D_SALDOS_T24 WHERE ST_CICLO = ? AND ST_NUMCLIENTE = ? AND ST_REFERENCIA = ? AND ST_SITUACION_ACTUAL_CREDITO != 4";
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(param++, ciclo);
			ps.setInt(param++, numCliente);
			ps.setString(param++, referencia);
//			Logger.debug("Ejecutando : "+query);
//			Logger.debug("Para : " + numCliente + " con ciclo: " + ciclo);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				saldo = new SaldoT24VO();
				saldo.capitalVencido = roundNum(rs.getDouble("st_capital_vencido"));
				saldo.comision = roundNum(rs.getDouble("st_comision"));
				saldo.ciclo = rs.getInt("st_ciclo");
				saldo.desembolso = roundNum(rs.getDouble("st_desembolso"));
				saldo.destinoCredito = rs.getInt("st_destino_credito");
				saldo.diasVencidos = rs.getInt("st_dias_vencidos");
				saldo.direccion1 = rs.getString("st_direccion1");
				saldo.direccion2 = rs.getString("st_direccion2");
				saldo.fechaDisposicion = rs.getDate("st_fecha_disposicion");
				saldo.fechaEntradaCartVencida = rs.getDate("st_fecha_entrada_a_cart_vencida");
				saldo.fechaGeneracion = rs.getDate("st_fecha_generacion");
				saldo.fechaIncumplimiento = rs.getDate("st_fecha_incumplimiento");
				saldo.fechaInf = rs.getDate("st_fecha_inf");
				saldo.fechaVencimiento = rs.getDate("st_fecha_vencimiento");
				saldo.feeVencidos = roundNum(rs.getDouble("st_fee_vencidos"));
				saldo.formaAmortizacion = rs.getInt("st_forma_amortizacion");
				saldo.formaPagoIntereses = rs.getInt("st_forma_pago_intereses");
				saldo.frecuenciaAmortizacion = rs.getString("st_frecuencia_amortizacion");
				saldo.interesesDevNoCobrados = roundNum(rs.getDouble("st_intereses_dev_no_cobrados"));
				saldo.interesesVencidos = roundNum(rs.getDouble("st_intereses_vencidos"));
				saldo.interesMoratorio = roundNum(rs.getDouble("st_interes_moratorio"));
				saldo.ivaComision = roundNum(rs.getDouble("st_iva_comision"));
				saldo.ivaMulta = roundNum(rs.getDouble("st_iva_multa"));
				saldo.moneda = rs.getInt("st_moneda");
				saldo.montoAmortizacion = roundNum(rs.getDouble("st_monto_amortizacion"));
				saldo.montoInteresesPorCobrar = roundNum(rs.getDouble("st_monto_intereses_por_cobrar"));
				saldo.montoLineaCredito = roundNum(rs.getDouble("st_monto_linea_credito"));
				saldo.multa = roundNum(rs.getDouble("st_multa"));
				saldo.nombre = rs.getString("st_nombre");
				saldo.nombreSucursal = rs.getString("st_nombre_sucursal");
				saldo.numCliente = rs.getInt("st_numcliente");
				saldo.numCuotas = rs.getInt("st_numero_cuotas");
				saldo.numCuotasRestantes = rs.getInt("st_numero_cuotas_restantes");
				saldo.numeroPagosVencidos = rs.getInt("st_numero_pagos_vencidos");
				saldo.numSucursal = rs.getInt("st_numsucursal");
				saldo.responsabilidadTotal = roundNum(rs.getDouble("st_responsabilidad_total"));
				saldo.rfc = rs.getString("st_rfc");
				saldo.saldoInsoluto = roundNum(rs.getDouble("st_saldo_insoluto"));
				saldo.situacionActualCredito = rs.getInt("st_situacion_actual_credito");
				saldo.tasaBruta = roundNum(rs.getDouble("st_tasa_bruta"));
				saldo.tasaReferencia = roundNum(rs.getDouble("st_tasa_referencia"));
				saldo.telefono = rs.getString("st_telefono");
				saldo.ivaMoratorio = roundNum(rs.getDouble("st_iva_moratorio"));
				saldo.totalVencido = roundNum(rs.getDouble("st_total_vencido"));
				saldo.totalExigible = roundNum(rs.getDouble("st_total_exigible"));
				saldo.capitalPagado = roundNum(rs.getDouble("st_capital_pagado"));
				saldo.interesPagado = roundNum(rs.getDouble("st_interes_pagado"));
				saldo.totalPagado = roundNum(rs.getDouble("st_total_pagado"));
				saldo.ejecutivo = rs.getString("st_ejecutivo");
				saldo.diasTotales = rs.getInt("st_dias_totales");
				saldo.interesFuturo = roundNum(rs.getDouble("st_interes_futuro"));
				saldo.moratorioPagado = roundNum(rs.getDouble("st_moratorio_pagado"));
				saldo.montoLiquidacionAnticipada = roundNum(rs.getDouble("st_monto_liquidacion_anticipada"));
				saldo.fechaProximoVencimiento = rs.getDate("st_fecha_proximo_vencimiento");
				saldo.interesAlDia = roundNum(rs.getDouble("st_interes_al_dia"));
				saldo.diasTranscurridos = rs.getInt("st_dias_transcurridos");
				saldo.totalMulta = roundNum(rs.getDouble("st_total_multa"));
				saldo.totalMoratorios = roundNum(rs.getDouble("st_total_moratorios"));
				saldo.fechaSigAmortizacion = rs.getDate("st_fecha_sig_amort");
				saldo.fuenteFondeo = rs.getInt("st_fuente_fondeo");
				saldo.numOperacion = rs.getInt("st_numoperacion");
				saldo.tipoGtiaReal = rs.getInt("st_tipo_garantia_real");
				saldo.tipoRelacion = rs.getInt("st_tipo_relacion");
				saldo.tipoIndustria = rs.getInt("st_tipo_industria");
				saldo.montoAprobado = roundNum(rs.getDouble("st_monto_aprobado"));
				saldo.noCuenta = rs.getString("st_referencia");
				saldo.nombreProducto = rs.getString("st_nombre_producto");
			}
		}
		catch(SQLException exc){
			Logger.debug("SQLException en saldoT24DAO.getSaldosT24ByContrato::" + exc);
			exc.printStackTrace();
		}
		catch(NamingException exc){
			Logger.debug("NamingException en saldoT24DAO.getSaldosT24ByContrato::" + exc);
			exc.printStackTrace();
		}
		catch(Exception exc){
			Logger.debug("Exception en saldoT24DAO.getSaldosT24ByContrato::" + exc);
			exc.printStackTrace();
		}
		finally{
			if(cn != null){
				try{
					cn.close();
				}
				catch(SQLException exc1){
					Logger.debug("SQLException en saldoT24DAO.getSaldosT24ByContrato::" + exc1);
					exc1.printStackTrace();
				}
			}
		}
		
		return saldo;
	}
	
	public SaldoT24VO getSaldosT24ByNumClienteSolicitudProducto(int numCliente, int numSolicitud, int numProducto){
		Connection cn = null;
		int param = 1;
		SaldoT24VO saldo = new SaldoT24VO();
		String query = "SELECT * FROM D_SALDOS_T24 WHERE ST_CICLO = ? AND ST_NUMCLIENTE = ? AND ST_NUMOPERACION = ? AND ST_SITUACION_ACTUAL_CREDITO != 4";
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(param++, numSolicitud);
			ps.setInt(param++, numCliente);
			ps.setInt(param++, numProducto);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				saldo.capitalVencido = roundNum(rs.getDouble("st_capital_vencido"));
				saldo.comision = roundNum(rs.getDouble("st_comision"));
				saldo.ciclo = rs.getInt("st_ciclo");
				saldo.desembolso = roundNum(rs.getDouble("st_desembolso"));
				saldo.destinoCredito = rs.getInt("st_destino_credito");
				saldo.diasVencidos = rs.getInt("st_dias_vencidos");
				saldo.direccion1 = rs.getString("st_direccion1");
				saldo.direccion2 = rs.getString("st_direccion2");
				saldo.fechaDisposicion = rs.getDate("st_fecha_disposicion");
				saldo.fechaEntradaCartVencida = rs.getDate("st_fecha_entrada_a_cart_vencida");
				saldo.fechaGeneracion = rs.getDate("st_fecha_generacion");
				saldo.fechaIncumplimiento = rs.getDate("st_fecha_incumplimiento");
				saldo.fechaInf = rs.getDate("st_fecha_inf");
				saldo.fechaVencimiento = rs.getDate("st_fecha_vencimiento");
				saldo.feeVencidos = roundNum(rs.getDouble("st_fee_vencidos"));
				saldo.formaAmortizacion = rs.getInt("st_forma_amortizacion");
				saldo.formaPagoIntereses = rs.getInt("st_forma_pago_intereses");
				saldo.frecuenciaAmortizacion = rs.getString("st_frecuencia_amortizacion");
				saldo.interesesDevNoCobrados = roundNum(rs.getDouble("st_intereses_dev_no_cobrados"));
				saldo.interesesVencidos = roundNum(rs.getDouble("st_intereses_vencidos"));
				saldo.interesMoratorio = roundNum(rs.getDouble("st_interes_moratorio"));
				saldo.ivaComision = roundNum(rs.getDouble("st_iva_comision"));
				saldo.ivaMulta = roundNum(rs.getDouble("st_iva_multa"));
				saldo.moneda = rs.getInt("st_moneda");
				saldo.montoAmortizacion = roundNum(rs.getDouble("st_monto_amortizacion"));
				saldo.montoInteresesPorCobrar = roundNum(rs.getDouble("st_monto_intereses_por_cobrar"));
				saldo.montoLineaCredito = roundNum(rs.getDouble("st_monto_linea_credito"));
				saldo.multa = roundNum(rs.getDouble("st_multa"));
				saldo.nombre = rs.getString("st_nombre");
				saldo.nombreSucursal = rs.getString("st_nombre_sucursal");
				saldo.numCliente = rs.getInt("st_numcliente");
				saldo.numCuotas = rs.getInt("st_numero_cuotas");
				saldo.numCuotasRestantes = rs.getInt("st_numero_cuotas_restantes");
				saldo.numeroPagosVencidos = rs.getInt("st_numero_pagos_vencidos");
				saldo.numSucursal = rs.getInt("st_numsucursal");
				saldo.responsabilidadTotal = roundNum(rs.getDouble("st_responsabilidad_total"));
				saldo.rfc = rs.getString("st_rfc");
				saldo.saldoInsoluto = roundNum(rs.getDouble("st_saldo_insoluto"));
				saldo.situacionActualCredito = rs.getInt("st_situacion_actual_credito");
				saldo.tasaBruta = roundNum(rs.getDouble("st_tasa_bruta"));
				saldo.tasaReferencia = roundNum(rs.getDouble("st_tasa_referencia"));
				saldo.telefono = rs.getString("st_telefono");
				saldo.ivaMoratorio = roundNum(rs.getDouble("st_iva_moratorio"));
				saldo.totalVencido = roundNum(rs.getDouble("st_total_vencido"));
				saldo.totalExigible = roundNum(rs.getDouble("st_total_exigible"));
				saldo.capitalPagado = roundNum(rs.getDouble("st_capital_pagado"));
				saldo.interesPagado = roundNum(rs.getDouble("st_interes_pagado"));
				saldo.totalPagado = roundNum(rs.getDouble("st_total_pagado"));
				saldo.ejecutivo = rs.getString("st_ejecutivo");
				saldo.diasTotales = rs.getInt("st_dias_totales");
				saldo.interesFuturo = roundNum(rs.getDouble("st_interes_futuro"));
				saldo.moratorioPagado = roundNum(rs.getDouble("st_moratorio_pagado"));
				saldo.montoLiquidacionAnticipada = roundNum(rs.getDouble("st_monto_liquidacion_anticipada"));
				saldo.fechaProximoVencimiento = rs.getDate("st_fecha_proximo_vencimiento");
				saldo.interesAlDia = roundNum(rs.getDouble("st_interes_al_dia"));
				saldo.diasTranscurridos = rs.getInt("st_dias_transcurridos");
				saldo.totalMulta = roundNum(rs.getDouble("st_total_multa"));
				saldo.totalMoratorios = roundNum(rs.getDouble("st_total_moratorios"));
				saldo.fechaSigAmortizacion = rs.getDate("st_fecha_sig_amort");
				saldo.fuenteFondeo = rs.getInt("st_fuente_fondeo");
				saldo.numOperacion = rs.getInt("st_numoperacion");
				saldo.tipoGtiaReal = rs.getInt("st_tipo_garantia_real");
				saldo.tipoRelacion = rs.getInt("st_tipo_relacion");
				saldo.tipoIndustria = rs.getInt("st_tipo_industria");
				saldo.montoAprobado = roundNum(rs.getDouble("st_monto_aprobado"));
				saldo.noCuenta = rs.getString("st_referencia");
				saldo.nombreProducto = rs.getString("st_nombre_producto");
			}
		}
		catch(SQLException exc){
			Logger.debug("SQLException en saldoT24DAO.getSaldosT24ByContrato::" + exc);
			exc.printStackTrace();
		}
		catch(NamingException exc){
			Logger.debug("NamingException en saldoT24DAO.getSaldosT24ByContrato::" + exc);
			exc.printStackTrace();
		}
		catch(Exception exc){
			Logger.debug("Exception en saldoT24DAO.getSaldosT24ByContrato::" + exc);
			exc.printStackTrace();
		}
		finally{
			if(cn != null){
				try{
					cn.close();
				}
				catch(SQLException exc1){
					Logger.debug("SQLException en saldoT24DAO.getSaldosT24ByContrato::" + exc1);
					exc1.printStackTrace();
				}
			}
		}
		
		return saldo;
	}
	
	public void deleteSaldoT24(){
		Connection cn = null;
		String query = "DELETE FROM d_saldos_t24;";
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.executeUpdate();
		}
		catch(SQLException exc){
			Logger.debug("SQLException en saldoT24DAO.deleteSaldoT24()::" + exc);
			exc.printStackTrace();
		}
		catch(NamingException exc){
			Logger.debug("NamingException en saldoT24DAO.deleteSaldoT24()::" + exc);
			exc.printStackTrace();
		}
		finally{
			if(cn != null){
				try{
					cn.close();
				}
				catch(SQLException exc1){
					Logger.debug("SQLException en saldoT24DAO.deleteSaldoT24()::" + exc1);
					exc1.printStackTrace();
				}
			}
		}
	}

	
	public void insertSaldosT24(SaldoT24VO saldo){
		Connection cn = null;
		int param = 1;
		
		String query = "insert into d_saldos_t24 (st_numcliente,st_fecha_inf,st_fecha_generacion,";
		query +="st_nombre, st_direccion1, st_direccion2, st_telefono, st_numsucursal,st_nombre_sucursal,st_ciclo,st_codigo_linea,st_rfc,st_numero_cuotas,";
		query +="st_numero_cuotas_restantes,st_destino_credito,st_fecha_disposicion,st_fecha_vencimiento,";
		query +="st_responsabilidad_total,st_monto_linea_credito,st_saldo_insoluto,st_comision,st_iva_comision,";
		query +="st_desembolso,st_forma_amortizacion,st_monto_amortizacion,st_frecuencia_amortizacion,";
		query +="st_forma_pago_intereses,st_monto_intereses_por_cobrar,st_moneda,st_tasa_bruta,";
		query +="st_tasa_referencia,st_intereses_dev_no_cobrados,st_capital_vencido,st_intereses_vencidos,";
		query +="st_interes_moratorio,st_fee_vencidos,st_situacion_actual_credito,st_fecha_incumplimiento,";
		query +="st_fecha_entrada_a_cart_vencida,st_dias_vencidos,st_dias_transcurridos,st_numero_pagos_vencidos,";
		query +="st_multa,st_iva_multa,st_iva_moratorio,st_total_vencido, st_total_exigible, st_total_multa, st_total_moratorios," +
				"st_capital_pagado,st_interes_pagado,st_total_pagado,st_ejecutivo, st_interes_futuro, st_moratorio_pagado,";
		query +="st_dias_totales,st_monto_liquidacion_anticipada,st_fecha_proximo_vencimiento,st_interes_al_dia,st_fecha_sig_amort,";
		query+="st_fuente_fondeo,st_numoperacion,st_tipo_garantia_real,st_tipo_relacion,st_tipo_industria,st_monto_aprobado,st_referencia, st_nombre_producto) ";
		query +="values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setInt(param++, saldo.numCliente);
			ps.setDate(param++, saldo.fechaInf);
			ps.setDate(param++, saldo.fechaGeneracion);
			if(saldo.nombre==null)
				saldo.nombre = "";
			ps.setString(param++, saldo.nombre);
			if(saldo.direccion1 ==null)
				saldo.direccion1 = "";
			ps.setString(param++, saldo.direccion1);
			if(saldo.direccion2 == null)
				saldo.direccion2 = "";
			ps.setString(param++, saldo.direccion2);
			if(saldo.telefono == null)
				saldo.telefono = "";
			ps.setString(param++, saldo.telefono);
			ps.setInt(param++, saldo.numSucursal);
			if(saldo.nombreSucursal == null)
				saldo.nombreSucursal = "";
			ps.setString(param++, saldo.nombreSucursal);
			ps.setInt(param++, saldo.ciclo);
			ps.setString(param++, "");
			if(saldo.rfc == null)
				saldo.rfc = "";
			ps.setString(param++, saldo.rfc);
			//Logger.debug("RFC leido::" + saldo.rfc);
			ps.setInt(param++, saldo.numCuotas);
			ps.setInt(param++, saldo.numCuotasRestantes);
			ps.setInt(param++, saldo.destinoCredito);
			ps.setDate(param++, saldo.fechaDisposicion);
			ps.setDate(param++, saldo.fechaVencimiento);
			ps.setDouble(param++, saldo.responsabilidadTotal);
			ps.setDouble(param++, saldo.montoLineaCredito);
			ps.setDouble(param++, saldo.saldoInsoluto);
			ps.setDouble(param++, saldo.comision);
			ps.setDouble(param++, saldo.ivaComision);
			ps.setDouble(param++, saldo.desembolso);
			ps.setInt(param++, saldo.formaAmortizacion);
			ps.setDouble(param++, saldo.montoAmortizacion);
			if(saldo.frecuenciaAmortizacion == null)
				saldo.frecuenciaAmortizacion = "";
			ps.setString(param++, saldo.frecuenciaAmortizacion);
			ps.setInt(param++, saldo.formaPagoIntereses);
			ps.setDouble(param++, saldo.montoInteresesPorCobrar);
			ps.setInt(param++, saldo.moneda);
			ps.setDouble(param++, saldo.tasaBruta);
			ps.setDouble(param++, saldo.tasaReferencia);
			ps.setDouble(param++, saldo.interesesDevNoCobrados);
			ps.setDouble(param++, saldo.capitalVencido);
			ps.setDouble(param++, saldo.interesesVencidos);
			ps.setDouble(param++, saldo.interesMoratorio);
			ps.setDouble(param++, saldo.feeVencidos);
			ps.setInt(param++, saldo.situacionActualCredito);
			ps.setDate(param++, saldo.fechaIncumplimiento);
			ps.setDate(param++, saldo.fechaEntradaCartVencida);
			ps.setInt(param++, saldo.diasVencidos);
			ps.setInt(param++, saldo.diasTranscurridos);
			ps.setInt(param++, saldo.numeroPagosVencidos);
			ps.setDouble(param++, saldo.multa);
			ps.setDouble(param++, saldo.ivaMulta);
			ps.setDouble(param++, saldo.ivaMoratorio);
			ps.setDouble(param++, saldo.totalVencido);
			ps.setDouble(param++, saldo.totalExigible);
			ps.setDouble(param++, saldo.totalMulta);
			ps.setDouble(param++, saldo.totalMoratorios);
			ps.setDouble(param++, saldo.capitalPagado);
			ps.setDouble(param++, saldo.interesPagado);
			ps.setDouble(param++, saldo.totalPagado);
			if(saldo.ejecutivo == null)
				saldo.ejecutivo = "";
			ps.setString(param++, saldo.ejecutivo);
			ps.setDouble(param++, saldo.interesFuturo);
			ps.setDouble(param++, saldo.moratorioPagado);
			ps.setDouble(param++, saldo.diasTotales);
			ps.setDouble(param++, saldo.montoLiquidacionAnticipada);
			ps.setDate(param++, saldo.fechaProximoVencimiento);
			ps.setDouble(param++, saldo.interesAlDia);
			ps.setDate(param++, saldo.fechaSigAmortizacion);
			ps.setInt(param++, saldo.fuenteFondeo);
			ps.setInt(param++, saldo.numOperacion);
			ps.setInt(param++, saldo.tipoGtiaReal);
			ps.setInt(param++, saldo.tipoRelacion);
			ps.setInt(param++, saldo.tipoIndustria);
			ps.setDouble(param++, saldo.montoAprobado);
			ps.setString(param++, saldo.noCuenta);
			ps.setString(param++, saldo.nombreProducto);
//			Logger.debug("Ejecutando = " + query + " para cliente = " + saldo.numCliente);
			ps.executeUpdate();
		}
		catch(SQLException exc){
			Logger.debug("SQLException en saldoT24DAO.insertSaldosT24()::" + exc);
			exc.printStackTrace();
		}
		catch(NamingException exc){
			Logger.debug("NamingException en saldoT24DAO.insertSaldosT24()::" + exc);
			exc.printStackTrace();
		}
		finally{
			if(cn != null){
				try{
					cn.close();
				}
				catch(SQLException exc1){
					Logger.debug("SQLException en saldoT24DAO.insertSaldosT24()::" + exc1);
					exc1.printStackTrace();
				}
			}
		}		
	}


	public void insertSaldosT24(SaldoT24VO[] saldos){
		Connection cn = null;
		PreparedStatement ps = null;
		String query = "insert into d_saldos_t24 (st_numcliente,st_fecha_inf,st_fecha_generacion,";
		query +="st_nombre, st_direccion1, st_direccion2, st_telefono, st_numsucursal,st_nombre_sucursal,st_ciclo,st_codigo_linea,st_rfc,st_numero_cuotas,";
		query +="st_numero_cuotas_restantes,st_destino_credito,st_fecha_disposicion,st_fecha_vencimiento,";
		query +="st_responsabilidad_total,st_monto_linea_credito,st_saldo_insoluto,st_comision,st_iva_comision,";
		query +="st_desembolso,st_forma_amortizacion,st_monto_amortizacion,st_frecuencia_amortizacion,";
		query +="st_forma_pago_intereses,st_monto_intereses_por_cobrar,st_moneda,st_tasa_bruta,";
		query +="st_tasa_referencia,st_intereses_dev_no_cobrados,st_capital_vencido,st_intereses_vencidos,";
		query +="st_interes_moratorio,st_fee_vencidos,st_situacion_actual_credito,st_fecha_incumplimiento,";
		query +="st_fecha_entrada_a_cart_vencida,st_dias_vencidos,st_dias_transcurridos,st_numero_pagos_vencidos,";
		query +="st_multa,st_iva_multa,st_iva_moratorio,st_total_vencido, st_total_exigible, st_total_multa, st_total_moratorios," +
				"st_capital_pagado,st_interes_pagado,st_total_pagado,st_ejecutivo, st_interes_futuro, st_moratorio_pagado,";
		query +="st_dias_totales,st_monto_liquidacion_anticipada,st_fecha_proximo_vencimiento,st_interes_al_dia,st_fecha_sig_amort,";
		query+="st_fuente_fondeo,st_numoperacion,st_tipo_garantia_real,st_tipo_relacion,st_tipo_industria,st_monto_aprobado,st_referencia, st_nombre_producto) ";
		query +="values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
		try{
			cn = getConnection();
			Logger.debug("Numero de saldos a guardar::" + saldos.length);
			for(int i = 0; i < saldos.length; i++){
				int param = 1;
				ps = cn.prepareStatement(query);
				ps.setInt(param++, saldos[i].numCliente);
				ps.setDate(param++, saldos[i].fechaInf);
				ps.setDate(param++, saldos[i].fechaGeneracion);
				if(saldos[i].nombre==null)
					saldos[i].nombre = "";
				ps.setString(param++, saldos[i].nombre);
				if(saldos[i].direccion1 ==null)
					saldos[i].direccion1 = "";
				ps.setString(param++, saldos[i].direccion1);
				if(saldos[i].direccion2 == null)
					saldos[i].direccion2 = "";
				ps.setString(param++, saldos[i].direccion2);
				if(saldos[i].telefono == null)
					saldos[i].telefono = "";
				ps.setString(param++, saldos[i].telefono);
				ps.setInt(param++, saldos[i].numSucursal);
				if(saldos[i].nombreSucursal == null)
					saldos[i].nombreSucursal = "";
				ps.setString(param++, saldos[i].nombreSucursal);
				ps.setInt(param++, saldos[i].ciclo);
				ps.setString(param++, "");
				if(saldos[i].rfc == null)
					saldos[i].rfc = "";
				ps.setString(param++, saldos[i].rfc);
				ps.setInt(param++, saldos[i].numCuotas);
				ps.setInt(param++, saldos[i].numCuotasRestantes);
				ps.setInt(param++, saldos[i].destinoCredito);
				ps.setDate(param++, saldos[i].fechaDisposicion);
				ps.setDate(param++, saldos[i].fechaVencimiento);
				ps.setDouble(param++, saldos[i].responsabilidadTotal);
				ps.setDouble(param++, saldos[i].montoLineaCredito);
				ps.setDouble(param++, saldos[i].saldoInsoluto);
				ps.setDouble(param++, saldos[i].comision);
				ps.setDouble(param++, saldos[i].ivaComision);
				ps.setDouble(param++, saldos[i].desembolso);
				ps.setInt(param++, saldos[i].formaAmortizacion);
				ps.setDouble(param++, saldos[i].montoAmortizacion);
				if(saldos[i].frecuenciaAmortizacion == null)
					saldos[i].frecuenciaAmortizacion = "";
				ps.setString(param++, saldos[i].frecuenciaAmortizacion);
				ps.setInt(param++, saldos[i].formaPagoIntereses);
				ps.setDouble(param++, saldos[i].montoInteresesPorCobrar);
				ps.setInt(param++, saldos[i].moneda);
				ps.setDouble(param++, saldos[i].tasaBruta);
				ps.setDouble(param++, saldos[i].tasaReferencia);
				ps.setDouble(param++, saldos[i].interesesDevNoCobrados);
				ps.setDouble(param++, saldos[i].capitalVencido);
				ps.setDouble(param++, saldos[i].interesesVencidos);
				ps.setDouble(param++, saldos[i].interesMoratorio);
				ps.setDouble(param++, saldos[i].feeVencidos);
				ps.setInt(param++, saldos[i].situacionActualCredito);
				ps.setDate(param++, saldos[i].fechaIncumplimiento);
				ps.setDate(param++, saldos[i].fechaEntradaCartVencida);
				ps.setInt(param++, saldos[i].diasVencidos);
				ps.setInt(param++, saldos[i].diasTranscurridos);
				ps.setInt(param++, saldos[i].numeroPagosVencidos);
				ps.setDouble(param++, saldos[i].multa);
				ps.setDouble(param++, saldos[i].ivaMulta);
				ps.setDouble(param++, saldos[i].ivaMoratorio);
				ps.setDouble(param++, saldos[i].totalVencido);
				ps.setDouble(param++, saldos[i].totalExigible);
				ps.setDouble(param++, saldos[i].totalMulta);
				ps.setDouble(param++, saldos[i].totalMoratorios);
				ps.setDouble(param++, saldos[i].capitalPagado);
				ps.setDouble(param++, saldos[i].interesPagado);
				ps.setDouble(param++, saldos[i].totalPagado);
				if(saldos[i].ejecutivo == null)
					saldos[i].ejecutivo = "";
				ps.setString(param++, saldos[i].ejecutivo);
				ps.setDouble(param++, saldos[i].interesFuturo);
				ps.setDouble(param++, saldos[i].moratorioPagado);
				ps.setDouble(param++, saldos[i].diasTotales);
				ps.setDouble(param++, saldos[i].montoLiquidacionAnticipada);
				ps.setDate(param++, saldos[i].fechaProximoVencimiento);
				ps.setDouble(param++, saldos[i].interesAlDia);
				ps.setDate(param++, saldos[i].fechaSigAmortizacion);
				ps.setInt(param++, saldos[i].fuenteFondeo);
				ps.setInt(param++, saldos[i].numOperacion);
				ps.setInt(param++, saldos[i].tipoGtiaReal);
				ps.setInt(param++, saldos[i].tipoRelacion);
				ps.setInt(param++, saldos[i].tipoIndustria);
				ps.setDouble(param++, saldos[i].montoAprobado);
				ps.setString(param++, saldos[i].noCuenta);
				ps.setString(param++, saldos[i].nombreProducto);
				ps.executeUpdate();	
			}
		}
		catch(SQLException exc){
			Logger.debug("SQLException en saldoT24DAO.insertSaldosT24()::" + exc);
			exc.printStackTrace();
		}
		catch(NamingException exc){
			Logger.debug("NamingException en saldoT24DAO.insertSaldosT24()::" + exc);
			exc.printStackTrace();
		}
		finally{
			if(cn != null){
				try{
					cn.close();
				}
				catch(SQLException exc1){
					Logger.debug("SQLException en saldoT24DAO.insertSaldosT24()::" + exc1);
					exc1.printStackTrace();
				}
			}
		}		
	}


	public void updateSaldoT24(SaldoT24VO saldo, int ciclo, int numCliente){
		Connection cn = null;
		String query = "UPDATE d_saldos_t24 SET ";
		query += "st_capital_vencido = ?,";
		query += "st_comision = ?,";
		query += "st_ciclo = ?,";
		query += "st_desembolso = ?,";
		query += "st_destino_credito = ?,";
		query += "st_dias_vencidos = ?,";
		query += "st_direccion1 = ?,";
		query += "st_direccion2 = ?,";
		query += "st_fecha_disposicion = ?,";
		query += "st_fecha_entrada_a_cart_vencida = ?,";
		query += "st_fecha_generacion = ?,";
		query += "st_fecha_incumplimiento = ?,";
		query += "st_fecha_inf = ?,";
		query += "st_fecha_vencimiento = ?,";
		query += "st_fee_vencidos = ?,";
		query += "st_forma_amortizacion = ?,";
		query += "st_forma_pago_intereses = ?,";
		query += "st_frecuencia_amortizacion = ?,";
		query += "st_intereses_dev_no_cobrados = ?,";
		query += "st_intereses_vencidos = ?,";
		query += "st_interes_moratorio = ?,";
		query += "st_iva_comision = ?,";
		query += "st_iva_multa = ?,";
		query += "st_moneda = ?,";
		query += "st_monto_amortizacion = ?,";
		query += "st_monto_intereses_por_cobrar = ?,";
		query += "st_monto_linea_credito = ?,";
		query += "st_multa = ?,";
		query += "st_nombre = ?,";
		//query += "st_nombre_sucursal = ?,";
		query += "st_numcliente = ?,";
		query += "st_numero_cuotas = ?,";
		query += "st_numero_cuotas_restantes = ?,";
		query += "st_numero_pagos_vencidos = ?,";
		//query += "st_numsucursal = ?,";
		query += "st_responsabilidad_total = ?,";
		query += "st_rfc = ?,";
		query += "st_saldo_insoluto = ?,";
		query += "st_situacion_actual_credito = ?,";
		query += "st_tasa_bruta = ?,";
		query += "st_tasa_referencia = ?,";
		query += "st_telefono = ?,";
		query += "st_iva_moratorio = ?,";
		query += "st_total_vencido = ?,";
		query += "st_total_exigible = ?, ";
		query += "st_total_multa = ?,";
		query += "st_total_moratorios = ?,";
		query += "st_capital_pagado = ?,";
		query += "st_interes_pagado = ?,";
		query += "st_total_pagado = ?,";
		query += "st_ejecutivo = ?, ";
		query += "st_dias_totales = ?,";
		query += "st_interes_futuro = ?, ";
		query += "st_moratorio_pagado = ?, ";

		query += "st_dias_vencidos = ?,";
		query += "st_monto_liquidacion_anticipada = ?,";
		query += "st_fecha_proximo_vencimiento = ?, ";
		query += "st_interes_al_dia = ?, ";
		
		query += "st_fecha_sig_amort = ?, ";
		query += "st_fuente_fondeo = ?, ";
		query += "st_numoperacion = ?, ";
		query += "st_tipo_garantia_real = ?, ";
		query += "st_tipo_relacion = ?, ";
		query += "st_tipo_industria = ?, ";
		query += "st_monto_aprobado = ?, ";
		query += "st_referencia = ?, ";
		query += "st_nombre_producto = ? ";
		//saldo.nombreProducto = rs.getString("st_nombre_producto");
		
		query += "WHERE st_ciclo = ? AND st_numcliente = ? AND st_referencia = ?;";
		try{
			int param = 1;
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			
			ps.setDouble(param++, saldo.capitalVencido);
			ps.setDouble(param++, saldo.comision);
			ps.setInt(param++, saldo.ciclo);
			ps.setDouble(param++, saldo.desembolso);
			ps.setInt(param++, saldo.destinoCredito);
			ps.setInt(param++, saldo.diasVencidos);
			ps.setString(param++, saldo.direccion1);
			ps.setString(param++, saldo.direccion2);
			ps.setDate(param++, saldo.fechaDisposicion);
			ps.setDate(param++, saldo.fechaEntradaCartVencida);
			ps.setDate(param++, saldo.fechaGeneracion);
			ps.setDate(param++, saldo.fechaIncumplimiento);
			ps.setDate(param++, saldo.fechaInf);
			ps.setDate(param++, saldo.fechaVencimiento);
			ps.setDouble(param++, saldo.feeVencidos);
			ps.setInt(param++, saldo.formaAmortizacion);
			ps.setInt(param++, saldo.formaPagoIntereses);
			ps.setString(param++, saldo.frecuenciaAmortizacion);
			ps.setDouble(param++, saldo.interesesDevNoCobrados);
			ps.setDouble(param++, saldo.interesesVencidos);
			ps.setDouble(param++, saldo.interesMoratorio);
			ps.setDouble(param++, saldo.ivaComision);
			ps.setDouble(param++, saldo.ivaMulta);
			ps.setInt(param++, saldo.moneda);
			ps.setDouble(param++, saldo.montoAmortizacion);
			ps.setDouble(param++, saldo.montoInteresesPorCobrar);
			ps.setDouble(param++, saldo.montoLineaCredito);
			ps.setDouble(param++, saldo.multa);
			ps.setString(param++, saldo.nombre);
			//ps.setString(param++, saldo.nombreSucursal);
			ps.setInt(param++, saldo.numCliente);
			ps.setInt(param++, saldo.numCuotas);
			ps.setInt(param++, saldo.numCuotasRestantes);
			ps.setInt(param++, saldo.numeroPagosVencidos);
			//ps.setInt(param++, saldo.numSucursal);
			ps.setDouble(param++, saldo.responsabilidadTotal);
			ps.setString(param++, saldo.rfc);
			ps.setDouble(param++, saldo.saldoInsoluto);
			ps.setInt(param++, saldo.situacionActualCredito);
			ps.setDouble(param++, saldo.tasaBruta);
			ps.setDouble(param++, saldo.tasaReferencia);
			ps.setString(param++, saldo.telefono);
			ps.setDouble(param++, saldo.ivaMoratorio);
			ps.setDouble(param++, saldo.totalVencido);
			ps.setDouble(param++, saldo.totalExigible);
			ps.setDouble(param++, saldo.totalMulta);
			ps.setDouble(param++, saldo.totalMoratorios);
			ps.setDouble(param++, saldo.capitalPagado);
			ps.setDouble(param++, saldo.interesPagado);
			ps.setDouble(param++, saldo.totalPagado);
			ps.setString(param++, saldo.ejecutivo);
			ps.setInt(param++, saldo.diasTotales);
			ps.setDouble(param++, saldo.interesFuturo);
			ps.setDouble(param++, saldo.moratorioPagado);
			ps.setDouble(param++, saldo.diasVencidos);
			ps.setDouble(param++, saldo.montoLiquidacionAnticipada);
			ps.setDate(param++, saldo.fechaProximoVencimiento);
			ps.setDouble(param++, saldo.interesAlDia);
			
			ps.setDate(param++, saldo.fechaSigAmortizacion);
			ps.setInt(param++, saldo.fuenteFondeo);
			ps.setInt(param++, saldo.numOperacion);
			ps.setInt(param++, saldo.tipoGtiaReal);
			ps.setInt(param++, saldo.tipoRelacion);
			ps.setInt(param++, saldo.tipoIndustria);
			ps.setDouble(param++, saldo.montoAprobado);
			ps.setString(param++, saldo.noCuenta);
			ps.setString(param++, saldo.nombreProducto);
			
			ps.setInt(param++, ciclo);
			ps.setInt(param++, numCliente);
			ps.setString(param++, saldo.noCuenta);
			Logger.debug("Ejecutando::" + query);
			ps.executeUpdate();
		}
		catch(SQLException exc){
			Logger.debug("SQLException en saldoT24DAO.updateSaldoT24()::" + exc);
			exc.printStackTrace();
		}
		catch(NamingException exc){
			Logger.debug("NamingException en saldoT24DAO.updateSaldoT24()::" + exc);
			exc.printStackTrace();
		}
		finally{
			if(cn != null){
				try{
					cn.close();
				}
				catch(SQLException exc1){
					Logger.debug("SQLException en saldoT24DAO.updateSaldoT24()::" + exc1);
					exc1.printStackTrace();
				}
			}
		}
	}


	private void actualizaEjecutivoSucursalOperacion(){
		String query = "UPDATE D_SALDOS_T24, D_SOLICITUDES, C_EJECUTIVOS, D_CLIENTES, C_SUCURSALES, C_OPERACIONES ";
		//query += "SET ST_EJECUTIVO = CONCAT(COALESCE(EJ_NOMBRE,'') ,' ' ,COALESCE(EJ_APATERNO,'') ,' ',COALESCE(EJ_AMATERNO,'')), ";
		query += "SET ST_NOMBRE_SUCURSAL = SU_NOMBRE, ST_NUMSUCURSAL = SU_NUMSUCURSAL, ST_NOMBRE_PRODUCTO = OP_NOMBRE ";
		query += "WHERE ST_CICLO = SO_NUMSOLICITUD AND ST_NUMCLIENTE = SO_NUMCLIENTE AND SO_NUMCLIENTE = EN_NUMCLIENTE AND EJ_NUMSUCURSAL = SU_NUMSUCURSAL AND EJ_NUMEJECUTIVO = SO_NUMEJECUTIVO ";
		query += "AND OP_NUMOPERACION = ST_NUMOPERACION AND ST_NUMOPERACION NOT IN (3, 5)";
		
		Connection con = null;
		try{
			con = getConnection();
			PreparedStatement ps = con.prepareStatement(query);
			ps.executeUpdate();
		}
		catch(SQLException exc){
			Logger.debug("SQLException en saldoT24DAO.actualizaEjecutivo()::" + exc);
			exc.printStackTrace();
		}
		catch(NamingException exc){
			Logger.debug("NamingException en saldoT24DAO.actualizaEjecutivo()::" + exc);
			exc.printStackTrace();
		}
		finally{
			if(con!=null){
				try{
					con.close();
				}
				catch(SQLException exc){
					Logger.debug("SQLException en saldoT24DAO.actualizaEjecutivoSucursalOperacion()::" + exc);
					exc.printStackTrace();
				}
			}
		}
	}


	private void actualizaEjecutivoSucursalOperacionGrupal(){
		String query = "UPDATE D_SALDOS_T24, D_CICLOS_GRUPALES, C_EJECUTIVOS, D_GRUPOS, C_SUCURSALES, C_OPERACIONES ";
		//query += "SET ST_EJECUTIVO = CONCAT(COALESCE(EJ_NOMBRE,'') ,' ' ,COALESCE(EJ_APATERNO,'') ,' ',COALESCE(EJ_AMATERNO,'')), ";
		query += "SET ST_NOMBRE_SUCURSAL = SU_NOMBRE, ST_NUMSUCURSAL = SU_NUMSUCURSAL, ST_NOMBRE_PRODUCTO = OP_NOMBRE ";
		query += "WHERE ST_CICLO = CI_NUMCICLO ";
		query += "AND CI_NUMGRUPO = GR_NUMGRUPO AND ST_NUMCLIENTE = CI_NUMGRUPO AND EJ_NUMSUCURSAL = SU_NUMSUCURSAL ";
		query += "AND EJ_NUMEJECUTIVO = CI_EJECUTIVO AND ST_NUMOPERACION = OP_NUMOPERACION AND ST_NUMOPERACION IN (3, 5)";
		
		Connection con = null;
		try{
			con = getConnection();
			PreparedStatement ps = con.prepareStatement(query);
			ps.executeUpdate();
		}
		catch(SQLException exc){
			Logger.debug("SQLException en saldoT24DAO.actualizaEjecutivo()::" + exc);
			exc.printStackTrace();
		}
		catch(NamingException exc){
			Logger.debug("NamingException en saldoT24DAO.actualizaEjecutivo()::" + exc);
			exc.printStackTrace();
		}
		finally{
			if(con!=null){
				try{
					con.close();
				}
				catch(SQLException exc){
					Logger.debug("SQLException en saldoT24DAO.actualizaEjecutivoSucursalOperacion()::" + exc);
					exc.printStackTrace();
				}
			}
		}
	}


	private void actualizaDiasTotales(){
		String query = "update d_saldos_t24 ";
		query += "set st_dias_totales = DATEDIFF";
		query += "(st_fecha_vencimiento,st_fecha_disposicion)";
		
		Connection con = null;
		try{
			con = getConnection();
			PreparedStatement ps = con.prepareStatement(query);
			ps.executeUpdate();
		}
		catch(SQLException exc){
			Logger.debug("SQLException en saldoT24DAO.actualizaDiasTotales()::" + exc);
			exc.printStackTrace();
		}
		catch(NamingException exc){
			Logger.debug("NamingException en saldoT24DAO.actualizaDiasTotales()::" + exc);
			exc.printStackTrace();
		}
		finally{
			if(con!=null){
				try{
					con.close();
				}
				catch(SQLException exc){
					Logger.debug("SQLException en saldoT24DAO.actualizaDiasTotales()::" + exc);
					exc.printStackTrace();
				}
			}
		}
	}


	private void actualizaInteresAlDia(){
		String query = "update d_saldos_t24 ";
		query += "set st_interes_al_dia = (((st_tasa_bruta/360)*st_monto_linea_credito)/100 *round(mod(st_dias_transcurridos,(st_dias_totales/st_numero_cuotas))))*1.15";
		
		Connection con = null;
		try{
			con = getConnection();
			PreparedStatement ps = con.prepareStatement(query);
			ps.executeUpdate();
		}
		catch(SQLException exc){
			Logger.debug("SQLException en saldoT24DAO.actualizaInteresAlDia()::" + exc);
			exc.printStackTrace();
		}
		catch(NamingException exc){
			Logger.debug("NamingException en saldoT24DAO.actualizaInteresAlDia()::" + exc);
			exc.printStackTrace();
		}
		finally{
			if(con!=null){
				try{
					con.close();
				}
				catch(SQLException exc){
					Logger.debug("SQLException en saldoT24DAO.actualizaInteresAlDia()::" + exc);
					exc.printStackTrace();
				}
			}
		}
	}


	private void actualizaDemograficosIndividual(){
		String query = "UPDATE D_SALDOS_T24, D_TELEFONOS,D_DIRECCIONES, D_SOLICITUDES,C_COLONIAS ";
		query += "SET ST_DIRECCION1 = CONCAT(COALESCE(DI_CALLE,''), ' ', COALESCE(DI_NUMERO_EXT,''), ' ', COALESCE(DI_NUMERO_INT,'')),";
		query += "ST_DIRECCION2 = COALESCE(CO_NOMBRE_COLONIA,''),ST_TELEFONO = TE_TELEFONO ";
		query += "WHERE ST_NUMCLIENTE = TE_NUMCLIENTE AND TE_NUMCLIENTE = SO_NUMCLIENTE AND SO_NUMCLIENTE = DI_NUMCLIENTE ";
		query += "AND DI_NUMCLIENTE = ST_NUMCLIENTE AND CO_NUMCOLONIA = DI_NUMCOLONIA AND TE_NUMDIRECCION = DI_NUMDIRECCION ";
		query += "AND TE_TIPOTELEFONO = 1 AND ST_NUMOPERACION NOT IN (3, 5) AND DI_TABLA = 'D_CLIENTES' AND ST_CICLO = SO_NUMSOLICITUD";
		
		Connection cn = null;
		
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.executeUpdate();
		}
		catch(SQLException exc){
			Logger.debug("SQLException en saldoT24DAO.actualizaFechaInf()::" + exc);
			exc.printStackTrace();
		}
		catch(NamingException exc){
			Logger.debug("NamingException en saldoT24DAO.actualizaFechaInf()::" + exc);
			exc.printStackTrace();
		}
		finally{
			try{
				if(cn!=null)
					cn.close();
			}
			catch(SQLException exc){
				Logger.debug("SQLException en saldoT24DAO.actualizaFechaInf()::" + exc);
				exc.printStackTrace();
			}
		}
	}


	private void actualizaDemograficosGrupal(){
		String query = "UPDATE D_CICLOS_GRUPALES,D_DIRECCIONES_GENERICAS,D_GRUPOS,D_SALDOS_T24,C_COLONIAS ";
		query += "SET ST_DIRECCION1 = CONCAT(COALESCE(DG_CALLE,''), ' ', COALESCE(DG_NUMERO_EXT,''), ' ', COALESCE(DG_NUMERO_INT,'')),";
		query += "ST_DIRECCION2 = COALESCE(CO_NOMBRE_COLONIA,'') ";
		query += "WHERE GR_NUMGRUPO = ST_NUMCLIENTE AND CI_NUMGRUPO = GR_NUMGRUPO AND ST_CICLO = CI_NUMCICLO ";
		query += "AND CI_NUMDIRECCION_REUNION = DG_NUMDIRECCION AND ST_NUMOPERACION IN (3, 5) ";
		query += "AND DG_NUMCOLONIA = CO_NUMCOLONIA";
		
		Connection cn = null;
		
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.executeUpdate();
		}
		catch(NamingException exc){
			exc.printStackTrace();
		}
		catch(SQLException exc){
			exc.printStackTrace();
		}
		finally{
			try{
				if(cn!=null)
					cn.close();
			}
			catch(SQLException exc){
				exc.printStackTrace();
			}
		}
	}



	private void actualizaTelefonosGrupal(){
		String query = "UPDATE D_SALDOS_T24, D_GRUPOS, D_CICLOS_GRUPALES, D_INTEGRANTES_CICLO, D_TELEFONOS ";
		query += "SET ST_TELEFONO = TE_TELEFONO ";
		query += "WHERE ST_NUMCLIENTE = GR_NUMGRUPO ";
		query += "AND ST_CICLO = CI_NUMCICLO AND CI_NUMGRUPO = GR_NUMGRUPO ";
		query += "AND IC_NUMGRUPO = ST_NUMCLIENTE AND IC_NUMCICLO = ST_CICLO ";
		query += "AND IC_ROL = 1 AND TE_NUMCLIENTE = IC_NUMCLIENTE AND TE_TIPOTELEFONO = 1";
		
		Connection cn = null;
		
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.executeUpdate();
		}
		catch(NamingException exc){
			exc.printStackTrace();
		}
		catch(SQLException exc){
			exc.printStackTrace();
		}
		finally{
			try{
				if(cn!=null)
					cn.close();
			}
			catch(SQLException exc){
				exc.printStackTrace();
			}
		}
	}
	
	public void actualizaTotales(){
		try{
			this.actualizaDiasTotales();
			this.actualizaEjecutivoSucursalOperacion();
			this.actualizaEjecutivoSucursalOperacionGrupal();
			this.actualizaInteresAlDia();
			this.actualizaDemograficosIndividual();
			this.actualizaDemograficosGrupal();
			this.actualizaTelefonosGrupal();
			this.actualizaExigibleMoratoriosMulta();
			Logger.debug("			¡¡¡¡********Termina de actualizar totales********!!!!");
		}catch(Exception exc){
			exc.printStackTrace();
		}
	}
	
	public SaldoT24VO[] getSaldosT24ByRFC(String rfc){
		Logger.debug("$$$$$$____RFC recibido::" + rfc);
		Connection cn = null;
		int param = 1;
		ArrayList<SaldoT24VO> arrayListSaldos = new ArrayList<SaldoT24VO>();
		SaldoT24VO[] arraySaldo = null;
		String query = "SELECT * FROM d_saldos_t24 WHERE st_rfc LIKE ?;";
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setString(param++, rfc);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				SaldoT24VO saldo = new SaldoT24VO();
				saldo.capitalVencido = roundNum(rs.getDouble("st_capital_vencido"));
				saldo.comision = roundNum(rs.getDouble("st_comision"));
				saldo.ciclo = rs.getInt("st_ciclo");
				saldo.desembolso = roundNum(rs.getDouble("st_desembolso"));
				saldo.destinoCredito = rs.getInt("st_destino_credito");
				saldo.diasVencidos = rs.getInt("st_dias_vencidos");
				saldo.direccion1 = rs.getString("st_direccion1");
				saldo.direccion2 = rs.getString("st_direccion2");
				saldo.fechaDisposicion = rs.getDate("st_fecha_disposicion");
				saldo.fechaEntradaCartVencida = rs.getDate("st_fecha_entrada_a_cart_vencida");
				saldo.fechaGeneracion = rs.getDate("st_fecha_generacion");
				saldo.fechaIncumplimiento = rs.getDate("st_fecha_incumplimiento");
				saldo.fechaInf = rs.getDate("st_fecha_inf");
				saldo.fechaVencimiento = rs.getDate("st_fecha_vencimiento");
				saldo.feeVencidos = roundNum(rs.getDouble("st_fee_vencidos"));
				saldo.formaAmortizacion = rs.getInt("st_forma_amortizacion");
				saldo.formaPagoIntereses = rs.getInt("st_forma_pago_intereses");
				saldo.frecuenciaAmortizacion = rs.getString("st_frecuencia_amortizacion");
				saldo.interesesDevNoCobrados = roundNum(rs.getDouble("st_intereses_dev_no_cobrados"));
				saldo.interesesVencidos = roundNum(rs.getDouble("st_intereses_vencidos"));
				saldo.interesMoratorio = roundNum(rs.getDouble("st_interes_moratorio"));
				saldo.ivaComision = roundNum(rs.getDouble("st_iva_comision"));
				saldo.ivaMulta = roundNum(rs.getDouble("st_iva_multa"));
				saldo.moneda = rs.getInt("st_moneda");
				saldo.montoAmortizacion = roundNum(rs.getDouble("st_monto_amortizacion"));
				saldo.montoInteresesPorCobrar = roundNum(rs.getDouble("st_monto_intereses_por_cobrar"));
				saldo.montoLineaCredito = roundNum(rs.getDouble("st_monto_linea_credito"));
				saldo.multa = roundNum(rs.getDouble("st_multa"));
				saldo.nombre = rs.getString("st_nombre");
				saldo.nombreSucursal = rs.getString("st_nombre_sucursal");
				saldo.numCliente = rs.getInt("st_numcliente");
				saldo.numCuotas = rs.getInt("st_numero_cuotas");
				saldo.numCuotasRestantes = rs.getInt("st_numero_cuotas_restantes");
				saldo.numeroPagosVencidos = rs.getInt("st_numero_pagos_vencidos");
				saldo.numSucursal = rs.getInt("st_numsucursal");
				saldo.responsabilidadTotal = roundNum(rs.getDouble("st_responsabilidad_total"));
				saldo.rfc = rs.getString("st_rfc");
				saldo.saldoInsoluto = roundNum(rs.getDouble("st_saldo_insoluto"));
				saldo.situacionActualCredito = rs.getInt("st_situacion_actual_credito");
				saldo.tasaBruta = roundNum(rs.getDouble("st_tasa_bruta"));
				saldo.tasaReferencia = roundNum(rs.getDouble("st_tasa_referencia"));
				saldo.telefono = rs.getString("st_telefono");
				saldo.ivaMoratorio = roundNum(rs.getDouble("st_iva_moratorio"));
				saldo.totalVencido = roundNum(rs.getDouble("st_total_vencido"));
				saldo.totalExigible = roundNum(rs.getDouble("st_total_exigible"));
				saldo.capitalPagado = roundNum(rs.getDouble("st_capital_pagado"));
				saldo.interesPagado = roundNum(rs.getDouble("st_interes_pagado"));
				saldo.totalPagado = roundNum(rs.getDouble("st_total_pagado"));
				saldo.ejecutivo = rs.getString("st_ejecutivo");
				saldo.diasTotales = rs.getInt("st_dias_totales");
				saldo.interesFuturo = roundNum(rs.getDouble("st_interes_futuro"));
				saldo.moratorioPagado = roundNum(rs.getDouble("st_moratorio_pagado"));
				saldo.montoLiquidacionAnticipada = roundNum(rs.getDouble("st_monto_liquidacion_anticipada"));
				saldo.fechaProximoVencimiento = rs.getDate("st_fecha_proximo_vencimiento");
				saldo.interesAlDia = roundNum(rs.getDouble("st_interes_al_dia"));
				saldo.diasTranscurridos = rs.getInt("st_dias_transcurridos");
				saldo.totalMulta = roundNum(rs.getDouble("st_total_multa"));
				saldo.totalMoratorios = roundNum(rs.getDouble("st_total_moratorios"));
				saldo.fechaSigAmortizacion = rs.getDate("st_fecha_sig_amort");
				saldo.fuenteFondeo = rs.getInt("st_fuente_fondeo");
				saldo.numOperacion = rs.getInt("st_numoperacion");
				saldo.tipoGtiaReal = rs.getInt("st_tipo_garantia_real");
				saldo.tipoRelacion = rs.getInt("st_tipo_relacion");
				saldo.tipoIndustria = rs.getInt("st_tipo_industria");
				saldo.noCuenta = rs.getString("st_referencia");
				saldo.nombreProducto = rs.getString("st_nombre_producto");
				
				arrayListSaldos.add(saldo);
			}
		}
		catch(SQLException exc){
			Logger.debug("SQLException en saldoT24DAO.getSaldosT24ByRFC()::" + exc);
			exc.printStackTrace();
		}
		catch(NamingException exc){
			Logger.debug("NamingException en saldoT24DAO.getSaldosT24ByRFC()::" + exc);
			exc.printStackTrace();
		}
		catch(Exception exc){
			Logger.debug("Exception en saldoT24DAO.getSaldosT24ByRFC()::" + exc);
			exc.printStackTrace();
		}
		finally{
			if(cn != null){
				try{
					cn.close();
				}
				catch(SQLException exc1){
					Logger.debug("SQLException en saldoT24DAO.getSaldosT24ByRFC()::" + exc1);
					exc1.printStackTrace();
				}
			}
		}
		arraySaldo = new SaldoT24VO[ arrayListSaldos.size()];
		for(int i = 0; i < arraySaldo.length; i++){
			arraySaldo[i] = arrayListSaldos.get(i);
		}
		return arraySaldo;
	}
	
	public SaldoT24VO[] getSaldosBySucursal(int idSucursal){
		Connection cn = null;
		ArrayList<SaldoT24VO> arrayListSaldos = new ArrayList<SaldoT24VO>();
		SaldoT24VO[] arraySaldo = null;
		String query = "SELECT ST_NUMCLIENTE, ST_FECHA_INF, ST_FECHA_GENERACION, ST_NOMBRE, ST_NOMBRE_SUCURSAL, ST_CICLO, " +
					   "ST_CODIGO_LINEA, ST_RFC, ST_NUMERO_CUOTAS, ST_NUMERO_CUOTAS_RESTANTES, ST_DESTINO_CREDITO, ST_FECHA_DISPOSICION, " +
					   "ST_FECHA_VENCIMIENTO, ST_RESPONSABILIDAD_TOTAL, ST_MONTO_LINEA_CREDITO, ST_SALDO_INSOLUTO, ST_COMISION, ST_IVA_COMISION, " +
					   "ST_DESEMBOLSO, ST_FORMA_AMORTIZACION, ST_MONTO_AMORTIZACION, ST_FRECUENCIA_AMORTIZACION, ST_FORMA_PAGO_INTERESES, " +
					   "ST_MONTO_INTERESES_POR_COBRAR, ST_MONEDA, ST_TASA_BRUTA, ST_TASA_REFERENCIA, ST_INTERESES_DEV_NO_COBRADOS, " +
					   "ST_CAPITAL_VENCIDO, ST_INTERESES_VENCIDOS, ST_INTERES_MORATORIO, ST_FEE_VENCIDOS, ST_SITUACION_ACTUAL_CREDITO, " +
					   "ST_FECHA_INCUMPLIMIENTO, ST_FECHA_ENTRADA_A_CART_VENCIDA, ST_DIAS_VENCIDOS, ST_DIAS_TRANSCURRIDOS, " +
					   "ST_NUMERO_PAGOS_VENCIDOS, ST_MULTA, ST_IVA_MULTA, ST_NUMSUCURSAL, ST_DIRECCION1, ST_TELEFONO, ST_IVA_MORATORIO, " +
					   "ST_TOTAL_VENCIDO, ST_TOTAL_EXIGIBLE, ST_TOTAL_MULTA, ST_TOTAL_MORATORIOS, ST_DIRECCION2, ST_CAPITAL_PAGADO, " +
					   "ST_INTERES_PAGADO, ST_TOTAL_PAGADO, ST_EJECUTIVO, ST_DIAS_TOTALES, ST_INTERES_FUTURO, ST_MORATORIO_PAGADO, " +
					   "ST_MONTO_LIQUIDACION_ANTICIPADA, ST_FECHA_PROXIMO_VENCIMIENTO, ST_INTERES_AL_DIA, ST_FECHA_SIG_AMORT, ST_FUENTE_FONDEO, " +
					   "ST_NUMOPERACION, ST_TIPO_GARANTIA_REAL, ST_TIPO_RELACION, ST_TIPO_INDUSTRIA, ST_MONTO_APROBADO, ST_REFERENCIA, ST_NOMBRE_PRODUCTO " +
					   "FROM D_SALDOS_T24 WHERE ST_NUMSUCURSAL = ?";
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			int param = 1;
			ps.setInt(param++, idSucursal);
			Logger.debug("Ejecutando : " + query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				SaldoT24VO saldo = new SaldoT24VO();
				saldo.capitalVencido = roundNum(rs.getDouble("st_capital_vencido"));
				saldo.comision = roundNum(rs.getDouble("st_comision"));
				saldo.ciclo = rs.getInt("st_ciclo");
				saldo.desembolso = roundNum(rs.getDouble("st_desembolso"));
				saldo.destinoCredito = rs.getInt("st_destino_credito");
				saldo.diasVencidos = rs.getInt("st_dias_vencidos");
				saldo.direccion1 = rs.getString("st_direccion1");
				saldo.direccion2 = rs.getString("st_direccion2");
				saldo.fechaDisposicion = rs.getDate("st_fecha_disposicion");
				saldo.fechaEntradaCartVencida = rs.getDate("st_fecha_entrada_a_cart_vencida");
				saldo.fechaGeneracion = rs.getDate("st_fecha_generacion");
				saldo.fechaIncumplimiento = rs.getDate("st_fecha_incumplimiento");
				saldo.fechaInf = rs.getDate("st_fecha_inf");
				saldo.fechaVencimiento = rs.getDate("st_fecha_vencimiento");
				saldo.feeVencidos = roundNum(rs.getDouble("st_fee_vencidos"));
				saldo.formaAmortizacion = rs.getInt("st_forma_amortizacion");
				saldo.formaPagoIntereses = rs.getInt("st_forma_pago_intereses");
				saldo.frecuenciaAmortizacion = rs.getString("st_frecuencia_amortizacion");
				saldo.interesesDevNoCobrados = roundNum(rs.getDouble("st_intereses_dev_no_cobrados"));
				saldo.interesesVencidos = roundNum(rs.getDouble("st_intereses_vencidos"));
				saldo.interesMoratorio = roundNum(rs.getDouble("st_interes_moratorio"));
				saldo.ivaComision = roundNum(rs.getDouble("st_iva_comision"));
				saldo.ivaMulta = roundNum(rs.getDouble("st_iva_multa"));
				saldo.moneda = rs.getInt("st_moneda");
				saldo.montoAmortizacion = roundNum(rs.getDouble("st_monto_amortizacion"));
				saldo.montoInteresesPorCobrar = roundNum(rs.getDouble("st_monto_intereses_por_cobrar"));
				saldo.montoLineaCredito = roundNum(rs.getDouble("st_monto_linea_credito"));
				saldo.multa = roundNum(rs.getDouble("st_multa"));
				saldo.nombre = rs.getString("st_nombre");
				saldo.nombreSucursal = rs.getString("st_nombre_sucursal");
				saldo.numCliente = rs.getInt("st_numcliente");
				saldo.numCuotas = rs.getInt("st_numero_cuotas");
				saldo.numCuotasRestantes = rs.getInt("st_numero_cuotas_restantes");
				saldo.numeroPagosVencidos = rs.getInt("st_numero_pagos_vencidos");
				saldo.numSucursal = rs.getInt("st_numsucursal");
				saldo.responsabilidadTotal = roundNum(rs.getDouble("st_responsabilidad_total"));
				saldo.rfc = rs.getString("st_rfc");
				saldo.saldoInsoluto = roundNum(rs.getDouble("st_saldo_insoluto"));
				saldo.situacionActualCredito = rs.getInt("st_situacion_actual_credito");
				saldo.tasaBruta = roundNum(rs.getDouble("st_tasa_bruta"));
				saldo.tasaReferencia = roundNum(rs.getDouble("st_tasa_referencia"));
				saldo.telefono = rs.getString("st_telefono");
				saldo.ivaMoratorio = roundNum(rs.getDouble("st_iva_moratorio"));
				saldo.totalVencido = roundNum(rs.getDouble("st_total_vencido"));
				saldo.totalExigible = roundNum(rs.getDouble("st_total_exigible"));
				saldo.capitalPagado = roundNum(rs.getDouble("st_capital_pagado"));
				saldo.interesPagado = roundNum(rs.getDouble("st_interes_pagado"));
				saldo.totalPagado = roundNum(rs.getDouble("st_total_pagado"));
				saldo.ejecutivo = rs.getString("st_ejecutivo");
				saldo.diasTotales = rs.getInt("st_dias_totales");
				saldo.interesFuturo = roundNum(rs.getDouble("st_interes_futuro"));
				saldo.moratorioPagado = roundNum(rs.getDouble("st_moratorio_pagado"));
				saldo.montoLiquidacionAnticipada = roundNum(rs.getDouble("st_monto_liquidacion_anticipada"));
				saldo.fechaProximoVencimiento = rs.getDate("st_fecha_proximo_vencimiento");
				saldo.interesAlDia = roundNum(rs.getDouble("st_interes_al_dia"));
				saldo.diasTranscurridos = rs.getInt("st_dias_transcurridos");
				saldo.totalMulta = roundNum(rs.getDouble("st_total_multa"));
				saldo.totalMoratorios = roundNum(rs.getDouble("st_total_moratorios"));
				saldo.fechaSigAmortizacion = rs.getDate("st_fecha_sig_amort");
				saldo.fuenteFondeo = rs.getInt("st_fuente_fondeo");
				saldo.numOperacion = rs.getInt("st_numoperacion");
				saldo.tipoGtiaReal = rs.getInt("st_tipo_garantia_real");
				saldo.tipoRelacion = rs.getInt("st_tipo_relacion");
				saldo.tipoIndustria = rs.getInt("st_tipo_industria");
				saldo.montoAprobado = roundNum(rs.getDouble("st_monto_aprobado"));
				saldo.noCuenta = rs.getString("st_referencia");
				saldo.nombreProducto = rs.getString("st_nombre_producto");
				
				arrayListSaldos.add(saldo);
			}
		}
		catch(SQLException exc){
			Logger.debug("SQLException en saldoT24DAO.getSaldosBySucursal(): " + exc);
			exc.printStackTrace();
		}
		catch(NamingException exc){
			Logger.debug("NamingException en saldoT24DAO.getSaldosBySucursal(): " + exc);
		}
		catch(Exception exc){
			Logger.debug("Exception caugth: " + exc);
		}
		finally{
			if(cn != null){
				try{
					cn.close();
				}
				catch(SQLException exc1){
					Logger.debug("SQLException en saldoT24DAO.getSaldosBySucursal(): " + exc1);
				}
			}
		}
		arraySaldo = new SaldoT24VO[ arrayListSaldos.size()];
		for(int i = 0; i < arraySaldo.length; i++){
			arraySaldo[i] = arrayListSaldos.get(i);
		}
		return arraySaldo;
	}
	
	private void actualizaExigibleMoratoriosMulta() throws ClientesException{
		String query = "UPDATE D_SALDOS_T24 " +
					   "SET ST_TOTAL_EXIGIBLE = (ST_CAPITAL_VENCIDO + ST_FEE_VENCIDOS + ST_MULTA + ST_IVA_MULTA + ST_INTERES_MORATORIO + ST_IVA_MORATORIO)," +
					   "ST_TOTAL_MORATORIOS = (ST_INTERES_MORATORIO + ST_IVA_MORATORIO)," +
					   "ST_TOTAL_MULTA = (ST_MULTA + ST_IVA_MULTA)";
		Connection cn = null;
		try{
			PreparedStatement ps =null;
			cn = getConnection();
			ps = cn.prepareStatement(query); 
			Logger.debug("Ejecutando = "+query);
			ps.executeUpdate();
		}
		catch(SQLException sqle) {
			Logger.debug("SQLException en actualizaExigibleMoratoriosMulta : "+sqle.getMessage());
			sqle.printStackTrace();
			throw new ClientesDBException(sqle.getMessage());
		}
		catch(Exception e) {
			Logger.debug("Excepcion en actualizaExigibleMoratoriosMulta : "+e.getMessage());
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
	}
	
	public SaldoT24VO[] getVSaldosT24(int tipoOper){
		Connection cn = null;
		ArrayList<SaldoT24VO> arrayListSaldos = new ArrayList<SaldoT24VO>();
		SaldoT24VO[] arraySaldo = null;
		String query = "";
		
		if ( tipoOper == 1 ){
			query = "SELECT * FROM D_SALDOS_T24 WHERE ST_NUMOPERACION NOT IN (3,5) " +
					"AND ST_REFERENCIA NOT IN (SELECT BB_REFERENCIA FROM D_BITACORA_BUROCIRCULO WHERE BB_ESTATUS=2);";
		}
		else if ( tipoOper == 2 ){
			//query = "SELECT * FROM D_SALDOS_T24 WHERE ST_NUMOPERACION=3 AND ST_FECHA_DISPOSICION BETWEEN '2008-12-01' AND '2008-12-31' " +
			//Incrementar por dos meses cada mes en el between hasta el mes de diciembre del año 2008
			query = "SELECT * FROM D_SALDOS_T24 WHERE ST_NUMOPERACION=3 AND ST_FECHA_DISPOSICION BETWEEN '2008-09-01' AND '2008-12-31' " +
					"AND ST_SITUACION_ACTUAL_CREDITO=2";
		}
		else if ( tipoOper == 3 ){
			//Correr el query cada mes para cerrar cuentas 
			//update d_saldos_t24_grupal set st_saldo_insoluto=0.00 where st_numcliente in (SELECT ic_numcliente from d_integrantes_ciclo
			//where ic_numgrupo in (select st_numcliente FROM d_saldos_t24 where st_numoperacion=3
			//		and st_situacion_actual_credito=3 and st_fecha_disposicion between '2008-01-01' and '2008-08-31'));
			query = "SELECT * FROM D_SALDOS_T24_GRUPAL WHERE ST_REFERENCIA NOT IN (SELECT BB_REFERENCIA FROM D_BITACORA_BUROCIRCULO WHERE BB_ESTATUS=2)";
		}
		else if ( tipoOper == 4 ){
			//Grupales reportados a partir del año 2009
			query = "SELECT * FROM D_SALDOS_T24 WHERE ST_NUMOPERACION=3 AND ST_FECHA_DISPOSICION > '2009-01-01' " +
					"AND ST_REFERENCIA NOT IN (SELECT BB_REFERENCIA FROM D_BITACORA_BUROCIRCULO WHERE BB_ESTATUS=2) ";
		}

		Logger.debug("Procesando query: + " + query);

		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				SaldoT24VO saldo = new SaldoT24VO();
				saldo.capitalVencido = roundNum(rs.getDouble("st_capital_vencido"));
				saldo.diasVencidos = rs.getInt("st_dias_vencidos");
				saldo.fechaDisposicion = rs.getDate("st_fecha_disposicion");
				saldo.frecuenciaAmortizacion = rs.getString("st_frecuencia_amortizacion");
				saldo.montoAmortizacion = roundNum(rs.getDouble("st_monto_amortizacion"));
				saldo.montoInteresesPorCobrar = roundNum(rs.getDouble("st_monto_intereses_por_cobrar"));
				saldo.montoLineaCredito = roundNum(rs.getDouble("st_monto_linea_credito"));
				saldo.numCliente = rs.getInt("st_numcliente");
				saldo.ciclo = rs.getInt("st_ciclo");
				saldo.numCuotas = rs.getInt("st_numero_cuotas");
				saldo.numeroPagosVencidos = rs.getInt("st_numero_pagos_vencidos");
				saldo.rfc = rs.getString("st_rfc");
				saldo.saldoInsoluto = roundNum(rs.getDouble("st_saldo_insoluto"));
				saldo.situacionActualCredito = rs.getInt("st_situacion_actual_credito");
				saldo.numOperacion = rs.getInt("st_numoperacion");
				saldo.totalExigible = roundNum(rs.getDouble("st_total_exigible"));
				saldo.noCuenta = rs.getString("st_referencia");
				arrayListSaldos.add(saldo);
			}
		}
		catch(SQLException exc){
			Logger.debug("SQLException en saldoT24DAO.getSaldosT24()::" + exc);
			exc.printStackTrace();
		}
		catch(NamingException exc){
			Logger.debug("NamingException en saldoT24DAO.getSaldosT24()::" + exc);
			exc.printStackTrace();
		}
		catch(Exception exc){
			Logger.debug("Esception caugth::" + exc);
			exc.printStackTrace();
		}
		finally{
			if(cn != null){
				try{
					cn.close();
				}
				catch(SQLException exc1){
					Logger.debug("SQLException en saldoT24DAO.getSaldosT24()::" + exc1);
					exc1.printStackTrace();
				}
			}
		}
		arraySaldo = new SaldoT24VO[ arrayListSaldos.size()];
		for(int i = 0; i < arraySaldo.length; i++){
			arraySaldo[i] = arrayListSaldos.get(i);
		}
		return arraySaldo;
	}
	
	public SaldoT24VO getSaldosT24ByReferencia( String referencia ){

		Connection cn = null;
		int param = 1;
		SaldoT24VO saldo = null;
		String query = "SELECT * " +
				         "FROM D_SALDOS_T24 " +
				        "WHERE ST_REFERENCIA = ? ";
		try{
			cn = getConnection();
			PreparedStatement ps = cn.prepareStatement(query);
			ps.setString(param++, referencia);
//			Logger.debug("Ejecutando : "+query);
//			Logger.debug("Para : " + numCliente + " con ciclo: " + ciclo);
			ResultSet rs = ps.executeQuery();
			if(rs.next()){
				saldo = new SaldoT24VO();
				saldo.capitalVencido = roundNum(rs.getDouble("st_capital_vencido"));
				saldo.comision = roundNum(rs.getDouble("st_comision"));
				saldo.ciclo = rs.getInt("st_ciclo");
				saldo.desembolso = roundNum(rs.getDouble("st_desembolso"));
				saldo.destinoCredito = rs.getInt("st_destino_credito");
				saldo.diasVencidos = rs.getInt("st_dias_vencidos");
				saldo.direccion1 = rs.getString("st_direccion1");
				saldo.direccion2 = rs.getString("st_direccion2");
				saldo.fechaDisposicion = rs.getDate("st_fecha_disposicion");
				saldo.fechaEntradaCartVencida = rs.getDate("st_fecha_entrada_a_cart_vencida");
				saldo.fechaGeneracion = rs.getDate("st_fecha_generacion");
				saldo.fechaIncumplimiento = rs.getDate("st_fecha_incumplimiento");
				saldo.fechaInf = rs.getDate("st_fecha_inf");
				saldo.fechaVencimiento = rs.getDate("st_fecha_vencimiento");
				saldo.feeVencidos = roundNum(rs.getDouble("st_fee_vencidos"));
				saldo.formaAmortizacion = rs.getInt("st_forma_amortizacion");
				saldo.formaPagoIntereses = rs.getInt("st_forma_pago_intereses");
				saldo.frecuenciaAmortizacion = rs.getString("st_frecuencia_amortizacion");
				saldo.interesesDevNoCobrados = roundNum(rs.getDouble("st_intereses_dev_no_cobrados"));
				saldo.interesesVencidos = roundNum(rs.getDouble("st_intereses_vencidos"));
				saldo.interesMoratorio = roundNum(rs.getDouble("st_interes_moratorio"));
				saldo.ivaComision = roundNum(rs.getDouble("st_iva_comision"));
				saldo.ivaMulta = roundNum(rs.getDouble("st_iva_multa"));
				saldo.moneda = rs.getInt("st_moneda");
				saldo.montoAmortizacion = roundNum(rs.getDouble("st_monto_amortizacion"));
				saldo.montoInteresesPorCobrar = roundNum(rs.getDouble("st_monto_intereses_por_cobrar"));
				saldo.montoLineaCredito = roundNum(rs.getDouble("st_monto_linea_credito"));
				saldo.multa = roundNum(rs.getDouble("st_multa"));
				saldo.nombre = rs.getString("st_nombre");
				saldo.nombreSucursal = rs.getString("st_nombre_sucursal");
				saldo.numCliente = rs.getInt("st_numcliente");
				saldo.numCuotas = rs.getInt("st_numero_cuotas");
				saldo.numCuotasRestantes = rs.getInt("st_numero_cuotas_restantes");
				saldo.numeroPagosVencidos = rs.getInt("st_numero_pagos_vencidos");
				saldo.numSucursal = rs.getInt("st_numsucursal");
				saldo.responsabilidadTotal = roundNum(rs.getDouble("st_responsabilidad_total"));
				saldo.rfc = rs.getString("st_rfc");
				saldo.saldoInsoluto = roundNum(rs.getDouble("st_saldo_insoluto"));
				saldo.situacionActualCredito = rs.getInt("st_situacion_actual_credito");
				saldo.tasaBruta = roundNum(rs.getDouble("st_tasa_bruta"));
				saldo.tasaReferencia = roundNum(rs.getDouble("st_tasa_referencia"));
				saldo.telefono = rs.getString("st_telefono");
				saldo.ivaMoratorio = roundNum(rs.getDouble("st_iva_moratorio"));
				saldo.totalVencido = roundNum(rs.getDouble("st_total_vencido"));
				saldo.totalExigible = roundNum(rs.getDouble("st_total_exigible"));
				saldo.capitalPagado = roundNum(rs.getDouble("st_capital_pagado"));
				saldo.interesPagado = roundNum(rs.getDouble("st_interes_pagado"));
				saldo.totalPagado = roundNum(rs.getDouble("st_total_pagado"));
				saldo.ejecutivo = rs.getString("st_ejecutivo");
				saldo.diasTotales = rs.getInt("st_dias_totales");
				saldo.interesFuturo = roundNum(rs.getDouble("st_interes_futuro"));
				saldo.moratorioPagado = roundNum(rs.getDouble("st_moratorio_pagado"));
				saldo.montoLiquidacionAnticipada = roundNum(rs.getDouble("st_monto_liquidacion_anticipada"));
				saldo.fechaProximoVencimiento = rs.getDate("st_fecha_proximo_vencimiento");
				saldo.interesAlDia = roundNum(rs.getDouble("st_interes_al_dia"));
				saldo.diasTranscurridos = rs.getInt("st_dias_transcurridos");
				saldo.totalMulta = roundNum(rs.getDouble("st_total_multa"));
				saldo.totalMoratorios = roundNum(rs.getDouble("st_total_moratorios"));
				saldo.fechaSigAmortizacion = rs.getDate("st_fecha_sig_amort");
				saldo.fuenteFondeo = rs.getInt("st_fuente_fondeo");
				saldo.numOperacion = rs.getInt("st_numoperacion");
				saldo.tipoGtiaReal = rs.getInt("st_tipo_garantia_real");
				saldo.tipoRelacion = rs.getInt("st_tipo_relacion");
				saldo.tipoIndustria = rs.getInt("st_tipo_industria");
				saldo.montoAprobado = roundNum(rs.getDouble("st_monto_aprobado"));
				saldo.noCuenta = rs.getString("st_referencia");
				saldo.nombreProducto = rs.getString("st_nombre_producto");
			}
		}
		catch(SQLException exc){
			Logger.debug("SQLException en saldoT24DAO.getSaldosT24ByContrato::" + exc);
			exc.printStackTrace();
		}
		catch(NamingException exc){
			Logger.debug("NamingException en saldoT24DAO.getSaldosT24ByContrato::" + exc);
			exc.printStackTrace();
		}
		catch(Exception exc){
			Logger.debug("Exception en saldoT24DAO.getSaldosT24ByContrato::" + exc);
			exc.printStackTrace();
		}
		finally{
			if(cn != null){
				try{
					cn.close();
				}
				catch(SQLException exc1){
					Logger.debug("SQLException en saldoT24DAO.getSaldosT24ByContrato::" + exc1);
					exc1.printStackTrace();
				}
			}
		}
		
		return saldo;
	}


	public static double roundNum(double num) throws Exception
	{
		double valor = 0;
		valor = num;
		valor = valor*100;
		valor = java.lang.Math.round(valor);
		valor = valor/100;
		return valor;
	}
}