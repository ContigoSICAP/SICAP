/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.util;

import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.cartera.TablaAmortDAO;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.util.FechasUtil;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.OrdenDePagoVO;
import com.sicap.clientes.vo.cartera.TablaAmortVO;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author LDAVILA
 */
public class AdicionalUtil {

    private static org.apache.log4j.Logger myLogger = org.apache.log4j.Logger.getLogger(AdicionalUtil.class);

    /**
     * JECB 01/10/2017
     * Método que verifica la semana adicional
     * @param grupo bean con informacion del grupo
     * @param saldoVO bean con informacion del saldo
     * @return entero con el valor correspondiente a la semana adicional
     * @throws ClientesException
     * @throws ParseException 
     */
    public static int verificaSemanaAdicional(CicloGrupalVO ciclo, SaldoIBSVO saldoVO)throws ClientesException, ParseException{
        int semanaAdicional = 0;
        myLogger.debug("AdicionalUtil.verificaSemanaAdicional() semanas transcurridas:" + saldoVO.getNumeroCuotasTranscurridas());
        myLogger.debug("AdicionalUtil.verificaSemanaAdicional() semanas dispersion adicional:" + ciclo.getAceptaAdicional());
        semanaAdicional = AdicionalUtil.verificaSemanaAdicional(saldoVO, ciclo.getAceptaAdicional());
        myLogger.debug("AdicionalUtil.verificaSemanaAdicional() valor semana adicional:" + semanaAdicional);
        if(semanaAdicional != 0){
            if(saldoVO.getNumeroCuotasTranscurridas() > 9 || semanaAdicional <= saldoVO.getNumeroCuotasTranscurridas() ){
                semanaAdicional = 0;
            }
        }
        myLogger.debug("AdicionalUtil.verificaSemanaAdicional() valor semana adicional:" + semanaAdicional);
        return semanaAdicional;
    }
    
    //JECB 01/10/2017
    //Se modifica método para que maneje las nuevos plazos de semanas adicionales 
    //establecidas
    /**
     * Método que determina la semana a dispersar adicional
     * @param saldoVO bean de saldo para verificar semanas transcurridas
     * @param semDisp semanas dispersion
     * @return semana de adicioanl
     * @throws ClientesException
     * @throws ParseException 
     */
    public static int verificaSemanaAdicional(SaldoIBSVO saldoVO, int semDisp) throws ClientesException, ParseException {
        
        int resultado = 0;
        Date fechaHoy = new Date();
        //SE DEBE DE INCORPORAR LAS FECHAS INHABILES
        try {
            //compara con semana 4
            if (saldoVO.getNumeroCuotasTranscurridas() == 3 ) {
                resultado = 4;
            }//compara con semana 5
            else if (saldoVO.getNumeroCuotasTranscurridas() == 4 ) {
                resultado = 5;
            }//compara con semana 6
            else if (saldoVO.getNumeroCuotasTranscurridas() == 5 ) {
                resultado = 6;
            } //compara con semana 7
            else if (saldoVO.getNumeroCuotasTranscurridas() == 6 ) {
                resultado = 7;
            }//compara con semana 8
            else if (saldoVO.getNumeroCuotasTranscurridas() == 7 ) {
                resultado = 8;
            }//compara con semana 9
            else if (saldoVO.getNumeroCuotasTranscurridas() == 8 ) {
                resultado = 9;
            }//compara con semana 10
            else if (saldoVO.getNumeroCuotasTranscurridas() == 9 ) {
                resultado = 10;
            } else {
                resultado = 0;
            }
            if(resultado == semDisp)
                resultado = 0;
        } catch (Exception e) {
            myLogger.error("getCiclos", e);
            throw new ClientesException(e.getMessage());

        }

        return resultado;
    }
    
    public static double calculaCapitalAdicional(IntegranteCicloVO integranteVO){
        
        double montoCapital = 0;     
        myLogger.debug("calcula Adicional");
        double montoOriginal = integranteVO.monto - integranteVO.montoAdicional;
        myLogger.debug("montoOriginal "+montoOriginal);
        double capitalSemanal = montoOriginal/ 16;
        myLogger.debug("capitalSemanal "+capitalSemanal);
        double capitalPagado = capitalSemanal * (plazoAdicional(integranteVO.tipo_adicional));
        myLogger.debug("capitalPagado "+capitalPagado);
        montoCapital = montoOriginal - capitalPagado + integranteVO.montoAdicional;       
        myLogger.debug("montoCapital "+montoCapital);
        
        return montoCapital;
    }
    
