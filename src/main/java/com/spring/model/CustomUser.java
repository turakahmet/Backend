package com.spring.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomUser {
    private long userID;
    private String userName;
    private String userSurname;
    private String userToken;
    private String userEmail;
    private byte[] profilImageID;
    private byte[] coverImage;
    private String userImageUrl;

}
