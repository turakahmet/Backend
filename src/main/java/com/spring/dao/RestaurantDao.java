package com.spring.dao;

import com.spring.model.Restaurant;
import com.spring.model.Review;
import com.spring.model.UserRecords;
import org.omg.CORBA.OBJ_ADAPTER;

import java.util.ArrayList;
import java.util.List;

public interface RestaurantDao {


    boolean checkRestaurant(long id);

    Object findById(long id);

    List<Object> findByName(String name,int page);

    List<Object> findByCity(String city, int page);

    List<Object> findByLocality(String locality,int page);

    List<Object> votedRestaurantList(long id,int page);

    List<Object> findAllRestaurant(int page);

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
}