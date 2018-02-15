<%@page import="com.sicap.clientes.dao.IntegranteCicloDAO"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="com.sicap.clientes.vo.GrupoVO"%>
<%@ page import="com.sicap.clientes.vo.CicloGrupalVO"%>
<%@ page import="com.sicap.clientes.util.ClientesConstants"%>
<%@ page import="com.sicap.clientes.util.ClientesUtil"%>
<%@ page import="java.util.TreeMap"%>
<%@ page import="java.util.Vector"%>
<%@ page import="com.sicap.clientes.util.GrupoUtil"%>
<%@ page import="com.sicap.clientes.vo.DireccionGenericaVO"%>
<%@ page import="com.sicap.clientes.util.FormatUtil"%>
<jsp:directive.page import="com.sicap.clientes.vo.IntegranteCicloVO"/>

<%
Vector notificaciones = (Vector)request.getAttribute("NOTIFICACIONES");
GrupoVO grupo = new GrupoVO();
double total = 0;
double totalConComision = 0;
boolean sinConsulta = false;
IntegranteCicloDAO integranteDao = new IntegranteCicloDAO();
DireccionGenericaVO direccion = new DireccionGenericaVO();
CicloGrupalVO ciclo = new CicloGrupalVO();
CicloGrupalVO cicloAnterior = new CicloGrupalVO();
TreeMap catSucursales = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SUCURSALES);
TreeMap catMontosMaximosCiclo = CatalogoHelper.getCatalogo(ClientesConstants.CAT_MAXIMOS_POR_CICLO);
TreeMap catEstatus = CatalogoHelper.getCatalogoEstatusCiclo();
TreeMap catDiasReunon = CatalogoHelper.getCatalogoDiasReunion();
TreeMap catHorasReunion = CatalogoHelper.getCatalogoHorasReunion();
TreeMap catEjecutivos = new TreeMap();
TreeMap catCoordinadores = catSucursales;//new TreeMap();
TreeMap catRoles = CatalogoHelper.getCatalogoRolesGrupo();//new TreeMap();
TreeMap catTasasGrupal = CatalogoHelper.getCatalogoTasas(ClientesConstants.GRUPAL);
TreeMap catComisionesGrupal = CatalogoHelper.getCatalogoComisiones(ClientesConstants.GRUPAL);
TreeMap catPlazos = CatalogoHelper.getCatalogoPlazos();
TreeMap catBancos = CatalogoHelper.getCatalogoBancosDispersion();
int otraFinanciera = HTMLHelper.getParameterInt(request, "otraFinanciera");
int numIntegrantes = 0;
int tasa = 0;
int plazo=0;
// el campo de seguros completos se deja como 1 para pasar la validacion JBL- SEP/10
String colCalif = "";
String calif = "&nbsp;";

if ( session.getAttribute("GRUPO")!=null ){
	grupo = (GrupoVO)session.getAttribute("GRUPO");
	catEjecutivos = CatalogoHelper.getCatalogoEjecutivos(grupo.sucursal, "A");
	tasa = GrupoUtil.asignaTasaGrupal(grupo, false);
	ciclo.integrantes = (IntegranteCicloVO[])request.getAttribute("INTEGRANTES_NUEVO_CICLO");
	ciclo.estatus = 1;
	ciclo.idTipoCiclo = 1;
	ciclo.idCiclo = (Integer)request.getAttribute("ID_CICLO");
	//if ( ciclo!=null && ciclo.direccionReunion!=null ) direccion=ciclo.direccionReunion;
	
}

cicloAnterior = GrupoUtil.getCiclo(grupo.ciclos, 1);
if ( cicloAnterior!=null && cicloAnterior.direccionReunion!=null ){
	direccion=cicloAnterior.direccionReunion;
	ciclo.asesor = cicloAnterior.asesor;
	ciclo.coordinador = cicloAnterior.coordinador;
	ciclo.diaReunion = cicloAnterior.diaReunion;
	ciclo.direccionReunion = cicloAnterior.direccionReunion;
	ciclo.horaReunion = cicloAnterior.horaReunion;
	ciclo.multaFalta = cicloAnterior.multaFalta;
	ciclo.multaRetraso = cicloAnterior.multaRetraso;
	ciclo.idCiclo = (Integer)request.getAttribute("ID_CICLO");
}

