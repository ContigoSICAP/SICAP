<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.TreeMap"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
<link href="./css/main.css" rel="stylesheet" type="text/css">
<title>Alta de Ejecutivos</title>

<script type="text/javascript">

function desabilitaManejoFeriados(){

if ($("#idEvitaFeriados").val() == '2'){
	$("#idManejoFeriados").attr('disabled','-1');
}
else {
    $("#idManejoFeriados").removeAttr('disabled')
}

}


function guardaRepresentantes(){

        if ( window.document.forma.idSucursal.value==0){
		   alert('Debe seleccionar una sucursal');
		   return false;
		}
        if ( window.document.forma.nombre.value=='' || !esTexto( "^[a-zA-Z áéíóúAÉÍÓÚÑñ]+$",window.document.forma.nombre.value)){
		   alert('Debe introducir un Nombre v\u00e1lido');
		   return false;
		}
        if ( window.document.forma.factor.value<0){
		   alert('Debe de incluir un factor');
		   return false;
		}
		
		window.document.forma.submit();
	}
	
	function redireccionMenuAdmin(){
		window.document.forma.command.value = 'administracionRepresentantes';
		window.document.forma.submit();
	}
</script>
</head>
<%
RepresentantesVO representante = (RepresentantesVO)request.getAttribute("REPRESENTANTES");
	if(representante==null) 
		representante = new RepresentantesVO();

Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
SolicitudVO solicitud = new SolicitudVO();
TreeMap catSucursales = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SUCURSALES);
TreeMap catSegmento = CatalogoHelper.getCatalogoSegmento();
TreeMap catTipoTabla = CatalogoHelper.getCatalogoTipoTabla();
TreeMap catBaseInteres = CatalogoHelper.getCatalogoBaseInteres();
TreeMap catEvitaFeriados = CatalogoHelper.getCatalogoEvitaFeriados();
TreeMap catManejoFeriados = CatalogoHelper.getCatalogoManejoFeriados();
TreeMap catUnidadTiempo = CatalogoHelper.getCatalogoUnidadTiempo();

Logger.debug("Representante:"+representante.toString());
   
