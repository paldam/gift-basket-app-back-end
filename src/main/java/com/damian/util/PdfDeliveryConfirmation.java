package com.damian.util;

import com.damian.model.Order;
import com.damian.model.OrderItem;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Damian on 11.08.2018.
 */
public class PdfDeliveryConfirmation {
    static  Integer orderTypeId ;
    static Order order;

    public static ByteArrayInputStream generatePdf(Order orderToPrint) throws IOException {


        orderTypeId = orderToPrint.getDeliveryType().getDeliveryTypeId();
        order = orderToPrint;


        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {

            float[] columnWidths = {5, 5, 5,5,5,5,5,5,5,5};
            PdfPTable table = new PdfPTable(columnWidths);
            table.setWidthPercentage(100);

            BaseFont helvetica = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
            Font font=new Font(helvetica,7,Font.NORMAL,GrayColor.GRAY);

            BaseFont helvetica1 = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
            Font font15=new Font(helvetica,10,Font.NORMAL,GrayColor.GRAY);

            BaseFont helvetica2 = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
            Font font2=new Font(helvetica2,17,Font.NORMAL,GrayColor.BLACK);

            BaseFont helvetica5 = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
            Font font5=new Font(helvetica5,53,Font.NORMAL,GrayColor.BLACK);

            BaseFont helvetica6 = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
            Font font6=new Font(helvetica6,36,Font.NORMAL,GrayColor.BLACK);

            BaseFont helvetica3 = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
            Font font3=new Font(helvetica3,13,Font.NORMAL,GrayColor.BLACK);

            BaseFont helvetica4 = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
            Font font4=new Font(helvetica4,10,Font.NORMAL,GrayColor.BLACK);


            PdfPCell cell0 = new PdfPCell(new Phrase("Potwierdzenie odbioru",font2));
            cell0.setColspan(10);
            cell0.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell0.setBorder(Rectangle.BOTTOM );
            cell0.setMinimumHeight(30);
            table.addCell(cell0);


            PdfPCell cell = new PdfPCell(new Phrase("Nazwa Firmy:",font));
            cell.setColspan(10);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.LEFT | Rectangle.TOP| Rectangle.RIGHT);
            table.addCell(cell);

            PdfPCell cell2 = new PdfPCell(new Phrase(order.getCustomer().getOrganizationName(),font5));
            cell2.setColspan(10);
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setBorder(Rectangle.LEFT | Rectangle.BOTTOM| Rectangle.RIGHT);
            cell2.setMinimumHeight(130);
            table.addCell(cell2);

//row 2

//row 4



            PdfPCell cell10 = new PdfPCell(new Phrase("Adres Dostawy",font));
            cell10.setColspan(10);
            cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell10.setBorder(Rectangle.LEFT |  Rectangle.RIGHT);
            table.addCell(cell10);
//row 5


            PdfPCell cell12 = new PdfPCell(new Phrase(order.getAddress().AddressDesc(),font2));
            cell12.setColspan(10);
            cell12.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell12.setBorder(Rectangle.LEFT | Rectangle.BOTTOM| Rectangle.RIGHT);
            cell12.setMinimumHeight(80);
            table.addCell(cell12);

//row 6

            PdfPCell cell13 = new PdfPCell(new Phrase("Nr zamówienia",font));
            cell13.setColspan(3);
            cell13.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell13.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            table.addCell(cell13);

            PdfPCell cell14 = new PdfPCell(new Phrase("Imię i nazwisko",font));
            cell14.setColspan(4);
            cell14.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell14.setBorder(Rectangle.LEFT |  Rectangle.RIGHT);
            table.addCell(cell14);



            PdfPCell cell16 = new PdfPCell(new Phrase("Telefon",font));
            cell16.setColspan(3);
            cell16.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell16.setBorder(Rectangle.LEFT |  Rectangle.RIGHT);
            table.addCell(cell16);
// row 7



            PdfPCell cell17 = new PdfPCell(new Phrase(order.getOrderFvNumber(),font2));
            cell17.setColspan(3);
            cell17.setMinimumHeight(30);
            cell17.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell17.setBorder(Rectangle.LEFT |Rectangle.BOTTOM |  Rectangle.RIGHT);
            table.addCell(cell17);

            PdfPCell cell18 = new PdfPCell(
                    new Phrase(order.getCustomer().getName() ,font3));

            cell18.setColspan(4);
            cell18.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell18.setBorder(Rectangle.LEFT |Rectangle.BOTTOM |   Rectangle.RIGHT);
            table.addCell(cell18);



            String phoneTmp = "";

        if (order.getCustomer().getPhoneNumber()==null){
            phoneTmp = "brak";
        }else{
            phoneTmp = order.getCustomer().getPhoneNumber().toString();
        }
            PdfPCell cell20 = new PdfPCell(new Phrase(phoneTmp,font3));
            cell20.setColspan(3);
            cell20.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell20.setBorder(Rectangle.LEFT |Rectangle.BOTTOM |  Rectangle.RIGHT);
            table.addCell(cell20);
// row 8a

            PdfPCell cell210 = new PdfPCell(new Phrase("Pozycje zamówienia",font));
            cell210.setColspan(10);
            cell210.setMinimumHeight(14);
            cell210.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell210.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            table.addCell(cell210);

// row 8b
            PdfPCell cell21 = new PdfPCell(new Phrase("Nazwa Kosza",font));
            cell21.setColspan(4);
            cell21.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell21.setBorder(Rectangle.BOTTOM |Rectangle.LEFT) ;
            cell21.setMinimumHeight(13);
            table.addCell(cell21);

            PdfPCell cell22 = new PdfPCell(new Phrase("Ilość",font));
            cell22.setColspan(2);
            cell22.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell22.setBorder(Rectangle.BOTTOM);
            table.addCell(cell22);

            PdfPCell cell23 = new PdfPCell(new Phrase("Zmiany",font));
            cell23.setColspan(4);
            cell23.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell23.setBorder(Rectangle.BOTTOM|Rectangle.RIGHT);
            table.addCell(cell23);



            float[] columnWidths2 = {4,2,4};
            PdfPTable table2 = new PdfPTable(columnWidths2);
            table2.setWidthPercentage(100);


        for (OrderItem i: order.getOrderItems()) {
            table2.addCell(new PdfCellExt(new Phrase(i.getBasket().getBasketName(),font3)));
            table2.addCell(new PdfCellExt(new Phrase(i.getQuantity().toString(),font3)));
            table2.addCell(new PdfCellExt(new Phrase("",font3)));
        }

            float[] columnWidths3 = {5,5,5,5,5,5,5,5,5,5};
            PdfPTable table3 = new PdfPTable(columnWidths3);
            table3.setWidthPercentage(100);
//row9
            PdfPCell cell24 = new PdfPCell(new Phrase("Uwagi: ",font2));
            cell24.setColspan(10);
            cell24.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell24.setPaddingLeft(10);
            cell24.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            table3.addCell(cell24);
//row10


            PdfPCell cell25 = new PdfPCell(new Phrase(getAdditionalInformtionMassage(),font3));
            cell25.setColspan(10);
            cell25.setPaddingLeft(10);
            cell25.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell25.setBorder(Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.RIGHT);
            cell25.setMinimumHeight(60);
            table3.addCell(cell25);

//row10



            PdfPCell cell27 = new PdfPCell(new Phrase("Potwierdzam odbiór zestawów świątecznych zgodnie z zamówieniem ",font15));
            cell27.setColspan(10);
            cell27.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell27.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            table3.addCell(cell27);

//row11


            PdfPCell cell29 = new PdfPCell(new Phrase("",font2));
            cell29.setColspan(10);
            cell29.setHorizontalAlignment(Element.ALIGN_BOTTOM);
            cell29.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
            cell29.setMinimumHeight(60);
            table3.addCell(cell29);


            PdfPCell cell30 = new PdfPCell(new Phrase("Data",font15));
            cell30.setColspan(5);
            cell30.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell30.setBorder(Rectangle.LEFT |Rectangle.BOTTOM );
            cell30.setMinimumHeight(22);
            table3.addCell(cell30);

            PdfPCell cell31 = new PdfPCell(new Phrase("Podpis",font15));
            cell31.setColspan(5);
            cell31.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell31.setBorder(Rectangle.BOTTOM | Rectangle.RIGHT);
            table3.addCell(cell31);




            PdfWriter.getInstance(document, out);
            document.open();
            document.add(table);
            document.add(table2);
            document.add(table3);

            document.close();

        } catch (DocumentException ex) {

            Logger.getLogger(PdfGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private static String getAdditionalInformtionMassage(){

        StringBuilder additionalInformationTmp = new StringBuilder();

        if(Arrays.asList(5,6,7).contains(orderTypeId)) {
            additionalInformationTmp
                    .append("Pobranie ")
                    .append((double)order.getCod()/100)
                    .append(" zł | ")
                    .append((order.getAdditionalInformation()== null) ? "": order.getAdditionalInformation());
        } else{
            additionalInformationTmp.append((order.getAdditionalInformation()== null) ? "": order.getAdditionalInformation());
        }

        return additionalInformationTmp.toString();

    }
}



