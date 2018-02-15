package com.sicap.clientes.vo.inffinix;

import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.DireccionVO;

public class ClienteInffinix extends ClienteVO{


	public DireccionVO direcciones[];
	public SolicitudInfinix solicitudes[];

	public ClienteInffinix() {

		direcciones = null;
		solicitudes = null;
	}
}