package com.sicap.clientes.helpers;

import com.sicap.clientes.dao.ArchivoAsociadoDAO;
import com.sicap.clientes.dao.ClienteDAO;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.vo.ArchivoAsociadoVO;
import com.sicap.clientes.vo.ClienteVO;

public class MasivoTablasHelper {

	public static ClienteVO getIdCliente (String rfc, String nombreCompleto, ArchivoAsociadoVO temp){
		ClienteVO clientes[] = null;
		ClienteVO clienteActual = null;
		ClienteDAO dao = new ClienteDAO();
		ArchivoAsociadoVO[] archivos = null;
		ArchivoAsociadoDAO daoarchivo = new ArchivoAsociadoDAO();
		int idCliente = 0;
		int idSolicitud = 1;

		try{
			if (rfc!=null){
				clientes = dao.findByRFCTablas(rfc);
				if ( clientes!=null && clientes.length>0 ){
					clienteActual = dao.getCliente(clientes[0].idCliente);
					archivos = daoarchivo.getArchivos(idCliente, idSolicitud);
					temp.tipo = new Integer(ArchivosAsociadosHelper.getIndice(archivos, ClientesConstants.ARCHIVO_TIPO_AMORTIZACION));
				}
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return clienteActual;
	}
}
