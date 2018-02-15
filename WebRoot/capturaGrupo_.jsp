<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="com.sicap.clientes.vo.UsuarioVO"%>
<%@ page import="com.sicap.clientes.vo.GrupoVO"%>
<%@ page import="com.sicap.clientes.util.Notification"%>
<%@ page import="com.sicap.clientes.util.ClientesConstants"%>
<%@ page import="java.util.TreeMap"%>
<%@ page import="com.sicap.clientes.util.GrupoUtil"%>

<%
Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
UsuarioVO usuario = (UsuarioVO)session.getAttribute("USUARIO");
TreeMap catSucursales = CatalogoHelper.getCatalogoSucursal(usuario.sucursales);
TreeMap catEstatus = CatalogoHelper.getCatalogoEstatusCiclo();
TreeMap catIgnorarAlertasFuturas = CatalogoHelper.getCatalogoIgnorarAlertasFuturas();
//TreeMap catSucursales = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SUCURSALES);
TreeMap catCalificacionGrupal = CatalogoHelper.getCatalogoCalificacionGrupal();
TreeMap catTipogrupal = CatalogoHelper.getCatalogo(ClientesConstants.CAT_TIPOGRUPAL);
GrupoVO grupo = new GrupoVO();
int idGrupo = 0;
if ( request.getParameter("command").equals("capturaGrupo") )
	session.removeAttribute("GRUPO");
if ( session.getAttribute("GRUPO")!=null ){
	grupo = (GrupoVO)session.getAttribute("GRUPO");
	idGrupo = grupo.idGrupo;
}
boolean cicloActivo = GrupoUtil.tieneCicloActivo(grupo);
int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
boolean isMesaControl = request.isUserInRole("ANALISIS_CREDITO");
boolean isRestructuraFlag = grupo.idOperacion==ClientesConstants.REESTRUCTURA_GRUPAL;
boolean adminAlertas = request.isUserInRole("ADMINISTRADOR_ALERTAS");
String seguro = "";
%>

<html>
<head>
<title>Grupo</title>

