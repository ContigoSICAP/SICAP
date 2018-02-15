package com.sicap.clientes.util.inffinix;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;
import java.util.StringTokenizer;

import com.sicap.clientes.dao.DireccionDAO;
import com.sicap.clientes.dao.DireccionGenericaDAO;
import com.sicap.clientes.dao.EmpleoDAO;
import com.sicap.clientes.dao.PagoDAO;
import com.sicap.clientes.dao.SaldoT24DAO;
import com.sicap.clientes.dao.SucursalDAO;
import com.sicap.clientes.dao.TelefonoDAO;
import com.sicap.clientes.dao.inffinix.CicloGrupalInffinixDAO;
import com.sicap.clientes.dao.inffinix.SolicitudInffinixDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.IBSHelper;
import com.sicap.clientes.helpers.inffinix.SyncronetHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FormatUtil;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.DireccionGenericaVO;
import com.sicap.clientes.vo.DireccionVO;
import com.sicap.clientes.vo.EmpleoVO;
import com.sicap.clientes.vo.PagoVO;
import com.sicap.clientes.vo.SaldoT24VO;
import com.sicap.clientes.vo.SucursalVO;
import com.sicap.clientes.vo.inffinix.CicloGrupalInffinixVO;
import com.sicap.clientes.vo.inffinix.ClienteInffinix;
import com.sicap.clientes.vo.inffinix.GrupoInffinixVO;
import com.sicap.clientes.vo.inffinix.SolicitudInfinix;

public class InffinixUtil {
	
	public final String ESPACIOS_80 = "                                                                                ";
	public final String ESPACIOS_40 = "                                        ";
	public final String ESPACIOS_25 = "                         ";
	public final String ESPACIOS_15 = "               ";
	
	public static String getDireccion(DireccionVO direccion){
		String direccionStr = "";
		if ( direccion!=null ){
			if ( direccion.colonia!=null && direccion.colonia.equals("") )
				direccionStr = direccion.colonia;
			if ( direccion.calle!=null && direccion.calle.equals("") )
				direccionStr += " "+direccion.calle;
			if ( direccion.numeroExterior!=null && !direccion.numeroExterior.equals("") )
				direccionStr += " "+direccion.numeroExterior;
			if ( direccion.numeroInterior!=null && !direccion.numeroInterior.equals("") )
				direccionStr += " "+direccion.numeroInterior;
			
		}
		return direccionStr;
	}

	public static String getAniosMesesVivienda(String fechaInicio){
		
		StringTokenizer st = new StringTokenizer(fechaInicio, "/");
		String mes = st.nextToken();
		String anio = st.nextToken();
		return mesesDiferencia(mes, anio);
	}

	private static String mesesDiferencia (String mes, String anio){
		Calendar calInicio = Calendar.getInstance();
		Calendar calFinal = Calendar.getInstance();
		calInicio.set(Calendar.DAY_OF_MONTH, 1);
		calInicio.set(Calendar.MONTH, Integer.parseInt(mes));
		calInicio.set(Calendar.YEAR, Integer.parseInt(anio));
		int meses = 0;
		while( calFinal.after(calInicio) ){
			calInicio.add(Calendar.MONTH, 1);
			meses++;
		}
		int anios = meses/12;
		meses = meses - (anios*12);
		return anios+","+meses;
	}
	
	public static String formatoFechaInffinix(Date fecha){

		Locale local = new Locale("ES");
		SimpleDateFormat sd = new SimpleDateFormat(InffinixConstants.FORMATO_FECHA_SYNCRONET, local);

		if ( fecha!=null ){
			return sd.format(fecha).toUpperCase();
		}else{
			return "";
		}
	}

	public static String validaInt( int valor, int valorNulo, int valorDefecto, char caracter, int longitud) {
        String cadenaValor = null;
        if ( valor==valorNulo ){
        	return String.valueOf(valorDefecto);
        }else{
        	cadenaValor = String.valueOf(valor);
        	if ( cadenaValor.length()<=longitud ){
        		return completaCadena(cadenaValor, caracter, longitud);
        	}else{
        		return cadenaValor.substring(0, longitud);
        	}
        }
    }

    public static String validaCadena( StringBuffer valor, String valorDefecto, boolean upperCase, boolean trim ) {
        String value = valor==null?null:valor.toString();
        return validaCadena (value, valorDefecto, upperCase, trim);
    }

    public static String validaCadena( StringBuffer valor, String valorDefecto ) {
        String value = valor==null?null:valor.toString();
        return validaCadena (value, valorDefecto, true, false);
    }

	public static String validaCadena( String valor, String valorDefecto, int maxLength, int indexSubstringIni, int indexSubstringFin, boolean toUpperCase){
        if ( ( valor == null ) || ( valor.trim().equals("") ) ) {
            return valorDefecto;
        }
        if ( valor.length() > maxLength ) {
            if ( valor.length() < indexSubstringFin ) {
                if (toUpperCase) {
                    return valor.substring( indexSubstringIni ).toUpperCase();
                }else{
                    return valor.substring( indexSubstringIni );
                }
            } else {
                if (toUpperCase){
                    return valor.substring( indexSubstringIni, indexSubstringFin ).toUpperCase();
                }else{
                    return valor.substring( indexSubstringIni, indexSubstringFin );
                }
            }
        } else {
            String salida = "";
            if (toUpperCase){
                salida = indexSubstringIni<valor.length()?valor.substring( indexSubstringIni ).toUpperCase():valorDefecto.toUpperCase();
            }else{
                salida = indexSubstringIni<valor.length()?valor.substring( indexSubstringIni ):valorDefecto;
            }
            return salida;
        }
    }

    public static String validaCadena( String valor, String valorDefecto, boolean toUpperCase, boolean trim){
        String resultado = null;
        if ( ( valor == null ) || ( valor.trim().equals("") ) ){
            if (toUpperCase){
                if (valorDefecto != null){
                    resultado = valorDefecto.toUpperCase();
                }else{
                    resultado = valorDefecto;
                }
            }else{
                resultado = valorDefecto;
            }
            if (trim){
                if (resultado != null){
                    resultado = resultado.trim();
                }
            }
        }else{
            if (toUpperCase){
                resultado = valor.toUpperCase();
            }else{
                resultado = valor;
            }
            if (trim){
                if (resultado != null){
                    resultado = resultado.trim();
                }
            }
        }
        return resultado;
    }

    public static String completaCadena(String valor, char caracter, int longitud){

        if ( valor!=null ){
            if ( valor.length()>=longitud )
                return valor;
            else{
                for ( int i= valor.length() ; i<longitud ; i++ ){
                    valor = Character.toString(caracter)+valor;
                }
                return valor;
            }
        }else{
            char[] secuencia = new char[longitud];
            Arrays.fill(secuencia, caracter);
            return new String(secuencia);
        }
    }
    //-----------------------------------------------EN CONSTRUCCION-----------------------------------------------//
    
    
    
