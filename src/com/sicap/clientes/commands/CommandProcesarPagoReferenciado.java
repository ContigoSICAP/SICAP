package com.sicap.clientes.commands;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.CifraControlDAO;
import com.sicap.clientes.dao.IncidenciaSegurosDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.PagoDAO;
import com.sicap.clientes.dao.PagoReferenciadoDAO;
import com.sicap.clientes.dao.PagosExcedentesDAO;
import com.sicap.clientes.dao.ReferenciaGeneralDAO;
import com.sicap.clientes.dao.RegistroPagosSegurosDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.SaldoT24DAO;
import com.sicap.clientes.dao.SegurosDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.dao.cartera.PagoManualDAO;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.helpers.ArchivosAsociadosHelper;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SegurosUtil;
import com.sicap.clientes.vo.CifraControlVO;
import com.sicap.clientes.vo.IncidenciaSegurosVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.PagoExcedenteVO;
import com.sicap.clientes.vo.PagoGrupalVO;
import com.sicap.clientes.vo.PagoVO;
import com.sicap.clientes.vo.ReferenciaGeneralVO;
import com.sicap.clientes.vo.RegistroPagosSegurosVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.SaldoT24VO;
import com.sicap.clientes.vo.SegurosVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.jspsmart.upload.File;
import com.sicap.clientes.dao.CarteraTCIDAO;
import com.sicap.clientes.dao.CuentasBancariasDAO;
import com.sicap.clientes.dao.cartera.CreditoCartDAO;
import com.sicap.clientes.helpers.cartera.EventoHelper;
import com.sicap.clientes.helpers.cartera.TransaccionesHelper;
import com.sicap.clientes.util.FechasUtil;
import com.sicap.clientes.vo.CuentaBancariaVO;
import com.sicap.clientes.vo.cartera.CreditoCartVO;
import org.apache.log4j.Logger;

public class CommandProcesarPagoReferenciado {

    private static Logger myLogger = Logger.getLogger(CommandBuscaEjecutivos.class);

    int totalRegProcesados;
    ReferenciaGeneralDAO[] totalReferencias = null;
    ReferenciaGeneralDAO referenciaDAO = new ReferenciaGeneralDAO();
    SegurosUtil segurosUtil = new SegurosUtil();
    SegurosDAO segurosDAO = new SegurosDAO();
    SegurosVO seguroVO = null;
    ArrayList<PagoVO> totalPagosArray = new ArrayList<PagoVO>();
    int caso1 = 0, caso2 = 0, caso3 = 0, caso4 = 0, caso5 = 0, caso6 = 0, caso7 = 0, caso8 = 0, caso9 = 0, caso10 = 0, caso11 = 0, caso12 = 0, caso13 = 0, caso14 = 0;
    IncidenciaSegurosDAO incidenciasSegDAO = new IncidenciaSegurosDAO();
    IncidenciaSegurosVO incidenciaVO = new IncidenciaSegurosVO();
    ArrayList<PagoVO> incidenciasArreglo = new ArrayList<PagoVO>();
//	SaldoT24VO saldo = null;
    SaldoIBSVO saldo = null;
//	SaldoT24DAO saldosT24 = new SaldoT24DAO();
    SaldoIBSDAO saldosT24 = new SaldoIBSDAO();
    CifraControlDAO cifraControlDAO = new CifraControlDAO();
    // total de registros que cumplen con pago
    ReferenciaGeneralDAO[] totalSaldos = null;
    // total de referencias que cuentan con seguro
    ReferenciaGeneralDAO[] totalAsegurados = null;
    Convertidor Conv = new Convertidor();

    public String getTipoArchivo(String nombreArchivo) {
        String tipoDocumento = "";
        if (nombreArchivo.substring(0, 4).equals("BNTE")) {
            tipoDocumento = "BNTE";
        } else if (nombreArchivo.substring(0, 4).equals("HSBC")) {
            tipoDocumento = "HSBC";
        } else if (nombreArchivo.substring(0, 4).equals("BMER")) {
            tipoDocumento = "BMER";
        } else if (nombreArchivo.substring(0, 4).equals("BSFI")) {
            tipoDocumento = "BSFI";
        } else if (nombreArchivo.substring(0, 4).equals("AFME")) {
            tipoDocumento = "AFME";
        } else if (nombreArchivo.substring(0, 3).equals("IBS")) {
            tipoDocumento = "IBS";
        } else if (nombreArchivo.substring(0, 4).equals("COMA")) {
            tipoDocumento = "COMA";
        } else if (nombreArchivo.substring(0, 7).equals("BANAMEX")) {
            tipoDocumento = "BANAMEX";
        } else if (nombreArchivo.substring(0, 6).equals("SCOTIA")) {
            tipoDocumento = "SCOTIA";
        } else if (nombreArchivo.substring(0, 5).equals("BAJIO")) {
            tipoDocumento = "BAJIO";
        } else if (nombreArchivo.substring(0, 5).equals("COMER")) {
            tipoDocumento = "COMER";
        } else if (nombreArchivo.substring(0, 4).equals("OXXO")) {
            tipoDocumento = "OXXO";
        } else if (nombreArchivo.substring(0, 7).equals("TELECOM")) {
            tipoDocumento = "TELECOM";
        } else if (nombreArchivo.substring(0, 5).equals("FBENA")) {
            tipoDocumento = "FBENA";
        } else if (nombreArchivo.substring(0, 4).equals("FABC")) {
            tipoDocumento = "FABC";
        } else if (nombreArchivo.substring(0, 6).equals("FRESKO")) {
            tipoDocumento = "FRESKO";
        } else if (nombreArchivo.substring(0, 9).equals("SANTANDER")) {
            tipoDocumento = "SANTANDER";
        } else if (nombreArchivo.substring(0, 7).equals("OPENPAY")) {
            tipoDocumento = "OPENPAY";
        } else if (nombreArchivo.substring(0, 7).equals("SORIANA")) {
            tipoDocumento = "SORIANA";
        } else if (nombreArchivo.substring(0, 7).equals("CASALEY")) {
            tipoDocumento = "CASALEY";
        } else {
            tipoDocumento = "Vale";
        }
        return tipoDocumento;
    }

    public double getPrimaSeguro(String numReferencia) {

        ReferenciaGeneralVO refVo = getReferencia(numReferencia);
        double prima = 0.0;
        try {
            if (GrupoUtil.esPagoGrupal(numReferencia)) {
                prima = SegurosUtil.getPrimaSeguroGrupal(numReferencia);
            } else {
                seguroVO = segurosDAO.getSeguros(refVo.numcliente, refVo.numSolicitud);
                if (seguroVO != null) {
                    prima = seguroVO.prima;
                    // if(seguroVO.saldoInsoluto==0)
                    // prima = 0.0;										    
                }
            }
        } catch (ClientesException e) {
            // TODO Auto-generated catch block
            myLogger.error("Error en getPrimaSeguro", e);
        }
        return prima;

    }

    //public double getSaldoInsolutoAlCompromiso(String numReferencia) throws ClientesException {
    public double getSaldoInsolutoAlCompromiso(SaldoIBSVO saldo) throws ClientesException {
        double totalVigente = -1;
        double saldoVencido = -1;
        double complemento = -1;
        if (saldo != null) {
            myLogger.debug("saldo: " + saldo.toString());
            saldoVencido = saldo.getSaldoTotalAlDia();
            complemento = saldo.getCapitalSigAmortizacion() + saldo.getInteresSigAmortizacion();;
            if (complemento > saldo.getSaldoTotalAlDia()) {
                complemento = saldo.getSaldoTotalAlDia();
            }
            totalVigente = saldoVencido + complemento;
        }
        return totalVigente;
    }

    // Se apunta a saldos JBL JUN/10
//	public SaldoT24VO verificaLiquidadoCancelado(String numReferencia) throws ClientesException{
    public SaldoIBSVO verificaLiquidadoCancelado(String numReferencia) throws ClientesException {

        myLogger.debug("Verificando Liquidado o Cancelado");
        ReferenciaGeneralVO refLC = getReferencia(numReferencia);
//		SaldoT24VO saldoLC = new SaldoT24VO();
//		SaldoT24DAO saldosdaoLC = new SaldoT24DAO();
        SaldoIBSVO saldoLC = new SaldoIBSVO();
        SaldoIBSDAO saldosdaoLC = new SaldoIBSDAO();
        saldoLC = saldosdaoLC.getSaldo(refLC.numcliente, refLC.numSolicitud, refLC.referencia);
        return saldoLC;

    }

    public ReferenciaGeneralVO getReferencia(String numReferencia) {
        ReferenciaGeneralVO refVo = referenciaDAO.getReferenciaGeneral(numReferencia);
        return refVo;
    }

    public int getTotalPagosValidosVALE(String lines[]) {
        String[] fragLinea = null;
        int total = 0;
        for (int j = 1; j <= lines.length - 1; j++) {
            if (lines[j] != null && !lines[j].trim().equalsIgnoreCase("")) {
                fragLinea = lines[j].split(",");
                if (fragLinea[1] != null && !fragLinea[1].trim().equalsIgnoreCase("")) {
                    String ref = fragLinea[1];
                    if (ref.length() == 13) {
                        total++;
                    }
                }
            }
        }
        return total;

    }

    public int getTotalPagosValidosHSBC(String lines[]) {
        String[] fragLinea = null;
        int total = 0;
        for (int j = 1; j <= lines.length - 1; j++) {
            if (lines[j] != null && !lines[j].trim().equalsIgnoreCase("")) {
                fragLinea = lines[j].split("\t");
                if (fragLinea[1] != null && !fragLinea[1].trim().equalsIgnoreCase("")) {
                    String ref = fragLinea[12];
                    ref = ref.trim().replace(" ", "");

                    if (fragLinea[4].substring(0, 5).equals("ABONO")) {
                        total++;

                    }
                }
            }
        }
        return total;

    }

    public int getTotalPagosValidosBMER(String lines[]) {
        String[] fragLinea = null;
        int total = 0;
        for (int j = 1; j <= lines.length - 1; j++) {
            if (lines[j] != null && !lines[j].trim().equalsIgnoreCase("")) {
                fragLinea = lines[j].split("\t");

                if (fragLinea[1] != null && !fragLinea[1].trim().equalsIgnoreCase("")) {

                    myLogger.debug("Existe alguna referencia, cargo o abono");
                    if (fragLinea[3] != null && !fragLinea[3].trim().equals("")) {
                        total++;
                    }
                }
            }
        }
        return total;

    }

