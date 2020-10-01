package com.spring.Filter;

import com.spring.requestenum.RequestDescriptions;
import com.spring.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;



@WebFilter("/user/*")
@Component
@Order(1)
public class UserLoginFilter implements javax.servlet.Filter {

    private String description;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

        SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);

        System.out.println("Filter is initializing");

    }

    @Autowired
    LogService logService;


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        System.out.println("Starting a transaction for req: " + req.getRequestURI());


        if (req.getRequestURL().toString().endsWith("/insertuser")) {

            //Günde tek ip maks istek sayısı 10

            if (logService.getrecordcount(LocalDate.now(),req.getRemoteAddr(), RequestDescriptions.NEWACCOUNT.getText())) {
                System.out.println("Request is passed from filter");
                filterChain.doFilter(servletRequest, servletResponse);
            } else {

                System.out.println("Request cannot pass from Filter");


                ((HttpServletResponse) servletResponse).setStatus(403);
            }
        }


      else   if(req.getRequestURL().toString().endsWith("/checkstandard"))
        {
            //Günde tek ip maks istek sayısı 100

            if(logService.getrecordcount(LocalDate.now(),req.getRemoteAddr(),RequestDescriptions.STANDARDLOGIN.getText()))
                {
                    System.out.println("Login Request is passed from filter");
                    filterChain.doFilter(servletRequest, servletResponse);

                }
            else{
                    System.out.println("Login Request cannot pass from Filter");


                    ((HttpServletResponse) servletResponse).setStatus(403);
                }
        }


    else    if(req.getRequestURL().toString().endsWith("changepassword")){


            //Günde tek ip maks istek sayısı 30
            if(logService.getrecordcount(LocalDate.now(),req.getRemoteAddr(),RequestDescriptions.CHANGEPASSWORD.getText())){
                System.out.println("changepassword Request is passed from filter");
                filterChain.doFilter(servletRequest, servletResponse);

            }

            else{
                System.out.println("ChangePassword Request cannot pass from Filter");


                ((HttpServletResponse) servletResponse).setStatus(403);
            }
        }


     else if(req.getRequestURL().toString().endsWith("resetpassword")){
            if(logService.getrecordcount(LocalDate.now(),req.getRemoteAddr(),RequestDescriptions.RESETPASSWORD.getText())){
                System.out.println("resetpassword Request is passed from filter");
                filterChain.doFilter(servletRequest, servletResponse);

            }
            else{
                System.out.println("resetpassword Request cannot pass from Filter");
                ((HttpServletResponse) servletResponse).setStatus(403);
            }
        }


        else if(req.getRequestURL().toString().endsWith("sendmail")){
            if(logService.getrecordcount(LocalDate.now(),req.getRemoteAddr(),RequestDescriptions.SENDMAIL.getText())){
                System.out.println("sendmail Request is passed from filter");
                filterChain.doFilter(servletRequest, servletResponse);

            }
            else{
                System.out.println("sendmail Request cannot pass from Filter");
                ((HttpServletResponse) servletResponse).setStatus(403);
            }
        }


        else if(req.getRequestURL().toString().endsWith("setpassword")){
            if(logService.getrecordcount(LocalDate.now(),req.getRemoteAddr(),RequestDescriptions.SETPASSWORD.getText())){
                System.out.println("setpassword Request is passed from filter");
                filterChain.doFilter(servletRequest, servletResponse);

            }
            else{
                System.out.println("setpassword Request cannot pass from Filter");
                ((HttpServletResponse) servletResponse).setStatus(403);
            }
        }


        else if(req.getRequestURL().toString().endsWith("checkgoogle"))
        {
            if(logService.getrecordcount(LocalDate.now(),req.getRemoteAddr(),RequestDescriptions.GOOGLELOGIN.getText())){
                System.out.println("setpassword Request is passed from filter");
                filterChain.doFilter(servletRequest, servletResponse);

            }
            else{
                System.out.println("setpassword Request cannot pass from Filter");
                ((HttpServletResponse) servletResponse).setStatus(403);
            }
        }
        else
        {
            filterChain.doFilter(servletRequest, servletResponse);

        }


    }




}
