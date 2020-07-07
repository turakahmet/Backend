package com.spring.service;

import com.spring.model.AppUser;
import com.spring.model.CustomUser;
import com.spring.model.Review;
import com.sun.corba.se.impl.oa.poa.AOMEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by egulocak on 8.04.2020.
 */
public interface UserService {
    String checkUserType(AppUser user);
    AppUser insertUser(AppUser user);
    List<Object> listAllUsers();
    AppUser updateUser(AppUser user);
    Boolean isUserExist(String email);
    boolean checkStandardCredentials(String userEmail,String password);
    CustomUser findUserByEmail(String userEmail,String tokenstatus);
    Boolean checkUserCode(String email,long code);
    AppUser updateUserStatus(String email);
    Boolean isUserActive(String email);
    List<Review> getReview(String email);
    Long getreviewcount(String email);
    String changepassword(String email,String password);
    List<Object> getcategoryinfo(String email);
    List<Object> getcategorizedreviews(String email,String category);






    //....
    //....
    //....
    //....

}
