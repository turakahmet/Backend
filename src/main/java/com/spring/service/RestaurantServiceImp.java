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
    public Object findById(long id) {
        return restaurantDao.findById(id);
    }

    @Override
    public boolean checkRestaurant(long id) {

        return restaurantDao.checkRestaurant(id);
    }

    @Override
    public List<Object> findByName(String name, int page) {
        return restaurantDao.findByName(name,page);
    }

    @Override
    public List<Object> findByCity(String city, int page) {
        return restaurantDao.findByCity(city,page);
    }

    @Override
    public List<Object> findByLocality(String locality, int page) {
        return restaurantDao.findByLocality(locality,page);
    }

    @Override
    public List<Object> votedRestaurantList(long id, int page) {
        return restaurantDao.votedRestaurantList(id,page);
    }


    @Override
    public Object detailRestaurant(long id) {
        return restaurantDao.detailRestaurant(id);
    }

    @Override
    public void updateRestaurantReview(long restaurantID) {
        restaurantDao.updateRestaurantReview(restaurantID);
    }

    @Override
    public Object detailVote(long id) {
        return restaurantDao.detailVote(id);
    }

    @Override
    public void updateVote(Review review) {
         restaurantDao.updateVote(review);
    }

    @Override
    public Object getInfo(long userID, long restaurantID) {
        return restaurantDao.getInfo(userID,restaurantID);
    }

    @Override
    public List<Object> findAllRestaurant(int page) {
        return restaurantDao.findAllRestaurant(page);
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
