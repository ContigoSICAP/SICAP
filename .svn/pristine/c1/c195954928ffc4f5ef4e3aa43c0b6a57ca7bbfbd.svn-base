<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="java.util.Vector"%>
<%@page import="com.sicap.clientes.util.ClientesConstants"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@page import="com.sicap.clientes.vo.TarjetasVO"%>
<%
Vector notificaciones = (Vector)request.getAttribute("NOTIFICACIONES");
int i = 0;
int numTarjetas = 0;
int banco = HTMLHelper.getParameterInt(request, "banco");
int sucursal = HTMLHelper.getParameterInt(request, "sucursal");
ArrayList<TarjetasVO> arrTarjetas = new ArrayList<TarjetasVO>();
if(!request.getAttribute("TARJETAS").equals("")){
    arrTarjetas = (ArrayList<TarjetasVO>)request.getAttribute("TARJETAS");
    numTarjetas = arrTarjetas.size();
}
String bloqueado = "";
%>
<html>
<head>
    <title>Genera Dispersi&oacute;n Tarjetas</title>
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
    
    function calculaImporteArchivo(totalTarjetas){
        var importe = 0;
        importe = sumaImportes(totalTarjetas);
        importe.toFixed(2);
        document.getElementById("divMessage").innerHTML = '$  '+ importe;
        if( importe > 0 ){
            document.getElementById("getFile").disabled = false;
        }else{
            document.getElementById("getFile").disabled = true;
        }		
    }
    
    function sumaImportes(totalTarjetas){
        var importe = 0;
        var aux     = 0;
        for ( var i=0; i < totalTarjetas ; i++ ){
            var importeDinamico = "importe"+i;
            var estatusDinamico = "estatus"+i;
            if(window.document.getElementById(estatusDinamico).value!=2 && window.document.getElementById(estatusDinamico).value!=3 && window.document.getElementById(estatusDinamico).value!=5 &&window.document.getElementById(estatusDinamico).value!=7 && window.document.getElementById(estatusDinamico).value!=8){
                if(totalTarjetas == 1){
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
    
    function generaTarjetas(totalTarjetas){
        var importe = sumaImportes(totalTarjetas);
        document.getElementById("getFile").disabled = true;
        if(window.confirm("¿ Confirma que deseas crear el Archivo Dispersión Tarjetas por un Importe de $ "+importe +'?')){
            window.document.forma.command.value='generaFondeoTarjetas';
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
            }
        } else {
            document.getElementById("getFile").disabled = false;
        }
    }
    
    function regresar() {
        window.document.forma.command.value='buscaTarjetaDispersa';
        window.document.forma.submit();
    }
</script>
</head>
<body onload='calculaImporteArchivo(<%=numTarjetas%>)'>
    <br><jsp:include page="header.jsp" flush="true"/>
<center>
    <h2>Genera Dispersi&oacute;n Tarjetas</h2>
    <%=HTMLHelper.displayNotifications(notificaciones)%>
    <form name="forma" action="admin" method="post">
        <input type="hidden" name="command" value="">
        <input type="hidden" name="banco" value="<%=banco%>">
        <input type="hidden" name="sucursal" value="<%=sucursal%>">
        <input type="hidden" name="tipo" value="LAYOUTFONDEOT">
        <input type="hidden" name="filename" value="">
        <table border="1" width="90%" align="center" >
            <tr bgcolor="#545454">
                <td width="2%" align="center"><input type="checkbox" name="checkGeneral" id="checkGeneral" onclick="seleccionaTodo()" checked/></td>
                <td class="whitetext" align="center">Sucursal</td>
                <td class="whitetext" align="center">Equipo</td>
                <td class="whitetext" align="center">Cliente</td>
                <td class="whitetext" align="center">Solicitante</td>
                <td class="whitetext" align="center">Solicitud</td>
                <td class="whitetext" align="center">Importe</td>
                <td class="whitetext" align="center">Tarjeta</td>
                <td class="whitetext" align="center">Estatus</td>
                <td class="whitetext" align="center">Fecha Fondeo</td>
                <td class="whitetext" align="center">Num Envio</td>
            </tr>
            <%for(TarjetasVO tarjeta : arrTarjetas) {
                bloqueado = "";
                if(tarjeta.getEstatus()==ClientesConstants.OP_DISPERSADA || tarjeta.getEstatus()==ClientesConstants.OP_COBRADA || tarjeta.getEstatus()==ClientesConstants.OP_CANCELADA || tarjeta.getEstatus()==ClientesConstants.OP_CANCELACION_CONFIRMADA || tarjeta.getEstatus()==ClientesConstants.OP_POR_CONFIRMAR || tarjeta.getEstatus()==ClientesConstants.OP_DEVUELTA || tarjeta.getEstatus()==ClientesConstants.OP_SEGURO_ENVIADO || tarjeta.getEstatus()==ClientesConstants.OP_SEGURO_CANCELADO || tarjeta.getEstatus()==ClientesConstants.OP_SEGURO_CANCELADO_CONFIR){
                    bloqueado = "disabled";
                    }
            %>
            <tr>
                <td align="center"><input type="checkbox" name="idCheckBox" id="idCheckBox" value="<%=i%>" checked <%=bloqueado%> onchange="calculaImporteArchivo('<%=numTarjetas%>')"/></td>
                <td align="left"><%=HTMLHelper.displayField(tarjeta.getSucursalVO().getNombre())%></td>
                <td align="left"><%=HTMLHelper.displayField(tarjeta.getGrupo())%></td>
                <td align="center"><%=HTMLHelper.displayField(tarjeta.getIdCliente())%></td>
                <td align="left"><%=HTMLHelper.displayField(tarjeta.getNombre())%></td>
                <td align="center"><%=HTMLHelper.displayField(tarjeta.getIdSolicitud())%></td>
                <td align="right"><%=HTMLHelper.displayField(tarjeta.getMonto())%></td>
                <td align="left"><%=HTMLHelper.displayField(tarjeta.getTarjeta())%></td>
                <td align="center"><%=HTMLHelper.displayField(tarjeta.getDescEstatus())%></td>
                <td align="left"><%=HTMLHelper.displayField(tarjeta.getFechaEnvio())%></td>
                <td align="center"><%=HTMLHelper.displayField(tarjeta.getEnvio())%></td>
                <input type="hidden" name="nomSucursal<%=i%>" id="nomSucursal<%=i%>" value="<%=tarjeta.getSucursalVO().getNombre()%>">
                <input type="hidden" name="grupo<%=i%>" id="grupo<%=i%>" value="<%=tarjeta.getGrupo()%>">
                <input type="hidden" name="idCliente<%=i%>" id="idCliente<%=i%>" value="<%=tarjeta.getIdCliente()%>">
                <input type="hidden" name="nombre<%=i%>" id="nombre<%=i%>" value="<%=tarjeta.getNombre()%>">
                <input type="hidden" name="idSolicitud<%=i%>" id="idSolicitud<%=i%>" value="<%=tarjeta.getIdSolicitud()%>">
                <input type="hidden" name="importe<%=i%>" id="importe<%=i%>" value="<%=tarjeta.getMonto()%>">
                <input type="hidden" name="estatus<%=i%>" id="estatus<%=i%>" value="<%=tarjeta.getEstatus()%>">
                <input type="hidden" name="idSucursal<%=i%>" id="idSucursal<%=i%>" value="<%=tarjeta.getIdSucursal()%>">
                <input type="hidden" name="nombre<%=i%>" id="nombres<%=i%>" value="<%=tarjeta.getNombre()%>">
                <input type="hidden" name="identificacion<%=i%>" id="identificacion<%=i%>" value="<%=tarjeta.getCarga()%>">
                <input type="hidden" name="tarjeta<%=i%>" id="tarjeta<%=i%>" value="<%=tarjeta.getTarjeta()%>">
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
                    <input type="button" name="getFile" id="getFile" onClick="generaTarjetas(<%=numTarjetas%>)" value="Genera Archivo">
                    <input type="button" id="botonRegresar" onclick="regresar()" value="Regresar">
                </td>
            </tr>
        </table>
    </form>
</center>
    <jsp:include page="footer.jsp" flush="true"/></body>
</body>
</html>
