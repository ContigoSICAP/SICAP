package com.sicap.clientes.helpers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.TreeMap;

import com.sicap.clientes.dao.ClienteDAO;
import com.sicap.clientes.dao.DireccionDAO;
import com.sicap.clientes.dao.EmpleoDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.PagoDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.cartera.TablaAmortDAO;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FormatUtil;
import com.sicap.clientes.util.Logger;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.DireccionVO;
import com.sicap.clientes.vo.EmpleoVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.SaldoIBSVO;

public class GeneraArchivoCirculoHelper {

    public static void generaArchivo() throws Exception {

        try {
            Calendar cal = Calendar.getInstance();
            StringBuffer cargaCirculo = new StringBuffer();
            String salidaArchivo = "";
            //cal.set(Calendar.MONTH, 0);
            //cal.set(Calendar.DATE, 31);
            //System.out.println("cal.getTime() "+cal.getTime());
            String periodoReporte = Convertidor.dateToStringCirculo(cal.getTime());
            BufferedWriter out = new BufferedWriter(new FileWriter(ClientesConstants.RUTA_BASE_ARCHIVOS + "BuroCirculo\\001195_CREDIEQUIPOSCONTIGO-" + periodoReporte + ".xml"));
            System.out.println("out: "+ClientesConstants.RUTA_BASE_ARCHIVOS + "BuroCirculo\\CrediEquipos-" + periodoReporte + ".xml");
            Hashtable noMatch = new Hashtable();
            int cont = 0;
            int totalesEmpleo = 0;
            int totalesNombre = 0;
            int totalesDireccion = 0;
            int totalesCuenta = 0;
            int numGrupos = 0;
            int numClientes = 0;

            StringBuffer incidenciaSaldoActual = new StringBuffer();
            StringBuffer incidenciaMensualidad = new StringBuffer();
            StringBuffer incidenciaSaldoVencido = new StringBuffer();
            int incSaldo = 0;
            int incMensualidad = 0;
            int incVencido = 0;
            Double montoCredito = 0.00;
            Double saldoActual = 0.00;
            Double saldoInsoluto = 0.00;
            Double saldoTotales = 0.00;
            Double montoMaximo = 0.00;
//			Long vencidosTotales = new Long(0);
            Double vencidosTotales = 0.00;
            Double mensualidad = 0.00;
            Double saldoVencido = 0.00;
            String fechaDesembolso = null;
            String fechaUltPago = "";
            String frecuencia = null;
            String RFC = "";
            Integer plazo = 0;
            Integer diasMora = 0;
            int contadorMorosos = 0;
            int registrosProcesados = 0;
            int cuentasCerradas = 0;
            Date fechaInicioReferencias = Convertidor.stringToDate(ClientesConstants.FECHA_INICIO_CUENTAS_BURO);
            //Calendar cal = Calendar.getInstance();
            //String fechaReporte = String.valueOf(cal.get(cal.YEAR)) + String.valueOf(cal.get(cal.MONTH+1)) + String.valueOf(cal.get(cal.DAY_OF_MONTH));
            TreeMap catalogo = MapeoHelper.getCatalogoMapeo(1);
            SaldoIBSDAO cartera = new SaldoIBSDAO();
            SaldoIBSVO[] clientes = null;
            int migraSucur = 0;
            String cvePreven = "";
            double saldoCC = 0;
            IntegranteCicloDAO integranteDAO = new IntegranteCicloDAO();
            TablaAmortDAO tablaDAO = new TablaAmortDAO();
            PagoDAO pagoDAO = new PagoDAO();
            String fechaIncumplimiento = "";
            Double montoUltPago = 0.00;

            cargaCirculo.append("<?xml version='1.0' encoding = 'ISO-8859-1'?>\r");
            cargaCirculo.append("<Carga xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:noNamespaceSchemaLocation='/Carga.xsd'>\r");
            cargaCirculo.append("<Encabezado>\r");
            cargaCirculo.append("<ClaveOtorgante>0011950049</ClaveOtorgante>\r");
            cargaCirculo.append("<NombreOtorgante>CREDIEQUIPOS CONTIGO</NombreOtorgante>\r");
            cargaCirculo.append("<IdentificadorDeMedio>1</IdentificadorDeMedio>\r");
            cargaCirculo.append("<FechaExtraccion>" + periodoReporte + "</FechaExtraccion>\r");
            cargaCirculo.append("<NotaOtorgante></NotaOtorgante>\r");
            cargaCirculo.append("<Version>3</Version>\r");
            cargaCirculo.append("</Encabezado>\r");
            cargaCirculo.append("<Personas>\r");

            for (int paso = 2; paso < 3; paso++) {
                clientes = cartera.getVSaldosIBS(paso);

                for (int j = 0; j < clientes.length; j++) {
                    if (clientes[j].getEstatus() != 5) {
                        Date pagoMax = new Date();
                        montoCredito = clientes[j].getMontoCredito();
                        mensualidad = clientes[j].getCapitalSigAmortizacion() + clientes[j].getInteresSigAmortizacion();
                        saldoVencido = clientes[j].getTotalVencido();
                        saldoActual = clientes[j].getSaldoTotalAlDia();
                        saldoInsoluto = clientes[j].getSaldoCapital();
                        fechaDesembolso = Convertidor.dateToString(clientes[j].getFechaDesembolso());
                        pagoMax = pagoDAO.getUltimoPago(clientes[j].getReferencia());
                        Integer pagVencidos = 0;

                        String MOP = " V";
                        cvePreven = "";

                        if (pagoMax != null) {
                            fechaUltPago = Convertidor.dateToString(pagoMax);
                        } else {
                            fechaUltPago = fechaDesembolso;
                        }
                        TreeMap catalogoFrecuencia = CatalogoHelper.getCatalogo("C_FRECUENCIA_PAGO");
                        frecuencia = CatalogoHelper.getDescripcionFrecuenciaPago(clientes[j].getPeriodicidad(), catalogoFrecuencia);
                        plazo = clientes[j].getNumeroCuotas();
                        
                        if(clientes[j].getDiasMora() == 0 && clientes[j].getEstatus() == 2)
                            diasMora = 1;
                        else
                            diasMora = clientes[j].getDiasMora();
                        
                        if(clientes[j].getEstatus() == 2 || clientes[j].getEstatus() == 4 || clientes[j].getEstatus() == 6)
                            pagVencidos = clientes[j].getCuotasVencidas();

                        if (mensualidad == 0 && saldoActual > 0 && clientes[j].getEstatus() == 2) {
                            mensualidad = clientes[j].getSaldoTotalAlDia();
                        }
                        
                        if(mensualidad == 0 && clientes[j].getEstatus() == 6 || clientes[j].getEstatus() == 4){
                            mensualidad = clientes[j].getSaldoTotalAlDia();
                        }
                        
                        if (saldoActual <= 0) {
                            diasMora = 0;
                        }

                        if (saldoVencido == 0) {
                            diasMora = 0;
                        }
                        if (clientes[j].getEstatus() == 3) {
                            saldoActual = 0.00;
                            diasMora = 0;
                        }
                        /*if(clientes[j].getPeriodicidad() == ClientesConstants.PAGO_QUINCENAL)
                            frecuencia = "Q";
                        else if(clientes[j].getPeriodicidad() == ClientesConstants.PAGO_MENSUAL)
                            frecuencia = "M";
                        else if(clientes[j].getPeriodicidad() == ClientesConstants.PAGO_SEMANAL)
                            frecuencia = "S";
                        else if(clientes[j].getPeriodicidad() == ClientesConstants.PAGO_CATORCENAL)
                            frecuencia = "C";*/
                        frecuencia = "M";
                        
                        if (diasMora > 0) {
                            MOP = FormatUtil.completaCadena(String.valueOf(pagVencidos), '0', 2, "L");
                        }
                        
                        if((MOP.equals(" V") && saldoActual <= 0 && saldoVencido <= 0 && mensualidad == 0) || (clientes[j].getEstatus() == 3))
                            cvePreven = "CC";
                        if(clientes[j].getEstatus() == 7){
                            cvePreven = "CV";
                            saldoCC = cartera.getSaldoCarteraCedida(clientes[j].getCredito());
                            saldoCC = Math.round(saldoCC);
                            //CAMBIAR EL NUMERO DE ATRASOS PARA HISTORICO
                            MOP = "01";
                        }

                        ClienteDAO clienteDAO = new ClienteDAO();
                        DireccionDAO direccionDAO = new DireccionDAO();
                        EmpleoDAO empleoDAO = new EmpleoDAO();
                        ClienteVO infoCliente = new ClienteVO();
                        DireccionVO[] infoDireccion = null;
                        DireccionVO dir = null;
                        EmpleoVO empleo = null;
                        DireccionVO dirEmpleo = null;

                        if (saldoActual == 0 && !MOP.equals(" V")) {
                            incidenciaSaldoActual.append(RFC + "\n");
                            incSaldo++;
                            Logger.debug("Incidencia en RFC: " + RFC + " -- " + "SaldoActual vs MOP: " + saldoActual);
                        }

                        if (mensualidad == 0 && saldoActual > 0) {
                            incidenciaMensualidad.append(RFC + "\n");
                            incMensualidad++;
                            Logger.debug("Incidencia en RFC: " + RFC + " -- " + "SaldoActual vs Mensualidad: " + saldoActual);
                        }

                        if (saldoVencido == 0 && !MOP.equals(" V")) {
                            incidenciaSaldoVencido.append(RFC + "\n");
                            incVencido++;
                            Logger.debug("Incidencia en RFC: " + RFC + " -- " + "SaldoVencido: vs MOP" + saldoVencido);
                        }

                        if (clientes[j].getIdProducto() == ClientesConstants.GRUPAL) {
                            fechaDesembolso = Convertidor.formatDateCirculo(fechaDesembolso);
                            fechaUltPago = Convertidor.formatDateCirculo(fechaUltPago);
                            numGrupos++;
                            IntegranteCicloVO[] integrantes = integranteDAO.getIntegrantes(clientes[j].getIdClienteSICAP(), clientes[j].getIdSolicitudSICAP());
                            if (integrantes != null) {
                                for (int y = 0; y < integrantes.length; y++) {
                                    if(integrantes[y].idCliente != 68041 && integrantes[y].idCliente != 68042 && integrantes[y].idCliente != 68043 && integrantes[y].idCliente != 68044 && integrantes[y].idCliente !=  68045 
                                        && integrantes[y].idCliente != 68046 && integrantes[y].idCliente != 68047 && integrantes[y].idCliente != 68048 && integrantes[y].idCliente != 68049 && integrantes[y].idCliente !=  68050 
                                        && integrantes[y].idCliente != 68052 && integrantes[y].idCliente != 68053 && integrantes[y].idCliente != 68054 && integrantes[y].idCliente != 68055 && integrantes[y].idCliente != 68056 
                                        && integrantes[y].idCliente != 68057 && integrantes[y].idCliente != 68058 && integrantes[y].idCliente != 268261 && integrantes[y].idCliente != 268264 && integrantes[y].idCliente != 268271
                                        && integrantes[y].idCliente != 268274 && integrantes[y].idCliente != 268279 && integrantes[y].idCliente != 283088 && integrantes[y].idCliente != 283089 && integrantes[y].idCliente != 283090
                                        && integrantes[y].idCliente != 297493 && integrantes[y].idCliente != 297500 && integrantes[y].idCliente != 297504 && integrantes[y].idCliente != 342445 && integrantes[y].idCliente != 342560
                                        && integrantes[y].idCliente != 427681 && integrantes[y].idCliente != 427717 && integrantes[y].idCliente != 427719 && integrantes[y].idCliente != 482430 && integrantes[y].idCliente != 482407
                                        && integrantes[y].idCliente != 24848 && integrantes[y].idCliente != 51413 && integrantes[y].idCliente != 308124 && integrantes[y].idCliente != 516504 && integrantes[y].idCliente != 564442
                                        && integrantes[y].idCliente != 597614){
                                        infoCliente = new ClienteDAO().getCliente(integrantes[y].idCliente);
                                        if (infoCliente != null && infoCliente.fechaNacimiento != null) {
                                            migraSucur = Integer.parseInt(clientes[j].getReferencia().substring(1, 3));
                                            if(migraSucur != infoCliente.idSucursal){
                                                infoCliente.idSucursal = migraSucur;
                                            }
                                            String referencia = ClientesUtil.makeReferencia(infoCliente, integrantes[y].idSolicitud);
                                            registrosProcesados++;
                                            totalesNombre++;
                                            totalesDireccion++;
                                            totalesCuenta++;
                                            if(infoCliente.aPaterno.equals(""))
                                                infoCliente.aPaterno = "No Proporcionado";
                                            if(infoCliente.aMaterno.equals(""))
                                                infoCliente.aMaterno = "No Proporcionado";
                                            cargaCirculo.append("<Persona>\r");
                                            cargaCirculo.append("<Nombre>\r");
                                            cargaCirculo.append("<ApellidoPaterno>" + FormatUtil.deleteChar(infoCliente.aPaterno.trim().toUpperCase(), '.') + "</ApellidoPaterno>\r");
                                            cargaCirculo.append("<ApellidoMaterno>" + FormatUtil.deleteChar(infoCliente.aMaterno.trim().toUpperCase(), '.') + "</ApellidoMaterno>\r");
//                                            cargaCirculo.append("<ApellidoAdicional></ApellidoAdicional>\r");
                                            cargaCirculo.append("<Nombres>" + FormatUtil.deleteChar(infoCliente.nombre.trim().toUpperCase(), '.') + "</Nombres>\r");
                                            cargaCirculo.append("<FechaNacimiento>" + Convertidor.dateToStringCirculo(infoCliente.fechaNacimiento) + "</FechaNacimiento>\r");
                                            cargaCirculo.append("<RFC>" + infoCliente.rfc + "</RFC>\r");
//                                            cargaCirculo.append("<CURP></CURP>\r");
//                                            cargaCirculo.append("<NumeroSeguridadSocial></NumeroSeguridadSocial>\r");
                                            cargaCirculo.append("<Nacionalidad>MX</Nacionalidad>\r");
//                                            cargaCirculo.append("<Residencia></Residencia>\r");
//                                            cargaCirculo.append("<NumeroLicenciaConducir></NumeroLicenciaConducir>\r");
//                                            cargaCirculo.append("<EstadoCivil></EstadoCivil>\r");
//                                            cargaCirculo.append("<Sexo></Sexo>\r");
//                                            cargaCirculo.append("<ClaveElectorIFE></ClaveElectorIFE>\r");
//                                            cargaCirculo.append("<NumeroDependientes></NumeroDependientes>\r");
//                                            cargaCirculo.append("<FechaDefuncion></FechaDefuncion>\r");
//                                            cargaCirculo.append("<IndicadorDefuncion></IndicadorDefuncion>\r");
//                                            cargaCirculo.append("<TipoPersona></TipoPersona>\r");
                                            cargaCirculo.append("</Nombre>\r");
                                            infoDireccion = direccionDAO.getDirecciones(infoCliente.idCliente);
                                            dir = direccionDAO.getDireccion(infoDireccion[0].idCliente, infoDireccion[0].idSolicitud, infoDireccion[0].tabla, infoDireccion[0].indiceTabla);
                                            String direccion = dir.calle.trim() + " " + dir.numeroExterior.trim() + " " + dir.numeroInterior.trim();
                                            direccion = direccion.trim().toUpperCase();
                                            cargaCirculo.append("<Domicilios>\r");
                                            cargaCirculo.append("<Domicilio>\r");
                                            cargaCirculo.append("<Direccion>" + FormatUtil.deleteChar(direccion, '.') + "</Direccion>\r");
                                            cargaCirculo.append("<ColoniaPoblacion>" + FormatUtil.deleteChar(dir.colonia.trim().toUpperCase(), '.') + "</ColoniaPoblacion>\r");
                                            cargaCirculo.append("<DelegacionMunicipio>" + FormatUtil.deleteChar(dir.municipio.trim().toUpperCase(), '.') + "</DelegacionMunicipio>\r");
                                            cargaCirculo.append("<Ciudad>" + FormatUtil.deleteChar(dir.municipio.trim().toUpperCase(), '.') + "</Ciudad>\r");
                                            String numestado = String.valueOf(dir.numestado);
                                            cargaCirculo.append("<Estado>" + catalogo.get(numestado) + "</Estado>\r");
                                            cargaCirculo.append("<CP>" + dir.cp.trim() + "</CP>\r");
//                                            cargaCirculo.append("<FechaResidencia></FechaResidencia>\r");
//                                            cargaCirculo.append("<NumeroTelefono></NumeroTelefono>\r");
//                                            cargaCirculo.append("<TipoDomicilio></TipoDomicilio>\r");
//                                            cargaCirculo.append("<TipoAsentamiento></TipoAsentamiento>\r");
                                            cargaCirculo.append("</Domicilio>\r");
                                            cargaCirculo.append("</Domicilios>\r");
                                            cargaCirculo.append("<Empleos>\r");
                                            cargaCirculo.append("<Empleo>\r");
                                            empleo = empleoDAO.getEmpleo(infoCliente.idCliente, integrantes[y].idSolicitud);
                                            if(empleo != null){
                                                dir = direccionDAO.getDireccion(infoCliente.idCliente, integrantes[y].idSolicitud, "d_empleos", 1);
                                                direccion = 
                                                direccion = (dir.calle != null ? dir.calle.trim() : "") + " " + (dir.numeroExterior != null ? dir.numeroExterior.trim() : "") + " " + (dir.numeroInterior != null ? dir.numeroInterior.trim() : "");
                                                direccion = direccion.trim().toUpperCase();
                                                cargaCirculo.append("<NombreEmpresa>"+empleo.razonSocial+"</NombreEmpresa>\r");
                                                cargaCirculo.append("<Direccion>"+FormatUtil.deleteChar(direccion, '.')+"</Direccion>\r");
                                                cargaCirculo.append("<ColoniaPoblacion>"+FormatUtil.deleteChar(dir.colonia.trim().toUpperCase(), '.')+"</ColoniaPoblacion>\r");
                                                cargaCirculo.append("<DelegacionMunicipio>"+FormatUtil.deleteChar(dir.municipio.trim().toUpperCase(), '.')+"</DelegacionMunicipio>\r");
                                                cargaCirculo.append("<Ciudad>"+FormatUtil.deleteChar(dir.municipio.trim().toUpperCase(), '.')+"</Ciudad>\r");
                                                cargaCirculo.append("<Estado>"+catalogo.get(numestado)+"</Estado>\r");
                                                cargaCirculo.append("<CP>"+dir.cp.trim()+"</CP>\r");
//                                                cargaCirculo.append("<NumeroTelefono></NumeroTelefono>\r");
//                                                cargaCirculo.append("<Extension></Extension>\r");
//                                                cargaCirculo.append("<Fax></Fax>\r");
//                                                cargaCirculo.append("<Puesto></Puesto>\r");
//                                                cargaCirculo.append("<FechaContratacion></FechaContratacion>\r");
//                                                cargaCirculo.append("<ClaveMoneda></ClaveMoneda>\r");
//                                                cargaCirculo.append("<SalarioMensual></SalarioMensual>\r");
//                                                cargaCirculo.append("<FechaUltimoDiaEmpleo></FechaUltimoDiaEmpleo>\r");
//                                                cargaCirculo.append("<FechaVerificacionEmpleo></FechaVerificacionEmpleo>\r");
                                            }
                                            cargaCirculo.append("</Empleo>\r");
                                            cargaCirculo.append("</Empleos>\r");
                                            cargaCirculo.append("<Cuenta>\r");
                                            cargaCirculo.append("<ClaveActualOtorgante>0011950049</ClaveActualOtorgante>\r");
                                            cargaCirculo.append("<NombreOtorgante>CREDIEQUIPOS CONTIGO</NombreOtorgante>\r");
                                            if (clientes[j].getFechaDesembolso().after(fechaInicioReferencias)) {
                                                cargaCirculo.append("<CuentaActual>" + referencia + "</CuentaActual>\r");
                                            } else {
                                                cargaCirculo.append("<CuentaActual>" + String.valueOf(infoCliente.idCliente) + "</CuentaActual>\r");
                                            }
                                            cargaCirculo.append("<TipoResponsabilidad>O</TipoResponsabilidad>\r");
                                            cargaCirculo.append("<TipoCuenta>F</TipoCuenta>\r");
                                            cargaCirculo.append("<TipoContrato>GS</TipoContrato>\r");
                                            cargaCirculo.append("<ClaveUnidadMonetaria>MX</ClaveUnidadMonetaria>\r");
//                                            cargaCirculo.append("<ValorActivoValuacion></ValorActivoValuacion>\r");
                                            cargaCirculo.append("<NumeroPagos>" + String.valueOf(plazo) + "</NumeroPagos>\r");
                                            cargaCirculo.append("<FrecuenciaPagos>" + frecuencia + "</FrecuenciaPagos>\r");
                                            if (saldoActual < mensualidad) {
                                                mensualidad = saldoActual;
                                            }
                                            
                                            int fchU = Integer.parseInt(fechaUltPago);
                                            int fchA = Integer.parseInt(fechaDesembolso);
                                            
                                            if (fchU < fchA) 
                                                fechaUltPago = fechaDesembolso;
                                                //cuenta.getChild("FechaUltimoPago").setText("" + fechaApertura);
                                            
                                            cargaCirculo.append("<MontoPagar>" + mensualidad.intValue() + "</MontoPagar>\r");
                                            cargaCirculo.append("<FechaAperturaCuenta>" + fechaDesembolso + "</FechaAperturaCuenta>\r");
                                            cargaCirculo.append("<FechaUltimoPago>" + fechaUltPago + "</FechaUltimoPago>\r");
                                            cargaCirculo.append("<FechaUltimaCompra>" + fechaDesembolso + "</FechaUltimaCompra>\r");
                                            
                                            if (saldoActual.intValue() <= 0 && saldoVencido.intValue() <= 0 && clientes[j].getEstatus() != 7) {
                                                cargaCirculo.append("<FechaCierreCuenta>" + periodoReporte + "</FechaCierreCuenta>\r");
                                                cuentasCerradas++;
                                            }
                                            if(clientes[j].getEstatus() == 7){
                                                cargaCirculo.append("<FechaCierreCuenta>" + Convertidor.dateToStringCirculo(clientes[j].getFechaGeneracion()) + "</FechaCierreCuenta>\r");
                                                cuentasCerradas++;
                                            }
                                            cargaCirculo.append("<FechaCorte>" + periodoReporte + "</FechaCorte>\r");
//                                            cargaCirculo.append("<Garantia></Garantia>\r");
                                            montoMaximo = integranteDAO.getMontoMaximoAutorizadoCC(infoCliente.idCliente);
                                            cargaCirculo.append("<CreditoMaximo>"+montoMaximo.intValue()+"</CreditoMaximo>\r");
                                            saldoTotales += saldoActual;
                                            cargaCirculo.append("<SaldoActual>" + Math.round(saldoActual) + "</SaldoActual>\r");
                                            cargaCirculo.append("<LimiteCredito>0</LimiteCredito>\r");
                                            String sdoVencido = "0";
                                            
                                            if (!MOP.equals(" V")) {
                                                if (saldoVencido > saldoActual) {
                                                    saldoVencido = saldoActual;
                                                }
                                                vencidosTotales += saldoVencido;
                                                sdoVencido = String.valueOf(Math.round(saldoVencido));
                                                contadorMorosos++;
                                            }
                                            if(clientes[j].getEstatus() == 7){
                                                sdoVencido = String.valueOf(saldoCC);
                                                sdoVencido = sdoVencido.replace(".0", "");
                                            }
                                            cargaCirculo.append("<SaldoVencido>" + sdoVencido + "</SaldoVencido>\r");
                                            cargaCirculo.append("<NumeroPagosVencidos>" + pagVencidos + "</NumeroPagosVencidos>\r");
                                            cargaCirculo.append("<PagoActual>" + MOP + "</PagoActual>\r");
//                                            cargaCirculo.append("<HistoricoPagos></HistoricoPagos>\r");
//                                            cargaCirculo.append("<ClavePrevencion></ClavePrevencion>\r");
//                                            cargaCirculo.append("<TotalPagosReportados></TotalPagosReportados>\r");
//                                            cargaCirculo.append("<ClaveAnteriorOtorgante></ClaveAnteriorOtorgante>\r");
//                                            cargaCirculo.append("<NombreCuentaAnterior></NombreCuentaAnterior>\r");
//                                            cargaCirculo.append("<NumeroCuentaAnterior></NumeroCuentaAnterior>\r");
                                            fechaIncumplimiento = "19010101";
                                            if(clientes[j].getEstatus() == 2 || clientes[j].getEstatus() == 4 || clientes[j].getEstatus() == 6){
                                                fechaIncumplimiento = Convertidor.dateToString(tablaDAO.getFechaPrimerIncumplimiento(clientes[j].getIdClienteSICAP(), clientes[j].getCredito()));
                                                fechaIncumplimiento = Convertidor.formatDateCirculo(fechaIncumplimiento);
                                            }
                                            cargaCirculo.append("<FechaPrimerIncumplimiento>"+fechaIncumplimiento+"</FechaPrimerIncumplimiento>\r");
                                            cargaCirculo.append("<SaldoInsoluto>"+Math.round(saldoInsoluto)+"</SaldoInsoluto>\r");
                                            montoUltPago = pagoDAO.getMontoUltimoPago(clientes[j].getReferencia());
                                            cargaCirculo.append("<MontoUltimoPago>"+montoUltPago.intValue()+"</MontoUltimoPago>\r");
                                            cargaCirculo.append("<PlazoMeses>"+Math.round(plazo*30.4)+"</PlazoMeses>\r");
                                            cargaCirculo.append("<MontoCreditoOriginacion>"+montoCredito.intValue()+"</MontoCreditoOriginacion>\r");
                                            if(!cvePreven.equals(""))
                                                cargaCirculo.append("<ClavePrevencion>" + cvePreven + "</ClavePrevencion>\r");
                                            cargaCirculo.append("</Cuenta>\r");
                                            cargaCirculo.append("</Persona>\r");

                                        } else {
                                            noMatch.put(cont, RFC);
                                            cont = cont + 1;
                                        }
                                    }
                                }
                            }
                        } else {
                            //System.out.println("Paso 6");
                            numClientes++;
                            infoCliente = clienteDAO.getCliente(clientes[j].getIdClienteSICAP());
                            if (infoCliente != null && infoCliente.fechaNacimiento != null) {
                                registrosProcesados++;
                                //fechaDesembolso = FormatUtil.convierteFecha(fechaDesembolso,0);
                                fechaDesembolso = Convertidor.formatDateCirculo(fechaDesembolso);
                                //fechaUltPago = FormatUtil.convierteFecha(fechaUltPago,0);
                                fechaUltPago = Convertidor.formatDateCirculo(fechaUltPago);
                                totalesNombre++;
                                totalesDireccion++;
                                totalesCuenta++;
                                cargaCirculo.append("<Persona>\r");
                                cargaCirculo.append("<Nombre>\r");
                                cargaCirculo.append("<ApellidoPaterno>" + FormatUtil.deleteChar(infoCliente.aPaterno.trim().toUpperCase(), '.') + "</ApellidoPaterno>\r");
                                cargaCirculo.append("<ApellidoMaterno>" + FormatUtil.deleteChar(infoCliente.aMaterno.trim().toUpperCase(), '.') + "</ApellidoMaterno>\r");
                                cargaCirculo.append("<Nombres>" + FormatUtil.deleteChar(infoCliente.nombre.trim().toUpperCase(), '.') + "</Nombres>\r");
                                cargaCirculo.append("<FechaNacimiento>" + Convertidor.dateToStringCirculo(infoCliente.fechaNacimiento) + "</FechaNacimiento>\r");
                                cargaCirculo.append("<RFC>" + infoCliente.rfc + "</RFC>\r");
                                cargaCirculo.append("</Nombre>\r");
                                infoDireccion = direccionDAO.getDirecciones(infoCliente.idCliente);
                                dir = direccionDAO.getDireccion(infoDireccion[0].idCliente, infoDireccion[0].idSolicitud, infoDireccion[0].tabla, infoDireccion[0].indiceTabla);
                                String direccion = dir.calle.trim() + " " + dir.numeroExterior.trim() + " " + dir.numeroInterior.trim();
                                direccion = direccion.trim().toUpperCase();
                                cargaCirculo.append("<Domicilios>\r");
                                cargaCirculo.append("<Domicilio>\r");
                                cargaCirculo.append("<Direccion>" + FormatUtil.deleteChar(direccion, '.') + "</Direccion>\r");
                                cargaCirculo.append("<ColoniaPoblacion>" + FormatUtil.deleteChar(dir.colonia.trim().toUpperCase(), '.') + "</ColoniaPoblacion>\r");
                                cargaCirculo.append("<DelegacionMunicipio>" + FormatUtil.deleteChar(dir.municipio.trim().toUpperCase(), '.') + "</DelegacionMunicipio>\r");
                                String numestado = String.valueOf(dir.numestado);
                                cargaCirculo.append("<Estado>" + catalogo.get(numestado) + "</Estado>\r");
                                cargaCirculo.append("<CP>" + dir.cp.trim() + "</CP>\r");
                                cargaCirculo.append("</Domicilio>\r");
                                cargaCirculo.append("</Domicilios>\r");
                                cargaCirculo.append("<Empleos>\r");
                                cargaCirculo.append("<Empleo>\r");
                                cargaCirculo.append("</Empleo>\r");
                                cargaCirculo.append("</Empleos>\r");
                                cargaCirculo.append("<Cuenta>\r");
                                cargaCirculo.append("<ClaveActualOtorgante>0006860049</ClaveActualOtorgante>\r");
                                cargaCirculo.append("<NombreOtorgante>CREDIEQUIPOS CONTIGO</NombreOtorgante>\r");
                                if (clientes[j].getFechaDesembolso().after(fechaInicioReferencias)) {
                                    cargaCirculo.append("<CuentaActual>" + clientes[j].getReferencia() + "</CuentaActual>\r");
                                } else {
                                    cargaCirculo.append("<CuentaActual>" + String.valueOf(infoCliente.idCliente) + "</CuentaActual>\r");
                                }
                                cargaCirculo.append("<TipoResponsabilidad>I</TipoResponsabilidad>\r");
                                cargaCirculo.append("<TipoCuenta>F</TipoCuenta>\r");
                                //cargaCirculo.append("<TipoContrato>PP</TipoContrato>\r");
                                cargaCirculo.append("<TipoContrato>GS</TipoContrato>\r");
                                cargaCirculo.append("<ClaveUnidadMonetaria>MX</ClaveUnidadMonetaria>\r");
                                cargaCirculo.append("<NumeroPagos>" + String.valueOf(plazo) + "</NumeroPagos>\r");
                                cargaCirculo.append("<FrecuenciaPagos>" + frecuencia + "</FrecuenciaPagos>\r");
                                if (saldoActual < mensualidad) {
                                    mensualidad = saldoActual;
                                }
                                
                                int fchU = Integer.parseInt(fechaUltPago);
                                        int fchA = Integer.parseInt(fechaDesembolso);
                                        
                                if (fchU < fchA) 
                                             fechaUltPago = fechaDesembolso;
                                         
                                cargaCirculo.append("<MontoPagar>" + mensualidad.intValue() + "</MontoPagar>\r");
                                cargaCirculo.append("<FechaAperturaCuenta>" + fechaDesembolso + "</FechaAperturaCuenta>\r");
                                cargaCirculo.append("<FechaUltimoPago>" + fechaUltPago + "</FechaUltimoPago>\r");
                                cargaCirculo.append("<FechaUltimaCompra>" + fechaDesembolso + "</FechaUltimaCompra>\r");
                                if (saldoActual.intValue() <= 0 && saldoVencido < 200) {
                                    cargaCirculo.append("<FechaCierreCuenta>" + periodoReporte + "</FechaCierreCuenta>\r");
                                    cuentasCerradas++;
                                }
                                cargaCirculo.append("<FechaCorte>" + periodoReporte + "</FechaCorte>\r");
                                cargaCirculo.append("<CreditoMaximo>" + montoCredito.intValue() + "</CreditoMaximo>\r");
                                saldoTotales += saldoActual;
                                cargaCirculo.append("<SaldoActual>" + saldoActual.intValue() + "</SaldoActual>\r");
                                //cargaCirculo.append("<LimiteCredito>"+montoCredito.intValue()+"</LimiteCredito>\r");
                                cargaCirculo.append("<LimiteCredito>0</LimiteCredito>\r");
                                String sdoVencido = "0";
                                if (!MOP.equals(" V")) {
                                    if (saldoVencido > saldoActual) {
                                        saldoVencido = saldoActual;
                                    }
                                    vencidosTotales += saldoVencido;
                                    sdoVencido = String.valueOf(Math.round(saldoVencido));
                                    contadorMorosos++;
                                }
                                if(clientes[j].getEstatus() == 7)
                                    sdoVencido = String.valueOf(0.0);

                                cargaCirculo.append("<SaldoVencido>" + sdoVencido + "</SaldoVencido>\r");
                                cargaCirculo.append("<NumeroPagosVencidos>" + pagVencidos + "</NumeroPagosVencidos>\r");
                                cargaCirculo.append("<PagoActual>" + MOP + "</PagoActual>\r");
                                cargaCirculo.append("</Cuenta>\r");
                                cargaCirculo.append("</Persona>\r");

                            } else {
                                noMatch.put(cont, RFC);
                                cont = cont + 1;
                            }
                        }
                    }
                    salidaArchivo = Convertidor.characterSet(cargaCirculo.toString());
                    out.write(salidaArchivo.trim());
                    cargaCirculo = new StringBuffer();
                }
            }
            cargaCirculo.append("</Personas>\r");
            cargaCirculo.append("<CifrasControl>\r");
            cargaCirculo.append("<TotalSaldosActuales>" + saldoTotales.intValue() + "</TotalSaldosActuales>\r");
            cargaCirculo.append("<TotalSaldosVencidos>" + vencidosTotales.intValue() + "</TotalSaldosVencidos>\r");
            cargaCirculo.append("<TotalElementosNombreReportados>" + totalesNombre + "</TotalElementosNombreReportados>\r");
            cargaCirculo.append("<TotalElementosDireccionReportados>" + totalesDireccion + "</TotalElementosDireccionReportados>\r");
            cargaCirculo.append("<TotalElementosEmpleoReportados>" + totalesEmpleo + "</TotalElementosEmpleoReportados>\r");
            cargaCirculo.append("<TotalElementosCuentaReportados>" + totalesCuenta + "</TotalElementosCuentaReportados>\r");
            cargaCirculo.append("<NombreOtorgante>CREDIEQUIPOS CONTIGO</NombreOtorgante>\r");
            cargaCirculo.append("<DomicilioDevolucion>Insurgentes Sur No.730 piso 17 Col. Colonia del Valle C.P. 03103</DomicilioDevolucion>\r");
            cargaCirculo.append("</CifrasControl>\r");
            cargaCirculo.append("</Carga>\r");

            salidaArchivo = Convertidor.characterSet(cargaCirculo.toString());
            out.write(salidaArchivo.trim());
            out.close();

            System.out.println("No. Clientes reportados: " + registrosProcesados);
            System.out.println("No. Grupos Procesados: " + numGrupos);

            System.out.println("No. Registros morosos reportados: " + contadorMorosos);
            System.out.println("No. Cuentas cerradas: " + cuentasCerradas);

        } catch (Exception e) {
            Logger.debug("Excepcion en Main cargaCirculo : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
