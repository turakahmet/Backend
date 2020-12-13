package com.spring.dao;

import com.spring.model.*;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;


@Repository
public class RestaurantDaoImp implements RestaurantDao {

    @Setter
    @Autowired
    private SessionFactory sessionFactory;
    //points
    private final double q1Coef = 0.14, q2Coef = 0.12, q3Coef = 0.09, q4Coef = 0.09, q5Coef = 0.08, q6Coef = 0.15, q7Coef = 0.12, q8Coef = 0.11, q9Coef = 0.1;
    private final double q1CategoryCoef = 0.31, q2CategoryCoef = 0.24, q3CategoryCoef = 0.22, q4CategoryCoef = 0.14, q5CategoryCoef = 0.13, q6CategoryCoef = 0.27, q7CategoryCoef = 0.27, q8CategoryCoef = 0.22, q9CategoryCoef = 0.2;
    double friendlyAverage, hygieneAverage, average;
    private final double fQ1Coef = 0.24, fQ2Coef = 0.22, fQ3Coef = 0.18, fQ4Coef = 0.36;
    private ArrayList<String> compareResult;


    int pageSize = 20;


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
                    "select new Map(r.restaurantID as restaurantID,r.restaurantName as restaurantName,r.average_review as reviewScore,r.cuisines as cuisines,r.status as status,r.restaurantImageUrl as rImageUrl,r.restaurantImageBlob as restaurantImageBlob,concat(t.townName,',',c.cityName) as localityVerbose," +
                            "r.latitude as rLatitude,r.longitude as rLongitude) from Restaurant r inner join r.townID t inner join t.cityID c where r.restaurantID =:id ");
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
                    "select new Map(r.restaurantID as restaurantID,r.restaurantName as restaurantName,r.average_review as reviewScore,r.cuisines as cuisines,r.restaurantImageUrl as rImageUrl,r.restaurantImageBlob as restaurantImageBlob,concat(t.townName,',',c.cityName) as localityVerbose," +
                            "r.latitude as rLatitude,r.longitude as rLongitude,r.status as status,r.hygiene_review as hygiene_review,r.friendly_review as friendly_review) " +
                            "from Restaurant r inner join r.townID t inner join t.cityID c where lower(r.restaurantName) like lower(concat('%',:restName,'%'))").setFirstResult(pageSize * (page - 1)).setMaxResults(pageSize);
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
    public List<Object> findByCity(String city, String category, int page) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createQuery(
                    "select new Map(r.restaurantID as restaurantID,r.restaurantName as restaurantName,r.average_review as reviewScore,r.cuisines as cuisines,r.restaurantImageUrl as rImageUrl,r.restaurantImageBlob as restaurantImageBlob,concat(t.townName,',',c.cityName) as localityVerbose," +
                            "r.latitude as rLatitude,r.longitude as rLongitude, r.address as address, r.timings as timings, " +
                            "r.status as status,r.hygiene_review as hygiene_review,r.friendly_review as friendly_review,r.category as category,r.restaurantImageBlob as restaurantImageBlob)" +
                            " from Restaurant r inner join r.townID t inner join t.cityID c where c.cityName =:city and r.category=:category").setFirstResult(40 * (page - 1)).setMaxResults(40);
            query.setParameter("city", city);
            query.setParameter("category", category);
            List restaurantList = query.list();
            transaction.commit();
            return restaurantList;
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return null;

        }
    }

    @Override
    public List<Object> findByTown(String town, int page) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createQuery(
                    "select new Map(r.restaurantID as restaurantID,r.restaurantName as restaurantName,r.average_review as reviewScore,r.cuisines as cuisines,r.restaurantImageUrl as rImageUrl,r.restaurantImageBlob as restaurantImageBlob,concat(t.townName,',',c.cityName) as localityVerbose," +
                            "r.latitude as rLatitude,r.longitude as rLongitude,r.status as status,r.hygiene_review as hygiene_review,r.friendly_review as friendly_review)" +
                            " from Restaurant r inner join r.townID t inner join t.cityID c where t.townName =:town").setFirstResult(pageSize * (page - 1)).setMaxResults(pageSize);
            query.setParameter("town", town);
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
                    "select new Map(r.reviewID as reviewID,rr.restaurantName as restaurantName,rr.restaurantID as restaurantID,rr.status as status,rr.average_review as reviewScore,concat(t.townName,',',c.cityName) as localityVerbose,rr.restaurantImageUrl as rImageUrl,r.restaurantImageBlob as restaurantImageBlob,r.reviewDate as reviewDate," +
                            "r.latitude as rLatitude,r.longitude as rLongitude,rr.hygiene_review as hygiene_review,rr.friendly_review as friendly_review)" +
                            "from Review r inner join r.restaurantID rr join r.userID ru inner join rr.townID t inner join t.cityID c where ru.userID =:id").setFirstResult(pageSize * (page - 1)).setMaxResults(pageSize);
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
    public List<Object> findAllRestaurant(int page, double enlem, double boylam) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createQuery(
                    "select new Map(r.restaurantID as restaurantID,r.restaurantName as restaurantName,r.average_review as reviewScore," +
                            "r.cuisines as cuisines,r.restaurantImageUrl as rImageUrl,r.restaurantImageBlob as restaurantImageBlob,concat(t.townName,',',c.cityName) as localityVerbose, " +
                            "r.latitude as rLatitude,r.longitude as rLongitude,r.status as status,r.address as address, r.category as category,r.hygiene_review as hygiene_review, r.friendly_review as friendly_review,r.timings as timings," +
                            "r.CleaningArrow as CleaningArrow, r.HygieneArrow as HygieneArrow, sqrt(POW(69.1 * (:enlem - ABS(r.latitude)), 2) + POW(69.1 * (ABS(r.longitude) - :boylam) * COS(latitude / 57.3), 2)) as distance) " +
                            "from Restaurant as r inner join r.townID t inner join t.cityID c   ORDER BY distance asc ").setFirstResult(pageSize * (page - 1)).setMaxResults(pageSize);

            query.setParameter("boylam", String.valueOf(boylam));
            query.setParameter("enlem", String.valueOf(enlem));
            List restaurantList = query.getResultList();
            transaction.commit();
            session.close();
            return restaurantList;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            session.close();
            return null;

        }
    }

    @Override
    public List<Object> findAllbyCategory(String category, int page, double enlem, double boylam) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            if (boylam != 0 && enlem != 0) {
                Query query = session.createQuery(
                        "select new Map(r.restaurantID as restaurantID,r.restaurantName as restaurantName,r.average_review as reviewScore,r.cuisines as cuisines,r.restaurantImageUrl as rImageUrl,r.restaurantImageBlob as restaurantImageBlob,concat(t.townName,',',c.cityName) as localityVerbose, " +
                                "r.latitude as rLatitude,r.longitude as rLongitude,r.status as status,r.address as address,r.category as category,r.hygiene_review as hygiene_review,r.friendly_review as friendly_review,r.timings as timings,r.CleaningArrow as CleaningArrow, r.HygieneArrow as HygieneArrow," +
                                "sqrt(POW(69.1 * (:enlem - ABS(r.latitude)), 2) + POW(69.1 * (ABS(r.longitude) - :boylam) * COS(:enlem / 57.3), 2)) as distance)" +
                                " from Restaurant r inner join r.townID t inner join t.cityID c " +
                                "where r.category like concat('%',:category,'%') ORDER BY distance asc").setFirstResult(pageSize * (page - 1)).setMaxResults(pageSize);
                query.setParameter("category", category);
                query.setParameter("boylam", String.valueOf(boylam));
                query.setParameter("enlem", String.valueOf(enlem));
                List restaurantList = query.getResultList();
                transaction.commit();
                session.close();
                return restaurantList;
            } else {
                Query query = session.createQuery(
                        "select new Map(r.restaurantID as restaurantID,r.restaurantName as restaurantName,r.average_review as reviewScore,r.cuisines as cuisines,r.restaurantImageUrl as rImageUrl,r.restaurantImageBlob as restaurantImageBlob,concat(t.townName,',',c.cityName) as localityVerbose, " +
                                "r.latitude as rLatitude,r.longitude as rLongitude,r.status as status,r.address as address,r.category as category,r.hygiene_review as hygiene_review,r.friendly_review as friendly_review,r.timings as timings,r.CleaningArrow as CleaningArrow, r.HygieneArrow as HygieneArrow)" +
                                " from Restaurant r inner join r.townID t inner join t.cityID c where r.category like concat('%',:category,'%')").setFirstResult(pageSize * (page - 1)).setMaxResults(pageSize);
                query.setParameter("category", category);
                List<Object> restaurantList = query.getResultList();
                transaction.commit();
                session.close();
                return restaurantList;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;

        }
    }

    @Override
    public ArrayList<City> getCity() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createQuery(
                    "select new Map(c.cityID as cityID,c.cityName as city) from City c");
            ArrayList<City> cities = (ArrayList<City>) query.getResultList();
            transaction.commit();
            return cities;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;

        }
    }

    @Override
    public ArrayList<Town> getTown(String cityName) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createQuery(
                    "select new Map(t.townID as townID, t.townName as townName) from Town t inner join t.cityID c where c.cityName= :name ");
            query.setParameter("name", cityName);
            ArrayList<Town> restaurantList = (ArrayList<Town>) query.getResultList();
            transaction.commit();
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
                            "concat(t.townName,',',c.cityName) as localityVerbose,r.review_count as review_count,r.status as status,r.average_review as reviewScore,r.hygiene_review as hygiene_review,r.friendly_review as friendly_review) from Restaurant r inner join r.townID t inner join t.cityID c where r.restaurantID = :id ");
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
    public ArrayList getInfo() {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        ArrayList<Long> arr = new ArrayList<Long>();
        Info mInfo = new Info();
        try {

            Query queryLastRecord = session.createQuery(
                    "select MAX(restaurantID)from Restaurant");
            Query queryTotalRestaurant = session.createQuery(
                    "select COUNT(restaurantID)from Restaurant");
            Query queryTotalUser = session.createQuery(
                    "select COUNT(userID)from AppUser");
            Query queryTotalRecord = session.createQuery(
                    "select COUNT(recordID)from UserRecords");
            mInfo.setTotalRestaurant((long) queryTotalRestaurant.getSingleResult());
            mInfo.setTotalUser((long) queryTotalUser.getSingleResult());
            mInfo.setTotalRecord((long) queryTotalRecord.getSingleResult());
            mInfo.setLastRestaurantId((long) queryLastRecord.getSingleResult());
            arr.add(mInfo.getLastRestaurantId());
            arr.add(mInfo.getTotalRecord());
            arr.add(mInfo.getTotalRestaurant());
            arr.add(mInfo.getTotalUser());
            transaction.commit();
            return arr;
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
                    "select new Map(r.reviewID as reviewID,r.question1 as question1,r.question2 as question2," +
                            "r.question3 as question3,r.question4 as question4,r.question5 as question5,r.question6 as question6," +
                            "r.question7 as question7,r.question8 as question8,r.question9 as question9,cast(r.reviewDate as string) as reviewDate,rr.restaurantID as restaurantID,rr.restaurantName as restaurantName,ru.userID as userID)" +
                            " from Review r inner join r.restaurant rr inner join r.user ru where r.reviewID = :id");
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
    public void updateVote(Review review) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
//        Review flag  = new Review();
        Query query = sessionFactory.getCurrentSession().createQuery("select r.reviewID from Review as r " +
                "where r.restaurant.restaurantID=:resID and r.user.userID=:userID");
        query.setParameter("resID", review.getRestaurant().getRestaurantID());
        query.setParameter("userID", review.getUser().getUserID());

        review.setUser(review.getUser());
        review.setRestaurant(review.getRestaurant());
        review.setReviewID(Long.parseLong(query.getSingleResult().toString()));
        review.setQuestion1(review.getQuestion1());
        review.setQuestion2(review.getQuestion2());
        review.setQuestion3(review.getQuestion3());
        review.setQuestion4(review.getQuestion4());
        review.setQuestion5(review.getQuestion5());
        review.setQuestion6(review.getQuestion6());
        review.setQuestion7(review.getQuestion7());
        review.setQuestion8(review.getQuestion8());
        review.setQuestion9(review.getQuestion9());

        if (review.getQuestion2() == 0) {
            try {
                friendlyAverage = Math.round((review.getQuestion4() * 0.4 + review.getQuestion6() * 0.6) * 10) / 10.0;
                hygieneAverage = Math.round((review.getQuestion1() * 0.6 + review.getQuestion7() * 0.4) * 10) / 10.0;
                average = Math.round((review.getQuestion1() * fQ1Coef + review.getQuestion7() * fQ2Coef + review.getQuestion4() * fQ3Coef + review.getQuestion6() * fQ4Coef) * 10) / 10.0;

                review.setHygieneAverage(hygieneAverage);
                review.setFriendlyAverage(friendlyAverage);
                review.setAverage(average);
                session.update(review);
                transaction.commit();
                session.close();
            } catch (Exception e) {
                System.out.print(e.getMessage());
            }
        } else {
            try {
                friendlyAverage = Math.round((review.getQuestion2() * q2CategoryCoef + review.getQuestion6() * q6CategoryCoef + review.getQuestion4() * q4CategoryCoef +
                        review.getQuestion5() * q5CategoryCoef + review.getQuestion8() * q8CategoryCoef) * 10) / 10.0;
                hygieneAverage = Math.round((review.getQuestion1() * q1CategoryCoef + review.getQuestion3() * q3CategoryCoef + review.getQuestion7() * q7CategoryCoef +
                        review.getQuestion9() * q9CategoryCoef) * 10) / 10.0;
                average = Math.round((review.getQuestion1() * q1Coef + review.getQuestion2() * q2Coef + review.getQuestion3() * q3Coef + review.getQuestion4() * q4Coef +
                        review.getQuestion5() * q5Coef + review.getQuestion6() * q6Coef + review.getQuestion7() * q7Coef +
                        review.getQuestion8() * q8Coef + review.getQuestion9() * q9Coef) * 10) / 10.0;

                review.setHygieneAverage(hygieneAverage);
                review.setFriendlyAverage(friendlyAverage);
                review.setAverage(average);
                session.update(review);
                transaction.commit();
                session.close();

            } catch (Exception e) {
                System.out.print(e.getMessage());
            }
        }
    }

    @Override
    public void updatedetailvote(Review review) {

    }

    @Override
    public void detelevote(Review review) {
        Session session = sessionFactory.openSession();
        Query query = sessionFactory.getCurrentSession().createQuery("delete from Review where reviewID =: id");
        query.setParameter("id", review.getReviewID());
        try {
            query.executeUpdate();
            session.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void createRecord(UserRecords userRecords) {
        sessionFactory.getCurrentSession().save(userRecords);
    }

    @Override
    public void deleteRecordId(long recordId) {
        Query query = sessionFactory.getCurrentSession()
                .createQuery("delete FROM  UserRecords  where recordID=:recordId");
        query.setParameter("recordId", recordId);
        query.executeUpdate();
    }

    @Override
    public List<Object> findAllRestaurantAdmin(int page) {
        try {
            Query query = sessionFactory.getCurrentSession().createQuery("select new Map(u.recordID as recordID,u.userID as userID,u.restaurantName as restaurantName,u.address as address," +
                    "t.townName as town,c.cityName as city,t.townID as townID,c.cityID as cityID, u.phone_number as phone_number,u.status as status, u.timings as timings, u.category as category, u.cuisines as cuisines,u.sticker as sticker, u.latitude as latitude,u.longitude as longitude," +
                    "u.restaurantImageUrl as restaurantImageUrl, u.restaurantImageBlob as restaurantImageBlob) from UserRecords u inner join u.townID t inner join t.cityID c").setFirstResult(pageSize * (page - 1)).setMaxResults(pageSize);
            List<Object> restaurantList = query.getResultList();
            return restaurantList;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public void saveRecord(Restaurant restaurant, long resID, long userID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Restaurant record = new Restaurant();
            UserPlaces userPlaces = new UserPlaces();
            userPlaces.setRestaurant(restaurant);
            AppUser appUser = new AppUser();
            appUser.setUserID(userID);
            userPlaces.setUser(appUser);
            sessionFactory.getCurrentSession().save(userPlaces);
            record.setRestaurantID(restaurant.getRestaurantID());
            record.setRestaurantName(restaurant.getRestaurantName());
            record.setAddress(restaurant.getAddress());
            record.setCityID(restaurant.getCityID());
            record.setTownID(restaurant.getTownID());
            record.setPhone_number(restaurant.getPhone_number());
            record.setTimings(restaurant.getTimings());
            record.setCategory(restaurant.getCategory());
            record.setReview_count(restaurant.getReview_count());
            record.setCuisines(restaurant.getCuisines());
            record.setLatitude(restaurant.getLatitude());
            record.setLongitude(restaurant.getLongitude());
            record.setStatus(restaurant.getStatus());
            if (restaurant.getRestaurantImageBlob() != null)
                record.setRestaurantImageBlob(restaurant.getRestaurantImageBlob());
            else if (restaurant.getRestaurantImageUrl() != null)
                record.setRestaurantImageUrl(restaurant.getRestaurantImageUrl());
            session.save(record);
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }


    @Override
    public ArrayList<Object> fastPoint(long ResID, double point) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createQuery(
                    "select restaurantName,friendly_review,hygiene_review from Restaurant  where restaurantID= :resID ");
            query.setParameter("resID", ResID);
            ArrayList<Object> fastPoint = (ArrayList<Object>) query.getResultList();
            fastPoint.add(point);
            transaction.commit();
            return fastPoint;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;

        }

    }

    @Override
    public void fastPointSend(long resID, long userID, double point) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Review review = new Review();
            Restaurant restaurant = new Restaurant();
            AppUser appUser = new AppUser();
            restaurant.setRestaurantID(resID);
            if (userID != -1) {
                appUser.setUserID(userID);//
            }
            review.setRestaurant(restaurant);
            review.setUser(appUser);
            review.setQuestion1(point);
            review.setQuestion2(point);
            review.setQuestion3(point);
            review.setQuestion4(point);
            review.setQuestion5(point);
            review.setQuestion6(point);
            review.setQuestion7(point);
            review.setQuestion8(point);
            review.setQuestion9(point);
            review.setAverage(point);
            review.setFriendlyAverage(point);
            review.setHygieneAverage(point);

            session.save(review);
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public List<Object> findAllSourceRestaurant(String name, String townName, String cityName, int page) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createQuery(
                    "select new Map(r.restaurantID as restaurantID,r.restaurantName as restaurantName,r.average_review as reviewScore,r.cuisines as cuisines,r.restaurantImageUrl as rImageUrl, r.restaurantImageBlob as restaurantImageBlob, concat(t.townName,',',c.cityName) as localityVerbose," +
                            "r.latitude as rLatitude,r.longitude as rLongitude,r.status as status,r.hygiene_review as hygiene_review, r.address as address, r.friendly_review as friendly_review,r.timings as timings,r.CleaningArrow as CleaningArrow, r.HygieneArrow as HygieneArrow) " +
                            "from Restaurant r inner join r.townID t inner join t.cityID c where lower(r.restaurantName) like lower(concat('%',:restName,'%')) " +
                            "and t.townName= :townName and c.cityName=:cityName").setFirstResult(pageSize * (page - 1)).setMaxResults(pageSize);
            query.setParameter("restName", name);
            query.setParameter("townName", townName);
            query.setParameter("cityName", cityName);
            List<Object> restaurantList = query.getResultList();
            transaction.commit();
            return restaurantList;
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return null;

        }
    }

    @Override
    public ArrayList<String> compareResults(double oldValueHygiene, double oldValueCleaning, long ResID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createQuery(
                    "select new com.spring.model.Compare(r.friendly_review as newValueHygiene, r.hygiene_review as newValueCleaning) from Restaurant r  where  restaurantID= :resID ", Compare.class);
            query.setParameter("resID", ResID);
            List<Compare> compare = query.getResultList();
            compareResult = new ArrayList<>();
            if (compare.get(0).getNewValueHygiene() >= oldValueHygiene) {
                compareResult.add("upArrow");
            } else {
                compareResult.add("downArrow");
            }
            if (compare.get(0).getNewValueCleaning() >= oldValueCleaning) {
                compareResult.add("upArrow");
            } else {
                compareResult.add("downArrow");
            }
            transaction.commit();
            return compareResult;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;

        }
    }

    @Override
    public void arrowPointSend(long resID, int cleaningArrow, int hygieneArrow) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Restaurant restaurant = (Restaurant) session.get(Restaurant.class, resID);
            restaurant.setCleaningArrow(cleaningArrow);
            restaurant.setHygieneArrow(hygieneArrow);
            transaction.commit();
            session.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
    }

    @Override
    public void reportSend(long resID, long UserID, int reportID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Report report = new Report();
            Restaurant restaurant = new Restaurant();
            AppUser appUser = new AppUser();
            restaurant.setRestaurantID(resID);
            appUser.setUserID(UserID);
            report.setRestaurant(restaurant);
            report.setUser(appUser);
            switch (reportID) {
                case 0:
                    report.setClosedPlace(1);
                    break;
                case 1:
                    report.setFakePlace(1);
                    break;
                case 2:
                    report.setWrongLocation(1);
                    break;
                case 3:
                    report.setWrongInfo(1);
                    break;
                case 4:
                    report.setWrongScore(1);
                    break;
                default:
                    break;

            }
            session.save(report);
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean isReportExist(long resID, long UserID) {
        boolean result = false;
        Query query = sessionFactory.getCurrentSession().createQuery("select r.reportID FROM Report r inner join r.restaurant rr inner join r.user ru WHERE rr.restaurantID = :restaurantID and ru.userID =:userID");
        query.setParameter("restaurantID", resID);
        query.setParameter("userID", UserID);
        if (query.uniqueResult() != null) {
            result = true;
        } else {
            result = false;
        }
        return result;

    }


    @Override
    public void updateReportSend(long resID, int reportID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query query = sessionFactory.getCurrentSession().createQuery("select r.reportID FROM Report r inner join r.restaurantID rr WHERE rr.restaurantID = :restaurantID");
        query.setParameter("restaurantID", resID);
        Report report = (Report) session.get(Report.class, Integer.valueOf(query.uniqueResult().toString()));

        try {

            switch (reportID) {
                case 0:
                    report.setClosedPlace(report.getClosedPlace() + 1);
                    transaction.commit();
                    session.close();
                    break;
                case 1:
                    report.setFakePlace(report.getFakePlace() + 1);
                    transaction.commit();
                    session.close();
                    break;
                case 2:
                    report.setWrongLocation(report.getWrongLocation() + 1);
                    transaction.commit();
                    session.close();
                    break;
                case 3:
                    report.setWrongInfo(report.getWrongInfo() + 1);
                    transaction.commit();
                    session.close();
                    break;
                case 4:
                    report.setWrongScore(report.getWrongScore() + 1);
                    transaction.commit();
                    session.close();
                    break;
                default:
                    break;

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
    }

    @Override
    public boolean adminCheck(AdminTK adminTK) {
        boolean result = false;
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query query = sessionFactory.getCurrentSession().createQuery("select a.adminID from AdminTK a WHERE a.adminIDName=:adminIDName and a.adminPW=:adminPW and a.uniqueID=:uniqueID and a.adminStatus=:adminStatus");
        query.setParameter("adminIDName", adminTK.getAdminIDName());
        query.setParameter("adminPW", adminTK.getAdminPW());
        query.setParameter("uniqueID", adminTK.getUniqueID());
        query.setParameter("adminStatus", adminTK.getAdminStatus());
        if (query.uniqueResult() != null) {
            result = true;
        } else {
            result = false;
        }
        return result;


    }

    @Override
    public List<Object> getEnYakin(Double enlem, Double boylam) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createSQLQuery(
                "select a.restaurantName,a.latitude, a.longitude, " +
                        "SQRT(POW(69.1 * (:enlem - ABS(a.latitude)), 2) + " +
                        "POW(69.1 * (ABS(a.longitude) - :boylam) * COS(latitude / 57.3), 2)) " +
                        "AS distance from Restaurant as a HAVING distance < 25  ORDER BY distance asc");
        query.setParameter("enlem", enlem);
        query.setParameter("boylam", boylam);
        return query.getResultList();
    }

    @Override
    public List<Object> filter(Filter filter) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query query = null;
        if (filter.getVirus() != 0) {
            query = session.createQuery(
                    "select new Map(rr.restaurantID as restaurantID, avg(r.question6) as score,rr.restaurantName as restaurantName , rr.cuisines as cuisines,rr.restaurantImageUrl as rImageUrl, rr.restaurantImageBlob as restaurantImageBlob," +
                            "rr.average_review as reviewScore,rr.friendly_review as friendly_review,rr.hygiene_review as hygiene_review,rr.timings as timings,rr.category as category, rr.latitude as rLatitude, rr.longitude as rLongitude," +
                            "rr.CleaningArrow as CleaningArrow,rr.status as status, rr.HygieneArrow as HygieneArrow,concat(t.townName,',',c.cityName) as localityVerbose)" +
                            "from Review r inner join r.restaurant rr inner join rr.cityID c inner join rr.townID t GROUP BY rr.restaurantID,c.cityName,t.townName,rr.restaurantName,rr.cuisines,rr.restaurantImageUrl,rr.average_review,rr.friendly_review," +
                            "rr.hygiene_review,rr.timings,rr.category,rr.latitude,rr.longitude,rr.CleaningArrow,rr.HygieneArrow order by score desc ").setMaxResults(20);
            query.getResultList();
        } else if (filter.getScore4_5() != 0) {
            query = session.createQuery(
                    "select new Map(rr.restaurantID as restaurantID, rr.restaurantName as restaurantName , rr.cuisines as cuisines,rr.restaurantImageUrl as rImageUrl, rr.restaurantImageBlob as restaurantImageBlob," +
                            "rr.average_review as reviewScore,rr.friendly_review as friendly_review,rr.hygiene_review as hygiene_review,rr.timings as timings,rr.category as category, rr.latitude as rLatitude, rr.longitude as rLongitude," +
                            "rr.CleaningArrow as CleaningArrow, rr.status as status, rr.HygieneArrow as HygieneArrow,concat(t.townName,',',c.cityName) as localityVerbose)" +
                            "from Restaurant rr inner join rr.cityID c inner join rr.townID t where rr.average_review >= 4.5").setMaxResults(20);
            query.getResultList();
        } else if (filter.getReviewPopularity() != 0) {
            query = session.createQuery(
                    "select new Map(rr.restaurantID as restaurantID, rr.restaurantName as restaurantName , rr.cuisines as cuisines,rr.restaurantImageUrl as rImageUrl, rr.restaurantImageBlob as restaurantImageBlob," +
                            "rr.average_review as reviewScore,rr.friendly_review as friendly_review,rr.hygiene_review as hygiene_review,rr.timings as timings,rr.category as category, rr.latitude as rLatitude, rr.longitude as rLongitude," +
                            "rr.CleaningArrow as CleaningArrow, rr.status as status, rr.HygieneArrow as HygieneArrow,concat(t.townName,',',c.cityName) as localityVerbose)" +
                            "from Restaurant rr inner join rr.cityID c inner join rr.townID t order by rr.review_count desc").setMaxResults(20);
            query.getResultList();
        } else if (filter.getFilters() != null) {
            query = session.createQuery(
                    "select new Map(rr.restaurantID as restaurantID, rr.restaurantName as restaurantName , rr.cuisines as cuisines,rr.restaurantImageUrl as rImageUrl, rr.restaurantImageBlob as restaurantImageBlob," +
                            "rr.average_review as reviewScore,rr.friendly_review as friendly_review,rr.hygiene_review as hygiene_review,rr.timings as timings,rr.category as category, rr.latitude as rLatitude, rr.longitude as rLongitude," +
                            "rr.CleaningArrow as CleaningArrow, rr.status as status, rr.HygieneArrow as HygieneArrow,concat(t.townName,',',c.cityName) as localityVerbose, rr.review_count as review_count)" +
                            "from Restaurant rr inner join rr.cityID c inner join rr.townID t where rr.category IN :category and rr.average_review >=:score and c.cityName=:currentCityName " +
                            "order by case when :sort=0 then rr.average_review else 0 end asc, " +
                            "case when :sort=1 then rr.review_count else 0 end desc," +
                            "case when :sort=2 then rr.average_review else  0 end desc," +
                            "case when :sort=3 then rr.average_review else 0 end asc," +
                            "case when :sort=4 then rr.hygiene_review else 0 end desc," +
                            "case when :sort=5 then rr.friendly_review else 0 end desc ").setMaxResults(20);
            query.setParameter("sort", filter.getFilters().getSort());
            query.setParameterList("category", filter.getFilters().getCategory());
            query.setParameter("score", filter.getFilters().getPoint());
            query.setParameter("currentCityName", filter.getFilters().getCurrentCityName());// burası user konum aldıktan sonra enlem boylam farkına göre değişecek where ifadesine eklenecek
            query.getResultList();
        }
        List<Object> restaurantList = query.getResultList();
        return restaurantList;
    }

    @Override
    public boolean sendDeviceToken(String token) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query query = sessionFactory
                .getCurrentSession()
                .createQuery("select d.deviceID FROM DeviceToken d WHERE d.deviceToken = :token");
        query.setParameter("token", token);
        if (query.uniqueResult() == null) {
            DeviceToken deviceToken = new DeviceToken();
            deviceToken.setDeviceToken(token);
            session.save(deviceToken);
            session.getTransaction().commit();
            session.close();
            return false;
        } else {
            session.close();
            return true;
        }
    }

    @Override
    public List<Object> getRandomPlaces(int page) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createQuery(
                    "select new Map(r.restaurantID as restaurantID,r.restaurantName as restaurantName,r.average_review as reviewScore,r.cuisines as cuisines,r.restaurantImageUrl as rImageUrl,concat(t.townName,',',c.cityName) as localityVerbose, " +
                            "r.latitude as rLatitude,r.status as status,r.longitude as rLongitude,r.address as address, r.category as category,r.hygiene_review as hygiene_review,r.friendly_review as friendly_review,r.timings as timings,r.CleaningArrow as CleaningArrow, r.HygieneArrow as HygieneArrow)" +
                            " from Restaurant r inner join r.townID t inner join t.cityID c").setFirstResult(pageSize * (page - 1)).setMaxResults(pageSize);

            List restaurantList = query.getResultList();
            transaction.commit();
            session.close();
            return restaurantList;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            session.close();
            return null;

        }
    }

    @Override
    public Boolean sendNewEvent(String token) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query query = sessionFactory
                .getCurrentSession()
                .createQuery("update  DeviceToken d set d.deviceStatus=1 WHERE d.deviceToken = :tokenEvent");
        query.setParameter("tokenEvent", token);
        query.executeUpdate();
        session.getTransaction().commit();
        session.close();
        return true;
    }

    @Override
    public Boolean getDeviceEventStatus(String token) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query query = sessionFactory
                .getCurrentSession()
                .createQuery("select d.deviceID FROM DeviceToken d WHERE d.deviceToken = :token and d.deviceStatus=1");
        query.setParameter("token", token);
        session.close();
        if (query.uniqueResult() != null)
            return true;
        else
            return false;

    }

    @Override
    public List<Long> getUserSummary(long userID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        List<Long> summary = new ArrayList<>();
        Query queryReview = sessionFactory
                .getCurrentSession()
                .createQuery("select count(reviewID) from Review where user.userID=:userID");
        queryReview.setParameter("userID", userID);
        Query queryRecord = sessionFactory
                .getCurrentSession()
                .createQuery("select count(placeID) from UserPlaces where user.userID=:userID");
        queryRecord.setParameter("userID", userID);

        Query queryFavorite = sessionFactory
                .getCurrentSession()
                .createQuery("select count(favoriteID) from FavoritePlace where user.userID=:userID");
        queryFavorite.setParameter("userID", userID);
        summary.add((Long) queryReview.getSingleResult());
        summary.add((Long) queryRecord.getSingleResult());
        summary.add((Long) queryFavorite.getSingleResult());
        session.close();
        return summary;
    }

    @Override
    public Boolean deleteLog(DeletePlaceLog deletePlaceLog) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        try {
            DeletePlaceLog log = new DeletePlaceLog();
            log.setPlaceName(deletePlaceLog.getPlaceName());
            log.setPlaceAddress(deletePlaceLog.getPlaceAddress());
            log.setCategory(deletePlaceLog.getCategory());
            session.save(log);
            session.getTransaction().commit();
            session.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Object> sharePlace(long resID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createQuery(
                    "select new Map(r.restaurantID as restaurantID,r.restaurantName as restaurantName,r.average_review as reviewScore,r.cuisines as cuisines,r.restaurantImageUrl as rImageUrl,concat(t.townName,',',c.cityName) as localityVerbose, " +
                            "r.latitude as rLatitude,r.longitude as rLongitude,r.status as status,r.address as address, r.category as category,r.hygiene_review as hygiene_review,r.friendly_review as friendly_review,r.timings as timings,r.CleaningArrow as CleaningArrow, r.HygieneArrow as HygieneArrow)" +
                            " from Restaurant r inner join r.townID t inner join t.cityID c where r.restaurantID=:resID");

            query.setParameter("resID", resID);
            List restaurantList = query.getResultList();
            transaction.commit();
            session.close();
            return restaurantList;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            session.close();
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
    public void updateRestaurantReview(long restaurantID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query queryHygiene = session.
                createQuery("select  avg(hygieneAverage) from Review where restaurantID=:restaurantID");
        queryHygiene.setParameter("restaurantID", restaurantID);

        Query queryFriendly = session.
                createQuery("select  avg(friendlyAverage) from Review where restaurantID=:restaurantID");
        queryFriendly.setParameter("restaurantID", restaurantID);

        Query queryAverage = session.
                createQuery("select  avg(average) from Review where restaurantID=:restaurantID");
        queryAverage.setParameter("restaurantID", restaurantID);

        Double friendlyAvg = (Double) queryFriendly.uniqueResult();
        Double hygieneAvg = (Double) queryHygiene.uniqueResult();
        Double queryAvg = (Double) queryAverage.uniqueResult();

        Query query = session.createQuery("select count(rr.restaurantID) from Review r inner join r.restaurant rr where rr.restaurantID = :id");
        query.setParameter("id", restaurantID);
        Long review_count = (Long) query.uniqueResult();
        System.out.print(review_count);

        if (review_count == 0) {
            friendlyAvg = 0.0;
            hygieneAvg = 0.0;
            queryAvg = 0.0;
        }

        Restaurant upRestaurant = (Restaurant) session.get(Restaurant.class, restaurantID);

        upRestaurant.setFriendly_review(Math.round(friendlyAvg * 10) / 10.0);
        upRestaurant.setHygiene_review(Math.round(hygieneAvg * 10) / 10.0);
        upRestaurant.setAverage_review(Math.round(queryAvg * 10) / 10.0);
        upRestaurant.setReview_count(review_count);

        session.save(upRestaurant);
        transaction.commit();
        session.close();

    }


    @Override
    public void Delete(long id) {
        Query query = sessionFactory.getCurrentSession()
                .createQuery("delete FROM  Restaurant  where restaurantID=:id");
        query.setParameter("id", id);
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
        Review flag = new Review();

        flag.setUser(review.getUser());
        flag.setRestaurant(review.getRestaurant());
        flag.setReviewID(review.getReviewID());
        flag.setQuestion1(review.getQuestion1());
        flag.setQuestion2(review.getQuestion2());
        flag.setQuestion3(review.getQuestion3());
        flag.setQuestion4(review.getQuestion4());
        flag.setQuestion5(review.getQuestion5());
        flag.setQuestion6(review.getQuestion6());
        flag.setQuestion7(review.getQuestion7());
        flag.setQuestion8(review.getQuestion8());
        flag.setQuestion9(review.getQuestion9());

        if (review.getQuestion2() == 0) {
            try {

                friendlyAverage = Math.round((review.getQuestion4() * 0.4 + review.getQuestion6() * 0.6) * 10) / 10.0;
                hygieneAverage = Math.round((review.getQuestion1() * 0.6 + review.getQuestion7() * 0.4) * 10) / 10.0;
                average = Math.round((review.getQuestion1() * fQ1Coef + review.getQuestion7() * fQ2Coef + review.getQuestion4() * fQ3Coef + review.getQuestion6() * fQ4Coef) * 10) / 10.0;

                flag.setHygieneAverage(hygieneAverage);
                flag.setFriendlyAverage(friendlyAverage);
                flag.setAverage(average);
                session.save(flag);
                transaction.commit();
                session.close();
            } catch (Exception e) {
                System.out.print(e.getMessage());
            }
        } else {
            try {
                friendlyAverage = Math.round((review.getQuestion2() * q2CategoryCoef + review.getQuestion6() * q6CategoryCoef + review.getQuestion4() * q4CategoryCoef +
                        review.getQuestion5() * q5CategoryCoef + review.getQuestion8() * q8CategoryCoef) * 10) / 10.0;
                hygieneAverage = Math.round((review.getQuestion1() * q1CategoryCoef + review.getQuestion3() * q3CategoryCoef + review.getQuestion7() * q7CategoryCoef +
                        review.getQuestion9() * q9CategoryCoef) * 10) / 10.0;
                average = Math.round((review.getQuestion1() * q1Coef + review.getQuestion2() * q2Coef + review.getQuestion3() * q3Coef + review.getQuestion4() * q4Coef +
                        review.getQuestion5() * q5Coef + review.getQuestion6() * q6Coef + review.getQuestion7() * q7Coef +
                        review.getQuestion8() * q8Coef + review.getQuestion9() * q9Coef) * 10) / 10.0;

                flag.setHygieneAverage(hygieneAverage);
                flag.setFriendlyAverage(friendlyAverage);
                flag.setAverage(average);
                session.save(flag);
                transaction.commit();
                session.close();

            } catch (Exception e) {
                System.out.print(e.getMessage());
            }
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

    @Override
    public List<Object> getTopRated(int page, String type) {
        Query query;
        if (type.equals("toprated")) {
            query = sessionFactory.getCurrentSession().createQuery("select new Map(r.restaurantID as restaurantID,r.restaurantName as restaurantName,r.status as status,r.average_review as reviewScore,r.cuisines as cuisines,r.restaurantImageUrl as rImageUrl,r.restaurantImageBlob as restaurantImageBlob," +
                    "concat(t.townName,',',c.cityName) as localityVerbose,r.latitude as rLatitude,r.longitude as rLongitude,r.hygiene_review as hygiene_review,r.friendly_review as friendly_review,r.timings as timings,r.CleaningArrow as CleaningArrow, r.HygieneArrow as HygieneArrow) from Restaurant r inner join r.townID t inner join t.cityID c order by  average_review desc")
                    .setMaxResults(10);
        } else if (type.equals("hygiene")) {
            query = sessionFactory.getCurrentSession().createQuery("select new Map(r.restaurantID as restaurantID,r.restaurantName as restaurantName,r.status as status,r.average_review as reviewScore,r.cuisines as cuisines,r.restaurantImageUrl as rImageUrl,r.restaurantImageBlob as restaurantImageBlob," +
                    "concat(t.townName,',',c.cityName) as localityVerbose,r.latitude as rLatitude,r.longitude as rLongitude,r.hygiene_review as hygiene_review,r.friendly_review as friendly_review,r.timings as timings,r.CleaningArrow as CleaningArrow, r.HygieneArrow as HygieneArrow) from Restaurant r inner join r.townID t inner join t.cityID c order by  hygiene_review desc")
                    .setMaxResults(10);
        } else {
            query = sessionFactory.getCurrentSession().createQuery("select new Map(r.restaurantID as restaurantID,r.restaurantName as restaurantName,r.status as status,r.average_review as reviewScore,r.cuisines as cuisines,r.restaurantImageUrl as rImageUrl,r.restaurantImageBlob as restaurantImageBlob," +
                    "concat(t.townName,',',c.cityName) as localityVerbose,r.latitude as rLatitude,r.longitude as rLongitude,r.hygiene_review as hygiene_review,r.friendly_review as friendly_review,r.timings as timings,r.CleaningArrow as CleaningArrow, r.HygieneArrow as HygieneArrow) from Restaurant r inner join r.townID t inner join t.cityID c order by  friendly_review desc")
                    .setMaxResults(10);
        }
        List<Object> restaurantList = query.getResultList();
        return restaurantList;
    }

//    public void deleteupdate(Review review) {
//        //puan silme  işlemisonrası restaurant puan güncellemesi
//        Session session = sessionFactory.openSession();
//        Transaction transaction = session.beginTransaction();
//
//        Query query = sessionFactory.getCurrentSession().createQuery("select r.restaurant.review_count from Review r");
//
//        long count = (long) query.uniqueResult();
//
//        if (count > 0)
//            updateRestaurantReview(review.getRestaurant().getRestaurantID());
//
//
//    }


    @Override
    public void setUserAchievement(long userID) {
        Session session = sessionFactory.openSession();
        Query a1 = session.createSQLQuery("select a1 from Achievements WHERE userID = :userID ");
        a1.setParameter("userID", userID);
        if (a1.uniqueResult().toString().equals("0")) {
            Query search = sessionFactory.getCurrentSession().createQuery("SELECT r.reviewID FROM Review r inner join Restaurant rr on " +
                    "r.restaurant.restaurantID=rr.restaurantID where r.user.userID=:userID and rr.category in ('Kafe','Türk Mutfağı','Tatlı','Dünya Mutfağı','Bar & Pub')");
            search.setParameter("userID", userID);
            if (search.list().size() > 5) {
                Query update = sessionFactory.getCurrentSession().createQuery("update  Achievements a set a.a1=1 WHERE a.user.userID = :userID ");
                update.setParameter("userID", userID);
                update.executeUpdate();
            }
        }
        Query a2 = session.createSQLQuery("select a2 from Achievements WHERE userID = :userID ");
        a2.setParameter("userID", userID);
        if (a2.uniqueResult().toString().equals("0")) {
            Query update = sessionFactory.getCurrentSession().createQuery("update  Achievements a set a.a2=1 WHERE a.user.userID = :userID ");
            update.setParameter("userID", userID);
            update.executeUpdate();
        }
        Query a3 = session.createSQLQuery("select a3 from Achievements WHERE userID = :userID ");
        a3.setParameter("userID", userID);
        if (a3.uniqueResult().toString().equals("0")) {
            Query search = sessionFactory.getCurrentSession().createQuery("SELECT r.reviewID FROM Review r WHERE r.user.userID = :userID ");
            search.setParameter("userID", userID);
            if (search.list().size() > 10){
                Query update = sessionFactory.getCurrentSession().createQuery("update  Achievements a set a.a3=1 WHERE a.user.userID = :userID ");
                update.setParameter("userID", userID);
                update.executeUpdate();
            }
        }
        Query a4 = session.createSQLQuery("select a4 from Achievements WHERE userID = :userID ");
        a4.setParameter("userID", userID);
        if (a4.uniqueResult().toString().equals("0")) {
            Query search = sessionFactory.getCurrentSession().createQuery("SELECT r.reviewID FROM Review r WHERE r.user.userID = :userID and r.question2>0.5 ");
            search.setParameter("userID", userID);
            if (search.list().size() > 10){
                Query update = sessionFactory.getCurrentSession().createQuery("update  Achievements a set a.a4=1 WHERE a.user.userID = :userID ");
                update.setParameter("userID", userID);
                update.executeUpdate();
            }
        }
        //qr kodu kendi içinde setle
        Query a6 = session.createSQLQuery("select a6 from Achievements WHERE userID = :userID ");
        a6.setParameter("userID", userID);
        if (a6.uniqueResult().toString().equals("0")) {
            Query search = sessionFactory.getCurrentSession().createQuery("SELECT r.reviewID FROM Review r inner join Restaurant rr on " +
                    "r.restaurant.restaurantID=rr.restaurantID where r.user.userID=:userID and rr.category = 'Otel'");
            search.setParameter("userID", userID);
            if (search.list().size() >= 1){
                Query update = sessionFactory.getCurrentSession().createQuery("update  Achievements a set a.a6=1 WHERE a.user.userID = :userID ");
                update.setParameter("userID", userID);
                update.executeUpdate();
            }
        }
        Query a7 = session.createSQLQuery("select a7 from Achievements WHERE userID = :userID ");
        a7.setParameter("userID", userID);
        if (a7.uniqueResult().toString().equals("0")) {
            Query search = sessionFactory.getCurrentSession().createQuery("SELECT r.reviewID FROM Review r inner join FavoritePlace rr on " +
                    "r.user.userID=rr.user.userID where r.user.userID=:userID");
            search.setParameter("userID", userID);
            if (search.list().size() > 5){
                Query update = sessionFactory.getCurrentSession().createQuery("update  Achievements a set a.a7=1 WHERE a.user.userID = :userID ");
                update.setParameter("userID", userID);
                update.executeUpdate();
            }
        }
        Query a8 = session.createSQLQuery("select a8 from Achievements WHERE userID = :userID ");
        a8.setParameter("userID", userID);
        if (a8.uniqueResult().toString().equals("0")) {
            Query search = sessionFactory.getCurrentSession().createQuery("SELECT r.reviewID FROM Review r inner join UserPlaces rr on " +
                    "r.user.userID=rr.user.userID where r.user.userID=:userID");
            search.setParameter("userID", userID);
            if (search.list().size() >=1){
                Query update = sessionFactory.getCurrentSession().createQuery("update  Achievements a set a.a8=1 WHERE a.user.userID = :userID ");
                update.setParameter("userID", userID);
                update.executeUpdate();
            }
        }
        //haftanın gözdesini kendi içinde setle
        Query a10 = session.createSQLQuery("select a10 from Achievements WHERE userID = :userID ");
        a10.setParameter("userID", userID);
        if (a10.uniqueResult().toString().equals("0")) {
            Query search = sessionFactory.getCurrentSession().createQuery("SELECT r.reviewID FROM Review r WHERE r.user.userID = :userID and r.question1=5");
            search.setParameter("userID", userID);
            if (search.list().size() >=5){
                Query update = sessionFactory.getCurrentSession().createQuery("update  Achievements a set a.a10=1 WHERE a.user.userID = :userID ");
                update.setParameter("userID", userID);
                update.executeUpdate();
            }
        }
        Query a11 = session.createSQLQuery("select a11 from Achievements WHERE userID = :userID ");
        a11.setParameter("userID", userID);
        if (a11.uniqueResult().toString().equals("0")) {
            Query search = sessionFactory.getCurrentSession().createQuery("SELECT r.reviewID FROM Review r WHERE r.user.userID = :userID and r.question6=5");
            search.setParameter("userID", userID);
            if (search.list().size() >=1){
                Query update = sessionFactory.getCurrentSession().createQuery("update  Achievements a set a.a11=1 WHERE a.user.userID = :userID ");
                update.setParameter("userID", userID);
                update.executeUpdate();
            }
        }
        Query a12 = session.createSQLQuery("select a12 from Achievements WHERE userID = :userID ");
        a12.setParameter("userID", userID);
        if (a12.uniqueResult().toString().equals("0")) {
            Query search = sessionFactory.getCurrentSession().createQuery("SELECT r.reviewID FROM Review r inner join Restaurant rr on " +
                    "r.restaurant.restaurantID=rr.restaurantID where r.user.userID=:userID and rr.category in ('AVM','Sinema Salonu','Eğlence Merkezi','Spor Salonu')");
            search.setParameter("userID", userID);
            if (search.list().size() >=1){
                Query update = sessionFactory.getCurrentSession().createQuery("update  Achievements a set a.a11=1 WHERE a.user.userID = :userID ");
                update.setParameter("userID", userID);
                update.executeUpdate();
            }
        }
        Query a13 = session.createSQLQuery("select a13 from Achievements WHERE userID = :userID ");
        a13.setParameter("userID", userID);
        if (a13.uniqueResult().toString().equals("0")) {
            Query search = sessionFactory.getCurrentSession().createQuery("SELECT r.reviewID FROM Review r where r.user.userID=:userID ");
            search.setParameter("userID", userID);
            if (search.list().size() >=100){
                Query update = sessionFactory.getCurrentSession().createQuery("update  Achievements a set a.a13=1 WHERE a.user.userID = :userID ");
                update.setParameter("userID", userID);
                update.executeUpdate();
            }
        }
    }
}







