package com.sicap.clientes.helpers;

import java.sql.Connection;
import java.util.Calendar;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.ChequesDAO;
import com.sicap.clientes.dao.LoteChequesDAO;
import com.sicap.clientes.dao.OrdenDePagoDAO;
import com.sicap.clientes.dao.TarjetaDAO;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Convertidor;
import com.sicap.clientes.vo.ChequeVO;
import com.sicap.clientes.vo.LoteChequesVO;
import com.sicap.clientes.vo.OrdenDePagoVO;
import com.sicap.clientes.vo.TarjetasVO;

public class ChequerasHelper {

	//Recibe el cliente de la session como parametro, agrega la información capturada en el formulario
    //retornando el objeto actualizado
    public static LoteChequesVO getVO(LoteChequesVO lote, HttpServletRequest request) throws Exception {
        if (lote == null) {
            lote = new LoteChequesVO();
        }
        lote.idSucursal = HTMLHelper.getParameterInt(request, "sucursal");
        lote.idBanco = HTMLHelper.getParameterInt(request, "banco");
        lote.numChequeIni = HTMLHelper.getParameterInt(request, "chequeinicial");
        lote.numChequeFin = HTMLHelper.getParameterInt(request, "chequefinal");
        return lote;

    }

    public static String asignaCheque(Connection conn, int idSucursal, int idCliente, int idSolicitud, int idGrupo, int idCiclo) {
        //Obtiene número de cheque consecutivo y lo asigna al crédito
        int numLote = 0;
        int numCheque = 0;

        LoteChequesDAO loteDAO = new LoteChequesDAO();
        ChequesDAO chequeDAO = new ChequesDAO();
        ChequeVO cheque = new ChequeVO();
        Calendar cal = Calendar.getInstance();

        try {
            LoteChequesVO[] lotes = loteDAO.getLotesCheques(idSucursal);
            if (lotes != null && lotes.length > 0) {
                numLote = lotes[0].idLote;
                numCheque = new ChequesDAO().getNumCheque(conn, numLote);
                if (numCheque == lotes[0].numChequeFin) {
                    loteDAO.updateLoteCheques(numLote);
                }
                if (numCheque == 0) {
                    numCheque = lotes[0].numChequeIni;
                }

                cheque.numCheque = numCheque;
                cheque.numLote = numLote;
                cheque.numCliente = idCliente;
                cheque.numSolicitud = idSolicitud;
                cheque.numGrupo = idGrupo;
                cheque.numCiclo = idCiclo;
                cheque.estatus = ClientesConstants.CHEQUE_ASIGNADO;
                cheque.fechaAsignacion = Convertidor.toSqlDate(cal.getTime());

                chequeDAO.updateCheque(conn, cheque);
                //chequeDAO.addCheque(conn, numCheque, numLote, idCliente, idSolicitud, idGrupo, idCiclo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return String.valueOf(numCheque);
    }

    public static String asignaOrdenDePago(Connection conn, OrdenDePagoVO oPago) {
        try {
            OrdenDePagoDAO oPagoDao = new OrdenDePagoDAO();
            oPagoDao.addOrdenPago(conn, oPago);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return oPago.getReferencia();
    }

    public static void asignaTarjeta(Connection conn, TarjetasVO tarjeta, TarjetaDAO tarjetaDAO) {

        int banco = 0;
        try {
            banco = tarjetaDAO.getBancoTarjeta(tarjeta.getIdCliente(), tarjeta.getTarjeta(), conn);
            tarjeta.setBanco(banco);
            tarjetaDAO.insertFondeoTarjeta(tarjeta, conn);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
