package com.spring.model;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by egulocak on 8.05.2020.
 */
@Getter
@Setter
public class CustomUser {
    private long userID;
    private String userName;
    private String userSurname;
    private String userToken;
    private String userEmail;
    private byte[] profilImageID;


}
