<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="java.util.*"%>

<html>
<head>
<title>Score consumo</title>
<script language="Javascript" src="./js/functions.js"></script>
<script type="text/javascript">

	function obtenScore(){

		window.document.forma.submit();
	}


</script>

<link href="./css/BEtext.css" rel="stylesheet" type="text/css"></head>
<%
Vector notificaciones = (Vector)request.getAttribute("NOTIFICACIONES");
//ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
Integer puntajeSIC = (Integer)request.getAttribute("puntajeSIC");
Integer puntajePerfil = (Integer)request.getAttribute("puntajePerfil");
if ( puntajeSIC==null )
	puntajeSIC = new Integer(0);
if ( puntajePerfil==null )
	puntajePerfil = new Integer(0);
%>
<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<jsp:include page="menuIzquierdo.jsp" flush="true"/>
<center>

<form name="forma" action="controller" method="post">
<input type="hidden" name="command" value="obtenScoreConsumo">
<input type="hidden" name="idSolicitud" value="<%=idSolicitud%>">
<table border="0" width="100%">
	<tr>
	<td align="center">
<h3>Score</h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
<table width="100%" border="0" cellpadding="0">

  <tr>
    <td width="50%" align="right">Puntuaci&oacute;n SIC</td>
    <td width="50%">
      <input type="text" name="puntajeSIC" size="20" value="<%=HTMLHelper.displayField(puntajeSIC.intValue()) %>" readonly="readonly">
    </td>
  </tr>
  <tr>
    <td width="50%" align="right">Puntuaci&oacute;n perfil</td>
    <td width="50%">
      <input type="text" name="puntajePerfil" size="20" value="<%=HTMLHelper.displayField(puntajePerfil.intValue()) %>" readonly="readonly">
    </td>
  </tr>
  <tr>
		<td align="center" colspan="2">
			<br><input type="button" value="Enviar" onClick="obtenScore()">
		</td>
	</tr>
		
</table>

		</td>
	</tr>
</table>
</form>
</center>
<jsp:include page="footer.jsp" flush="true"/>
</body>
</html>
