package com.spring.controller;

import com.spring.model.AppUser;
import com.spring.feedbacks.Error;
import com.spring.service.MailService;
import com.spring.service.UserService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by egulocak on 8.04.2020.
 */


@RestController
@RequestMapping("/user")
public class UserRestController {

    @Setter
    @Autowired
    UserService userService;

    @Autowired
    Error error;


    @Autowired
    MailService mailService;


    @RequestMapping(value = "/insertuser", method = RequestMethod.POST)
    public ResponseEntity<?> insertUser(@RequestBody AppUser user)   //Kullanıcı ekleyen endpoint
    {
        try {
            if (!userService.isUserExist(user.getUserEmail())) {



                if (user.getUserType().equals("standard")) {


                    error.setFeedback("Code sent. Check  your email to activate your account");
                    error.setCode(200);
                    user.setCode(mailService.sendMail(user.getUserEmail()));
                    userService.insertUser(user);

                    return new ResponseEntity<Error>(error, HttpStatus.UNAUTHORIZED);
                }

                return new ResponseEntity<AppUser>(userService.insertUser(user), HttpStatus.CREATED);

            } else
            {
                error.setCode(409);
                error.setFeedback("User already Exists");
                return new ResponseEntity<Error>(error, HttpStatus.CONFLICT);
            }



        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<AppUser>(HttpStatus.NOT_MODIFIED);
        }

    }



    @RequestMapping(value = "/listallusers", method = RequestMethod.GET)
    public ResponseEntity<List<Object>> listAllUsers()   //Kullanıcı ekleyen endpoint
    {
        try {
            return new ResponseEntity<List<Object>>(userService.listAllUsers(), HttpStatus.OK); //
        } catch (Exception e) {

            return new ResponseEntity<List<Object>>(HttpStatus.NOT_MODIFIED);
        }

    }

    @RequestMapping(value = "/updateuser", method = RequestMethod.POST)
    public ResponseEntity<AppUser> updateUser(@RequestBody AppUser user)   //Kullanıcı güncelleyen endpoint

    {       //kullanıcıyı update ederken komple kullanıcı classını karşılayan bir json gönderin
        //Ancak idsivtde olan bir id olmalı
        try {
            return new ResponseEntity<AppUser>(userService.updateUser(user), HttpStatus.OK); //

        } catch (Exception e) {
            return new ResponseEntity<AppUser>(userService.updateUser(user), HttpStatus.NOT_MODIFIED); //

        }


    }

    @RequestMapping(value = "/checkstandard", method = RequestMethod.GET)
    public ResponseEntity<?> checkStandard(@RequestParam("email") String email, @RequestParam("password") String password)   //Kullanıcı güncelleyen endpoint

    {


        try {
            if (userService.checkStandardCredentials(email, password)) {


                if (userService.isUserActive(email)) {


                    return new ResponseEntity<AppUser>(userService.findUserByEmail(email), HttpStatus.OK);
                } else {

                    error.setCode(204);
                    error.setFeedback("Please Activate your account via Code sent to your email");
                    return new ResponseEntity<Error>(error, HttpStatus.UNAUTHORIZED);

                }

            } else
            {
                error.setCode(404);
                error.setFeedback("Wrong email or password");
                return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);

            }
        } catch (Exception e) {

            error.setCode(405);
            error.setFeedback("Something went wrong");
            return new ResponseEntity<Error>(error, HttpStatus.NOT_MODIFIED);
        }


    }

    @RequestMapping(value = "/checkgoogle", method = RequestMethod.POST)
    public ResponseEntity<?> checkGoogle(@RequestBody AppUser user)   //Kullanıcı güncelleyen endpoint

    {
        if (!userService.isUserExist(user.getUserEmail())){
            return new ResponseEntity<AppUser>(userService.insertUser(user), HttpStatus.OK);        }
        else {
            if(userService.checkUserType(user).equals("google"))

                return new ResponseEntity<AppUser>(userService.findUserByEmail(user.getUserEmail()), HttpStatus.OK);
            else
            {
                error.setCode(409);
                error.setFeedback("User exist with another membership type");
                return new ResponseEntity<Error>(error, HttpStatus.CONFLICT);
            }
        }






    }

    @RequestMapping(value = "/checkusercode", method = RequestMethod.GET)
    public ResponseEntity<?> checkGoogle(@RequestParam("email") String email, @RequestParam("code") long code)   //Kullanıcı güncelleyen endpoint

    {
        if (userService.checkUserCode(email, code)) {
            return new ResponseEntity<AppUser>(userService.updateUserStatus(email), HttpStatus.OK);
        } else {
            error.setCode(204 );
            error.setFeedback("Code is not valid");
            return new ResponseEntity<Error>(error, HttpStatus.UNAUTHORIZED);

        }


    }


}
