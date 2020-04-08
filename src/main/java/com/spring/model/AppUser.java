package com.spring.model;
import javax.persistence.*;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by bdurmusoglu on 6.04.2020.
 */

@Setter
@Getter
@Entity
@Table

public class AppUser   {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userID;

    @Column
    private String userName;

    @Column
    private String userSurname;

    @Column(unique = true, nullable = false)
    private String userEmail;

    @Column(length = 30)
    private String userPassword;

    @Column(unique = true, nullable = false)
    private String userToken;

    @Column(nullable = false)
    private String userType;

    @Column
    private String profilImageID;


    public AppUser() {
        super();
    }
    public AppUser(String userName, String userSurname, String userEmail, String userPassword, String userToken, String userType, String profilImageID) {
        super();
        this.userName = userName;
        this.userSurname = userSurname;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userToken = userToken;
        this.userType = userType;
        this.profilImageID = profilImageID;
    }
}
