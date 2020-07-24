package com.damian.util;

import com.damian.domain.basket.Basket;
import com.damian.domain.order.Order;
import com.damian.dto.ProductToCollectOrder;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.damian.config.Constants.ANSI_RESET;
import static com.damian.config.Constants.ANSI_YELLOW;

public class PdfBasketContents {

    static Integer orderTypeId;
    static Order order;

    public static ByteArrayInputStream generateBasketProductsListPdf(Basket basket, byte[] basketImage) throws IOException {
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
            Image img = null;
            if (basketImage.length > 0) {
                img = Image.getInstance(basketImage);
                img.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
                float x = (PageSize.A4.getWidth() - img.getScaledWidth()) / 2;
                float y = (PageSize.A4.getHeight() - img.getScaledHeight()) / 2;
                img.setAbsolutePosition(x, y);
            }
            PdfPCell cell0 = new PdfPCell(new Phrase("Zawartość kosza " + basket.getBasketName(), font2));
            cell0.setColspan(10);
            cell0.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell0.setBorder(Rectangle.BOTTOM);
            cell0.setMinimumHeight(30);
            table.addCell(cell0);
            PdfPCell cell = new PdfPCell(new Phrase("Prudukty kosza:", font));
            cell.setColspan(10);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.LEFT | Rectangle.TOP | Rectangle.RIGHT);
            table.addCell(cell);
            float[] columnWidths2 = {6, 2, 2};
            PdfPTable table2 = new PdfPTable(columnWidths2);
            table2.setWidthPercentage(100);
            basket.getBasketItems().forEach(basketItems -> {
                table2.addCell(new PdfCellExt(new Phrase(basketItems.getProduct().getProductName(), font3)));
                table2.addCell(new PdfCellExt(new Phrase(basketItems.getProduct().getCapacity(), font3)));
                table2.addCell(new PdfCellExt(new Phrase(basketItems.getQuantity().toString(), font3)));
            });
            PdfWriter.getInstance(document, out);
            document.open();
            if (basketImage.length > 0) {
                document.add(img);
                document.newPage();
            }
            document.add(table);
            document.add(table2);
            document.close();
        } catch (DocumentException ex) {
            Logger.getLogger(PdfGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ByteArrayInputStream(out.toByteArray());
    }

    public static ByteArrayInputStream generateBasketProductsListCatalogNameVersionPdf(Basket basket, byte[] basketImage) throws IOException {
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
            Image img = null;
            if (basketImage.length > 0) {
                img = Image.getInstance(basketImage);
                img.scaleToFit(PageSize.A4.getWidth(), PageSize.A4.getHeight());
                float x = (PageSize.A4.getWidth() - img.getScaledWidth()) / 2;
                float y = (PageSize.A4.getHeight() - img.getScaledHeight()) / 2;
                img.setAbsolutePosition(x, y);
            }
            PdfPCell cell0 = new PdfPCell(new Phrase("Zawartość kosza " + basket.getBasketName(), font2));
            cell0.setColspan(10);
            cell0.setHorizontalAlignment(Element.ALIGN_LEFT);
            cell0.setBorder(Rectangle.BOTTOM);
            cell0.setMinimumHeight(30);
            table.addCell(cell0);
            PdfPCell cell = new PdfPCell(new Phrase("Prudukty kosza:", font));
            cell.setColspan(10);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorder(Rectangle.LEFT | Rectangle.TOP | Rectangle.RIGHT);
            table.addCell(cell);
            float[] columnWidths2 = {6, 2, 2};
            PdfPTable table2 = new PdfPTable(columnWidths2);
            table2.setWidthPercentage(100);
            basket.getBasketItems().forEach(basketItems -> {
                table2.addCell(new PdfCellExt(new Phrase(basketItems.getProduct().getProductCatalogName(), font3)));
                table2.addCell(new PdfCellExt(new Phrase(basketItems.getProduct().getCapacity(), font3)));
                table2.addCell(new PdfCellExt(new Phrase(basketItems.getQuantity().toString(), font3)));
            });
            PdfWriter.getInstance(document, out);
            document.open();
            if (basketImage.length > 0) {
                document.add(img);
                document.newPage();
            }
            document.add(table);
            document.add(table2);
            document.close();
        } catch (DocumentException ex) {
            Logger.getLogger(PdfGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ByteArrayInputStream(out.toByteArray());
    }
}
