package com.spring.dao;

import com.google.protobuf.StringValueOrBuilder;
import com.spring.model.AppUser;
import com.spring.model.Restaurant;
import com.spring.model.Review;
import com.spring.model.ReviewScore;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ExceptionDepthComparator;
import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Id;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class RestaurantDaoImp implements RestaurantDao {

    @Setter
    @Autowired
    private SessionFactory sessionFactory;


    @Override
    public boolean checkRestaurant(long id) {
        int result = 0;
        try {
            Query query = sessionFactory.getCurrentSession().
                    createQuery("from Restaurant where restaurantID =: id");
            query.setParameter("id", id);
            result = query.getResultList().size();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println(result);
        if (result > 0)
            return true; //restaurant var
        else
            return false;


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

    @Override
    public Restaurant findRestaurant(long restaurantID) {
                Query query = sessionFactory.getCurrentSession().
                createQuery("select from Restaurant where restaurantID =: restaurantID");
                query.setParameter("restaurantID",restaurantID);
                Restaurant restaurant = new Restaurant();
                 restaurant = (Restaurant) query.uniqueResult();

                return  restaurant;


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

        sessionFactory.getCurrentSession().save(restaurant);
//        if (restaurant != null) {
//            Session session = sessionFactory.openSession();
//            Transaction transaction = session.beginTransaction();
//
//            try {
//                session.save(restaurant);
//                transaction.commit();
//            } catch (Exception e) {
//                transaction.rollback();
//                session.close();
//            }
//        }

    }


    @Override
    public void Update(Restaurant p) {
    }

    @Override
    public void Delete(long id) {
    }

    @Override
    public ArrayList<Restaurant> findAllRestaurant() {

        try {
            Query query = sessionFactory.getCurrentSession().createNativeQuery("select * from Restaurant");
            return (ArrayList<Restaurant>) query.getResultList();
        } catch (Exception e) {

            return null;

        }


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


    @Override
    public void voteRestaurant(Review review) {





        sessionFactory.getCurrentSession().save(review);



        Query queryChild =sessionFactory.getCurrentSession().
                createQuery("select  avg((CHILD_FRIENDLY_1+CHILD_FRIENDLY_2+CHILD_FRIENDLY_3)/3) from Review where restaurantID=:restaurantID");

       queryChild.setParameter("restaurantID",review.getRestaurant().getRestaurantID());


        Query queryDisabled =sessionFactory.getCurrentSession().
                createQuery("select  avg((disabled_friendly1+disabled_friendly2+disabled_friendly3)/3) from Review where restaurantID=:restaurantID");

       queryDisabled.setParameter("restaurantID",review.getRestaurant().getRestaurantID());


        Query queryHygiene =sessionFactory.getCurrentSession().
               createQuery("select  avg((HYGIENE1+HYGIENE2+HYGIENE3)/3) from Review where restaurantID=:restaurantID");

        queryHygiene.setParameter("restaurantID",review.getRestaurant().getRestaurantID());

        Double childAvg = (Double) queryChild.uniqueResult();
        Double hygieneAvg= (Double) queryHygiene.uniqueResult();
        Double disabledAvg= (Double) queryDisabled.uniqueResult();
        Double average = (childAvg + disabledAvg+hygieneAvg)/3;

        ReviewScore reviewScore = new ReviewScore(average,hygieneAvg,childAvg, disabledAvg);




       updateRestaurantReview(review, reviewScore);


    }

    private void updateRestaurantReview(Review review, ReviewScore reviewScore) {


        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Restaurant upRestaurant = (Restaurant) session.get(Restaurant.class, review.getRestaurant().getRestaurantID());


        upRestaurant.setReviewRestaurantScore(reviewScore);
        //update işlemi başlar
        session.update(upRestaurant);
        tx.commit();
        session.close();


    }


    @Override
    public boolean isVoteExist(long userID, long restaurantID) {
        System.out.println("is exist vote");

        Query query = sessionFactory.getCurrentSession().createQuery("select reviewID FROM Review WHERE userID = :userID and restaurantID= :restaurantID");

        boolean result = false;
        query.setParameter("userID", userID);
        query.setParameter("restaurantID", restaurantID);
        if (query.uniqueResult() != null) {
            result = true;
        } else {
            result = false;
        }


        return result;
    }
}







