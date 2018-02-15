package com.sicap.clientes.commands;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.PagoDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.SaldoT24DAO;
import com.sicap.clientes.dao.cartera.PagoManualDAO;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.IBSHelper;
import com.sicap.clientes.helpers.ibs.BucketHelperIBS;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.GrupoVO;
import org.apache.log4j.Logger;

public class CommandConsultaEstadoCuentaGrupal implements Command{
	
	private String siguiente;
        private static Logger myLogger = Logger.getLogger(CommandConsultaEstadoCuentaGrupal.class);
	
	public CommandConsultaEstadoCuentaGrupal(String siguiente){
		this.siguiente = siguiente;
	}
	
	public String execute(HttpServletRequest request) throws CommandException{
		try{
			HttpSession session = request.getSession();
			GrupoVO grupo = (GrupoVO)session.getAttribute("GRUPO");
			Vector<Notification> notificaciones = new Vector<Notification>();
			
			int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
			CicloGrupalVO ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);

			if( ciclo!=null && (ciclo.saldo==null || ciclo.pagosConcentradora==null ) ){
//				if ( ciclo.idCreditoIBS != 0 && ciclo.idCuentaIBS != 0 ){
// Solo se verifica el credito, no la cuenta la cual no se usa JBL SEP/10				
				if ( ciclo.idCreditoIBS != 0 ){
					ciclo.saldo = new SaldoIBSDAO().getSaldo(grupo.idGrupo, ciclo.idCiclo, ciclo.referencia);
// Se retiran los pagos de IBS, JBL SEP/10					
//					ciclo.pagosIBS = new BucketHelperIBS().consultaEstadoBucket(ciclo.idCreditoIBS, notificaciones);
					ciclo.pagos = new PagoDAO().getFechasPagosCliente(ciclo.referencia);

				}else
					ciclo.saldo = IBSHelper.getSaldosT24ToIBS(new SaldoT24DAO().getSaldosT24ByNumClienteCiclo(grupo.idGrupo, idCiclo, ciclo.referencia));
				ciclo.pagosConcentradora = new PagoManualDAO().getPagoConcentradora(grupo.idGrupo, idCiclo, ciclo.referencia);
			}
//			else{
//				notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE,"No se tiene información del grupo.");
//				request.setAttribute("NOTIFICACIONES", notificaciones);
//			}
			GrupoUtil.setCiclo(grupo, ciclo, ciclo.idCiclo);
			session.setAttribute("GRUPO", grupo);
		}catch(Exception e){
                        myLogger.error("Exception", e);
			throw new CommandException(e.getMessage());
		}
		return siguiente;
	}
}
