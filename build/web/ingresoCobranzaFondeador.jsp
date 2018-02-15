<%-- 
    Document   : ingresoCobranzaFondeador
    Created on : 4/08/2015, 01:40:22 PM
    Author     : avillanueva
--%>

<%@page import="java.util.Date"%>
<%@page import="com.sicap.clientes.util.Convertidor"%>
<%@page import="java.util.Vector"%>
<%@page import="com.sicap.clientes.vo.SaldoFondeadorVO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.TreeMap"%>
<%@page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@page import="com.sicap.clientes.util.Notification"%>
<%@page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%
Vector notificaciones = (Vector)request.getAttribute("NOTIFICACIONES");
TreeMap catFondeador = CatalogoHelper.getCatalogoFondeadorActivo();
ArrayList<SaldoFondeadorVO> arrSaldoFondeador = new ArrayList<SaldoFondeadorVO>();
boolean captura = false;
int idFondeador = 0;
String strFechaCob = "";
String strFechaAct = Convertidor.dateToString(new Date());
if(request.getAttribute("listaCobranza") != null){
    captura = true;
    idFondeador = HTMLHelper.getParameterInt(request, "idFondeador");
    arrSaldoFondeador = (ArrayList<SaldoFondeadorVO>) request.getAttribute("listaCobranza");
    if(!arrSaldoFondeador.isEmpty())
        strFechaCob = Convertidor.dateToString(arrSaldoFondeador.get(0).getFechaIngreso());
    if(strFechaCob.equals(strFechaAct)){
        captura = false;
    }
}
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <title>Ingreso Cobranza Fondeador</title>
        <script language="Javascript" src="./js/functions.js"></script>
        <script>
            function regresar(){
                desHabilitaBoton();
                window.document.forma.command.value='administracionFondeadores';
                window.document.forma.submit();
            }
            function buscaCobranza(){
                desHabilitaBoton();
                window.document.forma.command.value='buscaCobranza';
                if(window.document.forma.idFondeador.value == 0){
                    habilitaBoton();
                    alert("Seleccione un fondeador");
                    return false;
                }
                window.document.forma.submit();
            }
            function ingresa(){
                desHabilitaBoton();
                window.document.forma.command.value='ingresaCobranza';
                if(!esFormatoMoneda(window.document.forma.montoCobranza.value)){
                    habilitaBoton();
                    alert("Capture un monto valido");
                    return false;
                }
                if(!confirm("Deseas ingresar un monto por $ "+formatoMillones(window.document.forma.montoCobranza.value, "2",".",","))){
                    habilitaBoton();
                    return false;
                }
                window.document.forma.submit();
            }
            function desHabilitaBoton(){
                document.body.style.cursor = "wait";
                document.getElementById("btnRegresar").disabled = true;
                if(document.getElementById("btnIngresa") != null)
                    document.getElementById("btnIngresa").disabled = true;
            }
            function habilitaBoton(){
                document.body.style.cursor = "default";
                document.getElementById("btnRegresar").disabled = false;
                if(document.getElementById("btnIngresa") != null)
                    document.getElementById("btnIngresa").disabled = false;
            }
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
        <center>
        <h2>Ingreso Cobranza Fondeador</h2> 
        <form name="forma" action="admin" method="post">
            <input type="hidden" name="command" value="">
            <input type="hidden" name="listaCobranza" value="">
            <table border="0" width="40%" cellspacing="0">
                <tr>
                    <td align="center" colspan="2"><%=HTMLHelper.displayNotifications(notificaciones)%><br></td>
                </tr>
                <tr>
                    <td align="right">Fondeador:</td>
                    <td width="50%">
                        <select name="idFondeador" size="1" onchange="buscaCobranza()">
                            <%= HTMLHelper.displayCombo(catFondeador, idFondeador)%>
                        </select>
                    </td>
                </tr>
                <tr><td colspan="2">&nbsp;</td></tr>
                <tr>
                    <td align="center" colspan="2"><input type="button" id="btnRegresar" value="Regresar" onClick="regresar()"></td>
                </tr>
            </table>
            <%if(!arrSaldoFondeador.isEmpty()){%>
            <br/>
            <table border="1" width="25%" cellspacing="0">
                <tr>
                    <td align="center"><b>Fecha Ingreso</b></td>
                    <td align="center"><b>Monto</b></td>
                </tr>
                <%for(int i = arrSaldoFondeador.size()-1; i >= 0; i--){%>
                <tr>
                    <td align="center"><%=HTMLHelper.displayField(arrSaldoFondeador.get(i).getFechaIngreso())%></td>
                    <td align="right">$&nbsp;<i><%=HTMLHelper.formatoMontoMillones(arrSaldoFondeador.get(i).getMontoCobranza())%></i>&nbsp;&nbsp;</td>
                </tr>
                <%}%>
            </table>
            <%}
            if(captura){%>
            <br/>
            <table border="1" width="25%" cellspacing="0">
                <tr>
                    <td align="right"><i>Ingreso Monto:&nbsp;&nbsp;</i></td>
                    <td align="center"><input type="text" name="montoCobranza"></td>
                </tr>
                <tr>
                    <td align="right" colspan="2"><input type="button" id="btnIngresa" value="Ingresar" onClick="ingresa()"></td>
                </tr>
            </table>
            <%}%>
        </center>
        <jsp:include page="footer.jsp" flush="true"/></body>
    </body>
</html>
