<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="com.sicap.clientes.vo.GrupoVO"%>
<%@ page import="com.sicap.clientes.vo.CicloGrupalVO"%>
<%@ page import="java.util.Vector"%>
<%@ page import="com.sicap.clientes.util.ClientesConstants"%>
<%@ page import="java.util.TreeMap"%>
<%@ page import="com.sicap.clientes.util.GrupoUtil"%>
<%@ page import="com.sicap.clientes.vo.DireccionGenericaVO"%>


<%
Vector notificaciones = (Vector)request.getAttribute("NOTIFICACIONES");
GrupoVO grupo = new GrupoVO();
double total = 0;
DireccionGenericaVO direccion = new DireccionGenericaVO();
CicloGrupalVO ciclo = new CicloGrupalVO();
TreeMap catSucursales = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SUCURSALES);
TreeMap catEstatus = CatalogoHelper.getCatalogoEstatusCiclo();
TreeMap catDiasReunon = CatalogoHelper.getCatalogoDiasReunion();
TreeMap catHorasReunion = CatalogoHelper.getCatalogoHorasReunion();
TreeMap catEjecutivos = new TreeMap();
TreeMap catCoordinadores = catSucursales;//new TreeMap();
TreeMap catRoles = CatalogoHelper.getCatalogoRolesGrupo();//new TreeMap();
int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
int numIntegrantes = 0;
int segurosCompletos = 0;
int idTasa=2;
int idComision=0;
int plazo=0;
int validaTCP=0;

if ( session.getAttribute("GRUPO")!=null ){
	grupo = (GrupoVO)session.getAttribute("GRUPO");
	if ( idCiclo!=0 ){
		ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
		if ( ciclo!=null && ciclo.direccionReunion!=null ) direccion=ciclo.direccionReunion;
		if ( ciclo!=null && ciclo.integrantes!=null ){
			numIntegrantes = ciclo.integrantes.length;
			 // cuando ya existe el ciclo obtener tasa, plazo y comision			 
			plazo = ciclo.tablaAmortizacion.length-1;
			//idTasa = 1;
			idComision = 1;
			if ( SeguroHelper.cicloSegurosCompletos(ciclo) )
				 segurosCompletos = 1;
		}
	}else if ( request.getAttribute("CICLO")!=null ){
		ciclo = (CicloGrupalVO)request.getAttribute("CICLO");
		if ( ciclo!=null && ciclo.direccionReunion!=null ) direccion=ciclo.direccionReunion;
		if ( ciclo.integrantes!=null )	numIntegrantes = ciclo.integrantes.length;
		if ( ciclo!=null && ciclo.integrantes!=null ){		
			// cuando es nuevo el ciclo trae los datos de tasa, plazo y comision (insertar tasa en BD)
			// si valida TCP es 1, no dejar pasar a la tabla de amortizacion, si es 0 todo correcto 
			validaTCP = GrupoHelper.validaTasaComisionPlazo(ciclo.integrantes);
			//if(validaTCP==0){
			if( ciclo.integrantes!=null && ciclo.integrantes.length>0 ){
				plazo = ciclo.integrantes[0].plazo;
				if( plazo ==0 ) 
					plazo = ciclo.plazo;
				//idTasa =  1;
				idComision = 1;
			}else{
				plazo = ciclo.plazo;
				//idTasa = 1;
				idComision = 1;
			}
			if ( SeguroHelper.cicloSegurosCompletos(ciclo) )
				segurosCompletos = 1;
		}
	}
	ciclo.tasa = 2;
	ciclo.comision = 1;
	if( ciclo.tablaAmortizacion!=null && ciclo.tablaAmortizacion.length>0 ){
		String resp = GeneraContratoHelper.makeContract(grupo, ciclo);
		int index = resp.indexOf("<center><b>PAGARÉ COLECTIVO CRÉDITO FIRME</b></center>");
		String temp = "<body>" + resp.substring(0,index) + "</body>";
		session.setAttribute("CONTRATO",temp);
		temp = "<body>" + resp.substring(index) + "</body>";
		session.setAttribute("PAGARE",temp);
		String pagareIndiv = GeneraContratoHelper.makePagareIndividual(grupo, ciclo, null);
		session.setAttribute("PAGAREINDIVIDUAL", pagareIndiv);
	}
	catEjecutivos = CatalogoHelper.getCatalogoEjecutivos(grupo.sucursal);
}
boolean cicloNuevo = ( request.getAttribute("CICLO_NUEVO")!=null  ? ((Integer)request.getAttribute("CICLO_NUEVO")==0) : false );
boolean desembolsadoFlag = GrupoHelper.isDesembolsado(ciclo.integrantes);
String desembolsoOk = "NO";
if ( request.getAttribute("VALIDACION")!=null )
	desembolsoOk = (String)request.getAttribute("VALIDACION");
