<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.sicap.clientes.vo.EjecutivoCreditoVO"%>

<%
int i = 0;
int muestrapantallaactualizacion = 0;
Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
int idSucursal = HTMLHelper.getParameterInt(request, "idSucursal");
String nombre = HTMLHelper.getParameterString(request, "nombre");
String aPaterno = HTMLHelper.getParameterString(request, "aPaterno");
String aMaterno = HTMLHelper.getParameterString(request, "aMaterno");
String estatus = HTMLHelper.getParameterString(request, "estatus");
if(request.getAttribute("ACTUALIZACION_EJECUTIVO")!= null)
	muestrapantallaactualizacion = (Integer)request.getAttribute("ACTUALIZACION_EJECUTIVO");

TreeMap catSucursales = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SUCURSALES);
TreeMap catTipoEjecutivos = CatalogoHelper.getCatalogoSeleccione(ClientesConstants.CAT_TIPO_EJECUTIVOS);
TreeMap catEjecutivosActivos = CatalogoHelper.getCatalogoEjecutivosActivos(idSucursal);
//UsuarioVO usuario = (UsuarioVO)session.getAttribute("USUARIO");
//TreeMap catSucursales = CatalogoHelper.getCatalogoSucursal(usuario.sucursales);
EjecutivoCreditoVO ejecutivo = null;
EjecutivoCreditoVO ejecutivoSeleccionado = null;
ArrayList listaEjecutivos = null;

//Listado de ejecutivos
if(request.getAttribute("EJECUTIVOS")!=null) {
	listaEjecutivos = (ArrayList)request.getAttribute("EJECUTIVOS");
}
//Ejecutivo seleccionado
if(request.getAttribute("EJECUTIVO")!=null) {
	ejecutivoSeleccionado = (EjecutivoCreditoVO)request.getAttribute("EJECUTIVO");
}
System.out.println("PANTALLA: "+muestrapantallaactualizacion);
%>
<html>
<head>
<title>Modificaci&oacute;n Ejecutivos</title>
<script language="Javascript" src="./js/functions.js"></script>

<script type="text/javascript">
<!--
	function listaEjecutivos(){
		window.document.forma.command.value = 'listaEjecutivos';
		window.document.forma.submit();
	}
	
	function seleccionarEjecutivo(){
	    window.document.forma.command.value = 'seleccionarEjecutivo';	     
		window.document.forma.submit();
	}

	function modificarEjecutivo(){
            if ( window.document.forma.idSucursal.value==0){
		   alert('Debe seleccionar una sucursal');
		   return false;
		}
        if ( window.document.forma.nombre.value=='' || !esTexto( "^[a-zA-Z áéíóúAÉÍÓÚÑñ]+$",window.document.forma.nombre.value)){
		   alert('Debe introducir un Nombre v\u00e1lido');
		   return false;
		}
		if ( window.document.forma.aPaterno.value=='' || !esTexto( "^[a-zA-Z áéíóúAÉÍÓÚÑñ]+$",window.document.forma.aPaterno.value)){
		   alert('Debe introducir un Apellido Paterno v\u00e1lido');
		   return false;
		}

		if ( window.document.forma.aMaterno.value=='' || !esTexto( "^[a-zA-Z áéíóúAÉÍÓÚÑñ]+$",window.document.forma.aMaterno.value)){
		   alert('Debe introducir un Apellido Materno v\u00e1lido');
		   return false;
		}
                if ( window.document.forma.tipoEjecutivo.value==0){
		   alert('Debe seleccionar el puesto');
		   return false;
		}                
		if ( window.document.forma.upline.value==''||window.document.forma.upline.value==0&&window.document.forma.tipoEjecutivo.value!=1){
		   alert('Debe seleccionar Jefe Inmediato');
		   return false;
		}
		window.document.forma.command.value = 'modificarEjecutivo';
		window.document.forma.submit();
	}
	
	function redireccionMenuAdmin(){
		window.document.forma.command.value = 'administracionEjecutivos';
		window.document.forma.submit();
	}
        function buscaEjecutivo(){
             window.document.forma.command.value='muestraEjecutivosActivos';
             window.document.forma.submit();
        }
        function validaUpline(){
            if (window.document.forma.tipoEjecutivo.value==1){
                window.document.forma.upline.value=0;
                document.getElementById("upline").disabled = true;
                
            }
            else {
                document.getElementById("upline").disabled = false;
            }
        }

//-->
</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>

