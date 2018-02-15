<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.List"%>
<%@page import="com.sicap.clientes.vo.LineaCreditoVO"%>
<%@page import="java.util.Date"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@ page import="com.sicap.clientes.vo.AmortizacionPagareVO"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.dao.*"%>
<%@ page import="java.util.TreeMap"%>
<%
    int idAmort = 0;
    int idPagare =0;
    int numPago = 0;
    String fechaPago = "";
    double capital = 0.0;
    int periodo = -1;
    double iva = 0.0;
    String command = "";
 
    
    AmortizacionPagareVO amortVO = (AmortizacionPagareVO) request.getAttribute("MODIFICACION_AMORT");
    Notification[] notificaciones = (Notification[]) request.getAttribute("NOTIFICACIONES");
    LineaCreditoVO lineaVO = (LineaCreditoVO) request.getAttribute("LINEACREDITO");
    TreeMap pagares = CatalogoHelper.getPagaresLineaCred();
    
    //eliminamos pagares que ya tienen amortizacion calculada
    AmortPagareDAO amDAO = new AmortPagareDAO();
    List<Integer> idsPagares = amDAO.getPagaresConAmortizaciones();
    for(Integer id : idsPagares){
        pagares.remove(id);
    }
    SimpleDateFormat sdf = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA);
    if(notificaciones != null && notificaciones.length > 0 && notificaciones[0].type == ClientesConstants.ERROR_TYPE ){
        idPagare = HTMLHelper.getParameterInt(request, "idPagare");
        numPago = HTMLHelper.getParameterInt(request, "numPago");
        Date fechReq = HTMLHelper.getParameterDate(request, "fechaPago");
        fechaPago = (fechReq!=null)?sdf.format(fechReq):fechaPago;
        capital = HTMLHelper.getParameterDouble(request, "capital");
        periodo = HTMLHelper.getParameterInt(request, "periodo");
    }
    
    
    String[] roles = new UsuarioDAO().getRoles();
    if (amortVO == null) 
    {
        command = "altaAmortPagare";
        //obtenemos datos de la pantalla Alta AmortizacionPagare
        /*pagares = CatalogoHelper.getPagaresActivos();
        numPago = HTMLHelper.getParameterInt(request, "numPago");
        fechaPago = HTMLHelper.getParameterDate(request, "fechaPago");
        capital = HTMLHelper.getParameterDouble(request, "capital");
        interes = HTMLHelper.getParameterDouble(request, "tasa");
        */
        //estatus al guardar cambiara a activo = 1
       
    } 
    else 
    {
        command = "modificaAmort";
        amortVO.getIdAmort();
        amortVO.getIdPagare();
        amortVO.getNumPago();
        amortVO.getCapital();

    }
%>
<html>
    <head>
        <title> Alta Amortizaci&oacute;n Pagar&eacute;</title>
        <script type="text/javascript" src="./js/functions.js"></script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
        <script>
            function validaCamposAmort()
            {
                //document.getElementById("boton").disabled = true;
                if(window.document.forma.idPagare.value==0){
                    alert("Es necesario seleccionar un Pagaré");
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                
                if( window.document.forma.fechaPago.value==='' && window.document.forma.fechaPago.value===null)
                {
                    alert("Ingrese la Fecha inicial de los Pago");
                    window.document.forma.fechaPago.focus();
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (!validaFecha(window.document.forma.fechaPago.value))
                {
                    alert('El dato [Fecha Pago] es erróneo, favor de validar e ingresarlo nuevamente');
                    window.document.forma.fechaPago.focus();
                    return false;
		}
                if( !numeroMayorCero(window.document.forma.capital.value))
                {
                    alert("Ingrese el monto de cada pagaré");
                    window.document.forma.capital.focus();
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (!esFormatoMoneda(window.document.forma.capital.value))
                {
                    alert('El dato [monto] es erróneo, favor de validar e ingresarlo nuevamente');
                    window.document.forma.monto.focus();
                    return false;
                        
		}
                if( window.document.forma.periodo.value == -1){
                    alert("Seleccione un periodo de pagos");
                    window.document.forma.periodo.focus();
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if( window.document.forma.numPago.value==='' && window.document.forma.numPago.value > 0){
                    alert("Ingrese el Número de Pagos");
                    window.document.forma.numPago.focus();
                    document.getElementById("boton").disabled = false;
                    return false;
                }
                if (!esEntero(window.document.forma.numPago.value))
                {
                    alert('El dato [Número Pago] es erróneo, favor de validar e ingresarlo nuevamente');
                    window.document.forma.numPago.focus();
                    return false;
                        
		}
                if(!numeroMayorCero(window.document.forma.numPago.value))
                {
                    alert('Ingrese el [Número Pago] mayor a cero');
                    window.document.forma.numPago.focus();
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
            function bloqueaNumPagos(){
                
                if(window.document.forma.periodo.value == 3){
                    window.document.forma.numPa.value = 1;
                    document.getElementById("numPa").disabled = true;
                }else{
                    document.getElementById("numPa").disabled = false;
                }
                
            }
            
        </script>
    </head>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
    <center>
        <h2>Alta Amortizaci&oacute;n Pagar&eacute;</h2> 
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
                <th align="right">Nombre de Pagar&eacute;:</th>
                <td width="50%">  
                    <select name="idPagare" id="idPagare" size="1">
                        <%=HTMLHelper.displayCombo(pagares, idPagare, 0)%>
                    </select>
                </td>
            </tr>
            
            <tr>
                <th align="right">Fecha inicial de Pagos:</th>
                <td align="left"><input type="text" name="fechaPago" placeholder="dd/mm/yyyy" id="fechaInicial" value="<%=fechaPago%>"></td>
            </tr>
            <tr>
                <th align="right">Monto de Pago:</th>
                <td align="left"><input type="text" name="capital" maxlength="9" value = "<%=capital%>"/></td>
            </tr>
            <tr>
                <th align="right">Periodo de pago:</th>
                <td>
                    <select name="periodo" size="1" onchange="bloqueaNumPagos();">
                        <option <%=(periodo==-1)?"selected":""%> value="-1">Selecciona periodo</option>
                        <option <%=(periodo==0)?"selected":""%> value="0">Semanal</option>
                        <option <%=(periodo==1)?"selected":""%> value="1">Mensual</option>
                        <option <%=(periodo==2)?"selected":""%> value="2">Semestral</option>
                        <option <%=(periodo==3)?"selected":""%> value="3">Unico</option>
                    </select>
                </td>
            </tr>
            <tr>
                <th align="right">N&uacute;mero de Pagos:</th>
                <td align="left"><input id="numPa" type="text" name="numPago" maxlength="2" value= "<%=numPago%>"></td>
            </tr>
            <tr>
                <td align="center" colspan="2"><br><input type="button" id="boton" value="Aceptar" onclick="validaCamposAmort()"> <input type="button" id="boton" value="Regresar" onClick="redireccionMenuGestionFondeadores()"><br><br></td>
            </tr>
        </table>
    </form>

</body>
</html>
