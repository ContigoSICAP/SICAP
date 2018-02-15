<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.*"%>
<jsp:directive.page import="com.sicap.clientes.util.SolicitudUtil"/>

<html>
<head>
<title>Consulta a Buró de Crédito</title>
<meta http-equiv="expires" content="0">
<script language="Javascript" src="./js/functions.js"></script>
<script>

	function consultaBuro(buro){
		window.document.forma.buro.value=buro;
		if(buro==false){
			if (confirm('Esta usted seguro de realizar una nueva consulta a Circulo de Crédito?')) {
				window.document.forma.buro.value=buro;
				window.document.forma.command.value='obtieneRespuestaCirculo';
				window.document.forma.submit();				
			}
		}else{
				window.document.forma.buro.value=buro;
				window.document.forma.command.value='obtieneRespuestaCirculo';
				window.document.forma.submit();
		}
	}

	
</script>

<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>
<% 
	String respBuro = "";
	int idSolicitud  = HTMLHelper.getParameterInt(request,"idSolicitud");
	int idObligado  = HTMLHelper.getParameterInt(request,"idObligado");
	ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
	int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);

	boolean circulo = false;
	Date fechaBuro = new Date();
	String persona = (String)request.getParameter("persona");
	String nombreObligado = "";
	
	if(persona.equals("cliente")){
		if( cliente.solicitudes[indiceSolicitud].infoCreditoCirculo != null ){
			respBuro = cliente.solicitudes[indiceSolicitud].infoCreditoCirculo.respuesta;
			circulo = true;
			fechaBuro = cliente.solicitudes[indiceSolicitud].infoCreditoCirculo.fechaConsulta;
		}
	
	}else if(persona.equals("obligado")){
		if( cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado-1].infoCreditoCirculo != null){
			respBuro = cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado-1].infoCreditoCirculo.respuesta;
			circulo = true;
			fechaBuro = cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado-1].infoCreditoCirculo.fechaConsulta;
		}
		
		nombreObligado = cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado-1].nombre + " " 
					   + cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado-1].aPaterno + " " 
					   + cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado-1].aMaterno;
	}
%>

<body bgcolor="white" bgproperties=fixed>

    <center><h3>Consulta a Circulo de Crédito</h3></center>
    <center><table>
<%if(persona.equals("cliente")){ %>
		<tr>
    		<td><b>id Cliente:</b> </td> <td><%=cliente.idCliente%></td>
		</tr>
    	<tr>
			<td><b>Nombre:</b> </td> <td><%= cliente.nombreCompleto %></td>
    	</tr>
<%}else{ %>
    	<tr>
			<td><b>Nombre:</b> </td> <td><%= nombreObligado %></td>
    	</tr>
<%} %>
    </table>
	</center>

<center>

<form name="forma" action="controller" method="post">
<input type="hidden" name="idSolicitud" value="<%=idSolicitud%>">
<input type="hidden" name="idObligado" value="<%=idObligado%>">
<input type="hidden" name="persona" value="<%=persona%>">
<table border="0" width="100%" cellspacing="0" align="center" height="10%">
<%if(!circulo){ %>
	<tr>
		<td colspan="2" height="10%" align="center">El cliente no cuenta con información de Buró de Crédito en el sistema,<br>
		para obtener el reporte presionar <b>"Consultar a Circulo"</b>
		</td>
	</tr>
	<%if ( request.isUserInRole("CONSULTA_CREDITICIA_NUEVA") || request.isUserInRole("CONSULTA_CREDITICIA_OBLIGADO_NUEVA") ){%>
		<tr>
			<td align="center" colspan="2">
			<br><input type="button" value="Consultar a Circulo" onClick="consultaBuro(false);this.disabled=true;this.value='Obteniendo Reporte de Círculo'"">	
			</td>
		</tr>
	<%} %>
<%}else{ %>

	<tr>
		<td colspan="2" height="10%" align="center">El cliente cuenta con un reporte de Buró de crédito con fecha de:</td>
	</tr>
	<tr>
		<td colspan="2" align="center"><a HREF="#" onClick="consultaBuro(true);"><b><font size="+1"><%=HTMLHelper.displayField(fechaBuro)+"<br>De Click aquí para consultar este reporte"%></font></b></a></td>
	</tr>	
	<tr>
		<td colspan="2" align="center"><b>_________________________________________________________________________</b></td>
	</tr>

<%if ( request.isUserInRole("CONSULTA_CREDITICIA_NUEVA") || request.isUserInRole("CONSULTA_CREDITICIA_OBLIGADO_NUEVA") ){%>
	<tr>
		<td align="center" colspan="2">
				Para obtener un nuevo reporte actualizado de Buró de crédito pulse el botón "Nueva consulta"
		<br><input type="button" value="Nueva consulta" onClick="consultaBuro(false);">
		</td>
	</tr>
<%} %>

<%} %>
	</table>


<input type="hidden" name="command" value="obtieneRespuestaBuro">
<input type="hidden" name="buro" value="">
<input type="hidden" name="persona" value="">
</form>
</center>

<%@include file="footer.jsp"%>

</body>
</html>