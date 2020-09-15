package com.spring.Filter;

import com.spring.requestenum.RequestDescriptions;
import com.spring.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import javax.servlet.*;
import javax.servlet.Filter;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

/**
 * Created by egulocak on 26.08.2020.
 */

@WebFilter("/restaurant/*")
@Component
public class RestaurantFilter  implements Filter {

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


        if (req.getRequestURL().toString().endsWith("/voteRestaurant")) {

            //Günde tek ip maks istek sayısı 10

            if (logService.getrecordcount(LocalDate.now(),req.getRemoteAddr(), RequestDescriptions.VOTERESTAURANT.getText())) {
                System.out.println("Request is passed from filter");
                filterChain.doFilter(servletRequest, servletResponse);
            } else {

                System.out.println("Request cannot pass from Filter");


                ((HttpServletResponse) servletResponse).setStatus(403);
            }
        }
        else if(req.getRequestURL().toString().endsWith("updatevote")){
            if (logService.getrecordcount(LocalDate.now(),req.getRemoteAddr(), RequestDescriptions.UPDATEVOTE.getText())){
                System.out.println("Request is passed from filter");

                filterChain.doFilter(servletRequest, servletResponse);

            }
            else {

                System.out.println("Request cannot pass from Filter");


                ((HttpServletResponse) servletResponse).setStatus(403);
            }
        }


        else if(req.getRequestURL().toString().endsWith("getRecord"))
        {
            if (logService.getrecordcount(LocalDate.now(),req.getRemoteAddr(), RequestDescriptions.GETRECORD.getText())){
                System.out.println("Request is passed from filter");

                filterChain.doFilter(servletRequest, servletResponse);

            }
            else {

                System.out.println("Request cannot pass from Filter");


                ((HttpServletResponse) servletResponse).setStatus(403);
            }
        }

        else if(req.getRequestURL().toString().endsWith("saveRecord"))
        {
            if (logService.getrecordcount(LocalDate.now(),req.getRemoteAddr(), RequestDescriptions.SAVERECORD.getText())){
                System.out.println("Request is passed from filter");

                filterChain.doFilter(servletRequest, servletResponse);

            }
            else {

                System.out.println("Request cannot pass from Filter");


                ((HttpServletResponse) servletResponse).setStatus(403);
            }
        }

        else
        {
            filterChain.doFilter(servletRequest,servletResponse);
        }




    }
}
