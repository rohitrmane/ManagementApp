package com.example.rohit.management;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

public class ViewSummary extends AppCompatActivity {

    TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //  mTextMessage.setText(R.string.title_home);
                    return true;
                case R.id.navigation_dashboard:
                    //  mTextMessage.setText("New Order");
                    startActivity(new Intent(ViewSummary.this,NewOrderActivity.class));
                    finish();
                    return true;
                case R.id.navigation_notifications:
                    // mTextMessage.setText("View All");
                    startActivity(new Intent(ViewSummary.this,ViewAllActivity.class));
                    finish();
                    return true;

                case R.id.navigation_add_person:
                    // mTextMessage.setText("View All");
                    startActivity(new Intent(ViewSummary.this,AddPerson.class));
                    finish();
                    return true;

                case R.id.navigation_view_summary:
                    // mTextMessage.setText("View All");
                    return true;
            }
            return false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_summary);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_view_summary);
    }
}
