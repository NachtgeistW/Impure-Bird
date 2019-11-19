package com.nachtgeistw.impurebird.ui.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.nachtgeistw.impurebird.R;
import com.nachtgeistw.impurebird.util.TweetAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import twitter4j.Status;
import twitter4j.TwitterException;

import static com.nachtgeistw.impurebird.BirdMainInterface.twitter;
import static com.nachtgeistw.impurebird.util.util.getBitmapFromURL;
import static java.lang.Thread.sleep;

public class HomeFragment extends Fragment {

    private List<Status> statusList = new ArrayList<>();
    private int showTweetNum = 20;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //关联recycler组件
        recyclerView = root.findViewById((R.id.home_timeline_recyclerview));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        //关联刷新组件
        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(() -> new PullHomeTimeline().execute());
        //获取推特并展示
        new PullHomeTimeline().execute();
        return root;

    }

    class PullHomeTimeline extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                statusList = twitter.getHomeTimeline();
                return true;
            } catch (TwitterException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            TweetAdapter adapter = new TweetAdapter(statusList);
            recyclerView.setAdapter(adapter);
            swipeRefreshLayout.setRefreshing(false);
            if (!result) {
                Toast.makeText(getContext(), String.valueOf(R.string.pull_home_timeline_failed),
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}