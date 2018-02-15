			package com.sicap.clientes.commands;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.ClienteDAO;
import com.sicap.clientes.dao.TelefonoDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.ClienteVO;
import com.sicap.clientes.vo.GrupoVO;
import com.sicap.clientes.vo.ReporteCobranzaGrupalVO;
import com.sicap.clientes.vo.TelefonoVO;


public class CommandConsultaInformeCobranza implements Command{


	private String siguiente;


	public CommandConsultaInformeCobranza(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {

		//Notification notificaciones[] = new Notification[1];
		GrupoVO grupo = new GrupoVO();
		ReporteCobranzaGrupalVO reporteCobranza = new ReporteCobranzaGrupalVO();
		HttpSession session = request.getSession();
		CicloGrupalVO ciclo = new CicloGrupalVO();
		grupo = (GrupoVO)session.getAttribute("GRUPO");
		try{
			int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
			int idCliente = HTMLHelper.getParameterInt(request, "idCliente");
			int idAlerta = HTMLHelper.getParameterInt(request, "idAlerta");
			int tipoOperacion = HTMLHelper.getParameterInt(request, "tipoOperacionCliente");
			ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
			
			if(ciclo.eventosDePago[idAlerta]!=null)
				reporteCobranza = GrupoUtil.getReporteIntegrante(ciclo.eventosDePago[idAlerta], idCliente);
			
			//Obtiene toda la info de cliente
			reporteCobranza.direccion = ClientesUtil.getDomicilio(idCliente);
			
			ClienteVO cliente = new ClienteDAO().getCliente(idCliente);
			TelefonoVO[] telefonos = new TelefonoDAO().getTelefonos(idCliente, 1);
			String telefono = "";
			for(int a=0;telefonos!=null && a<telefonos.length; a++){
				telefono +=telefonos[a].numeroTelefono;
				int temp=a+1;
				if(temp<telefonos.length)
					telefono +=" : ";
			}
			
			reporteCobranza.telefonos = telefono;
			
			if(tipoOperacion==1){
				//Opcion Consultar cuestionario
				
			}else if(tipoOperacion==2){
				//Opcion Agregar cuestionario
				reporteCobranza.numCliente = idCliente;
				reporteCobranza.nombreCliente = cliente.nombreCompleto;
				reporteCobranza.comentarios = "";
			}
		
		request.setAttribute("IDALERTA", idAlerta);	
		request.setAttribute("CLIENTE", cliente);	
		request.setAttribute("OPERACION", tipoOperacion);	
		request.setAttribute("REPORTE_COBRANZA", reporteCobranza);		
		request.setAttribute("CICLO_EVENTOS_PAGO", ciclo);
			//Actualiza el cliente en sesion
			//session.setAttribute("CLIENTE", cliente);
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
