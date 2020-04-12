package com.spring.controller;

import com.spring.model.Restaurant;
import com.spring.model.Review;
import com.spring.service.RestaurantService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurant")
public class RestaurantRestController {

    @Setter
    @Autowired
    private RestaurantService restaurantService;


    //add new context
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public ResponseEntity<Void> createRestaurant(@RequestBody Restaurant restaurant) {
        if (restaurantService.isRestaurantExist(restaurant)) {
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        } else {
            restaurantService.Create(restaurant);
            return new ResponseEntity<Void>(HttpStatus.CREATED);
        }
    }

    // deleteAllRestaurants
    @RequestMapping(value = "/deleteAllRestaurants", method = RequestMethod.GET)
    public ResponseEntity<Void> deleteAllRestaurants()   //Bütün restorantları silen endpoint
    {

        return null;
    }


    @RequestMapping(value = "/voteRestaurant", method = RequestMethod.POST)
    public ResponseEntity<String> voteRest(@RequestBody Review review) {
        try {
            if (!restaurantService.isVoteExist(review.getUser().getUserID(),review.getRestaurant().getRestaurantID())) {
                restaurantService.voteRestaurant(review);
                return new ResponseEntity<String>("Eklendi", HttpStatus.OK);

            } else return new ResponseEntity<String>("Vote already exist", HttpStatus.CONFLICT);

        }
        catch (Exception e) {

            return new ResponseEntity<String>("Something went wrong.", HttpStatus.NOT_MODIFIED);
        }
    }
}



