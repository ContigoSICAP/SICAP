<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.sicap.clientes.vo.*"%>
<html>
<head>
<title>Modificaci&oacute;n Representantes</title>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
<link href="./css/main.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
<!--

function desabilitaManejoFeriados(){

if ($("#idEvitaFeriados").val() == '2'){
	$("#idManejoFeriados").attr('disabled','-1');
}
else {
    $("#idManejoFeriados").removeAttr('disabled')
}

}

	function consultaRepresentantes(){
		if ( window.document.forma.idSucursal.value==0){
		   alert('Debe seleccionar una sucursal');
		   return false;
		}
		window.document.forma.command.value = 'consultaRepresentantes';
		window.document.forma.submit();
	}
	
	function seleccionaRepresentante(idRepresentante){
		window.document.forma.command.value = 'seleccionaRepresentante';
		window.document.forma.idRepresentante.value=idRepresentante;
		window.document.forma.submit();
	}
	
	function modificarRepresentante(idRepresentante){
		if ( window.document.forma.idSucursal.value==0){
		   alert('Debe seleccionar una sucursal');
		   return false;
		}
		if ( window.document.forma.txtNombre.value=='' || !esTexto( "^[a-zA-Z áéíóúAÉÍÓÚÑñ]+$",window.document.forma.txtNombre.value)){
		  	alert('Debe introducir un Nombre v\u00e1lido');
		   	return false;
		}
		if ( window.document.forma.txtFactor.value<0){
		  	alert('Debe introducir un factor');
		   	return false;
		}
		window.document.forma.command.value = 'modificarRepresentante';
		window.document.forma.idRepresentante.value=idRepresentante;
		window.document.forma.submit();
	}
	
	function redireccionMenuAdminRep(){
		window.document.forma.command.value = 'administracionRepresentantes';
		window.document.forma.submit();
	}

//-->
</script>
</head>

<%
int i = 0;
int muestrapantallaactualizacion = 0;
Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
int idSucursal = HTMLHelper.getParameterInt(request, "idSucursal");
//int idEjecutivo = HTMLHelper.getParameterInt(request, "idRepresentante");

if(request.getAttribute("ACTUALIZACION_REPRESENTANTE")!= null)
	muestrapantallaactualizacion = (Integer)request.getAttribute("ACTUALIZACION_REPRESENTANTE");

//TreeMap catSucursales = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SUCURSALES);
UsuarioVO usuario = (UsuarioVO)session.getAttribute("USUARIO");
TreeMap catSucursales = CatalogoHelper.getCatalogoSucursal(usuario.sucursales);
TreeMap catSegmento = CatalogoHelper.getCatalogoSegmento();
TreeMap catTipoTabla = CatalogoHelper.getCatalogoTipoTabla();
TreeMap catBaseInteres = CatalogoHelper.getCatalogoBaseInteres();
TreeMap catEvitaFeriados = CatalogoHelper.getCatalogoEvitaFeriados();
TreeMap catManejoFeriados = CatalogoHelper.getCatalogoManejoFeriados();
TreeMap catUnidadTiempo = CatalogoHelper.getCatalogoUnidadTiempo();
RepresentantesVO representante = null;
RepresentantesVO representanteSeleccionado = null;
ArrayList listaRepresentantes = null;

//Listado de ejecutivos
if(request.getAttribute("REPRESENTANTES")!=null) {
	listaRepresentantes = (ArrayList)request.getAttribute("REPRESENTANTES");
}
//Ejecutivo seleccionado
if(request.getAttribute("REPRESENTANTE")!=null) {
	representanteSeleccionado = (RepresentantesVO)request.getAttribute("REPRESENTANTE");
}

%>

<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<form name="forma" action="admin" method="post">
<input type="hidden" name="command" value="">
<input type="hidden" name="idRepresentante" value="">

