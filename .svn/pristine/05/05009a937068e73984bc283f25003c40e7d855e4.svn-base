package com.sicap.clientes.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Hashtable;

public class FormatUtil {

    private static org.apache.log4j.Logger myLogger = org.apache.log4j.Logger.getLogger(FormatUtil.class);

    public static String completaCadena(String valor, char caracter, int longitud, String posicion) {

        if (valor != null) {
            if (valor.length() >= longitud) {
                return valor;
            } else {
                for (int i = valor.length(); i < longitud; i++) {
                    if (posicion.equalsIgnoreCase("L")) {
                        valor = Character.toString(caracter) + valor;
                    } else {
                        valor = valor + Character.toString(caracter);
                    }
                }
                return valor;
            }
        } else {
            char[] secuencia = new char[longitud];
            Arrays.fill(secuencia, caracter);
            return new String(secuencia);
        }

    }

    public static String completaCadena(String valor, char caracter, int longitud) {

        if (valor != null) {
            if (valor.length() >= longitud) {
                return valor;
            } else {
                for (int i = valor.length(); i < longitud; i++) {
                    valor = Character.toString(caracter) + valor;
                }
                return valor;
            }
        } else {
            char[] secuencia = new char[longitud];
            Arrays.fill(secuencia, caracter);
            return new String(secuencia);
        }

    }

    public static String eliminaCaracteresInvalidos(String cadena) {
        if (cadena != null && !cadena.trim().equals("")) {
            cadena = cadena.toUpperCase();
            cadena = cadena.replace('Ñ', 'N');
            cadena = cadena.replace('Á', 'A');
            cadena = cadena.replace('É', 'E');
            cadena = cadena.replace('Í', 'I');
            cadena = cadena.replace('Ó', 'O');
            cadena = cadena.replace('Ú', 'U');
            cadena = cadena.replace('Ü', 'U');
            cadena = cadena.replace("/", "");
            cadena = cadena.replace(".", "");
            cadena = cadena.replace("(", "");
            cadena = cadena.replace(")", "");
            cadena = cadena.replace("-", " ");
            cadena = cadena.replace("_", " ");
        }
        return cadena;
    }

    public static String convierteFecha(String fecha, int tipoOPer) {
        //Si tipoOper = 0 devuelve la fecha con el mes numérico
        //Si tipoOper = 1 devuelve la fecha con el mes en letra sin abreviación
        String resp = "";
        fecha = fecha.replace(' ', '/');
        fecha = fecha.replace('-', '/');
        fecha = fecha.replace('.', '/');

        int index = fecha.indexOf("/");
        String mes = fecha.substring(index + 1, index + 4);
        Hashtable<String, String> numeroMes = new Hashtable<String, String>();
        Hashtable<String, String> meses = new Hashtable<String, String>();
        Hashtable<String, String> numeroLetra = new Hashtable<String, String>();

        numeroMes.put("ENE", "01");
        numeroMes.put("FEB", "02");
        numeroMes.put("MAR", "03");
        numeroMes.put("ABR", "04");
        numeroMes.put("MAY", "05");
        numeroMes.put("JUN", "06");
        numeroMes.put("JUL", "07");
        numeroMes.put("AGO", "08");
        numeroMes.put("SEP", "09");
        numeroMes.put("OCT", "10");
        numeroMes.put("NOV", "11");
        numeroMes.put("DIC", "12");

        meses.put("ENE", "ENERO");
        meses.put("FEB", "FEBRERO");
        meses.put("MAR", "MARZO");
        meses.put("ABR", "ABRIL");
        meses.put("MAY", "MAYO");
        meses.put("JUN", "JUNIO");
        meses.put("JUL", "JULIO");
        meses.put("AGO", "AGOSTO");
        meses.put("SEP", "SEPTIEMBRE");
        meses.put("OCT", "OCTUBRE");
        meses.put("NOV", "NOVIEMBRE");
        meses.put("DIC", "DICIEMBRE");

        numeroLetra.put("01", "ENERO");
        numeroLetra.put("02", "FEBRERO");
        numeroLetra.put("03", "MARZO");
        numeroLetra.put("04", "ABRIL");
        numeroLetra.put("05", "MAYO");
        numeroLetra.put("06", "JUNIO");
        numeroLetra.put("07", "JULIO");
        numeroLetra.put("08", "AGOSTO");
        numeroLetra.put("09", "SEPTIEMBRE");
        numeroLetra.put("10", "OCTUBRE");
        numeroLetra.put("11", "NOVIEMBRE");
        numeroLetra.put("12", "DICIEMBRE");

        mes = mes.toUpperCase();
        if (tipoOPer == 0) {
            resp = fecha.substring(0, index + 1) + (String) numeroMes.get(mes);
        } else {
            resp = fecha.substring(0, index + 1) + (String) meses.get(mes);
        }

        String año = fecha.substring(fecha.lastIndexOf("/") + 1);

        if (año.length() == 2) {
            resp += "/20" + año;
        } else {
            resp += fecha.substring(fecha.lastIndexOf("/"));
        }

        return resp;

    }

