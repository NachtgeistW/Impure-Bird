package com.nachtgeistw.impurebird;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.TwitterException;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.nachtgeistw.impurebird.util.TweetAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.nachtgeistw.impurebird.BirdMainInterface.twitter_main;

public class UserHomeActivity extends AppCompatActivity {

    private List<Status> statusList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_gallery);
        Intent intent = getIntent();
        userId = intent.getLongExtra("userId", 0);
        System.out.println(userId);
        //关联recycler组件
        recyclerView = findViewById((R.id.home_timeline_recyclerview));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);
        recyclerView.setLayoutManager(layoutManager);
        //关联刷新组件
        swipeRefreshLayout = findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(() -> new PullUserTimeline().execute());
        //获取推特并展示
        new PullUserTimeline().execute();

    }

    class PullUserTimeline extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                statusList = twitter_main.getUserTimeline(userId,new Paging(1, 50));
                Log.e("status",statusList.get(1).getUser().getName());
                return true;
            } catch (TwitterException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            TweetAdapter adapter = new TweetAdapter(getApplicationContext(),statusList);
            recyclerView.setAdapter(adapter);
            swipeRefreshLayout.setRefreshing(false);
            if (!result) {
            }
        }
    }
}
