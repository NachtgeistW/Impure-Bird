package com.nachtgeistw.impurebird;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DetailPageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String username = bundle.getString("user_name");

        TextView textView = (TextView)findViewById(R.id.user_name);
        textView.setText(username);

    }
}