    private SaldoT24VO[] leeArchivo(){
    	File fArchivoCarga = null;
    	FileReader fReader = null;
    	BufferedReader bReader = null;
    	SaldoT24VO[] saldosVO = null;
    	char reempl = '"';
    	String sRegistro = "";
    	String fechaDefecto = "18991229";
    	ArrayList<SaldoT24VO> arrayListSaldos = new ArrayList<SaldoT24VO>();
    	//String rutaArchivo = generaRutaArchivoEntrada();
    	String rutaArchivo = CatalogoHelper.getParametro("RUTA_CARPETA_ENTRADA_INFFINIX");
    	String[] listaArchivos = null;
    	Logger.debug("Ruta de archivo::" + rutaArchivo);
    	String delim = ",";
    	try{
    		FiltroInffinix aei = new FiltroInffinix(InffinixConstants.EXTENSION_ARCHIVO_ENTRADA);
    		File rutaArchivoCarga = new File(rutaArchivo);
    		listaArchivos = rutaArchivoCarga.list(aei);
    		for(int i = 0; i < listaArchivos.length; i++){
    			//Logger.debug("Archivo encontrado::" + listaArchivos[i]);
    		}
    		if(listaArchivos.length>1){
    			Logger.debug("Existe m�s de un archivo con extension " + InffinixConstants.EXTENSION_ARCHIVO_ENTRADA);
    		}
    		if(listaArchivos.length == 0){
    			aei = new FiltroInffinix(InffinixConstants.EXTENSION_ARCHIVO_ENTRADA.toLowerCase());
    			rutaArchivoCarga = new File(rutaArchivo);
        		listaArchivos = rutaArchivoCarga.list(aei);
        		if(listaArchivos.length == 0){
        			Logger.debug("No existe ningun archivo con extension " + InffinixConstants.EXTENSION_ARCHIVO_ENTRADA.toLowerCase());
        		}
    		}
    		if(listaArchivos.length == 1){
    			fArchivoCarga = new File(rutaArchivo + listaArchivos[listaArchivos.length -1]);
    			fReader = new FileReader(fArchivoCarga);
            	bReader = new BufferedReader(fReader);
            	if(fArchivoCarga.exists()){
            		sRegistro = bReader.readLine();
            		sRegistro = bReader.readLine();
            		while(sRegistro!=null){
            			try{
            				String[] sCamposSaldos = sRegistro.split(delim);
                			for(int i = 0; i < sCamposSaldos.length; i++){
                				sCamposSaldos[i] = FormatUtil.deleteChar(sCamposSaldos[i], reempl);
                			}
                			SaldoT24VO saldoVO = new SaldoT24VO();
                			int param = 0;
                			saldoVO.fechaInf = Convertidor.stringToSqlDate(FormatUtil.deleteChar(sCamposSaldos[param++], reempl),InffinixConstants.FORMATO_FECHA_SYNCRONET);
                			saldoVO.fechaGeneracion = Convertidor.stringToSqlDate(FormatUtil.deleteChar(sCamposSaldos[param++], reempl),InffinixConstants.FORMATO_FECHA_SYNCRONET);
                			//param++;
                			//param++;
                			//Logger.debug("saldoVO.nombre:" +sCamposSaldos[param]);
                			if(sCamposSaldos[param] == null){
                				sCamposSaldos[param] = "";
                				param++;
                			}
                			else
                				saldoVO.nombre = FormatUtil.deleteChar(sCamposSaldos[param++], reempl);
            				//Logger.debug("saldoVO.nombreSucursal");
            				//saldoVO.nombreSucursal = FormatUtil.deleteChar(sCamposSaldos[param++], reempl);
            				//Logger.debug("saldoVO.ejecutivo");
            				//saldoVO.ejecutivo = FormatUtil.deleteChar(sCamposSaldos[param++], reempl);
            				//Logger.debug("saldoVO.numCliente:" +sCamposSaldos[param]);
            				saldoVO.numCliente = Integer.parseInt(sCamposSaldos[param++]);
            				//Logger.debug("saldoVO.contrato:" +sCamposSaldos[param]);
            				saldoVO.ciclo = Integer.parseInt(sCamposSaldos[param++]);
            				param++;
            				//Logger.debug("saldoVO.rfc:" +sCamposSaldos[param]);
            				saldoVO.rfc = FormatUtil.deleteChar(sCamposSaldos[param++], reempl);
            				
            				//Logger.debug("saldoVO.numCuotas:" +sCamposSaldos[param]);
            				saldoVO.numCuotas = Integer.parseInt(sCamposSaldos[param++]);
            				//Logger.debug("saldoVO.numCuotasRestantes:" +sCamposSaldos[param]);
            				saldoVO.numCuotasRestantes = Integer.parseInt(sCamposSaldos[param++]);
            				//Logger.debug("saldoVO.destinoCredito:" +sCamposSaldos[param]);
            				saldoVO.destinoCredito = Integer.parseInt(sCamposSaldos[param++]);
            				
            				//Logger.debug("saldoVO.fechaDisposicion:" +sCamposSaldos[param]);
            				saldoVO.fechaDisposicion = Convertidor.stringToSqlDate(FormatUtil.deleteChar(sCamposSaldos[param++], reempl),InffinixConstants.FORMATO_FECHA_SYNCRONET);
            				//Logger.debug("saldoVO.fechaVencimiento:" +sCamposSaldos[param]);
            				if(sCamposSaldos[param] == null || sCamposSaldos[param].trim().equalsIgnoreCase("") || sCamposSaldos[param].trim().equalsIgnoreCase("0")){
            					sCamposSaldos[param] = fechaDefecto;
            				}
            				saldoVO.fechaVencimiento = Convertidor.stringToSqlDate(FormatUtil.deleteChar(sCamposSaldos[param++], reempl),InffinixConstants.FORMATO_FECHA_SYNCRONET );
            				//Logger.debug("saldoVO.responsabilidadTotal:" +sCamposSaldos[param]);
            				
            				saldoVO.responsabilidadTotal = Double.parseDouble(sCamposSaldos[param++]);
            				
            				//Logger.debug("saldoVO.montoLineaCredito:" +sCamposSaldos[param]);
            				
            				saldoVO.montoLineaCredito = Double.parseDouble(sCamposSaldos[param++]);
            				
            				saldoVO.montoAprobado = Double.parseDouble(sCamposSaldos[param++]);
            				//Logger.debug("saldoVO.capitalPagado:" +sCamposSaldos[param]);
            				
            				saldoVO.capitalPagado = Double.parseDouble(sCamposSaldos[param++]);
            				//Logger.debug("saldoVO.interesPagado:" +sCamposSaldos[param]);
            				
            				saldoVO.interesPagado = Double.parseDouble(sCamposSaldos[param++]);
            				//Logger.debug("saldoVO.moratorioPagado:" +sCamposSaldos[param]);
            				
            				saldoVO.moratorioPagado = Double.parseDouble(sCamposSaldos[param++]);
            				//Logger.debug("saldoVO.saldoInsoluto:" +sCamposSaldos[param]);
            				
            				saldoVO.saldoInsoluto = Double.parseDouble(sCamposSaldos[param++]);
            				//Logger.debug("saldoVO.comision:" +sCamposSaldos[param]);
            				
            				saldoVO.comision = Double.parseDouble(sCamposSaldos[param++]);
            				//Logger.debug("saldoVO.ivaComision:" +sCamposSaldos[param]);
            				
            				saldoVO.ivaComision = Double.parseDouble(sCamposSaldos[param++]);
            				//Logger.debug("saldoVO.desembolso:" +sCamposSaldos[param]);
            				saldoVO.desembolso = Double.parseDouble(sCamposSaldos[param++]);
            				//Logger.debug("saldoVO.formaAmortizacion:" +sCamposSaldos[param]);
            				saldoVO.formaAmortizacion = Integer.parseInt(sCamposSaldos[param++]);
            				//Logger.debug("saldoVO.fechaSigAmortizacion:" +sCamposSaldos[param]);
            				if(sCamposSaldos[param] == null || sCamposSaldos[param].trim().equalsIgnoreCase("")){
            					sCamposSaldos[param] = fechaDefecto;
            				}
            				saldoVO.fechaSigAmortizacion = Convertidor.stringToSqlDate(FormatUtil.deleteChar(sCamposSaldos[param++], reempl),InffinixConstants.FORMATO_FECHA_SYNCRONET );
            				//Logger.debug("saldoVO.fechaSigAmortizacion convertida:" +saldoVO.fechaSigAmortizacion.toString());
            				//Logger.debug("saldoVO.montoAmortizacion:" +sCamposSaldos[param]);
            				saldoVO.montoAmortizacion = Double.parseDouble(sCamposSaldos[param++]);
            				//Logger.debug("saldoVO.frecuenciaAmortizacion:" +sCamposSaldos[param]);
            				saldoVO.frecuenciaAmortizacion = FormatUtil.deleteChar(sCamposSaldos[param++], reempl);
            				//Logger.debug("saldoVO.formaPagoIntereses:" +sCamposSaldos[param]);
            				saldoVO.formaPagoIntereses = Integer.parseInt(sCamposSaldos[param++]);
            				//Logger.debug("saldoVO.montoInteresesPorCobrar:" +sCamposSaldos[param]);
            				saldoVO.montoInteresesPorCobrar = Double.parseDouble(sCamposSaldos[param++]);
            				
            				//Logger.debug("saldoVO.moneda:" +sCamposSaldos[param]);
            				saldoVO.moneda = Integer.parseInt(sCamposSaldos[param++]);
            				
            				//Logger.debug("saldoVO.fuenteFondeo:" +sCamposSaldos[param]);
            				if(sCamposSaldos[param].trim().equalsIgnoreCase("")){
            					saldoVO.fuenteFondeo = 0;
            					param++;
            				}
            				else
            					saldoVO.fuenteFondeo = Integer.parseInt(sCamposSaldos[param++]);
            				//Logger.debug("saldoVO.tasaBruta:" +sCamposSaldos[param]);
            				saldoVO.tasaBruta = Double.parseDouble(sCamposSaldos[param++]);
            				double tasaBruta = saldoVO.tasaBruta * 100;
            				tasaBruta = Math.round(tasaBruta);
            				tasaBruta /= 100;
            				saldoVO.tasaBruta = tasaBruta;
            				//Logger.debug("saldoVO.tasaReferencia:" +sCamposSaldos[param]);
            				if(sCamposSaldos[param].trim().equalsIgnoreCase("")){
            					saldoVO.tasaReferencia = 0;
            					param++;
            				}
            				else
            					saldoVO.tasaReferencia = Double.parseDouble(sCamposSaldos[param++]);
            				//Logger.debug("saldoVO.interesesDevNoCobrados:" +sCamposSaldos[param]);
            				saldoVO.interesesDevNoCobrados = Double.parseDouble(sCamposSaldos[param++]);
            				//Logger.debug("saldoVO.interesFuturo:" +sCamposSaldos[param]);
            				//saldoVO.interesFuturo = Double.parseDouble(sCamposSaldos[param++]);
            				//Logger.debug("saldoVO.capitalVencido:" +sCamposSaldos[param]);
            				saldoVO.capitalVencido = Double.parseDouble(sCamposSaldos[param++]);
            				//Logger.debug("saldoVO.interesesVencidos:" +sCamposSaldos[param]);
            				saldoVO.interesesVencidos = Double.parseDouble(sCamposSaldos[param++]);
            				//Logger.debug("saldoVO.interesMoratorio:" +sCamposSaldos[param]);
            				saldoVO.interesMoratorio = Double.parseDouble(sCamposSaldos[param++]);
            				//Logger.debug("saldoVO.ivaMoratorio");
            				//saldoVO.ivaMoratorio = Double.parseDouble(sCamposSaldos[param++]);
            				//Logger.debug("saldoVO.feeVencidos:" +sCamposSaldos[param]);
            				if(sCamposSaldos[param].trim().equalsIgnoreCase("")){
            					saldoVO.feeVencidos=0;
            					param++;
            				}
            				else
            					saldoVO.feeVencidos = Double.parseDouble(sCamposSaldos[param++]);
            				/*Logger.debug("saldoVO.totalVencido:" +sCamposSaldos[param]);
            				if(sCamposSaldos[param].trim().equalsIgnoreCase("")){
            					saldoVO.totalVencido = 0;
            					param++;
            				}
            				else
            					saldoVO.totalVencido = Double.parseDouble(sCamposSaldos[param++]);
            				*/
            				//Logger.debug("saldoVO.situacionActualCredito:" +sCamposSaldos[param]);
            				if(sCamposSaldos[param].trim().equalsIgnoreCase("")){
            					saldoVO.situacionActualCredito = 0;
            					param++;
            				}
            				else
            					saldoVO.situacionActualCredito = Integer.parseInt(sCamposSaldos[param++]);
            				//Logger.debug("saldoVO.numOperacion:" +sCamposSaldos[param]);
            				if(sCamposSaldos[param].trim().equalsIgnoreCase("")){
            					saldoVO.numOperacion = 0;
            					param++;
            				}
            				else
            					saldoVO.numOperacion = Integer.parseInt(sCamposSaldos[param++]);
            				switch(saldoVO.numOperacion){
            				case 1:
            					saldoVO.numOperacion = 2;
            					break;
            				case 2:
            					saldoVO.numOperacion = 1;
            					break;
            				case 5:
            					saldoVO.numOperacion = 4;
            					break;
            				case 6:
            					saldoVO.numOperacion = 21;
            					break;
            				case 9:
            					saldoVO.numOperacion = 5;
            					break;
            				default:
            					break;
            				}
            				//Logger.debug("saldoVO.tipoGtiaReal:" +sCamposSaldos[param]);
            				if(sCamposSaldos[param].trim().equalsIgnoreCase("")){
            					saldoVO.tipoGtiaReal = 0;
            					param++;
            				}
            				else
            					saldoVO.tipoGtiaReal = Integer.parseInt(sCamposSaldos[param++]);
            				//Logger.debug("saldoVO.tipoRelacion:" +sCamposSaldos[param]);
            				
            				if(sCamposSaldos[param].trim().equalsIgnoreCase("")){
            					saldoVO.tipoRelacion = 0;
            					param++;
            				}
            				else
            					saldoVO.tipoRelacion = Integer.parseInt(sCamposSaldos[param++]);
            				//Logger.debug("saldoVO.tipoIndustria:" +sCamposSaldos[param]);
            				if(sCamposSaldos[param].trim().equalsIgnoreCase("")){
            					saldoVO.tipoIndustria = 0;
            					param++;
            				}
            				else
            					saldoVO.tipoIndustria = Integer.parseInt(sCamposSaldos[param++]);
            				//Logger.debug("saldoVO.fechanIncumplimiento:" +sCamposSaldos[param]);
            				if(sCamposSaldos[param] == null || sCamposSaldos[param].trim().equalsIgnoreCase("") || sCamposSaldos[param].trim().equalsIgnoreCase("0")){
            					sCamposSaldos[param] = fechaDefecto;
            				}
            				saldoVO.fechaIncumplimiento = Convertidor.stringToSqlDate(FormatUtil.deleteChar(sCamposSaldos[param++], reempl),InffinixConstants.FORMATO_FECHA_SYNCRONET);
            				//Logger.debug("saldoVO.fechaEntradaCartVencida:" +sCamposSaldos[param]);
            				if(sCamposSaldos[param] == null || sCamposSaldos[param].trim().equalsIgnoreCase("") || sCamposSaldos[param].trim().equalsIgnoreCase("0")){
            					sCamposSaldos[param] = fechaDefecto;
            				}
            				saldoVO.fechaEntradaCartVencida = Convertidor.stringToSqlDate(FormatUtil.deleteChar(sCamposSaldos[param++], reempl),InffinixConstants.FORMATO_FECHA_SYNCRONET );
            				//Logger.debug("saldoVO.diasVencidos:" +sCamposSaldos[param]);
            				String diasVencidos = sCamposSaldos[param++];
            				if ( !diasVencidos.equals("") )
            					saldoVO.diasVencidos = Integer.parseInt(diasVencidos);            					
            				//Logger.debug("saldoVO.diasTranscurridos:" +sCamposSaldos[param]);
            				saldoVO.diasTranscurridos = Integer.parseInt(sCamposSaldos[param++]);
            				//Logger.debug("saldoVO.diasTotales:" +sCamposSaldos[param]);
            				//saldoVO.diasTotales = Integer.parseInt(sCamposSaldos[param++]);
            				//Logger.debug("saldoVO.numeroPagosVencidos:" +sCamposSaldos[param]);
            				saldoVO.numeroPagosVencidos = (int)Double.parseDouble(sCamposSaldos[param++]);
            				//Logger.debug("saldoVO.multa:" +sCamposSaldos[param]);
            				saldoVO.multa = Double.parseDouble(sCamposSaldos[param++]);
            				//Logger.debug("saldoVO.ivaMulta:" +sCamposSaldos[param]);
            				saldoVO.ivaMulta = Double.parseDouble(sCamposSaldos[param++]);
            				sCamposSaldos[param] = FormatUtil.deleteChar(sCamposSaldos[param], reempl);
            				saldoVO.noCuenta = sCamposSaldos[param++].trim();
            				if(saldoVO.noCuenta.length()>13){
            					if(saldoVO.noCuenta.charAt(13)=='9'){
            						sRegistro = bReader.readLine();
            						continue;
            					}
            						
            					else
            						saldoVO.noCuenta = saldoVO.noCuenta.substring(0, 13);
            				}	
            				//Logger.debug("Moneda:" +sCamposSaldos[param]);
            				param++;
            				//Logger.debug("NumUnico:" +sCamposSaldos[param]);
            				param++;
            				//Logger.debug("NumEjecutivo:" +sCamposSaldos[param]);
            				param++;
            				saldoVO.ejecutivo = sCamposSaldos[param++].trim();
            				arrayListSaldos.add(saldoVO);
            				sRegistro = bReader.readLine();	
            			}
            			catch(NumberFormatException exc){
            	    		System.out.println("NumberFormatException en InffinixUtil.leeArchivo::" + exc);
            	    		exc.printStackTrace();
            	    		sRegistro = bReader.readLine();
            	    	}
            			
            		}
            		saldosVO = new SaldoT24VO[arrayListSaldos.size()];
            		Logger.debug("Longitud de array : " + saldosVO.length);
            		for(int contIter = 0; contIter < saldosVO.length; contIter++){
            			saldosVO[contIter] = arrayListSaldos.get(contIter);
            		}	
        		}
    		}
    	}
    	catch(FileNotFoundException exc){
    		System.out.println("FileNotFoundException en InffinixUtil.leeArchivo::" + exc);
    		exc.printStackTrace();
    	}
    	catch(IOException exc){
    		System.out.println("IOException en InffinixUtil.leeArchivo::" + exc);
    		exc.printStackTrace();
    	}
    	catch(Exception exc){
    		System.out.println("Exception en InffinixUtil.leeArchivo::" + exc);
    		exc.printStackTrace();
    	}
    	finally{
    		try{
    			bReader.close();
    		}
    		catch(Exception exc){
    			exc.printStackTrace();
    		}
    	}
    	return saldosVO;
    }
    
    private void guardaSaldos(SaldoT24VO saldosVO[], SaldoT24DAO saldoDAO){
    	for(int i = 0; i < saldosVO.length; i++)
    		saldoDAO.insertSaldosT24(saldosVO[i]);
    }
    
