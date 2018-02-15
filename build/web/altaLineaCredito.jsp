<%@page import="java.util.Date"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@ page import="com.sicap.clientes.vo.LineaCreditoVO"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.dao.*"%>
<%@ page import="java.util.TreeMap"%>
<%
    int idFondeador = 0;
    String razonSocial = "";
    String nombreLineaCredito = "";
    int montoLineaCredito = 0;
    Date fechaVigenciaInicio = null;
    Date fechaVigenciaFin = null;
    String tasa = "";
    int estatus = 0;
    String command = "";
    String identificador = "";
    int i =0;
    
    LineaCreditoVO lineaVO = (LineaCreditoVO) request.getAttribute("LINEACREDITO");
    TreeMap catFondeadores = CatalogoHelper.getCatalogoSeleccione(ClientesConstants.CAT_FONDEADORES);
    
    catFondeadores.remove(ClientesConstants.ID_FONDEADOR_BURSA);
    
    Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");
    String[] roles = new UsuarioDAO().getRoles();
    if (lineaVO == null) 
    {
        command = "altaLineaCredito";
        //obtenemos datos de la pantalla AltaLineaCredito
        /* nombreLineaCredito = HTMLHelper.getParameterString(request, "nombre");
        montoLineaCredito = HTMLHelper.getParameterInt(request, "monto");
        fechaVigenciaInicio = HTMLHelper.getParameterDate(request, "fechaInicio");
        fechaVigenciaFin = HTMLHelper.getParameterDate(request, "fechaFin");
        tasa = HTMLHelper.getParameterDouble(request, "tasa");
        preSeleccionCartera = HTMLHelper.getParameterInt(request, "preSeleccionCartera");
        */
        //estatus al guardar cambiara a activo = 1
       
    } 
    else 
    {
        command = "modificaLineaCredito";
        lineaVO.getIdLineaCredito();
        lineaVO.getIdFondeador();
        lineaVO.getNombreLineaCredito();
        lineaVO.getEstatus();
    }
%>
<html>
    <head>
        <title> Alta L&iacute;nea de Cr&eacute;dito</title>
        <script type="text/javascript" src="./js/functions.js"></script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
        <script>
            function validaCamposLineaCredito()
            {
                if (validaFechFinMayorFechFin(window.document.forma.fechaInicio.value, window.document.forma.fechaFin.value))
                {
                    window.document.forma.fechaInicio.focus();
                    return true;
                }
                if(window.document.forma.idFondeador.value==0){
                    alert("Es necesario seleccionar un Fondeador");
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if(window.document.forma.idFondeador.value===null && window.document.forma.idFondeador.value===0)
                {
                    alert("Seleccione un fondeador");
                    window.document.forma.idFondeador.focus();
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if( window.document.forma.nombre.value===''){
                    alert("Ingrese el nombre de la linea credito");
                    window.document.forma.nombre.focus();
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if( window.document.forma.monto.value==='' && window.document.forma.monto.value > 0)
                {
                    alert("Ingrese el monto de la línea crédito");
                    window.document.forma.monto.focus();
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (!esEntero(window.document.forma.monto.value))
                {
                    alert('El dato [Monto] es erróneo, favor de validar e ingresarlo nuevamente');
                    window.document.forma.monto.focus();
                    return false;
                        
		}
                if(!numeroMayorCero(window.document.forma.monto.value))
                {
                    alert('Ingrese el [Monto] mayor a cero');
                    window.document.forma.monto.focus();
                    return false;
                }
                
                if( window.document.forma.fechaInicio.value==='' && window.document.forma.fechaInicio.value===null)
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
                
                if( window.document.forma.tasa.value==='')
                {
                    alert("Ingrese la Tasa de Interés");
                    window.document.forma.tasa.focus();
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
        <h2>Alta L&iacute;nea de Cr&eacute;dito</h2> 
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
                <th align="right">Fondeador:</th>
                <td width="50%">  
                    <select name="idFondeador" size="1">
                        <%=HTMLHelper.displayCombo(catFondeadores, idFondeador)%>
                    </select>
                </td>
            </tr>
            <tr>
                <th align="right">Nombre de la L&iacute;nea de Cr&eacute;dito:</th>
                <td align="left"><input type="text" name="nombre" maxlength="60" value= "<%=nombreLineaCredito%>"/></td>
            </tr>
            <tr>
                <th align="right">Monto de la L&iacute;nea de Cr&eacute;dito: </th>
                <td align="left"><input type="text" maxlength="9" name="monto" value = "<%=montoLineaCredito%>"/></td>

            </tr>
            <tr>
                <th align="right">Vigencia</th>
            </tr>
            <tr>
                <th align="right">Fecha Inicio:</th>
                <td align="left"><input type="text" name="fechaInicio" id="fechaInicio" placeholder="dd/mm/yyyy"></td>
            </tr>
            <tr>
                <th align="right">Fecha Fin:</th>
                <td align="left"><input type="text" name="fechaFin" id="fechaFin" placeholder="dd/mm/yyyy"></td>
            </tr>
            <tr>
                <th align="right">Tasa:</th>
                <td align="left"><input type="text" name="tasa" value = "<%=tasa%>" style="text-transform: uppercase" placeholder="TIIE"/></td>
            </tr>
            <tr>
                <td align="center" colspan="2"><br><input type="button" id="boton" value="Aceptar" onclick="validaCamposLineaCredito()"> <input type="button" id="boton" value="Regresar" onClick="redireccionMenuGestionFondeadores()"><br><br></td>
            </tr>
        </table>
    </form>

</body>
</html>
