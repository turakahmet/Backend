package com.spring.dao;

import com.google.protobuf.StringValueOrBuilder;
import com.spring.model.AppUser;
import com.spring.model.Restaurant;
import com.spring.model.Review;
import com.spring.model.ReviewScore;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class RestaurantDaoImp implements RestaurantDao {


    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean checkRestaurant(long id) {
        int result=0;
        try {
            Query query = sessionFactory.getCurrentSession().
                    createQuery("from Restaurant where restaurantID =: id");
            query.setParameter("id",id);
           result = query.getResultList().size();
        }
        catch (Exception e){
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
        if (restaurant != null) {
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


    @Override
    public void voteRestaurant(Review review) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            double aHygieneScore = Math.round(((review.getHygiene1() + review.getHygiene2() + review.getHygiene3()) / 3) * 10.0) / 10.0;
            double aChildrenScore = Math.round(((review.getChild_friendly_1() + review.getChild_friendly_2() + review.getChild_friendly_3()) / 3) * 10.0) / 10.0;
            double aDisabledScore = Math.round(((review.getDisabled_friendly1() + review.getDisabled_friendly2() + review.getDisabled_friendly3()) / 3) * 10.0) / 10.0;
            double averageScore = Math.round(((aHygieneScore + aChildrenScore + aDisabledScore) / 3) * 10.0) / 10.0;

            ReviewScore reviewScore = new ReviewScore(averageScore, aHygieneScore, aChildrenScore, aDisabledScore);
            review.setReviewRestaurantScore(reviewScore);
            session.save(review);
            transaction.commit();
        } catch (Exception e) {
            transaction.rollback();
            session.close();
        }
    }

    @Override
    public boolean isVoteExist(long userID, long restaurantID) {

        try {


            Query query = sessionFactory.getCurrentSession().
                    createQuery("from Review where restaurantID =:restaurantID and userID =:userID");
            query.setParameter("restaurantID", restaurantID);
            query.setParameter("userID", userID);


            if (query.getResultList().size() > 0)
                return true;

            else return false;
        } catch (Exception e) {

            System.out.println(e.getMessage());
            return false;
        }


    }

}
