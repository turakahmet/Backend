package com.spring.dao;

import com.spring.model.Restaurant;

import java.util.List;

public interface RestaurantDao {

    Restaurant findById(long id);

    Restaurant findByName(String name);

    Restaurant findByCity(String city);

    void Create(Restaurant restaurant);

    void Update(Restaurant restaurant);

    void Delete(long id);

    List<Restaurant>findAllRestaurant();

    void deleteAllRestaurant();

    boolean isRestaurantExist(Restaurant restaurant);
}