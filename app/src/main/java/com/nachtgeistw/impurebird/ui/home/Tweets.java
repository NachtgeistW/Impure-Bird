package com.nachtgeistw.impurebird.ui.home;


import android.graphics.Bitmap;

public class Tweets {
    private String user_name;
    private Bitmap user_head;


    Tweets(String user_name,Bitmap user_head){
        this.user_head = user_head;
        this.user_name = user_name;
    }

    public Bitmap getUser_head(){return user_head;}
    public  String getName() {
        return user_name;

    }
}
