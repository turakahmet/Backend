package com.spring.requestenum;


public enum sizes {

    SIGNLOGIN(200),  //size for signup,login,vote
    credentialschange(5), //size for changing password,reset new password,set password or mail request per day
    VOTE(50),
    SUPPORT(1),
    LOGINS(1000);
    // bir ip den 1 günde atılabilecek istek sayısı



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
