<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>

<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>

<%
	
	UsuarioVO usuario = (UsuarioVO)session.getAttribute("USUARIO");
	usuario.identificador = "I";
	TreeMap catOperaciones = CatalogoHelper.getCatalogoOperaciones(usuario, false);	
	Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
	TablaAmortizacionVO[] tabla = (TablaAmortizacionVO[])request.getAttribute("tablaAmortizacion");
	Integer elementos[] = (Integer[])request.getAttribute("elementos");
	double tasa = 0.000000;
	if ((Double)request.getAttribute("tasa")!=null){
		tasa = (Double)request.getAttribute("tasa");
	}
%>
<html>
<head>
<title>Genera Tabla Amortización Saldo Insoluto</title>
<script language="Javascript" src="./js/functions.js"></script>
<script type="text/javascript">
<!--
	function generaTabla(){
		
		window.document.forma.submit();
	}

//-->

</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>
<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<form action="controller" method="post" name="forma">
<input type="hidden" name="command" value="generaAmortizacion">
<table border="0" width="100%">
	<tr>
		<td align="center">
<h3>Simulador Tablas Amortizaci&oacute;n<br></h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
<table  border="0" cellpadding="0" width="100%">
  <tr>
  	<td width="50%" align="right">Tipo de operaci&oacute;n</td>
  	<td width="50%">  
    	<select name="operacion" size="1" onKeyPress="return submitenter(this,event)">
			<%=HTMLHelper.displayCombo(catOperaciones,HTMLHelper.getParameterInt(request,("operacion")))%>
   		</select>
    </td>
  </tr>

  <tr>
    <td width="50%" align="right">Monto</td>
    <td width="50%">
        <input type="text" name="montoAutorizado" size="10" maxlength="10" value="<%=HTMLHelper.displayField(request.getParameter("montoAutorizado")) %>" />
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Pago unitario<br></td>
    <td width="50%"><input type="text" name="pagoUnitario" size="10" maxlength="10" value="<%=HTMLHelper.displayField(request.getParameter("pagoUnitario")) %>" /></td>
  </tr>
  <tr>
    <td width="50%" align="right">Plazo autorizado(meses)<br></td>
    <td width="50%"><input type="text" name="plazoAutorizado" size="2" maxlength="2" value="<%=HTMLHelper.displayField(request.getParameter("plazoAutorizado")) %>" /></td>
  </tr>

  <tr>
    <td width="50%" align="right">Fecha inicio<br></td>
    <td width="50%"><input type="text" name="fecha" size="10" maxlength="10" value="<%=HTMLHelper.displayField(request.getParameter("fecha")) %>" /></td>
  </tr>

  <tr>
    <td width="50%" align="right">Frecuencia de pago(1=Quincenal, 2=Mensual, 3=Semanal, 4=Catorcenal)<br></td>
    <td width="50%"><input type="text" name="frecuenciaPago" size="1" maxlength="1" value="<%=HTMLHelper.displayField(request.getParameter("frecuenciaPago")) %>" /></td>
  </tr>
  <tr>
    <td width="50%" align="right"><b>Tasa calculada</b><br></td>
    <td width="50%"><input disabled type="text" name="tasaCalculada" size="10" maxlength="10" value="<%=HTMLHelper.displayField(tasa) %>" /></td>
  </tr>

  <tr>
  	<td colspan="2" align="center">
  		<br>
  		<input type="button" name="" value="Enviar" onClick="generaTabla();" >
  		<br><br>
  	</td>
  </tr>

	<%if(tabla!=null && tabla.length > 0){ %>
	<tr>
		<td colspan="2" align="center">
			<center><%=TablaAmortizacionHelper.makeTableIBS(tabla, elementos, HTMLHelper.getParameterInt(request,"frecuenciaPago"))%></center>
		</td>
	</tr>
	<%} %>
  <tr>
	<td valign="top" colspan = "3" align="center">&nbsp;
		<br><a href="<%=request.getContextPath() %>">Inicio</a>
	</td>
  </tr>
  
 </table>
		</td>
	</tr>
</table>
</form>
<jsp:include page="footer.jsp" flush="true"/></body>
</html>
