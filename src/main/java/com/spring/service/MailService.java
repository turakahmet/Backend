package com.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * Created by egulocak on 16.04.2020.
 */

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendMail(String userEmail,String code){
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("teamoftarnet@gmail.com");
        email.setTo(userEmail);
        email.setSubject("Üyelik Onay");
        email.setText("üyeliği tamamlamak için gereken kod: "+code);

        mailSender.send(email);
    }



}
