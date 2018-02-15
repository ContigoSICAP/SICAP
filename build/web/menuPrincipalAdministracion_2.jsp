<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<html>
<head>
<title>Menú principal administración</title>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
<link href="./css/main.css" rel="stylesheet" type="text/css">
<script language="Javascript" src="./js/functions.js"></script>
<script>

	function administracionUsuarios(){
		window.document.forma.command.value='administracionUsuarios';
		window.document.forma.submit();
	}
	
	function consultaDB(){
		window.document.forma.command.value='ejecutaConsulta';
		window.document.forma.submit();
	}
	
	function administracionEjecutivos(){
		window.document.forma.command.value='administracionEjecutivos';
		window.document.forma.submit();
	}
	
	function administracionRepresentantes(){
		window.document.forma.command.value='administracionRepresentantes';
		window.document.forma.submit();
	}
	
	function pagosReferenciados(){
		window.document.forma.command.value='pagosReferenciados';
		window.document.forma.submit();
	}
	
	
	function consultaReportes(){
		window.document.forma.command.value='consultaReportes';
		window.document.forma.submit();
	}
	
	function ordenesDePago(){
		window.document.forma.command.value='ordenesDePago';
		window.document.forma.submit();
	}

	function soporte(){
		window.document.forma.command.value='administracionSoporte';
		window.document.forma.submit();
	}
	
	function cierreDia(){
		window.document.forma.command.value='cierreDia';
		window.document.forma.submit();
	}
	
	function cierreContable(){
		window.document.forma.command.value='cierreContable';
		window.document.forma.submit();
	}

	
	function mayorizacion(){
		window.document.forma.command.value='mayorizacion';
		window.document.forma.submit();
	}

</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>
<%


%>

<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<center>
    <h3>Administraci&oacute;n SICAP</h3> 
    <br>
    <br>
    <br>
<form name="forma" action="admin" method="post">
<input type="hidden" name="command" value="">
<table align="center" class="tabla_d" border="0">

   <%if(request.isUserInRole("admin")){%> 
	<tr>
  		<td class='texto_c'><h3>Cierre</h3></td>
  		<td class='texto_c'><h3>Pagos</h3></td>
  		<td class='texto_c'><h3>Taller de Productos</h3></td>
  		<td class='texto_c'><h3>Seguridad</h3></td>
  		<td class='texto_c'><h3>Soporte</h3></td>
	</tr>
	
	<tr>
		<td class='texto_c'>&nbsp;</td>
  		<td class='texto_c'>&nbsp;</td>
  		<td class='texto_c'>&nbsp;</td>
  		<td class='texto_c'>&nbsp;</td>
  		<td class='texto_c'>&nbsp;</td>
	</tr>
	
	<tr>
  		<td class='texto_c'><br><a href="#" onClick="cierreDia()">Cierre de Día</a><br><br></td>
  		<%}if(request.isUserInRole("ADM_PAGOS")){%> 
  		<td class='texto_c'><br><a href="#" onClick="pagosReferenciados()">Administración de Pagos</a><br><br></td>
  		<td class='texto_c'><br><a href="#" onClick="administracionRepresentantes()">Administración de Convenios</a><br><br></td>
  		<td class='texto_c'><br><a href="#" onClick="administracionUsuarios()">Administración de usuarios</a><br><br></td>
  		<td class='texto_c'><br><a href="#" onClick="consultaDB()">Consultas</a><br><br></td>
	</tr>
	
	<tr>
		<td class='texto_c'>&nbsp;</td>
  		<td class='texto_c'>&nbsp;</td>
  		<td class='texto_c'>&nbsp;</td>
  		<td class='texto_c'>&nbsp;</td>
  		<td class='texto_c'>&nbsp;</td>
	</tr>	
	
	<%}if(request.isUserInRole("ADM_EJECUTIVOS")){%>
	<tr>
  		<td class='texto_c'><br><a href="#" onClick="cierreContable()">Cierre Contable</a><br><br></td>
  		<td class='texto_c'><br><a href="#" onClick="ordenesDePago()">Ordenes de Pagos</a><br><br></td>
  		<td class='texto_c'>&nbsp;</td>
  		<td class='texto_c'><br><a href="#" onClick="administracionEjecutivos()">Administración de ejecutivos</a><br><br></td>
  		<td class='texto_c'><br><a href="#" onClick="soporte()">Administración Soporte</a><br><br></td>
	</tr>
	
	<tr>
		<td class='texto_c'>&nbsp;</td>
  		<td class='texto_c'>&nbsp;</td>
  		<td class='texto_c'>&nbsp;</td>
  		<td class='texto_c'>&nbsp;</td>
  		<td class='texto_c'>&nbsp;</td>
	</tr>
	<%}if(request.isUserInRole("ADM_REPRESENTANTES")){%>
	<tr>
  		<td class='texto_c'><br><a href="#" onClick="mayorizacion()">Mayorizacion y Envio</a><br><br></td>
  		<td class='texto_c'>&nbsp;</td>
  		<td class='texto_c'>&nbsp;</td>
  		<td class='texto_c'>&nbsp;</td>
  		<td class='texto_c'><br><a href="#" onClick="consultaReportes()">Reportes</a><br><br></td>
	</tr>
    <%}%>

</table>
</form>
</center>
<jsp:include page="footer.jsp" flush="true"/></body>
</html>