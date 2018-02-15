package com.sicap.clientes.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.sicap.clientes.exceptions.ValidationException;

/**
 *
 * @author  hugo
 */
public class ValidaDatos {
    /**************************************************************************/
    /*                         Validacion RFC                                 */
    /**************************************************************************/
    public static boolean isRfcPM (String rfc)
    throws ValidationException{
        boolean isRfcPm = true;
        if (rfc == null || rfc.trim().length() == 0) return isRfcPm;
        String msg_error = "";
        rfc = rfc.trim();
        String inicNombre = rfc.substring ( 0, 3 );
        String fecha = rfc.substring ( 3 );

        char [] cRfc = inicNombre.toCharArray();
        for ( int i=0; i<cRfc.length; i++ ) {
            if ( ! esAlfabetico ( cRfc[i] ) ) {
                msg_error += "Formato de Iniciales Inválido (\"" + inicNombre + "\") para RFC de Persona Moral ";
                isRfcPm = false;
                break;
            }
        }

        if  ( ! isFecha ( fecha, "yyMMdd" ) ) {
            msg_error = "Formato de Fecha Inválido ( \"" + fecha + "\" ) para RFC de Persona Moral ";
            isRfcPm = false;
        }

        if ( ! isRfcPm ) {
            throw new ValidationException ( msg_error, null, null);
        }
        return isRfcPm;
    }

        public static boolean isRfcPF (String rfc) 
        throws ValidationException{
            boolean isRfcPm = true;
            if (rfc == null || rfc.trim().length() == 0) return isRfcPm;
            String msg_error = "";		
            rfc = rfc.trim();

            if ( rfc.length() < 10 ) {
            msg_error = "Longitud Inválida (\"" + rfc + "\") para RFC de Persona Física ";
            throw new ValidationException ( msg_error, null, null );
        }
        
        String inicNombre = rfc.substring ( 0, 4 );
        String fecha = rfc.substring ( 4 );

        char [] cRfc = inicNombre.toCharArray();
        for ( int i=0; i<cRfc.length; i++ ) {
            if ( ! esAlfabetico ( cRfc[i] ) ) {
                msg_error += "Formato de Iniciales Inválido (\"" + inicNombre + "\") para RFC de Persona Física ";
                isRfcPm = false;
                break;
            }
        }

        if  ( ! isFecha ( fecha, "yyMMdd" ) ) {
            msg_error = "Formato de Fecha Inválido ( \"" + fecha + "\" ) para RFC de Persona Física ";
            isRfcPm = false;
        }

        if ( ! isRfcPm ) {
            throw new ValidationException ( msg_error, null, null );
        }
        return isRfcPm;
    }

    public static boolean isRfcPMHomoclave  (String rfc) 
    throws ValidationException{
        boolean isRfcPm = true;
        if (rfc == null || rfc.trim().length() == 0) return isRfcPm;
        String msg_error = "";		
        rfc 		  = rfc.trim();
        if (rfc.length() <= 9 ) {
            msg_error = "Formato de Tamaño Inválido para RFC (AAAyyMMddXXX)";
            //msg_error += "Formato de RFC Inv�lido (\"" + rfc + "\") para RFC de Persona Moral ";
            throw new ValidationException ( msg_error, null, null);
        }
        
        String inicNombre = rfc.substring ( 0, 3 );
        String fecha      = rfc.substring ( 3, 9 );
        String homoclave  = rfc.substring ( 9 );

        char [] cRfc = inicNombre.toCharArray();
        for ( int i=0; i<cRfc.length; i++ ) {
            if ( ! esAlfabetico ( cRfc[i] ) ) {
                msg_error += "Formato de Iniciales Inválido (\"" + inicNombre + "\") para RFC de Persona Moral ";
                isRfcPm = false;
                break;
            }
        }

        if  ( ! isFecha ( fecha, "yyMMdd" ) ) {
            msg_error = "Formato de Fecha Inválido ( \"" + fecha + "\" ) para RFC de Persona Moral";
            isRfcPm = false;
        }

        char [] cHomoclave = homoclave.trim().toCharArray();
        for ( int i=0; i<cHomoclave.length; i++ ) {
            if ( ! esAlfaNumerico ( cHomoclave[i] ) ) {
                msg_error += "Formato de Homoclave Inválido (\"" + homoclave + "\") para RFC de Persona Moral ";
                isRfcPm = false;
                break;
            }
        }

        if ( ! isRfcPm ) {
            throw new ValidationException ( msg_error, null, null);
        }
        return isRfcPm;
    }



