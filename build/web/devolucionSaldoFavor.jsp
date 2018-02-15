<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.SaldoIBSVO"%>
<%@ page import="com.sicap.clientes.vo.OrdenDePagoVO"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.*"%>

<%
    Vector notificaciones = (Vector)request.getAttribute("NOTIFICACIONES");
    SaldoIBSVO saldoFavor = (SaldoIBSVO)request.getAttribute("SALDOAFAVOR");
    OrdenDePagoVO ordenPago = (OrdenDePagoVO)request.getAttribute("ORDENDEPAGO");
    int numEquipo = HTMLHelper.getParameterInt(request, "numEquipo");
    int numCiclo = HTMLHelper.getParameterInt(request, "numCiclo");
    TreeMap catBancos = CatalogoHelper.getCatalogoBancosDispersion();
%>
<html>
    <head>
        <title>Devolución de Saldo a Favor</title>
        <script language="Javascript" src="./js/functions.js"></script>
        <script type="text/javascript">
            function consultaSaldoFavor(){
                if (window.document.forma.numEquipo.value==0||window.document.forma.numEquipo.value==''|| !esEntero(window.document.forma.numEquipo.value)){
                    alert('Número de equipo inválido');
                    return false;
		}
                if (window.document.forma.numCiclo.value==0||window.document.forma.numCiclo.value==''|| !esEntero(window.document.forma.numCiclo.value)){
                    alert('Número de ciclo inválido');
                    return false;
		}
		window.document.forma.command.value='consultaSaldoFavor';
		window.document.forma.submit();
            }
            function guardaOrdenSaldoFavor(){
                if (window.document.forma.beneficiario.value==0){
                    alert('Seleccione un beneficiario');
                    return false;
		}
                if(confirm("¿Está seguro de generar la Orden de Pago?")){
                    window.document.forma.command.value="guardaOrdenPagoSaldoFavor";
                    window.document.forma.submit();
                }
            }            
            function nuevaBusqueda(){
                window.document.forma.command.value="devolucionSaldoFavor";
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }
            function muestraDocumento(tipo){
                window.document.forma.command.value='muestraDocumento';
                window.document.forma.tipo.value=tipo;
                window.document.forma.submit();
            }
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
        <center>
            <h2>Devolución de Saldo a Favor</h2>
            <%=HTMLHelper.displayNotifications(notificaciones)%>
            <form action="controller" method="post" name="forma">
                <input type="hidden" name="command" value="">
                <input type="hidden" name="tipo" value="">
                <%if ( saldoFavor != null){
                    TreeMap catBeneficiarios = CatalogoHelper.getCatalogoBeneficiariosSaldoFavor(numEquipo, numCiclo);
                %>
                <input type="hidden" name="numEquipo" value="<%=numEquipo%>">
                <input type="hidden" name="numCiclo" value="<%=numCiclo%>">
                <table border="0" width="90%" align="center" >
                    <tr>
                        <td width="50%" align="right"><strong>Nombre del equipo </strong></td>
                        <td width="50%"><input type="text" name="nombreEquipo" value="<%=saldoFavor.getNombreCliente() %>" readonly="readonly" class="soloLectura"/></td>
                    </tr>
                    <tr>
                        <td width="50%" align="right"><strong>Saldo a Favor </strong></td>
                        <td width="50%"><input type="text" name="montoSaldoFavor" size="7" maxlength="7" value="<%=saldoFavor.getSaldoBucket()%>"readonly="readonly" class="soloLectura"/></td>
                    </tr>
                    <tr>
                        <td width="49%" align="right"><strong>Banco</strong></td>
                        <td width="51%">
                            <select name="idBanco" id="idBanco" readOnly="readOnly">
                                <%=HTMLHelper.displayCombo(catBancos, 0)%>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td width="49%" align="right"><strong>Beneficiario</strong></td>
                        <td width="51%">
                            <select name="beneficiario" id="beneficiario" readOnly="readOnly" onchange="">
                                <%=HTMLHelper.displayCombo(catBeneficiarios, 0)%>
                            </select>
                        </td>
                    </tr>
                    <tr>
                        <td align="right">
                            <br>
                            <input type="button" name="" value="Generar Orden de Pago" onClick="guardaOrdenSaldoFavor()">
                            <br>
                            <br>
                        </td>
                        <td align="left">
                            <br>
                            <input type="button" name="" value="Nueva Consulta" onClick="nuevaBusqueda()">
                            <br>
                            <br>
                        </td>
                    </tr>
                </table>
                <%} else if ( ordenPago != null){
                    String nombreEquipo = (String)request.getAttribute("NOMBREEQUIPO");
                %>
                <input type="hidden" name="referencia" value="<%=ordenPago.getReferencia()%>">
                <table border="0" width="90%" align="center" >
                    <tr>
                        <td width="50%" align="right"><strong>Nombre del equipo </strong></td>
                        <td width="50%"><input type="text" name="nombreEquipo" value="<%=nombreEquipo %>" readonly="readonly" class="soloLectura"/></td>
                    </tr>
                    <tr>
                        <td width="50%" align="right"><strong>Saldo a Favor </strong></td>
                        <td width="50%"><input type="text" name="montoSaldoFavor" size="7" maxlength="7" value="<%=ordenPago.getMonto() %>"readonly="readonly" class="soloLectura"/></td>
                    </tr>
                    <tr>
                        <td width="49%" align="right"><strong>Banco</strong></td>
                        <td width="50%"><input type="text" name="nombreBanco" value="<%=HTMLHelper.getDescripcion(catBancos, ordenPago.getIdBanco()) %>" readonly="readonly" class="soloLectura"/></td>
                    </tr>
                    <tr>
                        <td width="49%" align="right"><strong>Beneficiario</strong></td>
                        <td width="50%"><input type="text" name="nombreBeneficiario" size="50" value="<%=ordenPago.getNombre() %>" readonly="readonly" class="soloLectura"/></td>
                    </tr>
                    <tr>
                        <td align="right">
                            <br>
                            <input type="button" name="" value="Consulta Orden de Pago" onClick="muestraDocumento('ORDENPAGOSALDOFAVOR')">
                            <br>
                            <br>
                        </td>
                        <td align="left">
                            <br>
                            <input type="button" name="" value="Nueva Consulta" onClick="nuevaBusqueda()">
                            <br>
                            <br>
                        </td>
                    </tr>
                </table>
                <%} else {%>
                <table border="0" width="90%" align="center" >
                    <tr>
                        <td width="50%" align="right">Número de Equipo</td>
                        <td width="50%"><input type="text" name="numEquipo" size="7" maxlength="7" value="" /></td>
                    </tr>
                    <tr>
                        <td width="50%" align="right">Número de Ciclo</td>
                        <td width="50%"><input type="text" name="numCiclo" size="7" maxlength="7" value="" /></td>
                    </tr>
                    <tr>
                        <td colspan="2" align="center">
                            <br>
                            <input type="button" name="" value="Consultar" onClick="consultaSaldoFavor();" >
                            <br>
                            <br>
                        </td>
                    </tr>
                </table>
             <%}%>
            </form>
        </center>
        <jsp:include page="footer.jsp" flush="true"/>
    </body>
</html>