package com.nachtgeistw.impurebird;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import static com.nachtgeistw.impurebird.util.util.getBitmapFromURL;

public class DetailPageActivity extends AppCompatActivity {

    ImageView user_head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_page);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String userName = bundle.getString("user_name");
        String userTweet = bundle.getString("user_tweet");
        String userHead = bundle.getString("user_head");

        user_head = (ImageView) findViewById(R.id.user_head);
        TextView user_name = (TextView) findViewById(R.id.user_name);
        TextView user_tweet = (TextView) findViewById(R.id.user_text);

        user_name.setText(userName);
        user_tweet.setText(userTweet);
        //关联好四个图片
        ImageView[] user_images = new ImageView[4];
        user_images[0] = findViewById(R.id.user_image1);
        user_images[1] = findViewById(R.id.user_image2);
        user_images[2] = findViewById(R.id.user_image3);
        user_images[3] = findViewById(R.id.user_image4);

        int imageNum = bundle.getInt("imagenum");
        //可能主线程太快了，还没execute就下一个循环了
        String[] userImages = new String[4];
        String user_image = "user_image" + String.valueOf(0);
        for (int temp = 0; temp < 4; temp++) {
            userImages[temp] = null;
        }
        for (int i = 0; i < imageNum; i++) {
            user_image = "user_image" + String.valueOf(i);
            userImages[i] = bundle.getString(user_image);
            Log.e("#######图片", userImages[i]);
        }
        for (int i = 0; i < imageNum; i++) {
            Log.e("设！！！！置图片", userImages[i]);
            DownLoadHead image = new DownLoadHead(user_images[i]);
            image.execute(userImages[i]);
        }
        DownLoadHead head = new DownLoadHead(user_head);
        // holder.userHead.setImageBitmap(tweet.getUser_head());
        head.execute(userHead);

//开始设置点击事件
        user_images[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userImages[0] != null) {
                    Intent intent = new Intent(DetailPageActivity.this, PicActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("url", userImages[0]);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        user_images[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userImages[1] != null) {
                    Intent intent = new Intent(DetailPageActivity.this, PicActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("url", userImages[1]);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        user_images[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userImages[2] != null) {
                    Intent intent = new Intent(DetailPageActivity.this, PicActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("url", userImages[2]);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        user_images[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userImages[3] != null) {
                    Intent intent = new Intent(DetailPageActivity.this, PicActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("url", userImages[3]);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }

    class DownLoadHead extends AsyncTask<String, Void, Bitmap> {
        private ImageView mImageView;
        String url;
        DownLoadHead(ImageView imageView) {
            mImageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            url = params[0];
            // BitmapDrawable drawable = new BitmapDrawable(mContext.getResources(),bitmap);
            return getBitmapFromURL(url);
        }

        @Override
        protected void onPostExecute(Bitmap bimage) {
            super.onPostExecute(bimage);
            if (mImageView != null && bimage != null) {
                mImageView.setImageBitmap(bimage);
            }
        }
    }
}
