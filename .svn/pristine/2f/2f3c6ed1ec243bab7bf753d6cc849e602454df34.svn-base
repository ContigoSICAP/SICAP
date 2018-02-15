<%@page import="com.sicap.clientes.vo.ReporteVO"%><%@ page language="java" pageEncoding="ISO-8859-1" import="java.util.*" %><%
String       filename = (String)request.getAttribute("FILENAME"); 
ReporteVO[] reportesVO     = (ReporteVO[])request.getAttribute("FILE"); 
response.setContentType("application/csv");
response.setHeader("Content-Disposition",
			       "attachment; filename=\""+filename+"\"");
response.setHeader("cache-control", "no-cache");
if( reportesVO != null ){
    String linea ="No. Grupo,Grupo,No. cliente,RFC,No. Solicitud,Nombre completo,Asesor,Fecha de Firma,Fecha de Vencimiento";
    out.println( linea );
    for (ReporteVO reportesVO1 : reportesVO) {
                linea =(reportesVO1.numGrupo + "," + reportesVO1.nombreGrupo + "," + reportesVO1.idCliente + "," + reportesVO1.rfc + "," + reportesVO1.idSolicitud + "," + reportesVO1.nombre + "," + reportesVO1.ejecutivo + "," + reportesVO1.fechaFirma + "," + reportesVO1.fechaFirma );
                out.println( linea );
            }

}%>