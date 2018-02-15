package com.sicap.clientes.vo;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public class IntegranteCicloVO implements Serializable, Cloneable {

    public int idGrupo;
    public int idCiclo;
    public int idCliente;
    public int idSolicitud;
    public String numCheque;
    public OrdenDePagoVO ordenPago;
    public int desembolsado;
    public String nombre;
    public double montoDesembolso;
    public double monto;
    public double montoRefinanciado;
    public double montoAdicional;
    public int plazo;
    public int idTasa;
    public int estatus;
    public int rol;
    public int comision;
    public Timestamp fechaCaptura;
    public int calificacion;
    public int aceptaRegular;
    public double primaSeguro;
    public String grupo;
    public Date fecha;
    public Date fechaDesembAdicional;
    public int seguro;
    public int empleo;
    public int tipo;
    public int tipo_adicional;
    public int scoreFico;
    public int medioCobro;
    public int medioDisp;
    public TarjetasVO tarjetaCobro;
    public int consultaCC;
    public int calificacionAutomatica;
    public int esNuevo;
    public int esInterciclo;
    public int semDisp;
    public int RenovacionDoc;
    public int DocCompletos;
    public int idPorcentajeAdicional;
    public double montoConSeguro;
    /**
     * JECB 01/10/2017
     * Atributo empleado para referenciar el sexo del integrante ciclo
     */
    public int sexoCliente;
    /**
     * JECB 01/10/2017
     * Atributo empleado para referencias el númeto total de 
     * solicitudes que tiene un integrante con estatus desembolsado
     */
    public int totalSolicitudesDesembolsados;
    /** 
     * JECB 01/10/2017
     * Atributo empleado para referenciar si un integrante de un ciclo 
     * cuenta contratado con un seguro de vida 
     */
    public int contratacionSeguro;
    /**
     * Variable temporal que indica si el integrante 
     * cuenta con Monto con seguro financiado.
     */
    public double montoConSeguroTemp;    

    /**
     * Tipo seguro.
     */
    public int tipoSeguro;
    /**
     * Monto del seguro financiado.
     */
    public double costoSeguro;
    /**
     * Identificador de la sucursal.
     */
    public int idSucursal;
    
    /**
     * Para saber si contrato o no seguro.
     */
    public int segContratado;

    public IntegranteCicloVO() {
        idGrupo = 0;
        idCiclo = 0;
        idCliente = 0;
        idSolicitud = 0;
        plazo = 0;
        idTasa = 0;
        numCheque = null;
        ordenPago = null;
        desembolsado = 0;
        nombre = null;
        montoDesembolso = 0;
        monto = 0;
        montoRefinanciado = 0;
        estatus = 0;
        rol = 0;
        comision = 0;
        fechaCaptura = null;
        calificacion = 0;
        primaSeguro = 0;
        calificacionAutomatica = 0;
        semDisp = 0;
        RenovacionDoc=0;
        DocCompletos=0;
        idPorcentajeAdicional=0;
        /**
         * 01/10/2017
         * Inicialización de atributos que sirven para indicar 
         * el sexo, numero total de solicitudes desembolsadas 
         * de los integrantes del ciclo asi como si el cliente 
         * tiene contratado seguro
         */
        sexoCliente=0;
        totalSolicitudesDesembolsados=0;
        contratacionSeguro=0;
    }

    public String toString() {
        String respuesta = null;
        respuesta = "idGrupo=[" + idGrupo + "],";
        respuesta += "idCiclo=[" + idCiclo + "],";
        respuesta += "idCliente=[" + idCliente + "],";
        respuesta += "idSolicitud=[" + idSolicitud + "],";
        respuesta += "plazo=[" + plazo + "],";
        respuesta += "idTasa=[" + idTasa + "],";
        respuesta += "numCheque=[" + numCheque + "],";
        respuesta += "desembolsado=[" + desembolsado + "],";
        respuesta += "nombre=[" + nombre + "],";
        respuesta += "montoDesembolso=[" + montoDesembolso + "],";
        respuesta += "monto=[" + monto + "],";
        respuesta += "montoRefinanciado=[" + montoRefinanciado + "],";
        respuesta += "estatus=[" + estatus + "],";
        respuesta += "rol=[" + rol + "],";
        respuesta += "comision=[" + comision + "],";
        respuesta += "fechaCaptura=[" + fechaCaptura + "],";
        respuesta += "calificacion=[" + calificacion + "],";
        respuesta += "primaSeguro=[" + primaSeguro + "],";
        respuesta += "calificacionAutomatica=[" + calificacionAutomatica + "],";
        respuesta += "consultaCC=[" + consultaCC + "],";
        respuesta += "semDisp=[" + semDisp + "],";
        respuesta += "porcentajeAdicional=[" + idPorcentajeAdicional + "],";
        respuesta += "sexoCliente=[" + sexoCliente + "],";
        respuesta += "totalSolicitudesDesembolsados=[" + totalSolicitudesDesembolsados + "], ";
        respuesta += "contratacionSeguro=[" + contratacionSeguro + "]";
        return respuesta;
    }

    public IntegranteCicloVO(int idCliente, int idSolicitud, double monto, String grupo, Date fecha, int idCiclo) {
        this.idCliente = idCliente;
        this.idSolicitud = idSolicitud;
        this.monto = monto;
        this.grupo = grupo;
        this.fecha = fecha;
        this.idCiclo = idCiclo;
    }
    
    public IntegranteCicloVO(int idCliente, int idSolicitud, String nombreCompleto, double monto) {
        this.idCliente = idCliente;
        this.idSolicitud = idSolicitud;
        this.nombre = nombreCompleto;
        this.monto = monto;
    }
    
    public IntegranteCicloVO(int idCliente, int idSolicitud, int rol){
        this.idCliente = idCliente;
        this.idSolicitud = idSolicitud;
        this.rol = rol;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    public int getComision() {
        return comision;
    }

    public void setComision(int comision) {
        this.comision = comision;
    }

    public int getDesembolsado() {
        return desembolsado;
    }

    public void setDesembolsado(int desembolsado) {
        this.desembolsado = desembolsado;
    }

    public int getEstatus() {
        return estatus;
    }

    public void setEstatus(int estatus) {
        this.estatus = estatus;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Timestamp getFechaCaptura() {
        return fechaCaptura;
    }

    public void setFechaCaptura(Timestamp fechaCaptura) {
        this.fechaCaptura = fechaCaptura;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public int getIdCiclo() {
        return idCiclo;
    }

    public void setIdCiclo(int idCiclo) {
        this.idCiclo = idCiclo;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    public int getIdSolicitud() {
        return idSolicitud;
    }

    public void setIdSolicitud(int idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public int getIdTasa() {
        return idTasa;
    }

    public void setIdTasa(int idTasa) {
        this.idTasa = idTasa;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public double getMontoDesembolso() {
        return montoDesembolso;
    }

    public void setMontoDesembolso(double montoDesembolso) {
        this.montoDesembolso = montoDesembolso;
    }

    public double getMontoRefinanciado() {
        return montoRefinanciado;
    }

    public void setMontoRefinanciado(double montoRefinanciado) {
        this.montoRefinanciado = montoRefinanciado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumCheque() {
        return numCheque;
    }

    public void setNumCheque(String numCheque) {
        this.numCheque = numCheque;
    }

    public OrdenDePagoVO getOrdenPago() {
        return ordenPago;
    }

    public void setOrdenPago(OrdenDePagoVO ordenPago) {
        this.ordenPago = ordenPago;
    }

    public int getPlazo() {
        return plazo;
    }

    public void setPlazo(int plazo) {
        this.plazo = plazo;
    }

    public double getPrimaSeguro() {
        return primaSeguro;
    }

    public void setPrimaSeguro(double primaSeguro) {
        this.primaSeguro = primaSeguro;
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }

    public int getEmpleo() {
        return empleo;
    }

    public void setEmpleo(int empleo) {
        this.empleo = empleo;
    }

    public int getSeguro() {
        return seguro;
    }

    public void setSeguro(int seguro) {
        this.seguro = seguro;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public int getScoreFico() {
        return scoreFico;
    }

    public void setScoreFico(int scoreFico) {
        this.scoreFico = scoreFico;
    }

    public int getMedioCobro() {
        return medioCobro;
    }

    public void setConTarjeta(int medioCobro) {
        this.medioCobro = medioCobro;
    }

    public TarjetasVO getTarjetaCobro() {
        return tarjetaCobro;
    }

    public void setTarjetaCobro(TarjetasVO tarjetaCobro) {
        this.tarjetaCobro = tarjetaCobro;
    }

    public int getConsultaCC() {
        return consultaCC;
    }

    public void setConsultaCC(int consultaCC) {
        this.consultaCC = consultaCC;
    }

    public int getMedioDisp() {
        return medioDisp;
    }

    public void setMedioDisp(int medioDisp) {
        this.medioDisp = medioDisp;
    }

    public int getCalificacionAutomatica() {
        return calificacionAutomatica;
    }

    public void setCalificacionAutomatica(int calificacionAutomatica) {
        this.calificacionAutomatica = calificacionAutomatica;
    }

    public int getEsNuevo() {
        return esNuevo;
    }

    public void setEsNuevo(int esNuevo) {
        this.esNuevo = esNuevo;
    }

    public int getEsInterciclo() {
        return esInterciclo;
    }

    public void setEsInterciclo(int esInterciclo) {
        this.esInterciclo = esInterciclo;
    }

    public int getSemDisp() {
        return semDisp;
    }

    public void setSemDisp(int semDisp) {
        this.semDisp = semDisp;
    }

    public int getAceptaRegular() {
        return aceptaRegular;
    }

    public void setAceptaRegular(int aceptaRegular) {
        this.aceptaRegular = aceptaRegular;
    }

    public int getRenovacionDoc() {
        return RenovacionDoc;
    }

    public void setRenovacionDoc(int RenovacionDoc) {
        this.RenovacionDoc = RenovacionDoc;
    }

    public int getDocCompletos() {
        return DocCompletos;
    }

    public void setDocCompletos(int DocCompletos) {
        this.DocCompletos = DocCompletos;
    } 

    public int getIdPorcentajeAdicional() {
        return idPorcentajeAdicional;
    }

    public void setIdPorcentajeAdicional(int idPorcentajeAdicional) {
        this.idPorcentajeAdicional = idPorcentajeAdicional;
    }

    public double getMontoAdicional() {
        return montoAdicional;
    }

    public void setMontoAdicional(double montoAdicional) {
        this.montoAdicional = montoAdicional;
    }

    public Date getFechaDesembAdicional() {
        return fechaDesembAdicional;
    }

    public void setFechaDesembAdicional(Date fechaDesembAdicional) {
        this.fechaDesembAdicional = fechaDesembAdicional;
    }

    public int getTipo_adicional() {
        return tipo_adicional;
    }

    public void setTipo_adicional(int tipo_adicional) {
        this.tipo_adicional = tipo_adicional;
    }
    public double getMontoConSeguro() {
        return montoConSeguro;
    }

    public void setMontoConSeguro(double montoConSeguro) {
        this.montoConSeguro = montoConSeguro;
    }

    public int getTipoSeguro() {
        return tipoSeguro;
    }

    public void setTipoSeguro(int tipoSeguro) {
        this.tipoSeguro = tipoSeguro;
    }

    public int getSegContratado() {
        return segContratado;
    }

    public void setSegContratado(int segContratado) {
        this.segContratado = segContratado;
    }

    public int getSexoCliente() {
        return sexoCliente;
    }

    public void setSexoCliente(int sexoCliente) {
        this.sexoCliente = sexoCliente;
    }

    public int getTotalSolicitudesDesembolsados() {
        return totalSolicitudesDesembolsados;
    }

    public void setTotalSolicitudesDesembolsados(int totalSolicitudesDesembolsados) {
        this.totalSolicitudesDesembolsados = totalSolicitudesDesembolsados;
    }

    public int getContratacionSeguro() {
        return contratacionSeguro;
    }

    public void setContratacionSeguro(int contratacionSeguro) {
        this.contratacionSeguro = contratacionSeguro;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        IntegranteCicloVO clone = (IntegranteCicloVO)super.clone();
        clone.setOrdenPago((OrdenDePagoVO)clone.getOrdenPago().clone());
        return clone; 
    }
}