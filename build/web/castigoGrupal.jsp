<%@page import="com.sicap.clientes.dao.CatalogoDAO"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.util.Notification"%>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.vo.cartera.TablaAmortVO"%>
<%@ page import="com.sicap.clientes.vo.CicloGrupalVO"%>
<%@ page import="com.sicap.clientes.vo.GrupoVO"%>
<%@ page import="com.sicap.clientes.vo.SaldoIBSVO"%>
<html>
    <head>
        <title>Castigo a Grupos</title>
        <script>
            function muestraCredito() {
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

            function castigar(numGrupos) {

                var existeReferenciaParaReprocesar = 'noexisteReferencia';
                var i;
                if (numGrupos == 1) {
                    if (window.document.forma.saldos.checked) {
                        existeReferenciaParaReprocesar = 'existeReferencia';
                    }

                } else if (numGrupos > 1) {

                    for (i = 0; i < window.document.forma.saldos.length; i++) {
                        var temp = window.document.forma.saldos[i].checked;
                        if (window.document.forma.saldos[i].checked) {
                            existeReferenciaParaReprocesar = 'existeReferencia';
                            break;
                        }
                    }
                }


                if (existeReferenciaParaReprocesar == 'noexisteReferencia') {
                    alert("No ha seleccionado ningún credito para castigar");
                    return false;
                } else {

                    if (confirm("¿Va a castigar estos creditos?"))
                    {
                        window.document.getElementById("botonCastigar").disabled = true;
                        window.document.forma.command.value = 'castigarGrupo';
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
        //SaldoIBSVO[]    saldoIBS = null;
        SaldoIBSVO saldo = null;
        GrupoVO grupo = new GrupoVO();
        CatalogoDAO catalogoDao = new CatalogoDAO();
        if (request.getAttribute("SALDOS") != null) {
            saldo = (SaldoIBSVO) request.getAttribute("SALDOS");
            ciclo = (CicloGrupalVO) request.getAttribute("CICLO");
        }

    %>

    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
    <center><h3>Castigo a Grupos<br></h3></center>

    <form name="forma" action="admin" method="POST">
        <input type="hidden" name="command" value="listarGruposCastigo">
        <center><%=HTMLHelper.displayNotifications(notificaciones)%></center>
        <div><table border="0" width="100%" cellspacing="0" align="center" height="10%">
                <tr>
                    <td width="50%" height="10%" align="right">Grupo<br></td>
                        <%if (idGrupo > 0) {
                        %>
                    <td width="50%"><input type="text" name="idGrupo" id="idGrupo" size="10" maxlength="10" value="<%=HTMLHelper.displayField(idGrupo)%>"></td>
                        <%} else {
                        %>      
                    <td width="50%"><input type="text" name="idGrupo" id="idGrupo" size="10" maxlength="10"></td>
                        <%}
                        %>      
                </tr>

                <tr>
                    <td width="50%" height="10%" align="right">Ciclo<br></td>
                        <%if (idCiclo > 0) {
                        %>
                    <td width="50%"><input type="text" name="idCiclo" id="idCiclo" size="3" maxlength="3" value="<%=HTMLHelper.displayField(idCiclo)%>"></td>
                        <%} else {
                        %>      
                    <td width="50%"><input type="text" name="idCiclo" id="idCiclo" size="3" maxlength="3"></td>
                        <%}
                        %>      
                </tr>
                <% if (saldo != null) {	%>

                <tr>

                    <td colspan="2">
                        <br>
                        <table width="100%" border="0">
                            <tr bgcolor="#009865">
                                <td class="whitetext" align="center">Num Grupo</td>
                                <td class="whitetext" align="center">Num Credito</td>
                                <td class="whitetext" align="center">Sucursal</td>
                                <td class="whitetext" align="center">Monto del Crédito</td>
                                <td class="whitetext" align="center">Total Vencido</td>
                                <td class="whitetext" align="center">Dias Vencidos</td>
                                <td class="whitetext" align="center">Castigar</td>
                            </tr>
                            <%
                            %>
                            <tr>
                                <td align="center">
                                    <%=saldo.getIdClienteSICAP()%>
                                </td>
                                <td align="center">
                                    <%=saldo.getCredito()%>									
                                </td>								
                                <td align="center">
                                    <%=HTMLHelper.displayField(saldo.getNombreSucursal())%>
                                </td>
                                <td align="center"> 
                                    <%=HTMLHelper.displayField(saldo.getMontoCredito())%>
                                </td>		
                                <td align="center">
                                    <%=HTMLHelper.displayField(saldo.getTotalVencido())%>
                                </td>
                                <td align="right">								
                                    <%=saldo.getDiasMora()%>
                                </td>
                                <td align="center">
                                    <input type="checkbox" name ="saldos" value="<%=HTMLHelper.displayField(saldo.getCuotasVencidas())%>">									
                                </td>

                            </tr>
                            <%
                            %>
                        </table>

                    </td>				
                </tr>
                <tr>
                    <td colspan="3" height="10%" align="center">
                        <%if (!catalogoDao.ejecutandoCierre()) {%>
                        <input type="button" value="Castigar" id="botonCastigar" onclick="castigar(<%=1%>);">
                        <%}%>
                    </td>
                </tr>
                <%}
                %>

                <tr>
                    <td colspan="3" height="10%" align="center">
                        <input type="button" value="Consulta Crédito" onclick="muestraCredito();">
                        <input type="button" value="Regresar" onClick="redireccionMenuAdmin()">
                    </td>
                </tr>
            </table>
        </div>
        <input type="hidden" name ="idCredito" value="<%=HTMLHelper.displayField(ciclo.idCreditoIBS)%>">									
    </form>

    <%@include file="footer.jsp"%></body>
</html>
