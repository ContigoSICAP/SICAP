package com.sicap.clientes.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.naming.NamingException;

import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.SaldoT24VO;

public class SaldoHistoricoSFHDAO extends DAOMaster {
	
	public void insertSaldoHistoricoSHF(SaldoT24VO saldo){
		
		if( saldo == null ){
			return;
		}
		
		Connection cn = null;
		int param = 1;
		
		String query = "insert into d_saldos_historicos_shf (st_numcliente,st_fecha_inf,st_fecha_generacion,";
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


}
