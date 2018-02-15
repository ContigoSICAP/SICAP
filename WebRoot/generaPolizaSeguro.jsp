<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@ page import="com.sicap.clientes.helpers.ClienteHelper"%>
<%@ page import="com.sicap.clientes.util.ClientesConstants"%>
<%@ page import="java.util.TreeMap"%>
<%@ page import="com.sicap.clientes.vo.ClienteVO"%>
<%@ page import="com.sicap.clientes.vo.BeneficiarioVO"%>
<%@ page import="com.sicap.clientes.vo.DireccionSucursalVO"%>
<%@ page import="com.sicap.clientes.vo.CatalogoVO"%>
<%@ page import="com.sicap.clientes.vo.UsuarioVO"%>
<%@ page import="com.sicap.clientes.util.ClientesUtil"%>
<%@ page import="com.sicap.clientes.util.Convertidor"%>
<%@ page import="com.sicap.clientes.util.FormatUtil"%>
<%@ page import="com.sicap.clientes.util.ConvertNumberToString"%>
<%@ page import="com.sicap.clientes.util.SolicitudUtil"%>
<%@ page import="com.sicap.clientes.dao.*"%>

<%
response.setContentType("application/rtf");
response.setHeader("Content-Disposition","attachment; filename=\"Poliza.rtf\"");
TreeMap catSexo = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SEXO);
//TreeMap catSucursales = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SUCURSALES);
UsuarioVO usuario = (UsuarioVO)session.getAttribute("USUARIO");
TreeMap catSucursales = CatalogoHelper.getCatalogoSucursal(usuario.sucursales);
TreeMap catSumasAseguradas = CatalogoHelper.getCatalogoSumaAsegurada();
TreeMap catRelacion = CatalogoHelper.getCatalogoRelacion();
ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DireccionSucursalDAO dirSucursalDAO = new DireccionSucursalDAO();
DireccionSucursalVO direccionVO = dirSucursalDAO.getDireccion(cliente.idSucursal);
String direccion = direccionVO.calle + " Col." + direccionVO.colonia + " C.p." + direccionVO.cp + " " + direccionVO.ciudad + ", " + direccionVO.estado;
CatalogoDAO catDAO = new CatalogoDAO();
CatalogoVO catVO[] = catDAO.getCatalogo("C_ACTIVIDADES");
String ocupacion = "";
String beneficiarios = "";
String relaciones = "";
String porcentajes = "";
for( int i=0 ; cliente.solicitudes[indiceSolicitud].negocio!=null && i<catVO.length ; i++ ){
	if(cliente.solicitudes[indiceSolicitud].negocio.activiad==catVO[i].id)
		ocupacion = catVO[i].descripcion;
}

for ( int i=0 ; i<cliente.solicitudes[indiceSolicitud].seguro.beneficiarios.length ; i++){
	BeneficiarioVO beneficiario = cliente.solicitudes[indiceSolicitud].seguro.beneficiarios[i];
	beneficiarios = beneficiarios + beneficiario.nombre+" "+beneficiario.aPaterno+" "+beneficiario.aMaterno+"<br>";
	relaciones = relaciones + (String)catRelacion.get( beneficiario.relacion )+"<br>";
	porcentajes = porcentajes + String.valueOf(beneficiario.porcentaje)+"<br>";
}
beneficiarios = beneficiarios.substring(0,beneficiarios.lastIndexOf('<'));
relaciones = relaciones.substring(0,relaciones.lastIndexOf('<'));
porcentajes = porcentajes.substring(0,porcentajes.lastIndexOf('<'));

