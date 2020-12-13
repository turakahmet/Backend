package com.spring.service;

import com.spring.dao.UserDAO;
import com.spring.model.*;
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
    public void insertUser(AppUser user) {
        userDao.insertUser(user);
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
    public List<Object> getcategoryinfo(String email) {
        return userDao.getcategoryinfo(email);
    }



    @Override
    public List<Object> getcategorizedreviews(String email, String category) {
      return  userDao.getcategorizedreviews(email,category);
    }



    @Override
    public Boolean changeUserImage(long userID, byte[] profileImage, byte[] coverImage) {
        return userDao.changeUserImage(userID,profileImage,coverImage);
    }

    @Override
    public byte[] getProfileImage(long userID) {
        return userDao.getProfileImage(userID);
    }

    @Override
    public byte[] getCoverImage(long userID) {
        return userDao.getCoverImage(userID);
    }

    @Override
    public ArrayList<Restaurant> getUserPoints(long userID, int page) {
        return userDao.getUserPoints(userID,page);
    }

    @Override
    public ArrayList<Restaurant> getUserPlace(long userID, int page) {
        return userDao.getUserPlace(userID,page);
    }

    @Override
    public ArrayList<Restaurant> getUserFavorite(long userID, int page) {
        return userDao.getUserFavorite(userID,page);
    }

    @Override
    public Boolean setFavoriteRes(FavoritePlace favoriteRes) {
        return userDao.setFavoriteRes(favoriteRes);
    }

    @Override
    public Boolean removeFavorite(FavoritePlace favoritePlace) {
        return userDao.removeFavorite(favoritePlace);
    }

    @Override
    public ArrayList<Restaurant> getUserSubCategory(long userID, int category, int page) {
        return userDao.getUserSubCategory(userID,category,page);
    }

    @Override
    public Boolean changeUserName(String email, String name) {
        return userDao.changeUserName(email,name);
    }

    @Override
    public Boolean changePassword(String email, String password, String newPassword) {
        return userDao.changePassword(email,password,newPassword);
    }

    @Override
    public Boolean supportMessage(String email, String body) {
        return userDao.supportMessage(email,body);
    }

    @Override
    public Boolean resetPassword(String email) {
        return userDao.resetPassword(email);
    }


    @Override
    public Boolean newPassword(String email, String newPassword) {
        return userDao.newPassword(email,newPassword);
    }

    @Override
    public Boolean deleteMyPlace(long resID, long userID) {
        return userDao.deleteMyPlace(resID,userID);
    }

    @Override
    public Boolean invalidPlace(long resID, long userID ,String timingValue) {
        return userDao.invalidPlace(resID,userID,timingValue);
    }

    @Override
    public List<Object> getTopUserList() {
        return userDao.getTopUserList();
    }

    @Override
    public List<Object> getUserDetail(long userID) {
        return userDao.getUserDetail(userID);
    }

    @Override
    public List<Object> getUserReviewDetail(long userID,int page) {
        return userDao.getUserReviewDetail(userID, page);
    }

    @Override
    public List<Object> getUserAchievement(long userID) {
        return userDao.getUserAchievement(userID);
    }


}
