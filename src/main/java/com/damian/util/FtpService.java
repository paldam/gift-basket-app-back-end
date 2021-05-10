package com.damian.util;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class FtpService {

    @Value("${app.ftp.spaceprefix.basketext}")
    private String spacePrefixForBasketExt;
    @Value("${app.ftp.spaceprefix.prize}")
    private String spacePrefixForPrize;
    @Value("${app.ftp.spaceprefix.basket}")
    private String spacePrefixForBasket;
    @Value("${app.ftp.spaceprefix.basketext.generator}")
    private String spacePrefixForBasketGeneratorExt;
    @Value("${app.ftp.server}")
    private String server;
    @Value("${app.ftp.port}")
    private String port;
    @Value("${app.ftp.username}")
    private String user;
    @Value("${app.ftp.username.generator}")
    private String userGenerator;
    @Value("${app.ftp.password}")
    private String pass;
    @Value("${app.ftp.password.generator}")
    private String passGenerator;

    public void sendFileViaFtp(InputStream image, String imgName, Enum<FtpSpace> ftpSpace) throws FtpConnectionException {
        image.mark(1);

        try {
            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(server, Integer.valueOf(port));
            showServerReply(ftpClient);
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                System.out.println("Operation failed. Server reply code: " + replyCode);
                return;
            }

            boolean success;

                success = ftpClient.login(user, pass);


            showServerReply(ftpClient);
            if (!success) {
                throw new FtpConnectionException("Problem z połaczeniem FTP");
            } else {
                System.out.println("LOGGED IN SERVER");
            }
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            String tmpImgPathAndName;

            if (ftpSpace == FtpSpace.BASKETEXT) {
                tmpImgPathAndName = spacePrefixForBasketExt + imgName + ".jpg";

            } else if (ftpSpace == FtpSpace.BASKETS) {
                tmpImgPathAndName = spacePrefixForBasket + imgName + ".jpg";

            } else {
                tmpImgPathAndName = spacePrefixForPrize + imgName + ".jpg";

            }


            ftpClient.storeFile(tmpImgPathAndName, image);
            image.reset();
            ftpClient.logout();


        } catch (IOException ex) {
            ex.printStackTrace();
        }


        if(ftpSpace == FtpSpace.BASKETEXT) {
            try {
                FTPClient ftpClient2 = new FTPClient();
                ftpClient2.connect(server, Integer.valueOf(port));
                showServerReply(ftpClient2);
                int replyCode2 = ftpClient2.getReplyCode();
                if (!FTPReply.isPositiveCompletion(replyCode2)) {
                    System.out.println("Operation failed. Server reply code: " + replyCode2);
                    return;
                }
                ftpClient2.login(userGenerator, passGenerator);
                ftpClient2.setFileType(FTP.BINARY_FILE_TYPE);
                ftpClient2.storeFile("basket-image/" + imgName + ".jpg", image);
                ftpClient2.logout();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

    private static void showServerReply(FTPClient ftpClient) {
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String aReply : replies) {
                System.out.println("SERVER: " + aReply);
            }
        }
    }
}
