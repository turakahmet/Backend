package com.spring.token;

import com.spring.model.Review;
import com.sun.org.apache.xpath.internal.operations.Bool;


public interface Validation {
     Boolean isvalidate(Token token);
    Boolean isValidateAction(Review review,String email,String password);
    Boolean isValidateRequest(String email,String token);
    Boolean isValidateGoogle(String email,String token);
    Boolean isValidateGoogleAction(Review review,String email,String token);
    String generatetoken();


    Boolean votevalidator(long id,String email,String password);
    Boolean votevalidatorforgoogle(long id,String email,String token);
    }

