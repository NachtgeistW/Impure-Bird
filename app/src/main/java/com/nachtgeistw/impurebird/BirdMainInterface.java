package com.nachtgeistw.impurebird;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.jakewharton.disklrucache.DiskLruCache;
import com.nachtgeistw.impurebird.Util.BaseAppCompatActivity;
import com.nachtgeistw.impurebird.Util.Util;

import java.io.IOException;
import java.io.OutputStream;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

import static com.nachtgeistw.impurebird.LoginActivity.PREF_KEY_TWITTER_LOGIN;
import static com.nachtgeistw.impurebird.LoginActivity.TWITTER_CONSUMER_KEY;
import static com.nachtgeistw.impurebird.LoginActivity.TWITTER_CONSUMER_SECRET;
import static com.nachtgeistw.impurebird.Util.Cache.downloadUrlToStream;
import static com.nachtgeistw.impurebird.Util.Cache.openDiskLruCache;
import static com.nachtgeistw.impurebird.Util.Image.SetUserAvatar;
import static com.nachtgeistw.impurebird.Util.Util.ActivityCollector;
import static com.nachtgeistw.impurebird.Util.Util.USER_AVATAR_URL;
import static com.nachtgeistw.impurebird.Util.Util.USER_NICKNAME;
import static com.nachtgeistw.impurebird.Util.Util.USER_NAME;
import static com.nachtgeistw.impurebird.Util.Util.hashKeyForDisk;
import static com.nachtgeistw.impurebird.Util.Util.utilDiskLruCache;
import static com.nachtgeistw.impurebird.Util.Util.tweet_content;
import static com.nachtgeistw.impurebird.Util.Util.utilSharedPreferences;


public class BirdMainInterface extends BaseAppCompatActivity {
    static final String PREF_KEY_OAUTH_TOKEN = "access_token";
    static final String PREF_KEY_OAUTH_SECRET = "access_token_secret";
    static final int send_tweet = 1;

    Intent intent;
    private AppBarConfiguration mAppBarConfiguration;
    public static Twitter twitter_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bird_main_interface);
        Util.ActivityCollector.addActivity(this);
        try {
            //Twitter initial
            SharedPreferences mSharedPreferences = LoginActivity.mSharedPreferences;
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
            builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
            String access_token = mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");
            String access_token_secret = mSharedPreferences.getString(PREF_KEY_OAUTH_SECRET, "");
            AccessToken accessToken = new AccessToken(access_token, access_token_secret);
            twitter_main = new TwitterFactory(builder.build()).getInstance(accessToken);

            //UI initial
            Toolbar toolbar = findViewById(R.id.toolbar);

            setSupportActionBar(toolbar);
            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener((View v) -> {
                intent = new Intent(BirdMainInterface.this, SendTweetActivity.class);
                startActivityForResult(intent, send_tweet);
            });


            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            NavigationView navigationView = findViewById(R.id.nav_view);
            View headerView = navigationView.getHeaderView(0);
            TextView userNickname = headerView.findViewById(R.id.nav_header_user_nickname);
            TextView userName = headerView.findViewById(R.id.nav_header_user_name);
            ImageView userAvatar = headerView.findViewById((R.id.nav_header_avatar));
            utilSharedPreferences = getApplicationContext().getSharedPreferences("MyPref", 0);
            utilDiskLruCache = openDiskLruCache(getApplicationContext());
            if (!utilSharedPreferences.contains(USER_NAME)) {
                userName.setText(utilSharedPreferences.getString(USER_NICKNAME, "1"));
                userNickname.setText(utilSharedPreferences.getString(USER_NAME, "2"));
            } else {
                GetUserInfo(utilSharedPreferences, utilDiskLruCache);
                userName.setText(utilSharedPreferences.getString(USER_NICKNAME, "3"));
                userNickname.setText(utilSharedPreferences.getString(USER_NAME, "4"));
            }
            SetUserAvatar(utilSharedPreferences, utilDiskLruCache, userAvatar);

            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            mAppBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.nav_home, R.id.nav_gallery)
                    .setDrawerLayout(drawer)
                    .build();
            NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
            NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, navController);

            navigationView.setCheckedItem(R.id.nav_log_out);
            navigationView.setNavigationItemSelectedListener(menuItem -> {
                // Shared Preferences
                SharedPreferences.Editor e = mSharedPreferences.edit();

                // After getting access token, access token secret
                // store them in application preferences
                e.putString(PREF_KEY_OAUTH_TOKEN, "");
                e.putString(PREF_KEY_OAUTH_SECRET, "");
                e.putBoolean(PREF_KEY_TWITTER_LOGIN, false);
                e.apply(); // save changes
                ActivityCollector.finishAll();
                return true;
            });
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

    static void GetUserInfo(SharedPreferences s, DiskLruCache d) {
        final String[] userNickname = new String[1];
        final String[] userName = new String[1];
        final String[] avatarUrl = new String[1];

        new Thread(() -> {
            try {
                userName[0] = twitter_main.verifyCredentials().getName();
                userNickname[0] = twitter_main.verifyCredentials().getScreenName();
                avatarUrl[0] = twitter_main.verifyCredentials().get400x400ProfileImageURLHttps();
                String key = hashKeyForDisk(avatarUrl[0]);
                DiskLruCache.Editor dlcEditor = d.edit(key);
                SharedPreferences.Editor spEditor = s.edit();
                if (dlcEditor != null) {
                    OutputStream outputStream = dlcEditor.newOutputStream(0);
                    boolean b = downloadUrlToStream(avatarUrl[0], outputStream);
                    if (b) {
                        spEditor.putString(USER_NAME, userName[0]);
                        spEditor.putString(USER_NICKNAME, userNickname[0]);
                        spEditor.putString(USER_AVATAR_URL, key);
                        dlcEditor.commit();
                    } else {
                        dlcEditor.abort();
                    }
                }
                d.flush();
                spEditor.apply();
            } catch (TwitterException | IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    //对通过intent返回的数据做处理（这里只有一个send_tweet）
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case send_tweet:
                if (resultCode == RESULT_OK) {
                    String return_tweet_content = data.getStringExtra(tweet_content);
                    new SendTweet(return_tweet_content).execute();
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.bird_main_interface, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    //发推的AsyncTask
    class SendTweet extends AsyncTask<Void, Integer, Boolean> {
        String tweet_context;

        SendTweet(String return_tweet_content) {
            tweet_context = return_tweet_content;
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                Log.e("Twitter", "BirdMainInterface > SendTweetActivity > doInBackground > true");
                twitter_main.updateStatus(tweet_context);
                return true;
            } catch (TwitterException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // 如何传值到result上：https://www.jianshu.com/p/817a34a5f200
            // 当doInBackground(Params...)执行完毕并通过return语句进行返回时，这个方法就很快会被调用。返回的数据会作为参数传递到此方法中，
            // **可以利用返回的数据来进行一些UI操作，在主线程中进行，比如说提醒任务执行的结果。**
            if (result) {
                Toast.makeText(BirdMainInterface.this, R.string.tweet_succeed, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), R.string.tweet_fail, Toast.LENGTH_LONG).show();
            }
        }
    }
}