<script type="text/javascript">

	function guardaGrupo(){
                document.getElementById("boton").disabled = true;
		window.document.forma.command.value='guardaGrupo';
		if ( window.document.forma.nombre.value=='' ){
			alert('Debe capturar el nombre del grupo');
			return false;
	    }
	    if ( window.document.forma.fechaFormacion.value!='' ){
			if ( !esFechaValida(window.document.forma.fechaFormacion,false) ){
				alert("La Fecha de formación es inv\u00e1lida");
				return false;
			}
		}else{
			alert("Debe capturar la Fecha de formación");
			return false;
		}
		document.body.style.cursor = "wait";
                window.document.forma.submit();
	}

	function consultaCliente(idCliente){
		window.opener.document.forma.command.value='consultaCliente';
		window.opener.document.forma.idCliente.value=window.document.forma.idCliente.value;
		window.opener.document.forma.submit();
	}

	function capturaCicloGrupal(){
	document.getElementById("td_nuevo").disabled = true;
        <% if ( grupo.ciclos!=null && grupo.ciclos.length>0 ){%>
		window.document.forma.command.value='renuevaCicloGrupal';
	<%}else{
		if( grupo.idOperacion == ClientesConstants.GRUPAL ){%>
			window.document.forma.command.value='capturaCicloGrupal';
		<%}else{%>
			window.document.forma.command.value='capturaCicloRestructura';
	<%}}%>
		window.document.forma.command.idCiclo=0;
		if ( window.document.forma.idOperacion.value==5 ){
			window.document.forma.nuevoCiclo.disabled = true;
			alert('Por el momento se encuentran suspendidas las reestructuras grupales en SICAP');
		}else{
			//window.document.forma.nuevoCiclo.disabled = false;
			document.body.style.cursor = "wait";
                        window.document.forma.submit();
		}
		
	}

	function capturaCicloOtraFinanciera(){
	document.getElementById("boton").disabled = true;
        <% if ( grupo.ciclos!=null && grupo.ciclos.length>0 ){%>
		window.document.forma.command.value='renuevaCicloGrupal';
	<%}else{
                if( grupo.idOperacion == ClientesConstants.GRUPAL ){%>
			window.document.forma.command.value='capturaCicloGrupal';
		<%}else{%>
			window.document.forma.command.value='capturaCicloRestructura';
	<%}}%>
		window.document.forma.command.idCiclo=0;
		window.document.forma.otraFinanciera.value=<%=ClientesConstants.CICLO_OTRA_FINANCIERA%>
		if ( window.document.forma.idOperacion.value==5 ){
			window.document.forma.nuevoCicloOtro.disabled = true;
			alert('Por el momento se encuentran suspendidas las reestructuras grupales en SICAP');
		}else{
			window.document.forma.nuevoCicloOtro.disabled = false;
                        alert ("otraFinanciera"+window.document.forma.command.value);
			document.body.style.cursor = "wait";
                        window.document.forma.submit();
		}
		
	}

	function consultaCicloGrupal(idCiclo, estatus){
            document.getElementById("td").disabled = true;
            <%if( grupo.idOperacion == ClientesConstants.GRUPAL ){%>
                        if(estatus == 7){
                            alert("El ciclo del grupo pertenece a TCI");
                            <%if (request.isUserInRole("admin")){%>
                                window.document.forma.command.value='consultaCicloGrupal';
                            <%} else {%>
                                window.document.forma.command.value='consultaGrupo';
                            <%}%>
                        } else if(estatus == 8){
                            alert("El ciclo del grupo pertenece a FinContigo");
                            <%if (request.isUserInRole("admin")){%>
                                window.document.forma.command.value='consultaCicloGrupal';
                            <%} else {%>
                                window.document.forma.command.value='consultaGrupo';
                            <%}%>
                        } else {
                            window.document.forma.command.value='consultaCicloGrupal';
                        }
			<%}else{%>
				window.document.forma.command.value='consultaCicloRestructura';
		<%}%>
                if(idCiclo == 1){
                    if(window.document.forma.conSeguro.checked){
                        //window.document.forma.conSeguro.value = "si";
                        window.document.forma.campSeguro.value = "si";
                    }
                } else {
                    if(window.document.forma.conSeguro[idCiclo-1].checked){
                        //window.document.forma.conSeguro[idCiclo-1].value = "si";
                        window.document.forma.campSeguro.value = "si";
                    }
                    //alert((idCiclo-1)+" "+window.document.forma.conSeguro[idCiclo-1].value);
                    //alert("- "+window.document.forma.campSeguro.value);
                }
		window.document.forma.idCiclo.value=idCiclo;
		document.body.style.cursor = "wait";
                window.document.forma.submit();
	}

	function regresaAGrupos(){
		window.document.forma.command.value = 'administraGrupos';
		window.document.forma.idGrupo.value = 0;
		window.document.forma.idSucursal.value = 0;
		document.body.style.cursor = "wait";
                window.document.forma.submit();
	}

	function regresaABusquedaGrupos(){
                document.getElementById("boton").disabled = true;
		window.document.forma.command.value = 'administraGrupos';
		window.document.forma.action="controller";
		document.body.style.cursor = "wait";
                window.document.forma.submit();
	}
	
	function consultaDetalleMonitor(idCiclo){
                document.getElementById("boton").disabled = true;
		window.document.forma.command.value = 'consultaDetalleMonitor';
		window.document.forma.idCiclo.value = idCiclo;
		document.body.style.cursor = "wait";
                window.document.forma.submit();
	}

	function validaTipo(){
		if ( window.document.forma.idOperacion.value==5 ){
			window.document.forma.boton.disabled = true;
			alert('Por el momento se encuentran suspendidas las reestructuras grupales en SICAP');
		}else{
			window.document.forma.boton.disabled = false;
		}

	}

	function capturaCicloRestructura(){
                document.getElementById("boton").disabled = true;
		window.document.forma.command.value='capturaCicloRestructura';
		window.document.forma.idOperacion.value=<%=ClientesConstants.REESTRUCTURA_GRUPAL%>
		window.document.forma.idCiclo.value=0;
		res = confirm('Esta apunto de generar un nuevo grupo de restructura. ¿Esta seguro de que desea registrar el nuevo grupo?');
    	if ( !res ) return res;
			document.body.style.cursor = "wait";
                        window.document.forma.submit();
	}
	
	
	function descartarAlertas(numeroCiclo){
		
		window.document.forma.command.value = "ignorarAlertasFuturas";
		window.document.forma.idCiclo.value = numeroCiclo;
		
		if( window.document.forma.motivoIgnorarAlertas.value==0 ){
			alert('Debe seleccionar un motivo');
			return false;
		}
		var comentario = prompt("Ingrese un comentario","");
		if(comentario==null || comentario=="" ){
			alert('Debe agregar un comentario');
			return false;
		}
		window.document.forma.comentarioIgnorarFuturasAlertas.value = comentario;
		res = confirm('¿Esta seguro de ignorar todas las alertas futuras que se generen de este ciclo? ');
    	if ( !res ) return res;
		document.body.style.cursor = "wait";
                window.document.forma.submit();
		
	}
        
        	
