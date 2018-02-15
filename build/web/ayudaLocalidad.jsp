<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.CatalogoVO"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>

<html>
<head>
<title>Clientes</title>
<script language="Javascript" src="./js/functions.js"></script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">

<script>

function inicializa(){
	window.document.formaLocalidad.idColonia.value = window.opener.document.forma.idColonia.value;
	window.document.formaLocalidad.nombre.focus();
}

	function buscar(){
		if ( window.document.formaLocalidad.nombre.value=='' && window.document.formaLocalidad.numero.value=='' ){
			alert("Ingrese un nombre o número de localidad para realizar la búsqueda");
			window.document.formaLocalidad.nombre.focus();
			return false;
		}
		window.document.formaLocalidad.submit();

	}

	function utilizar(indice){
		window.opener.document.forma.localidad.value = window.document.formaLocalidad.localidad[indice].value;
		window.opener.document.forma.idLocalidad.value = window.document.formaLocalidad.idLocalidad[indice].value;
		window.close();
	}

</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>
<%
	Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
	CatalogoVO[] localidades = (CatalogoVO[])request.getAttribute("LOCALIDADES");
	//ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
	//DireccionVO direccion = new DireccionVO();

	//if ( cliente.direcciones!=null ){
	//	direccion = cliente.direcciones[0];
	//}
	
%>

<body leftmargin="0" topmargin="0" onload="inicializa();">
<center>

<form name="formaLocalidad" action="controller" method="post" >
<input type="hidden" name="command" value="buscaLocalidad">
<input type="hidden" name="idLocalidad" value="0">
<input type="hidden" name="idColonia" value="0">
<input type="hidden" name="localidad" value="">

<table border="1" cellpadding="0" width="500" >

<tr>
	<td align="right">
		Nombre Localidad
	</td>
	<td align="left">
		<input type="text" name="nombre" size="30" maxlength="30" value="<%=HTMLHelper.displayField( request.getParameter("nombre") )%>">
	</td>
</tr>
<tr>
	<td align="right">
		Número Localidad IFE
	</td>
	<td align="left">
		<input type="text" name="numero" size="4" maxlength="4" value="<%=HTMLHelper.displayField( request.getParameter("numero") )%>">
	</td>
</tr>
<tr>
	<td colspan="2" align="center">
		<input type="button" value="Buscar" onClick="buscar()">
	</td>
</tr>
<tr height="30">
	<td colspan="2" align="center"><%=HTMLHelper.displayNotifications(notificaciones)%></td>
</tr>
<tr>
	<td colspan="2" align="center">Localidad</td>
</tr>
<%
for ( int i=0 ; localidades!=null && i<localidades.length ; i++ ){
	CatalogoVO localidad = localidades[i];
%>
<tr>
	<td colspan="2">
	<input type="hidden" name="idLocalidad" value="<%=HTMLHelper.displayField(localidad.id)%>">
	<input type="hidden" name="localidad" value="<%=HTMLHelper.displayField(localidad.descripcion)%>">
	<a href="#" onClick="utilizar(<%=i+1%>)">  <%=HTMLHelper.displayField(localidad.descripcion)%></a>
	</td>
</tr>
<%
}
%>

</table> 
</form>
</center>
</body>
</html>