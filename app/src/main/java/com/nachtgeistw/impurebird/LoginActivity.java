package com.nachtgeistw.impurebird;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class LoginActivity extends Activity {
// Constants
    /**
     * Register your here app https://dev.twitter.com/apps/new and get your
     * consumer key and secret
     */
    static String TWITTER_CONSUMER_KEY = "3NBkXtqQHAVbEgPHXnIM09577"; // place your
    // consumer
    // key here
    static String TWITTER_CONSUMER_SECRET = "cS5v9H3K4bbNtO3KDQ9il6eVnYZkcQfEcWeQ30KG8QWfiIwL7D"; // place

    // Preference Constants
    static String PREFERENCE_NAME = "twitter_oauth";
    static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    static final String PREF_KEY_TWITTER_LOGIN = "isTwitterLogedIn";

    static final String TWITTER_CALLBACK_URL = "https://project-8519450044196476085.firebaseapp.com/__/auth/handler";

    // Twitter oauth urls
    static final String URL_TWITTER_AUTH = "auth_url";
    static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
    static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";

    // Login button
    Button btnShareTwitter;
    WebView myWebView;

    // Twitter
    private static Twitter twitter;
    private static RequestToken requestToken;
    private AccessToken accessToken;

    // Shared Preferences
    private static SharedPreferences tSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // All UI elements
        btnShareTwitter = findViewById(R.id.btnShareTwitter);
        myWebView = findViewById(R.id.loginWebView);
        myWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                if (url != null && url.startsWith(TWITTER_CALLBACK_URL))
                    //开一个线程进行推特登录和发推操作
                    new AfterLoginTask().execute(url);
                else
                    webView.loadUrl(url);
                return true;
            }
        });

        // Shared Preferences
        tSharedPreferences = getApplicationContext().getSharedPreferences(
                "MyPref", 0);

        //Twitter login button click event will call loginToTwitter() function
        btnShareTwitter.setOnClickListener(arg0 -> {
            // Call login twitter function
            new LoginTask().execute();
        });

    }

    /**
     * Function to login twitter
     */
    private void loginToTwitter() {
        // Check if already logged in
        if (!isTwitterAlreadyLoggedIn()) {
            //获取推特的授权
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
            builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);
            Configuration configuration = builder.build();

            TwitterFactory factory = new TwitterFactory(configuration);
            twitter = factory.getInstance();
            try {
                requestToken = twitter.getOAuthRequestToken(TWITTER_CALLBACK_URL);
            } catch (TwitterException e) {
                e.printStackTrace();
            }
        } else {
            Looper.prepare();
            // user already logged into twitter
            Toast.makeText(getApplicationContext(),
                    "Already Logged into twitter", Toast.LENGTH_LONG).show();
            Looper.loop();
//            Uri uri = Uri.parse(TWITTER_CALLBACK_URL);
//
//            // oAuth verifier
//            final String verifier = uri.getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);
//            try {
//                LoginActivity.this.accessToken = twitter.getOAuthAccessToken(requestToken, verifier);
//            } catch (TwitterException e) {
//                e.printStackTrace();
//            }
//            // Access Token
//            Looper.prepare();
//            Toast.makeText(getApplicationContext(), accessToken.getToken(), Toast.LENGTH_SHORT).show();
//            // Access Token Secret
//            Toast.makeText(getApplicationContext(), accessToken.getTokenSecret(), Toast.LENGTH_SHORT).show();
//            Looper.loop();
        }
    }

    /**
     * Check user already logged in your application using twitter Login flag is
     * fetched from Shared Preferences
     */
    private boolean isTwitterAlreadyLoggedIn() {
        // return twitter login status from Shared Preferences
        return tSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
    }

    public void handleTwitterCallback() {
        Uri uri = Uri.parse(TWITTER_CALLBACK_URL);

        // oAuth verifier
        final String verifier = uri.getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);

        try {
            // Get the access token
            LoginActivity.this.accessToken = twitter.getOAuthAccessToken(requestToken, verifier);

            // Shared Preferences
            SharedPreferences.Editor e = tSharedPreferences.edit();

            // After getting access token, access token secret
            // store them in application preferences
            e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
            e.putString(PREF_KEY_OAUTH_SECRET, accessToken.getTokenSecret());
            // 存储登陆状态：true
            e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
            e.apply(); // save changes

            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.setOAuthConsumerKey(TWITTER_CONSUMER_KEY);
            builder.setOAuthConsumerSecret(TWITTER_CONSUMER_SECRET);

            // Access Token
            String access_token = tSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");
            // Access Token Secret
            String access_token_secret = tSharedPreferences.getString(PREF_KEY_OAUTH_SECRET, "");

            AccessToken accessToken = new AccessToken(access_token, access_token_secret);
            Twitter twitter = new TwitterFactory(builder.build()).getInstance(accessToken);
//             Update status
            twitter4j.Status response = twitter.updateStatus("测试🌃⚙👼🅰🏃‍⭕\nvia 不浄な白い鳥");

//            Looper.prepare();
//            Toast.makeText(getApplicationContext(), accessToken.getToken(), Toast.LENGTH_SHORT).show();
//            Toast.makeText(getApplicationContext(), accessToken.getTokenSecret(), Toast.LENGTH_SHORT).show();
//            Looper.loop();
//            Intent intent = new Intent(LoginActivity.this, BirdMainInterface.class);
//            intent.putExtra("access_token", access_token);
//            intent.putExtra("access_token_secret", access_token_secret);
//            startActivity(intent);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class LoginTask extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected Boolean doInBackground(Void... params) {
            loginToTwitter();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            myWebView.loadUrl(requestToken.getAuthenticationURL());
            myWebView.setVisibility(View.VISIBLE);
            myWebView.requestFocus(View.FOCUS_DOWN);
        }
    }

    class AfterLoginTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            myWebView.clearHistory();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            // TODO Auto-generated method stub
            handleTwitterCallback();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            myWebView.setVisibility(View.GONE);
            Toast.makeText(LoginActivity.this, "Tweet Successful", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        if (myWebView.getVisibility() == View.VISIBLE) {
            if (myWebView.canGoBack()) {
                myWebView.goBack();
                return;
            } else {
                myWebView.setVisibility(View.GONE);
                return;
            }
        }
        super.onBackPressed();
    }
}