    public static String deleteChar(String cadena, char caracter) {
        StringBuffer buf = new StringBuffer(cadena);
        int flag = cadena.indexOf(caracter);
        while (flag != -1) {
            cadena = buf.deleteCharAt(flag).toString();
            flag = cadena.indexOf(caracter);
        }
        return cadena;
    }

    public static String deleteChars(String cadena, String caracteres) {
        //Elimina los caracteres de una cadena de entrada de una serie de caracteres 
        for (int i = 0; i <= caracteres.length() - 1; i++) {
            String x = caracteres.substring(i, i + 1);
            int index = 0;
            while (index != -1) {
                index = cadena.indexOf(x);
                if (index == -1) {
                    break;
                }
                StringBuffer buf = new StringBuffer(cadena);
                cadena = buf.deleteCharAt(index).toString();
            }
        }

        return cadena;
    }

    public static final double roundDouble(double d, int places) {
        return Math.round(d * Math.pow(10, (double) places)) / Math.pow(10,
                (double) places);
    }

    public static String formatDoble(double value) {
        String text = new DecimalFormat(ClientesConstants.FORMATO_MONTOCDC).format(value);
        return text;
    }

    public static String formatDobleMiles(double value) {
        String text = new DecimalFormat(ClientesConstants.FORMATO_MONTO_MILES).format(value);
        return text;
    }

    public static String formatIntegerMiles(double value) {
        String text = new DecimalFormat(ClientesConstants.FORMATO_MONTO_MILES_INT).format(value);
        return text;
    }

    public static String formatDoble(String format, double value) {
        String text = new DecimalFormat(format).format(value);
        return text;
    }

    public static double redondeaMoneda(double monto) {

        //myLogger.debug("Monto" + monto);
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
        simbolos.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat(ClientesConstants.FORMATO_MONTO, simbolos);

        //myLogger.debug("ClientesConstants.FORMATO_MONTO" + ClientesConstants.FORMATO_MONTO);
        String formateado = df.format(monto);
        //myLogger.debug("formateado" + formateado);
        return Double.parseDouble(formateado);

    }

    public static double formatSaldo(double saldo) {
        if (saldo < 0) {
            return 0;
        }
        return saldo;
    }

    /**
     * Redondea el número double a la cantidad de dígitos decimales
     * especificados
     *
     * @param number número a redondear
     * @param decimals dígitos decimales
     * @return el número redondeado tantos decimales como se especificaron
     */
    public static final double roundDecimal(double number, int decimals) {
        BigDecimal bd = new BigDecimal(number);
        bd = bd.setScale(decimals, 0);
        return bd.doubleValue();
    }

    public static String formateaPorcentaje(double field) {

        NumberFormat myFormatter = NumberFormat.getPercentInstance();
        String output = "";
        myFormatter.setMaximumFractionDigits(2);
        output = myFormatter.format(field);
        return output;

    }
    
    public static String formateaMoneda(double field) {

        NumberFormat myFormatter = NumberFormat.getCurrencyInstance();
        String output = "";
        myFormatter.setMaximumFractionDigits(2);
        output = myFormatter.format(field);
        return output;

    }
}
