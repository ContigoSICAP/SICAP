package com.sicap.clientes.helpers;

import java.util.Hashtable;
import java.util.TreeMap;

import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FormatUtil;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.ClienteVO;

public class ParserStringHelper {


	public static Hashtable getDataCliente (String respuesta){

			Hashtable buroCredito = new Hashtable();

			try{
					respuesta = respuesta.substring(49);
					boolean finCadena = false;
					String tipoRegistro = "";
					String cvePosicion = "";
					int numSegmentos = 0;
					
					while (!finCadena){
						int posFin = 0;
						boolean finSegmento = false;
						numSegmentos++;
						
						//Obtiene el tipo de registro para trabajar en el While
						tipoRegistro = respuesta.substring(0, 2);
						
						if (tipoRegistro.equals("ES") || (tipoRegistro.equals("CR"))){
							finCadena = true;
						}else{
						//Redimensiona la cadena
						respuesta = respuesta.substring(2);

						//Datos del cliente
						Hashtable temporal = new Hashtable();
						cvePosicion = tipoRegistro;
						while(!finSegmento){
							posFin = Convertidor.stringToInt(respuesta.substring(0, 2));
							respuesta = respuesta.substring(2);
							temporal.put(cvePosicion, respuesta.substring(0, posFin));
							respuesta = respuesta.substring(posFin);
							cvePosicion = respuesta.substring(0, 2);
							finSegmento = GetBuroHelper.finSegmento(cvePosicion);
							if (!finSegmento)
								respuesta = respuesta.substring(2);
							}
						
							buroCredito.put(numSegmentos, temporal);
						}
					}

				}catch(Exception e){
					e.printStackTrace();
				}
				return buroCredito;

	}

	public static String makeString(ClienteVO cliente, String user, String password, int idSolicitud, String persona, int idObligado){
		String cadena = "";		
		try{

			String direccion = "";
			int longCadena = 0;
			TreeMap catalogo = MapeoHelper.getCatalogoMapeo(2);
			String aPaterno = "";
			String aMaterno = "";
			String nombre = "";
			String rfc = "";
			int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
			
			if ( cliente!=null){
				if(persona.equals("cliente")){
					aPaterno = cliente.aPaterno;
					aMaterno = cliente.aMaterno;
					nombre = cliente.nombre;
					
					if(aPaterno.length()>25)
						aPaterno = aPaterno.substring(0, 25);
					if(aMaterno.length()>25)
						aMaterno = aMaterno.substring(0, 25);
					if(nombre.length()>25)
						nombre = nombre.substring(0, 25);			
					
					cadena = "INTL11                         001MX0000";
					cadena+= "1CSMX000000000SP01     0000000PN";
					cadena+= FormatUtil.completaCadena(String.valueOf(aPaterno.trim().length()), '0' ,2,"L")+aPaterno.trim();
					cadena+= "00" + FormatUtil.completaCadena(String.valueOf(aMaterno.trim().length()), '0' ,2,"L")+aMaterno.trim(); 
					cadena+= "02" + FormatUtil.completaCadena(String.valueOf(nombre.trim().length()), '0' ,2,"L")+nombre.trim();
					cadena+= "0408" +FormatUtil.deleteChar(Convertidor.dateToString(cliente.fechaNacimiento), '/');
					cadena+= "05" + FormatUtil.completaCadena(String.valueOf(cliente.rfc.trim().length()), '0' ,2,"L")+cliente.rfc.trim();
					//Forma concatenacion de direccion INICIO//
					for( int i=0; i<cliente.direcciones.length; i++ ){
						cadena+= "PA";
						direccion = cliente.direcciones[i].calle.trim();
						direccion+= " " + cliente.direcciones[i].numeroExterior.trim();
						direccion+= " " + cliente.direcciones[i].numeroInterior.trim();
						direccion = direccion.trim();
						if(direccion.length()>39)
							direccion=direccion.substring(0, 39);
						cadena+= FormatUtil.completaCadena(String.valueOf(direccion.trim().length()), '0' ,2,"L")+direccion.trim();
						cadena+= "01" + FormatUtil.completaCadena(String.valueOf(cliente.direcciones[i].colonia.trim().length()), '0' ,2,"L")+cliente.direcciones[i].colonia.trim();
						cadena+= "02" + FormatUtil.completaCadena(String.valueOf(cliente.direcciones[i].municipio.trim().length()), '0' ,2,"L")+cliente.direcciones[i].municipio.trim();
						String estado = (String)catalogo.get(String.valueOf(cliente.direcciones[i].numestado));
						cadena+= "04" + FormatUtil.completaCadena(String.valueOf(estado.length()), '0' ,2,"L")+estado;
						cadena+= "0505" + cliente.direcciones[i].cp.trim();
					}
					//Forma concatenacion de direccion FIN//
					longCadena = cadena.length() + 15;
					cadena+= "ES05" + FormatUtil.completaCadena(String.valueOf(longCadena), '0' ,5,"L");
					cadena+= "0002**";
					cadena = Convertidor.toSyncronetCharacterSet(cadena.toUpperCase());
					cadena = cadena.substring(0,40) + user + password + cadena.substring(40);
				}else if(persona.equals("obligado")){
					aPaterno = cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado-1].aPaterno.trim();
					aMaterno = cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado-1].aMaterno.trim();
					nombre 	 = cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado-1].nombre.trim();
					rfc      = cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado-1].rfc.trim();
					
