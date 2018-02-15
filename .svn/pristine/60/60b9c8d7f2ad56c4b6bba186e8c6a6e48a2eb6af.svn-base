<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp"%><%@ page import="com.sicap.clientes.vo.*"%><%@ page import="com.sicap.clientes.util.*"%><%@ page import="com.sicap.clientes.helpers.*"%><%

int id          = -1;
int tipoArrendatario = 0;

	try{		
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition","attachment; filename=\"Hoja_de_respuesta.pdf\"");
		response.setHeader("cache-control", "no-cache");
		GeneraPDFDatosComunalHelper doc = new GeneraPDFDatosComunalHelper();			
		id = HTMLHelper.getParameterInt(request, "idSolicitud");
        ClienteVO cliente  = (ClienteVO)session.getAttribute("CLIENTE");
 		int idReferencia = HTMLHelper.getParameterInt(request,"idReferencia");						
		doc.pdfWriter(request, response, cliente, id, idReferencia);
		return;
	}

	catch(Exception e){
		Logger.debug("Exception en generaPDFDatosComunal : "+e.getMessage());
		e.printStackTrace();
	}%>