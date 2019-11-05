package com.nachtgeistw.impurebird.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.twitter.sdk.android.tweetui.TweetTimelineRecyclerViewAdapter;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }

    TweetTimelineRecyclerViewAdapter recyclerViewAdapter = new TweetTimelineRecyclerViewAdapter();

}