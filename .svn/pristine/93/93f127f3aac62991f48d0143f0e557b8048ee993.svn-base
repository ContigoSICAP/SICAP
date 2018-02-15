package com.sicap.clientes.commands;

import com.sicap.clientes.dao.GrupoDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import com.sicap.clientes.dao.OrdenDePagoDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.cartera.CreditoCartDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.cartera.EventoHelper;
import com.sicap.clientes.helpers.cartera.TransaccionesHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.IntegranteCicloVO;
import com.sicap.clientes.vo.OrdenDePagoVO;
import com.sicap.clientes.vo.cartera.CreditoCartVO;
import com.sicap.clientes.vo.cartera.RubrosVO;
import java.util.ArrayList;
import java.util.Calendar;

public class CommandGuardaOrdenSaldoFavor implements Command{

	private String siguiente;

	public CommandGuardaOrdenSaldoFavor(String siguiente) {
		this.siguiente = siguiente;
	}

	public String execute(HttpServletRequest request) throws CommandException {
		Vector<Notification> notificaciones = new Vector<Notification>();
                try{
                    int idEquipo = HTMLHelper.getParameterInt(request, "numEquipo");
                    int idCiclo = HTMLHelper.getParameterInt(request, "numCiclo");
                    int rolIntegrante = HTMLHelper.getParameterInt(request, "beneficiario");
                    int idBanco = HTMLHelper.getParameterInt(request, "idBanco");
                    double monto = HTMLHelper.getParameterDouble(request, "montoSaldoFavor");
                    BitacoraUtil bitacora = new BitacoraUtil(idEquipo, request.getRemoteUser(), "CommandGuardaOrdenSaldoFavor");
                    IntegranteCicloDAO integranteDAO = new IntegranteCicloDAO();
                    OrdenDePagoDAO oPagoDAO = new OrdenDePagoDAO();
                    GrupoDAO grupoDao = new GrupoDAO();
                    CreditoCartDAO creditoDao = new CreditoCartDAO();
                    SaldoIBSDAO saldoDao = new SaldoIBSDAO();
                    IntegranteCicloVO integrante = integranteDAO.getIntegranteCicloPorRol(idEquipo, idCiclo, rolIntegrante);
                    OrdenDePagoVO oPago = oPagoDAO.getOrdenPago(integrante.getIdCliente(), integrante.getIdSolicitud());
                    String nombreEquipo = grupoDao.getNombreGrupo(idEquipo);
                    String referencia = oPago.getReferencia();
                    String nuevaReferencia = "";
                    TransaccionesHelper transaccionHelper = new TransaccionesHelper();
                    EventoHelper eventoHelper = new EventoHelper();
                    CreditoCartVO credito = creditoDao.getCreditoClienteSol(idEquipo, idCiclo);
                    Calendar c1 = Calendar.getInstance();
                    for (int i = 0; i < referencia.length(); i++) {
                        int aux = referencia.charAt(i)-48;//Código ASCII, se convierte a entero
                        if(i==3){
                            aux+=1;
                        }
                        nuevaReferencia+=aux;                        
                    }
                    oPago.setReferencia(nuevaReferencia);
                    oPago.setEstatus(ClientesConstants.OP_DESEMBOLSADO);
                    oPago.setIdBanco(idBanco);
                    oPago.setMonto(monto);
                    oPago.setUsuario(request.getRemoteUser().toString());
                    oPagoDAO.addOrdenPago(oPago);
                    creditoDao.eliminaMontoCuenta(idEquipo, idCiclo);
                    saldoDao.eliminaSaldoBucket(idEquipo, idCiclo);
                    bitacora.registraEvento(oPago);
                    transaccionHelper.registraDevolucionSaldoFavor(credito, oPago, c1.getTime());
                    eventoHelper.registraDevolucionSaldoFavor(credito, oPago, c1.getTime());
                    notificaciones.add(new Notification(ClientesConstants.INFO_TYPE,"Se genera Orden de Pago correctamente"));
                    request.setAttribute("NOTIFICACIONES",notificaciones);
                    request.setAttribute("ORDENDEPAGO", oPago);
                    request.setAttribute("NOMBREEQUIPO", nombreEquipo);
                            
		}catch(ClientesDBException dbe){
			throw new CommandException(dbe.getMessage());
		}
		catch(Exception e){
			e.printStackTrace();
			throw new CommandException(e.getMessage());
		}
                return siguiente;
	}
}