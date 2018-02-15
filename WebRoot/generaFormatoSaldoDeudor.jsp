<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.util.ClientesConstants"%>
<%@ page import="java.util.TreeMap"%>
<%@ page import="com.sicap.clientes.vo.ClienteVO"%>
<%@ page import="com.sicap.clientes.util.FechasUtil"%>
<%@ page import="com.sicap.clientes.util.Logger"%>
<%@ page import="com.sicap.clientes.dao.DireccionSucursalDAO"%>
<%@ page import="com.sicap.clientes.vo.DireccionSucursalVO"%>
<%@ page import="com.sicap.clientes.util.SolicitudUtil"%>
<%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server

response.setContentType("application/rtf");
response.setHeader("Content-Disposition","attachment; filename=\"Poliza.rtf\"");
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

ClienteVO cliente = (ClienteVO)session.getAttribute("CLIENTE");
int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
TreeMap catSexo = CatalogoHelper.getCatalogo(ClientesConstants.CAT_SEXO);
TreeMap catActividades = CatalogoHelper.getCatalogo(ClientesConstants.CAT_ACTIVIDADES);
String actividad = "";
//String contrato = "";
DireccionSucursalVO dirSucursal = null;
dirSucursal = new DireccionSucursalDAO().getDireccion(cliente.idSucursal);
if ( cliente.solicitudes[indiceSolicitud].negocio!=null ){
	Logger.debug("\n\nNegocio : "+cliente.solicitudes[indiceSolicitud].negocio);
	actividad = (String)catActividades.get( new Integer(cliente.solicitudes[indiceSolicitud].negocio.activiad) );
}
//if ( cliente.solicitudes[indiceSolicitud].contrato!=null )
//	contrato = cliente.solicitudes[indiceSolicitud].contrato;
%>
<html>
<head>

<title>Poliza de seguro</title>

</head>

<body rightmargin="0px" leftmargin="0px" topmargin="0px" bottommargin="0px" style="font-family: Arial;" >

<table height="20%" width="100%" border="1" cellpadding="0" cellspacing="0">
<tr>
	<td>
	<img name="imagen" src="<%=basePath%>/images/logoSegurosAfirme.gif"  border="0">
	</td>
	<td colspan="9" align="right">
	<font size="-1" style="font-weight: bold">CONSENTIMIENTO INDIVIDUAL<br> PARA EL SEGURO COLECTIVO DE VIDA<br> TEMPORALA UN AÑO RENOVABLE</font>
	</td>
</tr>
<tr>
	<td colspan="10" align="left">
	<font size="-2" style="font-weight: bold">DATOS GENERALES</font>
	</td>
</tr>
<tr>
	<td colspan="6" align="left">
	<font size="-2">Consentimiento individual para formar parte del Seguro Colectivo de Vida Temporal a un Año solicitado a <b>Seguros AFIRME, S.A. de C.V., Afirme Grupo Financiero</b>, por la colectividad a la que pertenezco.</font>
	</td>
	<td colspan="2" align="center" valign="top">
	<font size="-2">NUMERO DE CERTIFICADO<br><%=HTMLHelper.displayField( cliente.solicitudes[indiceSolicitud].referencia )%></font>
	</td>
	<td align="center">
	<font size="-2">NUMERO DE POLIZA <br><%=ClientesConstants.NUM_POLIZA_MAESTRA_SALDO%></font>
	</td>
	<td align="center" colspan="2">
	<font size="-2">VIGENCIA DE POLIZA <br><%=ClientesConstants.VIG_POLIZA_MAESTRA_SALDO%></font>
	</td>
</tr>
<tr>
	<td colspan="10" align="left">
	<font size="-2" style="font-weight: bold">CONTRATANTE O RAZON SOCIAL <br> CREDITO FIRME S.A. DE C.V. SOFOM E.R.</font>
	</td>
</tr>
<tr>
	<td colspan="3" align="left">
	<font size="-2">NOMBRE (S) DEL ASEGURADO:<br> <%=cliente.nombre%></font>
	</td>
	<td colspan="3" align="left">
	<font size="-2">APELLIDO PATERNO:<br> <%=cliente.aPaterno%></font>
	</td>
	<td colspan="3" align="left">
	<font size="-2">APELLIDO MATERNO:<br> <%=cliente.aMaterno%></font>
	</td>
	<td align="center">
	<font size="-2">SEXO: <%=catSexo.get(new Integer(cliente.sexo))%></font>
	</td>
