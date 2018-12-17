package com.damian.service;

import com.damian.dto.FileDto;
import com.damian.dto.NumberProductsToChangeStock;
import com.damian.dto.OrderDto;
import com.damian.exceptions.OrderStatusException;
import com.damian.model.*;
import com.damian.repository.*;
import com.damian.rest.OrderController;
import com.damian.security.SecurityUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.log4j.Logger;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private static final Logger logger = Logger.getLogger(OrderService.class);
    private OrderDao orderDao;
    private CustomerDao customerDao;
    private AddressDao addressDao;
    private ProductDao productDao;
    private SupplierDao supplierDao;
    private DbFileDao dbFileDao;
    //public static final int  NOWE = 9;
    //public static final int  PRZYJETE= 9;

    OrderService(OrderDao orderDao, CustomerDao customerDao, AddressDao addressDao, ProductDao productDao,DbFileDao dbFileDao, SupplierDao supplierDao
                 ) {
        this.orderDao = orderDao;
        this.customerDao = customerDao;
        this.addressDao = addressDao;
        this.productDao = productDao ;
        this.supplierDao = supplierDao;
        this.dbFileDao = dbFileDao;

    }

    @Transactional
    public void createOrUpdateOrder(Order order) throws OrderStatusException {

        logger.error(" W Serwisie użytkownik to " + SecurityUtils.getCurrentUserLogin());

        if (Objects.isNull(order.getOrderId()) ){


           } else {

            Order orderPrevState = orderDao.findByOrderId(order.getOrderId());
            Integer prevOrderStatus = orderPrevState.getOrderStatus().getOrderStatusId();
            Integer newOrderStatus = order.getOrderStatus().getOrderStatusId();




            if (prevOrderStatus == 1 && (newOrderStatus == 2 || newOrderStatus == 5)) {
                logger.info("brak możliwości zmiany ");

                throw new OrderStatusException("brak możliwości zmiany statusu z 'nowy' na 'wyslany' lub 'zrealizowny'");

            }


            if (prevOrderStatus == 99 && newOrderStatus != 99) {
                logger.info("brak możliwości zmiany statusu, zamówienie archiwalne");

                throw new OrderStatusException("brak możliwości zmiany statusu, zamówienie archiwalne");

            }


            if (prevOrderStatus == 1 && newOrderStatus == 4) {
                logger.info("AAA Stan 1- nowe   4-przyjete");
                changeStockStatusNoweToPrzyjete(order);

            }
            if (prevOrderStatus == 1 && newOrderStatus == 3) {
                logger.info("AAA Stan 1- nowe   3-skompletowane");
                changeStockStatusNoweSkompletowane(order);
            }


            if (prevOrderStatus == 4 && newOrderStatus == 4) {
                logger.info("AAA Stan 4- przyjete   4-przyjete");
                changeStockStatusPrzyjteToPrzyjete(order);

            }


            if (prevOrderStatus == 4 && newOrderStatus == 1|| prevOrderStatus == 4 && newOrderStatus == 99) {
                logger.info("AAA Stan 4- przyjete   1-nowe lub 99 usuniete");
                changeStockStatusPrzyjeteToNoweUsuniete(order.getOrderId());

            }
            if (prevOrderStatus == 4 && newOrderStatus == 3) {
                logger.info("AAA Stan 4- przyjete   3-skmpletowane");
                changeStockStatusPrzyjeteSkompletowane(order);

            }
            if (prevOrderStatus == 4 && newOrderStatus == 2|| prevOrderStatus == 4 && newOrderStatus == 5) {
                logger.info("AAA Stan 4- przyjete   2-wysłane lub 5 zrealizwane;");
                changeStockStatusPrzyjeteToWyslaneZrealizowane(order);

            }

            if (prevOrderStatus == 3 && newOrderStatus == 4) {
                logger.info("AAA Stan 3-skmpletowane    4- przyjete ");
                changeStockStatusSkompletowanePrzyjete(order);

            }

            if (prevOrderStatus == 3 && newOrderStatus == 2|| prevOrderStatus == 3 && newOrderStatus == 5) {
                logger.info("AAA Stan 3- skompletowane   2-wysłane lub 5 zrealizwane;");
                changeStockStatusSkompletowaneToWyslaneZrealizowane(order);

            }

            if (prevOrderStatus == 3 && newOrderStatus == 3) {
                logger.info("AAA Stan 3-skmpletowane    3- skompletowane ");
                changeStockStatusSkompletowaneToSkompletowane(order);

            }

            if (prevOrderStatus == 3 && newOrderStatus == 1 || prevOrderStatus == 3 && newOrderStatus == 99) {
                logger.info("AAA Stan 3- skompletowane   1-nowe lub 99 usuniete");
                changeStockStatusSkompletowaneToNoweUsuniete(order.getOrderId());

            }

            if ((prevOrderStatus == 2 || prevOrderStatus == 5) && ( newOrderStatus == 3)) {
                logger.info("AAA Stan 2- wysłane lub zrealizowne 5  ->  3 - skompletowane");
                changeStockStatuswyslaneOrZrezalizowaneToSkompletowane(order);

            }

            if ((prevOrderStatus == 2 || prevOrderStatus == 5) && ( newOrderStatus == 4)) {
                logger.info("AAA Stan 2- wysłane lub zrealizowne 5  ->  4 - przyjete");
                changeStockStatuswyslaneOrZrezalizowaneToPrzyjete(order);

            }


            if ((prevOrderStatus == 2 || prevOrderStatus == 5) && ( newOrderStatus == 1)) {
                logger.info("AAA Stan 2- wysłane lub zrealizowne 5  ->  1- nowe");
                changeStockStatuswyslaneOrZrezalizowaneToNowe(order);

            }


        }   //
//            1 -nowe
//            4-przyjete
//            3-skompletowane


//            2-wyslane
//            5-zrealizwane;



//            6 - usuniete



        Customer customer = order.getCustomer();
        logger.info("Id customera "+ customer.getCustomerId());
        if (customer.getCustomerId() != null) {
            logger.info("zamówienie z klientem z bazy");
            Customer customerToSave = order.getCustomer();
            //order.setCustomer(null);
            customer.setAddresses(null);
            customerDao.saveAndFlush(customer);
            orderDao.saveAndFlush(order);

        } else {
            logger.info("zamówienie z nowym klientem");
            Customer savedCustomer = customerDao.saveAndFlush(customer);
            Address tmpAddres = savedCustomer.getAddresses().get(0);
            order.setAddress(tmpAddres);
            order.setCustomer(savedCustomer);
            orderDao.save(order);
        }

    }

    public List<OrderDto> getOrderDao(){

        List<Order> orderList = orderDao.findAllWithoutDeleted();
        List<OrderDto>   orderDtoList = new ArrayList<>() ;
        List<DbFile>   dbFileDtoList = dbFileDao.findAll() ;

        List<OrderItem> oredrItemsList = new ArrayList<>();


        orderList.forEach(order -> {
            List<DbFile> result = new LinkedList<>();
            //result =  dbFileDtoList.stream().filter(data -> data.getOrderId() == 835).collect(Collectors.toList());

            result =  dbFileDtoList.stream()
                    .filter(data -> order.getOrderId().equals(data.getOrderId()))
                    .collect(Collectors.toList());

            Long fileIdTmp = null;


            if(result.size() >0) {
                fileIdTmp = result.get(0).getFileId();
            }else{
                fileIdTmp = 0L;
            }

            orderDtoList.add(new OrderDto(order.getOrderId(), order.getOrderFvNumber(), order.getCustomer(), order.getOrderDate(),
                    order.getAdditionalInformation(), order.getDeliveryDate(),order.getWeekOfYear(),order.getDeliveryType(),
                    order.getOrderStatus(), order.getOrderTotalAmount(), fileIdTmp,oredrItemsList,order.getAdditionalSale())) ;
        });


        return orderDtoList;

    }


    public List<OrderDto> getOrderStats(){

        List<Order> orderList = orderDao.findAllWithoutDeleted();
        List<OrderDto>   orderDtoList = new ArrayList<>() ;
        List<DbFile>   dbFileDtoList = dbFileDao.findAll() ;

        List<OrderItem> oredrItemsList = new ArrayList<>();


        orderList.forEach(order -> {

            Long fileIdTmp = null;


            orderDtoList.add(new OrderDto(order.getOrderId(), order.getOrderFvNumber(), order.getCustomer(), order.getOrderDate(),
                    order.getAdditionalInformation(), order.getDeliveryDate(),order.getWeekOfYear(),order.getDeliveryType(),
                    order.getOrderStatus(), order.getOrderTotalAmount(), fileIdTmp,order.getOrderItems(),order.getAdditionalSale())) ;
        });


        return orderDtoList;

    }


    public List<OrderDto> getOrderDaoByCustomer(Integer id){

        List<Order> orderList = orderDao.findByCustomerId(id);
        List<OrderDto>   orderDtoList = new ArrayList<>() ;
        List<DbFile>   dbFileDtoList = dbFileDao.findAll() ;

        List<OrderItem> oredrItemsList = new ArrayList<>();


        orderList.forEach(order -> {
            logger.error("EEEEEEEE" + order.getWeekOfYear());
            List<DbFile> result = new LinkedList<>();
            //result =  dbFileDtoList.stream().filter(data -> data.getOrderId() == 835).collect(Collectors.toList());

            result =  dbFileDtoList.stream()
                    .filter(data -> order.getOrderId().equals(data.getOrderId()))
                    .collect(Collectors.toList());

            Long fileIdTmp = null;


            if(result.size() >0) {
                fileIdTmp = result.get(0).getFileId();
            }else{
                fileIdTmp = 0L;
            }

            orderDtoList.add(new OrderDto(order.getOrderId(), order.getOrderFvNumber(), order.getCustomer(), order.getOrderDate(),
                    order.getAdditionalInformation(), order.getDeliveryDate(),order.getWeekOfYear(),order.getDeliveryType(),
                    order.getOrderStatus(), order.getOrderTotalAmount(), fileIdTmp,oredrItemsList,order.getAdditionalSale())) ;
        });


        return orderDtoList;

    }

