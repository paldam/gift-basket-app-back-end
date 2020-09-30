package com.damian.util;
import com.damian.domain.audit.OrderAuditedRevisionEntity;
import com.damian.domain.order.Order;
import com.damian.domain.order.OrderDao;
import com.damian.domain.order.OrderItem;
import com.damian.domain.order_file.DbFileDao;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditQuery;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import java.text.SimpleDateFormat;

import java.io.*;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.damian.config.Constants.ANSI_RESET;
import static com.damian.config.Constants.ANSI_YELLOW;

@Component
public class PdfGenerator {

    private static  Integer orderTypeId ;
    private static Order order;
    private  static DbFileDao dbFileDao;
    private static OrderDao orderDaoin ;
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(PdfGenerator.class);

    @Autowired
    private DbFileDao dao;
    private  OrderDao orderDao;
    private static EntityManagerFactory factory;

    @PostConstruct
    private void initStaticDao () {
        dbFileDao = this.dao;
        orderDaoin = this.orderDao;
    }

    public static ByteArrayInputStream generatePdf(List<Order> orderToPrint) throws IOException {




        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try{
            PdfWriter.getInstance(document, out);
            document.open();
        } catch (DocumentException ex) {

        }






        orderToPrint.forEach(order->{


            //            List<Object[]> orderHistoryListTmp = orderDaoin.getOrderHistoryById(1859);
            //            List<OrderAuditedRevisionEntity> orderAuditedRevisionEntitiesList = new ArrayList<>();
            //            String LastEditUserTmp;
            //            if(orderAuditedRevisionEntitiesList.size() > 0){
            //                LastEditUserTmp = orderAuditedRevisionEntitiesList.get(0).getUser();
            //            }else{
            //                LastEditUserTmp = "brak";
            //            }


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






                PdfPCell cell01 = new PdfPCell(new Phrase("Klient imię i nazwisko:",font));
                cell01.setColspan(4);
                cell01.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell01.setBorder(Rectangle.LEFT | Rectangle.TOP| Rectangle.RIGHT);

                table.addCell(cell01);


                PdfPCell cell02 = new PdfPCell(new Phrase("Email:",font));
                cell02.setColspan(3);
                cell02.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell02.setBorder(Rectangle.LEFT | Rectangle.TOP| Rectangle.RIGHT);

                table.addCell(cell02);

                PdfPCell cell03 = new PdfPCell(new Phrase("Telefon:",font));
                cell03.setColspan(3);
                cell03.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell03.setBorder(Rectangle.LEFT | Rectangle.TOP| Rectangle.RIGHT);

                table.addCell(cell03);


                PdfPCell cell04 = new PdfPCell(new Phrase(order.getCustomer().getName(),font2));
                cell04.setColspan(4);
                cell04.setMinimumHeight(48);
                cell04.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell04.setBorder(Rectangle.LEFT |  Rectangle.BOTTOM| Rectangle.RIGHT);

                table.addCell(cell04);



                String emailTmp = "";

                if (order.getCustomer().getEmail() == null){
                    emailTmp = "brak";
                }else{
                    emailTmp = order.getCustomer().getEmail();
                }

                PdfPCell cell05 = new PdfPCell(new Phrase(emailTmp,font2));
                cell05.setColspan(3);
                cell05.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell05.setBorder(Rectangle.LEFT |  Rectangle.BOTTOM| Rectangle.RIGHT);

                table.addCell(cell05);


                String telCustTmp = "";

                if (order.getCustomer().getPhoneNumber() == null){
                    telCustTmp = "brak";
                }else{
                    telCustTmp = order.getCustomer().getPhoneNumber();
                }

                PdfPCell cell06 = new PdfPCell(new Phrase(telCustTmp ,font2));
                cell06.setColspan(3);
                cell06.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell06.setBorder(Rectangle.LEFT |  Rectangle.BOTTOM| Rectangle.RIGHT);

                table.addCell(cell06);

                PdfPCell cell = new PdfPCell(new Phrase("Nazwa Firmy:",font));
                cell.setColspan(10);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBorder(Rectangle.LEFT | Rectangle.TOP| Rectangle.RIGHT);

                table.addCell(cell);

                PdfPCell cell2 = new PdfPCell(new Phrase(order.getCustomer().getCompany().getCompanyName(),font5));
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
                cell6.setRowspan(3);
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

                PdfPCell cell10 = new PdfPCell(new Phrase("Data Wystawienia",font));
                cell10.setColspan(4);
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




                SimpleDateFormat format2 = new SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.ENGLISH);
                String orderDateString = format2.format(order.getOrderDate());

                PdfPCell cell12 = new PdfPCell(new Phrase(orderDateString,font2));
                cell12.setColspan(4);
                cell12.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell12.setBorder(Rectangle.LEFT | Rectangle.BOTTOM| Rectangle.RIGHT);
                table.addCell(cell12);


                PdfPCell cell111 = new PdfPCell(new Phrase("Adres dostawy",font));
                cell111.setColspan(10);
                cell111.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell111.setBorder(Rectangle.LEFT |  Rectangle.RIGHT);
                table.addCell(cell111);
                //row 5


                PdfPCell cell112 = new PdfPCell(new Phrase(order.getAddress().addressDesc(),font2));
                cell112.setColspan(10);
                cell112.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell112.setBorder(Rectangle.LEFT | Rectangle.BOTTOM| Rectangle.RIGHT);
                cell112.setMinimumHeight(30);
                table.addCell(cell112);

                //row 6

                PdfPCell cell13 = new PdfPCell(new Phrase("Dostawa - osoba do kontaktu",font));
                cell13.setColspan(4);
                cell13.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell13.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                table.addCell(cell13);

                PdfPCell cell14 = new PdfPCell(new Phrase("Dostawa - telefon",font));
                cell14.setColspan(3);
                cell14.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell14.setBorder(Rectangle.LEFT |  Rectangle.RIGHT);
                table.addCell(cell14);

                PdfPCell cell15 = new PdfPCell(new Phrase("Dostawa - dodatkowe informacje",font));
                cell15.setColspan(3);
                cell15.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell15.setBorder(Rectangle.LEFT |  Rectangle.RIGHT);
                table.addCell(cell15);

                // row 7



                PdfPCell cell17 = new PdfPCell(new Phrase(order.getAddress().getContactPerson(),font2));
                cell17.setColspan(4);
                cell17.setMinimumHeight(30);
                cell17.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell17.setBorder(Rectangle.LEFT |Rectangle.BOTTOM |  Rectangle.RIGHT);
                table.addCell(cell17);

                String phoneTmp = "";

                if (order.getAddress().getPhoneNumber()==null){
                    phoneTmp = "brak";
                }else{
                    phoneTmp = order.getAddress().getPhoneNumber().toString();
                }


                PdfPCell cell18 = new PdfPCell(new Phrase(phoneTmp,font3));
                cell18.setColspan(3);
                cell18.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell18.setBorder(Rectangle.LEFT |Rectangle.BOTTOM |   Rectangle.RIGHT);
                table.addCell(cell18);


                PdfPCell cell19 = new PdfPCell(new Phrase(order.getAddress().getAdditionalInformation(),font4));
                cell19.setColspan(3);
                cell19.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell19.setBorder(Rectangle.LEFT |Rectangle.BOTTOM |   Rectangle.RIGHT);
                table.addCell(cell19);


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
                    table2.addCell(new PdfCellExt(new Phrase(i.getBasket().getBasketSezon().getBasketSezonName(),font3)));
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

                PdfPCell cell26 = new PdfPCell(new Phrase("Produkcja: ",font));
                cell26.setColspan(4);
                cell26.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell26.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                table3.addCell(cell26);

                PdfPCell cell27 = new PdfPCell(new Phrase("Ostatnia zmiana: ",font));
                cell27.setColspan(3);
                cell27.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell27.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                table3.addCell(cell27);


                PdfPCell cell27a = new PdfPCell(new Phrase("Wystawione przez: ",font));
                cell27a.setColspan(3);
                cell27a.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell27a.setBorder(Rectangle.LEFT | Rectangle.RIGHT);
                table3.addCell(cell27a);

                //row11

                String productionTmp = "";

                if (order.getProductionUser() == null){
                    productionTmp = "brak";
                }else{
                    productionTmp = order.getProductionUser().getLogin();
                }

                PdfPCell cell28 = new PdfPCell(new Phrase(productionTmp,font2));
                cell28.setColspan(4);
                cell28.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell28.setBorder(Rectangle.LEFT | Rectangle.BOTTOM |Rectangle.RIGHT);
                cell28.setMinimumHeight(33);
                table3.addCell(cell28);

                PdfPCell cell29 = new PdfPCell(new Phrase(""));
                cell29.setColspan(3);
                cell29.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell29.setBorder(Rectangle.LEFT |Rectangle.BOTTOM | Rectangle.RIGHT);
                table3.addCell(cell29);

                PdfPCell cell30 = new PdfPCell(new Phrase(order.getUserName(),font2));
                cell30.setColspan(3);
                cell30.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell30.setBorder(Rectangle.LEFT |Rectangle.BOTTOM | Rectangle.RIGHT);
                table3.addCell(cell30);


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


        Long numberOfFile = dbFileDao.countByOrderId(order.getOrderId());
        String attachmentInformation = "";
        if (numberOfFile > 0) {
            attachmentInformation = "Uwaga załącznik w systemie | ";
        }

        StringBuilder additionalInformationTmp = new StringBuilder();
        orderTypeId = order.getDeliveryType().getDeliveryTypeId();

        if(Arrays.asList(5,6,7).contains(orderTypeId)) {
            additionalInformationTmp
                .append(attachmentInformation)
                .append("Pobranie ")
                .append((double)order.getCod()/100)
                .append(" zł | ")
                .append((order.getAdditionalInformation()== null) ? "": order.getAdditionalInformation());
        } else{
            additionalInformationTmp.append(attachmentInformation);
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
