<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="java.util.Vector"%>
<%@page import="com.sicap.clientes.util.ClientesConstants"%>
<%@page import="com.sicap.clientes.vo.OrdenDePagoVO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%
    //Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");
    Vector notificaciones = (Vector)request.getAttribute("NOTIFICACIONES");
    int i = 0;
    int numOrdenes = 0;
    double montoArchivo = 0;
    int banco = HTMLHelper.getParameterInt(request, "banco");
    int sucursal = HTMLHelper.getParameterInt(request, "sucursal");
    ArrayList<OrdenDePagoVO> arrOrdenes = new ArrayList<OrdenDePagoVO>();
    if(!request.getAttribute("ORDENESPAGO").equals("")){
        arrOrdenes = (ArrayList<OrdenDePagoVO>)request.getAttribute("ORDENESPAGO");
        numOrdenes = arrOrdenes.size();
    }
    String estatus = "";
    String bloqueado = "";
%>
<html>
<head>
    <title>Genera Ordenes de Pago</title>
    <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
<script type="text/javascript">
    function seleccionaTodo(){
        var checa = 1;
        if(window.document.forma.checkGeneral.checked == false)
            checa = 0;
        for (var i = 0; i<window.document.forma.numRegistros.value; i++){
            if(window.document.forma.numRegistros.value == 1)
                window.document.forma.idCheckBox.checked=checa;
            else
                window.document.forma.idCheckBox[i].checked=checa;
        }
        calculaImporteArchivo(window.document.forma.numRegistros.value);
    }
    
    function calculaImporteArchivo(totalOrdenes){
        var importe = 0;
        if(window.document.forma.banco.value == 10){
            importe = sumaImportesTelecom(totalOrdenes);
        }else{
            importe = sumaImportes(totalOrdenes);
        }
        importe.toFixed(2);
        document.getElementById("divMessage").innerHTML = '$  '+ importe;
        if( importe > 0 ){
            document.getElementById("getFile").disabled = false;
        }else{
            document.getElementById("getFile").disabled = true;
        }		
    }
    
    function sumaImportesTelecom(totalOrdenes){
        var importe = 0;
        var aux     = 0;
        for ( var i=0; i < totalOrdenes ; i++ ){
            var importeDinamico = "importe"+i;
            var estatusDinamico = "estatus"+i;
            if(totalOrdenes == 1){
                if(window.document.forma.idCheckBox.checked == true){
                    aux = parseFloat(window.document.getElementById(importeDinamico).value);
                    importe = importe+aux;
                }
            }else{
                if(window.document.forma.idCheckBox[i].checked == true && document.getElementById(estatusDinamico).value == 0){
                    aux = parseFloat(document.getElementById(importeDinamico).value);
                    importe = importe+aux;
                }
            }
        }
        return importe;
    }
    
    function sumaImportes(totalOrdenes){
        var importe = 0;
        var aux     = 0;
        for ( var i=0; i < totalOrdenes ; i++ ){
            var importeDinamico = "importe"+i;
            var estatusDinamico = "estatus"+i;
            if(window.document.getElementById(estatusDinamico).value!=2 && window.document.getElementById(estatusDinamico).value!=3 && window.document.getElementById(estatusDinamico).value!=5 &&window.document.getElementById(estatusDinamico).value!=7 && window.document.getElementById(estatusDinamico).value!=8){
                if(totalOrdenes == 1){
                    if(window.document.forma.idCheckBox.checked == true){
                        aux = parseFloat(window.document.getElementById(importeDinamico).value);
                        importe = importe+aux;
                    }
                }else{
                    if(window.document.forma.idCheckBox[i].checked == true){
                        aux = parseFloat(document.getElementById(importeDinamico).value);
                        importe = importe+aux;
                    }
                }
            }
        }
        return importe;
    }
    
    function generaOrdenDePago(totalOrdenes){
        var importe = sumaImportes(totalOrdenes);
        document.getElementById("getFile").disabled = true;
        if(window.confirm("¿ Confirma que deseas Crear un Archivo de Dispersión por un Importe de $ "+importe +'?')){
            /*if(window.document.forma.banco.value==10){
                window.document.forma.command.value='generaOrdenesPagos';
            }else{
                window.document.forma.command.value='generaOrdenDePago';
            }*/
            window.document.forma.command.value='generaOrdenesPagos';
            var pasa = false;
            var verifica = false;
            for(var i = 0; i<window.document.forma.numRegistros.value; i++){
                if(window.document.forma.numRegistros.value == 1)
                    verifica = window.document.forma.idCheckBox.checked;
                else
                    verifica = window.document.forma.idCheckBox[i].checked;
                if(verifica){
                    pasa = true;
                }
            }
            if(pasa){
                window.document.forma.submit();
            }else{
                alert("Debe seleccionar algun equipo");
            }
        } else {
            document.getElementById("getFile").disabled = false;
        }
    }