//-->
</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css"></head>
<body leftmargin="0" topmargin="0" onload="muestraGranDiv()">
    
<jsp:include page="header.jsp" flush="true"/>
<table border="0" width="100%">
	<tr>
		<td align="center">
<h3>Alta/Modificaci&oacute;n de Grupo</h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
<br>
<form name="forma" action="controller" method="post">
<input type="hidden" name="command" value=""/>
<input type="hidden" name="idGrupo" value="<%=idGrupo%>"/>
<input type="hidden" name="idCiclo" value=""/>
<input type="hidden" name="nombreGrupo" value="<%=HTMLHelper.displayField(grupo.nombre)%>"/>
<input type="hidden" name="idSolicitud" value="<%=idSolicitud%>"/>
<input type="hidden" name="comentarioIgnorarFuturasAlertas" value=""/>
<input type="hidden" name="otraFinanciera" value=""/>
<input type="hidden" name="campSeguro" value=""/>

<table width="100%" border="0" cellpadding="0">
	<tr>
		<td width="50%" align="right">N&uacute;mero del grupo </td>
		<td width="50%">
			<input type="text" name="idGrupo" size="10" maxlength="10" value="<%=HTMLHelper.displayField(grupo.idGrupo)%>" readonly="readonly" class="soloLectura">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Nombre del grupo </td>
		<td width="50%">
			<input type="text" name="nombre" size="45" maxlength="150" value="<%=HTMLHelper.displayField(grupo.nombre)%>">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Sucursal</td>
		<td width="50%">  
			<select name="idSucursal" size="1">
			<%=HTMLHelper.displayCombo(catSucursales, grupo.sucursal)%>
			</select>
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Fecha de formaci&oacute;n</td>
		<td width="50%">  
			<input type="text" name="fechaFormacion" id="fechaFormacion" size="10" maxlength="10" value="<%=HTMLHelper.displayField(grupo.fechaFormacion)%>" >(dd/mm/aaaa)
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Tipo de Grupo<br></td>
		<td width="50%">  
			<input type="hidden" name="idOperacion"  value="<%=(grupo.idOperacion==0 ? 3:grupo.idOperacion)%>" >
			<b><%=(isRestructuraFlag ? "Restructura":"Grupal")%></b>
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Clasificacion comportamiento<br></td>
		<td width="50%">
			<%if ( isMesaControl && grupo.idOperacion==ClientesConstants.GRUPAL && idGrupo!=0 ) { %> 
				<select name="calificacionGrupal" size="1">
				<%=HTMLHelper.displayCombo(catCalificacionGrupal, grupo.calificacion )%>
				</select>
			<%}else{%>
				<input type="text" name="comportamiento" size="20" maxlength="150" value="<%=HTMLHelper.displayField(grupo.calificacion)%>" readonly="readonly" size="10" class="soloLectura">				
			<%}%> 
		</td>
	</tr>
	<tr>
		<td align="center" colspan="2">
			<br>
			<%if ( !cicloActivo || isMesaControl ) {%><input type="button" id="boton" value="Enviar" onClick="guardaGrupo()" name="boton"><%}%>
			<input type="button" id="boton" value="Regresar" onClick="regresaABusquedaGrupos()">
		</td>
	</tr>
