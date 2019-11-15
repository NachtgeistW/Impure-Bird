package com.nachtgeistw.impurebird;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;


public class BirdMainInterface extends AppCompatActivity {
    static final String PREF_KEY_OAUTH_TOKEN = "access_token";
    static final String PREF_KEY_OAUTH_SECRET = "access_token_secret";
    static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";


    private AppBarConfiguration mAppBarConfiguration;
    //Twitter 初始化
    Intent intent;
    String access_token, access_token_secret;
    ConfigurationBuilder builder = new ConfigurationBuilder();
    AccessToken accessToken;
    Twitter twitter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bird_main_interface);

        intent = getIntent();
        access_token = intent.getStringExtra(PREF_KEY_OAUTH_TOKEN);
        access_token_secret = intent.getStringExtra(PREF_KEY_OAUTH_SECRET);
        Log.e("Twitter", "onCreate: access_token: " + access_token);
        builder.setOAuthConsumerKey(String.valueOf(R.string.consumer_key));
        builder.setOAuthConsumerSecret(String.valueOf(R.string.consumer_secret));
        accessToken = new AccessToken(access_token, access_token_secret);
        twitter = new TwitterFactory(builder.build()).getInstance(accessToken);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> new SendTweet().execute());

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_setting, R.id.nav_share, R.id.nav_send)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
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

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
//                twitter4j.Status response = twitter.updateStatus("测试🌃⚙👼🅰🏃‍⭕\n via 不浄な白い鳥");
                twitter.updateStatus("测试🌃⚙👼🅰🏃‍⭕\n via 不浄な白い鳥");
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Looper.prepare();
                Toast.makeText(getApplicationContext(), R.string.tweet_succeed, Toast.LENGTH_SHORT).show();
                Looper.loop();
            } else {
                Looper.prepare();
                Toast.makeText(getApplicationContext(), R.string.tweet_fail, Toast.LENGTH_SHORT).show();
                Looper.loop();

            }
        }
    }

}
