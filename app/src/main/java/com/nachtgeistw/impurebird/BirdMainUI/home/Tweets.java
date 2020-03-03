package com.nachtgeistw.impurebird.BirdMainUI.home;

import android.graphics.Bitmap;

public class Tweets {
    private String user_name;
    private Bitmap user_avatar;


    Tweets(String user_name,Bitmap user_avatar){
        this.user_avatar = user_avatar;
        this.user_name = user_name;
    }

    public Bitmap getuser_avatar(){return user_avatar;}
    public  String getName() {
        return user_name;
    }
}
