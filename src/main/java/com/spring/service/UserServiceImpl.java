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
    public Boolean isAdminId(String uniqueId) {
       return userDao.isAdminId(uniqueId);
    }

    @Override
    public List<Object> listAllUsers() {
        return userDao.listAllUsers();
    }

//    @Override
//    public AppUser updateUser(AppUser user) {
//        return userDao.updateUser(user);
//    }

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

    @Override
    public Boolean isAdmin(AdminTK adminTK) {
        return userDao.isadmin(adminTK);
    }

    @Override
    public Boolean sendmail(String email) {
       return  userDao.sendmail(email);
    }

    @Override
    public String getusertype(String email) {
        return  userDao.getusertype(email);
    }

    @Override
    public List<Review> getReview(String email) {
        return userDao.getReview(email);
    }

    @Override
    public String changeusername(AppUser user) {
        return userDao.changeusername(user);
    }

    @Override
    public Long getreviewcount(String email,String password) {
        return userDao.getreviewcount(email,password);
    }

    @Override
    public String changepassword(String email,String password,String newpw) {
        return userDao.changpassword(email,password,newpw);
    }

    @Override
    public List<Object> getcategoryinfo(String email) {
        return userDao.getcategoryinfo(email);
    }

    @Override
    public Boolean insertpwcode(String email,String code) {
      return   userDao.insertpwcode(email,code);
    }

    @Override
    public List<Object> getcategorizedreviews(String email, String category) {
      return  userDao.getcategorizedreviews(email,category);
    }

    @Override
    public Boolean setpassword(String email, String newpw, String token) {
        return userDao.setpassword(email,newpw,token);
    }


}