boolean esMesaControl = request.isUserInRole("ANALISIS_CREDITO");
boolean isOtraFinancieraFlag =  (otraFinanciera==ClientesConstants.CICLO_OTRA_FINANCIERA ? true : false );

if (isOtraFinancieraFlag)
	ciclo.idTipoCiclo = otraFinanciera;

%>
<html>
<head>
<title>Alta de Ciclo Grupal</title>
<script language="Javascript" src="./js/functions.js"></script>
<script language="Javascript" src="./js/functionsGrupal.js"></script>
<script type="text/javascript">
<!--
	function guardaCicloGrupal(integrantes){
                disableEdit();
		window.document.forma.command.value = 'guardaRenovacionCicloGrupal';
		window.document.forma.action="controller";
		var noIntegrantes = cuentaChecked(integrantes);
		if ( window.document.forma.plazo.value==0 ){
			alert('Debe indicar el tipo de plzo');
                        enableEdit();
			return false;
		}
                if ( noIntegrantes>40 ){
			alert("El grupo debe contar con un máximo de 40 integrantes");
                        enableEdit();
			return false;
		}
		if ( window.document.forma.idColonia.value==0 ){
			alert("Debe capturar la dirección");
                        enableEdit();
			return false;
		}
		if ( window.document.forma.calle.value=='' ){
			alert("Debe capturar la calle");
                        enableEdit();
			return false;
		}
		if ( window.document.forma.numeroExterior.value=='' ){
			alert("Debe capturar el número exterior");
                        enableEdit();
			return false;
		}
		if ( window.document.forma.estatus.value==0 ){
			alert('Debe indicar el estatus del ciclo');
                        enableEdit();
			return false;
	    }
            if ( window.document.forma.estatus.value>2 ){
                    alert('estatus inválido');
                    enableEdit();
                    return false;
                }
	    if ( window.document.forma.diaReunion.value==0 ){
			alert('Debe indicar el día de reunión');
                        enableEdit();
			return false;
	    }
	    if ( window.document.forma.horaReunion.value==0 ){
			alert('Debe indicar la hora de reunión');
                        enableEdit();
			return false;
	    }
	    if ( window.document.forma.asesor.value==0 ){
			alert('Debe indicar el ejecutivo');
                        enableEdit();
			return false;
	    }
	    if ( window.document.forma.coordinador.value==0 ){
			alert('Debe indicar el coordinador');
                        enableEdit();
			return false;
	    }
	    if ( !esFormatoMoneda(window.document.forma.multaRetraso.value) || window.document.forma.multaRetraso.value<=0 ){
			alert('El monto de la multa por retraso es inválida');
                        enableEdit();
			return false;
	    }
	    if ( !esFormatoMoneda(window.document.forma.multaFalta.value) || window.document.forma.multaFalta.value<=0  ){
			alert('El monto de la multa por falta es inválida');
                        enableEdit();
			return false;
	    }
            if ( window.document.forma.fechaDispersion.value=='' ){
			alert("Debe capturar la fecha de dispersión");
                        enableEdit();
			return false;
            }
            if ( window.document.forma.bancoDispersion.value==0 ){
			alert('Debe indicar el banco para dispersar');
                        enableEdit();
			return false;
            }
	    if( !validaRol(integrantes) ){
			enableEdit();
                        return false;
		}
		if( !minimoIntegrantes(<%=grupo.idOperacion%>, <%=ciclo.idTipoCiclo%>, integrantes, <%=esMesaControl%>) ){
			enableEdit();
                        return false;
		}
		if( !validaMontosMaximos(integrantes) ){
				enableEdit();
                                return false;
		}
		if ( !getCalificacionGrupal(integrantes) ){
			alert('La calificación de Círculo de crédito determina rechazar el crédito');
                        enableEdit();
			return false;
		}
		<%for(int i=0;i<ciclo.integrantes.length; i++){%>
                    if (<%=integranteDao.esSolicitudAsignada(ciclo.integrantes[i].idCliente, ciclo.integrantes[i].idSolicitud, ciclo.integrantes[i].idGrupo)%>&&document.getElementById("desembolso"+<%=i%>).checked==true){
                        alert ("El cliente <%=ciclo.integrantes[i].nombre%> tiene solicitud activa en otro equipo");
                        enableEdit();
                        return false;
                    }
                <%}%>
                
		if ( noIntegrantes>=10 && <%=ciclo.idCiclo%>>1 ){
			var confirmaIncremento;
			switch(<%=ciclo.idCiclo%>){
				case 2:
					if( noIntegrantes<<%=ClientesConstants.INTEGRANTES_MINIMOS_CICLO_2%> ){
						confirmaIncremento = confirm('Incremento en precio por numero de integrantes menor a la politica,  ¿ Desea continuar ?');
					}else{
						confirmaIncremento = true;
					}
				break;
				case 3:
					if( noIntegrantes<<%=ClientesConstants.INTEGRANTES_MINIMOS_CICLO_3%> ){
						confirmaIncremento = confirm('Incremento en precio por numero de integrantes menor a la politica,  ¿ Desea continuar ?');
					}else{
						confirmaIncremento = true;
					}
				break;
				default:
					if( noIntegrantes<<%=ClientesConstants.INTEGRANTES_MINIMOS_CICLO_3%> && <%=ciclo.idCiclo%>>3){
						confirmaIncremento = confirm('Incremento en precio por numero de integrantes menor a la politica,  ¿ Desea continuar ?');
					}else{
						confirmaIncremento = true;
					}
				break;
			
			} 
			if ( !confirmaIncremento ) return confirmaIncremento;
		}
                
	if (!confirm('La información que está apunto de guardar no podrá ser modificada, verifique que se encuentren todos los integrantes del grupo y los montos sean los correctos. ¿Esta seguro de que desea registrar el ciclo con los datos actuales?')){
            enableEdit();
            return false;
        }
        document.body.style.cursor = "wait";
        window.document.forma.submit();
	}

	function regresaAGrupo(){
                document.getElementById("td").disabled = true;
		window.document.forma.command.value = 'consultaDetalleGrupo';
		window.document.forma.action="controller";
                document.body.style.cursor = "wait";
		window.document.forma.submit();
	}

	function consultaCliente(idCliente){
		window.document.forma.command.value='consultaCliente';
		window.document.forma.idCliente.value=idCliente;
                document.body.style.cursor = "wait";
		window.document.forma.submit();
	}
	
	function cuentaChecked(integrantes){
		var numchecked = 0;
		for ( var i=0; i < integrantes ; i++ ){
			var nombreDinamico = "desembolso"+i;
			var elemento = document.getElementById(nombreDinamico);
			if ( elemento.checked == true )
				numchecked++;
		}
		
		return numchecked;
	}

	function abilitaBoton(){
		if( window.document.forma.estatus.value==2 || window.document.forma.estatus.value==3)
			window.document.forma.guardaCiclo.disabled=true;
		else
			window.document.forma.guardaCiclo.disabled=false;
	}
        function disableEdit(){
            <%if (esMesaControl){%>
                document.getElementById("botonGuardaCiclo").disabled = true;
            <%}%>
            document.getElementById("botonRegresaGrupo").disabled = true;
        }
        
        function enableEdit(){
            <%if (esMesaControl){%>
                document.getElementById("botonGuardaCiclo").disabled = false;
            <%}%>
            document.getElementById("botonRegresaGrupo").disabled = false;
        }
        function days(date){
            var day = date.getDay();
            var diaHabilitado = window.document.forma.diaReunion.value;
            return [(day==diaHabilitado),""];
        }
        function resetDate(){
             window.document.forma.fechaDispersion.value='';
        }
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
<input type="hidden" name="idCliente" value="">
<input type="hidden" name="idCiclo" value="<%=ciclo.idCiclo%>">
<input type="hidden" name="idSucursal" value="<%=grupo.sucursal%>">
<input type="hidden" name="numIntegrantes" value="<%=ciclo.integrantes.length%>">
<input type="hidden" name="idColonia" value="<%=HTMLHelper.displayField(direccion.idColonia)%>">
<input type="hidden" name="asentamientoCP" value="<%=HTMLHelper.displayField(direccion.asentamiento_cp)%>">

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
			<select name="estatus" id="estatus" size="1">
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
	<tr>
		<td width="50%" align="right">Tasa</td>
		<td width="50%">  
			<input type="text" name="tasaDescripcion" size="10" maxlength="8" value="<%=CatalogoHelper.getDescripcionTasa(tasa, catTasasGrupal)%>" readonly="readonly" class="soloLectura"> 
			<input type="hidden" name="idTasa" value="<%=tasa%>">
		</td>
	</tr>
        <tr>
		<td width="50%" align="right">Plazo</td>
                <td><select name="plazo" id="plazo" size="1" onKeyPress="return submitenter(this,event)">
                    <%= HTMLHelper.displayCombo(catPlazos, plazo)%>
                    </select>
                </td>
	</tr>
        <tr>
		<td width="50%" align="right">Fecha de Dispersión</td>
                <td><input type="text" name="fechaDispersion" id="fechaDispersion" size="10%" maxlength="10" readonly="readonly">
                </td>
	</tr>
        <tr>
		<td width="50%" align="right">Dispersar por</td>
		<td width="50%">  
			<select name="bancoDispersion" size="1">
			<%=HTMLHelper.displayCombo(catBancos, ciclo.bancoDispersion)%>
			</select>
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
						Monto maximo posible 
					</td>
					<td align="center">
						Monto a desembolsar
					</td>
					<td align="center">
						Monto con comisión
					</td>
					<td align="center">
						Comisión
					</td>
					<td align="center">
						Calificación
					</td>
					<td align="center">
						Rol
					</td>
					<td align="center">
						 
					</td>
				</tr>
