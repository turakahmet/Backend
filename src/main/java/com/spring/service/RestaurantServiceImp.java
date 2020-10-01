package com.spring.service;

import com.spring.dao.RestaurantDao;
import com.spring.model.*;
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

    public void setRestaurantDAO(RestaurantDao restaurantDao) {
        this.restaurantDao = restaurantDao;
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
        return restaurantDao.findByName(name, page);
    }

    @Override
    public List<Object> findByCity(String city, String category, int page) {
        return restaurantDao.findByCity(city, category, page);
    }

    @Override
    public List<Object> findByTown(String town, int page) {
        return restaurantDao.findByTown(town, page);
    }

    @Override
    public List<Object> votedRestaurantList(long id, int page) {
        return restaurantDao.votedRestaurantList(id, page);
    }

    @Override
    public List<Object> findAllbyCategory(String category, int page) {
        return restaurantDao.findAllbyCategory(category, page);
    }

    @Override
    public ArrayList<City> getCity() {
        return restaurantDao.getCity();
    }

    @Override
    public ArrayList<Town> getTown(String cityName) {
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
    public void updatedetailvote(Review review) {

        restaurantDao.updatedetailvote(review);

    }

    @Override
    public void deleteVote(Review review) {
        restaurantDao.detelevote(review);

    }

    @Override
    public ArrayList getInfo() {
        return restaurantDao.getInfo();
    }

    @Override
    public List<Object> findAllRestaurant(int page, String cityName,String townName) {
        return restaurantDao.findAllRestaurant(page,cityName,townName);
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
    public List<Object> getTopRated(int page, String type) {
        return restaurantDao.getTopRated(page, type);
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
    public boolean isVoteExist(long userID, long restaurantID) {
        return restaurantDao.isVoteExist(userID, restaurantID);
    }

    @Override
    public void saveRecord(Restaurant restaurant) {
        restaurantDao.saveRecord(restaurant);
    }

    @Override
    public ArrayList<Object> fastPoint(long ResID, double point) {
        return restaurantDao.fastPoint(ResID, point);
    }

    ///
    @Override
    public void fastPointSend(long resID, long userID, double point) {
        restaurantDao.fastPointSend(resID, userID, point);
    }

    @Override
    public List<Object> findAllSourceRestaurant(String name, String townName, String cityName, int page) {
        return restaurantDao.findAllSourceRestaurant(name, townName,cityName, page);
    }

    @Override
    public ArrayList<String> compareResults(double oldValueHygiene, double oldValueCleaning, long ResID) {
        return restaurantDao.compareResults(oldValueHygiene, oldValueCleaning, ResID);
    }

    @Override
    public void arrowPointSend(long resID, int cleaningArrow, int hygieneArrow) {
        restaurantDao.arrowPointSend(resID, cleaningArrow, hygieneArrow);
    }

    @Override
    public void reportSend(long resID, long UserID, int reportID) {
        restaurantDao.reportSend(resID, UserID, reportID);
    }

    @Override
    public boolean isReportExist(long ResID, long UserID) {
        return restaurantDao.isReportExist(ResID, UserID);
    }

    @Override
    public void updateReportSend(long resID, int reportID) {
        restaurantDao.updateReportSend(resID, reportID);
    }

    @Override
    public boolean adminCheck(AdminTK adminTK) {
        return restaurantDao.adminCheck(adminTK);
    }

    @Override
    public List<Object> getEnYakin(Double enlem, Double boylam) {
        return restaurantDao.getEnYakin(enlem, boylam);
    }

    @Override
    public List<Object> filter(Filter filter) {
        return restaurantDao.filter(filter);
    }

    @Override
    public Boolean sendDeviceToken(String token) {
         return restaurantDao.sendDeviceToken(token);
    }

    @Override
    public List<Object> getRandomPlaces(int page) {
        return restaurantDao.getRandomPlaces(page);
    }

    @Override
    public Boolean sendNewEvent(String token) {
        return restaurantDao.sendNewEvent(token);
    }

    @Override
    public Boolean getDeviceEventStatus(String token) {
        return restaurantDao.getDeviceEventStatus(token);
    }


}