</tr>
<tr>
	<td align="left" width="15%">
	<font size="-2">FECHA DE NACIMIENTO</font>
	</td>
	<td align="center" width="3%">
	<font size="-2"><%=FechasUtil.obtenParteFecha(cliente.fechaNacimiento, 1)%><br>DIA</font>
	</td>
	<td align="center" width="3%">
	<font size="-2"><%=FechasUtil.obtenParteFecha(cliente.fechaNacimiento, 2)%><br>MES</font>
	</td>
	<td align="center" width="3%">
	<font size="-2"><%=FechasUtil.obtenParteFecha(cliente.fechaNacimiento, 3)%><br>AÑO</font>
	</td>
	<td align="center" width="15%">
	<font size="-2">FECHA DE ALTA DE SEGURO</font>
	</td>
	<td align="center" width="3%">
	<font size="-2">01<br>DIA</font>
	</td>
	<td align="center" width="3%">
	<font size="-2">03<br>MES</font>
	</td>
	<td align="center" width="3%">
	<font size="-2">2008<br>AÑO</font>
	</td>
	<td align="center" width="31%">
	<font size="-2">OCUPACIÓN O ACTIVIDAD DEL ASEGURADO<br><%=actividad%></font>
	</td>
	<td align="center" width="21%">
	<font size="-2">SUMA ASEGURADA <br>SALDO INSOLUTO</font>
	</td>
</tr>
<tr>
	<td colspan="10" align="left">
	<font size="-2" style="font-weight: bold">CUESTIONARIO DE SALUD</font>
	</td>
</tr>
<tr>
	<td colspan="5" align="left" bgcolor="yellow">
	<font size="-2">ESTATURA<br>____________________________________cm</font>
	</td>
	<td colspan="5" align="left" bgcolor="yellow">
	<font size="-2" >PESO<br>____________________________________kg</font>
	</td>
</tr>
<tr>
	<td colspan="7" align="left">
	<font size="-2" >1.- ¿ Ha presentado usted algún padecimiento relacionado con el corazón, el sistema circulatorio, las vias respiratorias, el aparato digestivo, el aparato reproductor o las vías urinarias ?</font>
	</td>
	<td colspan="3" align="center" bgcolor="yellow" valign="bottom">
	<font size="-2">NO<img border="0" name="imagen" src="<%=basePath%>/images/cuadro.jpg">&nbsp;</font>
	<font size="-2">SI<img border="0" name="imagen" src="<%=basePath%>/images/cuadro.jpg">&nbsp;____________________________________</font>
	</td>
</tr>
<tr>
	<td colspan="7" align="left">
	<font size="-2" >2.- ¿ Presenta usted alguna enfermedad como diabetes, cáncer, tumores o SIDA ?</font>
	</td>
	<td colspan="3" align="center" bgcolor="yellow" valign="bottom">
	<font size="-2">NO<img border="0" name="imagen" src="<%=basePath%>/images/cuadro.jpg">&nbsp;</font>
	<font size="-2">SI<img border="0" name="imagen" src="<%=basePath%>/images/cuadro.jpg">&nbsp;____________________________________</font>
	</td>
</tr>
<tr>
	<td colspan="7" align="left">
	<font size="-2" >3.- ¿ Usted esta bajo algún tipo de tratamiento médico o tiene programada alguna intervención quirúrgica ?</font>
	</td>
	<td colspan="3" align="center" bgcolor="yellow" valign="bottom">
	<font size="-2">NO<img border="0" name="imagen" src="<%=basePath%>/images/cuadro.jpg">&nbsp;</font>
	<font size="-2">SI<img border="0" name="imagen" src="<%=basePath%>/images/cuadro.jpg">&nbsp;____________________________________</font>
	</td>
</tr>
<tr>
	<td colspan="10" align="left">
	<font size="-2">En caso de haber contestado de manera afirmativa alguna de las preguntas anteriores, favor de dar amplia informacion en el cuadro siguiente:</font>
	</td>
</tr>
<tr>
	<td align="center">
	<font size="-2">PREGUNTA</font>
	</td>
	<td colspan="3" align="center">
	<font size="-2">ENFERMEDAD, LESION, ESTUDIOS O TRATAMIENTOS</font>
	</td>
	<td align="center">
	<font size="-2">FECHA</font>
	</td>
	<td colspan="2" align="center">
	<font size="-2">DURACION</font>
	</td>
	<td colspan="3" align="center">
	<font size="-2">CONDICION ACTUAL</font>
	</td>
</tr>
<tr>
	<td align="center">
	<font size="-2">&nbsp;</font>
	</td>
	<td colspan="3" align="center">
	<font size="-2">&nbsp;</font>
	</td>
	<td align="center">
	<font size="-2">&nbsp;</font>
	</td>
	<td colspan="2" align="center">
	<font size="-2">&nbsp;</font>
	</td>
	<td colspan="3" align="center">
	<font size="-2">&nbsp;</font>
	</td>
