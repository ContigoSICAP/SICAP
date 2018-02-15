<%@page import="com.sicap.clientes.dao.OrdenDePagoDAO"%>
<%@page import="java.util.TreeMap"%>
<%@page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@page import="com.sicap.clientes.vo.SaldoIBSVO"%>
<%@page import="com.sicap.clientes.vo.CicloGrupalVO"%>
<%@page import="java.util.Vector"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.util.Notification"%>
<%@ page import="com.sicap.clientes.helpers.HTMLHelper"%>
<html>
    <head>
        <title>Eliminación de ciclo grupal</title>
        <script>
            function consultaCiclo(){
                if (window.document.forma.idEquipo.value==0||window.document.forma.idEquipo.value==''|| !esEntero(window.document.forma.idEquipo.value)){
                    alert('Número de equipo inválido');
                    return false;
		}
                else if (window.document.forma.idCiclo.value==0||window.document.forma.idCiclo.value==''|| !esEntero(window.document.forma.idCiclo.value)){
                    alert('Número de ciclo inválido');
                    return false;
                }
                else {
                    window.document.forma.command.value='consultaCicloEliminacion';
                    window.document.forma.submit();
                }
            }
            function nuevaConsulta(){
                window.document.forma.command.value='eliminaCiclo';
                window.document.forma.submit();
            }
            function eliminaCiclo(){
                if(window.document.forma.comentario.value==""){
                  alert("Favor de ingresar el motivo de eliminación")  
                  return false;
                } else if(!confirm("¿Deseas eliminar el ciclo?")){                    
                    return false;
                } else {
                    window.document.getElementById("botonElimina").disabled=true;                
                    window.document.forma.command.value='eliminarCiclo';
                    window.document.forma.submit();
                }
            }
            function regresar(){
                window.document.forma.command.value='administracionCiclos';
                window.document.forma.submit();
            }
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>
    <%
        TreeMap catCiclos = CatalogoHelper.getCatalogo("c_estatus_ciclo");
        TreeMap catSaldos = CatalogoHelper.getCatalogo("c_estatus_saldos");
        Vector notificaciones = (Vector)request.getAttribute("NOTIFICACIONES");
        CicloGrupalVO ciclo = (CicloGrupalVO)request.getAttribute("CICLO");
        SaldoIBSVO saldo = (SaldoIBSVO)request.getAttribute("SALDO");
        OrdenDePagoDAO ordenPagoDao = new OrdenDePagoDAO();
    %>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
        <center>
            <h3>Eliminación de ciclo grupal</h3>
            <%=HTMLHelper.displayNotifications(notificaciones)%>
            <form name="forma" action="admin" method="POST">
               <input type="hidden" name="command" value="">
               <%if(ciclo!=null){ %>
               <div>
                   <table border="1" cellpadding="0" cellspacing="0" width="35%">
                       <tr>
                           <td width="25%" align="right">Equipo</td>
                           <input type="hidden" name="idEquipo" value="<%=ciclo.getIdGrupo()%>">
                           <input type="hidden" name="idCiclo" value="<%=ciclo.getIdCiclo()%>">
                           <td width="40%" align="left">&nbsp; <%=ciclo.getIdGrupo()%></td>
                       </tr>
                       <tr>
                           <td width="20%" align="right">Nombre</td>
                           <td width="45%" align="left">&nbsp; <%=ciclo.getNombreEquipo()%></td>
                       </tr>
                       <tr>
                           <td width="20%" align="right">Sucursal</td>
                           <td width="45%" align="left">&nbsp; <%=ciclo.getNombreSucursal()%></td>
                       </tr>
                       <tr>
                           <td width="20%" align="right">Ciclo</td>
                           <td width="45%" align="left">&nbsp; <%=ciclo.getIdCiclo()%></td>
                       </tr>
                       <%if(saldo!=null){ %>
                            <tr>
                                <td width="20%" align="right">Estatus</td>
                                <td width="45%" align="left">&nbsp; <%=HTMLHelper.getDescripcion(catSaldos, saldo.getEstatus()) %></td>
                            </tr>
                            <tr>
                                <td width="20%" align="right">Fecha de dispersión</td>
                                <td width="45%" align="left">&nbsp; <%=saldo.getFechaDesembolso()%></td>
                            </tr>
                            <tr>
                                <td width="20%" align="right">Semanas transcurridas</td>
                                <td width="45%" align="left">&nbsp; <%=saldo.getNumeroCuotasTranscurridas()%></td>
                            </tr>                            
                       <%} else {%>
                            <tr>
                                <td width="20%" align="right">Estatus</td>
                                <td width="45%" align="left">&nbsp; <%=HTMLHelper.getDescripcion(catCiclos, ciclo.getEstatus()) %></td>
                            </tr>
                       <%}%>
                       <br>
                       <br>
                   </table>
                   <table>
                       <%if((!ordenPagoDao.odpNoEnviadas(ciclo.getIdGrupo(), ciclo.getIdCiclo()))||(!ordenPagoDao.odpEnviadas(ciclo.getIdGrupo(), ciclo.getIdCiclo()))){%>
                       <tr>
                           <td align="center">Volver a Generar <input type="checkbox" name="generar" size="10" maxlength="10"/></td>
                       </tr>
                       <%} else{%>
                       <input type="hidden" name="generar" value="">
                       <%}%>
                       <tr>
                        <td colspan="2" align="center"><textarea name="comentario" rows="1" cols="45" placeholder="Escribe el motivo de eliminación..."></textarea>                        </td>                        
                       </tr>
                       <tr>
                           <td align="center">
                               <br>
                               <input type="button" id="botonElimina" value="Eliminar Ciclo" onClick="eliminaCiclo()">
                               <input type="button" value="Nueva consulta" onClick="nuevaConsulta()">
                           </td>
                       </tr>
                   </table>
                </div>
               <%} else {%>
                <div>
                    <table border="0" width="100%" cellspacing="0" align="center" height="10%">
                        <tr>
                            <td width="50%" height="10%" align="right">Equipo<br></td>
                            <td width="50%"><input type="text" name="idEquipo" size="10" maxlength="10"></td>
                        </tr>
                        <tr>
                            <td width="50%" height="10%" align="right">Ciclo<br></td>
                            <td width="50%"><input type="text" name="idCiclo" size="10" maxlength="10"></td>
                        </tr>
                        <tr>
                            <td align="right"><br><input type="button" value="Consultar" onclick="consultaCiclo();"></td>
                            <td align="left"><br><input type="button" value="Regresar" onclick="regresar();"></td>
                        </tr>
                    </table>
                </div>
                <%}%>
            </form>
        </center>
    </body>
</html>
