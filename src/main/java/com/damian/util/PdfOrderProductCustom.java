package com.damian.util;

import com.damian.domain.basket.Basket;
import com.damian.domain.order.Order;
import com.damian.dto.OrderItemsDto;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.damian.config.Constants.ANSI_RESET;
import static com.damian.config.Constants.ANSI_YELLOW;

public class PdfOrderProductCustom {

    static Integer orderTypeId;
    static Order order;

    public static ByteArrayInputStream generateBasketsProductsCustomListPdf(java.util.List<OrderItemsDto> orderItemsDto, Order order) throws IOException {


        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {


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
            Font font4 = new Font(helvetica4, 17, Font.BOLD, GrayColor.BLACK);



            PdfWriter.getInstance(document, out);
            document.open();

            float[] columnWidths0 = {5, 5, 5, 5, 5, 5, 5, 5, 5, 5};
            PdfPTable table0 = new PdfPTable(columnWidths0);
            table0.setWidthPercentage(100);


            SimpleDateFormat format2 = new SimpleDateFormat("yyyy.MM.dd", Locale.ENGLISH);
            String orderDateString = format2.format(order.getDeliveryDate());



            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder
                .append("Zamówienie: ")
                .append(order.getOrderFvNumber())
                .append(" | Firma: ")
                .append(order.getCustomer().getCompany().getCompanyName())
                .append(" | Osoba: ")
                .append(order.getCustomer().getName())
                .append(" | Data wysyłki: ")
                .append(orderDateString)
                .append(" | Produkcja: ");

            if( order.getProductionUser() != null){
                stringBuilder.append(order.getProductionUser().getLogin());
            }


            String headline = stringBuilder.toString();


            PdfPCell cell00 = new PdfPCell(new Phrase(headline, font2));
            cell00.setColspan(10);
            cell00.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell00.setBorder(Rectangle.BOTTOM);
            cell00.setMinimumHeight(30);
            cell00.setPaddingBottom(10);
            table0.addCell(cell00);



            PdfPCell cell0 = new PdfPCell(new Phrase("", font3));
            cell0.setColspan(10);
            cell0.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell0.setVerticalAlignment(Element.ALIGN_CENTER);
            cell0.setBorder(Rectangle.LEFT | Rectangle.TOP | Rectangle.RIGHT);
            table0.addCell(cell0);

            orderItemsDto.forEach(orderItems -> {

                if(orderItems.getAdded() == true) {


                    float[] columnWidths = {5, 5, 5, 5, 5, 5, 5, 5, 5, 5};
                    PdfPTable table = new PdfPTable(columnWidths);
                    table.setWidthPercentage(100);

                    PdfPCell cell = new PdfPCell(new Phrase("Kosz: " + orderItems.getBasket().getBasketName() + "        Ilość: " + orderItems.getQuantity(), font4));
                    cell.setMinimumHeight(40);
                    cell.setColspan(10);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cell.setVerticalAlignment(Element.ALIGN_CENTER);
                    cell.setBorder(Rectangle.LEFT | Rectangle.TOP | Rectangle.RIGHT);
                    table.addCell(cell);


                    float[] columnWidths2 = {6, 2, 2};
                    PdfPTable table2 = new PdfPTable(columnWidths2);
                    table2.setWidthPercentage(100);


                    orderItems.getBasket().getBasketItems().forEach(basketItems -> {

                        table2.addCell(new PdfCellExt(new Phrase(basketItems.getProduct().getProductName(), font3)));
                        table2.addCell(new PdfCellExt(new Phrase(basketItems.getProduct().getCapacity(), font3)));

                        Integer total = (basketItems.getQuantity() * orderItems.getQuantity());


                        table2.addCell(new PdfCellExt(new Phrase(total.toString(), font3)));
                    });

                    try {
                        document.add(table0);
                        document.add(table);
                        document.add(table2);
                        document.newPage();
                    } catch (DocumentException ex) {
                        Logger.getLogger(PdfGenerator.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });


            document.close();

        } catch (DocumentException ex) {

            Logger.getLogger(PdfGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

}
