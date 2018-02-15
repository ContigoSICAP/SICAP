<%@page import="mantenimiento.StringMD"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="org.apache.log4j.Logger"%>
<%! public static Logger myLogger = Logger.getLogger("convierteAMD5");%>
<%
    String texto = request.getParameter("textoMD5");
    String textoMD5 = null;
    String textoMD5_2 = null;
    if (texto != null && !texto.equals("")) {
        textoMD5 = SecurityUtil.toMD5(texto);
        textoMD5_2 = StringMD.getStringMessageDigest(texto, "MD5");
    } else {
        textoMD5 = null;
    }
    myLogger.debug("texto::" + texto);
    myLogger.debug("textoMD5::" + textoMD5);
    myLogger.debug("textoMD5_2" + textoMD5_2);
    
%>
<html>
    <head>
        <title>Alta Conversi&oacute;n a MD5</title>
        <script language="Javascript" src="./js/functions.js"></script>
        <script>
            function enviaTextoEncripcion() {
                document.forma.command.value = "capturaTextoEncripcion";
                document.forma.submit();
            }
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
    <center>
        <h3>Encripci&oacute;n de texto</h3>
        <form name="forma" method="post">
            <input type="hidden" name="command"/>
            <table border="0" width="100%" cellspacing="0">
                <tr>
                    <td width="50%" align="right">Texto</td>
                    <td width="50%">  
                        <input type="text" name="textoMD5" size="20">
                    </td>
                </tr>
                <%if (textoMD5 != null) {%>
                <tr>
                    <td align="center" colspan="2">
                        <br>Resultado: [<b><%=textoMD5%></b>]
                    </td>
                </tr>
                <tr>
                    <td align="center" colspan="2">
                        <br>Resultado: [<b><%=textoMD5_2%></b>]
                    </td>
                </tr>
                <%}%>
                <tr>
                    <td align="center" colspan="2">
                        <br><input type="button" value="Enviar" onClick="enviaTextoEncripcion()">
                    </td>
                </tr>
            </table>
        </form>
    </center>

    <jsp:include page="footer.jsp" flush="true"/></body>
</html>