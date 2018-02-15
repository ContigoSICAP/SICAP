<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.*"%>
<html>
<head>
<title>Reasignaci&oacute;n de Cartera</title>
<script language="Javascript" src="./js/functions.js"></script>

<script type="text/javascript">
<!--
	function buscaEjecutivos(){
		window.document.forma.command.value = 'buscaEjecutivos';
		window.document.forma.submit();
	}

	function listarSolicitudes(){
		window.document.forma.command.value = 'listarSolicitudes';
		window.document.forma.submit();
	}
	
	function reasignarCartera(){
		window.document.forma.command.value = 'reasignarCartera';
		window.document.forma.submit();
	}

//-->
</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>

<%
Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
int idSucursal = HTMLHelper.getParameterInt(request, "idSucursal");
//int idEjecutivoOrigen = HTMLHelper.getParameterInt(request,"idEjecutivoOrigen");
TreeMap catSucursales = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SUCURSALES);
//TreeMap catEjecutivos = CatalogoHelper.getCatalogoEjecutivos(idSucursal);
//TreeMap catEjecutivosDestino = null;
//SolicitudVO solicitudes[] = null;
//SolicitudVO solicitudCliente = new SolicitudVO();

//ArrayList<EjecutivoCreditoVO> ejecutivos = null;
//if(request.getAttribute("EJECUTIVOS")!=null) {
//	catEjecutivosDestino = (TreeMap<Integer,String>)request.getAttribute("EJECUTIVOS");
//}
//if(request.getAttribute("CARTERA")!=null) {
//	solicitudes = (SolicitudVO[])request.getAttribute("CARTERA");
//}

%>

<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<jsp:include page="menuIzquierdo.jsp" flush="true"/>
<form name="forma" action="controller" method="post">
<input type="hidden" name="command" value="">

			<h3>Reasignaci&oacute;n de Cartera</h3>
			<%=HTMLHelper.displayNotifications(notificaciones)%>
			<br>
			<table width="100%" border="1" cellpadding="0">
				<tr>
					<td width="50%" align="right">Sucursal</td>
					<td width="50%">  
						<select name="idSucursal" size="1">
						<%=HTMLHelper.displayCombo(catSucursales, idSucursal)%>
						</select>
					</td>										
				</tr>					
				
				<tr>
					<td align="center" colspan="2">
						<input type="button" value="Listar Ejecutivos" onClick="buscaEjecutivos()">
						&nbsp;
						<input type="button" value="Listar Solicitudes" onClick="listarSolicitudes()">
						&nbsp;
						<input type="button" value="Reasignar" onClick="reasignarCartera()">
					</td>	
				</tr>
			</table>
</form>
<jsp:include page="footer.jsp" flush="true"/>

</body>
</html>
