package com.nachtgeistw.impurebird.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nachtgeistw.impurebird.R;
import com.nachtgeistw.impurebird.ui.home.Tweets;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import twitter4j.Status;

import static com.nachtgeistw.impurebird.util.util.getBitmapFromURL;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<Status> mTweetList;

    public TweetAdapter(List<Status> tweetlist) {

        this.mTweetList = tweetlist;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        //先定义里面要放的组件
        //tweet_item 中的控件
        TextView username;
        TextView usertext;
        ImageView userhead;

        ViewHolder(View view) {
            super(view);
            //关联组件
            username = itemView.findViewById(R.id.user_name);
            usertext = itemView.findViewById(R.id.user_text);
            userhead = itemView.findViewById(R.id.user_head);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Status tweet = mTweetList.get(position);//在一组15条的TweetList，分别提出来，一条条赋值给xml

        holder.username.setText(tweet.getUser().getName());
        holder.usertext.setText(tweet.getText());

        class setuserhead extends AsyncTask<Void, Integer, Boolean> {
            Bitmap pngBM = null;

            @Override
            protected Boolean doInBackground(Void... voids) {
                //Log.i("Twitter", "HomeFragment > PullHomeTimeline");
                try {
                    String str = tweet.getUser().get400x400ProfileImageURL();
                    pngBM = getBitmapFromURL(str);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (!result) {
                    holder.userhead.setImageBitmap(pngBM);
                    Log.i("Twitter", "@@@@@@@@@@@@@@@"+tweet.getUser().getName());
                }
            }
        }
        new setuserhead().execute();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //这里获取
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tweet_item, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public int getItemCount() {
        return mTweetList.size();
    }
}
