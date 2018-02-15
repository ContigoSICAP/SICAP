package com.sicap.clientes.helpers;

import com.sicap.clientes.dao.CarteraTCIDAO;
import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.dao.CifraControlDAO;
import com.sicap.clientes.dao.IncidenciaPagoGrupalDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.sicap.clientes.dao.PagoDAO;
import com.sicap.clientes.dao.PagoReferenciadoDAO;
import com.sicap.clientes.dao.PagosExcedentesDAO;
import com.sicap.clientes.dao.PaynetDAO;
import com.sicap.clientes.dao.ReferenciaGeneralDAO;
import com.sicap.clientes.dao.SaldoHistoricoDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.cartera.CreditoCartDAO;
import com.sicap.clientes.dao.cartera.PagoManualDAO;
import com.sicap.clientes.dao.cartera.TablaAmortDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.helpers.cartera.TransaccionesHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.vo.ArchivoAsociadoVO;
import com.sicap.clientes.vo.CifraControlVO;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.IncidenciaPagoGrupalVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.PagareVO;
import com.sicap.clientes.vo.PagoExcedenteVO;
import com.sicap.clientes.vo.PagoGrupalVO;
import com.sicap.clientes.vo.PagoPagareVO;
import com.sicap.clientes.vo.PagoReferenciadoVO;
import com.sicap.clientes.vo.PagoVO;
import com.sicap.clientes.vo.PaynetVO;
import com.sicap.clientes.vo.ReferenciaGeneralVO;
import com.sicap.clientes.vo.SaldoHistoricoVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.cartera.CreditoCartVO;
import com.sicap.clientes.vo.cartera.TablaAmortVO;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class PagosReferenciadosHelper {

    public static Logger myLogger = Logger.getLogger(PagosReferenciadosHelper.class);
    public static int totT24 = 0;
    public static int totAnt = 0;
    public static int totInc = 0;
    private String referencia = null;
    private String tipo_reg = null;
    private String nombre = "";
    private String deposito = null;
    private String saldo = null;
    private String fecha = null;
    private String banco = null;
    private final String ctaBancoHSBC = "MXN100210001";
    private final String ctaBancoBANORTE = "MXN100220001";
    private double depositoParse = 0;
    ArrayList array = null;
    
    public PagoReferenciadoVO getPagosReferenciadosVO(String lineaArchEnt) {
        double deposit = 0;
        java.sql.Date fechaVO = null;
        PagoReferenciadoVO pagosVO = new PagoReferenciadoVO();
        String[] fragArchEnt;
        fragArchEnt = lineaArchEnt.split("	");
        myLogger.info("Fecha::" + fragArchEnt[0]);
        myLogger.info("Referencia::" + fragArchEnt[1]);
        myLogger.info("Retiro::" + fragArchEnt[2]);
        myLogger.info("Deposito::" + fragArchEnt[3]);
        myLogger.info("Saldo::" + fragArchEnt[4]);
        myLogger.info("Banco::" + fragArchEnt[5]);
        fecha = fragArchEnt[0].trim().toUpperCase();
        referencia = fragArchEnt[1].trim();
        pagosVO.referencia = referencia;
        tipo_reg = fragArchEnt[2].trim();
        deposito = formatCantidad(fragArchEnt[3].trim());
        try {
            deposit = Double.parseDouble(deposito);
        } catch (NumberFormatException exc) {

            myLogger.info("Error al parsear el deposito::" + deposit);
            myLogger.info("Se asignara el siguiente valor al deposito::" + 0);
            deposit = 0;
            exc.printStackTrace();
        }
        myLogger.info("Deposito::::::" + deposito);
        if (deposit == 0) {
            pagosVO.descripcion = "IGNORAR";
            return pagosVO;
        }
        saldo = formatCantidad(fragArchEnt[4].trim());
        banco = formatCantidad(fragArchEnt[5].trim());
        myLogger.info("Saldo:::::::" + saldo);
        if (fragArchEnt.length > 5) {
            for (int j = 5; j < fragArchEnt.length; j++) {
                if (fragArchEnt[j].trim().length() > 0) {
                    nombre = fragArchEnt[j];
                    myLogger.info("Valor obtenido de nombre::" + nombre);
                    break;
                }
            }
        }
        try {
            fechaVO = Convertidor.stringToSqlDate(fecha);
        } catch (Exception exc) {
            myLogger.error("getPAgosReferenciadosVO", exc);
        }

        pagosVO.referencia = referencia;
        pagosVO.fecha_movimiento = fechaVO;
        pagosVO.descripcion = tipo_reg;
        try {
            deposito = deposito.trim();
            saldo = saldo.trim();
            banco = banco.trim();
            myLogger.info("Por parsear::" + deposito + " y " + saldo);
            myLogger.info("Banco a parsear::" + banco);
            pagosVO.deposito = Double.parseDouble(deposito);
            pagosVO.saldo = Double.parseDouble(saldo);
            pagosVO.banco = Integer.parseInt(banco);
        } catch (NumberFormatException exc) {
            myLogger.info("getPAgosReferenciadosVO", exc);
            PagoReferenciadoDAO dao = new PagoReferenciadoDAO();
            try {
                dao.addIncidencia(pagosVO, 3, "Deposito o saldo no contenido");
            } catch (ClientesException ec) {
                myLogger.info("getPAgosReferenciadosVO", ec);
            }
        }
        return pagosVO;
    }

    public boolean existeReferencia(PagoReferenciadoVO pagosVO) {
        myLogger.info("Referencia en pagosVO:, dentro de existe referencia:" + pagosVO.referencia);
        PagoReferenciadoDAO pagosDAO = new PagoReferenciadoDAO();
        try {
            if (pagosDAO.existeReferencia(pagosVO.referencia)) {
                return true;
            }
        } catch (Exception exc) {
            myLogger.error("existeReferencia", exc);
        }
        return false;
    }

    public boolean esPagoT24(PagoReferenciadoVO pagosVO) {
        PagoReferenciadoDAO pagosDAO = new PagoReferenciadoDAO();
        try {
            if (pagosDAO.esPagoT24(pagosVO.referencia)) {
                return true;
            }
        } catch (ClientesException exc) {
            myLogger.error("existeReferencia", exc);
        }
        return false;
    }

    public void agregaPago(PagoReferenciadoVO pagosVO, String tipo) throws ClientesException {
        PagoVO pago = new PagoVO();
        PagoDAO pagoDAO = new PagoDAO();
        /*String contrato = pago.referencia;
         if(contrato!=null && contrato.startsWith("LD"))
         contrato = contrato.substring(0,12);
         */
        pago.referencia = pagosVO.referencia;
        pago.monto = pagosVO.deposito;
        pago.fechaPago = pagosVO.fecha_movimiento;
        pago.tipo = tipo;
        pago.enviado = 0;
        myLogger.info("Por llamar a insertPago...");
        pagoDAO.insertPago(pago);
    }

    public String agregaPagoArchivo(PagoReferenciadoVO pagosVO) {
        String lineaEsc = null;
        try {
            //String nom_arch_sal = "C:\\Interfase\\ArchivoSalidaT24\\PAGOST24_";
            myLogger.info("En agregaPagoArchivo");
            //SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");
            //java.util.Date date = new java.util.Date();
            //String fechaHoy = sdf.format(date);
            //nom_arch_sal += fechaHoy + ".csv";
            //FileWriter archSync = new FileWriter(nom_arch_sal, true);
            PagoReferenciadoDAO pagosDAO = new PagoReferenciadoDAO();
            String contrato = pagosDAO.getContrato(pagosVO.referencia);
            if (contrato != null) {
                pagosVO.contrato = contrato;
                contrato = contrato.replaceAll("\"", "");
                if (contrato.startsWith("LD")) {
                    contrato = contrato.substring(0, 12);
                }
                String fecha_mov = ClientesUtil.formatoFechaSyncronet(pagosVO.fecha_movimiento);
                //if(pagosVO.nombre != null || pagosVO.nombre.equalsIgnoreCase("1"))
                if (pagosVO.banco == 1) {
                    lineaEsc = fecha_mov + "," + contrato + "," + pagosVO.deposito + "," + this.ctaBancoHSBC + "\n";
                    //archSync.write(fecha_mov + "," + contrato + "," + pagosVO.deposito + "," + this.ctaBancoHSBC + "\n");
                }
                if (pagosVO.banco == 2) {
                    lineaEsc = fecha_mov + "," + contrato + "," + pagosVO.deposito + "," + this.ctaBancoBANORTE + "\n";
                    //archSync.write(fecha_mov + "," + contrato + "," + pagosVO.deposito + "," + this.ctaBancoBANORTE + "\n");
                }
                //else
                //	archSync.write(fecha_mov + "," + contrato + "," + pagosVO.deposito + "\n");
                //archSync.close();
                totT24++;
            }
        } /*catch(IOException exc){
         exc.printStackTrace();
         myLogger.info("Exception caught:" + exc);
         }*/ catch (ClientesException exc) {
            myLogger.error("agregaPagoArchivo", exc);
        }
        return lineaEsc;
    }

    public void agregaIncidencia(PagoReferenciadoVO pagosVO) {
        int tipo_incidencia = 1;
        String observaciones = "Archivo no encontrado";
        try {
            PagoReferenciadoDAO pagosDAO = new PagoReferenciadoDAO();
            pagosDAO.addIncidencia(pagosVO, tipo_incidencia, observaciones);
            totInc++;
        } catch (ClientesException exc) {
            exc.printStackTrace();
            myLogger.info("Exception caught:" + exc);
        }
    }

    public void addPagoReferenciaAnterior(PagoReferenciadoVO pagosVO) {
        try {
            PagoReferenciadoDAO pagosDAO = new PagoReferenciadoDAO();
            pagosDAO.addReferenciaAnterior(pagosVO);
            pagosVO.contrato = pagosDAO.getContrato(pagosVO.referencia);
            totAnt++;
        } catch (ClientesException exc) {
            exc.printStackTrace();
            myLogger.info("Exception caught:" + exc);
        }
    }

    public String formatCantidad(String cantidad) {
        String cantFormat = new String();
        if (!cantidad.trim().equalsIgnoreCase("")) {
            cantidad = cantidad.trim().substring(cantidad.indexOf("$") + 1);
            myLogger.info("Sin $::" + cantidad);
            if (cantidad.length() > 0 && cantidad.indexOf(" ") == 0) {
                cantidad = cantidad.substring(1);
            }
            String[] tokens;
            try {

                int posComa = cantidad.indexOf(',');
                if (posComa >= 0) {
                    tokens = cantidad.split(",");
                    for (int i = 0; i < tokens.length; i++) {
                        cantFormat += tokens[i];
                    }
                } else {
                    cantFormat = cantidad;
                }
            } catch (Exception exc) {
                myLogger.info("Error generado::" + exc);
            }
        }
        return cantFormat;
    }

    // DESDE AQUI ES BANORTE
    public StringBuffer procesaPagosBANORTE(String[] lineasArchivo) {
        myLogger.info("Iniciando lectura de archivo BANORTE...");
        myLogger.info("Numero de lineas::" + lineasArchivo.length);
        String fechaBanorte = "";
        String lineaSalida = "";
        StringBuffer strb = new StringBuffer();
        for (int i = 0; i < lineasArchivo.length; i++) {
            if (i == 0) {
                fechaBanorte = leerEncabezado(lineasArchivo[i]);
            } else if (i == lineasArchivo.length - 1) {
                myLogger.info("EOF..");
            } else {
                lineaSalida = leerPagosBanorte(fechaBanorte, lineasArchivo[i]);
                if (lineaSalida.trim().length() > 0) {
                    String lineaArchivo = generarLineaArchivo(lineaSalida, fechaBanorte);

                    //strb.append(lineaArchivo);
                }
            }
        }
        return strb;
    }

    public String leerEncabezado(String encabezado) {
        String fechaBanorte = encabezado.substring(7, 15);
        myLogger.info("Fecha::" + fechaBanorte);
        return fechaBanorte;
    }

    public String leerPagosBanorte(String fechaBanorte, String body) {
        String lineaSalida = "";
        try {
            PagoReferenciadoVO pagosRefVO = new PagoReferenciadoVO();
            String depositoBanorte = body.substring(53, 67);
            depositoBanorte += ".";
            depositoBanorte += body.substring(67, 69);
            String referenciaBanorte = body.substring(96, 109);
            String nombre = body.substring(109, 149);
            myLogger.info("Deposito::" + depositoBanorte);
            myLogger.info("Referencia::" + referenciaBanorte);
            depositoParse = Double.parseDouble(depositoBanorte);
            pagosRefVO.banco = 2;
            pagosRefVO.contrato = referenciaBanorte;
            pagosRefVO.deposito = depositoParse;
            pagosRefVO.descripcion = "";
            pagosRefVO.nombre = nombre.trim();
            pagosRefVO.fecha_movimiento = Convertidor.stringToSqlDate(fechaBanorte, ClientesConstants.FORMATO_FECHA_SYNCRONET);
            pagosRefVO.referencia = referenciaBanorte;
            myLogger.info("Deposito parseado::" + depositoParse);
            if (existeReferencia(pagosRefVO)) {
                if (this.esPagoT24(pagosRefVO) || pagosRefVO.referencia.substring(0, 2).equalsIgnoreCase("LD")) {
                    pagosRefVO.esT24 = true;
                    myLogger.info("Generando linea de BANORTE para archivo de salida...");
                    lineaSalida = pagosRefVO.contrato + "," + pagosRefVO.deposito + "," + ctaBancoBANORTE;
                    //lineaSalida = agregaPagoArchivo(pagosRefVO);
                    agregaPago(pagosRefVO, "T");
                } else {
                    pagosRefVO.esT24 = true;
                    addPagoReferenciaAnterior(pagosRefVO);
                    agregaPago(pagosRefVO, "A");
                }
            } else {
                this.agregaIncidencia(pagosRefVO);
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
        return lineaSalida;
    }

    public String generarLineaArchivo(String lineaSalida, String fechaBanorte) {
        lineaSalida = fechaBanorte + "," + lineaSalida + "\n";
        myLogger.info("Linea armada para el archivo de salida::" + lineaSalida);
        return lineaSalida;
    }

    // HASTA AQUI LLEGA BANORTE
    public boolean buscarReferencia(File archEnt) {
        boolean existeReg = false;
        String[] fragArchEnt;
        try {
            BufferedReader br = new BufferedReader(new FileReader(archEnt));
            String lineArch = br.readLine();
            while (lineArch != null && !existeReg) {
                fragArchEnt = lineArch.split("	");
                myLogger.info("Referencia encontrada::" + fragArchEnt[1]);

                fecha = fragArchEnt[0];
                referencia = fragArchEnt[1].substring(0, fragArchEnt[1].length() - 3);
                tipo_reg = fragArchEnt[2];
                deposito = fragArchEnt[3];
                saldo = fragArchEnt[4];
                nombre = fragArchEnt[5];
                try {
                    PagoReferenciadoDAO pagosRef = new PagoReferenciadoDAO();
                    existeReg = pagosRef.existeReferencia(referencia);
                    lineArch = br.readLine();
                } catch (ClientesException exc) {
                    //exc.printStackTrace();
                    myLogger.info("Exception caught in PagosReferenciadosHelper.buscarReferencia::" + exc);
                }
            }
        } catch (FileNotFoundException exc) {
            //exc.printStackTrace();
            myLogger.info("Exception caught trying to open archEnt::" + exc);
        } catch (IOException exc) {
            //exc.printStackTrace();
            myLogger.info("Exception caught trying to read archEnt::" + exc);
        }
        return existeReg;
    }

    //---------------------------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------------------
    public static boolean existe(ArchivoAsociadoVO[] archivos, int tipoArchivo) {

        boolean existe = false;
        if (archivos != null && archivos.length > 0) {
            for (int i = 0; i < archivos.length; i++) {
                if (archivos[i].tipo == tipoArchivo) {
                    existe = true;
                }
            }
        }
        return existe;
    }

    public static boolean esFormatoValido(com.jspsmart.upload.File archivo) {

        boolean permitido = false;
        if (archivo != null && archivo.getFileExt() != null) {

            if (archivo.getFieldName().equals("amortizacion")) {
                for (int i = 0; i < ClientesConstants.FORMATOS_PERMITIDOS_AMORTIZACION.length; i++) {
                    if (ClientesConstants.FORMATOS_PERMITIDOS_AMORTIZACION[i].equalsIgnoreCase(archivo.getFileExt())) {
                        permitido = true;
                    }
                }
            }
        }
        return permitido;
    }

    public static int getIndice(ArchivoAsociadoVO[] archivos, int tipoArchivo) {
        int indice = -1;
        if (archivos != null && archivos.length > 0) {
            myLogger.info("Archivos : " + archivos.length);
            for (int i = 0; i < archivos.length; i++) {
                myLogger.info("Archivo : " + i + " Nombre : " + archivos[i].nombre + " Tipo : " + archivos[i].tipo);
                if (archivos[i].tipo == tipoArchivo) {
                    indice = i;
                }
            }
        }
        return indice;
    }

    public static String getTexto(ArchivoAsociadoVO[] archivos, int tipoArchivo) {
        String texto = "Sin archivo";
        boolean existe = false;
        if (archivos != null && archivos.length > 0) {
            for (int i = 0; i < archivos.length; i++) {
                if (archivos[i].tipo == tipoArchivo) {
                    existe = true;
                }
            }
        }
        if (existe) {
            texto = "<a href=\"#\" onClick=\"muestraArchivo(" + tipoArchivo + ")\">Ver archivo</a>";
        }
        return texto;
    }

    public static String getRutaArchivo(int idCliente, int idSolicitud) {
        String ruta = null;
        ruta = ClientesConstants.RUTA_BASE_ARCHIVOS + idCliente + "\\" + idSolicitud + "\\";
        return ruta;
    }

    public static String getNombreArchivo(ClienteVO cliente, int idSolicitud, int tipoArchivo) {

        String nombreArchivo = null;
        SolicitudVO solicitud = cliente.solicitudes[idSolicitud - 1];
        ArchivoAsociadoVO[] archivos = solicitud.archivosAsociados;
        if (archivos != null && archivos.length > 0) {
            for (int i = 0; i < archivos.length; i++) {
                if (archivos[i].tipo == tipoArchivo) {
                    nombreArchivo = archivos[i].nombre;
                }
            }
        }
        return nombreArchivo;
    }

    public ArrayList<PagoVO> getPagosManuales(HttpServletRequest request) throws Exception{

        ArrayList<PagoVO> arrPagos = new ArrayList<PagoVO>();
        String[] idPago = null;
        BitacoraUtil bitacora = new BitacoraUtil(HTMLHelper.getParameterInt(request, "idGrupo"), request.getRemoteUser(), "CommandIngresoPagosManaules");
        try {
            idPago = request.getParameterValues("idCheckBox");
            if (idPago != null) {
                for (int i = 0; i < idPago.length; i++) {
                    arrPagos.add(new PagoVO(HTMLHelper.getParameterString(request, "referencia"+idPago[i]), HTMLHelper.getParameterDouble(request, "monto"+idPago[i]), HTMLHelper.getParameterInt(request, "banco"+idPago[i]), HTMLHelper.getParameterSqlDate(request, "fechaPago"+idPago[i]), HTMLHelper.getParameterInt(request, "numRegistro"+idPago[i]), HTMLHelper.getParameterInt(request, "cuenta"+idPago[i])));
                    bitacora.registraEventoString("referencia="+request.getParameter("referencia"+idPago[i])+", monto="+HTMLHelper.getParameterString(request, "monto"+idPago[i])+", banco="+HTMLHelper.getParameterString(request, "banco"+idPago[i])+" fecha="+HTMLHelper.getParameterString(request, "fechaPago"+idPago[i])+" numRegistro="+HTMLHelper.getParameterInt(request, "numRegistro"+idPago[i])+" cuenta="+HTMLHelper.getParameterInt(request, "cuenta"+idPago[i]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrPagos;
    }
    
    public ArrayList<PagoVO> getEliminaPagos(HttpServletRequest request) throws Exception {
        
        ArrayList<PagoVO> arrPagos = new ArrayList<PagoVO>();
        String[] idPago = null;
        BitacoraUtil bitacora = new BitacoraUtil(HTMLHelper.getParameterInt(request, "idGrupo"), request.getRemoteUser(), "CommandEliminaPagos");
        try {
            idPago = request.getParameterValues("idCheckBox");
            if (idPago != null) {
                for (int i = 0; i < idPago.length; i++) {
                    arrPagos.add(new PagoVO(HTMLHelper.getParameterString(request, "referencia"), HTMLHelper.getParameterDouble(request, "monto" + idPago[i]), HTMLHelper.getParameterSqlDate(request, "fechaPago" + idPago[i]), HTMLHelper.getParameterInt(request, "enviado" + idPago[i]), HTMLHelper.getParameterInt(request, "banco" + idPago[i]), HTMLHelper.getParameterInt(request, "cuenta" + idPago[i])));
                    bitacora.registraEventoString("referencia=" + request.getParameter("referencia") + ", monto=" + HTMLHelper.getParameterString(request, "monto" + idPago[i]) + ", banco=" + HTMLHelper.getParameterString(request, "banco" + idPago[i]) + " fecha=" + HTMLHelper.getParameterString(request, "fechaPago" + idPago[i]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return arrPagos;
    }
    
    public void transfierePagosIncidencias(String referencia){
        PagoReferenciadoDAO pagoRefDao = new PagoReferenciadoDAO();
        PagoDAO pagoDao = new PagoDAO();
        ArrayList<PagoVO> pagos = new ArrayList<PagoVO>();
        ArrayList<PagoVO> incidencias = new ArrayList<PagoVO>();
        myLogger.info("Variables inicializadas");
        try {
            pagoRefDao.eliminaIncidenciaCiclo(referencia);
            myLogger.info("Incidencias eliminadas");
            myLogger.info("Obteniendo pagos para transferir a incidencias");
            pagos = pagoRefDao.getPagosReferencia(referencia);
            if(pagos.size()>0){
                for (PagoVO pago : pagos){
                    PagoVO pagoRef = new PagoVO();
                    pagoRef.setFechaPago(pago.getFechaPago());
                    pagoRef.setTipo("7");
                    pagoRef.setObservaciones("Eliminación de ciclo");
                    pagoRef.setReferencia(pago.getReferencia());
                    pagoRef.setNuevareferencia(pago.getReferencia());
                    pagoRef.setBancoReferencia(pago.getBancoReferencia());
                    pagoRef.setReprocesar(1);
                    pagoRef.setUsuarioReproceso("sistema");
                    pagoRef.setComentarios("Pago proveniente de eliminación de ciclo");
                    pagoRef.setMonto(pago.getMonto());
                    pagoRef.setNumCuenta(pago.getNumCuenta());
                    incidencias.add(pagoRef);
                    myLogger.info("Se agrega incidencia "+pago);
                }
                pagoRefDao.insertaIncidenciasCiclo(incidencias);
                myLogger.info("Incidencias insertadas");
                pagoDao.deletePagoCiclo(referencia);
                pagoDao.deletePagoCicloODS(referencia);
                myLogger.info("Pagos de cartera eliminados");
            }
        } catch(Exception e){
            myLogger.error("Exception", e);
            e.printStackTrace();
        }
    }
    
    public void procesaPagos(ArrayList<PaynetVO> arrPagos, int numCuenta) throws SQLException{
        
        boolean error = false;
        CifraControlVO cifrasControl = new CifraControlVO();
        PagoVO pagoVO = null;
        SaldoIBSVO saldoCredVO = new SaldoIBSVO();
        ReferenciaGeneralVO refGeneralVO = null;
        PagoVO pagoCartera = new PagoVO();
        PagoGrupalVO pagoGVO = null;
        PagoExcedenteVO pagoExcedente = new PagoExcedenteVO();
        CreditoCartVO creditoVO = new CreditoCartVO();
        java.util.Date fecha = new java.util.Date();
        Connection con = null;
        Connection conCart = null;
        CifraControlDAO cifraControlDAO = new CifraControlDAO();
        PagoReferenciadoDAO pagoReferenciadoDAO = new PagoReferenciadoDAO();
        PagoDAO pagoDAO = new PagoDAO();
        PaynetDAO paynetDAO = new PaynetDAO();
        ReferenciaGeneralDAO refGeneralDAO = new ReferenciaGeneralDAO();
        SaldoIBSDAO saldosDAO = new SaldoIBSDAO();
        CarteraTCIDAO tciDAO = new CarteraTCIDAO();
        IntegranteCicloDAO integrantesDAO = new IntegranteCicloDAO();
        PagosExcedentesDAO pagosExcedenteDAO = new PagosExcedentesDAO();
        CreditoCartDAO creditoDAO = new CreditoCartDAO();
        PagoManualDAO pagoManualDAO = new PagoManualDAO();
        int idPolizaContable = 0, numPago = 0;
        int caso1 = 0, caso2 = 0, caso3 = 0, caso4 = 0, caso5 = 0, caso6 = 0, caso7 = 0, caso8 = 0, caso9 = 0, caso10 = 0, caso11 = 0, caso12 = 0, caso13 = 0, caso14 = 0;
        ArrayList<Double> arrExcedentes = new ArrayList<Double>();
        ArrayList<PagoVO> arrIncidencias = new ArrayList<PagoVO>();
        ArrayList<PagoVO> arrPagosProcesados = new ArrayList<PagoVO>();
        ArrayList<PagoVO> arrPagosInsuficiente = new ArrayList<PagoVO>();
        IntegranteCicloVO[] integrantes = null;
        double totalPagos = 0, totPagoProcesado = 0, totPagoNOProcesado = 0, totPagoNormales = 0, totPagoExcedente = 0, totalVigente = 0;
        double noProcesadosTCI = 0, procesadosTCI = 0;
        GrupoUtil grupoUtil = new GrupoUtil();
        TransaccionesHelper transHelper = new TransaccionesHelper();
        try {
            con = ConnectionManager.getMySQLConnection();
            con.setAutoCommit(false);
            conCart = ConnectionManager.getCWConnection();
            conCart.setAutoCommit(false);
            cifrasControl.setFechaMov(Convertidor.toSqlDate(fecha));
            cifrasControl.setBancoReferencia(ClientesConstants.ID_BANCO_PAYNET);
            cifrasControl.setNumCuenta(0);
            idPolizaContable = cifraControlDAO.addCifraControl(cifrasControl, con);
            cifrasControl.setIdCifraControl(idPolizaContable);
            for (PaynetVO pagoPaynet : arrPagos) {
                numPago = 0;
                pagoVO = new PagoVO(pagoPaynet.getReferenciaPay().substring(6, pagoPaynet.getReferenciaPay().length()-1), pagoPaynet.getMonto(), pagoPaynet.getFechaAutPagPay(), 0, ClientesConstants.ID_BANCO_PAYNET, "T", idPolizaContable, numCuenta);
                if(pagoVO.getReferencia().length() == 13){
                    if(existeReferencia(pagoVO.getReferencia()) != 0){
                        totalVigente = getSaldoInsolutoAlCompromiso(pagoVO.getReferencia());
                        if (!(totalVigente == -1.0)) {
                            refGeneralVO = refGeneralDAO.getReferenciaGeneral(pagoVO.getReferencia());
                            saldoCredVO = saldosDAO.getSaldo(refGeneralVO.numcliente, refGeneralVO.numSolicitud, refGeneralVO.referencia);
                            if (saldoCredVO.getEstatus() != ClientesConstants.SITUACION_CREDITO_SALDOST24_LIQUIDADO && saldoCredVO.getEstatus() != ClientesConstants.SITUACION_CREDITO_SALDOST24_CARTERACEDIDA) {
                                if (!(saldoCredVO.getEstatus() == ClientesConstants.SITUACION_CREDITO_SALDOST24_CANCELADO)) {
                                    if(pagoDAO.getPagoVO(pagoVO, con) == 0){
                                        pagoGVO = grupoUtil.esPagoGrupalVO(pagoVO.getReferencia());
                                        integrantes = integrantesDAO.getIntegrantes(pagoGVO.numGrupo, pagoGVO.numCiclo);
                                        if (integrantes != null) {
                                            myLogger.debug("Va a Matriz de Pagos");
                                            if (grupoUtil.matrizPagos(integrantes, pagoVO, "sistema", con) == 0)//LA MATRIZ DE PAGOS ESTA LIGADA A 16 SEMANAS
                                                myLogger.debug("Matriz de Pagos Lista para :" + pagoVO.getReferencia());
                                            else{
                                                myLogger.debug("Error en Matriz de Pagos para :" + pagoVO.getReferencia());
                                                /*SE DESHABILITA ESTA OPCION PARA PODER SEGUIR CON LOS PAGOS SIN DAR EL ROLLBACK
                                                error = true;
                                                break;*/
                                            }
                                        }
                                        //APLICA PAGO A CARTERA
                                        if (pagoVO.getMonto() >= totalVigente) {
                                            if (pagoVO.getMonto() > totalVigente) {
                                            //Pago de cartera con Incidencia de excedente
                                                pagoExcedente.bancoReferencia = pagoVO.getBancoReferencia();
                                                pagoExcedente.idContabilidad = pagoVO.getIdContable();
                                                pagoExcedente.fechaMovimiento = pagoVO.getFechaPago();
                                                pagoExcedente.referencia = pagoVO.getReferencia();
                                                pagoExcedente.montoExcedente = pagoVO.getMonto() - totalVigente;
                                                pagoExcedente.numCliente = refGeneralVO.numcliente;
                                                pagoExcedente.numCuentaContable = "";
                                                if(!pagosExcedenteDAO.addPagoExcedente(pagoExcedente, con)){
                                                    error = true;
                                                    break;
                                                }
                                                arrExcedentes.add(pagoExcedente.montoExcedente);
                                                caso8++;
                                                pagoVO.setStatus(8);
                                                totPagoExcedente += pagoVO.getMonto();
                                            } else {
                                            //Pago de cartera menor al vigente
                                                caso7++;
                                                pagoVO.setStatus(7);
                                            }
                                        } else {
                                            //No cubre con lo minimo a pagar, causa incidencia
                                            arrPagosInsuficiente.add(pagoVO);
                                            caso6++;
                                            pagoVO.setStatus(6);
                                        }
                                        //APLICA PAGO A CARTERA
                                        pagoVO.setSucursal(pagoVO.getReferencia().substring(0, 3));
                                        pagoVO.setFechaHora(new Timestamp(System.currentTimeMillis()));
                                        pagoPaynet.setEstatus(ClientesConstants.ID_ESTATUS_PAYNET_ENVIO_PAGOS);
                                        // Se verifica que el pago no exista en la base de datos
                                        creditoVO = creditoDAO.getCreditoReferencia(pagoVO.getReferencia(), conCart);
                                        if(creditoVO.getMontoCuenta() >= creditoVO.getMontoCuentaCongelada()){
                                            pagoVO.setDestino(1);
                                            pagoVO.setEnviado(0);
                                        } else {
                                            creditoVO.setMontoCuenta(pagoVO.getMonto() + creditoVO.getMontoCuenta());
                                            if(!creditoDAO.updatePagoCreditoCierre(creditoVO, conCart)){
                                                error = true;
                                                break;
                                            }
                                            if(!transHelper.registraPago(creditoVO, pagoVO, conCart)){
                                                error = true;
                                                break;
                                            }
                                            pagoVO.setDestino(3);
                                            pagoVO.setEnviado(1);
                                        }
                                        numPago = pagoDAO.insertPagoCartera(pagoVO, con);
                                        pagoPaynet.setNumPago(numPago);
                                        //if(!pagoDAO.insertPagoCartera(pagoVO, con)){
                                        if(numPago == 0){
                                            error = true;
                                            break;
                                        }
                                        if(!pagoManualDAO.insertPago(pagoVO, conCart)){
                                            error = true;
                                            break;
                                        }
                                        arrPagosProcesados.add(pagoVO);
                                        //pagoVO.setStatus(6);
                                        totPagoProcesado += pagoVO.getMonto();
                                    } else {
                                    //Causa incidencia, Pago Repetido
                                        numPago = pagoReferenciadoDAO.addIncidencia(pagoVO, 6, "Pago Repetido", con);
                                        pagoPaynet.setEstatus(ClientesConstants.ID_ESTATUS_PAYNET_INCIDENCIA);
                                        pagoPaynet.setNumPago(numPago);
                                        if(numPago == 0){
                                            error = true;
                                            break;
                                        }
                                        arrIncidencias.add(pagoVO);
                                        caso14++;
                                        pagoVO.setStatus(14);
                                        totPagoNOProcesado += pagoVO.getMonto();
                                    }
                                } else {//NO ENTRA A ESTA VALIDACION
                                //Causa incidencia, Crédito Cancelado
                                    numPago = pagoReferenciadoDAO.addIncidencia(pagoVO, 5, "Credito Cancelado", con);
                                    pagoPaynet.setEstatus(ClientesConstants.ID_ESTATUS_PAYNET_INCIDENCIA);
                                    pagoPaynet.setNumPago(numPago);
                                    if(numPago == 0){
                                        error = true;
                                        break;
                                    }
                                    arrIncidencias.add(pagoVO);
                                    caso5++;
                                    pagoVO.setStatus(5);
                                    totPagoNOProcesado += pagoVO.getMonto();
                                }
                            } else {
                                myLogger.debug("Entra para ver si se va a TCI");
                                //VERIFICA EL ESTATSU CARTERA CEDIDA (7)
                                if(tciDAO.verificaEstatusCredito(pagoVO.getReferencia())){
                                    pagoCartera = new PagoVO(pagoVO.getReferencia(), pagoVO.getMonto(), pagoVO.getFechaPago(), new Timestamp(System.currentTimeMillis()), 0, 6, pagoVO.getBancoReferencia(), pagoVO.getReferencia().substring(0, 3), numCuenta);
                                    //VERIFICA QUE NO ESTE LIQUEIDADO
                                    if(tciDAO.verificaEstatusCedido(pagoVO.getReferencia())){
                                        tciDAO.insertIncidenciaTCI(pagoCartera, 4, "Credito Liquidado");
                                        noProcesadosTCI++;
                                    } else {
                                    //VERIFICA QUE EL PAGO NO ESTE DUPLICADO
                                        if(tciDAO.verificaPagoDuplicado(pagoVO.getReferencia(), pagoVO.getMonto(), pagoVO.getFechaPago(), pagoVO.getBancoReferencia())){
                                            tciDAO.insertIncidenciaTCI(pagoCartera, 6, "Pago Repetido");
                                            noProcesadosTCI++;
                                        } else {
                                            tciDAO.insertPagoTCI(pagoCartera);
                                            procesadosTCI++;
                                        }
                                    }
                                } else {
                                //Causa incidencia, Crédito Liquidado
                                    numPago = pagoReferenciadoDAO.addIncidencia(pagoVO, 4, "Credito Liquidado", con);
                                    pagoPaynet.setEstatus(ClientesConstants.ID_ESTATUS_PAYNET_INCIDENCIA);
                                    pagoPaynet.setNumPago(numPago);
                                    if(numPago == 0){
                                        error = true;
                                        break;
                                    }
                                    arrIncidencias.add(pagoVO);
                                    caso4++;
                                    pagoVO.setStatus(4);
                                    totPagoNOProcesado += pagoVO.getMonto();
                                }
                            }
                        } else {
                            //Causa incidencia, Referencia encontrada, Saldo no encontrado
                            numPago = pagoReferenciadoDAO.addIncidencia(pagoVO, 3, "Saldo No Encontrado", con);
                            pagoPaynet.setEstatus(ClientesConstants.ID_ESTATUS_PAYNET_INCIDENCIA);
                            pagoPaynet.setNumPago(numPago);
                            if(numPago == 0){
                                error = true;
                                break;
                            }
                            arrIncidencias.add(pagoVO);
                            caso3++;
                            pagoVO.setStatus(3);
                            totPagoNOProcesado += pagoVO.getMonto();
                        }
                    } else {
                        //Causa incidencia, Referencia No Encontrada
                        numPago = pagoReferenciadoDAO.addIncidencia(pagoVO, 2, "Referencia No Encontrada", con);
                        pagoPaynet.setEstatus(ClientesConstants.ID_ESTATUS_PAYNET_INCIDENCIA);
                        pagoPaynet.setNumPago(numPago);
                        if(numPago == 0){
                            error = true;
                            break;
                        }
                        arrIncidencias.add(pagoVO);
                        caso2++;
                        pagoVO.setStatus(2);
                        totPagoNOProcesado += pagoVO.getMonto();
                    }
                } else {
                    //Causa Incidencia, Referencia No Valida
                    numPago = pagoReferenciadoDAO.addIncidencia(pagoVO, 1, "Referencia No Válida", con);
                    pagoPaynet.setEstatus(ClientesConstants.ID_ESTATUS_PAYNET_INCIDENCIA);
                    pagoPaynet.setNumPago(numPago);
                    if(numPago == 0){
                        error = true;
                        break;
                    }
                    arrIncidencias.add(pagoVO);
                    caso1++;
                    pagoVO.setStatus(1);
                    totPagoNOProcesado += pagoVO.getMonto();
                }
                pagoVO.setSucursal(pagoVO.getReferencia().substring(0, 3));
                if(!error && !pagoDAO.insertPago(pagoVO, con)){
                    error = true;
                    break;
                } else{
                    if(!error && !paynetDAO.updatePagoPaynet(pagoPaynet, con)){
                        error = true;
                        break;
                    } else
                        totalPagos += pagoVO.getMonto();
                }
                myLogger.debug("Pagos: " + pagoVO.toString());
            }
            // NOTA: En los pagos procesados la aplicación está descontando la prima
            totPagoNormales = totPagoProcesado;//cmdPagoRef.getMontoTotalArchivo(pagosProcesadosArray) - cmdPagoRef.getMontoTotalArchivoExcedentes(excedentesArray) - totalPrima;

            cifrasControl.registrosIntroducidos = arrPagos.size();
            cifrasControl.registrosProcesados = arrPagosProcesados.size();
            cifrasControl.registrosNoProcesados = arrIncidencias.size();
            //cifrasControl.seguros = segurosArray.size(); NO

            cifrasControl.montosIntroducidos = totalPagos;
            cifrasControl.montosProcesados = totPagoProcesado;
            cifrasControl.montosNoProcesados = totPagoNOProcesado;
            cifrasControl.montosAplicadosaCredito = totPagoNormales;
            //cifrasControl.montosAplicadosaSeguros = totalPrima; NO
            cifrasControl.montosdeExcedentes = totPagoExcedente;//cmdPagoRef.getMontoTotalArchivoExcedentes(excedentesArray);

            myLogger.debug("REGISTROS INTRODUCIDOS: " + arrPagos.size()); // TODOS LOS PAGOS
            myLogger.debug("REGISTROS PROCESADOS: " + arrPagosProcesados.size()); // PAGOS INCLUYENDO PAGO INSUFICIENTE, PAGO EXCEDENTE Y SEGUROS
            myLogger.debug("REGISTROS NO PROCESADOS: " + arrIncidencias.size()); // PAGOS CON REF NO ENCONTRADA,SALDO NO ENCONTRADO,CREDITO CANCELADO,CREDITO LIQUIDADO
            myLogger.debug("REGISTROS PROCESADOS TCI: " + procesadosTCI);
            myLogger.debug("REGISTROS NO PROCESADOS TCI: " + noProcesadosTCI);
            myLogger.debug("MONTOS INTRODUCIDOS: " + cifrasControl.montosIntroducidos);
            myLogger.debug("MONTOS PROCESADOS: " + cifrasControl.montosProcesados);
            myLogger.debug("MONTOS NO PROCESADOS: " + cifrasControl.montosNoProcesados);
            myLogger.debug("MONTOS APLICADOS A CRÉDITO: " + cifrasControl.montosAplicadosaCredito);
            myLogger.debug("MONTOS APLICADOS A SEGUROS: " + cifrasControl.montosAplicadosaSeguros);
            myLogger.debug("MONTOS DE EXCEDENTES: " + cifrasControl.montosdeExcedentes);
            myLogger.debug("------ Otros Datos -----------");
            //myLogger.debug("REGISTROS SEGUROS: " + segurosArray.size());
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
            //  cifra control
            if(!error && !cifraControlDAO.updateCifraControl(cifrasControl, con))
                error = true;
            
            if(!error){
                con.commit();
                conCart.commit();
            }else{
                con.rollback();
                conCart.rollback();
            }
        } catch (Exception e) {
            con.rollback();
            conCart.rollback();
            myLogger.error("Error en procesaPagos", e);
        }
    }
    
    public int existeReferencia(String referencia) {
        //Metodo que verifica si existe referencia.
        int existeRef = 0;
        PagoReferenciadoDAO pagoReferenciado = new PagoReferenciadoDAO();
        try {
            if (pagoReferenciado.existeReferencia(referencia))
                existeRef = 1;
            else if (pagoReferenciado.existeReferenciaenTablaAlterna(referencia))
                existeRef = 2;
        } catch (ClientesException e) {
            myLogger.error("Error dentro existeReferencia",e);
        }
        return existeRef;
    }
    
    public double getSaldoInsolutoAlCompromiso(String numReferencia) throws ClientesException {
        
        ReferenciaGeneralVO refVo = new ReferenciaGeneralDAO().getReferenciaGeneral(numReferencia);
        double totalVigente = -1;
        double saldoVencido = -1;
        double complemento = -1;
        SaldoIBSVO saldo = new SaldoIBSDAO().getSaldo(refVo.numcliente, refVo.numSolicitud, refVo.referencia);
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
    
    public static void validaFichasCompletasATiempo(String fechaPagos) throws SQLException, ClientesDBException{
        
        ArrayList<TablaAmortVO> arrPagosTabla = new ArrayList<TablaAmortVO>();
        SaldoIBSVO saldoVO = new SaldoIBSVO();
        SaldoHistoricoVO saldoHistVO = new SaldoHistoricoVO();
        TablaAmortDAO tablaDAO = new TablaAmortDAO();
        SaldoIBSDAO saldoDAO = new SaldoIBSDAO();
        SaldoHistoricoDAO saldoHistDAO = new SaldoHistoricoDAO();
        Connection conCW = null, conC = null;
        boolean error = false;
        int numPagos = 0;
        try {
            Calendar c1 = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat(ClientesConstants.FORMATO_FECHA_EU);
            java.util.Date fechaUltimoPago = sdf.parse(fechaPagos);
            c1.setTime(fechaUltimoPago);
            c1.add(Calendar.DAY_OF_YEAR, 1);
            conCW = ConnectionManager.getCWConnection();
            conCW.setAutoCommit(false);
            conC = ConnectionManager.getMySQLConnection();
            arrPagosTabla = tablaDAO.getPagosCorteTabla(conCW, Convertidor.stringToSqlDate(fechaPagos, ClientesConstants.FORMATO_FECHA_EU));
            for (TablaAmortVO pagoTabla : arrPagosTabla) {
                myLogger.debug("Pago Tabla Amortizacion "+pagoTabla.getNumCliente()+", "+pagoTabla.getNumCredito()+", "+pagoTabla.getMulta()+", "+pagoTabla.getStatus()+", "+pagoTabla.getPagado());
                if(pagoTabla.getPagado().equals(ClientesConstants.DIVIDENDO_PAGADO) && pagoTabla.getMulta() == 0){
                    saldoVO = saldoDAO.getNumPagosATiempo(conC, pagoTabla);
                    saldoHistVO = saldoHistDAO.getNumPagosHistorico(conC, pagoTabla);
                    numPagos = saldoVO.getNumeroPagosRealizados() - saldoHistVO.getNumeroPagosRealizados();
                    myLogger.debug("Validando Cuotas "+saldoVO.getNumeroCuotasTranscurridas());
                    if(saldoVO.getNumeroCuotasTranscurridas() == 1){
                        numPagos -= 1;
                    }
                    myLogger.debug("Numero de Pagos Efectuados "+numPagos+" Estatus "+saldoVO.getEstatus()+" Fecha Generacion "+saldoVO.getFechaGeneracion());
                    if(numPagos <= 1 || (saldoVO.getEstatus() == ClientesConstants.SITUACION_CREDITO_SALDOST24_LIQUIDADO && saldoVO.getFechaGeneracion().before(fechaUltimoPago))){
                        pagoTabla.setCompletaATiempo(1);
                        if(!tablaDAO.updateFichaCompletaATiempo(conCW, pagoTabla)){
                            error = true;
                            break;
                        }
                    }
                }
            }
            if(!error){
                if(c1.get(Calendar.DAY_OF_WEEK) == 7){
                    c1.add(Calendar.DAY_OF_YEAR, 2);
                }
                new CatalogoDAO().updateParametro("FECHA_VALIDA_PAGOS", Convertidor.dateToString(c1.getTime(), ClientesConstants.FORMATO_FECHA_EU));
                conCW.commit();
            } else
                conCW.rollback();
        } catch (Exception ex) {
            conCW.rollback();
            myLogger.error("Error dentro validaFichasCompletasATiempo",ex);
        } finally{
            try {
                if(conCW != null)
                    conCW.close();
                if(conC != null)
                    conC.close();
            } catch (SQLException sqle) {
                throw new ClientesDBException(sqle.getMessage());
            }
        }
    }
     public ArrayList<PagareVO> getPagosPagare(HttpServletRequest request) throws Exception{

        ArrayList<PagareVO> pagares = new ArrayList<PagareVO>();
        String[] idPagare = null;
        BitacoraUtil bitacora = new BitacoraUtil(HTMLHelper.getParameterInt(request, "idPagare"), request.getRemoteUser(), "CommandPagoPagare");
        try {
            idPagare = request.getParameterValues("idRadio");
            
            if (idPagare != null) {
                for (int i = 0; i < idPagare.length; i++) {
                    pagares.add(new PagareVO(HTMLHelper.getParameterInt(request, "numPagare"+idPagare[i]), HTMLHelper.getParameterString(request, "nombrePagare"+idPagare[i]), HTMLHelper.getParameterString(request, "nombreFond"+idPagare[i]), HTMLHelper.getParameterSqlDate(request, "fechaFin"+idPagare[i]), HTMLHelper.getParameterDouble(request, "monto"+idPagare[i])));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pagares;
    }
}
