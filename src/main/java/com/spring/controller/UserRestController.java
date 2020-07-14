package com.spring.controller;

import com.spring.dao.UserDAO;
import com.spring.model.AdminTK;
import com.spring.token.*;
import com.spring.model.AppUser;
import com.spring.feedbacks.Error;
import com.spring.model.CustomUser;
import com.spring.model.Review;
import com.spring.service.MailService;
import com.spring.service.UserService;
import com.spring.token.ValidationDao;
import lombok.Setter;
import org.hibernate.annotations.common.util.impl.Log;
import org.hibernate.event.service.internal.EventListenerServiceInitiator;
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
    UserDAO userDAO;


    @Autowired
    Validation validation;

    @Autowired
    Error error;


    @Autowired
    MailService mailService;


    @RequestMapping(value = "/insertuser", method = RequestMethod.POST)
    public ResponseEntity<?> insertUser(@RequestBody AppUser user)   //Kullanıcı ekleyen endpoint
    {
        try {
            if (!userService.isUserExist(user.getUserEmail())) {





                    user.setStatus("deactive");


                    error.setFeedback("Giriş Yaparken tek kullanımlık kod mail adresinize gönderildi.");
                    error.setCode(200);
                    user.setCode(mailService.sendMail(user.getUserEmail()));
                     userService.insertUser(user);

                    return new ResponseEntity<CustomUser>(userService.findUserByEmail(user.getUserEmail(),""), HttpStatus.UNAUTHORIZED);



            } else
            {
                error.setCode(409);
                error.setFeedback("Bu email ile kayıtlı kullanıcı zaten var.");
                return new ResponseEntity<Error>(error, HttpStatus.CONFLICT);
            }



        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<AppUser>(HttpStatus.NOT_MODIFIED);
        }

    }



    @RequestMapping(value = "/listallusers", method = RequestMethod.POST)
    public ResponseEntity<List<Object>> listAllUsers(@RequestBody AdminTK adminTK)   //Kullanıcı ekleyen endpoint
    {
        try {
            if(userService.isAdmin(adminTK))
            return new ResponseEntity<List<Object>>(userService.listAllUsers(), HttpStatus.OK); //

            else if(!userService.isAdmin(adminTK))
                return new ResponseEntity<List<Object>>( HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS); //

            else{
          return new ResponseEntity<List<Object>>( HttpStatus.SERVICE_UNAVAILABLE); //

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
    public ResponseEntity<?> checkStandard(@RequestParam("email") String email, @RequestParam("password") String password)   //Kullanıcı güncelleyen endpoint

    {
            System.out.println("CHEEECK STANDARDDDDDD");

        try {
            if (userService.checkStandardCredentials(email, password)) {


                if (userService.isUserActive(email)) {


                    return new ResponseEntity<CustomUser>(userService.findUserByEmail(email,""), HttpStatus.OK);
                } else {


                    return new ResponseEntity<CustomUser>(userService.findUserByEmail(email,""), HttpStatus.UNAUTHORIZED);

                }

            } else
            {
                error.setCode(404);
                error.setFeedback("Parolanızı veya email adresinizi yanlış girdiniz.");
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
            if(userService.checkUserType(user).equals("google") || userService.checkUserType(user).equals("facebook") )

                return new ResponseEntity<CustomUser>(userService.findUserByEmail(user.getUserEmail(),""), HttpStatus.OK);
            else
            {
                error.setCode(409);
                error.setFeedback("Bu adrese kayıtlı kullanıcı zaten var.");
                return new ResponseEntity<Error>(error, HttpStatus.CONFLICT);
            }
        }






    }

    @RequestMapping(value = "/checkusercode", method = RequestMethod.GET)
    public ResponseEntity<?> checkGoogle(@RequestParam("email") String email, @RequestParam("code") long code,@RequestParam("password") String password)   //Kullanıcı güncelleyen endpoint

    {
        if (userService.checkUserCode(email, code)) {
            System.out.println("Code: "+code+"");


            return new ResponseEntity<AppUser>(userService.updateUserStatus(email), HttpStatus.OK);
        } else {
            error.setCode(204 );
            error.setFeedback("Girmiş olduğunuz kod geçerli değil.");
            return new ResponseEntity<Error>(error, HttpStatus.UNAUTHORIZED);

        }


    }
    @RequestMapping(value = "/getuserid", method = RequestMethod.GET)
    public ResponseEntity<?> checkGoogle(@RequestParam("email") String email)   //Kullanıcı güncelleyen endpoint

    {

            return new ResponseEntity<CustomUser>(userService.findUserByEmail(email,""), HttpStatus.OK);



    }

    @RequestMapping(value = "/listreviews", method = RequestMethod.POST)
    public ResponseEntity<List<Review>> listreviews(@RequestBody AdminTK adminTK,@RequestParam("email") String email)
    {
        try {
            if(userService.isAdmin(adminTK))
            return new ResponseEntity<List<Review>>(userService.getReview(email), HttpStatus.OK); //

            else if(!userService.isAdmin(adminTK))
                return new ResponseEntity<List<Review>>(userService.getReview(email), HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS);
            else{
                return new ResponseEntity<List<Review>>(HttpStatus.SERVICE_UNAVAILABLE);
            }

        } catch (Exception e) {

            System.out.print(e.getMessage());

            return new ResponseEntity<List<Review>>(HttpStatus.NOT_MODIFIED);
        }

    }

    @RequestMapping(value = "/getuserreviews", method = RequestMethod.GET)
    public ResponseEntity<List<Object>> getuserreviews(@RequestParam("email") String email,@RequestParam("token") String token,String password)   //Kullanıcı ekleyen endpoint
    {
        try{
            Token myToken = new Token(token,email,password,"All");
            if(validation.isvalidate(myToken))
            {

                    List<Object> reviewList = userDAO.getuserreviews(email);
                return new ResponseEntity<List<Object>>(reviewList,HttpStatus.OK); //



        }
            else
                return new ResponseEntity<List<Object>>(HttpStatus.UNAUTHORIZED); //



        } catch (Exception e) {

            System.out.print(e.getMessage());

            return new ResponseEntity<List<Object>>(HttpStatus.NOT_MODIFIED);
        }

    }

    @RequestMapping(value = "/getreviewcount", method = RequestMethod.GET)
    public ResponseEntity<Long> getreviewcount(@RequestParam("email") String email,@RequestParam("password") String password,@RequestParam("token") String token)
    {


        try {
            Token myToken = new Token(token,email,password,"all");
            if(validation.isvalidate(myToken))
            {
                return new ResponseEntity<Long>(userDAO.getreviewcount(email,password,token), HttpStatus.OK); //
            }
            else
                return new ResponseEntity<Long>( HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS); //


        } catch (Exception e) {

            System.out.print(e.getMessage());

            return new ResponseEntity<Long>(HttpStatus.NOT_MODIFIED);
        }

    }


    @RequestMapping(value = "/changepassword", method = RequestMethod.GET)
    public ResponseEntity<String> changepassword(@RequestParam("email") String email,@RequestParam("password") String password,@RequestParam("token") String token)   //Kullanıcı ekleyen endpoint
    {
        try{
            Token myToken = new Token(token,email,password,"User");
            if(validation.isvalidate(myToken)){
                String result = userService.changepassword(email,password);
                if(result.equals("ok"))
                    return new ResponseEntity<String>(HttpStatus.OK); //
                else
                    return new ResponseEntity<String>(HttpStatus.NOT_MODIFIED); //
            }

            else{
                return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED); //

            }



        }

        catch(Exception e){
            return new ResponseEntity<String>(HttpStatus.NOT_MODIFIED); //

        }




    }
    @RequestMapping(value = "/getcategoryinfo", method = RequestMethod.GET)
    public ResponseEntity<Object> getcategoryinfo(@RequestParam("email") String email,@RequestParam("password") String password,@RequestParam("token") String token)
    {

        try{
            Token myToken = new Token(token,email,password,"all");
            if(validation.isvalidate(myToken)){
                return new ResponseEntity<Object>(userService.getcategoryinfo(email),HttpStatus.OK); //

            }
            else {
                return new ResponseEntity<Object>(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS); //

            }
        }
        catch(Exception e){
            return new ResponseEntity<Object>(HttpStatus.NOT_MODIFIED); //

        }


    }

    @RequestMapping(value = "/getcategorizedreviews", method = RequestMethod.GET)
        public ResponseEntity<List<Object>> getcategorizedreviews(@RequestParam("email") String email,@RequestParam("category") String category,@RequestParam("password") String password,@RequestParam("token") String token)   //Kullanıcı ekleyen endpoint
        {


            try{
                Token myToken = new Token(token,email,password,"all");
                if(validation.isvalidate(myToken))
                {
                    return new ResponseEntity<List<Object>>(userService.getcategorizedreviews(email,category),HttpStatus.OK); //

                }

                else{
                    return new ResponseEntity<List<Object>>(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS); //

                }
            }

            catch(Exception e){
                return new ResponseEntity<List<Object>>(HttpStatus.NOT_MODIFIED); //
            }
    }

    @RequestMapping(value = "/changeusername", method = RequestMethod.GET)
    public ResponseEntity<String> changeusername(@RequestParam("email") String email,@RequestParam("password") String password,@RequestParam("token") String token,@RequestParam("username") String username)   //Kullanıcı ekleyen endpoint
    {


        try{
            Token myToken = new Token(token,email,password,"all");
            if(validation.isvalidate(myToken))
            {
                return new ResponseEntity<String>(userService.changeusername(email,username),HttpStatus.OK); //

            }

            else{
                return new ResponseEntity<String>(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS); //

            }
        }

        catch(Exception e){


            return new ResponseEntity<String>(HttpStatus.SERVICE_UNAVAILABLE); //
        }
    }
}
