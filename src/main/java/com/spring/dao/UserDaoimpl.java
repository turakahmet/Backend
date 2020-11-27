package com.spring.dao;

import com.spring.model.*;
import com.spring.service.MailService;
import com.spring.token.Validation;
import lombok.Setter;
//import lombok.launch.PatchFixesHider;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;


@Transactional
@Repository
public class UserDaoimpl implements UserDAO {
    @Autowired
    MailService mailService;

    @Setter
    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    Validation validation;

    @Override
    public void insertUser(AppUser user) {
        try {
            sessionFactory.getCurrentSession().save(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    int pageSize = 20;

    @Override
    public boolean checkStandardCredentials(String userEmail, String password) {
        Session session = sessionFactory.openSession();
        Query query = session.createQuery("select userID from AppUser where userEmail=:userEmail and userPassword =: userPassword ");
        query.setParameter("userEmail", userEmail);
        query.setParameter("userPassword", password);
        if (query.uniqueResult() != null) {
            return true;
        } else
            return false;
    }

    @Override
    public boolean checkGoogleCredentials(AppUser user) {
        return false;
    }

    @Override
    public String getusertype(String email) {
        String result;
        try {

            System.out.println("EMAİL!!!:" + email);
            Query query = sessionFactory.getCurrentSession().
                    createQuery("select userType from AppUser where userEmail=:userEmail");
            query.setParameter("userEmail", email);

            result = (String) query.getSingleResult();
            return result;
        } catch (Exception e) {

            e.printStackTrace();
            return e.getMessage();

        }

    }

    @Override
    public CustomUser findUserByEmail(String userEmail) {
        try {
            Session session = sessionFactory.openSession();


            CustomUser cUser = new CustomUser();
            Query query = sessionFactory.getCurrentSession()
                    .createQuery("from AppUser where userEmail =: userEmail");
            query.setParameter("userEmail", userEmail);

            AppUser aUser = (AppUser) query.uniqueResult();
            session.close();
            cUser.setUserID(aUser.getUserID());
            cUser.setUserEmail(aUser.getUserEmail());
            cUser.setUserName(aUser.getUserName());
            cUser.setProfilImageID(aUser.getProfilImageID());
            cUser.setCoverImage(aUser.getCoverImage());
            cUser.setUserImageUrl(aUser.getUserImageUrl());


            return cUser;
        } catch (Exception e) {

            return null;
        }


    }


    @Override
    public List<Object> listAllUsers() {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("select new Map(a.userID as userID ,a.userName as userName,a.userSurname as userSurname," +
                    "a.userEmail as userEmail,a.profilImageID as profilImageID,a.userToken as userToken," +
                    "a.userType as userType,a.status as status) from AppUser a");
            List<Object> userList = query.list();
            transaction.commit();
            session.close();
            return userList;
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return null;
        }
    }

//    @Override
//    public AppUser updateUser(AppUser user) {
//        //Kullanıcının update olacak hali geliyor saedece id aynı
//
//
//        try {
//            Session session = sessionFactory.openSession();
//            Transaction tx = session.beginTransaction();
//
//            AppUser upUser = (AppUser) session.get(AppUser.class, user.getUserID()); //idyi burda yakalayıp bu idde klon kullanıcı oluşuyor.
//            //neler değişecekse ilgili şeyler altta yapılır.
//            upUser.setUserPassword(user.getUserPassword());
//            upUser.setUserName(user.getUserName());
//            upUser.setUserSurname(user.getUserSurname());
//            upUser.setUserEmail(user.getUserEmail());
//
//            //update işlemi başlar
//            session.update(upUser);
//            tx.commit();
//            session.close();
//            return upUser;
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//            return null;
//        }
//
//
//    }

    @Override
    public Boolean isUserExist(String email) {
        try {
            Session session = sessionFactory.openSession();
            Query query = session.createQuery("select userID from AppUser where userEmail=:email");
            query.setParameter("email", email);
            if (query.uniqueResult() == null)
                return true;
            else
                return false;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Boolean isUserActive(String email) {


        Session session = sessionFactory.openSession();
        Query query = session.createQuery("select userID from AppUser where userEmail =: userEmail and status =: status ");
        query.setParameter("userEmail", email);
        query.setParameter("status", "active");
        //parola değiştirecek kullanıcı standard olmalı
        if (query.uniqueResult() != null)
            return true;
        else
            return false;
    }


    @Override
    public Boolean checkUserCode(String email, long code) {
        Query query = sessionFactory.getCurrentSession()
                .createQuery("from AppUser  where userEmail =: userEmail and code=:code ");
        query.setParameter("userEmail", email);
        query.setParameter("code", code);
        if (query.uniqueResult() != null) {
            changeUserCode(email, code);
            return true;
        } else
            return false;
    }


    @Override
    public Boolean sendmail(String email) {
        try {
            Session session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();


            Query query = sessionFactory.getCurrentSession()
                    .createQuery("from AppUser where userEmail =: userEmail");
            query.setParameter("userEmail", email);

            AppUser aUser = (AppUser) query.uniqueResult();
            aUser.setCode(mailService.sendMail(email, aUser.getUserPassword()));
            session.update(aUser);
            tx.commit();
            session.close();
            return true;

        } catch (Exception e) {
            System.out.println("CATCH!!!:" + e.getMessage());
            //Catche giriyor sessionlarla alakalı problem var. Burası fixlenmeli

        } finally {
            return true;
        }
    }


    @Override
    public void changeUserCode(String email, long code) {

        try {
            Session session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("from AppUser where userEmail =: userEmail and code=:code");
            query.setParameter("userEmail", email);
            query.setParameter("code", code);


            AppUser tempUser = (AppUser) query.uniqueResult();

            AppUser upUser = (AppUser) session.get(AppUser.class, tempUser.getUserID());
            upUser.setCode(0L);
            upUser.setStatus("active");
            session.update(upUser);
            tx.commit();
            session.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());

        }
    }

    @Override
    public List<Review> getReview(String email) {
        Session session = sessionFactory.openSession();
        CustomUser cUser = new CustomUser();
        cUser = findUserByEmail(email);
        Query query = session.createQuery("select new map(r.id as id,r.question1 as q1,r.question2 as q2,r.question3 as q3,r.question4 as q4,r.question5 as q5,r.question6 as q6," +
                "r.question7 as q7,r.question8 as a8,r.question9 as q9,r.average as average ,r.hygieneAverage as hygieneavg,r.friendlyAverage  as friendlyavg )from Review r where user.userEmail =:email  ");
        query.setParameter("email", email);
        List<Review> reviewList = query.list();

        return reviewList;


    }

    @Override
    public Boolean isadmin(AdminTK adminTK) {
        try {

            int status = 0;
            Session session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();

            Query isAdmin = sessionFactory.getCurrentSession().createQuery("from AdminTK  where uniqueID  =: uniqueID and adminIDName =:" +
                    " name and adminPW =: pw and adminStatus =: status ");
            isAdmin.setParameter("uniqueID", adminTK.getUniqueID());
            isAdmin.setParameter("name", adminTK.getAdminIDName());
            isAdmin.setParameter("pw", adminTK.getAdminPW());
            isAdmin.setParameter("status", adminTK.getAdminStatus());
            if (isAdmin.uniqueResult() != null)
                return true;
            else
                return false;
        } catch (Exception e) {

            System.out.println("HATA: " + e.getMessage());
            return null;
        }


    }

    @Override
    public List<Object> getuserreviews(String email) {

        Session session = sessionFactory.openSession();

        Transaction transaction = session.beginTransaction();

        Query query = session.createQuery("select a.userID as userID ,a.userName as userName,a.userSurname as userSurname," +
                "a.userEmail as userEmail,a.profilImageID as profilImageID,a.userToken as userToken," +
                "a.userType as userType,a.status as status from AppUser a where userEmail =: email");

        query.setParameter("email", email);
        if (query.uniqueResult() != null) {
            CustomUser cUser = findUserByEmail(email);
            Query query3 = session.createQuery("select new Map(r.average as average,r.reviewDate as date,r.restaurant.restaurantID as restaurantID,r.id as ID,r.hygieneAverage as hygieneAverage,r.friendlyAverage as friendlyAverage" +
                    "  ,r.restaurant.restaurantName as restaurantName  ,r.restaurant.restaurantImageUrl as restaurantImage) from Review  r where user.userID =: id ORDER BY  reviewDate ASC ");
            query3.setParameter("id", cUser.getUserID());


            System.out.println(cUser.getUserID());


//        query2.setParameter("email",email);
            List<Object> reviewList = query3.list();
            transaction.commit();
//        <ListAppUser aUser =  (AppUser) query2.uniqueResult();

            return reviewList;
        }

        //TODO:BURADA DAHA SONRA İYİLEŞTİRME YAPICAM.
        else {
            return null;

        }

    }

    @Override
    public Long getreviewcount(String email, String password) {
        if (getusertype(email).equals("google")) {
            System.out.println("GOOGLE USER COUNT REQUEST");
            if (validation.isValidateGoogle(email, password)) {
                System.out.println("GOOGLE USER COUNT REQUEST VALİDATED");

                Session session = sessionFactory.openSession();
                Transaction transaction = session.beginTransaction();
                Query query = session.createQuery("select COUNT(*)  from Review where user.userEmail =: email  ");
                query.setParameter("email", email);
                return (Long) query.getSingleResult();


            } else
                return null;
        } else {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("select COUNT(*)  from Review where user.userEmail =: email  ");
            query.setParameter("email", email);


            return (Long) query.getSingleResult();
        }


    }


    @Override
    public List<Object> getcategoryinfo(String email) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        Query query3 = session.createQuery("select new Map(count(r2.category) as count,r2.category as category) from Review r,Restaurant r2 , AppUser  a where r.user.userID = a.userID and " +
                "r.restaurant.restaurantID = r2.restaurantID and a.userEmail =:email group by r2.category");
        query3.setParameter("email", email);


        List<Object> reviewList = query3.list();
        transaction.commit();
        return reviewList;
    }

    @Override
    public List<Object> getcategorizedreviews(String email, String category) {
        Session session = sessionFactory.openSession();

        Transaction transaction = session.beginTransaction();

        Query query = session.createQuery("select a.userID as userID ,a.userName as userName,a.userSurname as userSurname," +
                "a.userEmail as userEmail,a.profilImageID as profilImageID,a.userToken as userToken," +
                "a.userType as userType,a.status as status from AppUser a where userEmail =: email");
        CustomUser cUser = findUserByEmail(email);
        System.out.println(cUser.getUserID());

        //TODO:BURADA DAHA SONRA İYİLEŞTİRME YAPICAM.
        if (category.equals("Benzin İstasyonu") || category.equals("AVM") || category.equals("Otel")) {

            Query general = session.createQuery("select new Map(r.average as average,r.restaurant.restaurantID as restaurantID,r.id as ID,r.reviewDate as date,r.hygieneAverage as hygieneAverage,r.friendlyAverage as friendlyAverage" +
                    "  ,r.restaurant.restaurantName as restaurantName  ,r.restaurant.restaurantImageUrl as restaurantImage,r.restaurant.category as restaurantCategory) from Review  r where user.userID =: id and r.restaurant.category =: category ORDER BY  reviewDate ASC");
            general.setParameter("id", cUser.getUserID());
            general.setParameter("category", category);
            List<Object> reviewList = general.list();
            transaction.commit();
            return reviewList;

            //General querysi gelen categoryi direk işleyerek sorgu atar(Alt kategorisi olmayan kategoriler için)
            //Benzinlik,otel,AVM kategorileri için

        } else if (category.equals("Restaurant")) {
            Query restaurant = session.createQuery("select new Map(r.average as average,r.reviewDate as date,r.restaurant.restaurantID as restaurantID,r.id as ID,r.hygieneAverage as hygieneAverage,r.friendlyAverage as friendlyAverage" +
                    "  ,r.restaurant.restaurantName as restaurantName  ,r.restaurant.restaurantImageUrl as restaurantImage,r.restaurant.category as restaurantCategory) from Review  r where user.userID =: id and r.restaurant.category ='Kafe' " +
                    "or r.restaurant.category ='Türk Mutfağı' or r.restaurant.category ='Tatlı' or r.restaurant.category ='Bar&Pubs' or r.restaurant.category ='Dünya Mutfağı' ORDER BY  reviewDate ASC");
            restaurant.setParameter("id", cUser.getUserID());
            List<Object> reviewList = restaurant.list();
            transaction.commit();
            return reviewList;
        } else if (category.equals("Halka Açık")) {
            Query public_ = session.createQuery("select new Map(r.average as average,r.reviewDate as date,r.restaurant.restaurantID as restaurantID,r.id  as ID,r.hygieneAverage as hygieneAverage,r.friendlyAverage as friendlyAverage" +
                    "  ,r.restaurant.restaurantName as restaurantName  ,r.restaurant.restaurantImageUrl as restaurantImage,r.restaurant.category as restaurantCategory) from Review  r where user.userID =: id and r.restaurant.category ='Spor Salonu' " +
                    "or r.restaurant.category ='sinema' or r.restaurant.category ='Eğlence Merkezi' ORDER BY  reviewDate ASC");
            public_.setParameter("id", cUser.getUserID());
            List<Object> reviewList = public_.list();
            transaction.commit();
            return reviewList;

        } else {
            Query other = session.createQuery("select new Map(r.average as average,r.reviewDate as date,r.id as ID,r.restaurant.restaurantID as restaurantID,r.hygieneAverage as hygieneAverage,r.friendlyAverage as friendlyAverage" +
                    "  ,r.restaurant.restaurantName as restaurantName  ,r.restaurant.restaurantImageUrl as restaurantImage,r.restaurant.category as restaurantCategory) from Review  r where user.userID =: id and r.restaurant.category <> 'Kafe'" +
                    "and r.restaurant.category <> 'Türk Mutfağı' and r.restaurant.category <> 'Bar & Pub' and r.restaurant.category <> 'Dünya Mutfağı'" +
                    "and r.restaurant.category <> 'Otel' and r.restaurant.category <> 'Benzin İstasyonu' and r.restaurant.category <> 'Spor Salonu'" +
                    "and r.restaurant.category <> 'AVM' and r.restaurant.category <> 'sinema' and r.restaurant.category <> 'Eğlence Merkezi' ORDER BY  reviewDate ASC ");
            other.setParameter("id", cUser.getUserID());
            List<Object> reviewList = other.list();
            transaction.commit();
            return reviewList;
        }


//        query2.setParameter("email",email);
//        <ListAppUser aUser =  (AppUser) query2.uniqueResult();

    }


    @Override
    public Boolean changeUserImage(long userID, byte[] profileImage, byte[] coverImage) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        if (profileImage != null) {
            Query query = sessionFactory.getCurrentSession().createQuery("update AppUser a  set a.profilImageID =:profileImage " +
                    "where a.userID=:userID");
            query.setParameter("profileImage", profileImage);
            query.setParameter("userID", userID);
            query.executeUpdate();
            session.getTransaction().commit();
            session.close();
            return true;
        } else if (coverImage != null) {
            Query query1 = sessionFactory.getCurrentSession().createQuery("update AppUser a  set a.coverImage =:coverImage " +
                    "where a.userID=:userID");
            query1.setParameter("coverImage", coverImage);
            query1.setParameter("userID", userID);
            query1.executeUpdate();
            session.getTransaction().commit();
            session.close();
            return true;
        }
        return false;

    }

    @Override
    public byte[] getProfileImage(long userID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query query = sessionFactory.getCurrentSession().createQuery("" +
                "select profilImageID from AppUser  where userID=:userID");
        query.setParameter("userID", userID);
        return (byte[]) query.getSingleResult();
    }

    @Override
    public byte[] getCoverImage(long userID) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query query = sessionFactory.getCurrentSession().createQuery("" +
                "select coverImage from AppUser  where userID=:userID");
        query.setParameter("userID", userID);
        return (byte[]) query.getSingleResult();
    }

