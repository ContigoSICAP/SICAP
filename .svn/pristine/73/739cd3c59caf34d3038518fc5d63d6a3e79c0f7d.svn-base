<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %><%@ page import="com.sicap.clientes.vo.ClienteVO"%><%@ page import="com.sicap.clientes.helpers.ArchivosAsociadosHelper"%><jsp:useBean id="mySmartUpload" scope="page" class="com.jspsmart.upload.SmartUpload" /><%
mySmartUpload.initialize(pageContext);
mySmartUpload.upload();
//mySmartUpload.setContentDisposition("attachment");
ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
int idCliente = cliente.idCliente;
int idSolicitud = Integer.parseInt( mySmartUpload.getRequest().getParameter("idSolicitud") );
int tipo = Integer.parseInt( mySmartUpload.getRequest().getParameter("tipo") );
String ruta = ArchivosAsociadosHelper.getRutaArchivo(idCliente, idSolicitud);
String nombreArchivo = ArchivosAsociadosHelper.getNombreArchivo(cliente, idSolicitud, tipo);
mySmartUpload.downloadFile(ruta+nombreArchivo, "application/octet-stream", nombreArchivo);%>