package com.spring.token;

import com.spring.model.Review;
import lombok.Setter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

/**
 * Created by egulocak on 6.07.2020.
 */
@Repository
@Transactional
public class ValidationDao implements  Validation {

    @Setter
    @Autowired
    private SessionFactory sessionFactory;
    @Override
    public Boolean isvalidate(Token token) {
        try{
            Session session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();


                Query query = sessionFactory.getCurrentSession().createQuery("from AppUser where userEmail =: email  and userPassword =: password ");
                query.setParameter("email",token.getEmail());
                query.setParameter("password",token.getPassword());

                if(query.uniqueResult() != null)
                    return true;
                else {
                    System.out.println("---------------ELSEDE");
                    System.out.println(token.email);
                    System.out.println(token.password);

                    return false;
                }




        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }

    }

    @Override
    public Boolean isValidateAction(Review review,String email,String password) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();



        Query query = sessionFactory.getCurrentSession().createQuery("from Review where user.userEmail=:email and user.userPassword=:password and reviewID =:reviewid");
        query.setParameter("email",email);
        query.setParameter("password",password);
        query.setParameter("reviewid",review.getReviewID());
        if(query.uniqueResult() != null)
            return true;
        else{
            System.out.println("---------------ELSEDE");
            return false;


        }
    }

    @Override
    public Boolean isValidateRequest(String email, String token) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();

        Query query = sessionFactory.getCurrentSession().createQuery("from AppUser where userEmail=:email and resetCode=:token ");
        query.setParameter("email",email);
        query.setParameter("token",token);
        if(query.uniqueResult() != null)
            return true;
        else{
            System.out.println("---------------ELSEDE");
            return false;


        }
    }

    @Override
    public String generatetoken() {
        System.out.println("CALISTIIIIIII");
        char[] chars = "abcdefghij1234567klmnopqrstuvwxyz".toCharArray();
        StringBuilder sb = new StringBuilder(75);
        Random random = new Random();
        for (int i = 0; i < 75; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }
}
