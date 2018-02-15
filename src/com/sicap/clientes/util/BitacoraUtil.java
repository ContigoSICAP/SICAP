package com.sicap.clientes.util;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.BitacoraDAO;
import com.sicap.clientes.dao.BitacoraEstatusDAO;
import com.sicap.clientes.vo.BitacoraEstatusVO;
import com.sicap.clientes.vo.BitacoraVO;
import com.sicap.clientes.vo.SolicitudVO;


public class BitacoraUtil {

	private BitacoraVO evento;
	
	public BitacoraUtil(int idCliente, String usuario, String comando){
		evento = new BitacoraVO();
		evento.idCliente = idCliente;
		evento.usuario = usuario;
		evento.comando = comando;
	}

    public BitacoraUtil() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

	public void registraEvento(Object objeto) throws Exception{
		BitacoraDAO bitdao = new BitacoraDAO();
                evento.objeto = objeto;
                bitdao.add(evento);
	}
        
	public void registraEventoString(String objeto) throws Exception{
		BitacoraDAO bitdao = new BitacoraDAO();
		evento.objeto = objeto;
		bitdao.add(evento);
	}
	
	public void registraCambioEstatus(SolicitudVO solicitud, HttpServletRequest request) throws Exception{
		BitacoraEstatusVO bitVO = new BitacoraEstatusVO();
		BitacoraEstatusDAO bitDAO = new BitacoraEstatusDAO();
		if(request.isUserInRole("ANALISIS_CREDITO"))
			bitVO.esAnalisisCredito = 1;
		bitVO.estatus = solicitud.estatus;
		bitVO.idCliente = solicitud.idCliente;
		bitVO.idSolicitud = solicitud.idSolicitud;
		bitVO.usuario = request.getRemoteUser();
                bitVO.numComentario = bitDAO.getNumComentario(solicitud.idCliente, solicitud.idSolicitud)+1;
		bitDAO.add(bitVO);
	}
        public void registraCambioEstatus(BitacoraEstatusVO bitVO, HttpServletRequest request) throws Exception{
		
		BitacoraEstatusDAO bitDAO = new BitacoraEstatusDAO();
		if(request.isUserInRole("ANALISIS_CREDITO"))
			bitVO.esAnalisisCredito = 1;
                bitVO.numComentario = bitDAO.getNumComentario(bitVO.idCliente, bitVO.idSolicitud)+1;
		bitVO.usuario = request.getRemoteUser();
		bitDAO.add(bitVO);
	}
	
	public void decideInsercionCambioEstatus(int estatusPrevio, SolicitudVO solicitud, HttpServletRequest request) throws Exception{
		if(estatusPrevio != solicitud.estatus)
			registraCambioEstatus(solicitud, request);
	}

}
