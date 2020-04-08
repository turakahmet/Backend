package com.spring.dao;

import com.spring.model.AppUser;

import java.util.ArrayList;

/**
 * Created by egulocak on 8.04.2020.
 */
public interface UserDAO {

    AppUser insertUser(AppUser user); //Bütün Kullanıcı tiplerini kaydeden fonksiyon;
    ArrayList<AppUser> listAllUsers();//Bütün kullanıcıları listeler
    AppUser updateUser(AppUser user); //Kullanıcı günceller


}
