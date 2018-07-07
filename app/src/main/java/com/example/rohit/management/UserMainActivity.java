package com.example.rohit.management;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class UserMainActivity extends AppCompatActivity {
    private TextView mTextMessage;
    Webservices webservices ;
    common commonObj;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //mTextMessage.setText(R.string.title_home);
                    startActivity(new Intent(UserMainActivity.this,UserMainActivity.class));
                    finish();
                    return true;
                case R.id.navigation_dashboard:
                    // mTextMessage.setText(R.string.title_dashboard);
                    startActivity(new Intent(UserMainActivity.this,UserMainActivity.class));
                    finish();
                    return true;
                case R.id.navigation_notifications:
                    // mTextMessage.setText(R.string.title_notifications);
                    return true;
                case R.id.navigation_add_person:
                    // mTextMessage.setText("View All");
                    startActivity(new Intent(UserMainActivity.this,UserMainActivity.class));
                    finish();
                    return true;

                case R.id.navigation_view_summary:
                    // mTextMessage.setText("View All");
                    startActivity(new Intent(UserMainActivity.this,UserMainActivity.class));
                    finish();
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);

        webservices = new Webservices();
        commonObj = new common();

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_notifications);
    }
}
