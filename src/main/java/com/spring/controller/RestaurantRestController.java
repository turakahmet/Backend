package com.spring.controller;

import com.spring.model.Restaurant;
import com.spring.model.Review;
import com.spring.service.RestaurantService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/restaurant")
public class RestaurantRestController {

    @Setter
    @Autowired
    private RestaurantService restaurantService;


    //add new context
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public ResponseEntity<Void> createRestaurant(@RequestBody Restaurant restaurant) {

            restaurantService.Create(restaurant);
            return new ResponseEntity<Void>(HttpStatus.CREATED);

    }

    // deleteAllRestaurants
    @RequestMapping(value = "/deleteAllRestaurants", method = RequestMethod.GET)
    public ResponseEntity<Void> deleteAllRestaurants()   //Bütün restorantları silen endpoint
    {

        return null;
    }


    @RequestMapping(value = "/voteRestaurant", method = RequestMethod.POST)
    public ResponseEntity<String> voteRest(@RequestBody Review review) {







        restaurantService.voteRestaurant(review);
              return new ResponseEntity<String>("Eklendi", HttpStatus.OK);



    }

    @RequestMapping(value = "/lisallrestaurants", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<Restaurant>> listAllRestaurants() {
        try {


            return new ResponseEntity<ArrayList<Restaurant>>(restaurantService.findAllRestaurant(), HttpStatus.OK);


        } catch (Exception e) {

            return new ResponseEntity<ArrayList<Restaurant>> (HttpStatus.NOT_FOUND);


        }
    }
}



