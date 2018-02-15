package com.sicap.clientes.util;

import com.sicap.clientes.vo.TarjetasVO;
import org.apache.commons.fileupload.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class TarjetasUtil {

    private static Logger myLogger = Logger.getLogger(TarjetasUtil.class);

    public List<TarjetasVO> procesaArchivoTarjetasBanamex(FileItem archivo) throws Exception {

        List<TarjetasVO> lstTarjetas = new ArrayList();
        try {
            InputStream data = archivo.getInputStream();
            POIFSFileSystem fsFileSystem = new POIFSFileSystem(data);
            HSSFWorkbook workBook = new HSSFWorkbook(fsFileSystem);
            HSSFSheet hssfSheet = workBook.getSheetAt(0);
            Iterator rowIterator = hssfSheet.rowIterator();
            int inicioC = 0, inicioR= 0;
            while (rowIterator.hasNext()) {
                HSSFRow hssfRow = (HSSFRow) rowIterator.next();
                Iterator iterator = hssfRow.cellIterator();
                List cellTempList = new ArrayList();
                while (iterator.hasNext()) {
                    HSSFCell hssfCell = (HSSFCell) iterator.next();
                    //System.out.println("CELDA "+inicioR+": "+hssfCell);
                    if(inicioC > 0 && inicioR > 0)
                        lstTarjetas.add(new TarjetasVO(hssfCell.toString()));
                    inicioR++;
                }
                inicioC++;
                //System.out.println("COLUMNA "+inicioC+": "+cellTempList);
            }
        } catch (Exception e) {
            myLogger.error("Error ", e);
        }

        return lstTarjetas;
    }
}
