package com.spring.token;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class Token {
    String email;
    String password;
    String type;


    public Token(String email, String password, String type){
        this.email = email;
        this.password = password;
        this.type=type;
    }
}