%>
<html>
<head>
<title>Alta de Ciclo Grupal</title>
<script language="Javascript" src="./js/functions.js"></script>
<script language="Javascript" src="./js/functionsGrupal.js"></script>
<script type="text/javascript">
<!--
	function guardaCicloGrupal(integrantes){
		window.document.forma.command.value = 'guardaCicloRestructura';
		window.document.forma.action="controller";
		if( !minimoIntegrantes(<%=grupo.idOperacion%>, <%=ciclo.idTipoCiclo%>, integrantes, false) ){
			return false;
		}
		if ( integrantes>40 ){
			alert("El grupo debe contar con un máximo de 40 integrantes");
			return false;
		}
		if( <%=validaTCP%>==1 ){
		     alert("El ciclo no puede ser generado, es necesario que todos los integrantes tengan asignada la misma tasa, comisión y plazo");
			 return false;
		}
		if ( window.document.forma.idColonia.value==0 ){
			alert("Debe capturar la dirección");
			return false;
		}
		if ( window.document.forma.calle.value=='' ){
			alert("Debe capturar la calle");
			return false;
		}
		if ( window.document.forma.numeroExterior.value=='' ){
			alert("Debe capturar el número exterior");
			return false;
		}
		if ( window.document.forma.estatus.value==0 ){
			alert('Debe indicar el estatus del ciclo');
			return false;
	    }
	    if ( window.document.forma.diaReunion.value==0 ){
			alert('Debe indicar el día de reunión');
			return false;
	    }
	    if ( window.document.forma.horaReunion.value==0 ){
			alert('Debe indicar la hora de reunión');
			return false;
	    }
	    if ( window.document.forma.asesor.value==0 ){
			alert('Debe indicar el ejecutivo');
			return false;
	    }
	    if ( window.document.forma.coordinador.value==0 ){
			alert('Debe indicar el coordinador');
			return false;
	    }
	    if ( !esFormatoMoneda(window.document.forma.multaRetraso.value) || window.document.forma.multaRetraso.value<=0 ){
			alert('El monto de la multa por retraso es inválida');
			return false;
	    }
	    if ( !esFormatoMoneda(window.document.forma.multaFalta.value) || window.document.forma.multaFalta.value<=0  ){
			alert('El monto de la multa por falta es inválida');
			return false;
	    }
	    if ( window.document.forma.plazo.value==0 || esPlazoValido(window.document.forma.plazo.value, <%=ClientesConstants.REESTRUCTURA_GRUPAL%>) ){
			alert('El plazo para la restructura es invalido, ingreselo nuevamente.');
			return false;
	    }

	    res = confirm('La información que está apunto de guardar no podrá ser modificada, verifique que se encuentren todos los integrantes del grupo y los montos sean los correctos. ¿Esta seguro de que desea registrar el ciclo con los datos actuales?');
    	if ( !res ) return res;
		window.document.forma.submit();
	}

	function desembolsaCicloRestructura(integrantes){
		window.document.forma.command.value = 'desembolsaCicloRestructura';
		if( !minimoIntegrantes(<%=grupo.idOperacion%>, <%=ciclo.idTipoCiclo%>, integrantes, false) ){
			return false;
		}
		window.document.forma.submit();
	}

	function regresaAGrupo(){
		window.document.forma.command.value = 'consultaDetalleGrupo';
		window.document.forma.action="controller";
		window.document.forma.submit();
	}
	
	function consultaPagosComunales(){
		window.document.forma.command.value='validarPagoComunal';
		window.document.forma.submit();
	}

	function muestraDocumento(tipo){
		window.document.forma.action="muestraDocumento.jsp";
		//window.document.forma.target="_blank";
		window.document.forma.tipo.value=tipo;
		window.document.forma.submit();
	}
	
	function consultaEstadoCuentaGrupal(){
		window.document.forma.command.value="consultaEstadoCuentaGrupal";
		window.document.forma.submit();
	}
	
	function consultaCliente(idCliente){
		window.document.forma.command.value='consultaCliente';
		window.document.forma.idCliente.value=idCliente;
		window.document.forma.submit();
	}
	
	<%if( desembolsadoFlag ){%>
		function abilitaCierreCiclo(){
			if( window.document.forma.estatus.value==2 )
				window.document.forma.guardaCiclo.disabled=false;
			else
				window.document.forma.guardaCiclo.disabled=true;
		}
	<%}%>

