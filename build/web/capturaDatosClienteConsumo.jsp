<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.*"%>
<html>
<head>
<title>Alta/Modificaci&oacute;n de cliente</title>
<script language="Javascript" src="./js/functions.js"></script>
<script>

	function guardaDatosCliente(tipoOper, esExterno){
	
		window.document.forma.command.value='guardaDatosClienteConsumo';
		if ( window.document.forma.ejecutivo.value==0 && !esExterno){
			alert('Debe seleccionar un ejecutivo');
			return false;		
		}
		
		if(tipoOper==21){
			if ( window.document.forma.numrepresentante.value==0 ){
			alert('Debe seleccionar un representante');
			return false;		
		}
		}
		if ( window.document.forma.medio.value==0 ){
			alert('Debe seleccionar como se enter\u00f3 del servicio');
			return false;
		}
		if ( window.document.forma.nombre.value=='' || !esTexto( "^[a-zA-Z áéíóúAÉÍÓÚÑñ]+$",window.document.forma.nombre.value)){
		   alert('Debe introducir un Nombre v\u00e1lido');
		   return false;
		}
		if ( window.document.forma.aPaterno.value=='' || !esTexto( "^[a-zA-Z áéíóúAÉÍÓÚÑñ]+$",window.document.forma.aPaterno.value) || window.document.forma.aPaterno.value.toUpperCase()=='NO PROPORCIONADO'){
		   alert('Debe introducir un Apellido Paterno v\u00e1lido');
		   return false;
		}
		if ( window.document.forma.aMaterno.value=='' || !esTexto( "^[a-zA-Z áéíóúAÉÍÓÚÑñ]+$",window.document.forma.aMaterno.value)){
		   alert('Debe introducir un Apellido Materno v\u00e1lido');
		   return false;
		}
		if ( window.document.forma.fechaNacimiento.value!='' ){
			if ( !esFechaValida(window.document.forma.fechaNacimiento,false) ){
				alert("La Fecha de nacimiento es inv\u00e1lida");
				return false;
			}else if ( !esEdadValida(window.document.forma.fechaNacimiento.value,tipoOper) ){
				alert("La edad del cliente está fuera del rango permitido");
				return false;
			}
		}else{
			alert("Debe capturar la Fecha de nacimiento");
			return false;
		}
		if ( window.document.forma.sexo.value==0 ){
			alert('Debe seleccionar sexo');
			return false;
		}
		if ( window.document.forma.dependientesEconomicos.value==0 ){
			alert('El número de dependientes económicos no es inválido');
			return false;
		}
		if ( window.document.forma.tipoIdentificacion.value==0 ){
			alert('Debe seleccionar tipo de identificaci\u00f3n');
			return false;
		    }
		if ( window.document.forma.numeroIdentificacion.value==''){
		   alert('Debe introducir un Número de Indentificaci\u00f3n v\u00e1lido');
		   return false;
		}else{
			if ( window.document.forma.tipoIdentificacion.value==1 && !esClaveElector(window.document.forma.numeroIdentificacion.value) ){
				alert('El número de identificación es inválido, para credencial de IFE debe usar la clave de elector');
			   return false;
			}
		}
		if ( window.document.forma.estadoCivil.value==0 ){
			alert('Debe seleccionar Estado Civil');
			return false;
		}
		if ( window.document.forma.idColonia.value==0 ){
			alert("Debe capturar la direcci\u00f3n");
			return false;
		}
		if ( window.document.forma.calle.value==''){
		   alert('Debe introducir una Calle');
		   return false;
		}
		if ( window.document.forma.numeroExterior.value=='' || !esEntero(window.document.forma.numeroExterior.value)){
		   alert('Debe introducir un N\u00famero exterior de Domicilio');
		   return false;
		}
		if ( window.document.forma.antiguedadDomicilio.value=='' || !esMesesAnios(window.document.forma.antiguedadDomicilio.value)){
		   alert('La Antigüedad en el Domicilio es inv\u00e1lida');
		   return false;
		}
		if ( window.document.forma.telefonoPrincipal.value=='' || !esTelefonoValido(window.document.forma.telefonoPrincipal.value) ){
		   alert('Debe introducir un Teléfono domicilio o recados (incluir clave LADA)');
		   return false;
		}
		if ( window.document.forma.situacionVivienda.value==0 ){
			alert('Debe indicar la Situaci\u00f3n de la Vivienda');
			return false;
		}
		if ( window.document.forma.nivelEstudios.value==0 ){
			alert('Debe indicar el Nivel de estudios');
			return false;
		}
		if ( tipoOper == 4 ){
/*
			if (window.document.forma.curp.value==''){
				alert('Debe ingresar la CURP');
				return false;
			}
*/
			if ( window.document.forma.localidad.value=='' ){
				alert('Debe ingresar una localidad');
				return false;
			}			
		}
				
	    window.document.forma.submit();
	}

	function capturaGrupo(){
		params ="?command=capturaGrupo";
		url = "/CEC/controller";
		abreVentana(url + params,'scrollbars=no',750 ,410 ,true, 0, 0);
	}



