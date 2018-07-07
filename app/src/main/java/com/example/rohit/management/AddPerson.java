package com.example.rohit.management;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;

public class AddPerson extends AppCompatActivity {

    TextView mTextMessage;
    TextInputEditText personName,conatactNo,username,password ;
    RadioButton userRadio,adminRadio;
    Button addBtn;


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
                    startActivity(new Intent(AddPerson.this,NewOrderActivity.class));
                    finish();
                    return true;
                case R.id.navigation_notifications:
                    // mTextMessage.setText("View All");
                    startActivity(new Intent(AddPerson.this,ViewAllActivity.class));
                    finish();
                    return true;

                case R.id.navigation_add_person:
                    // mTextMessage.setText("View All");
                    return true;

                case R.id.navigation_view_summary:
                    // mTextMessage.setText("View All");
                    startActivity(new Intent(AddPerson.this,ViewSummary.class));
                    finish();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_person);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_add_person);

        personName= (TextInputEditText) findViewById(R.id.person_name);
        conatactNo = (TextInputEditText)findViewById(R.id.contactNo);
        username = (TextInputEditText)findViewById(R.id.username);
        password = (TextInputEditText) findViewById(R.id.password);
        adminRadio = (RadioButton) findViewById(R.id.adminRadio);
        userRadio = (RadioButton)findViewById(R.id.userRadio);
        addBtn = (Button)findViewById(R.id.btnAddPerson);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(personName.getText().toString().isEmpty() || conatactNo.getText().toString().isEmpty() || username.getText().toString().isEmpty() || password.getText().toString().isEmpty()  ){
                    new MaterialStyledDialog.Builder(AddPerson.this)
                            .setTitle("Alert!")
                            .setDescription("Please fill all required fields")
                            .setPositiveText("OK")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Log.d("MaterialStyledDialogs", "Do something!");
                                }})
                            .show();

                }
                else if(!adminRadio.isChecked() && !userRadio.isChecked()){
                    new MaterialStyledDialog.Builder(AddPerson.this)
                            .setTitle("Alert!")
                            .setDescription("Please select user role")
                            .setPositiveText("OK")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Log.d("MaterialStyledDialogs", "Do something!");
                                }})
                            .show();
                }
                else if(conatactNo.getText().toString().length() != 10){
                    new MaterialStyledDialog.Builder(AddPerson.this)
                            .setTitle("Alert!")
                            .setDescription("Please enter valid 10 digit number")
                            .setPositiveText("OK")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Log.d("MaterialStyledDialogs", "Do something!");
                                }})
                            .show();
                }
                else{
                    String msg = "Your username:"+username.getText().toString() +" & password: "+username.getText().toString();
                    sendSMS(conatactNo.getText().toString(),msg);

                    //call webservice
                }
            }
        });

    }




    public void sendSMS(String phoneNo, String msg) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(),ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
}
