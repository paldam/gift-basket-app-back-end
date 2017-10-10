package com.damian.util;

import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;


public class PdfCellExt extends PdfPCell{
    public PdfCellExt(Phrase phrase)  {
        super(phrase);
        setMinimumHeight(20);
        setVerticalAlignment(Element.ALIGN_CENTER);
        setHorizontalAlignment(Element.ALIGN_CENTER);
    }
}
