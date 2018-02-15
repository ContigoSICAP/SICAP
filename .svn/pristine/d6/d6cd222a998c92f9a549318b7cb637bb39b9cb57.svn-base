package com.sicap.clientes.commands;

import com.sicap.clientes.dao.ArchivoAsociadoDAO;
import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.CreditoDAO;
import com.sicap.clientes.dao.DireccionGenericaDAO;
import com.sicap.clientes.dao.EmpleoDAO;
import com.sicap.clientes.dao.EventosPagosGrupalDAO;
import com.sicap.clientes.dao.GrupoDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.ReferenciaGeneralDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.SaldoT24DAO;
import com.sicap.clientes.dao.SegurosDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.dao.SucursalDAO;
import com.sicap.clientes.dao.TablaAmortizacionDAO;
import com.sicap.clientes.dao.cartera.TablaAmortDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.IBSHelper;
import com.sicap.clientes.helpers.SeguroHelper;
import com.sicap.clientes.util.AdicionalUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SeguroConstantes;
import com.sicap.clientes.vo.ArchivoAsociadoVO;
import com.sicap.clientes.vo.CreditoVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.SaldoIBSVO;
import com.sicap.clientes.vo.SucursalVO;
import com.sicap.clientes.vo.TablaAmortizacionVO;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;

public class CommandObtenCicloAdicional implements Command {

    private static Logger myLogger = Logger.getLogger(CommandObtenCicloAdicional.class);
    
    private String siguiente;

