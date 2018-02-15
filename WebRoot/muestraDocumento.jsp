<%@ page contentType="text/html; charset=iso-8859-1" language="java" errorPage="error.jsp" %><jsp:useBean id="mySmartUpload" scope="page" class="com.jspsmart.upload.SmartUpload" /><%@ page import="com.sicap.clientes.vo.*"%><%@ page import="com.sicap.clientes.helpers.*"%><%@ page import="com.sicap.clientes.util.*"%><%@page import="java.io.FileInputStream"%><%String tipo = null;int semDisp = 0;
    tipo = request.getParameter("tipo");
    if (tipo == null) {
        mySmartUpload.initialize(pageContext);
        mySmartUpload.upload();
        tipo = mySmartUpload.getRequest().getParameter("tipo");
    }
    System.out.println("tipo:"+tipo);
    //GrupoUtil.actualizaGrupoEnSesion(session);

    if (tipo.equals("CONTRATO")) {
        int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
        GrupoVO grupo = (GrupoVO) session.getAttribute("GRUPO");
        CicloGrupalVO ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"ContratoComunal.pdf\"");
        response.setHeader("cache-control", "no-cache");
        GeneraContratoHelper contrato = new GeneraContratoHelper();
        contrato.doContratoComunal(response, grupo, ciclo);
    } else if (tipo.equals("CARATULA")) {
        int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
        GrupoVO grupo = (GrupoVO) session.getAttribute("GRUPO");
        CicloGrupalVO ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"CaratulaContrato.pdf\"");
        response.setHeader("cache-control", "no-cache");
        GeneraContratoHelper caratula = new GeneraContratoHelper();
        caratula.doCaratulaComunal(response, grupo, ciclo);
    } else if (tipo.equals("AVISOPRIVACIDAD")) {
        int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
        GrupoVO grupo = (GrupoVO) session.getAttribute("GRUPO");
        CicloGrupalVO ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"AvisoPrivacidad.pdf\"");
        response.setHeader("cache-control", "no-cache");
        GeneraDocumentosHelper avisoPrivacidad = new GeneraDocumentosHelper();
        avisoPrivacidad.doAvisoPrivacidad(response, grupo, ciclo);
    } else if (tipo.contains("_") && tipo.substring(0,9).equals("ORDENPAGO")) {
        int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
        GrupoVO grupo = (GrupoVO) session.getAttribute("GRUPO");
        CicloGrupalVO ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"OrdenPagoComunal.pdf\"");
        response.setHeader("cache-control", "no-cache");
        GeneraContratoHelper ordenPago = new GeneraContratoHelper();
        semDisp = Integer.parseInt(tipo.substring(tipo.length()-1, tipo.length()));
        ordenPago.doOrdenPagoComunal(response, grupo, ciclo, semDisp);
    } else if (tipo.equals("ORDENPAGOSEGURO")) {
        int idCliente = HTMLHelper.getParameterInt(request, "idCliente");
        int idSolicitud = HTMLHelper.getParameterInt(request, "idSolicitud");
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"OrdenPagoSeguro.pdf\"");
        response.setHeader("cache-control", "no-cache");
        GeneraContratoHelper ordenPago = new GeneraContratoHelper();
        ordenPago.doOrdenPagoSeguros(response, idCliente, idSolicitud);    
    } else if (tipo.equals("ORDENPAGOSALDOFAVOR")) {
        String referencia = HTMLHelper.getParameterString(request, "referencia");
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"OrdenPagoSaldoFavor.pdf\"");
        response.setHeader("cache-control", "no-cache");
        GeneraContratoHelper ordenPago = new GeneraContratoHelper();
        ordenPago.doOrdenPagoReferencia(response, referencia);    
    } else if (tipo.equals("ORDENPAGOINDIVIDUAL")) {
        String referencia = HTMLHelper.getParameterString(request, "referencia");
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"OrdenPago.pdf\"");
        response.setHeader("cache-control", "no-cache");
        GeneraContratoHelper ordenPago = new GeneraContratoHelper();
        ordenPago.doOrdenPagoReferencia(response, referencia);    
    } else if (tipo.equals("HOJARESUMEN")) {
        int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
        GrupoVO grupo = (GrupoVO) session.getAttribute("GRUPO");
        CicloGrupalVO ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"HojaResumenGrupo.pdf\"");
        response.setHeader("cache-control", "no-cache");
        GeneraContratoHelper contrato = new GeneraContratoHelper();
        contrato.generaHojaResumen(response, grupo, ciclo);
    } else if (tipo.equals("VIVIENDA")) {
        StringBuffer output = new StringBuffer();
        String respuesta = (String) session.getAttribute(tipo);
        output.append(respuesta);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"ContratoComunal.pdf\"");
        response.setHeader("cache-control", "no-cache");
    /*} else if (tipo.equals("ORDENPAGO")) {
        response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
        response.setHeader("Pragma", "no-cache"); //HTTP 1.0
        response.setDateHeader("Expires", 0); //prevents caching at the proxy server
        StringBuffer output = new StringBuffer();
        String respuesta = (String) session.getAttribute(tipo);
        output.append(respuesta);
        response.setContentType("application/rtf");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"OrdenDePago.rtf\"");
        response.setHeader("cache-control", "no-cache");
        out.println(output.toString());*/
    } else if (tipo.equals("FICHASEGURO") || tipo.equals("FICHAGARANTIA") || tipo.equals("DOCOFICIALES") || tipo.equals("FICHASEGUROINTER") || tipo.equals("FICHAGARANTIAINTER") || tipo.equals("DOCOFICIALESINTER")
            || tipo.indexOf("DOCADICIONAL") > -1 || tipo.indexOf("DOCLEGALADICIONAL") > -1
            ) {
        String nFile = "";
        String tipoDoc = "\\DocumentacionGrupal\\";
        if (tipo.equals("FICHASEGURO")) {
            nFile = HTMLHelper.getParameterString(request, "fileSeguro");
        } else if (tipo.equals("FICHAGARANTIA")) {
            nFile = HTMLHelper.getParameterString(request, "fileGarantia");
        } else if (tipo.equals("DOCOFICIALES")) {
            nFile = HTMLHelper.getParameterString(request, "fileDocumento");
        } else if (tipo.equals("FICHASEGUROINTER")) {
            nFile = HTMLHelper.getParameterString(request, "fileSegurosInter");
        } else if (tipo.equals("FICHAGARANTIAINTER")) {
            nFile = HTMLHelper.getParameterString(request, "fileGarantiaInter");
        } else if (tipo.equals("DOCOFICIALESINTER")) {
            nFile = HTMLHelper.getParameterString(request, "fileDocumentoInter");
        }else if (tipo.indexOf("DOCADICIONAL") > -1) {
            String copyTipo = new String(tipo);
            copyTipo=copyTipo.replaceAll("\\D+","");
            System.out.println("copyTipo:"+copyTipo);
            nFile = HTMLHelper.getParameterString(request, "fileDocAdicional"+copyTipo);  
        }else if (tipo.indexOf("DOCLEGALADICIONAL") > -1) {
            String copyTipo = new String(tipo);
            copyTipo=copyTipo.replaceAll("\\D+","");
            System.out.println("copyTipo:"+copyTipo);
            nFile = HTMLHelper.getParameterString(request, "fileDocLegalAdicional"+copyTipo);  
        }
        
        System.out.println("nFile:"+nFile);
        
        FileInputStream archivo = new FileInputStream(ClientesConstants.RUTA_BASE_ARCHIVOS + tipoDoc + HTMLHelper.getParameterInt(request, "idGrupo") + "\\" + HTMLHelper.getParameterInt(request, "idCiclo") + "\\" + nFile);
        int longitud = archivo.available();
        byte[] datos = new byte[longitud];
        archivo.read(datos);
        archivo.close();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + nFile);
        ServletOutputStream ouputStream = response.getOutputStream();
        ouputStream.write(datos);
        ouputStream.flush();
        ouputStream.close();
    }
    else if (tipo.contains("PAGAREGRUPAL")) {
        int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
        GrupoVO grupo = (GrupoVO) session.getAttribute("GRUPO");
        CicloGrupalVO ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition","attachment; filename=\"PagareGrupal.pdf\"");
        response.setHeader("cache-control", "no-cache");
        GeneraPagareHelper pagare = new GeneraPagareHelper();
        semDisp = Integer.parseInt(tipo.substring(tipo.length()-1, tipo.length()));
        pagare.doPagareGrupalPDF(request, response, grupo, ciclo, semDisp);
    }
    else if (tipo.equals("LISTADEASISTENCIA")) {
        int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
        GrupoVO grupo = (GrupoVO) session.getAttribute("GRUPO");
        CicloGrupalVO ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition","attachment; filename=\"ListaDeAsistencia.pdf\"");
        response.setHeader("cache-control", "no-cache");
        GeneraDocumentosHelper listaAsistencia = new GeneraDocumentosHelper();
        listaAsistencia.dolistaAsistenciaPDF(request, response, grupo, ciclo);
    }
    else if (tipo.equals("CONTROLDEPAGOS")) {
        int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
        GrupoVO grupo = (GrupoVO) session.getAttribute("GRUPO");
        CicloGrupalVO ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition","attachment; filename=\"ControlDePagos.pdf\"");
        response.setHeader("cache-control", "no-cache");
        GeneraDocumentosHelper controlPagos = new GeneraDocumentosHelper();
        controlPagos.doControlPagosPDF(request, response, grupo, ciclo);
    } else if (tipo.contains("PAGAREINDIVIDUAL")) {
        int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
        GrupoVO grupo = (GrupoVO) session.getAttribute("GRUPO");
        CicloGrupalVO ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition","attachment; filename=\"PagareIndividual.pdf\"");
        response.setHeader("cache-control", "no-cache");
        GeneraPagareHelper pagare = new GeneraPagareHelper();
        semDisp = Integer.parseInt(tipo.substring(tipo.length()-1, tipo.length()));
        pagare.doPagareIndividualPDF(request, response, grupo, ciclo,semDisp);
    } else if (tipo.equals("LAYOUTODP") || tipo.equals("LAYOUTFONDEOT") || tipo.equals("LAYOUTPERSONAT")) {
        String nFile = (String)request.getAttribute("FILENAME"); 
        String tipoDoc = "";
        if (tipo.equals("LAYOUTODP")) {
            tipoDoc = "\\EnvioOrdenesDePago\\";
        } else if (tipo.equals("LAYOUTFONDEOT")) {
            tipoDoc = "\\EnvioTarjetas\\";
        } else if (tipo.equals("LAYOUTPERSONAT")) {
            tipoDoc = "\\EnvioTarjetas\\";
        }
        FileInputStream archivo = new FileInputStream(ClientesConstants.RUTA_BASE_ARCHIVOS + tipoDoc + "\\" + nFile);
        int longitud = archivo.available();
        byte[] datos = new byte[longitud];
        archivo.read(datos);
        archivo.close();
        response.setContentType("text/plain");
        response.setHeader("Content-Disposition", "attachment;filename=" + nFile);
        response.setHeader("cache-control", "no-cache");
        ServletOutputStream ouputStream = response.getOutputStream();
        ouputStream.write(datos);
        ouputStream.flush();
        ouputStream.close();
    } else if (tipo.contains("ADDENDUM")){
        int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
        GrupoVO grupo = (GrupoVO) session.getAttribute("GRUPO");
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"Addendum.pdf\"");
        response.setHeader("cache-control", "no-cache");
        GeneraContratoHelper contrato = new GeneraContratoHelper();  
        semDisp = Integer.parseInt(tipo.substring(tipo.length()-1, tipo.length()));
        contrato.doAddendumIC(response, grupo, idCiclo,semDisp);
        
    }else if (tipo.equals("REGLAMENTO")) {
        String nFile = "";
        String tipoDoc = "\\Contratos\\Comunal\\REGLAMENTO_INTERNO_CONTIGO.pdf";
        FileInputStream archivo = new FileInputStream(ClientesConstants.RUTA_BASE_ARCHIVOS + tipoDoc);
        int longitud = archivo.available();
        byte[] datos = new byte[longitud];
        archivo.read(datos);
        archivo.close();
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=" + "REGLAMENTO_INTERNO_CONTIGO.pdf");
        ServletOutputStream ouputStream = response.getOutputStream();
        ouputStream.write(datos);
        ouputStream.flush();
        ouputStream.close();
    }else if (tipo.contains("_") && tipo.indexOf("ODPADICIONAL")> -1) {
        int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
        int semAdicional = Integer.parseInt(tipo.substring(tipo.indexOf("_")+1, tipo.length()));
        GrupoVO grupo = (GrupoVO) session.getAttribute("GRUPO");
        CicloGrupalVO ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"OrdenPagoAdicional_Sem"+String.valueOf(semAdicional)+".pdf\"");
        response.setHeader("cache-control", "no-cache");
        GeneraContratoHelper ordenPago = new GeneraContratoHelper();
        
        ordenPago.doOrdenPagoAdicional(response, grupo, ciclo, semAdicional);
    } else if (tipo.contains("_") && tipo.indexOf("PAGINDADICIONAL")> -1) {
        int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
        int semAdicional = Integer.parseInt(tipo.substring(tipo.indexOf("_")+1, tipo.length()));
        GrupoVO grupo = (GrupoVO) session.getAttribute("GRUPO");
        CicloGrupalVO ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition","attachment; filename=\"PagareIndividual_Adicional_Sem"+String.valueOf(semAdicional)+".pdf\"");
        response.setHeader("cache-control", "no-cache");
        GeneraPagareHelper pagare = new GeneraPagareHelper();
        
        pagare.doPagareIndividualAdicionalPDF(request, response, grupo, ciclo,semAdicional);
    }else if (tipo.contains("_") && tipo.indexOf("PAGGRUPALADICIONAL")> -1 ) {
        int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
        GrupoVO grupo = (GrupoVO) session.getAttribute("GRUPO");
        CicloGrupalVO ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
        int semAdicional = Integer.parseInt(tipo.substring(tipo.indexOf("_")+1, tipo.length()));
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition","attachment; filename=\"PagGrup_Adicional_Sem"+String.valueOf(semAdicional)+".pdf\"");
        response.setHeader("cache-control", "no-cache");
        GeneraPagareHelper pagare = new GeneraPagareHelper();
        semDisp = Integer.parseInt(tipo.substring(tipo.length()-1, tipo.length()));
        pagare.doPagareGrupalAdicionalPDF(request, response, grupo, ciclo, semAdicional);
    }else if (tipo.equals("CARTARENOVACION")) {
        int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
        GrupoVO grupo = (GrupoVO) session.getAttribute("GRUPO");
        CicloGrupalVO ciclo = GrupoUtil.getCiclo(grupo.ciclos, idCiclo);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"CartaRenovacion.pdf\"");
        response.setHeader("cache-control", "no-cache");
        GeneraDocumentosHelper cartaRenovacion = new GeneraDocumentosHelper();
        cartaRenovacion.doCartaRenovacionPDF(request, response, grupo, ciclo);
    }else if ( tipo.contains("_") && tipo.indexOf ("ADENDUMADICIONAL") > -1 ){
        int idCiclo = HTMLHelper.getParameterInt(request, "idCiclo");
        GrupoVO grupo = (GrupoVO) session.getAttribute("GRUPO");
        int semAdicional = Integer.parseInt(tipo.substring(tipo.indexOf("_")+1, tipo.length()));
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "attachment; filename=\"AddendumAdicionalSem"+String.valueOf(semAdicional)+".pdf\"");
        response.setHeader("cache-control", "no-cache");
        GeneraContratoHelper contrato = new GeneraContratoHelper();  
        semDisp = Integer.parseInt(tipo.substring(tipo.length()-1, tipo.length()));
        contrato.doAddendumAdicional(response, grupo, idCiclo,semAdicional);
        
    }
    
    /* else {
        response.setHeader("Cache-Control", "no-cache"); //HTTP 1.1
        response.setHeader("Pragma", "no-cache"); //HTTP 1.0
        response.setDateHeader("Expires", 0); //prevents caching at the proxy server
        StringBuffer output = new StringBuffer();
        String respuesta = (String) session.getAttribute(tipo);
        output.append(respuesta);
        response.setContentType("application/rtf");
        if (tipo.equals("PAGARE")) {
            response.setHeader("Content-Disposition",
                    "attachment; filename=\"PagareFincontigo.rtf\"");
        }
        if (tipo.equals("PAGAREINDIVIDUAL")) {
            response.setHeader("Content-Disposition",
                    "attachment; filename=\"PagareGrupalFinContigo.rtf\"");
        }
        response.setHeader("cache-control", "no-cache");
        out.println(output.toString());
    }*/
    %>
