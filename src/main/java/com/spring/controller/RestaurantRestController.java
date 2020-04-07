package com.spring.controller;

import com.spring.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by egulocak on 7.04.2020.
 */

@RestController
@RequestMapping("/restaurant")
public class RestaurantRestController {

    @Autowired
    RestaurantService restaurantService;


    @RequestMapping(value = "/deleteAllRestaurants", method = RequestMethod.GET)
    public ResponseEntity<Void> deleteAllRestaurants()   //Bütün restorantları silen endpoint
    {
        if (restaurantService.deleteAllRestaurants()==true)

            return new ResponseEntity<Void>(HttpStatus.OK); //restaurantDAO true gelirse ok döner

        else

            return new ResponseEntity<Void>(HttpStatus.NOT_MODIFIED); //restaurantDAO false dönerse NOT MODIFIED döner.

    }

}