@Transactional

    public List<Order> getOrderListFromIdList(List<Long> orederIdList) {

        List<Order> ordersList = new ArrayList<>();

        orederIdList.forEach(orderId -> {
            Order orderToAdd = orderDao.findOne(orderId);
            ordersList.add(orderToAdd);

        });

        return  ordersList;
    }


 @Transactional
 public void changeStockStatusPrzyjteToPrzyjete(Order order) {

     List<NumberProductsToChangeStock> numberProductsToChangeStocks = productDao.numberProductsToChangeStock(order.getOrderId());

     numberProductsToChangeStocks.forEach(product -> {

         productDao.updateStockTmpMinus(product.getProductId(), product.getQuantityToChange());

     });


     order.getOrderItems().forEach(orderItem -> {
          int v1 = orderItem.getQuantity();
         orderItem.getBasket().getBasketItems().forEach(basketItems -> {


                 int v2 = basketItems.getQuantity();


             logger.info(basketItems.getProduct().getProductName() + " Liczba " +(v1 * v2));

             productDao.updateStockTmpAdd(basketItems.getProduct().getId(),(new Long(v1*v2)));


         });

     });

 }



    public void changeStockStatusNoweSkompletowane(Order order) {

        order.getOrderItems().forEach(orderItem -> {
            int v1 = orderItem.getQuantity();
            orderItem.getBasket().getBasketItems().forEach(basketItems -> {


                int v2 = basketItems.getQuantity();


                logger.info(basketItems.getProduct().getProductName() + " Liczba " +(v1 * v2));

                productDao.updateStockMinus(basketItems.getProduct().getId(),(new Long(v1*v2)));


            });

        });
    }



    public void changeStockStatusPrzyjeteSkompletowane(Order order) {

        List<NumberProductsToChangeStock> numberProductsToChangeStocks = productDao.numberProductsToChangeStock(order.getOrderId());


        numberProductsToChangeStocks.forEach(product ->{

            productDao.updateStockTmpMinus(product.getProductId(),product.getQuantityToChange());

        });

        order.getOrderItems().forEach(orderItem -> {
            int v1 = orderItem.getQuantity();
            orderItem.getBasket().getBasketItems().forEach(basketItems -> {


                int v2 = basketItems.getQuantity();


                logger.info(basketItems.getProduct().getProductName() + " Liczba " +(v1 * v2));

                productDao.updateStockMinus(basketItems.getProduct().getId(),(new Long(v1*v2)));


            });

        });


    }




    public void changeStockStatusPrzyjeteToNoweUsuniete(Long orderId) {

        List<NumberProductsToChangeStock> numberProductsToChangeStocks = productDao.numberProductsToChangeStock(orderId);


        numberProductsToChangeStocks.forEach(product ->{

            productDao.updateStockTmpMinus(product.getProductId(),product.getQuantityToChange());

        });
    }


    public void changeStockStatusPrzyjeteToWyslaneZrealizowane(Order order) {

        List<NumberProductsToChangeStock> numberProductsToChangeStocks = productDao.numberProductsToChangeStock(order.getOrderId());


        numberProductsToChangeStocks.forEach(product ->{

            productDao.updateStockTmpMinus(product.getProductId(),product.getQuantityToChange());

        });

        order.getOrderItems().forEach(orderItem -> {
            int v1 = orderItem.getQuantity();
            orderItem.getBasket().getBasketItems().forEach(basketItems -> {


                int v2 = basketItems.getQuantity();


                logger.info(basketItems.getProduct().getProductName() + " Liczba " +(v1 * v2));

                productDao.updateStockMinus(basketItems.getProduct().getId(),(new Long(v1*v2)));


            });

        });
    }




    public void changeStockStatusNoweToPrzyjete(Order order) {

        order.getOrderItems().forEach(orderItem -> {
            int v1 = orderItem.getQuantity();
            orderItem.getBasket().getBasketItems().forEach(basketItems -> {


                int v2 = basketItems.getQuantity();


                logger.info(basketItems.getProduct().getProductName() + " Liczba " +(v1 * v2));

                productDao.updateStockTmpAdd(basketItems.getProduct().getId(),(new Long(v1*v2)));


            });

        });
    }



    public void changeStockStatusSkompletowaneToWyslaneZrealizowane(Order order) {

        List<NumberProductsToChangeStock> numberProductsToChangeStocks = productDao.numberProductsToChangeStock(order.getOrderId());

        numberProductsToChangeStocks.forEach(product -> {

            productDao.updateStockAdd(product.getProductId(), product.getQuantityToChange());

        });


        order.getOrderItems().forEach(orderItem -> {
            int v1 = orderItem.getQuantity();
            orderItem.getBasket().getBasketItems().forEach(basketItems -> {


                int v2 = basketItems.getQuantity();


                logger.info(basketItems.getProduct().getProductName() + " Liczba " + (v1 * v2));

                productDao.updateStockMinus(basketItems.getProduct().getId(), (new Long(v1 * v2)));


            });

        });
    }

    public void changeStockStatusSkompletowaneToNoweUsuniete(Long orderId) {

        List<NumberProductsToChangeStock> numberProductsToChangeStocks = productDao.numberProductsToChangeStock(orderId);

        numberProductsToChangeStocks.forEach(product ->{

            productDao.updateStockAdd(product.getProductId(),product.getQuantityToChange());

        });
    }



    public void changeStockStatusSkompletowanePrzyjete(Order order) {

        List<NumberProductsToChangeStock> numberProductsToChangeStocks = productDao.numberProductsToChangeStock(order.getOrderId());

        numberProductsToChangeStocks.forEach(product ->{

            productDao.updateStockAdd(product.getProductId(),product.getQuantityToChange());

        });


        order.getOrderItems().forEach(orderItem -> {
            int v1 = orderItem.getQuantity();
            orderItem.getBasket().getBasketItems().forEach(basketItems -> {


                int v2 = basketItems.getQuantity();


                logger.info(basketItems.getProduct().getProductName() + " Liczba " +(v1 * v2));

                productDao.updateStockTmpAdd(basketItems.getProduct().getId(),(new Long(v1*v2)));


            });

        });

    }



    public void changeStockStatusSkompletowaneToSkompletowane(Order order) {

        List<NumberProductsToChangeStock> numberProductsToChangeStocks = productDao.numberProductsToChangeStock(order.getOrderId());

        numberProductsToChangeStocks.forEach(product -> {

            productDao.updateStockAdd(product.getProductId(), product.getQuantityToChange());

        });


        order.getOrderItems().forEach(orderItem -> {
            int v1 = orderItem.getQuantity();
            orderItem.getBasket().getBasketItems().forEach(basketItems -> {


                int v2 = basketItems.getQuantity();


                logger.info(basketItems.getProduct().getProductName() + " Liczba " + (v1 * v2));

                productDao.updateStockMinus(basketItems.getProduct().getId(), (new Long(v1 * v2)));


            });

        });

    }

        public void changeStockStatuswyslaneOrZrezalizowaneToSkompletowane(Order order) {

            List<NumberProductsToChangeStock> numberProductsToChangeStocks = productDao.numberProductsToChangeStock(order.getOrderId());

            numberProductsToChangeStocks.forEach(product -> {

                productDao.updateStockAdd(product.getProductId(), product.getQuantityToChange());

            });


            order.getOrderItems().forEach(orderItem -> {
                int v1 = orderItem.getQuantity();
                orderItem.getBasket().getBasketItems().forEach(basketItems -> {


                    int v2 = basketItems.getQuantity();


                    logger.info(basketItems.getProduct().getProductName() + " Liczba " + (v1 * v2));

                    productDao.updateStockMinus(basketItems.getProduct().getId(), (new Long(v1 * v2)));


                });

            });
        }


            public void changeStockStatuswyslaneOrZrezalizowaneToPrzyjete(Order order) {

                List<NumberProductsToChangeStock> numberProductsToChangeStocks = productDao.numberProductsToChangeStock(order.getOrderId());

                numberProductsToChangeStocks.forEach(product -> {

                    productDao.updateStockAdd(product.getProductId(), product.getQuantityToChange());

                });


                order.getOrderItems().forEach(orderItem -> {
                    int v1 = orderItem.getQuantity();
                    orderItem.getBasket().getBasketItems().forEach(basketItems -> {


                        int v2 = basketItems.getQuantity();


                        logger.info(basketItems.getProduct().getProductName() + " Liczba " + (v1 * v2));

                        productDao.updateStockTmpAdd(basketItems.getProduct().getId(), (new Long(v1 * v2)));


                    });

                });

            }


    public void changeStockStatuswyslaneOrZrezalizowaneToNowe(Order order) {

        List<NumberProductsToChangeStock> numberProductsToChangeStocks = productDao.numberProductsToChangeStock(order.getOrderId());

        numberProductsToChangeStocks.forEach(product -> {

            productDao.updateStockAdd(product.getProductId(), product.getQuantityToChange());

        });

        


}}



