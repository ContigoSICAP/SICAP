<%@page import="com.sicap.clientes.dao.SucursalDAO"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="com.sicap.clientes.vo.UsuarioVO"%>
<%@ page import="com.sicap.clientes.vo.GrupoVO"%>
<%@ page import="java.util.*"%>
<%
Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
int idSucursal = HTMLHelper.getParameterInt(request, "idSucursal");
UsuarioVO usuario = (UsuarioVO)session.getAttribute("USUARIO");
TreeMap catSucursales = CatalogoHelper.getCatalogoSucursal(usuario.sucursales);
//TreeMap catSucursales = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SUCURSALES);
boolean consultaDetallada = false;
GrupoVO[] grupos = null;
if ( request.getAttribute("GRUPOS")!=null )
	grupos = (GrupoVO[])request.getAttribute("GRUPOS");
	
if ( request.getAttribute("DETALLADA")!=null && request.getAttribute("DETALLADA").equals("OK") )
	consultaDetallada = true;

%>
<html>
<head>
<title>Consulta grupos</title>
<script language="Javascript" src="./js/functions.js"></script>

<script type="text/javascript">
<!--
	function buscaGrupos(){
            document.getElementById("tr").disabled = true;
            	window.document.forma.command.value = 'buscaGrupos';
		if( window.document.forma.idGrupo.value!='' && !esEntero(window.document.forma.idGrupo.value) ){
			alert('No es numero de grupo valido');
                        document.getElementById("tr").disabled = false;
			return false;
		}
                document.body.style.cursor = "wait";
                window.document.forma.submit();
                
        }

	function camposInteligentes(caso){
		switch (caso){
			case 1:
				if(window.document.forma.idSucursal.value != 0){
					window.document.forma.nombreGrupo.disabled=true;
					window.document.forma.idGrupo.disabled=true;
				}else{
					window.document.forma.nombreGrupo.disabled=false;
					window.document.forma.idGrupo.disabled=false;
				}
			break
			case 2:
				if(window.document.forma.nombreGrupo.value != ''){
					window.document.forma.idSucursal.disabled=true;
					window.document.forma.idGrupo.disabled=true;
				}else{
					window.document.forma.idGrupo.disabled=false;
					window.document.forma.idSucursal.disabled=false;
				}
			break
			case 3:
				if(window.document.forma.idGrupo.value != ''){
					window.document.forma.nombreGrupo.disabled=true;
					window.document.forma.idSucursal.disabled=true;
				}else{
					window.document.forma.nombreGrupo.disabled=false;
					window.document.forma.idSucursal.disabled=false;
				}
			break
		}
	}

	function capturaGrupo(){
                document.getElementById("tr").disabled = true;
                <%if (CatalogoHelper.esSucursaldeIxaya(usuario.idSucursal)) {%>
                    alert('El movimiento se debe de realizar en el sistema JUCAVI');
                    return false;
                <%}%>
		window.document.forma.command.value = 'capturaGrupo';
		window.document.forma.idGrupo.value = 0;
		document.body.style.cursor = "wait";
                window.document.forma.submit();
	}
        
	function consultaDetalleGrupo(idGrupo){
                document.getElementById("td").disabled = true;
                window.document.forma.command.value = 'consultaDetalleGrupo';
		window.document.forma.idGrupo.value = idGrupo;
		document.body.style.cursor = "wait";
                window.document.forma.submit();
                
                
	}

	function consultaDetalleMonitor(idGrupo, idCiclo, nombre){
                document.getElementById("td").disabled = true;
		window.document.forma.command.value = 'consultaDetalleMonitor';
		window.document.forma.idGrupoDetallada.value = idGrupo;
		window.document.forma.idCiclo.value = idCiclo;
		window.document.forma.nombre.value = nombre;
		document.body.style.cursor = "wait";
                window.document.forma.submit();
	}
        
        function consultaCicloEstatus(){
            window.document.forma.command.value='consultaCicloEstatus';
            window.document.forma.submit();
        }
        function consultaInterCicloEstatus(){
            window.document.forma.command.value='consultaInterCicloEstatus';
            window.document.forma.submit();
            
        }
        
              
//-->
</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>
<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<jsp:include page="menuInicio.jsp" flush="true"/>
<form name="forma" action="controller" method="post">
<input type="hidden" name="command" value="buscaGrupos">
<input type="hidden" name="idGrupoDetallada" value="">
<input type="hidden" name="idCiclo" value="">
<input type="hidden" name=nombre value="">
<table border="0" width="100%" id="tabla">
	<tr>
		<td valign="top">
			
		</td>
		<td align="center">
<h3>Grupos</h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
<br>
<table width="60%" border="0" cellpadding="0">
	<tr>
		<td width="50%" align="right">Sucursal</td>
		<td width="50%">  
			<select name="idSucursal" size="1" onChange="camposInteligentes(1)">
			<%=HTMLHelper.displayCombo(catSucursales, idSucursal)%>
			</select>
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Nombre del grupo</td>
		<td width="50%">  
			<input type="text" name="nombreGrupo" size="53" value="" onChange="camposInteligentes(2)">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Numero del grupo</td>
		<td width="50%">  
			<input type="text" name="idGrupo" size="10" value="" onChange="camposInteligentes(3)">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Informe de alertas</td>
		<td width="50%">  
			<%=HTMLHelper.displayCheck("consultaDetallada", false)%>
		</td>
	</tr>
	<tr id="tr">
		<td align="right">
			<br><input type="button" value="Buscar" onClick="buscaGrupos()">
		</td>
		<td align="left">
			<br><input type="button" value="Nuevo grupo" onClick="capturaGrupo()">
		</td>
	</tr>
