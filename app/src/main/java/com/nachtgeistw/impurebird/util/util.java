package com.nachtgeistw.impurebird.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import twitter4j.Status;

public class util {
    public static String tweet_content = "tweet_content";

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src", src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap", "returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception", e.getMessage());
            return null;
        }

    }

    public static class ActivityCollector{
        public static List<Activity> activityList = new ArrayList<>();

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

}