</tr>
<tr>
	<td align="center">
	<font size="-2">&nbsp;</font>
	</td>
	<td colspan="3" align="center">
	<font size="-2">&nbsp;</font>
	</td>
	<td align="center">
	<font size="-2">&nbsp;</font>
	</td>
	<td colspan="2" align="center">
	<font size="-2">&nbsp;</font>
	</td>
	<td colspan="3" align="center">
	<font size="-2">&nbsp;</font>
	</td>
</tr>
<tr>
	<td align="center">
	<font size="-2">&nbsp;</font>
	</td>
	<td colspan="3" align="center">
	<font size="-2">&nbsp;</font>
	</td>
	<td align="center">
	<font size="-2">&nbsp;</font>
	</td>
	<td colspan="2" align="center">
	<font size="-2">&nbsp;</font>
	</td>
	<td colspan="3" align="center">
	<font size="-2">&nbsp;</font>
	</td>
</tr>
<tr>
	<td colspan="10" align="left">
	<font size="-2" style="font-weight: bold">DESIGNACION DE BENEFICIARIOS</font>
	</td>
</tr>
<tr>
	<td colspan="2" align="center">
	<font size="-2">NOMBRE (S)</font>
	</td>
	<td colspan="2" align="center">
	<font size="-2">APELLIDO PATERNO</font>
	</td>
	<td colspan="2" align="center">
	<font size="-2">APELLIDO MATERNO</font>
	</td>
	<td align="center">
	<font size="-2">%</font>
	</td>
	<td colspan="3" align="center">
	<font size="-2">PARENTESCO PARA EFECTOS DE IDENTIFICACION</font>
	</td>
</tr>
<tr>
	<td colspan="6" align="center">
	<font size="-2">BENEFICIARIO PREFERENTE: CREDITO FIRME S.A. DE C.V SOFOM E.R.</font>
	</td>
	<td align="center">
	<font size="-2">100</font>
	</td>
	<td colspan="3" align="center">
	<font size="-2">TITULAR</font>
	</td>
</tr>
<tr>
	<td colspan="10" align="left">
	<font size="-2" style="font-weight: bold">ADVERTENCIAS</font>
	</td>
</tr>
</table>

<p><font size="1">
En el caso de que se desee nombrar beneficiarios a menores de edad, no se debe señalar a un mayor de edad como representante de los menores para efecto de que, en su representación, cobre la indemnización.<br>
Lo anterior porque las legislaciones civiles previenen la forma en que debe designarse tutores,albaceas, representantes de herederos u otros cargos similares y no consideran al contrato de seguro como el instrumento adecuado para tales designaciones.<br>
La designación que se hiciera de un mayor de edad como representante de menores beneficiarios, durante la minoria de edad de ellos, legalmente puede implicar que se nombra beneficiario al mayor de edad, quien en todo caso sólo tendría una obligación moral, pues la designación que se hace de beneficiarios en un contrato de seguro le concede el derecho incondicinado de disponer de la Suma Asegurada.<br>
OTORGO MI CONSENTIMIENTO PARA SER ASEGURADO EN LA POLIZA DE SEGURO COLECTIVO DE VIDA CITADA A <b>SEGUROS AFIRME, S.A. DE C.V., AFRIME GRUPO FINANCIERO</b>, DE ACUERDO A LAS CONDICIONES GENERALES DE LA POLIZA.
PARA TODOS LOS EFECTOS QUE PUEDA TENER ESTE CONSENTIMIENTO, HAGO CONSTAR QUE LAS DECLARACIONES CONTENIDAS EN EL MISMO LAS HE HECHO PERSONALMENTE, SON VERIDICAS Y ESTAN COMPLETAS.
</font></p>

<p align="center"><font size="1">Lugar y Fecha: <%=dirSucursal.ciudad+", "+dirSucursal.estado%> <font style="background-color: yellow;">el día de _________________________ de ____________________________ de __________ </font></font></p>
<br><br>
<table align="center" width="100%">
	<tr>
		<td width="10%"><font size="1"></font></td>
		<td width="35%" align="center"><font size="1">________________________________</font></td>
		<td width="10%"><font size="1"></font></td>
		<td width="35%" align="center"><font size="1">________________________________</font></td>
		<td width="10%"><font size="1"></font></td>
	</tr>
	<tr>
		<td width="10%"></td>
		<td width="35%" align="center"><font size="1">FIRMA DEL ASEGURADO</font></td>
		<td width="10%"></td>
		<td width="35%" align="center"><font size="1">FIRMA DEL CONTRATANTE</font></td>
		<td width="10%"></td>
	</tr>
</table>
<br>
<p><font size="1">
Este documento y al nota técnica que lo fundamenta, están registrados ante la Comisión Nacional de Seguros y Fianaza de conformidad con los Artículos 36-A 36-B de la Ley General de Instituciones y Sociedades Mutualistas de Seguros, por oficio 06-367-II-I.I/24689 de expediente 732.1 (S-190)/4 del 17 de Noviembre de 98
</font></p>
</body>
</html>