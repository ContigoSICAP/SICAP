package com.sicap.clientes.helpers.ibs;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.TreeMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.ReferenciaGeneralDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.dao.SucursalDAO;
import com.sicap.clientes.dao.ibs.CreditoIBSDAO;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.SeguroHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FormatUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.ComisionVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.SucursalVO;
import com.sicap.clientes.vo.TasaInteresVO;
import com.sicap.clientes.vo.ibs.CreditoIBSVO;

public class CreditoHelperIBS{

	public boolean registraCreditoIBS( ClienteVO cliente, SolicitudVO solicitud, HttpServletRequest request, Vector notificaciones ) throws Exception {

		int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, solicitud.idSolicitud);
		boolean result = false;
		HttpSession session = request.getSession();
		CreditoIBSVO objlCredit = new CreditoIBSVO();
		loadSources();

		try{
			objlCredit = getCreditInfo( cliente, solicitud, request.getRemoteUser());
			CreditoIBSDAO.getInstance().saveCredit(objlCredit, "S47829PETL");
			solicitud.idCreditoIBS = Convertidor.stringToInt(objlCredit.getNumeroCredito());
			if ( solicitud.idCreditoIBS != 0 ){
				new SolicitudDAO().updateSolicitudCredito(solicitud);
				cliente.solicitudes[indiceSolicitud] = solicitud;
				session.setAttribute("CLIENTE", cliente);
				result = true;
			}

		}catch(Exception e){
			notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE ,"No se logró registrar el CRÉDITO en el sistema de cartera IBS -- " + e.getMessage()));
			e.printStackTrace();
		}
		
		return result;
	}

	public CreditoIBSVO registraCreditoGrupoIBS( GrupoVO grupo, CicloGrupalVO ciclo, HttpServletRequest request, Vector notificaciones ) throws Exception {

		HttpSession session = request.getSession();
		CreditoIBSVO objlCredit = new CreditoIBSVO();
		loadSources();

		try{
			objlCredit = getCreditGroupInfo( grupo, ciclo, request.getRemoteUser());
			CreditoIBSDAO.getInstance().saveCredit(objlCredit, "S47829PETL");
			ciclo.idCreditoIBS = Convertidor.stringToInt(objlCredit.getNumeroCredito());
			if ( ciclo.idCreditoIBS != 0 ){
				new CicloGrupalDAO().updateCicloCredito(ciclo);
				grupo.ciclos[ciclo.idCiclo-1] = ciclo;
				session.setAttribute("GRUPO", grupo);
			}

		}catch(Exception e){
			//notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE ,"No se logr� registrar el CR�DITO en el sistema de cartera IBS -- " + e.getMessage());
			notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE ,"No se logró registrar el CRÉDITO en el sistema de cartera IBS -- " + e.getMessage()));
			e.printStackTrace();
		}
		
		return objlCredit;
	}

	private void loadSources() {
		InputStream objlInputStream = null;

		objlInputStream =
			this.getClass().getClassLoader().getResourceAsStream("com/afirme/clientes/resources/sources.xml");
		try {
			objlInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static CreditoIBSVO getCreditInfo( ClienteVO cliente, SolicitudVO solicitud, String user ) {
		Calendar cal = Calendar.getInstance();
		CreditoIBSVO objlCredit = new CreditoIBSVO();
		Double tasaIva = 0.000;
		TasaInteresVO tasaInteres = null;
		ComisionVO comision = null;
		boolean esReestructura = solicitud.reestructura==1 ? true : false;
		
		try{
			SucursalVO sucursal = new SucursalDAO().getSucursal(cliente.idSucursal);
			if ( Convertidor.esFronterizo(cliente.idSucursal) )
				tasaIva = ClientesConstants.TASA_IVA_FRONTERIZO;
			else
				tasaIva = ClientesConstants.TASA_IVA_NO_FRONTERIZO;

			TreeMap<Integer,ComisionVO> catComisiones = CatalogoHelper.getCatalogoComisiones( solicitud.tipoOperacion );
			TreeMap<Integer,TasaInteresVO> catTasas = CatalogoHelper.getCatalogoTasas( solicitud.tipoOperacion );
			double primaConComision = SeguroHelper.obtenPrimaConComision(solicitud, catComisiones);
			tasaInteres = catTasas.get(solicitud.decisionComite.tasa);
			comision = catComisiones.get(solicitud.decisionComite.comision);
			objlCredit.setNumeroClienteSicap( String.valueOf(cliente.idCliente) );
			objlCredit.setNumeroClienteIBS( String.valueOf(cliente.idClienteIBS) );
			objlCredit.setNumeroReferencia( solicitud.referencia );
			objlCredit.setNumeroCuenta( String.valueOf(solicitud.idCuentaIBS) );
			objlCredit.setSucursalID( String.valueOf(cliente.idSucursal) );
			objlCredit.setFechaAutorizacion( Convertidor.formatDateCirculo(Convertidor.dateToString(solicitud.amortizacion[0].fechaPago)) );
			objlCredit.setFechaDesembolso( Convertidor.formatDateCirculo(Convertidor.dateToString(solicitud.amortizacion[0].fechaPago)) );
			objlCredit.setFechaAmortizacion1( Convertidor.formatDateCirculo(Convertidor.dateToString(solicitud.amortizacion[1].fechaPago)) );
			objlCredit.setFechaAmortizacion2( Convertidor.formatDateCirculo(Convertidor.dateToString(solicitud.amortizacion[2].fechaPago)) );
			objlCredit.setFechaVencimiento( Convertidor.formatDateCirculo(Convertidor.dateToString(solicitud.amortizacion[solicitud.amortizacion.length-1].fechaPago)) );
			objlCredit.setValorCuota( String.valueOf(solicitud.amortizacion[1].montoPagar) );
			objlCredit.setNumeroCuotas( String.valueOf(solicitud.amortizacion.length-1) );
			objlCredit.setFrecuencia( String.valueOf(solicitud.decisionComite.frecuenciaPago) );
			objlCredit.setMontoCredito( String.valueOf(solicitud.decisionComite.montoAutorizado+primaConComision) );
			objlCredit.setMontoDesembolso( String.valueOf(ClientesUtil.calculaMontoSinComision(solicitud.decisionComite.montoAutorizado, solicitud.decisionComite.comision, catComisiones)) );
			//Modificar en cuanto se reactiven los seguros
			objlCredit.setMontoSeguroVida( String.valueOf(solicitud.seguro.primaTotal) );
			objlCredit.setMontoComision( String.valueOf(solicitud.amortizacion[0].comisionInicial) );
			objlCredit.setMontoIvaComision( String.valueOf(solicitud.amortizacion[0].ivaComision) );
			objlCredit.setTasaAnual( String.valueOf( solicitud.tasaCalculada/(1+tasaIva) ) );
			objlCredit.setTasaAnualIVA( String.valueOf( solicitud.tasaCalculada ) );
			objlCredit.setTasaMoratoria( String.valueOf( solicitud.tasaCalculada*2 ) );
			objlCredit.setTasaComision( String.valueOf( comision.porcentaje ) );
			objlCredit.setTasaIva( FormatUtil.completaCadena(String.valueOf( tasaIva*100 ), '0', 6, "R") );
			if( esReestructura ){
				TreeMap catProductosReestructura = CatalogoHelper.getCatalogoReestructurasIBS(); 
				objlCredit.setProducto( FormatUtil.completaCadena( (String)catProductosReestructura.get(solicitud.tipoOperacion), '0', 4, "L") );
			}else {
				objlCredit.setProducto( FormatUtil.completaCadena(String.valueOf(solicitud.tipoOperacion), '0', 4, "L") );
			}
			
			//********************Modificar para parametrizar con cat�logo de fondeadores
			objlCredit.setFondeador( FormatUtil.completaCadena("1", '0', 4, "L") );
			//********************Modificar para parametrizar con cat�logo de bancos
			if ( sucursal.idBanco == 2 )
				objlCredit.setBancoDesembolso( FormatUtil.completaCadena("72", '0', 4, "L") );
			else if ( sucursal.idBanco == 0 )
				objlCredit.setBancoDesembolso( FormatUtil.completaCadena("21", '0', 4, "L") );
			if ( solicitud.reestructura == 1 ){
				String referenciaAnterior = new ReferenciaGeneralDAO().getReferencia(cliente.idCliente, solicitud.solicitudReestructura, 'I');
				objlCredit.setReferenciaReestructurado(referenciaAnterior.replace("LD", "99"));
				objlCredit.setBancoDesembolso( "5000" );
			}
			objlCredit.setOrdenPago( solicitud.referencia );
			objlCredit.setNumeroDesembolso( String.valueOf(solicitud.idSolicitud) );
			objlCredit.setUser( user );
			objlCredit.setFechaSistema( Convertidor.formatDateCirculo(Convertidor.dateToString(cal.getTime())) );
			objlCredit.setHoraSistema( FormatUtil.deleteChar(HTMLHelper.getHoraActual(), ':') );
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return objlCredit;
	}

	private static CreditoIBSVO getCreditGroupInfo( GrupoVO grupo, CicloGrupalVO ciclo, String user ) {
		Calendar cal = Calendar.getInstance();
		CreditoIBSVO objlCredit = new CreditoIBSVO();
		Double tasaIva = 0.000;
		Double montoSeguro = 0.00;
		TasaInteresVO tasaInteres = null;
		ComisionVO comision = null;
		double montoDesembolso = 0.00;

		try{
			TreeMap<Integer,ComisionVO> catComisiones = CatalogoHelper.getCatalogoComisiones( ClientesConstants.GRUPAL );
			TreeMap<Integer,TasaInteresVO> catTasas = CatalogoHelper.getCatalogoTasas( ClientesConstants.GRUPAL );

			SucursalVO sucursal = new SucursalDAO().getSucursal(grupo.sucursal);

			if ( Convertidor.esFronterizo(grupo.sucursal) )
				tasaIva = ClientesConstants.TASA_IVA_FRONTERIZO;
			else
				tasaIva = ClientesConstants.TASA_IVA_NO_FRONTERIZO;

            for(int i=0; i<ciclo.integrantes.length; i++){
                montoSeguro += ciclo.integrantes[i].primaSeguro;
                montoDesembolso+=ClientesUtil.calculaMontoSinComision(ciclo.integrantes[i].monto, ciclo.integrantes[i].comision, catComisiones);
            }

			tasaInteres = catTasas.get(ciclo.tasa);
			comision = catComisiones.get(ciclo.comision);
			objlCredit.setNumeroClienteSicap( String.valueOf(grupo.idGrupo) );
			objlCredit.setNumeroClienteIBS( String.valueOf(grupo.idGrupoIBS) );
			objlCredit.setNumeroReferencia( ciclo.referencia );
			objlCredit.setNumeroCuenta( String.valueOf(ciclo.idCuentaIBS) );
			objlCredit.setSucursalID( String.valueOf(grupo.sucursal) );
			objlCredit.setFechaAutorizacion( Convertidor.formatDateCirculo(Convertidor.dateToString(ciclo.fechaCaptura)) );
			objlCredit.setFechaDesembolso( Convertidor.formatDateCirculo(Convertidor.dateToString(ciclo.tablaAmortizacion[0].fechaPago)) );
			objlCredit.setFechaAmortizacion1( Convertidor.formatDateCirculo(Convertidor.dateToString(ciclo.tablaAmortizacion[1].fechaPago)) );
			objlCredit.setFechaAmortizacion2( Convertidor.formatDateCirculo(Convertidor.dateToString(ciclo.tablaAmortizacion[2].fechaPago)) );
			objlCredit.setFechaVencimiento( Convertidor.formatDateCirculo(Convertidor.dateToString(ciclo.tablaAmortizacion[ciclo.tablaAmortizacion.length-1].fechaPago)) );
			objlCredit.setValorCuota( String.valueOf(ciclo.tablaAmortizacion[1].montoPagar) );
			objlCredit.setNumeroCuotas( String.valueOf(ciclo.tablaAmortizacion.length-1) );
			objlCredit.setFrecuencia( String.valueOf(ClientesConstants.PAGO_SEMANAL) );
			objlCredit.setMontoCredito( String.valueOf(ciclo.montoConComision) );
			objlCredit.setMontoDesembolso( String.valueOf(montoDesembolso) );
			objlCredit.setMontoSeguroVida( String.valueOf(montoSeguro) );
			objlCredit.setMontoComision( String.valueOf(ciclo.tablaAmortizacion[0].comisionInicial) );
			objlCredit.setMontoIvaComision( String.valueOf(ciclo.tablaAmortizacion[0].ivaComision) );
			objlCredit.setTasaAnual( String.valueOf( ciclo.tasaCalculada/(1+tasaIva) ) );
			objlCredit.setTasaAnualIVA( String.valueOf( ciclo.tasaCalculada ) );
			objlCredit.setTasaMoratoria( String.valueOf( ciclo.tasaCalculada*2 ) );
			objlCredit.setTasaComision( String.valueOf(100 - ((ciclo.monto*100)/ciclo.montoConComision)) );
			objlCredit.setTasaIva( FormatUtil.completaCadena(String.valueOf( tasaIva*100 ), '0', 6, "R") );
			objlCredit.setProducto( FormatUtil.completaCadena(String.valueOf(ClientesConstants.GRUPAL), '0', 4, "L") );
			//********************Modificar para parametrizar con cat�logo de fondeadores
			objlCredit.setFondeador( FormatUtil.completaCadena("1", '0', 4, "L") );
			//********************Modificar para parametrizar con cat�logo de bancos
			if ( sucursal.idBanco == 2 )
				objlCredit.setBancoDesembolso( FormatUtil.completaCadena("72", '0', 4, "L") );
			else if ( sucursal.idBanco == 0 )
				objlCredit.setBancoDesembolso( FormatUtil.completaCadena("21", '0', 4, "L") );
			if ( grupo.idOperacion == ClientesConstants.REESTRUCTURA_GRUPAL ){
				String referenciaAnterior = new ReferenciaGeneralDAO().getReferencia(grupo.idGrupoOriginal, grupo.idCicloOriginal, 'G');
				objlCredit.setReferenciaReestructurado(referenciaAnterior.replace("LD", "99"));
				objlCredit.setBancoDesembolso( "5000" );
				objlCredit.setProducto( FormatUtil.completaCadena( "0006", '0', 4, "L") );
			}
//			objlCredit.setBancoDesembolso( FormatUtil.completaCadena("62", '0', 4, "L") );
			objlCredit.setOrdenPago( ciclo.referencia );
			objlCredit.setNumeroDesembolso( String.valueOf(ciclo.idCiclo) );
			objlCredit.setUser( user );
			objlCredit.setFechaSistema( Convertidor.formatDateCirculo(Convertidor.dateToString(cal.getTime())) );
			objlCredit.setHoraSistema( FormatUtil.deleteChar(HTMLHelper.getHoraActual(), ':') );
			//CreditoIBSDAO.printValues(objlCredit);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return objlCredit;
	}

}