    public CommandObtenCicloAdicional(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        HttpSession session = request.getSession();
        GrupoVO grupo = new GrupoVO();
        CicloGrupalDAO ciclodao = new CicloGrupalDAO();
        CreditoVO informacionCrediticia = null;
        /**
         * JECB 01/10/2017
         * Se agrega Vector para agregar mensajes de validaciones de adicional
         * en caso de NO cumplir las reglas de negocio y poderse desplegarse
         * en pantalla
         */
        Vector<Notification> notificaciones = new Vector<Notification>();
        /**
         * JECB 01/10/2017
         * Se crea referencia a dao de solicitud
         */
        SolicitudDAO  solicitudDao = new SolicitudDAO();
        try {
            TreeMap catComisionesGrupal = CatalogoHelper.getCatalogoComisiones(ClientesConstants.GRUPAL);
            int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
            int semanaAdicional = HTMLHelper.getParameterInt(request, "semAdicional");
            if (session.getAttribute("GRUPO") == null) {
                grupo = new GrupoDAO().getGrupo(HTMLHelper.getParameterInt(request, "idGrupo"));
                grupo.ciclos = new CicloGrupalDAO().getCiclos(grupo.idGrupo);
                grupo.ciclos[0] = new CicloGrupalDAO().getCiclo(grupo.idGrupo, idCiclo);
                if (grupo.ciclos[0].idCreditoIBS != 0) {
                    grupo.ciclos[0].saldo = new SaldoIBSDAO().getSaldos(grupo.idGrupo, idCiclo);
                    grupo.ciclos[0].setAtrasos(new TablaAmortDAO().getAtrasos(grupo.idGrupo, grupo.ciclos[0].idCreditoIBS));
                } else {
                    grupo.ciclos[0].saldo = IBSHelper.getSaldosT24ToIBS(new SaldoT24DAO().getSaldosT24ByNumClienteSolicitudProducto(grupo.idGrupo, idCiclo, ClientesConstants.GRUPAL));
                    grupo.ciclos[0].setAtrasos(0);
                }
                if (grupo.ciclos[0].saldo != null) {
                    grupo.ciclos[0].estatusT24 = grupo.ciclos[0].saldo.getEstatus();
                }
            } else {
                grupo = (GrupoVO) session.getAttribute("GRUPO");
            }
            if (idCiclo != 0) {
                grupo.ciclos[idCiclo - 1].integrantes = new IntegranteCicloDAO().getIntegrantes(grupo.idGrupo, idCiclo);
                for (int a = 0; grupo.ciclos[idCiclo - 1].integrantes != null && a < grupo.ciclos[idCiclo - 1].integrantes.length; a++) {
                    if (grupo.ciclos[idCiclo - 1].integrantes[a].calificacion == ClientesConstants.CALIFICACION_CIRCULO_REGULAR) {
                        informacionCrediticia = new CreditoDAO().getCredito(grupo.ciclos[idCiclo - 1].integrantes[a].idCliente, grupo.ciclos[idCiclo - 1].integrantes[a].idSolicitud, 2);
                        if (informacionCrediticia != null) {
                            grupo.ciclos[idCiclo - 1].integrantes[a].aceptaRegular = informacionCrediticia.aceptaRegular;
                        } else {
                            if ( grupo.ciclos[idCiclo - 1].integrantes[a].getIdSolicitud() == 1) {
                                grupo.ciclos[idCiclo - 1].integrantes[a].calificacion  = ClientesConstants.CALIFICACION_CIRCULO_SIN_CONSULTA;
                            } else {
                                grupo.ciclos[idCiclo - 1].integrantes[a].calificacion = ClientesConstants.CALIFICACION_CIRCULO_NA;
                            }
                        }
                    }
                    grupo.ciclos[idCiclo - 1].integrantes[a].montoDesembolso = ClientesUtil.calculaMontoSinComision(grupo.ciclos[idCiclo - 1].integrantes[a].monto, grupo.ciclos[idCiclo - 1].integrantes[a].comision, catComisionesGrupal);
                    if (grupo.ciclos[idCiclo - 1].idCreditoIBS == 0 && grupo.ciclos[idCiclo - 1].desembolsado != 2) {
                        grupo.ciclos[idCiclo - 1].integrantes[a].seguro = new SegurosDAO().compruebaSeguro(grupo.ciclos[idCiclo - 1].integrantes[a]);
                        grupo.ciclos[idCiclo - 1].integrantes[a].empleo = new EmpleoDAO().compruebaEmpleo(grupo.ciclos[idCiclo - 1].integrantes[a]);
                        grupo.ciclos[idCiclo - 1].integrantes[a].grupo = new IntegranteCicloDAO().getIntegranteActivo(grupo.ciclos[idCiclo - 1].integrantes[a].idCliente);
                    }
                    
                    // Calculamos el monto con seguro financiado Agosto 2017
                    IntegranteCicloVO integrante = grupo.ciclos[idCiclo - 1].integrantes[a];
                    if (integrante.montoConSeguro == 0) {
                        integrante.montoConSeguro = integrante.montoDesembolso;
                    } 
                    // Solo si tiene un seguro contratado se muestra el costo del seguro
                    if (integrante.segContratado == SeguroConstantes.CONTRATACION_SI) {
                        // Agregamos el costo del seguro Agosto 2017
                        int idSucursal = integrante.idSucursal;
                        int tipoSeguro = integrante.tipoSeguro;
                        SucursalVO sucursalVo = new SucursalDAO().getSucursal(idSucursal);
                        double costoSeguroContratado = SeguroHelper.getCostoSeguro(tipoSeguro, sucursalVo);
                        integrante.costoSeguro = costoSeguroContratado;
                    }
                    
                    /**
                     * JECB 01/10/2017
                     * Se obtiene el total de solicitudes del cliente que 
                     * ha desembolsado
                     */
                    grupo.ciclos[idCiclo - 1].integrantes[a].totalSolicitudesDesembolsados = solicitudDao.getTotalSolicitudesDesembolsadas(grupo.ciclos[idCiclo - 1].integrantes[a].idCliente, ClientesConstants.DESEMBOLSADO);
                    myLogger.debug("cliente:" + grupo.ciclos[idCiclo - 1].integrantes[a].idCliente + " total solicitudes desembolso:" + grupo.ciclos[idCiclo - 1].integrantes[a].totalSolicitudesDesembolsados);
                }
                grupo.ciclos[idCiclo - 1].direccionReunion = new DireccionGenericaDAO().getDireccion(grupo.ciclos[idCiclo - 1].idDireccionReunion);
                grupo.ciclos[idCiclo - 1].direccionAlterna = new DireccionGenericaDAO().getDireccion(grupo.ciclos[idCiclo - 1].idDireccionAlterna);
                grupo.ciclos[idCiclo - 1].tablaAmortizacion = new TablaAmortizacionDAO().getElementos(grupo.idGrupo, grupo.ciclos[idCiclo - 1].idCiclo, ClientesConstants.AMORTIZACION_GRUPAL);
                grupo.ciclos[idCiclo - 1].referencia = new ReferenciaGeneralDAO().getReferencia(grupo.idGrupo, grupo.ciclos[idCiclo - 1].idCiclo, 'G');
                grupo.ciclos[idCiclo - 1].eventosDePago = new EventosPagosGrupalDAO().getEventosPagos(grupo.idGrupo, grupo.ciclos[idCiclo - 1].idCiclo);
                grupo.ciclos[idCiclo - 1].archivosAsociados = new ArchivoAsociadoDAO().getArchivosTipo(grupo.idGrupo, grupo.ciclos[idCiclo - 1].idCiclo);
                
                /**
                 * JECB 01/10/2017
                 * Implementacion de las reglas de negocio:
                 * Solo se puede desembolsar un credito adicional siempre y cuando la fecha actual
                 * sea un dia habil antes de la fecha de pago
                 * Previamente se hayan agregado las solicitudes de crédito de adicional 
                 * correspondientes
                 */
                myLogger.debug("semana de dispersion adicional==>" + semanaAdicional);
                TablaAmortizacionVO tablaAmortizacion = null;
                if(grupo.ciclos[idCiclo - 1].tablaAmortizacion != null){

                    for(TablaAmortizacionVO tmpTabAmor: grupo.ciclos[idCiclo - 1].tablaAmortizacion ){
                        if(tmpTabAmor.numPago == semanaAdicional){
                            tablaAmortizacion = tmpTabAmor;
                            break;
                        }
                    }
                    
                    Date fechaActual = Calendar.getInstance().getTime();
                    myLogger.debug("Fecha de pago tabla de amortizacion:"+tablaAmortizacion.fechaPago);
                    myLogger.debug("Fecha Actual:"+new SimpleDateFormat("yyyy-MM-dd").format(fechaActual));
                    boolean es1DiaAntesFechaPago = 
                            AdicionalUtil.fechaAntesDeFechaPago(fechaActual,
                                    tablaAmortizacion.fechaPago, 
                                    (Date[]) request.getSession().getAttribute("INHABILES"));
                    myLogger.debug("Fecha actual antes de fecha de pago:"+es1DiaAntesFechaPago);
                    if(!es1DiaAntesFechaPago){
                       notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "El desembolso solo puede aplicarse 1 dia antes o el mismo dia que la fecha de pago"));                       
                    }
                    request.setAttribute("FechaPagoValida", new Boolean(es1DiaAntesFechaPago));
                }
                
                /**
                 * JECB 01/10/2017
                 * Se verifica si el grupo tiene ya al menos un integrate con 
                 * una asignacion de crédito adicional
                 */
                boolean tieneSolicitudCreditoAdicional = false;
                if(grupo.ciclos[idCiclo - 1].archivosAsociados != null 
                        && grupo.ciclos[idCiclo - 1].archivosAsociados.length > 0){
                    List<ArchivoAsociadoVO> listArchivosAdicional = new ArrayList<ArchivoAsociadoVO>();
                    for(ArchivoAsociadoVO fileAsociado : grupo.ciclos[idCiclo - 1].archivosAsociados){
                        if(fileAsociado.tipo == ClientesConstants.ARCHIVO_TIPO_SOLICITUD_ADICIONAL){
                            listArchivosAdicional.add(fileAsociado);
                        }
                    }
                    
                    if(listArchivosAdicional.isEmpty()){
                        myLogger.debug("No tiene documentos de solicitud adicional");
                        tieneSolicitudCreditoAdicional = false;
                    }else{
                        //TODO iterar la lista para ssaber si la solicitud corresponde con al de semana
                        String nombreDoc = ClientesConstants.nombreDocArchivoAdicional;
                        nombreDoc = nombreDoc.replace("#",String.valueOf(semanaAdicional));
                        myLogger.debug("Nombre de documento a buscar:" + nombreDoc);
                        for(java.util.Iterator<ArchivoAsociadoVO> itr =  listArchivosAdicional.iterator(); itr.hasNext(); ){
                            ArchivoAsociadoVO fileTmp = itr.next();
                            if(fileTmp.nombre.contains(nombreDoc)){
                                tieneSolicitudCreditoAdicional = true;
                                break;
                            }
                        }
                    }
                    
                    myLogger.debug("Tiene solicitud de credito adicional registrada para la semana:"+semanaAdicional+ " ==>"+tieneSolicitudCreditoAdicional);
                    
                    if(!tieneSolicitudCreditoAdicional){
                        notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Favor de agregar la documentación pertinente relacionada con el credito adicional"));
                    }
                }
                request.setAttribute("tieneSolicitudAdicional", tieneSolicitudCreditoAdicional);  
                //JECB 19/10/2017
                //Se verifica el estatus del credito para verificar que se encuentre vigente
                myLogger.debug("Verificando el estatus del credito para el grupo :"+grupo.idGrupo+ ", credito:"+grupo.ciclos[idCiclo - 1].idCreditoIBS);
                  
                SaldoIBSVO saldoGrupo = new SaldoIBSDAO().getSaldo(grupo.idGrupo, grupo.ciclos[idCiclo - 1].idCreditoIBS);
                boolean creditoVigente = false;
                if(saldoGrupo.getEstatus() != ClientesConstants.SITUACION_CREDITO_SALDOST24_VIGENTE){
                    notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Crédito no se encuentra vigente"));
                    creditoVigente = false;
                }else{
                    creditoVigente = true;
                }
                request.setAttribute("EstatusCredito", creditoVigente);
                myLogger.debug("Credito vigente :"+grupo.idGrupo+ ", credito:"+grupo.ciclos[idCiclo - 1].idCreditoIBS + " estatus:" + creditoVigente );
                  
                
                
                boolean tieneCargadoDocLegales = false;
                //No se piden documentos legales 
                if(semanaAdicional == ClientesConstants.DISPERSION_SEMANA_4 ){
                    myLogger.debug("No existen semanas de adicional previas a la semana 4" );
                    tieneCargadoDocLegales = true;
                }else{
                    //Obtenemos todas las las semanas en las que se han desembolsado adicionales
                    TreeSet<Integer> setSemanasDesembolsadasAdicionales = (TreeSet<Integer>) AdicionalUtil.obtienePlazosAdicionalDeIntegrantesCiclo (grupo.ciclos[idCiclo - 1].integrantes);
                    
                    if(setSemanasDesembolsadasAdicionales.isEmpty()){
                        myLogger.debug("Grupo no cuenta con integrantes que hayan desembolsado adicional previamente"  );
                        tieneCargadoDocLegales = true;
                    }else{
                        //se obtiene la ultima semana inmediata anteriro a la que se desembolso un adicional
                        int semanaAdicionalPrevia = setSemanasDesembolsadasAdicionales.last();
                        myLogger.debug("Ultima semana desembolsada de adicional:" + semanaAdicionalPrevia);
                        
                        if(grupo.ciclos[idCiclo - 1].archivosAsociados != null && grupo.ciclos[idCiclo - 1].archivosAsociados.length > 0){
                            ArchivoAsociadoVO docLegalAdicional = null;
                            
                            String nombreDocLegalAd = ClientesConstants.nombreDocLegalesAdicionales;
                            nombreDocLegalAd = nombreDocLegalAd.replace("#",String.valueOf(semanaAdicionalPrevia));
                            myLogger.debug("Nombre documento legal adicional:" + nombreDocLegalAd);

                            for(ArchivoAsociadoVO fileAsociado : grupo.ciclos[idCiclo - 1].archivosAsociados){
                                if(fileAsociado.tipo == ClientesConstants.ARCHIVO_TIPO_DOC_LEGAL_ADICIONAL 
                                        && fileAsociado.nombre.contains(nombreDocLegalAd)){
                                    docLegalAdicional = fileAsociado;
                                }
                            }
                            
                            if(docLegalAdicional != null ){
                                tieneCargadoDocLegales = true;
                            }else{
                                tieneCargadoDocLegales = false;
                                notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Favor de agregar la documentación legal pertinente a la semana de adicional " + semanaAdicionalPrevia));
                            }
                            
                        }else{
                            tieneCargadoDocLegales = false;
                            notificaciones.add(new Notification(ClientesConstants.INFO_TYPE, "Favor de agregar la documentación legal pertinente a la semana de adicional " + semanaAdicionalPrevia));
                        }
                    }
                }
                
                request.setAttribute("docLegalesCargados", tieneCargadoDocLegales);
                
                
                if(!notificaciones.isEmpty()){
                    request.setAttribute("NOTIFICACIONES", notificaciones);
                }
                    
            }
            session.setAttribute("GRUPO", grupo);
            session.setAttribute("SEMANA_ADICIONAL",semanaAdicional);
        } catch (ClientesDBException dbe) {
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommandException(e.getMessage());
        }
        return siguiente;
    }
}
