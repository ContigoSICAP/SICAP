package com.sicap.clientes.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Hashtable;

import com.sicap.clientes.vo.SolicitudVO;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class FechasUtil {

    public static int inBetweenDays(Date fechaIni, Date fechaFin) {
        //Calcula el n�mero de d�as entre dos fechas

        GregorianCalendar cal1 = new GregorianCalendar();
        GregorianCalendar cal2 = new GregorianCalendar();
        try {
            cal1.setTime(fechaIni);
            cal1.set(Calendar.HOUR_OF_DAY, 0);
            cal1.set(Calendar.MINUTE, 0);
            cal1.set(Calendar.SECOND, 0);
            cal1.set(Calendar.MILLISECOND, 0);
            cal2.setTime(fechaFin);
            cal2.set(Calendar.HOUR_OF_DAY, 0);
            cal2.set(Calendar.MINUTE, 0);
            cal2.set(Calendar.SECOND, 0);
            cal2.set(Calendar.MILLISECOND, 0);
        } catch (Exception e) {
            System.err.println("inBetweenDays Exception: " + e);
        }
        int numDays = 0;
        while (cal1.getTime().before(cal2.getTime())) {
            cal1.add(Calendar.DAY_OF_YEAR, 1);
            numDays++;
        }
        return numDays;
    }

    public static Date getDate(Date fechaIni, int numero, int mesesDias) {
        //Calcula la fecha final de un periodo apartir de la fecha inicial y un n�mero de meses o d�as
        //0 = meses
        //1 = dias

        GregorianCalendar fechaFin = new GregorianCalendar();
        fechaFin.setTime(fechaIni);
        if (mesesDias == 0) {
            fechaFin.add(Calendar.MONTH, numero);
        }
        if (mesesDias == 1) {
            fechaFin.add(Calendar.DAY_OF_YEAR, numero);
        }

        return fechaFin.getTime();
    }

    public static String obtenParteFecha(Date fecha, int parte) {
        String resultado = "";
        Calendar cal = Calendar.getInstance();

        if (fecha != null) {
            cal.setTime(fecha);
            switch (parte) {
                //Dia del mes
                case 1:
                    resultado = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
                    if (resultado.length() == 1) {
                        resultado = "0" + resultado;
                    }
                    break;
                //Mes
                case 2:
                    resultado = String.valueOf(cal.get(Calendar.MONTH) + 1);
                    if (resultado.length() == 1) {
                        resultado = "0" + resultado;
                    }
                    break;
                //A�o
                case 3:
                    resultado = String.valueOf(cal.get(Calendar.YEAR));
                    break;
            }
        }

        return resultado;
    }

    public static String obtenNombreMes() {
        String nombreMes = null;
        return nombreMes;
    }

    public static String obtenNombreMes(String fecha) {

        String resp = "";
        fecha = fecha.replace(' ', '/');
        fecha = fecha.replace('-', '/');
        fecha = fecha.replace('.', '/');

        int index = fecha.indexOf("/");
        String mes = fecha.substring(index + 1, index + 3);

        Hashtable<String, String> numeroLetra = new Hashtable<String, String>();

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

        mes = fecha.substring(index + 1, index + 3);
        resp = (String) numeroLetra.get(mes);

        return resp;

    }

    public static Date getBusinessDate(Date dateIn) {
        //Obtiene la fecha del primer d�a laborable anterior de la semana en caso de que la fecha
        //de entrada sea s�bado o domingo
        GregorianCalendar dateOut = new GregorianCalendar();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateIn);
        dateOut.setTime(dateIn);

        if (cal.get(Calendar.DAY_OF_WEEK) == 7) //			  dateOut.roll(Calendar.DAY_OF_YEAR, -1);
        {
            dateOut.add(Calendar.DAY_OF_YEAR, -1);
        }
        if (cal.get(Calendar.DAY_OF_WEEK) == 1) //    		dateOut.roll(Calendar.DAY_OF_YEAR, -2);
        {
            dateOut.add(Calendar.DAY_OF_YEAR, -2);
        }

        return dateOut.getTime();
    }

    public static Date getNextWeekDate(Date dateIn, int dayWeek) {
        //Obtiene la fecha del primer dia de la siguiente semana correspondienta a un dia de la semana fijo
        GregorianCalendar dateOut = new GregorianCalendar();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateIn);
        dateOut.setTime(dateIn);

        while (dateOut.get(Calendar.DAY_OF_WEEK) != dayWeek) {
            dateOut.add(Calendar.DAY_OF_YEAR, 1);
        }
        return dateOut.getTime();
    }

    public static Date getDateCierreMes(Date dateIn) {
        //Obtiene la fecha del primer pago y la establece al d�a 25 del mes para facilitar cierre de mes en operaciones
        GregorianCalendar dateOut = new GregorianCalendar();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateIn);
        dateOut.setTime(dateIn);

        if (cal.get(Calendar.DAY_OF_MONTH) > 25) {
            dateOut.set(Calendar.DAY_OF_MONTH, 25);
        }

        return dateOut.getTime();
    }


    /*
     * Calcula la edad en base a la fecha de nacimiento y la fecha del sistema
     */
    public static int calculaEdad(Date fechaNacimiento) {
        int anios = 0;

        if (fechaNacimiento != null) {
            anios = obtenAniosDiferencia(fechaNacimiento, new Date());
        }
        return anios;
    }


    /*
     * Calcula la diferencia de a�os entre dos fechas.
     */
    public static int obtenAniosDiferencia(Date fechaIni, Date fechaFin) {
        int diferencia = 0;
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        if (fechaIni != null && fechaFin != null) {
            cal1.setTime(fechaIni);
            cal2.setTime(fechaFin);
            while (cal1.before(cal2)) {
                cal1.add(Calendar.YEAR, 1);
                diferencia++;
            }
        }
        return diferencia - 1;
    }

    /*
     * Verifica si la fecha de entrada es d�a inh�bil
     */
    public static boolean esDiaInhabil(Date fecha, Date[] fechas) throws Exception {

        boolean result = false;
        Date temporal = null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);

        if (cal.get(Calendar.DAY_OF_WEEK) == 7 || cal.get(Calendar.DAY_OF_WEEK) == 1) {
            return true;
        } else {
            for (int i = 0; i < fechas.length; i++) {
                temporal = new Date();
                temporal = fechas[i];
                if (Convertidor.dateToString(temporal).equals(Convertidor.dateToString(fecha))) {
                    return true;
                }
            }
        }

        return result;
    }

    public static void calculaFechaReestructura(SolicitudVO solicitud, Calendar calendario, Date[] fechas) throws Exception {
        int solicitudReestructura = solicitud.solicitudReestructura;
        //SE TRATA DE UNA REESTRUCTURA
        if (solicitudReestructura != 0) {
            if (solicitud.decisionComite.fechaValor != null) {
                calendario.setTime(solicitud.decisionComite.fechaValor);
            } else {
                //SE ADELANTA DOS DIAS EL CALCULO DE SU TABLA DE AMORTIZACION
                calendario.add(Calendar.DAY_OF_YEAR, 5);
                //VALIDA QUE SE TRATE DE UN DIA HABIL
                if (esDiaInhabil(calendario.getTime(), fechas)) {
                    for (int i = 0; esDiaInhabil(calendario.getTime(), fechas) && i < 10; i++) {
                        calendario.add(Calendar.DAY_OF_YEAR, 1);
                    }
                }
            }
        }
    }

    public static String formatDateSlashes(String fechaIn, int formatoEntrada) {
        //formato entrada = 1 (DDMMAAAA)
        //formato entrada = 2 (AAAAMMDD)
        String fechaOut = "&nbsp;";

        if (fechaIn != null && !fechaIn.equals("0")) {
            if (formatoEntrada == 1) {
                fechaOut = fechaIn.substring(0, 2) + "/" + fechaIn.substring(2, 4) + "/" + fechaIn.substring(4, 8);
            } else if (formatoEntrada == 2) {
                fechaOut = fechaIn.substring(6, 8) + "/" + fechaIn.substring(4, 6) + "/" + fechaIn.substring(0, 4);
            }
        }

        return fechaOut;
    }

    public static Date getFirstLastDay(Date dateIn, int option) {
        //Obtiene la fecha del primer d�a del mes o el �ltimo d�a del mes de la fecha de entrada
        //0 = Primer d�a del mes
        //1 = �ltimo d�a del mes
        //2 = Primer d�a de la semana
        //3 = �ltimo d�a de la semana

        GregorianCalendar dateOut = new GregorianCalendar();
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateIn);
        dateOut.setTime(dateIn);

        switch (option) {
            case 0:
                dateOut.set(Calendar.DAY_OF_MONTH, 1);
                break;
            case 1:
                dateOut.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DATE));
                break;
            case 2:
                dateOut.set(Calendar.DAY_OF_WEEK, cal.getActualMinimum(Calendar.DAY_OF_WEEK));
                dateOut.roll(Calendar.DAY_OF_YEAR, -7);
                break;
            case 3:
                dateOut.set(Calendar.DAY_OF_WEEK, cal.getActualMaximum(Calendar.DAY_OF_WEEK));
                break;
        }
        return dateOut.getTime();
    }
    
    public static Date truncDate(Date fecha){

        Calendar calTrunc = Calendar.getInstance();
        calTrunc.setTime(fecha);
        calTrunc.set(Calendar.HOUR_OF_DAY, 0);
        calTrunc.set(Calendar.MINUTE, 0);
        calTrunc.set(Calendar.SECOND, 0);
        calTrunc.set(Calendar.MILLISECOND, 0);

        System.out.println("Date Trunc: "+calTrunc.getTime());
        
        return calTrunc.getTime();
    }
    
    public static Date getRestarDias(Date fechaActual, int dias) {

        System.out.println("Restar dias entra: "+fechaActual);
        System.out.println("Dias: "+dias);       
        
        Calendar calActual = Calendar.getInstance();
        calActual.setTime(fechaActual);
        calActual.set(Calendar.HOUR_OF_DAY, 0);
        calActual.set(Calendar.MINUTE, 0);
        calActual.set(Calendar.SECOND, 0);
        calActual.set(Calendar.MILLISECOND, 0);
        
        System.out.println("Tiempo actual long: "+calActual.getTime().getTime());
        
        calActual.add(Calendar.DAY_OF_MONTH, (-1)*(dias));
        
        System.out.println("Restar dias Sale: "+calActual.getTime());
        return calActual.getTime();
    }

