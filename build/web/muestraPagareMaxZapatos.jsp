<%@ page contentType="text/html; charset=iso-8859-1" language="java"%>
<%@ page import="com.sicap.clientes.vo.ClienteVO"%>
<%@ page import="com.sicap.clientes.vo.SolicitudVO"%>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.util.SolicitudUtil"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
int idDisposicion = HTMLHelper.getParameterInt(request, "idDisposicion");
SolicitudVO solicitud = cliente.solicitudes[indiceSolicitud];
//Date fechaPago = TablaAmortizacionHelper.calcFechaInicioMensualMaxZapatos(solicitud.disposiciones[0].fechaCaptura);
double montoPagar = solicitud.disposiciones[idDisposicion-1].tablaAmostizaciones[1].montoPagar + solicitud.disposiciones[idDisposicion-1].tablaAmostizaciones[2].montoPagar;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>Pagaré Max Zapatos (Disposición 1)</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

	<script type="text/javascript">
	
	function imprimir() {
	  if (window.print)
	    window.print();
	  else
	    alert("Lo siento, pero a tu navegador no se le puede ordenar imprimir desde la web. Actualizate o hazlo desde los menús");
	}	
	
	</script>

  </head>
  
<body style="font-family: sans-serif;font: menu">
<center>
<table width="230" cellspacing="0" cellpadding="0" border="0" style="font-size: 12px">
<tr>
<td colspan="2" align="center">CREDITO FIRME</td>
</tr>
<tr>
<td>&nbsp;</td>
</tr>
<tr>
<td colspan="2">Fecha: <%=HTMLHelper.displayField(solicitud.disposiciones[idDisposicion-1].fechaCaptura)%></td>
</tr>
<tr>
<td colspan="2">Hora: <%=HTMLHelper.displayField(solicitud.disposiciones[idDisposicion-1].fechaCaptura, 2)%></td>
</tr>
<tr>
<td colspan="2">&nbsp;</td>
</tr>
<tr>
<td colspan="2">  C   L   I   E   N   T   E: </td>
</tr>
<tr>
<td colspan="2"><%=cliente.nombreCompleto%></td>
</tr>
<tr>
<td colspan="2">&nbsp;</td>
</tr>
<tr>
<td>Pague antes de : <%=HTMLHelper.displayField(solicitud.disposiciones[idDisposicion-1].tablaAmostizaciones[1].fechaPago)%></td>
</tr>
<tr>
<td>La cantidad de : <%=HTMLHelper.formatCantidad(solicitud.disposiciones[idDisposicion-1].tablaAmostizaciones[0].saldoInicial)%></td>
</tr>
<tr>
<td>&nbsp;</td>
</tr>
<tr>
<td>Pague antes de : <%=HTMLHelper.displayField(solicitud.disposiciones[idDisposicion-1].tablaAmostizaciones[2].fechaPago)%></td>
</tr>
<tr>
<td>La cantidad de : <%=HTMLHelper.formatCantidad(montoPagar)%></td>
</tr>
<tr>
<td colspan="2">&nbsp;</td>
</tr>
<tr>
<td colspan="2">&nbsp;</td>
</tr>
<tr>
<td colspan="2" align="center">......................................................................</td>
<tr>
<td colspan="2" align="center">Nombre completo</td>
</tr>
<tr>
<td colspan="2">&nbsp;</td>
</tr>
<tr>
<td colspan="2" align="center">......................................................................</td>
</tr>
<tr>
<td colspan="2" align="center">Firma</td>
</tr>
<tr>
<td colspan="2">&nbsp;</td>
</tr>
<tr>
<td colspan="2">Referencia de pago : <%=solicitud.referencia%></td>
</tr>
<tr>
<td colspan="2">&nbsp;</td>
</tr>
<tr>
<td colspan="2" align="justify">Por este pagaré me obligo a pagar incondicionalmente a la orden de CRÉDITO FIRME, S.A. DE C.V., SOFOM E.R., la cantidad que aparece en el totalde este título el cual suscribo al amparo del contrato que tengo celebrado con dicha institución para el uso de este crédito.</td>
</tr>
<tr>
<td colspan="2">&nbsp;</td>
</tr>
<tr>
<td colspan="2">&nbsp;</td>
</tr>
<tr>
<td colspan="2">&nbsp;</td>
</tr>
</table>
<table width="230" cellspacing="0" cellpadding="0" border="0" style="font-size: 12px">
<tr>
<td colspan="2" align="center">CREDITO FIRME<br>COPIA CLIENTE</td>
</tr>
<tr>
<td>&nbsp;</td>
</tr>
<tr>
<td colspan="2">Fecha: <%=HTMLHelper.displayField(solicitud.disposiciones[idDisposicion-1].fechaCaptura)%></td>
</tr>
<tr>
<td colspan="2">Hora: <%=HTMLHelper.displayField(solicitud.disposiciones[idDisposicion-1].fechaCaptura, 2)%></td>
</tr>
<tr>
<td colspan="2">&nbsp;</td>
</tr>
<tr>
<td colspan="2">  C   L   I   E   N   T   E: </td>
</tr>
<tr>
<td colspan="2"><%=cliente.nombreCompleto%></td>
</tr>
<tr>
<td colspan="2">&nbsp;</td>
</tr>
<tr>
<td>Pague antes de : <%=HTMLHelper.displayField(solicitud.disposiciones[idDisposicion-1].tablaAmostizaciones[1].fechaPago)%></td>
</tr>
<tr>
<td>La cantidad de : <%=HTMLHelper.formatCantidad(solicitud.disposiciones[idDisposicion-1].tablaAmostizaciones[0].saldoInicial)%></td>
</tr>
<tr>
<td>&nbsp;</td>
</tr>
<tr>
<td>Pague antes de : <%=HTMLHelper.displayField(solicitud.disposiciones[idDisposicion-1].tablaAmostizaciones[2].fechaPago)%></td>
</tr>
<tr>
<td>La cantidad de : <%=HTMLHelper.formatCantidad(montoPagar)%></td>
</tr>
<tr>
<td colspan="2">&nbsp;</td>
</tr>
<tr>
<td colspan="2">&nbsp;</td>
</tr>
<tr>
<td colspan="2" align="center">......................................................................</td>
<tr>
<td colspan="2" align="center">Nombre completo</td>
</tr>
<tr>
<td colspan="2">&nbsp;</td>
</tr>
<tr>
<td colspan="2" align="center">......................................................................</td>
</tr>
<tr>
<td colspan="2" align="center">Firma</td>
</tr>
<tr>
<td colspan="2">&nbsp;</td>
</tr>
<tr>
<td colspan="2">Referencia de pago : <%=solicitud.referencia%></td>
</tr>
<tr>
<td colspan="2">&nbsp;</td>
</tr>
<tr>
<td colspan="2" align="justify">Por este pagaré me obligo a pagar incondicionalmente a la orden de CRÉDITO FIRME, S.A. DE C.V., SOFOM E.R., la cantidad que aparece en el totalde este título el cual suscribo al amparo del contrato que tengo celebrado con dicha institución para el uso de este crédito.</td>
</tr>
<tr>
<td colspan="2">&nbsp;</td>
</tr>
<tr>
<td colspan="2">&nbsp;</td>
</tr>
<tr>
<td colspan="2">&nbsp;</td>
</tr>
</table>
<input onclick="imprimir()" type="button" value="Imprimir" name="boton">
<input onclick="javascrip:window.close();" type="button" value="Cerrar" name="boton">

</center>
</body>
</html>
