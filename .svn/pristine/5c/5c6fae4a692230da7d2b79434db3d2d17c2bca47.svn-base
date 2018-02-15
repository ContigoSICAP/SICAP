<%@page import="com.sicap.clientes.dao.CatalogoDAO"%>
<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %>
<%@ page import="com.sicap.clientes.util.*"%>
<%@ page import="com.sicap.clientes.helpers.*"%>
<%@ page import="com.sicap.clientes.vo.*"%>
<%
int i = 0;
%>
<html>
<head>
<title>Reprocesa Pagos</title>
<script>
  
    function submitenter(myfield,e){
        var keycode;
          if (window.event) 
              keycode = window.event.keyCode;
          else if (e) 
             keycode = e.which;
          else 
              return true;

         if (keycode == 13){
             ConsultarPagosReprocesar();
           }else
              return true;
           }

     function redireccionMenuAdmin(){
		window.document.forma.command.value = 'menuPagosReferenciados';
		window.document.forma.submit();
	 }
	 
	 function ConsultarPagosReprocesar(){
		window.document.forma.command.value = 'consultarPagosReprocesar';
		window.document.forma.submit();
	 }
	 
	 
	function reprocesarPagos(numPagos){
	   	  
	 var existeReferenciaParaReprocesar = 'noexisteReferencia'; 	  	  
	 var i; 	  
	 	 
	 if(numPagos==1){
	    if(window.document.forma.Incidencias.checked){	            
	               existeReferenciaParaReprocesar = 'existeReferencia';
	    }           
	 
	 }else if(numPagos>1){
	 
	     for( i = 0; i<window.document.forma.Incidencias.length; i++){
	         var temp = window.document.forma.Incidencias[i].checked;	    	      
	             if(window.document.forma.Incidencias[i].checked){	            
	               existeReferenciaParaReprocesar = 'existeReferencia';
	               break;
	              }
	      }  
	  }    
	      
	      	        
       if ( existeReferenciaParaReprocesar == 'noexisteReferencia' ){			
				alert("No ha seleccionado ningún pago para reprocesar");
				return false;			
		}else{
		  
		  if (confirm("¿Va a reprocesar estos pagos?"))
           {          
            window.document.forma.command.value = 'reprocesarPagos';
		    window.document.forma.submit();
           } 
		  
		}
	}
        
	function eliminarIncidencias(numPagos){
	   	  
	 var existeIncidenciaEliminar = 'noExisteIncidencia'; 	  	  
	 var i ; 	  
	 	 
	 if(numPagos==1){
	    if(window.document.forma.Incidencias.checked){	            
	               existeIncidenciaEliminar = 'existeIncidencia';
	    }           
	 
	 }else if(numPagos>1){
	 
	     for( i = 0; i<window.document.forma.Incidencias.length; i++){
	         var temp = window.document.forma.Incidencias[i].checked;
                 if(window.document.forma.Incidencias[i].checked){
                         var comm = "comment"+i;
                         existeIncidenciaEliminar = 'existeIncidencia';
                         if(document.getElementById(comm).value==''){
                             alert("Ingresa un comentario del motivo de la eliminación");
                             return false;
                         }
	              //break;
	              }
	      }  
	  }    
	      
	      	        
       if ( existeIncidenciaEliminar == 'noExisteIncidencia' ){			
				alert("No ha seleccionado ninguna incidencia para eliminar");
				return false;			
		}else{
		  
		  if (confirm("Las incidencias se van a eliminar"))
           {          
            window.document.forma.command.value = 'eliminarIncidencias';
		    window.document.forma.submit();
           } 
		  
		}
	}
        function seleccionaTodo(){
                var checa = 1;
                if(window.document.forma.checkAll.checked == false)
                    checa = 0;
                for (var i = 0; i<window.document.forma.numIncidencias.value; i++){
                    if(window.document.forma.numIncidencias.value == 1)
                        window.document.forma.Incidencias.checked=checa;
                    else
                        window.document.forma.Incidencias[i].checked=checa;
                }
            }

</script>
<link href="./css/BEtext.css" rel="stylesheet" type="text/css">
</head>
<%
Notification[] notificaciones = (Notification[])request.getAttribute("NOTIFICACIONES");

