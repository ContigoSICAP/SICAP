<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%><%@ page import="com.sicap.clientes.vo.*"%><%   
if(request.getAttribute("PAGOS_CARTERA")!=null){
	ArrayList<PagoVO> pagoCartera = (ArrayList<PagoVO>)request.getAttribute("PAGOS_CARTERA");
	int contador=0;
	//Archivo de salida
	response.setContentType("application/csv");
	response.setHeader("Content-Disposition","attachment; filename=\"Salida.csv\"");
	response.setHeader("cache-control", "no-cache");
    int id_consecutivo=0;
    
	while( contador<pagoCartera.size() ){
		if(id_consecutivo > 99 )
			id_consecutivo = 0;
		if ( pagoCartera.get(contador).destino!=3 ){
			out.println(pagoCartera.get(contador).fechaPago+","+pagoCartera.get(contador).referencia+","+ ( (pagoCartera.get(contador).fechaHora.getTime()) + (id_consecutivo++) )  +","+
			pagoCartera.get(contador).monto+","+pagoCartera.get(contador).bancoReferencia+","+pagoCartera.get(contador).sucursal);
		}
		contador++;
	}
	out.flush();
}%>
