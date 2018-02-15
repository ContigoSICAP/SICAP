package com.sicap.clientes.dao.ibs;

/*
 * Created on Jan 21, 2009
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
import org.apache.log4j.Logger;

import com.sicap.clientes.vo.ibs.CreditoIBSVO;
import com.afirme.commons.model.dao.AS400DAO;
import com.afirme.commons.model.vo.ResultVO;
import com.afirme.commons.model.vo.SourceVO;
import com.afirme.commons.util.Log4jUtils;

import datapro.eibs.beans.CRF000401Message;


/**
 * @author jasvazher
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class CreditoIBSDAO extends AS400DAO {

		private final Logger logger =
			Log4jUtils.getLogger(this.getClass().getName());

		/**
		 * Instance of this DAO
		 */
		private static CreditoIBSDAO instance = null;

		private CreditoIBSDAO() {
			super();
		}

		/**
		 * Returns an instance of this DAO
		 * this DAO is a <code>Singleton</code>,
		 * therefore always returns the same instance.
		 *
		 * @return an instance of this DAO.
		 */
		public static synchronized CreditoIBSDAO getInstance() {
			if (instance == null) {
				instance = new CreditoIBSDAO();
			}
			return instance;
		}

		public void saveCredit(CreditoIBSVO objlClient, String saLoggedUser)
			throws Exception {
			SourceVO objlSource = null;
			ResultVO objlResult = null;
			CRF000401Message objlBean = null;
			objlSource = getSource("createCredit", saLoggedUser);
			objlSource.addField("H01USERID", "SOFCFIRME");
			//objlSource.addField("H01USERID", "FABCFIRME");
			objlSource.addField("E01CSI", objlClient.getNumeroClienteSicap());
			objlSource.addField("E01CUN", objlClient.getNumeroClienteIBS());
			objlSource.addField("E01REF", objlClient.getNumeroReferencia());
			objlSource.addField("E01ACC", objlClient.getNumeroCredito());
			objlSource.addField("E01AC1", objlClient.getNumeroCuenta());
			objlSource.addField("E01BRN", objlClient.getSucursalID());
			objlSource.addField("E01FAU", objlClient.getFechaAutorizacion());
			objlSource.addField("E01FDE", objlClient.getFechaDesembolso());
			objlSource.addField("E01FA1", objlClient.getFechaAmortizacion1());
			objlSource.addField("E01FA2", objlClient.getFechaAmortizacion2());
			objlSource.addField("E01FVE", objlClient.getFechaVencimiento());
			objlSource.addField("E01VCU", objlClient.getValorCuota());
			objlSource.addField("E01NCU", objlClient.getNumeroCuotas());
			objlSource.addField("E01FPA", objlClient.getFrecuencia());
			objlSource.addField("E01OAM", objlClient.getMontoCredito());
			objlSource.addField("E01MDE", objlClient.getMontoDesembolso());
			objlSource.addField("E01MSE", objlClient.getMontoSeguroVida());
			objlSource.addField("E01MCS", objlClient.getMontoComision());
			objlSource.addField("E01MIC", objlClient.getMontoIvaComision());
			objlSource.addField("E01RTE", objlClient.getTasaAnual());
			objlSource.addField("E01TIN", objlClient.getTasaAnualIVA());
			objlSource.addField("E01TIM", objlClient.getTasaMoratoria());
			objlSource.addField("E01TCO", objlClient.getTasaComision());
			objlSource.addField("E01TIV", objlClient.getTasaIva());
			objlSource.addField("E01CDE", objlClient.getProducto());
			objlSource.addField("E01FON", objlClient.getFondeador());
			objlSource.addField("E01BDE", objlClient.getBancoDesembolso());
			objlSource.addField("E01ORD", objlClient.getOrdenPago());
			objlSource.addField("E01NDE", objlClient.getNumeroDesembolso());
			objlSource.addField("E01REE", objlClient.getReferenciaReestructurado());
			objlSource.addField("E01USI", objlClient.getUser());
			objlSource.addField("E01FSI", objlClient.getFechaSistema());
			objlSource.addField("E01HSI", objlClient.getHoraSistema());
			//printValues(objlClient);

			objlResult = getResult(objlSource, logger);
			throwBussinesErrors(objlResult);
			objlBean = (CRF000401Message) objlResult.getData();
			objlClient.setNumeroCredito(objlBean.getE01ACC());
		}

		public static void printValues( CreditoIBSVO objlClient ){
			System.out.println("getNumeroClienteSicap: " + objlClient.getNumeroClienteSicap() );
			System.out.println("getNumeroClienteIBS: " + objlClient.getNumeroClienteIBS());
			System.out.println("getNumeroReferencia: " + objlClient.getNumeroReferencia());
			System.out.println("getNumeroCredito: " + objlClient.getNumeroCredito());
			System.out.println("getNumeroCuenta: " + objlClient.getNumeroCuenta());
			System.out.println("getSucursalID: " + objlClient.getSucursalID());
			System.out.println("getFechaAutorizacion: " + objlClient.getFechaAutorizacion());
			System.out.println("getFechaDesembolso: " + objlClient.getFechaDesembolso());
			System.out.println("getFechaAmortizacion1: " + objlClient.getFechaAmortizacion1());
			System.out.println("getFechaAmortizacion2: " + objlClient.getFechaAmortizacion2());
			System.out.println("getFechaVencimiento: " + objlClient.getFechaVencimiento());
			System.out.println("getValorCuota: " + objlClient.getValorCuota());
			System.out.println("getNumeroCuotas: " + objlClient.getNumeroCuotas());
			System.out.println("getFrecuencia: " + objlClient.getFrecuencia());
			System.out.println("getMontoCredito: " + objlClient.getMontoCredito());
			System.out.println("getMontoDesembolso: " + objlClient.getMontoDesembolso());
			System.out.println("getMontoSeguroVida: " + objlClient.getMontoSeguroVida());
			System.out.println("getMontoComision: " + objlClient.getMontoComision());
			System.out.println("getMontoIvaComision: " + objlClient.getMontoIvaComision());
			System.out.println("getTasaAnual: " + objlClient.getTasaAnual());
			System.out.println("getTasaAnualIVA: " + objlClient.getTasaAnualIVA());
			System.out.println("getTasaMoratoria: " + objlClient.getTasaMoratoria());
			System.out.println("getTasaComision: " + objlClient.getTasaComision());
			System.out.println("getTasaIva: " + objlClient.getTasaIva());
			System.out.println("getProducto: " + objlClient.getProducto());
			System.out.println("getFondeador: " + objlClient.getFondeador());
			System.out.println("getBancoDesembolso: " + objlClient.getBancoDesembolso());
			System.out.println("getOrdenPago: " + objlClient.getOrdenPago());
			System.out.println("getNumeroDesembolso: " + objlClient.getNumeroDesembolso());
			System.out.println("getReferenciaReestructurado: " + objlClient.getReferenciaReestructurado());
			System.out.println("getUser: " + objlClient.getUser());
			System.out.println("getFechaSistema: " + objlClient.getFechaSistema());
			System.out.println("getHoraSistema: " + objlClient.getHoraSistema());

		}
	}

