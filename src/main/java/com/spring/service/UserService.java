package com.spring.service;

import com.spring.model.AdminTK;
import com.spring.model.AppUser;
import com.spring.model.CustomUser;
import com.spring.model.Review;
import com.sun.corba.se.impl.oa.poa.AOMEntry;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;
import java.util.List;


public interface UserService {
    String checkUserType(AppUser user);
    AppUser insertUser(AppUser user);
    Boolean isAdminId(String uniqueId);
    List<Object> listAllUsers();
    Boolean isUserExist(String email);
    boolean checkStandardCredentials(String userEmail,String password);
    CustomUser findUserByEmail(String userEmail);
    Boolean checkUserCode(String email,long code);
    AppUser updateUserStatus(String email);
    Boolean isUserActive(String email);
    Boolean isAdmin(AdminTK adminTK);
    Boolean sendmail(String email);
    String getusertype(String email);
    List<Review> getReview(String email);
    String changeusername(AppUser user);
    Long getreviewcount(String email,String password);
    String changepassword(String email,String password,String newpw);
    List<Object> getcategoryinfo(String email);
    Boolean insertpwcode(String email,String code);
    List<Object> getcategorizedreviews(String email,String category);
    Boolean setpassword(String email,String newpw,String token);





    //....
    //....
    //....
    //....

}
