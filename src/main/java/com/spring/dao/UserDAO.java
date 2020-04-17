package com.spring.dao;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import com.spring.model.AppUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by egulocak on 8.04.2020.
 */
public interface UserDAO {

    AppUser insertUser(AppUser user); //Bütün Kullanıcı tiplerini kaydeden fonksiyon;
    boolean checkStandardCredentials(String userEmail, String password);
    boolean checkGoogleCredentials(AppUser user);
    AppUser findUserByEmail(String userEmail);
    List<Object> listAllUsers();//Bütün kullanıcıları listeler
    AppUser updateUser(AppUser user); //Kullanıcı günceller
    Boolean isUserExist(String email);
      AppUser findByName(String name); // deneme efe sil bunu
}
