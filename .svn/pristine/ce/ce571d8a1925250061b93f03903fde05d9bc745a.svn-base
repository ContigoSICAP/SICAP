<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<html>
<head>
<title>Alta Clientes</title>
<script language="Javascript" src="./js/functions.js"></script>
<script>

     function submitenter(myfield,e){
        var keycode;
          if (window.event) 
              keycode = window.event.keyCode;
          else if (e) 
             keycode = e.which;
          else 
              return true;

         if (keycode == 13){
             buscar();
           }else
              return true;
           }

	function buscar(){
		if( window.document.forma.rfc.value=='' && window.document.forma.idCte.value=='' && window.document.forma.apellidoPaterno.value=='' && window.document.forma.apellidoMaterno.value=='' && window.document.forma.nombreS.value==''){
			alert('Introduzca un valor para su búsqueda por favor');
			return false;
		}
		if(window.document.forma.idCte.value!='' && !esEntero(window.document.forma.idCte.value)){
			alert('Introduzca un n\u00famero de cliente v\u00e1lido por favor');
			return false;
		}
		window.document.forma.command.value='buscaClientePorRFC';
		window.document.forma.submit();
	}

	function consultaCliente(idCliente){
		window.document.forma.command.value='consultaCliente';
		window.document.forma.idCliente.value=idCliente;
		window.document.forma.idSolicitud.value=1;
		window.document.forma.submit();
	}
	
	function nuevoCliente(){
		window.document.forma.command.value='nuevoCliente';
		window.document.forma.submit();
	}

	function reporteCartera(){
		window.document.forma.command.value='reporteCartera';
		window.document.forma.submit();
	}
	
	function menuReportes(){
		window.document.forma.command.value='menuReportes';
		window.document.forma.submit();
	}

	function admonChequera(){
		window.document.forma.command.value='admonChequera';
		window.document.forma.submit();
	}

	function prueba(){
		window.document.forma.command.value='pruebas';
		window.document.forma.submit();
	}

</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>
<%
	Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
	ClienteVO clientes[] = (ClienteVO[])request.getAttribute("CLIENTES_POR_RFC");

%>

<body leftmargin="0" topmargin="0" >
<jsp:include page="header.jsp" flush="true"/>
<jsp:include page="menuInicio.jsp" flush="true"/>
<center>
    <h2>Administraci&oacute;n de Clientes</h2> 
    <h3>B&uacute;squeda por RFC</h3>
<form name="forma" action="controller" method="post">
	

	<input type="button" name="pruebas" value="Proceso Monitor Pagos" id="pruebas" onClick="prueba()">
	<input type="hidden" name="command" value="">
</form>
</center>

<jsp:include page="footer.jsp" flush="true"/></body>
</html>