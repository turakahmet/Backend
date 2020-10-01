package com.spring.requestenum;


public enum RequestDescriptions {



    INFOADMIN("infoadmin"),
    NEWACCOUNT("newaccount"),
    STANDARDLOGIN("standardlogin"),
    GOOGLELOGIN("googlelogin"),
    CHECKCODE("checkcode"),
    GETUSERID("userid"),
    LISTREVIEWS("listreview"),
    GETUSERREVIEWS("getuserreviews"),
    CHANGEPASSWORD("changepassword"),
    CATEGORYINFO("categoryinfo"),
    CATEGORIZEDREVIEW("categorizedreview"),
    CHANGEUSERNAME("changeusername"),
    RESETPASSWORD("resetpassword"),
    SETPASSWORD("setpassword"),
    SENDMAIL("sendmail"),
    VOTERESTAURANT("voterestaurant"),
    UPDATEVOTE("updatevote"),
    GETRECORD("getrecord"),
    SAVERECORD("saverecord");


    private String text;

     RequestDescriptions(String text)
    {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