int modulos = cliente.solicitudes[indiceSolicitud].seguro.modulos;
String strAsegurada = String.valueOf(FormatUtil.deleteChar((String)catSumasAseguradas.get(cliente.solicitudes[indiceSolicitud].seguro.sumaAsegurada),','));
Double sumaAsegurada = Convertidor.stringToDouble(strAsegurada)*modulos;
strAsegurada = HTMLHelper.formatCantidad(sumaAsegurada);
String decimal = strAsegurada.substring(strAsegurada.indexOf('.')+1);
int parteEntera = sumaAsegurada.intValue();
ConvertNumberToString numeroEntero = new ConvertNumberToString(parteEntera);
String sumaletras = numeroEntero.convertirLetras(parteEntera).toUpperCase();
String unidadesPlazo = "";
switch ( cliente.solicitudes[indiceSolicitud].decisionComite.frecuenciaPago ){
	case (ClientesConstants.PAGO_MENSUAL):
		unidadesPlazo = "meses";
		break;
	case (ClientesConstants.PAGO_QUINCENAL):
		unidadesPlazo = "quincenas";
		break;
	case (ClientesConstants.PAGO_SEMANAL):
		unidadesPlazo = "semanas";
}
%>
<html>
<head>

<title>Poliza de seguro</title>
  
<style type="text/css">
  p.parrafo {
  	text-align: left;
  	font-size: 11px;
  }
  p.parrafo_justificado {
  	text-align: justify;
  	font-size: 11px;
  }
  td.celdas {
  	text-align: center;
  	font-size: 11px;
  }
  td.celda_justificada{
  	text-align: justify;
  	font-size: 11px;
  }
</style>

</head>

<body rightmargin="0px" leftmargin="0px" topmargin="0px" bottommargin="0px" style="font-family: Arial;">

<table height="20%" width="100%" border="1" cellpadding="0" cellspacing="0">
<tr>
<td width="10%"><img name="imagen" src="<%=basePath%>/images/logoCF.jpg"  border="0"></td>
<td colspan="3" align="center"><font size="-1" style="font-weight: bold">POLIZA DE SEGURO DE VIDA</font></td>
<td colspan="2" align="center"><font size="-4">FECHA<br><%=HTMLHelper.displayField(new java.util.Date())%></font></td>
</tr>
<tr>
<td align="center"><img name="imagen" src="<%=basePath%>/images/logoSegurosAfirme.gif" border="0"></td>
<td colspan="3" align="center"><font size="-1">Seguros Afirme, S.A. de C.V.<br>AFIRME GRUPO FINANCIERO</font></td>
<td colspan="2" width="15%" align="center"><font size="-4">No DE CONSENTIMIENTO<br> CERTIFICADO INDIVIDUAL<br><%=HTMLHelper.displayField( cliente.solicitudes[indiceSolicitud].referencia )%></font></td>
</tr>
<tr>
<td align="center"><font size="-4">No. POLIZA MAESTRA: <br><%=ClientesConstants.NUM_POLIZA_MAESTRA%></font></td>
<td colspan="3" align="center"><font size="-4">VIGENCIA POLIZA MAESTRA : <br><%=ClientesConstants.VIG_POLIZA_MAESTRA%></font></td>
<td colspan="2" align="center"><font size="-4">OCUPACION : <br><%=ocupacion%></font></td>