<%for ( int i=0 ; ciclo.integrantes!=null && i<ciclo.integrantes.length ; i++ ){%>
				<tr>
					<td align="center"> 
					<%=HTMLHelper.displayCheck("desembolso"+i,true)%>
					</td>
					<td align="center"> 
						<%=i+1%>
					</td>
					<td align="center"> 
						<input type="hidden" name="idCliente<%=i%>" id="idCliente<%=i%>" value="<%=ciclo.integrantes[i].idCliente%>">
						<a href="#" onClick="consultaCliente(<%=ciclo.integrantes[i].idCliente%>)"><%=ciclo.integrantes[i].idCliente%></a>
					</td>
					<td align="center"> 
						<input type="hidden" name="idSolicitud<%=i%>" value="<%=ciclo.integrantes[i].idSolicitud%>">
						<%=ciclo.integrantes[i].idSolicitud%>
					</td>
					<td align="center">
						<input type="hidden" name="nombre<%=i%>" id="nombre<%=i%>" value="<%=ciclo.integrantes[i].nombre%>">
						<%=ciclo.integrantes[i].nombre%>
					</td>
					<td align="right">
                                            <%--<input type="hidden" name="montoMaximo<%=i%>" id="montoMaximo<%=i%>" value="<%=HTMLHelper.formatoMonto( ClientesUtil.calculaMontoMaximoGrupal(ciclo.idTipoCiclo, ciclo.idCiclo, ClientesUtil.calculaMontoSinComision( ciclo.integrantes[i].monto, ciclo.integrantes[i].comision, catComisionesGrupal ), catMontosMaximosCiclo ) )%>">
						<%=HTMLHelper.formatoMonto( ClientesUtil.calculaMontoMaximoGrupal(ciclo.idTipoCiclo, ciclo.idCiclo, ClientesUtil.calculaMontoSinComision( ciclo.integrantes[i].monto, ciclo.integrantes[i].comision, catComisionesGrupal ), catMontosMaximosCiclo ) )%>
                                                --%>
                                                <input type="hidden" name="montoMaximo<%=i%>" id="montoMaximo<%=i%>" value="<%=HTMLHelper.formatoMonto(ClientesUtil.calculaMontoEscalera(ciclo.integrantes[i].idCliente, ciclo.integrantes[i].idSolicitud) )%>">
						<%=HTMLHelper.formatoMonto( ClientesUtil.calculaMontoEscalera(ciclo.integrantes[i].idCliente, ciclo.integrantes[i].idSolicitud ) )%>
                                        </td>
					<td align="right">
						<input type="text" name="monto<%=i%>" id="monto<%=i%>" value="<%=HTMLHelper.formatoMonto( ClientesUtil.calculaMontoSinComision( ciclo.integrantes[i].monto, ciclo.integrantes[i].comision, catComisionesGrupal ) )%>" size="8" maxlength="9">
					</td>
					<td align="right">
						<input type="hidden" name="montoComision<%=i%>" id="montoComision<%=i%>" value="<%=ciclo.integrantes[i].monto%>" >
						<%=HTMLHelper.formatoMonto(ciclo.integrantes[i].monto)%>
					</td>
					<td align="right">
						<input type="hidden" name="comision<%=i%>" value="<%=ciclo.integrantes[i].comision%>">
						<%=CatalogoHelper.getDescripcionComision(ciclo.integrantes[i].comision, catComisionesGrupal) %>
					</td>
					<%if(ciclo.integrantes[i].calificacion == ClientesConstants.CALIFICACION_CIRCULO_BUENA || ciclo.integrantes[i].calificacion == 0){
                                            colCalif = "#33FF33";
                                        } else if(ciclo.integrantes[i].calificacion == ClientesConstants.CALIFICACION_CIRCULO_REGULAR){
                                            colCalif = "#BDBDBA";
                                        } else if(ciclo.integrantes[i].calificacion == ClientesConstants.CALIFICACION_CIRCULO_MALA ){
                                            colCalif = "#FF0000";
                                        } else if ( ciclo.integrantes[i].calificacion == ClientesConstants.CALIFICACION_CIRCULO_NA ){
                                            colCalif = "#33FF99";
                                        } else if(ciclo.integrantes[i].calificacion == ClientesConstants.CALIFICACION_CIRCULO_SIN_CONSULTA ){
                                            sinConsulta=true;
                                            colCalif = "#FFEBCD";
                                            calif = "Sin consulta";
                                        }%>
                                                <input type="hidden" name="calificacion<%=i%>" id="calificacion<%=i%>" value="<%=ciclo.integrantes[i].calificacion%>">
						<td BGCOLOR="<%=colCalif%>"><center><font style="font-style: oblique;"><%=calif%></font></center></td>
					

					<td align="center">
						<select name="rol<%=i%>" id="rol<%=i%>" size="1" value="<%=ciclo.integrantes[i].rol%>" >
						<%=HTMLHelper.displayCombo(catRoles, ciclo.integrantes[i].rol)%>
						</select>
					</td>
                                        <!--<td align="center">
                                        <input type="button" id ="boton" value="Consultar" onClick="">
                                        </td>-->
				</tr>
<%
total += FormatUtil.redondeaMoneda(ClientesUtil.calculaMontoSinComision( ciclo.integrantes[i].monto, ciclo.integrantes[i].comision, catComisionesGrupal ));
totalConComision += ciclo.integrantes[i].monto;
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
					<td align="right">
						<input type="hidden" name="montoTotalConComision" value="<%=totalConComision%>">
						<%=HTMLHelper.formatoMonto(totalConComision) %>
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
	<tr>
		<td align="center" colspan="2" id ="td">
			<%if( !sinConsulta){ %>
				<br><input type="button" id="botonGuardaCiclo" name="guardaCiclo" value="Enviar" onClick="guardaCicloGrupal(<%=numIntegrantes+1%>)">
			<%}%>
                        <input type="button" id ="botonRegresaGrupo" value="Regresar" onClick="regresaAGrupo()">
		</td>
	</tr>
</table>
</form>
		</td>
	</tr>
</table>
</body>
</html>