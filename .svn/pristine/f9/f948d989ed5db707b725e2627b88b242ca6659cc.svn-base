package com.sicap.clientes.commands;

import com.sicap.clientes.dao.ArchivoAsociadoDAO;
import com.sicap.clientes.dao.ArrendatarioDAO;
import com.sicap.clientes.dao.AseguradosDAO;
import com.sicap.clientes.dao.BeneficiarioDAO;
import com.sicap.clientes.dao.CapacidadPagoDAO;
import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.CreditoDAO;
import com.sicap.clientes.dao.CreditoViviendaDAO;
import com.sicap.clientes.dao.DecisionComiteDAO;
import com.sicap.clientes.dao.DireccionDAO;
import com.sicap.clientes.dao.DisposicionDAO;
import com.sicap.clientes.dao.EconomiaObligadoDAO;
import com.sicap.clientes.dao.EmpleoDAO;
import com.sicap.clientes.dao.ExpedienteDAO;
import com.sicap.clientes.dao.InformacionCrediticiaDAO;
import com.sicap.clientes.dao.InformacionFinancieraDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.LimiteCreditoDAO;
import com.sicap.clientes.dao.NegocioDAO;
import com.sicap.clientes.dao.ObligadoSolidarioDAO;
import com.sicap.clientes.dao.OrdenDePagoDAO;
import com.sicap.clientes.dao.ReferenciaComercialDAO;
import com.sicap.clientes.dao.ReferenciaCrediticiaDAO;
import com.sicap.clientes.dao.ReferenciaGeneralDAO;
import com.sicap.clientes.dao.ReferenciaLaboralDAO;
import com.sicap.clientes.dao.ReferenciaPersonalDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.SaldoT24DAO;
import com.sicap.clientes.dao.ScoringDAO;
import com.sicap.clientes.dao.SegurosDAO;
import com.sicap.clientes.dao.SellFinanceDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.dao.TablaAmortizacionDAO;
import com.sicap.clientes.dao.ViviendaDAO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.IBSHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.util.FechasUtil;
import com.sicap.clientes.util.FormatUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SolicitudUtil;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.CreditoVO;
import com.sicap.clientes.vo.SolicitudVO;
import com.sicap.clientes.vo.UsuarioVO;
import java.sql.Date;
import java.util.Vector;
import org.apache.log4j.Logger;

public class CommandConsultaSolicitudCliente implements Command {

    private String siguiente;
    private static Logger myLogger = Logger.getLogger(CommandConsultaSolicitudCliente.class);

