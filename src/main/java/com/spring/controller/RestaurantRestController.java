package com.spring.controller;

import com.spring.model.Restaurant;
import com.spring.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/restaurant")
public class RestaurantRestController {

    @Autowired
    private RestaurantService restaurantService;

    public void setRestaurantService(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

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
     //list Restaurants
//    @RequestMapping(value = "/restaurant", method = RequestMethod.GET)
//    public String listRestaurants(Model model) {
//        model.addAttribute("restaurant", new Restaurant());
//        model.addAttribute("listRestaurants", this.restaurantService.listRestaurants());
//        return "restaurant";
//
//    }
//    @RequestMapping(value = "/restaurant/edit/{id}", method = RequestMethod.POST)
//    public ModelAndView edditingRestaurant(@ModelAttribute Restaurant restaurant, @PathVariable Integer id) {
//
//        ModelAndView modelAndView = new ModelAndView("home");
//
//        restaurantService.updateRestaurant(restaurant);
//
//        String message = "Restaurant was successfully edited.";
//        modelAndView.addObject("message", message);
//
//        return modelAndView;
//    }
}