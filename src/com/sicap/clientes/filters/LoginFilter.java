package com.sicap.clientes.filters;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.sicap.clientes.dao.InhabilesDAO;
import com.sicap.clientes.dao.UserSucursalDAO;
import com.sicap.clientes.dao.UsuarioDAO;
import com.sicap.clientes.exceptions.ClientesException;
import com.sicap.clientes.util.ClientesConstants;
import com.sicap.clientes.util.Notification;
import com.sicap.clientes.vo.UsuarioVO;
import org.apache.log4j.Logger;

public class LoginFilter implements Filter {

    FilterConfig config;
    private static Logger myLogger = Logger.getLogger(LoginFilter.class);

    public void init(FilterConfig config) {
        this.config = config;
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        Notification notificaciones[] = new Notification[1];
        UsuarioVO datosUsuario = null;
        UsuarioDAO userDao = new UsuarioDAO();
        String username = req.getRemoteUser();
        int expiracion = -1;
        int diassinacceso = -1;
        InhabilesDAO inHabilesDao = new InhabilesDAO();
        Calendar cal = Calendar.getInstance();

        try {
            myLogger.debug("Entrando a LoginFilter");
            if (session.getAttribute("USUARIO") == null) {
                Date[] fechas = inHabilesDao.getInhabiles(String.valueOf(cal.get(Calendar.YEAR)));
                session.setAttribute("INHABILES", fechas);
                datosUsuario = userDao.getDatosUsuario(username);
                datosUsuario.sucursales = new UserSucursalDAO().getSucursales(datosUsuario);
                datosUsuario.productos = userDao.getProductos(username);
                expiracion = datosUsuario.diasExpiracion;
                diassinacceso = datosUsuario.diasSinAccesar;
                datosUsuario.intentosFallidos = 0;
                datosUsuario.fechaAcceso = new java.sql.Date(System.currentTimeMillis());
                /**
                 * Validaciones con respecto a Días sin Accesar
                 */
                if (diassinacceso >= ClientesConstants.MAXIMO_DIAS_INACTIVIDAD) {
                    notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "El usuario se encuentra deshabilitado por exceder el máximo de días de inactividad");
                    //session.setAttribute("USUARIO", datosUsuario);
                    request.setAttribute("NOTIFICACIONES", notificaciones);
                    request.getRequestDispatcher("/loginMensajesError.jsp").forward(request, response);
                    return;
                } else if (datosUsuario.status.equals("I")) {
                    notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "El usuario se encuentra desactivado por exceder el n&uacute;mero de intentos permitido");
                    //session.setAttribute("USUARIO", datosUsuario);
                    request.setAttribute("NOTIFICACIONES", notificaciones);
                    request.getRequestDispatcher("/loginMensajesError.jsp").forward(request, response);
                    return;
                } else if (expiracion <= 0) {
                    if (request.getParameter("command") != null && request.getParameter("command").equals("actualizaUsuario")) {
                    } else {
                        notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "Tu contraseña ha expirado, es necesario que la actualices");
                        session.setAttribute("USUARIO_CAMBIO_PWD", datosUsuario);
                        request.setAttribute("NOTIFICACIONES", notificaciones);
                        session.setAttribute("SESION_INICIADA", new String("1"));
                        request.getRequestDispatcher("/cambioPasswordUsuario.jsp").forward(request, response);
                        return;
                    }
                } else if (expiracion >= 1 && expiracion <= ClientesConstants.DIAS_DESPLIEGUE_MENSAJE) {
                    session.setAttribute("USUARIO", datosUsuario);
                    notificaciones[0] = new Notification(ClientesConstants.ERROR_TYPE, "<a href=\"#\" onClick=\"cambioPasswordUsuario()\" class=\"ligaRojo\">Tu contraseña expira dentro de " + expiracion + " d&iacute;a(s). Haz clic aqui para cambiarla</a>");
                    request.setAttribute("NOTIFICACIONES", notificaciones);
                    userDao.updateUsuario(datosUsuario, username);
                    request.getRequestDispatcher("").forward(request, response);
                    return;
                } else {
                    userDao.updateUsuario(datosUsuario, username);
                    session.setAttribute("USUARIO", datosUsuario);
                    myLogger.debug("Contexto : " + req.getContextPath() + " Usuario: " + username);
                    res.sendRedirect(req.getContextPath());
                    return;
                }
            }
        } catch (ClientesException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            myLogger.error("ClientesException: ", e);
            //myLogger.error(e);
            throw new ServletException(e.getMessage());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            myLogger.error("Exception: ", e);
            throw new ServletException(e.getMessage());
        }
        chain.doFilter(request, response);
    }

    public void destroy() {
    }
}