//-->
</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>
<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<table border="0" width="100%">
	<tr>
		<td align="center">
<h3>Alta de Ciclo Grupal</h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
<br>
<form name="forma" action="controller" method="post">
<input type="hidden" name="command" value="guardaCicloGrupal">
<input type="hidden" name="idGrupo" value="<%=grupo.idGrupo%>">

<input type="hidden" name="idSucursal" value="<%=grupo.sucursal%>">
<input type="hidden" name="tipo" value="">
<input type="hidden" name="idCliente" value="">
<input type="hidden" name="numIntegrantes" <%if(numIntegrantes==0 && ciclo.integrantes != null ){%>value="<%=ciclo.integrantes.length%>"<%}else{%>value="<%=numIntegrantes%>"<%}%> >
<input type="hidden" name="idColonia" value="<%=HTMLHelper.displayField(direccion.idColonia)%>">
<input type="hidden" name="asentamientoCP" value="<%=HTMLHelper.displayField(direccion.asentamiento_cp)%>">
<input type="hidden" name="segurosCompletos" value="<%=segurosCompletos%>">
<input type="hidden" name="desembolsado" value="<%=desembolsadoFlag%>">
<input type="hidden" name="idComision" value="<%=idComision%>">
<input type="hidden" name="idTasa" value="<%=idTasa%>">


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
			<input type="text" name="nombre" size="45" maxlength="150" value="<%=HTMLHelper.displayField(grupo.nombre)%>" readonly="readonly" class="soloLectura">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Número de ciclo</td>
		<td width="50%">
			<input type="text" name="idCiclo" size="10" maxlength="10" value="<% if ( ciclo.idCiclo!=0 ) out.print(HTMLHelper.displayField(ciclo.idCiclo)); else out.print(HTMLHelper.displayField(idCiclo)); %>" readonly="readonly" class="soloLectura">
		</td>
	</tr>
	<tr>
		<td width="100%" align="center" colspan="2"><a href="#" onClick="ayudaCodigoPostal()">Ayuda CP</a></td>
	</tr>
	<tr>
		<td width="50%" align="right">Estado</td>
		<td width="50%">  
		<input type="text" name="estado" size="40" maxlength="100" value="<%=HTMLHelper.displayField(direccion.estado)%>" readonly="readonly" class="soloLectura">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Municipio</td>
		<td width="50%">  
		<input type="text" name="municipio" size="40" maxlength="100" value="<%=HTMLHelper.displayField(direccion.municipio)%>" readonly="readonly" class="soloLectura">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Colonia</td>
		<td width="50%">  
		<input type="text" name="colonia" size="40" maxlength="100" value="<%=HTMLHelper.displayField(direccion.colonia)%>" readonly="readonly" class="soloLectura">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">C&oacute;digo postal</td>
		<td width="50%">  
		<input type="text" name="cp" size="6" maxlength="6" value="<%=HTMLHelper.displayField(direccion.cp)%>" readonly="readonly" class="soloLectura">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Calle</td>
		<td width="50%">  
		<input type="text" name="calle" size="30" maxlength="60" value="<%=HTMLHelper.displayField(direccion.calle)%>">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">N&uacute;mero exterior</td>
		<td width="50%">  
		<input type="text" name="numeroExterior" size="10" maxlength="10" value="<%=HTMLHelper.displayField(direccion.numeroExterior)%>">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">N&uacute;mero interior</td>
		<td width="50%">  
		<input type="text" name="numeroInterior" size="10" maxlength="10" value="<%=HTMLHelper.displayField(direccion.numeroInterior)%>">
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Estatus</td>
		<td width="50%">  
			<select name="estatus"  size="1" <%if(desembolsadoFlag){%>onchange="abilitaCierreCiclo()"<%}%>>
			<%=HTMLHelper.displayCombo(catEstatus, ciclo.estatus)%>
			</select>
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">D&iacute;a de reuni&oacute;n del grupo</td>
		<td width="50%">  
			<select name="diaReunion" size="1">
			<%=HTMLHelper.displayCombo(catDiasReunon, ciclo.diaReunion)%>
			</select>
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Hora de reuni&oacute;n del grupo</td>
		<td width="50%">  
			<select name="horaReunion" size="1">
			<%=HTMLHelper.displayCombo(catHorasReunion, ciclo.horaReunion)%>
			</select>
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Ejecutivo</td>
		<td width="50%">  
			<select name="asesor" size="1">
			<option value="0">Seleccione...</option>
			<%=HTMLHelper.displayCombo(catEjecutivos, ciclo.asesor)%>
			</select>
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Coordinador</td>
		<td width="50%">  
			<select name="coordinador" size="1">
			<%=HTMLHelper.displayCombo(catCoordinadores, ciclo.coordinador)%>
			</select>
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Multa por retraso</td>
		<td width="50%">  
			<input type="text" name="multaRetraso" size="10" maxlength="8" value="<%=HTMLHelper.formatoMonto(ciclo.multaRetraso)%>" >
		</td>
	</tr>
	<tr>
		<td width="50%" align="right">Multa por falta</td>
		<td width="50%">  
			<input type="text" name="multaFalta" size="10" maxlength="8" value="<%=HTMLHelper.formatoMonto(ciclo.multaFalta)%>" > 
		</td>
	</tr>
