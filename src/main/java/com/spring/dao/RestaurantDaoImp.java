package com.spring.dao;

import com.google.protobuf.Internal;
import com.spring.model.*;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.DoubleAccumulator;


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
                    "select new Map(r.restaurantID as restaurantID,r.restaurantName as restaurantName,r.average_review as reviewScore,r.cuisines as cuisines,r.restaurantImageUrl as rImageUrl,concat(t.townName,',',c.cityName) as localityVerbose," +
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
                    "select new Map(r.restaurantID as restaurantID,r.restaurantName as restaurantName,r.average_review as reviewScore,r.cuisines as cuisines,r.restaurantImageUrl as rImageUrl,concat(t.townName,',',c.cityName) as localityVerbose," +
                            "r.latitude as rLatitude,r.longitude as rLongitude,r.hygiene_review as hygiene_review,r.friendly_review as friendly_review) " +
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
    public List<Object> findByCity(String city, int page) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createQuery(
                    "select new Map(r.restaurantID as restaurantID,r.restaurantName as restaurantName,r.average_review as reviewScore,r.cuisines as cuisines,r.restaurantImageUrl as rImageUrl,concat(t.townName,',',c.cityName) as localityVerbose," +
                            "r.latitude as rLatitude,r.longitude as rLongitude,r.hygiene_review as hygiene_review,r.friendly_review as friendly_review)" +
                            " from Restaurant r inner join r.townID t inner join t.cityID c where c.cityName =:city ").setFirstResult(pageSize * (page - 1)).setMaxResults(pageSize);
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
    public List<Object> findByTown(String town, int page) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createQuery(
                    "select new Map(r.restaurantID as restaurantID,r.restaurantName as restaurantName,r.average_review as reviewScore,r.cuisines as cuisines,r.restaurantImageUrl as rImageUrl,concat(t.townName,',',c.cityName) as localityVerbose," +
                            "r.latitude as rLatitude,r.longitude as rLongitude,r.hygiene_review as hygiene_review,r.friendly_review as friendly_review)" +
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
                    "select new Map(r.reviewID as reviewID,rr.restaurantName as restaurantName,rr.restaurantID as restaurantID,rr.average_review as reviewScore,concat(t.townName,',',c.cityName) as localityVerbose,rr.restaurantImageUrl as rImageUrl,r.reviewDate as reviewDate," +
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
    public List<Object> findAllRestaurant(int page) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createQuery(
                    "select new Map(r.restaurantID as restaurantID,r.restaurantName as restaurantName,r.average_review as reviewScore,r.cuisines as cuisines,r.restaurantImageUrl as rImageUrl,concat(t.townName,',',c.cityName) as localityVerbose, " +
                            "r.latitude as rLatitude,r.longitude as rLongitude,r.category as category,r.hygiene_review as hygiene_review,r.friendly_review as friendly_review,r.timings as timings,r.CleaningArrow as CleaningArrow, r.HygieneArrow as HygieneArrow)" +
                            " from Restaurant r inner join r.townID t inner join t.cityID c").setFirstResult(pageSize * (page - 1)).setMaxResults(pageSize);

            List restaurantList = query.getResultList();
            transaction.commit();
            return restaurantList;
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return null;

        }
    }

    @Override
    public List<Object> findAllbyCategory(String category, int page) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createQuery(
                    "select new Map(r.restaurantID as restaurantID,r.restaurantName as restaurantName,r.average_review as reviewScore,r.cuisines as cuisines,r.restaurantImageUrl as rImageUrl,concat(t.townName,',',c.cityName) as localityVerbose, " +
                            "r.latitude as rLatitude,r.longitude as rLongitude,r.category as category,r.hygiene_review as hygiene_review,r.friendly_review as friendly_review,r.timings as timings,r.CleaningArrow as CleaningArrow, r.HygieneArrow as HygieneArrow)" +
                            " from Restaurant r inner join r.townID t inner join t.cityID c where r.category like concat('%',:category,'%')").setFirstResult(pageSize * (page - 1)).setMaxResults(pageSize);
            query.setParameter("category", category);
            List<Object> restaurantList = query.getResultList();
            transaction.commit();
            return restaurantList;
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
                            "concat(t.townName,',',c.cityName) as localityVerbose,r.review_count as review_count,r.average_review as reviewScore,r.hygiene_review as hygiene_review,r.friendly_review as friendly_review) from Restaurant r inner join r.townID t inner join t.cityID c where r.restaurantID = :id ");
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
        Review flag  = new Review();
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