    public CommandConsultaSolicitudCliente(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        HttpSession session = request.getSession();
        UsuarioVO usuario = (UsuarioVO) session.getAttribute("USUARIO");
        ClienteVO cliente = new ClienteVO();
        CicloGrupalVO cicloGrupalVO = null;
        //SolicitudDAO solicitudDAO = new SolicitudDAO();
        CicloGrupalDAO cicloGrupalDAO = new CicloGrupalDAO();
        String siguienteConsumo = "/capturaDatosClienteConsumo.jsp";
        String siguienteVivienda = "/capturaDatosClienteVivienda.jsp";
        String sellFinanceExterno = "/capturaAbreviadaDatosCliente.jsp";
        String siguienteDescuento = "/capturaDatosClienteDescuento.jsp";
        String siguienteCapturaRapida = "/clienteCapturaRapida.jsp";
        String siguienteRenovacion = "/nuevoCliente.jsp";
        String siguienteCancelacionCapturaRapida = "/cancelaCapRap.jsp";
        SolicitudVO solicitud = null;
        int idSolicitud = 0;
        SaldoIBSDAO saldoIBSDAO = new SaldoIBSDAO();
        SaldoT24DAO saldoT24DAO = new SaldoT24DAO();
        ObligadoSolidarioDAO obligadoDAO = new ObligadoSolidarioDAO();
        DireccionDAO direccionDAO = new DireccionDAO();
        SolicitudDAO solicitudDAO = new SolicitudDAO();
        CreditoDAO creditoDAO = new CreditoDAO();
        SellFinanceDAO SellFinanceDAO = new SellFinanceDAO();
        IntegranteCicloDAO integrantesDAO = new IntegranteCicloDAO();
        boolean limpiaSolicitud = false;
        Notification[] notificaciones = new Notification[1];
        
        try {
            idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
            cliente = (ClienteVO) session.getAttribute("CLIENTE");
            int indiceSolicitud = SolicitudUtil.getIndice(cliente.solicitudes, idSolicitud);
            solicitud = cliente.solicitudes[indiceSolicitud];
            
            //------------- 070417 ------------------
           
            int k = cliente.solicitudes.length;
            myLogger.debug("---> k trae:"+k);
            
            //------------- 070417 ------------------
            cliente.solicitudes[indiceSolicitud].idGrupo = new SolicitudDAO().getNumEquipo(cliente.idCliente, cliente.solicitudes[indiceSolicitud].idSolicitud);
            cliente.solicitudes[indiceSolicitud].idCiclo = new IntegranteCicloDAO().getCicloSolicitud(cliente.solicitudes[indiceSolicitud].idGrupo, solicitud);
            if (cliente.solicitudes[indiceSolicitud].idCiclo != 0) {
                cicloGrupalVO = new CicloGrupalVO();
                cicloGrupalVO = cicloGrupalDAO.getCiclo(cliente.solicitudes[indiceSolicitud].idGrupo, cliente.solicitudes[indiceSolicitud].idCiclo);
            }
            /*if (SolicitudUtil.revisaAntSolicitud(solicitud, cicloGrupalVO)) {
                //Carga la informaicon de la solicitud de nuevo
                SolicitudUtil.limpiaSolicitud(solicitud, cicloGrupalVO,cliente.solicitudes);
                System.out.println("carga la informacion de la solicitud nuevamente");
                solicitud = solicitudDAO.getSolicitud(cliente.idCliente, idSolicitud);
                cliente.solicitudes[indiceSolicitud] = solicitud;
                
            }*/
            cliente.solicitudes[indiceSolicitud].referencia = new ReferenciaGeneralDAO().getReferencia(cliente.idCliente, cliente.solicitudes[indiceSolicitud].idSolicitud, 'I');
            if (cliente.solicitudes[indiceSolicitud].saldoVigente == 0 || cliente.solicitudes[indiceSolicitud].saldoVencido == 0) {
                if (cliente.solicitudes[indiceSolicitud].idCreditoIBS != 0 && cliente.solicitudes[indiceSolicitud].idCuentaIBS != 0) {
                    cliente.solicitudes[indiceSolicitud].saldo = saldoIBSDAO.getSaldo(cliente.idCliente, cliente.solicitudes[indiceSolicitud].idSolicitud, cliente.solicitudes[indiceSolicitud].referencia);
                } else {
                    cliente.solicitudes[indiceSolicitud].saldo = IBSHelper.getSaldosT24ToIBS(saldoT24DAO.getSaldosT24ByNumClienteCiclo(cliente.idCliente, cliente.solicitudes[indiceSolicitud].idSolicitud, cliente.solicitudes[indiceSolicitud].referencia));
                }

                if (cliente.solicitudes[indiceSolicitud].saldo != null) {
                    cliente.solicitudes[indiceSolicitud].saldoVencido = FormatUtil.roundDouble(cliente.solicitudes[indiceSolicitud].saldo.getTotalVencido(), 2);
                    cliente.solicitudes[indiceSolicitud].saldoVigente = cliente.solicitudes[indiceSolicitud].saldo.getSaldoTotalAlDia();
                    if (cliente.solicitudes[indiceSolicitud].saldoVigente < 0) {
                        cliente.solicitudes[indiceSolicitud].saldoVigente = 0;
                    }
                }
            }
            cliente.solicitudes[indiceSolicitud].obligadosSolidarios = obligadoDAO.getObligadosSolidarios(cliente.idCliente, idSolicitud);
            for (int j = 0; j < cliente.solicitudes[indiceSolicitud].obligadosSolidarios.length; j++) {
                int idObligado = cliente.solicitudes[indiceSolicitud].obligadosSolidarios[j].idObligado;
                cliente.solicitudes[indiceSolicitud].obligadosSolidarios[j].direccion = direccionDAO.getDireccion(cliente.idCliente, idSolicitud, "d_obligados_solidarios", idObligado);
                cliente.solicitudes[indiceSolicitud].obligadosSolidarios[j].buroCredito = creditoDAO.getCredito(cliente.idCliente, idSolicitud, ClientesConstants.BURO_CREDIT0, idObligado);
                cliente.solicitudes[indiceSolicitud].obligadosSolidarios[j].circuloCredito = creditoDAO.getCredito(cliente.idCliente, idSolicitud, ClientesConstants.CIRCULO_CREDIT0, idObligado);
                cliente.solicitudes[indiceSolicitud].obligadosSolidarios[j].economia = new EconomiaObligadoDAO().getEconomiaObligado(cliente.idCliente, idSolicitud, idObligado);
                cliente.solicitudes[indiceSolicitud].obligadosSolidarios[j].infoCreditoBuro = new InformacionCrediticiaDAO().getInfoCrediticia(cliente.idCliente, cliente.solicitudes[indiceSolicitud].idSolicitud, idObligado, ClientesConstants.SOCIEDAD_BURO);
                cliente.solicitudes[indiceSolicitud].obligadosSolidarios[j].infoCreditoCirculo = new InformacionCrediticiaDAO().getInfoCrediticia(cliente.idCliente, cliente.solicitudes[indiceSolicitud].idSolicitud, idObligado, ClientesConstants.SOCIEDAD_CIRCULO);
            }
            cliente.solicitudes[indiceSolicitud].buroCredito = creditoDAO.getCredito(cliente.idCliente, idSolicitud, ClientesConstants.BURO_CREDIT0);
            cliente.solicitudes[indiceSolicitud].circuloCredito = creditoDAO.getCredito(cliente.idCliente, idSolicitud, ClientesConstants.CIRCULO_CREDIT0);
            cliente.solicitudes[indiceSolicitud].referenciasComerciales = new ReferenciaComercialDAO().getReferenciasComerciales(cliente.idCliente, idSolicitud);
            cliente.solicitudes[indiceSolicitud].referenciasPersonales = new ReferenciaPersonalDAO().getReferenciaPersonales(cliente.idCliente, idSolicitud);
            cliente.solicitudes[indiceSolicitud].decisionComite = new DecisionComiteDAO().getDecisionComite(cliente.idCliente, idSolicitud);
            cliente.solicitudes[indiceSolicitud].ordenPago = new OrdenDePagoDAO().getOrdenDePago(cliente.idCliente, idSolicitud);
            cliente.solicitudes[indiceSolicitud].arrendatarioDomicilio = new ArrendatarioDAO().getArrendatario(cliente.idCliente, idSolicitud, ClientesConstants.ARRENDATARIO_DOMICILIO);
            cliente.solicitudes[indiceSolicitud].arrendatarioLocal = new ArrendatarioDAO().getArrendatario(cliente.idCliente, idSolicitud, ClientesConstants.ARRENDATARIO_LOCAL);
            cliente.solicitudes[indiceSolicitud].negocio = new NegocioDAO().getNegocio(cliente.idCliente, idSolicitud);
            if (cliente.solicitudes[indiceSolicitud].negocio != null) {
                cliente.solicitudes[indiceSolicitud].negocio.direccion = direccionDAO.getDireccion(cliente.idCliente, idSolicitud, "d_negocios", 1);
            }
            cliente.solicitudes[indiceSolicitud].informacion = new InformacionFinancieraDAO().getInformacionFinanciera(cliente.idCliente, idSolicitud);
            cliente.solicitudes[indiceSolicitud].archivosAsociados = new ArchivoAsociadoDAO().getArchivos(cliente.idCliente, idSolicitud);
            cliente.solicitudes[indiceSolicitud].empleo = new EmpleoDAO().getEmpleo(cliente.idCliente, idSolicitud);
            if (cliente.solicitudes[indiceSolicitud].empleo != null) {
                cliente.solicitudes[indiceSolicitud].empleo.direccion = direccionDAO.getDireccion(cliente.idCliente, idSolicitud, "d_empleos", 1);
            }
            cliente.solicitudes[indiceSolicitud].vivienda = new ViviendaDAO().getVivienda(cliente.idCliente, idSolicitud);
            cliente.solicitudes[indiceSolicitud].capacidadPago = new CapacidadPagoDAO().getCapacidadPago(cliente.idCliente, idSolicitud);
            cliente.solicitudes[indiceSolicitud].referenciaLaboral = new ReferenciaLaboralDAO().getReferencia(cliente.idCliente, idSolicitud);
            cliente.solicitudes[indiceSolicitud].referenciasCrediticias = new ReferenciaCrediticiaDAO().getReferenciasCrediticias(cliente.idCliente, idSolicitud);
            cliente.solicitudes[indiceSolicitud].seguro = new SegurosDAO().getSeguros(cliente.idCliente, idSolicitud);
            cliente.solicitudes[indiceSolicitud].amortizacion = new TablaAmortizacionDAO().getElementos(cliente.idCliente, idSolicitud, ClientesConstants.AMORTIZACION_INDIVIDUAL);
            if (cliente.solicitudes[indiceSolicitud].seguro != null) {
                cliente.solicitudes[indiceSolicitud].seguro.asegurados = new AseguradosDAO().getAsegurados(cliente.idCliente, idSolicitud);
                cliente.solicitudes[indiceSolicitud].seguro.beneficiarios = new BeneficiarioDAO().getBeneficiarios(cliente.idCliente, idSolicitud);
            }
            cliente.solicitudes[indiceSolicitud].scoring = new ScoringDAO().getScoring(cliente.idCliente, idSolicitud);
            cliente.solicitudes[indiceSolicitud].creditoVivienda = new CreditoViviendaDAO().getCreditoVivienda(cliente.idCliente, idSolicitud);
            if (cliente.solicitudes[indiceSolicitud].creditoVivienda != null) {
                cliente.solicitudes[indiceSolicitud].creditoVivienda.direccion = new DireccionDAO().getDireccionLocalidad(cliente.idCliente, idSolicitud, "d_credito_vivienda", 1);
            }
            cliente.solicitudes[indiceSolicitud].expediente = new ExpedienteDAO().getExpediente(cliente.idCliente, idSolicitud);
            cliente.solicitudes[indiceSolicitud].infoCreditoBuro = new InformacionCrediticiaDAO().getInfoCrediticia(cliente.idCliente, cliente.solicitudes[indiceSolicitud].idSolicitud, 0, ClientesConstants.SOCIEDAD_BURO);
            cliente.solicitudes[indiceSolicitud].infoCreditoCirculo = new InformacionCrediticiaDAO().getInfoCrediticia(cliente.idCliente, cliente.solicitudes[indiceSolicitud].idSolicitud, 0, ClientesConstants.SOCIEDAD_CIRCULO);
            cliente.solicitudes[indiceSolicitud].limites = new LimiteCreditoDAO().getLimite(cliente.idCliente, idSolicitud);
            cliente.solicitudes[indiceSolicitud].disposiciones = new DisposicionDAO().getDisposiciones(cliente.idCliente, idSolicitud);
            for (int c = 0; cliente.solicitudes[indiceSolicitud].disposiciones != null && c < cliente.solicitudes[indiceSolicitud].disposiciones.length; c++) {
                cliente.solicitudes[indiceSolicitud].disposiciones[c].tablaAmostizaciones = new TablaAmortizacionDAO().getElementos(cliente.idCliente, idSolicitud, cliente.solicitudes[indiceSolicitud].disposiciones[c].idDisposicion, ClientesConstants.AMORTIZACION_INDIVIDUAL);
            }
            if (cliente.solicitudes[indiceSolicitud].tipoOperacion == ClientesConstants.SELL_FINANCE) {
                cliente.solicitudes[indiceSolicitud].sellFinance = SellFinanceDAO.getSellFinance(cliente.idCliente, idSolicitud);
            }
            cliente.solicitudes[indiceSolicitud].enGarantia = integrantesDAO.getGrupoGarantia(cliente.idCliente, idSolicitud);
            
//            IntegranteCicloDAO integranteCicloDAO = new IntegranteCicloDAO();
             Date fechaHoy = Convertidor.toSqlDate(new java.util.Date());
             SolicitudDAO sol = new SolicitudDAO();
             CreditoDAO cred = new CreditoDAO();
             InformacionCrediticiaDAO infocred = new InformacionCrediticiaDAO();

             if (solicitud.desembolsado==ClientesConstants.LISTO_DESEMBOLSAR || solicitud.desembolsado==0){
                 myLogger.info("Solicitud SIN DESEMBOLSO");
                 SolicitudVO ultimoDesembolso = obtenerUltimoDesembolso(cliente, indiceSolicitud);
                 CreditoVO circuloUltimoCredito = null;
                 
                 //Si tiene desembolsos anteriores buscamos si la fecha de desembolso es menor a un año
                 if (ultimoDesembolso!=null && FechasUtil.inBetweenDays(ultimoDesembolso.fechaDesembolso, fechaHoy) < 365){
                     myLogger.info("Hay desembolso anterior menor a un año");
                     myLogger.info("Se queda el estatus como esta: "+solicitud.estatus);
                     notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Tu solicitud aún está vigente");
                 }else{//Si NO tiene desembolsos anteriores o es mayor a un año, verificamos si la consulta de credito de la solicitud es menor a 30 dias
                    myLogger.info("NO tiene desembolsos anteriores o es mayor a un año...");
                    circuloUltimoCredito = creditoDAO.getUltimoCredito(cliente.idCliente, ClientesConstants.CIRCULO_CREDIT0, 0);
                    if (circuloUltimoCredito != null
                           && FechasUtil.inBetweenDays(circuloUltimoCredito.fechaConsulta, fechaHoy) < 30) {
                        myLogger.info("Se queda el estatus como esta: "+solicitud.estatus);
                        notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "Tu solicitud aún está vigente");
                    }else if (solicitud.estatus != ClientesConstants.SOLICITUD_NUEVA && solicitud.estatus != ClientesConstants.SOLICITUD_EN_ANALISIS
                                && solicitud.estatus != ClientesConstants.SOLICITUD_PENDIENTE && solicitud.estatus != ClientesConstants.SOLICITUD_REVALORACION){
                        myLogger.debug("Sin Consulta de credito, o consulta mayor a 30 dias");
                        myLogger.debug("Diferente a captura rapida y analisis");
                        actualizaCapturaRapida(cliente, sol, cred, infocred, indiceSolicitud);
                        solicitud.estatus =7;//para que se guarde en session y se vaya a la ventana correcta
                        cliente.solicitudes[indiceSolicitud].circuloCredito = null;
                    }else{
                        myLogger.debug("Se queda como esta....");
                    }
                    
                 }                
             }
            
            //TCC
            if(indiceSolicitud == (cliente.solicitudes.length - 1))
            {
               //Se heredan datos
                if(cliente.solicitudes.length > 1)
                {
//                    cliente.solicitudes[indiceSolicitud] = com.sicap.clientes.util.SolicitudUtil.heredaDatosDelCicloAnterior(cliente.solicitudes[indiceSolicitud], cliente.solicitudes[indiceSolicitud - 1], cliente.idSucursal, cliente.solicitudes[indiceSolicitud].idEjecutivo);
                    cliente.solicitudes[indiceSolicitud].fechaFirmaConUltimoCambioDoctos =  com.sicap.clientes.util.SolicitudUtil.DeterminaFechaUltimoCambioEnDocumentacion(cliente);
                }
            }
            
            session.setAttribute("CLIENTE", cliente);
            session.setAttribute("CICLOGRUPAL", cicloGrupalVO);
            request.setAttribute("NOTIFICACIONES", notificaciones);
        } catch (ClientesDBException dbe) {
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommandException(e.getMessage());
        }
        myLogger.debug("---- manda a jsp´s ---");
        if (solicitud.tipoOperacion == ClientesConstants.CONSUMO || solicitud.tipoOperacion == ClientesConstants.CREDIHOGAR || solicitud.tipoOperacion == ClientesConstants.MAX_ZAPATOS) {
            return siguienteConsumo;
        } else if (solicitud.tipoOperacion == ClientesConstants.VIVIENDA) {
            return siguienteVivienda;
        } else if (solicitud.tipoOperacion == ClientesConstants.SELL_FINANCE && usuario.identificador.equals("E")) {
            return sellFinanceExterno;
        } else if (solicitud.tipoOperacion == ClientesConstants.SELL_FINANCE && usuario.identificador.equals("I")) {
            return siguienteDescuento;
        } else if (solicitud.estatus == ClientesConstants.SOLICITUD_NUEVA || solicitud.estatus == ClientesConstants.SOLICITUD_EN_ANALISIS
                || solicitud.estatus == ClientesConstants.SOLICITUD_PENDIENTE || solicitud.estatus == ClientesConstants.SOLICITUD_REVALORACION) {
            return siguienteCapturaRapida;
         //        } else if (solicitud.estatus == ClientesConstants.SOLICITUD_CAPTURADO){
//            return siguienteRenovacion;
        //--------------------------------
        /*} else if (solicitud.tipoOperacion == ClientesConstants.GRUPAL) {
            //EVALUACION DE DIAS DE FECHA DE CREDITO Y FECHA DE ULTIMA CONSULTA
            if (solicitud.estatus == ClientesConstants.SOLICITUD_NUEVA || solicitud.estatus == ClientesConstants.SOLICITUD_EN_ANALISIS
                || solicitud.estatus == ClientesConstants.SOLICITUD_PENDIENTE || solicitud.estatus == ClientesConstants.SOLICITUD_REVALORACION){
                return siguienteCapturaRapida;
            }
        */
        //--------------------------------    
        } else {
            return siguiente;
        }
    }
    
    private void actualizaCapturaRapida(ClienteVO cliente, SolicitudDAO sol, CreditoDAO cred, InformacionCrediticiaDAO infocred, int indiceSolicitud) throws ClientesException{
        //QUERYS LALIN
        myLogger.info("Aplica CAPTURA RAPIDA");
        myLogger.debug("-------------------------------------------");
        myLogger.debug("idCliente:"+cliente.idCliente);
        myLogger.debug("idSolicitud:"+cliente.solicitudes[indiceSolicitud].idSolicitud);
        myLogger.debug("antes de updateSolicitudCapturaRapida()");
        sol.updateSolicitudCapturaRapida(cliente.idCliente, cliente.solicitudes[indiceSolicitud].idSolicitud);

        myLogger.debug("idCliente:"+cliente.idCliente);
        myLogger.debug("idSolicitud:"+cliente.solicitudes[indiceSolicitud].idSolicitud);
        myLogger.debug("antes de delInfoCrediticia() en cred");
        cred.delInfoCrediticia(null,cliente.idCliente, cliente.solicitudes[indiceSolicitud].idSolicitud);

        myLogger.debug("idCliente:"+cliente.idCliente);
        myLogger.debug("idSolicitud:"+cliente.solicitudes[indiceSolicitud].idSolicitud);
        myLogger.debug("antes de delInfoCrediticia() en infocred");
        infocred.delInfoCrediticia(null,cliente.idCliente, cliente.solicitudes[indiceSolicitud].idSolicitud);
        myLogger.debug("-------------------------------------------");
        
        
        myLogger.debug("Elimina Archivos asociados y actualiza bandera doc completa");
        cliente.solicitudes[indiceSolicitud].documentacionCompleta = 1;
                                    
        myLogger.info("Fecha Firma: "+cliente.solicitudes[indiceSolicitud].fechaFirma);
        if(cliente.solicitudes[indiceSolicitud].fechaFirma==null){
            cliente.solicitudes[indiceSolicitud].fechaFirma = new Date(new java.util.Date().getTime());
        }
        new SolicitudDAO().updateDocumentacionCompletaSolicitud(null, cliente.solicitudes[indiceSolicitud].idCliente , indiceSolicitud, cliente.solicitudes[indiceSolicitud].documentacionCompleta, cliente.solicitudes[indiceSolicitud].fechaFirma);

        cliente.solicitudes[indiceSolicitud].fechaFirmaConUltimoCambioDoctos = SolicitudUtil.DeterminaFechaUltimoCambioEnDocumentacion(cliente);

        //VALIDAR AQUI SI SE DEBE CONSIDERAR ESTATUS DE GRUPO PARA NO ELIMINAR EL REGISTRO
        ArchivoAsociadoDAO archivoDAO = new ArchivoAsociadoDAO();
        archivoDAO.deleteArchivoAsociado(cliente.solicitudes[indiceSolicitud].idCliente, cliente.solicitudes[indiceSolicitud].idSolicitud, ClientesConstants.ARCHIVO_TIPO_AUTORIZACION);
        cliente.solicitudes[indiceSolicitud].archivosAsociados = null;
    }
    
    private SolicitudVO obtenerUltimoDesembolso(ClienteVO cliente, int indiceSolicitud){
        SolicitudVO ultimoDesembolso = null;
        for(int i = indiceSolicitud-1; i>=0; i--){
            if(cliente.solicitudes[i].desembolsado == ClientesConstants.DESEMBOLSADO){
                myLogger.info("indice Ultimo desembolso: "+i);
                myLogger.info("Fecha desembolso2: "+cliente.solicitudes[i].fechaDesembolso);
                ultimoDesembolso=cliente.solicitudes[i];
                break;
            }
        }
        return ultimoDesembolso;
    }

}
