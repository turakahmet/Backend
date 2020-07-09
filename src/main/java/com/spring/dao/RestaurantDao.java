package com.spring.dao;

import com.spring.model.*;

import java.util.ArrayList;
import java.util.List;

public interface RestaurantDao {


    boolean checkRestaurant(long id);

    Object findById(long id);

    List<Object> findByName(String name,int page);

    List<Object> findByCity(String city, int page);

    List<Object> findByTown(String town,int page);

    List<Object> votedRestaurantList(long id,int page);

    List<Object> findAllRestaurant(int page);

    List<Object> findAllbyCategory(String category,int page);

    ArrayList<City> getCity();

    ArrayList<Town> getTown(String cityName);

    Object detailRestaurant(long id);

    ArrayList getInfo();

    Object detailVote(long id);

    void updateVote(Review review);

    void updatedetailvote(Review review);


    void detelevote(Review review);

    void Create(Restaurant restaurant);

    void Update(Restaurant restaurant);

    void updateRestaurantReview(long restaurantID);

    void Delete(long id);

    void deleteAllRestaurant();

    boolean isRestaurantExist(Restaurant restaurant);

    void voteRestaurant(Review review);

    boolean isVoteExist(long userID,long restaurantID);

    List<Object> getTopRated(int page,String type);

    void createRecord(UserRecords userRecords);

    void deleteRecordId(long recordId);

    List<Object> findAllRestaurantAdmin(int page);

    void saveRecord(Restaurant restaurant);

    ArrayList<Object> fastPoint(long ResID, double point);

    void fastPointSend(long resID, long userID, double point);

    List<Object> findAllSourceRestaurant(String name, String townName,int page);

    ArrayList<String> compareResults(double oldValueHygiene, double oldValueCleaning,long ResID);

    void arrowPointSend(long resID, int cleaningArrow, int hygieneArrow);

    void reportSend(long resID,long UserID, int reportID);

    boolean isReportExist(long resID,long UserID);

    void updateReportSend(long resID, int reportID);

    boolean adminCheck(AdminTK adminTK);

    List<Object> getEnYakin(Double enlem, Double boylam);

    List<Object> filter(Filter filter);
}