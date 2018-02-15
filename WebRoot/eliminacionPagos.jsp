<%-- 
    Document   : eliminacionPagos
    Created on : 14/03/2014, 10:27:25 AM
    Author     : avillanueva
--%>

<%@page import="com.sicap.clientes.dao.CatalogoDAO"%>
<%@page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@page import="java.util.TreeMap"%>
<%@page import="com.sicap.clientes.vo.PagoVO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.sicap.clientes.util.Notification"%>
<%@page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp"%>
<!DOCTYPE html>
<%
Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
String comando = request.getParameter("command");
ArrayList<PagoVO> arrPagos = new ArrayList<PagoVO>();
int numPagos = 0;
int i = 0;
TreeMap catBancos = new TreeMap();
String ref = "";
int numEquipo = 0, numCiclo = 0;
if(comando.equals("buscaPagosEliminar")){
    ref = String.valueOf(request.getAttribute("REFERENCIA"));
    arrPagos = (ArrayList<PagoVO>)request.getAttribute("PAGOS");
    numPagos = arrPagos.size();
    catBancos = CatalogoHelper.getCatalogo("C_BANCOS");
    numEquipo = (Integer)request.getAttribute("GRUPO");
    numCiclo = (Integer)request.getAttribute("CICLO");
}
CatalogoDAO catalogoDao = new CatalogoDAO();
%>
<html>
    <head>
        <title>Busca Pagos Eliminar</title>
        <script type="text/javascript">
            function BuscaPagos(){
                document.body.style.cursor = "wait";
                window.document.forma.command.value = 'buscaPagosEliminar';
                if(window.document.forma.idGrupo.value == 0){
                    document.body.style.cursor = "default";
                    alert("Ingrese el Numero del Grupo");
                    return false;
                }
                if(window.document.forma.idCiclo.value == 0){
                    document.body.style.cursor = "default";
                    alert("Ingrese el Numero del Ciclo");
                    return false;
                }
                window.document.forma.submit();
            }
            function seleccionaTodo(){
                var checa = 1;
                if(window.document.forma.checkGeneral.checked == false)
                    checa = 0;
                for (var i = 0; i<window.document.forma.numPagos.value; i++){
                    if(window.document.forma.numPagos.value == 1)
                        window.document.forma.idCheckBox.checked=checa;
                    else
                        window.document.forma.idCheckBox[i].checked=checa;
                }
            }
            function ElimiarPagos(){
                var pasa = false;
                var verifica = false;
                window.document.forma.command.value='eliminaPagos';
                document.getElementById("Eliminar").disabled = true;
		document.body.style.cursor = "wait";
                for(var i = 0; i<window.document.forma.numPagos.value; i++){
                    if(window.document.forma.numPagos.value == 1)
                        verifica = window.document.forma.idCheckBox.checked;
                    else
                        verifica = window.document.forma.idCheckBox[i].checked;
                    if(verifica){
                        pasa = true;
                    }
                }
                if(pasa){
                    if(confirm("¿Deseas confirmas la eliminacion de los pagos?.")){
                        window.document.forma.submit();
                    }else{
                        document.getElementById("Eliminar").disabled = false;
                        document.body.style.cursor = "default";
                    }
                }else{
                    alert("Debe seleccionar algun pago");
                    document.body.style.cursor = "default";
                }
            }
            function regresar() {
                window.document.forma.command.value='pagosReferenciados';
                window.document.forma.submit();
            }
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css"/>
    </head>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
        <form name="forma" action="admin" method="post">
            <input type="hidden" name="command" value=""/>
            <table border="0" width="25%" align="center" cellpadding="5">
                <tr>
                    <td align="center" colspan="2"><h3>Buca Pagos a Eliminar</h3>
                        <%=HTMLHelper.displayNotifications(notificaciones)%></td>
                </tr>
                <tr>
                    <td align="right">Grupo:</td>
                    <td align="left"><input size="15" type="text" name="idGrupo" value="<%=numEquipo%>"/></td>
                </tr>
                <tr>
                    <td align="right">Ciclo:</td>
                    <td align="left"><input size="15" type="text" name="idCiclo" value="<%=numCiclo%>"/></td>
                </tr>
                <tr>
                    <td colspan="2" align="center" id="td"><input type="button" value="Buscar" onClick="BuscaPagos()"/></td>
                </tr>
            </table>
                <%if(comando.equals("buscaPagosEliminar")){%>
                <br><br>
                <input type="hidden" name="referencia" id="referencia" value="<%=ref%>">
            <table border="1" width="35%" align="center" cellpadding="5">
                <tr bgcolor="#009865">
                    <td width="2%" align="center"><input type="checkbox" name="checkGeneral" id="checkGeneral" onclick="seleccionaTodo()" checked/></td>
                    <td class="whitetext" align="center">Fecha de Dep&oacute;sito</td>
                    <td class="whitetext" align="center">Monto</td>
                    <td class="whitetext" align="center">Banco</td>
                    <td class="whitetext" align="center">Aplicado</td>
                </tr>
                    <%if(numPagos!=0){%>
                        <%for(PagoVO pago : arrPagos) {%>
                <tr>
                    <td align="center"><input type="checkbox" name="idCheckBox" id="idCheckBox" value="<%=i%>" checked=""/></td>
                    <td align="center"><%=HTMLHelper.displayField(pago.getFechaPago())%></td>
                    <td align="right">$<%=HTMLHelper.formatoMonto(pago.getMonto())%></td>
                    <td align="center"><%=HTMLHelper.displayField(catBancos.get(pago.getBancoReferencia()).toString())%></td>
                    <td align="left"><%=(pago.getEnviado() == 0 ? "Sin Aplicar" : "Aplicado")%></td>
                    
                    <input type="hidden" name="fechaPago<%=i%>" id="fechaPago<%=i%>" value="<%=pago.getFechaPago()%>">
                    <input type="hidden" name="monto<%=i%>" id="monto<%=i%>" value="<%=pago.getMonto()%>">
                    <input type="hidden" name="banco<%=i%>" id="banco<%=i%>" value="<%=pago.getBancoReferencia()%>">
                    <input type="hidden" name="enviado<%=i%>" id="enviado<%=i%>" value="<%=pago.getEnviado()%>">
                    <input type="hidden" name="cuenta<%=i%>" id="cuenta<%=i%>" value="<%=pago.getNumCuenta()%>">
                </tr>
                            <%i++;
                        }%>
                    <input type="hidden" name="numPagos" id="numPagos" value="<%=i%>"/>
            </table>
                        <%if(!catalogoDao.ejecutandoCierre()){%>
            <table border="0" width="35%" align="center" cellpadding="5">
                <tr align="right">
                    <td>
                        <input type="button" align="center" value="Eliminar" id="Eliminar" onClick="ElimiarPagos()"/>
                        <input type="button" id="botonRegresar" onclick="regresar()" value="Regresar">
                    </td>
            </table>
                        <%}%>
                    <%}%>
            <br>
                <%}%>
        </form>
        <jsp:include page="footer.jsp" flush="true"/>
    </body>
</html>