    public static boolean isRfcPFHomoclave ( String rfc) 
    throws ValidationException{
        boolean isRfcPm = true;
        if (rfc == null || rfc.trim().length() == 0) return isRfcPm;
        String msg_error = "";		
        rfc = rfc.trim();
        if (rfc.length() <= 10 ) {
            msg_error = "Formato de Tamaño Inválido para RFC (AAAAyyMMddXXX)";
            //msg_error += "Formato de RFC Inv�lido (\"" + rfc + "\") para RFC de Persona F�sica.";
            throw new ValidationException ( msg_error, null, null);
        }
        String inicNombre = rfc.substring ( 0, 4 );
        String fecha      = rfc.substring ( 4, 10 );
        String homoclave  = rfc.substring ( 10 );

        char [] cRfc = inicNombre.toCharArray();
        for ( int i=0; i<cRfc.length; i++ ) {
            if ( ! esAlfabetico ( cRfc[i] ) ) {
                msg_error += "Formato de Iniciales Inválido (\"" + inicNombre + "\") para RFC de Persona Física ";
                isRfcPm = false;
                break;
            }
        }

        if  ( ! isFecha ( fecha, "yyMMdd" ) ) {
            msg_error = "Formato de Fecha Inválido ( \"" + fecha + "\" ) para RFC de Persona Física.";
            isRfcPm = false;
        } 

        char [] cHomoclave = homoclave.trim().toCharArray();
        for ( int i=0; i<cHomoclave.length; i++ ) {
            if ( ! esAlfaNumerico ( cHomoclave[i] ) ) {
                msg_error += "Formato de Homoclave Inválido (\"" + homoclave + "\") para RFC de Persona Física.";
                isRfcPm = false;
                break;
            }
        }

        if ( ! isRfcPm ) {
            throw new ValidationException ( msg_error, null, null );
        }
        return isRfcPm;
    }

    /***************************************************************************
    Funci�n que obtiene una fecha tipo java.util.Date a partir de un string fecha
    ****************************************************************************/
    public static boolean isFecha ( String fecha, String formatoFecha ) {
        SimpleDateFormat date = new SimpleDateFormat ( formatoFecha ); 
        try { 
            date.setLenient(false);
            date.parse(fecha); 
                 if (!isDigito(fecha)){
            return false;  
            }
        } catch ( ParseException e ) { 
            return false;
        }
        return true; 
    }
    
    /**
     * Funcion que verifica que un caracter sea un numero
     **/
    public static boolean isDigito ( String digito ) { 
        char [] cDigito = digito.toCharArray();
        boolean bandera = true;
        for ( int i=0; i<cDigito.length; i++ ) {
            if ( ! esDigito ( cDigito [i] ) ) {
                bandera = false;
                break;
            }
        }
        return bandera;
    }

    /**
     * Funcion que verifica que un caracter sea un numero
     **/
    public static boolean esDigito ( char car ) { 
         String numeros = "0123456789";
         return ( numeros.indexOf ( car ) >= 0 );
    } 


    /**
     * Funcion que verifica que un caracter sea un alfabetico
     **/
    public static boolean isAlfabetico ( String cadena ) { 
        char [] cCadena = cadena.toCharArray();
        boolean bandera = true;
        for ( int i=0; i<cCadena.length; i++ ) {
            if ( ! esAlfabetico ( cCadena [i] ) ) {
                bandera = false;
                break;
            }
        }
        return bandera;
    }

    /**
     * Funcion que verifica que un caracter sea una letra minuscula
     **/
    public static boolean esAlfabetico ( char car ) {
         String alfabeto = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
         return ( alfabeto.indexOf ( car ) >= 0 );
    }
	

    /**
     * Funcion que verifica que un caracter sea un alfabetico
     **/
    public static boolean isAlfaNumerico ( String cadena ) { 
        char [] cCadena = cadena.toCharArray();
        boolean bandera = true;
        for ( int i=0; i<cCadena.length; i++ ) {
            if ( ! esAlfaNumerico ( cCadena [i] ) ) {
                bandera = false;
                break;
            }
        }
        return bandera;
    }

    /**
     * Funcion que verifica que un caracter sea una letra mayuscula
     **/
    public static boolean esAlfaNumerico ( char car ) {
         String alfaNumerico ="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
         return ( alfaNumerico.indexOf ( car ) >= 0 );
    } 


    /**
     * Funcion que verifica que un caracter sea un alfabetico
     **/
    public static boolean isAlfabeticoEspecial ( String cadena ) { 
        char [] cCadena = cadena.toCharArray();
        boolean bandera = true;
        for ( int i=0; i<cCadena.length; i++ ) {
            if ( ! esAlfabeticoEspecial( cCadena [i] ) ) {
                bandera = false;
                break;
            }
        }
        return bandera;
    }

    /**
     * Funcion que verifica que un caracter sea una letra mayuscula
     **/
    public static boolean esAlfabeticoEspecial ( char car ) {
         String alfaNumericoEspecial = "abcdefghijklmnñopqrstuvwxyzáéíóúüABCDEFGHIJKLMNÑOPQRSTUVWXYZÁÉÍÓÚÜ";
         return ( alfaNumericoEspecial.indexOf ( car ) >= 0 );
    } 
}
