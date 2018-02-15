package com.sicap.clientes.helpers.ibs;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import com.sicap.clientes.dao.ibs.BucketIBSDAO;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.ibs.BucketIBSVO;

public class BucketHelperIBS{

	public BucketIBSVO[] consultaEstadoBucket( int idCreditoIBS, Vector notificaciones ) throws Exception {

		BucketIBSVO objlBucket = new BucketIBSVO();
		BucketIBSVO pagos[] = null;
		loadSources();

		try{
			if ( idCreditoIBS != 0 ){
				objlBucket.setNumeroCredito(String.valueOf(idCreditoIBS));
				pagos = BucketIBSDAO.getInstance().getPagosBucket(objlBucket, "S47829PETL");
			}

		}catch(Exception e){
			notificaciones.add( new Notification(ClientesConstants.ERROR_TYPE ,"No se logró consultar los movimientos de la cuenta Bucket -- " + e.getMessage()) );
			e.printStackTrace();
		}
		
		return pagos;
	}

	private void loadSources() {
		InputStream objlInputStream = null;

		try{
			objlInputStream = this.getClass().getClassLoader().getResourceAsStream("com/afirme/clientes/resources/sources.xml");
			objlInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}