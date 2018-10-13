package com.damian.util;
import com.damian.model.Order;
import com.damian.model.OrderItem;
import com.damian.rest.UserJWTController;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.slf4j.LoggerFactory;


import java.text.SimpleDateFormat;

import java.io.*;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PdfGenerator {

    static  Integer orderTypeId ;
    static Order order;
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(PdfGenerator.class);


    public static ByteArrayInputStream generatePdf(List<Order> orderToPrint) throws IOException {


        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try{
            PdfWriter.getInstance(document, out);
            document.open();
        } catch (DocumentException ex) {

        }

        orderToPrint.forEach(order->{

                try {

                    float[] columnWidths = {5, 5, 5,5,5,5,5,5,5,5};
                    PdfPTable table = new PdfPTable(columnWidths);
                    table.setWidthPercentage(100);

                    BaseFont helvetica = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1250, BaseFont.EMBEDDED);
                    Font font=new Font(helvetica,7,Font.NORMAL,GrayColor.GRAY);

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
                            PdfPCell cell3 = new PdfPCell(new Phrase("Planowana data wysyłki",font));
                            cell3.setColspan(3);
                            cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell3.setBorder(Rectangle.LEFT | Rectangle.TOP| Rectangle.RIGHT);
                            table.addCell(cell3);

                            PdfPCell cell4 = new PdfPCell(new Phrase("Rodzaj dostawy",font));
                            cell4.setColspan(3);
                            cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell4.setBorder(Rectangle.LEFT | Rectangle.TOP| Rectangle.RIGHT);
                            table.addCell(cell4);

                            PdfPCell cell5 = new PdfPCell(new Phrase("Tydzień dostawy",font));
                            cell5.setColspan(4);
                            cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell5.setBorder(Rectangle.LEFT | Rectangle.TOP| Rectangle.RIGHT);
                            table.addCell(cell5);
                // row 3

                            SimpleDateFormat format = new SimpleDateFormat("dd/MM", Locale.ENGLISH);
                            String deliveryDateString = format.format(order.getDeliveryDate());

                            PdfPCell cell6 = new PdfPCell(new Phrase(deliveryDateString,font6));
                            cell6.setColspan(3);
                            cell6.setMinimumHeight(60);
                            cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell6.setBorder(Rectangle.LEFT | Rectangle.BOTTOM| Rectangle.RIGHT);
                            table.addCell(cell6);

                            PdfPCell cell7 = new PdfPCell(new Phrase(order.getDeliveryType().getDeliveryTypeName(),font2));
                            cell7.setColspan(3);
                            cell7.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell7.setBorder(Rectangle.LEFT | Rectangle.BOTTOM| Rectangle.RIGHT);
                            table.addCell(cell7);

                            PdfPCell cell8 = new PdfPCell(new Phrase(getDateFromDayWeek(order),font2));
                            cell8.setColspan(4);
                            cell8.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell8.setBorder(Rectangle.LEFT | Rectangle.BOTTOM| Rectangle.RIGHT);
                            table.addCell(cell8);
                //row 4

                            PdfPCell cell9 = new PdfPCell(new Phrase("Nr zamówienia",font));
                            cell9.setColspan(3);
                            cell9.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell9.setBorder(Rectangle.LEFT |  Rectangle.RIGHT);
                            table.addCell(cell9);

                            PdfPCell cell10 = new PdfPCell(new Phrase("Adres Dostawy",font));
                            cell10.setColspan(7);
                            cell10.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell10.setBorder(Rectangle.LEFT |  Rectangle.RIGHT);
                            table.addCell(cell10);
                //row 5
                            PdfPCell cell11 = new PdfPCell(new Phrase(order.getOrderFvNumber(),font2));
                            cell11.setColspan(3);
                            cell11.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell11.setBorder(Rectangle.LEFT |Rectangle.BOTTOM|  Rectangle.RIGHT);
                            cell11.setMinimumHeight(30);
                            table.addCell(cell11);

                            PdfPCell cell12 = new PdfPCell(new Phrase(order.getAddress().AddressDesc(),font2));
                            cell12.setColspan(7);
                            cell12.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell12.setBorder(Rectangle.LEFT | Rectangle.BOTTOM| Rectangle.RIGHT);
                            table.addCell(cell12);

                //row 6

                            PdfPCell cell13 = new PdfPCell(new Phrase("Data wystawienia",font));
                            cell13.setColspan(3);
                            cell13.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell13.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            table.addCell(cell13);

                            PdfPCell cell14 = new PdfPCell(new Phrase("Imię i nazwisko",font));
                            cell14.setColspan(3);
                            cell14.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell14.setBorder(Rectangle.LEFT |  Rectangle.RIGHT);
                            table.addCell(cell14);

                            PdfPCell cell15 = new PdfPCell(new Phrase("Email",font));
                            cell15.setColspan(2);
                            cell15.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell15.setBorder(Rectangle.LEFT |  Rectangle.RIGHT);
                            table.addCell(cell15);

                            PdfPCell cell16 = new PdfPCell(new Phrase("Telefon",font));
                            cell16.setColspan(2);
                            cell16.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell16.setBorder(Rectangle.LEFT |  Rectangle.RIGHT);
                            table.addCell(cell16);
                // row 7

                            SimpleDateFormat format2 = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.ENGLISH);
                            String orderDateString = format2.format(order.getOrderDate());

                            PdfPCell cell17 = new PdfPCell(new Phrase(orderDateString,font2));
                            cell17.setColspan(3);
                            cell17.setMinimumHeight(30);
                            cell17.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell17.setBorder(Rectangle.LEFT |Rectangle.BOTTOM |  Rectangle.RIGHT);
                            table.addCell(cell17);

                            PdfPCell cell18 = new PdfPCell(
                                    new Phrase(order.getCustomer().getName() ,font3));

                            cell18.setColspan(3);
                            cell18.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell18.setBorder(Rectangle.LEFT |Rectangle.BOTTOM |   Rectangle.RIGHT);
                            table.addCell(cell18);

                            PdfPCell cell19 = new PdfPCell(new Phrase(order.getCustomer().getEmail(),font4));
                            cell19.setColspan(2);
                            cell19.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell19.setBorder(Rectangle.LEFT |Rectangle.BOTTOM |   Rectangle.RIGHT);
                            table.addCell(cell19);

                            String phoneTmp = "";

                            if (order.getCustomer().getPhoneNumber()==null){
                                phoneTmp = "brak";
                            }else{
                                phoneTmp = order.getCustomer().getPhoneNumber().toString();
                            }
                            PdfPCell cell20 = new PdfPCell(new Phrase(phoneTmp,font3));
                            cell20.setColspan(2);
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
                            cell21.setColspan(3);
                            cell21.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell21.setBorder(Rectangle.BOTTOM |Rectangle.LEFT) ;
                            cell21.setMinimumHeight(13);
                            table.addCell(cell21);

                            PdfPCell cell22 = new PdfPCell(new Phrase("Ilość",font));
                            cell22.setColspan(2);
                            cell22.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell22.setBorder(Rectangle.BOTTOM);
                            table.addCell(cell22);

                            PdfPCell cell222 = new PdfPCell(new Phrase("Sezon",font));
                            cell222.setColspan(2);
                            cell222.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell222.setBorder(Rectangle.BOTTOM);
                            table.addCell(cell222);

                            PdfPCell cell23 = new PdfPCell(new Phrase("Zmiany",font));
                            cell23.setColspan(3);
                            cell23.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell23.setBorder(Rectangle.BOTTOM|Rectangle.RIGHT);
                            table.addCell(cell23);



                            float[] columnWidths2 = {3,2,2,3};
                            PdfPTable table2 = new PdfPTable(columnWidths2);
                            table2.setWidthPercentage(100);


                            for (OrderItem i: order.getOrderItems()) {
                                table2.addCell(new PdfCellExt(new Phrase(i.getBasket().getBasketName(),font3)));
                                table2.addCell(new PdfCellExt(new Phrase(i.getQuantity().toString(),font3)));
                                table2.addCell(new PdfCellExt(new Phrase(i.getBasket().getSeason(),font3)));
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


                            PdfPCell cell25 = new PdfPCell(new Phrase(getAdditionalInformtionMassage(order),font3));
                            cell25.setColspan(10);
                            cell25.setPaddingLeft(10);
                            cell25.setHorizontalAlignment(Element.ALIGN_LEFT);
                            cell25.setBorder(Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.RIGHT);
                            cell25.setMinimumHeight(60);
                            table3.addCell(cell25);

                //row10

                            PdfPCell cell26 = new PdfPCell(new Phrase("Grawer: ",font));
                            cell26.setColspan(6);
                            cell26.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell26.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            table3.addCell(cell26);

                            PdfPCell cell27 = new PdfPCell(new Phrase("Wystawiono przez: ",font));
                            cell27.setColspan(4);
                            cell27.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell27.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                            table3.addCell(cell27);

                //row11

                            PdfPCell cell28 = new PdfPCell(new Phrase("",font2));
                            cell28.setColspan(6);
                            cell28.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell28.setBorder(Rectangle.LEFT | Rectangle.BOTTOM |Rectangle.RIGHT);
                            cell28.setMinimumHeight(33);
                            table3.addCell(cell28);

                            PdfPCell cell29 = new PdfPCell(new Phrase(order.getUserName(),font2));
                            cell29.setColspan(4);
                            cell29.setHorizontalAlignment(Element.ALIGN_CENTER);
                            cell29.setBorder(Rectangle.LEFT |Rectangle.BOTTOM | Rectangle.RIGHT);
                            table3.addCell(cell29);

                    
                            document.add(table);
                            document.add(table2);
                            document.add(table3);
                            document.newPage();




                } catch (DocumentException ex) {
                    Logger.getLogger(PdfGenerator.class.getName()).log(Level.SEVERE, null, ex);
                }  catch (IOException ioEx){
                    Logger.getLogger(PdfGenerator.class.getName()).log(Level.SEVERE, null, ioEx);

                }
        });


        document.close();
        return new ByteArrayInputStream(out.toByteArray());
    }


    
    private static String getAdditionalInformtionMassage(Order order){

        StringBuilder additionalInformationTmp = new StringBuilder();
        orderTypeId = order.getDeliveryType().getDeliveryTypeId();

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

    private static String getDateFromDayWeek(Order order ) {


        if (Optional.ofNullable(order.getWeekOfYear()).isPresent()) {
            Calendar c = Calendar.getInstance();
            c.setTime(order.getOrderDate());

            int orderDateWeek = c.get(Calendar.WEEK_OF_YEAR);
            int year = c.get(Calendar.YEAR);
            // 41               // 40
            if (orderDateWeek > order.getWeekOfYear()) {

                year += 1;
            }

            log.error(Integer.toString(year));

            LocalDate monday = LocalDate.now();
            monday = monday.with(WeekFields.ISO.dayOfWeek(), 1);
            monday = monday.with(WeekFields.ISO.weekOfWeekBasedYear(), order.getWeekOfYear());
            monday = monday.with(WeekFields.ISO.weekBasedYear(), year);

            LocalDate sunday = monday.plusDays(6);


            StringBuilder sb = new StringBuilder();
            sb.append("TYDZIEŃ ");
            sb.append(order.getWeekOfYear().toString());
            sb.append("                   ");
            sb.append(monday.toString());
            sb.append("  ");
            sb.append(sunday.toString());
            return sb.toString();


        } else {
            return "";
        }

    }
}
