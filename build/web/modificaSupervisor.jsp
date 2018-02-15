<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.sicap.clientes.vo.SupervisorCreditoVO"%>
<html>
<head>
<title>Modificaci&oacute;n Supervisor</title>
<script language="Javascript" src="./js/functions.js"></script>

<script type="text/javascript">
<!--
	function listaSupervisor(){
		window.document.forma.command.value = 'listaSupervisor';
		window.document.forma.submit();
	}
	
	function seleccionaSupervisor(){
	    window.document.forma.command.value = 'seleccionaSupervisor';	     
		window.document.forma.submit();
	}

	function modificarSupervisor(){
		window.document.forma.command.value = 'modificarSupervisor';
		window.document.forma.submit();
	}
	
	function redireccionMenuAdmin(){
		window.document.forma.command.value = 'administracionEjecutivos';
		window.document.forma.submit();
	}

//-->
</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>

<%
int i = 0;
int muestrapantallaactualizacion = 0;
Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
int idSucursal = HTMLHelper.getParameterInt(request, "idSucursal");
String nombre = HTMLHelper.getParameterString(request, "nombre");
String aPaterno = HTMLHelper.getParameterString(request, "aPaterno");
String aMaterno = HTMLHelper.getParameterString(request, "aMaterno");
String estatus = HTMLHelper.getParameterString(request, "estatus");
if(request.getAttribute("ACTUALIZACION_SUPERVISOR")!= null)
	muestrapantallaactualizacion = (Integer)request.getAttribute("ACTUALIZACION_SUPERVISOR");

TreeMap catSucursales = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SUCURSALES);
//UsuarioVO usuario = (UsuarioVO)session.getAttribute("USUARIO");
//TreeMap catSucursales = CatalogoHelper.getCatalogoSucursal(usuario.sucursales);
SupervisorCreditoVO supervisor = null;
SupervisorCreditoVO supervisorSeleccionado = null;
ArrayList listaSupervisor = null;

//Listado de supervisor
if(request.getAttribute("SUPERVISORES")!=null) {
	listaSupervisor = (ArrayList)request.getAttribute("SUPERVISORES");
}
//supervisor seleccionado
if(request.getAttribute("SUPERVISORES")!=null) {
	supervisorSeleccionado = (SupervisorCreditoVO)request.getAttribute("SUPERVISORES");
}
%>

<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<form name="forma" action="admin" method="post">
<input type="hidden" name="command" value="">
<table border="0" width="100%">
	<tr>
		<td align="center">
			<h3>Modificaci&oacute;n de Supervisores</h3>
			<%=HTMLHelper.displayNotifications(notificaciones)%>
			<br>
			<table width="60%" border="0" cellpadding="0">
				<tr>
					<td width="50%" align="right">Sucursal</td>
					<td width="50%">  
						<select name="idSucursal" size="1">
						<%=HTMLHelper.displayCombo(catSucursales, idSucursal)%>
						</select>
					</td>
				</tr>
				<%if(supervisorSeleccionado!=null || muestrapantallaactualizacion == 1) {
				%>
				<tr>
					<td width="50%" align="right">Nombre</td>
					<td width="50%"><input type="text" value="<%=HTMLHelper.displayField(supervisorSeleccionado.nombre)%>" name="nombre" maxlength="70">
					<input type="hidden" value="<%=supervisorSeleccionado.idSupervisor %>" name="idSupervisor">
					</td>
				</tr>
				<tr>
					<td width="50%" align="right">Apellido Paterno</td>
					<td width="50%"><input type="text" value="<%=HTMLHelper.displayField(supervisorSeleccionado.aPaterno)%>" name="aPaterno" maxlength="70"></td>
				</tr>
				<tr>
					<td width="50%" align="right">Apellido Materno</td>
					<td width="50%"><input type="text" value="<%=HTMLHelper.displayField(supervisorSeleccionado.aMaterno)%>" name="aMaterno" maxlength="70"></td>
				</tr>
				<tr>
					<td width="50%" align="right">Status</td>
					<td width="50%">
						<select name="estatus">						
						<%= HTMLHelper.getComboStatusSupervisor(supervisorSeleccionado.estatus) %>						
						</select>
					</td>
				</tr>
				<%
				}
				%>
				<tr>
					<td colspan="2">
				<%if ( listaSupervisor!=null ){
				%>
					<br><table width="100%" border="0">
						    <tr bgcolor="#009865">
						        <td class="whitetext" align="center">&nbsp;</td>
						    	<td class="whitetext" align="center">Numero</td>
								<td class="whitetext" align="center">Nombres</td>
								<td class="whitetext" align="center">Apellido Paterno</td>
								<td class="whitetext" align="center">Apellido Materno</td>
								<td class="whitetext" align="center">Status</td>
								<td class="whitetext" align="center">Fecha última modificación</td>
							</tr>
							<% 
								for(i=0; i<listaSupervisor.size();i++) {		
									supervisor = (SupervisorCreditoVO)listaSupervisor.get(i);
							%>	
							<tr>
								<td><input type="radio" name="opcion" value="<%=supervisor.idSupervisor%>" checked>&nbsp;</td>
								<td><%=supervisor.idSupervisor%></td>
								<td><%=supervisor.nombre%></td>
								<td><%=supervisor.aPaterno%></td>
								<td><%=supervisor.aMaterno%></td>
								<td><%= HTMLHelper.getStatusEjecutivo(supervisor.estatus)%></td>
								<td><%= HTMLHelper.displayField(supervisor.fechaHoramodificacion)%></td>
							</tr>
							<% 
								}
							%>
					</table>	
										
				<%
				}	//fin lista supervisor
				%>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<br><input type="button" value="Listar" onClick="listaSupervisor()">
					    &nbsp;
					    <% if(listaSupervisor!=null){ %>
						<input type="button" value="Escoger" onClick="seleccionaSupervisor()">
					    &nbsp;
					    <% } %>
					    <%if ( supervisorSeleccionado!=null ){  %>
						<input type="button" value="Aceptar" onClick="modificarSupervisor()">
						<% }  %>
						<input type="button" value="Regresar" onClick="redireccionMenuAdmin()">
					</td>						
				</tr>
				
			</table>
		</td>
	</tr>		
</table>
</form>
<jsp:include page="footer.jsp" flush="true"/>

</body>
</html>
