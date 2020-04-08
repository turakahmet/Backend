package com.spring.controller;

import com.spring.model.AppUser;
import com.spring.service.UserService;
import com.spring.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * Created by egulocak on 8.04.2020.
 */


@RestController
@RequestMapping("/user")
public class UserRestController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "/insertuser", method = RequestMethod.POST)
    public ResponseEntity<AppUser> insertUser(@RequestBody AppUser user)   //Kullanıcı ekleyen endpoint
    {
        try {
            return new ResponseEntity<AppUser>(userService.insertUserWithMail(user),HttpStatus.CREATED); //
        }

        catch(Exception e){
            System.out.println(e.getMessage());
            return new ResponseEntity<AppUser>(HttpStatus.NOT_MODIFIED);
        }



    }

    @RequestMapping(value = "/listallusers", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<AppUser>> listAllUsers()   //Kullanıcı ekleyen endpoint
    {
        try {
            return new ResponseEntity<ArrayList<AppUser>>(userService.listAllUsers(),HttpStatus.OK); //
        }

        catch(Exception e){

            return new ResponseEntity<ArrayList<AppUser>>(HttpStatus.NOT_MODIFIED);
        }



    }
    @RequestMapping(value = "/updateuser", method = RequestMethod.POST)
    public ResponseEntity<AppUser> updateUser(@RequestBody AppUser user)   //Kullanıcı ekleyen endpoint

    {       //kullanıcıyı update ederken komple kullanıcı classını karşılayan bir json gönderin
            //Ancak idsivtde olan bir id olmalı
            try{
                return new ResponseEntity<AppUser>(userService.updateUser(user),HttpStatus.OK); //

            }
            catch(Exception e){
                return new ResponseEntity<AppUser>(userService.updateUser(user),HttpStatus.NOT_MODIFIED); //

            }





    }
}
