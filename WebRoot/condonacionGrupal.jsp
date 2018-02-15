<%@page import="com.sicap.clientes.dao.CatalogoDAO"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.util.Notification"%>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.vo.cartera.TablaAmortVO"%>
<%@ page import="com.sicap.clientes.vo.CicloGrupalVO"%>
<%@ page import="com.sicap.clientes.vo.GrupoVO"%>
<html>
    <head>
        <title>Condonación Grupal</title>
        <script>
            function muestraDividendo() {
                if (window.document.forma.idGrupo.value == '' || window.document.forma.idCiclo.value == '') {
                    alert("Debe especificar un grupo y ciclo");
                    return false;
                }
                window.document.forma.submit();
            }

            function redireccionMenuAdmin() {
                window.document.forma.command.value = 'administracionPagos';
                window.document.forma.submit();
            }

            function condonarMora(numDividendos) {

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
                    alert("No ha seleccionado ningún dividendo para condonar");
                    return false;
                } else {

                    if (confirm("¿Va a condonar estos dividendos?"))
                    {
                        window.document.forma.command.value = 'condonarMora';
                        window.document.forma.submit();
                    }

                }
            }

        </script>

        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>

    <%Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");
        int idGrupo = HTMLHelper.getParameterInt(request, "idGrupo");
        int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
        int idCredito = HTMLHelper.getParameterInt(request, "idCredito");
        CicloGrupalVO ciclo = new CicloGrupalVO();
        TablaAmortVO[] tablaAmort = null;
        TablaAmortVO tabla = new TablaAmortVO();
        GrupoVO grupo = new GrupoVO();
        double total_multa_div = 0.00;
        CatalogoDAO catalogoDao = new CatalogoDAO();
        if (request.getAttribute("DIVIDENDOS") != null) {
            tablaAmort = (TablaAmortVO[]) request.getAttribute("DIVIDENDOS");
            ciclo = (CicloGrupalVO) request.getAttribute("CICLO");

        }

    %>

    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
    <center><h3>Condonación Grupal<br></h3></center>

    <form name="forma" action="admin" method="POST">
        <input type="hidden" name="command" value="listarDividendosMora">
        <center><%=HTMLHelper.displayNotifications(notificaciones)%></center>
        <div><table border="0" width="100%" cellspacing="0" align="center" height="10%">
                <tr>
                    <td width="50%" height="10%" align="right">Grupo<br></td>
                        <%if (idGrupo > 0) {
                        %>
                    <td width="50%"><input type="text" name="idGrupo" size="10" maxlength="10" value="<%=HTMLHelper.displayField(idGrupo)%>"></td>
                        <%} else {
                        %>      
                    <td width="50%"><input type="text" name="idGrupo" size="10" maxlength="10"></td>
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
                    <td width="50%"><input type="text" name="idCiclo" size="3" maxlength="3"></td>
                        <%}
                        %>      
                </tr>
                <% 	if (tablaAmort != null) {
                %>
                <input type="hidden" name ="idCredito" value="<%=HTMLHelper.displayField(ciclo.idCreditoIBS)%>">									
                <input type="hidden" name ="idGrupo" value="<%=HTMLHelper.displayField(ciclo.idGrupo)%>">									

                <tr>

                    <td colspan="2">
                        <br>
                        <table width="100%" border="0">
                            <tr bgcolor="#009865">
                                <td class="whitetext" align="center">Num Grupo</td>
                                <td class="whitetext" align="center">Num Credito</td>
                                <td class="whitetext" align="center">Fecha Pago</td>
                                <td class="whitetext" align="center">Num Dividendo</td>
                                <td class="whitetext" align="center">Rubro</td>
                                <td class="whitetext" align="center">Saldo Mora con IVA</td>
                                <td class="whitetext" align="center">Condonar</td>
                            </tr>
                            <%
                                for (int i = 0; i < tablaAmort.length; i++) {
                                    tabla = tablaAmort[i];
                                    total_multa_div = tabla.multa - tabla.multaPagado + tabla.ivaMulta - tabla.ivaMultaPagado;
                                    if (total_multa_div > 0) {
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
                                    MULTA
                                </td>
                                <td align="right">								
                                    <%=HTMLHelper.formatoMonto(total_multa_div)%>
                                </td>
                                <td align="center">
                                    <input type="checkbox" name ="dividendos" value="<%=HTMLHelper.displayField(tablaAmort[i].numPago)%>">									
                                </td>

                            </tr>
                            <%
                                    }
                                }
                            %>
                        </table>

                    </td>				
                </tr>
                <tr>
                    <td colspan="3" height="10%" align="center">
                        <%if(!catalogoDao.ejecutandoCierre()){%>
                        <input type="button" value="Condonar" onclick="condonarMora(<%=tablaAmort.length%>);">
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

