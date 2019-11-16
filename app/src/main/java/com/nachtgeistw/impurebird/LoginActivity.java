package com.nachtgeistw.impurebird;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
    // cosumer
    // key here
    static String TWITTER_CONSUMER_SECRET = "cS5v9H3K4bbNtO3KDQ9il6eVnYZkcQfEcWeQ30KG8QWfiIwL7D"; // place

    // Preference Constants
    static String PREFERENCE_NAME = "twitter_oauth";
    static final String PREF_KEY_OAUTH_TOKEN = "access_token";
    static final String PREF_KEY_OAUTH_SECRET = "access_token_secret";
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
    public static SharedPreferences mSharedPreferences;

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
                    new AfterLoginTask().execute(url);
                else
                    webView.loadUrl(url);
                return true;
            }
        });

        // Shared Preferences
        mSharedPreferences = getApplicationContext().getSharedPreferences(
                "MyPref", 0);

        /**
         * Twitter login button click event will call loginToTwitter() function
         * */
        btnShareTwitter.setOnClickListener(arg0 -> {
            Log.e("Twitter", "LoginActivity > onCreate > setOnClickListener");
            // Call login twitter function
            new LoginTask().execute();
        });
    }

    /**
     * Function to login twitter
     */
    private void loginToTwitter() {
        Log.e("Twitter", "LoginActivity > loginToTwitter");
        // Check if already logged in
        if (!isTwitterLoggedInAlready()) {
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
            Intent intent = new Intent(LoginActivity.this, BirdMainInterface.class);
//            String token = mSharedPreferences.getString(PREF_KEY_OAUTH_TOKEN, "");
//            String secret = mSharedPreferences.getString(PREF_KEY_OAUTH_SECRET, "");
//            intent.putExtra(PREF_KEY_OAUTH_TOKEN, token);
//            intent.putExtra(PREF_KEY_OAUTH_SECRET, secret);
            startActivity(intent);
        }
    }

    /**
     * Check user already logged in your application using twitter Login flag is
     * fetched from Shared Preferences
     */
    private boolean isTwitterLoggedInAlready() {
        // return twitter login status from Shared Preferences
        return mSharedPreferences.getBoolean(PREF_KEY_TWITTER_LOGIN, false);
    }

    public void handleTwitterCallback(String url) {
        Log.e("Twitter", "LoginActivity > handleTwitterCallback");
        Uri uri = Uri.parse(url);

        // oAuth verifier
        final String verifier = uri
                .getQueryParameter(URL_TWITTER_OAUTH_VERIFIER);

        try {
            // Get the access token
            LoginActivity.this.accessToken = twitter.getOAuthAccessToken(
                    requestToken, verifier);

            // Shared Preferences
            SharedPreferences.Editor e = mSharedPreferences.edit();

            // After getting access token, access token secret
            // store them in application preferences
            e.putString(PREF_KEY_OAUTH_TOKEN, accessToken.getToken());
            e.putString(PREF_KEY_OAUTH_SECRET, accessToken.getTokenSecret());
            // Store login status - true
            e.putBoolean(PREF_KEY_TWITTER_LOGIN, true);
            e.apply(); // save changes

            Intent intent = new Intent(LoginActivity.this, BirdMainInterface.class);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class LoginTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... params) {
            Log.e("Twitter", "LoginActivity > LoginTask > doInBackground");

            loginToTwitter();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            Log.e("Twitter", "LoginActivity > LoginTask > onPostExecute");
            try {
                myWebView.loadUrl(requestToken.getAuthenticationURL());
                myWebView.setVisibility(View.VISIBLE);
                myWebView.requestFocus(View.FOCUS_DOWN);
            } catch (Exception e) {
                // 获取不到 AuthenticationURL 时抛出异常的 Toast。
                // 在中国大陆这种事情太正常了，不包会闪退
                // 登陆不上不关本组的事
                Log.e("Twitter", e.getMessage());
                if (!isTwitterLoggedInAlready())
                    Toast.makeText(getApplicationContext(), R.string.get_auth_url_fail, Toast.LENGTH_LONG).show();
            }
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
            handleTwitterCallback(params[0]);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            // TODO Auto-generated method stub
            myWebView.setVisibility(View.GONE);
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