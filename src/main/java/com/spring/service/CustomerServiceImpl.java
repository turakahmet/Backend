package com.spring.service;

import com.spring.dao.CustomerDAO;
import com.spring.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerDAO customerDAO;

    public void setCustomerDAO(CustomerDAO customerDAO){
        this.customerDAO=customerDAO;
    }

    @Override
    public Customer findById(long id) {
        return customerDAO.findById(id);
    }

    @Override
    public Customer findByName(String name) {
        return customerDAO.findByName(name);
    }



    @Override
    public void Create(Customer customer) {
        customerDAO.Create(customer);
    }

    @Override
    public void Update(Customer customer) {
        customerDAO.Update(customer);
    }

    @Override
    public void Delete(long id) {

    }

    @Override
    public List<Customer> findAllCustomer() {
        return customerDAO.findAllCustomer();
    }

    @Override
    public void deleteAllCustomers() {
    customerDAO.deleteAllCustomers();
    }

    @Override
    public boolean isCustomerExist(Customer customer) {
        return customerDAO.isCustomerExist(customer);

    }
    @Override
    public Customer findByEmail(String email)
    {
        return customerDAO.findByEmail(email);
    }

}
