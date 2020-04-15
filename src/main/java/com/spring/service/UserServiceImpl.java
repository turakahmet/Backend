package com.spring.service;

import com.spring.dao.UserDAO;
import com.spring.model.AppUser;
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
    public AppUser insertUserWithMail(AppUser user) {
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
}
