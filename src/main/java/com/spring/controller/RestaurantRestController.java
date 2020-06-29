package com.spring.controller;

import com.spring.model.*;
import com.spring.service.RestaurantService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.HttpURLConnection;
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
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @RequestMapping(value = "/deletebyId", method = RequestMethod.GET)
    public ResponseEntity<Void> delete(@RequestParam("id") long id) {

        restaurantService.Delete(id);
        return new ResponseEntity<>(HttpStatus.OK);

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
                restaurantService.updateRestaurantReview(review.getRestaurant().getRestaurantID());
                return new ResponseEntity<>("Inserted", HttpStatus.OK);
            } else
                return new ResponseEntity<>("Vote already exist", HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("Something went wrong.", HttpStatus.NOT_MODIFIED);
        }

    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Object>> findRestaurantbyName(@RequestParam("name") String name, @RequestParam("page") int page) {
        try {
            return new ResponseEntity<>(restaurantService.findByName(name, page), HttpStatus.OK); //
        } catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    @RequestMapping(value = "/findRestaurantbyID", method = RequestMethod.GET)
    public ResponseEntity<Object> findRestaurantbyID(@RequestParam("id") long id) {
        try {
            return new ResponseEntity<>(restaurantService.findById(id), HttpStatus.OK); //
        } catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    @RequestMapping(value = "/findRestaurantbyCity", method = RequestMethod.GET)
    public ResponseEntity<List<Object>> findRestaurantbyCity(@RequestParam("city") String city, @RequestParam("page") int page) {
        try {
            return new ResponseEntity<>(restaurantService.findByCity(city, page), HttpStatus.OK); //
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    @RequestMapping(value = "/findRestaurantbyTown", method = RequestMethod.GET)
    public ResponseEntity<List<Object>> findRestaurantbyLocality(@RequestParam("town") String town, @RequestParam("page") int page) {
        try {
            return new ResponseEntity<>(restaurantService.findByTown(town, page), HttpStatus.OK); //
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    @RequestMapping(value = "/allRestaurants", method = RequestMethod.GET)
    public ResponseEntity<List<Object>> listAllRestaurants(@RequestParam("page") int page) {
        try {
            return new ResponseEntity<>(restaurantService.findAllRestaurant(page), HttpStatus.OK); //
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    @RequestMapping(value = "/allRestaurantsAdmin", method = RequestMethod.GET)
    public ResponseEntity<List<Object>> listAllRestaurantsAdmin(@RequestParam("page") int page) {
        try {
            return new ResponseEntity<>(restaurantService.findAllRestaurantAdmin(page), HttpStatus.OK); //
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    @RequestMapping(value = "/votedRestaurants", method = RequestMethod.GET)
    public ResponseEntity<List<Object>> votedRestaurants(@RequestParam("user") long id, @RequestParam("page") int page) {
        try {
            return new ResponseEntity<>(restaurantService.votedRestaurantList(id, page), HttpStatus.OK); //
        } catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    @RequestMapping(value = "/detailRestaurant", method = RequestMethod.GET)
    public ResponseEntity<Object> detailRestaurant(@RequestParam("restaurant") long id) {
        try {
            return new ResponseEntity<>(restaurantService.detailRestaurant(id), HttpStatus.OK); //
        } catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    @RequestMapping(value = "/detailVote", method = RequestMethod.GET)
    public ResponseEntity<Object> detailVote(@RequestParam("vote") long id) {
        try {
            return new ResponseEntity<>(restaurantService.detailVote(id), HttpStatus.OK); //
        } catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    @RequestMapping(value = "/updateVote", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@RequestBody Review review) {
        try {
            restaurantService.updateVote(review);
            restaurantService.updateRestaurantReview(review.getRestaurant().getRestaurantID());
            return ResponseEntity.ok().body("Vote has been updated successfully.");
        } catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }

    }

    //get userID and restaurantID service
    @RequestMapping(value = "/infoAdmin", method = RequestMethod.GET)
    public ResponseEntity<ArrayList> getInfo() {
        try {
            return new ResponseEntity<ArrayList>(restaurantService.getInfo(), HttpStatus.OK); //
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }

    }

    @RequestMapping(value = "/getcategorized", method = RequestMethod.GET)
    public ResponseEntity<List<Object>> topRated(@RequestParam("page") int page, @RequestParam("type") String type) {

        try {
            return new ResponseEntity<>(restaurantService.getTopRated(page, type), HttpStatus.OK); //
        } catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }

    }

    @RequestMapping(value = "/getRecord", method = RequestMethod.POST)
    public ResponseEntity<Void> addRecord(@RequestBody UserRecords userRecords) {
        try {
            restaurantService.createRecord(userRecords);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        }
    }

    @RequestMapping(value = "/allbycategory", method = RequestMethod.GET)
    public ResponseEntity<List<Object>> listAllbyCategory(@RequestParam("category") String category, @RequestParam("page") int page) {
        try {
            return new ResponseEntity<>(restaurantService.findAllbyCategory(category, page), HttpStatus.OK); //
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    @RequestMapping(value = "/city", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<City>> getCities() {
        try {
            return new ResponseEntity<ArrayList<City>>(restaurantService.getCity(), HttpStatus.OK); //
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    @RequestMapping(value = "/town", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<Town>> getTowns(@RequestParam("cityName") String cityName) {
        try {
            return new ResponseEntity<ArrayList<Town>>(restaurantService.getTown(cityName), HttpStatus.OK); //
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    @RequestMapping(value = "/deleteRecord", method = RequestMethod.POST)
    public ResponseEntity<Void> deleteRecord(@RequestParam("recordId") long recordId) {
        try {
            restaurantService.deleteRecordId(recordId);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        }
    }

    @RequestMapping(value = "/saveRecord", method = RequestMethod.POST)
    public ResponseEntity<Void> saveRecord(@RequestBody Restaurant restaurant) {
        try {
            restaurantService.saveRecord(restaurant);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        }
    }

    ////hızlı puanlama servisleri
    //get
    @RequestMapping(value = "/fastPoint", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<Object>> fastPoint(@RequestParam("ResID") long ResID, @RequestParam("point") double point) {
        try {
            return new ResponseEntity<ArrayList<Object>>(restaurantService.fastPoint(ResID, point), HttpStatus.OK); //
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        }
    }

    //post
    @RequestMapping(value = "/fastPointSend", method = RequestMethod.POST)
    public ResponseEntity<Void> fastPointSend(@RequestParam("ResID") long ResID, @RequestParam("UserID") long UserID, @RequestParam("point") double point) {
        try {
            if (!restaurantService.isVoteExist(UserID, ResID)) {
                restaurantService.fastPointSend(ResID, UserID, point);
                restaurantService.updateRestaurantReview(ResID);
                return new ResponseEntity<>(HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        }
    }

    ////
    //search controller
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ResponseEntity<List<Object>> findbyNameTown(@RequestParam("name") String name, @RequestParam("town") String townName, @RequestParam("page") int page) {
        try {
            return new ResponseEntity<>(restaurantService.findAllSourceRestaurant(name, townName, page), HttpStatus.OK); //
        } catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    //compare results
    @RequestMapping(value = "/compare", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<String>> Compare(@RequestParam("oldValueHygiene") double oldValueHygiene, @RequestParam("oldValueCleaning") double oldValueCleaning, @RequestParam("ResID") long ResID) {
        try {
            return new ResponseEntity<ArrayList<String>>(restaurantService.compareResults(oldValueHygiene, oldValueCleaning, ResID), HttpStatus.OK); //
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    @RequestMapping(value = "/arrowPointSend", method = RequestMethod.POST)
    public ResponseEntity<Void> arrowPointSend(@RequestParam("ResID") long ResID, @RequestParam("CleaningArrow") int CleaningArrow, @RequestParam("HygieneArrow") int HygieneArrow) {
        try {
            restaurantService.arrowPointSend(ResID, CleaningArrow, HygieneArrow);
            return new ResponseEntity<>(HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        }
    }

    //report post service
    @RequestMapping(value = "/reportSend", method = RequestMethod.POST)
    public ResponseEntity<Void> reportSend(@RequestParam("ResID") long ResID, @RequestParam("UserID") long UserID, @RequestParam("reportID") int reportID) {
        try {
            if (!restaurantService.isReportExist(ResID, UserID)) {
                restaurantService.reportSend(ResID, UserID, reportID);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        }
    }

    // admin check service
    @RequestMapping(value = "/adminCheck", method = RequestMethod.POST)
    public ResponseEntity<Void> adminCheck(@RequestBody AdminTK adminTK) {
        try {
            if(restaurantService.adminCheck(adminTK)) {
                return new ResponseEntity<>(HttpStatus.OK);
            }else{
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);

        }
    }
}



