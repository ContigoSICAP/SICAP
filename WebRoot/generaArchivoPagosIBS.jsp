<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp"%><%@ page import="java.util.*"%><%@ page import="com.sicap.clientes.vo.*"%><%@ page import="com.sicap.clientes.util.*"%><%@ page import="com.sicap.clientes.helpers.*"%><%@ page import="com.sicap.clientes.dao.PagoDAO"%><%
	//CommandProcesarPagoReferenciado cmdPagoRef = new CommandProcesarPagoReferenciado();
	//OBTIENE LOS PAGOS DE UN PROCESO EN PARTICULAR
	ArrayList<PagoVO> pagos = new PagoDAO().getPagosIBS();

	//Archivo de salida
	response.setContentType("text/plain");
	response.setHeader("Content-Disposition","attachment; filename=\"pagosIBS.txt\"");
	response.setHeader("cache-control", "no-cache");
	Iterator<PagoVO> it =pagos.iterator();
	while (it.hasNext()){
		PagoVO pago = (PagoVO) it.next();

		// Obteniendo fecha transaccion en formato de IBS
		String fechaTransaccion = pago.fechaPago.toString().replace("-","");

		// Obteniendo fecha y hora de registro(SICAP) en formato de IBS
		String [] fechaHora = pago.fechaHora.toString().split(" ");
		String fechaSICAP = fechaHora[0].replace("-","");
		String horaSICAP = fechaHora[1].replace(":","").replace(".","");

		// Obteniendo monto en formato de IBS
		String monto = String.valueOf(pago.monto);
		String montoSinDecimal = monto.substring(0, monto.indexOf("."));
		String decimales = monto.substring(monto.indexOf(".")+1,monto.length());
		montoSinDecimal = FormatUtil.completaCadena(montoSinDecimal,'0',13);
		decimales = FormatUtil.completaCadena(decimales,'0',2,"R");

		// Obteniendo id banco en formato de IBS
		String idBanco = CatalogoHelper.getIDCNBV(Integer.parseInt(String.valueOf(pago.bancoReferencia))).substring(1);

		// Obteniendo # de crédito en IBS
		String numcreditoIBS = SolicitudHelper.obtenNumeroCreditoIBS(pago.referencia);
		out.println(fechaTransaccion + fechaSICAP + horaSICAP + idBanco + pago.referencia + montoSinDecimal + decimales + numcreditoIBS + 'N' + 'N');

	}
%>