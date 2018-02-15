<%@page import="com.sicap.clientes.dao.CatalogoDAO"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.util.Notification"%>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.sicap.clientes.vo.cartera.*"%>
<%@ page import="com.sicap.clientes.vo.*"%>
<html>
    <head>
        <title>Descongelar pago en garant&iacute;a grupal</title>
        <%
            int i = 0;
            Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");
            CreditoCartVO grupoDescongelaMonto = (CreditoCartVO) request.getAttribute("MONTODESCONGELADO");
            GrupoVO datosGrupo = (GrupoVO) request.getAttribute("DATOSGRUPO");
            int numeroGrupo = 0;
            int numeroCiclo = 0;
            String referencia = null;
            String nombreGrupo = null;
            double montoCongeladoT = 0;
            CatalogoDAO catalogoDao = new CatalogoDAO();
        %>
        <script>
            function submitenter1(myfield,e){
                var keycode;
                if (window.event) 
                    keycode = window.event.keyCode;
                else if (e) 
                    keycode = e.which;
                else 
                    return true;
                if (keycode == 13){
                    window.document.forma.idCiclo.focus();          
                }else
                    return true;
            }
            function submitenter2(myfield,e){
                var keycode;
                if (window.event) 
                    keycode = window.event.keyCode;
                else if (e) 
                    keycode = e.which;
                else 
                    return true;
                if (keycode == 13){
                    listarGrupo();          
                }else
                    return true;
            }
            function submitenter3(myfield,e){
                var keycode;
                if (window.event) 
                    keycode = window.event.keyCode;
                else if (e) 
                    keycode = e.which;
                else 
                    return true; 
                if (keycode == 13){
                    descongelaAhorro();          
                }else
                    return true;
            }      	          
            function listarGrupo(){
                if (window.document.forma.idGrupo.value=='' || window.document.forma.idCiclo.value==''){
                    alert("Debe especificar un grupo y ciclo");
                    return false;
                }            
                window.document.forma.command.value = 'descongelaAhorro';
                window.document.forma.submit();
            }           
            function descongelaAhorro(){
                if (!esFormatoMoneda(window.document.forma.montoNDescongelar.value) || window.document.forma.montoNDescongelar.value<=0){
                    alert('El monto a descongelar es inválido');
                    return false;
                }
                if (!esFormatoMoneda(window.document.forma.montoCongelado.value) || window.document.forma.montoCongelado.value<=0){
                    alert('El monto a congelado es inválido');
                    return false;
                }      	  
                if (parseFloat(window.document.forma.montoNDescongelar.value) > parseFloat(window.document.forma.montoCongelado.value)) {
                    alert("El monto a descongelar debe ser menor");
                    //alert("2monto a descongelar "+window.document.forma.montoNDescongelar.value+"> montocongelado "+window.document.forma.montoCongelado.value)
                    return false;
                } else {
                    window.document.forma.command.value = 'descongelaAhorro';
                    //alert("Mando el submit");
                    window.document.forma.submit();
                }	  	  
            }
        </script>

        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>

    <body leftmargin="0" topmargin="0" onload ="javascript:window.document.forma.idGrupo.focus();">

        <jsp:include page="header.jsp" flush="true"/>
    <center><h3>Descongelar Pago en Garant&iacute;a<br></h3></center>

    <form name="forma" action="admin" method="POST">
        <input type="hidden" name="command" value="descongelaAhorro">
        <center><%=HTMLHelper.displayNotifications(notificaciones)%></center>
        <div>
            <table border="0" width="100%" cellspacing="0" align="center" height="10%">
                <tr>
                    <td width="50%" height="10%" align="right">Grupo<br></td>
                        <% if (grupoDescongelaMonto == null) {%>
                    <td width="50%"><input type="text" name="idGrupo" size="10" maxlength="10" onKeyPress="return submitenter1(this,event)"></td>
                        <%} else {%>
                    <td width="50%"><input disabled type="text" name="idGrupo" size="10" maxlength="10"></td>
                        <%}%>							            
                </tr>            
                <tr>
                    <td>&nbsp;</td>
                </tr>
                <tr>
                    <td width="50%" height="10%" align="right">Ciclo<br></td>
                        <% if (grupoDescongelaMonto == null) {%>
                    <td width="50%"><input type="text" name="idCiclo" size="10" maxlength="10" onKeyPress="return submitenter2(this,event)"></td>
                        <%} else {%>
                    <td width="50%"><input disabled type="text" name="idCiclo" size="10" maxlength="10"></td>
                        <%}%>
                </tr>            
                <tr>
                    <td>&nbsp;</td>
                </tr>            
                <tr>
                    <td colspan="2">
                        <%if (grupoDescongelaMonto != null) {
                        %>
                        <br>
                        <table width="60%" align="center" border="0">
                            <tr bgcolor="#009865">
                                <td class="whitetext" align="center">Descongelar</td>
                                <td class="whitetext" align="center">Numero de Grupo</td>
                                <td class="whitetext" align="center">Ciclo</td>
                                <td class="whitetext" align="center">Nombre</td>
                                <td class="whitetext" align="center">Monto Congelado</td>
                                <td class="whitetext" align="center">Monto a Descongelar</td>
                            </tr>
                            <%
                                for (i = 0; i < 1; i++) {
                                    //grupoDescongelaMonto = (DescongelaAhorroVO)listaGrupos.get(i);
                                    //grupoDescongelaMonto = (DescongelaAhorroVO[]) request.getAttribute("MONTODESCONGELADO");
                                    numeroGrupo = grupoDescongelaMonto.getNumCliente();
                                    numeroCiclo = grupoDescongelaMonto.getNumSolicitud();
                                    referencia = grupoDescongelaMonto.getReferencia();
                                    montoCongeladoT = grupoDescongelaMonto.getMontoCuentaCongelada();
                                    nombreGrupo = datosGrupo.nombre;

                                    double sugerenciaDescongelar = 0;//es la resta del monto congelado menos la garantia garantia igual al 10% del desembolso
                                    double garantia = 0;

                                    garantia = 0.10 * grupoDescongelaMonto.getMontoDesembolsado();

                                    if (montoCongeladoT - garantia > 0) {
                                        sugerenciaDescongelar = montoCongeladoT - garantia;
                                    }


                            %>	
                            <tr>
                                <td width = "20%"><input type="radio" name="opcion" value="<%=numeroGrupo%>" checked>&nbsp;</td>
                                <td width = "10%" align = "center"><%=numeroGrupo%></td>
                                <td width = "10%" align = "center"><%=numeroCiclo%></td>
                                <td width = "20%" align = "center"><%=nombreGrupo%></td>
                                <td width = "20%" align = "center"><input disabled type="text" size = "20" name="montoCongelado" maxlength="10" value ="<%=montoCongeladoT%>"/></td>
                                <td width = "20%"><input value="<%=sugerenciaDescongelar%>"   type="text" size = "20" id="mND" name="montoNDescongelar" maxlength="10" onKeyPress="return submitenter3(this,event)"/></td>
                            </tr>
                            <%
                                }
                            %>
                        </table>											
                        <%
                            }
                        %>
                    </td>
                </tr>		
                <tr>
                    <td><input type="hidden" name="numGrupo" size="10" maxlength="10" value="<%=numeroGrupo%>"></td>
                    <td><input type="hidden" name="numReferencia" size="13" maxlength="13" value="<%=referencia%>"></td>
                </tr>	
                <tr>
                    <td colspan="3" height="10%" align="center">
                        <% if (grupoDescongelaMonto == null) {%>
                        <input type="button" value="Buscar" onClick="listarGrupo();">
                        <%} else {%>
                        <input disabled type="button" value="Buscar" onClick="listarGrupo();">
                        <%}%>				
                        <% if (grupoDescongelaMonto != null && !catalogoDao.ejecutandoCierre()) {%>
                        <input type="button" value="Descongelar" onClick="descongelaAhorro()">
                        &nbsp;										
                        <% }%>
                    </td>
                    <td colspan="3" height="10%" align="center"><br>
                    </td>
                </tr>
            </table>
        </div>
    </form>
    <%@include file="footer.jsp"%>
</body>
</html>

