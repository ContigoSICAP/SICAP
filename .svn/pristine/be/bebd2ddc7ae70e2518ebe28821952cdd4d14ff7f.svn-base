package com.sicap.clientes.helpers;

import com.barcodelib.barcode.Linear;

public class CodigoBarrasHelper {
    
    public String codigoBarra128(String referencia, String ruta) throws Exception{
        
        String rutaImagen = ruta;
        Linear linear = new Linear();
        linear.setData(referencia);
        System.out.println("linear "+linear.getData());
        linear.setType(Linear.CODE128);
        linear.setUOM(Linear.UOM_PIXEL);
        linear.setX(1);
        linear.setY(12);
        linear.setLeftMargin(0);
        linear.setRightMargin(0);
        linear.setTopMargin(0);
        linear.setBottomMargin(0);
        linear.renderBarcode(ruta+"codigo128.gif");
        rutaImagen += "codigo128.gif";
        
        return rutaImagen;
    }
}
