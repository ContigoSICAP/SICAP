<%@ page import="java.io.ByteArrayOutputStream"%><%@ page import="com.sicap.clientes.util.Notification" %><%@ page import="com.sicap.clientes.helpers.HTMLHelper"%><%
try{
	Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
    if( notificaciones != null ){%>
    	<HTML>
    		<HEAD>
    			<TITLE>Reporte de Circulo de Crédito</TITLE>
    		</HEAD>
    		<BODY>
    			<%@ include file="header.jsp" %>
					<center><h4><%=HTMLHelper.displayNotifications(notificaciones)%></h4></center>
	    			<center><a href="javascript:window.close();">Cerrar ventana</a></center>
				<%@ include file="footer.jsp" %>
    		<BODY>
    	</HTML>
    <%}else{
    	ByteArrayOutputStream filePDF = (ByteArrayOutputStream) request.getAttribute("pdf");
	    ServletOutputStream sos;
	   	if(filePDF != null){
			response.setContentType("application/pdf");
			response.setHeader("Content-disposition", "attachment; filename=circulo.pdf");
			sos = response.getOutputStream();
			filePDF.writeTo(sos);
			sos.flush();
		}	
    }
} catch(Exception e){}%>