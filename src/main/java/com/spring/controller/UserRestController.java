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

import javax.mail.Multipart;
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
                    user.setCode(mailService.sendMail(user.getUserEmail(),user.getUserPassword()));
                     userService.insertUser(user);

                    return new ResponseEntity<CustomUser>(userService.findUserByEmail(user.getUserEmail()), HttpStatus.UNAUTHORIZED);



            } else
            {
                error.setCode(409);
                error.setFeedback("Bu email ile kayıtlı kullanıcı zaten var.");
                return new ResponseEntity<Error>(error, HttpStatus.CONFLICT);
            }



        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<String>(e.getMessage(),HttpStatus.NOT_MODIFIED);
        }

    }



    @RequestMapping(value = "/listallusers", method = RequestMethod.POST)  //Bunu Kaldır
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
                System.out.println("CHEEECK STANDARDDDDDD TRUE");


                if (userService.isUserActive(email)) {

                    System.out.println("İS ACTİVE TRUE");

                    return new ResponseEntity<CustomUser>(userService.findUserByEmail(email), HttpStatus.OK);
                } else {


                    return new ResponseEntity<CustomUser>(userService.findUserByEmail(email), HttpStatus.UNAUTHORIZED);

                }

            } else
            {
                error.setCode(404);
                error.setFeedback("Parolanızı veya email adresinizi yanlış girdiniz.");
                return new ResponseEntity<Error>(error, HttpStatus.NOT_MODIFIED);

            }
        } catch (Exception e) {

            error.setCode(405);
            error.setFeedback("Something went wrong");
            return new ResponseEntity<Error>(error, HttpStatus.NOT_FOUND);
        }


    }

    @RequestMapping(value = "/checkgoogle", method = RequestMethod.POST)
    public ResponseEntity<?> checkGoogle(@RequestBody AppUser user)   //Kullanıcı güncelleyen endpoint

    {
        user.setUserPassword("YNGUP_default_");
        if (!userService.isUserExist(user.getUserEmail())){

            return new ResponseEntity<AppUser>(userService.insertUser(user), HttpStatus.OK);        }
        else {
            if(userService.getusertype(user.getUserEmail()).equals("google") || userService.getusertype(user.getUserEmail()).equals("facebook") )

            {
                System.out.println("USERTYPE GOOGLE VEYA FACEBOOKTOR.");
                return new ResponseEntity<CustomUser>(userService.findUserByEmail(user.getUserEmail()), HttpStatus.OK);
            }
            else
            {

                return new ResponseEntity<String>(HttpStatus.CONFLICT);
            }
        }






    }

    @RequestMapping(value = "/checkusercode", method = RequestMethod.GET)
    public ResponseEntity<?> checkGoogle(@RequestParam("email") String email, @RequestParam("code") long code,@RequestParam("password") String password)   //Kullanıcı güncelleyen endpoint

    {
        try{
            Token myToken = new Token(email,password,"");
            if(validation.isvalidate(myToken)){
                if (userService.checkUserCode(email, code)) {
                    System.out.println("Code: "+code+"");


                    return new ResponseEntity<AppUser>(userService.updateUserStatus(email), HttpStatus.OK);
                } else {
                    error.setCode(204 );
                    error.setFeedback("Girmiş olduğunuz kod geçerli değil.");
                    return new ResponseEntity<Error>(error, HttpStatus.UNAUTHORIZED);

                }
            }

            else{
                return new ResponseEntity<Error>(error, HttpStatus.UNAUTHORIZED);

            }
        }
        catch(Exception e){
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);

        }



    }
    @RequestMapping(value = "/getuserid", method = RequestMethod.GET)
    public ResponseEntity<?> checkGoogle(@RequestParam("email") String email)   //Kullanıcı güncelleyen endpoint

    {

            return new ResponseEntity<CustomUser>(userService.findUserByEmail(email), HttpStatus.OK);



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
    public ResponseEntity<List<Object>> getuserreviews(@RequestParam("email") String email,@RequestParam("password") String password)   //Kullanıcı ekleyen endpoint
    {
        if(userService.getusertype(email).equals("google") && validation.isValidateGoogle(email,password)){
            List<Object> reviewList = userDAO.getuserreviews(email);
            return new ResponseEntity<List<Object>>(reviewList, HttpStatus.OK); //
        }
        else{
            try{
            Token myToken = new Token(email,password,"");
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
        }}


    }

    @RequestMapping(value = "/getreviewcount", method = RequestMethod.GET)
    public ResponseEntity<Long> getreviewcount(@RequestParam("email") String email,@RequestParam("password") String password)
    {
            if(validation.isValidateGoogle(email,password)){
                return new ResponseEntity<Long>(userDAO.getreviewcount(email,password), HttpStatus.OK); //

            }
            else{
                try {
                    Token myToken = new Token(email,password,"all");
                    if(validation.isvalidate(myToken))
                    {
                        return new ResponseEntity<Long>(userDAO.getreviewcount(email,password), HttpStatus.OK); //
                    }
                    else
                        return new ResponseEntity<Long>( HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS); //


                } catch (Exception e) {

                    System.out.print(e.getMessage());

                    return new ResponseEntity<Long>(HttpStatus.NOT_MODIFIED);
                }
            }


    }


    @RequestMapping(value = "/changepassword", method = RequestMethod.GET)
    public ResponseEntity<String> changepassword(@RequestParam("email") String email,@RequestParam("password") String password,
                                                 @RequestParam("newpw") String newpw)   //Kullanıcı ekleyen endpoint
    {
        try{
                Token token = new Token(email,password,"");
                if(validation.isvalidate(token)){

                    String result = userService.changepassword(email,password,newpw);
                    if(result.equals("ok"))
                        return new ResponseEntity<String>("OK",HttpStatus.OK);
                    else{
                        return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
                    }
                }

                else
                    return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED); //






        }

        catch(Exception e){
            return new ResponseEntity<String>(HttpStatus.NOT_MODIFIED); //

        }




    }
    @RequestMapping(value = "/getcategoryinfo", method = RequestMethod.GET)
    public ResponseEntity<Object> getcategoryinfo(@RequestParam("email") String email,@RequestParam("password") String password)
    {
        if(userService.getusertype(email).equals("google") && validation.isValidateGoogle(email,password)){
            List<Object> reviewList = userDAO.getuserreviews(email);
            return new ResponseEntity<Object>(userService.getcategoryinfo(email), HttpStatus.OK); //
        }
        else{
            try{
                Token myToken = new Token(email,password,"all");
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



    }

    @RequestMapping(value = "/getcategorizedreviews", method = RequestMethod.GET)
        public ResponseEntity<List<Object>> getcategorizedreviews(@RequestParam("email") String email,@RequestParam("category") String category,@RequestParam("password") String password)   //Kullanıcı ekleyen endpoint
        {

            if(userService.getusertype(email).equals("google") && validation.isValidateGoogle(email,password)){
                List<Object> reviewList = userDAO.getuserreviews(email);
                return new ResponseEntity<List<Object>>(userService.getcategorizedreviews(email,category), HttpStatus.OK); //
            }
            else{
                try{
                    Token myToken = new Token(email,password,"");
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


    }

    @RequestMapping(value = "/changeusername", method = RequestMethod.POST)
    public ResponseEntity<String> changeusername(@RequestBody AppUser user)   //Kullanıcı ekleyen endpoint
    {
        try{
            Token myToken = new Token(user.getUserEmail(),user.getUserPassword(),"");
            if(validation.isvalidate(myToken))
            {
                return new ResponseEntity<String>(userService.changeusername(user),HttpStatus.OK); //
            }

            else{
                return new ResponseEntity<String>("err",HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS); //

            }
        }

        catch(Exception e){


            return new ResponseEntity<String>("err",HttpStatus.SERVICE_UNAVAILABLE); //
        }
    }
    @RequestMapping(value = "/resetpassword", method = RequestMethod.GET)
    public ResponseEntity<Void> passwordreset(@RequestParam("email") String email)   //Kullanıcı ekleyen endpoint
    {


        try{
            if(userService.isUserExist(email))
            {

                if(userService.isUserActive(email)){
                    if(mailService.resetpassword(email))
                    return new ResponseEntity<Void>(HttpStatus.OK); //
                    else
                        return new ResponseEntity<Void>(HttpStatus.SERVICE_UNAVAILABLE);


                }
                else
                    return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED); //


            }
            else{
                return new ResponseEntity<Void>(HttpStatus.NOT_FOUND); //

            }


        }

        catch(Exception e){


            return new ResponseEntity<Void>(HttpStatus.SERVICE_UNAVAILABLE); //
        }
    }

    @RequestMapping(value = "/setpassword", method = RequestMethod.GET)
    public ResponseEntity<Void> setpassword(@RequestParam("email") String email,@RequestParam("token") String token,@RequestParam("newpw") String newpw)   //Kullanıcı ekleyen endpoint
    {


        try {
            if (validation.isValidateRequest(email, token)) {
                 if(userService.setpassword(email,newpw,token)){
                     return new ResponseEntity<Void>(HttpStatus.OK); //

                 }
             else
                return new ResponseEntity<Void>(HttpStatus.SERVICE_UNAVAILABLE); //


        }

            else{
                return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED); //

            }


        }

        catch(Exception e){


            return new ResponseEntity<Void>(HttpStatus.SERVICE_UNAVAILABLE); //
        }
    }

    @RequestMapping(value = "/isuserexist", method = RequestMethod.GET)
    public ResponseEntity<?> isuserexist(@RequestParam ("email") String email)   //Kullanıcı ekleyen endpoint
    {
        try {

            if(userDAO.isUserExist(email))
            {
                return new ResponseEntity<String>("true",HttpStatus.OK); //

            }
            else{
                return new ResponseEntity<String>("false",HttpStatus.UNAUTHORIZED); //

            }

        }
        catch(Exception e){
            return new ResponseEntity<String>(e.getMessage().toString(),HttpStatus.NOT_MODIFIED); //

        }
    }
}
