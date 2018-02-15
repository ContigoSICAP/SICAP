<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<link href="./css/cupertino/jquery-ui-1.8.6.custom.css" rel="stylesheet" type="text/css">
<link href="./css/example.css" rel="stylesheet" type="text/css">
<link href="./css/menu.css" rel="stylesheet" type="text/css">
<script language="Javascript" src="./js/functions.js"></script>
<script type="text/javascript" src="./js/jquery-1.4.2.min.js" ></script>
<script type="text/javascript" src="./js/jquery-ui-1.8.6.custom.min.js" ></script>
<script type="text/javascript" src="./js/jquery-impromptu.3.1.min.js" ></script>
<script type="text/javascript" src="./js/calendarios.js" ></script>

<script language="Javascript">
document.oncontextmenu = function(){return false;};
</script>

<map name="mymap">
<area href="<%=basePath%>" alt="Inicio" coords="1,2,300,100">
</map>

<table border="0" cellpadding="0" cellspacing="0" width="100%"> 
	<tr>
		<td valign="top" bgcolor="#ffffff"><img name="imagen" src="images/logo.png"  border="0" alt="" usemap="#mymap" width="302" height="101"></td>
		<td valign="top" bgcolor="#ffffff" align="left"><h3>Bienvenido <%=request.getRemoteUser()%><br/></h3> 
		</td> 
	</tr>
	<tr>
		<td valign="top" bgcolor="#ffffff" align="left"><img name="imagen" src="images/degradado.jpg" border="0" align="right" width="100%"></td>
		<td valign="top" bgcolor="#ffffff" align="left"></td>
	</tr>
</table>

<ul align="rigth"> 
	<li><a href="salirSistema.jsp" target="_top">Cerrar Sesi&oacute;n</a>
   </li> 
   <li><a href="<%=basePath%>admin" target="_top">Administraci&oacute;n</a> 
   </li> 
   <!--<li><a href="http://200.67.132.13:8080/openreports" target="_blank">Reportes</a> -->
   <li><a href="../openreports" target="_blank">Reportes</a> 
   </li> 
   <li><a href="<%=basePath%>">Inicio</a>
   </li> 
   <li><a href="contacto.jsp" target="_top">Contactos</a>
   </li>
   <li><a href="http://www.soporte.fincontigo.mx/scp/" target="_blank" target="_top">Soporte</a>
   </li>  
</ul> 
<br/>
<br/>
<br/>