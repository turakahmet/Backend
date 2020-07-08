package com.spring.service;

import com.spring.dao.UserDAO;
import com.spring.model.AdminTK;
import com.spring.model.AppUser;
import com.spring.model.CustomUser;
import com.spring.model.Review;
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
    public CustomUser findUserByEmail(String userEmail,String tokenstatus) {

        return userDao.findUserByEmail(userEmail,tokenstatus);
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

    @Override
    public Boolean isAdmin(AdminTK adminTK) {
        return userDao.isadmin(adminTK);
    }

    @Override
    public List<Review> getReview(String email) {
        return userDao.getReview(email);
    }

    @Override
    public Long getreviewcount(String email) {
        return userDao.getreviewcount(email);
    }

    @Override
    public String changepassword(String email,String password) {
        return userDao.changpassword(email,password);
    }

    @Override
    public List<Object> getcategoryinfo(String email) {
        return userDao.getcategoryinfo(email);
    }

    @Override
    public List<Object> getcategorizedreviews(String email, String category) {
      return  userDao.getcategorizedreviews(email,category);
    }


}
