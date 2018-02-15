package com.sicap.clientes.dao.ibs;

/*
 * Created on Jan 21, 2009
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
import java.util.List;

import org.apache.log4j.Logger;

import com.sicap.clientes.vo.ibs.BucketIBSVO;
import com.afirme.commons.model.dao.AS400DAO;
import com.afirme.commons.model.vo.ResultVO;
import com.afirme.commons.model.vo.SourceVO;
import com.afirme.commons.util.Log4jUtils;

import datapro.eibs.beans.CRF003801Message;


/**
 * @author jasvazher
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class BucketIBSDAO extends AS400DAO {

	private final Logger logger =
		Log4jUtils.getLogger(this.getClass().getName());

	/**
	 * Instance of this DAO
	 */
	private static BucketIBSDAO instance = null;

	private BucketIBSDAO() {
		super();
	}

	/**
	 * Returns an instance of this DAO
	 * this DAO is a <code>Singleton</code>,
	 * therefore always returns the same instance.
	 *
	 * @return an instance of this DAO.
	 */
	public static synchronized BucketIBSDAO getInstance() {
		if (instance == null) {
			instance = new BucketIBSDAO();
		}
		return instance;
	}


	public BucketIBSVO[] getPagosBucket(BucketIBSVO objlBucket, String saLoggedUser) throws Exception {
		SourceVO objlSource = null;
		ResultVO objlResult = null;
		List result = null;
		BucketIBSVO elementos[] = null;

		objlSource = getSource("getEstadoBucket", saLoggedUser);
		objlSource.addField("H01USERID", "SOFCFIRME");
		//objlSource.addField("H01USERID", "S47CFIRME");
		objlSource.addField("E01ACC", objlBucket.getNumeroCredito());
		objlResult = getResult(objlSource, logger);
		throwBussinesErrors(objlResult);
		result = (List) objlResult.getData();
		if ( result.size()>0 ){
			elementos = new BucketIBSVO[result.size()];
			for(int i=0;i<result.size(); i++)
				elementos[i] = dataMapping((CRF003801Message)result.get(i));
		}
		return elementos;

	}


	private BucketIBSVO dataMapping( CRF003801Message data ){

		BucketIBSVO temporal = new BucketIBSVO();
		temporal.setNumeroCredito(data.getE01ACC());
		temporal.setNumeroCuenta(data.getE01CTA());
		temporal.setFechaTransaccion(data.getE01FEC());
		temporal.setConceptoTransaccion(data.getE01DSC());
		temporal.setMontoTransaccion(data.getE01MON());
		if ( data.getE01CDE()!=null && data.getE01CDE().equals("Abono") )
			temporal.setTipoTransaccion("Pago");
		else if ( data.getE01CDE()!=null && data.getE01CDE().equals("Canc.") )
			temporal.setTipoTransaccion("Cancelación");
		else
			temporal.setTipoTransaccion(data.getE01CDE());
		temporal.setSaldoCuenta(data.getE01SAL());
		//NUEVOS CAMPOS PARA ESTADO DE CUENTA SICAP
		temporal.setFechaDeposito( data.getE01RDT() );
		temporal.setCodigoBanco( data.getE01BNK() );

		return temporal;
	}

/*
	public BucketIBSVO[] getPagosBucket(BucketIBSVO objlBucket, String saLoggedUser) throws Exception {
		BucketIBSVO[] pago = new BucketIBSVO[1];
		pago[0] = new BucketIBSVO();
		pago[0].setNumeroCredito("1");
		pago[0].setNumeroCuenta("2");
		pago[0].setFechaTransaccion("20090624");//AAAAMMDD
		pago[0].setConceptoTransaccion("C");
		pago[0].setMontoTransaccion("100");
		pago[0].setTipoTransaccion("Pago");
		pago[0].setSaldoCuenta("0");
		pago[0].setFechaDeposito("0");
		pago[0].setCodigoBanco("0062");

		return pago;
	}
*/

}