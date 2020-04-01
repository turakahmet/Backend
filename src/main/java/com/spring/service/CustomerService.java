package com.spring.service;
import java.util.List;

import com.spring.model.Customer;

public interface CustomerService {

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

