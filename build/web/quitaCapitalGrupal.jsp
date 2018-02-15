<%@page import="com.sicap.clientes.dao.CatalogoDAO"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.util.Notification"%>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.vo.cartera.TablaAmortVO"%>
<%@ page import="com.sicap.clientes.vo.cartera.CreditoCartVO"%>
<%@ page import="com.sicap.clientes.vo.GrupoVO"%>
<%@ page import="com.sicap.clientes.vo.CicloGrupalVO"%>
<%@ page import="com.sicap.clientes.util.ClientesConstants"%>
<html>
    <head>
        <title>Quita Capital Vencido</title>
        <script>
            function muestraDividendo() {
                if (window.document.forma.idGrupo.value == '' || window.document.forma.idCiclo.value == '') {
                    alert("Debe especificar grupo y ciclo");
                    return false;
                }
                window.document.forma.submit();
            }

            function redireccionMenuAdmin() {
                window.document.forma.command.value = 'administracionPagos';
                window.document.forma.submit();
            }

            function quitaCapital(numDividendos) {

                var existeReferenciaParaReprocesar = 'noexisteReferencia';
                var i;

                if (numDividendos == 1) {
                    if (window.document.forma.dividendos.checked) {
                        existeReferenciaParaReprocesar = 'existeReferencia';
                    }

                } else if (numDividendos > 1) {

                    for (i = 0; i < window.document.forma.dividendos.length; i++) {
                        var temp = window.document.forma.dividendos[i].checked;
                        if (window.document.forma.dividendos[i].checked) {
                            existeReferenciaParaReprocesar = 'existeReferencia';
                            break;
                        }
                    }
                }


                if (existeReferenciaParaReprocesar == 'noexisteReferencia') {
                    alert("No ha seleccionado ningún dividendo para quita");
                    return false;
                } else {

                    if (confirm("¿Va a realizar quita de estos dividendos?"))
                    {
                        window.document.forma.command.value = 'quitaCapital';
                        window.document.forma.submit();
                    }

                }
            }

        </script>

        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>

    <%Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");
        int idGrupo = 0;
        int idCiclo = 0;
        int idCredito = 0;
        boolean sinMora = true;

        idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
        idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
        idCredito = HTMLHelper.getParameterInt(request, "idCredito");
        CatalogoDAO catalogoDao = new CatalogoDAO();
        CicloGrupalVO ciclo = new CicloGrupalVO();
        TablaAmortVO[] tablaAmort = null;
        TablaAmortVO tabla = new TablaAmortVO();
        TablaAmortVO tablaVer = new TablaAmortVO();
        double totalMora = 0;

        GrupoVO grupo = new GrupoVO();

        if (request.getAttribute("DIVIDENDOS") != null) {
            tablaAmort = (TablaAmortVO[]) request.getAttribute("DIVIDENDOS");
            ciclo = (CicloGrupalVO) request.getAttribute("CICLO");
            for (int j = 0; j < tablaAmort.length; j++) {
                tablaVer = tablaAmort[j];
                totalMora = totalMora + (tablaVer.intMoratorio - tablaVer.intMoratorioPagado + tablaVer.ivaIntMoratorio - tablaVer.intMoratorioPagado
                        + tablaVer.interes - tablaVer.interesPagado + tablaVer.ivaInteres - tablaVer.ivaInteresPagado);
            }
            if (totalMora > 0) {
                sinMora = false;
            }
            notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Se debe antes de condonar mora e interes vencido");

        }


    %>

    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
    <center><h3>Quita Capital Vencido<br></h3></center>

    <form name="forma" action="admin" method="POST">
        <input type="hidden" name="command" value="listarDividendosCapitalVen">
        <center><%=HTMLHelper.displayNotifications(notificaciones)%></center>
        <div><table border="0" width="100%" cellspacing="0" align="center" height="10%">
                <tr>
                    <td width="50%" height="10%" align="right">Grupo<br></td>
                        <%if (idGrupo > 0) {
                        %>
                    <td width="50%"><input type="text" name="idGrupo" size="10" maxlength="10" value="<%=HTMLHelper.displayField(idGrupo)%>"></td>
                        <%} else {
                        %>      
                    <td width="50%"><input type="text" name="idGrupo" size="10" maxlength="10" value="<%=HTMLHelper.displayField(idGrupo)%>"></td>
                        <%}
                        %>      
                </tr>

                <tr>
                    <td width="50%" height="10%" align="right">Ciclo<br></td>
                        <%if (idCiclo > 0) {
                        %>
                    <td width="50%"><input type="text" name="idCiclo" size="3" maxlength="3" value="<%=HTMLHelper.displayField(idCiclo)%>"></td>
                        <%} else {
                        %>      
                    <td width="50%"><input type="text" name="idCiclo" size="3" maxlength="3" value="<%=HTMLHelper.displayField(idCiclo)%>"></td>
                        <%}
                        %>      
                </tr>
                <% 	if (tablaAmort != null && sinMora) {
                %>
                <input type="hidden" name ="idCredito" value="<%=HTMLHelper.displayField(ciclo.idCreditoIBS)%>">									
                <input type="hidden" name ="idCiclo" value="<%=HTMLHelper.displayField(ciclo.idCiclo)%>">									

                <tr>

                    <td colspan="2">
                        <br>
                        <table width="100%" border="0">
                            <tr bgcolor="#009865">
                                <td class="whitetext" align="center">Num Cliente</td>
                                <td class="whitetext" align="center">Num Credito</td>
                                <td class="whitetext" align="center">Fecha Pago</td>
                                <td class="whitetext" align="center">Num Dividendo</td>
                                <td class="whitetext" align="center">Rubro</td>
                                <td class="whitetext" align="center">Saldo Capital</td>
                                <td class="whitetext" align="center">Quita</td>
                            </tr>
                            <%
                                for (int i = 0; i < tablaAmort.length; i++) {
                                    tabla = tablaAmort[i];
                            %>
                            <tr>
                                <td align="center">
                                    <%=tablaAmort[i].numCliente%>
                                </td>
                                <td align="center">
                                    <%=tablaAmort[i].numCredito%>									
                                </td>								
                                <td align="center">
                                    <%=HTMLHelper.displayField(tablaAmort[i].fechaPago)%>
                                </td>
                                <td align="center"> 
                                    <%=tabla.numPago%>
                                </td>		
                                <td align="center">
                                    Capital
                                </td>
                                <td align="right">								
                                    <%=HTMLHelper.formatoMonto(tabla.abonoCapital - tabla.capitalPagado)%>
                                </td>
                                <td align="center">
                                    <input type="checkbox" name ="dividendos" value="<%=HTMLHelper.displayField(tablaAmort[i].numPago)%>">									
                                </td>

                            </tr>
                            <%
                                }
                            %>
                        </table>

                    </td>				
                </tr>
                <tr>
                    <td colspan="3" height="10%" align="center">
                        <%if(!catalogoDao.ejecutandoCierre()){%>
                        <input type="button" value="Quita" onclick="quitaCapital(<%=tablaAmort.length%>);">
                        <%}%>
                    </td>
                </tr>
                <%}
                %>

                <tr>
                    <td colspan="3" height="10%" align="center">
                        <input type="button" value="Consulta Dividendo" onclick="muestraDividendo();">
                        <input type="button" value="Regresar" onClick="redireccionMenuAdmin()">
                    </td>
                </tr>
            </table>
        </div>
    </form>

    <%@include file="footer.jsp"%></body>
</html>

