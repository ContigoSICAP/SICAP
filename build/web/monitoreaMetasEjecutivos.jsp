<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>

<%@ page import="com.sicap.clientes.vo.UsuarioVO"%>
<%@ page import="com.sicap.clientes.vo.GrupoVO"%>
<%@ page import="com.sicap.clientes.dao.GrupoDAO"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.*"%>

<%
	UsuarioVO usuario = (UsuarioVO)session.getAttribute("USUARIO");
	TreeMap catSucursales = CatalogoHelper.getCatalogoSucursal(usuario.sucursales);
	TreeMap catNoRenovacion = CatalogoHelper.getCatalogo(ClientesConstants.CAT_MOTIVOS_NO_RENOVACION);
	Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
	GrupoVO[] gruposNoDesembolsados = (GrupoVO[])request.getAttribute("GRUPOS_NODESEMBOLSADOS");
	GrupoVO[] gruposDesembolsados	= (GrupoVO[])request.getAttribute("GRUPOS_DESEMBOLSADOS");
	String outHtml	= (String)request.getAttribute("SALIDA");
	//EjecutivoCreditoVO ejecutivo = (EjecutivoCreditoVO)request.getAttribute("EJECUTIVO");
	TreeMap catEjecutivos = CatalogoHelper.getCatalogoEjecutivos(HTMLHelper.getParameterInt(request, "idSucursal"), "A");
	int numGrupos = 0;

%>
<html>
<head>
<title>Monitoreo de metas ejecutivos</title>
<script language="Javascript" src="./js/functions.js"></script>
<script type="text/javascript">
	
	function consultaMetasMonitoreo(){
		if ( window.document.forma.idSucursal.value==0){
			alert('Debe seleccionar una sucursal');
			return false;
		}
		
		window.document.forma.command.value='consultaMetasMonitoreo';
		window.document.forma.submit();
	}

	function guardaPlaneacion(){
		window.document.forma.command.value='guardaPlaneacion';
		window.document.forma.submit();
	}

</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>
<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<form action="admin" method="post" name="forma">
<input type="hidden" name="command" value="">
<input type="hidden" name="grupos" value=<%=numGrupos%>>
<input type="hidden" name="tipoconsulta" value="monitoreo">

<table border="0" width="100%">
	<tr>
		<td colspan="2" align="center">
		<h3>Monitoreo Metas<br></h3>
		<%=HTMLHelper.displayNotifications(notificaciones)%>
		</td>
	</tr>

  <tr>
    <td width="50%" align="right">Sucursal</td>
    <td width="50%" align="left"><select name="idSucursal""><%=HTMLHelper.displayCombo(catSucursales, HTMLHelper.getParameterInt(request, "idSucursal"))%></select></td>
  </tr>

<%=outHtml%>
  <tr>
  	<td align="right">
  		<br>
  		<input type="button" name="" value="Consultar" onClick="consultaMetasMonitoreo();" >
  		<br><br>
  	</td>

  	<td align="left">
  		<br>
  		<input type="button" name="guardar" value="Guardar" onClick="guardaCambios();" >
  		<br><br>
  	</td>

  </tr>
<tr>
	<td colspan="2" align="center">
		<br><a href="<%=request.getContextPath() %>">Inicio</a>
	</td>
</tr>

</table>

</form>
<jsp:include page="footer.jsp" flush="true"/>
</body>
</html>