PagoVO[] pagos = (PagoVO[])request.getAttribute("INCIDENCIAS");
CatalogoDAO catalogoDao = new CatalogoDAO();
%>
<body leftmargin="0" topmargin="0">
<jsp:include page="header.jsp" flush="true"/>
<form name="forma" action="admin" method="post">
  <input type="hidden" name="command" value="">
  <table border="0" width="100%">
    <tr>
      <td align="center"><h3>Referencias de Pagos a Reprocesar</h3>
        <%=HTMLHelper.displayNotifications(notificaciones)%></td>
    </tr>
    <tr>
      <td align="center"> <p>&nbsp;</p>
        <p>Fecha Inicio[AAAA-MM-DD]:
          <input type="text" name="Fecha" id="Fecha" size="10" maxlength="10" value="" onKeyPress="return submitenter(this,event)">
        </p>
        <p><br/>
          Fecha Fin[AAAA-MM-DD]:
          <input type="text" name="FechaFin" id="FechaFin" size="10" maxlength="10" value="" onKeyPress="return submitenter(this,event)">
        </p>
        <p><br/>
          <input type="button" value="Consultar" onClick="ConsultarPagosReprocesar()">
      </p></td>
    </tr>
    <tr>
      <td><%if(pagos!=null && pagos.length>0){%>
        <table border="0" width="100%" cellspacing="3" cellpadding="0" >
          <tr bgcolor="#009865">
            <td width="15%" align="center" class="whitetext">Fecha</td>
            <td width="10%" align="center" class="whitetext">Banco</td>
            <td width="10%" align="center" class="whitetext">Monto</td>
            <td width="10%" align="center" class="whitetext">Status</td>
            <td width="15%" align="center" class="whitetext">Referencia</td>
            <td width="10%" align="center" class="whitetext">Nueva Referencia</td>
            <td width="10%" align="center" class="whitetext">Comentario</td>
            <td width="10%" align="center" class="whitetext"><input type="checkbox" name="checkAll" id="checkAll" onclick="seleccionaTodo()"/></td>
          </tr>
          <%         //for(int i=0; i<pagos.length; i++){
                     for (PagoVO pago : pagos){
               %>
          <tr>
            <td width="15%" align="center"><%= pago.fechaPago %></td>
            <td width="10%" align="center"><%= pago.bancoReferencia %></td>
            <td width="10%" align="center"><%= pago.monto %></td>
            <td width="10%" align="center"><%= pago.tipo %></td>
            <td width="15%" align="center"><%= pago.referencia %></td>
            <td width="10%" align="center"><input type="text" name="<%=HTMLHelper.displayField(pago.idIncidencia) %>" size="45" maxlength="13" value="<%=HTMLHelper.displayField(pago.nuevareferencia) %>"></td>
            <td width="10%" align="center"><input type="text" id="<%="comment"+i%>" name="<%=HTMLHelper.displayField(pago.idIncidencia)+"comentarios" %>" size="45" maxlength="180" value="<%=HTMLHelper.displayField(pago.comentarios) %>"></td>
            <td width="10%" align="center"><input type="checkbox" name="Incidencias" size="45" value="<%=HTMLHelper.displayField(pago.idIncidencia)%>"></td>
            <input type ="hidden" name="<%=HTMLHelper.displayField(pago.idIncidencia)+"transaccion" %>" id ="<%=HTMLHelper.displayField(pago.idIncidencia)+"transaccion" %>"value ="<%=pago.numTransaccion%> "
          </tr>
          <% i++;
                     }  %>
          <tr>
            <td align="center" colspan="6"><br>
                <%if(!catalogoDao.ejecutandoCierre()){%>
              <input type="button" value="Reprocesar" onClick="reprocesarPagos(<%=i%>)">
              <%}%>
            </td>
            <td align="center"><br>
              <input type="button" value="Eliminar incidencias" onClick="eliminarIncidencias(<%=i%>)">
            </td>
          </tr>
        </table>
        <%}%></td>
    </tr>
  </table>
        <input type="hidden" name="numIncidencias" id="numIncidencias" value="<%=i%>"/>
</form>
<jsp:include page="footer.jsp" flush="true"/>
</body>
</html>