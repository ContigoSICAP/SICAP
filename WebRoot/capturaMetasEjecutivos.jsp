<%@page import="com.sicap.clientes.dao.PlaneacionRenovacionDAO"%>
<%@page import="com.sicap.clientes.vo.PlaneacionRenovacionVO"%>
<%@page import="com.sicap.clientes.vo.MetasEjecutivosVO"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>

<%@ page import="com.sicap.clientes.vo.UsuarioVO"%>
<%@ page import="com.sicap.clientes.vo.GrupoVO"%>
<%@ page import="com.sicap.clientes.vo.EjecutivoCreditoVO"%>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="java.util.*"%>

<%
    UsuarioVO usuario = (UsuarioVO) session.getAttribute("USUARIO");
    TreeMap catSucursales = CatalogoHelper.getCatalogoSucursal(usuario.sucursales);
    TreeMap catBinario = CatalogoHelper.getCatalogoBinario();
    TreeMap catNoRenovacion = CatalogoHelper.getCatalogo(ClientesConstants.CAT_MOTIVOS_NO_RENOVACION);
    ArrayList<GrupoVO> grupos = (ArrayList<GrupoVO>)request.getAttribute("GRUPOS");
    PlaneacionRenovacionDAO planeacionDao = new PlaneacionRenovacionDAO();
    EjecutivoCreditoVO ejecutivo = (EjecutivoCreditoVO) request.getAttribute("EJECUTIVO");
    MetasEjecutivosVO metaEjecutivo = (MetasEjecutivosVO) request.getAttribute("METAEJECUTIVO");
    TreeMap catEjecutivos = CatalogoHelper.getCatalogoEjecutivos(HTMLHelper.getParameterInt(request, "idSucursal"), "A");
    Vector notificaciones = (Vector)request.getAttribute("NOTIFICACIONES");
    String nombreEjecutivo = "";

    int numGrupos = 0;
    if (ejecutivo != null) {
        nombreEjecutivo = ejecutivo.nombre + " " + " " + ejecutivo.aPaterno + " " + ejecutivo.aMaterno;
    }
    if (grupos != null) {
        numGrupos = grupos.size();
    }
