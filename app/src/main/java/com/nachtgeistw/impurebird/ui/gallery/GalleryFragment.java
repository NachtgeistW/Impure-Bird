package com.nachtgeistw.impurebird.ui.gallery;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import twitter4j.Status;
import twitter4j.TwitterException;

import com.nachtgeistw.impurebird.R;
import com.nachtgeistw.impurebird.util.TweetAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.nachtgeistw.impurebird.BirdMainInterface.twitter_main;

public class GalleryFragment extends Fragment {

    private List<Status> statusList = new ArrayList<>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        //关联recycler组件
        recyclerView = root.findViewById((R.id.home_timeline_recyclerview));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);
        recyclerView.setLayoutManager(layoutManager);
        //关联刷新组件
        swipeRefreshLayout = root.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.
                OnRefreshListener() {
            @Override
            public void onRefresh() {
                new PullUserTimeline().execute();
            }
        });
        //获取推特并展示
        new PullUserTimeline().execute();
        return root;
    }

    class PullUserTimeline extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(Void... voids) {
            try {
                statusList = twitter_main.getUserTimeline();
                return true;
            } catch (TwitterException e) {
                e.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            TweetAdapter adapter = new TweetAdapter(getContext(),statusList);
            recyclerView.setAdapter(adapter);
            swipeRefreshLayout.setRefreshing(false);
            if (!result) {
                Toast.makeText(getContext(), String.valueOf(R.string.pull_home_timeline_failed),
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}