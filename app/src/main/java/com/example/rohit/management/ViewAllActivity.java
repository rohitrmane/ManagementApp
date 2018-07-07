package com.example.rohit.management;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.example.rohit.management.Adapter.OrderAdapter;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ViewAllActivity extends AppCompatActivity {

    private TextView mTextMessage;
    RecyclerView orderRecyclerView;
    OrderAdapter adapter;
    LinearLayoutManager layoutManager;
    public ArrayList<OrderModel> orderList = new ArrayList<>();
    Webservices webservices ;
    common commonObj;
    ArrayList<OrderModel> pendingList = new ArrayList();
    ArrayList<OrderModel> completeList = new ArrayList();

    RadioButton allOrderRadioBtn,pendingRadioBtn,completeRadioBtn;


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //mTextMessage.setText(R.string.title_home);
                    startActivity(new Intent(ViewAllActivity.this,MainActivity.class));
                    finish();
                    return true;
                case R.id.navigation_dashboard:
                   // mTextMessage.setText(R.string.title_dashboard);
                    startActivity(new Intent(ViewAllActivity.this,NewOrderActivity.class));
                    finish();
                    return true;
                case R.id.navigation_notifications:
                   // mTextMessage.setText(R.string.title_notifications);
                    return true;
                case R.id.navigation_add_person:
                    // mTextMessage.setText("View All");
                    startActivity(new Intent(ViewAllActivity.this,AddPerson.class));
                    finish();
                    return true;

                case R.id.navigation_view_summary:
                    // mTextMessage.setText("View All");
                    startActivity(new Intent(ViewAllActivity.this,ViewSummary.class));
                    finish();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all);

        webservices = new Webservices();
        commonObj = new common();


        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_notifications);

        allOrderRadioBtn = findViewById(R.id.allOrderRadio);
        pendingRadioBtn = findViewById(R.id.pendingOrderRadio);
        completeRadioBtn = findViewById(R.id.completOrderRadio);

        orderRecyclerView =(RecyclerView)findViewById(R.id.orderRecycleView);
        layoutManager = new LinearLayoutManager(ViewAllActivity.this);
        orderRecyclerView.setLayoutManager(layoutManager);
        orderRecyclerView.setHasFixedSize(true);

//        orderList.add(new OrderModel("ST100","24th June","Rohit Mane","10000","Pending","5000","5000","9833411104","lahan murti"));
//        orderList.add(new OrderModel("ST100","24th June","Rohit Mane","10000","Pending","5000","5000","9833411104","lahan murti"));
//        orderList.add(new OrderModel("ST100","24th June","Rohit Mane","10000","Pending","5000","5000","9833411104","lahan murti"));
//        orderList.add(new OrderModel("ST100","24th June","Rohit Mane","10000","Pending","5000","5000","9833411104","lahan murti"));
//        orderList.add(new OrderModel("ST100","24th June","Rohit Mane","10000","Pending","5000","5000","9833411104","lahan murti"));

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new getAllOrderAsync().execute();
        }
        else{
            commonObj.showAlertForInetnet(ViewAllActivity.this);
        }

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            String status = bundle.getString("option");
            if(status.equals("pending")){
                pendingRadioBtn.setChecked(true);
            }
            else if(status.equals("complete")){
                completeRadioBtn.setChecked(true);
            }
            else if(status.equals("total")){
                allOrderRadioBtn.setChecked(true);
            }
        }
        else{
            allOrderRadioBtn.setChecked(true);
        }

        allOrderRadioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter = new OrderAdapter(ViewAllActivity.this, orderList);
                adapter.notifyDataSetChanged();
                orderRecyclerView.setAdapter(adapter);
            }
        });


        pendingRadioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter = new OrderAdapter(ViewAllActivity.this, pendingList);
                adapter.notifyDataSetChanged();
                orderRecyclerView.setAdapter(adapter);
            }
        });

        completeRadioBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter = new OrderAdapter(ViewAllActivity.this, completeList);
                adapter.notifyDataSetChanged();
                orderRecyclerView.setAdapter(adapter);
            }
        });


    }

    class getAllOrderAsync extends AsyncTask<Void,Void,Void> {
        String response=null;
        ProgressDialog pd ;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd =new ProgressDialog(ViewAllActivity.this);
            pd.setMessage("Please wait... ");
            pd.show();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            response = webservices.getAllOrder("get_all_order_details");
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
                    if(jObject.get("code").toString().equals("200")){
                        JSONArray jsonArray = jObject.getJSONArray("customerList");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            String id = object.get("id").toString();
                            String billNo = object.get("bill_no").toString();
                            String bill_date = object.get("bill_date").toString();
                            String customer_name = object.get("customer_name").toString();
                            String type = object.get("type").toString();
                            String contactNo = object.get("contactNo").toString();
                            String total_amount = object.get("total_amount").toString();
                            String balance_amount = object.get("balance_amount").toString();
                            String advance_amount = object.get("advance_amount").toString();
                            String status = object.get("status").toString();
                            String imgString = object.get("imageString").toString();

                            orderList.add(new OrderModel(id,billNo,bill_date,customer_name,total_amount,status,balance_amount,advance_amount,contactNo,type,imgString));

                            if(status.equalsIgnoreCase("Pending")){
                                pendingList.add(new OrderModel(id,billNo,bill_date,customer_name,total_amount,status,balance_amount,advance_amount,contactNo,type,imgString));
                            }
                            else{
                                completeList.add(new OrderModel(id,billNo,bill_date,customer_name,total_amount,status,balance_amount,advance_amount,contactNo,type,imgString));
                            }

                        }

                        adapter = new OrderAdapter(ViewAllActivity.this, orderList);
                        adapter.notifyDataSetChanged();
                        orderRecyclerView.setAdapter(adapter);
                    }
                    else{
                        Toast.makeText(ViewAllActivity.this,"No Data Present",Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(ViewAllActivity.this,"Some issue occured",Toast.LENGTH_SHORT).show();
            }

        }
    }

}
