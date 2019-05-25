
package com.damian.util;

        import com.damian.dto.ProductToCollectOrder;
        import com.damian.domain.order.Order;
        import com.itextpdf.text.*;
        import com.itextpdf.text.pdf.*;

        import java.io.ByteArrayInputStream;
        import java.io.ByteArrayOutputStream;
        import java.io.IOException;
        import java.util.logging.Level;
        import java.util.logging.Logger;


public class PdfProductToCollectOrder {


    public static ByteArrayInputStream generateProductToCollectOrderPdf(java.util.List<ProductToCollectOrder> productToCollectOrder,Long orderId,Order order) throws IOException {


        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            float[] columnWidths = {5, 5, 5, 5, 5, 5, 5, 5, 5, 5};
            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(100);

            BaseFont helvetica = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
            Font font = new Font(helvetica, 7, Font.NORMAL, GrayColor.GRAY);

            BaseFont helvetica1 = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
            Font font15 = new Font(helvetica, 10, Font.NORMAL, GrayColor.GRAY);

            BaseFont helvetica2 = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
            Font font2 = new Font(helvetica2, 17, Font.NORMAL, GrayColor.BLACK);

            BaseFont helvetica5 = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
            Font font5 = new Font(helvetica5, 53, Font.NORMAL, GrayColor.BLACK);

            BaseFont helvetica6 = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
            Font font6 = new Font(helvetica6, 36, Font.NORMAL, GrayColor.BLACK);

            BaseFont helvetica3 = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
            Font font3 = new Font(helvetica3, 13, Font.NORMAL, GrayColor.BLACK);

            BaseFont helvetica4 = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
            Font font4 = new Font(helvetica4, 10, Font.NORMAL, GrayColor.BLACK);


            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder
                .append("Zamówienie ")
                .append(order.getOrderFvNumber())
                .append(" ")
                .append(order.getCustomer().getCompany().getCompanyName())
                .append(" | ")
                .append(order.getCustomer().getName());

            String headline = stringBuilder.toString();





            PdfPCell cell0 = new PdfPCell(new Phrase(headline, font2));
            cell0.setColspan(10);
            cell0.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell0.setBorder(Rectangle.BOTTOM);
            cell0.setMinimumHeight(30);
            cell0.setPaddingBottom(10);
            table.addCell(cell0);


            PdfPCell cell = new PdfPCell(new Phrase("Prudukty zamówienia:", font));
            cell.setColspan(10);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.LEFT | Rectangle.TOP | Rectangle.RIGHT);
            table.addCell(cell);


            float[] columnWidths2 = {6,2,2};
            PdfPTable table2 = new PdfPTable(columnWidths2);
            table2.setWidthPercentage(100);


            for (ProductToCollectOrder i: productToCollectOrder){

                table2.addCell(new PdfCellExt(new Phrase(i.getProduct_name(),font3)));
                table2.addCell(new PdfCellExt(new Phrase(i.getCapacity(),font3)));
                table2.addCell(new PdfCellExt(new Phrase(i.getSuma().toString(),font3)));
            }

//row 2

//row 4


            PdfWriter.getInstance(document, out);
            document.open();
            document.add(table);
            document.add(table2);

            document.close();

        } catch (DocumentException ex) {

            Logger.getLogger(PdfGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

}



