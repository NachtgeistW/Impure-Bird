package com.nachtgeistw.impurebird;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nachtgeistw.impurebird.util.image.DownloadPic;

import java.util.Objects;

import static com.nachtgeistw.impurebird.util.image.getBitmapFromURL;

public class DetailPageActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_page_with_four_pics);

        //UI initial
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;

        TextView userName = findViewById(R.id.detail_page_user_name);
        String s1 = bundle.getString("user_name");
        userName.setText(s1);

        TextView userTweet = findViewById(R.id.detail_page_user_text);
        String s2 = bundle.getString("user_tweet");
        userTweet.setText(s2);

        ImageView userAvatar = findViewById(R.id.detail_page_user_avatar);
        String s3 = bundle.getString("user_avatar");
        DownloadPic avatar = new DownloadPic(userAvatar);
        avatar.execute(s3);

        int picNum = bundle.getInt("pic_num");
        if (picNum > 0) {
            ImageView[] picList = new ImageView[4];
            picList[0] = findViewById(R.id.TWEET_PIC_1);
            picList[1] = findViewById(R.id.TWEET_PIC_2);
            picList[2] = findViewById(R.id.TWEET_PIC_3);
            picList[3] = findViewById(R.id.TWEET_PIC_4);

            //可能主线程太快了，还没execute就下一个循环了
            String[] picURL = new String[4];
            for (int i = 0; i < 4; i++) {
                picURL[i] = null;
            }
            for (int i = 0; i < picNum; i++) {
                picURL[i] = bundle.getString("user_image" + i);
                DownloadPic tweetPic = new DownloadPic(picList[i]);
                tweetPic.execute(picURL[i]);
            }

            //开始设置点击事件
            picList[0].setOnClickListener(v -> {
                if (picURL[0] != null) {
                    Intent intent1 = new Intent(DetailPageActivity.this, PicActivity.class);
                    intent1.putExtra("url", picURL[0]);
                    startActivity(intent1);
                }
            });
            picList[1].setOnClickListener(v -> {
                if (picURL[1] != null) {
                    Intent intent2 = new Intent(DetailPageActivity.this, PicActivity.class);
                    intent2.putExtra("url", picURL[1]);
                    startActivity(intent2);
                }
            });
            picList[2].setOnClickListener(v -> {
                if (picURL[2] != null) {
                    Intent intent3 = new Intent(DetailPageActivity.this, PicActivity.class);
                    intent3.putExtra("url", picURL[2]);
                    startActivity(intent3);
                }
            });
            picList[3].setOnClickListener(v -> {
                if (picURL[3] != null) {
                    Intent intent4 = new Intent(DetailPageActivity.this, PicActivity.class);
                    intent4.putExtra("url", picURL[3]);
                    startActivity(intent4);
                }
            });
        }
    }

    //Toolbar的事件---返回
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}