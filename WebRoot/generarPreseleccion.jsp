<%@page import="java.util.Vector"%>
<%@page import="java.util.TreeMap"%>
<%@page import="com.afirme.commons.model.dao.helpers.CatalogDAO"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp"%>
<%@ page import="com.sicap.clientes.dao.CatalogoDAO"%>
<%@ page import="com.sicap.clientes.vo.ParametroVO"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<jsp:directive.page import="com.sicap.clientes.util.inffinix.InffinixUtil"/>

<%
//Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
Vector notificaciones = (Vector)request.getAttribute("NOTIFICACIONES");

String ultimaFecha = CatalogoHelper.getParametro("FECHA_CIERRE");

Calendar calendario = Calendar.getInstance();

Date fechaActual = Convertidor.toSqlDate(calendario.getTime());
SimpleDateFormat sdf = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_EU);
Date fechaCierre = sdf.parse(ultimaFecha);
fechaCierre = Convertidor.toSqlDate(fechaCierre);
String fechaActualString = sdf.format(fechaActual);
CatalogoDAO catalogoDao = new CatalogoDAO();

TreeMap catFondeador = CatalogoHelper.getCatalogoFondeadorPreseleccion();
int idFondeador = 0;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
<script language="Javascript" src="./js/functions.js"></script>
<script>

   function ejecutaPreseleccion(){
//       Validar ademas o en lugar del cierre dia que no se este ejecutando el cierre bursa 
       <%if(!catalogoDao.ejecutandoCierreFondeadores() &&  !catalogoDao.ejecutandoCierre()){%>
               if(window.document.forma.idFondeadorCombo.value == 0){
                   alert("Seleccione un fondeador");
               }else{
                   if(confirm("Se va a procesar la preseleción")){
                       window.document.getElementById("botonEjecutaPre").disabled=true;
                       window.document.forma.command.value='ejecutaPreseleccion';
                       window.document.forma.submit();
                   }else{
                       return false;
                   }
                    
                }
              
       <%} else{%>
            alert("Se está ejecutando un proceso de cierre, intentarlos más tarde.");
            return false;
       <%}%>
    }
   
   function regresar(){
        window.document.forma.command.value='administracionFondeadores';
        window.document.forma.submit();
    }
   
	
</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    
<title>Preselecci&oacute;n de Cartera</title>
    
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

<h2>Administraci&oacute;n de Fondeadores</h2>
<h3>Preselecci&oacute;n de Cartera</h3>

<form name="forma" action="admin" method="post">
<input type="hidden" name="command" value="">

<table border="0" width="100%" cellspacing="0">
	<tr>
            <td width="100%" align="center" colspan="2"><%=HTMLHelper.displayNotifications(notificaciones)%><br></td>
	</tr>
        <tr>
            <td align="right">Fondeador a preseleccionar cartera</td>
            <td width="50%">
                <select name="idFondeadorCombo" size="1" >
                    <%= HTMLHelper.displayCombo(catFondeador, idFondeador)%>
                </select>
            </td>
       </tr>
        
        <tr>
        
        <%if((!catalogoDao.ejecutandoCierreFondeadores())&&(fechaCierre.before(fechaActual))&&(!ultimaFecha.equals(fechaActualString))){%>
        <td colspan="2" align="center"><br>
            <%if( !catalogoDao.ejecutandoCierre() ){%>
                <input type="button" id="botonEjecutaPre" value="Ejecutar preselecci&oacute;n" onClick="ejecutaPreseleccion()">
            <%}%>
            <input type="button" value="Regresar" onclick="regresar();">
        </td>
        <%}%>
        
        </tr>
        
        
</table>
</form>
</center>

<jsp:include page="footer.jsp" flush="true"/>
  </body>
</html>
