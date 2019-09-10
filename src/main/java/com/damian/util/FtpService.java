package com.damian.util;

import com.damian.config.Constants;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

import static com.damian.config.Constants.ANSI_RESET;
import static com.damian.config.Constants.ANSI_YELLOW;

@Service
public class FtpService {

    private String spacePrefixForBasketExt;
    private String spacePrefixForPrize;
    private String server;
    private String port;
    private String user;
    private String pass;
    private Environment environment;

    public FtpService(Environment environment) {
        this.environment = environment;
        this.spacePrefixForBasketExt = environment.getRequiredProperty("ftp.spaceprefix.basketext");
        this.spacePrefixForPrize = environment.getRequiredProperty("ftp.spaceprefix.prize");
        this.server = environment.getRequiredProperty("ftp.server");
        this.port = environment.getRequiredProperty("ftp.port");
        this.user = environment.getRequiredProperty("ftp.username");
        this.pass = environment.getRequiredProperty("ftp.password");
    }

    public void sendFileViaFtp(InputStream image, String imgName, Enum<FtpSpace> ftpSpace) throws FtpConnectionException {
        try {


            FTPClient ftpClient = new FTPClient();
            ftpClient.connect(server, Integer.valueOf(port));
            showServerReply(ftpClient);
            int replyCode = ftpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(replyCode)) {
                System.out.println("Operation failed. Server reply code: " + replyCode);
                return;
            }
            boolean success = ftpClient.login(user, pass);
            showServerReply(ftpClient);
            if (!success) {
                throw new FtpConnectionException("Problem z połaczeniem FTP");
            } else {
                System.out.println("LOGGED IN SERVER");
            }
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);


            String tmpImgPathAndName;

            if(ftpSpace == FtpSpace.BASKETEXT){
                tmpImgPathAndName = spacePrefixForBasketExt + imgName +".jpg";
            }else{
                tmpImgPathAndName = spacePrefixForPrize + imgName +".jpg";
            }

            ftpClient.storeFile(tmpImgPathAndName, image);
            ftpClient.logout();
        } catch (IOException ex) {
            ex.printStackTrace();
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
