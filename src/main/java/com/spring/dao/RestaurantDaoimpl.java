package com.spring.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by egulocak on 7.04.2020.
 */


@Repository
@Transactional

public class RestaurantDaoimpl implements RestaurantDAO {

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public boolean deleteAllRestaurants() {


        System.out.println("deleteAllRestauranttayım");
        Query query = sessionFactory.getCurrentSession().createSQLQuery("delete FROM Restaurant"); //bütün restaurantları silen sorgu
        query.executeUpdate();
        return true;

//        catch(Exception e){
//
//            System.out.print(e.getMessage()); //catche girerse exceptionu gösterir.
//            return false;
//
//
//        }


    }
}
