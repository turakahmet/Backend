package com.spring.dao;

import com.spring.model.Restaurant;
import com.spring.model.Review;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;

public interface RestaurantDao {


    boolean checkRestaurant(long id);

    Object findById(long id); //+

    List<Object> findByName(String name,int page); //+

    List<Object> findByCity(String city,int page); //+

    List<Object> findByLocality(String locality,int page); //+

    List<Object> votedRestaurantList(long id,int page); //+

    List<Object> findAllRestaurant(int page);  //+

    Object detailRestaurant(long id);  //+

    Object detailVote(long id);

    void updateVote(Review review);

    void Create(Restaurant restaurant);

    void Update(Restaurant restaurant);

    void Delete(long id);

    void deleteAllRestaurant();

    boolean isRestaurantExist(Restaurant restaurant);

    void voteRestaurant(Review review);

    boolean isVoteExist(long userID,long restaurantID);

}