</table><br>
<%if ( grupos!=null ){ %>
<table <%if ( consultaDetallada ){ %>width="70%"<%}else{%>width="50%"<%}%> border="1">
	<tr>
		<%if ( consultaDetallada ){ %>
			<td width="8%" align="center"><b>N&uacute;mero</b></td>
			<td width="20%" align="center"><b>Nombre del grupo</b></td>
                        <td width="30%" align="center"><b>Sucursal</b></td>
			<td width="10%" align="center"><b>Comportamiento</b></td>
			<td width="8%" align="center"><b>Ciclo activo</b></td>
			<td width="8%" align="center"><b>No. de pago</b></td>
			<td width="8%" align="center"><b>No. atrasos</b></td>
			<td width="8%" align="center"><b>En Mora</b></td>
			<td width="5%" align="center"><b>Cobrador</b></td>
			<td width="5%" align="center"><b>Gerente</b></td>
			<td width="5%" align="center"><b>Gestor</b></td>
			<td width="5%" align="center"><b>Acci&oacute;n</b></td>
		<%}else{%>
			<td width="10%" align="center"><b>N&uacute;mero</b></td>
			<td width="30%" align="center"><b>Nombre del grupo</b></td>
                        <td width="30%" align="center"><b>Sucursal</b></td>
			<td width="10%" align="center"><b>Comportamiento</b></td>
			<td width="5%" align="center"><b>Accion</b></td>
		<%}%>
	</tr>
<% 
	for ( int i=0 ; i<grupos.length ; i++ ){
            SucursalDAO d = new SucursalDAO();
            
%>
	<tr>
		<td align="center"><%=grupos[i].idGrupo%></td>
		<td align="center"><%=grupos[i].nombre%></td>
                <td align="center"><%= d.getSucursalNombre(grupos[i].sucursal)  %></td>
		<td align="center"><%=grupos[i].calificacion%></td>
		<%if ( !consultaDetallada ){ %>
                <td align="center" id ="td"><a href="#" onclick="consultaDetalleGrupo(<%=grupos[i].idGrupo%>)">Modificar</span></a></td>
		<%}
		if ( consultaDetallada ){ %>
			<%if ( grupos[i].monitorPagos.numCiclo != 0 ){ %>
				<td align="center"><%=grupos[i].monitorPagos.numCiclo%></td>
				<td align="center"><%=grupos[i].monitorPagos.numPago%></td>
				<td align="center"><%=grupos[i].monitorPagos.numAtrasos%></td>
				<td align="center"><%=grupos[i].monitorPagos.enMora%></td>
				<td align="center" <%if(grupos[i].monitorPagos.estatusVisitaSupervisor==1){%> BGCOLOR="#FF0000" id="td"><a href="#" onclick="consultaDetalleMonitor(<%=grupos[i].idGrupo%>, <%=grupos[i].monitorPagos.numCiclo%>, '<%=grupos[i].nombre%>')">C</a><%}else{%> >&nbsp;<%}%></td>
				<td align="center" <%if(grupos[i].monitorPagos.estatusVisitaGerente==1){%> BGCOLOR="#FF0000" id="td"><a href="#" onclick="consultaDetalleMonitor(<%=grupos[i].idGrupo%>, <%=grupos[i].monitorPagos.numCiclo%>, '<%=grupos[i].nombre%>')">G</a><%}else{%> >&nbsp;<%}%></td>
				<td align="center" <%if(grupos[i].monitorPagos.estatusVisitaGestor==1){%> BGCOLOR="#FF0000" id="td"><a href="#" onclick="consultaDetalleMonitor(<%=grupos[i].idGrupo%>, <%=grupos[i].monitorPagos.numCiclo%>, '<%=grupos[i].nombre%>')">T</a><%}else{%> >&nbsp;<%}%></td>
				<td align="center"id ="td"><a href="#" onclick="consultaDetalleGrupo(<%=grupos[i].idGrupo%>)">Modificar</a></td>
			<%}else{%>
				<td colspan="1" align="center">&nbsp;</td>
				<td colspan="1" align="center">&nbsp;</td>
				<td colspan="1" align="center">&nbsp;</td>
				<td colspan="1" align="center">&nbsp;</td>
				<td colspan="3" align="center">Sin visitas</td>
				<td align="center"id ="td"><a href="#" onclick="consultaDetalleGrupo(<%=grupos[i].idGrupo%>)">Modificar</a></td>
			<%}%>
		<%}%>
	</tr>
<%}%>        
</table>
<%} %>

		</td>
	</tr>
        <tr>
            <td align="center" colspan="2"><a href="#" onClick="consultaCicloEstatus()">Consulta de Equipos por Estatus</a></td>            
        </tr>
</table>
</form>
<jsp:include page="footer.jsp" flush="true"/></body>
</html>
