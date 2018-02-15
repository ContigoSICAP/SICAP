package com.sicap.clientes.commands;

import com.sicap.clientes.dao.BeneficiarioDAO;
import com.sicap.clientes.dao.ClienteDAO;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import com.sicap.clientes.dao.OrdenDePagoDAO;
import com.sicap.clientes.dao.SegurosDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.OrdenDePagoVO;
import java.util.ArrayList;

public class CommandConsultaOrdenPagoSeguro implements Command{

	private String siguiente;

	public CommandConsultaOrdenPagoSeguro(String siguiente) {
		this.siguiente = siguiente;
	}

	public String execute(HttpServletRequest request) throws CommandException {
		Vector<Notification> notificaciones = new Vector<Notification>();

		try{
                    int idCliente = HTMLHelper.getParameterInt(request, "idCliente");
                    int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
                    float monto = HTMLHelper.getParameterFloat(request, "monto");
                    int generarNuevaExistente = HTMLHelper.getParameterInt(request, "confirmaGenerarNuevaODP");
                    int nuevaBusqueda = HTMLHelper.getParameterInt(request, "nuevaConsulta");
                    int idBeneficiario = 0;
                    int contratoSeguro = 0;
                    BeneficiarioDAO beneficiarioDAO = new BeneficiarioDAO();
                    ClienteDAO clienteDAO = new ClienteDAO();
                    OrdenDePagoDAO opagoDAO = new OrdenDePagoDAO();
                    SegurosDAO seguroDAO = new SegurosDAO();
                    ArrayList<String> nombresBeneficiarios = new ArrayList<String>();
                    ArrayList<Integer> porcentajes = new ArrayList<Integer>();
                    OrdenDePagoVO ordenDePago =  opagoDAO.getOrdenDePagoSeguro(idCliente, idSolicitud);
                    contratoSeguro = seguroDAO.contratoSeguro(idCliente, idSolicitud);
                    boolean muestraBeneficiarios=false;
                    boolean advertenciaExiste=false;
                    if( ordenDePago == null ){
                        notificaciones.add(new Notification(ClientesConstants.INFO_TYPE,"No se encontraron Registros con los Datos Proporcionados"));
                    }
                    else if(contratoSeguro==2||contratoSeguro==0){
                        notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE,"El cliente "+clienteDAO.getNombreCliente(idCliente)+" no tiene contratado un seguro en la solicitud "+idSolicitud));
                        ordenDePago=null;
                    }
                    else if (nuevaBusqueda==1){
                    if((ordenDePago.getEstatus()==9||ordenDePago.getEstatus()==10||ordenDePago.getEstatus()==11)){
                        if(!request.isUserInRole("SUPERADM_SEGUROS")){
                            notificaciones.add(new Notification(ClientesConstants.ERROR_TYPE,"El cliente "+clienteDAO.getNombreCliente(idCliente)+" ya cuenta con una Orden de Pago para seguro en la solicitud "+idSolicitud));
                            ordenDePago=null;
                        }
                        else{
                            advertenciaExiste=true;
                        }
                    }
                    }
                    else if (generarNuevaExistente==1){
                        idBeneficiario = HTMLHelper.getParameterInt(request, "tipoBeneficiario");
                        nombresBeneficiarios = beneficiarioDAO.getNombreBeneficiario(idCliente, idSolicitud);
                        porcentajes = beneficiarioDAO.getPorcentajes(idCliente, idSolicitud);
                        if (idBeneficiario!=0){
                            muestraBeneficiarios=true;
                        }                        
                    }
                    else {
                        ordenDePago=null;
                    }
                    System.out.println("CONFIRMA: "+generarNuevaExistente);
                    request.setAttribute("NOTIFICACIONES",notificaciones);
                    request.setAttribute("ORDENDEPAGO", ordenDePago);
                    request.setAttribute("MUESTRABENEFICIARIO", muestraBeneficiarios);
                    request.setAttribute("ADVERTENCIAODPEXISTENTE", advertenciaExiste);
                    request.setAttribute("IDBENEFICIARIO", idBeneficiario);
                    request.setAttribute("NOMBRESBENEFICIARIOS", nombresBeneficiarios);
                    request.setAttribute("PORCENTAJES", porcentajes);
                    request.setAttribute("MONTO", monto);
                    request.setAttribute("IMPRIMIRORDEN", false);
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