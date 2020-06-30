package com.spring.dao;

import com.spring.model.AppUser;
import com.spring.model.CustomUser;
import com.spring.model.Review;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by egulocak on 8.04.2020.
 */

@Transactional
@Repository
public class UserDaoimpl implements UserDAO {

    @Setter
    @Autowired
    SessionFactory sessionFactory;

    @Override
    public AppUser insertUser(AppUser user) {


        try {
            sessionFactory.getCurrentSession().save(user);

            return user;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;


        }

    }


    @Override
    public boolean checkStandardCredentials(String userEmail, String password) {


        Query query = sessionFactory.getCurrentSession().
                createQuery("from AppUser where userEmail=:userEmail and userPassword =: userPassword");
        query.setParameter("userEmail", userEmail);
        query.setParameter("userPassword", password);



        if (query.uniqueResult() != null) {
            return true;
        } else
            return false;


        //ToDo hangisi yanlıssa onu de bildiren bir  query vs yazılabilir.


    }

    @Override
    public boolean checkGoogleCredentials(AppUser user) {
        return false;
    }

    @Override
    public CustomUser findUserByEmail(String userEmail) {


        CustomUser cUser = new CustomUser();
        Query query = sessionFactory.getCurrentSession()
                .createQuery("from AppUser where userEmail =: userEmail");
        query.setParameter("userEmail", userEmail);

        AppUser aUser =  (AppUser) query.uniqueResult();

        cUser.setUserID(aUser.getUserID());
        cUser.setUserEmail(aUser.getUserEmail());
        cUser.setUserName(aUser.getUserName());
        cUser.setUserSurname(aUser.getUserSurname());
        return cUser;

    }



    @Override
    public List<Object> listAllUsers() {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("select new Map(a.userID as userID ,a.userName as userName,a.userSurname as userSurname," +
                    "a.userEmail as userEmail,a.profilImageID as profilImageID,a.userToken as userToken," +
                    "a.userType as userType,a.status as status) from AppUser a");
            @SuppressWarnings("unchecked")
            List<Object> userList = query.list();
            transaction.commit();
            return userList;
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return null;
        }
    }

    @Override
    public AppUser updateUser(AppUser user) {
        //Kullanıcının update olacak hali geliyor saedece id aynı


        try {
            Session session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();

            AppUser upUser = (AppUser) session.get(AppUser.class, user.getUserID()); //idyi burda yakalayıp bu idde klon kullanıcı oluşuyor.
            //neler değişecekse ilgili şeyler altta yapılır.
            upUser.setUserPassword(user.getUserPassword());
            upUser.setUserName(user.getUserName());
            upUser.setUserSurname(user.getUserSurname());
            upUser.setUserEmail(user.getUserEmail());

            //update işlemi başlar
            session.update(upUser);
            tx.commit();
            session.close();
            return upUser;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }


    }

    @Override
    public Boolean isUserExist(String email) {

        try {
            Query query = sessionFactory.getCurrentSession().createQuery("from AppUser where userEmail=:email");
            query.setParameter("email", email);
            if (query.getResultList().size() > 0)
                return true;
            else return false;


        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }


    }

    @Override
    public Boolean isUserActive(String email) {


        Query query = sessionFactory.getCurrentSession().createQuery("from AppUser where userEmail =: userEmail and status =: status");
        query.setParameter("userEmail",email);
        query.setParameter("status","active");

        if(query.uniqueResult() != null)
            return true;
        else
            return false;


    }


    @Override
    public Boolean checkUserCode(String email, long code) {
        Query query = sessionFactory.getCurrentSession()
                .createQuery("from AppUser  where userEmail =: userEmail and code=:code");
        query.setParameter("userEmail",email);
        query.setParameter("code",code);
        if(query.uniqueResult()!=null){
            changeUserCode(email,code);
            return true;
        }

        else
            return false;
    }


    @Override
    public AppUser updateUserStatus(String email) {

        try {
            Session session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("from AppUser where userEmail =: userEmail");
            query.setParameter("userEmail",email);


            AppUser tempUser = (AppUser) query.uniqueResult();

            AppUser upUser = (AppUser) session.get(AppUser.class, tempUser.getUserID());
            upUser.setStatus("active");
            //idyi burda yakalayıp bu idde klon kullanıcı oluşuyor.
            //neler değişecekse ilgili şeyler altta yapılır.

            //update işlemi başlar
            session.update(upUser);
            tx.commit();
            session.close();
            return upUser;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }



    @Override
    public void changeUserCode(String email, long code) {

        try {
            Session session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("from AppUser where userEmail =: userEmail and code=:code");
            query.setParameter("userEmail",email);
            query.setParameter("code",code);


            AppUser tempUser = (AppUser) query.uniqueResult();

            AppUser upUser = (AppUser) session.get(AppUser.class, tempUser.getUserID());
            upUser.setCode(0);
            //idyi burda yakalayıp bu idde klon kullanıcı oluşuyor.
            //neler değişecekse ilgili şeyler altta yapılır.

            //update işlemi başlar
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
        CustomUser  cUser = new CustomUser();
        cUser = findUserByEmail(email);
        long id = cUser.getUserID();
        Query query = session.createQuery("from Review where user =:id  ");
        query.setParameter("id",id);
        List<Review> reviewList = query.list();

        return reviewList;




    }

    @Override
    public List<Object> getuserreviews(String email) {

        Session session = sessionFactory.openSession();

        Transaction transaction = session.beginTransaction();

        Query query = session.createQuery("select a.userID as userID ,a.userName as userName,a.userSurname as userSurname," +
                "a.userEmail as userEmail,a.profilImageID as profilImageID,a.userToken as userToken," +
                "a.userType as userType,a.status as status from AppUser a where userEmail =: email");
        CustomUser cUser = findUserByEmail(email);
        System.out.println(cUser.getUserID());

        //TODO:BURADA DAHA SONRA İYİLEŞTİRME YAPICAM.
        Query query3 = session.createQuery("select new Map(r.average as average,r.reviewDate as date,r.hygieneAverage as hygieneAverage,r.friendlyAverage as friendlyAverage" +
                "  ,r.restaurant.restaurantName as restaurantName  ,r.restaurant.restaurantImageUrl as restaurantImage) from Review  r where user.userID =: id");
        query3.setParameter("id",cUser.getUserID());





//        query2.setParameter("email",email);
        List<Object> reviewList = query3.list();
        transaction.commit();
//        <ListAppUser aUser =  (AppUser) query2.uniqueResult();

        return reviewList;
    }

    @Override
    public Long getreviewcount(String email) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("select COUNT(*)  from Review where user.userEmail =: email  ");
        query.setParameter("email",email);


        return (Long) query.getSingleResult();




    }

    @Override
    public String changpassword(String email,String password) {
        try {
            Session session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("from AppUser where userEmail =: userEmail");
            query.setParameter("userEmail",email);


            AppUser tempUser = (AppUser) query.uniqueResult();

            AppUser upUser = (AppUser) session.get(AppUser.class, tempUser.getUserID());
            upUser.setUserPassword(password);


            //update işlemi başlar
            session.update(upUser);
            tx.commit();
            session.close();
            return "ok";
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public List<Object> getcategoryinfo(String email) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();

        Query query3 = session.createQuery("select new Map(count(r2.category) as count,r2.category as category) from Review r,Restaurant r2 , AppUser  a where r.user.userID = a.userID and " +
                "r.restaurant.restaurantID = r2.restaurantID and a.userEmail =:email group by r2.category");
        query3.setParameter("email",email);


        List<Object> reviewList = query3.list();
        transaction.commit();
        return reviewList;
    }


}
