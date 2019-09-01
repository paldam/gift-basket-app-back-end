package com.damian.boundry.rest;

import com.damian.domain.notification.Notification;
import com.damian.domain.prize.Prize;
import com.damian.domain.prize.PrizeDao;
import com.damian.domain.user.User;
import com.damian.security.SecurityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class PrizeController {

    PrizeDao prizeDao;

    public PrizeController(PrizeDao prizeDao) {
        this.prizeDao = prizeDao;
    }

    @CrossOrigin
    @GetMapping("/prizelist")
    ResponseEntity<List<Prize>> getPrizes() {
        List<Prize> prizeList = prizeDao.findAllBy();
        return new ResponseEntity<List<Prize>>(prizeList, HttpStatus.OK);
    }
}
