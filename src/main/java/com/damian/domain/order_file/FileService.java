package com.damian.domain.order_file;

import com.damian.domain.order.Order;
import com.damian.util.PdfGenerator;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Service
public class FileService {

   public ByteArrayInputStream preparePdfFile(List<Order> orders) throws IOException {

        PdfGenerator pdfGenerator = new PdfGenerator();

        return  pdfGenerator.generatePdf(orders);
    }


}