    public int getTotalPagosValidosAFME(String lines[]) {
        String[] fragLinea = null;
        int total = 0;
        for (int j = 2; j <= lines.length - 2; j++) {
            if (lines[j] != null && !lines[j].trim().equalsIgnoreCase("")) {
                fragLinea = lines[j].split(",");

                if (fragLinea[0] != null && !fragLinea[0].trim().equalsIgnoreCase("")) {

                    myLogger.debug("Existe alguna referencia, cargo o abono");
                    if (fragLinea[4] != null && !fragLinea[4].trim().startsWith("0.0")) {
                        total++;

                    }
                }
            }
        }
        myLogger.debug("Total lineas" + total);
        return total;

    }

    public void addIncidenciasSeguros(Date fechaMov, String referencia, String observacion, int bancoReferencia) {
        ReferenciaGeneralVO refVo = getReferencia(referencia);
        IncidenciaSegurosVO incidencia = new IncidenciaSegurosVO();
        incidencia.fechaMovimiento = fechaMov;
        incidencia.idCliente = refVo.numcliente;
        incidencia.referencia = referencia;
        incidencia.observaciones = observacion;
        incidencia.bancoReferencia = bancoReferencia;

        try {

            incidenciasSegDAO.addIncidencias(incidencia);
        } catch (ClientesException e) {
            // TODO Auto-generated catch block
            myLogger.error("Error en addIncidenciasSeguros", e);
        }

    }
    //  PARSEO ARCHIVO BANORTE

    public PagoVO[] procesarArchivoBNTE(File archivo, ArrayList<PagoVO> incidenciasArray, int numCuenta) throws Exception {

        PagoReferenciadoDAO pagoRefDAO = new PagoReferenciadoDAO();
        String linea = archivo.getContentString();
        String lines[] = linea.split("\n");
        com.sicap.clientes.vo.PagoVO[] totalPagos = null;
        int contadorTotalPagos = 0;

        //Obtengo Fecha Banorte de Encabrzado
        String annio = lines[0].substring(7, 11).toString();
        String mes = lines[0].substring(11, 13).toString();
        String dia = lines[0].substring(13, 15).toString();

        String fecha = dia + "/" + mes + "/" + annio;
        myLogger.debug(fecha);

        //   fechaPago= Convertidor.stringToSqlDate(fecha);
        totalPagos = new com.sicap.clientes.vo.PagoVO[lines.length - 2];

        for (int j = 1; j <= totalPagos.length; j++) {
            com.sicap.clientes.vo.PagoVO pago = new com.sicap.clientes.vo.PagoVO();
            //Obtengo la referencia Banorte
            //pago.referencia = lines[j].substring(96, 109).trim().toString();
            pago.referencia = lines[j].substring(70, 84).trim().toString();
            //Obtengo la referencia Banorte
            pago.fechaPago = Convertidor.stringToSqlDate(fecha);
            //Obtengo Monto Bruto, de Archivo Banorte
            //pago.monto = Convertidor.stringToDouble(lines[j].substring(26, 40).toString() + "." + lines[j].substring(40, 42).toString());
            pago.monto = Convertidor.stringToDouble(lines[j].substring(27, 38).toString() + "." + lines[j].substring(38, 40).toString());
            //Establezo tipo 2-Banorte
            pago.bancoReferencia = 2;
            pago.numCuenta = numCuenta;

            //Se Verifica si cumple con longitud
            if (pago.referencia.length() == 13) {
                totalPagos[contadorTotalPagos] = pago;
                contadorTotalPagos++;
            } else {
                //En caso de No cumplir longitud, causa incidencia
                incidenciasArray.add(pago);
                pagoRefDAO.addIncidencia(pago, 2, "Referencia No Valida ");
                caso1++;
                pago.status = 1;

            }
            myLogger.debug("Referencia: " + pago.referencia);
            myLogger.debug("Fecha de Pago: " + Convertidor.stringToSqlDate(fecha));
            myLogger.debug("Importe: " + Convertidor.stringToDouble(lines[j].substring(26, 40).toString() + "." + lines[j].substring(40, 42).toString()));

            myLogger.debug("Hora: " + lines[j].substring(241, 247).toString());
        }

        myLogger.debug("TERMINO DE PROCESAMIENTO BANORTE...");
        return totalPagos;
    }

    // PARSEO ARCHIVO BANSEFI
    public PagoVO[] procesarArchivoBSFI(File archivo, ArrayList<PagoVO> incidenciasArray, int numCuenta) throws Exception {

        String linea = archivo.getContentString();
        String lines[] = linea.split("\n");
        PagoVO[] totalPagos = new PagoVO[lines.length - 2];
        int contadorTotalPagos = 0;
        myLogger.debug("lines:" + lines.length);

        for (int i = 0; i < lines.length; i++) {
            myLogger.debug("lines:" + i + " : " + lines[i]);
        }

        for (int j = 1; j <= totalPagos.length; j++) {

            //String referenciafechamonto = lines[j].substring(31, 67);
            PagoVO pago = new PagoVO();

            //	FECHA
            String annio = lines[j].substring(43, 47).toString();
            String mes = lines[j].substring(47, 49).toString();
            String dia = lines[j].substring(49, 51).toString();
            String fecha = dia + "/" + mes + "/" + annio;

            pago.referencia = lines[j].substring(30, 43).trim().toString();
            myLogger.debug("REFERENCIA BANSEFI: " + pago.referencia);
            pago.monto = Convertidor.stringToDouble(lines[j].substring(51, 64).toString() + "." + lines[j].substring(64, 66).toString());
            pago.fechaPago = Convertidor.stringToSqlDate(fecha);
            //Tipo Banco BANSEFI
            pago.bancoReferencia = 5;
            pago.numCuenta = numCuenta;

            totalPagos[contadorTotalPagos] = pago;
            contadorTotalPagos++;

            myLogger.debug("PAGOS BANSEFI:" + pago.toString());

        }
        return totalPagos;

    }

    //PARSEP ARCHIVO IBS
    public PagoVO[] procesarArchivoIBS(File archivo, ArrayList<PagoVO> incidenciasArray) throws Exception {

        PagoReferenciadoDAO pagoRefDAO = new PagoReferenciadoDAO();
        String linea = archivo.getContentString();
        String lines[] = linea.split("\n");
        com.sicap.clientes.vo.PagoVO[] totalPagos = null;
        int contadorTotalPagos = 0;

        totalPagos = new com.sicap.clientes.vo.PagoVO[lines.length];

        for (int j = 0; j < totalPagos.length; j++) {
            PagoVO pago = new PagoVO();
            //Obtengo la referencia, cambio layout archivo JBL JUN-10
//			pago.referencia = lines[j].substring(26, 39) ;
            pago.referencia = lines[j].substring(8, 21);
            //Obtengo la fecha
            pago.fechaPago = Convertidor.stringToSqlDate(lines[j].substring(6, 8) + "/" + lines[j].substring(4, 6) + "/" + lines[j].substring(0, 4));
            //Obtengo Monto Bruto, cambio pocisiones por nuevo archivo JBL JUN-10
//			pago.monto = Convertidor.stringToDouble(lines[j].substring(40, 52).toString()+"."+lines[j].substring(52, 54).toString());
            pago.monto = Convertidor.stringToDouble(lines[j].substring(21, 34).toString() + "." + lines[j].substring(34, 36).toString());
            //Establezo referencia idbanco tipo sicap cambio pocisiones por nuevo archivo JBL JUN-10
//			pago.bancoReferencia = CatalogoHelper.getIDBancoReferenciaSICAP(lines[j].substring(22, 26));	
            pago.bancoReferencia = CatalogoHelper.getIDBancoReferenciaSICAP(lines[j].substring(36, 40));
            //pago.numCuenta = numCuenta;
            System.out.println("lines " + lines[j].substring(40, 42));
            pago.numCuenta = Convertidor.stringToInt(lines[j].substring(40, 42));

            //Se Verifica si cumple con longitud
            if (pago.referencia.length() == 13) {
                totalPagos[contadorTotalPagos] = pago;
                contadorTotalPagos++;
            } else {
                //En caso de No cumplir longitud, causa incidencia
                incidenciasArray.add(pago);
                pagoRefDAO.addIncidencia(pago, 2, "Referencia No Valida ");
                caso1++;
                pago.status = 1;

            }
            myLogger.debug("Referencia: " + pago.referencia);
            myLogger.debug("Fecha de Pago: " + Convertidor.stringToSqlDate(lines[j].substring(6, 8) + "/" + lines[j].substring(4, 6) + "/" + lines[j].substring(0, 4)));
            myLogger.debug("Importe: " + Convertidor.stringToDouble(lines[j].substring(21, 34).toString() + "." + lines[j].substring(34, 36).toString()));

//			Logger.debug("Hora: "
//					+ lines[j].substring(16, 22).toString());
        }

        myLogger.debug("TERMINO DE PROCESAMIENTO IBS...");
        return totalPagos;
    }

    // PARSEO ARCHIVO HSBC 
    public PagoVO[] procesarArchivoHSBC(File archivo, ArrayList<PagoVO> incidenciasArray, int numCuenta) throws Exception {
        String linea = archivo.getContentString();
        String lines[] = linea.split("\n");
        com.sicap.clientes.vo.PagoVO[] totalPagos = null;
        int contadorTotalPagos = 0;
        SaldoIBSDAO ibsDao = new SaldoIBSDAO();
        TreeMap catBancos = CatalogoHelper.getCatalogoBancosIBS_CWin();
        String fragLinea[];
        myLogger.debug("lines:" + lines.length);
        totalPagos = new com.sicap.clientes.vo.PagoVO[getTotalPagosValidosHSBC(lines)];

        for (int j = 1; j <= lines.length - 1; j++) {
            if (lines[j] != null && !lines[j].trim().equalsIgnoreCase("")) {
                fragLinea = lines[j].split("\t");

                if (fragLinea[1] != null
                        && !fragLinea[1].trim().equalsIgnoreCase("")) {
                    // Columna relacionada con la columna de

                    String ref = fragLinea[12];
                    com.sicap.clientes.vo.PagoVO pago = new com.sicap.clientes.vo.PagoVO();
                    pago.referencia = ref.trim().replace(" ", "");
                    pago.monto = Double.parseDouble(fragLinea[6].toString());
                    pago.fechaPago = Convertidor.stringToSqlDate(fragLinea[1].toString());
                    pago.isCreditoIBS = ibsDao.isCreditoIBS(pago.referencia);
                    pago.bancoReferencia = (Integer) catBancos.get(Convertidor.stringToInt(fragLinea[14]));
                    pago.numCuenta = numCuenta;
                    myLogger.debug("Objeto Pago:" + pago.toString());
                    myLogger.debug("fragLinea[4]:" + fragLinea[4]);
                    myLogger.debug("ref.trim().replace()" + ref.trim().replace(" ", "").length());

                    //Se verifica si es un abono y cumple con longitud
                    if (fragLinea[4].substring(0, 5).equals("ABONO")) {
                        if (totalPagos.length > 0) {
                            totalPagos[contadorTotalPagos] = pago;
                        }
                        contadorTotalPagos++;
                    } else {
                        myLogger.debug("No es un Abono");
                    }

                }

            }
        }

        return totalPagos;
    }

