<%-- 
    Document   : pagosManuales
    Created on : 6/03/2014, 07:48:29 AM
    Author     : avillanueva
--%>

<%@page import="com.sicap.clientes.dao.CatalogoDAO"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.TreeMap"%>
<%@page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@page import="com.sicap.clientes.vo.PagoVO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.sicap.clientes.util.Notification"%>
<%@page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp"%>

<%
Vector notificaciones = (Vector)request.getAttribute("NOTIFICACIONES");
ArrayList<PagoVO> arrPagos = new ArrayList<PagoVO>();
int numPagos = 0;
int i = 0;
TreeMap catBancos = null;
if(!request.getAttribute("PAGOS").equals("")){
    arrPagos = (ArrayList<PagoVO>)request.getAttribute("PAGOS");
    numPagos = arrPagos.size();
    catBancos = CatalogoHelper.getCatalogo("C_BANCOS");
}
CatalogoDAO catalogoDao = new CatalogoDAO();
%>

<html>
    <head>
        <title>Ingreso de Pagos Manual</title>
        <script type="text/javascript">
            function seleccionaTodo(){
                var checa = 1;
                if(window.document.forma.checkGeneral.checked == false)
                    checa = 0;
                for (var i = 0; i<window.document.forma.numRegistros.value; i++){
                    if(window.document.forma.numRegistros.value == 1)
                        window.document.forma.idCheckBox.checked=checa;
                    else
                        window.document.forma.idCheckBox[i].checked=checa;
                }
            }
            function aplicaPagos(){
                window.document.forma.command.value='aplicaPagosManual';
		document.body.style.cursor = "wait";
                window.document.forma.submit();
            }
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css"/>
    </head>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
        <form name="forma" action="admin" method="post">
            <input type="hidden" name="command" value="">
            <table border="0" width="25%" align="center" cellpadding="5">
                <tr>
                    <td align="center" colspan="8"><h3>Ingreso de Pagos Manuales</h3>
                        <%=HTMLHelper.displayNotifications(notificaciones)%></td>
                </tr>
                <tr bgcolor="#009865">
                    <td width="2%" align="center"><input type="checkbox" name="checkGeneral" id="checkGeneral" onclick="seleccionaTodo()" checked/></td>
                    <td class="whitetext" align="center">Equipo</td>
                    <td class="whitetext" align="center">Ciclo</td>
                    <td class="whitetext" align="center">Referncia</td>
                    <td class="whitetext" align="center">Monto</td>
                    <td class="whitetext" align="center">Fecha Pago</td>
                    <td class="whitetext" align="center">Banco</td>
                </tr>
                <%if(numPagos!=0){%>
                    <%for(PagoVO pago : arrPagos) {%>
                <tr>
                    <td align="center"><input type="checkbox" name="idCheckBox" id="idCheckBox" value="<%=i%>" checked=""/></td>
                    <td align="left"><%=HTMLHelper.displayField(pago.getReferencia().substring(5, 10))%></td>
                    <td align="center"><%=HTMLHelper.displayField(pago.getReferencia().substring(11, 12))%></td>
                    <td align="center"><%=HTMLHelper.displayField(pago.getReferencia())%></td>
                    <td align="right"><%=HTMLHelper.formatoMontoDouble(pago.getMonto())%></td>
                    <td align="center"><%=HTMLHelper.displayField(pago.getFechaPago())%></td>
                    <td align="center"><%=HTMLHelper.displayField(catBancos.get(pago.getBancoReferencia()).toString())%></td>
                    
                    <input type="hidden" name="referencia<%=i%>" id="referencia<%=i%>" value="<%=pago.getReferencia()%>">
                    <input type="hidden" name="monto<%=i%>" id="monto<%=i%>" value="<%=pago.getMonto()%>">
                    <input type="hidden" name="fechaPago<%=i%>" id="fechaPago<%=i%>" value="<%=pago.getFechaPago()%>">
                    <input type="hidden" name="fechaHora<%=i%>" id="fechaHora<%=i%>" value="<%=pago.getFechaHora()%>">
                    <input type="hidden" name="banco<%=i%>" id="banco<%=i%>" value="<%=pago.getBancoReferencia()%>">
                    <input type="hidden" name="numRegistro<%=i%>" id="numRegistro<%=i%>" value="<%=pago.getNumRegistro()%>">
                    <input type="hidden" name="cuenta<%=i%>" id="cuenta<%=i%>" value="<%=pago.getNumCuenta()%>">
                </tr>
                        <%i++;
                    }%>
                <tr>
                    <td height="46" colspan="8" align="right">
                    <%if(!catalogoDao.ejecutandoCierre()){%>
                        <label><input name="getFile" type="button" class="text" id="getFile" onClick="aplicaPagos()" value="Aplica Pagos"></label>
                    <%}%>
                    </td>
                </tr>
                <%}%>
                <input type="hidden" name="numRegistros" id="numRegistros" value="<%=i%>"/>
            </table>
        </form>
        <jsp:include page="footer.jsp" flush="true"/></body>
    </body>
</html>