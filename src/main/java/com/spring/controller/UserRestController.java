package com.spring.controller;

import com.spring.dao.UserDAO;
import com.spring.model.*;
import com.spring.requestenum.RequestDescriptions;
import com.spring.service.LogService;
import com.spring.token.*;
import com.spring.feedbacks.Error;
import com.spring.service.MailService;
import com.spring.service.UserService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/user")
public class UserRestController {

    @Setter
    @Autowired
    UserService userService;

    @Autowired
    UserDAO userDAO;

    @Autowired
    LogService logService;

    @Autowired
    Validation validation;

    @Autowired
    Error error;


    @Autowired
    MailService mailService;


    @RequestMapping(value = "/insertUser", method = RequestMethod.POST)
    public ResponseEntity<Void> insertUser(@RequestBody AppUser user) {
        logService.savelog(new com.spring.model.Log(RequestDescriptions.NEWACCOUNT.getText(), getUserIP()));
        try {
            if (userService.isUserExist(user.getUserEmail())) {
                if (user.getUserType().equals("standard")) {
                    user.setStatus("passive");
                    user.setCode(mailService.sendMail(user.getUserEmail(), user.getUserPassword()));
                } else {
                    user.setStatus("active");
                }
                userService.insertUser(user);
                return new ResponseEntity<>(HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }

    }


    @RequestMapping(value = "/listallusers", method = RequestMethod.POST)  //Bunu Kaldır
    public ResponseEntity<List<Object>> listAllUsers(@RequestBody AdminTK adminTK)   //Kullanıcı ekleyen endpoint
    {
        try {
            if (userService.isAdmin(adminTK))
                return new ResponseEntity<List<Object>>(userService.listAllUsers(), HttpStatus.OK); //

            else if (!userService.isAdmin(adminTK))
                return new ResponseEntity<List<Object>>(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS); //

            else {
                return new ResponseEntity<List<Object>>(HttpStatus.SERVICE_UNAVAILABLE); //

            }

        } catch (Exception e) {

            return new ResponseEntity<List<Object>>(HttpStatus.NOT_MODIFIED);
        }

    }

//    @RequestMapping(value = "/updateuser", method = RequestMethod.POST)
//    public ResponseEntity<AppUser> updateUser(@RequestBody AppUser user)   //Kullanıcı güncelleyen endpoint
//
//    {       //kullanıcıyı update ederken komple kullanıcı classını karşılayan bir json gönderin
//        //Ancak idsivtde olan bir id olmalı
//        try {
//            return new ResponseEntity<AppUser>(userService.updateUser(user), HttpStatus.OK); //
//
//        } catch (Exception e) {
//            return new ResponseEntity<AppUser>(userService.updateUser(user), HttpStatus.NOT_MODIFIED); //
//
//        }
//
//
//    }

    @RequestMapping(value = "/checkstandard", method = RequestMethod.GET)
    public ResponseEntity<?> checkStandard(@RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("userType") String userType)   //Kullanıcı güncelleyen endpoint

    {
        logService.savelog(new com.spring.model.Log(RequestDescriptions.STANDARDLOGIN.getText(), getUserIP()));

        try {
            if (!userService.isUserExist(email) || !userType.equals("standard")) {
                if (userService.checkStandardCredentials(email, password)) {
                    if (userService.isUserActive(email)) {
                        return new ResponseEntity<CustomUser>(userService.findUserByEmail(email), HttpStatus.OK);
                    } else {
                        userService.sendmail(email);
                        return new ResponseEntity<CustomUser>(userService.findUserByEmail(email), HttpStatus.NOT_MODIFIED);
                    }

                } else {
                    error.setFeedback("fail login, wrong id or pw");
                    return new ResponseEntity<Error>(error, HttpStatus.UNAUTHORIZED);
                }
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            error.setFeedback("Something went wrong");
            return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
        }
    }


//    @RequestMapping(value = "/checkgoogle", method = RequestMethod.POST)
//    public ResponseEntity<?> checkGoogle(@RequestBody AppUser user)   //Kullanıcı güncelleyen endpoint
//
//    {
//        logService.savelog(new com.spring.model.Log(RequestDescriptions.GOOGLELOGIN.getText(), getUserIP()));
//
//        user.setUserPassword("YNGUP_default_");
//        if (!userService.isUserExist(user.getUserEmail())) {
//
//            return new ResponseEntity<AppUser>(userService.insertUser(user), HttpStatus.OK);
//        } else {
//            if (userService.getusertype(user.getUserEmail()).equals("google") || userService.getusertype(user.getUserEmail()).equals("facebook")) {
//                System.out.println("USERTYPE GOOGLE VEYA FACEBOOKTOR.");
//                return new ResponseEntity<CustomUser>(userService.findUserByEmail(user.getUserEmail()), HttpStatus.OK);
//            } else {
//
//                return new ResponseEntity<String>(HttpStatus.CONFLICT);
//            }
//        }
//
//
//    }

    @RequestMapping(value = "/checkusercode", method = RequestMethod.GET)
    public ResponseEntity<?> checkGoogle(@RequestParam("email") String email, @RequestParam("code") long code, @RequestParam("password") String password)   //Kullanıcı güncelleyen endpoint
    {
        logService.savelog(new com.spring.model.Log(RequestDescriptions.CHECKCODE.getText(), getUserIP()));
        try {
            if (userService.checkUserCode(email, code)) {
                return new ResponseEntity<>("Hesabınız Başarıyla Aktifleştirildi\nYour Account Has Been Successfully Activated", HttpStatus.OK);
            } else {
                error.setCode(204);
                error.setFeedback("Girmiş olduğunuz kod geçerli değil.\nThe code you entered is not valid.");
                return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @RequestMapping(value = "/getuserid", method = RequestMethod.GET)
    public ResponseEntity<?> checkGoogle(@RequestParam("email") String email, @RequestParam("googleid") String googleId)   //Kullanıcı güncelleyen endpoint

    {
        logService.savelog(new com.spring.model.Log(RequestDescriptions.GETUSERID.getText(), getUserIP()));

        if (userService.getusertype(email).equals("google") && validation.isValidateGoogle(email, googleId))
            return new ResponseEntity<CustomUser>(userService.findUserByEmail(email), HttpStatus.OK);
        else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        }


    }

    @RequestMapping(value = "/listreviews", method = RequestMethod.POST)
    public ResponseEntity<List<Review>> listreviews(@RequestBody AdminTK adminTK, @RequestParam("email") String email) {
        try {

            logService.savelog(new com.spring.model.Log(RequestDescriptions.LISTREVIEWS.getText(), getUserIP()));


            if (userService.isAdmin(adminTK))
                return new ResponseEntity<List<Review>>(userService.getReview(email), HttpStatus.OK); //

            else if (!userService.isAdmin(adminTK))
                return new ResponseEntity<List<Review>>(userService.getReview(email), HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
            else {
                return new ResponseEntity<List<Review>>(HttpStatus.SERVICE_UNAVAILABLE);
            }

        } catch (Exception e) {

            System.out.print(e.getMessage());

            return new ResponseEntity<List<Review>>(HttpStatus.NOT_MODIFIED);
        }

    }

    @RequestMapping(value = "/getuserreviews", method = RequestMethod.GET)
    public ResponseEntity<List<Object>> getuserreviews(@RequestParam("email") String email, @RequestParam("password") String password)   //Kullanıcı ekleyen endpoint
    {

        logService.savelog(new com.spring.model.Log(RequestDescriptions.GETUSERREVIEWS.getText(), getUserIP()));

        if (userService.getusertype(email).equals("google") && validation.isValidateGoogle(email, password)) {
            List<Object> reviewList = userDAO.getuserreviews(email);
            return new ResponseEntity<List<Object>>(reviewList, HttpStatus.OK); //
        } else {
            try {
                Token myToken = new Token(email, password, "");
                if (validation.isvalidate(myToken)) {

                    List<Object> reviewList = userDAO.getuserreviews(email);
                    return new ResponseEntity<List<Object>>(reviewList, HttpStatus.OK); //


                } else
                    return new ResponseEntity<List<Object>>(HttpStatus.UNAUTHORIZED); //


            } catch (Exception e) {

                System.out.print(e.getMessage());

                return new ResponseEntity<List<Object>>(HttpStatus.NOT_MODIFIED);
            }
        }


    }

    @RequestMapping(value = "/getreviewcount", method = RequestMethod.GET)
    public ResponseEntity<Long> getreviewcount(@RequestParam("email") String email, @RequestParam("password") String password) {
        if (validation.isValidateGoogle(email, password)) {
            return new ResponseEntity<Long>(userDAO.getreviewcount(email, password), HttpStatus.OK); //

        } else {
            try {
                Token myToken = new Token(email, password, "all");
                if (validation.isvalidate(myToken)) {
                    return new ResponseEntity<Long>(userDAO.getreviewcount(email, password), HttpStatus.OK); //
                } else
                    return new ResponseEntity<Long>(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS); //


            } catch (Exception e) {

                System.out.print(e.getMessage());

                return new ResponseEntity<Long>(HttpStatus.NOT_MODIFIED);
            }
        }


    }


    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    public ResponseEntity<Boolean> changePassword(@RequestParam("email") String email, @RequestParam("password") String password,
                                                  @RequestParam("newPassword") String newPassword) {
        logService.savelog(new com.spring.model.Log(RequestDescriptions.CHANGEPASSWORD.getText(), getUserIP()));
        try {
            if (userService.changePassword(email, password, newPassword))
                return new ResponseEntity<>(true, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/supportMessage", method = RequestMethod.POST)
    public ResponseEntity<Void> supportMessage(@RequestParam("email") String email, @RequestParam("body") String body) {
        logService.savelog(new com.spring.model.Log(RequestDescriptions.CHANGEPASSWORD.getText(), getUserIP()));
        try {
            if (userService.supportMessage(email, body))
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/getcategoryinfo", method = RequestMethod.GET)
    public ResponseEntity<Object> getcategoryinfo(@RequestParam("email") String email, @RequestParam("password") String password) {

        logService.savelog(new com.spring.model.Log(RequestDescriptions.CATEGORYINFO.getText(), getUserIP()));

        if (userService.getusertype(email).equals("google") && validation.isValidateGoogle(email, password)) {
            List<Object> reviewList = userDAO.getuserreviews(email);
            return new ResponseEntity<Object>(userService.getcategoryinfo(email), HttpStatus.OK); //
        } else {
            try {
                Token myToken = new Token(email, password, "all");
                if (validation.isvalidate(myToken)) {
                    return new ResponseEntity<Object>(userService.getcategoryinfo(email), HttpStatus.OK); //

                } else {
                    return new ResponseEntity<Object>(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS); //

                }
            } catch (Exception e) {
                return new ResponseEntity<Object>(HttpStatus.NOT_MODIFIED); //

            }
        }


    }

    @RequestMapping(value = "/getcategorizedreviews", method = RequestMethod.GET)
    public ResponseEntity<List<Object>> getcategorizedreviews(@RequestParam("email") String email, @RequestParam("category") String category, @RequestParam("password") String password)   //Kullanıcı ekleyen endpoint
    {
        logService.savelog(new com.spring.model.Log(RequestDescriptions.CATEGORIZEDREVIEW.getText(), getUserIP()));


        if (userService.getusertype(email).equals("google") && validation.isValidateGoogle(email, password)) {
            List<Object> reviewList = userDAO.getuserreviews(email);
            return new ResponseEntity<List<Object>>(userService.getcategorizedreviews(email, category), HttpStatus.OK); //
        } else {
            try {
                Token myToken = new Token(email, password, "");
                if (validation.isvalidate(myToken)) {
                    return new ResponseEntity<List<Object>>(userService.getcategorizedreviews(email, category), HttpStatus.OK); //

                } else {
                    return new ResponseEntity<List<Object>>(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS); //

                }
            } catch (Exception e) {
                return new ResponseEntity<List<Object>>(HttpStatus.NOT_MODIFIED); //
            }
        }


    }

    @RequestMapping(value = "/changeUserName", method = RequestMethod.POST)
    public ResponseEntity<Boolean> changeUserName(@RequestParam("email") String email, @RequestParam("name") String name) {
        logService.savelog(new com.spring.model.Log(RequestDescriptions.CHANGEUSERNAME.getText(), getUserIP()));
        try {
            if (userService.changeUserName(email, name)) {
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(false, HttpStatus.NOT_MODIFIED);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
    public ResponseEntity<Void> resetPassword(@RequestParam("email") String email) {
        logService.savelog(new com.spring.model.Log(RequestDescriptions.RESETPASSWORD.getText(), getUserIP()));
        try {
            if (userService.resetPassword(email)) {
                return new ResponseEntity<>(HttpStatus.OK);
            } else
                return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }



    @RequestMapping(value = "/newPassword", method = RequestMethod.GET)
    public ResponseEntity<Void> newPassword(@RequestParam("email") String email, @RequestParam("newPassword") String newPassword)   //Kullanıcı ekleyen endpoint
    {
        try {
            if (userService.newPassword(email, newPassword)) {
                return new ResponseEntity<Void>(HttpStatus.OK); //
            } else
                return new ResponseEntity<Void>(HttpStatus.SERVICE_UNAVAILABLE); //
        }
        catch (Exception e) {
            return new ResponseEntity<Void>(HttpStatus.SERVICE_UNAVAILABLE); //
        }
    }

    @RequestMapping(value = "/isuserexist", method = RequestMethod.GET)
    public ResponseEntity<?> isuserexist(@RequestParam("email") String email)   //Kullanıcı ekleyen endpoint
    {
        try {

            if (userDAO.isUserExist(email)) {
                return new ResponseEntity<String>("true", HttpStatus.OK); //

            } else {
                return new ResponseEntity<String>("false", HttpStatus.UNAUTHORIZED); //

            }

        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage().toString(), HttpStatus.NOT_MODIFIED); //

        }
    }


    @RequestMapping(value = "/sendmail", method = RequestMethod.GET)
    public ResponseEntity<?> sendmail(@RequestParam("email") String email) {
        try {
            logService.savelog(new com.spring.model.Log(RequestDescriptions.SENDMAIL.getText(), getUserIP()));
            if (!userDAO.isUserActive(email)) {
                return new ResponseEntity<>(userService.sendmail(email), HttpStatus.OK); //
            } else {
                return new ResponseEntity<String>("false", HttpStatus.NOT_FOUND); //
            }
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage().toString(), HttpStatus.NOT_MODIFIED); //
        }
    }

    public String getUserIP() {
        ServletRequestAttributes attr = (ServletRequestAttributes)
                RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        return request.getRemoteAddr();
    }

    //burdan sonrası değişti
    @RequestMapping(value = "/changeUserImage", method = RequestMethod.POST)
    public ResponseEntity<Void> changeUserImage(@RequestBody AppUser user) {
        if (userService.changeUserImage(user.getUserID(), user.getProfilImageID(), user.getCoverImage())) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    //getProfileImage
    @RequestMapping(value = "/getProfileImage", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getProfileImage(@RequestParam("userID") long userID) {
        return new ResponseEntity<>(userService.getProfileImage(userID), HttpStatus.OK);
    }

    //getCoverImage
    @RequestMapping(value = "/getCoverImage", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getCoverImage(@RequestParam("userID") long userID) {
        return new ResponseEntity<>(userService.getCoverImage(userID), HttpStatus.OK);
    }

    //getUserPoints
    @RequestMapping(value = "/getUserPoints", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<Restaurant>> getUserPoints(@RequestParam("userID") long userID, @RequestParam("page") int page) {
        return new ResponseEntity<>(userService.getUserPoints(userID, page), HttpStatus.OK);
    }

    //getUserPlace
    @RequestMapping(value = "/getUserPlace", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<Restaurant>> getUserPlace(@RequestParam("userID") long userID, @RequestParam("page") int page) {
        return new ResponseEntity<>(userService.getUserPlace(userID, page), HttpStatus.OK);
    }

    //getUserFavorite
    @RequestMapping(value = "/getUserFavorite", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<Restaurant>> getUserFavorite(@RequestParam("userID") long userID, @RequestParam("page") int page) {
        return new ResponseEntity<>(userService.getUserFavorite(userID, page), HttpStatus.OK);
    }

    //getUserSubCategory
    @RequestMapping(value = "/getUserSubCategory", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<Restaurant>> getUserFavorite(@RequestParam("userID") long userID, @RequestParam("category") int category, @RequestParam("page") int page) {
        return new ResponseEntity<>(userService.getUserSubCategory(userID, category, page), HttpStatus.OK);
    }

    //getTopUserList
    @RequestMapping(value = "/getTopUserList", method = RequestMethod.GET)
    public ResponseEntity<List<Object>> getTopUserList() {
        try {
            return new ResponseEntity<List<Object>>(userService.getTopUserList(), HttpStatus.OK); //
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

}
