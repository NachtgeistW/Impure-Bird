package com.nachtgeistw.impurebird;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.conf.ConfigurationBuilder;

import static com.nachtgeistw.impurebird.LoginActivity.TWITTER_CONSUMER_KEY;
import static com.nachtgeistw.impurebird.LoginActivity.TWITTER_CONSUMER_SECRET;


public class BirdMainInterface extends AppCompatActivity {
    static final String PREF_KEY_OAUTH_TOKEN = "access_token";
    static final String PREF_KEY_OAUTH_SECRET = "access_token_secret";


    private AppBarConfiguration mAppBarConfiguration;
    public static Twitter twitter;
    List<Status> tweetList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //Twitter initial
        SharedPreferences mSharedPreferences = LoginActivity.mSharedPreferences;
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
        builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
        String access_token = mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");
        String access_token_secret = mSharedPreferences.getString(PREF_KEY_OAUTH_SECRET, "");
        AccessToken accessToken = new AccessToken(access_token, access_token_secret);
        twitter = new TwitterFactory(builder.build()).getInstance(accessToken);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bird_main_interface);

        //UI initial
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

//            Toast.makeText(getApplicationContext(), "获取到首页推特了[表情]", Toast.LENGTH_LONG).show();
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
                Log.e("Twitter", "BirdMainInterface > SendTweet > doInBackground > true");
//                twitter4j.Status response = twitter.updateStatus("测试\n🌃⚙👼🅰🏃‍⭕\n@Nightwheel_C\nvia 不浄な白い鳥");
                twitter.updateStatus("测试🌃⚙👼🅰🏃‍⭕\n via 不浄な白い鳥");
                return true;
            } catch (TwitterException e) {
                Log.e("Twitter", "BirdMainInterface > SendTweet > doInBackground > false");
                Log.e("Twitter", e.getErrorMessage());
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // 之前报错：java.lang.NullPointerException: Attempt to invoke virtual method 'boolean java.lang.Boolean.booleanValue()' on a null object reference
            // 如何传值到result上：https://www.jianshu.com/p/817a34a5f200
            // 当doInBackground(Params...)执行完毕并通过return语句进行返回时，这个方法就很快会被调用。返回的数据会作为参数传递到此方法中，
            // **可以利用返回的数据来进行一些UI操作，在主线程中进行，比如说提醒任务执行的结果。**
            if (result) {
                Toast.makeText(getApplicationContext(), R.string.tweet_succeed, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), R.string.tweet_fail, Toast.LENGTH_LONG).show();
            }
        }
    }

}
