<%-- 
    Document   : saldoFondeadores
    Created on : 10/06/2015, 12:16:26 PM
    Author     : avillanueva
--%>

<%@page import="java.util.Date"%>
<%@page import="com.sicap.clientes.vo.SaldoFondeadorVO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%
ArrayList<SaldoFondeadorVO> arrFondeador = new ArrayList<SaldoFondeadorVO>();
if(request.getAttribute("SaldoFondeadores") != null){
    arrFondeador = (ArrayList<SaldoFondeadorVO>) request.getAttribute("SaldoFondeadores");
}
int numFondeador = arrFondeador.size();
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <title>Saldo Fondeadores</title>
        <script language="Javascript" src="./js/functions.js"></script>
        <script>
            function regresar(){
                window.document.forma.command.value='administracionFondeadores';
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
        <center>
        <h2>Saldo Fondeadores</h2> 
        <form name="forma" action="admin" method="post">
            <input type="hidden" name="command" value="">
            <table border="1" width="40%" cellspacing="0">
                <tr><td align="center" colspan="<%=(numFondeador+1)%>">Saldos del d&iacute;a <%=HTMLHelper.displayField(new Date())%></td></tr>
                <tr>
                    <td align="center">&nbsp;</td>
                <%for(SaldoFondeadorVO fondeador : arrFondeador){%>
                    <td align="center"><b><%=HTMLHelper.displayField(fondeador.getNombreFondeador())%></b></td>
                <%}%>
                </tr>
                <tr>
                    <td width="10%" align="left"><b><i>Saldo Inicial</b></i></td>
                <%for(SaldoFondeadorVO fondeador : arrFondeador){%>
                    <td width="<%=(30/numFondeador)%>%" align="right"><i>$ </i><%=HTMLHelper.formatoMontoMillones(fondeador.getSaldoInicial())%></td>
                <%}%>
                </tr>
                <tr>
                    <td align="left"><b><i>&nbsp;&nbsp;Dispersiones</b></i></td>
                <%for(SaldoFondeadorVO fondeador : arrFondeador){%>
                    <td align="right"><i>$ </i><%=HTMLHelper.formatoMontoMillones(fondeador.getMontoDispersion())%></td>
                <%}%>
                </tr>
                <tr>
                    <td align="left"><b><i>&nbsp;&nbsp;Cancelaciones</b></i></td>
                <%for(SaldoFondeadorVO fondeador : arrFondeador){%>
                    <td align="right"><i>$ </i><%=HTMLHelper.formatoMontoMillones(fondeador.getMontoCancelacion())%></td>
                <%}%>
                </tr>
                <tr>
                    <td align="left"><b><i>Saldo Final</b></i></td>
                <%for(SaldoFondeadorVO fondeador : arrFondeador){%>
                    <td align="right"><i>$ </i><%=HTMLHelper.formatoMontoMillones(fondeador.getSaldoFinal())%></td>
                <%}%>
                </tr>
            </table>
                <br/><input type="button" value="Regresar" onClick="regresar()">
        </center>
        <jsp:include page="footer.jsp" flush="true"/></body>
    </body>
</html>
