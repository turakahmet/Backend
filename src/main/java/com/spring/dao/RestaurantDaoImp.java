package com.spring.dao;

import com.spring.model.Restaurant;
import com.spring.model.Review;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import static jdk.nashorn.internal.objects.NativeMath.round;

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

    @Override
    public List<Object> findById(long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createQuery(
                    "select new Map(r.restaurantID as RestaurantID,r.restaurantName as RestaurantName,r.average_review as average_review,r.cuisines as Cuisine,r.restaurantImageUrl as ImageURL,r.locality_verbose as locality_verbose) from Restaurant r where r.restaurantID =:id ");
            query.setParameter("id", id);
            List<Object> restaurantList = query.getResultList();
            transaction.commit();
            return restaurantList;
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return null;

        }
    }


    @Override
    public List<Object> findByName(String name) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createQuery(
                    "select new Map(r.restaurantID as RestaurantID,r.restaurantName as RestaurantName,r.average_review as average_review,r.cuisines as Cuisine,r.restaurantImageUrl as ImageURL,r.locality_verbose as locality_verbose) from Restaurant r where r.restaurantName =:restName ");
            query.setParameter("restName", name);
            List<Object> restaurantList = query.getResultList();
            transaction.commit();
            return restaurantList;
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return null;

        }
    }


    @Override
    public List<Object> findByCity(String city) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createQuery(
                    "select new Map(r.restaurantID as RestaurantID,r.restaurantName as RestaurantName,r.average_review as average_review,r.cuisines as Cuisine,r.restaurantImageUrl as ImageURL,r.locality_verbose as locality_verbose) from Restaurant r where r.city =:city ");
            query.setParameter("city", city);
            List<Object> restaurantList = query.getResultList();
            transaction.commit();
            return restaurantList;
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return null;

        }
    }

    @Override
    public List<Object> findByLocality(String locality) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createQuery(
                    "select new Map(r.restaurantID as RestaurantID,r.restaurantName as RestaurantName,r.average_review as average_review,r.cuisines as Cuisine,r.restaurantImageUrl as ImageURL,r.locality_verbose as locality_verbose) from Restaurant r where r.locality =:locality ");
            query.setParameter("locality", locality);
            List<Object> restaurantList = query.getResultList();
            transaction.commit();
            return restaurantList;
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return null;

        }
    }

    @Override
    public List<Object> votedRestaurantList(long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createQuery(
                    "select new Map(r.reviewID as reviewID,rr.restaurantName as restaurantName,rr.restaurantID as restaurantID,rr.average_review as average_review,rr.locality_verbose as locality_verbose)" +
                            "from Review r inner join r.restaurant rr join r.user ru where ru.userID =:id");
            query.setParameter("id", id);
            List<Object> restaurantList = query.getResultList();
            transaction.commit();
            return restaurantList;
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return null;

        }
    }

    @Override
    public Restaurant detailRestaurant(long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Restaurant restaurant = new Restaurant();
        try {
            Query query = session.createQuery(
                    "select r from Restaurant r where r.restaurantID =:id ", Restaurant.class);
            query.setParameter("id", id);
            restaurant = (Restaurant) query.uniqueResult();
            transaction.commit();
            return restaurant;
        } catch (Exception e) {
            System.out.println(e.getMessage());

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
    public List<Object> findAllRestaurant() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createQuery(
                    "select new Map(r.restaurantID as RestaurantID,r.restaurantName as RestaurantName,r.average_review as average_review,r.cuisines as Cuisine,r.restaurantImageUrl as ImageURL,r.locality_verbose as locality_verbose) from Restaurant r");
            List<Object> restaurantList = query.getResultList();
            transaction.commit();
            return restaurantList;
        } catch (Exception e) {
            System.out.println(e.getMessage());

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

        try {

            sessionFactory.getCurrentSession().save(review);


            Query queryChild = sessionFactory.getCurrentSession().
                    createQuery("select  avg((CHILD_FRIENDLY_1+CHILD_FRIENDLY_2+CHILD_FRIENDLY_3)/3) from Review where restaurantID=:restaurantID");

            queryChild.setParameter("restaurantID", review.getRestaurant().getRestaurantID());


            Query queryDisabled = sessionFactory.getCurrentSession().
                    createQuery("select  avg((disabled_friendly1+disabled_friendly2+disabled_friendly3)/3) from Review where restaurantID=:restaurantID");

            queryDisabled.setParameter("restaurantID", review.getRestaurant().getRestaurantID());


            Query queryHygiene = sessionFactory.getCurrentSession().
                    createQuery("select  avg((HYGIENE1+HYGIENE2+HYGIENE3)/3) from Review where restaurantID=:restaurantID");

            queryHygiene.setParameter("restaurantID", review.getRestaurant().getRestaurantID());

            Double childAvg = (Double) queryChild.uniqueResult();
            Double hygieneAvg = (Double) queryHygiene.uniqueResult();
            Double disabledAvg = (Double) queryDisabled.uniqueResult();
            Double average = (childAvg + disabledAvg + hygieneAvg) / 3;
            updateRestaurantReview(review.getRestaurant().getRestaurantID(), childAvg, hygieneAvg, disabledAvg, average);
        } catch (Exception e) {

        }


    }

    private void updateRestaurantReview(long restaurantID, double childAvg, double hygieneAvg, double disabledAvg, double average) {

        try {

            Session session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();

            Restaurant upRestaurant = (Restaurant) session.get(Restaurant.class, restaurantID);


            upRestaurant.setChild_friendly_review(Math.round(childAvg * 10) / 10.0);
            upRestaurant.setHygiene_review(Math.round(hygieneAvg * 10) / 10.0);
            upRestaurant.setDisabled_friendly_review(Math.round(disabledAvg * 10) / 10.0);
            upRestaurant.setAverage_review(Math.round(average * 10) / 10.0);
            session.update(upRestaurant);
            tx.commit();
            session.close();
        } catch (Exception e) {

        }


    }


    @Override
    public boolean isVoteExist(long userID, long restaurantID) {
        boolean result = false;


        Query query = sessionFactory.getCurrentSession().createQuery("select reviewID FROM Review WHERE userID = :userID and restaurantID= :restaurantID");


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







