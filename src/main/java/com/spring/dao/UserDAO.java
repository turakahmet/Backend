package com.spring.dao;

import com.spring.model.AppUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by egulocak on 8.04.2020.
 */
public interface UserDAO {

    AppUser insertUser(AppUser user); //Bütün Kullanıcı tiplerini kaydeden fonksiyon;
    List<Object> listAllUsers();//Bütün kullanıcıları listeler
    AppUser updateUser(AppUser user); //Kullanıcı günceller
    Boolean isUserExist(String email);

}
