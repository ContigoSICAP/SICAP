package com.sicap.clientes.vo.inffinix;
import com.sicap.clientes.vo.PagoVO;
import com.sicap.clientes.vo.SaldoT24VO;
import com.sicap.clientes.vo.SolicitudVO;

public class SolicitudInfinix extends SolicitudVO{
	
	public SaldoT24VO saldo;
	public PagoVO[] pagos;
	public String referencia;
	
	public SolicitudInfinix(){
		super();
		saldo = null;
		pagos = null;
		referencia = null;
	}
}
