package com.spring.dao;

import com.spring.model.Customer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.hibernate.Criteria;
import java.util.List;

@Repository

public class CustomerDAOimpl implements CustomerDAO{


    @Autowired
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory){
        this.sessionFactory=sessionFactory;
    }

    public Customer findById(long id) {
        Session session=sessionFactory.openSession();
        Transaction transaction=session.beginTransaction();
        Customer customer=new Customer();
        try {
            customer=(Customer)session.get(Customer.class,id);
            transaction.commit();
            session.close();
        }catch (Exception e){
            transaction.rollback();
            session.close();
        }
        return customer;
    }


    public Customer findByName(String name) {
        Session session=sessionFactory.openSession();
        Transaction transaction=session.beginTransaction();
        Customer customer=new Customer();
        String hql="from com.spring.model.Customer where name= ?";
        try {
            Query query=session.createNativeQuery(hql);
            //query.setParameter(o,name);
            customer=(Customer)query.uniqueResult();
            transaction.commit();
            session.close();

        }catch (Exception e){
            transaction.rollback();
            session.close();
        }

        return customer;
    }


    public Customer findByEmail(String email) {

        String hql="from Customer where email=:email";
            try {
                Query query = sessionFactory.getCurrentSession().createQuery(hql);
                query.setString("email", email);
                System.out.println(query.getResultList().size());
                if(query.getResultList().size()>0)
                    return (Customer) query.getSingleResult();
                else
                    return null;
            }

            catch (Exception e){
                return null;
            }






    }

    public void Create(Customer customer) {
    Session session=sessionFactory.openSession();
    Transaction transaction=session.beginTransaction();
    if (customer!=null){
        try {
            session.save(customer);
            transaction.commit();
        }catch (Exception e){
            transaction.rollback();
            session.close();
        }

    }

    }
    @Override
    public void Update(Customer p) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(p);
        logger.info("Customer updated successfully, Customer Details="+p);
    }

    @Override
    public void Delete(long id) {
    }

    @SuppressWarnings("unchecked")
    public List<Customer> findAllCustomers() {
        Customer customer = createEntityCriteria();
        return (List<Customer>)findAllCustomers();

        }




    @Override
    public void deleteAllCustomers() {

    }

    @Override
    public boolean isCustomerExist(Customer customer) {
        return false;
    }
}
