package com.example.rohit.management;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class NewOrderActivity extends AppCompatActivity {

    private TextView mTextMessage;
    Webservices webservices ;
    common commonObj;
    Button submit;
    EditText txtAdvanceAmt,txtTotalAmt,custName,contactNo;
    TextView txtBalanceAmt;
    Spinner typeofMurti;
    TextView txtBillNo,txtBillDate;
    String typeOfMurti="",customerName="",contact="",billNo="",billDate="";
    int balanceAmt=0,advanceAmt=0,totalAmt=0 ;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //mTextMessage.setText(R.string.title_home);
                    startActivity(new Intent(NewOrderActivity.this,MainActivity.class));
                    finish();
                    return true;
                case R.id.navigation_dashboard:

                    //mTextMessage.setText(R.string.title_dashboard);
                    //startActivity(new Intent(MainActivity.this,NewOrderActivity.class));
                    return true;
                case R.id.navigation_notifications:
                    //mTextMessage.setText(R.string.title_notifications);
                    startActivity(new Intent(NewOrderActivity.this,ViewAllActivity.class));
                    finish();
                    return true;

                case R.id.navigation_add_person:
                    // mTextMessage.setText("View All");
                    startActivity(new Intent(NewOrderActivity.this,AddPerson.class));
                    finish();
                    return true;

                case R.id.navigation_view_summary:
                    // mTextMessage.setText("View All");
                    startActivity(new Intent(NewOrderActivity.this,ViewSummary.class));
                    finish();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_order);

        webservices = new Webservices();
        commonObj = new common();

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_dashboard);

        txtBalanceAmt = findViewById(R.id.txtBalanceAmt);
        txtAdvanceAmt = findViewById(R.id.txtAdvanceAmt);
        txtTotalAmt = findViewById(R.id.txtTotalAmt);
        typeofMurti = findViewById(R.id.type);
        custName = findViewById(R.id.custName);
        contactNo = findViewById(R.id.contactNo);
        txtBillNo = findViewById(R.id.txtBillNo);
        txtBillDate = findViewById(R.id.txtBillDate);

        //set current date

        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String dateToStr = format.format(today);
        txtBillDate.setText(dateToStr);
        //System.out.println(dateToStr);

        //set bill no
        int randomeNo = getRandomIntegerBetweenRange(1000,999999);
        txtBillNo.setText(randomeNo+"");


        String [] types = {"lahan murti","mothi murti","medium murti"};
        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, types);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeofMurti.setAdapter(adp1);


        txtTotalAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                txtBalanceAmt.setText(charSequence);
                txtAdvanceAmt.setEnabled(true);
                try{
                    txtAdvanceAmt.setText("0");
                }
                catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        txtAdvanceAmt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(txtTotalAmt.getText().toString().isEmpty()){
                    Toast.makeText(NewOrderActivity.this,"Please fill Total Amount first",Toast.LENGTH_SHORT).show();
                    txtAdvanceAmt.setEnabled(false);
                }
                else{
                    try{
                        int bal = Integer.parseInt(txtTotalAmt.getText().toString())-Integer.parseInt(String.valueOf(charSequence));
                        if(bal < 0){
                            Toast.makeText(NewOrderActivity.this,"Please enter valid amount",Toast.LENGTH_SHORT).show();
                        }else{
                            txtBalanceAmt.setText(bal+"");
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });




        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                balanceAmt = Integer.parseInt(txtBalanceAmt.getText().toString());
                advanceAmt = Integer.parseInt(txtAdvanceAmt.getText().toString());
                totalAmt = Integer.parseInt(txtTotalAmt.getText().toString());
                customerName = custName.getText().toString();
                contact = contactNo.getText().toString();
                typeOfMurti = typeofMurti.getSelectedItem().toString();
                billNo=txtBillNo.getText().toString();
                billDate = txtBillDate.getText().toString();

                if(totalAmt >0 || !customerName.isEmpty() || !contact.isEmpty() || !typeOfMurti.isEmpty()){


                if(totalAmt >0){
                    if(advanceAmt >0 ){
                        int balAmt = totalAmt - advanceAmt;
                        if(balanceAmt != balAmt){
                            // Calculation Mistake
                            Toast.makeText(NewOrderActivity.this,"Calculation Mistake",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                            if (networkInfo != null && networkInfo.isConnected()) {
                                new NewOrderAsync().execute();
                            }
                            else{
                                commonObj.showAlertForInetnet(NewOrderActivity.this);
                            }
                        }
                    }
                    else{
                          if(balanceAmt != totalAmt){
                              // Calculation Mistake
                              Toast.makeText(NewOrderActivity.this,"Calculation Mistake",Toast.LENGTH_SHORT).show();

                          }
                          else {
                              ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                              NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                              if (networkInfo != null && networkInfo.isConnected()) {
                                  new NewOrderAsync().execute();
                              }
                              else{
                                  commonObj.showAlertForInetnet(NewOrderActivity.this);
                              }
                          }
                    }
                }

                }
                else{
                    new MaterialStyledDialog.Builder(NewOrderActivity.this)
                            .setTitle("Alert!")
                            .setDescription("Please fill required fields")
                            .setPositiveText("OK")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Log.d("MaterialStyledDialogs", "Do something!");
                                }})
                            .show();
                }




            }
        });
    }

    class NewOrderAsync extends AsyncTask<Void,Void,Void>{
        String response=null;
        ProgressDialog pd ;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd =new ProgressDialog(NewOrderActivity.this);
            pd.setMessage("Please wait... ");
            pd.show();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            response = webservices.addNewOrder("new_order",billNo,billDate,customerName,typeOfMurti,String.valueOf(totalAmt),String.valueOf(balanceAmt),String.valueOf(advanceAmt),contact,"Pending");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(pd.isShowing()){
                pd.dismiss();
            }
            if(response!=null){
                try {
                    JSONObject jObject = new JSONObject(response);
                    Log.d("response",response);
                    //txtBillNo.setText("");
                    txtAdvanceAmt.setText("");
                    txtBalanceAmt.setText("");
                    txtTotalAmt.setText("");
                   // typeofMurti.setText("");
                    contactNo.setText("");
                    custName.setText("");

                    new MaterialStyledDialog.Builder(NewOrderActivity.this)
                            .setTitle("Success!")
                            .setDescription("You have successfully inserted new entry")
                            .setPositiveText("OK")
                            .onPositive(new MaterialDialog.SingleButtonCallback() {
                                @Override
                                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                    Log.d("MaterialStyledDialogs", "Do something!");
                                    int randomeNo = getRandomIntegerBetweenRange(1000,999999);
                                    txtBillNo.setText(randomeNo+"");
                                }})
                            .show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{

            }

        }
    }

    public static int getRandomIntegerBetweenRange(int min, int max){
        int x = (int)(Math.random()*((max-min)+1))+min;
        return x;
    }



}