//    public static Date getRestarDias(Date fechaActual, int dias) {
//
//        System.out.println("Restar dias entra: "+fechaActual);
//        System.out.println("Dias: "+dias);
//        long tiempoActual = fechaActual.getTime();
//        long numDias = dias * 24 * 60 * 60 * 1000;
//        System.out.println("Tiempo actual long: "+tiempoActual);
//        System.out.println("Num dias Long: "+numDias);
//        System.out.println("Diferencia: "+(tiempoActual-numDias));
//        Date fechaAyer = new Date(tiempoActual - numDias);
//        System.out.println("Restar dias Sale: "+fechaAyer);
//        return fechaAyer;
//    }
    
    public static String getNumeroMes(String mes){
        
        String numMes = "";
        if(mes.equals("ENE") || mes.equals("Ene") || mes.equals("ene"))
            numMes = "01";
        else if(mes.equals("FEB") || mes.equals("Feb") || mes.equals("feb"))
            numMes = "02";
        else if(mes.equals("MAR") || mes.equals("Mar") || mes.equals("mar"))
            numMes = "03";
        else if(mes.equals("ABR") || mes.equals("Abr") || mes.equals("abr"))
            numMes = "04";
        else if(mes.equals("MAY") || mes.equals("May") || mes.equals("may"))
            numMes = "05";
        else if(mes.equals("JUN") || mes.equals("Jun") || mes.equals("jun"))
            numMes = "06";
        else if(mes.equals("JUL") || mes.equals("Jul") || mes.equals("jul"))
            numMes = "07";
        else if(mes.equals("AGO") || mes.equals("Ago") || mes.equals("ago"))
            numMes = "08";
        else if(mes.equals("SEP") || mes.equals("Sep") || mes.equals("sep"))
            numMes = "09";
        else if(mes.equals("OCT") || mes.equals("Oct") || mes.equals("oct"))
            numMes = "10";
        else if(mes.equals("NOV") || mes.equals("Nov") || mes.equals("nov"))
            numMes = "11";
        else if(mes.equals("DIC") || mes.equals("Dic") || mes.equals("dic"))
            numMes = "12";
        return numMes;
    }    
     
    // Se utiliza para restar horas JBL NOV-13
    public static java.sql.Timestamp subtractHours(java.sql.Timestamp stamp, int hours) {
            long current = stamp.getTime();
            long substracted = current - (60 * 60 * 1000) * hours;
        return new java.sql.Timestamp(substracted);
    }
    
   
    
    //obtener numero de dias entre 2 fechas dadas
    public static int getNumDiasFechas(Date date1, Date date2) 
    {

        if (date1 == null || date2 == null) 
        {
            return 0;
        }
        date1 = setMidnight(date1); 
        date2 = setMidnight(date2);

        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();

        if (date2.compareTo(date1) > 0) 
        {
            calendar1.setTime(date1);
            calendar2.setTime(date2);
        } 
        else {
            calendar1.setTime(date2);
            calendar2.setTime(date1);
        }
        int days = 0;
       

        while (calendar1.compareTo(calendar2) < 0) 
        {
            calendar1.add(Calendar.DAY_OF_MONTH, 1); 
            days++;
        }
        return days;
    }

    public static Date setMidnight(Date date) 
    {

        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);   
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }
    
    public static boolean fechaDentroDelRango(Calendar fechaAComparar, int dias) 
     {
        fechaAComparar.add(Calendar.DAY_OF_MONTH, dias);
        Calendar calendario1 = Calendar.getInstance();
        if(calendario1.before(fechaAComparar))
        {
            return true;
        }
       return false;
    }
}
