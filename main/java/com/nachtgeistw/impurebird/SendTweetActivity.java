// https://yq.aliyun.com/articles/36012
// 标题栏顶部最右边创建按钮的方法
package com.nachtgeistw.impurebird;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import static com.nachtgeistw.impurebird.util.util.tweet_content;


public class SendTweetActivity extends AppCompatActivity {

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_tweet);
        editText = findViewById(R.id.edit_text_tweet_info);
    }

    //把按钮（菜单）设置到标题栏
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.send_tweet_main, menu);
        return true;
    }

    //监听菜单按钮
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_send) {
            String tweet = editText.getText().toString();
            Intent intent = new Intent();
            intent.putExtra(tweet_content, tweet);
            //把获取到的推文内容传回上级Activity
            setResult(RESULT_OK, intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}