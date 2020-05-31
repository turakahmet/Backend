package com.spring.service;

import com.spring.dao.RestaurantDao;
import com.spring.model.Restaurant;
import com.spring.model.Review;
import com.spring.model.UserRecords;
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
    public List<Object> findByTown(String town, int page) {
        return restaurantDao.findByTown(town,page);
    }

    @Override
    public List<Object> votedRestaurantList(long id, int page) {
        return restaurantDao.votedRestaurantList(id,page);
    }

    @Override
    public List<Object> findAllbyCategory(String category, int page) {
        return restaurantDao.findAllbyCategory(category,page);
    }

    @Override
    public List<Object> getCity() {
        return restaurantDao.getCity();
    }

    @Override
    public List<Object> getTown(String cityName) {
        return restaurantDao.getTown(cityName);
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
    public ArrayList getInfo() {
        return restaurantDao.getInfo();
    }

    @Override
    public List<Object> findAllRestaurant(int page) {
        return restaurantDao.findAllRestaurant(page);
    }

    @Override
    public List<Object> findAllRestaurantAdmin(int page) {
        return restaurantDao.findAllRestaurantAdmin(page);
    }

    @Override
    public void Create(Restaurant restaurant) {
        restaurantDao.Create(restaurant);
    }

    @Override
    public void createRecord(UserRecords userRecords) {
        restaurantDao.createRecord(userRecords);
    }

    @Override
    public void deleteRecordId(long recordId) {
        restaurantDao.deleteRecordId(recordId);
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
    public List<Object> getTopRated(int page,String type) {
        return restaurantDao.getTopRated(page,type);
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

    @Override
    public void saveRecord(Restaurant restaurant) {
        restaurantDao.saveRecord(restaurant);
    }


}
