package com.spring.controller;

import com.spring.feedbacks.Error;
import com.spring.model.AppUser;
import com.spring.requestenum.RequestDescriptions;
import com.spring.service.LogService;
import com.spring.service.UserService;
import com.spring.token.Token;
import com.spring.token.Validation;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/")
public class MainController
{
    @Setter
    @Autowired
    UserService userService;

    @Autowired
    LogService logService;
    @Autowired
    Validation validation;
    @Autowired
    Error error;

    public String getUserIP()
    {
        ServletRequestAttributes attr = (ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        return request.getRemoteAddr();
    }
    @RequestMapping("/verification")
    public String actviationpage(@RequestParam("email") String email, @RequestParam("code") long code, @RequestParam("password") String password)
    {
        logService.savelog(new com.spring.model.Log(RequestDescriptions.CHECKCODE.getText(),getUserIP()));

        try{
            Token myToken = new Token(email,password,"");
            if(validation.isvalidate(myToken)){
                if (userService.checkUserCode(email, code)) {
                    return "Hesabınız Başarıyla Aktifleştirildi";
                } else {
                    error.setCode(204 );
                    return "Girmiş olduğunuz kod geçerli değil";
                }
            }
            else{
                return "İzinsiz İşlem";
            }
        }
        catch(Exception e){
            return "Servis Kullanılamıyor";
        }
    }

    @GetMapping("/privacy")
    public String privacypage()
    {
        return "privacy";
    }
}
