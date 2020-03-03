package com.nachtgeistw.impurebird.util;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.nachtgeistw.impurebird.DetailPageActivity;
import com.nachtgeistw.impurebird.PicActivity;
import com.nachtgeistw.impurebird.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import twitter4j.Status;

public class util {
    public static String tweet_content = "tweet_content";

    public static class ActivityCollector{
        static List<Activity> activityList = new ArrayList<>();

        public static void addActivity(Activity activity){
            activityList.add(activity);
        }

        public static void removeActivity(Activity activity){
            activityList.remove(activity);
        }

        public static void finishAll(){
            for (Activity activity: activityList){
                if(!activity.isFinishing()){
                    activity.finish();
                }
            }
            activityList.clear();
        }
    }

    public static final String USER_NAME = "user_name";
    public static final String USER_NICKNAME = "user_nickname";
    public static SharedPreferences utilSharedPreferences;
}
