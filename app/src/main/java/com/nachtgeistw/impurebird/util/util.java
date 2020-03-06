package com.nachtgeistw.impurebird.util;

import android.app.Activity;
import android.content.SharedPreferences;

import com.jakewharton.disklrucache.DiskLruCache;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class util {
    public static String tweet_content = "tweet_content";

    public static class ActivityCollector {
        static List<Activity> activityList = new ArrayList<>();

        public static void addActivity(Activity activity) {
            activityList.add(activity);
        }

        public static void removeActivity(Activity activity) {
            activityList.remove(activity);
        }

        public static void finishAll() {
            for (Activity activity : activityList) {
                if (!activity.isFinishing()) {
                    activity.finish();
                }
            }
            activityList.clear();
        }
    }

    public static DiskLruCache utilDiskLruCache;

    public static final String USER_NAME = "user_name";
    public static final String USER_NICKNAME = "user_nickname";
    public static SharedPreferences utilSharedPreferences;

    public static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(0xFF & aByte);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
