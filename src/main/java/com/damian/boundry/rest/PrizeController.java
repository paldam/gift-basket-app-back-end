package com.damian.boundry.rest;

import com.damian.domain.order.Order;
import com.damian.domain.prize.*;
import com.damian.domain.product.ProductType;
import com.damian.domain.user.User;
import com.damian.domain.user.UserRepository;
import com.damian.domain.user.UserService;
import com.damian.security.SecurityUtils;
import com.damian.util.FtpConnectionException;
import com.damian.util.FtpService;
import com.damian.util.FtpSpace;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/prize")
public class PrizeController {

    private PrizeDao prizeDao;
    private PointsDao pointsDao;
    private PrizeOrderDao prizeOrderDao;
    private PrizeOrderService prizeOrderService;
    private PrizeService prizeService;
    private FtpService ftpService;
    private UserRepository userService;

    public PrizeController(UserRepository userService, PointsDao pointsDao, PrizeDao prizeDao, PrizeOrderDao prizeOrderDao, PrizeOrderService prizeOrderService, PrizeService prizeService, FtpService ftpService) {
       this.pointsDao = pointsDao;
        this.prizeDao = prizeDao;
        this.prizeOrderDao = prizeOrderDao;
        this.prizeOrderService = prizeOrderService;
        this.prizeService = prizeService;
        this.ftpService = ftpService;
        this.userService=userService;
    }

    @CrossOrigin
    @PostMapping("/order")
    ResponseEntity<PrizeOrder> createOrder(@RequestBody PrizeOrder prizeOrder) {

        try{

            prizeOrderService.saveOrder(prizeOrder);

        }catch (NoPointsExceptions noPointsExceptions){

            return new ResponseEntity<PrizeOrder>( HttpStatus.NOT_ACCEPTABLE);
        }

        return new ResponseEntity<PrizeOrder>( HttpStatus.CREATED);
    }

    @CrossOrigin
    @DeleteMapping("/pointscheme/{id}")
    ResponseEntity<PointScheme> deletePointScheme(@PathVariable Long id) {
        pointsDao.deleteById(id);
        return new ResponseEntity<PointScheme>(HttpStatus.OK);
    }


    @CrossOrigin
    @GetMapping("orders")
    ResponseEntity<List<PrizeOrder>> getPrizeOrders() {
        List<PrizeOrder> prizeOrderList = prizeOrderDao.findAllByOrderByOrderDateDesc();
        return new ResponseEntity<List<PrizeOrder>>(prizeOrderList, HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("userorders")
    ResponseEntity<List<PrizeOrder>> getUserPrizeOrders() {
        SecurityUtils.getCurrentUserLogin();
        Optional<User> user= userService.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        List<PrizeOrder> prizeOrderList = prizeOrderDao.findAllByUser(user.get().getId());
        return new ResponseEntity<List<PrizeOrder>>(prizeOrderList, HttpStatus.OK);
    }


    @GetMapping(value = "/order/{id}")
    ResponseEntity<PrizeOrder> getPrizeOrder(@PathVariable Long id) {
        Optional<PrizeOrder> order = prizeOrderDao.findById(id);
        return new ResponseEntity<PrizeOrder>(order.get(), HttpStatus.OK);
    }

    @CrossOrigin
    @GetMapping("/prizelist")
    ResponseEntity<List<Prize>> getPrizes() {
        List<Prize> prizeList = prizeDao.findAllWithoutDel();


        return new ResponseEntity<List<Prize>>(prizeList, HttpStatus.OK);
    }


    @CrossOrigin
    @GetMapping("/pointscheme")
    ResponseEntity<List<PointScheme>> getPointsScheme() {
        List<PointScheme> prizeSchemeList = pointsDao.findBy();


        return new ResponseEntity<List<PointScheme>>(prizeSchemeList, HttpStatus.OK);
    }
    @CrossOrigin
    @PostMapping("/pointscheme/add")
    ResponseEntity<PointScheme> createPointScheme(@RequestBody PointScheme pointScheme) throws URISyntaxException {
        pointsDao.save(pointScheme);
        return new ResponseEntity<PointScheme>(pointScheme, HttpStatus.CREATED);
    }

    @CrossOrigin
    @PostMapping("/add/noimg")
    ResponseEntity<Prize> createOrder(@RequestBody Prize prize) {
        Prize savedPrize = prizeDao.save(prize);
        return new ResponseEntity<Prize>(savedPrize, HttpStatus.CREATED);
    }


    @CrossOrigin
    @PostMapping("/add")
        // handle file and object in one request
    ResponseEntity createPrize(@RequestPart("prizeimage") MultipartFile[] basketMultipartFiles, @RequestPart("prizeobject") Prize prize) throws URISyntaxException {
        try {
            InputStream img = new ByteArrayInputStream(basketMultipartFiles[0].getBytes());
            prizeService.savePrize(img, prize);
        } catch (IOException | FtpConnectionException ioe){
            return ResponseEntity.badRequest().body(ioe.getMessage());
        }
        return new ResponseEntity<>(prize, HttpStatus.CREATED);
    }


    @CrossOrigin
    @PostMapping("/editimage")
        // handle file and object in one request
    ResponseEntity editPrize(@RequestPart("prizeimage") MultipartFile[] basketMultipartFiles, @RequestPart("prizeobject") Prize prize) throws URISyntaxException {
        try {
            ftpService.sendFileViaFtp(new ByteArrayInputStream(basketMultipartFiles[0].getBytes()),prize.getId().toString(), FtpSpace.PRIZES);
        } catch (IOException | FtpConnectionException ioe){
            return ResponseEntity.badRequest().body(ioe.getMessage());
        }
        return new ResponseEntity<>(prize, HttpStatus.CREATED);
    }

    @CrossOrigin
    @PostMapping(value = "/order/status/{id}/{statusId}", produces = "text/plain;charset=UTF-8")
    ResponseEntity changeOrderStatus(@PathVariable Long id, @PathVariable Integer statusId) {

        PrizeOrder updatingOrder = prizeOrderDao.findById(id).get();

        PrizeOrderStatus updattingOrderNewStatus = new PrizeOrderStatus();
        updattingOrderNewStatus.setOrderStatusId(statusId);
        updatingOrder.setPrizeOrderStatus(updattingOrderNewStatus);

            prizeOrderService.updateOrder(updatingOrder);

            return new ResponseEntity<>(null, HttpStatus.OK);

    }


    @CrossOrigin
    @PostMapping(value = "status/{prizeId}/{isAve}", produces = "text/plain;charset=UTF-8")
    ResponseEntity changeOrderStatus(@PathVariable Long prizeId, @PathVariable Boolean isAve) {

        Prize updatingPrize = prizeDao.findById(prizeId).get();
        updatingPrize.setAvailable(isAve);

        prizeDao.save(updatingPrize);

        return new ResponseEntity<>(null, HttpStatus.OK);

    }


}
