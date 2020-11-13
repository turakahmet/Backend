package com.spring.dao;

import com.spring.model.Log;
import com.spring.requestenum.RequestDescriptions;
import com.spring.requestenum.sizes;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Transactional
@Repository
public class LogDaoImp implements LogDao {

    int size = 0;


    @Setter
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public String savelog(Log log) {
        Session session = sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        try {
            sessionFactory.getCurrentSession().save(log);
            return "Log created succesfully";
        } catch (Exception e) {

            e.printStackTrace();
            return "An error occured when saving log";

        }


    }

    @Override
    public Boolean getrecordcount(LocalDate localDate, String remoteIp, String action) {
        setSize(action);


        Log log = new Log();
        Query query = sessionFactory.getCurrentSession().createQuery(" from Log where requestIp =: remoteip  and requestDesc=:action   ");
        query.setParameter("remoteip", remoteIp);
        query.setParameter("action", action);
        query.setMaxResults(size + 1);


        System.out.println("REQUESTIP: " + remoteIp);

        System.out.println("DESCRIPTION: " + action);

        System.out.println("SIZE: " + query.getResultList().size());


        System.out.println("----------------LOG DATA ----------------------");

        System.out.println(log.getRequestDesc());

        return query.getResultList().size() >= size ? false : true;
    }

    public void setSize(String action) {
        if (action.equals(RequestDescriptions.NEWACCOUNT.getText()) || action.equals(RequestDescriptions.SENDMAIL.getText())
                || action.equals(RequestDescriptions.SETPASSWORD.getText()) || action.equals(RequestDescriptions.VOTERESTAURANT.getText())
                || action.equals(RequestDescriptions.UPDATEVOTE.getText()) || action.equals(RequestDescriptions.GETRECORD.getText())
                || action.equals(RequestDescriptions.SAVERECORD.getText()))
            this.size = sizes.SIGNLOGIN.getSize();


        else if (action.equals(RequestDescriptions.STANDARDLOGIN.getText()) || action.equals(RequestDescriptions.GOOGLELOGIN))
            this.size = sizes.LOGINS.getSize();
        else if (action.equals(RequestDescriptions.SUPPORT.getText()))
            this.size = sizes.SUPPORT.getSize();
        else
            this.size = sizes.credentialschange.getSize();
    }


}