    @Override
    public ArrayList<Restaurant> getUserPoints(long userID, int page) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query query = sessionFactory.getCurrentSession().createQuery("" +
                "select new Map(r.restaurantID as restaurantID,r.restaurantName as restaurantName,r.average_review as reviewScore,r.cuisines as cuisines,r.restaurantImageUrl as rImageUrl, r.restaurantImageBlob as restaurantImageBlob,concat(t.townName,',',c.cityName) as localityVerbose, t.townName as townName, c.cityName as cityName, " +
                "r.latitude as rLatitude,r.longitude as rLongitude, r.address as address, r.category as category,r.hygiene_review as hygiene_review,r.friendly_review as friendly_review,r.timings as timings,r.CleaningArrow as CleaningArrow, r.HygieneArrow as HygieneArrow, rev.question1 as question1," +
                "rev.question2 as question2," +
                "rev.question3 as question3," +
                "rev.question4 as question4," +
                "rev.question5 as question5," +
                "rev.question6 as question6," +
                "rev.question7 as question7," +
                "rev.question8 as question8," +
                "rev.question9 as question9)" +
                " from Restaurant r inner join r.townID t inner join t.cityID c inner join Review rev on r.restaurantID=rev.restaurant.restaurantID where rev.user.userID=:userID").setFirstResult(pageSize * (page - 1)).setMaxResults(pageSize);
        query.setParameter("userID", userID);
        return new ArrayList<>(query.list());
    }

    @Override
    public ArrayList<Restaurant> getUserPlace(long userID, int page) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query query = sessionFactory.getCurrentSession().createQuery("" +
                "select new Map(r.restaurantID as restaurantID,r.restaurantName as restaurantName,r.average_review as reviewScore," +
                "r.cuisines as cuisines,r.restaurantImageUrl as rImageUrl, r.restaurantImageBlob as restaurantImageBlob," +
                "concat(t.townName,',',c.cityName) as localityVerbose, t.townName as townName, c.cityName as cityName, " +
                "r.latitude as rLatitude,r.longitude as rLongitude,r.address as address, r.category as category,r.hygiene_review as hygiene_review," +
                "r.friendly_review as friendly_review,r.timings as timings,r.status as status, r.CleaningArrow as CleaningArrow, r.HygieneArrow as HygieneArrow)" +
                " from Restaurant r inner join r.townID t inner join t.cityID c inner join UserPlaces rev on r.restaurantID=rev.restaurant.restaurantID " +
                "where rev.user.userID=:userID").setFirstResult(pageSize * (page - 1)).setMaxResults(pageSize);
        query.setParameter("userID", userID);
        return new ArrayList<>(query.list());
    }

    @Override
    public ArrayList<Restaurant> getUserFavorite(long userID, int page) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query query = sessionFactory.getCurrentSession().createQuery("" +
                "select new Map(r.restaurantID as restaurantID,r.restaurantName as restaurantName,r.average_review as reviewScore," +
                "r.cuisines as cuisines,r.restaurantImageUrl as rImageUrl, r.restaurantImageBlob as restaurantImageBlob," +
                "concat(t.townName,',',c.cityName) as localityVerbose, t.townName as townName, c.cityName as cityName, " +
                "r.latitude as rLatitude,r.longitude as rLongitude, r.address as address, r.category as category,r.hygiene_review as hygiene_review," +
                "r.friendly_review as friendly_review,r.timings as timings,r.CleaningArrow as CleaningArrow, r.HygieneArrow as HygieneArrow)" +
                " from Restaurant r inner join r.townID t inner join t.cityID c inner join FavoritePlace rev on r.restaurantID=rev.restaurant.restaurantID " +
                "where rev.user.userID=:userID").setFirstResult(pageSize * (page - 1)).setMaxResults(pageSize);
        query.setParameter("userID", userID);
        return new ArrayList<>(query.list());
    }

    @Override
    public Boolean setFavoriteRes(FavoritePlace favoriteRes) {
        try {
            Query query = sessionFactory.getCurrentSession().createQuery("select" +
                    " f.favoriteID from FavoritePlace as f where f.user.userID=:userID and " +
                    "f.restaurant.restaurantID=:resID");
            query.setParameter("userID", favoriteRes.getUser().getUserID());
            query.setParameter("resID", favoriteRes.getRestaurant().getRestaurantID());
            if (query.getResultList().size() == 0) {
                if (sessionFactory.getCurrentSession().save(favoriteRes) != null) {
                    return true;
                } else {
                    return false;
                }
            } else
                return false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public Boolean removeFavorite(FavoritePlace favoritePlace) {
        try {
            Query query = sessionFactory.getCurrentSession().createQuery("" +
                    "delete from FavoritePlace as f where f.user.userID=:userID and " +
                    "f.restaurant.restaurantID=:resID");
            query.setParameter("userID", favoritePlace.getUser().getUserID());
            query.setParameter("resID", favoritePlace.getRestaurant().getRestaurantID());
            if (query.executeUpdate() > 0) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public ArrayList<Restaurant> getUserSubCategory(long userID, int category, int page) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        if (category == 1) {
            Query query = sessionFactory.getCurrentSession().createQuery("" +
                    "select new Map(r.restaurantID as restaurantID,r.restaurantName as restaurantName,r.average_review as reviewScore," +
                    "r.cuisines as cuisines,r.restaurantImageUrl as rImageUrl, r.restaurantImageBlob as restaurantImageBlob," +
                    "concat(t.townName,',',c.cityName) as localityVerbose, t.townName as townName, c.cityName as cityName, " +
                    "r.latitude as rLatitude,r.longitude as rLongitude,r.status as status, r.address as address,r.category as category,r.hygiene_review as hygiene_review," +
                    "r.friendly_review as friendly_review,r.timings as timings,r.CleaningArrow as CleaningArrow, r.HygieneArrow as HygieneArrow)" +
                    " from Restaurant r inner join r.townID t inner join t.cityID c inner join Review rev on r.restaurantID=rev.restaurant.restaurantID  " +
                    "where rev.user.userID=:userID and r.category in ('kafe','Türk Mutfağı','Tatlı','Bar & Pub','Dünya Mutfağı')").setFirstResult(pageSize * (page - 1)).setMaxResults(pageSize);
            query.setParameter("userID", userID);
            return new ArrayList<>(query.list());
        } else if (category == 2) {
            Query query = sessionFactory.getCurrentSession().createQuery("" +
                    "select new Map(r.restaurantID as restaurantID,r.restaurantName as restaurantName,r.average_review as reviewScore," +
                    "r.cuisines as cuisines,r.restaurantImageUrl as rImageUrl, r.restaurantImageBlob as restaurantImageBlob," +
                    "concat(t.townName,',',c.cityName) as localityVerbose, t.townName as townName, c.cityName as cityName, " +
                    "r.latitude as rLatitude,r.longitude as rLongitude,r.status as status, r.address as address,r.category as category,r.hygiene_review as hygiene_review," +
                    "r.friendly_review as friendly_review,r.timings as timings,r.CleaningArrow as CleaningArrow, r.HygieneArrow as HygieneArrow)" +
                    " from Restaurant r inner join r.townID t inner join t.cityID c inner join Review rev on r.restaurantID=rev.restaurant.restaurantID  " +
                    "where rev.user.userID=:userID and r.category='Otel'").setFirstResult(pageSize * (page - 1)).setMaxResults(pageSize);
            query.setParameter("userID", userID);
            return new ArrayList<>(query.list());
        } else if (category == 3) {
            Query query = sessionFactory.getCurrentSession().createQuery("" +
                    "select new Map(r.restaurantID as restaurantID,r.restaurantName as restaurantName,r.average_review as reviewScore," +
                    "r.cuisines as cuisines,r.restaurantImageUrl as rImageUrl, r.restaurantImageBlob as restaurantImageBlob," +
                    "concat(t.townName,',',c.cityName) as localityVerbose, t.townName as townName, c.cityName as cityName, " +
                    "r.latitude as rLatitude,r.longitude as rLongitude,r.status as status, r.address as address,r.category as category,r.hygiene_review as hygiene_review," +
                    "r.friendly_review as friendly_review,r.timings as timings,r.CleaningArrow as CleaningArrow, r.HygieneArrow as HygieneArrow)" +
                    " from Restaurant r inner join r.townID t inner join t.cityID c inner join Review rev on r.restaurantID=rev.restaurant.restaurantID  " +
                    "where rev.user.userID=:userID and r.category='AVM'").setFirstResult(pageSize * (page - 1)).setMaxResults(pageSize);
            query.setParameter("userID", userID);
            return new ArrayList<>(query.list());
        } else if (category == 4) {
            Query query = sessionFactory.getCurrentSession().createQuery("" +
                    "select new Map(r.restaurantID as restaurantID,r.restaurantName as restaurantName,r.average_review as reviewScore," +
                    "r.cuisines as cuisines,r.restaurantImageUrl as rImageUrl, r.restaurantImageBlob as restaurantImageBlob," +
                    "concat(t.townName,',',c.cityName) as localityVerbose, t.townName as townName, c.cityName as cityName, " +
                    "r.latitude as rLatitude,r.longitude as rLongitude,r.status as status, r.address as address,r.category as category,r.hygiene_review as hygiene_review," +
                    "r.friendly_review as friendly_review,r.timings as timings,r.CleaningArrow as CleaningArrow, r.HygieneArrow as HygieneArrow)" +
                    " from Restaurant r inner join r.townID t inner join t.cityID c inner join Review rev on r.restaurantID=rev.restaurant.restaurantID " +
                    "where rev.user.userID=:userID and r.category='Benzin İstasyonu'").setFirstResult(pageSize * (page - 1)).setMaxResults(pageSize);
            query.setParameter("userID", userID);
            return new ArrayList<>(query.list());
        } else if (category == 5) {
            Query query = sessionFactory.getCurrentSession().createQuery("" +
                    "select new Map(r.restaurantID as restaurantID,r.restaurantName as restaurantName,r.average_review as reviewScore," +
                    "r.cuisines as cuisines,r.restaurantImageUrl as rImageUrl, r.restaurantImageBlob as restaurantImageBlob," +
                    "concat(t.townName,',',c.cityName) as localityVerbose, t.townName as townName, c.cityName as cityName, " +
                    "r.latitude as rLatitude,r.longitude as rLongitude,r.status as status, r.address as address,r.category as category,r.hygiene_review as hygiene_review," +
                    "r.friendly_review as friendly_review,r.timings as timings,r.CleaningArrow as CleaningArrow, r.HygieneArrow as HygieneArrow)" +
                    " from Restaurant r inner join r.townID t inner join t.cityID c inner join Review rev on r.restaurantID=rev.restaurant.restaurantID  " +
                    "where rev.user.userID=:userID and r.category in ('Spor salonu','Sinema Salonu','Eğlence merkezi')").setFirstResult(pageSize * (page - 1)).setMaxResults(pageSize);
            query.setParameter("userID", userID);
            return new ArrayList<>(query.list());
        } else if (category == 6) {
            Query query = sessionFactory.getCurrentSession().createQuery("" +
                    "select new Map(r.restaurantID as restaurantID,r.restaurantName as restaurantName,r.average_review as reviewScore," +
                    "r.cuisines as cuisines,r.restaurantImageUrl as rImageUrl, r.restaurantImageBlob as restaurantImageBlob," +
                    "concat(t.townName,',',c.cityName) as localityVerbose, t.townName as townName, c.cityName as cityName, " +
                    "r.latitude as rLatitude,r.longitude as rLongitude,r.status as status, r.address as address,r.category as category,r.hygiene_review as hygiene_review," +
                    "r.friendly_review as friendly_review,r.timings as timings,r.CleaningArrow as CleaningArrow, r.HygieneArrow as HygieneArrow)" +
                    " from Restaurant r inner join r.townID t inner join t.cityID c inner join Review rev on r.restaurantID=rev.restaurant.restaurantID " +
                    "where rev.user.userID=:userID and r.category='Other'").setFirstResult(pageSize * (page - 1)).setMaxResults(pageSize);
            query.setParameter("userID", userID);
            return new ArrayList<>(query.list());
        }
        return null;
    }

    @Override
    public Boolean supportMessage(String email, String body) {
        if (mailService.supportMessage(email, body))
            return true;
        else
            return false;
    }


    @Override
    public Boolean changePassword(String email, String password, String newPassword) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query query = sessionFactory.getCurrentSession().createQuery("select a.userID from AppUser as a where " +
                "a.userEmail=:email and a.userPassword=:password");
        query.setParameter("email", email);
        query.setParameter("password", password);
        if (query.getSingleResult() != null) {
            query = sessionFactory.getCurrentSession().createQuery("update AppUser as a set a.userPassword=:newPassword where " +
                    "a.userEmail=:email");
            query.setParameter("newPassword", newPassword);
            query.setParameter("email", email);
            query.executeUpdate();
            session.getTransaction().commit();
            session.close();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean changeUserName(String email, String name) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query query = sessionFactory.getCurrentSession().createQuery("select a.userName from AppUser as a where " +
                "a.userEmail=:email");
        query.setParameter("email", email);
        if (query.getSingleResult() != null) {
            query = sessionFactory.getCurrentSession().createQuery("update AppUser as a set a.userName=:name where " +
                    "a.userEmail=:email");
            query.setParameter("name", name);
            query.setParameter("email", email);
            query.executeUpdate();
            session.getTransaction().commit();
            session.close();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean isAdminId(String uniqueId) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        Query query = sessionFactory.getCurrentSession().createQuery("from AdminTK   where uniqueID=:uniqueId");
        query.setParameter("uniqueId", uniqueId);


        return query.getResultList().size() > 0 ? true : false;
    }

    @Override
    public Boolean resetPassword(String email) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            Query query = sessionFactory.getCurrentSession().createQuery("select a.userID from AppUser as a where " +
                    "a.userEmail=:email and a.userType='standard'");
            query.setParameter("email", email);
            if (query.uniqueResult() != null) {
                mailService.resetpassword(email);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }


    @Override
    public Boolean newPassword(String email, String newPassword) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            Query query = sessionFactory.getCurrentSession().createQuery("update AppUser as a set a.userPassword=:newPassword where " +
                    "a.userEmail=:email and a.userType='standard' ");
            query.setParameter("email", email);
            query.setParameter("newPassword", newPassword);
            query.executeUpdate();
            session.getTransaction().commit();
            session.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean deleteMyPlace(long resID, long userID) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        try {
            Query query = sessionFactory.getCurrentSession().createQuery("select u.placeID from UserPlaces as u where u.restaurant.restaurantID=:resID and " +
                    "u.user.userID=:userID");
            query.setParameter("resID", resID);
            query.setParameter("userID", userID);
            if (query.uniqueResult() != null) {
                Query query1 = session.createQuery("delete from FavoritePlace as r where r.restaurant.restaurantID=:resID");
                Query query2 = session.createQuery("delete from Report as r where r.restaurant.restaurantID=:resID");
                Query query3 = session.createQuery("delete from Review as r where r.restaurant.restaurantID=:resID");
                Query query4 = session.createQuery("delete from UserPlaces as r where r.restaurant.restaurantID=:resID");
                Query query5 = session.createQuery("delete from Restaurant as r where r.restaurantID=:resID");
                query1.setParameter("resID", resID);
                query2.setParameter("resID", resID);
                query3.setParameter("resID", resID);
                query4.setParameter("resID", resID);
                query5.setParameter("resID", resID);
                query1.executeUpdate();
                query2.executeUpdate();
                query3.executeUpdate();
                query4.executeUpdate();
                query5.executeUpdate();
                session.getTransaction().commit();
                session.close();
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Boolean invalidPlace(long resID, long userID, String timingValue) {
        Session session = sessionFactory.openSession();
        try {
            Query query = sessionFactory.getCurrentSession().createQuery("select u.placeID from UserPlaces as u where u.restaurant.restaurantID=:resID and " +
                    "u.user.userID=:userID");
            query.setParameter("resID", resID);
            query.setParameter("userID", userID);
            if (query.uniqueResult() != null) {
                Query ck = sessionFactory.getCurrentSession().createQuery("select r.status from Restaurant as r where r.restaurantID=:resID");
                ck.setParameter("resID", resID);
                if (ck.uniqueResult().toString().equals("1")) {
                    Query ck2 = sessionFactory.getCurrentSession().createQuery("update Restaurant as r set r.timings=:timingValue , r.status=0 where r.restaurantID=:resID");
                    ck2.setParameter("resID", resID);
                    ck2.setParameter("timingValue", timingValue);
                    ck2.executeUpdate();
                    return true;
                } else {

                    Query query1 = sessionFactory.getCurrentSession().createQuery("update Restaurant as r set r.timings=:timingValue , r.status=1 where r.restaurantID=:resID");
                    query1.setParameter("resID", resID);
                    query1.setParameter("timingValue", timingValue);
                    Query query2 = sessionFactory.getCurrentSession().createQuery("update UserPlaces as r set r.placeStatus=1 where r.restaurant.restaurantID=:resID");
                    query2.setParameter("resID", resID);
                    query1.executeUpdate();
                    query2.executeUpdate();
                    return true;
                }

            }
            return false;
        } catch (Exception e) {
            return false;

        }
    }

    @Override
    public List<Object> getTopUserList() {
        Session session = sessionFactory.openSession();
        try {
            Query query = session.createQuery("select new Map(r.user.userID as userID," +
                    "r.user.userName as userName, r.user.userImageUrl as userImageUrl, r.user.profilImageID as profilImageID," +
                    "count(r.user.userID) as x) from Review r " +
                    "GROUP BY r.user.userID  ORDER BY x desc").setMaxResults(20);
            List getTopUserList = query.getResultList();
            session.close();
            return getTopUserList;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Object> getUserDetail(long userID) {
        Session session = sessionFactory.openSession();
        try {
            Query query = session.createQuery("select new Map(r.user.userID as userID," +
                    "r.user.userName as userName, r.user.userImageUrl as userImageUrl, r.user.profilImageID as profilImageID, r.user.coverImage as coverImage," +
                    "count(r.user.userID) as x) from Review r where r.user.userID=:userID " +
                    "GROUP BY r.user.userID  ORDER BY x desc").setMaxResults(20);
            query.setParameter("userID", userID);
            query.getResultList();
            List getTopUserList = query.getResultList();
            session.close();
            return getTopUserList;
        } catch (Exception e) {
            return null;
        }
    }
}


