package com.damian.util;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.System.out;

public class PdfGenerator {
    public static ByteArrayInputStream orderPdf() {

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

                float[] columnWidths = {4, 5, 5};
                PdfPTable table = new PdfPTable(columnWidths);
                table.setWidthPercentage(100);


                Font font = new Font(Font.FontFamily.HELVETICA, 7, Font.NORMAL, GrayColor.GRAY);
                PdfPCell cell = new PdfPCell(new Phrase("Nazwa Firmy:",font));
                cell.setColspan(3);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.LEFT | Rectangle.TOP| Rectangle.RIGHT);

                table.addCell(cell);


                Font font2 = new Font(Font.FontFamily.HELVETICA, 17, Font.NORMAL, GrayColor.BLACK);
                PdfPCell cell2 = new PdfPCell(new Phrase("UPC Polska",font2));
                cell2.setColspan(3);
                cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell2.setBorder(Rectangle.LEFT | Rectangle.BOTTOM| Rectangle.RIGHT);
                cell2.setMinimumHeight(70);
                table.addCell(cell2);

    //row 2
                PdfPCell cell3 = new PdfPCell(new Phrase("Data dostawy",font));
                cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell3.setBorder(Rectangle.LEFT | Rectangle.TOP| Rectangle.RIGHT);
                table.addCell(cell3);

                PdfPCell cell4 = new PdfPCell(new Phrase("Rodzaj dostawy",font));
                cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell4.setBorder(Rectangle.LEFT | Rectangle.TOP| Rectangle.RIGHT);
                table.addCell(cell4);

                PdfPCell cell5 = new PdfPCell(new Phrase("Gdzie",font));
                cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell5.setBorder(Rectangle.LEFT | Rectangle.TOP| Rectangle.RIGHT);
                table.addCell(cell5);
    // row 3
                PdfPCell cell6 = new PdfPCell(new Phrase("13.11.2018",font2));
                cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell6.setBorder(Rectangle.LEFT | Rectangle.BOTTOM| Rectangle.RIGHT);
                table.addCell(cell6);

                PdfPCell cell7 = new PdfPCell(new Phrase("Odbiór osobisty",font2));
                cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell7.setBorder(Rectangle.LEFT | Rectangle.BOTTOM| Rectangle.RIGHT);
                table.addCell(cell7);

                PdfPCell cell8 = new PdfPCell(new Phrase("Leśna 17c 05/110 Jabłonna",font2));
                cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell8.setBorder(Rectangle.LEFT | Rectangle.BOTTOM| Rectangle.RIGHT);
                table.addCell(cell8);
    //row 4

                PdfPCell cell9 = new PdfPCell(new Phrase("Nr zamówienia",font));
                cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell9.setBorder(Rectangle.LEFT |  Rectangle.RIGHT);
                table.addCell(cell9);

                PdfPCell cell10 = new PdfPCell(new Phrase("Adres Dostawy",font));
                cell10.setColspan(2);
                cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell10.setBorder(Rectangle.LEFT |  Rectangle.RIGHT);
                table.addCell(cell10);
    //row 5
                PdfPCell cell11 = new PdfPCell(new Phrase("12/2008",font2));
                cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell11.setBorder(Rectangle.LEFT |Rectangle.BOTTOM|  Rectangle.RIGHT);
                table.addCell(cell11);

                PdfPCell cell12 = new PdfPCell(new Phrase("Szkolna 17a  05-200 Warszawa",font2));
                cell12.setColspan(2);
                cell12.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell12.setBorder(Rectangle.LEFT | Rectangle.BOTTOM| Rectangle.RIGHT);
                table.addCell(cell12);

            PdfWriter.getInstance(document, out);
            document.open();
            document.add(table);

            document.close();

        } catch (DocumentException ex) {

            Logger.getLogger(PdfGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }
}
