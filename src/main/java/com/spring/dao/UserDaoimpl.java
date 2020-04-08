package com.spring.dao;

import com.spring.model.AppUser;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by egulocak on 8.04.2020.
 */


@Repository

public class UserDaoimpl implements UserDAO {

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
    public ArrayList<AppUser> listAllUsers() {
        try {
            Query query = sessionFactory.getCurrentSession().createQuery("from AppUser"); //burdaki sorgu ırmızı çıkabilir ancak çalışıyo.
            return (ArrayList<AppUser>) query.getResultList();
        } catch (Exception e) {
            System.out.println(e.getMessage());

            return null;
        }

    }

    @Override
    public AppUser updateUser(AppUser user) { //Kullanıcının update olacak hali geliyor saedece id aynı
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
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }


    }


}
