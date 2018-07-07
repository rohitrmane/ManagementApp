package com.example.rohit.management;

import android.app.ActivityOptions;
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
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rohit.management.Adapter.OrderAdapter;
import com.example.rohit.management.cards.CardSliderLayoutManager;
import com.example.rohit.management.cards.CardSnapHelper;
import com.example.rohit.management.cards.SliderAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView mTextMessage,completOrderCount,pendingOrderCount,totalOrderCount;
    RecyclerView orderRecyclerView;
    OrderAdapter adapter;
    LinearLayoutManager layoutManager;
    public ArrayList<OrderModel> orderList = new ArrayList<>();
    Webservices webservices ;
    common commonObj;
    int pendingCount =0 ;
    int completeCount= 0;
    LinearLayout pendingOrder,totalOrder,completeOrder;

    private final int[] pics = {R.drawable.p1, R.drawable.p2, R.drawable.p3, R.drawable.p4, R.drawable.p5};
    private final SliderAdapter sliderAdapter = new SliderAdapter(pics, 20, new OnCardClickListener());
    private CardSliderLayoutManager layoutManger;
    private RecyclerView recyclerView;
    private int currentPosition;

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
                    startActivity(new Intent(MainActivity.this,NewOrderActivity.class));
                    finish();
                    return true;
                case R.id.navigation_notifications:
                   // mTextMessage.setText("View All");
                    startActivity(new Intent(MainActivity.this,ViewAllActivity.class));
                    finish();
                    return true;

                case R.id.navigation_add_person:
                    // mTextMessage.setText("View All");
                    startActivity(new Intent(MainActivity.this,AddPerson.class));
                    finish();
                    return true;

                case R.id.navigation_view_summary:
                    // mTextMessage.setText("View All");
                    startActivity(new Intent(MainActivity.this,ViewSummary.class));
                    finish();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);

        webservices = new Webservices();
        commonObj = new common();

        initRecyclerView();

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        orderRecyclerView =(RecyclerView)findViewById(R.id.orderRecycleView);
        layoutManager = new LinearLayoutManager(MainActivity.this);
        orderRecyclerView.setLayoutManager(layoutManager);
        orderRecyclerView.setHasFixedSize(true);

        completOrderCount= findViewById(R.id.completOrderCount);
        pendingOrderCount = findViewById(R.id.pendingOrderCount);
        totalOrderCount = findViewById(R.id.totalOrderCount);

        //orderList.add(new OrderModel("1","ST100","24th June","Rohit Mane","10000","Pending","5000","5000","9833411104","lahan",""));
        //orderList.add(new OrderModel("2","ST100","24th June","Rohit Mane","10000","Pending","5000","5000","9833411104","lahan",""));


        pendingOrder =findViewById(R.id.pendingOrder);
        totalOrder = findViewById(R.id.totalOrder);
        completeOrder = findViewById(R.id.completeOrder);

        pendingOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ViewAllActivity.class);
                intent.putExtra("option","pending");
                startActivity(intent);

            }
        });

        totalOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ViewAllActivity.class);
                intent.putExtra("option","total");
                startActivity(intent);
            }
        });

        completeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,ViewAllActivity.class);
                intent.putExtra("option","complete");
                startActivity(intent);
            }
        });

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new getAllOrderAsync().execute();
        }
        else{
            commonObj.showAlertForInetnet(MainActivity.this);
        }

    }

    class getAllOrderAsync extends AsyncTask<Void,Void,Void> {
        String response=null;
        ProgressDialog pd ;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd =new ProgressDialog(MainActivity.this);
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

                        totalOrderCount.setText(jsonArray.length()+"");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject object = jsonArray.getJSONObject(i);
                            String status = object.get("status").toString();
                            if(status.equalsIgnoreCase("Pending")){
                                pendingCount = pendingCount+1;
                            }else {
                                completeCount= completeCount + 1;
                            }

                        }
                        completOrderCount.setText(completeCount+"");
                        pendingOrderCount.setText(pendingCount+"");

                        if(jsonArray.length()<5){
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

                            }

                        }else {
                            for (int i = 0; i < 5; i++) {
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

                                orderList.add(new OrderModel(id, billNo, bill_date, customer_name, total_amount, status, balance_amount, advance_amount, contactNo, type, imgString));

                            }
                        }
                        adapter = new OrderAdapter(MainActivity.this, orderList);
                        adapter.notifyDataSetChanged();
                        orderRecyclerView.setAdapter(adapter);

                    }
                    else{
                        Toast.makeText(MainActivity.this,"No Data Present",Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(MainActivity.this,"Some issue occured",Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setAdapter(sliderAdapter);
        recyclerView.setHasFixedSize(true);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    onActiveCardChange();
                }
            }
        });

        layoutManger = (CardSliderLayoutManager) recyclerView.getLayoutManager();

        new CardSnapHelper().attachToRecyclerView(recyclerView);
    }

    private void onActiveCardChange() {
        final int pos = layoutManger.getActiveCardPosition();
        if (pos == RecyclerView.NO_POSITION || pos == currentPosition) {
            return;
        }

        onActiveCardChange(pos);
    }

    private void onActiveCardChange(int pos) {
        int animH[] = new int[] {R.anim.slide_in_right, R.anim.slide_out_left};
        int animV[] = new int[] {R.anim.slide_in_top, R.anim.slide_out_bottom};

        final boolean left2right = pos < currentPosition;
        if (left2right) {
            animH[0] = R.anim.slide_in_left;
            animH[1] = R.anim.slide_out_right;

            animV[0] = R.anim.slide_in_bottom;
            animV[1] = R.anim.slide_out_top;
        }



        currentPosition = pos;
    }

    private class OnCardClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            final CardSliderLayoutManager lm =  (CardSliderLayoutManager) recyclerView.getLayoutManager();

            if (lm.isSmoothScrolling()) {
                return;
            }

            final int activeCardPosition = lm.getActiveCardPosition();
            if (activeCardPosition == RecyclerView.NO_POSITION) {
                return;
            }

            final int clickedPosition = recyclerView.getChildAdapterPosition(view);
            if (clickedPosition == activeCardPosition) {
//                final Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
//                intent.putExtra(DetailsActivity.BUNDLE_IMAGE_ID, pics[activeCardPosition % pics.length]);
//
//                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//                    startActivity(intent);
//                } else {
//                    final CardView cardView = (CardView) view;
//                    final View sharedView = cardView.getChildAt(cardView.getChildCount() - 1);
//                    final ActivityOptions options = ActivityOptions
//                            .makeSceneTransitionAnimation(MainActivity.this, sharedView, "shared");
//                    startActivity(intent, options.toBundle());
//                }
            } else if (clickedPosition > activeCardPosition) {
                recyclerView.smoothScrollToPosition(clickedPosition);
                onActiveCardChange(clickedPosition);
            }
        }
    }

}