</script>
</head>
<body onload='calculaImporteArchivo(<%=numOrdenes%>)'>
    <br><jsp:include page="header.jsp" flush="true"/>
<center>
    <h2>Administraci&oacute;n de Ordenes de Pago</h2>
    <%=HTMLHelper.displayNotifications(notificaciones)%>
    <form name="forma" action="admin" method="post">
        <input type="hidden" name="command" value="">
        <input type="hidden" name="banco" value="<%=banco%>">
        <input type="hidden" name="sucursal" value="<%=sucursal%>">
        <input type="hidden" name="tipo" value="LAYOUTODP">
        <input type="hidden" name="filename" value="x">
        <table border="1" width="90%" align="center" >
            <tr bgcolor="#545454">
                <td width="2%" align="center"><input type="checkbox" name="checkGeneral" id="checkGeneral" onclick="seleccionaTodo()" checked/></td>
                <!--<td class="whitetext" align="center">Producto</td>-->
                <td class="whitetext" align="center">Sucursal</td>
                <td class="whitetext" align="center">Equipo</td>
                <td class="whitetext" align="center">Cliente</td>
                <td class="whitetext" align="center">Solicitante</td>
                <td class="whitetext" align="center">Solicitud</td>
                <td class="whitetext" align="center">Importe</td>
                <%if(banco!=10){%>
                <td class="whitetext" align="center">Fecha Creacion ODP</td>
                <td class="whitetext" align="center">Referencia</td>
                <%}%>
                <td class="whitetext" align="center">Estatus</td>
                <td class="whitetext" align="center">Fecha Envio ODP</td>
                <td class="whitetext" align="center">Num Envio</td>
            </tr>
            <%for(OrdenDePagoVO ordenPago : arrOrdenes) {%>
            <tr>
                <%if(banco == 10){
                    if(ordenPago.getEstatus()==0){
                        estatus = "Sin Envio";
                        bloqueado = "";
                    }else if (ordenPago.getEstatus()==1){
                        estatus = "Enviado";
                        bloqueado = "disabled";
                    }else{
                        estatus = "--";
                        bloqueado = "";
                    }
                }else{
                    bloqueado = "";
                    estatus = ordenPago.getDescEstatus();
                    if(ordenPago.getEstatus()==ClientesConstants.OP_DISPERSADA || ordenPago.getEstatus()==ClientesConstants.OP_COBRADA || ordenPago.getEstatus()==ClientesConstants.OP_CANCELADA || ordenPago.getEstatus()==ClientesConstants.OP_CANCELACION_CONFIRMADA || ordenPago.getEstatus()==ClientesConstants.OP_POR_CONFIRMAR || ordenPago.getEstatus()==ClientesConstants.OP_DEVUELTA || ordenPago.getEstatus()==ClientesConstants.OP_SEGURO_ENVIADO || ordenPago.getEstatus()==ClientesConstants.OP_SEGURO_CANCELADO || ordenPago.getEstatus()==ClientesConstants.OP_SEGURO_CANCELADO_CONFIR){
                        bloqueado = "disabled";
                    }
                }%>
                <td align="center"><input type="checkbox" name="idCheckBox" id="idCheckBox" value="<%=i%>" checked <%=bloqueado%> onchange="calculaImporteArchivo('<%=numOrdenes%>')"/></td>
                <!--<td align="center"><%=HTMLHelper.displayField("1")%>&nbsp;&nbsp;</td>-->
                <td align="left"><%=HTMLHelper.displayField(ordenPago.getNomSucursal())%></td>
                <td align="left"><%=HTMLHelper.displayField(ordenPago.getGrupo())%></td>
                <td align="center"><%=HTMLHelper.displayField(ordenPago.getIdCliente())%></td>
                <td align="left"><%=HTMLHelper.displayField(ordenPago.getNombre())%></td>
                <td align="center"><%=HTMLHelper.displayField(ordenPago.getIdSolicitud())%></td>
                <td align="right"><%=HTMLHelper.displayField(ordenPago.getMonto())%></td>
                <%if(banco!=10){%>
                <td align="center"><%=ordenPago.getFechaCaptura()%>&nbsp;&nbsp;</td>
                <td align="center"><%=HTMLHelper.displayField(ordenPago.getReferencia())%>&nbsp;&nbsp;</td>
                <input type="hidden" name="idOrdenPago<%=i%>" id="idOrdenPago<%=i%>" value="<%=ordenPago.getIdOrdenPago()%>">
                <input type="hidden" name="referencia<%=i%>" id="referencia<%=i%>" value="<%=ordenPago.getReferencia()%>">
                <%}%>
                <td align="center"><%=HTMLHelper.displayField(estatus)%></td>
                <%if(ordenPago.getEstatus() == ClientesConstants.OP_DESEMBOLSADO || ordenPago.getEstatus() == ClientesConstants.OP_SEGURO){%>
                <td align="center">&nbsp;&nbsp;</td>
                <%}else{%>
                <td align="center"><%=ordenPago.getFechaEnvio()%>&nbsp;&nbsp;</td>
                <%}%>
                <td align="center"><%=HTMLHelper.displayField(ordenPago.getNumEnvio())%>&nbsp;&nbsp;</td>
                <input type="hidden" name="nomSucursal<%=i%>" id="nomSucursal<%=i%>" value="<%=ordenPago.getNomSucursal()%>">
                <input type="hidden" name="grupo<%=i%>" id="grupo<%=i%>" value="<%=ordenPago.getGrupo()%>">
                <input type="hidden" name="idCliente<%=i%>" id="idCliente<%=i%>" value="<%=ordenPago.getIdCliente()%>">
                <input type="hidden" name="nombre<%=i%>" id="nombre<%=i%>" value="<%=ordenPago.getNombre()%>">
                <input type="hidden" name="idSolicitud<%=i%>" id="idSolicitud<%=i%>" value="<%=ordenPago.getIdSolicitud()%>">
                <input type="hidden" name="importe<%=i%>" id="importe<%=i%>" value="<%=ordenPago.getMonto()%>">
                <input type="hidden" name="estatus<%=i%>" id="estatus<%=i%>" value="<%=ordenPago.getEstatus()%>">
                <input type="hidden" name="idSucursal<%=i%>" id="idSucursal<%=i%>" value="<%=ordenPago.getIdSucursal()%>">
                <input type="hidden" name="nombres<%=i%>" id="nombres<%=i%>" value="<%=ordenPago.getNombres()%>">
                <input type="hidden" name="apaterno<%=i%>" id="apaterno<%=i%>" value="<%=ordenPago.getApaterno()%>">
                <input type="hidden" name="amaterno<%=i%>" id="amaterno<%=i%>" value="<%=ordenPago.getAmaterno()%>">
                <input type="hidden" name="idOficTelecom<%=i%>" id="idOficTelecom<%=i%>" value="<%=ordenPago.getIdOficTelecom()%>">
                <input type="hidden" name="nomOficTelecom<%=i%>" id="nomOficTelecom<%=i%>" value="<%=ordenPago.getNomOficTelecom()%>">
                <input type="hidden" name="identificacion<%=i%>" id="identificacion<%=i%>" value="<%=ordenPago.getIdentificacion()%>">
                <input type="hidden" name="idOdPFechEnv<%=i%>" id="idOdPFechEnv<%=i%>" value="<%=ordenPago.getFechaEnvio()%>">
            </tr>
            <%  i++;
            }%>
        </table>
        <input type="hidden" name="numRegistros" id="numRegistros" value="<%=i%>"/>
        <table width="90%" border="0" cellspacing="2" class="maintitle">
            <tr>
                <td width="87%" align="right"> Importe Dispersiones</td>
                <td width="13%" align="center" bgcolor="#FB9A33">
                    <div id="divMessage"/>
                </td>
            </tr>
            <tr>
                <td height="46" colspan="2" align="right">
                    <label><input name="getFile" type="button" class="text" id="getFile" onClick="generaOrdenDePago(<%=numOrdenes%>)" value="Genera Archivo"></label>
                </td>
            </tr>
        </table>
    </form>
</center>
    <jsp:include page="footer.jsp" flush="true"/></body>
</body>
</html>
