package com.sicap.clientes.commands;

import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.ArchivoAsociadoDAO;
import com.sicap.clientes.dao.CicloGrupalDAO;
import com.sicap.clientes.dao.DireccionGenericaDAO;
import com.sicap.clientes.dao.EventosPagosGrupalDAO;
import com.sicap.clientes.dao.GrupoDAO;
import com.sicap.clientes.dao.IntegranteCicloDAO;
import com.sicap.clientes.dao.ReferenciaGeneralDAO;
import com.sicap.clientes.dao.ReporteCobranzaGrupalDAO;
import com.sicap.clientes.dao.ReporteVisitaGrupalDAO;
import com.sicap.clientes.dao.SaldoIBSDAO;
import com.sicap.clientes.dao.SaldoT24DAO;
import com.sicap.clientes.dao.TablaAmortizacionDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.CatalogoHelper;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.helpers.IBSHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ClientesUtil;
import com.sicap.clientes.util.GrupoUtil;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.CicloGrupalVO;
import com.sicap.clientes.vo.GrupoVO;


public class CommandConsultaGrupoMonitor implements Command{


	private String siguiente;


	public CommandConsultaGrupoMonitor(String siguiente) {
		this.siguiente = siguiente;
	}


	public String execute(HttpServletRequest request) throws CommandException {
		
		GrupoVO grupo = new GrupoVO();
		HttpSession session = request.getSession();
		CicloGrupalVO ciclo = new CicloGrupalVO();
		Notification notificaciones[] = new Notification[1];
		int idGrupo = 0;
		int idCiclo = 0;
		String nombre = "";
		try{
			idGrupo = HTMLHelper.getParameterInt(request, "idGrupoDetallada");
			idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
			nombre = HTMLHelper.getParameterString(request, "nombre");
			TreeMap catComisionesGrupal = CatalogoHelper.getCatalogoComisiones(ClientesConstants.GRUPAL);
			if ( idGrupo!=0 ){
				grupo = new GrupoDAO().getGrupo(idGrupo);
				grupo.ciclos = new CicloGrupalDAO().getCiclos(grupo.idGrupo);
				for ( int i=0 ; grupo.ciclos!=null && i<grupo.ciclos.length ; i++ ){
                                System.out.println ("entra a ciclos");
					grupo.ciclos[i] = new CicloGrupalDAO().getCiclo(idGrupo, grupo.ciclos[i].idCiclo);
					grupo.ciclos[i].integrantes = new IntegranteCicloDAO().getIntegrantes(grupo.idGrupo, grupo.ciclos[i].idCiclo);
					for(int a=0; grupo.ciclos[i].integrantes!=null && a<grupo.ciclos[i].integrantes.length; a++)
						grupo.ciclos[i].integrantes[a].montoDesembolso = ClientesUtil.calculaMontoSinComision( grupo.ciclos[i].integrantes[a].monto, grupo.ciclos[i].integrantes[a].comision, catComisionesGrupal );
					grupo.ciclos[i].direccionReunion = new DireccionGenericaDAO().getDireccion(grupo.ciclos[i].idDireccionReunion);
					grupo.ciclos[i].direccionAlterna = new DireccionGenericaDAO().getDireccion(grupo.ciclos[i].idDireccionAlterna);
					grupo.ciclos[i].tablaAmortizacion = new TablaAmortizacionDAO().getElementos(grupo.idGrupo, grupo.ciclos[i].idCiclo, ClientesConstants.AMORTIZACION_GRUPAL);
					grupo.ciclos[i].referencia = new ReferenciaGeneralDAO().getReferencia(grupo.idGrupo, grupo.ciclos[i].idCiclo, 'G');
					grupo.ciclos[i].eventosDePago = new EventosPagosGrupalDAO().getEventosPagos(idGrupo, grupo.ciclos[i].idCiclo);
					grupo.ciclos[i].archivosAsociados = new ArchivoAsociadoDAO().getArchivos(idGrupo, grupo.ciclos[i].idCiclo, "G");
					if ( grupo.ciclos[i].idCreditoIBS != 0 || grupo.ciclos[i].idCuentaIBS != 0 )
						grupo.ciclos[i].saldo = new SaldoIBSDAO().getSaldo(grupo.idGrupo, grupo.ciclos[i].idCiclo, grupo.ciclos[i].referencia);
                                        else 
						grupo.ciclos[i].saldo = IBSHelper.getSaldosT24ToIBS(new SaldoT24DAO().getSaldosT24ByNumClienteSolicitudProducto(grupo.idGrupo, grupo.ciclos[i].idCiclo, ClientesConstants.GRUPAL));
                                      
					grupo.ciclos[i].estatusT24 = grupo.ciclos[i].saldo.getEstatus();
				}
			}else if ( idGrupo==0 ){
				grupo = (GrupoVO)session.getAttribute("GRUPO");
			}
			
			if(idCiclo!=0)
				ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
                        System.out.println("entra a ciclos 2");
			//ciclo.eventosDePago = new EventosPagosGrupalDAO().getEventosPagos(idGrupo, idCiclo);
			for(int i=0; ciclo.eventosDePago!=null && i<ciclo.eventosDePago.length; i++){
				ciclo.eventosDePago[i].reporteVisita = new ReporteVisitaGrupalDAO().getReporteVisitaGrupal(ciclo.eventosDePago[i].identificador);
				ciclo.eventosDePago[i].reporteCobranza = new ReporteCobranzaGrupalDAO().getReportesCobranza(ciclo.eventosDePago[i].identificador);
			}
			if(ciclo.referencia != null)
				ciclo.fechaUltimoPago = new EventosPagosGrupalDAO().getFechaUltimoPago(ciclo.referencia);
			//ciclo.archivosAsociados = new ArchivoAsociadoDAO().getArchivos(idGrupo, idCiclo, "G");
			if ( ciclo.eventosDePago!=null && ciclo.eventosDePago.length>0 ){
				request.setAttribute("NOMBRE", nombre);
			}else{
				notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "No se encontraron datos");
				request.setAttribute("NOTIFICACIONES",notificaciones);
			}
			if ( ciclo.idCreditoIBS != 0 ){
				ciclo.saldo = new SaldoIBSDAO().getSaldo(ciclo.idGrupo, ciclo.idCiclo, ciclo.referencia);
                                ciclo.estatusT24 = ciclo.saldo.getEstatus();
                        }        
			ciclo.archivosAsociados = new ArchivoAsociadoDAO().getArchivos(ciclo.idGrupo, ciclo.idCiclo, "G");
			
                        GrupoUtil.setCiclo(grupo, ciclo, idCiclo);
                        
                        
			request.setAttribute("CICLO_EVENTOS_PAGO", ciclo);
			session.setAttribute("GRUPO", grupo);
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
