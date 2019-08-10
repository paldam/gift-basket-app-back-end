package com.damian.util;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PageEventListener extends PdfPageEventHelper {

    @Override
    public void onEndPage(PdfWriter writer, Document document)  {

        try {
            BaseFont helvetica2 = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);

            Font font2 = new Font(helvetica2, 11, Font.NORMAL, GrayColor.BLACK);
            PdfPTable tbl = new PdfPTable(3);
            tbl.setTotalWidth(520);


            PdfPCell cells1 = new PdfPCell(new Phrase("Przyjąłem:", font2));
            PdfPCell cells2 = new PdfPCell(new Phrase("Wydałem: ", font2));
            PdfPCell cells3 = new PdfPCell(new Phrase("Uwagi :", font2));
            cells1.setBorder(0);
            cells2.setBorder(0);
            cells3.setBorder(0);

            tbl.addCell(cells1);
            tbl.addCell(cells2);
            tbl.addCell(cells3);

            float x = document.leftMargin();
            float y = document.bottom() ;

            tbl.writeSelectedRows(0, -1, x, y, writer.getDirectContent());



        } catch (DocumentException ex) {
            Logger.getLogger(PdfGenerator.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ioEx) {
            Logger.getLogger(PdfGenerator.class.getName()).log(Level.SEVERE, null, ioEx);
        }






    }
}




