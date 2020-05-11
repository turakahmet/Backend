package com.spring.service;

import com.spring.dao.UserDAO;
import com.spring.model.AppUser;
import com.spring.model.CustomUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by egulocak on 8.04.2020.
 */


@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDao;


    @Override
    public String checkUserType(AppUser user) {
        if(user.getUserType().equals("google"))
            return "google";
        else if(user.getUserType().equals("standard"))
            return "standard";

        else
            return "facebook";

    }

    @Override
    public AppUser insertUser(AppUser user) {
        return userDao.insertUser(user);
    }

    @Override
    public List<Object> listAllUsers() {
        return userDao.listAllUsers();
    }

    @Override
    public AppUser updateUser(AppUser user) {
        return userDao.updateUser(user);
    }

    @Override
    public Boolean isUserExist(String email) {
        return userDao.isUserExist(email);
    }

    @Override
    public boolean checkStandardCredentials(String userEmail,String password) {
        return userDao.checkStandardCredentials(userEmail,password);
    }

    @Override
    public CustomUser findUserByEmail(String userEmail) {
        return userDao.findUserByEmail(userEmail);
    }

    @Override
    public Boolean checkUserCode(String email, long code) {
        return userDao.checkUserCode(email,code);
    }

    @Override
    public AppUser updateUserStatus(String email) {
        return  userDao.updateUserStatus(email);
    }

    @Override
    public Boolean isUserActive(String email) {
        return userDao.isUserActive(email);
    }
}
