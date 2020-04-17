package com.spring.service;

import com.spring.dao.RestaurantDao;
import com.spring.model.Restaurant;
import com.spring.model.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    public List<Object> findById(long id) {
        return restaurantDao.findById(id);
    }

    @Override
    public boolean checkRestaurant(long id) {

        return restaurantDao.checkRestaurant(id);
    }

    @Override
    public List<Object> findByName(String name) {
        return restaurantDao.findByName(name);
    }

    @Override
    public List<Object> findByCity(String city) {
        return restaurantDao.findByCity(city);
    }

    @Override
    public List<Object> findByLocality(String locality) {
        return restaurantDao.findByLocality(locality);
    }

    @Override
    public List<Object> votedRestaurantList(long id) {
        return restaurantDao.votedRestaurantList(id);
    }

    @Override
    public Restaurant detailRestaurant(long id) {
        return restaurantDao.detailRestaurant(id);
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
        restaurantDao.Delete(id);
    }

    @Override
    public List<Object> findAllRestaurant() {
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

    @Override
    public void voteRestaurant(Review review) {
        restaurantDao.voteRestaurant(review);
    }

    @Override
    public boolean isVoteExist(long userID,long restaurantID) {
        return restaurantDao.isVoteExist(userID,restaurantID);
    }


}
