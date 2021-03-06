package com.spring.service;
import com.spring.model.*;

import java.util.ArrayList;
import java.util.List;

public interface RestaurantService {

    Object findById(long id);

    boolean checkRestaurant(long id); //restaurant check

    List<Object> findByName(String name,int page);

    List<Object> findByCity(String city,String category,int page);

    List<Object> findByTown(String town,int page);

    List<Object> votedRestaurantList(long id,int page);

    List<Object> findAllbyCategory(String category,int page,double enlem, double boylam);

    ArrayList<City> getCity();

    ArrayList<Town> getTown(String cityName);


    Object detailRestaurant(long id);

    void updateRestaurantReview(long restaurantID);

    Object detailVote(long id);

    void updateVote(Review review);

    void updatedetailvote(Review review);


    void deleteVote(Review review);


    ArrayList getInfo();

    List<Object> findAllRestaurant(int page,double enlem, double boylam);

    List<Object> findAllRestaurantAdmin(int page);

    void Create(Restaurant restaurant);

    void createRecord(UserRecords userRecords);

    void deleteRecordId(long recordId);

    void Update(Restaurant restaurant);

    void Delete(long id);


    List<Object> getTopRated(int page,String type);
    boolean isRestaurantExist(Restaurant restaurant);

    void voteRestaurant(Review review);

    boolean isVoteExist(long  userID,long restaurantID);

    void saveRecord(Restaurant restaurant,long resID, long userID);

    ArrayList<Object> fastPoint(long ResID, double point);

    void fastPointSend(long resID, long userID, double point);

    List<Object> findAllSourceRestaurant(String name, String townName,String cityName,int page);

    ArrayList<String> compareResults(double oldValueHygiene, double oldValueCleaning,long ResID);

    void arrowPointSend(long resID, int cleaningArrow, int hygieneArrow);

    void reportSend(long resID,long UserID, int reportID);

    boolean isReportExist(long ResID,long UserID);

    void updateReportSend(long resID, int reportID);

    boolean adminCheck(AdminTK adminTK);

    List<Object> getEnYakin(Double enlem, Double boylam);

    List<Object> filter(Filter filter);

    Boolean sendDeviceToken(String token);

    List<Object> getRandomPlaces(int page);

    Boolean sendNewEvent(String token);

    Boolean getDeviceEventStatus(String token);

    List<Long> getUserSummary(long userID);

    Boolean deleteLog(DeletePlaceLog deletePlaceLog);

    List<Object> sharePlace(long resID);

    void setUserAchievement(long userID);
}

