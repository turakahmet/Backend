package com.spring.service;

import com.spring.dao.RestaurantDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by egulocak on 7.04.2020.
 */



@Service
@Transactional
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private RestaurantDAO restaurantDao;

    @Override
    public boolean deleteAllRestaurants() {

        return  restaurantDao.deleteAllRestaurants();  // Daodaki ilgili fonksiyona gider.




    }
}
