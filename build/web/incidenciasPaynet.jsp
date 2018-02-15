<%@page import="java.util.Vector"%>
<%@page import="java.util.TreeMap"%>
<%@page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@page import="com.sicap.clientes.vo.PaynetVO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.sicap.clientes.util.Notification"%>
<%@page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp"%>

<%
Vector notificaciones = (Vector)request.getAttribute("NOTIFICACIONES");
ArrayList<PaynetVO> arrTrans = new ArrayList<PaynetVO>();
int numTrans = 0;
int i = 0;
TreeMap catIncidencia = null;
if(!request.getAttribute("TRANSACCIONES").equals("")){
    arrTrans = (ArrayList<PaynetVO>)request.getAttribute("TRANSACCIONES");
    numTrans = arrTrans.size();
    catIncidencia = CatalogoHelper.getCatalogoGeneral(72);
}
%>

<html>
    <head>
        <title>Incidencias Paynet</title>
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
            function envioPago(totalTrans){
                window.document.forma.command.value='envioPagoPaynet';
                deshabilita();
                var numTrans = verificaEstatus(totalTrans, 1);
                if(numTrans > 0){
                    if(confirm("Desea Procesar las "+numTrans+" transacciones?"))
                        window.document.forma.submit();
                    else
                        habilita();
                } else{
                    alert("Seleccione una Transaccion o Verifique su seleccion.");
                    habilita();
                }
            }
            function confPago(totalTrans){
                window.document.forma.command.value='confirmaPagoPaynet';
		deshabilita();
                var numTrans = verificaEstatus(totalTrans, 3);
                if(numTrans > 0){
                    if(confirm("Desea Procesar las "+numTrans+" transacciones?"))
                        window.document.forma.submit();
                    else
                        habilita();
                } else{
                    alert("Seleccione una Transaccion o Verifique su seleccion.");
                    habilita();
                }
            }
            function confCan(totalTrans){
                window.document.forma.command.value='confirmaCancelaPaynet';
		deshabilita();
                var numTrans = verificaEstatus(totalTrans, 2);
                if(numTrans > 0){
                    if(confirm("Desea Procesar las "+numTrans+" transacciones?"))
                        window.document.forma.submit();
                    else
                        habilita();
                } else{
                    alert("Seleccione una Transaccion o Verifique su seleccion.");
                    habilita();
                }
            }
            function regresar(){
                window.document.forma.command.value='buscaIncidenciasPaynet';
		document.body.style.cursor = "wait";
                window.document.forma.submit();
            }
            function verificaEstatus(totalTrans, estatus){
                var numtran = 0;
                for(var i = 0; i<totalTrans; i++){
                    var estatusDinamico = "estatus"+i;
                    if(totalTrans == 1){
                        if(window.document.forma.idCheckBox.checked == true){
                            if(window.document.getElementById(estatusDinamico).value == estatus){
                                numtran++;
                            }else{
                                numtran = 0;
                                break;
                            }
                        }
                    }else{
                        if(window.document.forma.idCheckBox[i].checked == true){
                            if(window.document.getElementById(estatusDinamico).value == estatus){
                                numtran++;
                            }else{
                                numtran = 0;
                                break;
                            }
                        }
                    }
                }
                return numtran;
            }
            function habilita(){
                document.getElementById("getEnvioPago").disabled = false;
                document.getElementById("getConfPago").disabled = false;
                document.getElementById("getConfCan").disabled = false;
                document.getElementById("getRegesa").disabled = false;
                document.body.style.cursor = "default";
            }
            function deshabilita(){
                document.getElementById("getEnvioPago").disabled = true;
                document.getElementById("getConfPago").disabled = true;
                document.getElementById("getConfCan").disabled = true;
                document.getElementById("getRegesa").disabled = true;
                document.body.style.cursor = "wait";
            }
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css"/>
    </head>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
        <form name="forma" action="admin" method="post">
            <input type="hidden" name="command" value="">
            <table border="0" align="center" cellpadding="5">
                <tr>
                    <td align="center" colspan="8"><h3>Incidencias Transacciones Paynet</h3>
                        <%=HTMLHelper.displayNotifications(notificaciones)%></td>
                </tr>
                <tr bgcolor="#009865">
                    <td align="center"><input type="checkbox" name="checkGeneral" id="checkGeneral" onclick="seleccionaTodo()" checked/></td>
                    <td class="whitetext" align="center">Equipo</td>
                    <td class="whitetext" align="center">Nombre</td>
                    <td class="whitetext" align="center">Ciclo</td>
                    <td class="whitetext" align="center">Referencia</td>
                    <td class="whitetext" align="center">Referencia Paynet</td>
                    <td class="whitetext" align="center">Monto</td>
                    <td class="whitetext" align="center">Fecha Pago</td>
                    <td class="whitetext" align="center">Incidencia</td>
                </tr>
                <%if(numTrans!=0){%>
                    <%for(PaynetVO paynetVO : arrTrans) {%>
                <tr>
                    <td align="center"><input type="checkbox" name="idCheckBox" id="idCheckBox" value="<%=i%>" checked=""/></td>
                    <td align="left"><%=HTMLHelper.displayField(paynetVO.getRefGeneralVO().numcliente)%></td>
                    <td align="left"><%=HTMLHelper.displayField(paynetVO.getRefGeneralVO().nombre)%></td>
                    <td align="center"><%=HTMLHelper.displayField(paynetVO.getRefGeneralVO().numSolicitud)%></td>
                    <td align="center"><%=HTMLHelper.displayField(paynetVO.getRefGeneralVO().referencia)%></td>
                    <td align="center"><%=HTMLHelper.displayField(paynetVO.getReferenciaPay())%></td>
                    <td align="right"><%=HTMLHelper.formatoMontoDouble(paynetVO.getMonto())%></td>
                    <td align="right"><%=HTMLHelper.displayField(paynetVO.getFechaAutPagPay())%></td>
                    <td align="center"><%=HTMLHelper.getDescripcion(catIncidencia, paynetVO.getEstatus())%></td>
                    
                    <input type="hidden" name="idPago<%=i%>" id="idPago<%=i%>" value="<%=paynetVO.getIdPago()%>">
                    <input type="hidden" name="numEquipo<%=i%>" id="numEquipo<%=i%>" value="<%=paynetVO.getRefGeneralVO().numcliente%>">
                    <input type="hidden" name="nombre<%=i%>" id="nombre<%=i%>" value="<%=paynetVO.getRefGeneralVO().nombre%>">
                    <input type="hidden" name="numSolicitud<%=i%>" id="numSolicitud<%=i%>" value="<%=paynetVO.getRefGeneralVO().numSolicitud%>">
                    <input type="hidden" name="referencia<%=i%>" id="referencia<%=i%>" value="<%=paynetVO.getRefGeneralVO().referencia%>">
                    <input type="hidden" name="referenciaPay<%=i%>" id="referenciaPay<%=i%>" value="<%=paynetVO.getReferenciaPay()%>">
                    <input type="hidden" name="monto<%=i%>" id="monto<%=i%>" value="<%=paynetVO.getMonto()%>">
                    <input type="hidden" name="fechaAutPagPay<%=i%>" id="fechaAutPagPay<%=i%>" value="<%=paynetVO.getFechaAutPagPay()%>">
                    <input type="hidden" name="fechaAutMovPay<%=i%>" id="fechaAutMovPay<%=i%>" value="<%=paynetVO.getFechaAutMovPay()%>">
                    <input type="hidden" name="estatus<%=i%>" id="estatus<%=i%>" value="<%=paynetVO.getEstatus()%>">
                </tr>
                        <%i++;
                    }%>
                <tr>
                    <td colspan="8" align="right">
                        <label><input name="getFile" type="button" class="text" id="getEnvioPago" onClick="envioPago(<%=numTrans%>)" value="Envio a Pagos"></label>
                        <label><input name="getFile" type="button" class="text" id="getConfPago" onClick="confPago(<%=numTrans%>)" value="Confir. Pago"></label>
                        <label><input name="getFile" type="button" class="text" id="getConfCan" onClick="confCan(<%=numTrans%>)" value="Confir. Cancelaci&oacute;n"></label>
                        <label><input name="getFile" type="button" class="text" id="getRegesa" onClick="regresar(<%=numTrans%>)" value="Regresar"></label>
                    </td>
                </tr>
                <%}%>
                <input type="hidden" name="numRegistros" id="numRegistros" value="<%=i%>"/>
            </table>
        </form>
        <jsp:include page="footer.jsp" flush="true"/></body>
    </body>
</html>