%>
<html>
    <head>
        <title>Captura de metas ejecutivos</title>
        <script language="Javascript" src="./js/functions.js"></script>
        <script type="text/javascript">
            
            function consultaGrupos() {
                if (window.document.forma.idEjecutivo.value == 0) {
                    alert('Debe seleccionar un asesor');
                    return false;
                }
                window.document.forma.command.value = 'consultaEquiposRenovacion';
                window.document.forma.submit();
            }

            function buscaEjecutivos() {
                window.document.forma.command.value = 'buscaEjecutivosPlaneacion';
                window.document.forma.submit();
            }

            function guardaPlaneacion() {
                var nointegrantes = 0;
                var sumaIntegrantes = 0;
                if (window.document.forma.clientesNuevos.value == "") {
                    alert('Debe ingresar el número de clientes nuevos');
                    return false;
                }
                if (window.document.forma.meta.value == 0) {
                    alert('Debe ingresar la meta mensual del asesor seleccionado');
                    return false;
                }
                var numgrupos = window.document.forma.grupos.value;
                for (var i = 0; i < numgrupos; i++) {
                    var renueva = "renueva" + i;
                    var motivo = "motivo" + i;
                    var integrante = "integrante" + i;
                    var integranteTotal = "integranteTotal" + i;
                    var elemento1 = document.getElementById(renueva);
                    var elemento2 = document.getElementById(motivo);
                    var elemento3 = document.getElementById(integrante);
                    var integrante = document.getElementById(integranteTotal);
                    if (elemento1.value == 0) {
                        alert('Debe especificar si habrá renovación en los equipos');
                        return false;
                    } else {
                        if (elemento1.value == 1) {
                            if (integrante.value == 0) {
                                alert('Debe especificar el número total de integrantes del equipo');
                                return false;
                            } else {
                                nointegrantes += parseInt(elemento3.value);
                            }
                        }
                        if (elemento1.value == 2 && elemento2.value == 0) {
                            alert('Debe especificar el motivo de no renovación');
                            return false;
                        }
                    }
                    sumaIntegrantes+= parseInt(integrante.value);                    
                }
                var nuevos = parseInt(window.document.forma.clientesNuevos.value);
                var sumaClientes = sumaIntegrantes+nuevos;
                if(sumaClientes!=window.document.forma.meta.value){
                    alert('La meta no coincide con la suma de integrantes totales y clientes nuevos');
                    return false;
                }
                if(!confirm("¿Desea guardar la meta con los datos proporiconados?")){
                    return false;
                } else {                    
                    window.document.forma.integrantes.value = nointegrantes;
                    window.document.forma.integrantesTotal.value = sumaIntegrantes;
                    window.document.forma.command.value = 'guardaPlaneacion';
                    window.document.forma.submit();
                }
            }
            
            function activaCombos(i){
                if(window.document.getElementById("renueva"+i).value==1){
                    window.document.getElementById("motivo"+i).disabled=true;
                    window.document.getElementById("integranteTotal"+i).disabled=false;
                } else if (window.document.getElementById("renueva"+i).value==2){
                    window.document.getElementById("motivo"+i).disabled=false;
                    window.document.getElementById("integranteTotal"+i).disabled=true;
                    window.document.getElementById("integranteTotal"+i).value=0;
                }
            }
            
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>
    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
        <form action="admin" method="post" name="forma">
            <input type="hidden" name="command" value="">
            <input type="hidden" name="integrantes" value="">
            <input type="hidden" name="integrantesTotal" value="">
            <input type="hidden" name="grupos" value=<%=numGrupos%>>
            <table border="0" width="100%">
                <tr>
                    <td colspan="2" align="center">
                        <h3>Planeación de Renovación<br></h3>
                        <%=HTMLHelper.displayNotifications(notificaciones)%>
                    </td>
                </tr>
                <tr>
                    <td width="50%" align="right">Sucursal</td>
                    <td width="50%" align="left">
                        <select name="idSucursal" onchange="buscaEjecutivos()"><%=HTMLHelper.displayCombo(catSucursales, HTMLHelper.getParameterInt(request, "idSucursal"))%></select>
                    </td>
                </tr>
                <tr>
                    <td width="50%" align="right">Asesor</td>
                    <td width="50%" align="left">
                        <select name="idEjecutivo"><%=HTMLHelper.displayCombo(catEjecutivos, HTMLHelper.getParameterInt(request, "idEjecutivo"))%></select>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" align="center">
                        <br>
                        <input type="button" name="" value="Consultar" onClick="consultaGrupos();" >
                        <br><br>
                    </td>
                </tr>
                <%if (grupos != null) {%>
                <tr>
                    <td colspan="2" align="center">
                        <table border="1" cellpadding="1" width="100%" >
                            <tr bgcolor="#343482" class="whitetext">
                                <td width="8%" 	align="center">No.Asesor: <%=ejecutivo.idEjecutivo%></td>
                                <td width="20%" align="center">Asesor: <%= nombreEjecutivo.toUpperCase()%></td>
                                <%if(metaEjecutivo!=null){ %>
                                    <td width="5%"  align="right">Clientes Nuevos:<input type="text" size="3" name="clientesNuevos" maxlength="3" value="<%=metaEjecutivo.clientesNuevos%>"></td>
                                    <td width="5%"  align="right">Meta:<input type="text" size="3" name="meta" maxlength="3" value="<%=metaEjecutivo.meta%>"></td>
                                <%} else {%>
                                    <td width="5%"  align="right">Clientes Nuevos:<input type="text" size="3" name="clientesNuevos" maxlength="3" value="0"></td>
                                    <td width="5%"  align="right">Meta:<input type="text" size="3" name="meta" maxlength="3" value="0"></td>
                                <%}%>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" align="center">
                        <table border="1" width="100%" align="center">
                            <tr>
                                <td width="2%" 	align="center">Equipo</td>
                                <td width="8%" align="center">Nombre</td>
                                <td width="4%" 	align="center">Calificación</td>
                                <td width="2%"  align="center">Ciclo</td>
                                <td width="5%" 	align="center">Fecha vencimiento</td>
                                <td width="5%"	align="center">No.Integrantes</td>
                                <td width="4%" 	align="center">Renovación</td>
                                <td width="7%" 	align="center">Motivo no renovación</td>
                                <td width="5%" 	align="center">No.Integrantes Totales</td>
                            </tr>
                            <%for (int i = 0; grupos != null && i < grupos.size(); i++) {%>
                                <tr>
                                    <input type="hidden" name="idGrupo<%=i%>" value="<%=grupos.get(i).idGrupo%>">
                                    <td width="3%" 	align="center"> <%=grupos.get(i).idGrupo%></td>
                                    <td width="10%"	align="center"> <%=grupos.get(i).nombre%></td>
                                    <td width="5%"	align="center"> <%=grupos.get(i).calificacion%></td>
                                    <input type="hidden" name="idCiclo<%=i%>" value="<%=grupos.get(i).ciclos[0].idCiclo%>">
                                    <td width="3%"	align="center"> <%=grupos.get(i).ciclos[0].idCiclo%></td>
                                    <input type="hidden" name="vencimiento<%=i%>" value="<%=Convertidor.dateToString(grupos.get(i).ciclos[0].fechaUltimoPago)%>">
                                    <td width="7%"	align="center">	<%=HTMLHelper.displayField(grupos.get(i).ciclos[0].fechaUltimoPago)%></td>
                                    <input type="hidden" name="integrante<%=i%>" id="integrante<%=i%>" value="<%=grupos.get(i).ciclos[0].numIntegrantes%>">
                                    <td width="7%"	align="center">	<%=grupos.get(i).ciclos[0].numIntegrantes%></td>
                                    <%if(metaEjecutivo!=null){
                                        PlaneacionRenovacionVO planeacion = planeacionDao.getPlaneacionEquipo(grupos.get(i).idGrupo, grupos.get(i).ciclos[0].idCiclo);
                                    %>
                                    <td width="4%"	align="center">
                                        <select name="renueva<%=i%>" id="renueva<%=i%>" value="<%=planeacion.renueva%>" onchange="activaCombos(<%=i%>);">
                                        <%=HTMLHelper.displayCombo(catBinario, planeacion.renueva)%>
                                        </select>
                                    </td>
                                    <td width="4%"	align="center">
                                        <select name="motivo<%=i%>" id="motivo<%=i%>" value="<%=planeacion.numMotivo%>" >
                                        <%=HTMLHelper.displayCombo(catNoRenovacion, planeacion.numMotivo)%>
                                        </select>
                                    </td>
                                    <td width="7%"	align="center">
                                        <input type="text" size="3" name="integranteTotal<%=i%>" id="integranteTotal<%=i%>" value="<%=planeacion.integrantesTotal%>">
                                    </td>
                                    <%} else{%>
                                    <td width="4%"	align="center">
                                        <select name="renueva<%=i%>" id="renueva<%=i%>" value="0" onchange="activaCombos(<%=i%>);">
                                        <%=HTMLHelper.displayCombo(catBinario, 0)%>
                                        </select>
                                    </td>
                                    <td width="4%"	align="center">
                                        <select name="motivo<%=i%>" id="motivo<%=i%>" value="0" >
                                        <%=HTMLHelper.displayCombo(catNoRenovacion, 0)%>
                                        </select>
                                    </td>
                                    <td width="7%"	align="center">
                                        <input type="text" size="3" name="integranteTotal<%=i%>" id="integranteTotal<%=i%>" value="0">
                                    </td>
                                    <%}%>
                                </tr>
                            <%}%>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td colspan="2" align="center">
                        <br>
                        <%if(metaEjecutivo!=null) {%>
                        <input type="button" name="actualizar" value="Actualizar" onClick="guardaPlaneacion();" >
                        <input type="hidden" name="accion" value="actualizar">
                        <%}else {%>
                        <input type="button" name="guardar" value="Guardar" onClick="guardaPlaneacion();" >
                        <input type="hidden" name="accion" value="guardar">
                        <%}%>
                        <br><br>
                    </td>
                </tr>
                <%}%>
                <tr>
                    <td colspan="2" align="center"><br><a href="<%=request.getContextPath()%>">Inicio</a></td>
                </tr>
            </table>
        </form>
        <jsp:include page="footer.jsp" flush="true"/>
    </body>
</html>
