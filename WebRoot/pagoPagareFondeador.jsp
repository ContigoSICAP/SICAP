<%@page import="com.sicap.clientes.vo.PagareVO"%>
<%@page import="com.sicap.clientes.vo.PagoPagareVO"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.sicap.clientes.vo.LineaCreditoVO"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.dao.*"%>
<%@ page import="java.util.TreeMap"%>
<%
    Vector notificaciones = (Vector)request.getAttribute("NOTIFICACIONES");
    ArrayList<PagareVO> arrPagares = new ArrayList<PagareVO>();
    PagareVO[] listPagares = null;
    PagareDAO pagareDao = new PagareDAO();
    int numPagos = 0;
    double montoPago = 0.0;
    //Date fechaPago = null;
    //String refBancaria = "";
    int i = 0;
    String command = "";
    PagareVO pagareVO = (PagareVO) request.getAttribute("PAGOPAGARE");
    
    arrPagares = pagareDao.getPagaresActivosArr();
     numPagos = arrPagares.size();
    CatalogoDAO catalogoDao = new CatalogoDAO();
    
    if (listPagares != null && listPagares.length >0) 
    {
        command = "aplicaPagosPagare";
    }
   
%>
<html>
    <head>
        <title> Ingreso Pago de Pagaré a Fondeador</title>
        <script type="text/javascript" src="./js/functions.js"></script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
        <script>
            
            function validaCamposPago()
            {
                if(isNaN(window.document.forma.montoPago.value))
                {
                    alert("Ingrese el Pago a Capital");
                    window.document.forma.montoPago.focus();
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                
                
                else if(window.document.forma.fechaPago.value=='')
                {
                    alert("Ingrese la Fecha de Pago");
                    window.document.forma.fechaPago.focus();
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                else if(!validaFecha(window.document.forma.fechaPago.value))
                {
                    alert('El dato [Fecha Pago] es erróneo, favor de validar e ingresarlo nuevamente');
                    window.document.forma.fechaPago.focus();
                    document.getElementById("boton").disabled = false;
                    return false;
		}
                else if(window.document.forma.refBancaria.value==='')
                {
                    alert("Ingrese la Referencia Bancaria");
                    window.document.forma.refBancaria.focus();
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                window.document.forma.command.value = 'aplicaPagosPagare';
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }
            
            function seleccionar() {
                var checa = 1;
                if (window.document.forma.checkGeneral.checked == false)
                    checa = 0;
                for (var i = 0; i < window.document.forma.numRegistros.value; i++) {
                    if (window.document.forma.numRegistros.value == 1)
                        window.document.forma.idCheckBox.checked = checa;
                    else
                        window.document.forma.idCheckBox[i].checked = checa;
                }
            }
            function aplicaPagosPagare() {
                window.document.forma.command.value = 'aplicaPagosPagare';
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }
            function redireccionMenuGestionFondeadores(){
                window.document.forma.command.value = 'administracionFondeadores';
                document.body.style.cursor = "wait";
                window.document.forma.submit();
            }
        </script>
    </head>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
    <center>
        <h2>Aplicar Pago de Pagaré</h2> 
    </center>
    <form method="post" name="forma" action="admin">
        <input type="hidden" name="command" value="">
        <table border="0" width="25%" align="center" cellpadding="5">
            <tr>
                <td align="center" colspan="8"><h3></h3>
                    <%=HTMLHelper.displayNotifications(notificaciones)%></td>
            </tr>
            <tr bgcolor="#009865">
                <td class="whitetext" align="center">Seleccionar</td>
                <td class="whitetext" align="center">Num Pagaré</td>
                <td class="whitetext" align="center">Pagaré</td>
                <td class="whitetext" align="center">Fondeador</td>
                <td class="whitetext" align="center">Fecha de Vencimiento</td>
                <td class="whitetext" align="center">Monto de Págare</td>
                
            </tr>
            <%if (numPagos != 0) {%>
            <%for (PagareVO pagare : arrPagares) {%>
            <tr>
                <td align="center"><input type="radio" name="idRadio" id="idRadio" value="<%=i%>" checked=""/></td>
                <td align="center"><%=HTMLHelper.displayField(pagare.getNumPagare())%></td>
                <td align="center"><%=HTMLHelper.displayField(pagare.getNombrePagare())%></td>
                <td align="center"><%=HTMLHelper.displayField(pagare.getNombreFondeador())%></td>
                <td align="center"><%=HTMLHelper.displayField(pagare.getFechaFin())%></td>
                <td align="right"><%=HTMLHelper.formatoMonto(pagare.getMontoPagare())%></td>
                
                
                <input type="hidden" name="idFondeador<%=i%>" id="idFondeador<%=i%>" value="<%=pagare.getNumFondeador()%>">
                <input type="hidden" name="numPagare<%=i%>" id="numPagare<%=i%>" value="<%=pagare.getNumPagare()%>">
                <input type="hidden" name="nombrePagare<%=i%>" id="nombrePagare<%=i%>" value="<%=pagare.getNombrePagare()%>">
                <input type="hidden" name="nombreFond<%=i%>" id="nombreFond<%=i%>" value="<%=pagare.getNombreFondeador()%>">
                <input type="hidden" name="fechaFin<%=i%>" id="fechaFin<%=i%>" value="<%=pagare.getFechaFin()%>">
                <input type="hidden" name="monto<%=i%>" id="monto<%=i%>" value="<%=pagare.getMontoPagare()%>">
            </tr>
            <%i++;
                            }%>              
            <tr>
                <td></td>
                <th align="right">Pago de Capital:</th>
                <td align="left"><input type="text" name="montoPago" id="montoPago" maxlength="9" value= "<%=montoPago%>"/></td>
            </tr>
            <tr>
                <td></td>
                <th align="right">Fecha de Pago:</th>
                <td align="left"><input type="text" name="fechaPago" id="fechaInicial" placeholder="dd/mm/yyyy"></td>
            </tr>
            <tr>
                <td></td>
                <th align="right">Referencia Bancaria:</th>
                <td align="left"><input type="text" name="refBancaria" id="refBancaria"/></td>
            </tr>
            
            <tr>
                <td>
                    <%if (!catalogoDao.ejecutandoCierre()) {%>
                    <td align="center" colspan="2"><br><input type="button" id="boton" value="Aplicar Pago" onclick="validaCamposPago()"> <input type="button" id="boton" value="Regresar" onClick="redireccionMenuGestionFondeadores()"><br><br></td>
                    
                        <%}%>
                </td>
            </tr>
            <%}%>
            <input type="hidden" name="numRegistros" id="numRegistros" value="<%=i%>"/>
        </table>
    </form>

</body>
</html>
