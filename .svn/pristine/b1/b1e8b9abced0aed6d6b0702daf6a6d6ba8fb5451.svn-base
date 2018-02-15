<%@page import="com.sicap.clientes.dao.RenovacionDAO"%>
<%@page import="com.sicap.clientes.vo.RenovacionVO"%>
<%@page import="com.sicap.clientes.vo.PlaneacionRenovacionVO"%>
<%@page import="com.sicap.clientes.dao.PlaneacionRenovacionDAO"%>
<%@page import="com.sicap.clientes.dao.EjecutivoCreditoDAO"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>

<%@ page import="com.sicap.clientes.vo.UsuarioVO"%>
<%@ page import="com.sicap.clientes.vo.GrupoVO"%>
<%@ page import="com.sicap.clientes.vo.EjecutivoCreditoVO"%>
<%@ page import="com.sicap.clientes.vo.MetasEjecutivosVO"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.*"%>

<%
	UsuarioVO usuario = (UsuarioVO)session.getAttribute("USUARIO");
	TreeMap catSucursales = CatalogoHelper.getCatalogoSucursal(usuario.sucursales);
        TreeMap catMeses = CatalogoHelper.getCatalogoMeses();
        TreeMap catYear = CatalogoHelper.getCatalogoYear();
	Vector notificaciones = (Vector)request.getAttribute("NOTIFICACIONES");
	ArrayList<GrupoVO> equiposNoRenovados = (ArrayList<GrupoVO>)request.getAttribute("EQUIPOSNORENOVADOS");
        TreeMap catNoRenovacion = CatalogoHelper.getCatalogo(ClientesConstants.CAT_MOTIVOS_NO_RENOVACION);
	Calendar cal = Calendar.getInstance();
        Date dateNow = cal.getTime();
        System.out.println("DATE: "+dateNow);
        Calendar fechaVencimiento = Calendar.getInstance();        
