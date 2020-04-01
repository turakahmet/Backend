package com.spring.dao;

import java.util.List;
import com.spring.model.Customer;
import org.springframework.stereotype.Repository;

public interface CustomerDAO {

    Customer findById(long id);

    Customer findByName(String name);

    Customer findByEmail(String email);

    void Create(Customer customer);

    void Update(Customer customer);

    void Delete(long id);

    List<Customer>findAllCustomer();

    void deleteAllCustomers();

    boolean isCustomerExist(Customer customer);
}