package com.spring.service;

import com.spring.dao.RestaurantDao;
import com.spring.model.Restaurant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RestaurantServiceImp implements RestaurantService {

    @Autowired
    private RestaurantDao restaurantDao;
    public void setRestaurantDAO(RestaurantDao restaurantDao){
        this.restaurantDao=restaurantDao;
    }
    @Override
    public Restaurant findById(long id) {
        return restaurantDao.findById(id);
    }
    @Override
    public Restaurant findByName(String name) {
        return restaurantDao.findByName(name);
    }

    @Override
    public Restaurant findByCity(String city) {
        return restaurantDao.findByCity(city);
    }

    @Override
    public void Create(Restaurant restaurant) {
        restaurantDao.Create(restaurant);
    }

    @Override
    public void Update(Restaurant restaurant) {
        restaurantDao.Update(restaurant);
    }

    @Override
    public void Delete(long id) {
    }
    @Override
    public List<Restaurant> findAllRestaurant() {
        return restaurantDao.findAllRestaurant();
    }

    @Override
    public void deleteAllRestaurant() {
        restaurantDao.deleteAllRestaurant();
    }

    @Override
    public boolean isRestaurantExist(Restaurant restaurant) {
        return restaurantDao.isRestaurantExist(restaurant);
    }
}
