<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %><%@ page import="com.sicap.clientes.helpers.*,java.util.*,com.sicap.clientes.util.*,com.sicap.clientes.vo.ClienteVO ,com.sicap.clientes.vo.InformacionCrediticiaVO"%><%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server

	Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
	InformacionCrediticiaVO infoCrediticia = new InformacionCrediticiaVO();
	ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
	int idSolicitud  = HTMLHelper.getParameterInt(request,"idSolicitud");
	int	idObligado = HTMLHelper.getParameterInt(request, "idObligado");
	int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
	String persona = (String)request.getAttribute("persona");

	if ( persona.equals("cliente") )
		infoCrediticia = cliente.solicitudes[indiceSolicitud].infoCreditoBuro;
	else if ( persona.equals("obligado") )
		infoCrediticia = cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado-1].infoCreditoBuro;
	
		//Hashtable res = (Hashtable)session.getAttribute("BURO");
		if(notificaciones==null){
			if ( infoCrediticia != null ){
				Hashtable res = ParserStringHelper.getDataCliente(infoCrediticia.respuesta);
				response.setContentType("application/pdf");
				response.setHeader("Content-Disposition","attachment; filename=\"BuroCliente.pdf\"");
				response.setHeader("cache-control", "no-cache");
				PDFHelper doc = new PDFHelper();
				doc.pdfWriter(request,response,res);
			}else{%>
			<body>
				<%@ include file="header.jsp" %>
					<center><h4><%=HTMLHelper.displayNotifications(notificaciones)%></h4></center>
	    			<center><h4>Registro no encontrado en Buró de crédito</h4></center>
	    			<center><a href="javascript:window.close();">Cerrar ventana</a></center>
				<%@ include file="footer.jsp" %>
			</body>
			<%}
		}else{%>
			<body>
				<%@ include file="header.jsp" %>
					<center><h4><%=HTMLHelper.displayNotifications(notificaciones)%></h4></center>
	    			<center><a href="javascript:window.close();">Cerrar ventana</a></center>
				<%@ include file="footer.jsp" %>
			</body>
			<%}%>