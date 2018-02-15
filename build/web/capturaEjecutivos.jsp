<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.TreeMap"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
EjecutivoCreditoVO ejecutivo = (EjecutivoCreditoVO)request.getAttribute("EJECUTIVOS");
	if(ejecutivo==null) 
		ejecutivo = new EjecutivoCreditoVO();
int idSucursal = HTMLHelper.getParameterInt(request, "idSucursal");
//int idSucursal = 2;
Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
SolicitudVO solicitud = new SolicitudVO();
TreeMap catSucursales = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SUCURSALES);
TreeMap catTipoEjecutivos = CatalogoHelper.getCatalogoSeleccione(ClientesConstants.CAT_TIPO_EJECUTIVOS);
TreeMap catEjecutivosActivos = CatalogoHelper.getCatalogoEjecutivosActivos(idSucursal);
Logger.debug("Ejecutivo:"+ejecutivo.toString());
   
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
<title>Alta de Ejecutivos</title>
<script language="Javascript" src="./js/functions.js"></script>
<script type="text/javascript">
function guardaEjecutivos(){

        if ( window.document.forma.idSucursal.value==0){
		   alert('Debe seleccionar una sucursal');
		   return false;
		}
        if ( window.document.forma.nombre.value=='' || !esTexto( "^[a-zA-Z ·ÈÌÛ˙A…Õ”⁄—Ò]+$",window.document.forma.nombre.value)){
		   alert('Debe introducir un Nombre v\u00e1lido');
		   return false;
		}
		if ( window.document.forma.aPaterno.value=='' || !esTexto( "^[a-zA-Z ·ÈÌÛ˙A…Õ”⁄—Ò]+$",window.document.forma.aPaterno.value)){
		   alert('Debe introducir un Apellido Paterno v\u00e1lido');
		   return false;
		}

		if ( window.document.forma.aMaterno.value=='' || !esTexto( "^[a-zA-Z ·ÈÌÛ˙A…Õ”⁄—Ò]+$",window.document.forma.aMaterno.value)){
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
		
		window.document.forma.submit();
	}
	
	function redireccionMenuAdmin(){
		window.document.forma.command.value = 'administracionEjecutivos';
		window.document.forma.submit();
	}
        function consultaEjecutivo(){
            window.document.forma.command.value='buscaEjecutivosActivos';
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
                
</script>
</head>

<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<table border="0" width="100%">
	<tr>
		<td align="center">
		<h3>Alta de Ejecutivos</h3>
		<table border="0" width="100%">
			<tr>
				<td align="center">
				    <%=HTMLHelper.displayNotifications(notificaciones)%>
					<form name="forma" action="admin" method="POST">
						<input type="hidden" name="command" value="guardaEjecutivos">
						<input type="hidden" name="idSolicitud" value="<%=solicitud.idSolicitud%>">
						<table width="100%" border="0" cellpadding="0">
  							<tr>
    							<td width="50%" align="right">Sucursal</td>
    							<td width="50%" align="left">
                                                            <select name="idSucursal" id="idSucursal" onchange="consultaEjecutivo()">
    									<%= HTMLHelper.displayCombo(catSucursales, HTMLHelper.getParameterInt(request, "idSucursal"))%>
                                                            </select>
								</td>
  							</tr>
                                                                                                                
							<tr>
  								<td align="right">Nombre</td>
  								<td width="50%" align="left">  
  									<input type="text" name="nombre" size="45" maxlength="70" value="<%=HTMLHelper.displayField(ejecutivo.nombre)%>">
  								</td>
							</tr>
							<tr>
  								<td width="50%" align="right">Apellido paterno</td>
  								<td width="50%" align="left">   
  									<input type="text" name="aPaterno" size="45" maxlength="70" value="<%=HTMLHelper.displayField(ejecutivo.aPaterno)%>">
  								</td>
							</tr>
							<tr>
  								<td width="50%" align="right">Apellido materno</td>
  							    <td width="50%" align="left">  
  									<input type="text" name="aMaterno" size="45" maxlength="70" value="<%=HTMLHelper.displayField(ejecutivo.aMaterno)%>">
  								</td>
							</tr>
                                                        <tr>
    							<td width="50%" align="right">Puesto</td>
    							<td width="50%" align="left">
                                                            <select name="tipoEjecutivo" id="tipoEjecutivo" onchange="validaUpline()">
    									<%= HTMLHelper.displayCombo(catTipoEjecutivos, ejecutivo.tipoEjecutivo)%>
									</select>
								</td>
  							</tr>
                                                        <tr>
    							<td width="50%" align="right">Jefe inmediato</td>
    							<td width="50%" align="left">
                                                            <% if(ejecutivo.tipoEjecutivo==1){%>
                                                            <select name="upline" id="upline" disabled="true">
                                                            <%} else {%>
                                                            <select name="upline" id="upline" >
                                                            <%}%>
    									<%= HTMLHelper.displayCombo(catEjecutivosActivos, ejecutivo.upline)%>
									</select>
								</td>
  							</tr>
  							<tr>
								<td align="center" colspan="2">
									<br><input type="button" value="Enviar" onClick="guardaEjecutivos()">
									   <input type="button" value="Regresar" onClick="redireccionMenuAdmin()">
								</td>
							</tr>
						</table>
					</form>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>	
<jsp:include page="footer.jsp" flush="true"/>
</body>
</html>