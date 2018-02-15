<%@page import="com.sicap.clientes.vo.CobradorVO"%>
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
if(request.getAttribute("ACTUALIZACION_COBRADOR")!= null)
	muestrapantallaactualizacion = (Integer)request.getAttribute("ACTUALIZACION_COBRADOR");

TreeMap catSucursales = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SUCURSALES);
//UsuarioVO usuario = (UsuarioVO)session.getAttribute("USUARIO");
//TreeMap catSucursales = CatalogoHelper.getCatalogoSucursal(usuario.sucursales);
CobradorVO cobrador = null;
CobradorVO cobradorSeleccionado = null;
ArrayList listaCobradores = null;

//Listado de ejecutivos
if(request.getAttribute("COBRADORES")!=null) {
	listaCobradores = (ArrayList)request.getAttribute("COBRADORES");
}
//Ejecutivo seleccionado
if(request.getAttribute("COBRADOR")!=null) {    
	cobradorSeleccionado = (CobradorVO)request.getAttribute("COBRADOR");
}
System.out.println("PANTALLA: "+muestrapantallaactualizacion);
%>
<html>
<head>
<title>Modificaci&oacute;n Cobradores</title>
<script language="Javascript" src="./js/functions.js"></script>

<script type="text/javascript">
<!--
	function listaCobradores(){
		window.document.forma.command.value = 'listaCobradores';
		window.document.forma.submit();
	}
	
	function seleccionarCobrador(){
	    window.document.forma.command.value = 'seleccionarCobrador';	     
		window.document.forma.submit();
	}

	function modificarCobrador(Estatus, Sucursal){
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
            if (window.document.forma.estatus.value != Estatus && window.document.forma.estatus.value == 2){            
                    alert('La unica manera de Desactivar un Cobrador es Activando otro');
                    window.document.forma.estatus.value = Estatus;
                    return false;
            }
            if (Estatus == 1 && window.document.forma.idSucursal.value!=Sucursal){
                    alert('No se puede cambiar de sucursal un Cobrador Activo');
                    window.document.forma.estatus.value = Estatus;
                    window.document.forma.idSucursal.value = Sucursal;                    
                    return false;                
            }
            if (window.document.forma.estatus.value == 1) {
                    if (!confirm("Si existe un Cobrador Activo este pasara al estatus de Inactivo. ¿Desea Continuar?")) {
                        return false;
                    }
            }
                
            window.document.forma.command.value = 'modificarCobrador';
            window.document.forma.submit();
	}
	
	function redireccionMenuAdmin(){
		window.document.forma.command.value = 'administracionCobradores';
		window.document.forma.submit();
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
			<h3>Modificaci&oacute;n de Cobradores</h3>
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
				<%if(cobradorSeleccionado!=null || muestrapantallaactualizacion == 1) {
				%>
				<tr>
					<td width="50%" align="right">Nombre</td>
					<td width="50%"><input type="text" value="<%=HTMLHelper.displayField(cobradorSeleccionado.getNombre())%>" name="nombre" id="nombre" maxlength="70">
					<input type="hidden" value="<%=cobradorSeleccionado.getIdCobrador()%>" name="idCobrador" id="idCobrador">
					</td>
				</tr>
				<tr>
					<td width="50%" align="right">Apellido Paterno</td>
					<td width="50%"><input type="text" value="<%=HTMLHelper.displayField(cobradorSeleccionado.getaPaterno())%>" name="aPaterno" id="aPaterno" maxlength="70"></td>
				</tr>
				<tr>
					<td width="50%" align="right">Apellido Materno</td>
					<td width="50%"><input type="text" value="<%=HTMLHelper.displayField(cobradorSeleccionado.getaMaterno())%>" name="aMaterno" id="aMaterno" maxlength="70"></td>
				</tr>
				<tr>
					<td width="50%" align="right">Estatus</td>
					<td width="50%">
						<select name="estatus" id="estatus">						
						<%= HTMLHelper.getComboStatusCobrador(cobradorSeleccionado.getEstatus()) %>						
						</select>
					</td>
				</tr>

                                <%}%>
				<tr>
					<td colspan="2">
				<%if ( listaCobradores!=null ){
                                    System.out.println("La lista no esta vacia");
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
								for(i=0; i<listaCobradores.size();i++) {		
									cobrador = (CobradorVO)listaCobradores.get(i);
							%>	
							<tr>
								<td><input type="radio" name="opcion" value="<%=cobrador.getIdCobrador()%>" checked>&nbsp;</td>
								<td><%=cobrador.getIdCobrador()%></td>
								<td><%=cobrador.getNombre()%></td>
								<td><%=cobrador.getaPaterno()%></td>
								<td><%=cobrador.getaMaterno()%></td>
								<td><%= HTMLHelper.getStatusCobrador(cobrador.getEstatus())%></td>
								<td><%= HTMLHelper.displayField(cobrador.getFechaHoramodificacion())%></td>
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
						<br><input type="button" value="Listar" onClick="listaCobradores()">
					    &nbsp;
					    <% if(listaCobradores!=null){ %>
						<input type="button" value="Escoger" onClick="seleccionarCobrador()">
					    &nbsp;
					    <% } %>
					    <%if ( cobradorSeleccionado!=null ){  %>
						<input type="button" value="Aceptar" onClick="modificarCobrador(<%=cobradorSeleccionado.getEstatus()%>,<%=cobradorSeleccionado.getIdSucursal()%>)">
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

