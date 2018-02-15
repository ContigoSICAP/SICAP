package com.sicap.clientes.util;

import java.io.IOException;
import java.net.Socket;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import com.sicap.clientes.exceptions.CirculoCreditoRequestException;

public class ClienteCDCSocket {

    private static final String CIRCULO_CREDITO_IP = "172.17.1.14";
    private static final int CIRCULO_CREDITO_PORT = 25000;
    private static org.apache.log4j.Logger myLogger = org.apache.log4j.Logger.getLogger(ClienteCDCSocket.class);

    public static String getReporteCDC(String XMLSolicitud) {

        ClienteCDCSocket pruebaXML = new ClienteCDCSocket();
        String xmlResponse = null;

        String xmlString = XMLSolicitud;
        // Es una cadena XML valida
        if (xmlString != null) {
            try {
                //myLogger.debug("Solicitud: " + xmlString);
                // Enviando cadena XML a Circulo de Credito
                myLogger.info("Enviando cadena XML a Círculo de Crédito: " + XMLSolicitud);
                //xmlResponse = "<?xml version=\"1.0\" encoding = \"ISO-8859-1\"?><Respuesta xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:noNamespaceSchemaLocation=\"/Respuesta.xsd\"><Personas><Persona><Encabezado><FolioConsultaOtorgante>453727</FolioConsultaOtorgante><ClaveOtorgante>001195</ClaveOtorgante><ExpedienteEncontrado>1</ExpedienteEncontrado><FolioConsulta>164088255</FolioConsulta></Encabezado><Nombre><ApellidoPaterno>RENDON</ApellidoPaterno><ApellidoMaterno>JAVIER</ApellidoMaterno><ApellidoAdicional></ApellidoAdicional><Nombres>DORA ELENA</Nombres><FechaNacimiento>1980-09-22</FechaNacimiento><RFC>REJD800922</RFC><CURP></CURP><Nacionalidad></Nacionalidad><Residencia>5</Residencia><EstadoCivil>N</EstadoCivil><Sexo>N</Sexo><ClaveElectorIFE></ClaveElectorIFE><NumeroDependientes>0</NumeroDependientes><FechaDefuncion></FechaDefuncion></Nombre><Domicilios><Domicilio><Direccion>ADN D ALVAREZ 14</Direccion><ColoniaPoblacion>La Mira</ColoniaPoblacion><DelegacionMunicipio>Acapulco de Juárez</DelegacionMunicipio><Ciudad></Ciudad><Estado>GRO</Estado><CP>39480</CP><FechaResidencia>2015-06-29</FechaResidencia><NumeroTelefono></NumeroTelefono><TipoDomicilio></TipoDomicilio><TipoAsentamiento></TipoAsentamiento><FechaRegistroDomicilio>2015-06-29</FechaRegistroDomicilio></Domicilio><Domicilio><Direccion>CERCA DE LA ESCUELA SN SN</Direccion><ColoniaPoblacion>EL SALTO</ColoniaPoblacion><DelegacionMunicipio>ACAPULCO DE JUAREZ</DelegacionMunicipio><Ciudad>ACAPULCO</Ciudad><Estado>GRO</Estado><CP>39903</CP><FechaResidencia>2015-04-07</FechaResidencia><NumeroTelefono></NumeroTelefono><TipoDomicilio></TipoDomicilio><TipoAsentamiento>0</TipoAsentamiento><FechaRegistroDomicilio>2015-04-07</FechaRegistroDomicilio></Domicilio><Domicilio><Direccion>NICOLAS BRAVO</Direccion><ColoniaPoblacion>RUIZ MASSIEU</ColoniaPoblacion><DelegacionMunicipio>ACAPULCO DE JUAREZ</DelegacionMunicipio><Ciudad>CHILPANCINGO DE LOS BRAVO</Ciudad><Estado>GRO</Estado><CP>39070</CP><FechaResidencia>2014-05-14</FechaResidencia><NumeroTelefono></NumeroTelefono><TipoDomicilio></TipoDomicilio><TipoAsentamiento>0</TipoAsentamiento><FechaRegistroDomicilio>2014-05-14</FechaRegistroDomicilio></Domicilio></Domicilios><Empleos></Empleos><Mensajes><Mensaje><TipoMensaje>2</TipoMensaje><Leyenda>1</Leyenda></Mensaje></Mensajes><Cuentas><Cuenta><FechaActualizacion>2015-06-26</FechaActualizacion><RegistroImpugnado>0</RegistroImpugnado><ClaveOtorgante></ClaveOtorgante><NombreOtorgante>MERCANCIA PARA H</NombreOtorgante><CuentaActual></CuentaActual><TipoResponsabilidad>I</TipoResponsabilidad><TipoCuenta>F</TipoCuenta><TipoCredito>CC</TipoCredito><ClaveUnidadMonetaria>MX</ClaveUnidadMonetaria><ValorActivoValuacion>1999</ValorActivoValuacion><NumeroPagos>102</NumeroPagos><FrecuenciaPagos>S</FrecuenciaPagos><MontoPagar>446</MontoPagar><FechaAperturaCuenta>2014-07-11</FechaAperturaCuenta><FechaUltimoPago>2015-04-24</FechaUltimoPago><FechaUltimaCompra>2014-07-11</FechaUltimaCompra><FechaCierreCuenta></FechaCierreCuenta><FechaReporte>2015-06-26</FechaReporte><UltimaFechaSaldoCero></UltimaFechaSaldoCero><Garantia></Garantia><CreditoMaximo>3988</CreditoMaximo><SaldoActual>2484</SaldoActual><LimiteCredito>0</LimiteCredito><SaldoVencido>407</SaldoVencido><NumeroPagosVencidos>42</NumeroPagosVencidos><PagoActual>09</PagoActual><HistoricoPagos>090806060504030201 V V02121005 V</HistoricoPagos><FechaRecienteHistoricoPagos></FechaRecienteHistoricoPagos><FechaAntiguaHistoricoPagos></FechaAntiguaHistoricoPagos><ClavePrevencion></ClavePrevencion><TotalPagosReportados>0</TotalPagosReportados><PeorAtraso>12</PeorAtraso><FechaPeorAtraso>2015-03-20</FechaPeorAtraso><SaldoVencidoPeorAtraso>501</SaldoVencidoPeorAtraso></Cuenta><Cuenta><FechaActualizacion>2015-06-29</FechaActualizacion><RegistroImpugnado>0</RegistroImpugnado><ClaveOtorgante></ClaveOtorgante><NombreOtorgante>BANCO / BC</NombreOtorgante><CuentaActual></CuentaActual><TipoResponsabilidad>I</TipoResponsabilidad><TipoCuenta>F</TipoCuenta><TipoCredito>PP</TipoCredito><ClaveUnidadMonetaria>MX</ClaveUnidadMonetaria><ValorActivoValuacion></ValorActivoValuacion><NumeroPagos>16</NumeroPagos><FrecuenciaPagos>S</FrecuenciaPagos><MontoPagar>443</MontoPagar><FechaAperturaCuenta>2015-04-10</FechaAperturaCuenta><FechaUltimoPago>2015-06-24</FechaUltimoPago><FechaUltimaCompra>2015-04-10</FechaUltimaCompra><FechaCierreCuenta></FechaCierreCuenta><FechaReporte>2015-06-25</FechaReporte><UltimaFechaSaldoCero></UltimaFechaSaldoCero><Garantia></Garantia><CreditoMaximo>6000</CreditoMaximo><SaldoActual>2658</SaldoActual><LimiteCredito></LimiteCredito><SaldoVencido>0</SaldoVencido><NumeroPagosVencidos>0</NumeroPagosVencidos><PagoActual> V</PagoActual><HistoricoPagos> V01 V</HistoricoPagos><FechaRecienteHistoricoPagos></FechaRecienteHistoricoPagos><FechaAntiguaHistoricoPagos></FechaAntiguaHistoricoPagos><ClavePrevencion></ClavePrevencion><TotalPagosReportados></TotalPagosReportados><PeorAtraso></PeorAtraso><FechaPeorAtraso></FechaPeorAtraso><SaldoVencidoPeorAtraso>0</SaldoVencidoPeorAtraso></Cuenta><Cuenta><FechaActualizacion>2015-03-31</FechaActualizacion><RegistroImpugnado>0</RegistroImpugnado><ClaveOtorgante></ClaveOtorgante><NombreOtorgante>VENTA POR CATALO</NombreOtorgante><CuentaActual></CuentaActual><TipoResponsabilidad>I</TipoResponsabilidad><TipoCuenta>R</TipoCuenta><TipoCredito>LC</TipoCredito><ClaveUnidadMonetaria>MX</ClaveUnidadMonetaria><ValorActivoValuacion>0</ValorActivoValuacion><NumeroPagos>1</NumeroPagos><FrecuenciaPagos>M</FrecuenciaPagos><MontoPagar>0</MontoPagar><FechaAperturaCuenta>2014-04-02</FechaAperturaCuenta><FechaUltimoPago>2015-03-31</FechaUltimoPago><FechaUltimaCompra>2014-05-21</FechaUltimaCompra><FechaCierreCuenta>2015-03-31</FechaCierreCuenta><FechaReporte>2015-03-31</FechaReporte><UltimaFechaSaldoCero>2015-03-31</UltimaFechaSaldoCero><Garantia></Garantia><CreditoMaximo>4000</CreditoMaximo><SaldoActual>0</SaldoActual><LimiteCredito>0</LimiteCredito><SaldoVencido>0</SaldoVencido><NumeroPagosVencidos>2</NumeroPagosVencidos><PagoActual> V</PagoActual><HistoricoPagos> V------ V--030201</HistoricoPagos><FechaRecienteHistoricoPagos></FechaRecienteHistoricoPagos><FechaAntiguaHistoricoPagos></FechaAntiguaHistoricoPagos><ClavePrevencion></ClavePrevencion><TotalPagosReportados>0</TotalPagosReportados><PeorAtraso>3</PeorAtraso><FechaPeorAtraso>2014-09-30</FechaPeorAtraso><SaldoVencidoPeorAtraso>1175</SaldoVencidoPeorAtraso></Cuenta><Cuenta><FechaActualizacion>2015-06-24</FechaActualizacion><RegistroImpugnado>0</RegistroImpugnado><ClaveOtorgante></ClaveOtorgante><NombreOtorgante>MERCANCIA PARA H</NombreOtorgante><CuentaActual></CuentaActual><TipoResponsabilidad>I</TipoResponsabilidad><TipoCuenta>L</TipoCuenta><TipoCredito>CP</TipoCredito><ClaveUnidadMonetaria>MX</ClaveUnidadMonetaria><ValorActivoValuacion>0</ValorActivoValuacion><NumeroPagos>0</NumeroPagos><FrecuenciaPagos>S</FrecuenciaPagos><MontoPagar>732</MontoPagar><FechaAperturaCuenta>2014-05-21</FechaAperturaCuenta><FechaUltimoPago>2015-04-29</FechaUltimoPago><FechaUltimaCompra>2014-05-21</FechaUltimaCompra><FechaCierreCuenta></FechaCierreCuenta><FechaReporte>2015-06-24</FechaReporte><UltimaFechaSaldoCero></UltimaFechaSaldoCero><Garantia></Garantia><CreditoMaximo>732</CreditoMaximo><SaldoActual>732</SaldoActual><LimiteCredito>0</LimiteCredito><SaldoVencido>732</SaldoVencido><NumeroPagosVencidos>43</NumeroPagosVencidos><PagoActual>08</PagoActual><HistoricoPagos>0807060504030201 V V V0111110502</HistoricoPagos><FechaRecienteHistoricoPagos></FechaRecienteHistoricoPagos><FechaAntiguaHistoricoPagos></FechaAntiguaHistoricoPagos><ClavePrevencion></ClavePrevencion><TotalPagosReportados>0</TotalPagosReportados><PeorAtraso>11</PeorAtraso><FechaPeorAtraso>2015-03-18</FechaPeorAtraso><SaldoVencidoPeorAtraso>2091</SaldoVencidoPeorAtraso></Cuenta><Cuenta><FechaActualizacion>2015-04-30</FechaActualizacion><RegistroImpugnado>0</RegistroImpugnado><ClaveOtorgante></ClaveOtorgante><NombreOtorgante>TELEFONIA LOCAL</NombreOtorgante><CuentaActual></CuentaActual><TipoResponsabilidad>I</TipoResponsabilidad><TipoCuenta>L</TipoCuenta><TipoCredito>LC</TipoCredito><ClaveUnidadMonetaria>MX</ClaveUnidadMonetaria><ValorActivoValuacion>0</ValorActivoValuacion><NumeroPagos>0</NumeroPagos><FrecuenciaPagos>M</FrecuenciaPagos><MontoPagar>0</MontoPagar><FechaAperturaCuenta>2011-02-23</FechaAperturaCuenta><FechaUltimoPago></FechaUltimoPago><FechaUltimaCompra>2013-10-01</FechaUltimaCompra><FechaCierreCuenta></FechaCierreCuenta><FechaReporte>2015-04-30</FechaReporte><UltimaFechaSaldoCero></UltimaFechaSaldoCero><Garantia></Garantia><CreditoMaximo>653</CreditoMaximo><SaldoActual>0</SaldoActual><LimiteCredito>0</LimiteCredito><SaldoVencido>0</SaldoVencido><NumeroPagosVencidos>2</NumeroPagosVencidos><PagoActual> V</PagoActual><HistoricoPagos> V-- V-- V------ V V V V V V07</HistoricoPagos><FechaRecienteHistoricoPagos></FechaRecienteHistoricoPagos><FechaAntiguaHistoricoPagos></FechaAntiguaHistoricoPagos><ClavePrevencion></ClavePrevencion><TotalPagosReportados>0</TotalPagosReportados><PeorAtraso>7</PeorAtraso><FechaPeorAtraso>2011-09-09</FechaPeorAtraso><SaldoVencidoPeorAtraso>1871</SaldoVencidoPeorAtraso></Cuenta><Cuenta><FechaActualizacion>2014-03-06</FechaActualizacion><RegistroImpugnado>0</RegistroImpugnado><ClaveOtorgante></ClaveOtorgante><NombreOtorgante>FINANCIERA / BC</NombreOtorgante><CuentaActual></CuentaActual><TipoResponsabilidad>I</TipoResponsabilidad><TipoCuenta>F</TipoCuenta><TipoCredito>PP</TipoCredito><ClaveUnidadMonetaria>MX</ClaveUnidadMonetaria><ValorActivoValuacion></ValorActivoValuacion><NumeroPagos></NumeroPagos><FrecuenciaPagos>S</FrecuenciaPagos><MontoPagar>0</MontoPagar><FechaAperturaCuenta>2010-12-28</FechaAperturaCuenta><FechaUltimoPago>2014-02-24</FechaUltimoPago><FechaUltimaCompra>2010-12-28</FechaUltimaCompra><FechaCierreCuenta>2014-02-24</FechaCierreCuenta><FechaReporte>2014-02-28</FechaReporte><UltimaFechaSaldoCero></UltimaFechaSaldoCero><Garantia></Garantia><CreditoMaximo>2928</CreditoMaximo><SaldoActual>0</SaldoActual><LimiteCredito>0</LimiteCredito><SaldoVencido>0</SaldoVencido><NumeroPagosVencidos>33</NumeroPagosVencidos><PagoActual> V</PagoActual><HistoricoPagos> V131313131313131313131313131313131313131313131313</HistoricoPagos><FechaRecienteHistoricoPagos></FechaRecienteHistoricoPagos><FechaAntiguaHistoricoPagos></FechaAntiguaHistoricoPagos><ClavePrevencion></ClavePrevencion><TotalPagosReportados></TotalPagosReportados><PeorAtraso></PeorAtraso><FechaPeorAtraso></FechaPeorAtraso><SaldoVencidoPeorAtraso>0</SaldoVencidoPeorAtraso></Cuenta></Cuentas><ConsultasEfectuadas><ConsultaEfectuada><FechaConsulta>2015-06-29</FechaConsulta><ClaveOtorgante></ClaveOtorgante><NombreOtorgante>CREDIEQUIPOS CONTIGO</NombreOtorgante><TipoCredito>F</TipoCredito><ClaveUnidadMonetaria>MX</ClaveUnidadMonetaria><ImporteCredito>4500</ImporteCredito><TipoResponsabilidad></TipoResponsabilidad></ConsultaEfectuada><ConsultaEfectuada><FechaConsulta>2014-05-14</FechaConsulta><ClaveOtorgante></ClaveOtorgante><NombreOtorgante>BANCOS</NombreOtorgante><TipoCredito>F</TipoCredito><ClaveUnidadMonetaria>MX</ClaveUnidadMonetaria><ImporteCredito>0</ImporteCredito><TipoResponsabilidad></TipoResponsabilidad></ConsultaEfectuada><ConsultaEfectuada><FechaConsulta>2015-04-07</FechaConsulta><ClaveOtorgante></ClaveOtorgante><NombreOtorgante>BANCO / BC</NombreOtorgante><TipoCredito>PL</TipoCredito><ClaveUnidadMonetaria></ClaveUnidadMonetaria><ImporteCredito>0</ImporteCredito><TipoResponsabilidad>I</TipoResponsabilidad></ConsultaEfectuada><ConsultaEfectuada><FechaConsulta>2014-12-10</FechaConsulta><ClaveOtorgante></ClaveOtorgante><NombreOtorgante>BANCO / BC</NombreOtorgante><TipoCredito>CC</TipoCredito><ClaveUnidadMonetaria></ClaveUnidadMonetaria><ImporteCredito>0</ImporteCredito><TipoResponsabilidad>I</TipoResponsabilidad></ConsultaEfectuada></ConsultasEfectuadas><DeclaracionesConsumidor></DeclaracionesConsumidor><Scores><Score><NombreScore>FICO</NombreScore><Codigo>26</Codigo><Valor>508</Valor><Razon1>D8</Razon1><Razon2>R0</Razon2><Razon3>K0</Razon3><Razon4>D2</Razon4><Error>0</Error></Score></Scores></Persona></Personas></Respuesta>";
                xmlResponse = pruebaXML.getXMLResponse(CIRCULO_CREDITO_IP, CIRCULO_CREDITO_PORT, xmlString);
            } catch (Exception e) {
                //} catch (CirculoCreditoRequestException e) {
                // TODO Auto-generated catch block
                myLogger.error("getReporteCDC", e);
            }
        } else {
            myLogger.debug("Solicitud xmlString vacia...");
        }
        if (xmlResponse != null) {
            myLogger.info("Respuesta recibida");
            myLogger.debug("Respuesta: " + xmlResponse);
        } else {
            myLogger.info("Respuesta vacia...");
        }
        return xmlResponse;

    }

