package com.sicap.clientes.helpers;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.SaldoT24DAO;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.SaldoT24VO;

public class IBSHelper {
	
	public static List readFileSaldosIBS(){
		
		String path     = CatalogoHelper.getParametro("RUTA_CARPETA_ARCHIVOS_IBS");
		String fileName = CatalogoHelper.getParametro("NOMBRE_ARCHIVO_SALDOS_IBS");
		String linea = null;
		List<SaldoIBSVO> list = new ArrayList<SaldoIBSVO>();
		
		SaldoIBSVO saldosIBS = null;
		
		try{
			TreeMap catEstatus = CatalogoHelper.getCatalogoEstatusSaldos();
			TreeMap mapeoProductosIBS_SICAP = CatalogoHelper.getMapeoProductosIBS_SICAP();
			BufferedReader buffer =   new BufferedReader(new InputStreamReader(new FileInputStream(path+fileName),"ISO-8859-1"));
			
			while ((linea = buffer.readLine())!= null && linea.trim().length()>0){
				saldosIBS = new SaldoIBSVO();
				final int decimalSaldo   = 2;
				final int decimalTasa    = 6;
				final int decimalTasaIVA = 3;
				final String formatoFecha = "yyyyMMdd";
				
				saldosIBS.setOrigen                   ( 1 );
				saldosIBS.setCredito                  ( Convertidor.stringToInt(linea.substring(0, 12))  );	
				saldosIBS.setReferencia               ( linea.substring(12, 25).trim() );
				saldosIBS.setIdClienteSICAP           ( Convertidor.stringToInt(linea.substring(25, 37))  );
				saldosIBS.setIdSolicitudSICAP         ( Convertidor.stringToInt(linea.substring(37, 44))  );
				saldosIBS.setIdClienteIBS             ( Convertidor.stringToInt(linea.substring(44, 53))  );
				saldosIBS.setNombreCliente            ( linea.substring(53, 98).trim().toUpperCase() );
				saldosIBS.setRfc                      ( linea.substring(98, 113) );
				saldosIBS.setIdSucursal               ( Convertidor.stringToInt(linea.substring(113, 116))  );
				saldosIBS.setNombreSucursal           ( linea.substring(116, 151).trim() );
				saldosIBS.setFechaEnvio               ( Convertidor.stringToSqlDate(linea.substring(151, 159),formatoFecha)  );
				saldosIBS.setFechaGeneracion          ( Convertidor.stringToSqlDate(linea.substring(159, 167),formatoFecha)  );
				saldosIBS.setHoraGeneracion           ( Convertidor.stringToInt(linea.substring(167, 173))  );
				saldosIBS.setNumeroCuotas             ( Convertidor.stringToInt(linea.substring(173, 180))  );
				saldosIBS.setNumeroCuotasTranscurridas( Convertidor.stringToInt(linea.substring(180, 187))  );
				saldosIBS.setPlazo                    ( Convertidor.stringToInt(linea.substring(187, 191))  );
				saldosIBS.setPeriodicidad             ( Convertidor.stringToInt(linea.substring(191, 194))  );
				saldosIBS.setFechaDesembolso          ( Convertidor.stringToSqlDate(linea.substring(194, 202),formatoFecha)  );
				saldosIBS.setFechaVencimiento         ( Convertidor.stringToSqlDate(linea.substring(202, 210),formatoFecha)  );
				saldosIBS.setMontoCredito             ( Convertidor.stringToDouble(linea.substring(210, 225), decimalSaldo)  );
				saldosIBS.setSaldoTotalAlDia          ( Convertidor.stringToDouble(linea.substring(225, 240), decimalSaldo)  );
				saldosIBS.setSaldoCapital             ( Convertidor.stringToDouble(linea.substring(240, 255), decimalSaldo)  );
				saldosIBS.setSaldoInteres             ( Convertidor.stringToDouble(linea.substring(255, 271), decimalSaldo)  );
				saldosIBS.setSaldoInteresVigente      ( Convertidor.stringToDouble(linea.substring(271, 287), decimalSaldo)  );
				saldosIBS.setSaldoInteresVencido      ( Convertidor.stringToDouble(linea.substring(287, 303), decimalSaldo)  );
				saldosIBS.setSaldoInteresVencido90dias( Convertidor.stringToDouble(linea.substring(303, 318), decimalSaldo)  );
				saldosIBS.setSaldoInteresCtasOrden    ( Convertidor.stringToDouble(linea.substring(318, 334), decimalSaldo)  );
				saldosIBS.setSaldoIvaInteres          ( Convertidor.stringToDouble(linea.substring(334, 350), decimalSaldo)  );
				saldosIBS.setSaldoBonificacionDeIVA   ( Convertidor.stringToDouble(linea.substring(350, 366), decimalSaldo)  );
				saldosIBS.setSaldoMora                ( Convertidor.stringToDouble(linea.substring(366, 382), decimalSaldo)  );
				saldosIBS.setSaldoIVAMora             ( Convertidor.stringToDouble(linea.substring(382, 398), decimalSaldo)  );
				saldosIBS.setSaldoMulta               ( Convertidor.stringToDouble(linea.substring(398, 413), decimalSaldo)  );
				saldosIBS.setSaldoIVAMulta            ( Convertidor.stringToDouble(linea.substring(413, 428), decimalSaldo)  );
				saldosIBS.setCapitalPagado            ( Convertidor.stringToDouble(linea.substring(428, 443), decimalSaldo)  );
				saldosIBS.setInteresNormalPagado      ( Convertidor.stringToDouble(linea.substring(443, 458), decimalSaldo)  );
				saldosIBS.setIvaInteresNormalPagado   ( Convertidor.stringToDouble(linea.substring(458, 473), decimalSaldo)  );
				saldosIBS.setBonificacionPagada       ( Convertidor.stringToDouble(linea.substring(473, 489), decimalSaldo)  );
				saldosIBS.setMoratorioPagado          ( Convertidor.stringToDouble(linea.substring(489, 504), decimalSaldo)  );
				saldosIBS.setIvaMoraPagado            ( Convertidor.stringToDouble(linea.substring(504, 519), decimalSaldo)  );
				saldosIBS.setMultaPagada              ( Convertidor.stringToDouble(linea.substring(519, 534), decimalSaldo)  );
				saldosIBS.setIvaMultaPagado           ( Convertidor.stringToDouble(linea.substring(534, 549), decimalSaldo)  );
				saldosIBS.setComision                 ( Convertidor.stringToDouble(linea.substring(549, 564), decimalSaldo)  );
				saldosIBS.setIvaComision              ( Convertidor.stringToDouble(linea.substring(564, 579), decimalSaldo)  );
				saldosIBS.setMontoSeguro              ( Convertidor.stringToDouble(linea.substring(579, 594), decimalSaldo)  );
				saldosIBS.setMontoDesembolsado        ( Convertidor.stringToDouble(linea.substring(594, 609), decimalSaldo)  );
				saldosIBS.setFechaSigAmortizacion     ( Convertidor.stringToSqlDate(linea.substring(609,617), formatoFecha)  );
				saldosIBS.setCapitalSigAmortizacion   ( Convertidor.stringToDouble(linea.substring(617, 632), decimalSaldo)  );
				saldosIBS.setInteresSigAmortizacion   ( Convertidor.stringToDouble(linea.substring(632, 647), decimalSaldo)  );
				saldosIBS.setIvaSigAmortizacion       ( Convertidor.stringToDouble(linea.substring(647, 662), decimalSaldo)  );
				saldosIBS.setFondeador                ( Convertidor.stringToInt(linea.substring(662, 666)) );
				saldosIBS.setNombreFondeador          ( linea.substring(666, 701) );
				saldosIBS.setTasaInteresSinIVA        ( Convertidor.stringToDouble(linea.substring(701, 710), decimalTasa)  );
				saldosIBS.setTasaMoraSinIVA           ( Convertidor.stringToDouble(linea.substring(710, 719), decimalTasa)  );
				saldosIBS.setTasaIVA                  ( Convertidor.stringToDouble(linea.substring(719, 724), decimalTasaIVA)  );
				saldosIBS.setSaldoConInteresAlFinal   ( Convertidor.stringToDouble(linea.substring(724, 739), decimalSaldo)  );
				saldosIBS.setCapitalVencido           ( Convertidor.stringToDouble(linea.substring(739, 754), decimalSaldo)  );
				saldosIBS.setInteresVencido           ( Convertidor.stringToDouble(linea.substring(754, 770), decimalSaldo)  );
				saldosIBS.setIvaInteresVencido        ( Convertidor.stringToDouble(linea.substring(770, 785), decimalSaldo)  );
				saldosIBS.setTotalVencido             ( Convertidor.stringToDouble(linea.substring(785, 800), decimalSaldo)  );
//				saldosIBS.setEstatus                  ( linea.substring(800, 820) );
				saldosIBS.setEstatus                  ( (Integer)catEstatus.get( linea.substring(800, 820).trim().toUpperCase())   );
				
				saldosIBS.setCtaContable              ( Convertidor.stringToLong( linea.substring(820, 836).trim().toUpperCase() )   );
				
				saldosIBS.setIdProducto               ( ((Integer)mapeoProductosIBS_SICAP.get( linea.substring(836, 840) )).intValue()  );
				saldosIBS.setFechaIncumplimiento      ( Convertidor.stringToSqlDate(linea.substring(840, 848), formatoFecha)  );
				saldosIBS.setFechaAcarteraVencida     ( Convertidor.stringToSqlDate(linea.substring(848, 856), formatoFecha)  );
				saldosIBS.setDiasMora                 ( Convertidor.stringToInt(linea.substring(856, 863))  );
				saldosIBS.setDiasTranscurridos        ( Convertidor.stringToInt(linea.substring(863, 870))  );
				saldosIBS.setCuotasVencidas           ( Convertidor.stringToInt(linea.substring(870, 877))  );
				saldosIBS.setNumeroPagosRealizados    ( Convertidor.stringToInt(linea.substring(877, 884))  );
				saldosIBS.setMontoTotalPagado         ( Convertidor.stringToDouble(linea.substring(884, 899), decimalSaldo)  );
				saldosIBS.setFechaUltimoPago          ( Convertidor.stringToSqlDate(linea.substring(899, 907),formatoFecha)  );
				saldosIBS.setBanderaReestructura      ( linea.substring(907, 908) );
				saldosIBS.setCreditoReestructurado    ( Convertidor.stringToDouble(linea.substring(908, 920))  );
				saldosIBS.setDiasMoraReestructura     ( Convertidor.stringToInt(linea.substring(920, 932))  );
				saldosIBS.setTasaPreferencialIVA      ( linea.substring(932, 933) );
				saldosIBS.setCuentaBucket             ( Convertidor.stringToInt(linea.substring(933, 945))  );
				saldosIBS.setSaldoBucket              ( Convertidor.stringToDouble(linea.substring(945, 960), decimalSaldo)  );
				
				list.add( saldosIBS );
				
			}
		}catch(FileNotFoundException fnf){
			Logger.debug("Archivo de Saldos no encontrado en: " + path+fileName);
			return null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return list;
	}
	
	public static List readFileSaldosCarteraWin(){

		String path     = CatalogoHelper.getParametro("RUTA_CARPETA_ARCHIVOS_IBS");
		String fileName = CatalogoHelper.getParametro("NOMBRE_ARCHIVO_SALDOS_CWIN");
		String linea = null;
		List<SaldoIBSVO> list = new ArrayList<SaldoIBSVO>();

		SaldoIBSVO saldosIBS = null;
		String[] columnas = null;
		int cont = 0;

		final String formatoFecha = "yyyyMMdd";
		TreeMap catProductosCwin = CatalogoHelper.getCatalogoProductosCwin();
		
		try{
			TreeMap catEstatus = CatalogoHelper.getCatalogoEstatusSaldos();
			BufferedReader buffer =   new BufferedReader(new InputStreamReader(new FileInputStream(path+fileName),"ISO-8859-1"));

			while ((linea = buffer.readLine())!= null && linea.trim().length()>0){
				cont++;
				if( cont==1 )
					continue;
				columnas = linea.split( "," );
				saldosIBS = new SaldoIBSVO();
				
				if( columnas.length != 74 ){
					Logger.debug("El arvhivo "+fileName+" no cumple con el Layout Definido");
					break;
				}
				
				saldosIBS.setOrigen                   ( 2 );
				saldosIBS.setCredito                  ( Convertidor.stringToInt(columnas[1])  );	
				saldosIBS.setReferencia               ( columnas[2] );
				saldosIBS.setIdClienteSICAP           ( Convertidor.stringToInt(columnas[3])  );
				saldosIBS.setIdSolicitudSICAP         ( Convertidor.stringToInt(columnas[4])  );
				saldosIBS.setIdClienteIBS             ( Convertidor.stringToInt(columnas[0])  );
				saldosIBS.setNombreCliente            ( columnas[6].toUpperCase() );
				saldosIBS.setRfc                      ( columnas[7] );
				saldosIBS.setIdSucursal               ( Convertidor.stringToInt(columnas[8])  );
				saldosIBS.setNombreSucursal           ( columnas[9] );
				saldosIBS.setFechaEnvio               ( Convertidor.stringToSqlDate(columnas[10],formatoFecha)  );
				saldosIBS.setFechaGeneracion          ( Convertidor.stringToSqlDate(columnas[11],formatoFecha)  );
				saldosIBS.setHoraGeneracion           ( Convertidor.stringToInt(columnas[12])  );
				saldosIBS.setNumeroCuotas             ( Convertidor.stringToInt(columnas[13])  );
				saldosIBS.setNumeroCuotasTranscurridas( Convertidor.stringToInt(columnas[14])  );
				saldosIBS.setPlazo                    ( Convertidor.stringToInt(columnas[15])  );
				saldosIBS.setPeriodicidad             ( Convertidor.stringToInt(columnas[16])  );
				saldosIBS.setFechaDesembolso          ( Convertidor.stringToSqlDate(columnas[17],formatoFecha)  );
				saldosIBS.setFechaVencimiento         ( Convertidor.stringToSqlDate(columnas[18],formatoFecha)  );
				saldosIBS.setMontoCredito             ( Convertidor.stringToDouble(columnas[19])  );
				saldosIBS.setSaldoTotalAlDia          ( Convertidor.stringToDouble(columnas[20])  );
				saldosIBS.setSaldoCapital             ( Convertidor.stringToDouble(columnas[21])  );
				saldosIBS.setSaldoInteres             ( Convertidor.stringToDouble(columnas[22])  );
				saldosIBS.setSaldoInteresVigente      ( Convertidor.stringToDouble(columnas[23])  );
				saldosIBS.setSaldoInteresVencido      ( Convertidor.stringToDouble(columnas[24])  );
				saldosIBS.setSaldoInteresVencido90dias( Convertidor.stringToDouble(columnas[25])  );
				saldosIBS.setSaldoInteresCtasOrden    ( Convertidor.stringToDouble(columnas[26])  );
				saldosIBS.setSaldoIvaInteres          ( Convertidor.stringToDouble(columnas[27])  );
				saldosIBS.setSaldoBonificacionDeIVA   ( Convertidor.stringToDouble(columnas[28])  );
				saldosIBS.setSaldoMora                ( Convertidor.stringToDouble(columnas[29])  );
				saldosIBS.setSaldoIVAMora             ( Convertidor.stringToDouble(columnas[30])  );
				saldosIBS.setSaldoMulta               ( Convertidor.stringToDouble(columnas[31])  );
				saldosIBS.setSaldoIVAMulta            ( Convertidor.stringToDouble(columnas[32])  );
				saldosIBS.setCapitalPagado            ( Convertidor.stringToDouble(columnas[33])  );
				saldosIBS.setInteresNormalPagado      ( Convertidor.stringToDouble(columnas[34])  );
				saldosIBS.setIvaInteresNormalPagado   ( Convertidor.stringToDouble(columnas[35])  );
				saldosIBS.setBonificacionPagada       ( Convertidor.stringToDouble(columnas[36])  );
				saldosIBS.setMoratorioPagado          ( Convertidor.stringToDouble(columnas[37])  );
				saldosIBS.setIvaMoraPagado            ( Convertidor.stringToDouble(columnas[38])  );
				saldosIBS.setMultaPagada              ( Convertidor.stringToDouble(columnas[39])  );
				saldosIBS.setIvaMultaPagado           ( Convertidor.stringToDouble(columnas[40])  );
				saldosIBS.setComision                 ( Convertidor.stringToDouble(columnas[41])  );
				saldosIBS.setIvaComision              ( Convertidor.stringToDouble(columnas[42])  );
				saldosIBS.setMontoSeguro              ( Convertidor.stringToDouble(columnas[43])  );
				saldosIBS.setMontoDesembolsado        ( Convertidor.stringToDouble(columnas[44])  );
				saldosIBS.setFechaSigAmortizacion     ( Convertidor.stringToSqlDate(columnas[45], formatoFecha)  );
				saldosIBS.setCapitalSigAmortizacion   ( Convertidor.stringToDouble(columnas[46])  );
				saldosIBS.setInteresSigAmortizacion   ( Convertidor.stringToDouble(columnas[47])  );
				saldosIBS.setIvaSigAmortizacion       ( Convertidor.stringToDouble(columnas[48])  );
				saldosIBS.setFondeador                ( Convertidor.stringToInt(columnas[49]) );
				saldosIBS.setNombreFondeador          ( columnas[50] );
				saldosIBS.setTasaInteresSinIVA        ( Convertidor.stringToDouble(columnas[51])  );
				saldosIBS.setTasaMoraSinIVA           ( Convertidor.stringToDouble(columnas[52])  );
				saldosIBS.setTasaIVA                  ( Convertidor.stringToDouble(columnas[53])  );
				saldosIBS.setSaldoConInteresAlFinal   ( Convertidor.stringToDouble(columnas[54])  );
				saldosIBS.setCapitalVencido           ( Convertidor.stringToDouble(columnas[55])  );
				saldosIBS.setInteresVencido           ( Convertidor.stringToDouble(columnas[56])  );
				saldosIBS.setIvaInteresVencido        ( Convertidor.stringToDouble(columnas[57])  );
				saldosIBS.setTotalVencido             ( Convertidor.stringToDouble(columnas[58])  );
//				saldosIBS.setEstatus                  ( columnas[59] );
				saldosIBS.setEstatus                  ( (Integer)catEstatus.get( columnas[59].trim().toUpperCase())   );
				saldosIBS.setIdProducto               ( (Integer)catProductosCwin.get(columnas[60])  );
				saldosIBS.setFechaIncumplimiento      ( Convertidor.stringToSqlDate(columnas[61], formatoFecha)  );
				saldosIBS.setFechaAcarteraVencida     ( Convertidor.stringToSqlDate(columnas[62], formatoFecha)  );
				saldosIBS.setDiasMora                 ( Convertidor.stringToInt(columnas[63])  );
				saldosIBS.setDiasTranscurridos        ( Convertidor.stringToInt(columnas[64])  );
				saldosIBS.setCuotasVencidas           ( Convertidor.stringToInt(columnas[65])  );
				saldosIBS.setNumeroPagosRealizados    ( Convertidor.stringToInt(columnas[66])  );
				saldosIBS.setMontoTotalPagado         ( Convertidor.stringToDouble(columnas[67])  );
				saldosIBS.setFechaUltimoPago          ( Convertidor.stringToSqlDate(columnas[68],formatoFecha)  );
				saldosIBS.setBanderaReestructura      ( columnas[69] );
				saldosIBS.setCreditoReestructurado    ( Convertidor.stringToDouble(columnas[70])  );
				saldosIBS.setTasaPreferencialIVA      ( columnas[71] );
				saldosIBS.setCuentaBucket             ( Convertidor.stringToInt(columnas[72])  );
				saldosIBS.setSaldoBucket              ( Convertidor.stringToDouble(columnas[73])  );
				
				list.add( saldosIBS );

			}	

		}catch(FileNotFoundException fnf){
			Logger.debug("Archivo de Saldos no encontrado en: " + path+fileName);
			return null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
		return list;
	}

	public static List getSaldosT24ToIBS(){
		
		SaldoIBSVO saldosIBS = null;
		SaldoT24DAO st24DAO = new SaldoT24DAO();
//		List listSaldosT24 = st24DAO.getSaldosT24Hoy();
		
		SaldoT24VO[] saldosT24 = st24DAO.getSaldosT24();
		
		List<SaldoIBSVO> list = new ArrayList<SaldoIBSVO>();
		
		System.out.println("Registros totales en T24: "+ saldosT24.length);
		
		for( int i=0; saldosT24!=null && i<=saldosT24.length; i++ ){
			
			saldosIBS = new SaldoIBSVO();
			saldosIBS.setCapitalPagado         ( saldosT24[i].capitalPagado );
			saldosIBS.setCapitalVencido        ( saldosT24[i].capitalVencido  );
			saldosIBS.setIdSolicitudSICAP      ( saldosT24[i].ciclo  );
			saldosIBS.setFechaGeneracion       ( saldosT24[i].fechaGeneracion  );
			saldosIBS.setComision              ( saldosT24[i].comision  );
			saldosIBS.setMontoDesembolsado     ( saldosT24[i].desembolso  );
			saldosIBS.setDiasTranscurridos     ( saldosT24[i].diasTranscurridos  );
			saldosIBS.setDiasMora              ( saldosT24[i].diasVencidos  );
			saldosIBS.setFechaDesembolso       ( saldosT24[i].fechaDisposicion  );
			saldosIBS.setFechaAcarteraVencida  ( saldosT24[i].fechaEntradaCartVencida  );
			saldosIBS.setFechaEnvio            ( saldosT24[i].fechaInf  );
			saldosIBS.setFechaIncumplimiento   ( saldosT24[i].fechaIncumplimiento  );
			saldosIBS.setFechaSigAmortizacion  ( saldosT24[i].fechaSigAmortizacion  );
			saldosIBS.setFechaVencimiento      ( saldosT24[i].fechaVencimiento  );
			saldosIBS.setSaldoInteresVencido   ( saldosT24[i].feeVencidos  );
//			saldosIBS.setPeriodicidad          ( saldosT24[i].frecuenciaAmortizacion  );
//			saldosIBS.setFondeador             ( saldosT24[i].fuenteFondeo );
			saldosIBS.setInteresVencido        ( saldosT24[i].interesesVencidos  );
			saldosIBS.setSaldoConInteresAlFinal( saldosT24[i].montoLiquidacionAnticipada  );
			saldosIBS.setSaldoMora             ( saldosT24[i].interesMoratorio  );
			saldosIBS.setTasaInteresSinIVA     ( saldosT24[i].tasaBruta);
			saldosIBS.setInteresNormalPagado   ( saldosT24[i].interesPagado  );
			saldosIBS.setIvaComision           ( saldosT24[i].ivaComision  );
			saldosIBS.setSaldoIVAMora          ( saldosT24[i].ivaMoratorio  );
			saldosIBS.setSaldoIVAMulta         ( saldosT24[i].ivaMulta  );
			saldosIBS.setSaldoInteres          ( saldosT24[i].interesAlDia  );
			saldosIBS.setMontoCredito          ( saldosT24[i].montoAprobado  );
			saldosIBS.setMoratorioPagado       ( saldosT24[i].moratorioPagado  );
			saldosIBS.setSaldoMulta            ( saldosT24[i].multa  );
			saldosIBS.setNombreCliente         ( saldosT24[i].nombre  );
			saldosIBS.setNombreSucursal        ( saldosT24[i].nombreSucursal  );
			saldosIBS.setIdClienteSICAP        ( saldosT24[i].numCliente );
			saldosIBS.setNumeroCuotas          ( saldosT24[i].numCuotas  );
			saldosIBS.setCuotasVencidas        ( saldosT24[i].numeroPagosVencidos  );
			saldosIBS.setIdProducto            ( saldosT24[i].numOperacion  );
			saldosIBS.setIdSucursal            ( saldosT24[i].numSucursal  );
			saldosIBS.setReferencia            ( saldosT24[i].noCuenta  );
		    saldosIBS.setRfc                   ( saldosT24[i].rfc  );
		    saldosIBS.setSaldoTotalAlDia       ( saldosT24[i].saldoInsoluto  );
//		    saldosIBS.setEstatus               ( saldosT24[i].situacionActualCredito  );
		    saldosIBS.setCapitalSigAmortizacion( saldosT24[i].montoAmortizacion  );
		    saldosIBS.setMontoTotalPagado      ( saldosT24[i].totalPagado  );
		    saldosIBS.setTotalVencido          ( saldosT24[i].totalExigible  );
		    
		    list.add( saldosIBS );
		    
//		 --   saldosIBS.setIdSolicitudSICAP( saldosT24[i].ciclo  );
		}
		Logger.debug("El número de Clientes a cargar es: " + list.size());
		return list;
	}

	public static SaldoIBSVO getSaldosT24ToIBS(SaldoT24VO saldosT24){
		
		if ( saldosT24==null )
			return null;
		SaldoIBSVO saldosIBS = new SaldoIBSVO();
		
		saldosIBS.setCapitalPagado         ( saldosT24.capitalPagado );
		saldosIBS.setCapitalVencido        ( saldosT24.capitalVencido  );
		saldosIBS.setIdSolicitudSICAP      ( saldosT24.ciclo  );
		saldosIBS.setFechaGeneracion       ( saldosT24.fechaGeneracion  );
		saldosIBS.setComision              ( saldosT24.comision  );
		saldosIBS.setMontoDesembolsado     ( saldosT24.desembolso  );
		saldosIBS.setDiasTranscurridos     ( saldosT24.diasTranscurridos  );
		saldosIBS.setDiasMora              ( saldosT24.diasVencidos  );
		saldosIBS.setFechaDesembolso       ( saldosT24.fechaDisposicion  );
		saldosIBS.setFechaAcarteraVencida  ( saldosT24.fechaEntradaCartVencida  );
		saldosIBS.setFechaEnvio            ( saldosT24.fechaInf  );
		saldosIBS.setFechaIncumplimiento   ( saldosT24.fechaIncumplimiento  );
		saldosIBS.setFechaSigAmortizacion  ( saldosT24.fechaSigAmortizacion  );
		saldosIBS.setFechaVencimiento      ( saldosT24.fechaVencimiento  );
		saldosIBS.setSaldoInteresVencido   ( saldosT24.feeVencidos  );
		saldosIBS.setPeriodicidad          ( frecuenciaPago(saldosT24.frecuenciaAmortizacion) );
		saldosIBS.setFondeador             ( saldosT24.fuenteFondeo );
		saldosIBS.setInteresVencido        ( saldosT24.interesesVencidos  );
		saldosIBS.setSaldoConInteresAlFinal( saldosT24.montoLiquidacionAnticipada  );
		saldosIBS.setSaldoMora             ( saldosT24.interesMoratorio  );
		saldosIBS.setTasaInteresSinIVA     ( saldosT24.tasaBruta);
		saldosIBS.setInteresNormalPagado   ( saldosT24.interesPagado  );
		saldosIBS.setIvaComision           ( saldosT24.ivaComision  );
		saldosIBS.setSaldoIVAMora          ( saldosT24.ivaMoratorio  );
		saldosIBS.setSaldoIVAMulta         ( saldosT24.ivaMulta  );
		saldosIBS.setSaldoInteres          ( saldosT24.interesAlDia  );
		saldosIBS.setMontoCredito          ( saldosT24.montoAprobado  );
		saldosIBS.setMoratorioPagado       ( saldosT24.moratorioPagado  );
		saldosIBS.setSaldoMulta            ( saldosT24.multa  );
		saldosIBS.setNombreCliente         ( saldosT24.nombre  );
		saldosIBS.setNombreSucursal        ( saldosT24.nombreSucursal  );
		saldosIBS.setIdClienteSICAP        ( saldosT24.numCliente );
		saldosIBS.setNumeroCuotas          ( saldosT24.numCuotas  );
		saldosIBS.setCuotasVencidas        ( saldosT24.numeroPagosVencidos  );
		saldosIBS.setIdProducto            ( saldosT24.numOperacion  );
		saldosIBS.setIdSucursal            ( saldosT24.numSucursal  );
		saldosIBS.setReferencia            ( saldosT24.noCuenta  );
	    saldosIBS.setRfc                   ( saldosT24.rfc  );
	    saldosIBS.setSaldoTotalAlDia       ( saldosT24.saldoInsoluto  );
	    saldosIBS.setEstatus               ( saldosT24.situacionActualCredito  );
	    saldosIBS.setCapitalSigAmortizacion( saldosT24.montoAmortizacion  );
	    saldosIBS.setMontoTotalPagado      ( saldosT24.capitalPagado + saldosT24.interesPagado + saldosT24.moratorioPagado );
	    saldosIBS.setTotalVencido          ( saldosT24.totalExigible  );
		    
		return saldosIBS;
	}
	
	public SaldoT24VO[] getSaldosT24FromSaldosIBS(){
		
		SaldoIBSVO[] saldosIBS = null;
		List<SaldoT24VO> list = new ArrayList<SaldoT24VO>();
		SaldoT24VO saldoT24 = null;
		SaldoT24VO[] saldosT24 = null;
		
		try{
			saldosIBS = new SaldoIBSDAO().getSaldosIBS(null);
			
			for(int i=0; i<saldosIBS.length; i++){
				saldoT24 = new SaldoT24VO();
				
				saldoT24.capitalPagado              = saldosIBS[i].getCapitalPagado();
				saldoT24.ciclo                      = saldosIBS[i].getIdSolicitudSICAP();      
				saldoT24.fechaGeneracion            = saldosIBS[i].getFechaGeneracion();
				saldoT24.comision                   = saldosIBS[i].getComision();
				saldoT24.desembolso                 = saldosIBS[i].getMontoDesembolsado();
				saldoT24.diasTranscurridos          = saldosIBS[i].getDiasTranscurridos();
				saldoT24.diasVencidos               = saldosIBS[i].getDiasMora();
				saldoT24.fechaDisposicion           = saldosIBS[i].getFechaDesembolso();
				saldoT24.fechaEntradaCartVencida    = saldosIBS[i].getFechaAcarteraVencida();
				saldoT24.fechaInf                   = saldosIBS[i].getFechaEnvio();
				saldoT24.fechaIncumplimiento        = saldosIBS[i].getFechaIncumplimiento(); 
				saldoT24.fechaSigAmortizacion       = saldosIBS[i].getFechaSigAmortizacion();
				saldoT24.fechaVencimiento           = saldosIBS[i].getFechaVencimiento();
				saldoT24.feeVencidos                = saldosIBS[i].getSaldoInteresVencido() + saldosIBS[i].getIvaInteresVencido();
				saldoT24.frecuenciaAmortizacion     = getFrecuenciaPagoT24(saldosIBS[i].getPeriodicidad());
				saldoT24.fuenteFondeo               = saldosIBS[i].getFondeador();
				saldoT24.interesesVencidos          = saldosIBS[i].getInteresVencido();
				saldoT24.montoLiquidacionAnticipada = saldosIBS[i].getSaldoConInteresAlFinal();
				saldoT24.interesMoratorio           = saldosIBS[i].getSaldoMora() + saldosIBS[i].getSaldoIVAMora();
				saldoT24.tasaBruta                  = saldosIBS[i].getTasaInteresSinIVA();
				saldoT24.interesPagado              = saldosIBS[i].getInteresNormalPagado();
				saldoT24.ivaComision                = saldosIBS[i].getIvaComision();
				saldoT24.ivaMoratorio               = 0; //saldosIBS[i].getSaldoIVAMora();
				saldoT24.ivaMulta                   = saldosIBS[i].getSaldoIVAMulta();
				saldoT24.interesAlDia               = saldosIBS[i].getSaldoInteres();
				saldoT24.montoAprobado              = saldosIBS[i].getMontoCredito();
				saldoT24.moratorioPagado            = saldosIBS[i].getMoratorioPagado();
				saldoT24.multa                      = saldosIBS[i].getSaldoMulta();
				saldoT24.nombre                     = saldosIBS[i].getNombreCliente();
				saldoT24.nombreSucursal             = saldosIBS[i].getNombreSucursal();
				saldoT24.numCliente                 = saldosIBS[i].getIdClienteSICAP();
				saldoT24.numCuotas                  = saldosIBS[i].getNumeroCuotas();
				saldoT24.numeroPagosVencidos        = saldosIBS[i].getCuotasVencidas();
				saldoT24.numOperacion               = saldosIBS[i].getIdProducto();
				saldoT24.numSucursal                = saldosIBS[i].getIdSucursal();
				saldoT24.noCuenta                   = saldosIBS[i].getReferencia();
				saldoT24.rfc                        = saldosIBS[i].getRfc();
				saldoT24.saldoInsoluto              = saldosIBS[i].getSaldoTotalAlDia();
				saldoT24.situacionActualCredito     = saldosIBS[i].getEstatus();
				saldoT24.montoAmortizacion          = saldosIBS[i].getCapitalSigAmortizacion();
				saldoT24.totalPagado                = saldosIBS[i].getMontoTotalPagado();
				saldoT24.totalExigible              = saldosIBS[i].getTotalVencido();
				saldoT24.montoInteresesPorCobrar    = saldosIBS[i].getInteresSigAmortizacion() + saldosIBS[i].getIvaSigAmortizacion();
				saldoT24.capitalVencido              = saldosIBS[i].getCapitalVencido();
				
				
				list.add( saldoT24 );
			}
			saldosT24 = new SaldoT24VO[list.size()];
			for(int i=0;i<saldosT24.length; i++) saldosT24[i] = (SaldoT24VO)list.get(i);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return saldosT24;
	}
	
	private static int frecuenciaPago ( String frecuencia ){
		
		int idProducto = 0;
		
		if ( frecuencia != null ){
			if ( frecuencia.equals("M") )
				idProducto =  2;
			else if ( frecuencia.equals("15D") )
				idProducto = 1;
		}
		
		return idProducto;
	}
	
	private static String getFrecuenciaPagoT24( int dias ){
		String frecuencia = "M";
		if( dias == 15 )
			frecuencia = "15D";
		
		return frecuencia;
	}
}

