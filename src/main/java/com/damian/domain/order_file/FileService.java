package com.damian.domain.order_file;

import com.damian.domain.order.Order;
import com.damian.domain.order.OrderDao;
import com.damian.util.PdfGenerator;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FileService {

    private OrderDao orderDao;

    public FileService(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public ByteArrayInputStream preparePdfFile(Long orderId) throws IOException {
        Order orderToGenerate = orderDao.findByOrderId(orderId);
        List<Order> orderList = new ArrayList<>();
        orderList.add(orderToGenerate);
        return PdfGenerator.generatePdf(orderList);
    }

    public Optional<ByteArrayInputStream> prepareAllTodayPdfFile() throws IOException {
        Optional<List<Order>> allTodaysOrder = orderDao.findAllTodaysOrders();
        if (allTodaysOrder.isPresent()) {
            return Optional.of(PdfGenerator.generatePdf(allTodaysOrder.get()));
        } else {
            return Optional.empty();
        }
    }


}
