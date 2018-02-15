<%@page import="java.util.Date"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@ page import="com.sicap.clientes.vo.LineaCreditoVO"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.dao.*"%>
<%@ page import="java.util.TreeMap"%>
<%
    int idLineaCredito = 0;
    String command = "desactivarLineaCredito";
    LineaCreditoVO lineaVO = (LineaCreditoVO) request.getAttribute("LINEACREDITO");
    TreeMap lineasCredito = CatalogoHelper.getLineasCreditoActivas();
    Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");
    String[] roles = new UsuarioDAO().getRoles();
    
    if (lineaVO == null) 
    {
        command = "desactivarLineaCredito";
    }
%>
<html>
    <head>
        <title> Desactivar L&iacute;nea de Cr&eacute;dito</title>
        <script type="text/javascript" src="./js/functions.js"></script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
        <script>
            function validaCamposLineaCredito()
            {
                if(window.document.forma.idLineaCredito.value==0){
                    alert("Es necesario seleccionar una Línea de Crédito");
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if(window.document.forma.idLineaCredito.value===null && window.document.forma.idLineaCredito.value===0)
                {
                    alert("Es necesario seleccionar una Línea de Crédito");
                    window.document.forma.idFondeador.focus();
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }
	
            function redireccionMenuGestionFondeadores(){
                window.document.forma.command.value = 'administracionFondeadores';
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }
            
            
        </script>
    </head>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
    <center>
        <h2>Desactivar L&iacute;nea de Cr&eacute;dito</h2> 
    </center>
    <form method="post" name="forma" action="admin">
        <input type="hidden" name="command" value="<%=command%>">
        <table border="0" cellspacing="5" align="center" >
            <tr>
                <td align="center" colspan="2">
                    <%=HTMLHelper.displayNotifications(notificaciones)%><br>
                </td>
            </tr>
            <tr>
                <th align="right">Linea de Crédito:</th>
                <td width="50%">  
                    <select name="idLineaCredito" size="1">
                        <%=HTMLHelper.displayCombo(lineasCredito, idLineaCredito)%>
                    </select>
                </td>
            </tr>
           
            <tr>
                <td align="center" colspan="2"><br><input type="button" id="boton" value="Desactivar" onclick="validaCamposLineaCredito()"> <input type="button" id="boton" value="Regresar" onClick="redireccionMenuGestionFondeadores()"><br><br></td>
            </tr>
        </table>
    </form>

</body>
</html>
