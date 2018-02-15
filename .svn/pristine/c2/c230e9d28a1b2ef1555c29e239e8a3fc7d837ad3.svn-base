package com.sicap.clientes.commands;

import com.sicap.clientes.dao.BeneficiarioDAO;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import com.sicap.clientes.dao.OrdenDePagoDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.BitacoraUtil;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.OrdenDePagoVO;
import java.sql.Timestamp;
import java.util.ArrayList;

public class CommandGuardaOrdenSeguro implements Command{

	private String siguiente;

	public CommandGuardaOrdenSeguro(String siguiente) {
		this.siguiente = siguiente;
	}

	public String execute(HttpServletRequest request) throws CommandException {
		Vector<Notification> notificaciones = new Vector<Notification>();
                try{
                    java.util.Date date= new java.util.Date();
                    int idCliente = HTMLHelper.getParameterInt(request, "idCliente");
                    int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
                    int tipoBeneficiario = HTMLHelper.getParameterInt(request, "tipoBeneficiario");
                    int idBanco = HTMLHelper.getParameterInt(request, "bancoODP");
                    int idReferenciaSeguro = 3;
                    int estatusSeguroODP = ClientesConstants.OP_SEGURO;
                    boolean ordenSeguroGenerada = false;
                    float monto = HTMLHelper.getParameterFloat(request, "monto");
                    String nombreOrden = null;
                    OrdenDePagoDAO opagodao = new OrdenDePagoDAO();
                    BeneficiarioDAO beneficiarioDAO = new BeneficiarioDAO();
                    OrdenDePagoVO ordenPago =  opagodao.getODPSeguroGenerada(idCliente, idSolicitud);
                    if (ordenPago == null){
                        ordenPago = opagodao.getOrdenDePagoSeguro(idCliente, idSolicitud);
                    }
                    OrdenDePagoVO ordenSeguro =  new OrdenDePagoVO();
                    String referencia = ordenPago.getReferencia();
                    String referenciaSeguro = "";
                    BitacoraUtil bitacora = new BitacoraUtil(idCliente, request.getRemoteUser(), "CommandGuardaOrdenSeguro");
                    ArrayList<String> nombresBeneficiarios = new ArrayList<String>();
                    ArrayList<Integer> porcentajes = new ArrayList<Integer>();
                    ordenSeguro.setIdCliente(ordenPago.getIdCliente());
                    ordenSeguro.setIdSolicitud(ordenPago.getIdSolicitud());
                    ordenSeguro.setIdSucursal(ordenPago.getIdSucursal());
                    ordenSeguro.setIdBanco(idBanco);
                    ordenSeguro.setEstatus(estatusSeguroODP);
                    ordenSeguro.setUsuario(request.getRemoteUser().toString());
                    //ordenSeguro.setFechaCaptura(new Timestamp(date.getTime()));
                    //ordenSeguro.setFechaEnvio(new Timestamp(date.getTime()));
                    if (tipoBeneficiario==1||tipoBeneficiario==2||tipoBeneficiario==4){
                        if(tipoBeneficiario==1||tipoBeneficiario==2) {
                            nombreOrden = HTMLHelper.getParameterString(request, "nombreCompleto");
                        }
                        else if (tipoBeneficiario==4) {
                            nombreOrden = HTMLHelper.getParameterString(request, "otroBeneficiario");
                            request.setAttribute("OTROBENEFICIARIO", nombreOrden);
                        }
                        for(int i=0;i<referencia.length();i++){
                            if(i==3){
                                int aux = referencia.charAt(i)-48;//Código ASCII
                                if(aux>=3){
                                    referenciaSeguro+=Integer.toString(aux+1);
                                }
                                else{
                                    referenciaSeguro+=Integer.toString(idReferenciaSeguro);
                                }                                
                            }
                            else{
                                referenciaSeguro+=referencia.charAt(i);
                            }
                        }
                        ordenSeguro.setReferencia(referenciaSeguro);
                        ordenSeguro.setNombre(nombreOrden);
                        ordenSeguro.setMonto(monto);
                        opagodao.addOrdenPago(ordenSeguro);
                        ordenSeguroGenerada=true;
                        bitacora.registraEvento(ordenSeguro);
                        notificaciones.add(new Notification(ClientesConstants.INFO_TYPE,"Se genera Orden de Pago correctamente"));
                    }
                    else if (tipoBeneficiario==3) {
                        nombresBeneficiarios = beneficiarioDAO.getNombreBeneficiario(idCliente, idSolicitud);
                        porcentajes = beneficiarioDAO.getPorcentajes(idCliente, idSolicitud);
                        for(int i=0; i<nombresBeneficiarios.size(); i++){
                            nombreOrden = nombresBeneficiarios.get(i);
                            for(int j=0;j<referencia.length();j++){
                                if(j==3){
                                    int aux = referencia.charAt(j)-48;//Código ASCII
                                    if(aux>=3){
                                        referenciaSeguro+=Integer.toString(aux+i+1);
                                    }
                                    else {
                                        referenciaSeguro+=Integer.toString(idReferenciaSeguro+i);
                                    }
                                }
                                else{
                                    referenciaSeguro+=referencia.charAt(j);
                                }
                            }
                            ordenSeguro.setReferencia(referenciaSeguro);
                            referenciaSeguro="";
                            ordenSeguro.setMonto(porcentajes.get(i)*(monto/100));
                            ordenSeguro.setNombre(nombreOrden);
                            opagodao.addOrdenPago(ordenSeguro);
                            ordenSeguroGenerada=true;
                            bitacora.registraEvento(ordenSeguro);                            
                        }
                        notificaciones.add(new Notification(ClientesConstants.INFO_TYPE,"Se generan "+nombresBeneficiarios.size()+" Ordenes de Pago correctamente"));
                    }
                    request.setAttribute("NOTIFICACIONES",notificaciones);
                    request.setAttribute("ORDENDEPAGO", ordenPago);
                    request.setAttribute("MUESTRABENEFICIARIO", true);
                    request.setAttribute("IDBENEFICIARIO", tipoBeneficiario);
                    request.setAttribute("NOMBRESBENEFICIARIOS", nombresBeneficiarios);
                    request.setAttribute("ADVERTENCIAODPEXISTENTE", false);
                    request.setAttribute("PORCENTAJES", porcentajes);
                    request.setAttribute("MONTO", monto);
                    request.setAttribute("IMPRIMIRORDEN", ordenSeguroGenerada);
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