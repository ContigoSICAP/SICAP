package com.sicap.clientes.helpers;

public class ExpedienteHelper {

	 public String buscaCheck(String expediente){
		 String checkbox="";
            if(expediente != null){
		       if(expediente.equals("si"))
		    	 checkbox="checked";
		   }
	     return checkbox;
     }
}