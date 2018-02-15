<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.vo.OrdenDePagoVO"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.*"%>

<%
    TreeMap catSucursales = CatalogoHelper.getCatalogoSucursales();
    OrdenDePagoVO oPago = (OrdenDePagoVO) request.getAttribute("ORDENDEPAGO");
    Vector notificaciones = (Vector) request.getAttribute("NOTIFICACIONES");
    boolean isOrdenPagoAdicional = (request.getAttribute("ISORDENPAGOADICIONAL")) != null ? ((Boolean)request.getAttribute("ISORDENPAGOADICIONAL")).booleanValue() : false;
    boolean clienteTieneOrdPagAdicionales = (request.getAttribute("CTETIENEORDPAGADICIONALES")) != null 
            ? ((Boolean)request.getAttribute("CTETIENEORDPAGADICIONALES")).booleanValue() : false;     

    boolean procedeCancelAdic = (request.getAttribute("PROCEDECANCELACIONADICIONAL")) != null ? ((Boolean)request.getAttribute("PROCEDECANCELACIONADICIONAL")).booleanValue() : true;
%>

<html>
    <head>
        <title>Cancelación de Ordenes de Paos</title>
        <script language="Javascript" src="./js/jquery-1.4.2.min.js"></script>
        <script language="Javascript" src="./js/functions.js"></script>
        <script type="text/javascript">
            function consultaOrden() {
                if (window.document.forma.sucursal.value == 0) {
                    alert('Debe seleccionar una sucursal');
                    return false;
                }

                if (window.document.forma.referencia.value == '') {
                    alert('Ingrese el Número de Orden de Pago');
                    return false;
                }

                if (window.document.forma.idCliente.value == '') {
                    alert('Ingrese un número de cliente');
                    return false;
                }

                window.document.forma.command.value = 'consultaOrdenDePago';
                window.document.forma.submit();

            }

            function cancelaOrdenPago(idOrden) {

                var i
                for (i = 0; i < window.document.forma.operacion.length; i++) {
                    if (window.document.forma.operacion[i].checked)
                        break;
                }
                var operacion = window.document.forma.operacion[i].value
                var comando = 'cancelaOrdenDePago';
                
                
            <%if (oPago != null && oPago.getEstatus() != ClientesConstants.OP_POR_CONFIRMAR && oPago.getIdOperacion() == ClientesConstants.GRUPAL) {%>
                if (operacion == 2 || operacion == 3) {
                    comando = 'cancelaOrdenDePagoConfirmada';
                }
            <%}%>
                
                var mensaje = 'Estás seguro que deseas cancelar la Orden de pago Actual ?';

                if (operacion == 1)
                    mensaje = 'Estás seguro que deseas Actualizar la Orden de pago Actual ?';

                if (confirm(mensaje)) {
                    window.document.forma.idOperacion.value = operacion;
                    window.document.forma.idOrden.value = idOrden;
                    window.document.forma.command.value = comando;
                    window.document.forma.operacion.value = window.document.forma.operacion.checked;
                    window.document.forma.ordenPagoAdicional.value=<%=isOrdenPagoAdicional%>
                    window.document.forma.submit();
                }
            }
        </script>
        <script>
            
            
            
            $(document).ready(function(){
                $('#submit1').click(function(){
                    
                    <%if(null != oPago){%>
                             
                        if(<%=!isOrdenPagoAdicional%>){
                            cancelaOrdenPago(<%=oPago.getIdOrdenPago()%>);
                        }
                        else if(<%=isOrdenPagoAdicional%>){
                            var operacion = $('input[name=operacion]:checked', '#forma').val()
                            
                            if(operacion !== undefined){
                                if(operacion == 3){
                                    alert("La opción \"Cancela Orden de Pago(Trans. Gerente)\" no es válida cuando se trata de un crédito adicional");
                                }else{
                                    if(<%=!procedeCancelAdic%>){
                                        alert("Fecha no válida para realizar la cancelacion de de orde de pago de un adicional");
                                    }else{
                                        cancelaOrdenPago(<%=oPago.getIdOrdenPago()%>);
                                    }
                                    
                                }
                            }
                        }  
                    <%}%>
                        
                                 
                });
            });
                
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">

    </head>

    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
    <center>
        <h2>Modifica Orden de Pago</h2>  
        <div id="divNotificacion">
            <%=HTMLHelper.displayNotifications(notificaciones)%>
        </div>
        
        <form action="admin" method="post" name="forma" id="forma">
            <input type="hidden" name="command" value="">
            <input type="hidden" name="idOrden" value="">
            <input type="hidden" name="idOperacion" value="">
            <input type="hidden" name="ordenPagoAdicional" value="">
            
            <%if (oPago == null || clienteTieneOrdPagAdicionales) {%>
            <table border="0" width="90%" align="center" >
                <tr>
                    <td width="50%" align="right">Sucursal</td>
                    <td width="50%"><select name="sucursal"><%=HTMLHelper.displayCombo(catSucursales, 0)%></select></td>
                </tr>
                <tr>
                    <td width="50%" align="right">No. de Orden</td>
                    <td width="50%">
                        <input type="text" name="referencia" size="13" maxlength="13" value="" />
                    </td>
                </tr>
                <tr>
                    <td width="50%" align="right">No. Cliente</td>
                    <td width="50%">
                        <input type="text" name="idCliente" size="7" maxlength="7" value="" />
                    </td>
                </tr>
                <tr>
                    <td colspan="2" align="center">
                        <br>
                        <input type="button" name="" value="Consultar" onClick="consultaOrden();" >
                        <br><br>
                    </td>
                </tr>

            </table>		
            

            <%}else if (oPago != null) {%>
            <table border="0" width="80%" align="center" >

                <tr bgcolor="#009865" class="whitetext">
                    <td align="center">Orden de Pago</td>
                    <td align="center">No. Cliente</td>
                    <td align="center">No. Solicitud</td>
                    <td align="center">Nombre</td>  	
                    <td align="center">Monto</td>  	  	
                </tr>

                <tr>
                    <td align="center"><%=HTMLHelper.displayField(oPago.getReferencia())%></td>
                    <td align="center"><%=HTMLHelper.displayField(oPago.getIdCliente())%></td>
                    <td align="center"><%=HTMLHelper.displayField(oPago.getIdSolicitud())%></td>
                    <td align="center"><%=HTMLHelper.displayField(oPago.getNombre())%></td>
                    <td align="center"><%=HTMLHelper.formatoMonto(oPago.getMonto())%></td>
                </tr>
                <tr>
                    <td align="center" colspan="5">
                        <input type="hidden" name="operacion">
                        <%if (request.isUserInRole("CAMBIO_ESTATUS_CHEQUE")) { %>
                        <input type="radio" id="operacion" name="operacion" value="2"/> Cancela Orden de Pago
                        <input type="radio" id="operacion" name="operacion" value="3"/> Cancela Orden de Pago(Trans. Gerente)
                        <%}%>
                        <%if (request.isUserInRole("SOPORTE_OPERATIVO")) { %>
                        <input type="radio" id="operacion" name="operacion" value="1"/> Actualiza Nombre
                        <%}%>	
                    </td>
                </tr>
                <tr>
                    <td align="center" colspan="5">
                        <input type="button" id="submit1" name="" value="Enviar" >
                    </td>
                </tr>

            </table>
            <%}%>
        </form>
    </center>
    <jsp:include page="footer.jsp" flush="true"/>
</body>
</html>
