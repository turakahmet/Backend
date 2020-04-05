package com.spring.controller;

import com.spring.model.Customer;
import com.spring.service.CustomerService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/customer")
public class CustomerRestController
{
    @Autowired
    private CustomerService customerService;

    public void setCustomerService(CustomerService customerService)
    {
        this.customerService = customerService;
    }

    //add new context
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public ResponseEntity<Void> createCustomer(@RequestBody Customer customer)
    {
        if(customerService.isCustomerExist(customer))
        {
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
        else
        {
            customerService.Create(customer);
            return new ResponseEntity<Void>(HttpStatus.CREATED);
        }
    }
    @RequestMapping(value = "/searchByEmail", method = RequestMethod.GET)
    public ResponseEntity<Customer> searchByEmail(@RequestParam("email") String email)
    {
        if(customerService.findByEmail(email)==null)
        {
            return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
        }
        else
        {
            return new ResponseEntity<Customer>(customerService.findByEmail(email),HttpStatus.OK);
        }
    }
}
