<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.util.GrupoUtil"%>
<%@ page import="java.util.TreeMap"%>
<%@ page import="java.util.Date"%>
<%@ page import="com.sicap.clientes.vo.GrupoVO"%>
<%@ page import="com.sicap.clientes.vo.UsuarioVO"%>
<%@ page import="com.sicap.clientes.vo.CicloGrupalVO"%>

<%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server
response.setContentType("application/rtf");
response.setHeader("Content-Disposition","attachment; filename=\"ControlPagosAhorros.rtf\"");
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
GrupoVO grupo = (GrupoVO)session.getAttribute("GRUPO");
CicloGrupalVO ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
//TreeMap catSucursales = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SUCURSALES);
UsuarioVO usuario = (UsuarioVO)session.getAttribute("USUARIO");
TreeMap catSucursales = CatalogoHelper.getCatalogoSucursal(usuario.sucursales);

%>
<html>
<head>

<title>Poliza de seguro</title>
</head>
<body rightmargin="0px" leftmargin="0px" topmargin="0px" bottommargin="0px" style="font-family: Arial;" >
<img name="imagen" src="<%=basePath%>/images/logoCF.jpg"  border="0">
<br><br>
<p align="center"><font size="3">Control de Pagos y Garant&Iacute;as</font></p>
<table width="100%" border="0" cellpadding="0" cellspacing="0">
<tr>
	<td width="25%"><font size="1">Grupo: <%=grupo.nombre%></font></td>
</tr>
<tr>
	<td width="25%"><font size="1">Sucursal : <%=(String)catSucursales.get( new Integer (grupo.sucursal) )%></font></td>
</tr>
<tr>
	<td width="25%"><font size="1">Fecha : <%=HTMLHelper.displayField(new Date()) %></font></td>
</tr>
<tr>
	<td width="25%"><font size="1">Ciclo : <%=idCiclo%></font></td>
</tr>
<tr>
	<td width="25%"><font size="1">Semana : _______</font></td>
</tr>
<tr>
	<td width="25%"><font size="1">Asesor : _____________________________________</font></td>
</tr>
</table>
<br><br><br>
<table table width="100%" border="1" cellpadding="0" cellspacing="0">
<tr align="center">
	<td colspan="2" rowspan="2"><font size="1">Nombre del cliente</font></td>
	<td colspan="3" align="center"><font size="1">Crédito</font></td>
	<td colspan="3" align="center"><font size="1">Garant&Iacute;a Semanal</font></td>
	<td align="center" rowspan="2" width="10%"><font size="1">Multas</font></td>
</tr>
<tr align="center">
	<td width="8%"><font size="1">Saldo inicial</font></td>
	<td width="8%"><font size="1">Pago</font></td>
	<td width="8%"><font size="1">Saldo final</font></td>
	<td width="8%"><font size="1">Saldo inicial</font></td>
	<td width="8%"><font size="1">Pago</font></td>
	<td width="8%"><font size="1">Saldo final</font></td>
</tr>
<%
for ( int i=0 ; i<ciclo.integrantes.length ; i++ ){
%>
<tr>
	<td width="2%"><font size="1"><%=i+1%></font></td>
	<td><font size="1"><%=ciclo.integrantes[i].nombre%></font></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
</tr>
<%
}
%>
<tr>
	<td colspan="2" align="right"><font size="1">Totales</font></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
</tr>
</table>
<br><br><br><br>
<table align="center" width="60%" border="0" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td height="50" valign="bottom"><font size="1">________________________</font></td>
		<td height="50" valign="bottom"><font size="1">________________________</font></td>
		<td height="50" valign="bottom"><font size="1">________________________</font></td>
	</tr>
	<tr align="center">
		<td height="50" valign="top"><font size="1">Presidenta</font></td>
		<td height="50" valign="top"><font size="1">Tesorera</font></td>
		<td height="50" valign="top"><font size="1">Secretaria</font></td>
	</tr>
	<tr align="center">
		<td height="50"><font size="1"></font></td>
		<td height="50" valign="bottom"><font size="1">________________________</font></td>
		<td height="50"><font size="1"></font></td>
	</tr>
	<tr align="center">
		<td><font size="1"></font></td>
		<td><font size="1">Asesor</font></td>
		<td><font size="1"></font></td>
	</tr>
</table>
<br><br><br>
<p><font size="1">
<b>Nota</b>:<br>
Deberá ser llenado por la mesa directiva del grupo<br>
La mesa directiva deberá sacar una copia por cada pago semanal<br>
El saldo inicial del Crédito y garant&iacute;a debe ser el saldo final de la semana anterior<br>
</font></p>
</body>
</html>