%>
<html>
    <head>
        <title>Equipos No Renovados</title>
        <script language="Javascript" src="./js/functions.js"></script>
        <script type="text/javascript">
            
            function consultaNoRenovados(){
                if ( window.document.forma.idMonth.value==0){
                    alert('Debe seleccionar el mes de consulta');
                    return false;
                }                
                window.document.forma.command.value='consultaNoRenovados';
                window.document.forma.submit();
            }
            
            function guardaNoRenovados(){
                if(!confirm("¿Desea guardar los motivos de renovación seleccionados?")){
                    return false;
                } else {
                    window.document.forma.command.value='guardaNoRenovados';
                    window.document.forma.submit();
                }
            }
            
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
        <form action="admin" method="post" name="forma">
            <input type="hidden" name="command" value="">            
            <input type="hidden" name="idEquipo" value="">
            <input type="hidden" name="idCiclo" value="">
            <input type="hidden" name="idMotivo" value="">
            <input type="hidden" name="numIntegrantes" value="">
            <input type="hidden" name="fechaVencimiento" value="">            
            <table border="0" width="100%">
                <tr>
                    <td colspan="2" align="center">
                        <h3>Equipos sin Renovación<br></h3>
                        <%=HTMLHelper.displayNotifications(notificaciones)%>
                    </td>
                </tr>
                <tr>
                    <td width="50%" align="right">Sucursal</td>
                    <td width="50%" align="left">
                        <select name="idSucursal" ><%=HTMLHelper.displayCombo(catSucursales, HTMLHelper.getParameterInt(request, "idSucursal"))%></select>
                    </td>
                </tr>
                <tr>
                    <td width="50%" align="right">Mes</td>
                    <%if(HTMLHelper.getParameterInt(request, "idMonth")!=0){%>
                    <td width="50%" align="left"><select name="idMonth"><%=HTMLHelper.displayCombo(catMeses, HTMLHelper.getParameterInt(request, "idMonth"))%></select></td>
                    <%} else {%>
                    <td width="50%" align="left"><select name="idMonth"><%=HTMLHelper.displayCombo(catMeses, cal.get(Calendar.MONTH)+1)%></select></td>
                    <%}%>
                </tr>
                <tr>
                    <td width="50%" align="right">Año</td>
                    <%if(HTMLHelper.getParameterInt(request, "idYear")!=0){%>
                    <td width="50%" align="left"><select name="idYear"><%=HTMLHelper.displayCombo(catYear, HTMLHelper.getParameterInt(request, "idYear"))%></select></td>
                    <%} else {%>
                    <td width="50%" align="left"><select name="idYear"><%=HTMLHelper.displayCombo(catYear, 2)%></select></td>
                    <%}%>
                </tr>
                <tr>
                    <td colspan="2" align="center">
                        <br>
                        <input type="button" name="" value="Consultar" onClick="consultaNoRenovados();">
                        <br><br>
                    </td>
                </tr>
                <%if (equiposNoRenovados!=null&&equiposNoRenovados.size()>0){%>
                <input type="hidden" name="equipos" value="<%=equiposNoRenovados.size() %>">
                <tr>
                    <td colspan="2" align="center">
                        <table border="1" width="90%" align="center">
                            <tr>
                                <td width="3%" 	align="center">No.Equipo</td>
                                <td width="15%" align="center">Equipo</td>
                                <td width="3%" 	align="center">Calificación</td>
                                <td width="3%"  align="center">Ciclo</td>
                                <td width="3%"  align="center">No. Integrantes</td>
                                <td width="10%"  align="center">Asesor</td>
                                <td width="7%" 	align="center">Fecha de vencimiento</td>
                                <td width="5%" 	align="center">Motivo no renovación</td>
                            </tr>
                    <%for ( int i=0 ; i < equiposNoRenovados.size() ; i++ ){
                        EjecutivoCreditoVO ejecutivo = new EjecutivoCreditoDAO().getEjecutivo(equiposNoRenovados.get(i).ciclos[0].asesor);
                        String nombreEjecutivo = ejecutivo.getNombre()+" "+ ejecutivo.getaPaterno()+" "+ ejecutivo.getaMaterno();                        
                    %>
                    <tr>
                    <input type="hidden" name="bandera<%=i%>" value="0">
                    <input type="hidden" name="idEjecutivo<%=i%>" value="<%=ejecutivo.getIdEjecutivo() %>">
                    <input type="hidden" name="idGrupo<%=i%>" value="<%=equiposNoRenovados.get(i).idGrupo %>">
                        <td width="3%" align="center"> <%=equiposNoRenovados.get(i).idGrupo %></td>
                        <td width="15%" align="center"> <%=equiposNoRenovados.get(i).nombre %></td>
                        <td width="3%" align="center"> <%=equiposNoRenovados.get(i).calificacion %></td>
                        <input type="hidden" name="idCiclo<%=i%>" value="<%=equiposNoRenovados.get(i).idCicloOriginal %>">
                        <td width="3%"	align="center"> <%=equiposNoRenovados.get(i).idCicloOriginal %></td>
                        <input type="hidden" name="numIntegrantes<%=i%>" id="numIntegrantes<%=i%>" value="<%=equiposNoRenovados.get(i).ciclos[0].numIntegrantes %>">
                        <td width="3%"	align="center">	<%=equiposNoRenovados.get(i).ciclos[0].numIntegrantes %></td>
                        <td width="10%"	align="center">	<%=nombreEjecutivo %></td>
                        <input type="hidden" name="vencimiento<%=i%>" id="vencimiento<%=i%>" value="<%=Convertidor.dateToString(equiposNoRenovados.get(i).ciclos[0].fechaDispersion) %>">
                        <td width="7%"	align="center">	<%=equiposNoRenovados.get(i).ciclos[0].fechaDispersion %></td>
                        <td width="5%"	align="center">
                            <select name="motivo<%=i%>" id="motivo<%=i%>" ><%=HTMLHelper.displayCombo(catNoRenovacion, 0)%></select>
                        </td>
                    </tr>
                    <%}%>
                <tr></tr>
                    <tr></tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" align="center">
                        <br>
                        <input type="button" name="" value="Guardar" onClick="guardaNoRenovados();">
                        <br><br>
                    </td>
                <%}%>
                </tr>
                <tr><td colspan="2" align="center"><br><a href="<%=request.getContextPath() %>">Inicio</a></td></tr>
            </table>
        </form>
        <jsp:include page="footer.jsp" flush="true"/>
    </body>
</html>