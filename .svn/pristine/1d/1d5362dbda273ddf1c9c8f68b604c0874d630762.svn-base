package com.sicap.clientes.commands;

import java.sql.Connection;
import java.sql.SQLException;

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

public class CommandAltaSucursal implements Command {

    private String siguiente;

    public CommandAltaSucursal(String siguiente) {
        this.siguiente = siguiente;
    }

    @SuppressWarnings("null")
    public String execute(HttpServletRequest request) throws CommandException {
        Notification notificaciones[] = new Notification[1];
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
            int region = HTMLHelper.getParameterInt(request, "region");
            coloniaVO = colDAO.getColonia(colonia);
            int estado = coloniaVO.idEstado;
            int municipio = coloniaVO.idMunicipio;
            int codigo = HTMLHelper.getParameterInt(request, "codigo");


            System.out.println("La Sucursal es: " + sucursal);
            System.out.println("el municipio es: " + municipio);
            System.out.println("el estado es: " + estado);
            System.out.println("El identificador es: " + identificador);
            System.out.println("El representante es: " + representante);
            System.out.print(idBanco);


            sucursales = sucDAO.getSucursal(sucursal);
            //sucursalVO.idSucursal = sucursales.length - 2;
            System.out.println("Numero de datos: " + sucursales.length);
            //System.out.println("El nombre buscado es: "+ sucursales[0].nombre);
            if (sucursales.length != 0) {
                notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "La Sucursal ya existe.");
                request.setAttribute("NOTIFICACIONES", notificaciones);
                return siguiente;

            } else {

                sucursales = sucDAO.getSucursal();
                System.out.println("El numero id es: " + sucursales.length);
                conn = ConnectionManager.getMySQLConnection();
                conn.setAutoCommit(false);
                sucursalVO = new SucursalVO();
                if (sucursales.length - 1 < 33) {
                    sucursalVO.idSucursal = sucursales.length - 1;
                } else if (sucursales.length - 1 == 33) {
                    sucursalVO.idSucursal = 34;
                } else {
                    sucursalVO.idSucursal = sucursales.length;
                }
                sucursalVO.nombre = sucursal;
                sucursalVO.idRegion = region;
                sucursalVO.idPlaza = 1;
                sucursalVO.fronterizo = false;

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
                sucursalVO.codigo = codigo;

                sucDAO.addSucursal(sucursalVO, conn);
                //se toma el detalle de las variables estado, municipio, colonia
                //aunq no se guarden en la tabla c_sucursales se agregan al VO 
                //para pintar las variables en el regreso

                sucursalVO.estado = detalleEstado;
                sucursalVO.municipio = detalleMunicipio;
                sucursalVO.colonia = detalleColonia;
                sucursalVO.cp = detalleCp;


                conn.commit();
                notificaciones[0] = new Notification(ClientesConstants.INFO_TYPE, "La sucursal fue guardada correctamente");
            }

            request.setAttribute("NOTIFICACIONES", notificaciones);
            request.setAttribute("SUCURSAL", sucursalVO);
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
