package com.nachtgeistw.impurebird;

import androidx.appcompat.app.AppCompatActivity;
import twitter4j.MediaEntity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import static com.nachtgeistw.impurebird.util.util.getBitmapFromURL;

public class DetailPageActivity extends AppCompatActivity {

    ImageView user_head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_page);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String username = bundle.getString("user_name");
        String usertweet = bundle.getString("user_tweet");
        String userhead = bundle.getString("user_head");

        user_head = (ImageView)findViewById(R.id.user_head);
        TextView user_name = (TextView)findViewById(R.id.user_name);
        TextView user_tweet = (TextView)findViewById(R.id.user_text);

        user_name.setText(username);
        user_tweet.setText(usertweet);
        //关联好四个图片
        ImageView[] user_images = new ImageView[4];
        user_images[0] = (ImageView) findViewById(R.id.user_image1);
        user_images[1] = (ImageView) findViewById(R.id.user_image2);
        user_images[2] = (ImageView) findViewById(R.id.user_image3);
        user_images[3] = (ImageView) findViewById(R.id.user_image4);

        int imagenum = bundle.getInt("imagenum");
        //可能主线程太快了，还没execute就下一个循环了
        String[] userimages = new String[4];
        String user_image = "user_image" +String.valueOf(0);
        for(int temp=0;temp<4;temp++){
            userimages[temp]=null;
        }
        for(int i = 0;i < imagenum;i++){
            user_image ="user_image" + String.valueOf(i);
            userimages[i] = bundle.getString(user_image);
            Log.e("#######图片", userimages[i]);
        }
        for(int i = 0;i < imagenum;i++){
            Log.e("设！！！！置图片", userimages[i]);
            DownLoadHead image = new DownLoadHead(user_images[i]);
            image.execute(userimages[i]);
        }


        DownLoadHead head = new DownLoadHead(user_head);
        // holder.userhead.setImageBitmap(tweet.getUser_head());
        head.execute(userhead);

//开始设置点击事件
        user_images[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userimages[0]!=null){
                Intent intent = new Intent(DetailPageActivity.this, PicActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url",userimages[0]);
                intent.putExtras(bundle);
                startActivity(intent);
                }
            }
        });
        user_images[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userimages[1]!=null) {
                    Intent intent = new Intent(DetailPageActivity.this, PicActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("url", userimages[1]);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        user_images[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userimages[2]!=null) {
                    Intent intent = new Intent(DetailPageActivity.this, PicActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("url", userimages[2]);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        user_images[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userimages[3]!=null) {
                    Intent intent = new Intent(DetailPageActivity.this, PicActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("url", userimages[3]);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }
    class DownLoadHead extends AsyncTask<String ,Void, Bitmap> {
        private ImageView mImageView;
        String url;
        public DownLoadHead(ImageView imageView){
            mImageView = imageView;
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            url = params[0];
            Bitmap bitmap = getBitmapFromURL(url);
            // BitmapDrawable drawable = new BitmapDrawable(mContext.getResources(),bitmap);
            return  bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bimage) {
            super.onPostExecute(bimage);

            if ( mImageView != null && bimage != null){
                mImageView.setImageBitmap(bimage);
            }
        }
    }
}
