<%@page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@page import="java.util.Vector"%>
<%@page import="com.afirme.commons.model.dao.helpers.CatalogDAO"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp"%>
<jsp:directive.page import="com.sicap.clientes.util.inffinix.InffinixUtil"/>

<%
    Vector notificaciones = (Vector)request.getAttribute("NOTIFICACIONES");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
<script language="Javascript" src="./js/functions.js"></script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
<script>
    function generarInterfaces(){
        
        if( window.document.forma.fechaInicio.value==='' && window.document.forma.fechaInicio.value===null)
        {
            alert("Ingrese la Fecha ");
            window.document.forma.fechaInicio.focus();
            document.getElementById("botonInterfaz").disabled = false;
            return false;
        }
        if (!validaFecha(window.document.forma.fechaInicio.value))
        {
            alert('El dato [Fecha Inicio] es erróneo, favor de validar e ingresarlo nuevamente');
            window.document.forma.fechaInicio.focus();
            return false;
        }
        
        if(confirm("¿Deseas generar las interfaces de BURSA?")){
            window.document.getElementById("botonInterfaz").disabled=true;
             window.document.forma.command.value='generaInterfacesBursa';
             window.document.forma.submit();
         } else {
             return false;
         }
    }
</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    
    <title>Interfaces BURSA</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
  </head>
  
<body leftmargin="0" topmargin="0" >
<jsp:include page="header.jsp" flush="true"/>
<center>

<h2>BURSA</h2>
<h3>Generar Interfaces</h3>

<form name="forma" action="admin" method="post">
<input type="hidden" name="command" value="">
<table border="0" cellspacing="5" align="center">
	<tr>
            <td width="100%" align="center" colspan="2"><%=HTMLHelper.displayNotifications(notificaciones)%><br></td>
	</tr>
        <tr>
            <th align="right">Fecha:</th>
            <td align="left"><input type="text" name="fechaInicio" id="fechaInicio" placeholder="dd/mm/yyyy"></td>
        </tr>
        <tr>
            <td colspan="2" align="center"><br><input type="button" id="botonInterfaz" value="Generar Interfaces Bursa" onClick="generarInterfaces()"></td>
        </tr>  
</table>
</form>
</center>

<jsp:include page="footer.jsp" flush="true"/>
  </body>
</html>
