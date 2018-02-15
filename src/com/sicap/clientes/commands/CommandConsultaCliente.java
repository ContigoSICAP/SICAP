package com.sicap.clientes.commands;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.CatalogoDAO;
import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.ClienteDAO;
import com.sicap.clientes.dao.ConyugeDAO;
import com.sicap.clientes.dao.CreditoDAO;
import com.sicap.clientes.dao.DecisionComiteDAO;
import com.sicap.clientes.dao.DireccionDAO;
import com.sicap.clientes.dao.OrdenDePagoDAO;
import com.sicap.clientes.dao.SolicitudDAO;
import com.sicap.clientes.dao.SucursalDAO;
import com.sicap.clientes.dao.TelefonoDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.FechasUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.CatalogoVO;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.CreditoVO;
import com.sicap.clientes.vo.SolicitudVO;
import java.util.Date;
import org.apache.log4j.Logger;

public class CommandConsultaCliente implements Command {

    private static Logger myLogger = Logger.getLogger(CommandConsultaCliente.class);
    private String siguiente;

    public CommandConsultaCliente(String siguiente) {
        this.siguiente = siguiente;
    }

    public String execute(HttpServletRequest request) throws CommandException {

        HttpSession session = request.getSession();
        FechasUtil fechaUtil = new FechasUtil();
        ClienteVO cliente = null;        
        ClienteDAO clientedao = new ClienteDAO();
        SucursalDAO sucursaldao = new SucursalDAO();
        SolicitudDAO solicituddao = new SolicitudDAO();
        //SaldoIBSDAO saldoIBSDAO = new SaldoIBSDAO();
        //SaldoT24DAO saldoT24DAO = new SaldoT24DAO();
        DireccionDAO direcciondao = new DireccionDAO();
        TelefonoDAO telefonodao = new TelefonoDAO();
        //CreditoDAO creditodao = new CreditoDAO();
        //ObligadoSolidarioDAO obligadodao = new ObligadoSolidarioDAO();
        Notification notificaciones[] = new Notification[1];
        CatalogoVO localidad = null;
        CatalogoDAO dao = new CatalogoDAO();
        //SellFinanceDAO SellFinanceDAO = new SellFinanceDAO();
        //IntegranteCicloDAO integrantesDAO = new IntegranteCicloDAO();
        CicloGrupalDAO ciclodao = new CicloGrupalDAO();
        CicloGrupalVO ciclovo = new CicloGrupalVO();
        String MensajeDocumentos = "";
        boolean DocCompleta =false;

        int idCliente = 0;

        try {
            idCliente = HTMLHelper.getParameterInt(request, "idCliente");
            if (idCliente == 0) {
                notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "RFC no encontrado");
                request.setAttribute("NOTIFICACIONES", notificaciones);
            } else {
                cliente = clientedao.getCliente(idCliente);
                cliente.idBanco = sucursaldao.getSucursal(cliente.idSucursal).idBanco;
                cliente.direcciones = direcciondao.getDirecciones(idCliente);

                if (cliente.direcciones != null) {
                    localidad = dao.getLocalidad(cliente.direcciones[0].idColonia, cliente.direcciones[0].idLocalidad);
                    if (localidad != null) {
                        cliente.direcciones[0].localidad = localidad.descripcion;
                    }
                }
                for (int i = 0; cliente.direcciones != null && i < cliente.direcciones.length; i++) {
                    cliente.direcciones[i].telefonos = telefonodao.getTelefonos(idCliente, cliente.direcciones[i].idDireccion);
                }
                cliente.solicitudes = solicituddao.getSolicitudes(idCliente);
                
                validarSolicitud(cliente);
                
                for (int i = 0; i < cliente.solicitudes.length; i++) {
                    cliente.solicitudes[i].decisionComite = new DecisionComiteDAO().getDecisionComite(idCliente, cliente.solicitudes[i].idSolicitud);
                    cliente.solicitudes[i].ordenPago = new OrdenDePagoDAO().getOrdenDePago(idCliente, cliente.solicitudes[i].idSolicitud);
                    ciclodao.getCicloSolicitud(cliente.solicitudes[i]);
                    if (cliente.solicitudes[i].documentacionCompleta ==1){
                        DocCompleta= true;
                        MensajeDocumentos = "";
                        if (FechasUtil.obtenAniosDiferencia(cliente.solicitudes[i].fechaFirma, new Date())==1)
                            MensajeDocumentos = "Documentos no vigentes";
                    }
                }
                if (!DocCompleta){
                    for (int i = 0; i < cliente.solicitudes.length; i++) {
                        if (FechasUtil.obtenAniosDiferencia(cliente.solicitudes[i].fechaFirma, new Date())==1){
                            cliente.solicitudes[i].documentacionCompleta =1;
                            solicituddao.updateSolicitud(idCliente, cliente.solicitudes[i]);
                            MensajeDocumentos = "Documentos no vigentes"; 
                            break;
                        }
                    }
                }
                /*for (int i = 0; i < cliente.solicitudes.length; i++) {
                 cliente.solicitudes[i].referencia = new ReferenciaGeneralDAO().getReferencia(idCliente, cliente.solicitudes[i].idSolicitud, 'I');
                 if (cliente.solicitudes[i].saldoVigente == 0 || cliente.solicitudes[i].saldoVencido == 0) {
                 if (cliente.solicitudes[i].idCreditoIBS != 0 && cliente.solicitudes[i].idCuentaIBS != 0) {
                 cliente.solicitudes[i].saldo = saldoIBSDAO.getSaldo(cliente.idCliente, cliente.solicitudes[i].idSolicitud, cliente.solicitudes[i].referencia);
                 } else {
                 cliente.solicitudes[i].saldo = IBSHelper.getSaldosT24ToIBS(saldoT24DAO.getSaldosT24ByNumClienteCiclo(idCliente, cliente.solicitudes[i].idSolicitud, cliente.solicitudes[i].referencia));
                 }

                 if (cliente.solicitudes[i].saldo != null) {
                 cliente.solicitudes[i].saldoVencido = FormatUtil.roundDouble(cliente.solicitudes[i].saldo.getTotalVencido(), 2);
                 cliente.solicitudes[i].saldoVigente = cliente.solicitudes[i].saldo.getSaldoTotalAlDia();
                 if (cliente.solicitudes[i].saldoVigente < 0) {
                 cliente.solicitudes[i].saldoVigente = 0;
                 }
                 }
                 }
                 int idSolicitud = cliente.solicitudes[i].idSolicitud;
                 cliente.solicitudes[i].obligadosSolidarios = obligadodao.getObligadosSolidarios(idCliente, idSolicitud);
                 for (int j = 0; j < cliente.solicitudes[i].obligadosSolidarios.length; j++) {
                 int idObligado = cliente.solicitudes[i].obligadosSolidarios[j].idObligado;
                 cliente.solicitudes[i].obligadosSolidarios[j].direccion = direcciondao.getDireccion(idCliente, idSolicitud, "d_obligados_solidarios", idObligado);
                 cliente.solicitudes[i].obligadosSolidarios[j].buroCredito = creditodao.getCredito(idCliente, idSolicitud, ClientesConstants.BURO_CREDIT0, idObligado);
                 cliente.solicitudes[i].obligadosSolidarios[j].circuloCredito = creditodao.getCredito(idCliente, idSolicitud, ClientesConstants.CIRCULO_CREDIT0, idObligado);
                 cliente.solicitudes[i].obligadosSolidarios[j].economia = new EconomiaObligadoDAO().getEconomiaObligado(idCliente, idSolicitud, idObligado);
                 cliente.solicitudes[i].obligadosSolidarios[j].infoCreditoBuro = new InformacionCrediticiaDAO().getInfoCrediticia(idCliente, cliente.solicitudes[i].idSolicitud, idObligado, ClientesConstants.SOCIEDAD_BURO);
                 cliente.solicitudes[i].obligadosSolidarios[j].infoCreditoCirculo = new InformacionCrediticiaDAO().getInfoCrediticia(idCliente, cliente.solicitudes[i].idSolicitud, idObligado, ClientesConstants.SOCIEDAD_CIRCULO);
                 }
                 cliente.solicitudes[i].buroCredito = creditodao.getCredito(idCliente, idSolicitud, ClientesConstants.BURO_CREDIT0);
                 cliente.solicitudes[i].circuloCredito = creditodao.getCredito(idCliente, idSolicitud, ClientesConstants.CIRCULO_CREDIT0);
                 cliente.solicitudes[i].referenciasComerciales = new ReferenciaComercialDAO().getReferenciasComerciales(idCliente, idSolicitud);
                 cliente.solicitudes[i].referenciasPersonales = new ReferenciaPersonalDAO().getReferenciaPersonales(idCliente, idSolicitud);
                 cliente.solicitudes[i].decisionComite = new DecisionComiteDAO().getDecisionComite(idCliente, idSolicitud);
                 cliente.solicitudes[i].ordenPago = new OrdenDePagoDAO().getOrdenDePago(idCliente, idSolicitud);
                 cliente.solicitudes[i].arrendatarioDomicilio = new ArrendatarioDAO().getArrendatario(idCliente, idSolicitud, ClientesConstants.ARRENDATARIO_DOMICILIO);
                 cliente.solicitudes[i].arrendatarioLocal = new ArrendatarioDAO().getArrendatario(idCliente, idSolicitud, ClientesConstants.ARRENDATARIO_LOCAL);
                 cliente.solicitudes[i].negocio = new NegocioDAO().getNegocio(idCliente, idSolicitud);
                 if (cliente.solicitudes[i].negocio != null) {
                 cliente.solicitudes[i].negocio.direccion = direcciondao.getDireccion(idCliente, idSolicitud, "d_negocios", 1);
                 }
                 cliente.solicitudes[i].informacion = new InformacionFinancieraDAO().getInformacionFinanciera(idCliente, idSolicitud);
                 cliente.solicitudes[i].archivosAsociados = new ArchivoAsociadoDAO().getArchivos(idCliente, idSolicitud);
                 cliente.solicitudes[i].empleo = new EmpleoDAO().getEmpleo(idCliente, idSolicitud);
                 if (cliente.solicitudes[i].empleo != null) {
                 cliente.solicitudes[i].empleo.direccion = direcciondao.getDireccion(idCliente, idSolicitud, "d_empleos", 1);
                 }
                 cliente.solicitudes[i].vivienda = new ViviendaDAO().getVivienda(idCliente, idSolicitud);
                 cliente.solicitudes[i].capacidadPago = new CapacidadPagoDAO().getCapacidadPago(idCliente, idSolicitud);
                 cliente.solicitudes[i].referenciaLaboral = new ReferenciaLaboralDAO().getReferencia(idCliente, idSolicitud);
                 cliente.solicitudes[i].referenciasCrediticias = new ReferenciaCrediticiaDAO().getReferenciasCrediticias(idCliente, idSolicitud);
                 cliente.solicitudes[i].seguro = new SegurosDAO().getSeguros(idCliente, idSolicitud);
                 cliente.solicitudes[i].amortizacion = new TablaAmortizacionDAO().getElementos(idCliente, idSolicitud, ClientesConstants.AMORTIZACION_INDIVIDUAL);
                 if (cliente.solicitudes[i].seguro != null) {
                 cliente.solicitudes[i].seguro.asegurados = new AseguradosDAO().getAsegurados(idCliente, idSolicitud);
                 cliente.solicitudes[i].seguro.beneficiarios = new BeneficiarioDAO().getBeneficiarios(idCliente, idSolicitud);
                 }
                 cliente.solicitudes[i].scoring = new ScoringDAO().getScoring(idCliente, idSolicitud);
                 cliente.solicitudes[i].creditoVivienda = new CreditoViviendaDAO().getCreditoVivienda(idCliente, idSolicitud);
                 if (cliente.solicitudes[i].creditoVivienda != null) {
                 cliente.solicitudes[i].creditoVivienda.direccion = new DireccionDAO().getDireccionLocalidad(idCliente, idSolicitud, "d_credito_vivienda", 1);
                 }
                 cliente.solicitudes[i].expediente = new ExpedienteDAO().getExpediente(idCliente, idSolicitud);
                 cliente.solicitudes[i].infoCreditoBuro = new InformacionCrediticiaDAO().getInfoCrediticia(idCliente, cliente.solicitudes[i].idSolicitud, 0, ClientesConstants.SOCIEDAD_BURO);
                 cliente.solicitudes[i].infoCreditoCirculo = new InformacionCrediticiaDAO().getInfoCrediticia(idCliente, cliente.solicitudes[i].idSolicitud, 0, ClientesConstants.SOCIEDAD_CIRCULO);
                 cliente.solicitudes[i].limites = new LimiteCreditoDAO().getLimite(idCliente, idSolicitud);
                 cliente.solicitudes[i].disposiciones = new DisposicionDAO().getDisposiciones(idCliente, idSolicitud);
                 for (int c = 0; cliente.solicitudes[i].disposiciones != null && c < cliente.solicitudes[i].disposiciones.length; c++) {
                 cliente.solicitudes[i].disposiciones[c].tablaAmostizaciones = new TablaAmortizacionDAO().getElementos(idCliente, idSolicitud, cliente.solicitudes[i].disposiciones[c].idDisposicion, ClientesConstants.AMORTIZACION_INDIVIDUAL);
                 }
                 if (cliente.solicitudes[i].tipoOperacion == ClientesConstants.SELL_FINANCE) {
                 cliente.solicitudes[i].sellFinance = SellFinanceDAO.getSellFinance(idCliente, idSolicitud);
                 }
                 cliente.solicitudes[i].enGarantia = integrantesDAO.getGrupoGarantia(idCliente, idSolicitud);
                 }*/
                cliente.conyuge = new ConyugeDAO().getConyuge(idCliente);
                session.setAttribute("CLIENTE", cliente);
                session.setAttribute("MENSAJEDOC", MensajeDocumentos);
                
            }
        } catch (ClientesDBException dbe) {
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommandException(e.getMessage());
        }
        return siguiente;

    }
    
    private void validarSolicitud(ClienteVO cliente) throws ClientesException{
        
        int sizeSolicitudes = cliente.solicitudes.length;
        SolicitudDAO solicituddao = new SolicitudDAO();
//        CreditoDAO creditoDAO =  new CreditoDAO();
        int numSolCanceladas = 0;
//        CreditoVO circuloUltimoCredito = creditoDAO.getUltimoCredito(cliente.idCliente, ClientesConstants.CIRCULO_CREDIT0, 0);
//        int ultimaCalif = 0;
//        if(circuloUltimoCredito != null){
//            ultimaCalif = (circuloUltimoCredito.calificacionMesaControl==0)?circuloUltimoCredito.comportamiento:circuloUltimoCredito.calificacionMesaControl;
//        }
        for(SolicitudVO sol : cliente.solicitudes){   
            if(((sol.desembolsado==ClientesConstants.LISTO_DESEMBOLSAR || sol.desembolsado==0) && sol.idSolicitud!=sizeSolicitudes)//caso 1: si la solicitud es intermedia y sin desembolsar
/*                    ||((sol.desembolsado==ClientesConstants.LISTO_DESEMBOLSAR || sol.desembolsado==0) && sol.idSolicitud==sizeSolicitudes //caso 3: si la solicitud es la ultima, sin desembolsar y con mala calificacion
                        && (ultimaCalif == ClientesConstants.CALIFICACION_CIRCULO_MALA))*/)
            {
                sol.desembolsado=ClientesConstants.CANCELADO;
                sol.estatus=ClientesConstants.CANCELADO;
                solicituddao.cancelarSolicitud(sol.idCliente, sol.idSolicitud);
                numSolCanceladas++;
            }
        }
        myLogger.info("Numero de solicitudes canceladas: "+numSolCanceladas);
    }
}
