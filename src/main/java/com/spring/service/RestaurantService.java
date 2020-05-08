package com.spring.service;
import com.spring.model.Restaurant;
import com.spring.model.Review;

import java.util.ArrayList;
import java.util.List;

public interface RestaurantService {

    Object findById(long id);

    boolean checkRestaurant(long id); //restaurant check

    List<Object> findByName(String name,int page);

    List<Object> findByCity(String city,int page);

    List<Object> findByLocality(String locality,int page);

    List<Object> votedRestaurantList(long id,int page);

    Object detailRestaurant(long id);

    void updateRestaurantReview(long restaurantID);

    Object detailVote(long id);

    void updateVote(Review review);

    Object getInfo(long userID, long restaurantID);

    List<Object> findAllRestaurant(int page);

    void Create(Restaurant restaurant);

    void Update(Restaurant restaurant);

    void Delete(long id);

    void deleteAllRestaurant();

    List<Object> getTopRated(int page);
    boolean isRestaurantExist(Restaurant restaurant);

    void voteRestaurant(Review review);

    boolean isVoteExist(long  userID,long restaurantID);
}

