package com.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by egulocak on 18.08.2020.
 */
@Controller
@RequestMapping("/")
public class MainController {

    @GetMapping("/verification")
    public String actviationpage(@RequestParam("email") String email, @RequestParam("code") long verificationCode) {

        return "verification";

    }

    @GetMapping("/privacy")
    public String privacypage(){
        return "privacy";

            }
}
