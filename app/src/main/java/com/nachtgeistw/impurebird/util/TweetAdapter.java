package com.nachtgeistw.impurebird.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nachtgeistw.impurebird.DetailPageActivity;
import com.nachtgeistw.impurebird.R;

import java.util.List;

import twitter4j.Status;
import twitter4j.TwitterException;

import static com.nachtgeistw.impurebird.BirdMainInterface.twitter;
import static com.nachtgeistw.impurebird.util.util.getBitmapFromURL;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<Status> mTweetList;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        //先定义里面要放的组件
        View tweetView;
        TextView username;
        ImageView userhead, favorite, comment, retweet;
        TextView usertext;

        ViewHolder(View view) {
            super(view);
            tweetView = view;
            //关联组件
            username = itemView.findViewById(R.id.user_name);
            userhead = itemView.findViewById(R.id.user_head);
            usertext = itemView.findViewById(R.id.user_text);
            favorite = itemView.findViewById(R.id.icon_not_favorite);
            comment = itemView.findViewById(R.id.icon_not_comment);
            retweet = itemView.findViewById(R.id.icon_not_retweet);
        }
    }

    public TweetAdapter(Context context, List<Status> tweetlist) {
        this.context = context;
        this.mTweetList = tweetlist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //这里获取
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tweet_item, parent, false);

        //设置点击事件
        final ViewHolder holder = new ViewHolder(view);
        //点击除了图片以外的地方都跳到详情界面
        holder.tweetView.setOnClickListener(v -> {
            //从mTweetList中找出点击的是下标是几的然后通过intent传数据并跳转
            int position = holder.getAdapterPosition();
            Status status = mTweetList.get(position);

            Intent intent = new Intent(context, DetailPageActivity.class);
            Bundle bundle = new Bundle();

            //这里往bundle放详细页面所需要的数据
            bundle.putString("user_name", status.getUser().getName());
            //还要再放其他东西

            //整个bundle塞进去
            intent.putExtras(bundle);

            context.startActivity(intent);
        });

        // 转发的click listener
        holder.retweet.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            Status status = mTweetList.get(position);
            new SetStatusRetweet(holder.retweet, status).execute(position);
        });

        // 点赞的click listener
        holder.favorite.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            Status status = mTweetList.get(position);
            new SetStatusFavorite(holder.favorite, status).execute(position);
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Status tweet = mTweetList.get(position);

        holder.username.setText(tweet.getUser().getName());
        holder.usertext.setText(tweet.getText());

        //设置头像、RT和赞
        new DownLoadHead(holder.userhead).execute(tweet.getUser().get400x400ProfileImageURLHttps());
        new InitStatusFavorite(holder.favorite).execute(tweet);
        new InitStatusRetweet(holder.retweet).execute(tweet);
    }

    @Override
    public int getItemCount() {
        return mTweetList.size();
    }

    class DownLoadHead extends AsyncTask<String, Void, Bitmap> {
        private ImageView mImageView;
        String url;

        public DownLoadHead(ImageView imageView) {
            mImageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            url = params[0];
            Bitmap bitmap = downLoadBitmap(url);
            return bitmap;
        }

        private Bitmap downLoadBitmap(String url) {
            Bitmap bimage = getBitmapFromURL(url);
            return bimage;
        }

        @Override
        protected void onPostExecute(Bitmap bimage) {
            super.onPostExecute(bimage);

            if (mImageView != null && bimage != null) {
                mImageView.setImageBitmap(bimage);
            }
        }
    }

    // 设置赞的初始状态
    class InitStatusFavorite extends AsyncTask<twitter4j.Status, Void, Boolean> {
        ImageView favorite;
        twitter4j.Status status;

        InitStatusFavorite(ImageView imageView) {
            this.favorite = imageView;
        }

        @Override
        protected Boolean doInBackground(twitter4j.Status... statuses) {
            status = statuses[0];
            // 没点过赞
            return status.isFavorited();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result) {
                favorite.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_not_favorite));
            } else {
                favorite.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorited));
            }
        }
    }

    // 设置转发的初始状态
    class InitStatusRetweet extends AsyncTask<twitter4j.Status, Void, Boolean> {
        ImageView retweet;

        InitStatusRetweet(ImageView imageView) {
            this.retweet = imageView;
        }

        @Override
        protected Boolean doInBackground(twitter4j.Status... statuses) {
            // 没点过赞
            return statuses[0].isRetweetedByMe();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (!result) {
                retweet.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_not_repeat));
            } else {
                retweet.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_repeat));
            }
        }
    }

    // 转发推文
    // 只有下拉刷新一遍才能转发，没有办法的事情
    class SetStatusRetweet extends AsyncTask<Integer, Void, Integer> {
        ImageView retweet;
        twitter4j.Status status;
        final int is_retweeted = 1, is_not_favorited = 0, error = -1;

        SetStatusRetweet(ImageView imageView, twitter4j.Status status) {
            this.retweet = imageView;
            this.status = status;
        }

        @Override
        protected Integer doInBackground(Integer... positions) {
            Log.i("Twitter", "SetStatusFavorite > doInBackground");
            // 点过了
            if (status.isRetweetedByMe()) {
                try {
                    Log.i("Twitter", "SetStatusFavorite > is retweeted");
                    mTweetList.set(positions[0], twitter.unRetweetStatus(status.getId()));
                    return is_retweeted;
                } catch (TwitterException e) {
                    e.printStackTrace();
                    return error;
                }
            }
            // 没点过赞
            else
                try {
                    Log.i("Twitter", "SetStatusFavorite > is not retweeted");
                    mTweetList.set(positions[0], twitter.retweetStatus(status.getId()));
                    return is_not_favorited;

                } catch (TwitterException e) {
                    e.printStackTrace();
                    return error;
                }
        }


        @Override
        protected void onPostExecute(Integer result) {
            switch (result) {
                case is_retweeted: {
                    retweet.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_not_repeat));
                    Log.i("Twitter", "SetStatusRetweet > retweet canceled");
                    break;
                }
                case is_not_favorited: {
                    retweet.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_repeat));
                    Log.i("Twitter", "SetStatusRetweet > retweeted");
                    break;
                }
                default:
            }
        }
    }

    // 点赞
    class SetStatusFavorite extends AsyncTask<Integer, Void, Integer> {
        ImageView favorite;
        twitter4j.Status status;
        final int is_favorited = 1, is_not_favorited = 0, error = -1;

        SetStatusFavorite(ImageView imageView, twitter4j.Status status) {
            this.favorite = imageView;
            this.status = status;
        }

        @Override
        protected Integer doInBackground(Integer... positions) {
            Log.i("Twitter", "SetStatusFavorite > doInBackground");
            // 没点过赞
            if (status.isFavorited()) {
                try {
                    Log.i("Twitter", "SetStatusFavorite > is favorited");
                    mTweetList.set(positions[0], twitter.destroyFavorite(status.getId()));
                    return is_favorited;
                } catch (TwitterException e) {
                    e.printStackTrace();
                    return error;
                }
            }
            // 点过了
            else
                try {
                    Log.i("Twitter", "SetStatusFavorite > is not favorited");
                    mTweetList.set(positions[0], twitter.createFavorite(status.getId()));
                    // 这里要返回的结果其实是status.isFavorited()的，
                    // 为了在后面 onPostExecute 时判断有没有点过赞，进一步修改点赞状态
                    return is_not_favorited;

                } catch (TwitterException e) {
                    e.printStackTrace();
                    return error;
                }
        }


        @Override
        protected void onPostExecute(Integer result) {
            switch (result) {
                case is_favorited: {
                    favorite.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_not_favorite));
                    Log.i("Twitter", "SetStatusRetweet > favorite canceled");
                    break;
                }
                case is_not_favorited: {
                    favorite.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_favorited));
                    Log.i("Twitter", "SetStatusRetweet > favorited");
                    break;
                }
                default:
            }
        }
    }
}
//https://www.itranslater.com/qa/details/2325690476025873408
//java - 从Android中的AsyncTask返回一个值