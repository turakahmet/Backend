package com.spring.service;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Random;

/**
 * Created by egulocak on 16.04.2020.
 */

@Service
public class MailService {
    @Setter

    @Autowired
    UserService userService;
    @Autowired
    private JavaMailSender mailSender;

    public long sendMail(String userEmail){
            System.out.println("Mail service");

            long code = (long) Math.floor(Math.random() * 899999L) + 100000L;
            SimpleMailMessage email = new SimpleMailMessage();
            email.setFrom("teamoftarnet@gmail.com");
            email.setTo(userEmail);
            email.setSubject("Üyelik Onay");
            email.setText("üyeliği tamamlamak için gereken kod: "+code);

            mailSender.send(email);
            return code;



    }
    public Boolean resetpassword(String email){


        String url;
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 100) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        url = "http://10.0.3.248:8095/resetpassword?email="+email+"&token="+saltStr;
        url.toLowerCase();
        saltStr.toLowerCase();
        System.out.println(url);
        if(userService.insertpwcode(email,saltStr))
            System.out.println("KOD YERLEŞTİRME BAŞARILI");
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        String htmlMsg = "<p><h3>Parolanızı Sıfırlamak  için Lütfen Linke Tıklayınız:</h3><p><a href='"+url+"'>"+"link"+"</a>";
        try {
            helper.setText(htmlMsg, true); // Use this or above line.
            helper.setTo(email);
            helper.setSubject("Parolanızı Sıfırlama");
            helper.setFrom("teamoftarnet@gmail.com");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        mailSender.send(mimeMessage);



        return true;
    }



}
