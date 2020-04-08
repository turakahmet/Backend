package com.spring.service;

import com.spring.model.AppUser;

import java.util.ArrayList;

/**
 * Created by egulocak on 8.04.2020.
 */
public interface UserService {
    AppUser insertUserWithMail(AppUser user);
    ArrayList<AppUser> listAllUsers();
    AppUser updateUser(AppUser user);
    //....
    //....
    //....
    //....

}
