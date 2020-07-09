package com.spring.token;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by egulocak on 6.07.2020.
 */
@Getter
@Setter
@NoArgsConstructor
public class Token {
    String token;
    String email;
    String password;
    String type;


    public Token(String token, String email, String password, String type){
        this.token=token;
        this.email = email;
        this.password = password;
        this.type=type;
    }
}


