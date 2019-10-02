package com.damian.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.damian.config.Constants.ANSI_RESET;
import static com.damian.config.Constants.ANSI_YELLOW;

@Service
public class EmailService {

    public final JavaMailSender emailSender;


    public EmailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendSimpleMessage(
        String to, String subject, String text) {

        try {

            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true,"UTF-8");
            helper.setFrom("kontakt@punktyupominkowe.pl","Program lojalnościowy ");
           // helper.setTo("damianpaluch87@gmail.com");
           helper.setTo(to);
            helper.setSubject(subject);
            //helper.setContent(tresc, "text/plain");
            helper.setText(text, false);
            emailSender.send(message);
        } catch (MessagingException ex) {
            Logger.getLogger(EmailService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedEncodingException ec){

        }


    }

}
