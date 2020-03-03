package com.nachtgeistw.impurebird.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class image {

    public static void setTweetImage(Bundle bundle, int picNum, ImageView... list) throws Exception {
        ImageView[] picList;
        try {
            //Initialize picList according picNum
            switch (picNum) {
                case 1:
                    picList = new ImageView[1];
                    break;
                case 2:
                    picList = new ImageView[2];
                    break;
                case 3:
                    picList = new ImageView[3];
                    break;
                case 4:
                    picList = new ImageView[4];
                    break;
                default:
                    throw new Exception("Invalid tweet image number.");
            }
            for (int i = 0; i < picNum; i++) {
                String currentPicURL = bundle.getString("user_image" + i);
                DownloadPic tweetPic = new DownloadPic(picList[i]);
                tweetPic.execute(currentPicURL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //static问题稍后再搞
    public static class DownloadPic extends AsyncTask<String, Void, Bitmap> {
        ImageView mImageView;//mImageView为图片xml中的位置
        String url;

        public DownloadPic(ImageView imageView) {
            mImageView = imageView;
        }

        //获取图片url对应的bitmap
        @Override
        protected Bitmap doInBackground(String... params) {
            url = params[0];
            return getBitmapFromURL(url);
        }

        @Override
        protected void onPostExecute(Bitmap bitImage) {
            super.onPostExecute(bitImage);
            if (mImageView != null && bitImage != null) {
                mImageView.setImageBitmap(bitImage);
            }
        }
    }

    //A function to download bitmap by passing url argument
    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src", src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception", Objects.requireNonNull(e.getMessage()));
            return null;
        }
    }
}
