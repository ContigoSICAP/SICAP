<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<html>
    <head>
        <title>Menú principal administración</title>
        <script language="Javascript" src="./js/functions.js"></script>
        <script>

            function altaEjecutivos(){
                window.document.forma.command.value='altaEjecutivos';
                window.document.forma.submit();
            }
	
            function capturaSupervisor(){
                window.document.forma.command.value='capturaSupervisor';
                window.document.forma.submit();
            }

            function reasignacionCartera(){
                window.document.forma.command.value='reasignacionCartera';
                window.document.forma.submit();
            }
	
            function modificacionEjecutivos(){
                window.document.forma.command.value='modificacionEjecutivos';
                window.document.forma.submit();
            }
	
            function modificaSupervisor(){
                window.document.forma.command.value='modificaSupervisor';
                window.document.forma.submit();
            }

            function planeacion(){
                window.document.forma.command.value='metasPlaneacion';
                window.document.forma.submit();
            }

            function gestion(){
                window.document.forma.command.value='metasGestion';
                window.document.forma.submit();
            }
	
            function sinRenovacion(){
                window.document.forma.command.value='sinRenovacion';
                window.document.forma.submit();
            }
            
            function ratingasesores(){
                window.document.forma.command.value='calificacionasesores';
                window.document.forma.submit();
            }
        </script>
        <link href="./css/BEtext.css" rel="stylesheet" type="text/css">
    </head>
    <%


    %>

    <body leftmargin="0" topmargin="0">
        <jsp:include page="header.jsp" flush="true"/>
    <center>
        <h2>Administraci&oacute;n de Ejecutivos</h2> 
        <h3>Opciones</h3>
        <form name="forma" action="admin" method="post">
            <input type="hidden" name="command" value="">
            <table border="0" width="100%" cellspacing="0">
                <tr>
                    <td width="100%" align="center" colspan="2"><br><a href="#" onClick="reasignacionCartera()">Reasignaci&oacute;n de cartera</a><br><br></td>
                </tr>
            <%if (request.isUserInRole("ADM_EJECUTIVOS_ALTAS")) {%> 
                <tr>
                    <td width="100%" align="center" colspan="2"><br><a href="#" onClick="altaEjecutivos()">Alta Ejecutivo</a><br><br></td>
                </tr>
                <tr>
                    <td width="100%" align="center" colspan="2"><br><a href="#" onClick="modificacionEjecutivos()"> Modificaci&oacute;n Ejecutivo</a><br><br></td>
                </tr>
            <%}%>
                <!--<tr>
                    <td width="100%" align="center" colspan="2"><br><a href="#" onClick="capturaSupervisor()">Alta Supervisor</a><br><br></td>
                </tr>
                <tr>
                    <td width="100%" align="center" colspan="2"><br><a href="#" onClick="modificaSupervisor()">Modificaci&oacute;n Supervisor</a><br><br></td>
                </tr>-->
            <%if (request.isUserInRole("ADM_METAS_EJECUTIVOS")) {%> 
                <!--tr>
                    <td width="100%" align="center" colspan="2"><br><a href="#" onClick="ratingasesores()">Calificaci&oacute;n de Asesores</a><br><br></td>
                </tr-->
                <tr>
                    <td width="100%" align="center" colspan="2"><br><a href="#" onClick="planeacion()">Planeación Colocación</a><br><br></td>
                </tr>
                <tr>
                    <td width="100%" align="center" colspan="2"><br><a href="#" onClick="gestion()">Gestión Colocación</a><br><br></td>
                </tr>
                <tr>
                    <td width="100%" align="center" colspan="2"><br><a href="#" onClick="sinRenovacion()">Equipos no renovados</a><br><br></td>
                </tr>
            <%}%>
            </table>
        </form>
    </center>
    <jsp:include page="footer.jsp" flush="true"/></body>
</html>