<%if ( request.isUserInRole("DESEMBOLSO_REESTRUCTURA") ){ %>
	<tr>
		<td width="50%" align="right">Fecha valor</td>
		<td width="50%">  
			<input type="text" name="fechaValor" size="10" maxlength="10" value="<%=HTMLHelper.displayField(ciclo.fechaValor)%>" >(dd/mm/aaaa)
		</td>
	</tr>
<%} %>
	<tr>
		<td width="50%" align="right">Plazo</td>
		<td width="50%">  
			<input type="text" name="plazo" size="10" maxlength="8" value="<%=plazo%>" <%if(desembolsadoFlag){%>readonly="readonly"<%}%> > Semanas 
		</td>
	</tr>
	<tr>
		<td colspan="2" align="center"><br>
			<table border="1" width="90%">
				<tr>
					<td align="center">  
						
					</td>
					<td align="center">
						No.
					</td>
					<td align="center"> 
						Cliente
					</td>
					<td align="center"> 
						Solicitud
					</td>
					<td align="center">
						Nombre
					</td>
					<td align="center">
						Monto
					</td>				
					<td align="center">
						Rol
					</td>
				</tr>
<%for ( int i=0 ; ciclo.integrantes!=null && i<ciclo.integrantes.length ; i++ ){ %>
				<tr>
					<td align="center"> 
					<%=HTMLHelper.displayCheck("desembolso"+i,(cicloNuevo ? ciclo.integrantes[i].idSolicitud==0 : true))%>
					</td>
					<td align="center"> 
						<%=i+1%>
					</td>
					<td align="center"> 
						<input type="hidden" name="idCliente<%=i%>" value="<%=ciclo.integrantes[i].idCliente%>">
						<a href="#" onClick="consultaCliente(<%=ciclo.integrantes[i].idCliente%>)"><%=ciclo.integrantes[i].idCliente%></a>
					</td>
					<td align="center"> 
						<input type="hidden" name="idSolicitud<%=i%>" value="<%=ciclo.integrantes[i].idSolicitud%>">
						<%=ciclo.integrantes[i].idSolicitud%>
					</td>
					<td align="center" <%if(ciclo.integrantes[i].idSolicitud!=0 && cicloNuevo){%>class="soloLecturaRojo"<%}%>>
						<%ciclo.integrantes[i].idSolicitud=0;%>
						<input type="hidden" name="nombre<%=i%>" value="<%=ciclo.integrantes[i].nombre%>">
						<%=ciclo.integrantes[i].nombre%>
					</td>
					<%if(cicloNuevo){ %>
						<td align="right">
							<input type="text" name="monto<%=i%>" value="<%=HTMLHelper.formatoMonto( ciclo.integrantes[i].monto )%>" size="8" maxlength="9">
						</td>
					<%}else{%>
						<td align="right">
							<input type="hidden" name="monto<%=i%>" value="<%=ciclo.integrantes[i].monto%>">
							<%=HTMLHelper.formatoMonto( ciclo.integrantes[i].monto )%>
						</td>
					<%}%>
					<input type="hidden" name="comision<%=i%>" value="<%=ciclo.integrantes[i].comision%>">
					<td align="center">
						<select name="rol<%=i%>" size="1" value="<%=ciclo.integrantes[i].rol%>" >
						<%=HTMLHelper.displayCombo(catRoles, ciclo.integrantes[i].rol)%>
						</select>
					</td>
				</tr>
<%
total += ciclo.integrantes[i].monto;
numIntegrantes = i;
}%>
				<tr>
					<td colspan="5" align="center">  
						Total 
					</td>
					<td align="right">
						<input type="hidden" name="montoTotal" value="<%=total%>">
						<%=HTMLHelper.formatoMonto(total) %>
					</td>
					<td>
						&nbsp;
					</td>
					<% if(grupo.idOperacion==ClientesConstants.GRUPAL){%>
					<td>
						&nbsp;
					</td>
					<%}%>
				</tr>
			</table>
		</td>
	</tr>
