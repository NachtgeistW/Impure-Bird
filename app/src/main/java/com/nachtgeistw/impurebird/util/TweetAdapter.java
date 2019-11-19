package com.nachtgeistw.impurebird.util;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import twitter4j.Status;

import com.nachtgeistw.impurebird.R;

import java.util.List;

import static com.nachtgeistw.impurebird.util.util.getBitmapFromURL;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private List<Status> mTweetList;


    static class ViewHolder extends RecyclerView.ViewHolder {
        //先定义里面要放的组件
        TextView username;
        TextView usertext;
        ImageView userhead, favourite, comment, retweet;


        ViewHolder(View view) {
            super(view);
            //关联组件
            username = itemView.findViewById(R.id.user_name);
            userhead = itemView.findViewById(R.id.user_head);
            usertext = itemView.findViewById(R.id.user_text);
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
        holder.username.setText(tweet.getUser().getName());
        holder.usertext.setText(tweet.getText());

        DownLoadHead task = new DownLoadHead(holder.userhead);
        // holder.userhead.setImageBitmap(tweet.getUser_head());
        task.execute(tweet.getUser().get400x400ProfileImageURLHttps());


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

}
