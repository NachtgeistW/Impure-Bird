package com.nachtgeistw.impurebird;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.nachtgeistw.impurebird.Util.Image.DownloadPic;

import java.util.Objects;

import twitter4j.TwitterException;

import static com.nachtgeistw.impurebird.BirdMainInterface.twitter_main;

public class DetailPageActivity extends AppCompatActivity {
    ImageView like = null;
    ImageView comment = null;
    ImageView retweet = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_page_with_four_pics);

        //UI initial
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        Long tweetId = bundle.getLong("tweet_id");
        Boolean isRtByMe = bundle.getBoolean("is_rt_by_me");
        Boolean isLikeByMe = bundle.getBoolean("is_like_by_me");

        //Set user name, tweet text and user avatar
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

            //Maybe main thread is so fast that it doesn't execute and get into next circulation
            String[] picURL = new String[4];
            for (int i = 0; i < 4; i++) {
                picURL[i] = null;
            }
            for (int i = 0; i < picNum; i++) {
                picURL[i] = bundle.getString("user_image" + i);
                DownloadPic tweetPic = new DownloadPic(picList[i]);
                tweetPic.execute(picURL[i]);
            }

            //start setting pictures click event
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

        like = findViewById(R.id.detail_page_not_like);
        comment = findViewById(R.id.detail_page_not_comment);
        retweet = findViewById(R.id.detail_page_not_rt);

        like.setOnClickListener(v -> {
            Log.i("Twitter", "DetailPage > like.setOnClickListener");
            new SetStatusLike(tweetId, isLikeByMe).execute();
        });
    }

    //Toolbar event: return
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    // favorite event
    class SetStatusLike extends AsyncTask<Void, Void, Integer> {
        long tweetId;
        boolean isFavoritedByMe;
        final int is_favorited = 1;
        final int is_not_favorited = 0;
        final int error = -1;

        SetStatusLike(Long tweetId, Boolean isFavoritedByMe) {
            this.tweetId = tweetId;
            this.isFavoritedByMe = isFavoritedByMe;
        }

        @Override
        protected Integer doInBackground(Void... params) {
            Log.i("Twitter", "DetailPage > SetStatusFavorite > doInBackground");
            // favorited
            if (isFavoritedByMe) {
                try {
                    Log.i("Twitter", "SetStatusFavorite > is favorited");
                    twitter_main.destroyFavorite(tweetId);
                    boolean result = twitter_main.showStatus(tweetId).isFavorited();
                    Log.i("Twitter", "SetStatusFavorite > " + result);
                    return (result ? is_favorited : is_not_favorited);
                } catch (TwitterException e) {
                    e.printStackTrace();
                    return error;
                }
            }
            // not favorited
            else {
                try {
                    Log.i("Twitter", "SetStatusFavorite > is not favorited");
                    twitter_main.createFavorite(tweetId);
                    boolean result = twitter_main.showStatus(tweetId).isFavorited();
                    Log.i("Twitter", "SetStatusFavorite > " + result);
                    return (result ? is_favorited : is_not_favorited);
                } catch (TwitterException e) {
                    e.printStackTrace();
                    return error;
                }
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            switch (result) {
                case is_favorited: {
                    like.setImageResource(R.drawable.ic_not_favorite);
                    Log.i("Twitter", "SetStatusFavorite > favorite canceled");
                    break;
                }
                case is_not_favorited: {
                    like.setImageResource(R.drawable.ic_favorited);
                    Log.i("Twitter", "SetStatusFavorite > favorited");
                    break;
                }
                default:
            }
        }
    }
}