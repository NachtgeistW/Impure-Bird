package com.nachtgeistw.impurebird;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import twitter4j.Status;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<Status> mTweetList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        //先定义里面要放的组件
        TextView username;

        public ViewHolder(View view) {
            super(view);
            //关联组件
            username = view.findViewById(R.id.user_name);
        }
    }

    public TweetAdapter(List<Status> tweetlist) {
        mTweetList = tweetlist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //这里获取
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tweet_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Status tweet = mTweetList.get(position);
        holder.username.setText(tweet.getUser().getName());
    }

    @Override
    public int getItemCount() {
        return mTweetList.size();
    }

}
