<%@page import="com.afirme.commons.model.dao.helpers.CatalogDAO"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp"%>
<%@ page import="com.sicap.clientes.dao.CatalogoDAO"%>
<%@ page import="com.sicap.clientes.vo.ParametroVO"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@page import="java.util.Vector"%>
<jsp:directive.page import="com.sicap.clientes.util.inffinix.InffinixUtil"/>

<%
Vector<Notification> notificaciones = (Vector<Notification>)request.getAttribute("NOTIFICACIONES");
String ultimaFecha = CatalogoHelper.getParametro("FECHA_CIERRE");
int horaEspera = Integer.valueOf(CatalogoHelper.getParametro("HORA_CIERRE_MES"));
Calendar calendario = Calendar.getInstance();
String tiempoRestante = "";
if(horaEspera>0){
    Calendar horaRestante = Calendar.getInstance();
    horaRestante.set(Calendar.HOUR_OF_DAY, horaEspera);
    horaRestante.set(Calendar.MINUTE, 0);
    horaRestante.set(Calendar.SECOND, 0);
    long milis1 = horaRestante.getTimeInMillis();
    long milis2 = calendario.getTimeInMillis();
    long diff = milis1 - milis2;
    if(diff>0){
        int seconds = (int) (diff / 1000) % 60 ;
        int minutes = (int) ((diff / (1000*60)) % 60);
        int hours   = (int) ((diff / (1000*60*60)) % 24);
        tiempoRestante = FormatUtil.completaCadena(hours+"", '0', 2, "L")+":"+FormatUtil.completaCadena(minutes+"", '0', 2, "L")+":"+FormatUtil.completaCadena(seconds+"", '0', 2, "L");
    } else {
        horaEspera = 0;
    }
}
Date fechaActual = Convertidor.toSqlDate(calendario.getTime());
SimpleDateFormat sdf = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_EU);
Date fechaCierre = sdf.parse(ultimaFecha);
fechaCierre = Convertidor.toSqlDate(fechaCierre);
String fechaActualString = sdf.format(fechaActual);
CatalogoDAO catalogoDao = new CatalogoDAO();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
<script language="Javascript" src="./js/functions.js"></script>
<script>

   function cierreDia(){
       <%if(!catalogoDao.ejecutandoCierre()){%>
               if(confirm("Se va a procesar el cierre de dia")){
                   window.document.getElementById("botonCierre").disabled=true;
                    window.document.forma.command.value='procesaCierre';
                    window.document.forma.submit();
                } else {
                    return false;
                }
                <%} else{%>
                    alert("El cierre de día se encuentra en proceso");
                    return false;
                <%}%>
    }
    
   function restarTiempo(){
       if(window.document.forma.horaEspera.value >24){
           alert("La hora establecida supera las 24 hrs selecciones una hora menor.");
           return false;
       } else {
           window.document.forma.command.value='restarHora';
           window.document.forma.submit();
       }
    }
	
</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    
    <title>Cierre Dia Automático</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
<body leftmargin="0" topmargin="0" >
<jsp:include page="header.jsp" flush="true"/>
<center>

<h2>Administraci&oacute;n de Cierre</h2>
<h3>Cierre de Dia Automático</h3>

<form name="forma" action="admin" method="post">
<input type="hidden" name="command" value="">
<table border="0" width="100%" cellspacing="0">
	<tr>
            <td width="100%" align="center" colspan="2"><%=HTMLHelper.displayNotifications(notificaciones)%><br></td>
	</tr>
        <tr>
            <td width="50%" align="right">Fecha Cierre</td>
            <td width="50%"> 
                <input size="11" name="fechaCierre" value="<%=HTMLHelper.displayField(ultimaFecha)%>" readonly="readonly" class="soloLectura">			
            </td>
        </tr>
        <tr>
        <%if(horaEspera>0){%>
            <td width="50%" align="center" colspan="2"><br><b>Tiempo estimado restante para siguiente Cierre <%=tiempoRestante%></b></td>
        <%} else {%>
            <%if((!catalogoDao.ejecutandoCierre())&&(fechaCierre.before(fechaActual))&&(!ultimaFecha.equals(fechaActualString))){%>
            <td colspan="2" align="center"><br><input type="button" id="botonCierre" value="Cerrar Dia" onClick="cierreDia()"></td>
            <%}%>
        <%}%>
        </tr>
        <%if(horaEspera>0 && request.isUserInRole("BLOQUEO_CIERRE")){%>
        <tr><td>&nbsp;</td></tr>
        <tr>
            <td width="50%" align="right">Tiempo de Espera</td>
            <td width="10%"> 
                <input size="2" maxlength="2" name="horaEspera" id="horaEspera" value="<%=HTMLHelper.displayField(horaEspera)%>" >			
            </td>
        </tr>
        <tr>
            <td colspan="2" align="center"><br><input type="button" id="botonRestar" value="Restablecer Tiempo" onClick="restarTiempo()"></td>
        </tr>
        <%}%>
</table>
</form>
</center>

<jsp:include page="footer.jsp" flush="true"/>
  </body>
</html>
