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
}