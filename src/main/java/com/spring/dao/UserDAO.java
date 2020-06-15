package com.spring.dao;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import com.spring.model.AppUser;
import com.spring.model.CustomUser;
import com.spring.model.Review;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by egulocak on 8.04.2020.
 */
public interface UserDAO {

    AppUser insertUser(AppUser user); //Bütün Kullanıcı tiplerini kaydeden fonksiyon;
    boolean checkStandardCredentials(String userEmail, String password);
    boolean checkGoogleCredentials(AppUser user);
    CustomUser findUserByEmail(String userEmail);
    List<Object> listAllUsers();//Bütün kullanıcıları listeler
    AppUser updateUser(AppUser user); //Kullanıcı günceller
    Boolean isUserExist(String email);
    Boolean isUserActive(String email);
    Boolean checkUserCode(String email,long code);
    AppUser updateUserStatus(String email);
    void changeUserCode(String email,long code);
    List<Review> getReview(String email);
    List<Object> getuserreviews(String email);


}
