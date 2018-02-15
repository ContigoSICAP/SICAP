<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" errorPage="error.jsp"%>
<%@ page import="com.sicap.clientes.vo.*"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>   
        <title>Captura Pago Comunal</title>
        <%
            int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
            GrupoVO grupo = (GrupoVO) session.getAttribute("GRUPO");
            CicloGrupalVO ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
            Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");

            int numIntegrantes = 0;
            for (int i = 0; grupo.ciclos != null && i < grupo.ciclos.length; i++) {
                //obtiene los integrantes del pago comunal
                //obtiene la tabla de amortización para el grupo
            }
            if (idCiclo != 0) {
                ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
                if (ciclo.integrantes != null) {
                    numIntegrantes = ciclo.integrantes.length;
                }
            }
        %>

        <script language="Javascript" src="./js/functions.js"></script>
        <script>

            function confirmar() {
                if (confirm("¿Está seguro que los datos facilitados son correctos?")) {
                    recibir();
                } else {
                    //No envía el formulario
                    return false;
                }
            }

            function recibir() {

                var pagoRegistrado = 0;
                var solidario = 0;
                var ahorro = 0;
                var multa = 0;
                var bandera = 0;

                for (j = 0; j <= <%=ciclo.plazo%>; j++) {
                    pagoRegistrado = document.getElementById('registrado' + j);
                    solidario = document.getElementById('solidario' + j);
                    ahorro = document.getElementById('ahorro' + j);
                    multa = document.getElementById('multa' + j);
                    total = document.getElementById('pagototal' + j);
                    if (isNaN(parseFloat(pagoRegistrado.value))) {
                        break;
                    }
                    pagosIndividuales = document.getElementsByName('pago' + j);
                    var sumaPagos = 0;

                    if (pagosIndividuales[0].readOnly != true) {
                        //alert(pagosIndividuales[0].readOnly);
                        for (i = 0; i < pagosIndividuales.length; i++) {
                            if (isNaN(parseFloat(pagosIndividuales[i].value))) {
                                alert('Verifique los datos capturados, no se permiten celdas vacías o con valores no numéricos');
                                return false;
                            }
                            else {
                                sumaPagos = sumaPagos + parseFloat(pagosIndividuales[i].value);
                            }
                        }
                        sumaPagos = formateaADosDecimales(sumaPagos) + parseFloat(solidario.value) + parseFloat(ahorro.value) + parseFloat(multa.value);

                        if (sumaPagos != pagoRegistrado.value) {
                            alert('(Columna ' + j + ') Suma Pagos= ' + sumaPagos);
                            alert('(Columna ' + j + ') Pago Registrado= ' + pagoRegistrado.value);
                            alert('La suma de los pagos individuales no concuerdan con lo registrado');
                            bandera = 0;
                            total.value = sumaPagos;
                            break;
                        } else {
                            bandera = 1;
                        }
                    }
                }
                if (bandera == 1) {
                    window.document.forma.command.value = 'registrarPagoIndividual';
                    window.document.forma.submit();
                } else {
                    //alert('No se pudo procesar el pago comunal');
                    return false;
                }
            }

        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>

    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
        <form name="forma" action="controller" method="post">
            <input type="hidden" name="command" value="">
            <input type="hidden" name="idGrupo" value="<%=grupo.idGrupo%>">
            <input type="hidden" name="idCiclo" value="<%=idCiclo%>">
            <input type="hidden" name="tipo" value="">
            <input type="hidden" name="numIntegrantes" value="<%=numIntegrantes%>">
            <input type="hidden" name="idSolicitud" value="">

            <table border="0" width="100%">		
                <tr>
                    <td align="center" width="75%">
                        <h3>Matriz Pago Comunal</h3>
                        <%=HTMLHelper.displayNotifications(notificaciones)%>				
                        <table width="100%" border="1" cellpadding="0">

                            <tr>
                                <td style="font-weight: bold" width="15%" valign="top">N&uacute;mero del grupo&nbsp;&nbsp;
                                    <input type="text" name="idGrupo" size="5" value="<%=HTMLHelper.displayField(grupo.idGrupo)%>" readonly="readonly" class="soloLectura">
                                </td>
                                <td style="font-weight: bold" width="40%">
                                    Nombre del grupo&nbsp;&nbsp;
                                    <input type="text" name="nombre" size="50" value="<%=HTMLHelper.displayField(grupo.nombre)%>" readonly="readonly" class="soloLectura">
                                </td>
                                <td style="font-weight: bold" width="45%">
                                    Número de ciclo&nbsp;&nbsp;
                                    <input type="text" name="idCiclo" size="10" value="<%=HTMLHelper.displayField(ciclo.idCiclo)%>" readonly="readonly" class="soloLectura">
                                </td>
                            </tr>

                        </table>		

                        <table border="1" cellpadding="0" width="100%" >				
                            <tr>
                                <td align="center">
                                    Número
                                </td>
                                <td align="center">
                                    Nombre
                                </td>
                                <td align="center">
                                    Monto Individual
                                </td>
                                <td align="center">
                                    Pago 0
                                </td>
                                <td align="center">
                                    Pago 1
                                </td>
                                <td align="center">
                                    Pago 2
                                </td>
                                <td align="center">
                                    Pago 3
                                </td>
                                <td align="center">
                                    Pago 4
                                </td>
                                <td align="center">
                                    Pago 5
                                </td>
                                <td align="center">
                                    Pago 6
                                </td>
                                <td align="center">
                                    Pago 7
                                </td>
                                <td align="center">
                                    Pago 8
                                </td>
                                <td align="center">
                                    Pago 9
                                </td>
                                <td align="center">
                                    Pago 10
                                </td>
                                <td align="center">
                                    Pago 11
                                </td>
                                <td align="center">
                                    Pago 12
                                </td>
                                <% if (ciclo.plazo == 16) { %>
                                <td align="center">
                                    Pago 13
                                </td>
                                <td align="center">
                                    Pago 14
                                </td>
                                <td align="center">
                                    Pago 15
                                </td>
                                <td align="center">
                                    Pago 16
                                </td>
                                <% }%>
                            </tr>		
                            <!--  Aqui va el codigo que llena la matriz y la muestra -->  		  			
                            <%=HTMLHelper.displayMatrizPagoComunalDetalle(grupo.idOperacion, ciclo)%>		
                            <tr>
                                <td style="width:"1500px" colspan="20" align="center">
                                    <input type="button" value="Recibir Pagos" onClick="confirmar();" style="width: 120px"/>
                                </td>
                            </tr>	
                        </table>										
                    </td>
                </tr>
            </table>
        </form>
        <%
            request.setAttribute("NOTIFICACIONES", notificaciones);
        %>
        <jsp:include page="footer.jsp" flush="true"/>	    
    </body>
</html>






