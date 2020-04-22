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

import java.util.List;



@Repository
public class RestaurantDaoImp implements RestaurantDao {

    @Setter
    @Autowired
    private SessionFactory sessionFactory;

    int pageSize =5;


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
    public Object findById(long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createQuery(
                    "select new Map(r.restaurantID as restaurantID,r.restaurantName as restaurantName,r.average_review as reviewScore,r.cuisines as cuisines,r.restaurantImageUrl as restaurantImageUrl,r.locality_verbose as localityVerbose," +
                            "r.latitude as rLatitude,r.longitude as rLongitude) from Restaurant r where r.restaurantID =:id ");
            query.setParameter("id", id);
            Object restaurant = query.uniqueResult();
            transaction.commit();
            return restaurant;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    @Override
    public List<Object> findByName(String name, int page) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createQuery(
                    "select new Map(r.restaurantID as restaurantID,r.restaurantName as restaurantName,r.average_review as reviewScore,r.cuisines as cuisines,r.restaurantImageUrl as restaurantImageUrl,r.locality_verbose as localityVerbose," +
                            "r.latitude as rLatitude,r.longitude as rLongitude) " +
                            "from Restaurant r where lower(r.restaurantName) like lower(concat('%',:restName,'%'))").setFirstResult(pageSize*(page-1)).setMaxResults(pageSize);
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
    public List<Object> findByCity(String city, int page) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createQuery(
                    "select new Map(r.restaurantID as RestaurantID,r.restaurantName as restaurantName,r.average_review as reviewScore,r.cuisines as cuisines,r.restaurantImageUrl as restaurantImageUrl,r.locality_verbose as localityVerbose," +
                            "r.latitude as rLatitude,r.longitude as rLongitude)" +
                            " from Restaurant r where r.city =:city ").setFirstResult(pageSize*(page-1)).setMaxResults(pageSize);
            query.setParameter("city", city);
            List restaurantList = query.list();
            transaction.commit();
            return restaurantList;
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return null;

        }
    }

    @Override
    public List<Object> findByLocality(String locality, int page) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createQuery(
                    "select new Map(r.restaurantID as RestaurantID,r.restaurantName as restaurantName,r.average_review as reviewScore,r.cuisines as cuisines,r.restaurantImageUrl as restaurantImageUrl,r.locality_verbose as localityVerbose," +
                            "r.latitude as rLatitude,r.longitude as rLongitude)" +
                            " from Restaurant r where r.locality =:locality").setFirstResult(pageSize*(page-1)).setMaxResults(pageSize);
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
    public List<Object> votedRestaurantList(long id, int page) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createQuery(
                    "select new Map(r.reviewID as reviewID,rr.restaurantName as restaurantName,rr.restaurantID as restaurantID,rr.average_review as reviewScore,rr.locality_verbose as localityVerbose,rr.restaurantImageUrl as restaurantImageUrl,r.reviewDate as reviewDate," +
                            "r.latitude as rLatitude,r.longitude as rLongitude)" +
                            "from Review r inner join r.restaurant rr join r.user ru where ru.userID =:id").setFirstResult(pageSize*(page-1)).setMaxResults(pageSize);
            query.setParameter("id", id);
            List<Object> restaurantList = query.getResultList();
            transaction.commit();
            session.close();
            return restaurantList;
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return null;

        }
    }

    @Override
    public List<Object> findAllRestaurant(int page) {
        try {
            Query query = sessionFactory.getCurrentSession().createQuery("select new Map(r.restaurantID as restaurantID,r.restaurantName as restaurantName,r.average_review as reviewScore,r.cuisines as cuisines,r.restaurantImageUrl as restaurantImageUrl," +
                    "r.locality_verbose as localityVerbose,r.latitude as rLatitude,r.longitude as rLongitude) from Restaurant r").setFirstResult(pageSize*(page-1)).setMaxResults(pageSize);

            List<Object> restaurantList = query.getResultList();
            return restaurantList;
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return null;

        }
    }

    @Override
    public Object detailRestaurant(long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createQuery(
                    "select new Map(r.restaurantID as restaurantID,r.restaurantName as restaurantName,r.cuisines as cuisines,r.address as address,r.phone_number as phone_number," +
                            "r.locality_verbose as localityVerbose,r.review_count as review_count,r.average_review as reviewScore,r.disabled_friendly_review as disabled_friendly_review," +
                            "r.child_friendly_review as child_friendly_review) from Restaurant r where r.restaurantID =:id");

            query.setParameter("id",id);
            Object restaurant =  query.uniqueResult();
            transaction.commit();
            return restaurant;
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return null;

        }
    }

    @Override
    public Object detailVote(long id) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createQuery(
                    "select new Map(r.reviewID as reviewID,r.child_friendly_1 as child_friendly_1,r.child_friendly_2 as child_friendly_2," +
                            "r.child_friendly_3 as child_friendly_3,r.disabled_friendly1 as disabled_friendly1,r.disabled_friendly2 as disabled_friendly2,r.disabled_friendly3 as disabled_friendly3," +
                            "r.hygiene1 as hygiene1,r.hygiene2 as hygiene2,r.hygiene3 as hygiene3,cast(r.reviewDate as string) as reviewDate,rr.restaurantID as restaurantID,rr.restaurantName as restaurantName,ru.userID as userID)" +
                            " from Review r inner join r.restaurant rr inner join r.user ru where r.reviewID = :id");
            query.setParameter("id",id);
            Object restaurant =  query.uniqueResult();
            transaction.commit();
            return restaurant;
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return null;

        }
    }

    @Override
    public void updateVote(Review review) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.update(review);
            transaction.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());

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
    public void updateRestaurantReview(long restaurantID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query queryChild = session.
                createQuery("select  avg((CHILD_FRIENDLY_1+CHILD_FRIENDLY_2+CHILD_FRIENDLY_3)/3) from Review where restaurantID=:restaurantID");
        queryChild.setParameter("restaurantID",restaurantID);


        Query queryDisabled = session.
                createQuery("select  avg((disabled_friendly1+disabled_friendly2+disabled_friendly3)/3) from Review where restaurantID=:restaurantID");
        queryDisabled.setParameter("restaurantID", restaurantID);


        Query queryHygiene = session.
                createQuery("select  avg((HYGIENE1+HYGIENE2+HYGIENE3)/3) from Review where restaurantID=:restaurantID");
        queryHygiene.setParameter("restaurantID", restaurantID);

        Double childAvg = (Double) queryChild.uniqueResult();
        Double hygieneAvg = (Double) queryHygiene.uniqueResult();
        Double disabledAvg = (Double) queryDisabled.uniqueResult();
        Double average = (childAvg + disabledAvg + hygieneAvg) / 3;

        Query query=session.createQuery("select count(rr.restaurantID) from Review r inner join r.restaurant rr where rr.restaurantID = :id");
        query.setParameter("id",restaurantID);
        Long review_count= (Long) query.uniqueResult();
        System.out.print(review_count);

        Restaurant upRestaurant = (Restaurant)session.get(Restaurant.class, restaurantID);

        upRestaurant.setChild_friendly_review(Math.round(childAvg * 10) / 10.0);
        upRestaurant.setHygiene_review(Math.round(hygieneAvg * 10) / 10.0);
        upRestaurant.setDisabled_friendly_review(Math.round(disabledAvg * 10) / 10.0);
        upRestaurant.setAverage_review(Math.round(average * 10) / 10.0);
        upRestaurant.setReview_count(review_count);
        transaction.commit();
        session.close();

    }


    @Override
    public void Delete(long id) {
        Query query = sessionFactory.getCurrentSession()
                .createQuery("delete FROM  Restaurant  where restaurantID=:id");
        query.setParameter("id",id);
        query.executeUpdate();
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
            session.save(review);
            transaction.commit();
            session.close();

        } catch (Exception e) {
            System.out.print(e.getMessage());
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

        System.out.print(result);
        return result;
    }
}







