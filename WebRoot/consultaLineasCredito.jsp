<%@page import="java.util.ArrayList"%>
<%@page import="com.sicap.clientes.vo.PagareVO"%>
<%@page import="java.util.List"%>
<%@page import="com.sicap.clientes.vo.FondeadorVO"%>
<%@page import="java.util.Date"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@ page import="com.sicap.clientes.vo.LineaCreditoVO"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.dao.*"%>
<%@ page import="java.util.TreeMap"%>
<%
    String command = "consultarFondeadores";
    Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");
    List<PagareVO> pagares = new ArrayList<PagareVO>();
    pagares = (List<PagareVO>) request.getAttribute("PAGARES");
    
    TreeMap nombreLineasCredito = CatalogoHelper.getLineasCreditoActivas();
    int idLineaCredito = HTMLHelper.getParameterInt(request, "idLineaCredito");
%>
<html>
    <head>
        <title>Alta Fondeador</title>
        <script type="text/javascript" src="./js/functions.js"></script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
        <script>
            function validaLineaCredito()
            {
                if(window.document.forma.idLineaCredito.value===0)
                {
                    alert("Es necesario seleccionar una Línea de Crédito");
                    document.getElementById("boton").disabled = false;
                    return false;
                }
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
        <h2>Consula Lineas de Credito - Pagares</h2> 
    </center>
    <form method="post" name="forma" action="admin">
        <input type="hidden" name="command" value="<%=command%>">
        <table border="0" cellspacing="5" align="center" >
            <tr>
                <td align="center" colspan="2">
                    <%=HTMLHelper.displayNotifications(notificaciones)%><br>
                </td>
            </tr>
            <tr>
                <th align="right">L&iacute;nea de Cr&eacute;dito:</th>
                <td width="50%">
                    <select name="idLineaCredito" size="1">
                        <%=HTMLHelper.displayCombo(nombreLineasCredito, idLineaCredito)%>
                    </select>
                </td>
            </tr>
            <tr>
                <td align="center" colspan="2"><br><input type="button" id="botonAlta" value="Buscar" onclick="validaLineaCredito()"> <input type="button" id="botonRegresar" value="Regresar" onClick="redireccionMenuGestionFondeadores()"><br><br></td>
            </tr>
        </table>
        <center>
            <h2>Pagares Asociados</h2> 
        </center>
        <table border="2" cellspacing="5" align="center" >
            <tr bgcolor="#00bfff">
                <td class="whitetext" align="center">Nombre</td>
                <td class="whitetext" align="center">Monto</td>
                <td class="whitetext" align="center">Fecha de Inicio</td>
                <td class="whitetext" align="center">Fecha de Vencimiento</td>
                <td class="whitetext" align="center">Estatus</td>
            </tr>
            
            <%if(pagares!=null){
                for (PagareVO pagare : pagares) {%>
            <tr>
                <td align="center"><%=pagare.getNombrePagare()%></td>
                <td align="center"><%=HTMLHelper.formatoMonto(pagare.getMonto())%></td>
                <td align="center"><%=pagare.getFechaInicio()%></td>
                <td align="center"><%=pagare.getFechaFin()%></td>
                <td align="center"><%=(pagare.getEstatus()==1)?"Vigente":((pagare.getEstatus()==2)?"Pagado":"Vencido")%></td>
            </tr>
            <%  }
             }%>
        </table>
    </form>

</body>
</html>
