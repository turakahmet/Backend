package com.spring.service;

import com.spring.model.*;
import com.sun.corba.se.impl.oa.poa.AOMEntry;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.ArrayList;
import java.util.List;


public interface UserService {
    String checkUserType(AppUser user);
    void insertUser(AppUser user);
    Boolean isAdminId(String uniqueId);
    List<Object> listAllUsers();
    Boolean isUserExist(String email);
    boolean checkStandardCredentials(String userEmail,String password);
    CustomUser findUserByEmail(String userEmail);
    Boolean checkUserCode(String email,long code);
    Boolean isUserActive(String email);
    Boolean isAdmin(AdminTK adminTK);
    Boolean sendmail(String email);
    String getusertype(String email);
    List<Review> getReview(String email);
    List<Object> getcategoryinfo(String email);
    List<Object> getcategorizedreviews(String email,String category);
    Boolean changeUserImage(long userID, byte[] profilImageID, byte[] coverImage);
    byte[] getProfileImage(long userID);
    byte[] getCoverImage(long userID);
    ArrayList<Restaurant> getUserPoints(long userID,int page);
    ArrayList<Restaurant> getUserPlace(long userID, int page);
    ArrayList<Restaurant> getUserFavorite(long userID, int page);
    Boolean setFavoriteRes(FavoritePlace favoritePlace);

    Boolean removeFavorite(FavoritePlace favoritePlace);

    ArrayList<Restaurant> getUserSubCategory(long userID, int category, int page);

    Boolean changeUserName(String email, String name);

    Boolean changePassword(String email, String password, String newPassword);

    Boolean supportMessage(String email, String body);

    Boolean resetPassword(String email);

    Boolean newPassword(String email, String newPassword);

    Boolean deleteMyPlace(long resID, long userID);

    Boolean invalidPlace(long resID, long userID, String timingValue);

    List<Object> getTopUserList();

    List<Object> getUserDetail(long userID);

    List<Object> getUserReviewDetail(long userID, int page);

    List<Object> getUserAchievement(long userID);


}
