package com.damian.boundry.rest;

import com.damian.domain.prize.*;
import com.damian.domain.user.User;
import com.damian.domain.user.UserRepository;
import com.damian.security.SecurityUtils;
import com.damian.util.FtpConnectionException;
import com.damian.util.FtpService;
import com.damian.util.FtpSpace;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@CrossOrigin
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

    public PrizeController(UserRepository userService, PointsDao pointsDao, PrizeDao prizeDao,
                           PrizeOrderDao prizeOrderDao, PrizeOrderService prizeOrderService,
                           PrizeService prizeService, FtpService ftpService) {
        this.pointsDao = pointsDao;
        this.prizeDao = prizeDao;
        this.prizeOrderDao = prizeOrderDao;
        this.prizeOrderService = prizeOrderService;
        this.prizeService = prizeService;
        this.ftpService = ftpService;
        this.userService = userService;
    }

    @PostMapping("/order")
    public ResponseEntity<PrizeOrder> createOrder(@RequestBody PrizeOrder prizeOrder) {
        try {
            prizeOrderService.saveOrder(prizeOrder);
        } catch (NoPointsExceptions noPointsExceptions) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        } catch (AccessDeniedException  accessDeniedEx) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        } catch (PointsExceptions pointsExceptions) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/pointscheme/{id}")
    public ResponseEntity<PointScheme> deletePointScheme(@PathVariable Long id) {
        pointsDao.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("orders")
    public ResponseEntity<List<PrizeOrder>> getPrizeOrders() {
        List<PrizeOrder> prizeOrderList = prizeOrderDao.findAllByOrderByOrderDateDesc();
        return new ResponseEntity<>(prizeOrderList, HttpStatus.OK);
    }

    @GetMapping("userorders")
    public ResponseEntity<List<PrizeOrder>> getUserPrizeOrders() {
        SecurityUtils.getCurrentUserLogin();
        Optional<User> user = userService.findOneByLogin(SecurityUtils.getCurrentUserLogin());
        List<PrizeOrder> prizeOrderList = prizeOrderDao.findAllByUser(user.get().getId());
        return new ResponseEntity<>(prizeOrderList, HttpStatus.OK);
    }

    @GetMapping(value = "/order/{id}")
    public ResponseEntity<PrizeOrder> getPrizeOrder(@PathVariable Long id) {
        Optional<PrizeOrder> order = prizeOrderDao.findById(id);
        return new ResponseEntity<>(order.get(), HttpStatus.OK);
    }

    @GetMapping("/prizelist")
    public ResponseEntity<List<Prize>> getPrizes() {
        List<Prize> prizeList = prizeDao.findAllBy();
        return new ResponseEntity<>(prizeList, HttpStatus.OK);
    }

    @GetMapping("/prizelistnodel")
    public ResponseEntity<List<Prize>> getPrizesWithoutDel() {
        List<Prize> prizeList = prizeDao.findAllWithoutDel();
        return new ResponseEntity<>(prizeList, HttpStatus.OK);
    }

    @GetMapping("/pointscheme")
    ResponseEntity<List<PointScheme>> getPointsScheme() {
        List<PointScheme> prizeSchemeList = pointsDao.findBy();
        return new ResponseEntity<>(prizeSchemeList, HttpStatus.OK);
    }

    @PostMapping("/pointscheme/add")
    public ResponseEntity<PointScheme> createPointScheme(@RequestBody PointScheme pointScheme) {
        pointsDao.save(pointScheme);
        return new ResponseEntity<>(pointScheme, HttpStatus.CREATED);
    }

    @PostMapping("/add/noimg")
    ResponseEntity<Prize> createOrder(@RequestBody Prize prize) {
        Prize savedPrize = prizeDao.save(prize);
        return new ResponseEntity<>(savedPrize, HttpStatus.CREATED);
    }

    @PostMapping("/add")
    public ResponseEntity createPrize(@RequestPart("prizeimage") MultipartFile[] basketMultipartFiles, @RequestPart(
        "prizeobject") Prize prize) {
        try {
            InputStream img = new ByteArrayInputStream(basketMultipartFiles[0].getBytes());
            prizeService.savePrize(img, prize);
        } catch (IOException | FtpConnectionException ioe) {
            return ResponseEntity.badRequest().body(ioe.getMessage());
        }
        return new ResponseEntity<>(prize, HttpStatus.CREATED);
    }

    @PostMapping("/editimage")
    public ResponseEntity editPrize(@RequestPart("prizeimage") MultipartFile[] basketMultipartFiles, @RequestPart(
        "prizeobject") Prize prize) {
        try {
            ftpService.sendFileViaFtp(new ByteArrayInputStream(basketMultipartFiles[0].getBytes()),
                prize.getId().toString(), FtpSpace.PRIZES);
        } catch (IOException | FtpConnectionException ioe) {
            return ResponseEntity.badRequest().body(ioe.getMessage());
        }
        return new ResponseEntity<>(prize, HttpStatus.CREATED);
    }

    @PostMapping(value = "/order/status/{id}/{statusId}", produces = "text/plain;charset=UTF-8")
    public ResponseEntity changeOrderStatus(@PathVariable Long id, @PathVariable Integer statusId) {
        PrizeOrder updatingOrder = prizeOrderDao.findById(id).get();
        PrizeOrderStatus updattingOrderNewStatus = new PrizeOrderStatus();
        updattingOrderNewStatus.setOrderStatusId(statusId);
        updatingOrder.setPrizeOrderStatus(updattingOrderNewStatus);
        prizeOrderService.updateOrder(updatingOrder);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @PostMapping(value = "status/{prizeId}/{isAve}", produces = "text/plain;charset=UTF-8")
    public ResponseEntity changeOrderStatus(@PathVariable Long prizeId, @PathVariable Boolean isAve) {
        Prize updatingPrize = prizeDao.findById(prizeId).get();
        updatingPrize.setAvailable(isAve);
        prizeDao.save(updatingPrize);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
