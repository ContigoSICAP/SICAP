package com.sicap.clientes.helpers;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FormatUtil;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.ConsultaEfectuadaVO;
import com.sicap.clientes.vo.CuentaVO;
import com.sicap.clientes.vo.DomicilioVO;
import com.sicap.clientes.vo.ReporteCirculoEmpleoVO;
import com.sicap.clientes.vo.ReporteCirculoVO;
import com.sicap.clientes.vo.ResumenVO;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.sicap.clientes.vo.ScoreFicoVO;

public class CreatePdfCDC {

    private static ReporteCirculoVO reporte = null;
    private static AcroFields reciboForma = null;
    private PdfReader reader = null;
    private PdfStamper reciboPDF = null;
    private OutputStream out = new ByteArrayOutputStream();

    public CreatePdfCDC(ReporteCirculoVO _reporte) {
        reporte = _reporte;
        init();

    }

    public CreatePdfCDC() {
    }

    public OutputStream createPDF() {

        Logger.debug("Generando Documento PDF");

        try {
            writeDatosGenerales();
            writeDomicilios();
            writeEmpleos();
            writeResumen();
            writeCuentas();
            writeConsultas();
            reciboPDF.setFormFlattening(true);
            reciboPDF.close();

            Logger.debug("El documento PDF fue creado");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;

    }

    private static void writeDatosGenerales() {

        try {
            reciboForma.setField("nombre", reporte.getPersonas().getNombre());
            reciboForma.setField("apellidos", reporte.getPersonas().getPaterno() + " " + reporte.getPersonas().getMaterno());
//			reciboForma.setField("fechaNacimiento", Utils.convertDateToString(reporte.getPersonas().getNacimiento()));
            reciboForma.setField("fechaNacimiento", Convertidor.dateToStringCDC(reporte.getPersonas().getNacimiento()));
            reciboForma.setField("rfc", reporte.getPersonas().getRfc());
            reciboForma.setField("numeroConsulta", reporte.getEncabezadoCirculo().getFolioConsulta());
            reciboForma.setField("fechaConsulta", Convertidor.dateToStringCDC(new Date()));
            reciboForma.setField("curp", reporte.getPersonas().getCurp());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeDomicilios() {

        try {
            int cont = 0;
            DomicilioVO domicilio = null;
            Iterator iterator = reporte.getDomicilios().iterator();

            while (iterator.hasNext()) {
                cont++;
                domicilio = (DomicilioVO) iterator.next();

                reciboForma.setField("domId" + cont, cont + "");
                reciboForma.setField("domCalle" + cont, domicilio.getCalle());
                reciboForma.setField("domColonia" + cont, domicilio.getColonia());
                reciboForma.setField("domMunicipio" + cont, domicilio.getDelegacion());
                reciboForma.setField("domCiudad" + cont, domicilio.getCiudad());
                reciboForma.setField("domEstado" + cont, domicilio.getEstado());
                reciboForma.setField("domCp" + cont, domicilio.getCp());
                reciboForma.setField("domTelefono" + cont, domicilio.getTelefono());
                reciboForma.setField("domRegistro" + cont, Convertidor.dateToStringCDC(domicilio.getRegistro()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeResumen() {

        ResumenProductoHelper resumenProducto = new ResumenProductoHelper(reporte.getCuentas());
        ResumenVO resumen = null;
        List list = resumenProducto.doResumen();
        Iterator iterator = list.iterator();
        int cont = 0;

        try {
            while (iterator.hasNext()) {

                resumen = (ResumenVO) iterator.next();

                if (resumen.getDescripcion().equals("TOTALES")) {
                    reciboForma.setField("resTotal", resumen.getTotalCuentas() + "");
                    reciboForma.setField("totalLimite", resumen.getLimite() + "");
                    reciboForma.setField("totalAprobado", resumen.getAprobado() + "");
                    reciboForma.setField("totalActual", resumen.getActual() + "");
                    reciboForma.setField("totalVencido", resumen.getVencido() + "");
                    reciboForma.setField("totalSemanal", resumen.getPagoSemanal() + "");
                    reciboForma.setField("totalQuincenal", resumen.getPagoQuincenal() + "");
                    reciboForma.setField("totalMensual", resumen.getPagoMensual() + "");
                } else {
                    cont++;
                    reciboForma.setField("resProducto" + cont, resumen.getDescripcion());
                    reciboForma.setField("resTotCuentas" + cont, resumen.getTotalCuentas() + "");
                    reciboForma.setField("resLimite" + cont, resumen.getLimite() + "");
                    reciboForma.setField("resAprobado" + cont, resumen.getAprobado() + "");
                    reciboForma.setField("resActual" + cont, resumen.getActual() + "");
                    reciboForma.setField("resVencido" + cont, resumen.getVencido() + "");
                    reciboForma.setField("pSemanal" + cont, resumen.getPagoSemanal() + "");
                    reciboForma.setField("pQuincenal" + cont, resumen.getPagoQuincenal() + "");
                    reciboForma.setField("pMensual" + cont, resumen.getPagoMensual() + "");
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void writeEmpleos() {

        ReporteCirculoEmpleoVO empleo = null;
        Iterator iterator = reporte.getEmpleos().iterator();
        try {
            while (iterator.hasNext()) {

                empleo = (ReporteCirculoEmpleoVO) iterator.next();

                reciboForma.setField("empId" + empleo.getIdEmpleo(), empleo.getIdEmpleo() + "");
                reciboForma.setField("empCompania" + empleo.getIdEmpleo(), empleo.getEmpresa());
                reciboForma.setField("empPuesto" + empleo.getIdEmpleo(), empleo.getPuesto());
                reciboForma.setField("empSalario" + empleo.getIdEmpleo(), empleo.getSueldo() + "");
                reciboForma.setField("empCalle" + empleo.getIdEmpleo(), empleo.getCalle());
                reciboForma.setField("empColonia" + empleo.getIdEmpleo(), empleo.getColonia());
                reciboForma.setField("empMunicipio" + empleo.getIdEmpleo(), empleo.getMunicipio());
                reciboForma.setField("empCiudad" + empleo.getIdEmpleo(), empleo.getCiudad());
                reciboForma.setField("empEstado" + empleo.getIdEmpleo(), empleo.getEstado());
                reciboForma.setField("empCp" + empleo.getIdEmpleo(), empleo.getCp());
                reciboForma.setField("empTelefono" + empleo.getIdEmpleo(), empleo.getTelefono());
                reciboForma.setField("empRegistro" + empleo.getIdEmpleo(), Convertidor.dateToStringCDC(empleo.getFechaRegistro()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void writeCuentas() {

        CuentaVO cuenta = null;
        Iterator iterator = reporte.getCuentas().iterator();
        try {
            while (iterator.hasNext()) {

                cuenta = (CuentaVO) iterator.next();
                Convertidor.dateToStringCDC(cuenta.getFechaActualizacion());
                //System.out.println("cuenta.getIdCuenta() "+cuenta.getIdCuenta());
                //System.out.println("1) "+reciboForma.getField("frecuencia"+cuenta.getIdCuenta()));
                reciboForma.setField("frecuencia" + cuenta.getIdCuenta(), cuenta.getFrecuencia());
                //System.out.println("2) "+reciboForma.getField("frecuencia"+cuenta.getIdCuenta()));

                //System.out.println("1) "+reciboForma.getField("producto"+cuenta.getIdCuenta()));
                reciboForma.setField("producto" + cuenta.getIdCuenta(), cuenta.getDescTipoCredito());
                //System.out.println("2) "+reciboForma.getField("producto"+cuenta.getIdCuenta()));

                //System.out.println("1) "+reciboForma.getField("credito"+cuenta.getIdCuenta()));
                reciboForma.setField("credito" + cuenta.getIdCuenta(), cuenta.getTipoCuenta());
                //System.out.println("2) "+reciboForma.getField("credito"+cuenta.getIdCuenta()));

                //System.out.println("1) "+reciboForma.getField("otorgante"+cuenta.getIdCuenta()));
                reciboForma.setField("otorgante" + cuenta.getIdCuenta(), cuenta.getOtorgante());
                //System.out.println("2) "+reciboForma.getField("otorgante"+cuenta.getIdCuenta()));

                //System.out.println("1) "+reciboForma.getField("plazo"+cuenta.getIdCuenta()));
                reciboForma.setField("plazo" + cuenta.getIdCuenta(), cuenta.getNumeroPagos() + "");
                //System.out.println("2) "+reciboForma.getField("plazo"+cuenta.getIdCuenta()));

                //System.out.println("1) "+reciboForma.getField("limite"+cuenta.getIdCuenta()));
                reciboForma.setField("limite" + cuenta.getIdCuenta(), cuenta.getLimiteCredito() + "");
                //System.out.println("2) "+reciboForma.getField("limite"+cuenta.getIdCuenta()));

                //System.out.println("1) "+reciboForma.getField("aprobado"+cuenta.getIdCuenta()));
                reciboForma.setField("aprobado" + cuenta.getIdCuenta(), cuenta.getCreditoMaximo() + "");
                //System.out.println("2) "+reciboForma.getField("aprobado"+cuenta.getIdCuenta()));

                //System.out.println("1) "+reciboForma.getField("actual"+cuenta.getIdCuenta()));
                reciboForma.setField("actual" + cuenta.getIdCuenta(), cuenta.getSaldoActual() + "");
                //System.out.println("2) "+reciboForma.getField("actual"+cuenta.getIdCuenta()));

                //System.out.println("1) "+reciboForma.getField("vencido"+cuenta.getIdCuenta()));
                reciboForma.setField("vencido" + cuenta.getIdCuenta(), cuenta.getSaldoVencido() + "");
                //System.out.println("2) "+reciboForma.getField("vencido"+cuenta.getIdCuenta()));

                //System.out.println("1) "+reciboForma.getField("aPagar"+cuenta.getIdCuenta()));
                reciboForma.setField("aPagar" + cuenta.getIdCuenta(), cuenta.getAPagar() + "");
                //System.out.println("2) "+reciboForma.getField("aPagar"+cuenta.getIdCuenta()));

                //System.out.println("1) "+reciboForma.getField("reporte"+cuenta.getIdCuenta()));
                reciboForma.setField("reporte" + cuenta.getIdCuenta(), Convertidor.dateToStringCDC(cuenta.getFechaActualizacion()));
                //System.out.println("2) "+reciboForma.getField("reporte"+cuenta.getIdCuenta()));

                //System.out.println("1) "+reciboForma.getField("apertura"+cuenta.getIdCuenta()));
                reciboForma.setField("apertura" + cuenta.getIdCuenta(), Convertidor.dateToStringCDC(cuenta.getFechaApertura()));
                //System.out.println("2) "+reciboForma.getField("apertura"+cuenta.getIdCuenta()));

                //System.out.println("1) "+reciboForma.getField("cierre"+cuenta.getIdCuenta()));
                reciboForma.setField("cierre" + cuenta.getIdCuenta(), Convertidor.dateToStringCDC(cuenta.getFechaCierre()));
                //System.out.println("2) "+reciboForma.getField("cierre"+cuenta.getIdCuenta()));

                //System.out.println("1) "+reciboForma.getField("pago"+cuenta.getIdCuenta()));
                reciboForma.setField("pago" + cuenta.getIdCuenta(), Convertidor.dateToStringCDC(cuenta.getFechaUltimoPago()));
                //System.out.println("2) "+reciboForma.getField("pago"+cuenta.getIdCuenta()));

                //System.out.println("1) "+reciboForma.getField("historial"+cuenta.getIdCuenta()));
                reciboForma.setField("historial" + cuenta.getIdCuenta(), cuenta.getHistorial());
                //System.out.println("2) "+reciboForma.getField("historial"+cuenta.getIdCuenta()));

                //System.out.println("1) "+reciboForma.getField("atraso"+cuenta.getIdCuenta()));
                reciboForma.setField("atraso" + cuenta.getIdCuenta(), cuenta.getPeorAtraso() + "");
                //System.out.println("2) "+reciboForma.getField("atraso"+cuenta.getIdCuenta()));

                //System.out.println("1) "+reciboForma.getField("monto"+cuenta.getIdCuenta()));
                reciboForma.setField("monto" + cuenta.getIdCuenta(), cuenta.getSaldoVencidoPeorAtraso() + "");
                //System.out.println("2) "+reciboForma.getField("monto"+cuenta.getIdCuenta()));

                //System.out.println("1) "+reciboForma.getField("fecha"+cuenta.getIdCuenta()));
                reciboForma.setField("fecha" + cuenta.getIdCuenta(), Convertidor.dateToStringCDC(cuenta.getFechaPeorAtraso()));
                //System.out.println("2) "+reciboForma.getField("fecha"+cuenta.getIdCuenta()));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void writeConsultas() {

        try {
            ConsultaEfectuadaVO consulta = null;
            Iterator iterator = reporte.getConsultas().iterator();

            while (iterator.hasNext()) {

                consulta = (ConsultaEfectuadaVO) iterator.next();

                reciboForma.setField("conFecConsulta" + consulta.getIdConsulta(), Convertidor.dateToStringCDC(consulta.getFechaConsulta()));
                reciboForma.setField("conOtorgante" + consulta.getIdConsulta(), consulta.getOtorgante());
                reciboForma.setField("conTipoCredito" + consulta.getIdConsulta(), consulta.getTipoCredito());
                reciboForma.setField("conMonto" + consulta.getIdConsulta(), FormatUtil.formatDoble(consulta.getMonto()));
                reciboForma.setField("conMoneda" + consulta.getIdConsulta(), consulta.getMoneda());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init() {
        try {
            //selectPlantilla();
            reader = new PdfReader(selectPlantilla());
            reciboPDF = new PdfStamper(reader, out);
            reciboForma = reciboPDF.getAcroFields();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String selectPlantilla() {

        String plantilla = "";

        if (reporte.getCuentas().size() == 0) {
            plantilla = ClientesConstants.RUTA_BASE_ARCHIVOS + "CirculoCredito\\CirculoDeCredito_SC.pdf";
        } else if (reporte.getCuentas().size() <= 4) {
            plantilla = ClientesConstants.RUTA_BASE_ARCHIVOS + "CirculoCredito\\CirculoDeCredito.pdf";
        } else if (reporte.getCuentas().size() <= 6) {
            plantilla = ClientesConstants.RUTA_BASE_ARCHIVOS + "CirculoCredito\\CirculoDeCredito2.pdf";
        } else if (reporte.getCuentas().size() <= 15) {
            plantilla = ClientesConstants.RUTA_BASE_ARCHIVOS + "CirculoCredito\\CirculoDeCredito3.pdf";
        } else if (reporte.getCuentas().size() <= 27) {
            plantilla = ClientesConstants.RUTA_BASE_ARCHIVOS + "CirculoCredito\\CirculoDeCredito4.pdf";
        } else {
            plantilla = ClientesConstants.RUTA_BASE_ARCHIVOS + "CirculoCredito\\CirculoDeCredito5.pdf";
        }
        System.out.println("El numero de cuentas es: " + reporte.getCuentas().size() + " " + plantilla);
        return plantilla;
    }
}
