package com.spring.dao;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import com.spring.model.AdminTK;
import com.spring.model.AppUser;
import com.spring.model.CustomUser;
import com.spring.model.Review;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by egulocak on 8.04.2020.
 */
public interface UserDAO {

    AppUser insertUser(AppUser user); //Bütün Kullanıcı tiplerini kaydeden fonksiyon;
    boolean checkStandardCredentials(String userEmail, String password);
    boolean checkGoogleCredentials(AppUser user);
    CustomUser findUserByEmail(String userEmail,String changestatus);
    List<Object> listAllUsers();//Bütün kullanıcıları listeler
//    AppUser updateUser(AppUser user); //Kullanıcı günceller
    Boolean isUserExist(String email);
    Boolean isUserActive(String email);
    Boolean checkUserCode(String email,long code);
    AppUser updateUserStatus(String email);
    String changeusername(String email,String userName);
    void changeUserCode(String email,long code);
    List<Review> getReview(String email);
    Boolean isadmin(AdminTK adminTK);
    List<Object> getuserreviews(String email);
    Long getreviewcount(String email,String password);
    String changpassword(String email,String password,String newpw);
    List<Object> getcategoryinfo(String email);
    List<Object> getcategorizedreviews(String email,String category);




}
