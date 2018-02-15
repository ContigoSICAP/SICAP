package com.sicap.clientes.util.cartera;

import com.sicap.clientes.dao.AjusteCreditoDAO;
import com.sicap.clientes.dao.CuentasBancariasDAO;
import com.sicap.clientes.dao.SucursalDAO;
import com.sicap.clientes.exceptions.ClientesException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.text.DecimalFormat;

import com.sicap.clientes.vo.cartera.TransaccionesContabVO;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.FormatUtil;
import com.sicap.clientes.vo.SucursalVO;
import java.io.FileOutputStream;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class GeneraArchivoContpaqHelper {

    private static Logger myLogger = Logger.getLogger(GeneraArchivoContpaqHelper.class);
    private List<String> file = new ArrayList<String>();
    Calendar cal = Calendar.getInstance();
    int mes = cal.get(Calendar.MONTH);
    int hora = cal.get(Calendar.HOUR_OF_DAY);
    public int envio = 0;
    String fnBNTE = "", fnBNMX = "", fnBBVA = "", fnSNTDR = "", fnSCOT = "", fnBAJI = "";
    HSSFWorkbook workbookBNTE = null, workbookBNMX = null, workbookBBVA = null, workbookSNTDR= null, workbookSCOT = null, workbookBAJI = null;
    HSSFSheet sheetBNTE = null, sheetBNMX = null, sheetBBVA = null, sheetSNTDR = null, sheetSCOT= null, sheetBAJI= null;
    HSSFRow rowBNTE = null, rowBNMX = null, rowBBVA = null, rowSNTDR= null, rowSCOT= null, rowBAJI= null;
    FileOutputStream foutBNTE = null, foutBNMX = null, foutBBVA = null, foutSNTDR= null, foutSCOT = null, foutBAJI = null;

    public boolean generaArchivo(ArrayList registro, int contador) throws Exception {
        
        boolean error = true;
        try {
            SucursalVO sucursal = new SucursalVO();
            SucursalDAO sucursalDAO = new SucursalDAO();
            TransaccionesContabVO transaccion_cont = null;
            StringBuffer buffer = new StringBuffer();
            String periodoReporte = Convertidor.dateToString(new Date(), "yyyyMMdd");
            String filename = "CNT";
            int hora = cal.get(Calendar.HOUR_OF_DAY);
            filename += periodoReporte;
            filename += FormatUtil.completaCadena(String.valueOf(hora), '0', 2, "L");
            transaccion_cont = (TransaccionesContabVO) registro.get(0);
            filename += FormatUtil.completaCadena(transaccion_cont.tipoTransaccion, '0', 3, "L");
            filename += FormatUtil.completaCadena("" + transaccion_cont.fondeador, '0', 3, "L");
            filename += FormatUtil.completaCadena(String.valueOf(contador), '0', 3, "L");
            filename += ".TXT";
            BufferedWriter out = new BufferedWriter(new FileWriter(ClientesConstants.RUTA_BASE_ARCHIVOS + "Contabilidad\\" + filename));
            int centro_costos = 0;
            String concepto = "";
            String idDiario = "0";
            String sistema_origen = "11";
            String impresa = "0";
            String clase = "1";
            String tipo_poliza_ingreso = "1";
            String tipo_poliza_egreso = "2";
            String tipo_poliza_diario = "3";
            String tipo_poliza_mov = "D";
            String mov_poliza = "M1";
            String referencia = "";
            String tipo_movimiento = "1";
            String ajuste = "0";
            String monto_moneda_ex = "0.0";
            buffer.append(FormatUtil.completaCadena("P", ' ', 3, "R"));
            buffer.append(FormatUtil.completaCadena(periodoReporte, ' ', 9, "R"));
            for (int i = 0; i < registro.size(); i++) {
                transaccion_cont = (TransaccionesContabVO) registro.get(i);
                if (i == 0) {
                    concepto = CatalogoHelper.getDescripcionTipoTransaccion(transaccion_cont.tipoTransaccion);
                    if (transaccion_cont.tipoTransaccion.contentEquals(ClientesConstants.DESEMBOLSO)) {
                        buffer.append(FormatUtil.completaCadena(tipo_poliza_egreso, ' ', 4, "L"));
                    } else if (transaccion_cont.tipoTransaccion.contentEquals(ClientesConstants.REGISTRO_PAGO)) {
                        buffer.append(FormatUtil.completaCadena(tipo_poliza_ingreso, ' ', 4, "L"));
                    } else {
                        buffer.append(FormatUtil.completaCadena(tipo_poliza_diario, ' ', 4, "L"));
                    }
                    buffer.append(FormatUtil.completaCadena("0", ' ', 10, "L")); // se incluye numero de poliza 0 para que el Contpac le asigne uno
                    buffer.append(FormatUtil.completaCadena(clase, ' ', 2, "L"));
                    buffer.append(FormatUtil.completaCadena(null, ' ', 1, "R")); // se utiliza un espacio por un error en la defincion de Contpac
                    buffer.append(FormatUtil.completaCadena(idDiario, ' ', 11, "R"));
                    buffer.append(FormatUtil.completaCadena(concepto, ' ', 101, "R"));  // completa el folio
                    buffer.append(FormatUtil.completaCadena(sistema_origen, ' ', 4, "R"));
                    buffer.append(FormatUtil.completaCadena(impresa, ' ', 2, "R"));
                    buffer.append(FormatUtil.completaCadena(ajuste, ' ', 2, "R"));
                    out.write(buffer.toString());
                    out.newLine();
                    buffer = new StringBuffer();
                }
                buffer = new StringBuffer();
                Double monto = transaccion_cont.monto;
                DecimalFormat df = new DecimalFormat("#");
                df.setMaximumFractionDigits(2);
                tipo_movimiento = (transaccion_cont.tipoMovimiento.equals(String.valueOf("D")) ? "0" : "1");
                buffer.append(FormatUtil.completaCadena(mov_poliza, ' ', 3, "R"));
                buffer.append(FormatUtil.completaCadena(transaccion_cont.numCuenta, ' ', 31, "R"));
                buffer.append(FormatUtil.completaCadena(referencia, ' ', 21, "R"));
                buffer.append(FormatUtil.completaCadena(tipo_movimiento, ' ', 2, "R"));
                buffer.append(FormatUtil.completaCadena(df.format(monto), ' ', 21, "R"));  //monto
                buffer.append(FormatUtil.completaCadena(idDiario, ' ', 11, "R"));
                buffer.append(FormatUtil.completaCadena(monto_moneda_ex, ' ', 21, "R"));
                buffer.append(FormatUtil.completaCadena(concepto, ' ', 101, "R"));
                sucursal = sucursalDAO.getSucursalDirecto(transaccion_cont.centroCostos);
                centro_costos = sucursal.codigo;
                if (transaccion_cont.numCuenta.startsWith("1102") || transaccion_cont.numCuenta.startsWith("1401") || transaccion_cont.numCuenta.startsWith("2401") || transaccion_cont.numCuenta.startsWith("241101000002")) {
                    buffer.append(FormatUtil.completaCadena(" " + "", ' ', 5, "R"));
                } else {
                    buffer.append(FormatUtil.completaCadena(centro_costos + "", ' ', 5, "R")); // seg_neg                                    
                }
                out.write(buffer.toString());
                out.newLine();
            }
            out.close();
            error = false;
        } catch (Exception e) {
            myLogger.error("Excepcion en Main Generacion Archivo CONTPAQ : ", e);
            throw new ClientesException(e.getMessage());
        }
        return error;
    }
    
    public boolean generaArchivoSAP(ArrayList registro, int contador, String ejercicio, Date fechaCorte, int banco, int contadorGral) throws Exception {

        boolean error = true;
        try {
            SucursalVO sucursal = new SucursalVO();
            SucursalDAO sucursalDAO = new SucursalDAO();
            CuentasBancariasDAO cuentasDAO = new CuentasBancariasDAO();
            TransaccionesContabVO transaccion_cont = null;
            transaccion_cont = (TransaccionesContabVO) registro.get(0);
            String strContador = FormatUtil.completaCadena(String.valueOf(contadorGral), '0', 3, "L");
            String periodoReporte = Convertidor.dateToString(fechaCorte, "yyyyMMdd");
            String strfechaCorte = Convertidor.dateToString(fechaCorte, "ddMMyy");
            String filename = ClientesConstants.RUTA_BASE_ARCHIVOS + "Contabilidad\\"+strContador+"SAP_";
            String documento = transaccion_cont.tipoTransaccion;
            if(banco == ClientesConstants.ID_BANCO_BANORTE){
                filename = filename.replace("SAP_", "SAP_BNTE");
                documento = "BNTE";
            }else if(banco == ClientesConstants.ID_BANCO_BANAMEX){
                filename = filename.replace("SAP_", "SAP_BNMX");
                documento = "BNMX";
            }else if(banco == ClientesConstants.ID_BANCO_BANCOMER){
                filename = filename.replace("SAP_", "SAP_BBVA");
                documento = "BBVA";
            }else if(banco == ClientesConstants.ID_BANCO_SANTANDER){
                filename = filename.replace("SAP_", "SAP_SNTDR");
                documento = "SNTDR";
            }else if(banco == ClientesConstants.ID_BANCO_SCOTIABANK){
                filename = filename.replace("SAP_", "SAP_SCOT");
                documento = "SCOT";
            }else if(banco == ClientesConstants.ID_BANCO_BANBAJIO){
                filename = filename.replace("SAP_", "SAP_BAJI");
                documento = "BAJI";
            }
            String tipoDocumento = "";
            int hora = cal.get(Calendar.HOUR_OF_DAY);
            filename += periodoReporte;
            filename += ejercicio;
            filename += FormatUtil.completaCadena(String.valueOf(hora), '0', 2, "L");
            filename += FormatUtil.completaCadena(transaccion_cont.tipoTransaccion, '0', 3, "L");
//			filename += FormatUtil.completaCadena(""+transaccion_cont.centroCostos,'0', 3, "L");
            filename += FormatUtil.completaCadena("" + transaccion_cont.fondeador, '0', 3, "L");
            filename += FormatUtil.completaCadena(String.valueOf(contador), '0', 3, "L");
            filename += ".xls";
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet sheet = workbook.createSheet(transaccion_cont.tipoTransaccion);
            if(transaccion_cont.tipoTransaccion.equals(ClientesConstants.REGISTRO_PAGO) || transaccion_cont.tipoTransaccion.equals(ClientesConstants.REGISTRO_PAGO_GARANTIA)){
                if(banco != 0)
                    tipoDocumento = "ZI";
                else
                    tipoDocumento = "SA";
            } else if(transaccion_cont.tipoTransaccion.equals(ClientesConstants.DESEMBOLSO) || transaccion_cont.tipoTransaccion.equals(ClientesConstants.DEVOLUCION_SALDO_FAVOR)){
                tipoDocumento = "ZD";
            } else if(transaccion_cont.tipoTransaccion.equals("CAN_DES") || transaccion_cont.tipoTransaccion.equals("CAN_REG") || transaccion_cont.tipoTransaccion.equals("CAN_RGA") || transaccion_cont.tipoTransaccion.equals(ClientesConstants.DEVOLUCION)||transaccion_cont.tipoTransaccion.equals(ClientesConstants.DEVOLUCION_ADICIONAL)||transaccion_cont.tipoTransaccion.equals(ClientesConstants.DESEMBOLSO_ADICIONAL)||transaccion_cont.tipoTransaccion.equals(ClientesConstants.DEVOLUCION_INTERCICLO)||transaccion_cont.tipoTransaccion.equals(ClientesConstants.DESEMBOLSO_INTERCICLO)){
                if(banco != 0)
                    tipoDocumento = "ZC";
                else
                    tipoDocumento = "AB";
            } else if(transaccion_cont.tipoTransaccion.equals(ClientesConstants.CASTIGO) || transaccion_cont.tipoTransaccion.equals(ClientesConstants.C_ESTADO_VENCIDO) || transaccion_cont.tipoTransaccion.equals(ClientesConstants.CONDONACION) || transaccion_cont.tipoTransaccion.equals(ClientesConstants.MULTA) || transaccion_cont.tipoTransaccion.equals(ClientesConstants.PAGO) || transaccion_cont.tipoTransaccion.equals(ClientesConstants.PROVISION) || transaccion_cont.tipoTransaccion.equals(ClientesConstants.PAGO_GARANTIA)|| transaccion_cont.tipoTransaccion.equals(ClientesConstants.CAMBIO_FONDEADOR_INGRESO)|| transaccion_cont.tipoTransaccion.equals(ClientesConstants.CAMBIO_FONDEADOR_SALIDA)|| transaccion_cont.tipoTransaccion.equals(ClientesConstants.CARTERA_SALIDA_BURSA)|| transaccion_cont.tipoTransaccion.equals(ClientesConstants.CARTERA_INGRESO_BURSA)){
                tipoDocumento = "SA";
            } else if(transaccion_cont.tipoTransaccion.equals("CAN_CON") || transaccion_cont.tipoTransaccion.equals("CAN_PAG") || transaccion_cont.tipoTransaccion.equals("CAN_PRV") || transaccion_cont.tipoTransaccion.equals("CAN_MUL") || transaccion_cont.tipoTransaccion.equals("CAN_PGA")){
                tipoDocumento = "AB";            
            } else{
                myLogger.error("SIN CATALOGO DE ASIGNACION");
            }
            /*NOMBRE COLUMNAS*/
            HSSFRow rowhead = sheet.createRow(0);
            rowhead.createCell(0).setCellValue("C");
            rowhead.createCell(1).setCellValue("BKPF-BUKRS");
            rowhead.createCell(2).setCellValue("BKPF-BLART");
            rowhead.createCell(3).setCellValue("BKPF-BUDAT");
            rowhead.createCell(4).setCellValue("BKPF-BLDAT");
            rowhead.createCell(5).setCellValue("BKPF-MONAT");
            rowhead.createCell(6).setCellValue("BKPF-XBLNR");
            rowhead.createCell(7).setCellValue("BKPF-BKTXT");
            rowhead.createCell(8).setCellValue("BKPF-WAERS");
            rowhead.createCell(9).setCellValue("BSEG-BSCHL");
            rowhead.createCell(10).setCellValue("BSEG-HKONT");
            rowhead.createCell(11).setCellValue("BSEG-ZUONR");
            rowhead.createCell(12).setCellValue("BSEG-SGTXT");
            rowhead.createCell(13).setCellValue("BSEG-WRBTR");
            rowhead.createCell(14).setCellValue("BSEG-KOSTL");
            rowhead.createCell(15).setCellValue("BSEG-PRCTR");
            /*if(tipoDocumento.equals("ZI") || tipoDocumento.equals("ZD") || tipoDocumento.equals("ZC"))
                cabezeraBancos(rowhead, 0);*/

            String claveContabilizacion = "";
            String centroCosto = "";
            String centroBeneficio = "";
            String concepto = "";
            String tipoTrans = "";
            String strConcepto = "";
            Double monto = 0.00;
            DecimalFormat df = new DecimalFormat("#");
            
            concepto = CatalogoHelper.getDescripcionTipoTransaccion(transaccion_cont.tipoTransaccion);
            strConcepto = strContador+concepto;
            /*CABECERA*/
            HSSFRow row = sheet.createRow(1);
            row.createCell(0).setCellValue("C");
            row.createCell(1).setCellValue("CEGE");//BKPF-BUKRS
            row.createCell(2).setCellValue(tipoDocumento);//BKPF-BLART
            row.createCell(3).setCellValue(Convertidor.dateToString(fechaCorte, "dd/MM/yyyy").substring(0, 10));//BKPF-BUDAT
            row.createCell(4).setCellValue(Convertidor.dateToString(fechaCorte, "dd/MM/yyyy").substring(0, 10));//BKPF-BLDAT
            row.createCell(5).setCellValue(ejercicio.substring(5, 7));//BKPF-MONAT
            row.createCell(6).setCellValue(documento.length()>16 ? documento.substring(16) : documento);//BKPF-XBLNR
            row.createCell(7).setCellValue(strConcepto.length()>25 ? strConcepto.substring(0, 25) : strConcepto);//BKPF-BKTXT
            row.createCell(8).setCellValue("");//BKPF-WAERS
            row.createCell(9).setCellValue("");//BSEG-BSCHL
            row.createCell(10).setCellValue("");//BSEG-HKONT
            row.createCell(11).setCellValue("");//BSEG-ZUONR
            row.createCell(12).setCellValue("");//BSEG-SGTXT
            row.createCell(13).setCellValue("");//BSEG-WRBTR
            row.createCell(14).setCellValue("");//BSEG-KOSTL
            row.createCell(15).setCellValue("");//BSEG-PRCTR
            /*if(tipoDocumento.equals("ZI") || tipoDocumento.equals("ZD") || tipoDocumento.equals("ZC"))
                cabezeraBancos(row, 1);*/
            /*DETALLE*/
            for (int i = 0; i < registro.size(); i++) {
                //Obtiene la transaccion
                transaccion_cont = (TransaccionesContabVO) registro.get(i);
                // Se obtiene el segmento de segmento del movimiento
                if(transaccion_cont.tipoMovimiento.equals("H"))
                    claveContabilizacion = "50";
                else
                    claveContabilizacion = "40";
                monto = transaccion_cont.monto;
                df.setMaximumFractionDigits(2);
                sucursal = sucursalDAO.getSucursalDirecto(transaccion_cont.centroCostos);
                centroCosto = "";
                centroBeneficio = "";
                if(!transaccion_cont.numCuentaSap.substring(0, 4).equals("1102") && !transaccion_cont.numCuentaSap.substring(0, 4).equals("1401") && !transaccion_cont.numCuentaSap.substring(0, 4).equals("2401")){
                    if(transaccion_cont.numCuentaSap.substring(0, 1).equals("5"))
                        centroBeneficio = sucursal.codigoSAP;
                    else
                        centroCosto = sucursal.codigoSAP;
                }
                //tipoTrans = cuentasDAO.getCuentaBancaria(0);
                if(transaccion_cont.numCuentaSap.equals("1102004002"))
                    tipoTrans = "BNT885028755"+strfechaCorte;
                else if(transaccion_cont.numCuentaSap.equals("1102102002"))
                    tipoTrans = "REG197065337"+strfechaCorte;
                else if(transaccion_cont.numCuentaSap.equals("1102051002"))
                    tipoTrans = "BNMX862379"+strfechaCorte;
                else if(transaccion_cont.numCuentaSap.equals("1102201002"))
                    tipoTrans = "REG104132031"+strfechaCorte;
                else if(transaccion_cont.numCuentaSap.equals("1102151002"))//CUENTA DE BAJIO TEMPORAL
                    tipoTrans = "BAJI48231"+strfechaCorte;
                else if(transaccion_cont.numCuentaSap.equals("1102005003") || transaccion_cont.numCuentaSap.equals("1102053003") || transaccion_cont.numCuentaSap.equals("1102004003"))
                    tipoTrans = FormatUtil.completaCadena(sucursal.getIdSucursal()+"", '0', 3, "L")+"237385495";
                else if(transaccion_cont.numCuentaSap.equals("1102109003"))//Bancomer
                    tipoTrans = "DES197065337"+strfechaCorte;
                else if(transaccion_cont.numCuentaSap.equals("1102054003") || transaccion_cont.numCuentaSap.equals("1102051003"))
                    tipoTrans = FormatUtil.completaCadena(sucursal.getIdSucursal()+"", '0', 3, "L")+"1443957";
                else if(transaccion_cont.numCuentaSap.equals("1102301003"))//Scotia
                    tipoTrans = "DES104132031"+strfechaCorte;
                else
                    tipoTrans = transaccion_cont.tipoTransaccion.length()>18 ? transaccion_cont.tipoTransaccion.substring(0, 18) : transaccion_cont.tipoTransaccion;
                row = sheet.createRow(row.getRowNum()+1);
                row.createCell(0).setCellValue("");
                row.createCell(1).setCellValue("");//BKPF-BUKRS
                row.createCell(2).setCellValue("");//BKPF-BLART
                row.createCell(3).setCellValue("");//BKPF-BUDAT
                row.createCell(4).setCellValue("");//BKPF-BLDAT
                row.createCell(5).setCellValue("");//BKPF-MONAT
                row.createCell(6).setCellValue("");//BKPF-XBLNR
                row.createCell(7).setCellValue("");//BKPF-BKTXT
                row.createCell(8).setCellValue("MXN");//BKPF-WAERS
                row.createCell(9).setCellValue(claveContabilizacion.length()>2 ? claveContabilizacion.substring(0, 2) : claveContabilizacion);//BSEG-BSCHL
                row.createCell(10).setCellValue(transaccion_cont.numCuentaSap.length()>10 ? transaccion_cont.numCuentaSap.substring(0, 10) : transaccion_cont.numCuentaSap);//BSEG-HKONT
                //row.createCell(11).setCellValue(transaccion_cont.tipoTransaccion.length()>18 ? transaccion_cont.tipoTransaccion.substring(0, 18) : transaccion_cont.tipoTransaccion);//BSEG-ZUONR
                row.createCell(11).setCellValue(tipoTrans);//BSEG-ZUONR
                row.createCell(12).setCellValue(concepto.length()>50 ? concepto.substring(0, 50) : concepto);//BSEG-SGTXT
                row.createCell(13).setCellValue(df.format(monto));//BSEG-WRBTR .substring(0, 13)
                row.createCell(14).setCellValue(centroCosto.length()>10 ? centroCosto.substring(0, 10) : centroCosto);//BSEG-KOSTL
                row.createCell(15).setCellValue(centroBeneficio.length()>10 ? centroBeneficio.substring(0, 10) : centroBeneficio);//BSEG-PRCTR
                /*if(tipoDocumento.equals("ZI") || tipoDocumento.equals("ZD") || tipoDocumento.equals("ZC")){
                    if(!cuerpoBancos(row, transaccion_cont.numCuentaSap)){
                        sheet.removeRow(row);
                        row.setRowNum(row.getRowNum()-1);
                    }
                }*/
            }
            FileOutputStream fileOut = new FileOutputStream(filename);
            workbook.write(fileOut);
            fileOut.close();
            /*if(tipoDocumento.equals("ZI") || tipoDocumento.equals("ZD") || tipoDocumento.equals("ZC"))
                    generaBancos();*/
            error = false;
        } catch (Exception e) {
            myLogger.error("Excepcion en Main Generacion Archivo SAP : ", e);
            throw new ClientesException(e.getMessage());
        }
        return error;
    }
    
    private void archivosBancos(String filename, String documento) throws ClientesException{
        try {
            fnBNTE = filename.replace("SAP_", "SAP_BNTE");
            workbookBNTE = new HSSFWorkbook();
            sheetBNTE = workbookBNTE.createSheet(documento);
            fnBNMX = filename.replace("SAP_", "SAP_BNMX");
            workbookBNMX = new HSSFWorkbook();
            sheetBNMX = workbookBNMX.createSheet(documento);
            fnBBVA = filename.replace("SAP_", "SAP_BBVA");
            workbookBBVA = new HSSFWorkbook();
            sheetBBVA = workbookBBVA.createSheet(documento);
            fnSNTDR = filename.replace("SAP_", "SAP_SNTDR");
            workbookSNTDR = new HSSFWorkbook();
            sheetSNTDR = workbookSNTDR.createSheet(documento);
            fnSCOT = filename.replace("SAP_", "SAP_SCOT");
            workbookSCOT = new HSSFWorkbook();
            sheetSCOT = workbookSCOT.createSheet(documento);
            fnBAJI = filename.replace("SAP_", "SAP_BAJI");
            workbookBAJI = new HSSFWorkbook();
            sheetBAJI = workbookBAJI.createSheet(documento);
        } catch (Exception e) {
            myLogger.error("Error archivosBancos SAP : ", e);
            throw new ClientesException(e.getMessage());
        }
    }
    
    private void cabezeraBancos(HSSFRow row, int linea) throws ClientesException{
        try {
            rowBNTE = sheetBNTE.createRow(linea);
            rowBNMX = sheetBNMX.createRow(linea);
            rowBBVA = sheetBBVA.createRow(linea);
            rowSNTDR = sheetSNTDR.createRow(linea);
            rowSCOT = sheetSCOT.createRow(linea);
            rowBAJI = sheetBAJI.createRow(linea);
            for (int i = 0; i < 15; i++) {
                rowBNTE.createCell(i).setCellValue(row.getCell(i).getStringCellValue());
                rowBNMX.createCell(i).setCellValue(row.getCell(i).getStringCellValue());
                rowBBVA.createCell(i).setCellValue(row.getCell(i).getStringCellValue());
                rowSNTDR.createCell(i).setCellValue(row.getCell(i).getStringCellValue());
                rowSCOT.createCell(i).setCellValue(row.getCell(i).getStringCellValue());
                rowBAJI.createCell(i).setCellValue(row.getCell(i).getStringCellValue());
            }
        } catch (Exception e) {
            myLogger.error("Error cabezeraBancos SAP : ", e);
            throw new ClientesException(e.getMessage());
        }
    }
    
    private boolean cuerpoBancos(HSSFRow row, String cuenta) throws ClientesException{
        boolean corresponsalia = true;
        try {
            if(cuenta.equals("1102004002") || cuenta.equals("1102005003") || cuenta.equals("1102053003") || cuenta.equals("1102004003")){
                corresponsalia = false;
                rowBNTE = sheetBNTE.createRow(rowBNTE.getRowNum()+1);
                for (int i = 0; i < 15; i++) {
                    rowBNTE.createCell(i).setCellValue(row.getCell(i).getStringCellValue());
                }
            }
            if(cuenta.equals("1102051002") || cuenta.equals("1102054003") || cuenta.equals("1102051003")){
                corresponsalia = false;
                rowBNMX = sheetBNMX.createRow(rowBNMX.getRowNum()+1);
                for (int i = 0; i < 15; i++) {
                    rowBNMX.createCell(i).setCellValue(row.getCell(i).getStringCellValue());
                }
            }
            if(cuenta.equals("1102102002") || cuenta.equals("1102102003")){
                corresponsalia = false;
                rowBBVA = sheetBBVA.createRow(rowBBVA.getRowNum()+1);
                for (int i = 0; i < 15; i++) {
                    rowBBVA.createCell(i).setCellValue(row.getCell(i).getStringCellValue());
                }
            }
            if(cuenta.equals("1102201002") || cuenta.equals("1102201003")){
                corresponsalia = false;
                rowSCOT = sheetSCOT.createRow(rowSCOT.getRowNum()+1);
                for (int i = 0; i < 15; i++) {
                    rowSCOT.createCell(i).setCellValue(row.getCell(i).getStringCellValue());
                }
            }
            if(cuenta.equals("1102151002")){
                corresponsalia = false;
                rowBAJI = sheetBAJI.createRow(rowBAJI.getRowNum()+1);
                for (int i = 0; i < 15; i++) {
                    rowBAJI.createCell(i).setCellValue(row.getCell(i).getStringCellValue());
                }
            }
        } catch (Exception e) {
            myLogger.error("Error cuerpoBancos SAP : ", e);
            throw new ClientesException(e.getMessage());
        }
        return corresponsalia;
    }
    
    private void generaBancos() throws ClientesException{
        try {
            if(rowBNTE.getRowNum() > 1){
                foutBNTE = new FileOutputStream(fnBNTE);
                workbookBNTE.write(foutBNTE);
                foutBNTE.close();
            }
            if(rowBNMX.getRowNum() > 1){
                foutBNMX = new FileOutputStream(fnBNMX);
                workbookBNMX.write(foutBNMX);
                foutBNMX.close();
            }
            if(rowBBVA.getRowNum() > 1){
                foutBBVA = new FileOutputStream(fnBBVA);
                workbookBBVA.write(foutBBVA);
                foutBBVA.close();
            }
            if(rowSNTDR.getRowNum() > 1){
                foutSNTDR = new FileOutputStream(fnSNTDR);
                workbookSNTDR.write(foutSNTDR);
                foutSNTDR.close();
            }
            if(rowSCOT.getRowNum() > 1){
                foutSCOT = new FileOutputStream(fnSCOT);
                workbookSCOT.write(foutSCOT);
                foutSCOT.close();
            }
            if(rowBAJI.getRowNum() > 1){
                foutBAJI = new FileOutputStream(fnBAJI);
                workbookBAJI.write(foutBAJI);
                foutBAJI.close();
            }
        } catch (Exception e) {
            myLogger.error("Error generaBancos SAP : ", e);
            throw new ClientesException(e.getMessage());
        }
    }

}