</tr>
<tr>
<td colspan="4"><font size="-4">NUMERO Y NOMBRE DE SUCURSAL CONTRATANTE: <br><%=(String)catSucursales.get(new Integer(cliente.idSucursal)) + " - " + direccion%></font></td>
<td colspan="2" align="center"><font size="-4">PLAZO: <%=HTMLHelper.displayField( cliente.solicitudes[indiceSolicitud].decisionComite.plazoAutorizado+" "+unidadesPlazo )%></font></td>
</tr>
<tr>
<td colspan="2" height="10"><font size="-4">NOMBRE DEL ASEGURADO (NOMBRE(S) APELLIDO PATERNO Y MATERNO): <br><%=ClienteHelper.getNombreCompleto(cliente)%></font></td>
<td align="center"><font size="-4">SEXO :<br><%=(String)catSexo.get(new Integer(cliente.sexo))%></font></td>
<td align="center"><font size="-4">EDAD :<br><%=ClientesUtil.calculaEdad( cliente.fechaNacimiento )%> a&ntilde;os </font></td>
<td colspan="2" align="center"><font size="-4">FECHA DE NACIMIENTO: <br><%=HTMLHelper.displayField( cliente.fechaNacimiento )%></font></td>
</tr>
<tr>
<td align="center"><font size="-4">DOMICILIO: <br><%=HTMLHelper.displayField( cliente.direcciones[0].calle+" "+cliente.direcciones[0].numeroExterior+" "+cliente.direcciones[0].numeroInterior )%></font></td>
<td align="center"><font size="-4">COLONIA: <br><%=HTMLHelper.displayField( cliente.direcciones[0].colonia )%></font></td>
<td align="center"><font size="-4">CIUDAD: <br><%=HTMLHelper.displayField( cliente.direcciones[0].municipio )%></font></td>
<td align="center"><font size="-4">CP: <br><%=HTMLHelper.displayField( cliente.direcciones[0].cp )%></font></td>
<td colspan="2" align="center"><font size="-4">ESTADO: <br><%=HTMLHelper.displayField( cliente.direcciones[0].estado )%></font></td></tr>
<tr height="5">
<td colspan="6" height="5">
<font size="-4">TELEFONO : <%=cliente.direcciones[0].telefonos[0].numeroTelefono%></font>
</td>
</tr>
<tr height="5">
<td colspan="6" align="center">
<font size="-4">SUMA ASEGURADA : <%=strAsegurada + " (" + sumaletras + " PESOS " + decimal + "/100 M.N.)"%></font>
</td>
</tr>
<tr>
<td colspan="2" align="center"><font size="-4">BENEFICIARIOS:</font><br><font size="-4"><%=beneficiarios%></font></td>
<td colspan="2" align="center"><font size="-4">PARENTESCO(Para identificación):</font><br><font size="-4"><%=relaciones%></font></td>
<td colspan="2" align="center"><font size="-4">%</font><br><font size="-4"><%=porcentajes%></font></td>
</tr>

<tr>
<td colspan="6" align="justify"><font size="-4">ESTE DOCUMENTO Y LA NOTA TECNICA QUE LO  FUNDAMENTAN, ESTAN REGISTRADOS ANTE LA COMISION NACIONAL DE SEGUROS Y FIANZAS, DE CONFORMIDAD CON LO DISPUESTO EN EL ARTICULO 36 DE LA LEY GENERAL DE INSTITUCIONES Y SOCIEDADES MUTUALISTAS DE SEGUROS. POR OFICIO: 06-367-II. 1.1/24689 DEL EXPEDIENTE 732. 1 (S-190)/1 DEL 17/NOV/98.</font></td>
</tr>
</table>
<font size="1.5">
<b><font style="font-family: Arial;">(*) RECOMENDACIONES PARA LA DESIGNACION DE BENEFICIARIOS</font></b>
<p align="justify"><font style="font-family: Arial;">El asegurado debe designar en forma clara y precisa, para evitar cualquier incertidumbre sobre el particular. En caso de que se desee nombrar beneficiarios a menores de edad, no se debe señalar a un mayor de edad como representante de los menores para efecto de que, en su representación, cobre la indemnización. Lo anterior porque las legislaciones civiles previenen la forma en que deben designarse tutores, albaceas, representante de herederos u otros cargos similares y no consideran al contrato de seguro como el instrumento adecuado para tales designaciones. La designación que se hiciera de un mayor de edad como representante de menores beneficiarios, durante la minoría de edad de ellos, legalmente pueden implicar que se nombre beneficiario al mayor de edad, quien en sólo caso tendría una obligacion moral, pues la designación que se hace de beneficiarios en un contrato de seguro le concede el derecho incondicionado de disponer de la Suma Asegurada.</font></p>
<p><font style="font-family: Arial;">SI HAY VARIOS BENEFICIARIOS Y USTED DESEA QUE RECIBAN CONJUNTAMENTE LA SUMA ASEGURADA, DEBERA HACER SU DESIGNACIÓN DE TAL FORMA QUE QUEDE CLARO EL NOMBRE DE QUIENES Y CUANTO VAN A RECIBIR, TOMANDO EN CUENTA LA SIGUIENTE RECOMENDACIÓN:<br>
   a).- <font style="font-weight: bold">NOMBRE (S) COMPLETO (S)</font>, SIN ABREVIATURAS, NI LETRAS SUELTAS, A MENOS QUE ASÍ APAREZCA EN SU ACTA DE NACIMIENTO O CREDENCIAL DE ELECTOR, EN CASO DE MAYORES DE EDAD Y LAS MUJERES CON APELLIDO DE SOLTERA.<br>
   b).- <font style="font-weight: bold">PARENTESCO BIEN DEFINIDO</font> (padre, madre, tío, tía, hijo, hija, hermano, hermana, esposo, esposa, etc.)<br>
   c).- <font style="font-weight: bold">PORCENTAJE (%)</font> DEL REPARTO PARA CADA BENEFICIARIO. EL TOTAL DEL PORCENTAJE DEBERA SER UN 100%.<br>