    private ClienteInffinix[] generaListaClientes() throws CommandException{
    	ClienteInffinix[] clientes = null;
		actualizaCamposSaldosT24();
		try{
			clientes = SyncronetHelper.getClientesEnvio();
			obtenerDatosClientes(clientes);
			Logger.debug("\nClientes obtenidos : " + clientes.length);
		}catch(ClientesDBException dbe){
			throw new CommandException(dbe.getMessage());
		}
		catch(Exception e){
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		return clientes;
    }
    
    private GrupoInffinixVO[] generaListaGrupos() throws CommandException{
    	GrupoInffinixVO[] grupos = null;
		try{
			grupos = SyncronetHelper.getGruposEnvio();
			obtenerDatosGrupos(grupos);
			Logger.debug("\nGrupos obtenidos : " + grupos.length);
		}catch(ClientesDBException dbe){
			dbe.printStackTrace();
			throw new CommandException(dbe.getMessage());
		}
		catch(Exception e){
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
		return grupos;
    }
    
    private void obtenerDatosClientes(ClienteInffinix[] clientes)throws ClientesException{
    	StringBuffer string = new StringBuffer();
    	int solicitudesTotal = 0;
    	DireccionDAO direcciondao = new DireccionDAO();
		TelefonoDAO telefonodao = new TelefonoDAO();
		SolicitudInffinixDAO solicituddao = new SolicitudInffinixDAO();
		SaldoT24DAO saldoDAO = new SaldoT24DAO();
    	for( int cont=0 ; cont<clientes.length ; cont++ ){
			ClienteInffinix cliente = clientes[cont];
			string.append(cliente.rfc);
			string.append(",");
			cliente.direcciones = direcciondao.getDirecciones(cliente.idCliente);
			for ( int i=0 ; cliente.direcciones!=null && i<cliente.direcciones.length ; i++ ){
				cliente.direcciones[i].telefonos = telefonodao.getTelefonos(cliente.idCliente, cliente.direcciones[i].idDireccion);
			}
			//clientevo
			cliente.solicitudes = solicituddao.getSolicitudes(cliente.idCliente);
			Logger.debug("Solicitudes para el cliente ["+cliente.idCliente+"] : "+cliente.solicitudes.length);
			solicitudesTotal += cliente.solicitudes.length;
			for ( int i=0 ; i<cliente.solicitudes.length ; i++ ){
				PagoDAO pagosDAO = new PagoDAO();
				EmpleoDAO empDAO = new EmpleoDAO();
				DireccionDAO dirDAO = new DireccionDAO();
				cliente.solicitudes[i].saldo = saldoDAO.getSaldosT24ByNumClienteCiclo(cliente.idCliente, cliente.solicitudes[i].idSolicitud, cliente.solicitudes[i].referencia);
				if(cliente.solicitudes[i].saldo!=null)
					cliente.solicitudes[i].pagos = pagosDAO.getFechasPagosCliente(cliente.solicitudes[i].saldo.noCuenta);
				cliente.solicitudes[i].empleo = empDAO.getEmpleo(cliente.idCliente, cliente.solicitudes[i].idSolicitud);
				if(cliente.solicitudes[i].empleo!=null)
					cliente.solicitudes[i].empleo.direccion = dirDAO.getDireccion(cliente.idCliente, cliente.solicitudes[i].idSolicitud, "d_empleos", 1);
			}
			clientes[cont] = cliente;
		}
    	Logger.debug("Total de solicitudes:: " + solicitudesTotal);
    }
    
    
    private void obtenerDatosGrupos(GrupoInffinixVO[] grupos)throws ClientesException{
    	int ciclosTotal = 0;
		CicloGrupalInffinixDAO cicloGrupalDao = new CicloGrupalInffinixDAO();
		DireccionGenericaDAO direcDAO = new DireccionGenericaDAO();
		SaldoT24DAO saldoDAO = new SaldoT24DAO();
		PagoDAO pagoDAO = new PagoDAO();
    	for( int cont=0 ; cont<grupos.length ; cont++ ){
			GrupoInffinixVO grupo = grupos[cont];
			grupo.ciclosInffinix = cicloGrupalDao.getCiclosInffinix(grupo.idGrupo);
			if(grupo.ciclosInffinix != null){
				ciclosTotal += grupo.ciclosInffinix.length;
				for ( int i=0 ; i<grupo.ciclosInffinix.length ; i++ ){
					CicloGrupalInffinixVO cicloInffinix = grupo.ciclosInffinix[i];
					cicloInffinix.direccionReunion = direcDAO.getDireccion(cicloInffinix.idDireccionReunion);
//					cicloInffinix.saldo = IBSHelper.getSaldosT24ToIBS(saldoDAO.getSaldosT24ByNumClienteCiclo(grupo.idGrupo, cicloInffinix.idCiclo,cicloInffinix.referencia));
					cicloInffinix.saldoT24 = new SaldoT24DAO().getSaldosT24ByNumClienteCiclo(grupo.idGrupo, cicloInffinix.idCiclo,cicloInffinix.referencia);
					if(cicloInffinix.saldoT24!=null)
						cicloInffinix.pagos = pagoDAO.getFechasPagosCliente(cicloInffinix.saldoT24.noCuenta);
				}
			}
		}
    	Logger.debug("\nTotal de ciclos : " + ciclosTotal);
    }
    
    private void verificaExistenciaArchivo(String archivo){
    	File archivoInffinix = new File(archivo);
    	if(archivoInffinix.exists()){
    		archivoInffinix.delete();
    	}
    	File verificaExistencia = new File(archivo);
    	if(!verificaExistencia.exists()){
    		Logger.debug("El archivo de Inffinix ya no existe!!!");
    	}
    }
        
    private void renombraArchivo(String rutaArchivo){
    	FiltroInffinix filtro = new FiltroInffinix(InffinixConstants.EXTENSION_ARCHIVO_ENTRADA);
    	File rutaActual = new File(rutaArchivo);
    	File newFile = null;
    	File oldFile = null;
		String[] listaArchivo = null;
		listaArchivo = rutaActual.list(filtro);
		if(listaArchivo.length == 0){
			filtro = new FiltroInffinix(InffinixConstants.EXTENSION_ARCHIVO_ENTRADA.toLowerCase());
    		listaArchivo = rutaActual.list(filtro);
		}
		if(listaArchivo.length == 0){
			Logger.debug("No existe ningun archivo con extension " + InffinixConstants.EXTENSION_ARCHIVO_ENTRADA.toLowerCase());
		}
		if(listaArchivo.length >= 1){
			for(int i = 0; i < listaArchivo.length; i++){
				oldFile = new File(rutaArchivo + listaArchivo[i]);
				
				newFile = new File(rutaArchivo + listaArchivo[i] + InffinixConstants.NUEVA_EXTENSION_ARCHIVO_ENTRADA);
				Logger.debug("Archivo a renombrar:: " + oldFile);
				Logger.debug("Nuevo archivo:: " + newFile);
				boolean isRenamed = oldFile.renameTo(newFile);
				if(isRenamed)
					Logger.debug("Archivo renombrado!!!");
				else
					Logger.debug("No se pudo renombrar el archivo!!!");
			}
		}
    }
    
    private void generaArchivoBlanco(ClienteInffinix[] clientes){
    	String nomArchivo = CatalogoHelper.getParametro("RUTA_CARPETA_SALIDA_INFFINIX") + CatalogoHelper.getParametro("NOMBRE_ARCHIVO_BLANCOS_INFFINIX");
    	verificaExistenciaArchivo(nomArchivo);
    	EmpleoVO empVO = null;
    	SucursalVO sucVO = null;
    	try{
    	Logger.debug("Inicia generacion de archivo blancos individual...");
    	Hashtable<Integer, SucursalVO> sucursales = new SucursalDAO().getSucursales();
    	for(int i = 0; clientes!= null && i < clientes.length; i++){
    		ClienteInffinix cliente = clientes[i];
    		for(int j = 0; cliente.solicitudes!= null && j < cliente.solicitudes.length; j++){
    			SolicitudInfinix solicitud = cliente.solicitudes[j];
    			if(solicitud.saldo==null || cliente.direcciones == null)
    				continue;
    			try{
    				EmpleoDAO empDAO = new EmpleoDAO();
        			empVO = empDAO.getEmpleo(cliente.idCliente, solicitud.idSolicitud);
    			}
    	    	catch(ClientesException exc){
    	    		exc.printStackTrace();
    	    	}
    			if(solicitud != null){
    				PagoVO[] pagoVO = solicitud.pagos;			
    					StringBuffer lineaCliente = new StringBuffer();
    					SaldoT24VO saldo = solicitud.saldo;
    					if(saldo.noCuenta==null || saldo.noCuenta.trim().length()==0)
    						continue;
    					//Logger.debug("Generando el archivo para download");
    					//Prepara las variables para que el usuario pueda hacer el download
    					//Logger.debug("Solicitudes para el cliente : "+cliente.solicitudes.length);
    		
    					//****************  COMIENZAN EL LLENADO DEL ARCHIVO DE CARGA PARA CYBER
    					//CLAVE
    					//Logger.debug("--------------------------------------------------------------------------------------------------");
    					//Logger.debug("OTRACLAVE");
    					lineaCliente.append("   ");
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    					
    					//GRUPO DE LA CUENTA VARCHAR 1
    					//Logger.debug("GRUPO DE LA CUENTA");
    					lineaCliente.append(CatalogoHelper.getParametro("GRUPO_CUENTA_INFFINIX"));
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    				
    					//GRUPO VARCHAR 1
    					//lineaCliente.append(InffinixConstants.GRUPO);
    					////lineaCliente.append(InffinixConstants.SEPARADOR);
    					
    					//NUMERO DE LA CUENTA VARCHAR 25
    					//Logger.debug("NUM. DE CUENTA");
    					//lineaCliente.append( cliente.idCliente );
    					//String cuenta = saldo.contrato;
    					String cuenta = "";
    					String referencia = saldo.noCuenta;
    					if(referencia.length()>0){
    						for(int z = 0; z < 25 - referencia.length(); z++){
    							cuenta += " ";
    						}
    						cuenta = referencia + cuenta;
    						//Logger.debug(cuenta);
    						lineaCliente.append( cuenta );
    					}
    					else
    						lineaCliente.append( "                         " );
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    					
    					//DIRECCION1 DEUDOR VARCHAR(80)
    					//Logger.debug("DIRECCION 1 DEUDOR");
    					if ( cliente.direcciones!=null && cliente.direcciones.length>0 ){
    					//if ( cliente.direcciones!=null && cliente.direcciones.length!=0 ){
    						String direccion = cliente.direcciones[0].calle + " " + cliente.direcciones[0].numeroExterior + " " + cliente.direcciones[0].numeroInterior;
    						//Logger.debug(direccion);
    						if(direccion.length()>80){
    							direccion = direccion.substring(0,80);
    							//Logger.debug("No trueno2...");
    						}
    						if(direccion.length()<80){
    							cuenta = "";
    							for(int z = 0; z < 80 - direccion.length(); z++){
    								cuenta += " ";
    							}
    							direccion = direccion + cuenta;
    						}
    						lineaCliente.append(direccion);
    						//lineaCliente.append( InffinixUtil.validaCadena( InffinixUtil.getDireccion(cliente.direcciones[0]), "", 80, 0, 80, false) );
    					}
    					else{
    						lineaCliente.append( "                                                                                " );
    					}
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    		
    					//DIRECCION2 DEUDOR VARCHAR(80)
    					//Logger.debug("DIRECCION2 DEUDOR");
    					if ( cliente.direcciones!=null && cliente.direcciones.length>0 ){
    						String direccion = cliente.direcciones[0].colonia;
    						if(direccion != null){
    							if(direccion.length()>80)
    								direccion = direccion.substring(0,80);
    							if(direccion.length()<80){
    								cuenta = "";
    								for(int z = 0; z < 80 - direccion.length(); z++){
    									cuenta += " ";
    								}
    								direccion = direccion + cuenta;
    							}
    						}
    						lineaCliente.append(direccion);
    					}
    					else{
    						lineaCliente.append("                                                                                   ");
    					}
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    					
    					//STATUS
    					//Logger.debug("STATUS");
    					lineaCliente.append("                          ");
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    					
    					//AGENCIA ACTUAL
    					//Logger.debug("AGENCIA ACTUAL");
    					lineaCliente.append("        ");
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    		
    					//MONTO VENCIDO NUMBER (15, 2)
    					//Logger.debug("MONTO VENCIDO");
    					String monto;
    					String space = "";
    					if(saldo.totalExigible!=0){
    						monto = "" + HTMLHelper.formatoMonto(saldo.totalExigible);
        					monto = monto.replaceAll(",", "");
        					if(monto.length()> 0 ){
        						if(monto.length()<15){
        							for(int z = 0; z < 15 - monto.length(); z++){
        								space += " ";
        							}
        						}
        						String cant = space + monto;
        						//Logger.debug(cant);
        						lineaCliente.append(cant);
        					}
        					else
        						lineaCliente.append("               ");	
    					}
    					else
    						lineaCliente.append("               ");
    					
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    				
    					//DIRECCION TRABAJO1 VARCHAR(80)
    					//Logger.debug("DIRECCION1 TRABAJO");
    					if ( solicitud.empleo!=null){
    						if(solicitud.empleo.direccion!=null){
    							String direccion = solicitud.empleo.direccion.calle + " " + solicitud.empleo.direccion.numeroExterior + " " + solicitud.empleo.direccion.numeroInterior;
        						if(direccion.length()>80)
        							direccion = direccion.substring(0,80);
        						if(direccion.length()<80){
        							cuenta = "";
        							for(int z = 0; z < 80 - direccion.length(); z++){
        								cuenta += " ";
        							}
        							direccion = direccion + cuenta;
        						}
        						lineaCliente.append(direccion);	
    						}else{
        						lineaCliente.append( "                                                                                " );
        					}
    					}
    					else{
    						lineaCliente.append( "                                                                                " );
    					}
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    				
    					//DIRECCION TRABAJO2 VARCHAR(80)
    					//Logger.debug("DIRECCION TRABAJO2");
    					if ( solicitud.empleo!=null){
    						if(solicitud.empleo.direccion!=null){
    							String direccion = solicitud.empleo.direccion.colonia;
        						if(direccion != null){
        							if(direccion.length()>80)
        								direccion = direccion.substring(0,80);
        							if(direccion.length()<80){
        								cuenta = "";
        								for(int z = 0; z < 80 - direccion.length(); z++){
        									cuenta += " ";
        								}				
        							}
        						}
        						direccion = direccion + cuenta;
        						lineaCliente.append(direccion);	
    						}else{
        						lineaCliente.append( "                                                                                " );
        					}
    					}else{
    						lineaCliente.append( "                                                                                " );
    					}
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    				
    					//CIUDAD TRABAJO VARCHAR(40)
    					//Logger.debug("CIUDAD TRABAJO");
    					if (solicitud.empleo!=null){
    						if(solicitud.empleo.direccion!=null){
    							String ciudad = "";
        						space = "";
        						if(solicitud.empleo.direccion.ciudad!= null){
        							if(solicitud.empleo.direccion.ciudad.length() > 40)
        								ciudad = solicitud.empleo.direccion.ciudad.substring(0,80);
        							if(solicitud.empleo.direccion.ciudad.length() < 40){
        								for(int z = 0; z < 40 - solicitud.empleo.direccion.ciudad.length(); z++){
        									space += " ";
        								}
        								ciudad = ciudad + space;
        								lineaCliente.append(ciudad);
        							}
        						}
        						else
        							lineaCliente.append( "                                        " );	
    						}else
    							lineaCliente.append( "                                        " );
    					}else
							lineaCliente.append( "                                        " );
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    		
    					//NOMBRE EMPRESA VARCHAR(80)
    					//Logger.debug("NOMBRE EMPRESA");
    					if ( cliente.solicitudes!=null && cliente.solicitudes.length!=0 && cliente.solicitudes[j]!=null && cliente.solicitudes[j].empleo!=null ){
    						String razSoc = InffinixUtil.validaCadena( cliente.solicitudes[j].empleo.razonSocial, "", 80, 0, 80, false);
    						if(razSoc!=null){
    							if(razSoc.length()>80)
    								razSoc = razSoc.substring(0, 80);
    							if(razSoc.length()<80){
    								space = "";
    								for(int z = 0; z < 80 - razSoc.length(); z++){
    									space += " "; 
    								}
    								razSoc = razSoc + space;
    							}
    						}else
    							razSoc = "                                                                                "; 
    						lineaCliente.append( razSoc );
    					}
    					else{
    						lineaCliente.append( "                                                                                " );
    					}
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    				
    					//SUCURSAL
    					//Logger.debug("SUCURSAL");
    					//Logger.debug("Llego a sucursal...");
    					String numSuc = "" + cliente.idSucursal;
    					if(numSuc.length()>0){
    						if(numSuc.length()< 5){
    							space = "";
    							for(int z = 0; z < 5-numSuc.length(); z++)
    								space += "0";
    							numSuc = space + numSuc;
    						}
    						lineaCliente.append( numSuc );
    					}
    					else
    						lineaCliente.append( "00000" );
    					//Logger.debug(numSuc);
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    				
    					//ESTADO TRABAJO VARCHAR(5)
    					//Logger.debug("ESTADO TRABAJO");
    					if ( cliente.direcciones!=null && cliente.direcciones.length>1 ){
    						String estado = "";
    						if(cliente.direcciones[1].estado!= null){
    							if(cliente.direcciones[1].estado.length() > 5){
    								estado = cliente.direcciones[1].estado.substring(0,5);
    							}
    							if(cliente.direcciones[1].estado!= null && cliente.direcciones[1].estado.length() < 5){
    								space = "";
    								estado = cliente.direcciones[1].estado.substring(0,5);
    								for(int z = 0; z < 5 - cliente.direcciones[1].estado.length(); z++)
    									space += " ";
    								estado = estado + space;
    							}
    							lineaCliente.append(estado);
    							//Logger.debug(estado);
    						}
    						else
    							lineaCliente.append( "     " );			
    					}
    					else{
    						lineaCliente.append( "     " );
    					}
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    				
    					//ZONA POSTAL TRABAJO VARCHAR(10)
    					//Logger.debug("ZONA POSTAL TRABAJO");
    					if ( cliente.direcciones!=null){
    						if(cliente.direcciones.length>1 ){
    							if(cliente.direcciones[1].cp!= null ){
    								String cp = cliente.direcciones[1].cp;
    								space = "";
    								if( cp.length() <10){
    									for(int z = 0; z < 10 - cp.length(); z++)
    										space+= " ";
    									cp = cp + space;
    									lineaCliente.append(cp);
    								}
    								else if(cp.length() >10){
    									cp = cliente.direcciones[1].cp.substring(0,10);
    									lineaCliente.append(cp);
    									//Logger.debug("Zona postal trabajo::" + cp + "!!");
    								}
    								else
    									lineaCliente.append( "          " );
    							}
    							else{
    								lineaCliente.append( "          " );
    							}
    						}
    						else{
    							lineaCliente.append( "          " );
    						}
    					}
    					else
    						lineaCliente.append( "          " );
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    				
    					//CIUDAD VARCHAR(40)
    					//Logger.debug("CIUDAD");
    					//Logger.debug("" + cliente.direcciones.length);
    					//Logger.debug(cliente.direcciones[0].municipio);
    					if ( cliente.direcciones!=null){
    						if(cliente.direcciones.length>0){
    							String ciudad= cliente.direcciones[0].municipio;
    							if(ciudad!=null){
    								if(ciudad.length() < 40){
    									space = "";
    									for(int z = 0; z < 40 - ciudad.length(); z++){
    										space += " ";
    									}
    									ciudad = ciudad + space;
    								}
    								if(cliente.direcciones[0].ciudad!=null && cliente.direcciones[0].ciudad.length() > 40)
    									ciudad = cliente.direcciones[0].ciudad.substring(0,40);
    								lineaCliente.append(ciudad);
    							}
    							else{
    								lineaCliente.append( "                                        " );
    							}
    						}
    						else{
    							lineaCliente.append( "                                        " );
    						}
    					}
    					else{
    						lineaCliente.append( "                                        " );
    					}
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    				
    					//COMPA�IA (6)
    					//Logger.debug("COMPA�IA");
    					/*if ( cliente.solicitudes!=null){
    						if(cliente.solicitudes.length!=0 && cliente.solicitudes[j]!=null && cliente.solicitudes[j].empleo!=null ){
    							String company = cliente.solicitudes[j].empleo.razonSocial;
    							if(company.length() >6){
    								company = company.substring(0,6);
    								//Logger.debug("Company:: " + company);
    								
    								lineaCliente.append(company);
    							}
    							else if(company.length() <6){
    									space = "";
    									for(int z = 0; z < 6 -company.length(); z++){
    										space += " ";
    									}
    									company = company + space;
    							}
    							else{
    								lineaCliente.append( "      " );
    							}
    						}
    						else{
    							lineaCliente.append( "      " );
    						}
    					}	
    					else{
    						lineaCliente.append( "      " );
    					}*/
    					lineaCliente.append( "      " );
    					//lineaCliente.append( cliente.solicitudes[j].empleo.razonSocial );
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    				
    					//SALDO DEUDOR NUMBER (15, 2)
    					//Logger.debug("SALDO DEUDOR");
    					//double saldoDeudor = (saldo.saldoInsoluto + saldo.montoInteresesPorCobrar) + (saldo.totalExigible);
    					double saldoDeudor = saldo.saldoInsoluto;
    					saldoDeudor *= 100;
    					saldoDeudor = java.lang.Math.round(saldoDeudor);
    					saldoDeudor /= 100;
    					String saldoD = "" + HTMLHelper.formatoMonto(saldoDeudor);
    					saldoD = saldoD.replaceAll(",", "");
    					space = "";
    					if(saldoDeudor!=0){
    						if(saldoD.length()>0){
        						if(saldoD.length() < 15){
        							for(int z = 0; z < 15 - saldoD.length(); z++){
        								space += " ";
        							}
        							saldoD = space + saldoD;
        						}
        						lineaCliente.append( saldoD);
        					}
        					else{
        						lineaCliente.append( "               ");
        					}	
    					}else{
    						lineaCliente.append( "               ");
    					}
    					
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    				
    					//FECHA DE REESTRUCTURACION
    					//Logger.debug("FECHA DE REESTRUCUTRACION");
    					lineaCliente.append("        ");
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    				
    					//FECHA EN QUE CAYO EN MORA DATE
    					//Logger.debug("FECHA EN QUE CAYO EN MORA");
    					if(saldo.fechaIncumplimiento!=null && saldo.totalExigible!=0){
    						String fechaI = "";
    						String[] fechaSplit = saldo.fechaIncumplimiento.toString().split("-");
    						fechaI = fechaSplit[1] + fechaSplit[2] + fechaSplit[0];
    						//Logger.debug("Fecha partida::" + fechaI);
    						if(!fechaI.trim().equalsIgnoreCase("12291899"))
    							lineaCliente.append(fechaI);
    						else
    							lineaCliente.append("        ");
    					}
    					else
    						lineaCliente.append("        ");
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    				
    					//NOMBRE DEUDOR VARCHAR(80)
    					//Logger.debug("NOMBRE DEUDOR");
    					String nombreCompleto = saldo.nombre;
    					//Logger.debug("Nombre completo::" + nombreCompleto);
    					//String nombreCompleto = InffinixUtil.validaCadena( HTMLHelper.getNombreCompleto(cliente), "", 80, 0, 80, false);
    					space = "";
    					if(nombreCompleto.length()>80){
    						nombreCompleto = nombreCompleto.substring(0,80);
    					}
    					if(nombreCompleto.length()<80){
    						for(int z = 0; z < 80 - nombreCompleto.length(); z++){
    							space += " ";
    						}
    						nombreCompleto = nombreCompleto + space;
    					}
    					lineaCliente.append( nombreCompleto);
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    		
    					//VECES EN MORA DE 120 DIAS NUMBER (3)
    					lineaCliente.append( "   " );
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    				
    					//VECES EN MORA DE 150 DIAS NUMBER (3)
    					lineaCliente.append( "   " );
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    				
    					//VECES EN MORA DE 180 DIAS NUMBER (3)
    					lineaCliente.append( "   " );
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    						
    					//VECES EN MORA DE 30 DIAS NUMBER (3)
    					lineaCliente.append( "   " );
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    				
    					//VECES EN MORA DE 60 DIAS NUMBER (3)
    					lineaCliente.append( "   " );
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    				
    					//VECES EN MORA DE 90 DIAS NUMBER (3)
    					lineaCliente.append( "   " );
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    					
    					//ABIERTO PARA USO FUTURO
    					lineaCliente.append( "               " );
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    				
    					//MONTO APERTURA NUMBER (15, 2)
    					//Logger.debug("MONTO APERTURA");
    					String montoApertura = "" + HTMLHelper.formatoMonto(saldo.montoAprobado);
    					montoApertura = montoApertura.replaceAll(",", "");
    					if(saldo.montoAprobado!=0){
    						if(montoApertura.length() < 15){
        						space = "";
        						for(int z = 0; z < 15 - montoApertura.length(); z++){
        							space +=  " ";
        						}
        						montoApertura = space + montoApertura;
        					}	
    					}
    					else{
    						montoApertura = "               ";
    					}
    					lineaCliente.append(montoApertura);
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    				
    					//FECHA APERTURA CREDITO DATE
    					//Logger.debug("FECHA APERTURA");
    					if(saldo.fechaDisposicion!= null){
    						String fechaD = "";
    						String fechaSplit[] = saldo.fechaDisposicion.toString().split("-");
    						fechaD = fechaSplit[1] + fechaSplit[2] + fechaSplit[0];
    						lineaCliente.append(fechaD);
    					}
    					else
    						lineaCliente.append("        ");
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    					
    					//ULTIMO MONTO PAGADO NUMBER (15, 2)---------------------------------
    					//Logger.debug("ULTIMO MONTO PAGADO");
    					//Logger.debug("Pagos::" + pagoVO + " y longitud::" + pagoVO.length);
    					if(pagoVO!=null){
    						if(pagoVO.length > 0){
    							if(pagoVO[pagoVO.length -1].monto!=0){
    								monto = "" + HTMLHelper.formatoMonto(pagoVO[pagoVO.length -1].monto);
    								monto = monto.replaceAll(",", "");
            						if(monto.length()<15){
            							space = "";
            							for(int z = 0; z <15 - monto.length(); z++ ){
            								space += " ";
            							}
            							monto = space + monto;
            						}
            						if(monto.length()>15){
            							monto = monto.substring(0,15);
            						}	
    							}
    							else{
    								monto = "               ";
    							}
        					}
    						else
        						monto = "               ";
    					}
    					else
    						monto = "               ";
    					lineaCliente.append( monto );
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    							
//    					FECHA ULTIMO PAGO DATE---------------------------------------------
    					//Logger.debug("FECHA ULTIMO PAGO");
    					String fecha = "";
    					
    					if(pagoVO != null){
    						//Logger.debug("No es null");
	    					if(pagoVO.length > 0){
	    						//Logger.debug("Longitud mayor a cero");
	    						if(pagoVO[pagoVO.length -1].fechaPago!=null){
	    							//Logger.debug("Fecha de ultimo pago leida::" + pagoVO[pagoVO.length -1].fechaPago);
	    							fecha = "";
	    							String fechaSplit[] = pagoVO[pagoVO.length -1].fechaPago.toString().split("-");
	    							fecha = fechaSplit[1] + fechaSplit[2] + fechaSplit[0];
	    							if(fecha.trim().equalsIgnoreCase("12291899")){
	    								fecha = "        ";
	    							}
	    						}
	    						else{
	    							fecha = "        ";
	    						}
	    					}
	    					else{
	    						fecha = "        ";
	    					}
    					}
    					else
    						fecha = "        ";
    					lineaCliente.append(fecha);
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    					
    					//MONTO DE LIQUIDACION ANTICIPADA
    					//Logger.debug("MONTO DE LIQUIDACION ANTICIPADA");
    					//double montoLiquidacionAnticipada = saldo.totalExigible + saldo.saldoInsoluto + saldo.interesAlDia + saldo.montoInteresesPorCobrar;
    					//double montoLiquidacionAnticipada = saldo.totalExigible + saldo.saldoInsoluto + saldo.montoInteresesPorCobrar;
    					double montoLiquidacionAnticipada = saldo.saldoInsoluto;
    					//Logger.debug("Suma::" + montoLiquidacionAnticipada);
    					montoLiquidacionAnticipada = FormatUtil.roundDouble(montoLiquidacionAnticipada, 2);
    					//montoLiquidacionAnticipada *= 100;
    					//montoLiquidacionAnticipada = java.lang.Math.round(montoLiquidacionAnticipada);
    					//montoLiquidacionAnticipada /=100;
    					String sMontoLiqAnt = "" + HTMLHelper.formatoMonto(montoLiquidacionAnticipada);
    					sMontoLiqAnt = sMontoLiqAnt.replaceAll(",", "");
    					space = "";
    					if(montoLiquidacionAnticipada!=0){
    						if(sMontoLiqAnt.length() < 15){
        						for(int yy = 0; yy < 15 - sMontoLiqAnt.length(); yy++){
        							space += " ";
        						}
        						sMontoLiqAnt = space + sMontoLiqAnt;
        					}	
    					}
    					else{
    						sMontoLiqAnt = "               ";
    					}
    					lineaCliente.append(sMontoLiqAnt);
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    						
//    					FECHA VIGENCIA MONTO DE LIQUIDACION ANTICIPADA
    					//Logger.debug("FECHA VIGENCIA MONTO DE LIQUIDACION ANTICIPADA");
    					space = "";
    					String fechaProx = "        ";
    					if(saldo.fechaSigAmortizacion!=null){
    						String fechaProxVen[] = saldo.fechaSigAmortizacion.toString().split("-");
        					fechaProx = fechaProxVen[1] + fechaProxVen[2] + fechaProxVen[0];
        					if(fechaProx.equalsIgnoreCase("12291899"))
        						fechaProx = "        ";
    					}
    					lineaCliente.append(fechaProx);
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    					
    					//TIPO DE CUENTA/PRODUCTO VARCHAR(6)
    					//Logger.debug("TIPO DE CUENTA");
						switch(saldo.numOperacion){
							case 1:
									lineaCliente.append("CONS  ");
									break;
							case 2:
									lineaCliente.append("MICR  ");
									break;
							case 3:
									lineaCliente.append("COMU  ");
									break;
							case 4:
									lineaCliente.append("VIVI  ");
									break;
							case 5:
									lineaCliente.append("COMU  ");
									break;
							case 21:
									lineaCliente.append("CRHO  ");
									break;
							default:
									lineaCliente.append("      ");
									break;
						}
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    					
    					//ABIERTO PARA USO FUTURO
    					lineaCliente.append("   ");
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    				
    					//NUMERO CLIENTE VARCHAR(25)
    					//Logger.debug("NUMERO CLIENTE");
    					String numClient = saldo.numCliente + "";
    					if(numClient.length()>0){
    						if(numClient.length()<25){
    							space = "";
    							for(int z = 0; z < 25 - numClient.length(); z++){
    								space+= " ";
    							}
    							numClient = saldo.numCliente + space;
    						}
    						lineaCliente.append(numClient);
    					}
    					else
    						lineaCliente.append("                         ");
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    				
    					//ESTADO VARCHAR(5)
    					//Logger.debug("ESTADO");
    					String estado = "";
    					
    					if ( cliente.direcciones!=null && cliente.direcciones.length>0 ){
    						estado = "" + cliente.direcciones[0].numestado;
    						if(estado.length() > 5){
    							estado = cliente.direcciones[0].estado.substring(0,5);
    						}
    						if(estado.length() < 5){
    							space = "";
    							for(int z = 0; z < 5 - estado.length(); z++){
    								space += " ";
    							}
    							estado = estado + space;
    						}
    						else
    							estado = "     ";
    					lineaCliente.append(estado);
    					}
    					else
    						lineaCliente.append("     ");
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    				
    					//CP VARCHAR(10)
    					//Logger.debug("CP");
    					if ( cliente.direcciones!=null && cliente.direcciones.length!=0 ){
    						String cp = cliente.direcciones[0].cp;
    						if(cp.length() < 10){
    							space = "";
    							for(int z = 0; z < 10 - cp.length(); z++){
    								space += " ";
    							}
    							cp = cp + space;
    						}
    						lineaCliente.append(cp);
    					}
    					else
    						lineaCliente.append( "          " );
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    					
    					//ACTIVIDAD EMPRESARIAL
    					lineaCliente.append("    ");
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    				
    					//NUMERO DE AMORTIZACIONES POR VENCER
    					//Logger.debug("NUMERO DE AMORTIZACIONES POR VENCER");
    					String numAmor = "" + saldo.numCuotasRestantes;
    					if(numAmor.length()>0){
    						if(numAmor.length()< 5){
    							space = "";
    							for(int z = 0; z < 5 - numAmor.length(); z++){
    								space += " ";
    							}
    							numAmor = space + numAmor;
    						}
    						lineaCliente.append( numAmor );
    					}
    					else
    						lineaCliente.append("     ");
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    					
    					//NUMERO TOTAL DE AMORTIZACIONES
    					//Logger.debug("NUM. TOTAL DE AMORTIZACIONES");
    					String numCuo = "" +  saldo.numCuotas;
    					if(numCuo.length()>0){
    						if(numCuo.length() < 5){
    							space = "";
    							for(int z = 0; z < 5 - numCuo.length(); z++){
    								space += " ";
    							}
    							numCuo = space + numCuo;
    						}
    						lineaCliente.append( numCuo );
    					}
    					else
    						lineaCliente.append("     ");
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    					
    					//NUMERO DE AMORTIZACIONES VENCIDAS
    					//Logger.debug("NUM. AMORTIZACIONES VENCIDAS");
    					numCuo = "" +  saldo.numeroPagosVencidos;
    					if(numCuo.length()>0){
    						if(numCuo.length() < 5){
    							space = "";
    							for(int z = 0; z < 5 - numCuo.length(); z++){
    								space += " ";
    							}
    							numCuo = space + numCuo;
    						}
    						lineaCliente.append(numCuo);
    					}
    					else
    						lineaCliente.append("     ");
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    					
    					//AMORTIZACION
    					//Logger.debug("AMORTIZACION");
    					lineaCliente.append("    ");
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    					
    					//TELEFONO DOMICILIO VARCHAR(15)
    					//Logger.debug("TELEFONO DOMICILIO");
    					lineaCliente.append( "     " );
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    				
    					//TELEFONO TRABAJO VARCHAR(15)
    					//Logger.debug("TELEFONO TRABAJO");
   						lineaCliente.append( "     " );
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    				
    					//FAX VARCHAR(15)
    					//Logger.debug("FAX");
   						lineaCliente.append( "     " );
    					//lineaCliente.append(InffinixConstants.SEPARADOR);			
    				
    					//TELEFONO OPCIONAL VARCHAR(15)
    					//Logger.debug("TELEFONO OPCIONAL");
   						lineaCliente.append( "     " );
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    				
    					//CODIGO DE ASESOR
    					//Logger.debug("CODIGO ASESOR");
    					String codAses = "" + solicitud.idEjecutivo;
    					if(codAses.length()>0){
    						if(codAses.length() < 5){
    							space = "";
    							for(int z = 0; z < 5 - codAses.length(); z++){
    								space += " ";
    							}
    							codAses = codAses + space;
    						}
    						lineaCliente.append( codAses );
    					}
    					else
    						lineaCliente.append("     ");
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    					
    					//BENEFICIO
    					lineaCliente.append("    ");
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    					
    					//CAPITAL VENCIDO
    					//Logger.debug("CAPITAL VENCIDO");
    					String capVen = "" + HTMLHelper.formatoMonto(saldo.capitalVencido);
    					capVen = capVen.replaceAll(",", "");
    					if(saldo.capitalVencido!=0){
    						if(capVen.length()>0){
        						if(capVen.length() < 15){
        							space = "";
        							for(int z = 0; z < 15 - capVen.length(); z++){
        								space += " ";
        							}
        							capVen = space + capVen;
        						}
        						lineaCliente.append( capVen );
        					}
        					else
        						lineaCliente.append("               ");	
    					}else
    						lineaCliente.append("               ");
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    				
    					//CAPITAL VIGENTE
    					//Logger.debug("CAPITAL VIGENTE");
    					String capVig = "" + HTMLHelper.formatoMonto(saldo.montoAmortizacion);
    					capVig = capVig.replaceAll(",", "");
    					if(saldo.montoAmortizacion!=0){
    						if(capVig.length()>0){
        						if(capVig.length() < 15){
        							space = "";
        							for(int z = 0; z < 15 - capVig.length(); z++){
        								space += " ";
        							}
        							capVig = space + capVig;
        						}
        						lineaCliente.append( capVig );
        					}
        					else
        						lineaCliente.append("               ");	
    					}else
    						lineaCliente.append("               ");
    					
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    					
    					//CARGO POR COBRANZA
    					//Logger.debug("CARGO POR COBRANZA");
    					double multa = saldo.multa + saldo.ivaMulta;
    					String carCob = "" + HTMLHelper.formatoMonto(multa);
    					carCob = carCob.replaceAll(",", "");
    					if(multa!=0){
    						if(carCob.length()>0){
        						if(carCob.length() < 15){
        							space = "";
        							for(int z = 0; z < 15 - carCob.length(); z++){
        								space += " ";
        							}
        							carCob = space + carCob;
        						}
        						lineaCliente.append( carCob );
        					}
        					else
        						lineaCliente.append("               ");	
    					}else
    						lineaCliente.append("               ");
    					
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    					
    					//MONEDA VARCHAR(2)
    					lineaCliente.append(CatalogoHelper.getParametro("MONEDA_INFFINIX") + " ");
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    				
    					//ESTADO CIVIL
    					//Logger.debug("ESTADO CIVIL");
    					String edoCiv = "";
    					switch(cliente.estadoCivil){
    						case 1:
    								edoCiv = "CASD";
    								break;
    						case 2:
    								edoCiv = "SOLT";
    								break;
    						case 3:
    								edoCiv = "UNLI";
    								break;
    						case 4:
    								edoCiv = "DIVO";
    								break;
    						case 5:
    								edoCiv = "VIUD";
    								break;
    						default:
    								edoCiv = "    ";
    					}
    					lineaCliente.append( edoCiv );
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    					
    					//DIRECCION ELECTRONICA
    					if(cliente.correoElectronico!= null){
    						String email = cliente.correoElectronico;
    						if(cliente.correoElectronico.length()>40){
    							email = email.substring(0,40);
    						}
    						if(cliente.correoElectronico.length()<40){
    							space = "";
    							for(int z = 0; z < 40 - email.length(); z++)
    								space += " ";
    							email = email + space;
    						}
    						lineaCliente.append(email);
    					}
    					else
    						lineaCliente.append("                                        ");
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    					
    					//EXT. TELEFONO 1
    					lineaCliente.append("     ");
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    					
    					//EXT. TELEFONO 2
    					lineaCliente.append("     ");
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    		
    					//EXT. TELEFONO 3
    					lineaCliente.append("     ");
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    					
    					//EXT. TELEFONO 4
    					lineaCliente.append("     ");
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    					
    					//ORIGEN DE CREDITO
    					lineaCliente.append("          ");
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    								
    					//NUMERO DE GRUPO
    					//Logger.debug("NUM. GRUPO");
    					String numGpo = "" + cliente.idGrupo;
    					if(numGpo.length()>0){
    						if(numGpo.length() < 4){
    							space = "";
    							for(int z = 0; z < 4 - numGpo.length(); z++){
    								space += "0";
    							}
    							numGpo = numGpo + space;
    						}
    						lineaCliente.append(numGpo);
    					}
    					else
    						lineaCliente.append("   ");
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    					
    					//INTERES VENCIDO
    					//Logger.debug("INTERES VENCIDO");
    					String intVen = "" + HTMLHelper.formatoMonto(saldo.feeVencidos);
    					intVen = intVen.replaceAll(",", "");
    					if(saldo.feeVencidos!=0){
    						if(intVen.length() >0){
        						if(intVen.length() < 15){
        							space = "";
        							for( int z = 0; z < 15 - intVen.length(); z++){
        								space += "0";
        							}
        							intVen = space + intVen;
        						}
        						lineaCliente.append( intVen );
        					}
        					else
        						lineaCliente.append("               ");	
    					}else
    						lineaCliente.append("               ");
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    					
    					//INTERES A HOY
    					//Logger.debug("INTERES A HOY");
    					double inter =  saldo.montoInteresesPorCobrar;
    					inter *= 100;
    					inter = java.lang.Math.round(inter);
    					inter /= 100;
    					saldo.montoInteresesPorCobrar = inter;
    					String intAhoy = "" + HTMLHelper.formatoMonto(saldo.montoInteresesPorCobrar);
    					intAhoy = intAhoy.replaceAll(",", "");
    					if(saldo.montoInteresesPorCobrar!=0){
    						if(intAhoy.length()>0){
        						if(intAhoy.length() < 15){
        							space = "";
        							for( int z = 0; z < 15 - intAhoy.length(); z++){
        								space += "0";
        							}
        							intAhoy = space + intAhoy;
        						}
        						lineaCliente.append( intAhoy );
        					}
        					else
        						lineaCliente.append("               ");	
    					}else
    						lineaCliente.append("               ");
    					
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    				
    					//INTERES MORATORIO
    					//Logger.debug("INTERES MORATORIO");
    					double moratorio = saldo.interesMoratorio;
    					moratorio *= 100;
    					moratorio = java.lang.Math.round(moratorio);
    					moratorio /=100;
    					String intMora = "" + HTMLHelper.formatoMonto(moratorio);
    					intMora = intMora.replaceAll(",", "");
    					if(moratorio!=0){
    						if(intMora.length()>0){
        						if(intMora.length() < 15){
        							space = "";
        							for( int z = 0; z < 15 - intMora.length(); z++){
        								space += "0";
        							}
        							intMora = space + intMora;
        						}
        						lineaCliente.append( intMora );
        					}
        					else
        						lineaCliente.append("               ");	
    					}else
    						lineaCliente.append("               ");
    					
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    				
    					//TELEFONO DOMICILIO VARCHAR(15)
    					//Logger.debug("TELEFONO DOMICILIO");
    					if ( cliente.direcciones!=null && cliente.direcciones[0]!=null && cliente.direcciones[0].telefonos!=null && cliente.direcciones[0].telefonos[0]!=null ){
    						String telCasa = cliente.direcciones[0].telefonos[0].numeroTelefono;
    						if(telCasa!=null){
    							if(telCasa.length()>15){
        							telCasa = telCasa.substring(14 - telCasa.length(), telCasa.length()-1);
        						}
        						if(telCasa.length()<15){
        							space = "";
        							for(int z = 0; z < 15 - telCasa.length(); z++)
        								space += " ";
        							telCasa = telCasa + space;
        							lineaCliente.append(telCasa);
        						}
    						}else
        						lineaCliente.append( "               " );
    						//lineaCliente.append( InffinixUtil.validaCadena( cliente.direcciones[0].telefonos[0].numeroTelefono, "", 15, 0, 15, false) );
    					}
    					else
    						lineaCliente.append( "               " );
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    				
//    					TELEFONO TRABAJO VARCHAR(15)
    					//Logger.debug("TELEFONO TRABAJO");
    					if(solicitud.empleo!=null){
    						if(solicitud.empleo.direccion!=null){
    							String telDom = empVO.telefono;
    							if(telDom!=null){
    								if(telDom.length()>15){
        								telDom = telDom.substring(14 - telDom.length(), telDom.length()-1);
        							}else if(telDom.length()<15){
        								space = "";
        								for(int z = 0; z < 15 - telDom.length(); z++)
        									space += " ";
        								telDom = telDom + space;
        								lineaCliente.append(telDom);
        							}else
        	    						lineaCliente.append( "               " );	
    							}else
    	    						lineaCliente.append( "               " );
    						}else
	    						lineaCliente.append( "               " );
							//lineaCliente.append( InffinixUtil.validaCadena( cliente.direcciones[0].telefonos[0].numeroTelefono, "", 15, 0, 15, false) );
    					}else
    						lineaCliente.append( "               " );
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    				
    					//FAX VARCHAR(15)
    					lineaCliente.append( "               " );
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    				
    					//TELEFONO OPCIONAL VARCHAR(15)
    					//Logger.debug("TELEFONO OPCIONAL");
    					if(cliente.direcciones!= null && cliente.direcciones.length>2){
    						if ( cliente.direcciones!=null && cliente.direcciones[2]!=null && cliente.direcciones[2].telefonos!=null && cliente.direcciones[2].telefonos[0]!=null ){
    							String telOpc = cliente.direcciones[2].telefonos[0].numeroTelefono;
    							if(telOpc!=null){
    								if(telOpc.length()>15){
        								telOpc = telOpc.substring(14 - telOpc.length(), telOpc.length()-1);
        							}
        							if(telOpc.length()<15){
        								space = "";
        								for(int z = 0; z < 15 - telOpc.length(); z++)
        									space += " ";
        								telOpc = telOpc + space;
        								lineaCliente.append(telOpc);
        							}
    							}else
        							lineaCliente.append( "               " );
    							//lineaCliente.append( InffinixUtil.validaCadena( cliente.direcciones[0].telefonos[0].numeroTelefono, "", 15, 0, 15, false) );
    						}
    						else
    							lineaCliente.append( "               " );
    					}
    					else
    						lineaCliente.append( "               " );
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    				
    					//CODIGO DE PLAZA
   						sucVO = sucursales.get(new Integer(cliente.idSucursal));
    					String suc = "" + sucVO.idPlaza;
    					if(suc.length() > 0){
    						if(suc.length() < 5){
    							space = "";
    							for(int z = 0; z < 5 - suc.length(); z++){
    								space += "0"; 
    							}
    							suc = space + suc;
    							lineaCliente.append(suc);
    						}
    					}
    					else
    						lineaCliente.append("     ");		
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    					
    					//PLAN DE PAGO
    					String frecPago = saldo.frecuenciaAmortizacion + "   ";
    					if(frecPago.length()>4){
    						frecPago = frecPago.substring(0, 4);
    					}
    					if(frecPago.length()<4){
    						for(int cont = 0; cont < 4 - frecPago.length(); cont++){
    							frecPago += " ";
    						}
    					}
    					lineaCliente.append(frecPago);
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    					
    					//BENEFICIO APLICABLE
    					lineaCliente.append("       ");
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    					
    					//CODIGO DE REESTRUCTURACION
    					lineaCliente.append( "  " );
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    						
    					//REGIMEN CONYUGAL
    					lineaCliente.append( "    " );
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    					
    					//ZONA RESIDENCIA
    					//Logger.debug("ZONA RESIDENCIA");
    					String region = "" + sucVO.idRegion;
    					if(region.length() > 0){
    						if(region.length() < 10){
    							space = "";
    							for(int z = 0; z < 10 - region.length(); z++){
    								space += " "; 
    							}
    							region += space;
    							lineaCliente.append(region);
    						}
    					}
    					else
    					lineaCliente.append( "          " );
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    		
    					//MONTO CUENTA RELACIONADA
    					lineaCliente.append( "               " );
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    		
    					//FECHA SALDO CUENTA RELACIONADA
    					lineaCliente.append( "        " );
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    					
    					//NUM. CUENTA BANCO RELACIONADA
    					lineaCliente.append( "                " );
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    					
    					//NUM. IDENTIFICACION
    					//Logger.debug("NUM IDENTIFICACION");
    					String rfc = "" + cliente.rfc;
    					if(rfc.length()>0){
    						if(rfc.length()<14){
    							space = "";
    							for(int z = 0; z < 14 - rfc.length(); z++){
    								space += " ";
    							}
    							rfc = rfc + space;
    						}
    						lineaCliente.append( rfc );
    					}
    					else
    						lineaCliente.append("              ");
    					//lineaCliente.append(InffinixConstants.SEPARADOR);
    						
    					//TIPO DE PERSONA
    					lineaCliente.append("    ");
    					////lineaCliente.append(InffinixConstants.SEPARADOR);
    					
    					//SALDO AL CORRIENTE
    					//Logger.debug("SALDO AL CORRIENTE");
    					//Logger.debug("Monto de amortizacion::"+saldo.montoAmortizacion);
    					//Logger.debug("Monto de intereses por cobrar::"+saldo.montoInteresesPorCobrar);
    					saldoDeudor = saldo.montoAmortizacion + saldo.montoInteresesPorCobrar;
    					//saldoDeudor = saldo.interesesDevNoCobrados;
    					saldoDeudor *= 100;
    					saldoDeudor = java.lang.Math.round(saldoDeudor);
    					saldoDeudor /= 100;
    					saldoD = "" + HTMLHelper.formatoMonto(saldoDeudor);
    					saldoD = saldoD.replaceAll(",", "");
    					space = "";
    					if(saldoDeudor!=0){
    						if(saldoD.length()>0){
        						if(saldoD.length() < 15){
        							for(int z = 0; z < 15 - saldoD.length(); z++){
        								space += " ";
        							}
        							saldoD = space + saldoD;
        						}
        						lineaCliente.append( saldoD);
        					}
        					else{
        						lineaCliente.append( "               ");
        					}	
    					}else{
    						lineaCliente.append( "               ");
    					}
    					lineaCliente.append("\n");
        				escribeArchivo(lineaCliente, nomArchivo, true);
    			}
    		}
    	}
    	Logger.debug("Termina generacion de archivo blancos individual...");
    	}
    	catch(ClientesException exc){
    		exc.printStackTrace();
    	}
    }
    
    private void generaArchivoBlanco(GrupoInffinixVO[] grupos){
    	String nomArchivo = CatalogoHelper.getParametro("RUTA_CARPETA_SALIDA_INFFINIX") + CatalogoHelper.getParametro("NOMBRE_ARCHIVO_BLANCOS_INFFINIX");
    	Logger.debug("Grupos a procesar::::" + grupos.length);
    	SucursalDAO sucDAO = new SucursalDAO();
    	try{
    		Logger.debug("Inicia generacion de archivo blancos grupal...");
	    	Hashtable<Integer, SucursalVO> sucursales = sucDAO.getSucursales();
	    	for(int i = 0; grupos!= null && i < grupos.length; i++){
	    		GrupoInffinixVO grupo = grupos[i];
	    		for(int j = 0; grupo.ciclosInffinix!= null && j < grupo.ciclosInffinix.length; j++){
	    			CicloGrupalInffinixVO ciclo = grupo.ciclosInffinix[j];
	    			if(ciclo != null){
	    				PagoVO[] pagoVO = ciclo.pagos;
	    					StringBuffer lineaGrupo = new StringBuffer();
	    					SaldoT24VO saldo = ciclo.saldoT24;
	    					if(saldo==null)
	    						continue;
	    					if(saldo.noCuenta==null || saldo.noCuenta.trim().length()==0){
	    						continue;
	    					}	    					
	    					//****************  COMIENZAN EL LLENADO DEL ARCHIVO DE CARGA PARA CYBER
	    					//CLAVE
	    					lineaGrupo.append("   ");
	    					
	    					//GRUPO DE LA CUENTA VARCHAR 1
	    					lineaGrupo.append(CatalogoHelper.getParametro("GRUPO_CUENTA_INFFINIX"));
	    				
	    					//NUMERO DE LA CUENTA VARCHAR 25
	    					String referencia = saldo.noCuenta;
							referencia = validaCadena (referencia,this.ESPACIOS_25,25,0,25, true);
							referencia = FormatUtil.completaCadena(referencia, ' ', 25, "R");
							lineaGrupo.append(completaCadena(referencia, ' ', 25));
	    					
	    					//DIRECCION1 DEUDOR VARCHAR(80)
							String direccion = validaCadena(saldo.direccion1,this.ESPACIOS_80, 80, 0,80,true);
							direccion = FormatUtil.completaCadena(direccion, ' ', 80, "R");
							lineaGrupo.append(completaCadena(direccion, ' ',80));
	    		
	    					//DIRECCION2 DEUDOR VARCHAR(80)
							direccion = validaCadena(saldo.direccion2,this.ESPACIOS_80, 80, 0,80,false);
							direccion = FormatUtil.completaCadena(direccion, ' ', 80, "R");
							lineaGrupo.append(completaCadena(direccion, ' ',80));
	    					
	    					//STATUS
	    					lineaGrupo.append("                          ");
	    					
	    					//AGENCIA ACTUAL
	    					lineaGrupo.append("        ");
	    		
	    					//MONTO VENCIDO NUMBER (15, 2)
	    					String monto = "";
	    					if(saldo.totalExigible!=0){
	    						monto = monto + HTMLHelper.formatoMonto(saldo.totalExigible);
	        					monto = monto.replaceAll(",", "");
	        					monto = validaCadena(monto, this.ESPACIOS_15, 15, 0, 15, true);
	        					monto = FormatUtil.completaCadena(monto, ' ', 15,"L");    						
	    					}
	    					else
	    						monto = this.ESPACIOS_15;
	    					lineaGrupo.append(monto);
	    				
	    					//DIRECCION TRABAJO1 VARCHAR(80)
	    					lineaGrupo.append(this.ESPACIOS_80);
	    				
	    					//DIRECCION TRABAJO2 VARCHAR(80)
	    					lineaGrupo.append(this.ESPACIOS_80);
	    				
	    					//CIUDAD TRABAJO VARCHAR(40)
							lineaGrupo.append(this.ESPACIOS_40);
	    		
	    					//NOMBRE EMPRESA VARCHAR(80)
	    					lineaGrupo.append(this.ESPACIOS_80);
	    				
	    					//SUCURSAL
	    					String numSuc = "" + grupo.sucursal;
	    					numSuc = validaCadena(numSuc, "00000", 5, 0, 5, true);
	    					numSuc = FormatUtil.completaCadena(numSuc, '0', 5,"L");
	    					lineaGrupo.append( numSuc );
	    				
	    					//ESTADO TRABAJO VARCHAR(5)
	   						lineaGrupo.append( "     " );
	    				
	    					//ZONA POSTAL TRABAJO VARCHAR(10)
	    					lineaGrupo.append( "          " );
	    					
	    					//CIUDAD VARCHAR(40)
	    					DireccionGenericaVO dirVO = ciclo.direccionReunion;
	    					if ( dirVO!=null){
	    							String ciudad= dirVO.municipio;
	    							ciudad = validaCadena(ciudad, "                                        ", 40, 0, 40, false);
	    							ciudad = FormatUtil.completaCadena(ciudad, ' ', 40,"R");
	    							lineaGrupo.append(ciudad);
	    					}else{
	    						lineaGrupo.append( this.ESPACIOS_40 );
	    					}
	    				
	    					//COMPA�IA (6)
	    					lineaGrupo.append( "      " );
	    				
	    					//SALDO DEUDOR NUMBER (15, 2)
	    					double saldoDeudor = FormatUtil.roundDouble(saldo.saldoInsoluto, 2);
	    					String saldoD = "";
	    					if(saldoDeudor!=0){
	    						saldoD = "" + HTMLHelper.formatoMonto(saldoDeudor);
	        					saldoD = saldoD.replaceAll(",", "");
	        					saldoD = validaCadena(saldoD, this.ESPACIOS_15, 15, 0, 15, true);
	        					saldoD = FormatUtil.completaCadena(saldoD, ' ', 15, "L");	
	    					}
	    					else
	    						saldoD= this.ESPACIOS_15;
	    					lineaGrupo.append(saldoD);
	        				    				
	    					//FECHA DE REESTRUCTURACION
	    					lineaGrupo.append("        ");
	    					
	    					//FECHA EN QUE CAYO EN MORA DATE
	    					if(saldo.fechaIncumplimiento!=null && saldo.totalExigible!=0){
	    						String fechaI = "";
	    						String[] fechaSplit = saldo.fechaIncumplimiento.toString().split("-");
	    						fechaI = fechaSplit[1] + fechaSplit[2] + fechaSplit[0];
	    						//Logger.debug("Fecha partida::" + fechaI);
	    						if(!fechaI.trim().equalsIgnoreCase("12291899"))
	    							lineaGrupo.append(fechaI);
	    						else
	    							lineaGrupo.append("        ");
	    					}
	    					else
	    						lineaGrupo.append("        ");
	    				
	    					//NOMBRE DEUDOR VARCHAR(80)
	    					String nombreCompleto = validaCadena(saldo.nombre, this.ESPACIOS_80, 80, 0, 80, true);
	    					nombreCompleto = FormatUtil.completaCadena(nombreCompleto, ' ', 80, "R");
	    					lineaGrupo.append( completaCadena(nombreCompleto, ' ', 80));
	    		
	    					//VECES EN MORA DE 120 DIAS NUMBER (3)
	    					lineaGrupo.append( "   " );
	    				
	    					//VECES EN MORA DE 150 DIAS NUMBER (3)
	    					lineaGrupo.append( "   " );
	    				
	    					//VECES EN MORA DE 180 DIAS NUMBER (3)
	    					lineaGrupo.append( "   " );
	    						
	    					//VECES EN MORA DE 30 DIAS NUMBER (3)
	    					lineaGrupo.append( "   " );
	    				
	    					//VECES EN MORA DE 60 DIAS NUMBER (3)
	    					lineaGrupo.append( "   " );
	    				
	    					//VECES EN MORA DE 90 DIAS NUMBER (3)
	    					lineaGrupo.append( "   " );
	    					
	    					//ABIERTO PARA USO FUTURO
	    					lineaGrupo.append( this.ESPACIOS_15 );
	    				
	    					//MONTO APERTURA NUMBER (15, 2)
	    					double montoApertura = FormatUtil.roundDouble(saldo.montoAprobado,2);
	    					String sMontoApertura = "";
	    					if(montoApertura!=0){
	    						sMontoApertura = HTMLHelper.formatoMonto(montoApertura);
	        					sMontoApertura = sMontoApertura.replaceAll(",", "");
	        					sMontoApertura = validaCadena(sMontoApertura, this.ESPACIOS_15, 15, 0, 15, true);
	        					sMontoApertura = FormatUtil.completaCadena(sMontoApertura, ' ', 15, "L");	
	    					}
	    					else
	    						sMontoApertura = this.ESPACIOS_15;
	    					lineaGrupo.append(sMontoApertura);
	    					
	    					//FECHA APERTURA CREDITO DATE
	    					if(saldo.fechaDisposicion!= null){
	    						String fechaD = "";
	    						String fechaSplit[] = saldo.fechaDisposicion.toString().split("-");
	    						fechaD = fechaSplit[1] + fechaSplit[2] + fechaSplit[0];
	    						lineaGrupo.append(fechaD);
	    					}
	    					else
	    						lineaGrupo.append("        ");
	    					
	    					//ULTIMO MONTO PAGADO NUMBER (15, 2)---------------------------------
	    					monto = "               ";
	    					if(pagoVO!=null && pagoVO.length > 0){
    							if(pagoVO[pagoVO.length -1].monto!=0){
									double montoPagado = FormatUtil.roundDouble(pagoVO[pagoVO.length -1].monto,2);
									String sMontoPagado = "" + montoPagado;
									sMontoPagado = sMontoPagado.replaceAll(",", "");
									sMontoPagado = validaCadena(sMontoPagado, this.ESPACIOS_15, 15, 0, 15, true);
			    					sMontoPagado = FormatUtil.completaCadena(sMontoPagado, ' ', 15, "L");
			    					monto = sMontoPagado;
    							}
    							else{
    	    						monto = this.ESPACIOS_15;
    							}
	    					}
	    					else
	    						monto = this.ESPACIOS_15;
	    					lineaGrupo.append( monto );
	    							
	    					//FECHA ULTIMO PAGO DATE---------------------------------------------
	    					String fecha = "";
	    					
	    					if(pagoVO != null){
		    					if(pagoVO.length > 0){
		    						if(pagoVO[pagoVO.length -1].fechaPago!=null){
		    							fecha = "";
		    							String fechaSplit[] = pagoVO[pagoVO.length -1].fechaPago.toString().split("-");
		    							fecha = fechaSplit[1] + fechaSplit[2] + fechaSplit[0];
		    							if(fecha.trim().equalsIgnoreCase("12291899")){
		    								fecha = "        ";
		    							}
		    						}
		    						else{
		    							fecha = "        ";
		    						}
		    					}
		    					else{
		    						fecha = "        ";
		    					}
	    					}
	    					else
	    						fecha = "        ";
	    					lineaGrupo.append(fecha);
	    					
	    					//MONTO DE LIQUIDACION ANTICIPADA
	    					double montoLiquidacionAnticipada = saldo.saldoInsoluto;
	    					montoLiquidacionAnticipada = FormatUtil.roundDouble(montoLiquidacionAnticipada, 2);
	    					String sMontoLiqAnt = "";
	    					if(montoLiquidacionAnticipada!=0){
	    						sMontoLiqAnt += HTMLHelper.formatoMonto(montoLiquidacionAnticipada);
	        					sMontoLiqAnt = sMontoLiqAnt.replaceAll(",", "");
	        					sMontoLiqAnt = validaCadena(sMontoLiqAnt, this.ESPACIOS_15, 15, 0, 15, true);
	        					sMontoLiqAnt = FormatUtil.completaCadena(sMontoLiqAnt, ' ', 15, "L");	
	    					}
	    					else
	    						sMontoLiqAnt = this.ESPACIOS_15;
	    					lineaGrupo.append(sMontoLiqAnt);
	
	    						
	    					//FECHA VIGENCIA MONTO DE LIQUIDACION ANTICIPADA
	    					String fechaProx = "        ";
	    					if(saldo.fechaSigAmortizacion!=null){
	    						String fechaProxVen[] = saldo.fechaSigAmortizacion.toString().split("-");
	        					fechaProx = fechaProxVen[1] + fechaProxVen[2] + fechaProxVen[0];
	        					if(fechaProx.equalsIgnoreCase("12291899"))
	        						fechaProx = "        ";
	    					}
	    					lineaGrupo.append(fechaProx);
	    					
	    					//TIPO DE CUENTA/PRODUCTO VARCHAR(6)
							switch(saldo.numOperacion){
								case 1:
										lineaGrupo.append("CONS  ");
										break;
								case 2:
										lineaGrupo.append("MICR  ");
										break;
								case 3:
										lineaGrupo.append("COMU  ");
										break;
								case 4:
										lineaGrupo.append("VIVI  ");
										break;
								case 5:
										lineaGrupo.append("COMU  ");
										break;
								case 21:
										lineaGrupo.append("CRHO  ");
										break;
								default:
										lineaGrupo.append("      ");
										break;
							}
	    					
	    					//ABIERTO PARA USO FUTURO
	    					lineaGrupo.append("   ");
	    				
	    					//NUMERO CLIENTE VARCHAR(25)
	    					String numClient = "G" + saldo.numCliente;
	    					numClient = validaCadena(numClient, this.ESPACIOS_25, 25, 0, 25, true);
	    					numClient = FormatUtil.completaCadena(numClient, ' ', 25, "R");
	    					lineaGrupo.append(numClient);
	    					
	    					//ESTADO VARCHAR(5)
	    					String estado = null;
	    					if(dirVO!=null)
	    						estado = "" + dirVO.idEstado;
	    					estado = validaCadena(estado,"     ",5,0,5,true);
	    					estado = FormatUtil.completaCadena(estado, ' ', 5, "R");
	    					lineaGrupo.append(estado);
	    					
	    					//CP VARCHAR(10)
	    					String cp = null;
	    					if(dirVO!=null)
	    						cp = dirVO.cp;
	    					cp = validaCadena(cp, "          ", 10, 0, 10, true);
	    					cp = FormatUtil.completaCadena(cp, ' ', 10, "R");
	    					lineaGrupo.append(cp);
	    					
	    					//ACTIVIDAD EMPRESARIAL
	    					lineaGrupo.append("    ");
	    				
	    					//NUMERO DE AMORTIZACIONES POR VENCER
	    					String numAmor = "" + saldo.numCuotasRestantes;
	    					numAmor = validaCadena(numAmor, "     ", 5, 0, 5, true);
	    					numAmor = FormatUtil.completaCadena(numAmor, ' ', 5, "L");
	    					lineaGrupo.append( numAmor );
	    					
	    					//NUMERO TOTAL DE AMORTIZACIONES
	    					String numTotAmor = "" +  saldo.numCuotas;
	    					numTotAmor = validaCadena(numTotAmor, "     ", 5, 0, 5, true);
	    					numTotAmor = FormatUtil.completaCadena(numTotAmor, ' ', 5, "L");
	    					lineaGrupo.append(numTotAmor);
	    					
	    					//NUMERO DE AMORTIZACIONES VENCIDAS
	    					String numAmorVen = "" +  saldo.numeroPagosVencidos;
	    					numAmorVen = validaCadena(numAmorVen, "     ", 5, 0, 5, true);
	    					numAmorVen = FormatUtil.completaCadena(numAmorVen, ' ', 5, "L");
	    					lineaGrupo.append(numAmorVen);
	    					    					
	    					//AMORTIZACION
	    					lineaGrupo.append("    ");
	    					
	    					//TELEFONO DOMICILIO VARCHAR(15)
	    					lineaGrupo.append( "     " );
	
	    				
	    					//TELEFONO TRABAJO VARCHAR(15)
	   						lineaGrupo.append( "     " );
	    				
	    					//FAX VARCHAR(15)
	   						lineaGrupo.append( "     " );
	    				
	    					//TELEFONO OPCIONAL VARCHAR(15)
	   						lineaGrupo.append( "     " );
	    				
	    					//CODIGO DE ASESOR
	    					String codAses = "" + ciclo.asesor;
	    					codAses = validaCadena(codAses, "     ", 5, 0, 5, true);
	    					codAses = FormatUtil.completaCadena(codAses, ' ', 5, "R");
	    					lineaGrupo.append( codAses );
	    					
	    					//BENEFICIO
	    					lineaGrupo.append("    ");
	    					
	    					//CAPITAL VENCIDO
	    					double dCapVen = FormatUtil.roundDouble(saldo.capitalVencido, 2);
	    					String capVen = ""; 
	    					if(dCapVen!=0){
	    						capVen = HTMLHelper.formatoMonto(dCapVen);
	    						capVen = capVen.replaceAll(",", "");
	        					capVen = validaCadena(capVen, this.ESPACIOS_15, 15, 0, 15, true);
	        					capVen = FormatUtil.completaCadena(capVen, ' ', 15, "L");
	    					}
	    					else
	    						capVen = this.ESPACIOS_15;
	    					lineaGrupo.append( capVen );
	    					
	    					//CAPITAL VIGENTE
	    					double dCapVig = FormatUtil.roundDouble(saldo.montoAmortizacion, 2);
	    					String capVig = ""; 
	    					if(dCapVig!=0){
	    						capVig = HTMLHelper.formatoMonto(dCapVig);
	    						capVig = capVig.replaceAll(",", "");
	        					capVig = validaCadena(capVig, this.ESPACIOS_15, 15, 0, 15, true);
	        					capVig = FormatUtil.completaCadena(capVig, ' ', 15, "L");
	    					}
	    					else
	    						capVig = this.ESPACIOS_15;
	    					lineaGrupo.append( capVig );
	    					
	    					//CARGO POR COBRANZA
	    					double multa = saldo.multa + saldo.ivaMulta;
	    					multa = FormatUtil.roundDouble(multa, 2);
	    					String carCob = ""; 
	    					if(multa!=0){
	    						carCob = HTMLHelper.formatoMonto(multa);
	    						carCob = carCob.replaceAll(",", "");
	        					carCob = validaCadena(carCob, this.ESPACIOS_15, 15, 0, 15, true);
	        					carCob = FormatUtil.completaCadena(carCob, ' ', 15, "L");
	    					}
	    					else
	    						carCob = this.ESPACIOS_15;
	    					lineaGrupo.append( carCob );
	        				    					
	    					//MONEDA VARCHAR(2)
	    					lineaGrupo.append(CatalogoHelper.getParametro("MONEDA_INFFINIX") + " ");
	    				
	    					//ESTADO CIVIL
	    					String edoCiv = "    ";
	    					lineaGrupo.append( edoCiv );
	    					
	    					//DIRECCION ELECTRONICA
	    					lineaGrupo.append("                                        ");
	    					
	    					//EXT. TELEFONO 1
	    					lineaGrupo.append("     ");
	    					
	    					//EXT. TELEFONO 2
	    					lineaGrupo.append("     ");
	    					
	    					//EXT. TELEFONO 3
	    					lineaGrupo.append("     ");
	    					
	    					//EXT. TELEFONO 4
	    					lineaGrupo.append("     ");
	    					
	    					//ORIGEN DE CREDITO
	    					lineaGrupo.append("          ");
	    								
	    					//NUMERO DE GRUPO
	    					String numGpo = "" + grupo.idGrupo;
	    					numGpo = validaCadena(numGpo, "    ", 4, 0, 4, true);
	    					numGpo = FormatUtil.completaCadena(numGpo, ' ', 4, "R");
	    					lineaGrupo.append(numGpo);
	    					
	    					//INTERES VENCIDO
	    					double dIntVen = FormatUtil.roundDouble(saldo.feeVencidos, 2);
	    					String intVen = "";
	    					if(dIntVen!=0){
	    						intVen = "" + HTMLHelper.formatoMonto(dIntVen);
	        					intVen = intVen.replaceAll(",", "");
	        					intVen = validaCadena(intVen, this.ESPACIOS_15, 15, 0, 15, true);
	        					intVen = FormatUtil.completaCadena(intVen, '0', 15, "L");	
	    					}
	    					else
	    						intVen = this.ESPACIOS_15;
	    					lineaGrupo.append( intVen );
	    					
	    					//INTERES A HOY
	    					double inter =  FormatUtil.roundDouble(saldo.montoInteresesPorCobrar,2);
	    					String intAhoy = "";
	    					if(inter != 0){
	    						intAhoy = "" + HTMLHelper.formatoMonto(inter);
	    						intAhoy = intAhoy.replaceAll(",", "");
	        					intAhoy = validaCadena(intAhoy, this.ESPACIOS_15, 15, 0, 15, true);
	        					intAhoy = FormatUtil.completaCadena(intAhoy, '0', 15, "L");
	    					}
	    					else
	    						intAhoy = this.ESPACIOS_15;
	    					lineaGrupo.append( intAhoy );
	    				
	    					//INTERES MORATORIO
	    					double moratorio = FormatUtil.roundDouble(saldo.interesMoratorio, 2);
	    					String intMora = "";
	    					if(moratorio!=0){
	    						intMora = "" + HTMLHelper.formatoMonto(moratorio);
	    						intMora = intMora.replaceAll(",", "");
	        					intMora = validaCadena(intMora, this.ESPACIOS_15, 15, 0, 15, true);
	        					intMora = FormatUtil.completaCadena(intMora, '0', 15, "L");
	    					}
	    					else
	    						intMora = this.ESPACIOS_15;
	    					lineaGrupo.append( intMora );
	    					
	    					//TELEFONO DOMICILIO VARCHAR(15)
	    					String telCasa = saldo.telefono;
	    					if(telCasa==null)
	    						telCasa = this.ESPACIOS_15;
	    					else{
	    						telCasa = validaCadena(telCasa, this.ESPACIOS_15, 15, 0, 15, true);
	    						telCasa = FormatUtil.completaCadena(telCasa, ' ', 15, "R");
	   						}
	    					lineaGrupo.append(telCasa);
	    					
	    					//TELEFONO TRABAJO VARCHAR(15)
	    					lineaGrupo.append(this.ESPACIOS_15);
	    				
	    					//FAX VARCHAR(15)
	    					lineaGrupo.append(this.ESPACIOS_15);
	    				
	    					//TELEFONO OPCIONAL VARCHAR(15)
	    					lineaGrupo.append(this.ESPACIOS_15);
	    				
	    					//CODIGO DE PLAZA
	    					SucursalVO suc = sucursales.get(new Integer(grupo.sucursal));
	    					String sucur = "" + suc.idPlaza;
	    					sucur = FormatUtil.completaCadena(sucur, '0', 5, "L");
	    					lineaGrupo.append(sucur);
	    					
	    					//PLAN DE PAGO
	    					String frecPago = saldo.frecuenciaAmortizacion;
	    					frecPago = validaCadena(frecPago, "    ", 4, 0, 4, true);
	    					frecPago = FormatUtil.completaCadena(frecPago, ' ', 4, "R");
	    					lineaGrupo.append(frecPago);
	    					
	    					//BENEFICIO APLICABLE
	    					lineaGrupo.append("       ");
	    					
	    					//CODIGO DE REESTRUCTURACION
	    					lineaGrupo.append( "  " );
	    						
	    					//REGIMEN CONYUGAL
	    					lineaGrupo.append( "    " );
	    					
	    					//ZONA RESIDENCIA
	    					String region = "" + suc.idRegion;
	    					region = validaCadena(region, "          ", 10, 0, 10, true);
	    					region = FormatUtil.completaCadena(region, ' ', 10, "R");
	    					lineaGrupo.append(region);
	    		
	    					//MONTO CUENTA RELACIONADA
	    					lineaGrupo.append( "               " );
	    		
	    					//FECHA SALDO CUENTA RELACIONADA
	    					lineaGrupo.append( "        " );
	    					
	    					//NUM. CUENTA BANCO RELACIONADA
	    					lineaGrupo.append( "                " );
	    					
	    					//NUM. IDENTIFICACION
	    					String rfc = "" + grupo.rfc;
	    					rfc = validaCadena(rfc, "              ", 14, 0, 14, true);
	    					rfc = FormatUtil.completaCadena(rfc, ' ', 14, "R");
	    					lineaGrupo.append( rfc );
	    						
	    					//TIPO DE PERSONA
	    					lineaGrupo.append("    ");
	    					
	    					//SALDO AL CORRIENTE
	    					saldoDeudor = saldo.montoAmortizacion + saldo.montoInteresesPorCobrar;
	    					saldoDeudor = FormatUtil.roundDouble(saldoDeudor, 2);
	    					if(saldoDeudor!=0){
	    						saldoD = "" + HTMLHelper.formatoMonto(saldoDeudor);
	    						saldoD = "" + HTMLHelper.formatoMonto(saldoDeudor);
	        					saldoD = saldoD.replaceAll(",", "");
	        					saldoD = validaCadena(saldoD, this.ESPACIOS_15, 15, 0, 15, true);
	        					saldoD = FormatUtil.completaCadena(saldoD, ' ', 15, "L");
	    					}else
	    						saldoD = this.ESPACIOS_15;
	    					lineaGrupo.append( saldoD);
	        				
	    					lineaGrupo.append("\n");
	        				escribeArchivo(lineaGrupo, nomArchivo, true);
	        				//out.println("["+lineaGrupo.toString()+"]");
	        				//response.setContentType("text/plain");
	        				//response.setHeader("Content-Disposition","attachment; filename=\"clientesInfinnixCreditoFirme.txt\"");
	        				//response.setHeader("cache-control", "no-cache");
	    			}
	    		}
	    	}
	    	Logger.debug("Termina generacion de archivo blancos grupal...");
    	}catch(ClientesException exc){
    		exc.printStackTrace();
    	}
    }
    
    
    private void generaArchivo500(){
    	String sNomArch = CatalogoHelper.getParametro("RUTA_CARPETA_SALIDA_INFFINIX") + CatalogoHelper.getParametro("NOMBRE_ARCHIVO_500_INFFINIX");
    	verificaExistenciaArchivo(sNomArch);
    	PagoDAO pagoDAO = new PagoDAO();
    	PagoVO[] pagos = pagoDAO.getNoEnviados();

    	Logger.debug("Inicia generacion de archivo 500...");
    	if(pagos!=null){
    		for(int y = 0; y < pagos.length; y++){
    			PagoVO pago = pagos[y];
    			StringBuffer lineaPagosT24 = new StringBuffer();
    			
    			String fecha = "";
    			String monto = "";
    			String fechaTransaccion = "";
    			if(pago!=null){
    				if(pago.fechaHora!=null){
    					String fechaSplit[] = pago.fechaHora.toString().split("-");
    					String auxFecha[] = fechaSplit[2].split(" ");
    					fecha = fechaSplit[1] + auxFecha[0] + fechaSplit[0];
    					if(fecha.length()>8){
    						fecha = fecha.substring(0,8);
    					}
    					if(fecha.length()<8){
    						String space = "";
    						for(int z = 0; z <8 - fecha.length(); z++ ){
    							space += " ";
    						}
    						fecha = space + fecha;
    					}
    				}
    				else
    					fecha = "        ";
    				monto = "" + pago.monto;
    				if(monto.length()<15){
    					String space = "";
    					for(int z = 0; z < 15 - monto.length(); z++)
    						space += " ";
    					monto = space + monto;
    				}
    				else
    					monto = "               ";
    				if(pago.fechaPago!=null){
    					String[] fechaSplit = pago.fechaPago.toString().split("-");
    					fechaTransaccion = fechaSplit[1] + fechaSplit[2] + fechaSplit[0];
    				}
    				else
    					fechaTransaccion = "        ";
    			}
    			else{
    				fecha = "        ";
    				fechaTransaccion = "        ";
    				monto = "               ";
    			}


    			//CLAVE
    			//Logger.debug("CLAVE");
    			lineaPagosT24.append("500");

    	 		//GRUPO
    	 		//Logger.debug("GRUPO");
    	 		lineaPagosT24.append("2");

    			//CUENTA
    			//Logger.debug("CUENTA");
    	 		String cuenta = null;
    	 		if(pago.referencia!=null)
    	 			cuenta = pago.referencia;
    	 		else
    	 			cuenta = "                         ";
    			//if(cuenta.substring(0, 2).equalsIgnoreCase("LD") && cuenta.length()>12)
    			//	cuenta = cuenta.substring(0,12);
    			if(cuenta != null){
    				cuenta = cuenta.trim();
    				if(cuenta.length()>25){
    					cuenta = cuenta.substring(0,25);
    				}
    				if(cuenta.length()<25){
    					String espacio = "";
    					for(int z = 0; z < 25 - cuenta.length(); z++)
    						espacio +=  " ";
    					cuenta += espacio;
    				}
    			}
    			lineaPagosT24.append(cuenta);

    			//AGENCIA
    			//Logger.debug("AGENCIA");
    			lineaPagosT24.append("        ");

    			//GESTOR
    			//Logger.debug("GESTOR");
    			lineaPagosT24.append("        ");
    			
    			//FECHA DE TRANSACCION
    			//Logger.debug("FECHA DE TRANSACCION");
    			lineaPagosT24.append(fechaTransaccion);
    			
    			//MONTO
    			//Logger.debug("MONTO");
    			lineaPagosT24.append(monto);

    			//CODIGO DE TRANSACCION
    			//Logger.debug("CODIGO DE TRANSACCION");
    			lineaPagosT24.append("      ");

    			//FECHA DE REGISTRO DE TRANSACCION -----------------------
    			//Logger.debug("FECHA DE REGISTRO DE TRANSACCION");
    			lineaPagosT24.append(fecha);
    			
    			//TRANSACCION:DESCRIPCION
    			//Logger.debug("TRANSACCION:DESCRIPCION");
    			lineaPagosT24.append("PAGO           ");
    			
    			lineaPagosT24.append("\n");
    			escribeArchivo(lineaPagosT24, sNomArch, true);
    		}
    		for(int i = 0; i < pagos.length; i++){
    			pagos[i].enviado = 1;
    			pagoDAO.updatePagosCartera(pagos[i], pagos[i].referencia);
    		}
    		Logger.debug("Termina generacion de archivo 500...");
    	}
    }
    
    private void generaArchivo300(ClienteInffinix[] clientes){
    	String sNomArch = CatalogoHelper.getParametro("RUTA_CARPETA_SALIDA_INFFINIX") + CatalogoHelper.getParametro("NOMBRE_ARCHIVO_300_INFFINIX"); // FALTA EXTRAER EL NOMBRE DEL ARCHIVO DE LA TABLA DE PARAMETROS
    	verificaExistenciaArchivo(sNomArch);
    	for(int i = 0; clientes!= null && i < clientes.length; i++){
    		ClienteInffinix cliente = clientes[i];
    		//Logger.debug("Cliente::" + cliente.rfc);
    		//Logger.debug("Dentro del primer for...");
    		for(int j = 0; cliente.solicitudes!= null && j < cliente.solicitudes.length; j++){
    			//Logger.debug("Dentro del segundo for...");
    			SolicitudInfinix solicitud = cliente.solicitudes[j];
    			if(solicitud.saldo==null)
    				continue;
    			PagoVO[] pagoVO = solicitud.pagos;
    				//Logger.debug("Dentro del tercer for...");
    				//Logger.debug("Generando el 300...");
    				SaldoT24VO saldo = solicitud.saldo;
					//Logger.debug(saldo.contrato + ":");
					StringBuffer lineaPagosT24 = new StringBuffer();
					
					//CLAVE
					//Logger.debug("CODIGO DE TRANSACCION");
					String status = "";
					double saldoDeudor = (saldo.saldoInsoluto + saldo.montoInteresesPorCobrar) + (saldo.totalExigible);
					saldoDeudor *= 100;
					saldoDeudor = java.lang.Math.round(saldoDeudor);
					saldoDeudor /= 100;
//					if(saldo.diasVencidos > 180 && (saldo.totalExigible == 0 && (saldo.montoAmortizacion + saldo.montoInteresesPorCobrar)>0))
//						status = "301";
//					else 
					if( saldo.situacionActualCredito==ClientesConstants.SITUACION_CREDITO_SALDOST24_LIQUIDADO && saldoDeudor<=0 )//DMCURBAL == 0
						status = "302";
					else
						continue;
						//status = "   ";
					lineaPagosT24.append(status);
					
					//GRUPO
					//Logger.debug("GRUPO");
					lineaPagosT24.append("2");
					
					//CUENTA
					//Logger.debug("CUENTA");
					String referencia = saldo.noCuenta;
					if(referencia != null){
						referencia = referencia.trim();
						if(referencia.length()>25){
							referencia = referencia.substring(0,25);
						}
						if(referencia.length()<25){
							String espacio = "";
							for(int z = 0; z < 25 - referencia.length(); z++)
								espacio +=  " ";
							referencia += espacio;
						}
					}
					else
						referencia = "                         ";
					lineaPagosT24.append(referencia);
					
					//FECHA DE CAMBIO DE STATUS
					//Logger.debug("FECHA DE CAMBIO DE STATUS");
					String monto = "";
					String fechaTransaccion = "";
					if(pagoVO!=null && pagoVO.length > 0 && !status.equalsIgnoreCase("301")){
						monto = "" + pagoVO[pagoVO.length -1].monto;
						if(monto.length()<15){
							String space = "";
							for(int z = 0; z < 15 - monto.length(); z++)
								space += " ";
							monto = space + monto;
						}
						else
							monto = "               ";
						if(pagoVO[pagoVO.length -1].fechaPago!=null){
							String[] fechaSplit = pagoVO[pagoVO.length -1].fechaPago.toString().split("-");
							fechaTransaccion = fechaSplit[1] + fechaSplit[2] + fechaSplit[0];
							if(fechaTransaccion.equalsIgnoreCase("12291899")){
								fechaTransaccion = "        ";
							}
						}
						else
							fechaTransaccion = "        ";
					}
					else{
						fechaTransaccion = "        ";
						monto = "               ";
					}
					
					
					
					lineaPagosT24.append(fechaTransaccion);
		
					//MONTO CON EL Q ADQUIRIO EL STATUS DE R o S
					//Logger.debug("MONTO CON EL Q ADQUIRIO EL STATUS DE R o S");
					lineaPagosT24.append(monto);
    				lineaPagosT24.append("\n");
    				escribeArchivo(lineaPagosT24, sNomArch, true);
    				//out.println(lineaPagosT24.toString());
    				//response.setContentType("text/plain");
    				//response.setHeader("Content-Disposition","attachment; filename=\"clientesInfinnix300.txt\"");
    				//response.setHeader("cache-control", "no-cache");
    		}
    	}
    }
    
    
    private void generaArchivo300(GrupoInffinixVO[] grupos){
    	String sNomArch = CatalogoHelper.getParametro("RUTA_CARPETA_SALIDA_INFFINIX") + CatalogoHelper.getParametro("NOMBRE_ARCHIVO_300_INFFINIX"); // FALTA EXTRAER EL NOMBRE DEL ARCHIVO DE LA TABLA DE PARAMETROS
    	//verificaExistenciaArchivo(sNomArch);
    	Logger.debug("$$$$$$__Dentro de generacion de archivo 300 Grupal__$$$$$$");
    	for(int i = 0; grupos!= null && i < grupos.length; i++){
    		GrupoInffinixVO grupo = grupos[i];
    		for(int j = 0; grupo.ciclosInffinix!= null && j < grupo.ciclosInffinix.length; j++){
    			CicloGrupalInffinixVO ciclo = grupo.ciclosInffinix[j];
    			PagoVO[] pagoVO = ciclo.pagos;
    				SaldoT24VO saldo = ciclo.saldoT24;
					StringBuffer lineaPagosT24 = new StringBuffer();
					if(saldo==null)
						continue;
					//CLAVE
					//Logger.debug("CODIGO DE TRANSACCION");
					String status = "";
					double saldoDeudor = (saldo.saldoInsoluto + saldo.montoInteresesPorCobrar) + (saldo.totalExigible);
					saldoDeudor = FormatUtil.roundDouble(saldoDeudor, 2);
//					if(saldo.diasVencidos > 180 && (saldo.totalExigible == 0 && (saldo.montoAmortizacion + saldo.montoInteresesPorCobrar)>0))
//						status = "301";
					if( saldo.situacionActualCredito==ClientesConstants.SITUACION_CREDITO_SALDOST24_LIQUIDADO && saldoDeudor<=0 )//DMCURBAL == 0
						status = "302";
					else
						continue;
						//status = "   ";
					lineaPagosT24.append(status);
					
					//GRUPO
					lineaPagosT24.append("2");
					
					//CUENTA
					String referencia = saldo.noCuenta;
					referencia = validaCadena(referencia, this.ESPACIOS_25, 25, 0, 25, true);
					referencia = FormatUtil.completaCadena(referencia, ' ', 25, "R");
					lineaPagosT24.append(completaCadena(referencia, ' ', 25));
					
					//FECHA DE CAMBIO DE STATUS
					String monto = "";
					String fechaTransaccion = "";
					if( ( pagoVO!=null && pagoVO.length>0 ) && !status.equalsIgnoreCase("301")){
						monto = "" + pagoVO[pagoVO.length -1].monto;
						monto = monto.replaceAll(",", "");
						monto = validaCadena(monto, this.ESPACIOS_15, 15, 0, 15, true);
						monto = FormatUtil.completaCadena(monto, ' ', 15, "L");
						monto = completaCadena(monto, ' ', 15);
						if(pagoVO[pagoVO.length -1].fechaPago!=null){
							String[] fechaSplit = pagoVO[pagoVO.length -1].fechaPago.toString().split("-");
							fechaTransaccion = fechaSplit[1] + fechaSplit[2] + fechaSplit[0];
							if(fechaTransaccion.equalsIgnoreCase("12291899")){
								fechaTransaccion = "        ";
							}
						}
						else
							fechaTransaccion = "        ";
					}
					else{
						fechaTransaccion = "        ";
					}
					
					lineaPagosT24.append(fechaTransaccion);
		
					//MONTO CON EL Q ADQUIRIO EL STATUS DE R o S
					//Logger.debug("MONTO CON EL Q ADQUIRIO EL STATUS DE R o S");
					lineaPagosT24.append(monto);
    				lineaPagosT24.append("\n");
    				escribeArchivo(lineaPagosT24, sNomArch, true);
    				//out.println(lineaPagosT24.toString());
    				//response.setContentType("text/plain");
    				//response.setHeader("Content-Disposition","attachment; filename=\"clientesInfinnix300.txt\"");
    				//response.setHeader("cache-control", "no-cache");
    		}
    	}
    }
    
    private void escribeArchivo(StringBuffer str, String sNomArch, boolean tipoAperturaArchivo){
    	try{
    		BufferedWriter bwArch = new BufferedWriter(new FileWriter(new File(sNomArch), tipoAperturaArchivo));
    		bwArch.write(str.toString());
    		bwArch.close();
    	}
    	catch(FileNotFoundException exc){
    		System.out.println("FileNotFoundException en InffinixUtil.escribeArchivo::" + exc);
    	}
    	catch(IOException exc){
    	}
    }
    
    public int procesoEnvioArchivosInffinix(){
    	
    	int estado = 1;
    	try{
    		SaldoT24VO[] saldosVO = leeArchivo();
    		SaldoT24VO[] saldosIbsToT24 = new IBSHelper().getSaldosT24FromSaldosIBS();
    		
    		if(saldosVO!=null){
    			SimpleDateFormat sdf = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_EU);
    			Logger.debug("Cargando saldos...");
    			SaldoT24DAO saldoDAO = new SaldoT24DAO();
    			//new PagoDAO().actualizaPagosEnviados();
    			saldoDAO.deleteSaldoT24();
    			guardaSaldos(saldosVO,saldoDAO);
    			guardaSaldos(saldosIbsToT24,saldoDAO);
    			System.gc();
    			ClienteInffinix[] clientes = generaListaClientes();
    			GrupoInffinixVO[] grupos = generaListaGrupos();
    			Logger.debug("Creando archivos ...");
    			generaArchivoBlanco(clientes);
    			generaArchivoBlanco(grupos);
    			generaArchivo500();
    			generaArchivo300(clientes);
    			generaArchivo300(grupos);
//    			Logger.debug("Iniciando proceso de envio...");
//    			ComunicacionUtil comun = new ComunicacionUtil();
//    			comun.openSFTPConnection(CatalogoHelper.getParametro("IP_SERVIDOR_INFFINIX"), CatalogoHelper.getParametro("USR_SERVIDOR_INFFINIX"), CatalogoHelper.getParametro("PWD_SERVIDOR_INFFINIX"));
//    			Logger.debug("********Enviando primer archivo...");
//    			comun.sendSFTP(CatalogoHelper.getParametro("RUTA_CARPETA_SALIDA_INFFINIX") + CatalogoHelper.getParametro("NOMBRE_ARCHIVO_BLANCOS_INFFINIX"), CatalogoHelper.getParametro("RUTA_SERVIDOR_INFFINIX"), InffinixConstants.MODO_ENVIO_INFFINIX);
//    			Logger.debug("********Enviando segundo archivo...");
//    			comun.sendSFTP(CatalogoHelper.getParametro("RUTA_CARPETA_SALIDA_INFFINIX") + CatalogoHelper.getParametro("NOMBRE_ARCHIVO_500_INFFINIX"), CatalogoHelper.getParametro("RUTA_SERVIDOR_INFFINIX"), InffinixConstants.MODO_ENVIO_INFFINIX);
    			//new PagoDAO().respaldaPagosEnviados();
//    			comun.sendSFTP(CatalogoHelper.getParametro("RUTA_CARPETA_SALIDA_INFFINIX") + CatalogoHelper.getParametro("NOMBRE_ARCHIVO_300_INFFINIX"), CatalogoHelper.getParametro("RUTA_SERVIDOR_INFFINIX"), InffinixConstants.MODO_ENVIO_INFFINIX);
//    			Logger.debug("********Cerrando conexion...");
//    			comun.closeSFTPConnection();    			
    			this.renombraArchivo(CatalogoHelper.getParametro("RUTA_CARPETA_ENTRADA_INFFINIX"));
    			CatalogoHelper.updateParametro("FECHA_EJECUCION_INFFINIX", sdf.format(new Date()));
    			estado = 0;
    		}
    	}
    	catch(Exception exc){
    		Logger.debug("Exception en InffinixUtil.procesoEnvioArchivosInffinix::" + exc);
    		exc.printStackTrace();
    	}
    	return estado;
    }
    
    public void actualizaCamposSaldosT24(){
		SaldoT24DAO saldoDAO = new SaldoT24DAO();
		saldoDAO.actualizaTotales();
	}
    
    
    public boolean comparaFechasInffinix(){
    	boolean esFechaAnterior = false;
    	try{
			// Inicia comparacion entre la fecha actual y la ultima fecha de ejecucion de proceso
			String ultimaFecha = CatalogoHelper.getParametro("FECHA_EJECUCION_INFFINIX");
			SimpleDateFormat format = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_EU);
			Date fechaUltimoProceso = format.parse(ultimaFecha);
			Calendar hoy = Calendar.getInstance();
			Date fechaHoy = format.parse( hoy.get(Calendar.YEAR)+"-"+(hoy.get(Calendar.MONTH)+1)+"-"+hoy.get(Calendar.DAY_OF_MONTH) );
			if(fechaHoy.compareTo(fechaUltimoProceso)>0)
				esFechaAnterior = true;
    	}
    	catch(ParseException exc){
    		Logger.debug("ParseException en InffinixUtil.comparaFechasInffinix::" + exc);
    		exc.printStackTrace();
    	}
    	finally{
    		
    	}
    	return esFechaAnterior;
    }


