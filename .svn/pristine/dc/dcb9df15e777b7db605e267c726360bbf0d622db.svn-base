<%@ 	page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<html>
<head>
<title>Menú principal administración</title>
<script language="Javascript" src="./js/functions.js"></script>
<script>
	function dispersaOrdenPago(){
            document.getElementById("td").disabled = true;
		window.document.forma.command.value="dispersaOrdenPago";
		document.body.style.cursor = "wait";
                window.document.forma.submit();
	}
	function cargaArchivo(){
            document.getElementById("td").disabled = true;
 		window.document.forma.command.value="cargarArchivoOrdenPago";
		document.body.style.cursor = "wait";
                window.document.forma.submit();
	}
	function cambiaBanco(){
            document.getElementById("td").disabled = true;
 		window.document.forma.command.value="cambiaBanco";
		document.body.style.cursor = "wait";
                window.document.forma.submit();
	}
	function cancelaOrden(){
            document.getElementById("td").disabled = true;
		window.document.forma.command.value="windowCancelaOrdenDePago";
		document.body.style.cursor = "wait";
                window.document.forma.submit();
	}
	function cancelacionGrupal(){
            document.getElementById("td").disabled = true;
		window.document.forma.command.value="cancelacionGrupalODP";
		document.body.style.cursor = "wait";
                window.document.forma.submit();
	}
        	function devuelveOrden(){
            document.getElementById("td").disabled = true;
		window.document.forma.command.value="capturaDevolucionOrden";
		document.body.style.cursor = "wait";
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
    <h2>Administraci&oacute;n ORDENES DE PAGO</h2> 
    <h3>Opciones</h3>
<form name="forma" action="admin" method="post">
<input type="hidden" name="command" value="">
<table border="0" width="100%" cellspacing="0">
   
	<%if(request.isUserInRole("ADM_ORDENESPAGO")){%>
	<tr>
  		<td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="dispersaOrdenPago()">Genera Archivos de Dispersión</a><br><br></td>
	</tr>
	
	<tr>
  		<td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="cargaArchivo()">Cargar Archivo de Cobradas</a><br><br></td>
	</tr>
	<tr>
  		<td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="cambiaBanco()">Cambiar Banco de Ordenes de Pago</a><br><br></td>
	</tr>
        <%}%>
        <%if( request.isUserInRole("SOPORTE_OPERATIVO") ){%>
	<tr>
  		<td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="cancelaOrden()">Cancela Orden de Pago</a><br><br></td>
	</tr>
	<tr>
  		<td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="devuelveOrden()">Devuelve Orden de Pago</a><br><br></td>
	</tr>
	<%}%>
	<%if( request.isUserInRole("CAMBIO_ESTATUS_CHEQUE") ){%>
        <tr>
  		<td width="100%" align="center" colspan="2" id="td"><br><a href="#" onClick="cancelacionGrupal()">Cancelación Grupal</a><br><br></td>
	</tr>
	<%}%>        
</table>
</form>
</center>
<jsp:include page="footer.jsp" flush="true"/></body>
</html>