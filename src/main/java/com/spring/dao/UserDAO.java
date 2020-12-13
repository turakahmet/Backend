package com.spring.dao;

import com.spring.model.*;

import java.util.ArrayList;
import java.util.List;


public interface UserDAO {

    void insertUser(AppUser user); //Bütün Kullanıcı tiplerini kaydeden fonksiyon;
    boolean checkStandardCredentials(String userEmail, String password);
    boolean checkGoogleCredentials(AppUser user);
    String getusertype(String email);
    CustomUser findUserByEmail(String userEmail);
    List<Object> listAllUsers();//Bütün kullanıcıları listeler
//    AppUser updateUser(AppUser user); //Kullanıcı günceller
    Boolean isUserExist(String email);
    Boolean isUserActive(String email);
    Boolean isAdminId(String uniqueId);

    Boolean checkUserCode(String email,long code);
    Boolean sendmail(String email);
    void changeUserCode(String email,long code);
    List<Review> getReview(String email);
    Boolean isadmin(AdminTK adminTK);
    List<Object> getuserreviews(String email);
    Long getreviewcount(String email,String password);
    List<Object> getcategoryinfo(String email);
    List<Object> getcategorizedreviews(String email,String category);
    Boolean changeUserImage(long userID, byte[] profileImage, byte[] coverImage);
    byte[] getProfileImage(long userID);
    byte[] getCoverImage(long userID);
    ArrayList<Restaurant> getUserPoints(long userID, int page);
    ArrayList<Restaurant> getUserPlace(long userID, int page);
    ArrayList<Restaurant> getUserFavorite(long userID, int page);

    Boolean setFavoriteRes(FavoritePlace favoriteRes);

    Boolean removeFavorite(FavoritePlace favoritePlace);

    ArrayList<Restaurant> getUserSubCategory(long userID, int category, int page);

    Boolean changeUserName(String email ,String name);

    Boolean changePassword(String email, String password, String newPassword);

    Boolean supportMessage(String email, String body);


    Boolean resetPassword(String email);

    Boolean newPassword(String email, String newPassword);

    Boolean deleteMyPlace(long resID, long userID);

    Boolean invalidPlace(long resID, long userID, String timingValue);

    List<Object> getTopUserList();

    List<Object> getUserDetail(long userID);

    List<Object> getUserReviewDetail(long userID,int page);

    List<Object> getUserAchievement(long userID);

}
