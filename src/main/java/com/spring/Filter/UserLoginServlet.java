package com.spring.Filter;

/**
 * Created by egulocak on 21.08.2020.
 */

import com.spring.dao.LogDao;
import com.spring.model.AppUser;
import com.spring.requestenum.RequestDescriptions;
import com.spring.service.LogService;
import lombok.Setter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class UserLoginServlet {
//    Boolean flag;
//    @Setter
//    @Autowired
//    LogService logService;
//
//
//    @Override
//    public void init(ServletConfig config) throws ServletException {
//        super.init(config);
//        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
//    }
//
//
//    public UserLoginServlet() {
//        super();
//
//    }
//
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        doPost(request, response);
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        //Log tablosundan gelecek sorgulara göre filterlar uygulanacak
//        //Log tablosuna atılacak count sorgualrına  göre filterdan geçecekx1
//
//
////        String userIp = req.getRemoteAddr();
////        System.out.println("REMOTE IP:"+userIp);
////        resp.sendError(401);
//
//
//        if (logService.getrecordcount(req.getRemoteAddr(), RequestDescriptions.NEWACCOUNT.getText())) {
//            //burda yönlendirme yapılacak
//
//
//
//        } else
//            resp.sendError(401);
//
//    }
}