    // PARSEO ARCHIVO BANCOMER 
    public PagoVO[] procesarArchivoBMER(File archivo, ArrayList<PagoVO> incidenciasArray, int numCuenta) throws Exception {

        String linea = archivo.getContentString();
        String lines[] = linea.split("\n");
        PagoVO[] totalPagos = new PagoVO[lines.length - 7];
        int contadorTotalPagos = 0;
        myLogger.debug("lines:" + lines.length);
        for (int j = 0; j <= totalPagos.length; j++) {
            if (lines[j].substring(0, 7).equals("1259822")) {
                PagoVO pago = new PagoVO();
                //	FECHA
                String annio = lines[j].substring(114, 118).toString();
                String mes = lines[j].substring(119, 121).toString();
                String dia = lines[j].substring(122, 124).toString();
                String fecha = dia + "/" + mes + "/" + annio;

                pago.referencia = lines[j].substring(7, 27).trim().toString();
                pago.monto = Convertidor.stringToDouble(lines[j].substring(90, 105).toString());
                pago.fechaPago = Convertidor.stringToSqlDate(fecha);
                //Tipo Banco BMER
                pago.bancoReferencia = ClientesConstants.ID_BANCO_BANCOMER;
                pago.numCuenta = numCuenta;
                totalPagos[contadorTotalPagos] = pago;
                contadorTotalPagos++;

                myLogger.debug("Referencia BMER: " + pago.referencia);
                myLogger.debug("Fecha de Pago: " + pago.fechaPago);
                myLogger.debug("Importe: " + pago.monto);
            }

        }
        return totalPagos;
    }

//	PARSEO ARCHIVO AFIRME
    public PagoVO[] procesarArchivoAFME(File archivo, ArrayList<PagoVO> incidenciasArray, int numCuenta) throws Exception {
        String linea = archivo.getContentString();
        String lines[] = linea.split("\n");
        PagoVO[] totalPagos = null;
        int contadorTotalPagos = 0;
        String fragLinea[];
        myLogger.debug("lines:" + lines.length);
        totalPagos = new PagoVO[getTotalPagosValidosAFME(lines)];

        for (int j = 2; j <= lines.length - 2; j++) {
            if (lines[j] != null && !lines[j].trim().equalsIgnoreCase("")) {
                fragLinea = lines[j].split(",");

                //Columna 4 están los abonos 
                if (fragLinea[4] != null
                        && !fragLinea[4].trim().startsWith("0.0")) {

                    String ref = fragLinea[0];
                    PagoVO pago = new PagoVO();

                    // El año viene de dos digitos en el archivo (08 en vez de 2008, agregar el "20"+08)
                    String fecha = fragLinea[1].substring(0, 6) + "20" + fragLinea[1].substring(6, 8);

                    //if (fragLinea[3]!=null && !fragLinea[3].equals("") ) {
                    int temp = ref.trim().length();
                    pago.referencia = ref.substring(temp - 13, temp);
                    pago.monto = Double.parseDouble(fragLinea[4].toString());
                    pago.fechaPago = Convertidor.stringToSqlDate(fecha.toString());
                    pago.bancoReferencia = 4;
                    pago.numCuenta = numCuenta;

                    if (totalPagos.length > 0) {
                        totalPagos[contadorTotalPagos] = pago;
                    }
                    contadorTotalPagos++;

                } else {
                    myLogger.debug("No es un Abono");
                }

            }
        }

        return totalPagos;
    }

//	PARSEO ARCHIVO COMA
    public PagoVO[] procesarArchivoCOMA(File archivo, ArrayList<PagoVO> inidenciasArray, int numCuenta) throws Exception {

        String linea = archivo.getContentString();
        String lines[] = linea.split("\n");
        PagoVO[] totalPagos = new PagoVO[lines.length - 2];
        int contadorTotalPagos = 0;
        myLogger.debug("lines:" + lines.length);

        for (int i = 0; i < lines.length; i++) {
            myLogger.debug("lines:" + i + " : " + lines[i]);
        }

        for (int j = 1; j <= totalPagos.length; j++) {

            //String referenciafechamonto = lines[j].substring(31, 67);
            PagoVO pago = new PagoVO();

            //	FECHA
            String annio = lines[j].substring(0, 4).toString();
            String mes = lines[j].substring(4, 6).toString();
            String dia = lines[j].substring(6, 8).toString();
            String fecha = dia + "/" + mes + "/" + annio;

            pago.referencia = lines[j].substring(8, 21).trim().toString();
            myLogger.debug("REFERENCIA COMA: " + pago.referencia);
            pago.monto = Convertidor.stringToDouble(lines[j].substring(22, 34).toString() + "." + lines[j].substring(34, 36).toString());
            pago.fechaPago = Convertidor.stringToSqlDate(fecha);
            pago.bancoReferencia = ClientesConstants.ID_BANCO_COMA;
            pago.numCuenta = numCuenta;

            totalPagos[contadorTotalPagos] = pago;
            contadorTotalPagos++;

            myLogger.debug("PAGOS COMA:" + pago.toString());

        }
        return totalPagos;
    }

    public PagoVO[] procesarArchivoBANAMEX(File archivo, ArrayList<PagoVO> inidenciasArray, int numCuenta) throws Exception {

        String linea = archivo.getContentString();
        String lines[] = linea.split("\n");
        PagoVO[] totalPagos = new PagoVO[lines.length - 1];
        int contadorTotalPagos = 0;
        myLogger.debug("lines:" + lines.length);
        String annio = "";
        String mes = "";
        String dia = "";

        for (int j = 6; j <= totalPagos.length; j++) {

            PagoVO pago = new PagoVO();
            linea = lines[j].toString();
            linea = linea.replace(",", "");
            linea = linea.replace("|", ",");
            String divisor[] = linea.split(",");
            System.out.println("divisor[] " + divisor.length);
            /*System.out.println("divisor.length "+divisor.length);
            System.out.println("divisor[0].toString() "+divisor[0].toString());
            System.out.println("divisor[1].toString() "+divisor[1].toString());
            System.out.println("divisor[2].toString() "+divisor[2].toString());
            System.out.println("divisor[3].toString() "+divisor[3].toString());
            System.out.println("divisor[4].toString() "+divisor[4].toString());
            System.out.println("divisor[5].toString() "+divisor[5].toString());
            System.out.println("divisor[6].toString() "+divisor[6].toString());
            System.out.println("divisor[7].toString() "+divisor[7].toString());*/
            if (divisor[3].toString().equals("15")) {
                dia = divisor[1].substring(0, 3).toString();
                mes = divisor[1].substring(3, 6).toString();
                annio = "20" + divisor[1].substring(6, 8).toString();
                pago.fechaPago = Convertidor.stringToSqlDate(dia + mes + annio);
                pago.referencia = divisor[7].toString();
                pago.monto = Convertidor.stringToDouble(divisor[8].toString().replace(",", ""));
                pago.bancoReferencia = ClientesConstants.ID_BANCO_BANAMEX;
                pago.numCuenta = numCuenta;
                totalPagos[contadorTotalPagos] = pago;
                contadorTotalPagos++;
            }

            myLogger.debug("PAGOS BANAMEX:" + pago.toString());

        }
        return totalPagos;
    }

