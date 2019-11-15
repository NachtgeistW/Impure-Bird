package com.nachtgeistw.impurebird.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nachtgeistw.impurebird.R;
import com.twitter.sdk.android.tweetui.FixedTweetTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineRecyclerViewAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        RecyclerView recyclerView = root.findViewById(R.id.home_timeline_recyclerview);

        //Twitter 用户。这个应该在Profile里的
        //https://www.cnblogs.com/zyanrong/p/5415626.html

        //应该是Home Timeline
        //https://www.reddit.com/r/iOSProgramming/comments/3ms2l1/anyone_using_twitterkit_is_there_a_way_to_embed_a/
//        String userName = getActivity().getIntent().getStringExtra("user_name");
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//        final UserTimeline userTimeline = new UserTimeline.Builder().screenName(userName).build();
//        final TweetTimelineRecyclerViewAdapter adapter = new TweetTimelineRecyclerViewAdapter.Builder(getActivity())
//                .setTimeline(userTimeline)
//                .setViewStyle(R.style.tw__TweetLightWithActionsStyle)
//                .build();
//        recyclerView.setAdapter(adapter);
        return root;
    }
}