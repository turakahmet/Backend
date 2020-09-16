package com.spring.requestenum;

/**
 * Created by egulocak on 26.08.2020.
 */
public enum sizes {

    SIGNLOGIN(20),  //size for signup,login,vote
    credentialschange(5), //size for changing password,reset new password,set password or mail request per day
    VOTE(50),
    LOGINS(150);



    private int size;

    sizes(int size)
    {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setSize(String text) {
        this.size = size;
    }
}
