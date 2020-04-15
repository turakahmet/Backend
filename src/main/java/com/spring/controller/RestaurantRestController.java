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

        try {
        
        if (!restaurantService.isVoteExist(review.getUser().getUserID(), review.getRestaurant().getRestaurantID())) {
            restaurantService.voteRestaurant(review);
            return new ResponseEntity<String>("Inserted", HttpStatus.OK);
        } else
            return new ResponseEntity<String>("Vote already exist", HttpStatus.CONFLICT);
    }
    catch(Exception e){
        return new ResponseEntity<String>("Something went wrong.", HttpStatus.NOT_MODIFIED);

    }
        


    }

    @RequestMapping(value = "/findRestaurantbyName", method = RequestMethod.GET)
    public ResponseEntity<List<Object>> findRestaurantbyName(@RequestBody String name)
    {
        try {
            return new ResponseEntity<List<Object>>(restaurantService.findByName(name), HttpStatus.OK); //
        } catch (Exception e) {

            return new ResponseEntity<List<Object>>(HttpStatus.NOT_MODIFIED);
        }
    }
    @RequestMapping(value = "/findRestaurantbyID", method = RequestMethod.GET)
    public ResponseEntity<List<Object>> findRestaurantbyID(@RequestBody long id){
        try {
            return new ResponseEntity<List<Object>>(restaurantService.findById(id), HttpStatus.OK); //
        } catch (Exception e) {

            return new ResponseEntity<List<Object>>(HttpStatus.NOT_MODIFIED);
        }
    }
    @RequestMapping(value = "/findRestaurantbyCity", method = RequestMethod.GET)
    public ResponseEntity<List<Object>> findRestaurantbyCity(@RequestBody String city){
        try {
            return new ResponseEntity<List<Object>>(restaurantService.findByCity(city), HttpStatus.OK); //
        } catch (Exception e) {
            return new ResponseEntity<List<Object>>(HttpStatus.NOT_MODIFIED);
        }
    }
    @RequestMapping(value = "/findRestaurantbyLocality", method = RequestMethod.GET)
    public ResponseEntity<List<Object>> findRestaurantbyLocality(@RequestBody String locality){
        try {
            return new ResponseEntity<List<Object>>(restaurantService.findByLocality(locality), HttpStatus.OK); //
        } catch (Exception e) {
            return new ResponseEntity<List<Object>>(HttpStatus.NOT_MODIFIED);
        }
    }

    @RequestMapping(value = "/allRestaurants", method = RequestMethod.GET)
    public ResponseEntity<List<Object>> listAllRestaurants(){
        try {
            return new ResponseEntity<List<Object>>(restaurantService.findAllRestaurant(), HttpStatus.OK); //
        } catch (Exception e) {
            return new ResponseEntity<List<Object>>(HttpStatus.NOT_MODIFIED);
        }
    }
    @RequestMapping(value = "/votedRestaurantList", method = RequestMethod.GET)
    public ResponseEntity<List<Object>> votedRestaurants(@RequestBody long id){
        try {
            return new ResponseEntity<List<Object>>(restaurantService.votedRestaurantList(id), HttpStatus.OK); //
        } catch (Exception e) {

            return new ResponseEntity<List<Object>>(HttpStatus.NOT_MODIFIED);
        }
    }
    @RequestMapping(value = "/detailRestaurant", method = RequestMethod.GET)
    public ResponseEntity<Restaurant> detailRestaurant(@RequestBody long id){
        try {
            return new ResponseEntity<Restaurant>(restaurantService.detailRestaurant(id), HttpStatus.OK); //
        } catch (Exception e) {

            return new ResponseEntity<Restaurant>(HttpStatus.NOT_MODIFIED);
        }
    }

}



