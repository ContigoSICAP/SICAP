package com.sicap.clientes.helpers;

import com.sicap.clientes.dao.BitacoraDAO;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import com.sicap.clientes.dao.OrdenDePagoDAO;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FechasUtil;
import com.sicap.clientes.util.FormatUtil;
import com.sicap.clientes.vo.BitacoraVO;
import com.sicap.clientes.vo.OrdenDePagoVO;
import com.sicap.clientes.vo.ParametroVO;
import com.sicap.clientes.vo.TarjetasVO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class OrdenesDePagoHelper {

    private List<String> file = new ArrayList<String>();
    private List list = null;
    private int contador = 0;
    private int numDispersiones = 0;
    private double impDispersiones = 0;
    private int numCancelaciones = 0;
    private double impCancelaciones = 0;
    private int numModificaciones = 0;
    private double impModificaciones = 0;
    private int numSolicitudes = 0;
    private double importeArchivo = 0;
    public int envio = 0;
    public String mensaje = null;
    static final char numerico = '0';
    static final char alfanum = ' ';
    private static Logger myLogger = Logger.getLogger(OrdenesDePagoHelper.class);

    public OrdenesDePagoHelper() {
    }

    public OrdenesDePagoHelper(List _list) {
        try {
            this.list = _list;
            numSolicitudes = _list.size();
            getImporteArchivo();
            envio = new OrdenDePagoDAO().getNumeroEnvio() + 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public OrdenesDePagoHelper(List _list, int banco) {
        try {
            this.list = _list;
            numSolicitudes = _list.size();
            getImporteArchivo();
            envio = new OrdenDePagoDAO().getNumeroEnvio(banco) + 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public OrdenesDePagoHelper(List _list, int numEnvio, double monto) {
        try {
            this.list = _list;
            numSolicitudes = _list.size();
            importeArchivo = monto;
            envio = numEnvio;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List creaOrdenDePagoBanorte() {
        OrdenDePagoVO oPago = null;
        Iterator iterator = list.iterator();

        creaEncabezadoBanorte();
        while (iterator.hasNext()) {
            oPago = (OrdenDePagoVO) iterator.next();
            creaDetalleBanorte(oPago);
        }
        return file;
    }

    public List creaOrdenDePagoBancomer() {
        OrdenDePagoVO oPago = null;
        Iterator iterator = list.iterator();

        creaEncabezadoBancomer();
        while (iterator.hasNext()) {
            oPago = (OrdenDePagoVO) iterator.next();
            creaDetalleBancomer(oPago);
        }
        creaTrailerBancomer();
        return file;
    }

    public List creaOrdenDePagoBanamex() {
        OrdenDePagoVO oPago = null;
        //Iterator iterator = list.iterator();
        Iterator iteratorDet = list.iterator();
        int monto = 0;
        int conta = 0;

        registroControlBanamex();
        /*while (iterator.hasNext()) {
         oPago = (OrdenDePagoVO) iterator.next();
         monto += (int) (FormatUtil.roundDouble(oPago.getMonto(), 2) * 100);
         conta++;
         }*/
        //registroGlobalBanamex(monto);
        registroGlobalBanamex();
        while (iteratorDet.hasNext()) {
            oPago = (OrdenDePagoVO) iteratorDet.next();
            creaDetalleBanamex(oPago);
        }
        //registroTotalesBanamex(monto, conta);
        registroTotalesBanamex();
        return file;
    }

    public List creaOrdenDePagoScotia() {
        OrdenDePagoVO oPago = null;
        Iterator iterator = list.iterator();

        creaEncabezadoScotia();
        while (iterator.hasNext()) {
            oPago = (OrdenDePagoVO) iterator.next();
            creaDetalleScotia(oPago);
        }
        creaTrailerScotia();
        return file;
    }

    public List creaOrdenDePagoSantander(Date[] fechasInhabiles) {
        OrdenDePagoVO oPago = null;
        Iterator iterator = list.iterator();

        while (iterator.hasNext()) {
            oPago = (OrdenDePagoVO) iterator.next();
            creaDetalleSantander(oPago, fechasInhabiles);
        }
        return file;
    }

    public List creaCsvSantander() {
        OrdenDePagoVO oPago = null;
        Iterator iterator = list.iterator();
        creaEncabezadoModSantander();
        while (iterator.hasNext()) {
            oPago = (OrdenDePagoVO) iterator.next();
            creaDetalleSantanderMod(oPago);
        }
        return file;
    }

    public List creaOrdenDePagoTelecom(int envioIni, int envioFin) throws Exception {
        OrdenDePagoVO oPago = null;
        Iterator iterator = list.iterator();

        creaEncabezadoTelecom(envioIni, envioFin);
        while (iterator.hasNext()) {
            oPago = (OrdenDePagoVO) iterator.next();
            creaDetalleTelecom(oPago);
        }
        creaPieTelecom(envioIni, envioFin);
        return file;
    }

    private String creaEncabezadoBanorte() {

        StringBuffer buffer = new StringBuffer();
        String CVE_BANORTE = CatalogoHelper.getParametro("CVE_EMISORA_BANORTE");

        try {
            String totalDisp = FormatUtil.formatDoble("###00", impDispersiones * 100);
            String totalCanc = FormatUtil.formatDoble("###00", impCancelaciones * 100);
            String totalMod = FormatUtil.formatDoble("###00", impModificaciones * 100);
            String totalArchivo = FormatUtil.formatDoble("###00", importeArchivo * 100);
            String fecha = Convertidor.dateToString(new Date(), "yyyyMMdd");

            buffer.append(FormatUtil.completaCadena("0", numerico, 1, "L")); //CAE-TIP-REG
            buffer.append(FormatUtil.completaCadena(CVE_BANORTE, numerico, 5, "L"));//CAE-EMPRESA 
            buffer.append(FormatUtil.completaCadena("60", numerico, 2, "L")); //CAE-TIPFILE
            buffer.append(FormatUtil.completaCadena(fecha, numerico, 8, "L")); //CAE-FEC-PRE
            buffer.append(FormatUtil.completaCadena(envio + "", numerico, 4, "L")); //CAE-CONSECU
            buffer.append(FormatUtil.completaCadena(numDispersiones + "", numerico, 5, "L")); //CAE-REG-DOC
            buffer.append(FormatUtil.completaCadena(totalDisp, numerico, 18, "L")); //CAE-IMP-DOC
            buffer.append(FormatUtil.completaCadena(numCancelaciones + "", numerico, 5, "L")); //CAE-REG-CAN
            buffer.append(FormatUtil.completaCadena(totalCanc, numerico, 18, "L")); //CAE-IMP-CAN
            buffer.append(FormatUtil.completaCadena(numModificaciones + "", numerico, 5, "L")); //CAE-REG-MOD
            buffer.append(FormatUtil.completaCadena(totalMod, numerico, 18, "L")); //CAE-IMP-MOD
            buffer.append(FormatUtil.completaCadena(numSolicitudes + "", numerico, 5, "L")); //CAE-TOT-REG
            buffer.append(FormatUtil.completaCadena(totalArchivo, numerico, 18, "L")); //CAE-TOT-IMP
            buffer.append(FormatUtil.completaCadena("0", numerico, 670, "L")); //CAE-USO-FUT
//			buffer.append("\n");
            file.add(buffer.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    private String creaEncabezadoBancomer() {

        StringBuffer buffer = new StringBuffer();
        String CVE_BANCOMER = CatalogoHelper.getParametro("CVE_EMISORA_BANCOMER");
        String vacio = "";

        try {
            String fecha = Convertidor.dateToString(new Date(), "yyyy-MM-dd");

            buffer.append(FormatUtil.completaCadena("H", alfanum, 1, "L")); //HEA-TIP-REG
            buffer.append(FormatUtil.completaCadena(CVE_BANCOMER, numerico, 9, "L"));//HEA-CONVENIO 
            buffer.append(FormatUtil.completaCadena(fecha, alfanum, 10, "L")); //HEA-FECHA
            buffer.append(FormatUtil.completaCadena("01", numerico, 2, "L")); //HEA-TIPO-VAL-ARCHIVO
            buffer.append(FormatUtil.completaCadena(envio + "", numerico, 30, "L")); //HEA-ID-ARCHIVO
            buffer.append(FormatUtil.completaCadena(ClientesConstants.ODP_BANCOMER_CR_ENVIADO, numerico, 2, "L")); //HEA-CODIGO-RESPUESTA
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 20, "R")); //HEA-DESCRIPCION-CODIGO-RESPUESTA
            //buffer.append(FormatUtil.completaCadena("B", alfanum, 1, "L")); //HEA-CANAL
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 1, "L")); //HEA-CANAL
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 1291, "R")); //FILLER
//			buffer.append("\n");
            file.add(buffer.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    private String registroControlBanamex() {

        StringBuffer buffer = new StringBuffer();

        try {
            String fecha = Convertidor.dateToString(new Date(), "dd-MM-yyyy");
            fecha = fecha.replace("-", "");

            buffer.append(FormatUtil.completaCadena("1", numerico, 1, "L"));
            buffer.append(FormatUtil.completaCadena("100607780", numerico, 12, "L"));
            buffer.append(FormatUtil.completaCadena(fecha, numerico, 8, "L"));
            buffer.append(FormatUtil.completaCadena(envio + "", numerico, 4, "L"));
            buffer.append(FormatUtil.completaCadena("CEGE CAPITAL SAPI DE CV SOFOM ENR", alfanum, 36, "R"));
            buffer.append(FormatUtil.completaCadena("//MDODPReferenciadas", alfanum, 20, "R"));
            buffer.append(FormatUtil.completaCadena("09", numerico, 2, "L"));
            buffer.append(FormatUtil.completaCadena("", alfanum, 40, "R"));
            buffer.append(FormatUtil.completaCadena("B", alfanum, 1, "R"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 1, "R"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 1, "R"));
            file.add(buffer.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    private String creaEncabezadoScotia() {

        StringBuffer buffer = new StringBuffer();
        String cve_scotia = CatalogoHelper.getParametro("CVE_EMISORA_SOCOTIA");

        try {

            buffer.append(FormatUtil.completaCadena("EE", alfanum, 2, "L"));
            buffer.append(FormatUtil.completaCadena("HA", alfanum, 2, "L"));
            buffer.append(FormatUtil.completaCadena(cve_scotia, numerico, 5, "L"));
            buffer.append(FormatUtil.completaCadena("" + envio, numerico, 2, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 8, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 8, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 8, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 3, "L"));
            buffer.append(FormatUtil.completaCadena("", alfanum, 332, "L"));

            file.add(buffer.toString());
            buffer = new StringBuffer();

            buffer.append(FormatUtil.completaCadena("EE", alfanum, 2, "L"));
            buffer.append(FormatUtil.completaCadena("HB", alfanum, 2, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 2, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 4, "L"));
            buffer.append(FormatUtil.completaCadena("0102882185", numerico, 11, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 10, "R"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 3, "L"));
            buffer.append(FormatUtil.completaCadena("", alfanum, 336, "L"));

            file.add(buffer.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    private String creaEncabezadoTelecom(int envioIni, int envioFin) throws Exception {

        StringBuffer buffer = new StringBuffer();
        //String cve_scotia = CatalogoHelper.getParametro("CVE_EMISORA_SOCOTIA");
        String fecha = Convertidor.dateToString(new Date(), "MMddyyyy");
        int importe = (int) (FormatUtil.roundDouble(importeArchivo, 2) * 100);
        String hora = "" + Convertidor.toSqlTimeStamp(new Date());
        hora = hora.substring(11, 13) + hora.substring(14, 16);

        try {

            buffer.append(FormatUtil.completaCadena("01", numerico, 2, "L"));
            buffer.append(FormatUtil.completaCadena("GL", alfanum, 2, "L"));
            buffer.append(FormatUtil.completaCadena("CEG", alfanum, 3, "L"));
            buffer.append(FormatUtil.completaCadena("09", numerico, 2, "L"));
            buffer.append(FormatUtil.completaCadena(fecha, alfanum, 8, "L"));
            buffer.append(FormatUtil.completaCadena("I", alfanum, 1, "L"));
            buffer.append(FormatUtil.completaCadena(envioIni + "", numerico, 6, "L"));
            buffer.append(FormatUtil.completaCadena("F", alfanum, 1, "L"));
            buffer.append(FormatUtil.completaCadena(envioFin + "", numerico, 6, "L"));
            buffer.append(FormatUtil.completaCadena(numSolicitudes + "", numerico, 4, "L"));
            buffer.append(FormatUtil.completaCadena("P", alfanum, 1, "L"));
            buffer.append(FormatUtil.completaCadena(importe + "", numerico, 11, "L"));
            buffer.append(FormatUtil.completaCadena("H", alfanum, 1, "L"));
            buffer.append(FormatUtil.completaCadena(fecha + hora, alfanum, 12, "L"));
            buffer.append(FormatUtil.completaCadena("AAAA", alfanum, 4, "L"));
            buffer.append(FormatUtil.completaCadena("*", alfanum, 1, "L"));

            file.add(buffer.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    //private String registroGlobalBanamex(int montoTot) {
    private String registroGlobalBanamex() {

        StringBuffer buffer = new StringBuffer();
        String cve_banamex = CatalogoHelper.getParametro("CVE_EMISORA_BANAMEX");
        String totalArchivo = FormatUtil.formatDoble("###00", importeArchivo * 100);

        try {
            String fecha = Convertidor.dateToString(new Date(), "dd-MM-yyyy");

            buffer.append(FormatUtil.completaCadena("2", numerico, 1, "R"));
            buffer.append(FormatUtil.completaCadena("1", numerico, 1, "R"));
            buffer.append(FormatUtil.completaCadena("001", numerico, 3, "L"));
            //buffer.append(FormatUtil.completaCadena(montoTot+"", numerico, 18, "L"));
            buffer.append(FormatUtil.completaCadena(totalArchivo, numerico, 18, "L"));
            buffer.append(FormatUtil.completaCadena("01", numerico, 2, "L"));
            buffer.append(FormatUtil.completaCadena("7006", numerico, 4, "L"));
            buffer.append(FormatUtil.completaCadena(cve_banamex, numerico, 20, "L"));
            buffer.append(FormatUtil.completaCadena("", alfanum, 20, "R"));

            file.add(buffer.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    private String creaTrailerBancomer() {

        StringBuffer buffer = new StringBuffer();
        String vacio = "";

        try {
            String totalDisp = FormatUtil.formatDoble("###00", impDispersiones * 100);
            String totalCanc = FormatUtil.formatDoble("###00", impCancelaciones * 100);
            String totalMod = FormatUtil.formatDoble("###00", impModificaciones * 100);
            String totalArchivo = FormatUtil.formatDoble("###00", importeArchivo * 100);
            String fecha = Convertidor.dateToString(new Date(), "yyyy-MM-dd");

            buffer.append(FormatUtil.completaCadena("T", alfanum, 1, "L")); //TRAI-TIP-REG-1
            buffer.append(FormatUtil.completaCadena(numDispersiones + "", numerico, 10, "L")); //TRAI-REG-ALTAS-2
            buffer.append(FormatUtil.completaCadena(totalDisp, numerico, 15, "L")); //TRAI-REG-ALT-IMP-3
            buffer.append(FormatUtil.completaCadena(numCancelaciones + "", numerico, 10, "L")); //TRAI-REG-BAJAS-4
            buffer.append(FormatUtil.completaCadena(totalCanc, numerico, 15, "L")); //TRAI-REG-BAJAS-IMP-5
            buffer.append(FormatUtil.completaCadena(numModificaciones + "", numerico, 10, "L")); //TRAI-REG-MOD-6
            buffer.append(FormatUtil.completaCadena(totalMod, numerico, 15, "L")); //TRAI-REG-MOD-IMP-7
            buffer.append(FormatUtil.completaCadena("0", numerico, 10, "L")); //TRAI-RESPUESTA-8
            buffer.append(FormatUtil.completaCadena("0", numerico, 15, "L")); //TRAI-RESPUETA-IMP-9
            buffer.append(FormatUtil.completaCadena("0", numerico, 10, "L")); //TRAI-RESPUESTA-10
            buffer.append(FormatUtil.completaCadena("0", numerico, 15, "L")); //TRAI-RESPUETA-IMP-11
            buffer.append(FormatUtil.completaCadena("0", numerico, 10, "L")); //TRAI-RESPUESTA-12
            buffer.append(FormatUtil.completaCadena("0", numerico, 15, "L")); //TRAI-RESPUETA-IMP-13
            buffer.append(FormatUtil.completaCadena("0", numerico, 10, "L")); //TRAI-RESPUESTA-14
            buffer.append(FormatUtil.completaCadena("0", numerico, 15, "L")); //TRAI-RESPUETA-IMP-15
            buffer.append(FormatUtil.completaCadena("0", numerico, 10, "L")); //TRAI-RESPUESTA-16
            buffer.append(FormatUtil.completaCadena("0", numerico, 15, "L")); //TRAI-RESPUETA-IMP-17
            buffer.append(FormatUtil.completaCadena("0", numerico, 10, "L")); //TRAI-RESPUESTA-18
            buffer.append(FormatUtil.completaCadena("0", numerico, 15, "L")); //TRAI-RESPUETA-IMP-19
            buffer.append(FormatUtil.completaCadena("0", numerico, 10, "L")); //TRAI-RESPUESTA-20
            buffer.append(FormatUtil.completaCadena("0", numerico, 15, "L")); //TRAI-RESPUETA-IMP-21
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 1115, "R")); //FILLER-22
//			buffer.append("\n");
            file.add(buffer.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    //private String registroTotalesBanamex(int montoTot, int conta) {
    private String registroTotalesBanamex() {

        StringBuffer buffer = new StringBuffer();
        String totalArchivo = FormatUtil.formatDoble("###00", importeArchivo * 100);

        try {
            buffer.append(FormatUtil.completaCadena("4", numerico, 1, "R"));
            buffer.append(FormatUtil.completaCadena("001", numerico, 3, "R"));
            //buffer.append(FormatUtil.completaCadena(conta+"", numerico, 6, "L"));
            buffer.append(FormatUtil.completaCadena(numSolicitudes + "", numerico, 6, "L"));
            //buffer.append(FormatUtil.completaCadena(montoTot+"", numerico, 18, "L"));
            buffer.append(FormatUtil.completaCadena(totalArchivo, numerico, 18, "L"));
            buffer.append(FormatUtil.completaCadena("1", numerico, 6, "L"));
            //buffer.append(FormatUtil.completaCadena(montoTot+"", numerico, 18, "L"));
            buffer.append(FormatUtil.completaCadena(totalArchivo, numerico, 18, "L"));

            file.add(buffer.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    private String creaTrailerScotia() {

        StringBuffer buffer = new StringBuffer();

        try {
            String totalDisp = FormatUtil.formatDoble("###00", (impDispersiones + impModificaciones) * 100);
            String totalCanc = FormatUtil.formatDoble("###00", (impCancelaciones + impModificaciones) * 100);
            String numDisp = numDispersiones + numModificaciones + "";
            String numCanc = numCancelaciones + numModificaciones + "";

            buffer.append(FormatUtil.completaCadena("EE", alfanum, 2, "L"));
            buffer.append(FormatUtil.completaCadena("TB", alfanum, 2, "L"));
            buffer.append(FormatUtil.completaCadena(numDisp, numerico, 7, "L"));
            buffer.append(FormatUtil.completaCadena(totalDisp, numerico, 17, "L"));
            buffer.append(FormatUtil.completaCadena(numCanc, numerico, 7, "L"));
            buffer.append(FormatUtil.completaCadena(totalCanc, numerico, 17, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 7, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 17, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 7, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 17, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 7, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 17, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 7, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 17, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 7, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 17, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 7, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 17, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 7, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 17, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 7, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 17, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 3, "L"));
            buffer.append(FormatUtil.completaCadena("", alfanum, 123, "L"));

            file.add(buffer.toString());
            buffer = new StringBuffer();

            buffer.append(FormatUtil.completaCadena("EE", alfanum, 2, "L"));
            buffer.append(FormatUtil.completaCadena("TA", alfanum, 2, "L"));
            buffer.append(FormatUtil.completaCadena(numDisp, numerico, 7, "L"));
            buffer.append(FormatUtil.completaCadena(totalDisp, numerico, 17, "L"));
            buffer.append(FormatUtil.completaCadena(numCanc, numerico, 7, "L"));
            buffer.append(FormatUtil.completaCadena(totalCanc, numerico, 17, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 7, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 17, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 7, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 17, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 7, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 17, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 7, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 17, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 7, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 17, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 7, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 17, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 7, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 17, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 7, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 17, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 3, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 3, "L"));
            buffer.append(FormatUtil.completaCadena("", alfanum, 120, "L"));

            file.add(buffer.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    private String creaPieTelecom(int envioIni, int envioFin) throws Exception {

        StringBuffer buffer = new StringBuffer();
        String fecha = Convertidor.dateToString(new Date(), "MMddyyyy");
        int importe = (int) (FormatUtil.roundDouble(importeArchivo, 2) * 100);
        String hora = "" + Convertidor.toSqlTimeStamp(new Date());
        hora = hora.substring(11, 13) + hora.substring(14, 16);

        try {

            buffer.append(FormatUtil.completaCadena("FOOT", alfanum, 4, "L"));
            buffer.append(FormatUtil.completaCadena(" ", alfanum, 1, "L"));
            buffer.append(FormatUtil.completaCadena("01", numerico, 2, "L"));
            buffer.append(FormatUtil.completaCadena("GL", alfanum, 2, "L"));
            buffer.append(FormatUtil.completaCadena("CEG", alfanum, 3, "L"));
            buffer.append(FormatUtil.completaCadena("09", numerico, 2, "L"));
            buffer.append(FormatUtil.completaCadena(fecha, alfanum, 8, "L"));
            buffer.append(FormatUtil.completaCadena("I", alfanum, 1, "L"));
            buffer.append(FormatUtil.completaCadena(envioIni + "", numerico, 6, "L"));
            buffer.append(FormatUtil.completaCadena("F", alfanum, 1, "L"));
            buffer.append(FormatUtil.completaCadena(envioFin + "", numerico, 6, "L"));
            buffer.append(FormatUtil.completaCadena(numSolicitudes + "", numerico, 4, "L"));
            buffer.append(FormatUtil.completaCadena("P", alfanum, 1, "L"));
            buffer.append(FormatUtil.completaCadena(importe + "", numerico, 11, "L"));
            buffer.append(FormatUtil.completaCadena("H", alfanum, 1, "L"));
            buffer.append(FormatUtil.completaCadena(fecha + hora, alfanum, 12, "L"));
            buffer.append(FormatUtil.completaCadena("AAAA", alfanum, 4, "L"));
            buffer.append(FormatUtil.completaCadena("*", alfanum, 1, "L"));

            file.add(buffer.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    private String creaDetalleBanorte(OrdenDePagoVO pago) {

        String vacio = "";
        String CVE_BANORTE = CatalogoHelper.getParametro("CVE_EMISORA_BANORTE");
        StringBuffer buffer = new StringBuffer();
        String numDoc = CVE_BANORTE + pago.getReferencia();
        String ordenante = CatalogoHelper.getParametro("ORDENANTE_BANORTE");
        String codOperacion = null; //(pago.getEstatus()==ClientesConstants.OP_DISPERSADA ? "60" : "63");
        String nombre = FormatUtil.eliminaCaracteresInvalidos(pago.getNombre());
        int longitud = 0;
        if (nombre.length() > 60) {
            longitud = nombre.length() - 60;
            nombre = nombre.substring(0, nombre.length() - longitud);
        }

        switch (pago.getEstatus()) {
            case (ClientesConstants.OP_DISPERSADA):
                codOperacion = "60";
                break;
            case (ClientesConstants.OP_CANCELADA):
                codOperacion = "63";
                break;
            case (ClientesConstants.OP_ACTUALIZA_NOMBRE):
                codOperacion = "80";
                pago.setEstatus(ClientesConstants.OP_DISPERSADA);
                break;
            case (ClientesConstants.OP_SEGURO_ENVIADO):
                codOperacion = "60";
                break;
        }

        int monto = (int) (FormatUtil.roundDouble(pago.getMonto(), 2) * 100);

        contador++;

        try {
            String fecha = Convertidor.dateToString(new Date(), "yyyyMMdd");
//			buffer.append("\\n");
            buffer.append(FormatUtil.completaCadena("1", numerico, 1, "L")); //DET-TIP-REG
            buffer.append(FormatUtil.completaCadena(contador + "", numerico, 5, "L")); //DET-NUM-CON 
            buffer.append(FormatUtil.completaCadena(codOperacion, numerico, 2, "L")); //DET-COD-OPE
            buffer.append(FormatUtil.completaCadena("O", alfanum, 1, "R")); //DET-TIP-DOC
            buffer.append(FormatUtil.completaCadena(numDoc, numerico, 25, "L")); //DET-REFEREN 
            buffer.append(FormatUtil.completaCadena(fecha, numerico, 8, "L")); //DET-FEC-APL
            buffer.append(FormatUtil.completaCadena(vacio, numerico, 6, "L")); //DET-HOR-APL
            buffer.append(FormatUtil.completaCadena(monto + "", numerico, 13, "L")); //DET-IMPORTE 
            buffer.append(FormatUtil.completaCadena(vacio, numerico, 18, "L")); //DET-NUM-CTA
            buffer.append(FormatUtil.completaCadena(vacio, numerico, 2, "L")); //TIPO DE CUENTA
            buffer.append(FormatUtil.completaCadena(vacio, numerico, 4, "L")); //DET-NUM-SUC
            buffer.append(FormatUtil.completaCadena(vacio, numerico, 9, "L")); //DET-NUM-MOV
            buffer.append(FormatUtil.completaCadena(ordenante, alfanum, 60, "R")); //DET-NOM-ORD
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 35, "R")); //DOMICILIO DEL ORDENANTE
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 35, "R")); //DOMICILIO DEL ORDENANTE 2
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 35, "R")); //CIUDAD DEL ORDENANTE
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 3, "R")); //ESTADO DEL ORDENANTE
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 3, "R")); //PAIS DEL ORDENANTE
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 10, "R")); //CODIGO POSTAL DEL ORDENANTE
            buffer.append(FormatUtil.completaCadena(vacio, numerico, 15, "L")); //TELEFONO DEL ORDENANTE
            buffer.append(FormatUtil.completaCadena(nombre, alfanum, 60, "R")); //DET-NOM-BEN
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 35, "R")); //DOMICILIO DEL BENEFICIARIO
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 35, "R")); //DOMICILIO DEL BENEFICIARIO 2
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 35, "R")); //CIUDAD DEL BENEFICIARIO
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 3, "R")); //ESTADO DEL BENEFICIARIO
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 3, "R")); //PAIS DEL BENEFICIARIO
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 10, "R")); //CODIGO POSTAL DEL BENEFICIARIO
            buffer.append(FormatUtil.completaCadena(vacio, numerico, 15, "L")); //TELEFONO DEL BENEFICIARIO
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 60, "R")); //DET-MENSAJE
            buffer.append(FormatUtil.completaCadena("MXP", alfanum, 3, "R")); //DET-DIVISA
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 25, "R")); //TIPO DE IDENTIFICACI�N DEL BENEFICIARIO
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 25, "R")); //NUMERO DE IDENTIFICACION DEL BENEFICIARIO
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 10, "R")); //NUMERO DE AGENTE
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 3, "R")); //ESTADO DEL AGENTE
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 3, "R")); //PAIS DEL AGENTE
            buffer.append(FormatUtil.completaCadena(vacio, numerico, 14, "L")); //FECHA Y HORA DEL ENVIO
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 3, "R")); //NUMERO DE BANCO
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 3, "R")); //PAIS DE ORIGEN
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 3, "R")); //PAIS DE DESTINO
            buffer.append(FormatUtil.completaCadena("000", numerico, 3, "L")); //IDENTIFICACI�N DEL ORDENANTE
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 3, "R")); //ESTADO DE LA IDENTIFICACI�N DEL ORDENANTE
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 3, "R")); //PAIS DE LA IDENTIFICACI�N DEL ORDENANTE
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 20, "R")); //NUMERO DE IDENTIFICACION DEL ORDENANTE
            buffer.append(FormatUtil.completaCadena(vacio, numerico, 8, "L")); //FECHA DE VENCIMIENTO DE LA IDENTIFICACI�N DEL ORDENANTE
            buffer.append(FormatUtil.completaCadena(vacio, numerico, 8, "L")); //FECHA DE NACIMIENTO DEL ORDENANTE
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 40, "R")); //OCUPACION DEL ORDENANTE
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 11, "R")); //NUMERO DEL SEGURO SOCIAL DEL ORDENANTE
            buffer.append(FormatUtil.completaCadena(vacio, numerico, 13, "L")); //IMPORTE ORIGINAL DEL ENVIO
            buffer.append(FormatUtil.completaCadena("MXP", alfanum, 3, "R")); //MONEDA DE ENVIO
            buffer.append(FormatUtil.completaCadena(vacio, numerico, 11, "L")); //TIPO DE CAMBIO
            buffer.append(FormatUtil.completaCadena(vacio, numerico, 9, "L")); //COMISION
            buffer.append(FormatUtil.completaCadena(vacio, numerico, 12, "L")); //DET-USO-FUT

            file.add(buffer.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return buffer.toString();
    }

    private String creaDetalleBancomer(OrdenDePagoVO pago) {

        String vacio = "";
        String CVE_BANCOMER = CatalogoHelper.getParametro("CVE_EMISORA_BANCOMER");
        StringBuffer buffer = new StringBuffer();
        String concepto = "ODP" + pago.getIdCliente();
        String numDoc = "" + CVE_BANCOMER + pago.getReferencia();
// Se utiliza para Bancomer el mismo nombre de ordenante ya que es el nombre de la empresa
        String codOperacion = null; //(pago.getEstatus()==ClientesConstants.OP_DISPERSADA ? "60" : "63");
        String nombre = FormatUtil.eliminaCaracteresInvalidos(pago.getNombre());
        GregorianCalendar dateOut = new GregorianCalendar();
        Calendar cal = Calendar.getInstance();
        Date fechaCalc = new Date();
        int longitud = 0;
        if (nombre.length() > 40) {
            longitud = nombre.length() - 40;
            nombre = nombre.substring(0, nombre.length() - longitud);
        }
        switch (pago.getEstatus()) {
            case (ClientesConstants.OP_DISPERSADA):
                codOperacion = "A";
                break;
            case (ClientesConstants.OP_CANCELADA):
                codOperacion = "B";
                break;
            case (ClientesConstants.OP_ACTUALIZA_NOMBRE):
                numModificaciones = 0;
                impModificaciones = 0;
                codOperacion = "A";
                pago.setEstatus(ClientesConstants.OP_DISPERSADA);
                concepto = "ODPS" + pago.getIdCliente();
                numDispersiones++;
                impDispersiones += pago.getMonto();
                break;
            case (0):
                codOperacion = "B";
                pago.setEstatus(ClientesConstants.OP_DISPERSADA);
                numCancelaciones++;
                impCancelaciones += pago.getMonto();
                break;
            case (ClientesConstants.OP_SEGURO_ENVIADO):
                codOperacion = "A";
                break;
        }
        int monto = (int) (FormatUtil.roundDouble(pago.getMonto(), 2) * 100);
        contador++;
        try {
            String fecha = Convertidor.dateToString(new Date());
            fechaCalc = (Convertidor.stringToDate(fecha));
            cal.setTime(Convertidor.stringToDate(fecha));
            dateOut.setTime(fechaCalc);
            //dateOut.add(Calendar.DAY_OF_YEAR, 1);
            String fechaAut = Convertidor.dateToString(dateOut.getTime(), ClientesConstants.FORMATO_FECHA_EU);
            dateOut.add(Calendar.DAY_OF_YEAR, 13);
            String fechaCad = Convertidor.dateToString(dateOut.getTime(), ClientesConstants.FORMATO_FECHA_EU);
//			buffer.append("\\n");
            buffer.append(FormatUtil.completaCadena("D", alfanum, 1, "L")); //DET-TIP-REG-1
            buffer.append(FormatUtil.completaCadena(codOperacion, numerico, 1, "L")); //DET-COD-OPE-2
            buffer.append(FormatUtil.completaCadena("P", alfanum, 1, "R")); //DET-TIP-DOC-LLAVE-3
            buffer.append(FormatUtil.completaCadena(numDoc, alfanum, 20, "R")); //DET-REFEREN-4
            buffer.append(FormatUtil.completaCadena(concepto, alfanum, 30, "R")); //DET-CONCEPTO-5 
            buffer.append(FormatUtil.completaCadena("PDV", alfanum, 3, "R")); //DET-TIPO-SERVICIO-6
            buffer.append(FormatUtil.completaCadena("1", alfanum, 1, "R")); //DET-COD-OPERA-7
            buffer.append(FormatUtil.completaCadena("0", numerico, 20, "L")); //DET-LINEA-CREDITO-8
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 15, "R")); //DET-LEYENDA_CORTA-9
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 25, "R")); //DET-LEYENDA-LARGA-10
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 37, "R")); //DET-LEYENDA_2NDO-RENGLON-11
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 15, "R")); //DET-LEYENDA_CORTA-CUENTA-12
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 25, "R")); //DET-LEYENDA_CUENTA-13
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 37, "R")); //DET-LEYENDA_2NDO-REN-14
            buffer.append(FormatUtil.completaCadena("N", alfanum, 1, "R")); //DET-COMP-FISCAL-15
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 8, "R")); //DET-NO-TERCERO-CLIENTE-16
            buffer.append(FormatUtil.completaCadena("00", alfanum, 2, "R")); //DET-COMP-FISCAL-17
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 35, "R")); //DET-CUENTA_BANCOMER-18
            buffer.append(FormatUtil.completaCadena(nombre, alfanum, 40, "R")); //DET-NOM-BENEFICIARIO-19
            buffer.append(FormatUtil.completaCadena("2", alfanum, 1, "R")); //DET-TIPO-ID-20
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 30, "R")); //DET-NUMERO-ID-TER-21
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 40, "R")); //DET-TERCERO-22
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 1, "R")); //DET-CLAVE-ID-2-23
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 30, "R")); //DET-NO-ID-2-24
            buffer.append(FormatUtil.completaCadena("MXP", alfanum, 3, "R")); //DET-MONEDA-25
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 11, "R")); //DET-CVE-BIC-26
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 9, "R")); //DET-CVE-ABA-27
            buffer.append(FormatUtil.completaCadena("OT", alfanum, 2, "R")); //DET-TIPO-DOC-28
            buffer.append(FormatUtil.completaCadena(monto + "", numerico, 15, "L")); //DET-MONTO-29 
            buffer.append(FormatUtil.completaCadena("0", numerico, 15, "L")); //DET-IVA-30
            buffer.append(FormatUtil.completaCadena("00", alfanum, 2, "R")); //DET-TIPO-CONF-31
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 50, "R")); //DET-EMAIL-32
            buffer.append(FormatUtil.completaCadena("N", alfanum, 1, "R")); //DET-PERIOD-33
            buffer.append(FormatUtil.completaCadena("0", numerico, 8, "L")); //DET-TASA-INTERES-34
            buffer.append(FormatUtil.completaCadena("0", numerico, 4, "L")); //DET-TASA-IVA-INTERES-35
            buffer.append(FormatUtil.completaCadena(fechaAut, numerico, 10, "L")); //DET-FECHA-36
            buffer.append(FormatUtil.completaCadena(fechaCad, numerico, 10, "L")); //DET-FECHA-VEN-37
            buffer.append(FormatUtil.completaCadena("0001-01-01", numerico, 10, "L")); //DET-FECHA_GRA-38
            buffer.append(FormatUtil.completaCadena(fechaCad, numerico, 10, "L")); //DET-FECHA-CAD-39
            buffer.append(FormatUtil.completaCadena(fechaAut, numerico, 10, "L")); //DET-FECHA-DOC-40
            buffer.append(FormatUtil.completaCadena("N", alfanum, 1, "R")); //DET-IND-PAGO-REC-41
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 1, "R")); //DET-PER-PAGO-REC-42
            buffer.append(FormatUtil.completaCadena("0001-01-01", numerico, 10, "L")); //DET-FECHA_FIN-PAGO-REC-43
            buffer.append(FormatUtil.completaCadena("0", numerico, 3, "L")); //DET-LONG-44
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 700, "R")); //DET-DAT-ADI-45
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 10, "R")); //DET-FOLIO-ABO-46
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 10, "R")); //DET-FOLIO-CARG-47
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 2, "R")); //DET-COD-ESTATUS-48
            buffer.append(FormatUtil.completaCadena(vacio, alfanum, 30, "R")); //DET-DESC-COD-49
            buffer.append(FormatUtil.completaCadena("0001-01-01", alfanum, 10, "R")); //FECH-ULT-EVENTO-50

            file.add(buffer.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return buffer.toString();
    }

    private String creaDetalleBanamex(OrdenDePagoVO pago) {

        String cve_banamex = CatalogoHelper.getParametro("CVE_EMISORA_BANAMEX");
        StringBuffer buffer = new StringBuffer();
        String referencia = cve_banamex + pago.getReferencia();
        String nombre = FormatUtil.eliminaCaracteresInvalidos(pago.getNombre());
        int longitud = 0;
        if (nombre.length() > 55) {
            longitud = nombre.length() - 55;
            nombre = nombre.substring(0, nombre.length() - longitud);
        }
        GregorianCalendar dateOut = new GregorianCalendar();
        Calendar cal = Calendar.getInstance();
        Date fechaCalc = new Date();
        int codOperacion = 0;
        switch (pago.getEstatus()) {
            case (ClientesConstants.OP_DISPERSADA):
                codOperacion = 1;
                break;
            case (ClientesConstants.OP_CANCELADA):
                codOperacion = 2;
                break;
            case (ClientesConstants.OP_ACTUALIZA_NOMBRE):
                codOperacion = 1;
                pago.setEstatus(ClientesConstants.OP_DISPERSADA);
                referencia = "2" + referencia.substring(1, referencia.length());
                break;
            case (0):
                codOperacion = 2;
                pago.setEstatus(ClientesConstants.OP_DISPERSADA);
                break;
            case (ClientesConstants.OP_SEGURO_ENVIADO):
                codOperacion = 1;
                break;
        }
        int monto = (int) FormatUtil.roundDouble(pago.getMonto(), 2);
        contador++;

        try {
            String fecha = Convertidor.dateToString(new Date());
            fechaCalc = (Convertidor.stringToDate(fecha));
            cal.setTime(Convertidor.stringToDate(fecha));
            dateOut.setTime(fechaCalc);
            dateOut.add(Calendar.DAY_OF_YEAR, 13);
            String fechaCad = Convertidor.dateToString(dateOut.getTime(), "dd-MM-yyyy");
            fechaCad = fechaCad.replace("-", "");
            buffer.append(FormatUtil.completaCadena("3", numerico, 1, "L"));
            buffer.append(FormatUtil.completaCadena("07", numerico, 2, "L"));
            buffer.append(FormatUtil.completaCadena("0870", numerico, 4, "L"));
            buffer.append(FormatUtil.completaCadena("48", alfanum, 16, "R"));
            buffer.append(FormatUtil.completaCadena(monto + "", numerico, 17, "L") + ".00");
            buffer.append(FormatUtil.completaCadena("", alfanum, 34, "L"));
            buffer.append(FormatUtil.completaCadena(nombre, alfanum, 55, "R"));
            buffer.append(FormatUtil.completaCadena("", alfanum, 40, "R"));
            buffer.append(FormatUtil.completaCadena("0002", numerico, 4, "L"));
            buffer.append(FormatUtil.completaCadena("", alfanum, 6, "R"));
            buffer.append(FormatUtil.completaCadena("001", numerico, 3, "R"));
            buffer.append(FormatUtil.completaCadena(codOperacion + "", numerico, 1, "R"));//
            buffer.append(FormatUtil.completaCadena(referencia, alfanum, 20, "R"));
            buffer.append(FormatUtil.completaCadena("1", alfanum, 10, "R"));
            buffer.append(FormatUtil.completaCadena("2", alfanum, 10, "R"));
            buffer.append(FormatUtil.completaCadena(fechaCad, alfanum, 8, "R"));
            buffer.append(FormatUtil.completaCadena("", alfanum, 13, "R"));
            buffer.append(FormatUtil.completaCadena("", numerico, 12, "R"));
            buffer.append(FormatUtil.completaCadena("99", numerico, 2, "R"));
            buffer.append(FormatUtil.completaCadena("", alfanum, 31, "R"));
            buffer.append(FormatUtil.completaCadena("", numerico, 4, "R"));

            file.add(buffer.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return buffer.toString();
    }

    private String creaDetalleScotia(OrdenDePagoVO pago) {

        String cve_scotia = CatalogoHelper.getParametro("CVE_EMISORA_SOCOTIA");
        StringBuffer buffer = new StringBuffer();
        String referencia = cve_scotia + pago.getReferencia();
        String nombre = FormatUtil.eliminaCaracteresInvalidos(pago.getNombre());
        String fecha = "";
        int longitud = 0;
        if (nombre.length() > 40) {
            longitud = nombre.length() - 40;
            nombre = nombre.substring(0, nombre.length() - longitud);
        }
        String codOperacion = "";

        switch (pago.getEstatus()) {
            case (ClientesConstants.OP_DISPERSADA):
                codOperacion = "DA";
                break;
            case (ClientesConstants.OP_CANCELADA):
                codOperacion = "DB";
                break;
            case (ClientesConstants.OP_ACTUALIZA_NOMBRE):
                codOperacion = "DA";
                pago.setEstatus(ClientesConstants.OP_DISPERSADA);
                referencia = "0" + referencia;
                break;
            case (0):
                codOperacion = "DB";
                pago.setEstatus(ClientesConstants.OP_DISPERSADA);
                break;
            case (ClientesConstants.OP_SEGURO_ENVIADO):
                codOperacion = "DA";
                break;
        }
        double dbMonto = pago.getMonto() * 100;
        int monto = (int) dbMonto;
        contador++;

        try {
            if (codOperacion.equals("DB")) {
                fecha = Convertidor.dateToString(pago.getFechaEnvio(), "yyyyMMdd");
            } else {
                fecha = Convertidor.dateToString(new Date(), "yyyyMMdd");
            }

            buffer.append(FormatUtil.completaCadena("EE", alfanum, 2, "L"));
            buffer.append(FormatUtil.completaCadena(codOperacion, alfanum, 2, "L"));
            buffer.append(FormatUtil.completaCadena("1", numerico, 2, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 2, "L"));
            buffer.append(FormatUtil.completaCadena(monto + "", numerico, 15, "L"));
            buffer.append(FormatUtil.completaCadena(fecha, alfanum, 8, "L"));
            buffer.append(FormatUtil.completaCadena("3", numerico, 2, "L"));
            buffer.append(FormatUtil.completaCadena(referencia, alfanum, 20, "R"));
            buffer.append(FormatUtil.completaCadena("", alfanum, 13, "L"));
            buffer.append(FormatUtil.completaCadena(nombre, alfanum, 40, "R"));
            buffer.append(FormatUtil.completaCadena(pago.getReferencia(), numerico, 16, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 5, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 5, "L"));
            buffer.append(FormatUtil.completaCadena("11111111111", numerico, 20, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 5, "L"));
            buffer.append(FormatUtil.completaCadena("", alfanum, 40, "L"));
            buffer.append(FormatUtil.completaCadena("1", numerico, 1, "L"));
            buffer.append(FormatUtil.completaCadena("", alfanum, 1, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 5, "L"));
            buffer.append(FormatUtil.completaCadena("044", numerico, 3, "L"));
            buffer.append(FormatUtil.completaCadena("044", numerico, 3, "L"));
            buffer.append(FormatUtil.completaCadena("30", numerico, 3, "L"));
            buffer.append(FormatUtil.completaCadena("ORDEN DE PAGO SCOTIA " + pago.getReferencia(), alfanum, 50, "R"));
            buffer.append(FormatUtil.completaCadena("", alfanum, 20, "L"));
            buffer.append(FormatUtil.completaCadena("", alfanum, 20, "L"));
            buffer.append(FormatUtil.completaCadena("", alfanum, 20, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 3, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 1, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 3, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 8, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 5, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 5, "L"));
            buffer.append(FormatUtil.completaCadena("", alfanum, 22, "L"));

            file.add(buffer.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return buffer.toString();
    }

    private String creaDetalleSantander(OrdenDePagoVO pago, Date[] fechasInhabiles) {

        String cve_santander = CatalogoHelper.getParametro("CVE_EMISORA_SANTANDER");

        StringBuffer buffer = new StringBuffer();
        int diasven = 13;
        String referencia = StringUtils.leftPad(cve_santander.substring(4, 11) + pago.getReferencia(), 20, "0");
        String claveCliente = StringUtils.leftPad(pago.getIdCliente() + "", 7, "0") + StringUtils.leftPad(pago.getIdSolicitud() + "", 2, "0");
        String nombre = FormatUtil.eliminaCaracteresInvalidos(pago.getNombre());//verificar tratameinto de caracteres especiales con el banco
        String fechaVen = "";
        int longitud = 0;
        if (nombre.length() > 60) {
            longitud = nombre.length() - 60;
            nombre = nombre.substring(0, nombre.length() - longitud);
        }
        String codOperacion = "";
        double dbMonto = pago.getMonto()/* * 100*/;
        String monto = StringUtils.leftPad((int) dbMonto + "", 14, "0");
        contador++;

        try {

            String fecha = Convertidor.dateToString(new Date(), "dd/MM/yyyy");
            do {
                diasven++;
                fechaVen = Convertidor.dateToString(FechasUtil.getRestarDias(new Date(), -diasven), "dd/MM/yyyy");

            } while (FechasUtil.esDiaInhabil(Convertidor.stringToDate(fechaVen), fechasInhabiles));
            buffer.append(FormatUtil.completaCadena(cve_santander, alfanum, 16, "R"));
            buffer.append(FormatUtil.completaCadena(referencia, numerico, 20, "R"));
            buffer.append(FormatUtil.completaCadena(fecha, alfanum, 5, "R"));
            buffer.append(FormatUtil.completaCadena(fechaVen, alfanum, 5, "R"));
            buffer.append(FormatUtil.completaCadena(claveCliente, alfanum, 13, "R"));
            buffer.append(FormatUtil.completaCadena(nombre, alfanum, 60, "R"));
            buffer.append(FormatUtil.completaCadena("S", alfanum, 1, "R"));
            buffer.append(FormatUtil.completaCadena("", alfanum, 4, "R"));
            buffer.append(FormatUtil.completaCadena("E", alfanum, 1, "R"));
            buffer.append(FormatUtil.completaCadena(monto, numerico, 16, "R"));
            buffer.append(FormatUtil.completaCadena("ORDEN DE PAGO SANTANDER " + pago.getReferencia(), alfanum, 60, "R"));
            file.add(buffer.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return buffer.toString();
    }

    private String creaEncabezadoModSantander() {

        StringBuffer buffer = new StringBuffer();
        try {
            buffer.append("Sucursal,");
            buffer.append("Equipo,");
            buffer.append("Cliente,");
            buffer.append("Solicitante,");
            buffer.append("Solicitud,");
            buffer.append("Importe,");
            buffer.append("Estatus,");
            buffer.append("ODP Referencia");
            file.add(buffer.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }

    private String creaDetalleSantanderMod(OrdenDePagoVO pago) {

        String cve_santander = CatalogoHelper.getParametro("CVE_EMISORA_SANTANDER");

        StringBuffer buffer = new StringBuffer();
        String referencia = StringUtils.leftPad(cve_santander.substring(4, 11) + pago.getReferencia(), 20, "0");
        String nombre = FormatUtil.eliminaCaracteresInvalidos(pago.getNombre());//verificar tratameinto de caracteres especiales con el banco        
        String estatus = "";
        int longitud = 0;
        if (nombre.length() > 60) {
            longitud = nombre.length() - 60;
            nombre = nombre.substring(0, nombre.length() - longitud);
        }
        if (pago.getEstatus() == ClientesConstants.OP_CANCELADA) {
            estatus = "Cancelada";
        } else if (pago.getEstatus() == ClientesConstants.OP_DISPERSADA) {
            estatus = "Modificada";
            pago.setEstatus(ClientesConstants.OP_DISPERSADA);
        }
        double dbMonto = pago.getMonto()/* * 100*/;
        String monto = StringUtils.leftPad((int) dbMonto + "", 14, "0");
        contador++;

        try {
            buffer.append(pago.getNomSucursal() + ",");
            buffer.append(pago.getGrupo() + ",");
            buffer.append(pago.getIdCliente() + ",");
            buffer.append(nombre + ",");
            buffer.append(pago.getIdSolicitud() + ",");
            buffer.append(monto + ",");
            buffer.append(estatus + ",");
            buffer.append("'" + referencia);
            file.add(buffer.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return buffer.toString();
    }

    private String creaDetalleTelecom(OrdenDePagoVO orden) throws Exception {

        StringBuffer buffer = new StringBuffer();
        String fecha = Convertidor.dateToString(new Date(), "MM/dd/yy");
        int importe = (int) (FormatUtil.roundDouble(orden.getMonto(), 2) * 100);

        try {

            buffer.append(FormatUtil.completaCadena("S", alfanum, 1, "L"));
            buffer.append(FormatUtil.completaCadena(orden.getIdOrdenPago() + "", numerico, 6, "L"));
            buffer.append(FormatUtil.completaCadena(" ", alfanum, 1, "L"));
            buffer.append(FormatUtil.completaCadena(fecha, alfanum, 8, "L"));
            buffer.append(FormatUtil.completaCadena(" ", alfanum, 1, "L"));
            buffer.append(FormatUtil.completaCadena(orden.getReferencia(), numerico, 15, "L"));
            buffer.append(FormatUtil.completaCadena(" ", alfanum, 1, "L"));
            buffer.append(FormatUtil.completaCadena(importe + "", numerico, 9, "L"));
            buffer.append(FormatUtil.completaCadena(" ", alfanum, 1, "L"));
            buffer.append(FormatUtil.completaCadena("0", numerico, 7, "L"));
            buffer.append(FormatUtil.completaCadena(" ", alfanum, 1, "L"));
            buffer.append(FormatUtil.completaCadena(orden.getIdOficTelecom() + "", numerico, 5, "L"));//MAPEOD DE OFICINA
            buffer.append(FormatUtil.completaCadena("i", alfanum, 1, "L"));
            buffer.append(FormatUtil.completaCadena("MEXICO DF", alfanum, 36, "L").trim());
            buffer.append(FormatUtil.completaCadena("c", alfanum, 1, "L"));
            buffer.append(FormatUtil.completaCadena(orden.getNomOficTelecom(), alfanum, 70, "L").trim());
            buffer.append(FormatUtil.completaCadena("b", alfanum, 1, "L"));
            buffer.append(FormatUtil.completaCadena(orden.getNombres() + "/" + orden.getApaterno() + "/" + orden.getAmaterno(), alfanum, 60, "L").trim());//Nombre separado
            buffer.append(FormatUtil.completaCadena("d", alfanum, 1, "L"));
            buffer.append(FormatUtil.completaCadena("DOMICILIO CONOCIDO", alfanum, 100, "L").trim());
            buffer.append(FormatUtil.completaCadena("+", alfanum, 1, "L"));
            buffer.append(FormatUtil.completaCadena("COLONIA CONOCIDA", alfanum, 100, "L").trim());
            buffer.append(FormatUtil.completaCadena("r", alfanum, 1, "L"));
            buffer.append(FormatUtil.completaCadena("CEGE/CAPITAL/SAPI SOFOM", alfanum, 60, "L").trim());
            buffer.append(FormatUtil.completaCadena("f", alfanum, 1, "L"));
            buffer.append(FormatUtil.completaCadena(orden.getIdentificacion(), alfanum, 100, "L").trim());//NUMERO DEL IFE 
            buffer.append(FormatUtil.completaCadena("*", alfanum, 1, "L"));

            file.add(buffer.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return buffer.toString();
    }

    private void getImporteArchivo() {
        OrdenDePagoVO oPago = null;
        Iterator i = list.iterator();
        while (i.hasNext()) {
            oPago = (OrdenDePagoVO) i.next();
            switch (oPago.getEstatus()) {
                case (ClientesConstants.OP_DISPERSADA):
                    numDispersiones++;
                    impDispersiones += oPago.getMonto();
                    break;
                case (ClientesConstants.OP_CANCELADA):
                    numCancelaciones++;
                    impCancelaciones += oPago.getMonto();
                    break;
                case (ClientesConstants.OP_ACTUALIZA_NOMBRE):
                    numModificaciones++;
                    impModificaciones += oPago.getMonto();
                    break;
                case (ClientesConstants.OP_SEGURO_ENVIADO):
                    numDispersiones++;
                    impDispersiones += oPago.getMonto();
                    break;
            }

            importeArchivo += oPago.getMonto();

//			if( oPago.getEstatus() == ClientesConstants.OP_DISPERSADA ){
//				numDispersiones++;
//				impDispersiones += oPago.getMonto();
//			}else if( oPago.getEstatus() == ClientesConstants.OP_CANCELADA ){
//				numCancelaciones++;
//				impCancelaciones+=oPago.getMonto();
//			}
//			importeArchivo+=oPago.getMonto();
        }
    }

    public String getFileName() {
        String filename = "60";
        try {

            String fecha = Convertidor.dateToString(new Date(), "yyyyMMdd");

            filename += fecha.substring(3, 4);
            filename += fecha.substring(4, 6);
            filename += fecha.substring(6, 8);
            filename += FormatUtil.completaCadena(envio + "", numerico, 4, "L");
            filename += ".DIS";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return filename;
    }

    public String getFileName(String filename) {
        try {

            String fecha = Convertidor.dateToString(new Date(), "yyyyMMdd");

            filename += fecha.substring(2, 4);
            filename += fecha.substring(4, 6);
            filename += fecha.substring(6, 8);
            filename += FormatUtil.completaCadena(envio + "", numerico, 4, "L");
            filename += ".txt";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return filename;
    }

    public String getFileNameCSV(String filename) {
        try {

            String fecha = Convertidor.dateToString(new Date(), "yyyyMMdd");

            filename += fecha.substring(2, 4);
            filename += fecha.substring(4, 6);
            filename += fecha.substring(6, 8);
            filename += FormatUtil.completaCadena(envio + "", numerico, 4, "L");
            filename += ".csv";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return filename;
    }

    public String getFileNameTelecom(int numEnvio) {
        String filename = "";
        try {
            String fecha = Convertidor.dateToString(new Date(), "yyyyMMdd");
            String envio = FormatUtil.completaCadena(numEnvio + "", numerico, 3, "L");
            filename = "S09_" + fecha;
            filename += "." + envio;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return filename;
    }

    public List getOrdenPagofromFile(String path, String filename, String user) {

        String linea = null;
        String lineaBan = null;
        OrdenDePagoVO oPago = null;
        OrdenDePagoVO odpVO = null;
        OrdenDePagoDAO oPagoDAO = new OrdenDePagoDAO();
        List<OrdenDePagoVO> list = new ArrayList<OrdenDePagoVO>();
        String tipoRegistro = null;
        String referencia = null;
        String codRespuesta = null;
        BufferedReader buffer = null;
        int tipoBanco = 0;
        Connection con = null;
        boolean updateOK = true;
        BitacoraVO bitacora = null;
        BitacoraDAO bitacoradao = new BitacoraDAO();
        try {
            con = ConnectionManager.getMySQLConnection();
            con.setAutoCommit(false);
            if (filename.toUpperCase().startsWith("BANORTE_")) {
                myLogger.debug("es Banorte");
                tipoBanco = ClientesConstants.ID_BANCO_BANORTE;
//                buffer = new BufferedReader(new InputStreamReader(new FileInputStream(path + filename), "UTF-16"));
                buffer = new BufferedReader(new InputStreamReader(new FileInputStream(path + filename), "ISO-8859-1"));
            } else if (filename.startsWith("BANCOMER_")) {
                myLogger.debug("es Bancomer");
                tipoBanco = ClientesConstants.ID_BANCO_BANCOMER;
                buffer = new BufferedReader(new InputStreamReader(new FileInputStream(path + filename), "ISO-8859-1"));
            } else if (filename.startsWith("SCOTIA_")) {
                myLogger.debug("es Scotia");
                tipoBanco = ClientesConstants.ID_BANCO_SCOTIABANK;
                buffer = new BufferedReader(new InputStreamReader(new FileInputStream(path + filename), "ISO-8859-1"));
            } else if (filename.startsWith("BANAMEX_")) {
                myLogger.debug("es Banamex");
                tipoBanco = ClientesConstants.ID_BANCO_BANAMEX;
                buffer = new BufferedReader(new InputStreamReader(new FileInputStream(path + filename), "ISO-8859-1"));
            } else if (filename.startsWith("SANTANDER_")) {
                myLogger.debug("es Santander");
                tipoBanco = ClientesConstants.ID_BANCO_SANTANDER_NVO;
                buffer = new BufferedReader(new InputStreamReader(new FileInputStream(path + filename), "ISO-8859-1"));
            }
            if (tipoBanco > 0) {
                myLogger.debug("tipoBanco " + tipoBanco);
                while ((linea = buffer.readLine()) != null) {
                    if (linea.trim().length() > 0) {
                        // Pregunto por el tipo de banco para saber como leer el archivo
                        if (tipoBanco == ClientesConstants.ID_BANCO_BANORTE) {
                            tipoRegistro = linea.trim().substring(0, 1);
                            if (tipoRegistro.equals("1")) {
                                codRespuesta = linea.trim().substring(6, 8);
                                if (codRespuesta.equals("61")) {
                                    referencia = linea.trim().substring(21, 34);
                                    odpVO = new OrdenDePagoVO();
                                    odpVO.setMonto(Double.valueOf(linea.trim().substring(48, 59) + "." + linea.trim().substring(60, 62)));
                                    odpVO.setReferencia(referencia);
                                    oPago = oPagoDAO.getOrdenDePago(con, referencia);
                                    if (oPago != null) {
                                        if (oPago.getEstatus() == ClientesConstants.OP_DISPERSADA) {
                                            oPago.setNombreArchivo(filename);
                                            oPago.setEstatus(ClientesConstants.OP_COBRADA);
                                            oPago.setUsuario(user);
                                            if (oPagoDAO.updateOrdenDePago(con, oPago) != 1) {
                                                updateOK = false;
                                                break;
                                            }
                                            bitacora = new BitacoraVO();
                                            bitacora.idCliente = oPago.getIdCliente();
                                            bitacora.usuario = oPago.getUsuario();
                                            bitacora.comando = "guardaOrdenDePago.jsp";
                                            bitacora.objeto = oPago;
                                            bitacoradao.add(bitacora);
                                            odpVO.setDescEstatus("CONFIRMACION DE COBRO");
                                            odpVO.setEstatus(oPago.getEstatus());
                                        } else if (oPago.getEstatus() == ClientesConstants.OP_SEGURO_ENVIADO) {
                                            odpVO.setDescEstatus("CONFIRMACION DE COBRO ODP SEGURO");
                                            odpVO.setEstatus(oPago.getEstatus());
                                        } else {
                                            odpVO.setDescEstatus("ESTATUS PREVIO ERRONEO");
                                            odpVO.setEstatus(oPago.getEstatus());
                                        }
                                    } else {
                                        odpVO.setDescEstatus("ODP SIN COINCIDENCIA");
                                        odpVO.setEstatus(0);
                                    }
                                } else if (codRespuesta.equals("64")) {
                                    referencia = linea.trim().substring(21, 34);
                                    odpVO = new OrdenDePagoVO();
                                    odpVO.setMonto(Double.valueOf(linea.trim().substring(48, 59) + "." + linea.trim().substring(60, 62)));
                                    odpVO.setReferencia(referencia);
                                    oPago = oPagoDAO.getOrdenDePago(con, referencia);
                                    if (oPago != null) {
                                        if (oPago.getEstatus() == ClientesConstants.OP_CANCELADA) {
                                            oPago.setNombreArchivo(filename);
                                            oPago.setEstatus(ClientesConstants.OP_CANCELACION_CONFIRMADA);
                                            oPago.setUsuario(user);
                                            if (oPagoDAO.updateOrdenDePago(con, oPago) != 1) {
                                                updateOK = false;
                                                break;
                                            }
                                            bitacora = new BitacoraVO();
                                            bitacora.idCliente = oPago.getIdCliente();
                                            bitacora.usuario = oPago.getUsuario();
                                            bitacora.comando = "guardaOrdenDePago.jsp";
                                            bitacora.objeto = oPago;
                                            bitacoradao.add(bitacora);
                                            odpVO.setDescEstatus(linea.trim().substring(486, 500));
                                            odpVO.setEstatus(oPago.getEstatus());
                                        } else if (oPago != null && oPago.getEstatus() == ClientesConstants.OP_SEGURO_CANCELADO_CONFIR) {
                                            odpVO.setDescEstatus("CONFIRMACION CANCELACION ODP SEGURO");
                                            odpVO.setEstatus(oPago.getEstatus());
                                        } else {
                                            odpVO.setDescEstatus("ESTATUS PREVIO ERRONEO");
                                            odpVO.setEstatus(oPago.getEstatus());
                                        }
                                    } else {
                                        odpVO.setDescEstatus("ODP SIN COINCIDENCIA");
                                        odpVO.setEstatus(0);
                                    }
                                }
                                list.add(odpVO);
                            }
                        } else if (tipoBanco == ClientesConstants.ID_BANCO_BANCOMER) {
                            // Se procesa Bancomer
                            tipoRegistro = linea.trim().substring(0, 3);
                            if (tipoRegistro.equals("DBP")) {
                                codRespuesta = linea.trim().substring(1326, 1356).replace(" ", "");
                                if (codRespuesta.equals("REGISTRODADODEBAJA") || codRespuesta.equals("ELDOCUMENTONOESTAVIGENTE")) {
                                    referencia = linea.trim().substring(10, 23);
                                    odpVO = new OrdenDePagoVO();
                                    odpVO.setMonto(Double.valueOf(Integer.parseInt(linea.trim().substring(444, 457)) + "." + linea.trim().substring(458, 460)));
                                    odpVO.setReferencia(referencia);
                                    oPago = oPagoDAO.getOrdenDePago(con, referencia);
                                    if (oPago != null) {
                                        if (oPago.getEstatus() == ClientesConstants.OP_CANCELADA) {
                                            oPago.setNombreArchivo(filename);
                                            oPago.setEstatus(ClientesConstants.OP_CANCELACION_CONFIRMADA);
                                            oPago.setUsuario(user);
                                            if (oPagoDAO.updateOrdenDePago(con, oPago) != 1) {
                                                updateOK = false;
                                                break;
                                            }
                                            bitacora = new BitacoraVO();
                                            bitacora.idCliente = oPago.getIdCliente();
                                            bitacora.usuario = oPago.getUsuario();
                                            bitacora.comando = "guardaOrdenDePago.jsp";
                                            bitacora.objeto = oPago;
                                            bitacoradao.add(bitacora);
                                            odpVO.setDescEstatus(linea.trim().substring(1326, 1356));
                                            odpVO.setEstatus(oPago.getEstatus());
                                        } else if (oPago != null && oPago.getEstatus() == ClientesConstants.OP_SEGURO_CANCELADO_CONFIR) {
                                            odpVO.setDescEstatus("CONFIRMACION CANCELACION ODP SEGURO");
                                            odpVO.setEstatus(oPago.getEstatus());
                                        } else {
                                            odpVO.setDescEstatus("ESTATUS PREVIO ERRONEO");
                                            odpVO.setEstatus(oPago.getEstatus());
                                        }
                                    } else {
                                        odpVO.setDescEstatus("ODP SIN COINCIDENCIA");
                                        odpVO.setEstatus(0);
                                    }
                                } else {
                                    referencia = linea.trim().substring(10, 23);
                                    odpVO = new OrdenDePagoVO();
                                    odpVO.setMonto(Double.valueOf(linea.trim().substring(444, 457) + "." + linea.trim().substring(458, 460)));
                                    odpVO.setReferencia(referencia);
                                    odpVO.setDescEstatus(linea.trim().substring(1326, 1356));
                                    odpVO.setEstatus(0);
                                }
                                list.add(odpVO);
                            } else if (tipoRegistro.equals("DAP")) {
                                codRespuesta = linea.trim().substring(1326, 1356).replace(" ", "");
                                if (codRespuesta.equals("OPERACIONEXITOSA")) {
                                    referencia = linea.trim().substring(10, 23);
                                    odpVO = new OrdenDePagoVO();
                                    odpVO.setMonto(Double.valueOf(linea.trim().substring(444, 457) + "." + linea.trim().substring(458, 460)));
                                    odpVO.setReferencia(referencia);
                                    oPago = oPagoDAO.getOrdenDePago(con, referencia);
                                    if (oPago != null) {
                                        if (oPago.getEstatus() == ClientesConstants.OP_DISPERSADA) {
                                            oPago.setNombreArchivo(filename);
                                            oPago.setEstatus(ClientesConstants.OP_COBRADA);
                                            oPago.setUsuario(user);
                                            if (oPagoDAO.updateOrdenDePago(con, oPago) != 1) {
                                                updateOK = false;
                                                break;
                                            }
                                            bitacora = new BitacoraVO();
                                            bitacora.idCliente = oPago.getIdCliente();
                                            bitacora.usuario = oPago.getUsuario();
                                            bitacora.comando = "guardaOrdenDePago.jsp";
                                            bitacora.objeto = oPago;
                                            bitacoradao.add(bitacora);
                                            odpVO.setDescEstatus(linea.trim().substring(1326, 1356));
                                            odpVO.setEstatus(oPago.getEstatus());
                                        } else if (oPago != null && oPago.getEstatus() == ClientesConstants.OP_SEGURO_ENVIADO) {
                                            odpVO.setDescEstatus("CONFIRMACION DE COBRO ODP SEGURO");
                                            odpVO.setEstatus(oPago.getEstatus());
                                        } else {
                                            odpVO.setDescEstatus("ESTATUS PREVIO ERRONEO");
                                            odpVO.setEstatus(oPago.getEstatus());
                                        }
                                    } else {
                                        odpVO.setDescEstatus("ODP SIN COINCIDENCIA");
                                        odpVO.setEstatus(0);
                                    }
                                } else {
                                    referencia = linea.trim().substring(10, 23);
                                    odpVO = new OrdenDePagoVO();
                                    odpVO.setMonto(Double.valueOf(linea.trim().substring(444, 457) + "." + linea.trim().substring(458, 460)));
                                    odpVO.setReferencia(referencia);
                                    odpVO.setDescEstatus(linea.trim().substring(1326, 1356));
                                    odpVO.setEstatus(0);
                                }
                                list.add(odpVO);
                            }
                        } else if (tipoBanco == ClientesConstants.ID_BANCO_BANAMEX) {
                            // Se procesa Banamex
                            codRespuesta = linea.trim().substring(22, 26);
                            if (codRespuesta.equals("P001")) {
                                referencia = linea.trim().substring(7, 20);
                                odpVO = new OrdenDePagoVO();
                                odpVO.setMonto(0);
                                odpVO.setReferencia(referencia);
                                oPago = oPagoDAO.getOrdenDePago(con, referencia);
                                if (oPago != null) {
                                    if (oPago.getEstatus() == ClientesConstants.OP_DISPERSADA) {
                                        oPago.setNombreArchivo(filename);
                                        oPago.setEstatus(ClientesConstants.OP_COBRADA);
                                        oPago.setUsuario(user);
                                        if (oPagoDAO.updateOrdenDePago(con, oPago) != 1) {
                                            updateOK = false;
                                            break;
                                        }
                                        bitacora = new BitacoraVO();
                                        bitacora.idCliente = oPago.getIdCliente();
                                        bitacora.usuario = oPago.getUsuario();
                                        bitacora.comando = "guardaOrdenDePago.jsp";
                                        bitacora.objeto = oPago;
                                        bitacoradao.add(bitacora);
                                        odpVO.setDescEstatus(linea.trim().substring(29));
                                        odpVO.setEstatus(oPago.getEstatus());
                                    } else if (oPago != null && oPago.getEstatus() == ClientesConstants.OP_SEGURO_ENVIADO) {
                                        odpVO.setDescEstatus("CONFIRMACION COBRO ODP SEGURO");
                                        odpVO.setEstatus(oPago.getEstatus());
                                    } else {
                                        odpVO.setDescEstatus("ESTATUS PREVIO ERRONEO");
                                        odpVO.setEstatus(oPago.getEstatus());
                                    }
                                } else {
                                    odpVO.setDescEstatus("ODP SIN COINCIDENCIA");
                                    odpVO.setEstatus(0);
                                }
                                list.add(odpVO);
                            } else if (codRespuesta.equals("P956")) {
                                referencia = linea.trim().substring(7, 20);
                                odpVO = new OrdenDePagoVO();
                                odpVO.setMonto(0);
                                odpVO.setReferencia(referencia);
                                oPago = oPagoDAO.getOrdenDePago(con, referencia);
                                if (oPago != null) {
                                    if (oPago.getEstatus() == ClientesConstants.OP_CANCELADA) {
                                        oPago.setNombreArchivo(filename);
                                        oPago.setEstatus(ClientesConstants.OP_CANCELACION_CONFIRMADA);
                                        oPago.setUsuario(user);
                                        if (oPagoDAO.updateOrdenDePago(con, oPago) != 1) {
                                            updateOK = false;
                                            break;
                                        }
                                        bitacora = new BitacoraVO();
                                        bitacora.idCliente = oPago.getIdCliente();
                                        bitacora.usuario = oPago.getUsuario();
                                        bitacora.comando = "guardaOrdenDePago.jsp";
                                        bitacora.objeto = oPago;
                                        bitacoradao.add(bitacora);
                                        odpVO.setDescEstatus(linea.trim().substring(29));
                                        odpVO.setEstatus(oPago.getEstatus());
                                    } else if (oPago != null && oPago.getEstatus() == ClientesConstants.OP_SEGURO_CANCELADO_CONFIR) {
                                        odpVO.setDescEstatus("CONFIRMACION CANCELACION ODP SEGURO");
                                        odpVO.setEstatus(oPago.getEstatus());
                                    } else {
                                        odpVO.setDescEstatus("ESTATUS PREVIO ERRONEO");
                                        odpVO.setEstatus(oPago.getEstatus());
                                    }
                                } else {
                                    odpVO.setDescEstatus("ODP SIN COINCIDENCIA");
                                    odpVO.setEstatus(0);
                                }
                                list.add(odpVO);
                            } else {
                                referencia = linea.trim().substring(7, 20);
                                odpVO = new OrdenDePagoVO();
                                odpVO.setMonto(0);
                                odpVO.setReferencia(referencia);
                                odpVO.setDescEstatus(linea.trim().substring(29));
                                odpVO.setEstatus(0);
                                list.add(odpVO);
                            }
                        } else if (tipoBanco == ClientesConstants.ID_BANCO_SCOTIABANK) {
                            // Se procesa Scotiabank
                            codRespuesta = linea.trim().substring(2, 4);
                            if (codRespuesta.equals("DP")) {
                                referencia = linea.trim().substring(38, 51);
                                odpVO = new OrdenDePagoVO();
                                odpVO.setMonto(Double.valueOf(Integer.parseInt(linea.trim().substring(8, 21)) + "." + linea.trim().substring(21, 23)));
                                odpVO.setReferencia(referencia);
                                oPago = oPagoDAO.getOrdenDePago(con, referencia);
                                if (oPago != null) {
                                    if (oPago.getEstatus() == ClientesConstants.OP_DISPERSADA) {
                                        oPago.setNombreArchivo(filename);
                                        oPago.setEstatus(ClientesConstants.OP_COBRADA);
                                        oPago.setUsuario(user);
                                        if (oPagoDAO.updateOrdenDePago(con, oPago) != 1) {
                                            updateOK = false;
                                            break;
                                        }
                                        bitacora = new BitacoraVO();
                                        bitacora.idCliente = oPago.getIdCliente();
                                        bitacora.usuario = oPago.getUsuario();
                                        bitacora.comando = "guardaOrdenDePago.jsp";
                                        bitacora.objeto = oPago;
                                        bitacoradao.add(bitacora);
                                        odpVO.setDescEstatus("PAGADA");
                                        odpVO.setEstatus(oPago.getEstatus());
                                    } else if (oPago != null && oPago.getEstatus() == ClientesConstants.OP_SEGURO_ENVIADO) {
                                        odpVO.setDescEstatus("CONFIRMACION COBRO ODP SEGURO");
                                        odpVO.setEstatus(oPago.getEstatus());
                                    } else {
                                        odpVO.setDescEstatus("ESTATUS PREVIO ERRONEO");
                                        odpVO.setEstatus(oPago.getEstatus());
                                    }
                                } else {
                                    odpVO.setDescEstatus("ODP SIN COINCIDENCIA");
                                    odpVO.setEstatus(0);
                                }
                                list.add(odpVO);
                            } else if (codRespuesta.equals("--")) {
                                referencia = linea.trim().substring(38, 51);
                                odpVO = new OrdenDePagoVO();
                                odpVO.setMonto(Double.valueOf(Integer.parseInt(linea.trim().substring(8, 21)) + "." + linea.trim().substring(21, 23)));
                                odpVO.setReferencia(referencia);
                                oPago = oPagoDAO.getOrdenDePago(con, referencia);
                                if (oPago != null) {
                                    if (oPago.getEstatus() == ClientesConstants.OP_CANCELADA) {
                                        oPago.setNombreArchivo(filename);
                                        oPago.setEstatus(ClientesConstants.OP_CANCELACION_CONFIRMADA);
                                        oPago.setUsuario(user);
                                        if (oPagoDAO.updateOrdenDePago(con, oPago) != 1) {
                                            updateOK = false;
                                            break;
                                        }
                                        bitacora = new BitacoraVO();
                                        bitacora.idCliente = oPago.getIdCliente();
                                        bitacora.usuario = oPago.getUsuario();
                                        bitacora.comando = "guardaOrdenDePago.jsp";
                                        bitacora.objeto = oPago;
                                        bitacoradao.add(bitacora);
                                        odpVO.setDescEstatus("CANCELADA");
                                        odpVO.setEstatus(oPago.getEstatus());
                                    } else if (oPago != null && oPago.getEstatus() == ClientesConstants.OP_SEGURO_CANCELADO_CONFIR) {
                                        odpVO.setDescEstatus("CONFIRMACION CANCELACION ODP SEGURO");
                                        odpVO.setEstatus(oPago.getEstatus());
                                    } else {
                                        odpVO.setDescEstatus("ESTATUS PREVIO ERRONEO");
                                        odpVO.setEstatus(oPago.getEstatus());
                                    }
                                } else {
                                    odpVO.setDescEstatus("ODP SIN COINCIDENCIA");
                                    odpVO.setEstatus(0);
                                }
                                list.add(odpVO);
                            }
                        } else if (tipoBanco == ClientesConstants.ID_BANCO_SANTANDER_NVO) {
                            codRespuesta = linea.trim().substring(206, 207);
                            if (codRespuesta.equals(" ")) {
                                codRespuesta = linea.trim().substring(207, 208);
                            }
                            if (codRespuesta.equals("L")) {
                                referencia = linea.trim().substring(23, 36);
                                odpVO = new OrdenDePagoVO();
                                odpVO.setMonto(Double.valueOf(Integer.parseInt(linea.trim().substring(171, 185)) + "." + linea.trim().substring(185, 187)));
                                odpVO.setReferencia(referencia);
                                oPago = oPagoDAO.getOrdenDePago(con, referencia);
                                if (oPago != null) {
                                    if (oPago.getEstatus() == ClientesConstants.OP_DISPERSADA) {
                                        oPago.setNombreArchivo(filename);
                                        oPago.setEstatus(ClientesConstants.OP_COBRADA);
                                        oPago.setUsuario(user);
                                        if (oPagoDAO.updateOrdenDePago(con, oPago) != 1) {
                                            updateOK = false;
                                            break;
                                        }
                                        bitacora = new BitacoraVO();
                                        bitacora.idCliente = oPago.getIdCliente();
                                        bitacora.usuario = oPago.getUsuario();
                                        bitacora.comando = "guardaOrdenDePago.jsp";
                                        bitacora.objeto = oPago;
                                        bitacoradao.add(bitacora);
                                        odpVO.setDescEstatus("PAGADA");
                                        odpVO.setEstatus(oPago.getEstatus());
                                    } else if (oPago != null && oPago.getEstatus() == ClientesConstants.OP_SEGURO_ENVIADO) {
                                        odpVO.setDescEstatus("CONFIRMACION COBRO ODP SEGURO");
                                        odpVO.setEstatus(oPago.getEstatus());
                                    } else {
                                        odpVO.setDescEstatus("ESTATUS PREVIO ERRONEO");
                                        odpVO.setEstatus(oPago.getEstatus());
                                    }
                                } else {
                                    odpVO.setDescEstatus("ODP SIN COINCIDENCIA");
                                    odpVO.setEstatus(0);
                                }
                                list.add(odpVO);
                            } else if (codRespuesta.equals("C")) {
                                referencia = linea.trim().substring(23, 36);
                                odpVO = new OrdenDePagoVO();
                                odpVO.setMonto(Double.valueOf(Integer.parseInt(linea.trim().substring(171, 185)) + "." + linea.trim().substring(185, 187)));
                                odpVO.setReferencia(referencia);
                                oPago = oPagoDAO.getOrdenDePago(con, referencia);
                                if (oPago != null) {
                                    if (oPago.getEstatus() == ClientesConstants.OP_CANCELADA) {
                                        oPago.setNombreArchivo(filename);
                                        oPago.setEstatus(ClientesConstants.OP_CANCELACION_CONFIRMADA);
                                        oPago.setUsuario(user);
                                        if (oPagoDAO.updateOrdenDePago(con, oPago) != 1) {
                                            updateOK = false;
                                            break;
                                        }
                                        bitacora = new BitacoraVO();
                                        bitacora.idCliente = oPago.getIdCliente();
                                        bitacora.usuario = oPago.getUsuario();
                                        bitacora.comando = "guardaOrdenDePago.jsp";
                                        bitacora.objeto = oPago;
                                        bitacoradao.add(bitacora);
                                        odpVO.setDescEstatus("CANCELADA");
                                        odpVO.setEstatus(oPago.getEstatus());
                                    } else if (oPago != null && oPago.getEstatus() == ClientesConstants.OP_SEGURO_CANCELADO_CONFIR) {
                                        odpVO.setDescEstatus("CONFIRMACION CANCELACION ODP SEGURO");
                                        odpVO.setEstatus(oPago.getEstatus());
                                    } else {
                                        odpVO.setDescEstatus("ESTATUS PREVIO ERRONEO");
                                        odpVO.setEstatus(oPago.getEstatus());
                                    }
                                } else {
                                    odpVO.setDescEstatus("ODP SIN COINCIDENCIA");
                                    odpVO.setEstatus(0);
                                }
                                list.add(odpVO);
                            }
                        }
                    }
                }
                if (updateOK) {
                    con.commit();
                } else {
                    con.rollback();
                    list.clear();
                }
            }
            buffer.close();
            myLogger.debug("list " + list.size());
            if (list.isEmpty()) {
                mensaje = "El Archivo \"" + filename + "\", no contiene la información requerida. ";
            }

        } catch (Exception e) {
            myLogger.error("Error ", e);
            list.clear();
            mensaje = "Ocurrio un error al Leer el Archivo \"" + filename + "\"";
        } finally {
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException exc) {
                myLogger.error("getOrdenDePago", exc);
            }
        }
        return list;

    }

    public ArrayList<OrdenDePagoVO> getOrdenesPago(HttpServletRequest request) {

        ArrayList<OrdenDePagoVO> arrOrdnes = new ArrayList<OrdenDePagoVO>();
        String[] idOrdenes = null;
        BitacoraUtil bitacora = new BitacoraUtil(0, request.getRemoteUser(), "CommandGeneraOrdenesPago");
        try {
            int banco = HTMLHelper.getParameterInt(request, "banco");
            idOrdenes = request.getParameterValues("idCheckBox");
            if (idOrdenes != null) {
                if (banco != 16) {
                    for (int i = 0; i < idOrdenes.length; i++) {
                        arrOrdnes.add(new OrdenDePagoVO(HTMLHelper.getParameterInt(request, "idOrdenPago" + idOrdenes[i]), HTMLHelper.getParameterInt(request, "idCliente" + idOrdenes[i]), HTMLHelper.getParameterInt(request, "idSolicitud" + idOrdenes[i]), request.getParameter("nombre" + idOrdenes[i]),
                                HTMLHelper.getParameterDouble(request, "importe" + idOrdenes[i]), HTMLHelper.getParameterInt(request, "estatus" + idOrdenes[i]), request.getParameter("nomSucursal" + idOrdenes[i]), request.getParameter("grupo" + idOrdenes[i]),
                                HTMLHelper.getParameterInt(request, "idSucursal" + idOrdenes[i]), request.getParameter("nombres" + idOrdenes[i]), request.getParameter("apaterno" + idOrdenes[i]), request.getParameter("amaterno" + idOrdenes[i]),
                                HTMLHelper.getParameterInt(request, "idOficTelecom" + idOrdenes[i]), request.getParameter("nomOficTelecom" + idOrdenes[i]), request.getParameter("identificacion" + idOrdenes[i]), request.getParameter("referencia" + idOrdenes[i])));
                        bitacora.registraEventoString("grupo=" + request.getParameter("grupo" + idOrdenes[i]) + ", idCliente=" + HTMLHelper.getParameterInt(request, "idCliente" + idOrdenes[i]) + ", idSolicitud=" + HTMLHelper.getParameterInt(request, "idSolicitud" + idOrdenes[i]) + " importe=" + HTMLHelper.getParameterDouble(request, "importe" + idOrdenes[i]));
                    }
                } else {
                    for (int i = 0; i < idOrdenes.length; i++) {
                        arrOrdnes.add(new OrdenDePagoVO(HTMLHelper.getParameterInt(request, "idOrdenPago" + idOrdenes[i]), HTMLHelper.getParameterInt(request, "idCliente" + idOrdenes[i]), HTMLHelper.getParameterInt(request, "idSolicitud" + idOrdenes[i]), request.getParameter("nombre" + idOrdenes[i]),
                                HTMLHelper.getParameterDouble(request, "importe" + idOrdenes[i]), HTMLHelper.getParameterInt(request, "estatus" + idOrdenes[i]), request.getParameter("nomSucursal" + idOrdenes[i]), request.getParameter("grupo" + idOrdenes[i]),
                                HTMLHelper.getParameterInt(request, "idSucursal" + idOrdenes[i]), request.getParameter("nombres" + idOrdenes[i]), request.getParameter("apaterno" + idOrdenes[i]), request.getParameter("amaterno" + idOrdenes[i]),
                                HTMLHelper.getParameterInt(request, "idOficTelecom" + idOrdenes[i]), request.getParameter("nomOficTelecom" + idOrdenes[i]), request.getParameter("identificacion" + idOrdenes[i]), request.getParameter("referencia" + idOrdenes[i]),
                                Timestamp.valueOf(request.getParameter("idOdPFechEnv" + idOrdenes[i]))));
                        bitacora.registraEventoString("grupo=" + request.getParameter("grupo" + idOrdenes[i]) + ", idCliente=" + HTMLHelper.getParameterInt(request, "idCliente" + idOrdenes[i]) + ", idSolicitud=" + HTMLHelper.getParameterInt(request, "idSolicitud" + idOrdenes[i]) + " importe=" + HTMLHelper.getParameterDouble(request, "importe" + idOrdenes[i]));
                    }
                    
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrOrdnes;
    }
}
