<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.CodigoPostalVO"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<html>
<head>
<%String idDireccion = "0";
if(request.getParameter("numdireccion")!= null)
	idDireccion = (String)request.getParameter("numdireccion");
%>
<title>Clientes</title>
<script language="Javascript" src="./js/functions.js"></script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">

<script>

	function buscar(){
		if ( !esEntero(window.document.formaCP.cp.value) ){
			alert("Código postal incorrecto");
			window.document.formaCP.cp.focus();
			return false;
		}
		window.document.formaCP.submit();
	}

	function utilizar(indice){
		<%if(idDireccion.equals("2")){%>
			window.opener.document.forma.estadoTerceraDir.value = window.document.formaCP.estado[indice].value;
			window.opener.document.forma.municipioTerceraDir.value = window.document.formaCP.municipio[indice].value;
			window.opener.document.forma.coloniaTerceraDir.value = window.document.formaCP.colonia[indice].value;
			window.opener.document.forma.idColoniaTerceraDir.value = window.document.formaCP.idColonia[indice].value;
			window.opener.document.forma.cpTerceraDir.value = window.document.formaCP.cp.value;
			window.opener.document.forma.asentamientoCPTerceraDir.value = window.document.formaCP.asentamientoCP[indice].value;
			window.close();
		<%}else{%>
			window.opener.document.forma.estado.value = window.document.formaCP.estado[indice].value;
			window.opener.document.forma.municipio.value = window.document.formaCP.municipio[indice].value;
			window.opener.document.forma.colonia.value = window.document.formaCP.colonia[indice].value;
			window.opener.document.forma.idColonia.value = window.document.formaCP.idColonia[indice].value;
			window.opener.document.forma.cp.value = window.document.formaCP.cp.value;
			window.opener.document.forma.asentamientoCP.value = window.document.formaCP.asentamientoCP[indice].value;
			//window.opener.document.forma.localidad.value = '';
			window.close();
		<%}%>
		
	}

</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>
<%
	Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
	CodigoPostalVO[] codigosPostales = (CodigoPostalVO[])request.getAttribute("CODIGOS_POSTALES");
%>

<body leftmargin="0" topmargin="0" onload="javascript:window.document.formaCP.cp.focus();">
<center>

<form name="formaCP" action="controller" method="post">
<input type="hidden" name="command" value="buscaCodigoPostal">
<input type="hidden" name="estado" value="">
<input type="hidden" name="municipio" value="">
<input type="hidden" name="colonia" value="">
<input type="hidden" name="idColonia" value="0">
<input type="hidden" name="asentamientoCP" value="">
<input type="hidden" name="numdireccion" value="<%=idDireccion%>">

<table border="1" cellpadding="0" width="500" >

<tr>
	<td colspan="2" align="center">
		<input type="text" name="cp" size="5" maxlength="5" value="<%=HTMLHelper.displayField( request.getParameter("cp") )%>">
	</td>
	<td align="center">
		<input type="button" value="Buscar" onClick="buscar()">
	</td>
</tr>
<tr height="30">
	<td colspan="3" align="center"><%=HTMLHelper.displayNotifications(notificaciones)%></td>
</tr>
<tr>
	<td class="liga" align="center">Estado</td>
	<td class="a13a" align="center">Municipio</td>
	<td align="center">Colonia</td>
</tr>
<%
for ( int i=0 ; codigosPostales!=null && i<codigosPostales.length ; i++ ){
CodigoPostalVO codigoPostal = codigosPostales[i];
%>
<tr>
	<input type="hidden" name="estado" value="<%=HTMLHelper.displayField(codigoPostal.estado)%>">
	<input type="hidden" name="municipio" value="<%=HTMLHelper.displayField(codigoPostal.municipio)%>">
	<input type="hidden" name="colonia" value="<%=HTMLHelper.displayField(codigoPostal.colonia)%>">
	<input type="hidden" name="idColonia" value="<%=HTMLHelper.displayField(codigoPostal.idColonia)%>">
	<input type="hidden" name="asentamientoCP" value="<%=HTMLHelper.displayField(codigoPostal.asentamiento_cp)%>">

	<td><%=HTMLHelper.displayField(codigoPostal.estado)%></td>
	<td><%=HTMLHelper.displayField(codigoPostal.municipio)%></td>
	<td><a href="#" onClick="utilizar(<%=i+1%>)">  <%=HTMLHelper.displayField(codigoPostal.colonia)%></a></td>
</tr>
<%
}
%>

</table> 
</form>
</center>
</body>
</html>