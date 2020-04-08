package com.spring.dao;

import com.spring.model.Restaurant;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RestaurantDaoImp implements RestaurantDao {


    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Restaurant findById(long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Restaurant restaurant = new Restaurant();
        try {
            restaurant = (Restaurant) session.get(Restaurant.class, id);
            transaction.commit();
            session.close();
        } catch (Exception e) {
            transaction.rollback();
            session.close();
        }
        return restaurant;
    }


    public Restaurant findByName(String name) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Restaurant restaurant = new Restaurant();
        String hql = "from com.spring.model.Restaurant where name= ?";
        try {
            Query query = session.createNativeQuery(hql);
            //query.setParameter(o,name);
            restaurant = (Restaurant) query.uniqueResult();
            transaction.commit();
            session.close();

        } catch (Exception e) {
            transaction.rollback();
            session.close();
        }

        return restaurant;
    }


    public Restaurant findByCity(String city) {
        String hql = "from Restaurant where city=:city";
        try {
            Query query = sessionFactory.getCurrentSession().createQuery(hql);
            query.setString("city", city);
            System.out.println(query.getResultList().size());
            if (query.getResultList().size() > 0)
                return (Restaurant) query.getSingleResult();
            else
                return null;
        } catch (Exception e) {
            return null;
        }
    }


    @Override
    public void Create(Restaurant restaurant) {
        if(restaurant!= null) {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();

            try {
                session.save(restaurant);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
                session.close();
            }
        }

    }



    @Override
    public void Update(Restaurant p) {
    }

    @Override
    public void Delete(long id) {
    }

    @Override
    public List<Restaurant> findAllRestaurant() {
        return null;
    }

    @Override
    public void deleteAllRestaurant() {
        try {
            System.out.println("deleteAllRestauranttayım");
            Query query = sessionFactory.getCurrentSession().createSQLQuery("delete FROM Restaurant"); //bütün restaurantları silen sorgu
            query.executeUpdate();
        } catch (Exception e) {
            System.out.print(e.getMessage()); //catche girerse exceptionu gösterir.
        }
    }
    @Override
    public boolean isRestaurantExist(Restaurant restaurant) {
        return false;
    }

}
