package com.spring.token;

import com.sun.org.apache.xpath.internal.operations.Bool;

/**
 * Created by egulocak on 6.07.2020.
 */
public interface Validation {
     Boolean isvalidate(Token token);

    String generatetoken();
    }

