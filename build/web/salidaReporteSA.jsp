<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1" errorPage="error.jsp"%><%@ page import="com.sicap.clientes.vo.*"%><%
      if(request.getAttribute("SEGUROS")!=null){ 
         ArrayList<SegurosVO> seguros = (ArrayList<SegurosVO>)request.getAttribute("SEGUROS"); 
         int contador=0;            
            //Archivo de salida
		    response.setContentType("application/csv");
		    response.setHeader("Content-Disposition","attachment; filename=\"Reporte Seguros Afirme.csv\"");
		    response.setHeader("cache-control", "no-cache");
		    out.println("Sucursal"+","+"Nombre"+","+"Id Cliente"+","+"Fecha Contratación"+","+"Fecha Vencimiento"+","
		    +"Suma Asegurada"+","+"Módulos"+","+"Plazo"+","+"Fecha Nacimiento"+","+"Prima"
		    +","+"Status"+","+"Fecha");            	  
              	  while(contador<seguros.size()){
              	  out.println(seguros.get(contador).sucursal+","+seguros.get(contador).nombreCliente+","+
              	  seguros.get(contador).idCliente+","+seguros.get(contador).fechaContratacion+","+seguros.get(contador).fechaVencimiento
              	  +","+seguros.get(contador).sumaAsegurada+","+seguros.get(contador).modulos+","+seguros.get(contador).plazo
              	  +","+seguros.get(contador).fechaNacimiento+","+seguros.get(contador).prima+","+seguros.get(contador).estatus+","+seguros.get(contador).fechaReporte);
              	  contador++;
              	  }		 	    		 	    
	     	out.flush();	     	
	   }  	
%>