    /**
     * Obtiene una cadena XML de Círculo de Crédito
     *
     * @param xmlRequest
     * @return
     * @throws CirculoCreditoRequestException
     */
    public String getXMLResponse(String circuloCreditoIP,
            int circuloCreditoPort, String xmlRequest)
            throws CirculoCreditoRequestException {

        OutputStream outputStream = null;
        DataOutputStream dataOutputStream = null;
        InputStream inputStream = null;
        Socket socketClient = null;
        ByteArrayOutputStream dataStream = null;
        String xmlStringResponse = null;
        try {
            // Conectando a Círculo de Crédito
            socketClient = new Socket(circuloCreditoIP, circuloCreditoPort);
            socketClient.setTcpNoDelay(true);
            if (!socketClient.isConnected()) {
                throw new CirculoCreditoRequestException(
                        "El socket no está conectado, verificar IP y Puerto...");
            }
            // Enviando solicitud de cadena XML a Círculo de Crédito
            outputStream = socketClient.getOutputStream();
            dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeBytes(xmlRequest);
            dataOutputStream.flush();

            // Leyendo respuesta de Círculo de Crédito
            inputStream = socketClient.getInputStream();
            dataStream = new ByteArrayOutputStream();
            byte data;
            while ((data = (byte) inputStream.read()) != -1) {
                dataStream.write(data);
            }
            // Elimina los espacios de la cadena.
            xmlStringResponse = dataStream.toString().trim();

        } catch (IOException e) {
            myLogger.error("getXMLResponse", e);
            // TODO: handle exception
        } finally {
            // Cerrando Streams
            try {
                if (dataStream != null) {
                    dataStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (socketClient != null) {
                    socketClient.close();
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                myLogger.error("getXMLResponse", e);
            }
        }

        return xmlStringResponse;
    }

}
