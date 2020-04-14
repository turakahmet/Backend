package com.spring.dao;

import com.spring.model.Restaurant;
import com.spring.model.Review;

import java.util.ArrayList;
import java.util.List;

public interface RestaurantDao {


    boolean checkRestaurant(long id);

    Restaurant findById(long id);

    Restaurant findByName(String name);

    Restaurant findRestaurant(long restaurantID);

    Restaurant findByCity(String city);

    void Create(Restaurant restaurant);

    void Update(Restaurant restaurant);

    void Delete(long id);

    ArrayList<Restaurant> findAllRestaurant();

    void deleteAllRestaurant();

    boolean isRestaurantExist(Restaurant restaurant);

    void voteRestaurant(Review review);

    boolean isVoteExist(long userID,long restaurantID);

}