<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@ page import="com.sicap.clientes.helpers.SeguroHelper"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="java.util.TreeMap"%>
<%@ page import="com.sicap.clientes.vo.ClienteVO"%>
<%@ page import="com.sicap.clientes.vo.TablaAmortizacionVO"%>
<%@ page import="com.sicap.clientes.vo.DireccionSucursalVO"%>
<%@ page import="com.sicap.clientes.vo.SegurosVO"%>
<%@ page import="com.sicap.clientes.dao.*"%>

<%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
response.setContentType("application/rtf");
response.setHeader("Content-Disposition","attachment; filename=\"Poliza.rtf\"");
TreeMap catSucursales = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SUCURSALES);
ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
DireccionSucursalDAO dirSucursalDAO = new DireccionSucursalDAO();
DireccionSucursalVO direccionVO = dirSucursalDAO.getDireccion(cliente.idSucursal);
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
TablaAmortizacionVO tabla[] = cliente.solicitudes[indiceSolicitud].amortizacion;
SegurosVO seguro = cliente.solicitudes[indiceSolicitud].seguro;
Double pagoTotal = 0.00;
Double montoSeguro = 0.00;

montoSeguro = FormatUtil.roundDouble(SeguroHelper.getMontoPeriodo(cliente.solicitudes[indiceSolicitud].decisionComite.frecuenciaPago, seguro.sumaAsegurada, seguro.modulos), 2);

%>
<html>
<head>

<title>Poliza de seguro</title>
  
<style type="text/css">
  p.parrafo {
  	text-align: left;
  	font-size: 11px;
  }
  p.parrafo_justificado {
  	text-align: justify;
  	font-size: 11px;
  }
  td.celdas {
  	text-align: center;
  	font-size: 11px;
  }
  td.celda_justificada{
  	text-align: justify;
  	font-size: 11px;
  }
</style>

</head>

<body rightmargin="0" leftmargin="0" topmargin="0" bottommargin="0">

<center>
<P><img name="imagen" src="<%=basePath%>/images/logoCF.jpg"  border="0"></p>
<p><font size="3"><strong>HOJA DE AYUDA</strong></font></p>
</center>

<p><font style="font-weight: bold;">La siguiente tabla tiene como objetivo recordarle el monto que tendr&aacute; que depositar en el banco cada mes por concepto de:</font></p> 
 
<p align="justify"><font size="2">1. Contrato de cr&eacute;dito firmado en fecha del <b><%=HTMLHelper.displayField(tabla[0].fechaPago)%></b> y;</font></p> 
<p align="justify"><font size="2">2. P&oacute;liza de Seguro de Vida firmada en fecha del <b><%=HTMLHelper.displayField(tabla[0].fechaPago)%></b>.</font></p>

<table width="90%" border="1" align="center">
<tbody><tr>
<td width="15%"><center><strong># Pago Mensual</strong></center></td>
<td width="20%"><center><strong>Fecha</strong></center></td>
<td><center><strong>PAGO 1 <br>Relativo al Cr&eacute;dito</strong></center></td>
<td  width="20%"><center><strong>PAGO 2 <br>Relativo a la <br>Prima del Seguro <br>de Vida</strong></center></td>
<td><center><strong>Monto Total a <br>Depositar en el Banco</strong></center></td>
</tr>
<%for(int i=1;i<tabla.length;i++){
	Double montoMensual = tabla[i].montoPagar;
	pagoTotal = montoMensual+montoSeguro;
	%>
	<tr>
		<td><center><%=HTMLHelper.displayField(tabla[i].numPago)%></center></td>
		<td><center><b><%=HTMLHelper.displayField(tabla[i].fechaPago)%></b></center></td>
		<td><center><font style="font-style: oblique;"><%=HTMLHelper.displayField(HTMLHelper.formatCantidad(montoMensual))%></font></center></td>
		<td><center><font style="font-style: oblique;"><%=HTMLHelper.displayField(HTMLHelper.formatCantidad(montoSeguro))%></font></center></td>
		<td><center><b><%=HTMLHelper.displayField(HTMLHelper.formatCantidad(pagoTotal))%></b></center></td>
	</tr>
<%}%>
</tbody></table><font size="2" s="">
<p><br>Le recordamos que si tiene cualquiera duda acerca de sus pagos, o las cifras no corresponden a los mostrados en los contratos, le rogamos comunicarse con su asesor de cr&eacute;dito o acudir a la sucursal ubicada en:</p> 
 
<p>Cr&eacute;dito Firme <%=(String)catSucursales.get(new Integer(cliente.idSucursal))%></p> 
<p><%=direccionVO.calle%></p> 
<p>Col. <%=direccionVO.colonia + " C.P." + direccionVO.cp%></p> 
<p><%=direccionVO.ciudad + ", " + direccionVO.estado%></p> 
 
</font>
</body>
</html>
