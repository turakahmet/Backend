package com.spring.controller;

import com.spring.model.*;
import com.spring.requestenum.RequestDescriptions;
import com.spring.service.LogService;
import com.spring.service.RestaurantService;
import com.spring.service.UserService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import com.spring.token.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/restaurant")
public class RestaurantRestController {


    @Setter
    @Autowired
    private LogService logService;

    @Setter
    @Autowired
    private RestaurantService restaurantService;

    @Setter
    @Autowired
    private UserService userService;

    @Autowired
    Validation validation;

    //add new context
    @RequestMapping(value = "/new", method = RequestMethod.POST)
    public ResponseEntity<Void> createRestaurant(@RequestBody Restaurant restaurant) {

        restaurantService.Create(restaurant);
        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @RequestMapping(value = "/deletebyId", method = RequestMethod.GET)
    public ResponseEntity<Void> delete(@RequestParam("id") long id, @RequestParam("uniqueid") String uniqueId) {

        if (userService.isAdminId(uniqueId)) {
            restaurantService.Delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } else
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);


    }


    @RequestMapping(value = "/voteRestaurant", method = RequestMethod.POST)
    public ResponseEntity<String> voteRestaurant(@RequestBody Review review) {
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
    public ResponseEntity<List<Object>> findRestaurantbyCity(@RequestParam("city") String city, @RequestParam("category") String category, @RequestParam("page") int page) {
        try {
            return new ResponseEntity<>(restaurantService.findByCity(city, category, page), HttpStatus.OK); //
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
    public ResponseEntity<List<Object>> listAllRestaurants(@RequestParam("page") int page, @RequestParam("enlem") double enlem, @RequestParam("boylam") double boylam) {
        try {
            return new ResponseEntity<>(restaurantService.findAllRestaurant(page, enlem, boylam), HttpStatus.OK); //
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    @RequestMapping(value = "/getRandomPlaces", method = RequestMethod.GET)
    public ResponseEntity<List<Object>> getRandomPlaces(@RequestParam("page") int page) {
        try {
            return new ResponseEntity<>(restaurantService.getRandomPlaces(page), HttpStatus.OK); //
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

    @RequestMapping(value = "/updateVote", method = RequestMethod.POST)
    public ResponseEntity<String> update(@RequestBody Review review) {


        logService.savelog(new com.spring.model.Log(RequestDescriptions.UPDATEVOTE.getText(), getUserIP()));

        if (review != null) {
            restaurantService.updateVote(review);
            restaurantService.updateRestaurantReview(review.getRestaurant().getRestaurantID());
            return new ResponseEntity<>(HttpStatus.OK);
        } else {

            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }


    }

    //get userID and restaurantID service
    @RequestMapping(value = "/infoAdmin", method = RequestMethod.GET)
    public ResponseEntity<?> getInfo() {
        try {

//            logService.savelog(new Log(RequestDescriptions.INFOADMIN.getText(), getUserIP()));
            System.out.println("IP:" + getUserIP());

            return new ResponseEntity<ArrayList>(restaurantService.getInfo(), HttpStatus.OK); //
        } catch (Exception e) {
            System.out.println("Exception:" + e.getMessage());
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.NOT_MODIFIED);
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
            logService.savelog(new Log(RequestDescriptions.GETRECORD.getText(), getUserIP()));

            restaurantService.createRecord(userRecords);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        }
    }

    @RequestMapping(value = "/allbycategory", method = RequestMethod.GET)
    public ResponseEntity<List<Object>> listAllbyCategory(@RequestParam("category") String category, @RequestParam("page") int page,
                                                          @RequestParam("enlem") double enlem, @RequestParam("boylam") double boylam) {
        try {
            return new ResponseEntity<>(restaurantService.findAllbyCategory(category, page, enlem, boylam), HttpStatus.OK); //
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
    public ResponseEntity<Void> deleteRecord(@RequestParam("recordId") long recordId, @RequestParam("uniqueid") String uniqueId) {


        if (userService.isAdminId(uniqueId)) {
            try {
                restaurantService.deleteRecordId(recordId);
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

            }
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        }

    }

    @RequestMapping(value = "/saveRecord", method = RequestMethod.POST)
    public ResponseEntity<Void> saveRecord(@RequestBody Restaurant restaurant, @RequestParam("uniqueid") String uniqueId, @RequestParam("ResID") long ResID, @RequestParam("UserID") long UserID) {
        try {
            logService.savelog(new Log(RequestDescriptions.SAVERECORD.getText(), getUserIP()));
            if (userService.isAdminId(uniqueId)) {
                restaurantService.saveRecord(restaurant, ResID, UserID);
                return new ResponseEntity<>(HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
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
    public ResponseEntity<List<Object>> findbyNameTown(@RequestParam("name") String name, @RequestParam("town") String townName, @RequestParam("city") String cityName, @RequestParam("page") int page) {
        try {
            return new ResponseEntity<>(restaurantService.findAllSourceRestaurant(name, townName, cityName, page), HttpStatus.OK); //
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
            if (restaurantService.adminCheck(adminTK)) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);

        }
    }

    @RequestMapping(value = "/deletevote", method = RequestMethod.POST)
    public ResponseEntity<String> deletevote(@RequestBody Review review, @RequestParam("email") String email, @RequestParam("password") String password) {

        if (userService.getusertype(email).equals("google") && validation.isValidateGoogleAction(review, email, password)) {
            restaurantService.deleteVote(review);
            restaurantService.updateRestaurantReview(review.getRestaurant().getRestaurantID());
            return ResponseEntity.ok().body("Vote has been deleted successfully.");
        } else {
            try {

                if (validation.isValidateAction(review, email, password)) {

                    restaurantService.deleteVote(review);
                    restaurantService.updateRestaurantReview(review.getRestaurant().getRestaurantID());
                    return ResponseEntity.ok().body("Vote has been deleted successfully.");
                } else
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);


            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

            }
        }


    }

    //filter services
    @RequestMapping(value = "/filter", method = RequestMethod.POST)
    public ResponseEntity<List<Object>> filter(@RequestBody Filter filter) {
        try {

            return new ResponseEntity<>(restaurantService.filter(filter), HttpStatus.OK); //
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    //en yakin service
    @RequestMapping(value = "/getYakinRestoran", method = RequestMethod.GET)
    public ResponseEntity<List> getYakinRestoran(@RequestParam("enlem") Double enlem, @RequestParam("boylam") Double boylam) {
        List<enyakinRestoran> enyakinRestoranList = new ArrayList<>();
        List<Object> restorant = restaurantService.getEnYakin(enlem, boylam);
        for (Object o : restorant) {
            Object[] a = (Object[]) o;
            enyakinRestoran yakin = new enyakinRestoran();
            yakin.setIsim((String) a[0]);
            yakin.setEnlem((String) a[1]);
            yakin.setBoylam((String) a[2]);
            yakin.setUzaklik(String.valueOf(a[3]));
            enyakinRestoranList.add(yakin);
        }
        try {
            return new ResponseEntity<>(enyakinRestoranList, HttpStatus.OK); //
        } catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }

    }


    public String getUserIP() {
        ServletRequestAttributes attr = (ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        return request.getRemoteAddr();
    }

    //cihaz token alma ilk girişte mesaj basmak için
    @RequestMapping(value = "/sendDeviceToken", method = RequestMethod.POST)
    public ResponseEntity<Boolean> sendDeviceToken(@RequestParam("token") String token) {
        try {
            return new ResponseEntity<Boolean>(restaurantService.sendDeviceToken(token), HttpStatus.OK); //
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    //cihaza event bidaha gösterme ekranı basmak için
    @RequestMapping(value = "/sendNewEvent", method = RequestMethod.POST)
    public ResponseEntity<Boolean> sendNewEvent(@RequestParam("token") String token) {
        try {
            return new ResponseEntity<Boolean>(restaurantService.sendNewEvent(token), HttpStatus.OK); //
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    @RequestMapping(value = "/getDeviceEventStatus", method = RequestMethod.GET)
    public ResponseEntity<Boolean> getDeviceEventStatus(@RequestParam("token") String token) {
        try {
            return new ResponseEntity<Boolean>(restaurantService.getDeviceEventStatus(token), HttpStatus.OK); //
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }
    //user ekranı total score,fav,place sayısı

    @RequestMapping(value = "/getUserSummary", method = RequestMethod.GET)
    public ResponseEntity<List<Long>> getDeviceEventStatus(@RequestParam("userID") long UserID) {
        try {
            return new ResponseEntity<>(restaurantService.getUserSummary(UserID), HttpStatus.OK); //
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    //setFavorite place
    @RequestMapping(value = "/setFavoriteRes", method = RequestMethod.POST)
    public ResponseEntity<Boolean> setFavoriteRes(@RequestBody FavoritePlace favoritePlace) {
        try {
            if (userService.setFavoriteRes(favoritePlace))
                return new ResponseEntity<>(true, HttpStatus.OK);
            else
                return new ResponseEntity<>(false, HttpStatus.NOT_MODIFIED);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.CONFLICT);
        }
    }

    //removeFavorite place
    @RequestMapping(value = "/removeFavorite", method = RequestMethod.POST)
    public ResponseEntity<Boolean> removeFavorite(@RequestBody FavoritePlace favoritePlace) {
        try {
            if (userService.removeFavorite(favoritePlace))
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    //deleteLog add
    @RequestMapping(value = "/deleteLog", method = RequestMethod.POST)
    public ResponseEntity<Void> deleteLog(@RequestBody DeletePlaceLog deletePlaceLog, @RequestParam("uniqueID") String uniqueID) {
        try {
            if (userService.isAdminId(uniqueID)) {
                if (restaurantService.deleteLog(deletePlaceLog))
                    return new ResponseEntity<>(HttpStatus.OK);
                else
                    return new ResponseEntity<>(HttpStatus.CONFLICT);
            } else {
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    //deleteLog add
    @RequestMapping(value = "/sharePlace", method = RequestMethod.GET)
    public ResponseEntity<List<Object>> sharePlace(@RequestParam("resID") long resID) {
        try {
            return new ResponseEntity<>(restaurantService.sharePlace(resID), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    //delete place
    @RequestMapping(value = "/deleteMyPlace", method = RequestMethod.GET)
    public ResponseEntity<Boolean> deleteMyPlace(@RequestParam("resID") long resID,@RequestParam("userID") long userID) {
        try {
            if (userService.deleteMyPlace(resID,userID))
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    //invalid place
    @RequestMapping(value = "/invalidPlace", method = RequestMethod.GET)
    public ResponseEntity<Boolean> invalidPlace(@RequestParam("resID") long resID,@RequestParam("userID") long userID,@RequestParam("timingValue") String timingValue) {
        try {
            if (userService.invalidPlace(resID,userID,timingValue)) //userplace db de placestatus 1 ise invalidplace dir. 0 ise aktif
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }
    @RequestMapping(value = "/setUserAchievement", method = RequestMethod.GET)
    public ResponseEntity<Void> setUserAchievement(@RequestParam("userID") long userID) {
        try {
            restaurantService.setUserAchievement(userID);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }
}