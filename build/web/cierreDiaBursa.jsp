<%@page import="java.util.Vector"%>
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
    Vector notificaciones = (Vector)request.getAttribute("NOTIFICACIONES");
    //Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
    SimpleDateFormat sdf = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_EU);
    CatalogoDAO catalogoDao = new CatalogoDAO();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
<script language="Javascript" src="./js/functions.js"></script>
<script>

   function cierreDiaBursa(){
//       Validar ademas o en lugar del cierre dia que no se este ejecutando el cierre bursa 
       <%if(!catalogoDao.ejecutandoCierre()){%>
               if(confirm("Se va a procesar el cierre de dia")){
                   window.document.getElementById("botonCierre").disabled=true;
                    window.document.forma.command.value='procesaCierreBursa';
                    window.document.forma.submit();
                } else {
                    return false;
                }
                <%} else{%>
                    alert("El cierre de día se encuentra en proceso");
                    return false;
                <%}%>
    }
    
   
	
</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    
    <title>Cierre BURSA</title>
    
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
<h3>Cierre BURSA</h3>

<form name="forma" action="admin" method="post">
<input type="hidden" name="command" value="">
<table border="0" width="100%" cellspacing="0">
	<tr>
            <td width="100%" align="center" colspan="2"><%=HTMLHelper.displayNotifications(notificaciones)%><br></td>
	</tr>
        
        <tr>
        
        <%if( !catalogoDao.ejecutandoCierre() ){%>
            <td colspan="2" align="center"><br><input type="button" id="botonCierre" value="Cerrar Dia Bursa" onClick="cierreDiaBursa()"></td>
        <%}%>
        
        </tr>
        
        
</table>
</form>
</center>

<jsp:include page="footer.jsp" flush="true"/>
  </body>
</html>