%>
<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<table border="0" align="center" class="tabla_l">
	<tr>
		<td align="center">
		<h3>Alta Convenio</h3>
        </td>
    </tr>
    <tr>
		<td>&nbsp;</td>
    	<td>&nbsp;</td>
    </tr>
    
    <tr>
		<td align="center">
		<%=HTMLHelper.displayNotifications(notificaciones)%>
        </td>
    </tr>
    
    
    <tr> 
    <td>   
		<table border="0" align="center" class="tabla_l" style="width:100%">
			<tr>
				<td valign="top"><br></td><td align="center">
				    
					<form name="forma" action="admin" method="POST">
						<input type="hidden" name="command" value="guardaRepresentantes">
						<input type="hidden" name="idSolicitud" value="<%=solicitud.idSolicitud%>">
                  </td>
                  </tr>
						
							
							<tr>
  								<td valign="top"><br></td><td class='texto_l'>Nombre Convenio</td>
  								<td class='texto_r'>  
  									<input type="text" name="nombre" size="30" maxlength="70" value="<%=HTMLHelper.displayField(representante.nombre)%>">
  								</td>
  								<td class='texto_l'>Sucursal</td>
    							<td class='texto_r'>
    								<select name="idSucursal" id="idSucursal">
    									<%= HTMLHelper.displayCombo(catSucursales, representante.idSucursal)%>
									</select>
								</td>
							</tr>
  							
  							<tr>
    							<td valign="top"><br></td><td class='texto_l'>Segmento</td>
    							<td class='texto_r'>
    								<select name="idSegmento" id="idSegmento">
    									<%= HTMLHelper.displayCombo(catSegmento, representante.segmento)%>
									</select>
								</td>
								<td class='texto_l'>Tipo de Tabla</td>
    							<td class='texto_r'>
    								<select name="idTipoTabla" id="idTipoTabla">
    									<%= HTMLHelper.displayCombo(catTipoTabla, representante.tipoTabla)%>
									</select>
								</td>
  							</tr>
  							
  							<tr>
    							<td valign="top"><br></td><td class='texto_l'>Base Interés</td>
    							<td class='texto_r'>
    								<select name="idBaseInteres" id="idBaseInteres">
    									<%= HTMLHelper.displayCombo(catBaseInteres, representante.baseInteres)%>
									</select>
								</td>
								<td class='texto_l'>Factor Tasa Mora</td>
  								<td class='texto_r'>  
  									<input type="text" name="tasaMora" id="tasaMora" size="5" maxlength="70" value="<%=HTMLHelper.displayField(representante.tasaMora)%>">
  								</td>
  							</tr>
							
							<tr>
  								<td valign="top"><br></td><td class='texto_l'>Gasto de Cobranza</td>
  								<td class='texto_r'>  
  									<input type="text" name="gastoCobranza" id="gastoCobranza" size="5" maxlength="70" value="<%=HTMLHelper.displayField(representante.gastoCobranza)%>">
  								</td>
  								<td class='texto_l'>Evita Feriados</td>
    							<td class='texto_r'>
    								<select name="idEvitaFeriados" id="idEvitaFeriados" onclick="desabilitaManejoFeriados()">
    									<%= HTMLHelper.displayCombo(catEvitaFeriados, representante.feriado)%>
									</select>
								</td>
							</tr>
							
  							<tr>
  							
    							<td valign="top"><br></td><td class='texto_l'>Unidad de Tiempo</td>
    							<td class='texto_r'>
    								<select name="idUnidadTiempo" id="idUnidadTiempo">
    									<%= HTMLHelper.displayCombo(catUnidadTiempo, representante.unidadTiempo)%>
									</select>
								</td>
    							<td class='texto_l'>Manejo de Feriados</td>
    							<td class='texto_r'>
    								<select name="idManejoFeriados" id="idManejoFeriados">
    									<%= HTMLHelper.displayCombo(catManejoFeriados, representante.manejoFeriado)%>
									</select>
								</td>
  							</tr>
  							
							<tr>
  								<td valign="top"><br></td><td class='texto_l'>Plazo Máximo</td>
  								<td class='texto_r'>  
  									<input type="text" name="plazoMaximo" id="plazoMaximo" size="5" maxlength="70" value="<%=HTMLHelper.displayField(representante.plazoMaximo)%>">
  								</td>
  								<td class='texto_l'>Plazo Mínimo</td>
  								<td class='texto_r'>  
  									<input type="text" name="plazoMinimo" id="plazoMinimo" size="5" maxlength="70" value="<%=HTMLHelper.displayField(representante.plazoMinimo)%>">
  								</td>
							</tr>
						
							<tr>
  								<td valign="top"><br></td><td class='texto_l'>Días de Gracia</td>
  								<td class='texto_r'>  
  									<input type="text" name="diaGracia" id="diaGracia" size="5" maxlength="70" value="<%=HTMLHelper.displayField(representante.diaGracia)%>">
  								</td>
  								<td class='texto_l'>Días de Pago</td>
  								<td class='texto_r'>  
  									<input type="text" name="diaPago" id="diaPago" size="5" maxlength="70" value="<%=HTMLHelper.displayField(representante.diaPago)%>">
  								</td>
							</tr>
							
							<tr>
  								<td valign="top"><br></td><td class='texto_l'>Factor de Capacidad de Pago</td>
  								<td class='texto_r'>  
  									<input type="text" name="factor" size="6" maxlength="6" value="<%=HTMLHelper.displayField(representante.factor)%>">
  								</td>
							</tr>
							
							<tr>
    							<td>&nbsp;</td>
    							<td>&nbsp;</td>
  							</tr>
                            
							<tr>
								<td valign="top"><br></td>
								<td>&nbsp;</td>
								<td class='texto_l'>
									<input type="button" value="Enviar" onClick="guardaRepresentantes()">
								</td>	   
								<td class='texto_r'>	   
									   <input type="button" value="Regresar" onClick="redireccionMenuAdmin()">
								</td>
							</tr>
					</form>
                    
		</table>
        </td>
        </tr>
</table>	
<jsp:include page="footer.jsp" flush="true"/>
</body>
</html>