<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@ page import="java.util.TreeMap"%>
<%@ page import="com.sicap.clientes.vo.GrupoVO"%>
<%@ page import="com.sicap.clientes.vo.CicloGrupalVO"%>
<%@ page import="com.sicap.clientes.vo.UsuarioVO"%>
<jsp:directive.page import="com.sicap.clientes.util.GrupoUtil"/>
<jsp:directive.page import="com.sicap.clientes.helpers.HTMLHelper"/>

<%
response.setContentType("application/rtf");
response.setHeader("Content-Disposition","attachment; filename=\"ListaAsistencia.rtf\"");
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
<p align="center"><font size="3">LISTA DE ASISTENCIA</font></p>
<table width="100%" border="1" cellpadding="0" cellspacing="0">
<tr>
	<td width="25%" align="center"><font size="1">Grupo</font></td>
	<td width="25%" align="center"><font size="1">Suscursal</font></td>
	<td width="25%" align="center"><font size="1">Elaboró : Asesor</font></td>
	<td width="25%" align="center"><font size="1">Revisó : Supervisor</font></td>
</tr>
<tr>
	<td width="25%"><font size="1"><%=grupo.nombre%></font></td>
	<td width="25%"><font size="1"><%=(String)catSucursales.get( new Integer (grupo.sucursal) )%></font></td>
	<td width="25%" bgcolor="yellow"></td>
	<td width="25%" bgcolor="yellow"></td>
</tr>
</table>
<br><br><br>
<table table width="100%" border="1" cellpadding="0" cellspacing="0">
<tr>
	<td colspan="8"></td>
	<td colspan="16" align="center"><font size="1">Control de semana</font></td>
</tr>
<tr align="center">
	<td colspan="3"><font size="1">No.</font></td>
	<td colspan="5"><font size="1">Nombre</font></td>
	<td width="2%"><font size="1">1</font></td>
	<td width="2%"><font size="1">2</font></td>
	<td width="2%"><font size="1">3</font></td>
	<td width="2%"><font size="1">4</font></td>
	<td width="2%"><font size="1">5</font></td>
	<td width="2%"><font size="1">6</font></td>
	<td width="2%"><font size="1">7</font></td>
	<td width="2%"><font size="1">8</font></td>
	<td width="2%"><font size="1">9</font></td>
	<td width="2%"><font size="1">10</font></td>
	<td width="2%"><font size="1">11</font></td>
	<td width="2%"><font size="1">12</font></td>
	<td width="2%"><font size="1">13</font></td>
	<td width="2%"><font size="1">14</font></td>
	<td width="2%"><font size="1">15</font></td>
	<td width="2%"><font size="1">16</font></td>
</tr>
<%
for ( int i=0 ; i<ciclo.integrantes.length ; i++ ){
%>
<tr>
	<td colspan="3" align="center"><font size="1"><%=i+1%></font></td>
	<td colspan="5"><font size="1"><%=ciclo.integrantes[i].nombre%></font></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
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
<tr align="center">
	<td colspan="8"><font size="1">Visto bueno del asesor</font></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
</tr>
</table>
<br><br><br>
<table align="center" width="60%" border="1" cellpadding="0" cellspacing="0">
	<tr align="center">
		<td colspan="2"><font size="1">OBSERVACIONES</font></td>
	</tr>
	<tr align="center">
		<td><font size="1">Nombre del cliente</font></td>
		<td><font size="1">Observaciones</font></td>
	</tr>
	<tr align="center">
		<td height="15"></td>
		<td height="15"></td>
	</tr>
		<tr align="center">
		<td height="15"></td>
		<td height="15"></td>
	</tr>
		<tr align="center">
		<td height="15"></td>
		<td height="15"></td>
	</tr>
		<tr align="center">
		<td height="15"></td>
		<td height="15"></td>
	</tr>
		<tr align="center">
		<td height="15"></td>
		<td height="15"></td>
	</tr>
</table>
<br><br><br>
<p><font size="1">
<b>Nota</b> : la la persona que tome la asistencia deberá anotar<br>
<b>A</b>: Asistencia<br>
<b>R</b>: Retardo<br>
<b>F</b>: Falta<br>
</font></p>
</body>
</html>