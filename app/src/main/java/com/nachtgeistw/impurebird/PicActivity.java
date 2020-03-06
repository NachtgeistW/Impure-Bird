package com.nachtgeistw.impurebird;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import static com.nachtgeistw.impurebird.util.image.getBitmapFromURL;

public class PicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pic_output);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        assert bundle != null;
        String url = bundle.getString("url");

        ImageView pic = findViewById(R.id.pic_output);

        //task是DownLoadHead的实例
        DownLoadHead task = new DownLoadHead(pic);
        //.execute是用来启动线程的
        task.execute(url);
    }

    public class DownLoadHead extends AsyncTask<String, Void, Bitmap> {
        private ImageView mImageView;//mImageView为图片xml中的位置
        String url;

        DownLoadHead(ImageView imageView) {
            mImageView = imageView;
        }

        //获取图片url对应的bitmap
        @Override
        protected Bitmap doInBackground(String... params) {
            url = params[0];
            // BitmapDrawable drawable = new BitmapDrawable(mContext.getResources(),bitmap);
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
}
