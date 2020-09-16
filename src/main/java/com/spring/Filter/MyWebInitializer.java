package com.spring.Filter;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.*;
import javax.servlet.Filter;

/**
 * Created by egulocak on 26.08.2020.
 */
public class MyWebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {


    @Override
    protected String[] getServletMappings() {
        return new String[0];
    }

    @Override
    protected Filter[] getServletFilters() {
        return super.getServletFilters();
    }

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[0];
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[0];
    }
}