    public PagoVO[] procesarArchivoSCOTIABANK(File archivo, ArrayList<PagoVO> inidenciasArray, int totalRegistros, int numCuenta) throws Exception {

        String linea = archivo.getContentString();
        String lines[] = linea.split("\n");
        PagoVO[] totalPagos = new PagoVO[totalRegistros];
        int contadorTotalPagos = 0;

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].substring(0, 1).trim().toString().equals("D")) {
                myLogger.debug("lines:" + i + " : " + lines[i]);
                PagoVO pago = new PagoVO();
                String fecha = lines[i].substring(45, 55).toString();
                //System.out.println("fecha "+fecha);
                pago.referencia = lines[i].substring(165, 178).trim().toString();
                //System.out.println("pago.referencia "+pago.referencia);
                pago.monto = Convertidor.stringToDouble(lines[i].substring(149, 162).toString() + "." + lines[i].substring(162, 164).toString());
                //System.out.println("pago.monto "+pago.monto);
                pago.fechaPago = Convertidor.stringToSqlDate(fecha);
                pago.bancoReferencia = ClientesConstants.ID_BANCO_SCOTIABANK;
                pago.numCuenta = numCuenta;

                totalPagos[contadorTotalPagos] = pago;
                contadorTotalPagos++;

                myLogger.debug("PAGOS SCOTIABANK:" + pago.toString());
            }
        }
        return totalPagos;
    }

    public PagoVO[] procesarArchivoBANBAJIO(File archivo, ArrayList<PagoVO> inidenciasArray, int totalRegistros, int numCuenta) throws Exception {

        String linea = archivo.getContentString();
        String lines[] = linea.split("\n");
        PagoVO[] totalPagos = new PagoVO[totalRegistros];
        int contadorTotalPagos = 0;
        String annio = "", mes = "", dia = "", fecha = "", monto = "";

        for (int i = 0; i < lines.length; i++) {
            if (lines[i].substring(0, 5).trim().toString().equals("0001;")) {
                myLogger.debug("lines:" + i + " : " + lines[i]);
                PagoVO pago = new PagoVO();
                annio = lines[i].substring(154, 158).toString();
                mes = lines[i].substring(151, 154).toString();
                dia = lines[i].substring(149, 151).toString();
                mes = FechasUtil.getNumeroMes(mes);
                fecha = dia + "/" + mes + "/" + annio;
                pago.referencia = lines[i].substring(61, 74).trim().toString();
                //System.out.println("pago.referencia "+pago.referencia);
                monto = lines[i].substring(133, 148).trim().toString();
                monto = monto.replace(",", "");
                pago.monto = Convertidor.stringToDouble(monto);
                //System.out.println("pago.monto "+pago.monto);
                pago.fechaPago = Convertidor.stringToSqlDate(fecha);
                pago.bancoReferencia = ClientesConstants.ID_BANCO_BANBAJIO;
                pago.numCuenta = numCuenta;

                totalPagos[contadorTotalPagos] = pago;
                contadorTotalPagos++;

                myLogger.debug("PAGOS BANBAJIO:" + pago.toString());
            }
        }
        return totalPagos;
    }

    public PagoVO[] procesarArchivoCOMEXICANA(File archivo, ArrayList<PagoVO> inidenciasArray, int numCuenta) throws Exception {

        String linea = archivo.getContentString();
        String lines[] = linea.split("\n");
        PagoVO[] totalPagos = new PagoVO[lines.length - 1];
        int contadorTotalPagos = 0;

        for (int j = 1; j <= totalPagos.length; j++) {

            PagoVO pago = new PagoVO();
            String annio = lines[j].substring(32, 36).toString();
            //System.out.println("annio "+annio);
            String mes = lines[j].substring(30, 32).toString();
            //System.out.println("mes "+mes);
            String dia = lines[j].substring(28, 30).toString();
            //System.out.println("dia "+dia);
            String fecha = dia + "/" + mes + "/" + annio;

            pago.referencia = lines[j].substring(3, 16).trim().toString();
            myLogger.debug("REFERENCIA COMEXICANA: " + pago.referencia);
            pago.monto = Convertidor.stringToDouble(lines[j].substring(16, 24).toString() + "." + lines[j].substring(24, 26).toString());
            //System.out.println("pago.monto "+pago.monto);
            pago.fechaPago = Convertidor.stringToSqlDate(fecha);
            //System.out.println("pago.fechaPago "+pago.fechaPago);
            pago.bancoReferencia = ClientesConstants.ID_BANCO_COM_MEXICANA;
            pago.numCuenta = numCuenta;

            totalPagos[contadorTotalPagos] = pago;
            contadorTotalPagos++;

            myLogger.debug("PAGOS COMEXICANA:" + pago.toString());

        }
        return totalPagos;
    }

    public PagoVO[] procesarArchivoOXXO(File archivo, ArrayList<PagoVO> inidenciasArray, int numCuenta) throws Exception {

        String linea = archivo.getContentString();
        String lines[] = linea.split("\n");
        PagoVO[] totalPagos = new PagoVO[lines.length];
        int contadorTotalPagos = 0;

        for (int j = 0; j < totalPagos.length; j++) {

            PagoVO pago = new PagoVO();
            String annio = lines[j].substring(52, 56).toString();
            //System.out.println("annio "+annio);
            String mes = lines[j].substring(56, 58).toString();
            //System.out.println("mes "+mes);
            String dia = lines[j].substring(58, 60).toString();
            //System.out.println("dia "+dia);
            String fecha = dia + "/" + mes + "/" + annio;

            pago.referencia = lines[j].substring(69, 82).trim().toString();
            myLogger.debug("REFERENCIA OXXO: " + pago.referencia);
            pago.monto = Convertidor.stringToDouble(lines[j].substring(119, 132).toString() + "." + lines[j].substring(133, 135).toString());
            //System.out.println("pago.monto "+pago.monto);
            pago.fechaPago = Convertidor.stringToSqlDate(fecha);
            //System.out.println("pago.fechaPago "+pago.fechaPago);
            pago.bancoReferencia = ClientesConstants.ID_BANCO_OXXO;
            pago.numCuenta = numCuenta;

            totalPagos[contadorTotalPagos] = pago;
            contadorTotalPagos++;

            myLogger.debug("PAGOS OXXO:" + pago.toString());

        }
        return totalPagos;
    }

    public PagoVO[] procesarArchivoTELECOM(File archivo, ArrayList<PagoVO> inidenciasArray, int numCuenta) throws Exception {

        String linea = archivo.getContentString();
        String lines[] = linea.split("\n");
        PagoVO[] totalPagos = new PagoVO[lines.length];
        int contadorTotalPagos = 0;

        for (int j = 1; j < totalPagos.length; j++) {

            PagoVO pago = new PagoVO();
            String annio = "20" + lines[j].substring(30, 32).toString();
            //System.out.println("annio "+annio);
            String mes = lines[j].substring(28, 30).toString();
            //System.out.println("mes "+mes);
            String dia = lines[j].substring(26, 28).toString();
            //System.out.println("dia "+dia);
            String fecha = dia + "/" + mes + "/" + annio;
            //System.out.println("fecha "+fecha);
            pago.referencia = lines[j].substring(12, 25).trim().toString();
            myLogger.debug("REFERENCIA TELECOM: " + pago.referencia);
            pago.monto = Convertidor.stringToDouble(lines[j].substring(50, 58).toString() + "." + lines[j].substring(58, 60).toString());
            //System.out.println("pago.monto "+pago.monto);
            pago.fechaPago = Convertidor.stringToSqlDate(fecha);
            //System.out.println("pago.fechaPago "+pago.fechaPago);
            pago.bancoReferencia = ClientesConstants.ID_BANCO_TELECOM;
            pago.numCuenta = numCuenta;

            totalPagos[contadorTotalPagos] = pago;
            contadorTotalPagos++;

            myLogger.debug("PAGOS TELECOM:" + pago.toString());

        }
        return totalPagos;
    }

    public PagoVO[] procesarArchivoFBENA(File archivo, ArrayList<PagoVO> inidenciasArray, int numCuenta) throws Exception {

        String linea = archivo.getContentString();
        String lines[] = linea.split("\n");
        PagoVO[] totalPagos = new PagoVO[lines.length - 2];
        int contadorTotalPagos = 0;
        myLogger.debug("lines:" + lines.length);

        for (int j = 1; j <= totalPagos.length; j++) {

            //String referenciafechamonto = lines[j].substring(31, 67);
            PagoVO pago = new PagoVO();

            //	FECHA
            String annio = lines[j].substring(0, 4).toString();
            String mes = lines[j].substring(4, 6).toString();
            String dia = lines[j].substring(6, 8).toString();
            String fecha = dia + "/" + mes + "/" + annio;

            pago.referencia = lines[j].substring(8, 21).trim().toString();
            myLogger.debug("REFERENCIA FBENA: " + pago.referencia);
            pago.monto = Convertidor.stringToDouble(lines[j].substring(22, 34).toString() + "." + lines[j].substring(34, 36).toString());
            pago.fechaPago = Convertidor.stringToSqlDate(fecha);
            pago.bancoReferencia = ClientesConstants.ID_BANCO_BENAVIDES;
            pago.numCuenta = numCuenta;

            totalPagos[contadorTotalPagos] = pago;
            contadorTotalPagos++;

            myLogger.debug("PAGOS FBENA:" + pago.toString());

        }
        return totalPagos;
    }

    public PagoVO[] procesarArchivoFABC(File archivo, ArrayList<PagoVO> inidenciasArray, int numCuenta) throws Exception {

        String linea = archivo.getContentString();
        String lines[] = linea.split("\n");
        PagoVO[] totalPagos = new PagoVO[lines.length - 2];
        int contadorTotalPagos = 0;
        myLogger.debug("lines:" + lines.length);

        for (int j = 1; j <= totalPagos.length; j++) {

            //String referenciafechamonto = lines[j].substring(31, 67);
            PagoVO pago = new PagoVO();

            //	FECHA
            String annio = lines[j].substring(0, 4).toString();
            String mes = lines[j].substring(4, 6).toString();
            String dia = lines[j].substring(6, 8).toString();
            String fecha = dia + "/" + mes + "/" + annio;

            pago.referencia = lines[j].substring(8, 21).trim().toString();
            myLogger.debug("REFERENCIA FABC: " + pago.referencia);
            pago.monto = Convertidor.stringToDouble(lines[j].substring(22, 34).toString() + "." + lines[j].substring(34, 36).toString());
            pago.fechaPago = Convertidor.stringToSqlDate(fecha);
            pago.bancoReferencia = ClientesConstants.ID_BANCO_FABC;
            pago.numCuenta = numCuenta;

            totalPagos[contadorTotalPagos] = pago;
            contadorTotalPagos++;

            myLogger.debug("PAGOS FABC:" + pago.toString());

        }
        return totalPagos;
    }

    public PagoVO[] procesarArchivoFRESKO(File archivo, ArrayList<PagoVO> inidenciasArray, int numCuenta) throws Exception {

        String linea = archivo.getContentString();
        String lines[] = linea.split("\n");
        PagoVO[] totalPagos = new PagoVO[lines.length - 1];
        int contadorTotalPagos = 0;

        for (int j = 1; j <= totalPagos.length; j++) {

            PagoVO pago = new PagoVO();
            String annio = lines[j].substring(32, 36).toString();
            //System.out.println("annio "+annio);
            String mes = lines[j].substring(30, 32).toString();
            //System.out.println("mes "+mes);
            String dia = lines[j].substring(28, 30).toString();
            //System.out.println("dia "+dia);
            String fecha = dia + "/" + mes + "/" + annio;

            pago.referencia = lines[j].substring(3, 16).trim().toString();
            myLogger.debug("REFERENCIA FRESKO: " + pago.referencia);
            pago.monto = Convertidor.stringToDouble(lines[j].substring(16, 24).toString() + "." + lines[j].substring(24, 26).toString());
            //System.out.println("pago.monto "+pago.monto);
            pago.fechaPago = Convertidor.stringToSqlDate(fecha);
            //System.out.println("pago.fechaPago "+pago.fechaPago);
            pago.bancoReferencia = ClientesConstants.ID_BANCO_FRESKO;
            pago.numCuenta = numCuenta;

            totalPagos[contadorTotalPagos] = pago;
            contadorTotalPagos++;

            myLogger.debug("PAGOS FRESKO:" + pago.toString());

        }
        return totalPagos;
    }

    public PagoVO[] procesarArchivoSantander(File archivo, ArrayList<PagoVO> inidenciasArray, int numCuenta) throws Exception {

        String linea = archivo.getContentString();
        String lines[] = linea.split("\n");
        PagoVO[] totalPagos = new PagoVO[lines.length];
        int contadorTotalPagos = 0;

        for (int j = 0; j < totalPagos.length; j++) {
            if (lines[j].substring(36, 50).toString().equals("DEP EN EFECTIV")) {
                PagoVO pago = new PagoVO();
                String annio = lines[j].substring(20, 24).toString();
                //System.out.println("annio "+annio);
                String mes = lines[j].substring(16, 18).toString();
                //System.out.println("mes "+mes);
                String dia = lines[j].substring(18, 20).toString();
                //System.out.println("dia "+dia);
                String fecha = dia + "/" + mes + "/" + annio;

                pago.referencia = lines[j].substring(113, 126).trim().toString();
                myLogger.debug("REFERENCIA Santander: " + pago.referencia);
                pago.monto = Convertidor.stringToDouble(lines[j].substring(77, 89).toString() + "." + lines[j].substring(89, 91).toString());
                //System.out.println("pago.monto "+pago.monto);
                pago.fechaPago = Convertidor.stringToSqlDate(fecha);
                //System.out.println("pago.fechaPago "+pago.fechaPago);
                pago.bancoReferencia = ClientesConstants.ID_BANCO_SANTANDER_NVO;
                pago.numCuenta = numCuenta;

                totalPagos[contadorTotalPagos] = pago;
                contadorTotalPagos++;
                myLogger.debug("PAGOS SANTANDER:" + pago.toString());
            }
        }
        return totalPagos;
    }

    public com.sicap.clientes.vo.PagoVO[] procesarArchivoVALE(File archivo,
            ArrayList<PagoVO> incidenciasArray) throws Exception {
        //Este Proceso para lectura de vale
        PagoReferenciadoDAO pagoRefDAO = new PagoReferenciadoDAO();
        String linea = archivo.getContentString();
        String lines[] = linea.split("\n");
        com.sicap.clientes.vo.PagoVO[] totalPagos = null;
        int contadorTotalPagos = 0;
        String fragLinea[];

        totalPagos = new com.sicap.clientes.vo.PagoVO[getTotalPagosValidosVALE(lines)];
        int totalIncidencias = 0;

        for (int j = 1; j <= lines.length - 1; j++) {
            if (lines[j] != null && !lines[j].trim().equalsIgnoreCase("")) {
                fragLinea = lines[j].split(",");

                if (fragLinea[1] != null
                        && !fragLinea[1].trim().equalsIgnoreCase("")) {

                    PagoVO pago = new com.sicap.clientes.vo.PagoVO();
                    String ref = fragLinea[1];
                    pago.referencia = ref;
                    pago.monto = Double.parseDouble(fragLinea[2].toString());
                    pago.fechaPago = Convertidor.stringToSqlDate(fragLinea[0].toString());
                    //El Tipo 3 es un vale
                    pago.tipo = "3";

                    if (ref.length() == 13) {
                        totalPagos[contadorTotalPagos] = pago;
                        contadorTotalPagos++;

                    } else {
                        pagoRefDAO.addIncidencia(pago, 1,
                                "Referencia No Valida o Cargo..");
                        incidenciasArray.add(pago);
                        totalIncidencias++;
                    }

                }

            }

        }

        return totalPagos;
    }

    public PagoVO[] procesarArchivoSORIANA(File archivo, ArrayList<PagoVO> inidenciasArray, int numCuenta) throws Exception {

        String linea = archivo.getContentString();
        String lines[] = linea.split("\n");
        PagoVO[] totalPagos = new PagoVO[lines.length - 1];
        int contadorTotalPagos = 0;

        for (int j = 1; j <= totalPagos.length; j++) {

            PagoVO pago = new PagoVO();
            String annio = lines[j].substring(32, 36).toString();
            //System.out.println("annio "+annio);
            String mes = lines[j].substring(30, 32).toString();
            //System.out.println("mes "+mes);
            String dia = lines[j].substring(28, 30).toString();
            //System.out.println("dia "+dia);
            String fecha = dia + "/" + mes + "/" + annio;

            pago.referencia = lines[j].substring(3, 16).trim().toString();
            myLogger.debug("REFERENCIA SORIANA: " + pago.referencia);
            pago.monto = Convertidor.stringToDouble(lines[j].substring(16, 24).toString() + "." + lines[j].substring(24, 26).toString());
            //System.out.println("pago.monto "+pago.monto);
            pago.fechaPago = Convertidor.stringToSqlDate(fecha);
            //System.out.println("pago.fechaPago "+pago.fechaPago);
            pago.bancoReferencia = ClientesConstants.ID_BANCO_SORIANA;
            pago.numCuenta = numCuenta;

            totalPagos[contadorTotalPagos] = pago;
            contadorTotalPagos++;

            myLogger.debug("PAGOS SORIANA:" + pago.toString());

        }
        return totalPagos;
    }

    public PagoVO[] procesarArchivoCASALEY(File archivo, ArrayList<PagoVO> inidenciasArray, int numCuenta) throws Exception {

        String linea = archivo.getContentString();
        String lines[] = linea.split("\n");
        PagoVO[] totalPagos = new PagoVO[lines.length - 1];
        int contadorTotalPagos = 0;

        for (int j = 1; j <= totalPagos.length; j++) {

            PagoVO pago = new PagoVO();
            String annio = lines[j].substring(32, 36).toString();
            //System.out.println("annio "+annio);
            String mes = lines[j].substring(30, 32).toString();
            //System.out.println("mes "+mes);
            String dia = lines[j].substring(28, 30).toString();
            //System.out.println("dia "+dia);
            String fecha = dia + "/" + mes + "/" + annio;

            pago.referencia = lines[j].substring(3, 16).trim().toString();
            myLogger.debug("REFERENCIA CASALEY: " + pago.referencia);
            pago.monto = Convertidor.stringToDouble(lines[j].substring(16, 24).toString() + "." + lines[j].substring(24, 26).toString());
            //System.out.println("pago.monto "+pago.monto);
            pago.fechaPago = Convertidor.stringToSqlDate(fecha);
            //System.out.println("pago.fechaPago "+pago.fechaPago);
            pago.bancoReferencia = ClientesConstants.ID_BANCO_CASALEY;
            pago.numCuenta = numCuenta;

            totalPagos[contadorTotalPagos] = pago;
            contadorTotalPagos++;

            myLogger.debug("PAGOS CASALEY:" + pago.toString());

        }
        return totalPagos;
    }
    
    public PagoVO[] procesarArchivoOPENPAY(File archivo, ArrayList<PagoVO> inidenciasArray, int numCuenta) throws Exception {

        String linea = archivo.getContentString();
        String lines[] = linea.split("\n");
        String campos[];

        String status = "";
        String referenceOpenPay = "";
        String operationDate = "";
        String amount = "";
        
        PagoVO[] totalPagos = new PagoVO[lines.length - 1];

        int contadorTotalPagos = 0;
        for (int j = 1; j <= lines.length - 1; j++) {
            //----- CAMBIOS CSV 100317 -----------
            //lines[j] = lines[j].replace("\"", "");
            //lines[j] = lines[j].replace(",", "");
            //campos = lines[j].split("\\|");//para .txt por pipes
            campos = lines[j].split("(?!\\B\"[^\"]*),(?![^\"]*\"\\B)", -1);//para .csv , (?!\B"[^"]*),(?![^"]*"\B)
            //----- CAMBIOS CSV 100317 -----------

            status = campos[5].trim();
            String referenciaArchivo = campos[9].trim();
            String referenciaSinIssuer = referenciaArchivo.substring(6);

            operationDate = campos[10].trim();
            amount = campos[15].trim().replace("\"", "").replace(",", "");

            myLogger.debug("status: " + status);
            myLogger.debug("referenciaArchivo: " + referenciaArchivo);
            myLogger.debug("referenciaSinIssuer: " + referenciaSinIssuer);
            myLogger.debug("operationDate: " + operationDate);
            myLogger.debug("amount: " + amount);

            String grupo = referenciaSinIssuer.substring(1, 6+1);
            String ciclo = referenciaSinIssuer.substring(7, 7+2);		
            myLogger.debug(" grupo:"+grupo);
            myLogger.debug(" ciclo:"+ ciclo);

            ReferenciaGeneralVO referenciaGralVO = new ReferenciaGeneralDAO().getReferenciaGeneralByGrupoYCiclo(grupo,ciclo);
            myLogger.debug("###############################");
            PagoVO pago = new PagoVO();
            pago.monto = Convertidor.stringToDouble(amount);
            pago.fechaPago = Convertidor.stringToSqlDate(operationDate, ClientesConstants.FORMATO_FECHA_EU);
            pago.bancoReferencia = ClientesConstants.ID_BANCO_OPENPAY;
            pago.numCuenta = numCuenta;//se obtiene de D_CUENTAS_BANCO,ya esta en BD, numCuenta = 31
            myLogger.debug("contadorTotalPagos:" + contadorTotalPagos);
            totalPagos[contadorTotalPagos++] = pago;
            myLogger.debug("contadorTotalPagos:" + contadorTotalPagos);
            
            if (status.equalsIgnoreCase("completed") && referenciaGralVO != null ) {
                myLogger.debug("REFERENCIA ORIGINAL PARA OPENPAY: " + referenciaGralVO.referencia);
                pago.referencia = referenciaGralVO.referencia;

            }else{
                myLogger.debug("REFERENCIA ARCHIVO PAGOS TRUNCADA: " + referenciaGralVO.referencia);
                pago.referencia = referenciaArchivo.substring(3);
            }
            
            myLogger.debug("PAGOS OPENPAY:" + pago.toString());
            myLogger.debug("###############################");
        }
        return totalPagos;
        
        
    }
    
    public double getMontoTotalArchivo(ArrayList<PagoVO> pagos) {
        double montoTotal = 0.0;

        Iterator<PagoVO> it = pagos.iterator();
        int contador = 1;
        while (it.hasNext()) {
            PagoVO pago = (PagoVO) it.next();
            montoTotal = montoTotal + pago.monto;
            contador++;
        }

        return montoTotal;
    }

    public double getMontoTotalArchivoExcedentes(ArrayList<Double> pagoExcedente) {
        double montoTotal = 0.0;

        for (int i = 0; i < pagoExcedente.size(); i++) {
            //Logger.debug("Sumando: "+pagoExcedente.get(i));
            montoTotal = Double.valueOf(montoTotal) + Double.valueOf(pagoExcedente.get(i));
        }

        return montoTotal;
    }

    public int existeReferencia(String referencia) {
        //Metodo que verifica si existe referencia.
        // situacionreferencia = 0    referencia no existe
        // situacionreferencia = 1    referencia de referencias generales
        // situacionreferencia = 2    referencia alterna
        int existeRef = 0;
        PagoReferenciadoDAO pagoReferenciado = new PagoReferenciadoDAO();
        try {
            if (pagoReferenciado.existeReferencia(referencia)) {
                existeRef = 1;
            } else if (pagoReferenciado.existeReferenciaenTablaAlterna(referencia)) {
                existeRef = 2;
            }
        } catch (ClientesException e) {
            myLogger.error("Error dentro existeReferencia", e);
        }
        return existeRef;
    }

    public boolean tieneSeguro(String referencia) {
        ReferenciaGeneralVO refVo = getReferencia(referencia);
        boolean hasSeguro = false;

        try {
            seguroVO = segurosDAO.getSeguros(refVo.numcliente, refVo.numSolicitud);

            if (seguroVO != null && seguroVO.primaTotal == 0) {
                hasSeguro = true;
            }

        } catch (ClientesException e) {
            // TODO Auto-generated catch block
            myLogger.error("Error dentro tieneSeguro", e);
        }
        return hasSeguro;
    }

    public synchronized ArrayList<PagoVO> procesaArchivoReferenciado(File myFile, HttpServletRequest request) {
        //Metodo de Procesamiento de archivo via JSP Smart
        int totalRegistros = 0;
        int bancoReferencia = 0;
        int procesadosTCI = 0;
        int noProcesadosTCI = 0;
        int cuenta = 0;
        int movimientos = 0;

        double totalVigente = -1;
        double totalPrima = 0.0;
        double montoTotal = 0;

        CifraControlVO cf = new CifraControlVO();
        SaldoIBSVO saldoIBSVO = new SaldoIBSVO();
        ReferenciaGeneralVO refGeneralVO = new ReferenciaGeneralVO();
        PagoExcedenteVO pe = new PagoExcedenteVO();
        PagoVO pagosaldo = new PagoVO();
        PagoVO pagocartera = null;
        CreditoCartVO creditoVO = new CreditoCartVO();

        SaldoIBSDAO saldoDAO = new SaldoIBSDAO();
        IntegranteCicloDAO integranteDAO = new IntegranteCicloDAO();
        PagoManualDAO pagoManualDAO = new PagoManualDAO();
        PagosExcedentesDAO pagosExcedentes = new PagosExcedentesDAO();
        PagoDAO pagoDAO = new PagoDAO();
        PagoReferenciadoDAO pagoRefDAO = new PagoReferenciadoDAO();
        CarteraTCIDAO tciDAO = new CarteraTCIDAO();
        CreditoCartDAO creditoDAO = new CreditoCartDAO();
        CuentasBancariasDAO cuentasDAO = new CuentasBancariasDAO();

        Notification notificaciones[] = null;
        IntegranteCicloVO[] integrantes = null;

        ArrayList<Notification> array = new ArrayList<Notification>();
        ArrayList<Double> excedentesArray = new ArrayList<Double>();
        ArrayList<PagoVO> incidenciasArray = new ArrayList<PagoVO>();
        ArrayList<PagoVO> pagosProcesadosArray = new ArrayList<PagoVO>();
        ArrayList<PagoVO> pagosInsuficienteArray = new ArrayList<PagoVO>();
        ArrayList<Double> segurosArray = new ArrayList<Double>();
        ArrayList<PagoVO> pagoscarteraparaenviar = new ArrayList<PagoVO>();

        Date fechaMov = new Date(Calendar.getInstance().getTimeInMillis());

        TransaccionesHelper transacHelper = new TransaccionesHelper();
        EventoHelper eventoHelper = new EventoHelper();

        String bancoCuenta = "";

        if (!myFile.isMissing()) {
            myLogger.debug("Existe el archivo");
            System.out.println("myFile.getSize() " + myFile.getSize());
            if (myFile.getSize() > 0) {
                myLogger.debug("Contiene Datos");
                myLogger.debug(myFile.getFileName());
                myLogger.debug(myFile.getFieldName());
                String fileName = myFile.getFileName();
                //Se valida el tipo de archivo.
                if (ArchivosAsociadosHelper.esFormatoValido(myFile)) {
                    myLogger.debug("Tiene Formato Válido");
                    PagoVO[] pagos = null;
                    String content = myFile.getContentString();
                    try {
                        String tipoDocumento = getTipoArchivo(fileName);
                        bancoCuenta = fileName.replace("_", ",");
                        String nombre[] = bancoCuenta.split(",");
                        if (tipoDocumento.equals("HSBC")) {
                            //Logger.debug("Tipo Documento HSBC");
                            String lines[] = content.split("\n");
                            totalRegistros = lines.length - 1;
                            //Se obtienen los pagos HSBC, incluyendo arreglo de incidencias
                            bancoReferencia = ClientesConstants.ID_BANCO_HSBC;
                            cuenta = cuentasDAO.getNumCuentaBancaria(bancoReferencia, nombre[1], "C");
                            pagos = procesarArchivoHSBC(myFile, incidenciasArray, cuenta);
                        } else if (tipoDocumento.equals("IBS")) {
                            //Logger.debug("Tipo Documento IBS");
                            String lines[] = content.split("\n");
                            totalRegistros = lines.length - 3;
                            //Se obtienen los pagos IBS, incluyendo arreglo de incidencias
                            pagos = procesarArchivoIBS(myFile, incidenciasArray);
                        } else if (tipoDocumento.equals("BNTE")) {
                            //Logger.debug("Tipo Documento BNTE");
                            String lines[] = content.split("\r");
                            totalRegistros = lines.length - 3;
                            //Se obtienen los pagos BNTE, incluyendo arreglo de incidencias
                            bancoReferencia = ClientesConstants.ID_BANCO_BANORTE;
                            cuenta = cuentasDAO.getNumCuentaBancaria(bancoReferencia, nombre[1], "C");
                            pagos = procesarArchivoBNTE(myFile, incidenciasArray, cuenta);
                        } else if (tipoDocumento.equals("BMER")) {
                            //Logger.debug("Tipo Documento BMER");
                            String lines[] = content.split("\r");
                            totalRegistros = lines.length - 8;
                            bancoReferencia = ClientesConstants.ID_BANCO_BANCOMER;
                            cuenta = cuentasDAO.getNumCuentaBancaria(bancoReferencia, nombre[1], "C");
                            pagos = procesarArchivoBMER(myFile, incidenciasArray, cuenta);
                        } else if (tipoDocumento.equals("AFME")) {
                            //Logger.debug("Tipo Documento AFME");
                            String lines[] = content.split("\r");
                            totalRegistros = lines.length - 1;
                            bancoReferencia = ClientesConstants.ID_BANCO_AFIRME;
                            cuenta = cuentasDAO.getNumCuentaBancaria(bancoReferencia, nombre[1], "C");
                            pagos = procesarArchivoAFME(myFile, incidenciasArray, cuenta);
                        } else if (tipoDocumento.equals("BSFI")) {
                            //Logger.debug("Tipo Documento BSFI");
                            String lines[] = content.split("\r");
                            totalRegistros = lines.length - 3;
                            bancoReferencia = ClientesConstants.ID_BANCO_BANSEFI;
                            cuenta = cuentasDAO.getNumCuentaBancaria(bancoReferencia, nombre[1], "C");
                            pagos = procesarArchivoBSFI(myFile, incidenciasArray, cuenta);
                        } else if (tipoDocumento.equals("COMA")) {
                            //Logger.debug("Tipo Documento COMA");
                            String lines[] = content.split("\r");
                            totalRegistros = lines.length - 2;
                            bancoReferencia = ClientesConstants.ID_BANCO_COMA;
                            cuenta = cuentasDAO.getNumCuentaBancaria(bancoReferencia, nombre[1], "C");
                            pagos = procesarArchivoCOMA(myFile, incidenciasArray, cuenta);
                        } else if (tipoDocumento.equals("BANAMEX")) {
                            //Logger.debug("Tipo Documento BANAMEX");
                            String lines[] = content.split("\r");
                            totalRegistros = lines.length - 2;
                            bancoReferencia = ClientesConstants.ID_BANCO_BANAMEX;
                            cuenta = cuentasDAO.getNumCuentaBancaria(bancoReferencia, nombre[1], "C");
                            pagos = procesarArchivoBANAMEX(myFile, incidenciasArray, cuenta);
                        } else if (tipoDocumento.equals("SCOTIA")) {
                            //Logger.debug("Tipo Documento SCOTIA");
                            String lines[] = content.split("\r");
                            totalRegistros = lines.length - 2;
                            bancoReferencia = ClientesConstants.ID_BANCO_SCOTIABANK;
                            cuenta = cuentasDAO.getNumCuentaBancaria(bancoReferencia, nombre[1], "C");
                            pagos = procesarArchivoSCOTIABANK(myFile, incidenciasArray, totalRegistros, cuenta);
                        } else if (tipoDocumento.equals("BAJIO")) {
                            //Logger.debug("Tipo Documento BAJIO");
                            String lines[] = content.split("\r");
                            totalRegistros = lines.length - 2;
                            bancoReferencia = ClientesConstants.ID_BANCO_BANBAJIO;
                            cuenta = cuentasDAO.getNumCuentaBancaria(bancoReferencia, nombre[1], "C");
                            pagos = procesarArchivoBANBAJIO(myFile, incidenciasArray, totalRegistros, cuenta);
                        } else if (tipoDocumento.equals("COMER")) {
                            //Logger.debug("Tipo Documento COMEXICANA");
                            String lines[] = content.split("\r");
                            totalRegistros = lines.length - 2;
                            bancoReferencia = ClientesConstants.ID_BANCO_COM_MEXICANA;
                            cuenta = cuentasDAO.getNumCuentaBancaria(bancoReferencia, "00140", "C");
                            pagos = procesarArchivoCOMEXICANA(myFile, incidenciasArray, cuenta);
                        } else if (tipoDocumento.equals("OXXO")) {
                            //Logger.debug("Tipo Documento OXXO");
                            String lines[] = content.split("\r");
                            //totalRegistros = lines.length - 2;
                            totalRegistros = lines.length;
                            bancoReferencia = ClientesConstants.ID_BANCO_OXXO;
                            cuenta = cuentasDAO.getNumCuentaBancaria(bancoReferencia, "00180", "C");
                            pagos = procesarArchivoOXXO(myFile, incidenciasArray, cuenta);
                        } else if (tipoDocumento.equals("TELECOM")) {
                            //Logger.debug("Tipo Documento TELECOM");
                            String lines[] = content.split("\r");
                            totalRegistros = lines.length - 1;
                            bancoReferencia = ClientesConstants.ID_BANCO_TELECOM;
                            cuenta = cuentasDAO.getNumCuentaBancaria(bancoReferencia, "00100", "C");
                            pagos = procesarArchivoTELECOM(myFile, incidenciasArray, cuenta);
                        } else if (tipoDocumento.equals("FBENA")) {
                            //Logger.debug("Tipo Documento FBENA");
                            String lines[] = content.split("\r");
                            totalRegistros = lines.length - 2;
                            bancoReferencia = ClientesConstants.ID_BANCO_BENAVIDES;
                            cuenta = cuentasDAO.getNumCuentaBancaria(bancoReferencia, "00200", "C");
                            pagos = procesarArchivoFBENA(myFile, incidenciasArray, cuenta);
                        } else if (tipoDocumento.equals("FABC")) {
                            //Logger.debug("Tipo Documento FBENA");
                            String lines[] = content.split("\r");
                            totalRegistros = lines.length - 2;
                            bancoReferencia = ClientesConstants.ID_BANCO_FABC;
                            cuenta = cuentasDAO.getNumCuentaBancaria(bancoReferencia, "00210", "C");
                            pagos = procesarArchivoFABC(myFile, incidenciasArray, cuenta);
                        } else if (tipoDocumento.equals("FRESKO")) {
                            //Logger.debug("Tipo Documento FRESKO");
                            String lines[] = content.split("\r");
                            totalRegistros = lines.length - 2;
                            bancoReferencia = ClientesConstants.ID_BANCO_FRESKO;
                            cuenta = cuentasDAO.getNumCuentaBancaria(bancoReferencia, "00230", "C");
                            pagos = procesarArchivoFRESKO(myFile, incidenciasArray, cuenta);
                        } else if (tipoDocumento.equals("SANTANDER")) {
                            //Logger.debug("Tipo Documento Santandert");
                            String lines[] = content.split("\r");
                            totalRegistros = lines.length;
                            bancoReferencia = ClientesConstants.ID_BANCO_SANTANDER_NVO;
                            cuenta = cuentasDAO.getNumCuentaBancaria(bancoReferencia, "9185", "C");
                            pagos = procesarArchivoSantander(myFile, incidenciasArray, cuenta);
                        } else if (tipoDocumento.equals("OPENPAY")) {
                            //Logger.debug("Tipo Documento OPENPAY");
                            String lines[] = content.split("\r");
                            totalRegistros = lines.length - 1;
                            bancoReferencia = ClientesConstants.ID_BANCO_OPENPAY;// 25
                            cuenta = cuentasDAO.getNumCuentaBancaria(bancoReferencia, "00250", "C");//ya esta en BD ,cuenta = 31
                            pagos = procesarArchivoOPENPAY(myFile, incidenciasArray, cuenta);
                            //------------------- CAMBIOS 130317 LECTURA---------- 
                            if (pagos[0] == null) {
                                PagoVO pagoerror = new PagoVO();
                                pagoerror.referencia = "0019000000000";
                                pagoerror.comentarios = "ERROR:El archivo " + myFile.getFileName() + " no cuenta con registros con estatus completed.";
                                pagoerror.fechaPago = Convertidor.stringToSqlDate("2000-01-01", ClientesConstants.FORMATO_FECHA_EU);
                                pagos[0] = pagoerror;
                                array.add(new Notification(ClientesConstants.ERROR_TYPE, "El archivo " + myFile.getFileName() + " no cuenta con registros con estatus completed."));
                                myLogger.debug("El archivo " + myFile.getFileName() + " no cuenta con registros con estatus completed.");
                            }
                            //------------------- CAMBIOS 130317 LECTURA---------- 
                        } else if (tipoDocumento.equals("SORIANA")) {
                            //Logger.debug("Tipo Documento SORIANA");
                            String lines[] = content.split("\r");
                            totalRegistros = lines.length - 2;
                            bancoReferencia = ClientesConstants.ID_BANCO_SORIANA;//27
                            cuenta = cuentasDAO.getNumCuentaBancaria(bancoReferencia, "00260", "C");//32
                            pagos = procesarArchivoSORIANA(myFile, incidenciasArray, cuenta);
                            
                        }else if (tipoDocumento.equals("CASALEY")) {
                            //Logger.debug("Tipo Documento CASALEY");
                            String lines[] = content.split("\r");
                            totalRegistros = lines.length - 2;
                            bancoReferencia = ClientesConstants.ID_BANCO_CASALEY;//26
                            cuenta = cuentasDAO.getNumCuentaBancaria(bancoReferencia, "00270", "C");//32
                            pagos = procesarArchivoCASALEY(myFile, incidenciasArray, cuenta);                            
                        } else {
                            //Es un vale
                            //Logger.debug("Tipo Documento VALE");
                            String lines[] = content.split("\n");
                            totalRegistros = lines.length - 1;
                            pagos = procesarArchivoVALE(myFile, incidenciasArray);
                        }
                        PagoVO[] archivocarteraparaenviar = new PagoVO[pagos.length];
                        // Cifra de Control para la Póliza Contable
                        cf.fechaMov = fechaMov;
                        cf.bancoReferencia = bancoReferencia;
                        cf.numCuenta = cuenta;
                        int IdparaPolizaContable = cifraControlDAO.addCifraControl(cf);
                        cf.idCifraControl = IdparaPolizaContable;
                        for (int j = 0; j <= pagos.length - 1; j++) {
                            if (pagos[j] != null) {
                                movimientos = pagos.length;
                                bancoReferencia = pagos[j].bancoReferencia;
                                pagos[j].idContable = IdparaPolizaContable;
                                pagosaldo.referencia = pagos[j].referencia;
                                pagosaldo.monto = pagos[j].monto;
                                pagosaldo.fechaPago = pagos[j].fechaPago;
                                pagosaldo.fechaHora = pagos[j].fechaHora;
                                pagosaldo.tipo = "T";
                                pagosaldo.enviado = 0;
                                pagosaldo.bancoReferencia = bancoReferencia;
                                pagosaldo.idContable = IdparaPolizaContable;
                                pagocartera = new PagoVO();
                                pagocartera.setReferencia(pagos[j].referencia);
                                pagocartera.setFechaPago(pagos[j].fechaPago);
                                pagocartera.setFechaHora(pagos[j].fechaHora);
                                pagocartera.setBancoReferencia(bancoReferencia);
                                pagocartera.setIdContable(IdparaPolizaContable);
                                pagosaldo.bancoReferencia = bancoReferencia;
                                pagos[j].bancoReferencia = bancoReferencia;
                                //registropagoseguros.bancoReferencia = bancoReferencia;
                                //registropagoseguros.idContabilidad = IdparaPolizaContable;
                                pe.bancoReferencia = bancoReferencia;
                                pe.idContabilidad = IdparaPolizaContable;
                                if (pagos[j].referencia.length() == 13) {
                                    refGeneralVO = referenciaDAO.getReferenciaGeneral(pagos[j].referencia);
                                    //situacionreferencia = cmdPagoRef.existeReferencia(pagos[j].referencia);
                                    //if(situacionreferencia == 1) {
                                    if (refGeneralVO != null) {
                                        totalVigente = -1;
                                        myLogger.debug("Ir a Saldos para obtener total vigente");
                                        saldoIBSVO = saldoDAO.getSaldo(refGeneralVO.numcliente, refGeneralVO.numSolicitud, refGeneralVO.referencia);
                                        totalVigente = getSaldoInsolutoAlCompromiso(saldoIBSVO);
                                        if (!(totalVigente == -1.0)) {
                                            //verificaLiquidadoCancelado = cmdPagoRef.verificaLiquidadoCancelado(pagos[j].referencia);
                                            pagos[j].montoAbono = pagos[j].monto;
                                            montoTotal = pagos[j].monto;
                                            if (saldoIBSVO.getEstatus() != 3 && saldoIBSVO.getEstatus() != 7) {
                                                if (!(saldoIBSVO.getEstatus() == 5)) {
                                                    // Se verifica que el pago no exista en la base de datos
                                                    int pagoVerif = 0;
                                                    pagoVerif = pagoDAO.getPagoVO(pagos[j]);
                                                    if (pagoVerif == 0) {
                                                        // Si es pago individual validar si tiene seguro
                                                        //if (!GrupoUtil.esPagoGrupal(pagos[j].referencia)) {
                                                        if (GrupoUtil.esPagoGrupal(refGeneralVO.referencia)) {
                                                            pagocartera.setDestino(1);
                                                            if (pagos[j].isCreditoIBS) {
                                                                pagocartera.setDestino(3);
                                                            }
                                                            integrantes = integranteDAO.getIntegrantes(refGeneralVO.numcliente, refGeneralVO.numSolicitud);
                                                            if (integrantes != null) {
                                                                myLogger.debug("Va a Matriz de Pagos");
                                                                if (GrupoUtil.matrizPagos(integrantes, pagos[j], request) == 0) {
                                                                    myLogger.debug("Matriz de Pagos Lista para :" + pagos[j].referencia);
                                                                } else {
                                                                    myLogger.debug("Error en Matriz de Pagos para :" + pagos[j].referencia);
                                                                }
                                                            }
                                                        }
                                                        if ((montoTotal > totalVigente)) {
                                                            //Incidencias de excedente
                                                            pe.fechaMovimiento = pagos[j].fechaPago;
                                                            pe.referencia = pagos[j].referencia;
                                                            pe.montoExcedente = pagos[j].monto - totalVigente;
                                                            pe.numCliente = refGeneralVO.numcliente;
                                                            pe.numCuentaContable = "";
                                                            pagosExcedentes.addPagoExcedente(pe);
                                                            // Cifras Control
                                                            excedentesArray.add(pe.montoExcedente);
                                                            pagocartera.setMonto(pagos[j].monto);
                                                            //pago al credito
                                                            pagosProcesadosArray.add(pagos[j]);
                                                            caso8++;
                                                            pagosaldo.status = 8;
                                                            pagocartera.setStatus(8);
                                                        } else {
                                                            //No cubre con lo minimo a pagar, causa incidencia
                                                            pagocartera.setMonto(pagos[j].monto);
                                                            // Cifras Control
                                                            pagosProcesadosArray.add(pagos[j]);
                                                            pagosInsuficienteArray.add(pagos[j]);
                                                            caso6++;
                                                            pagosaldo.status = 6;
                                                            pagocartera.setStatus(6);
                                                        }
                                                        pagocartera.setSucursal(pagos[j].referencia.substring(0, 3));
                                                        pagocartera.setFechaHora(new Timestamp(System.currentTimeMillis()));
                                                        pagocartera.setNumCuenta(pagos[j].numCuenta);
                                                        creditoVO = creditoDAO.getCreditoReferencia(pagocartera.getReferencia());
                                                        if (creditoVO.getMontoCuenta() >= creditoVO.getMontoCuentaCongelada()) {
                                                            pagocartera.setDestino(1);
                                                            pagocartera.setEnviado(0);
                                                        } else {
                                                            if (pagocartera.getMonto() <= (creditoVO.getMontoCuentaCongelada() - creditoVO.getMontoCuenta())) {
                                                                transacHelper.registraPagoGarantia(creditoVO, pagocartera, true);
                                                            } else {
                                                                pagocartera.setMonto(creditoVO.getMontoCuentaCongelada() - creditoVO.getMontoCuenta());
                                                                transacHelper.registraPagoGarantia(creditoVO, pagocartera, false);
                                                                pagocartera.setMonto(pagos[j].monto - (creditoVO.getMontoCuentaCongelada() - creditoVO.getMontoCuenta()));
                                                                transacHelper.registraPago(creditoVO, pagocartera);
                                                            }
                                                            creditoVO.setMontoCuenta(pagos[j].monto + creditoVO.getMontoCuenta());
                                                            creditoDAO.updatePagoCreditoCierre(creditoVO);
                                                            pagocartera.setDestino(3);
                                                            pagocartera.setEnviado(1);
                                                            pagocartera.setMonto(pagos[j].monto);
                                                            eventoHelper.registraPago(creditoVO, pagocartera);
                                                        }
                                                        pagoDAO.insertPagoCartera(pagocartera);
                                                        pagoManualDAO.insertPago(pagocartera);
                                                        myLogger.debug("Pago Cartera1: " + pagocartera.toString());
                                                    } else {
                                                        //Causa incidencia, Pago Repetido
                                                        pagoRefDAO.addIncidencia(pagos[j], 6, "Pago Repetido");
                                                        // Cifras Control
                                                        incidenciasArray.add(pagos[j]);
                                                        caso14++;
                                                        pagosaldo.status = 6;
                                                    }
                                                } else {
                                                    //Causa incidencia, Crédito Cancelado
                                                    pagoRefDAO.addIncidencia(pagos[j], 5, "Crédito Cancelado");
                                                    // Cifras Control
                                                    incidenciasArray.add(pagos[j]);
                                                    caso5++;
                                                    pagosaldo.status = 5;
                                                }
                                            } else {
                                                myLogger.debug("Entra para ver si se va a TCI");
                                                //VERIFICA EL ESTATSU CARTERA CEDIDA (7)
                                                if (tciDAO.verificaEstatusCredito(pagos[j].referencia)) {
                                                    pagocartera = new PagoVO(pagos[j].referencia, pagos[j].monto, pagos[j].fechaPago, new Timestamp(System.currentTimeMillis()), 0, 6, pagos[j].bancoReferencia, pagos[j].referencia.substring(0, 3), pagos[j].numCuenta);
                                                    //VERIFICA QUE NO ESTE LIQUEIDADO
                                                    if (tciDAO.verificaEstatusCedido(pagos[j].referencia)) {
                                                        tciDAO.insertIncidenciaTCI(pagocartera, 4, "Credito Liquidado");
                                                        noProcesadosTCI++;
                                                    } else //VERIFICA QUE EL PAGO NO ESTE DUPLICADO
                                                    if (tciDAO.verificaPagoDuplicado(pagos[j].referencia, pagos[j].monto, pagos[j].fechaPago, pagos[j].bancoReferencia)) {
                                                        tciDAO.insertIncidenciaTCI(pagocartera, 6, "Pago Repetido");
                                                        noProcesadosTCI++;
                                                    } else {
                                                        tciDAO.insertPagoTCI(pagocartera);
                                                        procesadosTCI++;
                                                    }
                                                } else {
                                                    //Causa incidencia, Crédito Liquidado
                                                    pagoRefDAO.addIncidencia(pagos[j], 4, "Crédito Liquidado");
                                                    incidenciasArray.add(pagos[j]);
                                                    caso4++;
                                                    pagosaldo.status = 4;
                                                }
                                            }
                                        } else {
                                            //Causa incidencia, Referencia encontrada, Saldo no encontrado
                                            pagoRefDAO.addIncidencia(pagos[j], 3, "Saldo No Encontrado");
                                            incidenciasArray.add(pagos[j]);
                                            caso3++;
                                            pagosaldo.status = 3;
                                        }
                                    } else {
                                        //Causa incidencia, Referencia No Encontrada
                                        pagoRefDAO.addIncidencia(pagos[j], 2, "Referencia No Encontrada");
                                        //  Cifras Control
                                        incidenciasArray.add(pagos[j]);
                                        caso2++;
                                        pagosaldo.status = 2;
                                    }
                                } else {
                                    //Causa Incidencia, Referencia No Valida
                                    pagoRefDAO.addIncidencia(pagos[j], 1, "Referencia No Válida");
                                    // Cifras Control
                                    incidenciasArray.add(pagos[j]);
                                    caso1++;
                                    pagosaldo.status = 1;
                                }
                            }
                            //  Cifras Control, guardar todos los pagos recibidos (procesados o no)
                            totalPagosArray.add(pagos[j]);
                            pagosaldo.sucursal = pagos[j].referencia.substring(0, 3);
                            pagosaldo.numCuenta = pagos[j].numCuenta;
                            pagoDAO.insertPago(pagosaldo);
                            myLogger.debug("Pagos: " + pagosaldo.toString());
                            archivocarteraparaenviar[j] = pagocartera;
                            pagoscarteraparaenviar.add(archivocarteraparaenviar[j]);
                        }
                        // NOTA: En los pagos procesados la aplicación está descontando la prima
                        double pagos_normales = getMontoTotalArchivo(pagosProcesadosArray) - getMontoTotalArchivoExcedentes(excedentesArray) - totalPrima;
                        cf.registrosIntroducidos = totalPagosArray.size();
                        cf.registrosProcesados = pagosProcesadosArray.size();
                        cf.registrosNoProcesados = incidenciasArray.size();
                        cf.seguros = segurosArray.size();
                        cf.montosIntroducidos = getMontoTotalArchivo(totalPagosArray);
                        cf.montosProcesados = getMontoTotalArchivo(pagosProcesadosArray);
                        cf.montosNoProcesados = getMontoTotalArchivo(incidenciasArray);
                        cf.montosAplicadosaCredito = pagos_normales;
                        cf.montosAplicadosaSeguros = totalPrima;
                        cf.montosdeExcedentes = getMontoTotalArchivoExcedentes(excedentesArray);
                        myLogger.debug("REGISTROS INTRODUCIDOS: " + totalPagosArray.size()); // TODOS LOS PAGOS
                        myLogger.debug("REGISTROS PROCESADOS: " + pagosProcesadosArray.size()); // PAGOS INCLUYENDO PAGO INSUFICIENTE, PAGO EXCEDENTE Y SEGUROS
                        myLogger.debug("REGISTROS NO PROCESADOS: " + incidenciasArray.size()); // PAGOS CON REF NO ENCONTRADA,SALDO NO ENCONTRADO,CREDITO CANCELADO,CREDITO LIQUIDADO
                        myLogger.debug("REGISTROS PROCESADOS TCI: " + procesadosTCI);
                        myLogger.debug("REGISTROS NO PROCESADOS TCI: " + noProcesadosTCI);
                        myLogger.debug("MONTOS INTRODUCIDOS: " + cf.montosIntroducidos);
                        myLogger.debug("MONTOS PROCESADOS: " + cf.montosProcesados);
                        myLogger.debug("MONTOS NO PROCESADOS: " + cf.montosNoProcesados);
                        myLogger.debug("MONTOS APLICADOS A CRÉDITO: " + cf.montosAplicadosaCredito);
                        myLogger.debug("MONTOS APLICADOS A SEGUROS: " + cf.montosAplicadosaSeguros);
                        myLogger.debug("MONTOS DE EXCEDENTES: " + cf.montosdeExcedentes);
                        myLogger.debug("------ Otros Datos -----------");
                        myLogger.debug("REGISTROS SEGUROS: " + segurosArray.size());
                        myLogger.debug("------ Pagos No Procesados Por Caso -----------");
                        myLogger.debug("Referencia No Valida: " + caso1);
                        myLogger.debug("Referencia No Encontrada: " + caso2);
                        myLogger.debug("Saldo No Encontrado: " + caso3);
                        myLogger.debug("Crédito Liquidado: " + caso4);
                        myLogger.debug("Crédito Cancelado: " + caso5);
                        myLogger.debug("Pagos repetidos: " + caso14);
                        myLogger.debug("------ Pagos Procesados Por Caso -----------");
                        myLogger.debug("Pago insuficiente: " + caso6);
                        myLogger.debug("Pago normal al crédito, no tiene seguro: " + caso7);
                        myLogger.debug("Pago Excedente pero no tenía seguro: " + caso8);
                        myLogger.debug("Tiene seguro, pero sólo cubre el crédito exacto: " + caso9);
                        myLogger.debug("Pago excedente al crédito pero no alcanza a cubrir el seguro: " + caso10);
                        myLogger.debug("Pago normal al crédito y 90% al seguro: " + caso11);
                        myLogger.debug("Pago normal al crédito y al seguro: " + caso12);
                        myLogger.debug("Pago Excedente al crédito y con seguro: " + caso13);
                        cifraControlDAO.updateCifraControl(cf);
                        array.add(new Notification(ClientesConstants.INFO_LEVEL, "El archivo " + myFile.getFileName() + " se ha procesado correctamente"));

                    } catch (Exception e) {
                        myLogger.error("procesaArchivoReferenciado", e);
                    }
                } else {
                    array.add(new Notification(ClientesConstants.ERROR_TYPE, "El archivo " + myFile.getFileName() + " no se encuentra en un formato permitido."));
                    myLogger.debug("El archivo " + myFile.getFileName() + " no se encuentra en un formato permitido.");
                }
            } else {
                array.add(new Notification(ClientesConstants.ERROR_TYPE, "El archivo " + myFile.getFileName() + " excede el tamaño permitido."));
                myLogger.debug("El archivo " + myFile.getFileName() + " excede el tamaño permitido.");
            }
        } else {
            array.add(new Notification(ClientesConstants.ERROR_TYPE, "El archivo " + myFile.getFileName() + " no se cargó exitosamente."));
            myLogger.debug("El archivo " + myFile.getFileName() + " no se cargó exitosamente.");
        }
        if (array.size() > 0) {
            notificaciones = new Notification[array.size()];
            for (int i = 0; i < notificaciones.length; i++) {
                notificaciones[i] = (Notification) array.get(i);
            }
        }
        request.setAttribute("NOTIFICACIONES", notificaciones);
        return pagoscarteraparaenviar;
    }

    public void agregarIncidencia(PagoVO pago, int tipo, String obs) {
        //Metodo que agrega incidencia.
        PagoReferenciadoDAO pagoReferenciado = new PagoReferenciadoDAO();
        try {

            pagoReferenciado.addIncidencia(pago, tipo, obs);
        } catch (ClientesException e) {
            myLogger.error("Error dentro agregarIncidencia ", e);
        }

    }
}