</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>
<%
	UsuarioVO usuario = (UsuarioVO)session.getAttribute("USUARIO");
	ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
	ClienteVO clienteAyuda = (ClienteVO)session.getAttribute("ayudarfc");	
	TreeMap catMedios = CatalogoHelper.getCatalogo(ClientesConstants.CAT_MEDIOS);
	TreeMap catSexo = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SEXO);
	TreeMap catEstadoCivil = CatalogoHelper.getCatalogo(ClientesConstants.CAT_ESTADO_CIVIL);
	TreeMap catSituacionVivienda = null;
	TreeMap catEjecutivosCredito = CatalogoHelper.getCatalogoEjecutivos(cliente.idSucursal);
	TreeMap catDepEconomicos = CatalogoHelper.getCatalogo(ClientesConstants.CAT_DEP_ECONOMICOS);
	TreeMap catNivelesEstudio = CatalogoHelper.getCatalogo(ClientesConstants.CAT_NIVELES_ESTUDIO);
	TreeMap catRepresentantes = CatalogoHelper.getCatalogoRepresentantes(cliente.idSucursal);
	TreeMap catTiposIdentificacion = CatalogoHelper.getCatalogo(ClientesConstants.CAT_TIPO_IDENTIFICACION);
	boolean esReestructura = false;
	
	Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
	int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
	if ( idSolicitud==0 )
		idSolicitud = ((Integer)request.getAttribute("ID_SOLICITUD")).intValue();
	int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
	SolicitudVO solicitud = new SolicitudVO();
	DireccionVO direccion = new DireccionVO();

	if ( cliente.solicitudes!=null && cliente.solicitudes.length>0 ){
		solicitud = cliente.solicitudes[indiceSolicitud];
		   if(solicitud.reestructura==1)
		      esReestructura = true;
		
		if ( solicitud.tipoOperacion==ClientesConstants.CONSUMO || solicitud.tipoOperacion==ClientesConstants.VIVIENDA || solicitud.tipoOperacion==ClientesConstants.CREDIHOGAR || solicitud.tipoOperacion==ClientesConstants.MAX_ZAPATOS){
			catSituacionVivienda = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SITUACION_VIVIENDA, 5);
		}
		//if ( solicitud.tipoOperacion==ClientesConstants.MICROCREDITO ){
		else{
			catSituacionVivienda = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SITUACION_VIVIENDA);
		}
	}
	if ( cliente.direcciones!=null ){
		direccion = cliente.direcciones[0];
	}

	TelefonoVO telefonoPrincipal = ClienteHelper.getTelefono(direccion.telefonos, ClientesConstants.TELEFONO_PRINCIPAL);
	TelefonoVO telefonoCelular = ClienteHelper.getTelefono(direccion.telefonos, ClientesConstants.TELEFONO_CELULAR);
	TreeMap catOperaciones = CatalogoHelper.getCatalogo(ClientesConstants.CAT_OPERACIONES);

	if ( clienteAyuda != null ){
		cliente.nombre = clienteAyuda.nombre;
		cliente.aPaterno = clienteAyuda.aPaterno;
		cliente.aMaterno = clienteAyuda.aMaterno;
		cliente.fechaNacimiento = clienteAyuda.fechaNacimiento;
	}
boolean esExterno = false;
if(usuario.identificador.equals("E"))
	esExterno = true;
%>

<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<jsp:include page="menuIzquierdo.jsp" flush="true"/>
<center>
<form name="forma" action="controller" method="post">
<input type="hidden" name="command" value="">
<input type="hidden" name="idSolicitud" value="<%=solicitud.idSolicitud%>">
<input type="hidden" name="idColonia" value="<%=HTMLHelper.displayField(direccion.idColonia)%>">
<input type="hidden" name="asentamientoCP" value="<%=HTMLHelper.displayField(direccion.asentamiento_cp)%>">
<input type="hidden" name="idLocalidad" value="<%=HTMLHelper.displayField(direccion.idLocalidad)%>">
<input type="hidden" name="telefonoRecados" value="">
<table border="0" width="100%">
	<tr>
	<td align="center">
