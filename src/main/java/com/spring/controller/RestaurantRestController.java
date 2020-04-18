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

    @RequestMapping(value = "/findRestaurantbyName/page={page}", method = RequestMethod.GET)
    public ResponseEntity<List<Object>> findRestaurantbyName(@PathVariable("page") int page,@RequestBody String name)
    {
        try {
            return new ResponseEntity<List<Object>>(restaurantService.findByName(name,page), HttpStatus.OK); //
        } catch (Exception e) {

            return new ResponseEntity<List<Object>>(HttpStatus.NOT_MODIFIED);
        }
    }
    @RequestMapping(value = "/findRestaurantbyID", method = RequestMethod.GET)
    public ResponseEntity<Object> findRestaurantbyID(@RequestBody long id){
        try {
            return new ResponseEntity<Object>(restaurantService.findById(id), HttpStatus.OK); //
        } catch (Exception e) {

            return new ResponseEntity<Object>(HttpStatus.NOT_MODIFIED);
        }
    }
    @RequestMapping(value = "/findRestaurantbyCity/page={page}", method = RequestMethod.GET)
    public ResponseEntity<List<Object>> findRestaurantbyCity(@PathVariable("page") int page,@RequestBody String city){
        try {
            return new ResponseEntity<List<Object>>(restaurantService.findByCity(city,page), HttpStatus.OK); //
        } catch (Exception e) {
            return new ResponseEntity<List<Object>>(HttpStatus.NOT_MODIFIED);
        }
    }
    @RequestMapping(value = "/findRestaurantbyLocality/page={page}", method = RequestMethod.GET)
    public ResponseEntity<List<Object>> findRestaurantbyLocality(@PathVariable("page") int page, @RequestBody String locality){
        try {
            return new ResponseEntity<List<Object>>(restaurantService.findByLocality(locality,page), HttpStatus.OK); //
        } catch (Exception e) {
            return new ResponseEntity<List<Object>>(HttpStatus.NOT_MODIFIED);
        }
    }

    @RequestMapping(value = "/allRestaurants/page={page}", method = RequestMethod.GET)
    public ResponseEntity<List<Object>> listAllRestaurants(@PathVariable("page") int page){
        try {
            return new ResponseEntity<List<Object>>(restaurantService.findAllRestaurant(page), HttpStatus.OK); //
        } catch (Exception e) {
            return new ResponseEntity<List<Object>>(HttpStatus.NOT_MODIFIED);
        }
    }
    @RequestMapping(value = "/votedRestaurantList/page={page}", method = RequestMethod.GET)
    public ResponseEntity<List<Object>> votedRestaurants(@PathVariable("page")int page,@RequestBody long id){
        try {
            return new ResponseEntity<List<Object>>(restaurantService.votedRestaurantList(id,page), HttpStatus.OK); //
        } catch (Exception e) {

            return new ResponseEntity<List<Object>>(HttpStatus.NOT_MODIFIED);
        }
    }
    @RequestMapping(value = "/detailRestaurant", method = RequestMethod.GET)
    public ResponseEntity<Object> detailRestaurant(@RequestBody long id){
        try {
            return new ResponseEntity<Object>(restaurantService.detailRestaurant(id), HttpStatus.OK); //
        } catch (Exception e) {

            return new ResponseEntity<Object>(HttpStatus.NOT_MODIFIED);
        }
    }
    @RequestMapping(value = "/detailVote", method = RequestMethod.GET)
    public ResponseEntity<Object> detailVote(@RequestBody long id){
        try {
            return new ResponseEntity<Object>(restaurantService.detailVote(id), HttpStatus.OK); //
        } catch (Exception e) {

            return new ResponseEntity<Object>(HttpStatus.NOT_MODIFIED);
        }
    }
    @RequestMapping(value = "/deletebyId", method = RequestMethod.GET)
    public ResponseEntity<Void> delete(@RequestParam("id") long id) {

        restaurantService.Delete(id);
        return new ResponseEntity<Void>(HttpStatus.OK);

    }

    @RequestMapping(value = "/updateVote", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody Review review) {
        restaurantService.updateVote(review);
        return ResponseEntity.ok().body("Vote has been updated successfully.");
    }

}