    //JECB 01/10/2017
    //Se modifica método para que considere las  nuevas semanas
    //de adicional
    /**
     * Método que da el plazo de un adicional de acuerdo a su tipo
     * @param tipoAdicional clave del tipo de adicional
     * @return plazo correspondiente del adicional
     */
    public static int plazoAdicional(int tipoAdicional){
        
        int plazo = 0;
        if(tipoAdicional == ClientesConstants.TIPO_CLIENTE_ADICONAL_4)
            plazo = 4;
        else if(tipoAdicional == ClientesConstants.TIPO_CLIENTE_ADICONAL_5)
            plazo = 5;
        else if(tipoAdicional == ClientesConstants.TIPO_CLIENTE_ADICONAL_6)
            plazo = 6;
        else if(tipoAdicional == ClientesConstants.TIPO_CLIENTE_ADICONAL_7)
            plazo = 7;
        else if(tipoAdicional == ClientesConstants.TIPO_CLIENTE_ADICONAL_8)
            plazo = 8;
        else if(tipoAdicional == ClientesConstants.TIPO_CLIENTE_ADICONAL_9)
            plazo = 9;
        else if(tipoAdicional == ClientesConstants.TIPO_CLIENTE_ADICONAL_10)
            plazo = 10;
        
        return plazo;
    }
    
    public static double calculaAdicionalSemanal(double capital, double tasa, int plazo){
        double pagoIndividual = 0;
        double interes = ((((tasa * (1 + ClientesConstants.TASA_IVA_NO_FRONTERIZO))) / 100) / 360) * (plazo * 7);
        interes = (capital ) * interes;
        interes = FormatUtil.redondeaMoneda(interes);
        pagoIndividual = (capital  + interes) / (plazo);
        return pagoIndividual;
    }
    public static double calculaInteresAdicional(double tasa, int plazo){
        double interes = 0;
        interes = ((((tasa * (1 + ClientesConstants.TASA_IVA_NO_FRONTERIZO))) / 100) / 360) * (plazo * 7);
        return interes;
    }

    /**
     * JECB 01/10/2017
     * Método que valida si la fecha a evaluar se encuentra exactamente a un dia 
     * de la fecha de pago registrada en la tabla de amortizacion
     * @param fecha fecha a evaluar
     * @param fechaPago fecha de pago registrada en la tabla de amortizacion
     * @param fechasInhabiles listado de fechas inhabiles
     * @return true en caso de que la fecha actual este justamente 1 dia antes de 
     * la fecha de pago
     */
    public static boolean fechaAntesDeFechaPago(Date fecha, Date fechaPago, Date[] fechasInhabiles){
        Calendar cFechaPago = Calendar.getInstance();
        Calendar cFecha = Calendar.getInstance();
        cFechaPago.setTime(fechaPago);
        cFecha.setTime(fecha);
                
        cFechaPago.set(Calendar.HOUR_OF_DAY, 0);
        cFechaPago.set(Calendar.MINUTE, 0);
        cFechaPago.set(Calendar.SECOND, 0);
        cFechaPago.set(Calendar.MILLISECOND, 0);
        
        cFecha.set(Calendar.HOUR_OF_DAY, 0);
        cFecha.set(Calendar.MINUTE, 0);
        cFecha.set(Calendar.SECOND, 0);
        cFecha.set(Calendar.MILLISECOND, 0);
        
        if(cFecha.after(cFechaPago)){
            myLogger.debug("fecha actual posterior a la fecha de pago");
            return false;
        }
        if(cFecha.compareTo(cFechaPago) == 0){
            myLogger.debug("fecha actual igual a la fecha de pago");
            return true;
        }
        
        try {
            if(FechasUtil.esDiaInhabil(cFecha.getTime(), fechasInhabiles)){
                myLogger.debug("fecha actual es un dia inhabil ");
                return false;
            }
        } catch (Exception ex) {
            myLogger.debug("Error funcion dia inhabil");
            return false;
        }
        
        
        long diffTime = cFechaPago.getTimeInMillis() - cFecha.getTimeInMillis();
        long diffDays = diffTime / (1000 * 60 * 60 * 24);
        
        myLogger.debug("diferencias en dias:" + diffDays);
        
        if(new Long(diffDays).intValue() == 1){
            myLogger.debug("fecha actual con 1 dia de anticipacion de fecha de pago");
            return true;
        }else{
            myLogger.debug("fecha actual > 1 dia de anticipacion de fecha de pago");
            boolean todosDiasInhabiles = true;
            cFecha.add(Calendar.DATE, 1);
            do{
                try {
                    
                    myLogger.debug("Fecha Modificada:"+new SimpleDateFormat("yyyy-MM-dd").format(cFecha.getTime()));
                    boolean isDiaInhabil = FechasUtil.esDiaInhabil(cFecha.getTime(), fechasInhabiles);
                    myLogger.debug("isDiaInhabil:"+isDiaInhabil);
                    if(!isDiaInhabil){
                        todosDiasInhabiles = false;
                    }
                } catch (Exception ex) {
                    todosDiasInhabiles = false;
                    myLogger.debug("Error funcion dia inhabil");
                }
                cFecha.add(Calendar.DATE, 1);
            }while(cFecha.compareTo(cFechaPago)<0 && todosDiasInhabiles);
            
            if(todosDiasInhabiles){
                myLogger.debug("Todos los dias intermedios inhabiles");
                return true;
            }else{
                myLogger.debug("Algun dia no es inhabil");
                return false;
            }
        }
    }
    