<!-- INICIO DEL CODIGO ANTERIOR -->
<h3>Alta/Modificaci&oacute;n de cliente</h3>
<%=HTMLHelper.displayNotifications(notificaciones)%>
<table border="0" cellpadding="0" width="100%">
	<tr>
  	<td width="50%" align="right">N&uacute;mero de cliente</td>
  	<td width="50%">  
  	<input type="text" name="idCliente" size="10" value="<%=cliente.idCliente%>" readonly>
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">RFC</td>
  	<td width="50%">  
  	<input type="text" name="rfc" size="13" maxlength="13" value="<%=cliente.rfc%>" readonly>
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Producto</td>
  	<td width="50%">  
  	<input type="text" name="descripcion" size="18" value="<%=HTMLHelper.getDescripcion(catOperaciones, solicitud.tipoOperacion)%>" readonly>
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Reestructura</td>
  	<td width="50%">  
  	<%= HTMLHelper.displayCheck("reestructura", esReestructura, true) %>
  	</td>
	</tr>
	
	<%if(solicitud.tipoOperacion==ClientesConstants.VIVIENDA || solicitud.tipoOperacion==ClientesConstants.SELL_FINANCE) {%>
		<tr>
		  	<td width="50%" align="right">CURP</td>
		  	<td width="50%"><input type="text" name="curp" size="18" maxlength="18" value="<%=HTMLHelper.displayField(cliente.curp)%>"></td>
		</tr>
	<%} %>
	<tr>
  	<td width="50%" align="right">Fecha de captura de solicitud</td>
  	<td width="50%">  
  	<input type="text" name="solicitudFechaCaptura" size="10" maxlength="10" value="<%=HTMLHelper.displayField(solicitud.fechaCaptura,new Date())%>" readonly>(dd/mm/aaaa)
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">C&oacute;mo se enter&oacute; del servicio</td>
  	<td width="50%">  
  	<select name="medio" size="1">
		<%=HTMLHelper.displayCombo(catMedios,solicitud.medio)%>
	</select>
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Convenio</td>
  	<td width="50%">  
  	<select name="numrepresentante" size="1">
		<%=HTMLHelper.displayCombo(catRepresentantes,solicitud.numrepresentante)%>
	</select>
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Ejecutivo de cr&eacute;dito</td>
  	<td width="50%">
  	<%if ( solicitud.idEjecutivo==0 ){%>
  	<select name="ejecutivo" size="1">
  		<option value="0">Seleccione...</option>
		<%=HTMLHelper.displayCombo(catEjecutivosCredito,solicitud.idEjecutivo)%>
	</select>
	<%}else{%>
		<input type="hidden" name="ejecutivo" value="<%=solicitud.idEjecutivo%>">
		<input type="text" name="nombreEjecutivo" size="45" value="<%=HTMLHelper.getDescripcion(catEjecutivosCredito,solicitud.idEjecutivo)%>" readonly>
	<%}%>
  	</td>
	</tr>
	<%if(solicitud.tipoOperacion==ClientesConstants.CREDIHOGAR) {%>
		<tr>
			<td width="50%" align="right">Representante</td>
  			<td width="50%">  
		  	<select name="numrepresentante" size="1">
		  		<option value="0">Seleccione...</option>
				<%=HTMLHelper.displayCombo(catRepresentantes,solicitud.numrepresentante)%>
			</select>
			</td>
		</tr>
	<%} %>
	<tr>
  	<td width="50%" align="right">Nombres</td>
  	<td width="50%">  
  	<input type="text" name="nombre" size="45" maxlength="70" value="<%=HTMLHelper.displayField(cliente.nombre)%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Apellido paterno</td>
  	<td width="50%">  
  	<input type="text" name="aPaterno" size="45" maxlength="70" value="<%=HTMLHelper.displayField(cliente.aPaterno)%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Apellido materno</td>
  	<td width="50%">  
  	<input type="text" name="aMaterno" size="45" maxlength="70" value="<%=HTMLHelper.displayField(cliente.aMaterno)%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Fecha de nacimiento</td>
  	<td width="50%">  
  	<input type="text" name="fechaNacimiento" size="10" maxlength="10" value="<%=HTMLHelper.displayField(cliente.fechaNacimiento)%>">(dd/mm/aaaa)
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Sexo</td>
  	<td width="50%">  
  	<select name="sexo" size="1">
		<%=HTMLHelper.displayCombo(catSexo,cliente.sexo)%>
	</select>
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Dependientes econ&oacute;micos</td>
   	<td width="50%">  
  	<select name="dependientesEconomicos" size="1">
		<%=HTMLHelper.displayCombo(catDepEconomicos,cliente.dependientesEconomicos)%>
	</select>
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Tipo de identificaci&oacute;n</td>
  	<td width="50%">  
  	<select name="tipoIdentificacion" size="1">
		<%=HTMLHelper.displayCombo(catTiposIdentificacion,cliente.tipoIdentificacion)%>
	</select>
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">N&uacute;mero de identificaci&oacute;n</td>
  	<td width="50%">  
  	<input type="text" name="numeroIdentificacion" size="30" maxlength="30" value="<%=HTMLHelper.displayField(cliente.numeroIdentificacion)%>">
  	</td>
	</tr>
	<tr>
	<tr>
  	<td width="50%" align="right">Estado civil</td>
  	<td width="50%">  
  	<select name="estadoCivil" size="1">
		<%=HTMLHelper.displayCombo(catEstadoCivil,cliente.estadoCivil)%>
	</select>
  	</td>
	</tr>
	<tr>
  	<td width="100%" align="center" colspan="2"><a href="#" onClick="ayudaCodigoPostal()">Ayuda CP</a></td>
	</tr>
	<tr>
  	<td width="50%" align="right">Estado</td>
  	<td width="50%">  
  	<input type="text" name="estado" size="40" maxlength="100" value="<%=HTMLHelper.displayField(direccion.estado)%>" readonly>
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Municipio</td>
  	<td width="50%">  
  	<input type="text" name="municipio" size="40" maxlength="100" value="<%=HTMLHelper.displayField(direccion.municipio)%>" readonly>
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Colonia</td>
  	<td width="50%">  
  	<input type="text" name="colonia" size="40" maxlength="100" value="<%=HTMLHelper.displayField(direccion.colonia)%>" readonly>
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">C&oacute;digo postal</td>
  	<td width="50%">  
  	<input type="text" name="cp" size="6" maxlength="6" value="<%=HTMLHelper.displayField(direccion.cp)%>" readonly>
  	</td>
	</tr>
	<%if(solicitud.tipoOperacion==ClientesConstants.VIVIENDA) {%>
		<tr>
  			<td width="100%" align="center" colspan="2"><a href="#" onClick="ayudaLocalidad()">Ayuda Localidad</a></td>
		</tr>
		<tr>
	  		<td width="50%" align="right">Localidad</td>
	  		<td width="50%">  
	  		<input type="text" name="localidad" size="40" maxlength="80" value="<%=HTMLHelper.displayField(direccion.localidad)%>" readonly></td>
		</tr>
	<%} %>
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
	<tr>
  	<td width="50%" align="right">Antigüedad en domicilio </td>
  	<td width="50%">  
  	<input type="text" name="antiguedadDomicilio" size="7" maxlength="7" value="<%=HTMLHelper.displayField(direccion.antDomicilio)%>"> (mm/aaaa)
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Tel&eacute;fono domicilio o recados</td>
  	<td width="50%">  
  	<input type="text" name="telefonoPrincipal" size="10" maxlength="10" value="<%=HTMLHelper.displayField( telefonoPrincipal.numeroTelefono )%>">
  	</td>
	</tr>

	<tr>
  	<td width="50%" align="right">Tel&eacute;fono celular</td>
  	<td width="50%">  
  	<input type="text" name="telefonoCelular" size="10" maxlength="10" value="<%=HTMLHelper.displayField( telefonoCelular.numeroTelefono )%>">
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">Situaci&oacute;n de la vivienda</td>
  	<td width="50%">  
  	<select name="situacionVivienda" size="1">
			<%=HTMLHelper.displayCombo(catSituacionVivienda,direccion.situacionVivienda)%>
	</select>
  	</td>
	</tr>
	<tr>
  	<td width="50%" align="right">M&aacute;ximo nivel de educaci&oacute;n</td>
  	<td width="50%">  
  	<select name="nivelEstudios" size="1">
			<%=HTMLHelper.displayCombo(catNivelesEstudio ,cliente.nivelEstudios)%>
	</select>
  	</td>
	</tr>
	<tr>
	<%if( solicitud.desembolsado==ClientesConstants.DESEMBOLSADO ){ %>
		<td align="center" colspan="2">
			<br><input disabled type="button" value="Enviar" onClick="guardaDatosCliente(<%=solicitud.tipoOperacion%>)">
		</td>
	<%}else{ %>
		<td align="center" colspan="2">
			<br><input type="button" value="Enviar" onClick="guardaDatosCliente(<%=solicitud.tipoOperacion%>, <%=esExterno%>)">
		</td>
	<%}%>
	</tr>

</table>
</td>
	</tr>
</table>
<!-- FIN DEL CODIGO ANTERIOR -->		
</form>
</center>
<jsp:include page="footer.jsp" flush="true"/></body>
</html>