</font></p>

<p><b><font style="font-family: Arial;">DOCUMENTACION NECESARIA EN CASO DE SINIESTRO:</font></b><br>
<font style="font-family: Arial;">
1. PÓLIZA DE SEGURO DE VIDA ORIGINAL<br>
2. DEL ASEGURADO(ORIGINALES): ACTA DE NACIMIENTO, ACTA DE DEFUNCION Y CERTIFICADO MÉDICO QUE INDIQUE LA CAUSA DE LA MUERTE <br>
3. DECLARACIONES DE LA ASEGURADORA<br>
4. DE LOS BENEFICIARIOS: ACTA DE NACIMIENTO ORIGINAL, IDENTIFICACION OFICIAL CON FOTOGRAFIA<br>
5. EN CASO DE MUERTE ACCIDENTAL, ACTA DEL MINISTERIO PÚBLICO ORIGINAL<br>
</font>
</p>

<p><b><font style="font-family: Arial;">ENTREGUE ESTOS DOCUMENTOS EN LAS OFICINAS DE CREDITO FIRME, DOMICILIO: <%=direccion%></font></b></p>

<p><b><font style="font-family: Arial;">IMPORTANTE:</font></b>
<font style="font-family: Arial;">
* SOLO SE PUEDEN ASEGURAR PERSONAS DE 18 A 65 AÑOS DE EDAD.<br>
* DEBIDO A LOS REQUISITOS DE LA COMPAÑÍA ASEGURADORA: ESTE SEGURO DE VIDA ES VALIDO HASTA QUE LA PERSONA ASEGURADA CUMPLA 66  AÑOS DE EDAD Y TENGA VIGENTE EL SEGURO.</font><br>
<font size="3"><b><font style="font-family: Arial;">* AL MOMENTO DE CONTRATAR ESTE SEGURO EL ASEGURADO DEBER&Aacute; MANIFESTAR SI PADECE O HA PADECIDO DURANTE LOS ULTIMOS 2 A&Ntilde;OS ALGUNA DE LAS SIGUIENTES ENFERMEDADES: CORAZ&Oacute;N, DIABETES, SIDA, TUMORES, CANCER, ENFERMEDADES PULMONARES, YA QUE SI ESTE ES SU CASO, NO PODRA OBTENER ESTE SEGURO.</font></b></font><br>
<font style="font-family: Arial;">
* LA VIGENCIA DE ESTE CERTIFICADO INDIVIDUAL ESTA SUJETA AL PAGO DEL PERIODO CORRESPONDIENTE: NO ES PROCEDENTE PONER LOS PAGOS AL CORRIENTE DESPUES DEL FALLECIMIENTO DEL ASEGURADO.<br>
* EN CASO OMISION, FALSEDAD O INEXACTA DECLARACION SEGUROS AFIRME, S.A. DE C.V. AFIRME GRUPO FINANCIERO, PODRA CANCELAR EL CONTRATO Y SI FALLECE NO SE RESPONSABILIZA EN PAGAR EL SEGURO A LOS BENEFICIARIOS.
</font></p>

<p align="center"><b><font style="font-family: Arial;">DECLARO QUE TODO LO AQUÍ DESCRITO ES VERDAD</font></b></p>

<p align="center">
<br>____________________________<br>
<font style="font-family: Arial;">FIRMA DEL ASEGURADO</font>
</p>
</font>
</body>
</html>