    /**
     * JECB 01/10/2017
     * Método que valida si una orden de pago se trata de una orden de crédito adicioanl
     * @param ordenDePago bean que tiene referencia la orden de pago
     * @return true en caso de que la orden de pago se trate de una orden de pago adicional
     * en cualquier otro caso false
     */
    public static boolean isOrdenPagoAdicional(OrdenDePagoVO ordenDePago){
        boolean result = false;
        
        if(null != ordenDePago && null != ordenDePago.getReferencia() && !ordenDePago.getReferencia().isEmpty()){
            if(ordenDePago.getReferencia().substring(ordenDePago.getReferencia().length() -1).equals("8")){
                result = true;
            }
        }
        return result; 
    }
    
    /**
     * JECB 01/10/2017
     * Método que obtiene los plazos de adicional de los integrantes ciclo que tienen un adicional
     * @param icArray Arreglo de los integrantes de ciclo
     * @return Semanas en las que se ha desembolsado un adicional
     */
    public static Set<Integer> obtienePlazosAdicionalDeIntegrantesCiclo(IntegranteCicloVO[] icArray){
        myLogger.debug("com.sicap.clientes.util.AdicionalUtil.obtienePlazosAdicionalDeIntegrantesCiclo()");
                
        Set<Integer> setPlazosAdicionales = new TreeSet<Integer>();
        if(icArray != null && icArray.length > 0){
            for(IntegranteCicloVO ic : icArray){
                if(ic.tipo_adicional > 0 && ic.montoAdicional > 0){
                    myLogger.debug("integrante ciclo:" + ic.idCliente + " tipoAdicional:" + ic.tipo_adicional);
                       setPlazosAdicionales.add(AdicionalUtil.plazoAdicional(ic.tipo_adicional));
                }
            }
            myLogger.debug("Lista de plazos de adicionales:"+ Arrays.toString(setPlazosAdicionales.toArray()));
        }
        
        return setPlazosAdicionales;
    }
    
    /**
     * JECB 01/10/2017
     * Método que obtiene el tipo de adicional apartir de la semana de adicional
     * @param semanaAdicional semana de adicional
     * @return valor de tipo de adicional
     */
    public static int obtieneTipoAdicional(int semanaAdicional){
        
        int tipoAdicional = 0;
        if(semanaAdicional == 4 )
            tipoAdicional = ClientesConstants.TIPO_CLIENTE_ADICONAL_4;
        else if(semanaAdicional == 5)
            tipoAdicional = ClientesConstants.TIPO_CLIENTE_ADICONAL_5;
        else if(semanaAdicional == 6)
            tipoAdicional = ClientesConstants.TIPO_CLIENTE_ADICONAL_6;
        else if(semanaAdicional == 7)
            tipoAdicional = ClientesConstants.TIPO_CLIENTE_ADICONAL_7;
        else if(semanaAdicional == 8)
            tipoAdicional = ClientesConstants.TIPO_CLIENTE_ADICONAL_8;
        else if(semanaAdicional == 9)
            tipoAdicional = ClientesConstants.TIPO_CLIENTE_ADICONAL_9;
        else if(semanaAdicional == 10)
            tipoAdicional = ClientesConstants.TIPO_CLIENTE_ADICONAL_10 ;
        
        return tipoAdicional;
    }
}
