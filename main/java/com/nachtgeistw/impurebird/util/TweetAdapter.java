package com.nachtgeistw.impurebird.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import twitter4j.Status;

import com.nachtgeistw.impurebird.R;
import com.nachtgeistw.impurebird.ui.home.Tweets;

import java.util.List;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<Status> mTweetList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        //先定义里面要放的组件
        TextView username;

        ViewHolder(View view) {
            super(view);
            //关联组件
            username = itemView.findViewById(R.id.user_name);
        }
    }

    public TweetAdapter(List<Status> tweetlist) {

        this.mTweetList = tweetlist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //这里获取
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tweet_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Status tweet = mTweetList.get(position);
        holder.username.setText(tweet.getText());
    }

    @Override
    public int getItemCount() {
        return mTweetList.size();
    }
}
