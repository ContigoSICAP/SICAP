/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sicap.clientes.util;

import com.sicap.clientes.commands.CommandConfirmaDesembolsoGrupal;
import com.sicap.clientes.dao.BitacoraSalidaCarteraDAO;
import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.ClienteDAO;
import com.sicap.clientes.dao.DAOMaster;
import com.sicap.clientes.dao.DireccionDAO;
import com.sicap.clientes.dao.SaldoFondeadorDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.dao.TasaDAO;
import com.sicap.clientes.dao.cartera.CreditoCartDAO;
import com.sicap.clientes.dao.cartera.DevengamientoDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.cartera.TablaDevengamientoHelper;
import com.sicap.clientes.helpers.cartera.TransaccionesHelper;
import com.sicap.clientes.vo.AmortizacionPagareVO;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.SaldoFondeadorVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.cartera.CreditoCartVO;
import com.sicap.clientes.vo.cartera.RubrosVO;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author avillanueva
 */
public class FondeadorUtil extends DAOMaster {

    private static Logger myLogger = Logger.getLogger(FondeadorUtil.class);

    public static int asignaFondeador(CicloGrupalVO ciclo, int idSucursal) throws ClientesException {

        int numFondeador = 0, dirFommur = 0, numIntegrantes = 0, dirFinafim = 0;
        ClienteVO cliente = new ClienteVO();
        SolicitudVO solicitudVO = new SolicitudVO();
        DireccionDAO dirrecionDAO = new DireccionDAO();
        SolicitudDAO solicitudDAO = new SolicitudDAO();
        ClienteDAO clienteDAO = new ClienteDAO();
        String fechaInicio = "";
        int pagareExtra = 0;
        try {
            fechaInicio = CatalogoHelper.getParametro("FECHA_INI_PRONAFIM");
            pagareExtra = Integer.parseInt(CatalogoHelper.getParametro("FINAFIM_PAGARE"));
            SimpleDateFormat sdf = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_EU);
            Date fechaIni = sdf.parse(fechaInicio);
            if (ciclo.integrantesArray != null) {
                numIntegrantes = ciclo.integrantesArray.size();
                for (IntegranteCicloVO integrante : ciclo.integrantesArray) {
                    cliente = clienteDAO.getCliente(integrante.idCliente);
                    cliente.direcciones = dirrecionDAO.getDirecciones(integrante.idCliente);
                    solicitudVO = solicitudDAO.getSolicitud(integrante.idCliente, integrante.idSolicitud);
                    if (cliente.direcciones != null) {
                        cliente.direcciones[0].dirFommur = dirrecionDAO.getLocalidadFommur(cliente.direcciones[0].numestado, cliente.direcciones[0].numMunicipio, cliente.direcciones[0].idLocalidad);
                        cliente.direcciones[0].dirFommurDos = dirrecionDAO.getMunicipioFommur2(cliente.direcciones[0].numestado, cliente.direcciones[0].numMunicipio);
                        if (cliente.direcciones[0].dirFommur == 1 && cliente.sexo == ClientesConstants.ID_GENERO_FEMENINO && solicitudVO.fechaCaptura.after(fechaIni)) //if(dirrecionDAO.getLocalidadFommur(cliente.direcciones[0].numestado, cliente.direcciones[0].numMunicipio, cliente.direcciones[0].idLocalidad) == 1 && cliente.sexo == ClientesConstants.ID_GENERO_FEMENINO && solicitudVO.fechaCaptura.after(fechaIni))
                        {
                            dirFommur++;
                        }
                        if (cliente.direcciones[0].dirFommurDos == 1 && cliente.sexo == ClientesConstants.ID_GENERO_FEMENINO && solicitudVO.fechaCaptura.after(fechaIni)) {
                            dirFinafim++;
                        }
                    }
                }
            }
            if (numIntegrantes == dirFinafim & new TasaDAO().getTasaAutorizadaFommmur(ciclo.tasa)) {
                if(pagareExtra != 0){
                    //numFondeador = ClientesConstants.ID_FONDEADOR_FINAFIM_P2; //descomentar y comentar la lina de abajo para cambiar la prioridad de FINAFIM
                    numFondeador = ClientesConstants.ID_FONDEADOR_FINAFIM;
                }
                else{
                    //numFondeador = ClientesConstants.ID_FONDEADOR_FINAFIM; //descomentar y comentar la lina de abajo para cambiar la prioridad de FINAFIM
                    numFondeador = ClientesConstants.ID_FONDEADOR_FINAFIM_P2;
                }
            } else if (numIntegrantes == dirFommur & new TasaDAO().getTasaAutorizadaFommmur(ciclo.tasa)) {//APLICAR CONDICIONES DE SALDO
                numFondeador = ClientesConstants.ID_FONDEADOR_FOMMUR;
            } else {
                numFondeador = ClientesConstants.ID_FONDEADOR_CREDITO_REAL;
            }
        } catch (Exception e) {
            myLogger.error("ClientesException", e);
            throw new ClientesException(e.getMessage());
        }
        return numFondeador;
    }

    public static int cambiaFondeador(CicloGrupalVO ciclo, int idSucursal) throws ClientesException {

        int numFondeador = 0, dirFommur = 0, numIntegrantes = 0;
        ClienteVO cliente = new ClienteVO();
        SolicitudVO solicitudVO = new SolicitudVO();
        DireccionDAO dirrecionDAO = new DireccionDAO();
        SolicitudDAO solicitudDAO = new SolicitudDAO();
        ClienteDAO clienteDAO = new ClienteDAO();
        String fechaInicio = "";
        try {
            fechaInicio = CatalogoHelper.getParametro("FECHA_INI_PRONAFIM");
            SimpleDateFormat sdf = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_EU);
            Date fechaIni = sdf.parse(fechaInicio);
            if (ciclo.integrantes != null) {
                numIntegrantes = ciclo.integrantes.length;
                for (IntegranteCicloVO integrante : ciclo.integrantes) {
                    cliente = clienteDAO.getCliente(integrante.idCliente);
                    cliente.direcciones = dirrecionDAO.getDirecciones(integrante.idCliente);
                    solicitudVO = solicitudDAO.getSolicitud(integrante.idCliente, integrante.idSolicitud);
                    if (cliente.direcciones != null) {
                        cliente.direcciones[0].dirFommur = dirrecionDAO.getLocalidadFommur(cliente.direcciones[0].numestado, cliente.direcciones[0].numMunicipio, cliente.direcciones[0].idLocalidad);
                        if (cliente.direcciones[0].dirFommur == 1 && cliente.sexo == ClientesConstants.ID_GENERO_FEMENINO && solicitudVO.fechaCaptura.after(fechaIni)) {
                            dirFommur++;
                        }
                    }
                }
                if (numIntegrantes == dirFommur & new TasaDAO().getTasaAutorizadaFommmur(ciclo.tasa)) {
                    numFondeador = ClientesConstants.ID_FONDEADOR_FOMMUR;
                } else {
                    numFondeador = ClientesConstants.ID_FONDEADOR_CREDITO_REAL;
                }
            }

        } catch (Exception e) {
            myLogger.error("ClientesException", e);
            throw new ClientesException(e.getMessage());
        }
        return numFondeador;
    }

    public static int cambiaFondeadorFinafim(CicloGrupalVO ciclo, int idSucursal) throws ClientesException {

        int numFondeador = 0, dirFommur2 = 0, dirFommur = 0, numIntegrantes = 0;
        ClienteVO cliente = new ClienteVO();
        SolicitudVO solicitudVO = new SolicitudVO();
        DireccionDAO dirrecionDAO = new DireccionDAO();
        SolicitudDAO solicitudDAO = new SolicitudDAO();
        ClienteDAO clienteDAO = new ClienteDAO();
        String fechaInicio = "";
        try {
            fechaInicio = CatalogoHelper.getParametro("FECHA_INI_PRONAFIM");
            SimpleDateFormat sdf = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_EU);
            Date fechaIni = sdf.parse(fechaInicio);
            if (ciclo.integrantes != null) {
                numIntegrantes = ciclo.integrantes.length;
                for (IntegranteCicloVO integrante : ciclo.integrantes) {
                    cliente = clienteDAO.getCliente(integrante.idCliente);
                    cliente.direcciones = dirrecionDAO.getDirecciones(integrante.idCliente);
                    solicitudVO = solicitudDAO.getSolicitud(integrante.idCliente, integrante.idSolicitud);
                    if (cliente.direcciones != null) {
                        cliente.direcciones[0].dirFommur = dirrecionDAO.getLocalidadFommur(cliente.direcciones[0].numestado, cliente.direcciones[0].numMunicipio, cliente.direcciones[0].idLocalidad);
                        if (cliente.direcciones[0].dirFommurDos == 1 && cliente.sexo == ClientesConstants.ID_GENERO_FEMENINO && solicitudVO.fechaCaptura.after(fechaIni)) {
                            dirFommur2++;
                        }
                        if (cliente.direcciones[0].dirFommur == 1 && cliente.sexo == ClientesConstants.ID_GENERO_FEMENINO && solicitudVO.fechaCaptura.after(fechaIni)) {
                            dirFommur++;
                        }
                    }
                }
                if (numIntegrantes == dirFommur2 & new TasaDAO().getTasaAutorizadaFommmur(ciclo.tasa)) {
                    numFondeador = ClientesConstants.ID_FONDEADOR_FOMMUR_DOS;
                } else if (numIntegrantes == dirFommur & new TasaDAO().getTasaAutorizadaFommmur(ciclo.tasa)) {
                    numFondeador = ClientesConstants.ID_FONDEADOR_FOMMUR;
                } else {
                    numFondeador = ClientesConstants.ID_FONDEADOR_CREDITO_REAL;
                }
            }
        } catch (Exception e) {
            myLogger.error("ClientesException", e);
            throw new ClientesException(e.getMessage());
        }
        return numFondeador;
    }

    public int actualizaSaldoFondeador(SaldoFondeadorVO saldoFondeadorVo) throws ClientesDBException {
        int respuesta = 0;
        Connection cn = null;
        SaldoFondeadorDAO saldoFondDAO = new SaldoFondeadorDAO();
        NumberFormat num = new DecimalFormat("########0.##");

        try {
            cn = getConnection();
            cn.setAutoCommit(false);
            synchronized (this) {
                double saldoFondeador = Convertidor.stringToDouble(CatalogoHelper.getParametro("SALDO_FONDEADOR_" + saldoFondeadorVo.numFondeador, cn));
                myLogger.debug("Saldo inicial: " + saldoFondeador);
                if (saldoFondeadorVo.tipoMov.equals(ClientesConstants.DESEMBOLSO)) {
                    if (saldoFondeador > saldoFondeadorVo.monto) {
                        myLogger.debug("Entra a disminucion de Saldo Fondeador");
                        saldoFondeadorVo.saldoFinal = saldoFondeador - saldoFondeadorVo.monto;
                        myLogger.debug("Saldo final: " + num.format(saldoFondeadorVo.saldoFinal));
                        CatalogoHelper.updateParametro("SALDO_FONDEADOR_" + saldoFondeadorVo.numFondeador, num.format(saldoFondeadorVo.saldoFinal), cn);
                        saldoFondDAO.insertMovimiento(saldoFondeadorVo, cn);
                        respuesta = 1;
                    } else if (saldoFondeadorVo.numFondeador == ClientesConstants.ID_FONDEADOR_CREDITO_REAL) {
                        saldoFondeadorVo.saldoFinal = 0;
                        saldoFondDAO.insertMovimiento(saldoFondeadorVo, cn);
                        respuesta = 1;
                    } else { //saldo insuficiente
                        myLogger.debug("Saldo de fondeador insufiente");
                        respuesta = 2;
                    }
                } else if (saldoFondeadorVo.tipoMov.equals(ClientesConstants.CANCELACION_DESEMBOLSO)
                        || saldoFondeadorVo.tipoMov.equals(ClientesConstants.DEVOLUCION)
                        || saldoFondeadorVo.tipoMov.equals(ClientesConstants.CAMBIO_FONDEADOR_SALIDA)
                        || saldoFondeadorVo.tipoMov.equals(ClientesConstants.COBRANZA)) {
                    if (saldoFondeadorVo.numFondeador == ClientesConstants.ID_FONDEADOR_CREDITO_REAL) {
                        saldoFondeadorVo.saldoFinal = 0;
                        saldoFondDAO.insertMovimiento(saldoFondeadorVo, cn);
                        respuesta = 1;
                    } else {
                        myLogger.debug("Entra a aumento de Saldo Fondeador");
                        saldoFondeadorVo.saldoFinal = saldoFondeador + saldoFondeadorVo.monto;
                        myLogger.debug("Saldo final: " + num.format(saldoFondeadorVo.saldoFinal));
                        CatalogoHelper.updateParametro("SALDO_FONDEADOR_" + saldoFondeadorVo.numFondeador, num.format(saldoFondeadorVo.saldoFinal), cn);
                        saldoFondDAO.insertMovimiento(saldoFondeadorVo, cn);
                        respuesta = 1;
                    }
                }
            }
            cn.commit();
        } catch (Exception e) {
            myLogger.info("Entra al catch");
            myLogger.error("ActualizaSaldoFondeador: ", e);
            try {
                cn.rollback();
            } catch (Exception ce) {
                myLogger.info("Error al realizar el Commit");
                throw new ClientesDBException(ce.getMessage());
            }
            throw new ClientesDBException(e.getMessage());
        } finally {
            myLogger.info("Enviando respuesta");
            try {
                cn.close();
            } catch (Exception ce) {
                myLogger.info("Error al realizar el Commit");
                throw new ClientesDBException(ce.getMessage());
            }
        }
        return respuesta;
    }

    /***
     * Meotodo de asignacion y remocion de cartera BURSA (FONDEADOR 9)
     * @param conexion 
     */
    public  static void cierreBursa(Connection conexion,Connection conexionCart) {
        
        //Parametros Bursa
        double saldoBursaGarantizado=0;//Este viene de parámetros
        double porcentajeGarantiaBursa=0;//.20 viene tambien de parametros
        int numPagosMinimo = 0;
        int numDiasMora = 0;
        String ultimaFecha="";
        
        double saldoBursaActual=0;//1,500,000,000 o más.Este es de la suma viene de la Base de datos
        
        double saldoCarteraExcluidos = 0;//Este es la suma de los excluidos(a excluir)
        double saldoCarteraRestante = 0;//Este es la suma de los que se quedaron
        double saldoInsoluto15pc,saldoInsoluto30pc;
        ArrayList<SaldoIBSVO> listaSaldosBursa;//Lista con los saldos asociadosa bursa en la BASE DE DATOS
        ArrayList<SaldoIBSVO> listaSaldosExcluidos = new ArrayList<SaldoIBSVO>();//Esta es una de las Listas a actualizar en BASE DE DATOS
        ArrayList<SaldoIBSVO> listaSaldosRestantes = new ArrayList<SaldoIBSVO>();//Lista donde se agregan los saldos restantesen bursa
        ArrayList<SaldoIBSVO> listaSaldosAgregadosNuevos = new ArrayList<SaldoIBSVO>();//Lista con los saldos queseagregaran a lacarteradeBUrsa
        Map<Integer , Double >  mapMontoEstadoAcumulado ;
        SaldoIBSDAO saldoIBSDAO = new SaldoIBSDAO();//Instancia para el DAO
        double saldoAcumulado = 0;
        
        try {
            
            
            //Validar ultima fecha registrada de ejecucion
            //Traer fecha ultima ejejcucion completa
            ultimaFecha = CatalogoHelper.getParametro("FECHA_CIERRE");
            SimpleDateFormat sdf = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_EU);
            String sFechaUltimaEjecucion = CatalogoHelper.getParametroFondeador("FECHA_CIERRE_FONDEADOR",ClientesConstants.ID_FONDEADOR_BURSA);
            Date fechaHoy = new Date();// get current date    
            String sFechaHoy = sdf.format(fechaHoy);
            
            myLogger.info("Ultima Ejecucion: "+ sFechaUltimaEjecucion);
            myLogger.info("Hoy: "+ sFechaHoy);
            
            if(!sFechaHoy.equals(sFechaUltimaEjecucion)){           
            //1.- Traer parametros BURSA: SALDO GARANTIZADO, PORCENTAJE GARANTIA, DIAS MORA, NUM PAGOSMINIMO
            saldoBursaGarantizado = Convertidor.stringToDouble(CatalogoHelper.getParametroFondeador("SALDO_FONDEADOR",ClientesConstants.ID_FONDEADOR_BURSA,conexion));
            porcentajeGarantiaBursa = Convertidor.stringToDouble(CatalogoHelper.getParametroFondeador("PORCENTAJE_GARANTIA_FONDEADOR",ClientesConstants.ID_FONDEADOR_BURSA,conexion));
            numDiasMora = Convertidor.stringToInt(CatalogoHelper.getParametroFondeador("DIAS_MORA_FONDEADOR",ClientesConstants.ID_FONDEADOR_BURSA, conexion));
            numPagosMinimo = Convertidor.stringToInt(CatalogoHelper.getParametroFondeador("NUM_PAGOS_MINIMOS_FONDEADOR",ClientesConstants.ID_FONDEADOR_BURSA, conexion));
            
            saldoInsoluto15pc=0.15*( saldoBursaGarantizado*(1+porcentajeGarantiaBursa) );
            saldoInsoluto30pc=0.30*( saldoBursaGarantizado*(1+porcentajeGarantiaBursa) );
            
            myLogger.info("saldoBursaGarantizado: "+saldoBursaGarantizado);
            myLogger.info("saldoInsoluto15pc: "+saldoInsoluto15pc);
            myLogger.info("saldoInsoluto30pc: "+saldoInsoluto30pc);
            
            //2.- Traer saldo BURSA AL MOMENTO  
            saldoBursaActual=saldoIBSDAO.
                    getSumaSaldoCapitalByFondeadoGarantiaAndNotEstatus(ClientesConstants.ID_FONDEADOR_BURSA,ClientesConstants.SITUACION_CREDITO_SALDOST24_LIQUIDADO ,conexion);
            myLogger.info("saldoBursaActual: "+saldoBursaActual);
            

            //3.- Validar creditos con mora >= 90 dias a eliminar
            
            /*
                Traer lista de la tabla saldos de la cartera asociada a la 
                fondeadora(ib_fondeo_garantia) dada, es la lista al momento.
                Para BURSA se agrego con ID 9
            */
           
            listaSaldosBursa = saldoIBSDAO
                        .getSaldosEstadoByFondeoGarantiaAndNotEstatus(ClientesConstants.ID_FONDEADOR_BURSA,ClientesConstants.SITUACION_CREDITO_SALDOST24_LIQUIDADO ,conexion);
            myLogger.info("Condicion de dias de mora >= "+numDiasMora);
            for(SaldoIBSVO saldoBursa:listaSaldosBursa){
                
                if(saldoBursa.getDiasMora()>= numDiasMora){
                    //Sale de cartera en garantía
                    saldoBursa.setFondeoGarantia(0);//Se actualizará a 0(Sin fondeoGarantia)
                    listaSaldosExcluidos.add(saldoBursa);
                    saldoCarteraExcluidos+=saldoBursa.getSaldoCapital();
                }else{
                    listaSaldosRestantes.add(saldoBursa);
                    saldoCarteraRestante+=saldoBursa.getSaldoCapital();
                }
                
            }
            
            myLogger.info("listaSaldosExcluidos size: "+listaSaldosExcluidos.size());
            myLogger.info("saldoCarteraExcluidos: "+ saldoCarteraExcluidos);
            
             
            //4.- Agregar saldos si es necesario
            //Diferencia entre Saldo Garantizado y  SaldoActual
            //(saldoBursaActual  -  saldoCarteraExcluidos) < 1,500,000,000
            if( saldoBursaActual-saldoCarteraExcluidos < saldoBursaGarantizado*(1+porcentajeGarantiaBursa)){
                myLogger.info("Completar cartera");
                
                //Inicializar map de saldos por estado
                mapMontoEstadoAcumulado = inicializaMapaSaldoEstados(32);
                //displayMapSaldoByEstado(mapMontoEstadoAcumulado);
                
                //Llenar map saldos restantes
                Integer llaveEdo;
                Double valorSaldo;          
                for(SaldoIBSVO saldoRestante: listaSaldosRestantes){
                    llaveEdo=saldoRestante.getIdEstado();
                    valorSaldo=saldoRestante.getSaldoCapital();
                    
                    
                    if( mapMontoEstadoAcumulado.containsKey( llaveEdo ) ){
                        //Sumar al anterior
                        mapMontoEstadoAcumulado.
                                put( llaveEdo , mapMontoEstadoAcumulado.get(llaveEdo)+ valorSaldo );
                    }else{
                        //Agregar primera vez
                        mapMontoEstadoAcumulado.
                                put( llaveEdo , valorSaldo );
                    }
                    
                }
                
                double saldoElegibleTotal = saldoIBSDAO.getSaldoAcumuladoElegibleTotal(ClientesConstants.ID_FONDEADOR_CREDITO_REAL, conexion);
                
                double saldoElegible65pc = 0.65 * saldoElegibleTotal;
                        
                myLogger.info("Saldo Elegible Total: "+saldoElegibleTotal);
                myLogger.info("65% Saldo Elegible Total: "+saldoElegible65pc);
                
                //Traer lista ordenada de estados por saldo
                ArrayList<Integer> listaEstados;
                //listaEstados = saldoIBSDAO.getSaldosByEstadoByFondeadorAndEstatus(1, 1, 3);
                //Se agrego filtro de tasa en el query
                listaEstados = saldoIBSDAO
                        .getEstadosBySaldosAcumuladosElegibles(ClientesConstants.ID_FONDEADOR_CREDITO_REAL, 
                                ClientesConstants.SITUACION_CREDITO_SALDOST24_VIGENTE, numPagosMinimo,ClientesConstants.ID_FONDEADOR_BURSA, conexion);

                /*
                myLogger.info("----------ESTADOS----------");
                myLogger.info(listaEstados);
                */
                
                saldoAcumulado=saldoCarteraRestante;
                int countIter=0;
                double sumaSaldoAsignado = 0;
                for(Integer idEstado: listaEstados){
                //-----------------------------------------------------------------------------------------------------
                    //Traer lista de candidatos de base datos por estado
                    ArrayList<SaldoIBSVO> listaCandidatos;

                    
                    listaCandidatos = saldoIBSDAO
                            .getSaldosElegiblesByEstado( ClientesConstants.ID_FONDEADOR_CREDITO_REAL,
                                    ClientesConstants.SITUACION_CREDITO_SALDOST24_VIGENTE, numPagosMinimo, idEstado,ClientesConstants.ID_FONDEADOR_BURSA,conexion);

                    myLogger.info("-------SALDOS CANDIDATOS ESTADO ("+idEstado+") : "+listaCandidatos.size()+" -------");

                    
                    for(SaldoIBSVO saldoCandidato: listaCandidatos){

                        llaveEdo=saldoCandidato.getIdEstado();
                        valorSaldo=saldoCandidato.getSaldoCapital();
                        sumaSaldoAsignado+=valorSaldo;
                        /*
                        llaveEdo=Integer.valueOf( saldoCandidato.getIdEstado());
                        valorSaldo=Double.valueOf(saldoCandidato.getSaldoCapital() );
                        */
                        countIter++;
                        if(sumaSaldoAsignado>saldoElegible65pc){
                            myLogger.info("El saldo elegible sobrepasa el 65% del saldo total: "+sumaSaldoAsignado);
                            break;
                        }
                        
                        //boolean excepcionEstados=llaveEdo==15 || llaveEdo==9;

                        //double saldoMax  = excepcionEstados?saldoInsoluto30pc:saldoInsoluto15pc;//15% o 30%
                        double saldoMax  = saldoInsoluto15pc;//15%

                        /*double sumaSaldoEstado=excepcionEstados
                                ?mapMontoEstadoAcumulado.get(9)+mapMontoEstadoAcumulado.get(15)+valorSaldo
                                :mapMontoEstadoAcumulado.get(llaveEdo)+valorSaldo;*/
                        double sumaSaldoEstado=mapMontoEstadoAcumulado.get(llaveEdo)+valorSaldo;
                        

                        if(sumaSaldoEstado <= saldoMax){
                            //Se agrega saldo a lista a actualizar
                            saldoCandidato.setFondeoGarantia(ClientesConstants.ID_FONDEADOR_BURSA);//Se actualizará a  9
                            listaSaldosAgregadosNuevos.add(saldoCandidato);

                            //Se actualizan saldos por estado y por 
                            mapMontoEstadoAcumulado.put(llaveEdo, sumaSaldoEstado);
                            saldoAcumulado+=valorSaldo;
                        }

                        //Break segundo loop
                        if(saldoAcumulado >= saldoBursaGarantizado*(1+porcentajeGarantiaBursa)){
                            break;
                        }
                    }
                    
                    if(sumaSaldoAsignado>saldoElegible65pc){
                        myLogger.info("El saldo elegible sobrepasa el 65% del saldo total: "+sumaSaldoAsignado);
                        break;
                    }
                    //Break primero loop
                    if(saldoAcumulado >= saldoBursaGarantizado*(1+porcentajeGarantiaBursa)){
                        break;
                    }
                    
                //-----------------------------------------------------------------------------------------------------   
                }
                
                //Check de cartera garantizada o no
                if(/*false &&*/ saldoAcumulado >= saldoBursaGarantizado*(1+porcentajeGarantiaBursa)){
                    myLogger.info("CARTERA GARANTIZADA");
                }else{
                    //Enviar correo de cartera sin completar
                    myLogger.info("NO se completo la CARTERA GARANTIZADA");
                    
                    String Destinatarios = CatalogoHelper.getParametro("MAIL_CARTERA_INCOMPLETA_RECIPIENT");
                    myLogger.debug("Destinatarios: " + Destinatarios);
                    mandaCorreoCarteraIncompleta(ClientesConstants.ID_FONDEADOR_BURSA,saldoAcumulado, saldoBursaGarantizado*(1+porcentajeGarantiaBursa) , Destinatarios);
                    
                }
                
                myLogger.info("Break en "+countIter+" iteraciones");
                myLogger.info("Saldo acumulado: "+saldoAcumulado);
                displayMapSaldoByEstado(mapMontoEstadoAcumulado);
                
                myLogger.info("Saldos a Eliminar: "+listaSaldosExcluidos.size());
                myLogger.info("Saldos a Agregar: "+listaSaldosAgregadosNuevos.size());
                //displayListaSaldos(listaSaldosAgregadosNuevos);
                
                
                
                //Actualizar saldos
                
                //A agregar al fondeador
                
                saldoIBSDAO.updateFondeoGarantia(listaSaldosAgregadosNuevos,conexion,ultimaFecha);
                
                //Por cada saldo se insertan transacciones del perfil contable de ENTRADA DE BURSA
                registraPerfilContableCartera(listaSaldosAgregadosNuevos, conexionCart, ClientesConstants.CARTERA_INGRESO_BURSA,false,ClientesConstants.ID_FONDEADOR_BURSA);
                
                //TODO Actulizar fondeador en saldo,ciclogrupales y credito. Demanerasmimilrque en IntercicloHelper
                actualizaFondeadorOtrasTablas(listaSaldosAgregadosNuevos, conexion, conexionCart, ClientesConstants.ID_FONDEADOR_BURSA);

                //A quitar al fondeador
                if(!listaSaldosExcluidos.isEmpty()){
                    saldoIBSDAO.updateFondeoGarantia(listaSaldosExcluidos,conexion,ultimaFecha);
                    BitacoraSalidaCarteraDAO bitacoraSalidaCarteraDAO = new BitacoraSalidaCarteraDAO();
                    bitacoraSalidaCarteraDAO.addBitacoraSalida(listaSaldosExcluidos, ClientesConstants.ID_FONDEADOR_BURSA, 0, conexion); 
                    
                    //Por cada saldo se insertan transacciones del perfil contable de SALIDA DE BURSA
                    registraPerfilContableCartera(listaSaldosExcluidos, conexionCart, ClientesConstants.CARTERA_SALIDA_BURSA,false,ClientesConstants.ID_FONDEADOR_BURSA);
                    //Actualizar fondeador
                    actualizaFondeadorOtrasTablas(listaSaldosExcluidos, conexion, conexionCart, ClientesConstants.ID_FONDEADOR_CREDITO_REAL);                
                }
                
                
            }else{
                myLogger.info("Cartera completa");
                
            }
            
            //TODO
            //Guardar fecha de ejecucion
            CatalogoDAO catalogoDao= new CatalogoDAO();
            catalogoDao.updateParametroFondeador("FECHA_CIERRE_FONDEADOR",ClientesConstants.ID_FONDEADOR_BURSA, ultimaFecha, conexion);
            
            String saldoGarantizado = new DecimalFormat(ClientesConstants.FORMATO_MONTO).format( saldoAcumulado); 
            catalogoDao.updateParametroFondeador("SALDO_GARANTIZADO",ClientesConstants.ID_FONDEADOR_BURSA, saldoGarantizado , conexion);
            }else {
                myLogger.info("Ya se ha ejecutado el cierre FONDEADOR 9 para la fecha de hoy");
            }
            
        } catch (Exception ex) {
            myLogger.error("Exception", ex);
        }
    
    
    }
 
    public  static void cierreABC(Connection conexion) {
        
	//Parametros ABC
	double saldoGarantizado=0;//50000000
	double porcentajeGarantia=0;//.40 
	int numDiasMora = 0;//30, no mas de
        int numDiasVigencia = 0;//62
        int numPagosMinimo = 0;
	
	double saldoActual=0;//Suma al momento para id 12
	
	double saldoCarteraExcluidos = 0;//Suma de los excluidos(a excluir)
        double saldoCarteraRestante = 0;//Suma de los que se quedaron
	
	ArrayList<SaldoIBSVO> listaSaldosActual;//Lista con los saldos asociados a ABC en la BASE DE DATOS
	
	ArrayList<SaldoIBSVO> listaSaldosExcluidos = new ArrayList<SaldoIBSVO>();//Esta es una de las Listas a actualizar en BASE DE DATOS
	
	//ArrayList<SaldoIBSVO> listaSaldosRestantes = new ArrayList<SaldoIBSVO>();//Lista donde se agregan los saldos restantesen bursa
	ArrayList<SaldoIBSVO> listaSaldosAgregadosNuevos = new ArrayList<SaldoIBSVO>();//Lista con los saldos queseagregaran a lacarteradeBUrsa
	
	SaldoIBSDAO saldoIBSDAO = new SaldoIBSDAO();//Instancia para el DAO
        double saldoAcumulado=0;
        String  ultimaFecha ="";
        
        try {
            
            //Validar ultima fecha registrada de ejecucion
            //Traer fecha ultima ejejcucion completa
            ultimaFecha = CatalogoHelper.getParametro("FECHA_CIERRE");
            SimpleDateFormat sdf = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_EU);
            String sFechaUltimaEjecucion = CatalogoHelper.getParametroFondeador("FECHA_CIERRE_FONDEADOR",ClientesConstants.ID_FONDEADOR_ABC);
            Date fechaHoy = new Date();// get current date    
            String sFechaHoy = sdf.format(fechaHoy);
            
            myLogger.info("Ultima Ejecucion: "+ sFechaUltimaEjecucion);
            myLogger.info("Hoy: "+ sFechaHoy);
            boolean datesEquals = sFechaHoy.equals(sFechaUltimaEjecucion);
            myLogger.info("Equals: "+ datesEquals);
            
            if(!datesEquals ){
            //1.- Traer parametros : SALDO GARANTIZADO, PORCENTAJE GARANTIA, DIAS MORA
            saldoGarantizado = Convertidor.stringToDouble(CatalogoHelper.getParametroFondeador("SALDO_FONDEADOR",ClientesConstants.ID_FONDEADOR_ABC,conexion));
            porcentajeGarantia = Convertidor.stringToDouble(CatalogoHelper.getParametroFondeador("PORCENTAJE_GARANTIA_FONDEADOR",ClientesConstants.ID_FONDEADOR_ABC,conexion));
            numDiasMora = Convertidor.stringToInt(CatalogoHelper.getParametroFondeador("DIAS_MORA_FONDEADOR",ClientesConstants.ID_FONDEADOR_ABC, conexion));
            numDiasVigencia = Convertidor.stringToInt(CatalogoHelper.getParametroFondeador("NUM_DIAS_VIGENCIA_FONDEADOR",ClientesConstants.ID_FONDEADOR_ABC, conexion));
            numPagosMinimo = Convertidor.stringToInt(CatalogoHelper.getParametroFondeador("NUM_PAGOS_MINIMOS_FONDEADOR",ClientesConstants.ID_FONDEADOR_ABC, conexion));
            
            myLogger.info("saldoGarantizado: "+saldoGarantizado);
			
            //2.- Traer saldo ABC AL MOMENTO   
            saldoActual =saldoIBSDAO.
                    getSumaSaldoCapitalByFondeadoGarantiaAndNotEstatus(ClientesConstants.ID_FONDEADOR_ABC,ClientesConstants.SITUACION_CREDITO_SALDOST24_LIQUIDADO,conexion);
            myLogger.info("saldoActual: "+saldoActual);          

            //3.- Validar creditos con mora > 30 dias a eliminar
            
            /*
                Traer lista de la tabla saldos de la cartera asociada a la 
                fondeadora(ib_fondeo_garantia) dada, es la lista al momento.
                ABC se agrego con ID 12
            */
           
            //TODO Revisar query para ABC
            listaSaldosActual = saldoIBSDAO
                    .getSaldosEstadoByFondeoGarantiaAndNotEstatus(ClientesConstants.ID_FONDEADOR_ABC,ClientesConstants.SITUACION_CREDITO_SALDOST24_LIQUIDADO,conexion);
            myLogger.info("Condicion de dias de mora >= "+numDiasMora);
            for(SaldoIBSVO saldo:listaSaldosActual){
                
                if(saldo.getDiasMora()>= numDiasMora){
                    //Sale de cartera en garantía
                    saldo.setFondeoGarantia(0);//Se actualizará a 0(Sin fondeoGarantia)
                    listaSaldosExcluidos.add(saldo);
                    saldoCarteraExcluidos+=saldo.getSaldoCapital();
                }else{
                    //listaSaldosRestantes.add(saldo);
                    saldoCarteraRestante+=saldo.getSaldoCapital();
                }
                
            }
            
            myLogger.info("listaSaldosExcluidos size: "+listaSaldosExcluidos.size());
            myLogger.info("saldoCarteraExcluidos: "+ saldoCarteraExcluidos);
            
             
            //4.- Agregar saldos si es necesario
            //Diferencia entre Saldo Garantizado y  SaldoActual
            //(saldoActual  -  saldoCarteraExcluidos) < 67,500,000
            if( saldoActual-saldoCarteraExcluidos < saldoGarantizado*(1+porcentajeGarantia)){
                myLogger.info("Completar cartera");
                
                
                saldoAcumulado=saldoCarteraRestante;
                int countIter=0;
                //Traer lista de candidatos de base datos por estado
                ArrayList<SaldoIBSVO> listaCandidatos;

                //Saldos candidatos con filtro por vigencia
                listaCandidatos = saldoIBSDAO
                        .getSaldosElegiblesByDiasVigencia(ClientesConstants.ID_FONDEADOR_CREDITO_REAL,
                                ClientesConstants.SITUACION_CREDITO_SALDOST24_VIGENTE, numDiasVigencia, numPagosMinimo, conexion);
                myLogger.info("-------SALDOS CANDIDATOS : "+listaCandidatos.size()+" -------");

                Double valorSaldo;
                for(SaldoIBSVO saldoCandidato: listaCandidatos){
                    

                    valorSaldo=saldoCandidato.getSaldoCapital();

                    //Se agrega saldo a lista a actualizar
                    saldoCandidato.setFondeoGarantia(ClientesConstants.ID_FONDEADOR_ABC);//Se actualizará a  12
                    listaSaldosAgregadosNuevos.add(saldoCandidato);

                    saldoAcumulado+=valorSaldo;

                    countIter++;
                    //Break loop
                    if(saldoAcumulado >= saldoGarantizado*(1+porcentajeGarantia)){
                            break;
                    }
                }
                
                //Check de cartera garantizada o no
                if(/*false &&*/ saldoAcumulado >= saldoGarantizado*(1+porcentajeGarantia)){
                    myLogger.info("CARTERA GARANTIZADA");
                }else{
                    //Enviar correo de cartera sin completar
                    myLogger.info("NO se completo la CARTERA GARANTIZADA");
                    
                    String Destinatarios = CatalogoHelper.getParametro("MAIL_CARTERA_INCOMPLETA_RECIPIENT");
                    myLogger.debug("Destinatarios: " + Destinatarios);
                    mandaCorreoCarteraIncompleta(ClientesConstants.ID_FONDEADOR_ABC,saldoAcumulado, saldoGarantizado*(1+porcentajeGarantia) , Destinatarios);
                    
                }
                
                myLogger.info("Break en "+countIter+" iteraciones");
                myLogger.info("Saldo acumulado: "+saldoAcumulado);
                
                myLogger.info("Saldos a Eliminar: "+listaSaldosExcluidos.size());
                myLogger.info("Saldos a Agregar: "+listaSaldosAgregadosNuevos.size());
                //displayListaSaldos(listaSaldosAgregadosNuevos);
                
				
                //Actualizar saldos              
                //A agregar al fondeador
                
                saldoIBSDAO.updateFondeoGarantia(listaSaldosAgregadosNuevos,conexion,ultimaFecha);
               
                //A quitar al fondeador
                if(!listaSaldosExcluidos.isEmpty()){
                    saldoIBSDAO.updateFondeoGarantia(listaSaldosExcluidos,conexion,ultimaFecha);
                    BitacoraSalidaCarteraDAO bitacoraSalidaCarteraDAO = new BitacoraSalidaCarteraDAO();
                    bitacoraSalidaCarteraDAO.addBitacoraSalida(listaSaldosExcluidos, ClientesConstants.ID_FONDEADOR_ABC, 0, conexion);                    
                }
                
                
            }else{
                myLogger.info("Cartera completa");
                
            }
            
            //Guardar fecha de ejecucion
            CatalogoDAO catalogoDao= new CatalogoDAO();
            catalogoDao.updateParametroFondeador("FECHA_CIERRE_FONDEADOR",ClientesConstants.ID_FONDEADOR_ABC, ultimaFecha, conexion);
            
            String cSsaldoGarantizado = new DecimalFormat(ClientesConstants.FORMATO_MONTO).format( saldoAcumulado); 
            catalogoDao.updateParametroFondeador("SALDO_GARANTIZADO",ClientesConstants.ID_FONDEADOR_ABC, cSsaldoGarantizado , conexion);
            }else {
                myLogger.info("Ya se ha ejecutado el cierre FONDEADOR 12 para la fecha de hoy");
            }
            
        } catch (Exception ex) {
            myLogger.error("Exception", ex);
        }
	
    }
    
    /**
     * Metodo de cierre para actualizar cartera garantizada a un fondeador dado
     * @param idFondeador identificador de fondeadora cerrar
     * @param preSeleccion bandera para indicar si se hara preseleccion en lugar de asigancion directa de cartera
     * @param conexion 
     */
    public  static void cierreFondeadorGenerico(int idFondeador,boolean preSeleccion, Connection conexion) {
        
	//Parametros comunes BX+, ACTINVER, BAJIO
	double saldoGarantizado=0;//Saldo(Linea de credito)
	double porcentajeGarantia=0;//Porcentaje que se tendra que garantizar 
	int numDiasMora = 0;//Si aplica
        int numPagosMinimo = 0;
	
	double saldoActual=0;//Suma al momento para el ID dado
	
	double saldoCarteraExcluidos = 0;//Suma de los excluidos(a excluir)
        double saldoCarteraRestante = 0;//Suma de los que se quedaron
	
	ArrayList<SaldoIBSVO> listaSaldosActual;//Lista con los saldos asociados a ABC en la BASE DE DATOS
	
	ArrayList<SaldoIBSVO> listaSaldosExcluidos = new ArrayList<SaldoIBSVO>();//Esta es una de las Listas a actualizar en BASE DE DATOS
	
	//ArrayList<SaldoIBSVO> listaSaldosRestantes = new ArrayList<SaldoIBSVO>();//Lista donde se agregan los saldos restantes
	ArrayList<SaldoIBSVO> listaSaldosAgregadosNuevos = new ArrayList<SaldoIBSVO>();//Lista con los saldos queseagregaran a lacartera
	
	SaldoIBSDAO saldoIBSDAO = new SaldoIBSDAO();//Instancia para el DAO
        double saldoAcumulado=0;
        String ultimaFecha = "";
        
        try {
            
            //Validar ultima fecha registrada de ejecucion
            //Traer fecha ultima ejejcucion completa
            ultimaFecha = CatalogoHelper.getParametro("FECHA_CIERRE");
            SimpleDateFormat sdf = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_EU);
            String sFechaUltimaEjecucion = CatalogoHelper.getParametroFondeador("FECHA_CIERRE_FONDEADOR",idFondeador);
            Date fechaHoy = new Date();// get current date    
            String sFechaHoy = sdf.format(fechaHoy);
            
            myLogger.info("Ultima Ejecucion: "+ sFechaUltimaEjecucion);
            myLogger.info("Hoy: "+ sFechaHoy);
            
            //Si es preseleccion NO validar aqui si no en commadn deorigen
            if(!sFechaHoy.equals(sFechaUltimaEjecucion)){           
            
            
            //1.- Traer parametros : SALDO GARANTIZADO, PORCENTAJE GARANTIA, DIAS MORA
            saldoGarantizado = Convertidor.stringToDouble(CatalogoHelper.getParametroFondeador("SALDO_FONDEADOR ",idFondeador,conexion));
            porcentajeGarantia = Convertidor.stringToDouble(CatalogoHelper.getParametroFondeador("PORCENTAJE_GARANTIA_FONDEADOR",idFondeador,conexion));
            numPagosMinimo = Convertidor.stringToInt(CatalogoHelper.getParametroFondeador("NUM_PAGOS_MINIMOS_FONDEADOR",idFondeador, conexion));
            
            numDiasMora = Convertidor.stringToInt(CatalogoHelper.getParametroFondeador("DIAS_MORA_FONDEADOR",idFondeador, conexion));
            
            myLogger.info("saldoGarantizado: "+saldoGarantizado);
			
            //2.- Traer saldo FONDEADOAR AL MOMENTO   
            saldoActual =saldoIBSDAO.
                    getSumaSaldoCapitalByFondeadoGarantiaAndNotEstatus(idFondeador,ClientesConstants.SITUACION_CREDITO_SALDOST24_LIQUIDADO,conexion);
            myLogger.info("saldoActual: "+saldoActual);
            

            //3.- Validar creditos con mora > N  dias a eliminar
            
            /*
                Traer lista de la tabla saldos de la cartera asociada a la 
                fondeadora(ib_fondeo_garantia) dada, es la lista al momento.
            */
           
            
            listaSaldosActual = saldoIBSDAO
                    .getSaldosEstadoByFondeoGarantiaAndNotEstatus(idFondeador,ClientesConstants.SITUACION_CREDITO_SALDOST24_LIQUIDADO,conexion);
            
            myLogger.info("Condicion de dias de mora >= "+numDiasMora);
            
            for(SaldoIBSVO saldo:listaSaldosActual){
                
                
                if(saldo.getDiasMora()>= numDiasMora){
                    //Sale de cartera en garantía
                    saldo.setFondeoGarantia(0);//Se actualizará a 0(Sin fondeoGarantia)
                    listaSaldosExcluidos.add(saldo);
                    saldoCarteraExcluidos+=saldo.getSaldoCapital();
                }else{
                    //listaSaldosRestantes.add(saldo);
                    saldoCarteraRestante+=saldo.getSaldoCapital();
                }
                
            }
            
            myLogger.info("listaSaldosExcluidos size: "+listaSaldosExcluidos.size());
            myLogger.info("saldoCarteraExcluidos: "+ saldoCarteraExcluidos);
            
             
            //4.- Agregar saldos si es necesario
            //Diferencia entre Saldo Garantizado y  SaldoActual
            
            if( saldoActual-saldoCarteraExcluidos < saldoGarantizado*(1+porcentajeGarantia)){
                myLogger.info("Completar cartera");
                
                
                saldoAcumulado=saldoCarteraRestante;
                int countIter=0;
                //Traer lista de candidatos de base datos por estado
                ArrayList<SaldoIBSVO> listaCandidatos;

                //TODO HAcer o usar query por sin filtro de estado
                listaCandidatos = saldoIBSDAO
                        .getSaldosElegiblesByEstatusSaldoAndFondeador(ClientesConstants.ID_FONDEADOR_CREDITO_REAL,
                                ClientesConstants.SITUACION_CREDITO_SALDOST24_VIGENTE,numPagosMinimo, conexion);
                
                myLogger.info("-------SALDOS CANDIDATOS : "+listaCandidatos.size()+" -------");

                Double valorSaldo;
                for(SaldoIBSVO saldoCandidato: listaCandidatos){
                    

                    valorSaldo=saldoCandidato.getSaldoCapital();

                    //Se agrega saldo a lista a actualizar
                    saldoCandidato.setFondeoGarantia(preSeleccion?(-1*idFondeador):idFondeador);//Se actualizará a idFondeador
                                                                                                //Se valida si es preseleccion o no.
                    listaSaldosAgregadosNuevos.add(saldoCandidato);

                    saldoAcumulado+=valorSaldo;

                    countIter++;
                    //Break loop
                    if(saldoAcumulado >= saldoGarantizado*(1+porcentajeGarantia)){
                            break;
                    }
                }
                
                //Check de cartera garantizada o no
                if(/*false &&*/ saldoAcumulado >= saldoGarantizado*(1+porcentajeGarantia)){
                    myLogger.info("CARTERA GARANTIZADA");
                }else{
                    //Enviar correo de cartera sin completar
                    myLogger.info("NO se completo la CARTERA GARANTIZADA");
                    
                    String Destinatarios = CatalogoHelper.getParametro("MAIL_CARTERA_INCOMPLETA_RECIPIENT");
                    myLogger.debug("Destinatarios: " + Destinatarios);
                    mandaCorreoCarteraIncompleta(idFondeador,saldoAcumulado, saldoGarantizado*(1+porcentajeGarantia) , Destinatarios);
                    
                }
                
                myLogger.info("Break en "+countIter+" iteraciones");
                myLogger.info("Saldo acumulado: "+saldoAcumulado);
                
                myLogger.info("Saldos a Eliminar: "+listaSaldosExcluidos.size());
                myLogger.info("Saldos a Agregar: "+listaSaldosAgregadosNuevos.size());
                //displayListaSaldos(listaSaldosAgregadosNuevos);
                
				
                //Actualizar saldos              
                //A agregar al fondeador
                
                saldoIBSDAO.updateFondeoGarantia(listaSaldosAgregadosNuevos,conexion,ultimaFecha);
               
                //A quitar al fondeador
                if(!listaSaldosExcluidos.isEmpty()){
                    saldoIBSDAO.updateFondeoGarantia(listaSaldosExcluidos,conexion,ultimaFecha);
                    BitacoraSalidaCarteraDAO bitacoraSalidaCarteraDAO = new BitacoraSalidaCarteraDAO();
                    bitacoraSalidaCarteraDAO.addBitacoraSalida(listaSaldosExcluidos, idFondeador, 0, conexion);                    
                }
                
                
            }else{
                myLogger.info("Cartera completa");
                
            }            
            //Guardar fecha de ejecucion
            CatalogoDAO catalogoDao= new CatalogoDAO();
            catalogoDao.updateParametroFondeador("FECHA_CIERRE_FONDEADOR",idFondeador, ultimaFecha, conexion);
            
            String cSsaldoGarantizado = new DecimalFormat(ClientesConstants.FORMATO_MONTO).format( saldoAcumulado); 
            catalogoDao.updateParametroFondeador("SALDO_GARANTIZADO",idFondeador, cSsaldoGarantizado , conexion);
           }else{
                myLogger.info("Ya se ha ejecutado el cierre FONDEADOR "+idFondeador+" para la fecha de hoy");                
            } 
        } catch (Exception ex) {
            myLogger.error("Exception", ex);
        }
	
    }
    
    public static Map<Integer,Double> inicializaMapaSaldoEstados(int numEstados){
        Map <Integer, Double> mapa = new HashMap<Integer, Double>();
        for(int i=1; i<=numEstados ;i++){
            mapa.put(i, 0.0);
        }
        return mapa;
    }

    public static void displayMapSaldoByEstado(Map<Integer,Double> mapa){
    
         for(Map.Entry<Integer,Double> entry: mapa.entrySet()){
                    myLogger.info(entry.getKey() + "/" + entry.getValue());
                }
    }
    
    public static void displayListaSaldos(List<SaldoIBSVO> listaSaldos){
    
         for(SaldoIBSVO saldo: listaSaldos){
                    myLogger.info("PK: "+saldo.getIdClienteSICAP()+" - "+saldo.getIdSolicitudSICAP());
                    myLogger.info("Estado: "+saldo.getIdEstado());
                    myLogger.info("Saldo: "+saldo.getSaldoCapital());
                    
                }
    }
    
    public static boolean mandaCorreoCarteraIncompleta(int idFondeador,double saldoAcumulado,double montoGarantia, String destinatarios){
        
        MailUtil mail = new MailUtil();
        String asunto="";
        String msg="";
        String nombreFondeador="";
        
        switch(idFondeador){
            case ClientesConstants.ID_FONDEADOR_BURSA:
                nombreFondeador="BURSA";
                break;
            case ClientesConstants.ID_FONDEADOR_ABC:
                nombreFondeador="ABC";
                break;
            case ClientesConstants.ID_FONDEADOR_BAJIO:
                nombreFondeador="Bajío";
                break;
            case ClientesConstants.ID_FONDEADOR_BXMAS:
                nombreFondeador="BX+";
                break;
            case ClientesConstants.ID_FONDEADOR_ACTINVER:
                nombreFondeador="Actinver";
                break;
            default:
                nombreFondeador=Integer.toString(idFondeador);
                break;
        } 
        asunto="Cartera incompleta Fondeador " + nombreFondeador;
            
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        
        msg="La cartera garantizada no se completó para el Fondeador "+nombreFondeador+" "
                + "\n\nEl saldo acumulado es de: "+formatter.format(saldoAcumulado)
                +" de "+formatter.format(montoGarantia)+ " para garantizar cartera.";
        
        return mail.enviaCorreo(asunto, msg, destinatarios==null?"ramsses.solis@axxis.com.mx":destinatarios, 1 );
    
    }
    
    
    public static boolean isSaldoCandidatoValido(SaldoIBSVO saldo, int idFondeadorReglas){
        boolean esValido=false;
        
        switch(idFondeadorReglas){
            case ClientesConstants.ID_FONDEADOR_CREDITO_REAL:
                esValido=true;
                break;
            
            case ClientesConstants.ID_FONDEADOR_BURSA:
                //Validacion bursa->Dias Mora, Num Pagos, Porcentaje por estado, Tasa y Seguro completo
                esValido=validaDiasMora(saldo, idFondeadorReglas) 
                        && validaNumPagos(saldo, idFondeadorReglas) 
                        && validaPorcentajePorEstadoSuc(saldo, idFondeadorReglas) 
                        && validaTasa(saldo, idFondeadorReglas) 
                        && validaSeguroCompleto(saldo);//
                break;
            case ClientesConstants.ID_FONDEADOR_ABC:
                //Validacion ABC ->Dias Moras, Dias Vigencia
                esValido=validaDiasMora(saldo, idFondeadorReglas) && validaDiasVigencia(saldo, idFondeadorReglas);
                break;
            case ClientesConstants.ID_FONDEADOR_BAJIO:
            case ClientesConstants.ID_FONDEADOR_BXMAS:
            case ClientesConstants.ID_FONDEADOR_ACTINVER:
                //Validacion generica -> Dias Mora
                esValido=validaDiasMora(saldo, idFondeadorReglas);
                break;
            default:
                
                break;
        } 
        
        return esValido;    
    }
    
    /**
     * Metodo que valida dias de mora
     * en cuenta si afecta o no el total del Monto garantizado
     * @param saldo
     * @param idFondeador
     * @return 
     */
    public static boolean validaDiasMora(SaldoIBSVO saldo, int idFondeador){
        boolean valido = false;
        int numDiasMora = 0;
        try {
            //Traer parametro : DIAS MORA
            numDiasMora = Convertidor.stringToInt(CatalogoHelper.getParametroFondeador("DIAS_MORA_FONDEADOR",idFondeador));
            valido = saldo.getDiasMora() < numDiasMora;
            myLogger.info("validaDiasMora: "+valido);
            
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(FondeadorUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valido;
    }
    
    public static boolean validaNumPagos(SaldoIBSVO saldo, int idFondeador){
        boolean valido = false;
        int numPagosMinimo = 0;
        try {
            //Traer parametro : NUM PAGOS MINIMOS
            numPagosMinimo = Convertidor.stringToInt(CatalogoHelper.getParametroFondeador("NUM_PAGOS_MINIMOS_FONDEADOR",idFondeador));
            valido = saldo.getNumeroCuotas() >= numPagosMinimo;
            myLogger.info("validaNumPagos: "+valido);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(FondeadorUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valido;
    }
    
    public static boolean validaDiasVigencia(SaldoIBSVO saldo, int idFondeador){
        boolean valido = false;
        int numDiasVigencia = 0;
        try {
            //Traer parametro : NUM PAGOS MINIMOS
            numDiasVigencia = Convertidor.stringToInt(CatalogoHelper.getParametroFondeador("NUM_DIAS_VIGENCIA_FONDEADOR",idFondeador));
            //datediff(ib_fecha_vencimiento,ib_fecha_generacion) > ? 
            valido = FechasUtil.inBetweenDays(saldo.getFechaGeneracion(), saldo.getFechaVencimiento())  > numDiasVigencia;
            myLogger.info("validaDiasVigencia: "+valido);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(FondeadorUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valido;
    }
    
    public static boolean validaPorcentajePorEstadoSuc(SaldoIBSVO saldo, int idFondeador){
        boolean valido = false;
        
        double saldoBursaGarantizado=0;//Este viene de parámetros
        double porcentajeGarantiaBursa=0;//.20 viene tambien de parametros
        double sumaSaldoEstado=0;//Este es de la suma viene de la Base de datos para el esttado y fondeador asociado
        double saldoInsoluto15pc,saldoInsoluto30pc;
        
        int llaveEdo=0;
        
        SaldoIBSDAO saldoIBSDAO = new SaldoIBSDAO();
        try {
            
            
            //1.- Traer parametros : 
            saldoBursaGarantizado = Convertidor.stringToDouble(CatalogoHelper.getParametroFondeador("SALDO_FONDEADOR",idFondeador));
            porcentajeGarantiaBursa = Convertidor.stringToDouble(CatalogoHelper.getParametroFondeador("PORCENTAJE_GARANTIA_FONDEADOR",idFondeador));
            saldoInsoluto15pc=0.15*( saldoBursaGarantizado*(1+porcentajeGarantiaBursa) );
            saldoInsoluto30pc=0.30*( saldoBursaGarantizado*(1+porcentajeGarantiaBursa) );
            
            //2.- Identificar estado(Geografico de la sucursal) del saldo
            llaveEdo = saldoIBSDAO.getIdEstadoSaldo(saldo);
            
            //4.- Traer saldo por estado
            
            //3.- Ver si aplica expecion o no
            boolean excepcionEstados=llaveEdo==15 || llaveEdo==9;
            double saldoMax  = excepcionEstados?saldoInsoluto30pc:saldoInsoluto15pc;//15% o 30%
            
            //4.- Traer suma saldo por estado y fondeador
            if(excepcionEstados){
                //Sumar saldos de DF y EDOMEX
                double saldoEdo15 = saldoIBSDAO.getSumSaldosByEstadoAndFondeador(15, idFondeador);
                double saldoEdo9 = saldoIBSDAO.getSumSaldosByEstadoAndFondeador(9, idFondeador);
                sumaSaldoEstado=saldoEdo15+saldoEdo9;
            }else{
                //Trae solo saldo del Estado asociado
                sumaSaldoEstado=saldoIBSDAO.getSumSaldosByEstadoAndFondeador(llaveEdo, idFondeador);
            }
            
            valido =  (sumaSaldoEstado+saldo.getSaldoCapital()) <= saldoMax ;
            myLogger.info("validaPorcentajePorEstadoSuc: "+valido);
	    
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(FondeadorUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valido;
    }
    
    public static boolean validaTasa(SaldoIBSVO saldo, int idFondeador){
        boolean valido = false;
        double tasa = 0;
        try {
            //Traer parametro : TASA ELEGIBILIDAD
            tasa = Convertidor.stringToDouble(CatalogoHelper.getParametroFondeador("TASA_ELEGIBILIDAD_FONDEADOR",idFondeador));
            valido = saldo.getTasaInteresSinIVA() >= tasa;
            myLogger.info("validaTasa: "+valido);
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(FondeadorUtil.class.getName()).log(Level.SEVERE, null, ex);
        }
        return valido;
    }
    
    public static boolean validaSeguroCompleto(SaldoIBSVO saldo){
        boolean valido = false;
        CicloGrupalDAO cicloGrupalDao = new CicloGrupalDAO();
        try {
            CicloGrupalVO ciclo = cicloGrupalDao.getCiclo(saldo.getIdClienteSICAP(), saldo.getIdSolicitudSICAP());
            valido =   ciclo.seguroCompleto == 1;
            myLogger.info("validaSeguroCompleto: "+valido);
        } catch (ClientesException ex) {
            java.util.logging.Logger.getLogger(FondeadorUtil.class.getName()).log(Level.SEVERE, null, ex);
        }

        return valido;
    }
    /**
     * Metodo para enviar correo electronico cuando el pagare del fondeador esta
     * por vencer
     *
     * @param pagaresVencidos lista de pagares vencidos o por vencer
     *
     * @return <ul>
     * <li>true: envio de correo exitoso</li>
     * <li>false: ocurrio un error en el envio de correo</li>
     * </ul>
     */
    public static boolean enviaCorreoPagareVencido(List<AmortizacionPagareVO> pagaresVencidos) throws ClientesException {
        //obtenemos fecha actual del sistema
        SimpleDateFormat formateador = new SimpleDateFormat("dd-MM-yyyy");
       
        try {
            MailUtil mail = new MailUtil();
            String asunto = "Próximo vencimiento(s) de Pago(s) de Pagaré(s).";
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            
            StringBuilder msg = new StringBuilder();
            msg.append("Se informa que el o los siguientes pagos de Pagaré(s) están por vencer y/o están vencidos:\n");
            for(AmortizacionPagareVO ap : pagaresVencidos)
            {
                myLogger.debug("Pagare :"+ap.getIdPagare());

                String formatoFechaPago = formateador.format(ap.getFechaPago());

                msg.append("\n\nPagaré: "+ap.getIdPagare()+"\tNombre de Pagaré: "+ap.getNombrePagare()+"\n");
                msg.append("\nLínea de Crédito: " + ap.getNombreLineaCredito() + "\n");
                msg.append("\nNúmero de Pago: " + ap.getNumPago() + "\n");
                msg.append("\nFecha: " + formatoFechaPago + "\n");
                msg.append("\nCapital: " + formatter.format(ap.getCapital()) + "\n");
                msg.append("\nInterés: " + ap.getInteres().toUpperCase() + "\n");
                msg.append("\nIVA: " + formatter.format(ap.getIva()) + "\n\n");
                
            }
            msg.append( "\nSe sugiere realizar el pago correspondiente.");
            String destinatarios = CatalogoHelper.getParametro("MAIL_AMORT_PAGARE_RECIPIENT");       
            myLogger.debug("Destinatarios: " + destinatarios);
            return mail.enviaCorreo(asunto, msg.toString(), destinatarios == null ? "laura.mejia@axxis.com.mx" : destinatarios, 1);
        } catch (Exception ex) {
            myLogger.error("enviaCorreoPagareVencido()", ex);
        }
        return true;
    }

    public static boolean validaNombrebyId(String nombreFondeador, int idFondeador){
        boolean esValido=false;
        
        switch(idFondeador){
            case ClientesConstants.ID_FONDEADOR_BURSA:
                esValido=nombreFondeador.equals(ClientesConstants.NAME_FONDEADOR_BURSA);
                break;
            case ClientesConstants.ID_FONDEADOR_ABC:
                esValido=nombreFondeador.equals(ClientesConstants.NAME_FONDEADOR_ABC);
                break;
            case ClientesConstants.ID_FONDEADOR_BAJIO:
                esValido=nombreFondeador.equals(ClientesConstants.NAME_FONDEADOR_BAJIO);
                break;
            case ClientesConstants.ID_FONDEADOR_BXMAS:
                esValido=nombreFondeador.equals(ClientesConstants.NAME_FONDEADOR_BXMAS);
                break;
            case ClientesConstants.ID_FONDEADOR_ACTINVER:
                esValido=nombreFondeador.equals(ClientesConstants.NAME_FONDEADOR_ACTINVER);
                break;
            default:
                
                break;
        } 
        
        return esValido;    
    }
    //metodo actualizar Saldo Fondeador cuando se aplica pago a pagare
    //Naye
    public  int actualizaSaldoFondeadorPagare(SaldoFondeadorVO saldoFondeadorVo, double saldoFondeadorParam, Connection cn) throws ClientesDBException, SQLException
    {
        int respuesta = 0;
        SaldoFondeadorDAO saldoFondDAO = new SaldoFondeadorDAO();
        myLogger.debug("Saldo Fondeador Param"+saldoFondeadorParam);
        //double saldoFondeador = FormatUtil.formatSaldo(saldoFondeadorParam);
        NumberFormat num = new DecimalFormat("########0.##");
        myLogger.debug("Saldo Fondeador Param"+num.format(saldoFondeadorParam));
        boolean conexionAbierta = true;
        try 
        {
            if(cn == null){
                cn = getConnection();
                cn.setAutoCommit(false);
                conexionAbierta=false;
            }
            
            synchronized (this) 
            {
                double pagoCapital = saldoFondeadorVo.getMonto();
                myLogger.debug("Saldo inicial: " + pagoCapital);
                if (saldoFondeadorVo.tipoMov.equals(ClientesConstants.PAGO_FONDEADOR)) 
                {
                    if (saldoFondeadorParam > saldoFondeadorVo.getMonto()) 
                    {
                        myLogger.debug("Entra a disminucion de Saldo Fondeador");
                        saldoFondeadorVo.saldoFinal = saldoFondeadorParam - saldoFondeadorVo.monto;
                        myLogger.debug("Saldo Actual Fondeador: " + saldoFondeadorParam);
                        CatalogoHelper.updateParametro("SALDO_FONDEADOR_" + saldoFondeadorVo.numFondeador, num.format(saldoFondeadorVo.saldoFinal), cn);
                        saldoFondDAO.insertMovimiento(saldoFondeadorVo, cn);
                        respuesta = 1;
                    }
                    else if (saldoFondeadorVo.getMonto() < 0) 
                    {
                        myLogger.debug("Entra a suma de Saldo Fondeador");
                        saldoFondeadorVo.saldoFinal = saldoFondeadorParam + saldoFondeadorVo.monto;
                        myLogger.debug("Saldo Actual Fondeador: " + saldoFondeadorParam);
                        CatalogoHelper.updateParametro("SALDO_FONDEADOR_" + saldoFondeadorVo.numFondeador, num.format(saldoFondeadorVo.saldoFinal), cn);
                        saldoFondDAO.insertMovimiento(saldoFondeadorVo, cn);
                        respuesta = 1;
                    } 
                    else if (saldoFondeadorVo.numFondeador == ClientesConstants.ID_FONDEADOR_CREDITO_REAL) 
                    {
                        saldoFondeadorVo.saldoFinal = 0;
                        saldoFondDAO.insertMovimiento(saldoFondeadorVo, cn);
                        respuesta = 1;
                    } 
                    else 
                    { //saldo insuficiente
                        myLogger.debug("Saldo de fondeador insufiente");
                        respuesta = 2;
                    }
                } 
            }
            if(!conexionAbierta){
                cn.commit();
            }
            
        } 
        catch (Exception e) 
        {
            myLogger.info("Entra al catch");
            myLogger.error("actualizaSaldoFondeadorPagare: ", e);
            try 
            {
                if(!conexionAbierta){
                    cn.rollback();
                }
            } 
            catch (Exception ce) 
            {
                myLogger.info("Error al realizar el Commit");
                throw new ClientesDBException(ce.getMessage());
            }
            throw new ClientesDBException(e.getMessage());
        } 
        finally 
        {
            myLogger.info("Enviando respuesta");
            try 
            {
                if(!conexionAbierta){
                    cn.close();
                }
            } 
            catch (Exception ce) 
            {
                myLogger.info("Error al realizar el Commit");
                throw new ClientesDBException(ce.getMessage());
            }
        }
        return respuesta;
    }
    

    public static void registraPerfilContableCartera(List <SaldoIBSVO> lstSaldos, Connection conCart,String tipoTransaccion,boolean cerrarCon,int idFondeadorTrans ) throws ClientesException, Exception{
    
        CreditoCartDAO creditoCartDAO = new CreditoCartDAO();
        ArrayList<RubrosVO> rubros;
        TransaccionesHelper transHelper = new TransaccionesHelper();
        java.sql.Date fechaUltimoCierre = transHelper.obtenerFechaUltimoCierre();
        for (SaldoIBSVO saldo : lstSaldos) {
            //Retrieve Credito a partir de saldo
            CreditoCartVO creditoCartVo = creditoCartDAO.getCreditoClienteSol(saldo.getIdClienteSICAP(), saldo.getIdSolicitudSICAP());
            
            //Obtenr semanas dispersadas para enviar como parametro y clacular intDeveng
            int semDisp;
            if(saldo.getNumeroCuotasTranscurridas() == saldo.getPlazo()){
                semDisp=saldo.getNumeroCuotasTranscurridas();
            }else{
                semDisp=saldo.getNumeroCuotasTranscurridas()+1;
            }
            myLogger.info("semDisp: "+semDisp);
            
            //Llenar rubrosVo por credito
            rubros = fillLstRubros(saldo, semDisp);//TODO validar si es con +1  o no
            
            
            //convertir lista a array
            RubrosVO[] rubrosArr = new RubrosVO[rubros.size()];
            rubrosArr = rubros.toArray(rubrosArr);
            //Usar helper para insercion
            transHelper.registraPerfilCarteraFondeador(conCart, creditoCartVo, rubrosArr, tipoTransaccion,cerrarCon,idFondeadorTrans, fechaUltimoCierre);
            
        }
    
    }
    
    //Lista de rubros para transaccion de perfil contable a partir de Saldo
    public static ArrayList<RubrosVO> fillLstRubros (SaldoIBSVO saldo, int semDisp ) throws ClientesException{
        
        ArrayList<RubrosVO> lstRubrosVO = new ArrayList<RubrosVO>();
        RubrosVO rubro = new RubrosVO();
        DevengamientoDAO  tablaDevnHelper = new DevengamientoDAO();
        
        //CAPITAL - saldoCapital
        rubro.tipoRubro = ClientesConstants.CAPITAL;
        rubro.monto = saldo.getSaldoCapital();//saldoCapital;
        rubro.status = ClientesConstants.RUBRO_VIGENTE;
        rubro.origen = 0;
        lstRubrosVO.add(rubro);
        
        //INTERES - saldoInteres
        rubro = new RubrosVO();
        rubro.tipoRubro = ClientesConstants.INTERES;
        rubro.monto = tablaDevnHelper.getIntPorDevengar(saldo.getIdClienteSICAP(),saldo.getCredito());
        rubro.status = ClientesConstants.RUBRO_POR_DEVENGAR;
        rubro.origen = 0;
        lstRubrosVO.add(rubro);
        
        rubro = new RubrosVO();
        rubro.tipoRubro = ClientesConstants.INTERES;
        rubro.monto = (tablaDevnHelper.getIntDevengado(saldo.getIdClienteSICAP(),saldo.getCredito())-saldo.getInteresNormalPagado());
        rubro.status = ClientesConstants.RUBRO_VIGENTE;
        rubro.origen = 0;
        lstRubrosVO.add(rubro);
        
        return lstRubrosVO;
    }

    /**
     * Actualiza el campo fondeador en las tablas d_saldos,d_credito y d_ciclos grupales
     * @param lstSaldos
     * @param con
     * @param conCart
     * @throws ClientesException 
     */
    
    //Podria ir en el metodo registraPerfilContableCartera del Fondeador que se queda
    public static void actualizaFondeadorOtrasTablas(List <SaldoIBSVO> lstSaldos, Connection con, Connection conCart, int idFondeador) throws ClientesException{
        
        SaldoIBSDAO saldoIBSDAO = new SaldoIBSDAO();
        CreditoCartDAO creditoCartDAO = new CreditoCartDAO();
        CicloGrupalDAO cicloGruplaDAO = new CicloGrupalDAO();
        for (SaldoIBSVO saldo : lstSaldos) {
            
            //Retrieve Credito y Ciclo  a partir de saldo
            CreditoCartVO creditoCartVo = creditoCartDAO.getCreditoClienteSol(saldo.getIdClienteSICAP(), saldo.getIdSolicitudSICAP());
            CicloGrupalVO cicloVO = cicloGruplaDAO.getCiclo(saldo.getIdClienteSICAP(), saldo.getIdSolicitudSICAP());
            
            //Se setea el nuevo fondeador en el objeto 
            //TODO Aqui cambia el fondeadro dependiendo quien entre 
            
            cicloVO.setFondeador(idFondeador);
            creditoCartVo.setFondeador(idFondeador);
            saldo.setFondeador(idFondeador);
            
            //Modificar medtodos paramanejo delcierre de conexcion  
            saldoIBSDAO.updateOtrosDatos(con,saldo.getIdClienteSICAP(), saldo.getIdSolicitudSICAP(),idFondeador, 0,false);
            creditoCartDAO.updateCreditoFondeador(conCart,creditoCartVo,false);
            cicloGruplaDAO.updateCiclo(con, cicloVO,false);
            
        }
    
    
    }
    
}
