package com.spring.dao;

import com.spring.model.AdminTK;
import com.spring.model.AppUser;
import com.spring.model.CustomUser;
import com.spring.model.Review;
import com.spring.service.MailService;
import com.spring.token.Validation;
import lombok.Setter;
//import lombok.launch.PatchFixesHider;
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
    @Autowired
    MailService mailService;

    @Setter
    @Autowired
    SessionFactory sessionFactory;

    @Autowired
    Validation validation;

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

        System.out.println("CheckStandard credentials");
        Query query = sessionFactory.getCurrentSession().createQuery("from AppUser where userEmail=:userEmail and userPassword =: userPassword ");
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
    public String getusertype(String email) {
        String result;
        try{

            System.out.println("EMAİL!!!:"+email);
            Query query = sessionFactory.getCurrentSession().
                    createQuery("select userType from AppUser where userEmail=:userEmail");
            query.setParameter("userEmail", email);

            result = (String) query.getSingleResult();
            return result;
        }
        catch(Exception e){

             e.printStackTrace();
            return e.getMessage();

        }

    }

    @Override
    public CustomUser findUserByEmail(String userEmail) {
        try{
            Session session = sessionFactory.openSession();


            CustomUser cUser = new CustomUser();
            Query query = sessionFactory.getCurrentSession()
                    .createQuery("from AppUser where userEmail =: userEmail");
            query.setParameter("userEmail", userEmail);

            AppUser aUser =  (AppUser) query.uniqueResult();
            session.close();
            cUser.setUserID(aUser.getUserID());
            cUser.setUserEmail(aUser.getUserEmail());
            cUser.setUserName(aUser.getUserName());
            cUser.setUserSurname(aUser.getUserSurname());
            cUser.setProfilImageID(aUser.getProfilImageID());


            return cUser;
        }

        catch(Exception e)
        {
            System.out.println(e.getMessage() +"EFEE");
            return null;
        }


    }



    @Override
    public List<Object> listAllUsers( ) {
        try {
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
                Query query = session.createQuery("select new Map(a.userID as userID ,a.userName as userName,a.userSurname as userSurname," +
                        "a.userEmail as userEmail,a.profilImageID as profilImageID,a.userToken as userToken," +
                        "a.userType as userType,a.status as status) from AppUser a");
                List<Object> userList = query.list();
                transaction.commit();
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
            Query query = sessionFactory.getCurrentSession().createQuery("from AppUser where userEmail=:email");
            query.setParameter("email", email);
            if (query.getResultList().size() > 0)
                return true;
            else return false;


        } catch (Exception e) {
            return null;

        }


    }

    @Override
    public Boolean isUserActive(String email) {


        Query query = sessionFactory.getCurrentSession().createQuery("from AppUser where userEmail =: userEmail and status =: status and userType=:userType");
        query.setParameter("userEmail",email);
        query.setParameter("status","active");
        query.setParameter("userType","standard"); //parola değiştirecek kullanıcı standard olmalı

        if(query.uniqueResult() != null)
            return true;
        else
            return false;


    }


    @Override
    public Boolean checkUserCode(String email, long code) {
        Query query = sessionFactory.getCurrentSession()
                .createQuery("from AppUser  where userEmail =: userEmail and code=:code ");
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
            upUser.setUserPassword(null);
            return upUser;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
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
            aUser.setCode(mailService.sendMail(email,aUser.getUserPassword()));
            session.update(aUser);
            tx.commit();
            session.close();
            return true;

        }
        catch (Exception e){
            System.out.println("CATCH!!!:"+e.getMessage());
            //Catche giriyor sessionlarla alakalı problem var. Burası fixlenmeli

        }
        finally {
            return true;
        }
    }

    @Override
    public String changeusername(AppUser user) {

        try{
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            if(getusertype(user.getUserEmail()).equals("google")){
                Query query = sessionFactory.getCurrentSession().createQuery("from AppUser   where userEmail =:email");
                query.setParameter("email",user.getUserEmail());
                AppUser tempUser = (AppUser) query.uniqueResult();

                AppUser upUser = (AppUser) session.get(AppUser.class, tempUser.getUserID());

                if(user.getProfilImageID()!= null){
                    upUser.setProfilImageID(user.getProfilImageID());
                }



                //update işlemi başlar
                session.update(upUser);
                transaction.commit();
                session.close();
                return "ok";

            }
            else{
                Query query = sessionFactory.getCurrentSession().createQuery("from AppUser   where userEmail =:email");
                query.setParameter("email",user.getUserEmail());



                AppUser tempUser = (AppUser) query.uniqueResult();

                AppUser upUser = (AppUser) session.get(AppUser.class, tempUser.getUserID());
                if(user.getUserName()!=null)
                    upUser.setUserName(user.getUserName());
                if(user.getProfilImageID()!= null){
                    upUser.setProfilImageID(user.getProfilImageID());
                }



                //update işlemi başlar
                session.update(upUser);
                transaction.commit();
                session.close();
                return "ok";

            }


        }
        catch(Exception e){

            System.out.println("HATAAA:"+e.getMessage());
            return "err";
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
            upUser.setCode(0L);
            upUser.setStatus("active");
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
        Query query = session.createQuery("select new map(r.id as id,r.question1 as q1,r.question2 as q2,r.question3 as q3,r.question4 as q4,r.question5 as q5,r.question6 as q6," +
                "r.question7 as q7,r.question8 as a8,r.question9 as q9,r.average as average ,r.hygieneAverage as hygieneavg,r.friendlyAverage  as friendlyavg )from Review r where user.userEmail =:email  ");
        query.setParameter("email",email);
        List<Review> reviewList = query.list();

        return reviewList;




    }

    @Override
    public Boolean isadmin(AdminTK adminTK) {
        try{

            int status  = 0;
            Session session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();

            Query isAdmin = sessionFactory.getCurrentSession().createQuery("from AdminTK  where uniqueID  =: uniqueID and adminIDName =:" +
                    " name and adminPW =: pw and adminStatus =: status " );
            isAdmin.setParameter("uniqueID",adminTK.getUniqueID());
            isAdmin.setParameter("name",adminTK.getAdminIDName());
            isAdmin.setParameter("pw",adminTK.getAdminPW());
            isAdmin.setParameter("status",adminTK.getAdminStatus());
            if(isAdmin.uniqueResult() != null)
                return true;
            else
                return false;
        }

        catch(Exception e)
        {

            System.out.println("HATA: "+e.getMessage());
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

        query.setParameter("email",email);
        if(query.uniqueResult() !=null){
            CustomUser cUser = findUserByEmail(email);
            Query query3 = session.createQuery("select new Map(r.average as average,r.reviewDate as date,r.restaurant.restaurantID as restaurantID,r.id as ID,r.hygieneAverage as hygieneAverage,r.friendlyAverage as friendlyAverage" +
                    "  ,r.restaurant.restaurantName as restaurantName  ,r.restaurant.restaurantImageUrl as restaurantImage) from Review  r where user.userID =: id ORDER BY  reviewDate ASC ");
            query3.setParameter("id",cUser.getUserID());


            System.out.println(cUser.getUserID());



//        query2.setParameter("email",email);
            List<Object> reviewList = query3.list();
            transaction.commit();
//        <ListAppUser aUser =  (AppUser) query2.uniqueResult();

            return reviewList;
        }

        //TODO:BURADA DAHA SONRA İYİLEŞTİRME YAPICAM.
        else{
            return null;

        }

    }

    @Override
    public Long getreviewcount(String email,String password) {
        if(getusertype(email).equals("google")){
            System.out.println("GOOGLE USER COUNT REQUEST");
            if(validation.isValidateGoogle(email,password)){
                System.out.println("GOOGLE USER COUNT REQUEST VALİDATED");

                Session session = sessionFactory.openSession();
                Transaction transaction = session.beginTransaction();
                Query query = session.createQuery("select COUNT(*)  from Review where user.userEmail =: email  ");
                query.setParameter("email",email);
                return (Long) query.getSingleResult();


            }
            else
                return null;
        }

        else{
            Session session = sessionFactory.openSession();
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("select COUNT(*)  from Review where user.userEmail =: email  ");
            query.setParameter("email",email);


            return (Long) query.getSingleResult();
        }





    }


    @Override
    public String changpassword(String email,String password,String newpw) {
        try {
            Session session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("from AppUser where userEmail =: userEmail and userPassword=: password ");
            query.setParameter("userEmail",email);
            query.setParameter("password",password);


            if(query.uniqueResult() != null)
            {
                AppUser tempUser = (AppUser) query.uniqueResult();

                AppUser upUser = (AppUser) session.get(AppUser.class, tempUser.getUserID());
                upUser.setUserPassword(newpw);


                //update işlemi başlar
                session.update(upUser);
                tx.commit();
                session.close();
                return "ok";
            }

            else {
                System.out.println("NO UNİQUE RESULT");
                return "User does not Exist!!!";

            }


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
        if(category.equals("Benzin İstasyonu") || category.equals("AVM") || category.equals("Otel")){

            Query general = session.createQuery("select new Map(r.average as average,r.restaurant.restaurantID as restaurantID,r.id as ID,r.reviewDate as date,r.hygieneAverage as hygieneAverage,r.friendlyAverage as friendlyAverage" +
                    "  ,r.restaurant.restaurantName as restaurantName  ,r.restaurant.restaurantImageUrl as restaurantImage,r.restaurant.category as restaurantCategory) from Review  r where user.userID =: id and r.restaurant.category =: category ORDER BY  reviewDate ASC");
            general.setParameter("id",cUser.getUserID());
            general.setParameter("category",category);
            List<Object> reviewList = general.list();
            transaction.commit();
            return reviewList;

            //General querysi gelen categoryi direk işleyerek sorgu atar(Alt kategorisi olmayan kategoriler için)
            //Benzinlik,otel,AVM kategorileri için

        }

        else if(category.equals("Restaurant")){
            Query restaurant = session.createQuery("select new Map(r.average as average,r.reviewDate as date,r.restaurant.restaurantID as restaurantID,r.id as ID,r.hygieneAverage as hygieneAverage,r.friendlyAverage as friendlyAverage" +
                    "  ,r.restaurant.restaurantName as restaurantName  ,r.restaurant.restaurantImageUrl as restaurantImage,r.restaurant.category as restaurantCategory) from Review  r where user.userID =: id and r.restaurant.category ='Kafe' " +
                    "or r.restaurant.category ='Türk Mutfağı' or r.restaurant.category ='Tatlı' or r.restaurant.category ='Bar&Pubs' or r.restaurant.category ='Dünya Mutfağı' ORDER BY  reviewDate ASC");
            restaurant.setParameter("id",cUser.getUserID());
            List<Object> reviewList = restaurant.list();
            transaction.commit();
            return reviewList;
        }
        else if(category.equals("Halka Açık")){
            Query public_ = session.createQuery("select new Map(r.average as average,r.reviewDate as date,r.restaurant.restaurantID as restaurantID,r.id  as ID,r.hygieneAverage as hygieneAverage,r.friendlyAverage as friendlyAverage" +
                    "  ,r.restaurant.restaurantName as restaurantName  ,r.restaurant.restaurantImageUrl as restaurantImage,r.restaurant.category as restaurantCategory) from Review  r where user.userID =: id and r.restaurant.category ='Spor Salonu' " +
                    "or r.restaurant.category ='sinema' or r.restaurant.category ='Eğlence Merkezi' ORDER BY  reviewDate ASC");
            public_.setParameter("id",cUser.getUserID());
            List<Object> reviewList = public_.list();
            transaction.commit();
            return reviewList;

        }

        else{
            Query other = session.createQuery("select new Map(r.average as average,r.reviewDate as date,r.id as ID,r.restaurant.restaurantID as restaurantID,r.hygieneAverage as hygieneAverage,r.friendlyAverage as friendlyAverage" +
                    "  ,r.restaurant.restaurantName as restaurantName  ,r.restaurant.restaurantImageUrl as restaurantImage,r.restaurant.category as restaurantCategory) from Review  r where user.userID =: id and r.restaurant.category <> 'Kafe'" +
                    "and r.restaurant.category <> 'Türk Mutfağı' and r.restaurant.category <> 'Bar&Pubs' and r.restaurant.category <> 'Dünya Mutfağı'" +
                    "and r.restaurant.category <> 'Otel' and r.restaurant.category <> 'Benzin İstasyonu' and r.restaurant.category <> 'Spor Salonu'" +
                    "and r.restaurant.category <> 'AVM' and r.restaurant.category <> 'sinema' and r.restaurant.category <> 'Eğlence Merkezi' ORDER BY  reviewDate ASC ");
            other.setParameter("id",cUser.getUserID());
            List<Object> reviewList = other.list();
            transaction.commit();
            return reviewList;
        }







//        query2.setParameter("email",email);
//        <ListAppUser aUser =  (AppUser) query2.uniqueResult();

    }

    @Override
    public Boolean insertpwcode(String email,String code) {

        try {
            Session session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();
            Query query = sessionFactory.getCurrentSession().createQuery("from AppUser where userEmail =: userEmail");
            query.setParameter("userEmail",email);


            if(query.uniqueResult() != null)
            {
                AppUser tempUser = (AppUser) query.uniqueResult();

                AppUser upUser = (AppUser) session.get(AppUser.class, tempUser.getUserID());
                upUser.setResetCode(code);


                //update işlemi başlar
                session.update(upUser);
                tx.commit();
                session.close();
                return true;
            }

            else {
                System.out.println("NO UNİQUE RESULT");
                return false;
            }


        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    @Override
    public Boolean setpassword(String email, String newpw, String token) {


        try {
            if(token == null)
                return false;
                else {
                Session session = sessionFactory.openSession();
                Transaction transaction = session.beginTransaction();
                Query query = sessionFactory.getCurrentSession().createQuery("from AppUser   where userEmail =:email and resetCode =:token");
                query.setParameter("email", email);
                query.setParameter("token", token);
                AppUser tempUser = (AppUser) query.uniqueResult();
                AppUser upUser = (AppUser) session.get(AppUser.class, tempUser.getUserID());
                upUser.setUserPassword(newpw);
                upUser.setResetCode(null);
                //update işlemi başlar
                session.update(upUser);
                transaction.commit();
                session.close();
                return true;
            }

        } catch (Exception e) {
            System.out.println("HATA "+e.getMessage());
            return false;
        }
    }
    @Override
    public Boolean isAdminId(String uniqueId) {
        Session session = sessionFactory.openSession();
        Transaction tx  = session.beginTransaction();
        Query query = sessionFactory.getCurrentSession().createQuery("from AdminTK   where uniqueID=:uniqueId");
        query.setParameter("uniqueId",uniqueId);




        return query.getResultList().size()>0 ? true : false;
    }

}


