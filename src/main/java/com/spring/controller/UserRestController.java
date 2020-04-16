package com.spring.controller;

import com.spring.model.AppUser;
import com.spring.model.Review;
import com.spring.service.MailService;
import com.spring.service.UserService;
import com.spring.service.UserServiceImpl;
import com.sun.org.apache.regexp.internal.RE;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.SecondaryTable;
import java.util.ArrayList;
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
    MailService mailService;


    @RequestMapping(value = "/insertuser", method = RequestMethod.POST)
    public ResponseEntity<?> insertUser(@RequestBody AppUser user)   //Kullanıcı ekleyen endpoint
    {
        try {
            mailService.sendMail(user.getUserEmail(),"2TOT");
            if (!userService.isUserExist(user.getUserEmail())) {


                return new ResponseEntity<AppUser>(userService.insertUser(user), HttpStatus.CREATED);

            }


            else
                return new ResponseEntity<String>("User Already Exist", HttpStatus.CONFLICT);


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

                System.out.println("BURDA");
                return new ResponseEntity<AppUser>(userService.findUserByEmail(email), HttpStatus.OK);

            } else
                return new ResponseEntity<String>("Wrong Email or Password", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<String>("Something went wrong", HttpStatus.NOT_MODIFIED);
        }


    }

    @RequestMapping(value = "/checkgoogle", method = RequestMethod.POST)
    public ResponseEntity<?> checkGoogle(@RequestBody AppUser user)   //Kullanıcı güncelleyen endpoint

    {
        if (!userService.isUserExist(user.getUserEmail())){
            return new ResponseEntity<AppUser>(userService.insertUser(user), HttpStatus.OK);
        }
        else {
            if(userService.checkUserType(user) =="google")
                return new ResponseEntity<AppUser>(userService.findUserByEmail(user.getUserEmail()), HttpStatus.OK);
            else
                return new ResponseEntity<String>("User exist with another membership type", HttpStatus.CONFLICT);
        }






    }


}
