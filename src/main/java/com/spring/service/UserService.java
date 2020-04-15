package com.spring.service;

import com.spring.model.AppUser;
import com.sun.corba.se.impl.oa.poa.AOMEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by egulocak on 8.04.2020.
 */
public interface UserService {
    AppUser insertUserWithMail(AppUser user);
    List<Object> listAllUsers();
    AppUser updateUser(AppUser user);
    Boolean isUserExist(String email);
    //....
    //....
    //....
    //....

}
