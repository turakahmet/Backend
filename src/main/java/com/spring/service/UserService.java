package com.spring.service;

import com.spring.model.AdminTK;
import com.spring.model.AppUser;
import com.spring.model.CustomUser;
import com.spring.model.Review;
import com.sun.corba.se.impl.oa.poa.AOMEntry;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by egulocak on 8.04.2020.
 */
public interface UserService {
    String checkUserType(AppUser user);
    AppUser insertUser(AppUser user);
    List<Object> listAllUsers();
    Boolean isUserExist(String email);
    boolean checkStandardCredentials(String userEmail,String password);
    CustomUser findUserByEmail(String userEmail,String tokenstatus);
    Boolean checkUserCode(String email,long code);
    AppUser updateUserStatus(String email);
    Boolean isUserActive(String email);
    Boolean isAdmin(AdminTK adminTK);
    List<Review> getReview(String email);
    String changeusername(String email,String userName);
    Long getreviewcount(String email,String password);
    String changepassword(String email,String password,String newpw);
    List<Object> getcategoryinfo(String email);
    List<Object> getcategorizedreviews(String email,String category);






    //....
    //....
    //....
    //....

}