<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<form name="forma" action="admin" method="post">
<input type="hidden" name="command" value="">
<table border="0" width="100%">
	<tr>
		<td align="center">
			<h3>Modificaci&oacute;n de Ejecutivos</h3>
			<%=HTMLHelper.displayNotifications(notificaciones)%>
			<br>
			<table width="60%" border="0" cellpadding="0">
				<tr>
					<td width="50%" align="right">Sucursal</td>
					<td width="50%">
                                            <%if(ejecutivoSeleccionado!=null){%>
                                            <select name="idSucursal" size="1" onchange="buscaEjecutivo()">
						<%=HTMLHelper.displayCombo(catSucursales, idSucursal)%>
                                            </select>
                                            <%} else {%>
                                            <select name="idSucursal" size="1">
						<%=HTMLHelper.displayCombo(catSucursales, idSucursal)%>
                                            </select>
                                            <%}%>
					</td>
				</tr>
                                <% if(ejecutivoSeleccionado==null){%>
                                <tr>
                                        <td width="50%" align="right">Estatus</td>
					<td width="50%">
                                            <select name="selFilestatus" id="selFilestatus" size="1">
						<%=HTMLHelper.getComboFilStatusEjec()%>
                                            </select>
                                        </td>
                                </tr>
                                <%}%>
				<%if(ejecutivoSeleccionado!=null || muestrapantallaactualizacion == 1) {
				%>
				<tr>
					<td width="50%" align="right">Nombre</td>
					<td width="50%"><input type="text" value="<%=HTMLHelper.displayField(ejecutivoSeleccionado.nombre)%>" name="nombre" id="nombre" maxlength="70">
					<input type="hidden" value="<%=ejecutivoSeleccionado.idEjecutivo%>" name="idEjecutivo" id="idEjecutivo">
					</td>
				</tr>
				<tr>
					<td width="50%" align="right">Apellido Paterno</td>
					<td width="50%"><input type="text" value="<%=HTMLHelper.displayField(ejecutivoSeleccionado.aPaterno)%>" name="aPaterno" id="aPaterno" maxlength="70"></td>
				</tr>
				<tr>
					<td width="50%" align="right">Apellido Materno</td>
					<td width="50%"><input type="text" value="<%=HTMLHelper.displayField(ejecutivoSeleccionado.aMaterno)%>" name="aMaterno" id="aMaterno" maxlength="70"></td>
				</tr>
				<tr>
					<td width="50%" align="right">Estatus</td>
					<td width="50%">
						<select name="estatus" id="estatus">						
						<%= HTMLHelper.getComboStatusEjecutivo(ejecutivoSeleccionado.estatus) %>						
						</select>
					</td>
				</tr>
                                <tr>
    							<td width="50%" align="right">Puesto</td>
    							<td width="50%" align="left">
    								<select name="tipoEjecutivo" id="tipoEjecutivo" onchange="validaUpline()">
    									<%= HTMLHelper.displayCombo(catTipoEjecutivos, ejecutivoSeleccionado.tipoEjecutivo)%>
									</select>
								</td>
  							</tr>
                                                        <tr>
    							<td width="50%" align="right">Jefe inmediato</td>
    							<td width="50%" align="left">
                                                            <% if(ejecutivoSeleccionado.tipoEjecutivo==1){%>
                                                            <select name="upline" id="upline" disabled="true">
                                                            <%} else {%>
                                                            <select name="upline" id="upline" >
                                                            <%}%>
    									<%= HTMLHelper.displayCombo(catEjecutivosActivos, ejecutivoSeleccionado.upline)%>
									</select>
								</td>
  							</tr>
                                <%}%>
				<tr>
					<td colspan="2">
				<%if ( listaEjecutivos!=null ){
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
								for(i=0; i<listaEjecutivos.size();i++) {		
									ejecutivo = (EjecutivoCreditoVO)listaEjecutivos.get(i);
							%>	
							<tr>
								<td><input type="radio" name="opcion" value="<%=ejecutivo.idEjecutivo%>" checked>&nbsp;</td>
								<td><%=ejecutivo.idEjecutivo%></td>
								<td><%=ejecutivo.nombre%></td>
								<td><%=ejecutivo.aPaterno%></td>
								<td><%=ejecutivo.aMaterno%></td>
								<td><%= HTMLHelper.getStatusEjecutivo(ejecutivo.estatus)%></td>
								<td><%= HTMLHelper.displayField(ejecutivo.fechaHoramodificacion)%></td>
							</tr>
							<% 
								}
							%>
					</table>	
										
				<%
				}	//fin listaEjecutivos 
				%>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center">
						<br><input type="button" value="Listar" onClick="listaEjecutivos()">
					    &nbsp;
					    <% if(listaEjecutivos!=null){ %>
						<input type="button" value="Escoger" onClick="seleccionarEjecutivo()">
					    &nbsp;
					    <% } %>
					    <%if ( ejecutivoSeleccionado!=null ){  %>
						<input type="button" value="Aceptar" onClick="modificarEjecutivo()">
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
