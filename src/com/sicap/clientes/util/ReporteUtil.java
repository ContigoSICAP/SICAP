package com.sicap.clientes.util;

import java.sql.Timestamp;

import com.sicap.clientes.dao.BitacoraEstatusDAO;
import com.sicap.clientes.dao.ScoringDAO;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.vo.BitacoraEstatusVO;
import com.sicap.clientes.vo.ReporteVO;
import com.sicap.clientes.vo.ScoringVO;

public class ReporteUtil {
	
	public ReporteVO[] obtenerDictamen(ReporteVO[] reportesVO)throws ClientesException{
		ScoringUtil util = new ScoringUtil(ClientesConstants.CONSUMO);
		for(int i = 0; i < reportesVO.length; i++ ){
			if(reportesVO[i].dictamen==0){
				ScoringDAO dao = new ScoringDAO(); 
				ScoringVO score = dao.getScoring(reportesVO[i].idCliente, reportesVO[i].idSolicitud);
				if(score != null){
					util.getDictamen(score);
					reportesVO[i].dictamen = score.dictamenFinal;
				}
			}
		}
		return reportesVO;
	}
	
	public ReporteVO[] obtenerUltimosUsuarios(ReporteVO[] reportesVO)throws ClientesException{
		BitacoraEstatusVO[] bitVO = null;
		BitacoraEstatusDAO bitDAO = new BitacoraEstatusDAO();
		for(int i = 0; reportesVO!=null && i < reportesVO.length; i++ ){
			bitVO = bitDAO.getUltimoRegistroInsertado(reportesVO[i].idCliente, reportesVO[i].idSolicitud);
			if(bitVO!=null){
				reportesVO[i].fechaModificacion = obtenerUltimaFecha(bitVO);
				for(int j = 0; j < bitVO.length; j++){
					if(bitVO[j].esAnalisisCredito==1){
						reportesVO[i].usuarioMesaControl = bitVO[j].usuario;
					}else{
						reportesVO[i].usuarioSucursal = bitVO[j].usuario;
					}
				}
			}else{
				reportesVO[i].fechaModificacion = null;
				reportesVO[i].usuarioMesaControl = null;
				reportesVO[i].usuarioSucursal = null;
			}
		}
		return reportesVO;
	}
	
	public Timestamp obtenerUltimaFecha(BitacoraEstatusVO[] bitVO){
		Timestamp ultimaFechaModificacion = null;
		if(bitVO!=null){
			for(int i = 0; i < bitVO.length; i++){
				if(ultimaFechaModificacion == null)
					ultimaFechaModificacion = bitVO[i].fechaHora;
				if(bitVO[i].fechaHora.after(ultimaFechaModificacion))
					ultimaFechaModificacion = bitVO[i].fechaHora; 
			}
		}
		return ultimaFechaModificacion;
	}
}