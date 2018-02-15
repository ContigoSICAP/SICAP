package com.sicap.clientes.commands;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.sicap.clientes.dao.ColoniaDAO;
import com.sicap.clientes.dao.SucursalDAO;
import com.sicap.clientes.dao.UserSucursalDAO;
import com.sicap.clientes.dao.UsuarioDAO;
import com.sicap.clientes.exceptions.ClientesDBException;
import com.sicap.clientes.exceptions.CommandException;
import com.sicap.clientes.helpers.HTMLHelper;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.ConnectionManager;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.util.SecurityUtil;
import com.sicap.clientes.vo.ColoniaVO;
import com.sicap.clientes.vo.SucursalVO;
import com.sicap.clientes.vo.UsuarioVO;

public class CommandActualizaSucursal implements Command {

    private String siguiente;

    public CommandActualizaSucursal(String siguiente) {
        this.siguiente = siguiente;
    }

    @SuppressWarnings("null")
    public String execute(HttpServletRequest request) throws CommandException {
        Notification notificaciones[] = new Notification[1];

        ArrayList<SucursalVO> aux = new ArrayList<SucursalVO>();
        SucursalDAO sucDAO = new SucursalDAO();
        Connection conn = null;
        SucursalVO sucursalVO = null;
        SucursalVO[] sucursales = null;
        ColoniaDAO colDAO = new ColoniaDAO();
        ColoniaVO coloniaVO = null;

        try {
            String sucursal = request.getParameter("sucursal");
            String numero = request.getParameter("numero");
            //	String cpostal = HTMLHelper.getParameterInt(request, "cp");
            int cpostal = HTMLHelper.getParameterInt(request, "cp");
            String detalleColonia = request.getParameter("colonia");
            //	String detalleCp = request.getParameter("cp");
            int detalleCp = HTMLHelper.getParameterInt(request, "cp");
            String detalleMunicipio = request.getParameter("municipio");
            String detalleEstado = request.getParameter("estado");
            int colonia = HTMLHelper.getParameterInt(request, "idColonia");
            String telefono = request.getParameter("telefono");
            String direccion = request.getParameter("calle");
            String identificador = request.getParameter("identificador");
            String representante = request.getParameter("representante");
            int idBanco = HTMLHelper.getParameterInt(request, "banco");
            int idSucursal = HTMLHelper.getParameterInt(request, "idsucursal");
            int estatus = HTMLHelper.getParameterInt(request, "estatus");
            int region = HTMLHelper.getParameterInt(request, "region");
            int codigo = HTMLHelper.getParameterInt(request, "codigo");

            /*System.out.println("La Sucursal es: " + sucursal);
            System.out.println("El identificador es: " + identificador);
            System.out.println("El cp es: " + detalleCp);
            System.out.println("Lo qeu nos da el request:"+request.getParameter("idColonia"));
            System.out.println("El colonia:" + colonia);
            System.out.println("Lo qeu nos da el request estado:"+request.getParameter("idEstado"));
            System.out.println("El representante es: " + representante);
            System.out.println("El id sucursal " + idSucursal);
            System.out.println(idBanco);*/

            coloniaVO = colDAO.getColonia(colonia);
            int estado = coloniaVO.idEstado;
            int municipio = coloniaVO.idMunicipio;

            //System.out.println("el municipio es: " + municipio);
            //System.out.println("el estado es: " + estado);
            
            sucursales = sucDAO.getSucursal(sucursal);
            //sucursalVO.idSucursal = sucursales.length - 2;
            System.out.println("Numero de datos: " + sucursales.length);
            //System.out.println("El nombre buscado es: "+ sucursales[0].nombre);

            if (sucursales.length == 0) {
                //	notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE,"La Sucursal ya existe.");
                //	request.setAttribute("NOTIFICACIONES", notificaciones);
                request.setAttribute("SUCURSAL", sucursales);
                return siguiente;
            } else {
                conn = ConnectionManager.getMySQLConnection();
                conn.setAutoCommit(false);
                sucursalVO = new SucursalVO();
                sucursalVO.idSucursal = idSucursal;
                sucursalVO.nombre = sucursal;
                sucursalVO.idRegion = region;
                sucursalVO.idPlaza = 1;
                sucursalVO.fronterizo = false;
                sucursalVO.estatus = estatus;
                sucursalVO.codigo = codigo;

                sucursalVO.idEstado = estado;
                sucursalVO.idMunicipio = municipio;
                sucursalVO.idColonia = colonia;
                sucursalVO.cp = cpostal;
                sucursalVO.numero = numero;
                sucursalVO.telefono = telefono;

                sucursalVO.direccion_calle = direccion;
                sucursalVO.identificador = identificador;
                sucursalVO.idBanco = idBanco;
                sucursalVO.representante = representante;
                sucDAO.updateSucursal(conn, sucursalVO);
                sucDAO.getCambioEstatus(conn, sucursalVO);

                sucursalVO.estado = detalleEstado;
                sucursalVO.municipio = detalleMunicipio;
                sucursalVO.colonia = detalleColonia;
                sucursalVO.cp = detalleCp;


                conn.commit();
                notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "La sucursal fue guardada correctamente");
                aux.add(sucursalVO);
                sucursales = new SucursalVO[aux.size()];
                for (int i = 0; i < sucursales.length; i++) {
                    sucursales[i] = (SucursalVO) aux.get(i);
                }
            }

            request.setAttribute("NOTIFICACIONES", notificaciones);
            request.setAttribute("SUCURSAL", sucursales);
        } catch (ClientesDBException dbe) {
            throw new CommandException(dbe.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            throw new CommandException(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException sqle) {
                throw new CommandException(sqle.getMessage());
            }
        }
        return siguiente;
    }
}
