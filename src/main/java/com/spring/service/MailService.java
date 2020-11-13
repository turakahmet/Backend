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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Random;


@Service
public class MailService {
    @Setter

    @Autowired
    UserService userService;
    @Autowired
    private JavaMailSender mailSender;

    public long sendMail(String userEmail,String password){
        System.out.println("Mail service");
        long code = (long) Math.floor(Math.random() * 899999L) + 100000L;
        String url = "http://100numaram-env.eba-4s2ppuff.eu-central-1.elasticbeanstalk.com/verification?email="+userEmail+"&code="+code+"&password="+password;
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        String htmlMsg = "<p><h3>100 numaram<br>Üyeliğinizi tamamlamak için lütfen linke Tıklayınız yada Aktivasyon kodunuzu uygulamaya yazıp onaylayın<br>Aktivasyon kodu:</h3><h3>"+code+"<br></h3><p><br><a href='"+url+"'>"+"aktivasyon için buraya TIKLA"+"</a>";
        try {
            helper.setText(htmlMsg, true); // Use this or above line.
            helper.setTo(userEmail);
            helper.setSubject("100 Numaram Üyelik Tamamlama");
            helper.setFrom("numaramyuz@gmail.com");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

        mailSender.send(mimeMessage);
        return code;



    }

    public Boolean resetpassword(String email){
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 6) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        String url;
        url = "http://100numaram-env.eba-4s2ppuff.eu-central-1.elasticbeanstalk.com/setPassword?email="+email+"&code="+saltStr;
        if(userService.newPassword(email,saltStr)) {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            String htmlMsg = "<p><h3>Tek kullanımlık şifre ile giriş yapabilirsiniz.<br>Şifrenizi e-mail adresinizle giriş yaptıktan sonra ayarlar bölümü şifre değiştir bölümünden değiştirebilirsiniz," +
                    "<br>Şifre:"+saltStr+"</h3><p>";
            try {
                helper.setText(htmlMsg, true); // Use this or above line.
                helper.setTo(email);
                helper.setSubject("100 Numaram Şifre Sıfırlama");
                helper.setFrom("numaramyuz@gmail.com");
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            mailSender.send(mimeMessage);
            return true;
        }else{
            return false;
        }



    }

    public Boolean supportMessage(String email, String body){
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        try {
            helper.setText(body, true);
            helper.setTo("numaramyuz@gmail.com");
            helper.setSubject("Talep ve Öneri Mesajı ");
            helper.setFrom(email);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        mailSender.send(mimeMessage);
        return true;
    }
}
