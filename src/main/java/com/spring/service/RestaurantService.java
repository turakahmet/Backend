package com.spring.service;
import com.spring.model.Restaurant;
import com.spring.model.Review;

import java.util.ArrayList;
import java.util.List;

public interface RestaurantService {

    Restaurant findById(long id);

    boolean checkRestaurant(long id); //restaurant check

    Restaurant findByName(String name);

    Restaurant findByCity(String city);

    void Create(Restaurant restaurant);

    void Update(Restaurant restaurant);

    void Delete(long id);

    ArrayList<Restaurant> findAllRestaurant();

    void deleteAllRestaurant();


    boolean isRestaurantExist(Restaurant restaurant);

    void voteRestaurant(Review review);

    boolean isVoteExist(long  userID,long restaurantID);
}