//        if (review.getQuestion2() == 0) {
            try {
                friendlyAverage = Math.round((review.getQuestion4() * 0.4 + review.getQuestion6() * 0.6) * 10) / 10.0;
                hygieneAverage = Math.round((review.getQuestion1() * 0.6 + review.getQuestion7() * 0.4) * 10) / 10.0;
                average = Math.round((review.getQuestion1() * fQ1Coef + review.getQuestion7() * fQ2Coef + review.getQuestion4() * fQ3Coef + review.getQuestion6() * fQ4Coef) * 10) / 10.0;

                flag.setHygieneAverage(hygieneAverage);
                flag.setFriendlyAverage(friendlyAverage);
                flag.setAverage(average);
                session.update(flag);
                transaction.commit();
                session.close();
            } catch (Exception e) {
                System.out.print(e.getMessage());
            }
//        } else {
//            try {
//                friendlyAverage = Math.round((review.getQuestion2() * q2CategoryCoef + review.getQuestion6() * q6CategoryCoef + review.getQuestion4() * q4CategoryCoef +
//                        review.getQuestion5() * q5CategoryCoef + review.getQuestion8() * q8CategoryCoef) * 10) / 10.0;
//                hygieneAverage = Math.round((review.getQuestion1() * q1CategoryCoef + review.getQuestion3() * q3CategoryCoef + review.getQuestion7() * q7CategoryCoef +
//                        review.getQuestion9() * q9CategoryCoef) * 10) / 10.0;
//                average = Math.round((review.getQuestion1() * q1Coef + review.getQuestion2() * q2Coef + review.getQuestion3() * q3Coef + review.getQuestion4() * q4Coef +
//                        review.getQuestion5() * q5Coef + review.getQuestion6() * q6Coef + review.getQuestion7() * q7Coef +
//                        review.getQuestion8() * q8Coef + review.getQuestion9() * q9Coef) * 10) / 10.0;
//
//                flag.setHygieneAverage(hygieneAverage);
//                flag.setFriendlyAverage(friendlyAverage);
//                flag.setAverage(average);
//                session.save(flag);
//                transaction.commit();
//                session.close();
//
//            } catch (Exception e) {
//                System.out.print(e.getMessage());
//            }
//        }
    }

    @Override
    public void detelevote(Review review) {
        Session session = sessionFactory.openSession();
        Query query = sessionFactory.getCurrentSession().createQuery("delete from Review where reviewID =: id");
        query.setParameter("id",review.getReviewID());
        query.executeUpdate();
        try{
            session.delete(review);
        }
        catch(Exception e){
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
                    "t.townName as town,c.cityName as city,t.townID as townID,c.cityID as cityID, u.phone_number as phone_number, u.timings as timings, u.category as category, u.cuisines as cuisines,u.sticker as sticker, u.latitude as latitude,u.longitude as longitude," +
                    "u.restaurantImageUrl as restaurantImageUrl) from UserRecords u inner join u.townID t inner join t.cityID c").setFirstResult(pageSize * (page - 1)).setMaxResults(pageSize);
            List<Object> restaurantList = query.getResultList();
            return restaurantList;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public void saveRecord(Restaurant restaurant) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Restaurant record = new Restaurant();
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
    public List<Object> findAllSourceRestaurant(String name, String townName, int page) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Query query = session.createQuery(
                    "select new Map(r.restaurantID as restaurantID,r.restaurantName as restaurantName,r.average_review as reviewScore,r.cuisines as cuisines,r.restaurantImageUrl as rImageUrl,concat(t.townName,',',c.cityName) as localityVerbose," +
                            "r.latitude as rLatitude,r.longitude as rLongitude,r.hygiene_review as hygiene_review,r.friendly_review as friendly_review,r.timings as timings,r.CleaningArrow as CleaningArrow, r.HygieneArrow as HygieneArrow) " +
                            "from Restaurant r inner join r.townID t inner join t.cityID c where lower(r.restaurantName) like lower(concat('%',:restName,'%')) " +
                            "and t.townName= :townName").setFirstResult(pageSize * (page - 1)).setMaxResults(pageSize);
            query.setParameter("restName", name);
            query.setParameter("townName", townName);
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
    public void reportSend(long resID,long UserID, int reportID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            Report report = new Report();
            Restaurant restaurant=new Restaurant();
            AppUser appUser=new AppUser();
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
    public boolean isReportExist(long resID,long UserID) {
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
                    report.setClosedPlace(report.getClosedPlace()+1);
                    transaction.commit();
                    session.close();
                    break;
                case 1:
                    report.setFakePlace(report.getFakePlace()+1);
                    transaction.commit();
                    session.close();
                    break;
                case 2:
                    report.setWrongLocation(report.getWrongLocation()+1);
                    transaction.commit();
                    session.close();
                    break;
                case 3:
                    report.setWrongInfo(report.getWrongInfo()+1);
                    transaction.commit();
                    session.close();
                    break;
                case 4:
                    report.setWrongScore(report.getWrongScore()+1);
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
        if (query.uniqueResult()!=null) {
            result = true;
        } else {
            result = false;
        }
        return result;


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

        Restaurant upRestaurant = (Restaurant) session.get(Restaurant.class, restaurantID);

        upRestaurant.setFriendly_review(Math.round(friendlyAvg * 10) / 10.0);
        upRestaurant.setHygiene_review(Math.round(hygieneAvg * 10) / 10.0);
        upRestaurant.setAverage_review(Math.round(queryAvg * 10) / 10.0);
        upRestaurant.setReview_count(review_count);


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
            query = sessionFactory.getCurrentSession().createQuery("select new Map(r.restaurantID as restaurantID,r.restaurantName as restaurantName,r.average_review as reviewScore,r.cuisines as cuisines,r.restaurantImageUrl as rImageUrl," +
                    "concat(t.townName,',',c.cityName) as localityVerbose,r.latitude as rLatitude,r.longitude as rLongitude,r.hygiene_review as hygiene_review,r.friendly_review as friendly_review,r.timings as timings,r.CleaningArrow as CleaningArrow, r.HygieneArrow as HygieneArrow) from Restaurant r inner join r.townID t inner join t.cityID c order by  average_review desc")
                    .setMaxResults(10);
        } else if (type.equals("hygiene")) {
            query = sessionFactory.getCurrentSession().createQuery("select new Map(r.restaurantID as restaurantID,r.restaurantName as restaurantName,r.average_review as reviewScore,r.cuisines as cuisines,r.restaurantImageUrl as rImageUrl," +
                    "concat(t.townName,',',c.cityName) as localityVerbose,r.latitude as rLatitude,r.longitude as rLongitude,r.hygiene_review as hygiene_review,r.friendly_review as friendly_review,r.timings as timings,r.CleaningArrow as CleaningArrow, r.HygieneArrow as HygieneArrow) from Restaurant r inner join r.townID t inner join t.cityID c order by  hygiene_review desc")
                    .setMaxResults(10);
        } else {
            query = sessionFactory.getCurrentSession().createQuery("select new Map(r.restaurantID as restaurantID,r.restaurantName as restaurantName,r.average_review as reviewScore,r.cuisines as cuisines,r.restaurantImageUrl as rImageUrl," +
                    "concat(t.townName,',',c.cityName) as localityVerbose,r.latitude as rLatitude,r.longitude as rLongitude,r.hygiene_review as hygiene_review,r.friendly_review as friendly_review,r.timings as timings,r.CleaningArrow as CleaningArrow, r.HygieneArrow as HygieneArrow) from Restaurant r inner join r.townID t inner join t.cityID c order by  friendly_review desc")
                    .setMaxResults(10);
        }
        List<Object> restaurantList = query.getResultList();
        return restaurantList;
    }


}