					if(aPaterno.length()>25)
						aPaterno = aPaterno.substring(0, 25);
					if(aMaterno.length()>25)
						aMaterno = aMaterno.substring(0, 25);
					if(nombre.length()>25)
						nombre = nombre.substring(0, 25);			

					cadena = "INTL11                         001MX0000";
					cadena+= "1CSMX000000000SP01     0000000PN";
					cadena+= FormatUtil.completaCadena(String.valueOf(aPaterno.trim().length()), '0' ,2,"L")+aPaterno.trim();
					cadena+= "00" + FormatUtil.completaCadena(String.valueOf(aMaterno.trim().length()), '0' ,2,"L")+aMaterno.trim(); 
					cadena+= "02" + FormatUtil.completaCadena(String.valueOf(nombre.trim().length()), '0' ,2,"L")+nombre.trim();
					cadena+= "0408" +FormatUtil.deleteChar(Convertidor.dateToString(cliente.fechaNacimiento), '/');
					cadena+= "05" + FormatUtil.completaCadena(String.valueOf(rfc.trim().length()), '0' ,2,"L")+rfc.trim();
					cadena+= "PA";
					direccion = cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado-1].direccion.calle.trim();
					direccion+= " " + cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado-1].direccion.numeroExterior.trim();
					direccion+= " " + cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado-1].direccion.numeroInterior.trim();
					direccion = direccion.trim();
					if(direccion.length()>39)
						direccion=direccion.substring(0, 39);
					cadena+= FormatUtil.completaCadena(String.valueOf(direccion.trim().length()), '0' ,2,"L")+direccion.trim();
					cadena+= "01" + FormatUtil.completaCadena(String.valueOf(cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado-1].direccion.colonia.trim().length()), '0' ,2,"L")+cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado-1].direccion.colonia.trim();
					cadena+= "02" + FormatUtil.completaCadena(String.valueOf(cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado-1].direccion.municipio.trim().length()), '0' ,2,"L")+cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado-1].direccion.municipio.trim();
					String estado = (String)catalogo.get(String.valueOf(cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado-1].direccion.numestado));
					cadena+= "04" + FormatUtil.completaCadena(String.valueOf(estado.length()), '0' ,2,"L")+estado;
					cadena+= "0505" + cliente.solicitudes[indiceSolicitud].obligadosSolidarios[idObligado-1].direccion.cp.trim();
					longCadena = cadena.length() + 15;
					cadena+= "ES05" + FormatUtil.completaCadena(String.valueOf(longCadena), '0' ,5,"L");
					cadena+= "0002**";
					cadena = Convertidor.toSyncronetCharacterSet(cadena.toUpperCase());
					cadena = cadena.substring(0,40) + user + password + cadena.substring(40);
				}
			}
	
	}catch(Exception e){
		e.printStackTrace();
	}

		
		return cadena;
	}

}