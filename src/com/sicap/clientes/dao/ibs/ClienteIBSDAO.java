package com.sicap.clientes.dao.ibs;

/*
 * Created on Jan 21, 2009
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
import org.apache.log4j.Logger;

import com.sicap.clientes.vo.ibs.ClienteIBSVO;
import com.afirme.commons.model.dao.AS400DAO;
import com.afirme.commons.model.vo.ResultVO;
import com.afirme.commons.model.vo.SourceVO;
import com.afirme.commons.util.Log4jUtils;

import datapro.eibs.beans.CRF000201Message;


/**
 * @author jasvazher
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public class ClienteIBSDAO extends AS400DAO {

		private final Logger logger =
			Log4jUtils.getLogger(this.getClass().getName());

		/**
		 * Instance of this DAO
		 */
		private static ClienteIBSDAO instance = null;

		private ClienteIBSDAO() {
			super();
		}

		/**
		 * Returns an instance of this DAO
		 * this DAO is a <code>Singleton</code>,
		 * therefore always returns the same instance.
		 *
		 * @return an instance of this DAO.
		 */
		public static synchronized ClienteIBSDAO getInstance() {
			if (instance == null) {
				instance = new ClienteIBSDAO();
			}
			return instance;
		}

		public void saveClient(ClienteIBSVO objlClient, String saLoggedUser)
			throws Exception {
			SourceVO objlSource = null;
			ResultVO objlResult = null;
			CRF000201Message objlBean = null;
			objlSource = getSource("createClient", saLoggedUser);
			objlSource.addField("H01USERID", "SOFCFIRME");
			//objlSource.addField("H01USERID", "FABCFIRME");
			objlSource.addField("E1CLIENTE", objlClient.getClienteID());
			objlSource.addField("E1CUENTA", objlClient.getNumeroCuenta());
			objlSource.addField("E1CTEGPO", objlClient.getNumeroClienteSicap());
			objlSource.addField("E1APEPAT", objlClient.getApellidoPaterno());
			objlSource.addField("E1APEMAT", objlClient.getApellidoMaterno());
			String nombre = objlClient.getPrimerNombre();
			if ( nombre != null){
				if ( nombre.length() > 30 )
					nombre = nombre.substring(0, 29);
			}
			objlSource.addField("E1NOMBRE", nombre);
			//objlSource.addField("E1NOMBRE2", objlClient.getSegundoNombre());
			objlSource.addField("E1MESNAC", objlClient.getMesNacimiento());
			objlSource.addField("E1DIANAC", objlClient.getDiaNacimiento());
			objlSource.addField("E1ANONAC", objlClient.getAnioNacimiento());
			objlSource.addField("E1CALLE", objlClient.getCalleNumero());
			objlSource.addField("E1COLONIA", objlClient.getColonia());
			objlSource.addField("E1CODCIUD", objlClient.getCodigoCiudad());
			objlSource.addField("E1CODPAIS", objlClient.getCodigoPais());
			objlSource.addField("E1CODESTA", objlClient.getCodigoEstado());
			objlSource.addField("E1CODCOL", objlClient.getCodigoColonia());
			objlSource.addField("E1POBLAC", objlClient.getCiudad());
			objlSource.addField("E1ESTADO", objlClient.getEstado());
			objlSource.addField("E1PAIS", objlClient.getPais());
			objlSource.addField("E1SEXO", objlClient.getSexo());
			objlSource.addField("E1EDOCIV", objlClient.getEstadoCivil());
			objlSource.addField("E1RFC", objlClient.getRfc());
			objlSource.addField("E1TELPART", objlClient.getTelefono());
			objlSource.addField("E1CODPOS", objlClient.getCodigoPostal());
			objlSource.addField("E1CURP", objlClient.getCurp());
			objlSource.addField("E1STSCTE", objlClient.getStatusCliente());
			objlSource.addField("E1USRSICAP", objlClient.getUsuarioSicap());
			objlSource.addField("E1FECALT", objlClient.getFechaAlta());
			objlResult = getResult(objlSource, logger);
			throwBussinesErrors(objlResult);
			objlBean = (CRF000201Message) objlResult.getData();
			objlClient.setClienteID(objlBean.getE1CLIENTE());
			objlClient.setNumeroCuenta(objlBean.getE1CUENTA());
		}

	}