</table><br>
<%if ( grupo!=null && grupo.ciclos!=null && grupo.ciclos.length>0 ){%>
<table border="1">
<tr>
	<td colspan="1" align="center">Ciclo</td>
	<td colspan="1" align="center">Fecha captura</td>
	<td colspan="1" align="center">Liquidado</td>
	<td colspan="1" align="center">Estatus</td>
	<td colspan="1" align="center">Tipo</td>
	<td colspan="1" align="center">Historico de alertas</td>
	<td colspan="1" align="center">Con Seguro</td>
	<% if(adminAlertas) {%>
		<td colspan="1" align="center">Elimina futuras alertas</td>
	<% }%>
</tr>
<%	for ( int i=0 ; i<grupo.ciclos.length ; i++ ){
		if ( grupo.ciclos[i].estatus==ClientesConstants.ESTATUS_CAPTURADO )
			cicloActivo = true;
%>
	<tr>
		<td align="center" id="td"><a href="#" onClick="consultaCicloGrupal(<%=grupo.ciclos[i].idCiclo%>,<%=grupo.ciclos[i].estatusT24%>)"><%=grupo.ciclos[i].idCiclo%></a></td>
		<td align="center"><%=HTMLHelper.displayField(grupo.ciclos[i].fechaCaptura)%></td>
                <td align="center"><%if(grupo.ciclos[i].saldo.getEstatus()==3){%>SI<%}else{%>NO<%}%></td>
                <td align="center"><%=HTMLHelper.getDescripcion(catEstatus, grupo.ciclos[i].estatus)%></td>
                <td align="center"><%=grupo.ciclos[i].idTipoCiclo==2 ? "REFINANCIAMIENTO":grupo.idOperacion==5 ? "RESTRUCTURA":"NATURAL"%></td>
                <td align="center" id="boton"><a href="#" onclick="consultaDetalleMonitor(<%=grupo.ciclos[i].idCiclo%>)">Abrir</a></td>
                <%if(grupo.ciclos[i].seguro==1){
                    seguro = "value='si' checked";
                } else if(grupo.ciclos[i].seguro==0){
                       seguro = "";
                }
                if((grupo.ciclos.length-1)==i){%>
                    <!--<td align="center"><%//=HTMLHelper.displayCheck("conSeguro", seguro, false)%></td>-->
                    <td align="center"><input type="checkbox" name="conSeguro" id="conSeguro" <%=seguro%>/></td>
                <%}else{%>
                    <!--<td align="center"><%//=HTMLHelper.displayCheck("conSeguro", seguro, true)%></td>-->
                    <td align="center"><input type="checkbox" name="conSeguro" id="conSeguro" <%=seguro%> disabled/></td>
		<%}
                if(adminAlertas) {%>
			<td align="center">
			<% if(grupo.ciclos[i].estatus==1 && grupo.ciclos[i].estatusAlertasPago!=2) {%>
				<select name="motivoIgnorarAlertas" id="motivoIgnorarAlertas" size="1" value="0" >
						<%=HTMLHelper.displayCombo(catIgnorarAlertasFuturas, 0)%>
				</select>
				<a href="#" onClick="descartarAlertas(<%=grupo.ciclos[i].idCiclo%>)">Ignorar</a>
			<% }else{%>
				&nbsp;
		<% }%>
			</td>
		<%}%>
	</tr>
<%	}%>
	</table>
<tr>
<%}%>
<%if ( grupo.idGrupo!=0 && !cicloActivo && !grupo.calificacion.equals("B") && !isRestructuraFlag && CatalogoHelper.esSucursalAutorizadaComunal(grupo.sucursal)){%>
	<td align="center" colspan="2" id="td_nuevo">
            <br><input type="button" value="Nuevo ciclo" onClick="capturaCicloGrupal()" name="nuevoCiclo">
	<%if (request.isUserInRole("manager")){ %>
	<br><input type="button" value="Nuevo Ciclo Otra Financiera" onClick="capturaCicloOtraFinanciera()" name="nuevoCicloOtro">
        <%} %>
<%}else if(grupo.calificacion.equals("B") && cicloActivo && grupo.idGrupo!=0  && grupo.ciclos.length>0 && grupo.ciclos[grupo.ciclos.length-1].estatusT24==2 && request.isUserInRole("ORIGINACION_REESTRUCTURA") ){%>
	<br><input type="button" value="Restructura ciclo activo" onClick="capturaCicloRestructura()">
<%}%>
    </td>
</tr>
</form>
		</td>
	</tr>
</table>
<br></body>
</html>