<table border="0" cellspacing="0" width="100%">
		<tr>
		<td align="center">
			<h3>Modificaci&oacute;n de Convenios<br></h3>
			<%=HTMLHelper.displayNotifications(notificaciones)%>
			<br>
		</td>
		</tr>
		
        <tr>
        <td>
        			<table align="center" class="tabla_l" border="0">
                    <tr>
					<td width="50%" align="right">Sucursal</td>
					<td width="50%" align="left">  
						<select name="idSucursal" size="1">
						<%=HTMLHelper.displayCombo(catSucursales, idSucursal)%>
						</select>
					</td>
                    </tr>
                    </table>
        </td>
		</tr>
        
		<tr>
		<td>
			<table align="center" class="tabla_l" border="0">
				
				
				<%if(representanteSeleccionado!=null || muestrapantallaactualizacion == 1) {
				%>
				<tr>
					<td class='texto_l'>Nombre</td>
					<td class='texto_r'><input type="text" value="<%=HTMLHelper.displayField(representanteSeleccionado.nombre)%>" name=		"txtNombre" maxlength="90" size="45">
					<input type="hidden" value="<%=representanteSeleccionado.idRepresentante%>" name="idRepresentante1">
					</td>
				</tr>
  				
  				<tr>
    				<td class='texto_l'>Segmento</td>
    				<td class='texto_r'>
    				<select name="idSegmento" id="idSegmento">
    				<%= HTMLHelper.displayCombo(catSegmento, representanteSeleccionado.segmento)%>
					</select>
					</td>
                    <td class='texto_l'>Tipo de Tabla</td>
    				<td class='texto_r'>
    				<select name="idTipoTabla" id="idTipoTabla">
    				<%= HTMLHelper.displayCombo(catTipoTabla, representanteSeleccionado.tipoTabla)%>
					</select>
					</td>
  				</tr>
  							
  				<tr>
    				<td class='texto_l'>Base Interés</td>
    				<td class='texto_r'>
    				<select name="idBaseInteres" id="idBaseInteres">
    				<%= HTMLHelper.displayCombo(catBaseInteres, representanteSeleccionado.baseInteres)%>
					</select>
					</td>
                    <td class='texto_l'>Tasa Mora</td>
  					<td class='texto_r'>  
  					<input type="text" name="tasaMora" id="tasaMora" size="45" maxlength="70" value="<%=HTMLHelper.displayField(representanteSeleccionado.tasaMora)%>">
  					</td>
  				</tr>
                	
				<tr>
  					<td class='texto_l'>Gasto de Cobranza</td>
  					<td class='texto_r'>  
  					<input type="text" name="gastoCobranza" id="gastoCobranza" size="45" maxlength="70" value="<%=HTMLHelper.displayField(representanteSeleccionado.gastoCobranza)%>">
  					</td>
                    <td class='texto_l'>Evita Feriados</td>
    				<td class='texto_r'>
    				<select name="idEvitaFeriados" id="idEvitaFeriados" onclick="desabilitaManejoFeriados()">
    				<%= HTMLHelper.displayCombo(catEvitaFeriados, representanteSeleccionado.feriado)%>
					</select>
					</td>
				</tr>
  							
  				<tr>
    				<td class='texto_l'>Unidad de Tiempo</td>
    				<td class='texto_r'>
    				<select name="idUnidadTiempo" id="idUnidadTiempo">
    				<%= HTMLHelper.displayCombo(catUnidadTiempo, representanteSeleccionado.unidadTiempo)%>
					</select>
					</td>
                    <td class='texto_l'>Manejo de Feriados</td>
    				<td class='texto_r'>
    				<select name="idManejoFeriados" id="idManejoFeriados">
    				<%= HTMLHelper.displayCombo(catManejoFeriados, representanteSeleccionado.manejoFeriado)%>
					</select>
					</td>
  				</tr>
							
				<tr>
  					<td class='texto_l'>Plazo Máximo</td>
  					<td class='texto_r'>  
  					<input type="text" name="plazoMaximo" id="plazoMaximo" size="45" maxlength="70" value="<%=HTMLHelper.displayField(representanteSeleccionado.gastoCobranza)%>">
  					
  					</td>
                    <td class='texto_l'>Plazo Mínimo</td>
  					<td class='texto_r'>  
  					<input type="text" name="plazoMinimo" id="plazoMinimo" size="45" maxlength="70" value="<%=HTMLHelper.displayField(representanteSeleccionado.plazoMinimo)%>">
  					
  					</td>
				</tr>
							
				<tr>
  					<td class='texto_l'>Días de Gracias</td>
  					<td class='texto_r'>  
  					<input type="text" name="diaGracia" id="diaGracia" size="45" maxlength="70" value="<%=HTMLHelper.displayField(representanteSeleccionado.diaGracia)%>">
  					
  					</td>
                    <td class='texto_l'>Días de Pago</td>
  					<td class='texto_r'>  
  					<input type="text" name="diaPago" id="diaPago" size="45" maxlength="70" value="<%=HTMLHelper.displayField(representanteSeleccionado.diaPago)%>">
  					
  					</td>
				</tr>
				
				<tr>
					<td class='texto_l'>Factor</td>
					<td class='texto_r'><input type="text" value="<%=HTMLHelper.displayField(representanteSeleccionado.factor)%>" name="txtFactor" maxlength="6" size="45">
					<input type="hidden" value="<%=representanteSeleccionado.factor%>" name="factor1">
					</td>
                    <td class='texto_l'>Status</td>
					<td class='texto_r'>
						<select name="estatus">
						<%= HTMLHelper.getComboStatusEjecutivo(representanteSeleccionado.estatus) %>
						</select>
					</td>
				</tr>
				</table>
			</td>
			</tr>
				<%
				}
				%>
				<tr>
					<td colspan="2">
				<%if ( listaRepresentantes!=null ){
				%>
					<br><table width="100%" border="0" align="center">
						    <tr bgcolor="#009865">
						        <td class="whitetext" align="center">Numero</td>
								<td class="whitetext" align="center">Nombre</td>
								<td class="whitetext" align="center">Status</td>
								<td class="whitetext" align="center">Accion</td>
							</tr>
							<% 
								for(i=0; i<listaRepresentantes.size();i++) {		
									representante = (RepresentantesVO)listaRepresentantes.get(i);
							%>	
							<tr>
								
								<td align="center"><%=representante.idRepresentante%></td>
								<td><%=representante.nombre%></td>
								<td align="center"><%= HTMLHelper.getStatusEjecutivo(representante.estatus)%></td>
								<td align="center"><a href="#" onClick="seleccionaRepresentante(<%=representante.idRepresentante%>)">Modificar</a></td>
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
						<br><input type="button" value="Listar" onClick="consultaRepresentantes()">
					    &nbsp;
					    <%if ( representanteSeleccionado!=null ){  %>
						<input type="button" value="Aceptar" onClick="modificarRepresentante(<%=representanteSeleccionado.idRepresentante%>)">
						<% }  %>
						<input type="button" value="Regresar" onClick="redireccionMenuAdminRep()">
					</td>						
				</tr>
				
			</table>

</form>
<jsp:include page="footer.jsp" flush="true"/>

</body>
</html>