<%if ( GrupoHelper.isDesembolsado(ciclo.integrantes) ){%>
	<tr>
		<td align="center" colspan="2">
			<a href="#" onClick="muestraDocumento('CONTRATO')">Consulta contrato</a>&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="#" onClick="muestraDocumento('PAGARE')">Consulta pagaré grupal</a>&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="#" onClick="muestraDocumento('PAGAREINDIVIDUAL')">Consulta pagaré individual</a>&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="generaListaAsistenciaGrupal.jsp?idCiclo=<%=idCiclo%>">Lista de asistencia</a>&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="generaControlPagosAhorros.jsp?idCiclo=<%=idCiclo%>">Control de pagos</a>&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="generaFichasPagos.jsp?idOperacion=3&idCiclo=<%=idCiclo%>">Fichas de pago</a>&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="#" onClick="consultaEstadoCuentaGrupal()">Estado de cuenta</a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="#" onClick="consultaPagosComunales()"> Consulta pagos comunales</a>&nbsp;&nbsp;&nbsp;&nbsp;
			<a href="#" onClick="muestraDocumento('HOJARESUMEN')">Hoja resumen</a>
		</td>
	</tr>
<%} %>
	<tr>
		<td align="center" colspan="2">
			<br><input type="button" name="guardaCiclo" value="Enviar" onClick="guardaCicloGrupal(<%=numIntegrantes+1%>)" <%if(desembolsadoFlag){%>disabled<%}%> >
			<input type="button" value="Regresar" onClick="regresaAGrupo()">				
		</td>
	</tr>
	<tr>
		<td align="center" colspan="2">
			<%if ( ciclo.tablaAmortizacion!=null && ciclo.tablaAmortizacion.length>0 && !GrupoHelper.isDesembolsado(ciclo.integrantes) && desembolsoOk.equals("OK") && request.isUserInRole("DESEMBOLSO_REESTRUCTURA")){%>
				<br><input type="button" value="Desembolsar" onClick="desembolsaCicloRestructura(<%=numIntegrantes+1%>)">
			<%}else{ %>
				<br><input disabled type="button" value="Desembolsar" onClick="desembolsaCicloRestructura(<%=numIntegrantes+1%>)">
			<%} %>
		</td>
	</tr>
	<tr>
		<td align="center" colspan="2">
			<br><INPUT TYPE="button" value="Imprimir" onClick="window.print()">
		</td>
	</tr>
</table>
</form>
		</td>
	</tr>
</table>
</body>
</html>
