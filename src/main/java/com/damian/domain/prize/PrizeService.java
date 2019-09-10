package com.damian.domain.prize;

import com.damian.util.FtpConnectionException;
import com.damian.util.FtpService;
import com.damian.util.FtpSpace;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;

@Service
public class PrizeService {

    private PrizeDao prizeDao;
    private FtpService ftpService;

    public PrizeService(PrizeDao prizeDao, FtpService ftpService) {
        this.prizeDao = prizeDao;
        this.ftpService = ftpService;
    }

    @Transactional
    public void savePrize(InputStream prizeImg, Prize prize) throws FtpConnectionException  {
        prize.setAvailable(true);
        Prize savedPrize = prizeDao.save(prize);
        try {
            ftpService.sendFileViaFtp(prizeImg, savedPrize.getId().toString(), FtpSpace.PRIZES);
        } catch (FtpConnectionException ioe) {
            prizeDao.delete(savedPrize);
            throw new FtpConnectionException("Problem z połaczeniem FTP");
        }
    }
}
