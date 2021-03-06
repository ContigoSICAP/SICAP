<%@page import="com.sicap.clientes.util.Convertidor"%>
<%@page import="com.sicap.clientes.vo.BitacoraCicloVO"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.TreeMap"%>
<%@page import="com.sicap.clientes.helpers.CatalogoHelper"%>
<%@page import="java.util.Vector"%>
<%@page import="com.sicap.clientes.helpers.HTMLHelper"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<!DOCTYPE html>
<%
    TreeMap catEstatus = CatalogoHelper.getCatalogoEstatusCicloCombo();
    ArrayList<BitacoraCicloVO> registros = (ArrayList<BitacoraCicloVO>)request.getAttribute("REGISTROS");    
%>
<html>
    <head>
        <title>Historial Comentarios</title>
        <script>            
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>
    <%Vector notificaciones = (Vector)request.getAttribute("NOTIFICACIONES");%>
    <body leftmargin="0" topmargin="0">
        <center>
            <%=HTMLHelper.displayNotifications(notificaciones)%>
        </center>
            <%if(registros!=null&&registros.size()>0){%>
                <div>
                    <table border="0" width="95%" align="center">
                        <tr bgcolor="#343482" class="whitetext">
                            <td align="center">Fecha</td> 
                            <td align="center">Estatus</td> 
                            <td align="center">Modificación</td>
                            <td align="center">Asignado</td>
                            <td align="center">Comentario</td>
                        </tr>
                        <%for(BitacoraCicloVO registro : registros){%>
                        <tr>
                            <td align="center"><%=HTMLHelper.displayField(registro.getFechaHora())%></td>
                            <td align="left"><%=HTMLHelper.getDescripcion(catEstatus, registro.getEstatus())%></td>
                            <td align="left"><%=HTMLHelper.displayField(registro.getUsuarioComentario())%></td>
                            <td align="left"><%=HTMLHelper.displayField(registro.getUsuarioAsignado())%></td>
                            <td align="left"><%=HTMLHelper.displayField(registro.getComentario())%></td>                            
                        </tr>
                        <%}%>
                    </table>
                </div>
           <%}%>           
    </body>
</html>