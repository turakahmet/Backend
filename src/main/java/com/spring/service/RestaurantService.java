package com.spring.service;
import com.spring.model.Restaurant;
import com.spring.model.Review;

import java.util.ArrayList;
import java.util.List;

public interface RestaurantService {

    List<Object> findById(long id);

    boolean checkRestaurant(long id); //restaurant check

    List<Object> findByName(String name);

    List<Object> findByCity(String city);

    List<Object> findByLocality(String locality);

    List<Object> votedRestaurantList(long id);

    Restaurant detailRestaurant(long id);


    void Create(Restaurant restaurant);

    void Update(Restaurant restaurant);

    void Delete(long id);

    List<Object> findAllRestaurant();

    void deleteAllRestaurant();


    boolean isRestaurantExist(Restaurant restaurant);

    void voteRestaurant(Review review);

    boolean isVoteExist(long  userID,long restaurantID);
}

