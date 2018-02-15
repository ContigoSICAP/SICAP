<%-- 
    Document   : ajusteCredito
    Created on : 17/07/2012, 08:31:44 AM
    Author     : Alex
--%>

<%@page import="com.sicap.clientes.vo.SaldoIBSVO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.TreeMap"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
        <title>Mantenimiento de Cierre</title>
        <script>
            function ReprocesarCierre(){
                document.getElementById("td").disabled = true;
                window.document.forma.command.value = 'ajustarCredito';
                if(window.document.forma.estatus.checked == true){
                    if(window.document.forma.tipoEstatus.value != 0){
                        if(confirm("Desea ajustar los creditos con el tipo de estatus seleccionado")){
                            document.body.style.cursor = "wait";
                            window.document.forma.submit();
                        }
                    } else{
                        alert("Es necesario seleccionar un Estatus")
                    }
                }else{
                    document.body.style.cursor = "wait";
                    window.document.forma.submit();
                }
            }
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css"/>
    </head>
    <%
    TreeMap catEstatus = CatalogoHelper.getCatalogoEstatusCredito();
    Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
    ArrayList<SaldoIBSVO> arrSaldos = (ArrayList<SaldoIBSVO>)request.getAttribute("saldos");
    %>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
        <form name="forma" action="admin" method="post">
            <input type="hidden" name="command" value=""/>
            <input type="hidden" name="saldos" value=""/>
            <table border="0" width="25%" align="center" cellpadding="5">
                <tr>
                    <td align="center" colspan="2"><h3>Ajuste de Cierre</h3>
                        <%=HTMLHelper.displayNotifications(notificaciones)%></td>
                </tr>
                <tr>
                    <td align="right">Grupo:</td>
                    <td align="left"><input size="15" type="text" name="idGrupo" value="0"/></td>
                </tr>
                <tr>
                    <td align="right">Ciclo:</td>
                    <td align="left"><input size="15" type="text" name="numCiclo" value="0"/></td>
                </tr>
                <tr>
                    <td align="right">Multas a Condonar:</td>
                    <td align="left"><input size="15" type="text" name="numMultas" value="0"/></td>
                </tr>
                <tr>
                    <td align="right">&nbsp;</td>
                    <td align="left"><input type="checkbox" name="estatus" id="estatus"/>&nbsp;Aplicar a los Creditos</td>
                </tr>
                <tr>
                    <td align="right">Estatus:</td>
                    <td align="left">
                        <select name="tipoEstatus">
                            <%=HTMLHelper.displayCombo(catEstatus, 0)%>
                        </select>
                    </td>
                </tr>
                <tr>
                    <td align="right">Fecha&nbsp;Ajuste</td>
                    <td align="left"><input size="10" type="text" name="Fecha" id="Fecha" value="" onKeyPress="return submitenter(this,event)"/>&nbsp;(aaaa-mm-dd)</td>
                </tr>
                <tr>
                    <td colspan="2" align="center" id="td"><input type="button" value="Ajustar" onClick="ReprocesarCierre()"/></td>
                </tr>
            </table>
                        <br/>
                        <br/>
            <%if(arrSaldos != null){%>
            <table border="1" align="center" cellpadding="0">
                <tr align="center">
                    <td width="50">Grupo</td>
                    <td width="50">Ciclo</td>
                    <td width="100">Saldo del Credito</td>
                    <td width="100">Multas a Pagar</td>
                    <td width="100">Multas Pagadas</td>
                    <td width="100">Saldo Vencido</td>
                    <td width="100">Saldo Favor</td>
                    <td width="70">Estatus</td>
                </tr>
                <%for(SaldoIBSVO saldo : arrSaldos){%>
                    <tr>
                        <td align="center"><%=HTMLHelper.displayField(saldo.getIdClienteSICAP())%></td>
                        <td align="center"><%=HTMLHelper.displayField(saldo.getIdSolicitudSICAP())%></td>
                        <td align="right"><%=HTMLHelper.formatCantidad(saldo.getSaldoConInteresAlFinal())%></td>
                        <td align="right"><%=HTMLHelper.formatCantidad(saldo.getSaldoMulta()+saldo.getSaldoIVAMulta())%></td>
                        <td align="right"><%=HTMLHelper.formatCantidad(saldo.getMultaPagada()+saldo.getIvaMultaPagado())%></td>
                        <td align="right"><%=HTMLHelper.formatCantidad(saldo.getTotalVencido())%></td>
                        <td align="right"><%=HTMLHelper.formatCantidad(saldo.getSaldoBucket())%></td>
                        <td align="center"><%=HTMLHelper.displayField(CatalogoHelper.getDescripcionEstatusCredito(saldo.getEstatus(), catEstatus))%></td>
                    </tr>
                <%}%>
            </table>
            <%}%>
        </form>
        <jsp:include page="footer.jsp" flush="true"/>
    </body>
</html>
