package com.spring.Filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by egulocak on 21.08.2020.
 */
public class Filter {

//
//    private FilterConfig config =null;
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//
//        this.config = config;  //filtre initialize etme
//
//        config.getServletContext().log("Unitializing session checker filter");
//
//        System.out.println("Initializing session checker");
//
//
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//
//        System.out.println("Filterdaki URL:"+request.getRemoteAddr());
//    }
//
//    @Override
//    public void destroy() {
//            System.out.println("Destryonmgi filter ");
//    }
}
