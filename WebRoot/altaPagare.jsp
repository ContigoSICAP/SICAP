<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.sicap.clientes.vo.LineaCreditoVO"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@ page import="com.sicap.clientes.vo.PagareVO"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.dao.*"%>
<%@ page import="java.util.TreeMap"%>
<%
    int idLineaCredito = 0;
    String nombrePagare = "";
    int montoPagare = 0;
    String fechaInicio = "";
    String fechaFin = "";
    int estatus = 0;
    String command = "";
    String identificador = "";
    LineaCreditoVO lineaCredito = null;
    List<LineaCreditoVO> lineasCredito = null;
    LineaCreditoDAO lineaDao = new LineaCreditoDAO();
    lineasCredito=lineaDao.getNombreLineasCredito(ClientesConstants.ACTIVO);
    SimpleDateFormat sdf = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA);
 
    PagareVO pagareVO = (PagareVO) request.getAttribute("PAGARE");
    if(pagareVO!=null){
        
        nombrePagare = (pagareVO.getNombrePagare()!=null&&!pagareVO.getNombrePagare().isEmpty())?pagareVO.getNombrePagare():"";
        idLineaCredito = (pagareVO.getNumLineaCredito()>0)?pagareVO.getNumLineaCredito():0;
        montoPagare = (pagareVO.getMontoPagare()>0)?pagareVO.getMontoPagare():0;
        fechaInicio = (pagareVO.getFechaInicio()!=null)?sdf.format(pagareVO.getFechaInicio()):"";
        fechaFin = (pagareVO.getFechaFin()!=null)?sdf.format(pagareVO.getFechaFin()):"";
    }
    
    //select nombre de linea de credito -> modificar
    TreeMap nombreLineasCredito = CatalogoHelper.getLineasCreditoActivas();
    
    List<Integer> idsLineasPagaresCompletos = lineaDao.obtenerLineasCreditoConPagaresCompletos();
    
    for(Integer id : idsLineasPagaresCompletos){
        nombreLineasCredito.remove(id);
    }
    
    Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");
    String[] roles = new UsuarioDAO().getRoles();
    command = "altaPagare";

%>
<html>
    <head>
        <title> Alta Pagar&eacute;</title>
        <script type="text/javascript" src="./js/functions.js"></script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
        <script>
            function validaCamposPagare()
            {
                if(window.document.forma.idLineaCredito.value==0)
                {
                    alert("Es necesario seleccionar una Línea de Crédito");
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if( window.document.forma.nombre.value===''){
                    alert("Ingrese el nombre del pagaré");
                    window.document.forma.nombre.focus();
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if(!numeroMayorCero(window.document.forma.monto.value))
                {
                    alert("Ingrese el monto del pagaré");
                    window.document.forma.monto.focus();
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (!esEntero(window.document.forma.monto.value))
                {
                    alert('El dato [Monto Pagaré] es erróneo, favor de validar e ingresarlo nuevamente');
                    window.document.forma.monto.focus();
                    return false;
                        
		}
                if( window.document.forma.fechaInicio.value==='' || window.document.forma.fechaInicio.value===null)
                {
                    alert("Ingrese la Fecha Inicio");
                    window.document.forma.fechaInicio.focus();
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (!validaFecha(window.document.forma.fechaInicio.value))
                {
                    alert('El dato [Fecha Inicio] es erróneo, favor de validar e ingresarlo nuevamente');
                    window.document.forma.fechaInicio.focus();
                    return false;
		}
                if( window.document.forma.fechaFin.value==='')
                {
                    alert("Ingrese la Fecha Fin");
                    window.document.forma.fechaFin.focus();
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (!validaFecha(window.document.forma.fechaFin.value))
                {
                    alert('El dato [Fecha Fin] es erróneo, favor de validar e ingresarlo nuevamente');
                    window.document.forma.fechaFin.focus();
                    return false;
		}
                if (validaFechFinMayorFechFin(window.document.forma.fechaInicio.value, window.document.forma.fechaFin.value))
                {
                    window.document.forma.fechaInicio.focus();
                    return true;
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
        <h2>Alta Pagar&eacute;</h2> 
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
                <th align="right">Nombre del pagar&eacute;:</th>
                <td align="left"><input type="text" name="nombre" maxlength="60" value= "<%=nombrePagare%>"/></td>
            </tr>
            <tr>
                <th align="right">Monto del pagar&eacute;: </th>
                <td align="left"><input type="text" maxlength="9" name="monto" value = "<%=montoPagare%>"/></td>

            </tr>
            <tr>
                <th align="right">Vigencia</th>
            </tr>
            <tr>
                <th align="right">Fecha Inicio:</th>
                <td align="left"><input type="text" name="fechaInicio" id="fechaInicio" placeholder="dd/mm/yyyy" value="<%=fechaInicio%>"></td>/>
            </tr>
            <tr>
                <th align="right">Fecha Fin:</th>
                <td align="left"><input type="text" name="fechaFin" id="fechaFin" placeholder="dd/mm/yyyy" value="<%=fechaFin%>"></td>
            </tr>
            <tr>
                <td align="center" colspan="2"><br><input type="button" id="boton" value="Aceptar" onclick="validaCamposPagare()"> <input type="button" id="boton" value="Regresar" onClick="redireccionMenuGestionFondeadores()"><br><br></td>
            </tr>
        </table>
    </form>

</body>
</html>