    public int procesoCargaSaldos(){
    	
    	int estado = 1;
    	try{
    		SaldoT24VO[] saldosVO = leeArchivo();
    		SaldoT24VO[] saldosIbsToT24 = new IBSHelper().getSaldosT24FromSaldosIBS();
    		
    		if(saldosVO!=null){
    			Logger.debug("Cargando saldos...");
    			SaldoT24DAO saldoDAO = new SaldoT24DAO();
    			saldoDAO.deleteSaldoT24();
    			guardaSaldos(saldosVO,saldoDAO);
    			guardaSaldos(saldosIbsToT24,saldoDAO);
    			Logger.debug("Actualizando d_saldos_t24...");
    			actualizaCamposSaldosT24();
    			System.gc();
    			//TEMPORALMENTE SE USA UN PARAMETRO QUE NO SE LEE EN NINGUNA OTRA PARTE DE LA APLICACION
    			//PARA QUE LA CARGA DE SALDOS SE REALICE SOLO UNA VEZ AL DIA
    			CatalogoHelper.updateParametro("ELIMINACION_AUT_TABLAS_AMORT", "S");
    			CatalogoHelper.updateParametro("HORA_INICIO_INFFINIX", "12");
    			estado = 0;
    		}
    	}
    	catch(Exception exc){
    		Logger.debug("Exception en InffinixUtil.procesoEnvioArchivosInffinix::" + exc);
    		exc.printStackTrace();
    	}
    	return estado;
    }


}