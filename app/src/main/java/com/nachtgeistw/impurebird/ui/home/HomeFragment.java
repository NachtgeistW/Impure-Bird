package com.nachtgeistw.impurebird.ui.home;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nachtgeistw.impurebird.BirdMainInterface;
import com.nachtgeistw.impurebird.R;
import com.nachtgeistw.impurebird.TweetAdapter;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;

import static com.nachtgeistw.impurebird.BirdMainInterface.twitter;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    List<Status> tweetList = new ArrayList<>();
    String user = null;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        RecyclerView recyclerView = root.findViewById(R.id.home_timeline_recyclerview);

        //Twitter 用户。这个应该在Profile里的
        //https://www.cnblogs.com/zyanrong/p/5415626.html

        //获取推特并展示
        Log.e("Twitter", "BirdMainInterface > PullUserTimeline");
        new PullUserTimeline().execute();

        //获取完放进layout里
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        TweetAdapter adapter = new TweetAdapter(tweetList);
        recyclerView.setAdapter(adapter);
        return root;
    }

    class PullUserTimeline extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                tweetList = twitter.getHomeTimeline();
                return true;
            } catch (TwitterException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                Toast.makeText(getContext(), "获取到首页推特了[表情]", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "妹有网络获取不到首页推特哦(⊙o⊙)？", Toast.LENGTH_LONG).show();
            }
        }
    }
}